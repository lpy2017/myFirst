package com.dc.appengine.plugins.service;

public interface IAgent {
	/*
	 * 输入参数样式
	   {
		    "flowInstanceId": "133",
		    "instanceId": "2017071300003428",
		    "insvarMap": {
				"_fromMainInstanceId": "2017082200001569",
				"_instanceId": "2017082200001569",
				"_loopCount": "0",
				"_mainNextTaskId": "2017081800003797",
				"_op": "",
				"_pluginReturn": "",
				"_sendMsgSys": "LCKJ",
				"_snapshotId": "",
				"_taskBKey": "133",
				"_taskPreNodeId": "2017081800003794",
				"_taskPreTokenId": "2017082200008928",
				"_taskPreTokenUser": "admin",
				"_tokenId": "",
				"_topic": "",
				"_ywxtId": "JCKF",
				"preTokenId": "2017082200008930"
			},
		    "message": {
		        "SaltConfig_-1": {},
		        "componentInput": {
		            "ajpPort": "18119",
		            "deployPath": "/home/tomcat138/",
		            "shutdownPort": "18115",
		            "startPort": "18181",
		            "version": "8.5.15"
		        },
		        "componentName": "itsm_tomcat",
		        "componentOutput": {
		            "webappsPath": "/home/tomcat138/webapps/"
		        },
		        "configTemplate": {
		            "/srv/salt/d1ca04e5-c885-4e60-984a-744e76e706f7/files/output.ftl": "/srv/salt/d1ca04e5-c885-4e60-984a-744e76e706f7/output.properties",
		            "/srv/salt/d1ca04e5-c885-4e60-984a-744e76e706f7/files/server.ftl": "/srv/salt/d1ca04e5-c885-4e60-984a-744e76e706f7/files/server.xml",
		            "/srv/salt/d1ca04e5-c885-4e60-984a-744e76e706f7/files/settings.ftl": "/srv/salt/d1ca04e5-c885-4e60-984a-744e76e706f7/settings.sls"
		        },
		        "deployPath": "d1ca04e5-c885-4e60-984a-744e76e706f7",
		        "instanceId": "d1ca04e5-c885-4e60-984a-744e76e706f7",
		        "nodeIp": "10.1.108.100",
		        "operation": "deploy",
		        "pluginInput": {},
		        "resouceUrl": "salt_default\n",
		        "salt_-5": {},
		        "version": "f4f1bf7a-a1be-41db-a9ac-18f4dcac8f1a"
		    },
		    "nodeId": "2017071300002096",
		    "parPdId": "2017071300002157",
		    "parinstanceId": "2017071300001277",
		    "pluginName": "SaltConfig_-1",
		    "workitemId": "2017071300003430"
		}
		
		输出参数样式：
		{
			"message": "下载成功，filePath =/home/tomcat138/webapps/itsm.20170705.1455.zip",
			"result": true
		}
		
		输出参数和输出参数样式要一致，且内容只能增加，不能减少
	 */
	public String doActive(String param) throws Exception;
}
