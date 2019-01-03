package com.dc.appengine.appmaster.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.StopWalkException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.CompareResults;
import org.gitlab4j.api.models.Diff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.appmaster.dao.impl.ResourceDao;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.Version;
import com.dc.appengine.appmaster.git.GitClient;
import com.dc.appengine.appmaster.utils.GitUtil4Diff;

@Service("compareService")
public class CompareService {
	private static final Logger log = LoggerFactory.getLogger(CompareService.class); 
	
	@Autowired
	@Qualifier("resourceDao")
	private ResourceDao resourceDao;
	@Resource
	private ProjectService projectService;
	
	public String compareVersion(String id,String[] compareIds) throws GitLabApiException {
		Map<String, Object> resourceDetail = resourceDao.getResourceDetailById(id);
		Map<String, Object> map = new HashMap<>();
		map.put("project_id", resourceDetail.get("PROJECT_ID"));
		Page codePage = projectService.codePage(map, 1, 100);
		Map<String, Object> codeMap = (Map<String, Object>) codePage.getRows().get(0);
		String projectName = codeMap.get("firefly_project").toString();
		String branchName = codeMap.get("firefly_project_branch").toString();
		
		List<Map<String,Object>> versions = resourceDao.getVersionDetailByVersionIdS(Arrays.asList(compareIds));
//		List<String> list0 = JSON.parseArray(versions.get(0).get("VERSION_DESC").toString(),String.class);
//		String[] fromTasks = list0.toArray(new String[list0.size()]);
		String[] fromTasks = {versions.get(0).get("VERSION_DESC").toString()};
//		List<String> list1 = JSON.parseArray(versions.get(1).get("VERSION_DESC").toString(),String.class);
//		String[] toTasks = list1.toArray(new String[list1.size()]);
		String[] toTasks = {versions.get(1).get("VERSION_DESC").toString()};
		
		List<Diff> versionList = GitClient.compareByTask(fromTasks, toTasks, projectName, branchName);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < versionList.size(); i++) {
			Diff diff = versionList.get(i);
			sb.append(diff.getOldPath() + "\n");
			sb.append(diff.getDiff());
		}
		Map<String, Object> result = new HashMap<>();
		result.put("result", true);
		result.put("out", sb.toString());
		return JSON.toJSONString(result);
	}

	public String compareBranch(String projectName,String fromBranch,String toBranch) throws GitLabApiException {
		List<Diff> list = GitClient.compare(projectName, fromBranch, toBranch);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			Diff diff = list.get(i);
			sb.append(diff.getDiff());
		}
		return JSON.toJSONString(sb);
	}
	
/*	public List<String> getFilesBelong2Tasks(String remotePath,String userName,String password,String localPath,String branch){
		GitUtil4Diff git = new GitUtil4Diff(remotePath, userName, password, localPath, branch);
		String[] tasks = new String[]{};
		try {
			git.commitLogSearch(tasks);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<DiffEntry> diffList = git.getDiffList();
		
		return null;
		
	}*/
/*	public void cmdDiff() {
		boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
		
		List<String> list = new ArrayList<>();
		if (isWin) {
			list.add("cmd");
			list.add("/c");
		} else {
			list.add("sh");
			list.add("-c");
		}
		//  命令执行的工作区    git命令path  commit di  file
		list.add("cd D:\\project\\SmartCD\\SmartCD\\SmartCD & d: & D:\\app\\Git\\bin\\git.exe diff 58c76fe5800981408617403357253186050108f9 1d0efe640e3bd0e98928c03ac44117e512e7165e ./AppMaster/src/main/java/com/dc/appengine/appmaster");
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(list.toArray(new String[0]));
			InputStream is = process.getInputStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(is));
			String line = bf.readLine();
			while(line != null) {
				System.out.println(line);
				line = bf.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	public static void main(String[] args){
	
	}
	
}
