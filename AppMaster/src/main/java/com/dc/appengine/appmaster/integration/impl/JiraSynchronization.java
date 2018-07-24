package com.dc.appengine.appmaster.integration.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.BasicProject;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;
import com.dc.appengine.appmaster.entity.ReleaseTaskBean;
import com.dc.appengine.appmaster.integration.interfaces.IIntegrationSynchronization;

public class JiraSynchronization implements IIntegrationSynchronization {

	private static final Logger log = LoggerFactory.getLogger(JiraSynchronization.class);
	private transient static JiraSynchronization instance;
	private transient static Map<String, JiraRestClient> jiraClientMap;
	private static final Map<String, String> typeMap = new HashMap<>();
	private static final Map<String, String> levelMap = new HashMap<>();
	private static final Map<String, String> statusMap = new HashMap<>();
	private static final String JIRA_TYPE = "03";

	static {
		typeMap.put("新功能", "01");
		typeMap.put("故障", "02");
		typeMap.put("改进", "03");
		typeMap.put("任务", "04");
		typeMap.put("Epic", "05");
		typeMap.put("故事", "05");

		levelMap.put("Highest", "01");
		levelMap.put("High", "02");
		levelMap.put("Medium", "03");
		levelMap.put("Low", "04");
		levelMap.put("Lowest", "05");

		statusMap.put("待办", "01");
		statusMap.put("无分类", "01");
		statusMap.put("开放", "01");
		statusMap.put("处理中", "03");
		statusMap.put("完成", "04");
		statusMap.put("已解决", "04");
		statusMap.put("已关闭", "05");
		statusMap.put("重新打开", "06");
	}

	private JiraSynchronization() {
		jiraClientMap = new HashMap<>();
	}

	public static JiraSynchronization getInstance() {
		if (instance == null) {
			synchronized (JiraSynchronization.class) {
				if (instance == null) {
					instance = new JiraSynchronization();
				}
			}
		}
		return instance;
	}

	private JiraRestClient getClient(String url, String user, String password) throws URISyntaxException {
		String key = url + user + password;
		if (jiraClientMap.get(key) == null) {
			synchronized (JiraSynchronization.class) {
				if (jiraClientMap.get(key) == null) {
					AsynchronousJiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
					URI jiraServerUri = new URI(url);
					JiraRestClient restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, user,
							password);
					jiraClientMap.put(key, restClient);
				}
			}
		}
		return jiraClientMap.get(key);
	}

	@Override
	public List<ReleaseTaskBean> synchronize(String url, String user, String password, String projectName)
			throws Exception {
		JiraRestClient client = getClient(url, user, password);
		boolean projectExist = false;
		Promise<Iterable<BasicProject>> projects = client.getProjectClient().getAllProjects();
		Iterable<BasicProject> pIterator = projects.get();
		for (BasicProject project : pIterator) {
			String name = project.getName();
			if (projectName.equals(name)) {
				projectExist = true;
				break;
			}
		}
		if (!projectExist) {
			throw new Exception("project[" + projectName + "]在url[" + url + "]不存在，请检查目标jira或项目名称是否错误！");
		}
		String jql = "project=" + projectName;
		Iterable<BasicIssue> issues = client.getSearchClient().searchJql(jql).claim().getIssues();
		// List<E> list = (List<E>) Lists.newArrayList(issues);
		// IterableUtils.toList(issues);
		List<Issue> list = new ArrayList<Issue>();
		for (BasicIssue item : issues) {
			Promise<Issue> issue = client.getIssueClient().getIssue(item.getKey());
			list.add(issue.get());
		}
		return loadJiraIssues(list);
	}

	@Override
	public boolean testConnection(String url, String user, String password) {
		try {
			JiraRestClient client = getClient(url, user, password);
			client.getSessionClient().getCurrentSession().get();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
		return true;
	}

	@Override
	public void callbackStatus(String url, String user, String password, boolean status, String issueKey)
			throws Exception {
		try {
			JiraRestClient client = getClient(url, user, password);
			Issue issue = client.getIssueClient().getIssue(issueKey).claim();
			if (issue != null) {
				TransitionInput transitionInput = null;
				// 1-open开放 3-in progress正在处理 4-resolved已解决 5-reopened重新打开
				// 6-closed已关闭
				if (status) {
					transitionInput = new TransitionInput(4);
				} else {
					transitionInput = new TransitionInput(1);
				}
				client.getIssueClient().transition(issue, transitionInput);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception("回调jira[" + url + "]任务[" + issueKey + "]失败! " + e.getMessage());
		}
	}

	private List<ReleaseTaskBean> loadJiraIssues(List<Issue> list) throws ParseException, JSONException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<ReleaseTaskBean> issues = new ArrayList<>();
		for (Issue item : list) {
			StringBuffer sbLabel = new StringBuffer();
			for (String label : item.getLabels()) {
				sbLabel.append(label);
			}
			Date updateTime = (item.getUpdateDate() == null) ? null
					: format.parse(item.getUpdateDate().toString("yyyy-MM-dd HH:mm:ss"));
			Date dueTime = (item.getDueDate() == null) ? null
					: format.parse(item.getDueDate().toString("yyyy-MM-dd HH:mm:ss"));
			// String releasePhaseId =
			// ((JSONObject)item.getField("releasePhaseId").getValue()).getString("id");
			String releasePhaseId = "";
			ReleaseTaskBean task = new ReleaseTaskBean(item.getKey(), typeMap.get(item.getIssueType().getName()),
					levelMap.get(item.getPriority().getName()), statusMap.get(item.getStatus().getName()),
					sbLabel.toString(), item.getProject().getName(), releasePhaseId,
					((JSONObject) item.getField("creator").getValue()).getString("displayName"),
					item.getReporter().getName(), format.parse(item.getCreationDate().toString("yyyy-MM-dd HH:mm:ss")),
					updateTime, dueTime, item.getDescription(), item.getSummary(), JIRA_TYPE);
			issues.add(task);
		}
		return issues;
	}

}
