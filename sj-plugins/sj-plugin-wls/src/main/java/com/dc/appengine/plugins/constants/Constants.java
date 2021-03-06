/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.plugins.constants;

public final class Constants {
	/*
	 * 应用，数据库备份，升级常量
	 */
	public static final String DB = "db";//数据库补丁描述文件的关键字
	public static final String APP=  "app";//应用补丁描述文件的关键字
	public static final String FILTER=  "filter";//应用补丁描述文件的关键字
	public static final String APP_RULE_XML =  "app_rule.xml"; //应用备份规则描述文件
	public static final String FILE_SUFFIX_XML =".xml";//XML类型文件的扩展名
	public static final String FITER_TABLE ="table";//filter.xml文件中的oracle对象类型table 
	public static final String FITER_PKB ="package";//filter.xml文件中的oracle对象类型package body
	public static final String PKBObject ="PACKAGE_BODY";
	public static final String PKB ="\'PACKAGE BODY\'";
	public static final String FILE_SUFFIX_PKB =".pkb";
	public static final String FILE_SUFFIX_DMP =".dmp";
	public static final String FILE_SUFFIX_SQL =".sql";
	
	public final class App {
		public App() {
		};
		public static final String INSTANCE_ID = "INSTANCE_ID";
		public static final String MOM_TASKFILE="../momTask"; //mom挂掉时，存储mom消息的临时文件夹
	}

	public static final String comm_type_mom = "MOM";
	public static final String NODE = "node";
	public static final String WORKFLOW = "workflow";
	public static final String DEPLOY_DIR = "deploy";
	public static final String TEMP_DIR = "tempDir";
	public static final String DC_INSTALL_PATH = System
			.getProperty("com.dc.install_path");
	
	/*
	 * plugin节点执行状态码
	   状态码由两位数字组成，第一位表示插件执行的方法，第二位表示该方法的执行结果 
	   	第一位数字
	   	0 表示前处理
		1 表示invoke
		2 表示后处理
		3 表示agent
		第二位数字
		0 表示正在执行
		2 表示执行成功
		7 执行失败
	 * 
	 */
	public static final String preAction_d = "00";
	public static final String preAction_s = "02";
	public static final String preAction_f = "07";
	
	public static final String invoke_d = "10";
	public static final String invoke_s = "12";
	public static final String invoke_f = "17";
	
	public static final String postAction_d = "20";
	public static final String postAction_s = "22";
	public static final String postAction_f = "27";
	
	public static final String doActive_d = "30";
	public static final String doActive_s = "32";
	public static final String doActive_f = "37";
	
	public static final String invokeFrameRrror = "47";//调用workflow异常
	public static final String invokingFrame = "40";//正在调用workflow
	public static final String sendingMessage = "50";//正在发送mom消息
	public static final String receivedMessage = "51";//收到mom消息
	
	public static final String messageError = "67";//正在发送mom消息
	
	public static final String FAILCODE = "7";//失败码
	public static final String SUCCESSCODE = "2";//成功码
	
	/**
	 * 插件相关常量
	 */
	public final class Plugin{
		/*state*/
		public static final String SUCCESS_STATE = "true";
		public static final String FAIL_STATE = "false";
		public static final String PHRASE_PREACTION = "preAction";//前处理
		public static final String PHRASE_INVOKE = "invoke";//invoke
		public static final String PHRASE_POSTACTION = "postAction";//后处理
		public static final String PHRASE_ACTIVE = "active";//active
		/*
		 * 与master封装的message相关的key
		 */
		public static final String BLUEPRINTCONFG = "blueprintConfig";//message 中插件名的key
		public static final String PLUGINNAME = "pluginName";//message 中插件名的key
		public static final String COMPONENTINPUT = "componentInput";//message 中组件参数的key
		public static final String COMPONENTOUTPUT = "componentOutput";//message 中组件参数的key
		public static final String URL = "resouceUrl";//资源的url key
		public static final String INSTANCEID = "instanceId";//实例id key
		public static final String NODEIP = "hostIp";//实例id key
		public static final String OPERATION = "operation";//操作 key
		public static final String COMPONENTNAME = "componentName";//组件名 key
		public static final String CONFIGTEMPLATE = "configTemplate";//配置模板 key
		public static final String DEPLOYPATH = "deployPath";//配置模板 key
		public static final String LOGICALVERSION = "logicalVersion";//配置模板 key
		public static final String TargetVersion_Key = "target";
		public static final String CurrentVersion_Key = "current";
			
