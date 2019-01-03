package com.dc.appengine.appmaster.git;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.gitlab4j.api.CommitsApi;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Branch;
import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.CommitAction;
import org.gitlab4j.api.models.CommitAction.Action;
import org.gitlab4j.api.models.CommitAction.Encoding;
import org.gitlab4j.api.models.CompareResults;
import org.gitlab4j.api.models.Diff;
import org.gitlab4j.api.models.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.dc.appengine.appmaster.entity.FireflyEntity;
import com.dc.appengine.appmaster.git.CommitFile.Type;
import com.dc.appengine.appmaster.utils.DesUtils;
import com.dc.appengine.appmaster.utils.JschClient;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

@Service
public class GitClient {
	
	private static GitLabApi gitLabApi;
	
	private static final Logger log = LoggerFactory.getLogger(GitClient.class);
	
	private static String fireflyClientHostIp;
	private static String fireflyClientHostUsername;
	private static String fireflyClientHostPassword;
	private static String gitLabUsername;
	private static String gitLabPassword;
	
	@Autowired
	public void setGitLabApi(@Value("${gitLabURL}") String url, @Value("${gitLabToken}") String token,
			@Value("${gitLabUsername}") String username, @Value("${gitLabPassword}") String password) {
		gitLabApi = new GitLabApi(url, token);
		gitLabUsername = username;
		gitLabPassword = password;
	}
	
	@Autowired
	public void setFireflyClientHostIp(@Value("${firefly.host.ip}") String ip) {
		fireflyClientHostIp = ip;
	}
	
	@Autowired
	public void setFireflyClientHostUserName(@Value("${firefly.host.user}") String user) {
		fireflyClientHostUsername = user;
	}
	
	@Autowired
	public void setFireflyClientHostPassword(@Value("${firefly.host.pwd}") String pwd) {
		fireflyClientHostPassword = pwd;
	}
	
	private static String base64(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = 0;
		byte[] buffer = new byte[1024];
		while ((i = inputStream.read(buffer)) != -1) {
			baos.write(buffer, 0, i);
		}
		String str = Base64.getEncoder().encodeToString(baos.toByteArray());
		baos.close();
		inputStream.close();
		return str;
	}
	
