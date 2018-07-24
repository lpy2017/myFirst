package com.dc.appengine.appsvn.upload;

import java.util.Map;

public interface MUploadWebService {
	
	public final static int MAX_FILE_NAME_LEN = 100; 

	public String checkFile(String fileName, String userId);
	
	public Map<String, String> checkFileUploadInfo(String fileName, String userId);
	
	public String writeFile(String fileName, String localFileName, String remoteFileName, byte[] filestrem, String fileLength, String userInfo);
	
	public String copyFile(String fileName, String userId);
	
}
