/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.configuration;

/**
 * EnvConstants.java
 * 
 * @author liubingj
 */
public final class Constants {

	public final class Env {

		private Env() {
		}

		public static final String BASE_HOME = "base.home";

		public static final String BASE_CONF = "base.conf";
	}

	public final class ServiceProtocol {

		public static final String TCP = "tcp";

		public static final String HTTP = "http";

		public static final String MQ = "mq";

	}
	
	public final class Protocol{
		
		public static final String TYPE = "type";

		public static final String SYN = "syn";

		public static final String ASYN = "asyn";
		
		public static final String BODY = "body";

	}

	public final class ProtocalMapping {
		public static final String servicemapping = "SERVICE_MAPPING";
		public static final String serviceid = "SERVICE_ID";
		public static final String protocolId = "PROTOCOL_ID";
		public static final String reqconfig = "REQ_CONF";
		public static final String resconfig = "RES_CONF";
		public static final String flowname = "FLOW_NAME";
	}

	public final class App {
		private App() {
		}
		/**
		 * application next 操作
		 */
		public static final String NEXT_OP="NEXT_OP";
		/**
		 * 是否启动双网卡
		 */
		public static final String DUAL_NIC="DUAL_NIC";
		/**
		 * docker镜像中的应用启动脚本位置
		 */
		public static final String START_SCRIPT_PATH = "START_SCRIPT_PATH";
		
		public static final String CONTAINER_IP = "CONTAINER_IP";
		
		public static final String HOST_IP = "HOST_IP";
		
		public static final String PORTS = "PORTS";
		
		/**
		 * smartc服务包所包含的服务分组列表名称
		 */
		public static final String SUBDOMAINS = "SUBDOMAINS";

		/**
		 * image of app
		 */
		public static final String IMAGE = "IMAGE";
		
		public static final String RESOURCE = "RESOURCE";
		//image更新状态
		public static final String IMAGE_UPDATE_TYPE="IMAGE_UPDATE_TYPE";
		/**
		 * id of app
		 */
		public static final String APP_ID = "APP_ID";
		/**
		 * id of patch
		 */
		public static final String PATCH_ID = "PATCH_ID";
		/**
		 * ID of app instance
		 */
		public static final String INSTANCE_ID = "INSTANCE_ID";
		/**
		 * absolute path of container
		 */
		public static final String CONTAINER_ABSOLUTE_PATH = "CONTAINER_ABSOLUTE_PATH";
		/**
		 * container name
		 */
		public static final String CONTAINER_NAME = "CONTAINER_NAME";
		/**
		 * absolute path of app
		 */
		public static final String APP_ABSOLUTE_PATH = "APP_ABSOLUTE_PATH";
		/**
		 * Max size of memory
		 */
		public static final String OPTIONS_MMX = "OPTIONS_MMX";
		/**
		 * Max size of disk
		 */
		public static final String DISK_SIZE = "DISK_SIZE";
		/**
		 * application use port's number
		 */
		public static final String PORT_COUNT = "PORT_COUNT";
		/**
		 * USED_CPUARG 1,2,3,4
		 */
		public static final String OPTIONS_USED_CPU = "USED_CPU";
		/**
		 * type of app
		 */
		public static final String APP_TYPE = "APP_TYPE";
		/**
		 * type of base service 
		 */
		public static final String BASESERV_TYPE = "BASESERV_TYPE";
		/**
		 * port of app
		 */
		public static final String APP_PORT = "APP_PORT";

		/**
		 * lxc ip
		 */
		public static final String LXC_IP = "LXC_IP";
		
		/**
		 * 网络模式 none  bridge container host  默认none
		 */
		public static final String NET_MODEL="NET_MODEL";
		/**
		 * out going ip
		 */
		public static final String EXTERNAL_IP="IPS";
		/**
		 * The status of app
		 */
		public static final String APP_STATE = "APP_STATE";

		/**
		 * Error reason
		 */
		public static final String REASON = "REASON";

		/**
		 * RESULAT
		 */
		public static final String RESULAT = "RESULT";

		/**
		 * ISOLATE
		 */
		public static final String ISOLATE = "ISOLATE";
		/**
		 * Instance URL
		 */
		public static final String URL = "URL";

		/**
		 * Nodes group.
		 */
		public static final String NODES = "NODES";

		/**
		 * Instances group.
		 */
		public static final String INSTANCES = "INSTANCES";
		/**
		 * delete type
		 */
		public static final String DELETE_TYPE = "DELETE_TYPE";
		/**
		 * node name
		 */
		public static final String NODE_NAME = "NODE_NAME";
		/**
		 * instance ip
		 */
		public static final String INSTANCE_IP = "INSTANCE_IP";
		/**
		 * app service
		 */
		public static final String SERVICE = "SERVICE";
		/**
		 * service id
		 */
		public static final String SERVICE_ID = "SERVICE_ID";
		/**
		 * service name
		 */
		public static final String SERVICE_NAME = "SERVICE_NAME";
		/**
		 * service queue
		 */
		public static final String SERVICE_QUEUE = "SERVICE_QUEUE";
		/**
		 * instance flag
		 */
		public static final String INSTANCE_FLAG = "INSTANCE_FLAG";

