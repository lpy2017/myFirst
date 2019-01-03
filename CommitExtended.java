package com.dc.appengine.appmaster.git;

import java.util.ArrayList;
import java.util.List;

import org.gitlab4j.api.models.Commit;
import org.gitlab4j.api.models.Diff;

public class CommitExtended {
	
	private Commit commit;
	private List<Diff> diffs;
	private List<CommittedFile> files;
	
	public enum FileUpdateType {
		create, update, delete;
	}
	
	public CommitExtended(Commit commit) {
		this.commit = commit;
		this.diffs = new ArrayList<>();
		this.files = new ArrayList<>();
	}
	
	public class CommittedFile {
		private FileUpdateType type;
		private String filePath;
		
		public CommittedFile(String filePath) {
			this.filePath = filePath;
		}
		
		public FileUpdateType getType() {
			return type;
		}
		public void setType(FileUpdateType type) {
			this.type = type;
		}
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		
	}

	public Commit getCommit() {
		return commit;
	}

	public void setCommit(Commit commit) {
		this.commit = commit;
	}

	public List<Diff> getDiffs() {
		return diffs;
	}

	public void setDiffs(List<Diff> diffs) {
		this.diffs = diffs;
		for (Diff diff : diffs) {
			CommittedFile file = new CommittedFile(diff.getNewPath());
			file.setType(FileUpdateType.update);
			if (diff.getNewFile()) {
				file.setType(FileUpdateType.create);
			}
			if (diff.getDeletedFile()) {
				file.setType(FileUpdateType.delete);
			}
			this.files.add(file);
		}
	}

	public List<CommittedFile> getFiles() {
		return files;
	}

	public void setFiles(List<CommittedFile> files) {
		this.files = files;
	}

}
