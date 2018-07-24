package com.dc.appengine.appmaster.service.impl;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.entity.JenkinsIntervalBuilds;

@Service
public class JenkinsService {

	private String jenkinsUrl;
	private String userName;
	private String userPassword;

	private static final Logger log = LoggerFactory.getLogger(JenkinsService.class);

	public JenkinsService(@Value("${jenkins.url}") String jenkinsUrl, @Value("${jenkins.userName}") String userName,
			@Value("${jenkins.userPassword}") String userPassword) {
		this.jenkinsUrl = jenkinsUrl;
		this.userName = userName;
		this.userPassword = userPassword;
	}

	private String getAuth() {
		return Base64.getEncoder().encodeToString((this.userName + ":" + this.userPassword).getBytes());
	}

	private JSONObject crumbIssuer() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(jenkinsUrl).path("crumbIssuer").path("api").path("json");
		return target.request().header("Authorization", "Basic " + getAuth()).get(JSONObject.class);
	}
	
	private List<String> allJobs() {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(jenkinsUrl).path("api").path("json");
		List<String> jobs = new ArrayList<>();
		try {
			JSONObject jsonObj = target.queryParam("tree", "jobs[name]").request()
					.header("Authorization", "Basic " + getAuth()).get(JSONObject.class);
			JSONArray jsonArray = jsonObj.getJSONArray("jobs");
			for (Object object : jsonArray) {
				JSONObject job = (JSONObject) object;
				jobs.add(job.getString("name"));
			}
			return jobs;
		} catch (Throwable e) {
			log.error("", e);
		}
		return jobs;
	}

	private JSONArray allBuilds(String jobName) {
		if (!allJobs().contains(jobName)) {
			return new JSONArray();
		}
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(jenkinsUrl).path("job").path(jobName).path("api").path("json");
		try {
			JSONObject jsonObj = target.queryParam("tree", "allBuilds[number,building,result,duration,timestamp]").request()
					.header("Authorization", "Basic " + getAuth()).get(JSONObject.class);
			return jsonObj.getJSONArray("allBuilds");
		} catch (Throwable e) {
			log.error("", e);
			return new JSONArray();
		}
	}

	private JSONArray finishedBuilds(JSONArray builds) {
		List<Object> building = new ArrayList<>();
		for (Object build : builds) {
			JSONObject jsonObj = (JSONObject) build;
			if (jsonObj.getBoolean("building")) {
				building.add(build);
			}
		}
		for (Object object : building) {
			builds.remove(object);
		}
		return builds;
	}

	private String averageDuration(JSONArray builds) {
		if (builds.size() == 0) {
			return timeFormat(0);
		}
		finishedBuilds(builds);
		long all = 0;
		for (Object build : builds) {
			JSONObject jsonObj = (JSONObject) build;
			all += jsonObj.getLong("duration");
		}
		return timeFormat(all / builds.size());
	}

	private String timeFormat(long ms) {
		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		long hour = ms / hh;
		long minute = (ms - hour * hh) / mi;
		long second = (ms - hour * hh - minute * mi) / ss;
		long milliSecond = ms - hour * hh - minute * mi - second * ss;
		String strMinute = minute < 10 ? "0" + minute : "" + minute;
		String strSecond = second < 10 ? "0" + second : "" + second;
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : "" + milliSecond;
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : "" + strMilliSecond;
		return hour + "小时" + strMinute + "分" + strSecond + "秒" + strMilliSecond + "毫秒";
	}

	private String numberFormat(double number) {
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(2);
		return nf.format(number);
	}

	private String successRate(JSONArray builds) {
		if (builds.size() == 0) {
			return numberFormat(0);
		}
		double count = 0;
		for (Object object : builds) {
			JSONObject jsonObj = (JSONObject) object;
			if ("SUCCESS".equals(jsonObj.getString("result"))) {
				count++;
			}
		}
		return numberFormat(count / builds.size());
	}

	private String failureRate(JSONArray builds) {
		if (builds.size() == 0) {
			return numberFormat(0);
		}
		double count = 0;
		for (Object object : builds) {
			JSONObject jsonObj = (JSONObject) object;
			if ("FAILURE".equals(jsonObj.getString("result"))) {
				count++;
			}
		}
		return numberFormat(count / builds.size());
	}

	private JSONArray intervalCount(JSONArray builds, long start, long end) {
		JSONArray intervalBuilds = new JSONArray();
		for (Object object : builds) {
			JSONObject jsonObj = (JSONObject) object;
			if (jsonObj.getLong("timestamp") < start) {
				break;
			}
			if (jsonObj.getLong("timestamp") >= start && jsonObj.getLong("timestamp") < end) {
				intervalBuilds.add(object);
			}
		}
		return intervalBuilds;
	}

	private JSONArray intervalSuccessCount(JSONArray builds, long start, long end) {
		JSONArray intervalSuccessBuilds = new JSONArray();
		for (Object object : builds) {
			JSONObject jsonObj = (JSONObject) object;
			if (jsonObj.getLong("timestamp") < start) {
				break;
			}
			if ("SUCCESS".equals(jsonObj.getString("result")) && jsonObj.getLong("timestamp") >= start
					&& jsonObj.getLong("timestamp") < end) {
				intervalSuccessBuilds.add(object);
			}
		}
		return intervalSuccessBuilds;
	}

	private long intervalAverageDuration(JSONArray builds, long start, long end) {
		JSONArray intervalBuilds = new JSONArray();
		for (Object object : builds) {
			JSONObject jsonObj = (JSONObject) object;
			if (jsonObj.getLong("timestamp") < start) {
				break;
			}
			if (!jsonObj.getBoolean("building") && jsonObj.getLong("timestamp") >= start
					&& jsonObj.getLong("timestamp") < end) {
				intervalBuilds.add(object);
			}
		}
		if (intervalBuilds.isEmpty()) {
			return 0;
		}
		long total = 0;
		for (Object object : intervalBuilds) {
			JSONObject jsonObj = (JSONObject) object;
			total += jsonObj.getLong("duration");
		}
		return total / intervalBuilds.size();
	}

	private List<JenkinsIntervalBuilds> count(List<JenkinsIntervalBuilds> builds, String jobName) {
		JSONArray allBuilds = allBuilds(jobName);
		for (JenkinsIntervalBuilds build : builds) {
			build.setBuilds(intervalCount(allBuilds, build.getStart(), build.getEnd()));
		}
		return builds;
	}

	private List<JenkinsIntervalBuilds> successCount(List<JenkinsIntervalBuilds> builds, String jobName) {
		JSONArray allBuilds = allBuilds(jobName);
		for (JenkinsIntervalBuilds build : builds) {
			build.setBuilds(intervalSuccessCount(allBuilds, build.getStart(), build.getEnd()));
		}
		return builds;
	}

	private List<JenkinsIntervalBuilds> averageDuration(List<JenkinsIntervalBuilds> builds, String jobName) {
		JSONArray allBuilds = allBuilds(jobName);
		for (JenkinsIntervalBuilds build : builds) {
			build.setBuilds(intervalCount(allBuilds, build.getStart(), build.getEnd()));
			build.calculateAverage();
		}
		return builds;
	}

	private List<Long> countResult(List<JenkinsIntervalBuilds> builds, String jobName) {
		count(builds, jobName);
		List<Long> result = new ArrayList<>();
		for (JenkinsIntervalBuilds build : builds) {
			result.add(Long.valueOf(build.getBuilds().size()));
		}
		return result;
	}

	private List<Long> successCountResult(List<JenkinsIntervalBuilds> builds, String jobName) {
		successCount(builds, jobName);
		List<Long> result = new ArrayList<>();
		for (JenkinsIntervalBuilds build : builds) {
			result.add(Long.valueOf(build.getBuilds().size()));
		}
		return result;
	}

	private List<Long> averageDurationResult(List<JenkinsIntervalBuilds> builds, String jobName) {
		averageDuration(builds, jobName);
		List<Long> result = new ArrayList<>();
		for (JenkinsIntervalBuilds build : builds) {
			result.add(build.getAverageDuration());
		}
		return result;
	}
	
	public Map<String, Long> duration(long start, long end, String jobName) {
		JSONArray allBuilds = allBuilds(jobName);
		Map<String, Long> result = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
		for (Object build : allBuilds) {
			JSONObject jsonObj = (JSONObject) build;
			if (jsonObj.getLong("timestamp") < start) {
				break;
			}
			if (!jsonObj.getBoolean("building") && jsonObj.getLong("timestamp") >= start && jsonObj.getLong("timestamp") < end) {
				result.put(sdf.format(new Date(jsonObj.getLong("timestamp"))), jsonObj.getLong("duration"));
			}
		}
		return result;
	}
	
	public List<String> x(long start, long end, String intervalType) {
		List<String> x = new ArrayList<>();
		for (JenkinsIntervalBuilds build : getX(start, end, intervalType)) {
			x.add(build.getX());
		}
		return x;
	}
	
	private List<JenkinsIntervalBuilds> getX(long start, long end, String intervalType) {
		if ("month".equals(intervalType)) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(start);
			List<JenkinsIntervalBuilds> list = new ArrayList<>();
			while (true) {
				if (calendar.getTimeInMillis() >= end) {
					break;
				}
				long intervalStart = calendar.getTimeInMillis();
				calendar.add(Calendar.DATE, 1);
				long intervalEnd = calendar.getTimeInMillis();
				JenkinsIntervalBuilds builds = new JenkinsIntervalBuilds(intervalStart, intervalEnd, JenkinsIntervalBuilds.DAILY);
				list.add(builds);
			}
			return list;
		}
		if ("quarter".equals(intervalType)) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(start);
			calendar.add(Calendar.DATE, 1 - calendar.get(Calendar.DAY_OF_WEEK));
			start = calendar.getTimeInMillis();
			calendar.setTimeInMillis(end);
			calendar.add(Calendar.DATE, -1);
			calendar.add(Calendar.DATE, 7 - calendar.get(Calendar.DAY_OF_WEEK));
			calendar.add(Calendar.DATE, 1);
			end = calendar.getTimeInMillis();
			calendar.setTimeInMillis(start);
			List<JenkinsIntervalBuilds> list = new ArrayList<>();
			while (true) {
				if (calendar.getTimeInMillis() >= end) {
					break;
				}
				long intervalStart = calendar.getTimeInMillis();
				calendar.add(Calendar.WEEK_OF_YEAR, 1);
				long intervalEnd = calendar.getTimeInMillis();
				JenkinsIntervalBuilds builds = new JenkinsIntervalBuilds(intervalStart, intervalEnd, JenkinsIntervalBuilds.WEEKLY);
				list.add(builds);
			}
			return list;
		}
		if ("year".equals(intervalType)) {
			Calendar calendar = new GregorianCalendar();
			calendar.setTimeInMillis(start);
			List<JenkinsIntervalBuilds> list = new ArrayList<>();
			while (true) {
				if (calendar.getTimeInMillis() >= end) {
					break;
				}
				long intervalStart = calendar.getTimeInMillis();
				calendar.add(Calendar.MONTH, 1);
				long intervalEnd = calendar.getTimeInMillis();
				JenkinsIntervalBuilds builds = new JenkinsIntervalBuilds(intervalStart, intervalEnd, JenkinsIntervalBuilds.MONTHLY);
				list.add(builds);
			}
			return list;
		}
		return Collections.emptyList();
	}
	
	public List<Long> count(long start, long end, String jobName, String intervalType) {
		return countResult(getX(start, end, intervalType), jobName);
	}
	
	public List<Long> successCount(long start, long end, String jobName, String intervalType) {
		return successCountResult(getX(start, end, intervalType), jobName);
	}
	
	public List<Long> averageDuration(long start, long end, String jobName, String intervalType) {
		return averageDurationResult(getX(start, end, intervalType), jobName);
	}

	public static void main(String[] args) {
		// JenkinsService service = new
		// JenkinsService("http://9.1.176.13:8080/jenkins", "admin", "123456");
		JenkinsService service = new JenkinsService("http://10.126.3.222:8080", "guojwe", "guojwe");
		// JSONArray all = service.allBuilds("cd");
		// log.info("构建次数：{}", all.size());
		// log.info("平均构建时长：{}", service.averageDuration(all));
		// log.info("成功率：{}", service.successRate(all));
		// log.info("失败率：{}", service.failureRate(all));
		
//		List<JenkinsIntervalBuilds> list = new ArrayList<>();
//		for (int i = 1; i < 28; i++) {
//			Calendar calendar = new GregorianCalendar(2018, 5, i, 0, 0, 0);
//			long start = calendar.getTimeInMillis();
//			calendar.add(Calendar.DAY_OF_MONTH, 1);
//			long end = calendar.getTimeInMillis();
//			JenkinsIntervalBuilds builds = new JenkinsIntervalBuilds(start, end, JenkinsIntervalBuilds.DAILY);
//			list.add(builds);
//		}
//		Calendar start = new GregorianCalendar(2018, 5, 1, 0, 0, 0);
//		Calendar end = new GregorianCalendar(2018, 5, 27, 0, 0, 0);
//		log.info("构建次数：{}", JSON.toJSONString(service.countResult(list, "cd")));
//		log.info("构建成功次数：{}", JSON.toJSONString(service.successCountResult(list, "cd")));
//		log.info("构建平均时长：{}", JSON.toJSONString(service.averageDurationResult(list, "cd")));
//		log.info("构建实际时长：{}", JSON.toJSONString(service.duration(start.getTimeInMillis(), end.getTimeInMillis(), "cd")));
		
		Calendar calendar = new GregorianCalendar(2018, 0, 1, 0, 0, 0);
		long start = calendar.getTimeInMillis();
		calendar.add(Calendar.YEAR, 1);
		long end = calendar.getTimeInMillis();
		log.info("年构建次数：{}", JSON.toJSONString(service.count(start, end, "cd", "year")));
		log.info("年构建成功次数：{}", JSON.toJSONString(service.successCount(start, end, "cd", "year")));
		log.info("年构建时长平均值：{}", JSON.toJSONString(service.averageDuration(start, end, "cd", "year")));
		log.info("年构建时长：{}", JSON.toJSONString(service.duration(start, end, "cd")));
		
		calendar = new GregorianCalendar(2018, 3, 1, 0, 0, 0);
		start = calendar.getTimeInMillis();
		calendar.add(Calendar.MONTH, 3);
		end = calendar.getTimeInMillis();
		log.info("季度构建次数：{}", JSON.toJSONString(service.count(start, end, "cd", "quarter")));
		log.info("季度构建成功次数：{}", JSON.toJSONString(service.successCount(start, end, "cd", "quarter")));
		log.info("季度构建时长平均值：{}", JSON.toJSONString(service.averageDuration(start, end, "cd", "quarter")));
		log.info("季度构建时长：{}", JSON.toJSONString(service.duration(start, end, "cd")));
		
		calendar = new GregorianCalendar(2018, 5, 1, 0, 0, 0);
		start = calendar.getTimeInMillis();
		calendar.add(Calendar.MONTH, 1);
		end = calendar.getTimeInMillis();
		log.info("月构建次数：{}", JSON.toJSONString(service.count(start, end, "cd", "month")));
		log.info("月构建成功次数：{}", JSON.toJSONString(service.successCount(start, end, "cd", "month")));
		log.info("月构建时长平均值：{}", JSON.toJSONString(service.averageDuration(start, end, "cd", "month")));
		log.info("月构建时长：{}", JSON.toJSONString(service.duration(start, end, "cd")));
	}

}
