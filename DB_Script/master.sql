DROP TABLE IF EXISTS `ma_snapshot_blueprint_instance`;
CREATE TABLE `ma_snapshot_blueprint_instance` (                                                
   `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '快照ID 主键', 
   `BLUE_INSTANCE_ID` varchar(50) NOT NULL COMMENT '蓝图实例ID', 
   `SNAPSHOT_NAME` varchar(100) NOT NULL COMMENT '快照名称',                      
   `SNAPSHOT_INFO` mediumtext DEFAULT NULL COMMENT '快照信息',           
   `UPDATE_TIME` timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',  
   `USER_ID` bigint(20) DEFAULT NULL COMMENT '用户ID',                                 
   PRIMARY KEY (`ID`),                                                                   
   UNIQUE KEY `PK_MA_SNAPSHOT_BLUEPRINT_INSTANCE` (`ID`)                                        
 ) COMMENT='蓝图实例快照表' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `ma_application`;
CREATE TABLE IF NOT EXISTS `ma_application` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '版本ID 主键',
  `APP_NAME` varchar(30) DEFAULT NULL COMMENT '组件名称',
  `DESCRIPTION` text COMMENT '组件描述',
  `USERID` bigint(22) DEFAULT NULL COMMENT '用户主键',
  `APP_TYPE` varchar(30) DEFAULT NULL COMMENT '组件类型,通用组件和应用组件',
  `START_ID` int(11) DEFAULT NULL COMMENT '启动流程id',
  `DEPLOY_ID` int(11) DEFAULT NULL COMMENT '部署流程id',
  `STOP_ID` int(11) DEFAULT NULL COMMENT '停止流程id',
  `DESTROY_ID` int(11) DEFAULT NULL COMMENT '卸载流程id',
  `CLUSTER_ID` varchar(64) DEFAULT NULL COMMENT '集群id',
  `VERSIONID` varchar(50) DEFAULT NULL COMMENT '版本id',
  `BLUE_INSTANCE_ID` int(11) DEFAULT NULL COMMENT '蓝图实例id',
  `KEY` bigint(20) DEFAULT NULL COMMENT '在蓝图中的键',
  `RC_FLAG` tinyint(1) DEFAULT '0' COMMENT '维护实例数开关 0关闭 1开启',
  `COMPONENT_NAME` varchar(30) DEFAULT NULL COMMENT '组件资源名称',
  `CURRENT_VERSION` varchar(64) DEFAULT NULL COMMENT '当前版本',
  `CURRENT_INPUT` text COMMENT '当前版本input',
  `CURRENT_OUTPUT` text COMMENT '当前版本output',
  `TARGET_VERSION` varchar(64) DEFAULT NULL COMMENT '目标版本',
  `TARGET_INPUT` text COMMENT '目标版本input',
  `TARGET_OUTPUT` text COMMENT '目标版本output',
  `SMART_FLAG` tinyint(1) DEFAULT '0' COMMENT '组件智能开关 0非智能模式，1智能模式',
  `NODE_NAME` varchar(30) DEFAULT NULL COMMENT '组件节点名称',
  `EXECUTE_FLAG` tinyint(1) DEFAULT '1' COMMENT '组件是否执行开关 0不执行，1执行',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PK_MA_APPLICATION` (`ID`)
) COMMENT='蓝图实例组件表' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `ma_instance`;
CREATE TABLE IF NOT EXISTS `ma_instance` (
  `ID` varchar(64) NOT NULL COMMENT '实例id',
  `APP_ID` int(11) DEFAULT NULL COMMENT '组件id',
  `NODE_ID` varchar(64) DEFAULT NULL COMMENT '节点id',
  `DEPLOY_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `LXC_IP` varchar(16) DEFAULT '0.0.0.0' COMMENT '实例容器ip',
  `STATUS` varchar(16) DEFAULT 'UNDEPLOYED' COMMENT '实例状态',
  `RESOURCE_VERSION_ID` varchar(64) DEFAULT NULL COMMENT '资源版本id',
  `DEPLOY_PATH` varchar(100) DEFAULT NULL COMMENT '部署路径',
  `COMPONENT_INPUT` text COMMENT '组件input',
  `COMPONENT_INPUT_TEMP` text COMMENT '组件动态input',
  `COMPONENT_OUTPUT` text COMMENT '组件output',
  `COMPONENT_OUTPUT_TEMP` text COMMENT '组件动态output',
  `PARAMS` varchar(2000) DEFAULT NULL COMMENT '实例私有参数配置',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PK_MA_INSTANCE` (`ID`)
) COMMENT='组件实例表' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `ma_app_version_new`;

CREATE TABLE `ma_app_version_new` (
  `ID` bigint(22) NOT NULL AUTO_INCREMENT,
  `VERSION_UUID` varchar(64) DEFAULT NULL,
  `RESOURCE_ID` bigint(22) NOT NULL,
  `VERSION_ID` bigint(22) NOT NULL,
  `CONTAINER` varchar(200) NOT NULL,
  `IN_USE` tinyint(1) DEFAULT '1' COMMENT '是否目标版本（升级等过程的最终版本）',
  `DEPLOY_TIMEOUT` varchar(10) DEFAULT '60000',
  `START_TIMEOUT` varchar(10) DEFAULT '60000',
  `STOP_TIMEOUT` varchar(10) DEFAULT '60000',
  `DESTROY_TIMEOUT` varchar(10) DEFAULT '60000',
  `ACCESS_PORT` int(11) DEFAULT NULL,
  `REMOTE_REPO_ADDR` varchar(100) DEFAULT NULL,
  `VERSION_INFO` varchar(200) DEFAULT NULL,
  `IMAGE_INFO` varchar(200) DEFAULT NULL,
  `START_SCRIPT_PATH` varchar(300) DEFAULT NULL,
  `EXTRA_PORTS` varchar(200) DEFAULT NULL,
  `DOCKER_VOLUME_DIR` varchar(300) DEFAULT NULL,
  `LOG_FILE_DIR` varchar(300) DEFAULT NULL COMMENT 'log file dir in container',
  `USER_ID` bigint(20) DEFAULT NULL,
  `REGISTRY_ID` varchar(64) DEFAULT NULL,
  `CONFVERSION_ID` varchar(64) DEFAULT NULL,
  `NAME` varchar(32) DEFAULT 'v1',
  PRIMARY KEY (`ID`)
);

DROP TABLE IF EXISTS `ma_app_version_strategy`;

CREATE TABLE `ma_app_version_strategy` (
  `ID` bigint(22) NOT NULL AUTO_INCREMENT,
  `VERSION_ID` bigint(22) NOT NULL,
  `STRATEGY_ID` bigint(22) NOT NULL,
  `APP_ID` bigint(22) NOT NULL,
  `CREATED_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `IS_CURRENT_VERSION` tinyint(2) DEFAULT '0',
  `EXTEND_STRATEGY_ID` bigint(20) DEFAULT '-1' COMMENT '伸缩策略主键',
  PRIMARY KEY (`ID`)
);

DROP TABLE IF EXISTS `ma_instance_cpus`;

CREATE TABLE `ma_instance_cpus` (         
  `instance_id` varchar(64) DEFAULT NULL,  
  `cpu_id` bigint(20) DEFAULT NULL        
);

DROP TABLE IF EXISTS `ma_item`;


CREATE TABLE `ma_item` (
  `ID` bigint(22) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) DEFAULT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `CODE` varchar(50) DEFAULT NULL,
  `TYPE` varchar(50) DEFAULT NULL,
  `NULLABLE` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PK_MA_ITEM` (`ID`)
);