	private static String getPinYin(String inputString) {

		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		char[] input = inputString.trim().toCharArray();
		StringBuffer output = new StringBuffer("");

		try {
			for (int i = 0; i < input.length; i++) {
				if (Character.toString(input[i]).matches("[\u4E00-\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
					output.append(temp[0]);
					// output.append(" ");
				} else
					output.append(Character.toString(input[i]));
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return output.toString();
	}
	
	private static List<CommitFile> fireFlyFile2GitLabFile(List<FireflyEntity> files, String project, String branch) throws FileNotFoundException {
		List<CommitFile> commitFiles = new ArrayList<>();
		for (FireflyEntity file : files) {
			CommitFile commitFile = new CommitFile();
			commitFile.setFilePath(file.getFilePath());
			String tempFile = "/tmp/firefly/" + project + "/" + branch + "/" + file.getCsid() + "/" + file.getFilePath();
			switch (file.getType()) {
			case "CREATE":
				commitFile.setType(Type.create);
				commitFile.setInputStream(new FileInputStream(tempFile));
				break;
			case "UPDATE":
				commitFile.setType(Type.update);
				commitFile.setInputStream(new FileInputStream(tempFile));
				break;
			case "DELETE":
				commitFile.setType(Type.delete);
				commitFile.setFilePath(file.getMovefrom());
				break;
			case "MOVE":
				commitFile.setType(Type.move);
				commitFile.setInputStream(new FileInputStream(tempFile));
				commitFile.setPreviousPath(file.getMovefrom());
				break;
			case "ROLLBACK":
				commitFile.setType(Type.update);
				commitFile.setInputStream(new FileInputStream(tempFile));
				break;
			case "MERGE":
				commitFile.setType(Type.update);
				commitFile.setInputStream(new FileInputStream(tempFile));
				break;

			default:
				break;
			}
			commitFiles.add(commitFile);
		}
		return commitFiles;
	}
	
	private static String commit(List<CommitFile> files, String commitMessage, String group, String project, String branch) throws IOException, GitLabApiException {
		List<CommitAction> actions = new ArrayList<>();
		for (CommitFile commitFile : files) {
			CommitAction action = new CommitAction();
			action.setEncoding(Encoding.BASE64);
			action.setFilePath(commitFile.getFilePath());
			actions.add(action);
			switch (commitFile.getType()) {
			case create:
				action.setAction(Action.CREATE);
				action.setContent(base64(commitFile.getInputStream()));
				break;
			case update:
				action.setAction(Action.UPDATE);
				action.setContent(base64(commitFile.getInputStream()));
				break;
			case delete:
				action.setAction(Action.DELETE);
				break;
			case move:
				action.setAction(Action.MOVE);
				action.setContent(base64(commitFile.getInputStream()));
				action.setPreviousPath(commitFile.getPreviousPath());
				break;
			default:
				break;
			}
		}
		Commit commit = gitLabApi.getCommitsApi().createCommit(group + "/" + project, branch, commitMessage, null, null,
				null, actions);
		return commit.getId();
	}
	
	private static boolean remove(File file) {
		if (!file.exists()) {
			return false;
		}
		if (file.isFile()) {
			return file.delete();
		}
		Arrays.asList(file.listFiles()).forEach(GitClient::remove);
		return file.delete();
	}
	
	private static boolean cleanTemp(List<FireflyEntity> files, String project, String branch) {
		String tempFile = "/tmp/firefly/" + project + "/" + branch + "/" + files.get(0).getCsid();
		return remove(new File(tempFile));
	}
	
	public static String commit(List<FireflyEntity> files, String project, String branch) throws FileNotFoundException, IOException, GitLabApiException {
//		Map<String, Object> projectDTO = projectService.getByApplicationName(project);
//		String group = (String) projectDTO.get("gitlab_path");
		String projectPinyin = getPinYin(project);
		String commitMessage = files.get(0).getCrid() + "\n" + files.get(0).getComment();
		String commitId = commit(fireFlyFile2GitLabFile(files, project, branch), commitMessage, gitLabUsername, projectPinyin, branch);
		cleanTemp(files, project, branch);
		return commitId;
	}
	
	public static void createProjectAndBranch(String fireflyWorkspace, String projectName, String branch) throws GitLabApiException, JSchException, IOException {
		projectName = getPinYin(projectName);
		boolean projectExist = existProject(projectName);
		if (!projectExist) {
			createProject(projectName);
		}
		createBranch(projectName, branch);
		initBranch(projectName, branch, fireflyWorkspace);
	}
	
	public static void deleteProjectBranch(String projectName, String branch) throws GitLabApiException {
		projectName = getPinYin(projectName);
		List<Branch> branches = getBranches(projectName);
		if (branches.size() <= 2) {
			deleteProject(projectName);
			return;
		}
		deleteBranch(projectName, branch);
	}
	
	public static String sourceBranch(String projectName, String branch) throws Exception {
		projectName = getPinYin(projectName);
		Project project = gitLabApi.getProjectApi().getProject(gitLabUsername, projectName);
		String separator = "#";
		StringBuilder sb = new StringBuilder();
		sb.append(gitLabUsername).append(separator).append(gitLabPassword).append(separator).append(project.getHttpUrlToRepo()).append(separator).append(branch);
		DesUtils des = new DesUtils();
		return "ENC(" + des.encrypt(sb.toString()) + ")";
	}
	
//	private List<CommitExtended> getCommitsByMessage(String group, String project, String branch, String commitMessage) throws GitLabApiException {
//		Project projectGit = gitLabApi.getProjectApi().getProject(group, project);
//		List<Commit> commits = gitLabApi.getCommitsApi().getCommits(projectGit.getId(), branch, "./");
//		List<CommitExtended> commitsExtended = new ArrayList<>();
//		for (Commit commit : commits) {
//			CommitExtended commitExtended = new CommitExtended(commit);
//			commitsExtended.add(commitExtended);
//			commitExtended.setDiffs(gitLabApi.getCommitsApi().getDiff(projectGit.getId(), commit.getId()));
//		}
//		return commitsExtended;
//	}
//	
//	private Set<String> getFiles(List<CommitExtended> commits) {
//		Set<String> files = new HashSet<>();
//		for (CommitExtended commit : commits) {
//			for (CommittedFile file : commit.getFiles()) {
//				if (file.getType() == FileUpdateType.create || file.getType() == FileUpdateType.update) {
//					files.add(file.getFilePath());
//				}
//			}
//		}
//		return files;
//	}
	
	private static boolean existProject(String projectName) {
		boolean exist = false;
		try {
			gitLabApi.getProjectApi().getProject(gitLabUsername, projectName);
			exist = true;
		} catch (GitLabApiException e) {
			log.error("", e);
		}
		return exist;
	}
	
	private static List<Branch> getBranches(String projectName) throws GitLabApiException {
		Project project = gitLabApi.getProjectApi().getProject(gitLabUsername, projectName);
		return gitLabApi.getRepositoryApi().getBranches(project.getId());
	}
	
	private static void createProject(String projectName) throws GitLabApiException {
		gitLabApi.getProjectApi().createProject(projectName);
	}
	
	private static void deleteProject(String projectName) throws GitLabApiException {
		Project project = gitLabApi.getProjectApi().getProject(gitLabUsername, projectName);
		gitLabApi.getProjectApi().deleteProject(project);
	}
	
	private static void createBranch(String projectName, String branch) throws GitLabApiException {
		Project project = gitLabApi.getProjectApi().getProject(gitLabUsername, projectName);
		gitLabApi.getRepositoryApi().createBranch(project.getId(), branch, "master");
	}
	
	private static void deleteBranch(String projectName, String branch) throws GitLabApiException {
		Project project = gitLabApi.getProjectApi().getProject(gitLabUsername, projectName);
		gitLabApi.getRepositoryApi().deleteBranch(project.getId(), branch);
	}
	
	private static void initBranch(String projectName, String branch, String fireflyWorkspace) throws GitLabApiException, JSchException, IOException {
		Project project = gitLabApi.getProjectApi().getProject(gitLabUsername, projectName);
		String anywayConnector = " ; ";
		String andConnector = " && ";
		String gitWorkspaceParent = "/tmp/" + projectName + "/" + branch;
		String gitWorkspace = gitWorkspaceParent + "/" + projectName;
		String mkdir = "mkdir -p " + gitWorkspaceParent;
		String cd = "cd " + gitWorkspaceParent;
		String clone = "git clone " + project.getSshUrlToRepo() + " -b " + branch;
		String cp = "\\cp -r " + fireflyWorkspace + "/* " + gitWorkspace;
		String add = "cd " + gitWorkspace + " && git add .";
		String commit = "git commit -m 'init'";
		String push = "git push origin " + branch;
		String rm = "cd ~ && rm -rf " + gitWorkspaceParent;
		String command = mkdir + anywayConnector
				+ cd + andConnector
				+ clone + andConnector
				+ cp + andConnector
				+ add + andConnector
				+ commit + andConnector
				+ push + andConnector
				+ rm;
		Session session = JschClient.client.connect(fireflyClientHostIp, fireflyClientHostUsername, fireflyClientHostPassword);
		String result = JschClient.client.exec(session, command);
		log.info("git init log:\n{}", result);
	}
	
	/**
	 * 
	 * @param projectName 项目名，git 或firefly上的项目名
	 * @param from   比较的commitid 或者branch  
	 * @param to
	 * @return
	 * @throws GitLabApiException
	 */
	public  static List<Diff> compare(String projectName, String from, String to) throws GitLabApiException {
		projectName = getPinYin(projectName);
		CompareResults compareResults = gitLabApi.getRepositoryApi().compare(projectName, from, to);
		
		return compareResults.getDiffs();
	}
	public static List<Diff> compareVersion(String projectName,String branchName,String[] fromTasks,String[] toTasks) throws GitLabApiException {
		List<Diff> list = new ArrayList<>();
		String baseVersionCommit = "ec3e680f1be1ca94a0acefdb123a6f922f992c88";
		projectName = getPinYin(projectName);
		Map<String, Map<String, Object>> fromFiles = GitClient.extractByTask(fromTasks, projectName,branchName);
		Map<String, Map<String, Object>> toFiles = GitClient.extractByTask(toTasks, projectName,branchName);
		
		Set<String> set = new HashSet<>();
		set.addAll(fromFiles.keySet());
		set.addAll(toFiles.keySet());
		
		for (String file : set) {
			String fromCommit=null,toCommit=null;
			if(fromFiles.get(file) == null) {
				fromCommit = baseVersionCommit;
			}else {
				fromCommit = (String) fromFiles.get(file).get("commitId");
			}
			
			if(toFiles.get(file) == null) {
				toCommit = baseVersionCommit;
			}else {
				toCommit = (String) toFiles.get(file).get("commitId");
			}
			
			CompareResults compare = gitLabApi.getRepositoryApi().compare(gitLabApi.getProjectApi().getProject("root", projectName).getId(), fromCommit, toCommit);
			Optional<Diff> optional = compare.getDiffs().stream().filter((a)-> a.getOldPath().equals(file) ).findFirst();
			if(optional.isPresent()) {
				list.add(optional.get());
			}
		}
		return list;
	}
	
	public static Map<String, Map<String, Object>> extractByTask(String[] tasks,String projectName,String branchName) throws GitLabApiException {
		CommitsApi commitsApi=gitLabApi.getCommitsApi();
		projectName = getPinYin(projectName);
		Project project = gitLabApi.getProjectApi().getProject("root", projectName);
		List<Commit> list = commitsApi.getCommits(project.getId(),branchName,null);
//				.getCommits(project.getId());
		list = list.stream().filter((cm) -> {
			boolean b = false;
			for (int i = 0; i < tasks.length; i++) {
				if(cm.getMessage().contains(tasks[i])) {
					b = true;
					break;
				}
			}
			return b;
		}).collect(Collectors.toList());
		
		Map<String, Map<String, Object>> files = new HashMap<>();
		for (int i = 0; i < list.size(); i++) {
			Commit commit = list.get(i);
			String commitid = commit.getId();
			Date committedDate = commit.getCommittedDate();
			List<Diff> diffs = gitLabApi.getCommitsApi().getDiff(project.getId(), commit.getId());
			
			for (Diff diff : diffs) {
				Map<String, Object> map = new HashMap<>();
				if(files.get(diff.getOldPath()) == null) {
					map.put("commitId", commitid);
					map.put("commitDate", committedDate);
					map.put("file", diff.getOldPath());
				}else {
					map =  files.get(diff.getOldPath());
					Date date = (Date) map.get("commitDate");
					if(date.before(committedDate)) {
						map.put("commitId", commitid);
						map.put("commitDate", committedDate);
					}
				}
				files.put(diff.getOldPath(), map);
			}
		}
		return files;
	}
}
