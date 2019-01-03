package com.dc.appengine.appmaster.git;

import java.io.InputStream;

public class CommitFile {

	public enum Type {
		create, update, delete, move;
	}

	private String filePath;
	private String previousPath;
	private InputStream inputStream;
	private Type type;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getPreviousPath() {
		return previousPath;
	}

	public void setPreviousPath(String previousPath) {
		this.previousPath = previousPath;
	}
}