LOCK TABLES `ma_item` WRITE;
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('1','CPU','使用CPU占用所分配CPU百分比，单位%（如：设置50%则直接填写50）','CPU','SCALE',NULL);
-- INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	-- ('2','MEMORY','使用内存占用所分配内存的百分比，单位%（如：设置50%则直接填写50）','MEMORY','SCALE',NULL);
-- INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	-- ('3','RESPONSE TIME','平均响应时间，单位ms（如：url1=1000;url2=1000 或者 /=1000）','RESPONSETIME','SCALE',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('4','INSTANCE_NUMBER','实例个数（伸缩中最大实例数和最小实例数）','INSTANCE_NUMBER','SCALE',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('5','JVM_XMX','部署实例所占内存所占，单位M（如：128）','JVM_XMX','DEPLOY',NULL);
-- INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	-- ('6','VIP','VIP策略（如：com.dc.appengine.DFHCheck）','VIP','SCALE',NULL);
-- INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	-- ('7','LINKCOUNT','连接数（如：100）','LINKCOUNT','SCALE',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('9','SAMEHOST','是否同一台虚拟机（true、false）','SAMEHOST','DEPLOY',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('10','ALONE','是否独享节点（true、false）','ALONE','DEPLOY',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('11','CPU_NUMBER','强隔离时使用CPU个数（如：2），非强隔离时不起作用','CPU_NUMBER','DEPLOY',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('12','NODE_TYPE','使用虚拟机类型','NODE_TYPE','DEPLOY','1');
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('14','INSTANCE_NUMBER','实例个数','INSTANCE_NUMBER','DEPLOY',1);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('15','STRONG_ISOLATA','是否强隔离（true,false）','STRONG_ISOLATA','DEPLOY',1);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('16','AUTO_OPERATE','是否自动伸缩（true,false）','AUTO_OPERATE','SCALE',null);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('18','VIP','VIP策略（如：com.dc.appengine.DFHCheck）','VIP','DEPLOY',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('17','NODE_GROUP','是否为集群（如：true）','NODE_GROUP','DEPLOY',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('19','DISK','使用硬盘大小，单位MB（如：10）','DISK','DEPLOY',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('20','CPU_SCALE_THRESHOLD','CPU伸缩阈值，为伸缩策略中CPU使用率的百分比，如70表示70%','CPU_SCALE_THRESHOLD','SCALE',NULL);
-- INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	-- ('21','MEMORY_SCALE_THRESHOLD','内存使用率伸缩阈值，为伸缩策略中内存使用率的百分比，如70表示70%','MEMORY_SCALE_THRESHOLD','SCALE',NULL);
-- INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	-- ('22','RESPONSE_TIME_SCALE_THRESHOLD','响应时间伸缩阈值，为伸缩策略中响应时间的百分比，如70表示70%','RESPONSE_TIME_SCALE_THRESHOLD','SCALE',NULL);
-- INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	-- ('23','LINK_COUNT_SCALE_THRESHOLD','连接数伸缩阈值，为伸缩策略中连接数的百分比，如70表示70%','LINK_COUNT_SCALE_THRESHOLD','SCALE',NULL);
-- INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	-- ('24','INSTANCE_STANDBY_TIME','应急备用实例存在时间段，如 2015-11-11 00:00:00至2015-11-12 00:00:00','INSTANCE_STANDBY_TIME','SCALE',NULL);
-- INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	-- ('25','STANDBY_TIME_INSTANCE_NUM','应急时间段内最小运行实例数','STANDBY_TIME_INSTANCE_NUM','SCALE',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('26','DOCKER_IMAGE','docker镜像','DOCKER_IMAGE','DEPLOY',NULL);
insert into `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) values('27','CONNECTION','需要使用的连接数','CONNECTION','DEPLOY',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('28','SHARED_CPU','是否共享CPU（取值true或false），对于支持隔离容器的节点，设置为true，则cpu不再进行隔离','SHARED_CPU','DEPLOY',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('29','CLUSTERID','用户部署集群ID','CLUSTERID','DEPLOY',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('30','NODEIDS','部署集群下的node ID,多个用逗号隔开','NODEIDS','DEPLOY',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('31','RESOURCE_UPDATE_TYPE','资源更新策略，值为always, ifnotpresent, never','RESOURCE_UPDATE_TYPE','DEPLOY',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('32','CODE_CPU_QUOTA','cpu配额','CODE_CPU_QUOTA','DEPLOY',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('33','OUTGOING_IPS','一个或多个外部网络IP','OUTGOING_IPS','DEPLOY',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('34','AUTOMATIC','是否是自动模式','AUTOMATIC','SCALE',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('35','SCHEDULE','如果处于非自动模式，调度具体内容','SCHEDULE','SCALE',NULL);
INSERT INTO `ma_item` (`ID`, `NAME`, `DESCRIPTION`, `CODE`, `TYPE`, `NULLABLE`) VALUES
	('36','LABELS','部署策略，某些应用可以指定部署在包含某种标签的NODE上','LABELS','DEPLOY',NULL);
UNLOCK TABLES;


DROP TABLE IF EXISTS `ma_node`;

CREATE TABLE `ma_node` (
  `IP` varchar(50) DEFAULT NULL COMMENT '主机IP',
  `DESCRIPTION` text COMMENT '主机描述',
  `NAME` varchar(100) DEFAULT NULL COMMENT '主机名称',
  `TCP_PORT` bigint(22) DEFAULT NULL,
  `STATUS` varchar(20) DEFAULT NULL COMMENT '主机状态，暂未使用',
  `CPUCOUNT` bigint(22) DEFAULT NULL COMMENT 'CPU数量，暂未使用',
  `DISKSIZE` bigint(22) DEFAULT NULL COMMENT '磁盘大小，暂未使用',
  `HOSTNAME` varchar(50) DEFAULT NULL COMMENT '主机名，暂未使用',
  `MEMORYSIZE` bigint(22) DEFAULT NULL COMMENT '内存大小，暂未使用',
  `ENNAME` varchar(50) DEFAULT NULL COMMENT '主机类型编码，暂未使用',
  `NODE_GROUP` varchar(50) DEFAULT NULL COMMENT '主机所属组，暂未使用',
  `ISOLATA_STATE` varchar(1) DEFAULT NULL COMMENT '主机隔离状态，暂未使用',
  `UPGRADING` tinyint(1) DEFAULT '0'COMMENT '主机是否正在升级，暂未使用',
  `CLUSTERID` varchar(100) DEFAULT NULL COMMENT '所属环境ID',
  `ADAPTERNODEID` varchar(100) NOT NULL DEFAULT '' COMMENT '主机ID',
  `USER_NAME` VARCHAR(100) NULL DEFAULT '' COMMENT '主机用户名',
  `USER_PASSWORD` VARCHAR(100) NULL DEFAULT '' COMMENT '主机密码',
  `OS_TYPE` VARCHAR(50) DEFAULT 'linux' COMMENT '操作系统类型',
  PRIMARY KEY (`ADAPTERNODEID`),
  UNIQUE INDEX `IP` (`IP`)
) COMMENT='主机信息表';

DROP TABLE IF EXISTS `ma_node_cpus`;

CREATE TABLE `ma_node_cpus` (                      
  `id` bigint(20) NOT NULL AUTO_INCREMENT,                       
  `cpu_index` int(11) DEFAULT NULL,                    
  `shared` tinyint(1) DEFAULT NULL,                    
  `node_id` varchar(64) DEFAULT NULL,
  `used_count` int(11) DEFAULT 0,
  `max_count` int(11) DEFAULT 0,
  PRIMARY KEY (`id`)                                   
);

DROP TABLE IF EXISTS `ma_node_bridge`;
CREATE TABLE `ma_node_bridge` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `node_id` varchar(64) NOT NULL,
  `bridge_ip` varchar(40) NOT NULL,
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `ma_node_resource`;

CREATE TABLE `ma_node_resource` (
  `id` varchar(64) NOT NULL,
  `ip` varchar(50) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `disk` int(11) DEFAULT NULL,
  `memory` double DEFAULT NULL,
  `cpu_num` int(11) DEFAULT NULL,
  `cpu_status` double DEFAULT NULL,
  `cpus` varchar(1000) DEFAULT NULL,
  `instance_num` int(11) DEFAULT NULL,
  `host_name` varchar(100) DEFAULT NULL,
  `isolate_state` tinyint(4) DEFAULT NULL,
  `node_type` varchar(100) DEFAULT NULL,
  `node_group` varchar(20) DEFAULT NULL,
  `used_lxc` tinyint(1) DEFAULT 0,
  `version` bigint(20) DEFAULT '0' COMMENT '乐观锁实现字段',
  `is_new` tinyint(1) DEFAULT 0,
  `alone` tinyint(1) DEFAULT 0,
  `status` varchar(10) DEFAULT 'RUNNING',
  
  PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `ma_node_labels`;
CREATE TABLE `ma_node_labels` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    `node_id` varchar(64) DEFAULT NULL COMMENT '主机ID',
	`label_key` VARCHAR(100) DEFAULT NULL COMMENT '标签key',
    `label_value` VARCHAR(100) DEFAULT NULL COMMENT '标签value',           
    `label_type` VARCHAR(2) DEFAULT NULL COMMENT '0：非层，1：静态层，2：动态层，3：云层',
     PRIMARY KEY (`id`)
) COMMENT='主机标签表';


DROP TABLE IF EXISTS `ma_env_var`;

CREATE TABLE `ma_env_var` (
  `obj_id` varchar(64) NOT NULL COMMENT 'object that has envs, e.g. containers or services',
  `var_key` varchar(100) DEFAULT NULL,
  `var_value` varchar(1000) DEFAULT NULL,
  `obj_type` varchar(15) DEFAULT 'container' COMMENT 'object type, e.g. container or service',
  `value_from_type` varchar(15) DEFAULT NULL COMMENT 'type of obj the var value is from, i.e. app, service etc.',
  `value_from_id` varchar(64) DEFAULT NULL COMMENT 'the id of the obj the value from',
  `description` varchar(200) DEFAULT NULL,
  `version_uuid` varchar(64) DEFAULT NULL,
  `value_from_version_uuid` varchar(64) DEFAULT NULL
);


DROP TABLE IF EXISTS `ma_strategy`;

CREATE TABLE `ma_strategy` (
  `ID` bigint(22) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(200) DEFAULT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `USER_ID` bigint(22) DEFAULT NULL,
  `TYPE` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PK_STRATEGY` (`ID`)
);

DROP TABLE IF EXISTS `ma_strategy_item`;


CREATE TABLE `ma_strategy_item` (
  `ID` bigint(22) NOT NULL AUTO_INCREMENT,
  `ITEM_ID` bigint(22) DEFAULT NULL,
  `STRATEGY_ID` bigint(22) DEFAULT NULL,
  `VALUE` text,
  `WEIGHT` bigint(22) DEFAULT NULL,
  `OPERATOR` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PK_STRATEGY_ITEM` (`ID`)
);

DROP TABLE IF EXISTS `ma_app_deps`;

CREATE TABLE `ma_app_deps` (                                                        
   `app_id` varchar(64) DEFAULT NULL,                                                 
   `obj_id` varchar(64) DEFAULT NULL,                                                 
   `obj_type` varchar(10) DEFAULT NULL COMMENT 'dep obj type, e.g. app or service',  
   `version_uuid` varchar(64) DEFAULT NULL,
   `obj_version_uuid` varchar(64) DEFAULT NULL                                              
);


DROP TABLE IF EXISTS `user_relationship`;
CREATE TABLE `user_relationship` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `father_id` bigint(20) DEFAULT NULL COMMENT '归属用户ID'
) COMMENT='用户归属关系表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert  into `user_relationship`(`user_id`,`father_id`) values (-1,1),(1,1),(2,1);


DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` varchar(100) NOT NULL COMMENT '角色ID'
) COMMENT='用户角色映射表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert  into `user_role`(`user_id`,`role_id`) values (-1,'2'),(1,'1'),(2,'2');

/*Table structure for table `user_resource` */

DROP TABLE IF EXISTS `user_resource`;

CREATE TABLE `user_resource` (
  `user_id` bigint(20) NOT NULL,
  `cpu` int(11) DEFAULT '0',
  `memory` int(11) DEFAULT '0',
  `disk` int(11) DEFAULT '0',
  `cpu_used` int(11) DEFAULT '0',
  `memory_used` int(11) DEFAULT '0',
  `disk_used` int(11) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `accounts_info`;

CREATE TABLE `accounts_info` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `USERID` varchar(50) NOT NULL COMMENT '用户名',
  `USERPASSWORD` varchar(64) DEFAULT NULL COMMENT '用户密码MD5',
  `IS_NEW` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否新用户标识',
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `USERID` (`USERID`)
) COMMENT='用户信息表' ;

insert  into `accounts_info`(`USERID`,`USERPASSWORD`,`ID`) values ('admin','96e79218965eb72c92a549dd5a330112',1);
insert  into `accounts_info`(`USERID`,`USERPASSWORD`,`ID`) values ('paas','paas',2);
insert  into `accounts_info`(`USERID`,`USERPASSWORD`,`ID`) values ('sso_admin','sso_paas',-1);

DROP TABLE IF EXISTS `sv_resource`;
CREATE TABLE `sv_resource` (
  `ID` varchar(64) NOT NULL COMMENT 'uuid 主键',
  `RESOURCE_NAME` varchar(50) DEFAULT NULL COMMENT '资源名称',
  `RESOURCE_TYPE` varchar(30) DEFAULT NULL COMMENT '资源类型,应用类型，0通用，topo等',
  `RESOURCE_DESC` varchar(250) DEFAULT NULL COMMENT '资源描述',
  `IS_DOCKER_RESOURCE` tinyint(1) DEFAULT '0' COMMENT '是否是docker资源',
  `ICON` varchar(100) DEFAULT '' COMMENT '存储组件图片信息',
  `VERSION` int(11) default 0 COMMENT '乐观锁',
  `RELEASE_BLUEPRINT_ID` int(11) default null COMMENT '发布绑定的蓝图实例id',
  `RELEASE_BLUEPRINT_FLOW` varchar(20) default null COMMENT '发布绑定的蓝图流程id',
  `ROLLBACK_BLUEPRINT_ID` int(11) default null COMMENT '回退绑定的蓝图实例id',
  `ROLLBACK_BLUEPRINT_FLOW` varchar(20) default null COMMENT '回退绑定的蓝图流程id',
  `START_BLUEPRINT_ID` int(11) default null COMMENT '启动绑定的蓝图实例id',
  `START_BLUEPRINT_FLOW` varchar(20) default null COMMENT '启动绑定的蓝图流程id',
  `STOP_BLUEPRINT_ID` int(11) default null COMMENT '停止绑定的蓝图实例id',
  `STOP_BLUEPRINT_FLOW` varchar(20) default null COMMENT '停止绑定的蓝图流程id',
  `STATUS` varchar(20) default '00' COMMENT '组件状态 00-初始状态,50-启动中,51-启动成功,52-启动失败,60-停止中,61-停止成功,62-停止失败,70-批量发布中,71-批量发布成功,72-批量发布失败',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `RESOURCE_PK` (`ID`) USING BTREE
) COMMENT='组件资源表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sv_version`;
CREATE TABLE `sv_version` (
  `ID` varchar(64) NOT NULL COMMENT '资源版本主键',
  `RESOURCE_ID` varchar(64) NOT NULL COMMENT '资源主键',
  `RESOURCE_PATH` varchar(300) DEFAULT NULL COMMENT '仓库资源存储路径',
  `VERSION_DESC` varchar(200) DEFAULT NULL COMMENT '资源版本描述',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '资源版本创建时间',
  `DEPLOY_TIMEOUT` int(10) DEFAULT '60000' COMMENT '资源部署超时时间，暂未使用',
  `START_TIMEOUT` int(10) DEFAULT '60000' COMMENT '资源启动超时时间，暂未使用',
  `STOP_TIMEOUT` int(10) DEFAULT '60000' COMMENT '资源停止超时时间，暂未使用',
  `DESTROY_TIMEOUT` int(10) DEFAULT '60000' COMMENT '资源卸载超时时间，暂未使用',
  `VERSION_NAME` varchar(100) DEFAULT NULL COMMENT '资源版本名',
  `ACCESS_PORT` int(11) DEFAULT NULL COMMENT '访问端口',
  `START_SCRIPT_PARAM` varchar(300) DEFAULT NULL COMMENT '启动参数',
  `REGISTRY_ID` int(20) DEFAULT NULL COMMENT '仓库id，3-ftp仓库',
  `MD5` varchar(64) DEFAULT NULL COMMENT '组件包的MD5',
  `STATUS` varchar(20) DEFAULT '00' COMMENT '组件版本状态 00-初始状态,01-测试通过,10-部分测试通过,11-测试未通过,30-发布中,31-发布成功,32-发布失败,40-回退中,41-回退成功,42-回退失败,50-启动中,51-启动成功,52-启动失败,60-停止中,61-停止成功,62-停止失败',
  `TYPE` int(1) NOT NULL COMMENT '1-salt组件,2-通用组件',
  `INPUT` text COMMENT '输入参数',
  `OUTPUT` text COMMENT '输出参数',
  `VERSIONNUM` int(11) default 0 COMMENT '版本号',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `VERSION_PK` (`ID`) USING BTREE
) COMMENT='组件版本表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sv_version_flow`;
CREATE TABLE `sv_version_flow` (
  `VERSIONID` varchar(64) NOT NULL,
  `FLOWTYPE` varchar(64) NOT NULL,
  `FLOWID` bigint(64) NOT NULL,
  PRIMARY KEY (`VERSIONID`,`FLOWTYPE`,`FLOWID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ma_blueprint`;
CREATE TABLE ma_blueprint (
	`BLUEPRINT_ID` varchar(50) NOT NULL COMMENT '蓝图ID',
	`BLUEPRINT_NAME` varchar(50) NOT NULL COMMENT '蓝图名称',
	`USER_ID` int(11) NOT NULL COMMENT '所属用户',
	`BLUEPRINT_INFO` text COMMENT '蓝图信息',
	PRIMARY KEY (`BLUEPRINT_ID`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `ma_blueprint_type`;

CREATE TABLE `ma_blueprint_type` (
  `ID` varchar(20) NOT NULL COMMENT 'ID',
  `BLUEPRINT_INSTANCE_ID` varchar(50) NOT NULL COMMENT '关联蓝图ID',
  `FLOW_ID` bigint(20) NOT NULL COMMENT '工作流流程ID',
  `FLOW_TYPE` varchar(200) NOT NULL COMMENT '流程类型',
  `FLOW_NAME` varchar(200) COMMENT '流程名称',
  `FLOW_INFO` text COMMENT '工作流流程信息',
  `FLOW_INFO_GROUP` text COMMENT 'cd组节点流程信息',
  `APP_NAME` varchar(200) COMMENT '组件名称',
  PRIMARY KEY (`ID`)
) COMMENT='蓝图流程表' ENGINE=InnoDB AUTO_INCREMENT=4189 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `ma_blueprint_instance` */

DROP TABLE IF EXISTS `ma_blueprint_instance`;
CREATE TABLE IF NOT EXISTS `ma_blueprint_instance` (
  `ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `INSTANCE_ID` varchar(50) NOT NULL COMMENT '蓝图实例ID',
  `INSTANCE_NAME` varchar(100) NOT NULL COMMENT '蓝图实例名称',
  `DEPLOYID` varchar(32) DEFAULT NULL,
  `STARTID` varchar(32) DEFAULT NULL,
  `STOPID` varchar(32) DEFAULT NULL,
  `DESTROYID` varchar(32) DEFAULT NULL,
  `ROLLBACKID` varchar(32) DEFAULT NULL,
  `UPGRADEID` varchar(32) DEFAULT NULL,
  `SNAPSHOTID` varchar(32) DEFAULT NULL,
  `INFO` text COMMENT '蓝图实例信息',
  `USERID` varchar(32) DEFAULT NULL COMMENT '用户id',
  `INSTANCE_DESC` varchar(255) DEFAULT NULL COMMENT '蓝图实例描述',
  `RESOURCE_POOL_CONFIG` text COMMENT '蓝图实例资源池配置',
  `KEY_VALUE` text COMMENT '蓝图实例配置',
  `TEMPLATE_ID` varchar(50) DEFAULT NULL COMMENT '蓝图模板ID',
  `REDUCE_POOL_CONFIG` text DEFAULT NULL COMMENT '收缩执行的资源池配置',
  PRIMARY KEY (`ID`)
) COMMENT='蓝图实例表' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `app_config_version`;
CREATE TABLE `app_config_version` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `configId` int(32) DEFAULT NULL,
  `version` varchar(64) DEFAULT NULL,
  `last_update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1287 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `app_configs`;
CREATE TABLE `app_configs` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `USERID` int(32) DEFAULT NULL,
  `type` varchar(32) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1133 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `app_configs_instance`;
CREATE TABLE `app_configs_instance` (
  `id` bigint(64) NOT NULL AUTO_INCREMENT,
  `versionId` int(32) NOT NULL COMMENT '配置id',
  `key` varchar(100) DEFAULT NULL COMMENT '键',
  `value` varchar(300) DEFAULT NULL COMMENT '值',
  `type` varchar(32) DEFAULT NULL,
  `description` text COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5477 DEFAULT CHARSET=utf8;


/*Table structure for table `ma_application_flow_op` */

DROP TABLE IF EXISTS `ma_application_flow_op`;

CREATE TABLE `ma_application_flow_op` (
  `id` varchar(64) NOT NULL,
  `flow_id` varchar(64) DEFAULT NULL COMMENT '流程id',
  `app_id` int(11) NOT NULL COMMENT '组件id',
  `type` varchar(32) NOT NULL COMMENT '流程类型',
  `json` text COMMENT '流程信息',
  `user_id` int(11) DEFAULT NULL COMMENT '操作用户',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `ma_blueprint_template` */

DROP TABLE IF EXISTS `ma_blueprint_template`;

CREATE TABLE `ma_blueprint_template` (
  `ID` varchar(50) NOT NULL COMMENT '蓝图模板ID',
  `NAME` varchar(50) NOT NULL COMMENT '蓝图模板名称',
  `USER_ID` int(20) NOT NULL COMMENT '所属用户ID',
  `INFO` text COMMENT '蓝图模板信息',
  `DESC` varchar(50) DEFAULT NULL COMMENT '描述信息',
  `CREATE_TIME` TIMESTAMP NOT NULL DEFAULT 0 COMMENT '创建时间',
  `UPDATE_TIME` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`ID`)
) COMMENT='蓝图模板信息表' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `ma_flow_log` */

DROP TABLE IF EXISTS `ma_flow_log`;

CREATE TABLE `ma_flow_log` (
  `id` varchar(64) NOT NULL COMMENT '插件节点或开始节点的tokenId',
  `state` varchar(64) DEFAULT NULL COMMENT '节点执行的结果，暂未使用',
  `flowInstanceId` varchar(64) DEFAULT NULL COMMENT '节点所属流程的实例Id,暂未使用',
  `flowNodeId` varchar(64) DEFAULT NULL COMMENT '节点的nodeId(对应工作流中node表)',
  `log` longtext COMMENT '节点的日志详情',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) COMMENT='节点的日志表' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `ma_resource_script`;
CREATE TABLE `ma_resource_script` (
  `ID` varchar(64) NOT NULL COMMENT 'uuid主键',
  `RESOURCE_NAME` varchar(50) DEFAULT NULL COMMENT '资源名称',
  `RESOURCE_DESC` varchar(250) DEFAULT NULL COMMENT '资源描述',
  `RESOURCE_PATH` varchar(300) DEFAULT NULL COMMENT '仓库资源存储路径',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '资源创建时间',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `RESOURCE_PK` (`ID`) USING BTREE
) COMMENT='脚本资源表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sv_package`;
CREATE TABLE `sv_package` (
  `ID` varchar(64) NOT NULL COMMENT 'uuid主键',
  `RESOURCE_NAME` varchar(250) DEFAULT NULL COMMENT '工件包名称',
  `RESOURCE_DESC` varchar(250) DEFAULT NULL COMMENT '工件包描述',
  `RESOURCE_PATH` varchar(300) DEFAULT NULL COMMENT '工件包存储路径',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '工件包创建时间',
  `RESOURCE_SIZE` bigint(50) DEFAULT 0 COMMENT '工件包大小单位B',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `RESOURCE_PK` (`ID`) USING BTREE
) COMMENT='工件包资源表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sv_flow`;
CREATE TABLE `sv_flow` (                                                                        
`ID` varchar(20) NOT NULL COMMENT 'ID',                                                                        
`RESOURCE_ID` varchar(64) NOT NULL COMMENT '资源ID',                                  
`FLOW_TYPE` varchar(16) NOT NULL COMMENT '流程类型 deploy部署 start启动 stop停止 destroy卸载 upgrade升级 rollback回滚 snapshot快照',  
`FLOW_NAME` varchar(320) NOT NULL COMMENT '流程名称',                                 
`FLOW_ID` bigint(64) NOT NULL COMMENT '工作流流程ID',
`FLOW_INFO` text NOT NULL COMMENT '工作流流程信息',
`FLOW_INFO_CONDITION` text NOT NULL COMMENT 'cd单一判断流程信息',
PRIMARY KEY (`ID`),                                                                               
UNIQUE KEY `PK_SV_FLOW` (`ID`) USING BTREE                                                       
) COMMENT='组件流程表' ENGINE=InnoDB DEFAULT CHARSET=utf8; 

/*Table structure for table `policy_child_info` */

DROP TABLE IF EXISTS `policy_child_info`;

CREATE TABLE `policy_child_info` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `text` varchar(100) NOT NULL COMMENT '菜单名称',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标标志',
  `sref` varchar(100) DEFAULT NULL COMMENT '链接，暂时未使用',
  `type` varchar(100) DEFAULT NULL COMMENT '菜单类型（页面使用）',
  `parent_id` bigint(10) NOT NULL COMMENT '父级菜单id',
  PRIMARY KEY (`id`)
) COMMENT='二级菜单表' ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

/*Data for the table `policy_child_info` */

INSERT INTO `policy_child_info` (`id`, `text`, `icon`, `sref`, `type`, `parent_id`) VALUES
	(3, '权限管理', 'icon-shield', 'app.limit_manage.user', 'limit', 11),
	(4,'部署环境管理','icon-grid','app.cluster_manage','cluster',3),
	(5,'通用组件管理','fa fa-cube','app.component_manage','component',5),
	(6, '蓝图管理', 'icon-drawer', 'app.blueprint_manage', 'blueprint', 4),
	(7, '蓝图实例', 'fa fa-list-alt', 'app.blueprint_ins_manage', 'blueprint_ins', 4),
	(8, '插件管理', 'fa fa-cubes', 'app.plugin_manage', 'plugin', 11),
	(10, '工件管理', 'icon-speedometer', 'app.workpiece_package_manage', 'workpiece_package_manage', 11),
	(15, '脚本管理', 'fa fa-file-text', 'app.script_manage', 'script', 11),
    (16,'定制管理','fa fa-th','app.custom_manage','custom',11),
    (17,'定时任务','icon-clock','app.listQuartz','quartz',11),
    (18,'标签管理','fa fa-tag','app.label_manage','label',11),
    (19,'操作审计','fa fa-search-plus','app.operational','operational',11),
--    (20,'我的审批','fa fa-file-o','app.myApprove','myApprove',12),
--    (21,'发布班车','fa fa-bus','app.bus_manage','bus',13),
--    (22,'发布任务','fa fa-tasks','app.release_manage','release',13),
    (23,'发布','icon-rocket','app.release_ins','release_ins',15),
    (25,'任务','fa fa-tasks','app.release_task_manage','task',15),
    (27,'集成','icon-size-actual','app.release_integration_manage','integration',15),
    (29,'生命周期','fa fa-repeat','app.lifecycles','lifecycles',15),
    (31,'清单','icon-list','app.release_list_manage','list',15),
    -- (35,'baidu','icon-magnifier','http://www.baidu.com','s',11),
    (37,'应用发布策略','glyphicon glyphicon-sound-stereo','app.release_strategy_manage','release_strategy',15);
-- (11,'资源映射管理','icon-home','app.resourcemap_list','resourcemap',3)
/*Table structure for table `policy_info` */

DROP TABLE IF EXISTS `policy_info`;

CREATE TABLE `policy_info` (
  `page_id` bigint(10) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `text` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '菜单名称',
  `icon` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '菜单图标',
  `sref` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '链接暂时未使用',
  `title` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '菜单标题（一般和名称一致）',
  `type` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '菜单类型',
  `serialNum` bigint(10) DEFAULT NULL COMMENT '菜单优先级，暂未使用',
  PRIMARY KEY (`page_id`)
) COMMENT='一级菜单表' ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

/*Data for the table `policy_info` */

INSERT INTO `policy_info` (`page_id`, `text`, `icon`, `sref`, `title`, `type`, `serialNum`) VALUES
	(2, '系统概述', 'icon-social-dropbox', 'app.summary_manage', '系统概述', 'summary_manage', 0),
	(3, '部署环境管理', 'cd-ic_resource1', 'app.cluster_manage', '部署环境管理', 'limit1', 0),
	(4, '应用蓝图管理', 'cd-ic_use1', '#', '应用蓝图管理', '""', 0),
	(5, '组件管理', 'cd-ic_module2', 'app.component_manage', '组件管理', '""', 0),
	(10, '仪表盘', 'cd-ic_meter2', 'app.statistics_manage', '仪表盘', 'statistics', 0),
	(11, '系统配置', 'cd-ic_set2', '#', '系统配置', '""', 0),
--	(12, '审批管理', 'fa fa-files-o', 'app.approve_manage', '审批管理', 'approve', 0),
--	(13, '发布任务管理', 'fa fa-send-o', '#', '发布任务管理', 'release', 0),
	(15, '发布管理', 'icon-social-twitter', '#', '发布管理', 'releaseManage', 0);
/*Table structure for table `policy_menu` */

DROP TABLE IF EXISTS `policy_menu`;

CREATE TABLE `policy_menu` (
  `id` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '菜单Id',
  `title` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '按钮名称',
  `sref` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '按钮描述',
  PRIMARY KEY (`id`)
) COMMENT='三级菜单表（按钮菜单）' ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `policy_menu` */

-- insert  into `policy_menu`(`id`,`title`,`sref`) values ('10_1','创建策略','监控告警'),('10_10','更新策略通知人','监控告警'),('10_11','删除策略通知人','监控告警'),('10_12','修改策略资源','监控告警'),('10_13','创建通知人','监控告警'),('10_14','更新通知人','监控告警'),('10_15','删除通知人','监控告警'),('10_2','编辑策略','监控告警'),('10_3','删除策略','监控告警'),('10_4','启动策略','监控告警'),('10_5','停止策略','监控告警'),('10_6','添加策略规则','监控告警'),('10_7','更新策略规则','监控告警'),('10_8','删除策略规则','监控告警'),('10_9','修改策略通知人','监控告警'),('11_1','添加主机','平台组件监控'),('11_2','删除配置','平台组件监控'),('12_1','添加自定义数据源','自定义数据源'),('12_2','删除自定义数据','自定义数据源'),('13_1','新建配置','配置中心'),('13_10','删除配置版本','配置中心'),('13_2','删除配置','配置中心'),('13_3','配置下发','配置中心'),('13_4','配置导入','配置中心'),('13_5','配置导出','配置中心'),('13_6','保存配置参数','配置中心'),('13_7','编辑配置参数','配置中心'),('13_8','删除配置参数','配置中心'),('13_9','添加配置版本','配置中心'),('2_1','IAAS删除','IAAS接入页面'),('2_2','IAAS接入','IAAS接入页面'),('2_3','IAAS更新','IAAS接入页面'),('2_4','加入集群','IAAS接入页面'),('3_1','新建用户','权限页面'),('3_2','新建角色','权限页面'),('3_3','删除角色','权限页面'),('3_4','分配权限','权限页面'),('3_5','新建菜单','权限页面'),('3_6','删除菜单','权限页面'),('3_7','编辑菜单','权限页面'),('4_1','新建集群','集群管理'),('4_10','加入集群','集群管理'),('4_2','删除集群','集群管理'),('4_3','删除主机','集群管理'),('4_4','添加用户','集群管理'),('4_5','删除用户','集群管理'),('4_6','修改集群','集群管理'),('4_7','修改主机','集群管理'),('4_8','更改用户配额','集群管理'),('4_9','解绑主机告警策略','集群管理'),('5_1','创建应用','应用管理'),('5_10','版本部署','应用管理'),('5_11','版本删除','应用管理'),('5_12','解绑应用告警策略','应用管理'),('5_13','实例删除','应用管理'),('5_14','日志查询','应用管理'),('5_15','日志导出','应用管理'),('5_16','登录控制台','应用管理'),('5_17','创建负载','应用管理'),('5_18','更新负载','应用管理'),('5_19','删除负载','应用管理'),('5_2','删除应用','应用管理'),('5_20','删除端口','应用管理'),('5_21','创建端口','应用管理'),('5_22','添加外部网络','应用管理'),('5_23','删除外部网络','应用管理'),('5_24','创建伸缩策略','应用管理'),('5_25','暂停任务','应用管理'),('5_26','恢复任务','应用管理'),('5_27','删除任务','应用管理'),('5_3','导出应用','应用管理'),('5_4','启动应用','应用管理'),('5_5','更新应用','应用管理'),('5_6','停止应用','应用管理'),('5_7','维护应用','应用管理'),('5_8','升级.回滚应用','应用管理'),('5_9','卸载应用','应用管理'),('6_1','创建模板','应用模板'),('6_2','部署私有模板','应用模板'),('6_3','删除私有模板','应用模板'),('6_4','部署公共模板','应用模板'),('6_5','删除公共模板','应用模板'),('6_6','已部署模板启动','应用模板'),('6_7','已部署模板停止','应用模板'),('6_8','已部署模板卸载','应用模板'),('6_9','已部署模板删除','应用模板'),('7_1','创建任务','任务管理'),('7_2','更新任务','任务管理'),('7_3','删除任务','任务管理'),('7_4','执行任务','任务管理'),('7_5','执行中的任务卸载','任务管理'),('8_1','创建镜像','镜像仓库'),('8_2','编辑镜像版本','镜像仓库'),('8_3','删除镜像版本','镜像仓库'),('9_1','创建任务','持续集成'),('9_2','查看任务','持续集成'),('9_3','触发build','持续集成'),('9_4','查看build','持续集成'),('9_5','删除任务','持续集成'),('9_6','查看日志','持续集成');
insert  into `policy_menu`(`id`,`title`,`sref`) values 
('3_1','新建用户','权限管理'),('3_2','新建角色','权限管理'),('3_3','删除角色','权限管理'),('3_4','分配权限','权限管理'),('3_5','新建菜单','权限管理'),('3_6','删除菜单','权限管理'),('3_7','编辑菜单','权限管理'),
-- ('8_1','创建通用组件','通用组件管理'),('8_2','导出组件','通用组件管理'),('8_3','导入组件','通用组件管理'),('8_4','添加版本','通用组件管理'),('8_5','添加过程','通用组件管理'),('8_6','查看过程','通用组件管理'),('8_7','删除','通用组件管理'),('8_8','添加版本','通用组件详情'),('8_9','更新版本','通用组件详情'),('8_10','配置模板映射','通用组件详情'),('8_11','设置状态','通用组件详情'),
('6_1','创建蓝图','蓝图管理'),('6_2','导出蓝图','蓝图管理'),('6_3','导入蓝图','蓝图管理'),('6_4','创建蓝图实例','蓝图管理'),('6_5','查看','蓝图管理'),('6_6','编辑','蓝图管理'),('6_7','删除','蓝图管理'),('6_8','添加过程','蓝图管理'),('6_9','查看过程','蓝图管理'),
('7_1','蓝图实例展示','蓝图实例管理'),('7_2','删除蓝图实例','蓝图实例管理'),('7_3','查看组件','蓝图实例管理'),('7_4','查看配置','蓝图实例管理'),('7_5','日志管理','蓝图实例管理'),('7_6','资源映射','蓝图实例管理'),('7_7','执行过程','蓝图实例管理'),('7_8','配置对比','蓝图实例管理'),
('8_1','添加','插件管理'),('8_2','删除','插件管理'),('8_3','更新','插件管理'),('8_4','详情','插件管理'),
('10_1','添加','工件管理'),('10_2','删除','工件管理'),('10_3','导入工件','工件管理'),('10_4','导出工件','工件管理'),('10_5','下载','工件管理'),
('11_1','新建资源池','资源环境管理'),('11_2','删除资源池','资源环境管理'),('11_3','添加主机','资源环境管理'),('11_4','管理主机','资源环境管理'),('11_5','删除主机','资源环境管理'),
('15_1','添加','脚本管理'),('15_2','删除','脚本管理');

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` varchar(100) NOT NULL COMMENT '角色ID',
  `name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`)
) COMMENT='角色信息表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role` */

-- insert  into `role`(`id`,`name`) values ('1','管理员'),('2','运维人员'),('3','测试/开发'),('4','门户');
insert  into `role`(`id`,`name`) values ('1','管理员'),('2','运维人员');
/*Table structure for table `role_menu_mapping` */

DROP TABLE IF EXISTS `role_menu_mapping`;

CREATE TABLE `role_menu_mapping` (
  `menu_id` varchar(100) NOT NULL COMMENT '菜单ID',
  `role_id` varchar(100) NOT NULL COMMENT '角色ID'
) COMMENT='角色菜单映射表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role_menu_mapping` */

-- insert  into `role_menu_mapping`(`menu_id`,`role_id`) values ('10_1','4'),('6_1','5'),('6_2','5'),('10_1','2'),('4_1','1'),('4_10','1'),('4_2','1'),('4_3','1'),('4_4','1'),('4_5','1'),('4_6','1'),('4_7','1'),('4_8','1'),('4_9','1'),('5_1','1'),('5_10','1'),('3_1','1'),('3_2','1'),('3_3','1'),('3_4','1'),('3_5','1'),('3_6','1'),('3_7','1'),('4_1','7'),('4_10','7'),('4_2','7'),('4_3','7'),('4_4','7'),('4_5','7'),('4_6','7'),('4_7','7'),('4_8','7'),('4_9','7'),('4_10','8'),('4_1','6'),('4_10','6'),('4_2','6');
insert  into `role_menu_mapping`(`menu_id`,`role_id`) values
('3_1','1'),('3_2','1'),('3_3','1'),('3_4','1'),('3_5','1'),('3_6','1'),('3_7','1'),
('6_1','1'),('6_2','1'),('6_3','1'),('6_4','1'),('6_5','1'),('6_6','1'),('6_7','1'),('6_8','1'),('6_9','1'),
('7_1','1'),('7_2','1'),('7_3','1'),('7_4','1'),('7_5','1'),('7_6','1'),('7_7','1'),('7_8','1'),
('8_1','1'),('8_2','1'),('8_3','1'),('8_4','1'),
('9_1','1'),('9_2','1'),('9_3','1'),('9_4','1'),('9_5','1'),('9_6','1'),('9_7','1'),('9_8','1'),('9_9','1'),
('10_1','1'),('10_2','1'),('10_3','1'),('10_4','1'),('10_5','1'),
('11_1','1'),('11_2','1'),('11_3','1'),('11_4','1'),('11_5','1'),
('15_1','1'),('15_2','1');
/*Table structure for table `role_policy_child_mapping` */

DROP TABLE IF EXISTS `role_policy_child_mapping`;

CREATE TABLE `role_policy_child_mapping` (
  `role_id` varchar(200) NOT NULL COMMENT '角色id',
  `policy_child_id` bigint(10) NOT NULL COMMENT '二级菜单id',
  PRIMARY KEY (`role_id`,`policy_child_id`)
) COMMENT='角色和二级菜单关联表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role_policy_child_mapping` */

-- insert  into `role_policy_child_mapping`(`role_id`,`policy_child_id`) values ('1',1),('1',2),('1',3),('1',4),('1',5),('1',6),('1',7),('1',8),('1',9),('1',10),('1',11),('1',13),('1',15),('2',1),('2',4),('2',5),('2',6),('2',7),('2',8),('2',9),('2',10),('3',1),('3',4),('3',5),('3',6),('3',7),('3',8),('3',9),('3',10),('3',14),('4',3),('4',4),('4',5),('4',6),('4',7),('4',8),('4',9),('4',10),('6',3),('6',4),('6',8),('6',11),('7',4),('7',11);
insert  into `role_policy_child_mapping`(`role_id`,`policy_child_id`) 
values ('1',3),('1',4),('1',5),('1',6),('1',7),('1',8),('1',10),('1',15),('1',16),('1',17),('1',18),('1',19),('1',23),('1',25),('1',27),('1',29),('1',37),
('2',4),('2',5),('2',6),('2',7),('2',8),('2',9),('2',10),('2',15),('2',16),('2',17),('2',18),('2',19),('2',23),('2',25),('2',27),('2',29),('2',37)
-- ('3',6),('3',7),('3',8),('3',9),('3',10),
-- ('4',3),('4',6),('4',7),('4',8),('4',9),('4',10),('4',11),('4',15)
;

/*Table structure for table `role_policy_infomapping` */

DROP TABLE IF EXISTS `role_policy_infomapping`;

CREATE TABLE `role_policy_infomapping` (
  `role_id` bigint(20) NOT NULL COMMENT '角色id',
  `page_id` bigint(20) NOT NULL COMMENT '一级菜单id',
  PRIMARY KEY (`role_id`,`page_id`)
) COMMENT='角色和一级菜单关联表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role_policy_infomapping` */

-- insert  into `role_policy_infomapping`(`role_id`,`page_id`) values (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(2,2),(2,4),(2,5),(2,6),(2,7),(2,8),(2,12),(2,13),(3,1),(3,2),(3,5),(3,6),(3,7),(3,8),(3,9),(3,10),(4,1),(4,2),(4,3),(4,4),(4,5),(4,6),(4,7),(4,8),(4,9),(4,10),(4,11),(4,12),(4,13),(5,2),(5,4),(5,5),(5,6),(5,11),(8,2),(8,4);
insert  into `role_policy_infomapping`(`role_id`,`page_id`) values (1,2),(1,3),(1,4),(1,5),(1,10),(1,11),(1,15),(2,2),(2,3),(2,4),(2,5),(2,10),(2,11),(2,15);
/*记录蓝图实例和流程实例的映射关系*/
DROP TABLE IF EXISTS `ma_flowInstance`;
CREATE TABLE `ma_flowInstance` (
  `ID` varchar(64) NOT NULL COMMENT '蓝图实例Id',
  `INSTANCEID` varchar(64) NOT NULL COMMENT '蓝图执行的实例Id(对应工作流的实例Id)',
  `START_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '蓝图实例的执行时间',
  `APPNAME` varchar(64) COMMENT '组件维护时，记录的组件名称',
  `POOLCONFIG` text COMMENT '蓝图实例的资源池信息',
  `FLOWNAME` varchar(64) COMMENT '蓝图实例的流程名',
  `FLOWID` varchar(64) COMMENT '蓝图实例的流程Id',
  `APPVERSION` varchar(2000) default null COMMENT '流程实例中组件的版本信息'
)COMMENT='蓝图实例的执行记录表';


DROP TABLE IF EXISTS `ad_cluster`;
CREATE TABLE IF NOT EXISTS `ad_cluster` (
  `id` varchar(100) NOT NULL COMMENT '环境ID',
  `name` varchar(100) NOT NULL COMMENT '环境名称',
  `user_id` bigint(20) NOT NULL COMMENT '环境所属用户ID',
  PRIMARY KEY (`id`)
) COMMENT='环境信息表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `ad_cluster` (`id`, `name`, `user_id`) VALUES
	('615a6762-2589-441f-89cc-4033cc7a91df', 'COP', 1),
	('2c0117c6-a8c0-454c-803d-24f190d9d18e', '生产', 1),
	('47aecac4-e07c-46c8-9ab8-f33e93be3982', '准生产', 1),
	('f90774a4-53ad-4bba-bfec-ec3c41663501', 'SIT', 1),
	('fabac25b-24bd-4827-995b-989ab38fe160', 'UAT', 1);

-- 菜单、按钮资源映射表
DROP TABLE IF EXISTS `policy_resource_mapping`;

CREATE TABLE `policy_resource_mapping` (
  `policy_id` varchar(60) NOT NULL COMMENT '一级菜单ID',
  `resource_url` varchar(200) NOT NULL COMMENT 'url',
  PRIMARY KEY (`policy_id`,`resource_url`)
) COMMENT='一级菜单和url关联表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `policy_child_resource_mapping`;

CREATE TABLE `policy_child_resource_mapping` (
  `policy_child_id` varchar(70) NOT NULL COMMENT '二级菜单Id',
  `resource_url` varchar(100) NOT NULL COMMENT '二级菜单对应的url',
  PRIMARY KEY (`policy_child_id`,`resource_url`)
) COMMENT='二级菜单与url的关联表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `menu_resource_mapping`;

CREATE TABLE `menu_resource_mapping` (
  `menu_id` varchar(70) NOT NULL COMMENT '三级菜单（按钮）的id',
  `resource_url` varchar(100) NOT NULL COMMENT '三级菜单（按钮）关联的后台URL',
  PRIMARY KEY (`menu_id`,`resource_url`)
) COMMENT='按钮和请求url的关联关系表' ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `sv_ftl`;
CREATE TABLE `sv_ftl` (
  `ID` varchar(64) DEFAULT NULL COMMENT '主键id',
  `VERSION_ID` varchar(64) DEFAULT NULL COMMENT '版本id',
  `FTL_NAME` varchar(64) DEFAULT NULL COMMENT '文件名',
  `TEMPLATES` varchar(255) DEFAULT NULL COMMENT '模板映射',
  `FTL_TEXT` text COMMENT '文件内容'
) COMMENT='组件模板文件表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP TABLE IF EXISTS`ma_label_type`; -- 标签类型表
CREATE TABLE `ma_label_type`(
`CODE` bigint(20) NOT NULL COMMENT '标签类型标识号',
`NAME` varchar(64) NOT NULL COMMENT '标签类型名',
PRIMARY KEY(code)
)COMMENT='标签类型资源表' ENGINE=InnoDB DEFAULT CHARSET=utf8;
insert into ma_label_type(`CODE`,`NAME`) values(3,'组件标签'),(4,'插件标签');

DROP TABLE IF EXISTS`ma_label`;    -- 标签表
CREATE TABLE `ma_label`(              
`ID` varchar(64) NOT NULL COMMENT '标签主键',
`NAME` varchar(64) NOT NULL COMMENT '标签名',
`DESCRIPTION` varchar(64) NULL COMMENT '标签描述',
`UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
`TYPE_CODE` bigint(20) NOT NULL COMMENT '标签类型码',
PRIMARY KEY(ID)
)COMMENT='标签表' ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS`ma_resource_label`; -- 标签和资源的关联关系
CREATE TABLE `ma_resource_label`(
`LABEL_ID` varchar(64) NOT NULL COMMENT '标签主键',
`RESOURCE_ID` varchar(64) NOT NULL COMMENT '资源主键',
PRIMARY KEY(LABEL_ID,RESOURCE_ID)
)COMMENT='标签和资源的关联关系' ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ma_audit`;
CREATE TABLE `ma_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID 主键',
  `userId` varchar(64) COMMENT '用户名',
  `resourceType` varchar(64) COMMENT '资源类型',
  `resourceName` varchar(64) COMMENT '资源名称',
  `operateType` varchar(16) COMMENT '操作类型',
  `operateResult` varchar(16) COMMENT '操作结果 0失败 1成功',
  `operateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作日期',
  `detail` text COMMENT '详细信息',
   PRIMARY KEY (`id`)
) COMMENT='平台操作审计表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sv_version_audit`;
	create table `sv_version_audit`(
	`id` varchar(64) COMMENT '主键uuid',
	`userId` varchar(64) COMMENT '用户名',
	`resourceVersionId` varchar(64) COMMENT '补丁版本主键',
	`opType` varchar(20) COMMENT '操作类型 新建、发布、状态更新、追加描述、回退 、启动、停止、批量发布',
	`status` varchar(20) COMMENT '状态类型 00-新建、01-测试已通过、10-部分测试通过、11-测试未通过、30-发布中、31-发布成功、32-发布失败、40-回退中、41-回退成功、42-回退失败、50-启动中、51-启动成功、52-启动失败、60-停止中、61-停止成功、62-停止失败、70-批量发布中、71-批量发布成功、72-批量发布失败',
	`updateTime` dateTime default NULL COMMENT '更新时间',
	`description` varchar(64) COMMENT '描述',
	PRIMARY KEY(id)
)COMMENT='主键版本操作审计表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS release_task;
create table release_task (
	ID int(11) NOT NULL AUTO_INCREMENT COMMENT '任务主键ID',
	NAME varchar(120) NOT NULL COMMENT '任务名称',
	INITIATOR varchar(32) NOT NULL COMMENT '发起人',
	SYSTEM varchar(100) DEFAULT NULL COMMENT '系统名称',
	MODULE varchar(100) DEFAULT NULL COMMENT '模块名称',
	START_TIME datetime NOT NULL COMMENT '开始时间',
	STOP_TIME datetime NOT NULL COMMENT '截止时间',
	END_TIME datetime NULL COMMENT '结束时间',
	DESCRIPTION varchar(1000) DEFAULT NULL COMMENT '功能描述',
	STATUS varchar(2) DEFAULT '00' COMMENT '任务状态 00初始状态 01审批中 02任务执行中 03任务执行成功 04任务执行失败 05审批通过 06审批拒绝',
	BLURPRINT_TEMPLATE varchar(50) NOT NULL COMMENT '蓝图模板',
	BLUEPRINT_INSTANCE varchar(50) NOT NULL COMMENT '蓝图实例',
	BLUEPRINT_FLOW varchar(20) NOT NULL COMMENT '蓝图流程',
	BLUEPRINT_FLOW_INSTANCE varchar(20) DEFAULT NULL COMMENT '蓝图流程实例',
	BUS_ID int(11) NOT NULL COMMENT '班车ID',
	DEPEND_ID int(11) NOT NULL DEFAULT 0 COMMENT '依赖任务',
	AUTO_EXECUTE tinyint(1) DEFAUlT 0 COMMENT '自动执行 0-非自动 1-自动',
	CRONEXPRESSION varchar(120) DEFAULT NULL COMMENT '定时任务表达式',
	PRIMARY KEY (ID),
	UNIQUE KEY PK_RELEASE_TASK (ID)
)COMMENT='发布任务表' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS release_approval;
create table release_approval (
	ID int(11) NOT NULL AUTO_INCREMENT COMMENT '发布审批主键',
	TASK_ID int(11) NOT NULL COMMENT '任务ID',
	APPROVER varchar(32) NOT NULL COMMENT '审批人',
	PERFORMER varchar(32) NULL COMMENT '执行人',
	APPLY_TIME datetime NULL COMMENT '申请时间',
	APPROVE_TIME datetime NULL COMMENT '审批时间',
	APPROVE_ORDER int(2) NOT NULL COMMENT '审批顺序',
	STATUS varchar(2) DEFAULT '00' COMMENT '审批状态 00初始状态 01待审批 02审批通过 03审批拒绝',
	PRIMARY KEY (ID),
	UNIQUE KEY PK_RELEASE_APPROVAL (ID)
)COMMENT='发布审批表' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS release_bus;
create table release_bus (
	ID int(11) NOT NULL AUTO_INCREMENT COMMENT '班车主键',
	NAME varchar(120) NOT NULL COMMENT '班车名称',
	START_TIME datetime NOT NULL COMMENT '开始时间',
	STOP_TIME datetime NOT NULL COMMENT '结束时间',
	AUTHOR varchar(100) NOT NULL COMMENT '创建人',
	PRIMARY KEY (ID),
	UNIQUE KEY PK_RELEASE_BUS (ID)
)COMMENT='发布班车表' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;


CREATE TABLE QRTZ_JOB_DETAILS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    JOB_CLASS_NAME   VARCHAR(250) NOT NULL,
    IS_DURABLE VARCHAR(1) NOT NULL,
    IS_NONCONCURRENT VARCHAR(1) NOT NULL,
    IS_UPDATE_DATA VARCHAR(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE QRTZ_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT(13) NULL,
    PREV_FIRE_TIME BIGINT(13) NULL,
    PRIORITY INTEGER NULL,
    TRIGGER_STATE VARCHAR(16) NOT NULL,
    TRIGGER_TYPE VARCHAR(8) NOT NULL,
    START_TIME BIGINT(13) NOT NULL,
    END_TIME BIGINT(13) NULL,
    CALENDAR_NAME VARCHAR(200) NULL,
    MISFIRE_INSTR SMALLINT(2) NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    REPEAT_COUNT BIGINT(7) NOT NULL,
    REPEAT_INTERVAL BIGINT(12) NOT NULL,
    TIMES_TRIGGERED BIGINT(10) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CRON_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(200) NOT NULL,
    TIME_ZONE_ID VARCHAR(80),
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
  (          
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) 
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_BLOB_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CALENDARS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    CALENDAR_NAME  VARCHAR(200) NOT NULL,
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
);

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL, 
    PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_FIRED_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    ENTRY_ID VARCHAR(95) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    FIRED_TIME BIGINT(13) NOT NULL,
    SCHED_TIME BIGINT(13) NOT NULL,
    PRIORITY INTEGER NOT NULL,
    STATE VARCHAR(16) NOT NULL,
    JOB_NAME VARCHAR(200) NULL,
    JOB_GROUP VARCHAR(200) NULL,
    IS_NONCONCURRENT VARCHAR(1) NULL,
    REQUESTS_RECOVERY VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,ENTRY_ID)
);

CREATE TABLE QRTZ_SCHEDULER_STATE
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
    CHECKIN_INTERVAL BIGINT(13) NOT NULL,
    PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);

CREATE TABLE QRTZ_LOCKS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40) NOT NULL, 
    PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);

DROP TABLE IF EXISTS `release_lifecycle`;
CREATE TABLE IF NOT EXISTS `release_lifecycle` (
  `id` varchar(100) NOT NULL COMMENT 'ID',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发布生命周期';

DROP TABLE IF EXISTS `release_lifecycle_stage`;
CREATE TABLE IF NOT EXISTS `release_lifecycle_stage` (
  `id` varchar(100) NOT NULL COMMENT 'ID',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `release_lifecycle_id` varchar(100) NOT NULL COMMENT '发布生命周期ID',
  `pre_stage_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发布生命周期的阶段';

-- 发布和发布内容相关的表
DROP TABLE IF EXISTS `ma_release`;
CREATE TABLE `ma_release`(
	`id` varchar(64) COMMENT '主键uuid',
	`name` varchar(64) COMMENT '发布名称',
	`lifecycle_id` varchar(64) COMMENT '发布生命周期Id',
	`user_id` int(11) NOT NULL COMMENT '用户名',
	`update_time` dateTime default NULL COMMENT '更新时间',
	`description` varchar(64) COMMENT '描述',
	PRIMARY KEY(id)
)COMMENT='发布表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `release_apps`;
CREATE TABLE `release_apps`(
	`release_id` varchar(64) COMMENT '发布主键id',
	`resource_id` varchar(64) COMMENT '应用(组件)主键id'
)COMMENT='发布和应用关联表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `release_stage_env`;
CREATE TABLE `release_stage_env`(
	`id` varchar(64) COMMENT '主键uuid',
	`release_id` varchar(64) COMMENT '发布主键id',
	`lifecycle_stage_id` varchar(64) COMMENT '生命周期阶段id',
	`blueprint_ins_id` varchar(64) COMMENT '蓝图实例id',
	`blueprint_tmplate_id` varchar(64) COMMENT '蓝图模板id',
	`flow_id` varchar(64) COMMENT '蓝图模板过程id',
	`resource_id` varchar(64) COMMENT '应用(组件)id',
	`cluster_id` varchar(64) COMMENT '资源池id',
	`nodeIds` varchar(64) COMMENT 'node id数组',
	PRIMARY KEY(id)
)COMMENT='发布阶段环境关联关系表' ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS release_task_order;
create table release_task_order (
	`ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务主键ID',
	`NAME` varchar(200) NOT NULL COMMENT '任务名称',
	`TYPE` varchar(2) NOT NULL COMMENT '任务类型 01-需求 02-缺陷 03-优化 04-任务 05-其他',
	`LEVEL` varchar(2) NOT NULL COMMENT '任务优先级 01-紧急 02-非常高 03-高 04-中 05-低',
	`STATUS` varchar(2) NOT NULL COMMENT '任务状态 01-创建 02-更新 03-处理中 04-已解决 05-已关闭 06-重开启 07处理成功 08处理失败',
	`LABEL` varchar(100) DEFAULT NULL COMMENT '任务标签',
	`DEPEND_ID` varchar(200) DEFAULT '[]' COMMENT '依赖任务 可以多个 json数组',
	`PARENT_ID` int(11) DEFAULT -1 COMMENT '父任务',
	`SYSTEM_ID` varchar(50) NOT NULL COMMENT '系统ID',
	`SYSTEM_NAME` varchar(200) NOT NULL COMMENT '系统名称',
	`CREATER` varchar(100) NOT NULL COMMENT '创建人',
	`PRINCIPAL` varchar(100) NOT NULL COMMENT '负责人',
	`CREATE_TIME` datetime NOT NULL COMMENT '开始时间',
	`UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
	`EXPECT_TIME` datetime DEFAULT NULL COMMENT '计划完成时间',
	`AUTO_EXECUTE` tinyint(1) DEFAUlT 0 COMMENT '自动执行 0-手动 1-自动',
	`CRONEXPRESSION` varchar(120) DEFAULT NULL COMMENT '定时任务表达式',
	`DESCRIPTION` varchar(1000) DEFAULT NULL COMMENT '描述',
	`REMARK` varchar(1000) DEFAULT NULL COMMENT '备注',
	`SOURCE` varchar(2) NOT NULL COMMENT '任务来源 01-发布管理平台 02-redmine 03-jira 04-it作业管理平台 05-dragonFly 06-其他',
	`INTEGRATION_ID` int(11) DEFAULT NULL COMMENT '同步集成id',
	`ATTACHMENT` varchar(400) DEFAULT NULL COMMENT '附件',
	`INVENTORY_ID` varchar(400) DEFAULT '[]' COMMENT '所属清单 可以多个 json数组',
	`RELEASE_ID` varchar(100) DEFAULT NULL COMMENT '发布ID',
	`RELEASE_PHASE_ID` varchar(100) DEFAULT NULL COMMENT '发布阶段ID',
	`ENVIRONMENT_ID` varchar(50) DEFAULT NULL COMMENT '环境ID',
	`ENVIRONMENT_NAME` varchar(200) DEFAULT NULL COMMENT '环境名称',
	`SYSTEM_REQUIRE_ID` varchar(50) DEFAULT NULL COMMENT '系统需求ID',
	`SYSTEM_REQUIRE_NAME` varchar(200) DEFAULT NULL COMMENT '系统需求名称',
	`BUSINESS_REQUIRE_ID` varchar(50) DEFAULT NULL COMMENT '业务需求ID',
	`BUSINESS_REQUIRE_NAME` varchar(200) DEFAULT NULL COMMENT '业务需求名称',
	`CODE_BRANCH_NAME` varchar(200) DEFAULT NULL COMMENT '分支名称',
	`CODE_BASELINE_NAME` varchar(200) DEFAULT NULL COMMENT '基线名称',
	`BLUEPRINT_FLOW_INSTANCE` varchar(20) DEFAULT NULL COMMENT '蓝图流程实例',
	PRIMARY KEY (ID),
	UNIQUE KEY PK_RELEASE_TASK_ORDER (ID)
)COMMENT='发布任务变更表' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS release_integration;
create table release_integration (
	`ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '集成主键ID',
	`NAME` varchar(200) NOT NULL COMMENT '集成名称',
	`TYPE` varchar(2) NOT NULL COMMENT '集成类型 02-redmine 03-jira 04-it作业管理平台 05-dragonFly 06-其他',
	`SYSTEM_ID` varchar(100) NOT NULL COMMENT '系统id',
	`DESCRIPTION` varchar(1000) DEFAULT NULL COMMENT '描述',
	`URL` varchar(100) NOT NULL COMMENT '集成URL',
	`USER` varchar(100) NOT NULL COMMENT '集成用户',
	`PASSWORD` varchar(100) DEFAULT '' COMMENT '集成密码',
	`CREATER` varchar(100) NOT NULL COMMENT '创建人',
	`CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
	`UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
	`SYN_TIME` datetime DEFAULT NULL COMMENT '上次同步时间',
	PRIMARY KEY (ID),
	UNIQUE KEY PK_RELEASE_INTEGRATION (ID)
)COMMENT='发布集成表' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS release_inventory;
create table release_inventory (
	`ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务清单主键ID',
	`NAME` varchar(200) NOT NULL COMMENT '名称',
	`DESCRIPTION` varchar(1000) DEFAULT NULL COMMENT '描述',
	`CREATER` varchar(100) NOT NULL COMMENT '创建人',
	`PRINCIPAL` varchar(100) NOT NULL COMMENT '负责人',
	`CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
	`UPDATE_TIME` datetime DEFAULT NULL COMMENT '更新时间',
	`EXPECT_TIME` datetime NOT NULL COMMENT '期望完成时间',
	PRIMARY KEY (ID),
	UNIQUE KEY PK_RELEASE_INVENTORY (ID)
)COMMENT='发布清单表' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `application_release_strategy`;
CREATE TABLE IF NOT EXISTS `application_release_strategy` (
  `id` varchar(100) NOT NULL COMMENT '发布策略ID',
  `name` varchar(100) NOT NULL COMMENT '发布策略名称',
  `app_id` varchar(100) NOT NULL COMMENT '发布策略关联应用的ID',
  `task_status` varchar(2) NOT NULL COMMENT '发布策略关联任务状态',
  `cron` varchar(100) NOT NULL COMMENT '发布策略的cron表达式',
  `user_id` bigint(20) NOT NULL COMMENT '所属用户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用发布策略表';