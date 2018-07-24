package com.dc.appengine.appmaster.entity;

import java.util.Date;
import java.util.List;

public class ReleaseBus {
	private int id;
	private String name;
	private Date startTime;
	private Date stopTime;
	private String author;
	private List<ReleaseTask> tasks;

	public ReleaseBus() {

	}

	public ReleaseBus(String name, Date startTime, Date stopTime, String author) {
		this.name = name;
		this.startTime = startTime;
		this.stopTime = stopTime;
		this.author = author;
	}

	public ReleaseBus(int id, String name, Date startTime, Date stopTime, String author) {
		this.id = id;
		this.name = name;
		this.startTime = startTime;
		this.stopTime = stopTime;
		this.author = author;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public List<ReleaseTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<ReleaseTask> tasks) {
		this.tasks = tasks;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
