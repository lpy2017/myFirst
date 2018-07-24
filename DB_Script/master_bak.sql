DROP TABLE IF EXISTS `ma_snapshot_blueinstance`;

CREATE TABLE `ma_snapshot_blueinstance` (
  `ID` VARCHAR(100) NOT NULL,
  `BLUE_INSTANCE_ID` INT(11) DEFAULT NULL COMMENT '蓝图实例id',
  `UPDATE_TIME` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建快照时间',
  `SNAPSHOT_INFO` TEXT COMMENT '快照信息描述',
  `USER_ID` INT(11) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `RESOURCE_PK` (`ID`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `ma_snapshot_application`;

CREATE TABLE `ma_snapshot_application` (
  `ID` varchar(100) NOT NULL COMMENT '快照ID 主键',
  `SNAPSHOT_NAME` varchar(100) DEFAULT NULL COMMENT '快照名称',
  `APP_ID` int(11) DEFAULT NULL COMMENT '组件ID',
  `APP_NAME` varchar(100) DEFAULT NULL COMMENT '组件名称',
  `SNAPSHOT_INFO` text COMMENT '组件快照信息',
  `BLUE_INSTANCE_ID` int(11) DEFAULT NULL COMMENT '蓝图实例id',
  `APP_KEY` bigint(20) DEFAULT NULL COMMENT '在蓝图中的键',
  `UPDATE_TIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建快照时间',
  `USER_ID` bigint(20) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PK_MA_SNAPSHOT_APPLICATION` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `ma_application`;

CREATE TABLE `ma_application` (
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
  `VERSIONID` varchar(50) DEFAULT NULL,
  `BLUE_INSTANCE_ID` int(11) DEFAULT NULL COMMENT '蓝图实例id',
  `KEY` bigint(20) DEFAULT NULL COMMENT '在蓝图中的键',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PK_MA_APPLICATION` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `ma_instance`;

CREATE TABLE `ma_instance` (
  `ID` varchar(64) NOT NULL COMMENT '实例id',
  `APP_ID` int(11) DEFAULT NULL COMMENT '组件id',
  `NODE_ID` varchar(64) DEFAULT NULL COMMENT '节点id',
  `DEPLOY_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '实例更新',
  `LXC_IP` varchar(16) DEFAULT '0.0.0.0' COMMENT '实例容器ip',
  `status` varchar(16) DEFAULT 'undeployed' COMMENT '实例状态',
  `RESOURCE_VERSION_ID` varchar(64) DEFAULT NULL COMMENT '资源版本id',
  `COMPONENT_INPUT` text COMMENT '组件input',
  `COMPONENT_INPUT_TEMP` text COMMENT '组件input 动态',
  `COMPONENT_OUTPUT` text COMMENT '组件output',
  `COMPONENT_OUTPUT_TEMP` text COMMENT '组件output 动态',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `PK_MA_INSTANCE` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
  `IP` varchar(50) DEFAULT NULL,
  `DESCRIPTION` text,
  `NAME` varchar(100) DEFAULT NULL,
  `TCP_PORT` bigint(22) DEFAULT NULL,
  `STATUS` varchar(20) DEFAULT NULL,
  `CPUCOUNT` bigint(22) DEFAULT NULL,
  `DISKSIZE` bigint(22) DEFAULT NULL,
  `HOSTNAME` varchar(50) DEFAULT NULL,
  `MEMORYSIZE` bigint(22) DEFAULT NULL,
  `ENNAME` varchar(50) DEFAULT NULL,
  `NODE_GROUP` varchar(50) DEFAULT NULL,
  `ISOLATA_STATE` varchar(1) DEFAULT NULL,
  `UPGRADING` tinyint(1) DEFAULT '0',
  `CLUSTERID` varchar(100) DEFAULT NULL,
  `ADAPTERNODEID` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ADAPTERNODEID`),
  UNIQUE KEY `PK_MA_NODE` (`ADAPTERNODEID`)
);

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
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `node_id` varchar(64) DEFAULT NULL,
	`label_key` VARCHAR(100) DEFAULT NULL,
    `label_value` VARCHAR(100) DEFAULT NULL,           
    `label_type` VARCHAR(2) DEFAULT NULL,
     PRIMARY KEY (`id`)
);


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

DROP TABLE IF EXISTS `policy_info`;
CREATE TABLE `policy_info` (
  `page_id` bigint(10) NOT NULL AUTO_INCREMENT,
  `text` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `icon` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `sref` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `title` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `type` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `serialNum` BIGINT(10) NOT NULL,
  PRIMARY KEY (`page_id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci CHECKSUM=1 DELAY_KEY_WRITE=1 ROW_FORMAT=DYNAMIC;

insert  into `policy_info`(`page_id`,`text`,`icon`,`sref`,`title`,`type`,`serialNum`) values
(1,'总览','icon-home','app.home','总览','',0),
(3,'权限管理','icon-shield','app.limit.user','环境管理','limit1',0),
(4,'应用蓝图管理','icon-drawer','#','应用蓝图管理','lantu',0),
(5,'组件管理','fa fa-cube','#','组件管理','\"\"',0),
(9,'配置管理','icon-graduation','app.config','配置管理','config',0),
(10,'操作统计','icon-graduation','app.dashboard.statistics1','操作统计','dashboard',0),
(11,'系统配置','icon-graduation','#','系统配置','\"\"',0);

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` varchar(100) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert  into `role`(`id`,`name`) values ('1','管理员'),('2','运维人员'),('3','测试/开发'),('4','门户');


DROP TABLE IF EXISTS `role_policy_infomapping`;
CREATE TABLE `role_policy_infomapping` (
  `role_id` bigint(20) NOT NULL,
  `page_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_id`,`page_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert  into `role_policy_infomapping`(`role_id`,`page_id`) values (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(2,1),(2,2),(2,4),(2,5),(2,6),(2,7),(2,8),(2,9),(2,10),(2,11),(2,12),(2,13),(3,1),(3,2),(3,5),(3,6),(3,7),(3,8),(3,9),(3,10),(4,1),(4,2),(4,4),(4,5),(4,6),(4,7),(4,8),(4,9),(4,10),(4,11),(4,12),(4,13),(5,2),(5,3),(5,4),(8,2),(8,4);

DROP TABLE IF EXISTS `policy_child_info`;

CREATE TABLE `policy_child_info` (
  `id` BIGINT(10) NOT NULL AUTO_INCREMENT,
  `text` VARCHAR(100) NOT NULL,
  `icon` VARCHAR(100) NOT NULL,
  `sref` VARCHAR(100) NOT NULL,
  `type` VARCHAR(100) NOT NULL,
  `parent_id` BIGINT(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `policy_child_info` */

insert  into `policy_child_info`(`id`,`text`,`icon`,`sref`,`type`,`parent_id`) values 
(3,'权限管理','icon-shield','app.limit.user','limit',11),
(4,'集群管理','icon-grid','app.listclusters','cluster',3),
(5,'通用组件管理','fa fa-cube','app.resource.traditional','通用组件管理',5),
(6,'应用蓝图目录','icon-drawer','app.template','template',4),
(7,'应用蓝图实例','fa fa-list-alt','app.templateDeploy','deploy',4),
(8,'插件管理','fa fa-cubes','app.plugin','plugin',11),
(9,'工件库管理','fa fa-download','\"\"','integration',11),
(10,'工件管理','icon-speedometer','\"\"','warningmonitor',11),
(14,'仪表盘','fa fa-file-o','app.log','',11);
/*Table structure for table `role_policy_child_mapping` */

DROP TABLE IF EXISTS `role_policy_child_mapping`;

CREATE TABLE `role_policy_child_mapping` (
  `role_id` VARCHAR(200) NOT NULL,
  `policy_child_id` BIGINT(10) NOT NULL,
  PRIMARY KEY (`role_id`,`policy_child_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

/*Data for the table `role_policy_child_mapping` */

insert  into `role_policy_child_mapping`(`role_id`,`policy_child_id`) values ('1',1),('1',2),('1',3),('1',4),('1',5),('1',6),('1',7),('1',8),('1',9),('1',10),('1',11),('1',13),('1',14),('2',1),('2',3),('2',4),('2',5),('2',6),('2',7),('2',8),('2',9),('2',10),('2',14),('3',1),('3',3),('3',4),('3',5),('3',6),('3',7),('3',8),('3',9),('3',10),('3',14),('4',3),('4',4),('4',5),('4',6),('4',7),('4',8),('4',9),('4',10),('4',14);

DROP TABLE IF EXISTS `user_relationship`;
CREATE TABLE `user_relationship` (
  `user_id` bigint(20) NOT NULL,
  `father_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert  into `user_relationship`(`user_id`,`father_id`) values (-1,1),(1,1),(2,1);


DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert  into `user_role`(`user_id`,`role_id`) values (-1,'2'),(1,'1'),(2,'2'),(4,'4');

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

DROP TABLE IF EXISTS `policy_menu`;
CREATE TABLE `policy_menu` (
  `id` VARCHAR(100) COLLATE utf8_unicode_ci NOT NULL,
  `title` VARCHAR(100) COLLATE utf8_unicode_ci NOT NULL,
  `sref` VARCHAR(100) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT policy_menu VALUES
('2_1','IAAS删除','IAAS接入页面'),('2_2','IAAS接入','IAAS接入页面'),('2_3','IAAS更新','IAAS接入页面'),('2_4','加入集群','IAAS接入页面'),
('3_1','新建用户','权限页面'),('3_2','新建角色','权限页面'),('3_3','删除角色','权限页面'),('3_4','分配权限','权限页面'),('3_5','新建菜单','权限页面'),('3_6','删除菜单','权限页面'),('3_7','编辑菜单','权限页面'),
('4_1','新建集群','集群管理'),('4_2','删除集群','集群管理'),('4_3','删除主机','集群管理'),('4_4','添加用户','集群管理'),('4_5','删除用户','集群管理'),
('4_6','修改集群','集群管理'),('4_7','修改主机','集群管理'),('4_8','更改用户配额','集群管理'),('4_9','解绑主机告警策略','集群管理'),('4_10','加入集群','集群管理'),
('5_1','创建应用','应用管理'),('5_2','删除应用','应用管理'),('5_3','导出应用','应用管理'),('5_4','启动应用','应用管理'),('5_5','更新应用','应用管理'),
('5_6','停止应用','应用管理'),('5_7','维护应用','应用管理'),('5_8','升级.回滚应用','应用管理'),('5_9','卸载应用','应用管理'),('5_10','版本部署','应用管理'),
('5_11','版本删除','应用管理'),('5_12','解绑应用告警策略','应用管理'),('5_13','实例删除','应用管理'),('5_14','日志查询','应用管理'),('5_15','日志导出','应用管理'),
('5_16','登录控制台','应用管理'),('5_17','创建负载','应用管理'),('5_18','更新负载','应用管理'),('5_19','删除负载','应用管理'),('5_20','删除端口','应用管理'),
('5_21','创建端口','应用管理'),('5_22','添加外部网络','应用管理'),('5_23','删除外部网络','应用管理'),('5_24','创建伸缩策略','应用管理'),('5_25','暂停任务','应用管理'),
('5_26','恢复任务','应用管理'),('5_27','删除任务','应用管理'),
('6_1','创建模板','应用模板'),('6_2','部署私有模板','应用模板'),('6_3','删除私有模板','应用模板'),('6_4','部署公共模板','应用模板'),('6_5','删除公共模板','应用模板'),
('6_6','已部署模板启动','应用模板'),('6_7','已部署模板停止','应用模板'),('6_8','已部署模板卸载','应用模板'),('6_9','已部署模板删除','应用模板'),
('7_1','创建任务','任务管理'),('7_2','更新任务','任务管理'),('7_3','删除任务','任务管理'),('7_4','执行任务','任务管理'),('7_5','执行中的任务卸载','任务管理'),
('8_1','创建镜像','镜像仓库'),('8_2','编辑镜像版本','镜像仓库'),('8_3','删除镜像版本','镜像仓库'),
('9_1','创建任务','持续集成'),('9_2','查看任务','持续集成'),('9_3','触发build','持续集成'),('9_4','查看build','持续集成'),('9_5','删除任务','持续集成'),('9_6','查看日志','持续集成'),
('10_1','创建策略','监控告警'),('10_2','编辑策略','监控告警'),('10_3','删除策略','监控告警'),('10_4','启动策略','监控告警'),('10_5','停止策略','监控告警'),
('10_6','添加策略规则','监控告警'),('10_7','更新策略规则','监控告警'),('10_8','删除策略规则','监控告警'),('10_9','修改策略通知人','监控告警'),('10_10','更新策略通知人','监控告警'),
('10_11','删除策略通知人','监控告警'),('10_12','修改策略资源','监控告警'),('10_13','创建通知人','监控告警'),('10_14','更新通知人','监控告警'),('10_15','删除通知人','监控告警'),
('11_1','添加主机','平台组件监控'),('11_2','删除配置','平台组件监控'),
('12_1','添加自定义数据源','自定义数据源'),('12_2','删除自定义数据','自定义数据源'),
('13_1','新建配置','配置中心'),('13_2','删除配置','配置中心'),('13_3','配置下发','配置中心'),('13_4','配置导入','配置中心'),('13_5','配置导出','配置中心'),
('13_6','保存配置参数','配置中心'),('13_7','编辑配置参数','配置中心'),('13_8','删除配置参数','配置中心'),('13_9','添加配置版本','配置中心'),('13_10','删除配置版本','配置中心');

DROP TABLE IF EXISTS `role_menu_mapping`;
CREATE TABLE `role_menu_mapping` (
  `menu_id` VARCHAR(100) NOT NULL,
  `role_id` VARCHAR(100) NOT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8;

INSERT INTO role_menu_mapping VALUES
('2_1',1),('2_2',1),('2_3',1),('2_4',1),
('3_1',1),('3_2',1),('3_3',1),('3_4',1),('3_5',1),('3_6',1),('3_7',1),
('4_1',1),('4_2',1),('4_3',1),('4_4',1),('4_5',1),('4_6',1),('4_7',1),('4_8',1),('4_9',1),('4_10',1),
('5_1',1),('5_2',1),('5_3',1),('5_4',1),('5_5',1),('5_6',1),('5_7',1),('5_8',1),('5_9',1),('5_10',1),
('5_11',1),('5_12',1),('5_13',1),('5_14',1),('5_15',1),('5_16',1),('5_17',1),('5_18',1),('5_19',1),('5_20',1),
('5_21',1),('5_22',1),('5_23',1),('5_24',1),('5_25',1),('5_26',1),('5_27',1),
('6_1',1),('6_2',1),('6_3',1),('6_4',1),('6_5',1),('6_6',1),('6_7',1),('6_8',1),('6_9',1),
('7_1',1),('7_2',1),('7_3',1),('7_4',1),('7_5',1),
('8_1',1),('8_2',1),('8_3',1),
('9_1',1),('9_2',1),('9_3',1),('9_4',1),('9_5',1),('9_6',1),
('10_1',1),('10_2',1),('10_3',1),('10_4',1),('10_5',1),('10_6',1),('10_7',1),('10_8',1),('10_9',1),('10_10',1),
('10_11',1),('10_12',1),('10_13',1),('10_14',1),('10_15',1),
('11_1',1),('11_2',1),
('12_1',1),('12_2',1),
('13_1',1),('13_2',1),('13_3',1),('13_4',1),('13_5',1),('13_6',1),('13_7',1),('13_8',1),('13_9',1),('13_10',1),
('2_1',2),('2_2',2),('2_3',2),('2_4',2),
('4_1',2),('4_2',2),('4_3',2),('4_4',2),('4_5',2),('4_6',2),('4_7',2),('4_8',2),('4_9',2),('4_10',2),
('5_1',2),('5_2',2),('5_3',2),('5_4',2),('5_5',2),('5_6',2),('5_7',2),('5_8',2),('5_9',2),('5_10',2),
('5_11',2),('5_12',2),('5_13',2),('5_14',2),('5_15',2),('5_16',2),('5_17',2),('5_18',2),('5_19',2),('5_20',2),
('5_21',2),('5_22',2),('5_23',2),('5_24',2),('5_25',2),('5_26',2),('5_27',2),
('6_1',2),('6_2',2),('6_3',2),('6_4',2),('6_5',2),('6_6',2),('6_7',2),('6_8',2),('6_9',2),
('7_1',2),('7_2',2),('7_3',2),('7_4',2),('7_5',2),
('8_1',2),('8_2',2),('8_3',2),
('9_1',2),('9_2',2),('9_3',2),('9_4',2),('9_5',2),('9_6',2),
('10_1',2),('10_2',2),('10_3',2),('10_4',2),('10_5',2),('10_6',2),('10_7',2),('10_8',2),('10_9',2),('10_10',2),
('10_11',2),('10_12',2),('10_13',2),('10_14',2),('10_15',2),
('11_1',2),('11_2',2),
('12_1',2),('12_2',2),
('13_1',2),('13_2',2),('13_3',2),('13_4',2),('13_5',2),('13_6',2),('13_7',2),('13_8',2),('13_9',2),('13_10',2),
('2_1',3),('2_2',3),('2_3',3),('2_4',3),
('4_1',3),('4_2',3),('4_3',3),('4_4',3),('4_5',3),('4_6',3),('4_7',3),('4_8',3),('4_9',3),('4_10',3),
('5_1',3),('5_2',3),('5_3',3),('5_4',3),('5_5',3),('5_6',3),('5_7',3),('5_8',3),('5_9',3),('5_10',3),
('5_11',3),('5_12',3),('5_13',3),('5_14',3),('5_15',3),('5_16',3),('5_17',3),('5_18',3),('5_19',3),('5_20',3),
('5_21',3),('5_22',3),('5_23',3),('5_24',3),('5_25',3),('5_26',3),('5_27',3),
('6_1',3),('6_2',3),('6_3',3),('6_4',3),('6_5',3),('6_6',3),('6_7',3),('6_8',3),('6_9',3),
('7_1',3),('7_2',3),('7_3',3),('7_4',3),('7_5',3),
('8_1',3),('8_2',3),('8_3',3),
('9_1',3),('9_2',3),('9_3',3),('9_4',3),('9_5',3),('9_6',3),
('10_1',3),('10_2',3),('10_3',3),('10_4',3),('10_5',3),('10_6',3),('10_7',3),('10_8',3),('10_9',3),('10_10',3),
('10_11',3),('10_12',3),('10_13',3),('10_14',3),('10_15',3),
('11_1',3),('11_2',3),
('12_1',3),('12_2',3),
('13_1',3),('13_2',3),('13_3',3),('13_4',3),('13_5',3),('13_6',3),('13_7',3),('13_8',3),('13_9',3),('13_10',3);
DROP TABLE IF EXISTS `accounts_info`;

CREATE TABLE `accounts_info` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USERID` varchar(50) NOT NULL,
  `USERPASSWORD` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `USERID` (`USERID`)
);

insert  into `accounts_info`(`USERID`,`USERPASSWORD`,`ID`) values ('admin','96e79218965eb72c92a549dd5a330112',1);
insert  into `accounts_info`(`USERID`,`USERPASSWORD`,`ID`) values ('paas','paas',2);
insert  into `accounts_info`(`USERID`,`USERPASSWORD`,`ID`) values ('sso_admin','sso_paas',-1);

DROP TABLE IF EXISTS `sv_resource`;
CREATE TABLE `sv_resource` (
  `ID` varchar(64) NOT NULL,
  `RESOURCE_NAME` varchar(50) DEFAULT NULL COMMENT '资源名称',
  `RESOURCE_TYPE` varchar(30) DEFAULT NULL COMMENT '资源类型,应用类型，通用，topo等',
  `RESOURCE_DESC` varchar(250) DEFAULT NULL COMMENT '资源描述',
  `IS_DOCKER_RESOURCE` tinyint(1) DEFAULT '0' COMMENT '是否是docker资源',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `RESOURCE_PK` (`ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sv_version`;
CREATE TABLE `sv_version` (
  `ID` varchar(64) NOT NULL COMMENT '资源版本主键',
  `RESOURCE_ID` varchar(64) NOT NULL COMMENT '资源主键',
  `RESOURCE_PATH` varchar(300) DEFAULT NULL COMMENT '仓库资源存储路径',
  `VERSION_DESC` varchar(200) DEFAULT NULL COMMENT '资源版本描述',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '资源版本创建时间',
  `DEPLOY_TIMEOUT` int(10) DEFAULT '60000' COMMENT '资源部署超时时间',
  `START_TIMEOUT` int(10) DEFAULT '60000' COMMENT '资源启动超时时间',
  `STOP_TIMEOUT` int(10) DEFAULT '60000' COMMENT '资源停止超时时间',
  `DESTROY_TIMEOUT` int(10) DEFAULT '60000' COMMENT '资源卸载超时时间',
  `VERSION_NAME` varchar(100) DEFAULT NULL COMMENT '资源版本名',
  `ACCESS_PORT` int(11) DEFAULT NULL COMMENT '访问端口',
  `START_SCRIPT_PARAM` varchar(300) DEFAULT NULL COMMENT '启动参数',
  `REGISTRY_ID` int(20) DEFAULT NULL COMMENT '仓库id',
  `STATUS` varchar(20) DEFAULT '00' COMMENT '组件版本状态 00-初始状态,10-测试不通过,11-测试通过,20-不允许发布,21-允许发布',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `VERSION_PK` (`ID`) USING BTREE
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
CREATE TABLE ma_blueprint_type (
	`ID` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
	`BLUEPRINT_ID` varchar(50) NOT NULL COMMENT '关联蓝图ID',
	`FLOW_ID` bigint(20) NOT NULL COMMENT '流程ID',
	`FLOW_TYPE` varchar(50) NOT NULL COMMENT '流程类型',
	`FLOW_INFO` text COMMENT '流程蓝图信息',
	PRIMARY KEY (`ID`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `ma_blueprint_instance`;
CREATE TABLE ma_blueprint_instance (
	`ID` int(11) NOT NULL AUTO_INCREMENT COMMENT '蓝图实例ID',
	`BLUEPRINT_ID` varchar(50) NOT NULL COMMENT '关联蓝图ID',
	`INSTANCE_NAME` varchar(50) NOT NULL COMMENT '蓝图实例名称',
	PRIMARY KEY (`ID`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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