		/*
		 * 与workflow封装的paramJson相关的key
		 */
		public static final String MESSAGE = "message";//workflow封装的报文中，message的key
		public static final String INSVARMAP = "insvarMap";//workflow封装的报文中，insvarMap的key
		public static final String WORKITEMID = "workitemId";//workflow封装的报文中，workitemId的key,value为当前实例的tokenid
		public static final String ENDNODEID = "endnodeId";//workflow封装的报文中，endnodeId的key,value为endnodeid的tokenid
		public static final String NODEID = "nodeId";//实例node id key
		public static final String TOKENINSTANCEID = "instanceId";//实例id key
		
		public static final String FROMUSER = "admin";//默认admin
		public static final String TOUSER = "admin";//admin
		
		/*与flowAgent约定的key*/
		public static final String DELETE_FILES = "deleteFiles";
		public static final String COMMAND_FILE = "commandFile";
		public static final String COMMAND_PARAMS = "commandParams";
		public static final String RESULT = "result";
		public static final String RESULT_MESSAGE = "message";
		
		/*mom消息发送*/
		public static final String NODECOMMANDINVOKER = "NodeCommandInvoker";
		
		public static final String CMD_KEY = "CMD";
		public static final String SSHPORT = "22";//默认端口22
		
		public static final String PROTOCOLTYPE_SSH = "ssh";//ssh协议
		public static final String PROTOCOLTYPE_HTTP = "http";//http协议
		public static final String PROTOCOLTYPE_TCP = "tcp";//tcp协议
		
		public static final String SUBFLOWNODEID = "subFlowNodeId";//子流程id
		
		//weblogic插件
		public static final String APPDEPLOY4WEBLOGIC = "AppDeploy4Weblogic";
		public static final String APPUNDEPLOY4WEBLOGIC = "AppUndeploy4Weblogic";
		public static final String WEBLOGICJMSQUEUECREATE = "WeblogicJMSQueueCreate";
		public static final String WEBLOGICJMSQUEUEUPDATE = "WeblogicJMSQueueUpdate";
		public static final String WEBLOGICJMSQUEUEDELETE = "WeblogicJMSQueueDelete";
		public static final String DATASOURCENEW4WEBLOGIC = "DataSourceNew4Weblogic";
		public static final String DATASOURCEDELETE4WEBLOGIC = "DataSourceDelete4Weblogic";
		//all
		public static final String WLSUSR = "wlsUsr";
		public static final String WLSPWD = "wlsPwd";
		//app
		public static final String APPNAME = "appName";
		public static final String APPPATH = "appPath";
		public static final String APPTARGET = "appTarget";
		public static final String WLSDOMAINPATH = "wlsDomainPath";
		public static final String WLSVERSION = "wlsVersion";
		public static final String WLSENCODING = "wlsEncoding";
		//datasource
		public static final String DSNAME = "dsName";
		public static final String DSJNDI = "dsJndi";
		public static final String DSTARGETSERVER = "dsTargetServer";
		public static final String DBTYPE = "dbType";
		public static final String DBJTA = "dbJTA";
		public static final String DBNAME = "dbName";
		public static final String DBIP = "dbIp";
		public static final String DBPORT = "dbPort";
		public static final String DBUSR = "dbUsr";
		public static final String DBPWD = "dbPwd";
		//jms
		public static final String DOMAIN_DIR = "domainDir";
		public static final String QUEUE_NAME = "queueName";
		public static final String QUEUE_JNDINAME = "queueJNDIName";
		public static final String SUB_DEPLOYNAME = "subDeployName";
		public static final String JMS_MODULE_DESCRIPTOR = "JMSModuleDescriptor";
		public static final String THRESHOLDS_BYTES = "thresholds_bytes";
		public static final String THRESHOLDS_MESSAGES = "thresholds_messages";
		public static final String MAXIMUM_MESSAGE_SIZE = "maximum_message_size";
		
		
	}
}