		/**
		 * used cpu
		 */
		public static final String USED_CPU = "USED_CPU";
		
		/**
		 * if share cpu
		 */
		public static final String SHARE_CPU="SHARE_CPU";
		
		/**
		 * cpu quota
		 */
		public static final String CPU_QUOTA = "CPU_QUOTA";

		/**
		 * 
		 */
		public static final String ERROR_CODE = "ERROR_CODE";

		/**
		 * app env variable
		 */
		public static final String ENV_VARIABLE = "ENV_VARIABLE";

		/**
		 * timeout
		 */
		public static final String TIMEOUT = "TIMEOUT";

		/**
		 * used port
		 */
		public static final String USEDPORT = "USEDPORT";

		public static final String TYPE_ATTRIBUTE = "TYPE_ATTRIBUTE";
		
	  
		/**
		 * image repository remote url
		 */
		public static final String REMOTEURL = "REMOTEURL";

		public static final String DOCKER_FS_PARAMS = "DOCKER_FS_PARAMS";
		
		public static final String DOCKER_VOLUME_DIR ="DOCKER_VOLUME_DIR";
		
		public static final String DOCKER_VOLUMES = "DOCKER_VOLUMES";
		
		public static final String DOCKER_PORT_MAPPINGS = "DOCKER_PORT_MAPPINGS";
		/**
		 * OUTER_CONNECT，是否对外连接
		 */
		public static final String OUTER_CONNECT = "OUTER_CONNECT";
		
		/**
		 * TASKCMD，执行脚本
		 */
		public static final String TASKCMD = "TASKCMD";
		
		public static final String JOB_TYPE = "TYPE";
		
		/**
		 * RCTASK,维护实例数任务
		 */
		public static final String RCTASK = "RCTASK";
		
		public static final String THROUGHROUTER = "THROUGHROUTER";
		
		/**
		 * RCTASK,维护实例数任务
		 */
		public static final String TIMESTAMP = "TIMESTAMP";
		
		/**
		 * 主机模式，日志路径
		 */
		public static final String LOG_DIR="LOG_DIR";
		
		
		/**
		 * app type
		 */
		public final class AppType {

			public static final String SERIVICE = "service";
			public static final String APP = "app";
			public static final String CACHE = "cache";
			public static final String MONGO = "mongo";
			public static final String MYSQL = "mysql";
			public static final String GRIDFS = "gridfs";
			public static final String REDIS = "redis";

		}

		// for base service
		public static final String CREATE_FLAG = "CREATE_FLAG";
		public static final String DELETE_FLAG = "DELETE_FLAG";
		public static final String SYSUSERID = "SYSUSERID";
		public static final String SYSUSERPWD = "SYSUSERPWD";
		public static final String DBNAME = "DBNAME";
		public static final String DBUSERID = "DBUSERID";
		public static final String DBUSERPWD = "DBUSERPWD";
		public static final String BIND_IP = "BIND_IP";
		public static final String DB_NAME = "DB_NAME";
		public static final String ROUTER_PORT = "ROUTER_PORT";
		public static final String IS_MASTER = "IS_MASTER";

	}

	/**
	 * 基础服务
	 */
	public final class BaseService {
		/**
		 * mysql节点类型
		 */
		public static final String MYSQL_NODE_TYPE = "A11D3D3C-F603-40A1-AAEB-7AAD39F11A4D";
		/**
		 * cache节点类型
		 */
		public static final String CACHE_NODE_TYPE = "DB92565E-E202-4F30-A201-1D2466D41663";
		/**
		 * cache节点类型
		 */
		public static final String MONGO_NODE_TYPE = "93466B1C-B14C-4E7C-887D-D3C71BD19E6C";
		/**
		 * 基础服务节点是否为共享节点
		 */
		public static final String SERVICE_NODE_SHARED = "SERVICE_NODE_SHARED";
		/**
		 * 基础服务提供服务的应用实例id
		 */
		public static final String INSTANCE_ID = "INSTANCE_ID";
		/**
		 * 数据库文件大小
		 */
		public static final String DB_FILE_SIZE = "DB_FILE_SIZE";
		/**
		 * 数据库最大连接数
		 */
		public static final String DB_MAX_CONN = "DB_MAX_CONN";
		/**
		 * 数据库名称
		 */
		public static final String DB_NAME = "DB_NAME";
		/**
		 * Cache大小
		 */
		public static final String CACHE_SIZE = "CACHE_SIZE";
		/**
		 * Cache名称
		 */
		public static final String CACHE_NAME = "CACHE_NAME";
		/**
		 * cache id，缓存对象id
		 */
		public static final String CACHE_ID = "CACHE_ID";
		/**
		 * renter id，租户id，申请基础服务的租户id
		 */
		public static final String RENTER_ID = "RENTER_ID";
	}
	
	public final class portMapingProtocol {

		public static final String TCP = "tcp";

		public static final String UDP= "udp";
	}
	public final class STATICDOMAIN{
		public static final String  DNS="dns";
		public static final String DOMAINNAME="domainName";
		public static final String IP="ip";
	}
}
