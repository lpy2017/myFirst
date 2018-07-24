package com.dc.appengine.node.configuration.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class NodeProperties {
	public static String momUrl;
	public static String shellExecutor;
	// redis
	public static int redisMaxTotal;
	public static int redisMaxIdle;
	public static int maxWaitMillis;
	public static String redisIp;
	public static int redisPort;
	public static String redisAuth;
	public static int redisTimeOut;
	public static int redisDataTimeOut;
	public static boolean testOnBorrow;
	public static boolean testOnReturn;

	public static String hostBasePath;
	public static String sshAuthMethod;
	public static String keyLocation;
	public static String hostUser;
	public static String hostPassword;

	public static String ftpUrl;
	public static String ftpUser;
	public static String ftpPwd;
	public static int ftpPort;
	public static String ftpUploadDir;
	public static String ftpTmpDir;
	public static int ftpTimeOut;
	public static String uploadMode;
	
	public static String masterRest;
	public static String frameRest;
	public static String nodeIp;
	
	public static String sender;
	public static String password;
	public static String smtpHost;
	public static String smtpPort;


	public NodeProperties(
			@Value("${shellExecutor}") String shellExecutor,
			@Value("${momUrl}") String momUrl,
			// ssh
			@Value("${hostBasePath}") String hostBasePath,
			@Value("${sshAuthMethod}") String sshAuthMethod,
			@Value("${keyLocation}") String keyLocation,
			@Value("${hostUser}") String hostUser,
			@Value("${hostPassword}") String hostPassword,
			// redis
			@Value("${redisMaxTotal}") int redisMaxTotal,
			@Value("${redisMaxIdle}") int redisMaxIdle,
			@Value("${maxWaitMillis}") int maxWaitMillis,
			@Value("${redisIp}") String redisIp,
			@Value("${redisPort}") int redisPort,
			@Value("${redisTimeOut}") int redisTimeOut,
			@Value("${redisDataTimeOut}") int redisDataTimeOut,
			@Value("${redisAuth}") String redisAuth,
			@Value("${testOnBorrow}") boolean testOnBorrow, 
			@Value("${testOnReturn}") boolean testOnReturn,
			// plugin
			@Value("${ftp.url}") String ftpUrl,
			@Value("${ftp.user}") String ftpUser,
			@Value("${ftp.pwd}") String ftpPwd,
			@Value("${ftp.port}") int ftpPort,
			@Value("${ftp.uploadDir}") String ftpUploadDir,
			@Value("${ftp.tmpDir}") String ftpTmpDir,
			@Value("${ftp.timeOut}") int ftpTimeOut,
			@Value("${upload.mode}") String uploadMode,
			
			@Value("${masterRest}") String masterRest,
			@Value("${frameRest}") String frameRest,
			@Value("${node.ip}") String nodeIp,
			
			@Value("${sender}") String sender,
			@Value("${password}") String password,
			@Value("${smtpHost}") String smtpHost,
			@Value("${smtpPort}") String smtpPort) {
		
		NodeProperties.momUrl = momUrl;
		NodeProperties.shellExecutor = shellExecutor;
		// redis
		NodeProperties.redisMaxTotal = redisMaxTotal;
		NodeProperties.redisMaxIdle = redisMaxIdle;
		NodeProperties.maxWaitMillis = maxWaitMillis;
		NodeProperties.redisIp = redisIp;
		NodeProperties.redisPort = redisPort;
		NodeProperties.redisTimeOut = redisTimeOut;
		NodeProperties.redisDataTimeOut = redisDataTimeOut;
		NodeProperties.redisAuth = redisAuth;
		NodeProperties.testOnBorrow = testOnBorrow;
		NodeProperties.testOnReturn = testOnReturn;

		NodeProperties.ftpUrl = ftpUrl;
		NodeProperties.ftpUser = ftpUser;
		NodeProperties.ftpPwd = ftpPwd;
		NodeProperties.ftpPort = ftpPort;
		NodeProperties.ftpUploadDir = ftpUploadDir;
		NodeProperties.ftpTmpDir = ftpTmpDir;
		NodeProperties.ftpTimeOut = ftpTimeOut;
		NodeProperties.uploadMode = uploadMode;
		
		NodeProperties.masterRest = masterRest;
		NodeProperties.frameRest = frameRest;
		NodeProperties.nodeIp = nodeIp;
		
		NodeProperties.sender = sender;
		NodeProperties.password = password;
		NodeProperties.smtpHost = smtpHost;
		NodeProperties.smtpPort = smtpPort;
		
		print();
	}

	public static void print() {
		System.out.println("momUrl: " + momUrl);
		System.out.println("shellExecutor: " + shellExecutor);
		// redis
		System.out.println("redisMaxTotal: " + redisMaxTotal);
		System.out.println("redisMaxIdle: " + redisMaxIdle);
		System.out.println("maxWaitMillis: " + maxWaitMillis);
		System.out.println("redisIp: " + redisIp);
		System.out.println("redisPort: " + redisPort);
		System.out.println("redisTimeOut: " + redisTimeOut);
		System.out.println("redisDataTimeOut: " + redisDataTimeOut);
		System.out.println("redisAuth: " + redisAuth);
		System.out.println("testOnBorrow: " + testOnBorrow);
		System.out.println("testOnReturn: " + testOnReturn);

		System.out.println("masterRest: " + masterRest);
		System.out.println("frameRest: " + frameRest);
		System.out.println("nodeIp: " + nodeIp);
	}

	public static String getMomUrl() {
		return momUrl;
	}

	public static void setMomUrl(String momUrl) {
		NodeProperties.momUrl = momUrl;
	}

	public static String getShellExecutor() {
		return shellExecutor;
	}

	public static void setShellExecutor(String shellExecutor) {
		NodeProperties.shellExecutor = shellExecutor;
	}

	public static int getRedisMaxTotal() {
		return redisMaxTotal;
	}

	public static void setRedisMaxTotal(int redisMaxTotal) {
		NodeProperties.redisMaxTotal = redisMaxTotal;
	}

	public static int getRedisMaxIdle() {
		return redisMaxIdle;
	}

	public static void setRedisMaxIdle(int redisMaxIdle) {
		NodeProperties.redisMaxIdle = redisMaxIdle;
	}

	public static int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public static void setMaxWaitMillis(int maxWaitMillis) {
		NodeProperties.maxWaitMillis = maxWaitMillis;
	}

	public static String getRedisIp() {
		return redisIp;
	}

	public static void setRedisIp(String redisIp) {
		NodeProperties.redisIp = redisIp;
	}

	public static int getRedisPort() {
		return redisPort;
	}

	public static void setRedisPort(int redisPort) {
		NodeProperties.redisPort = redisPort;
	}

	public static String getRedisAuth() {
		return redisAuth;
	}

	public static void setRedisAuth(String redisAuth) {
		NodeProperties.redisAuth = redisAuth;
	}

	public static int getRedisTimeOut() {
		return redisTimeOut;
	}

	public static void setRedisTimeOut(int redisTimeOut) {
		NodeProperties.redisTimeOut = redisTimeOut;
	}

	public static int getRedisDataTimeOut() {
		return redisDataTimeOut;
	}

	public static void setRedisDataTimeOut(int redisDataTimeOut) {
		NodeProperties.redisDataTimeOut = redisDataTimeOut;
	}

	public static boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public static void setTestOnBorrow(boolean testOnBorrow) {
		NodeProperties.testOnBorrow = testOnBorrow;
	}

	public static boolean isTestOnReturn() {
		return testOnReturn;
	}

	public static void setTestOnReturn(boolean testOnReturn) {
		NodeProperties.testOnReturn = testOnReturn;
	}

	public static String getHostBasePath() {
		return hostBasePath;
	}

	public static void setHostBasePath(String hostBasePath) {
		NodeProperties.hostBasePath = hostBasePath;
	}

	public static String getSshAuthMethod() {
		return sshAuthMethod;
	}

	public static void setSshAuthMethod(String sshAuthMethod) {
		NodeProperties.sshAuthMethod = sshAuthMethod;
	}

	public static String getKeyLocation() {
		return keyLocation;
	}

	public static void setKeyLocation(String keyLocation) {
		NodeProperties.keyLocation = keyLocation;
	}

	public static String getHostUser() {
		return hostUser;
	}

	public static void setHostUser(String hostUser) {
		NodeProperties.hostUser = hostUser;
	}

	public static String getHostPassword() {
		return hostPassword;
	}

	public static void setHostPassword(String hostPassword) {
		NodeProperties.hostPassword = hostPassword;
	}

	public static String getFtpUrl() {
		return ftpUrl;
	}

	public static void setFtpUrl(String ftpUrl) {
		NodeProperties.ftpUrl = ftpUrl;
	}

	public static String getFtpUser() {
		return ftpUser;
	}

	public static void setFtpUser(String ftpUser) {
		NodeProperties.ftpUser = ftpUser;
	}

	public static String getFtpPwd() {
		return ftpPwd;
	}

	public static void setFtpPwd(String ftpPwd) {
		NodeProperties.ftpPwd = ftpPwd;
	}

	public static int getFtpPort() {
		return ftpPort;
	}

	public static void setFtpPort(int ftpPort) {
		NodeProperties.ftpPort = ftpPort;
	}

	public static String getFtpUploadDir() {
		return ftpUploadDir;
	}

	public static void setFtpUploadDir(String ftpUploadDir) {
		NodeProperties.ftpUploadDir = ftpUploadDir;
	}

	public static String getFtpTmpDir() {
		return ftpTmpDir;
	}

	public static void setFtpTmpDir(String ftpTmpDir) {
		NodeProperties.ftpTmpDir = ftpTmpDir;
	}

	public static int getFtpTimeOut() {
		return ftpTimeOut;
	}

	public static void setFtpTimeOut(int ftpTimeOut) {
		NodeProperties.ftpTimeOut = ftpTimeOut;
	}

	public static String getUploadMode() {
		return uploadMode;
	}

	public static void setUploadMode(String uploadMode) {
		NodeProperties.uploadMode = uploadMode;
	}

	public static String getMasterRest() {
		return masterRest;
	}

	public static void setMasterRest(String masterRest) {
		NodeProperties.masterRest = masterRest;
	}

	public static String getFrameRest() {
		return frameRest;
	}

	public static void setFrameRest(String frameRest) {
		NodeProperties.frameRest = frameRest;
	}

	public static String getNodeIp() {
		return nodeIp;
	}

	public static void setNodeIp(String nodeIp) {
		NodeProperties.nodeIp = nodeIp;
	}
	
}
