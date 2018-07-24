package com.dc.appengine.appmaster.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JenkinsIntervalBuilds {
	
	public static final String DAILY = "daily";
	public static final String WEEKLY = "weekly";
	public static final String MONTHLY = "monthly";
	
	private JSONArray builds;
	private final long start;
	private final long end;
	private final String intervalType;
	private long averageDuration;
	private String x;
	
	public JenkinsIntervalBuilds(long start, long end, String intervalType) {
		builds = new JSONArray();
		this.start = start;
		this.end = end;
		this.intervalType = intervalType;
	}
	
	public JSONArray getBuilds() {
		return builds;
	}
	public void setBuilds(JSONArray builds) {
		this.builds = builds;
	}

	public String getIntervalType() {
		return intervalType;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}
	
	public void calculateAverage() {
		if (builds.isEmpty()) {
			averageDuration = 0;
			return;
		}
		long total = 0;
		for (Object object : builds) {
			JSONObject jsonObj = (JSONObject) object;
			total += jsonObj.getLong("duration");
		}
		averageDuration = total / builds.size();
	}

	public long getAverageDuration() {
		return averageDuration;
	}
	
	private void toDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		x = sdf.format(new Date(start));
	}
	
	private void toWeek() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		x = sdf.format(new Date(start)) + "-" + sdf.format(new Date(end));
	}
	
	private void toMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		x = sdf.format(new Date(start)) + "-" + sdf.format(new Date(end));
	}
	
	public String getX() {
		if (intervalType.equals(DAILY)) {
			toDay();
		}
		if (intervalType.equals(WEEKLY)) {
			toWeek();
		}
		if (intervalType.equals(MONTHLY)) {
			toMonth();
		}
		return x;
	}

}
