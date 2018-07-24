
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sfwf_activitymeta
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_activitymeta`;
CREATE TABLE `sfwf_activitymeta` (
`NAME`  varchar(50)  NOT NULL COMMENT '绘图元素名称' ,
`TYPE`  int(2) NOT NULL COMMENT '绘图元素类型' ,
`ICON`  varchar(50)  NULL DEFAULT NULL COMMENT '绘图元素使用的图标' ,
`DESCRIPTION`  varchar(40)  NULL DEFAULT NULL COMMENT '描述' ,
PRIMARY KEY (`NAME`),
INDEX `IDX_SFWF_ACTIVITYMETA` (`TYPE`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_aggentrule
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_aggentrule`;
CREATE TABLE `sfwf_aggentrule` (
`ID`  bigint(16) NOT NULL COMMENT '规则ID' ,
`NAME`  varchar(100)  NOT NULL COMMENT '规则名称' ,
`FROMUSER`  varchar(15)  NOT NULL COMMENT '委托人id' ,
`TOUSER`  varchar(15)  NOT NULL COMMENT '被委托人ID' ,
`PROCESSDEFINITIONNAME`  varchar(50)  NULL DEFAULT NULL COMMENT '流程模板名称' ,
`VERSION`  varchar(50)  NULL DEFAULT NULL COMMENT '流程模板版本' ,
`ISALLPD`  char(1)  NULL DEFAULT NULL COMMENT '是否对所有模板有效(Y有效N无效)' ,
`STARTTIME`  timestamp NULL DEFAULT NULL COMMENT '规则开始时间' ,
`ENDTIME`  timestamp NULL DEFAULT NULL COMMENT '规则结束时间' ,
`ISALLTIME`  char(1)  NULL DEFAULT NULL COMMENT '是否所有时间有效(Y有效N无效)' ,
`DESCRIPTION`  varchar(500)  NULL DEFAULT NULL COMMENT '描述' ,
`ISSTART`  char(1)  NULL DEFAULT NULL COMMENT '是否启动该规则(Y启动N不启动)' ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_calendar
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_calendar`;
CREATE TABLE `sfwf_calendar` (
`NAME`  varchar(20)  NOT NULL COMMENT '日历名称' ,
`DESCRIPTION`  varchar(60)  NULL DEFAULT NULL COMMENT '日历描述' ,
`ISDEFAULT`  char(1)  NULL DEFAULT NULL COMMENT '是否默认日历' ,
PRIMARY KEY (`NAME`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_calendarrule
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_calendarrule`;
CREATE TABLE `sfwf_calendarrule` (
`RULEID`  varchar(20)  NOT NULL COMMENT '日历规则id' ,
`CALENDARID`  varchar(20)  NOT NULL COMMENT '日历规则所属日历id' ,
`ISBUSY`  char(1)  NULL DEFAULT NULL COMMENT '是否工作日 Y为是,N为否' ,
`RULETYPE`  char(1)  NULL DEFAULT NULL COMMENT '规则类型 W周规则 D单日规则' ,
`PRIORITY`  int(3) NULL DEFAULT NULL COMMENT '优先级（数字越小，优先级越高）' ,
`STARTDATE`  timestamp NULL DEFAULT NULL COMMENT '日历规则开始时间' ,
`ENDDATE`  timestamp NULL DEFAULT NULL COMMENT '日历规则结束时间' ,
`VALIDDAYOFWEEK`  int(1) NULL DEFAULT NULL COMMENT '生效日(Sunday - Saturday),该字段与STARTDATE和ENDDATE互斥' ,
`WORKTIME`  varchar(20)  NULL DEFAULT NULL COMMENT '日历规则引用的每日工作时间安排' ,
PRIMARY KEY (`RULEID`),
FOREIGN KEY (`WORKTIME`) REFERENCES `sfwf_calendarworktime` (`NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FK_WORKTIME` (`WORKTIME`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_calendarworktime
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_calendarworktime`;
CREATE TABLE `sfwf_calendarworktime` (
`NAME`  varchar(20)  NOT NULL COMMENT '每日工作时间名称' ,
`DESCRIPTION`  varchar(60)  NULL DEFAULT NULL COMMENT '每日工作时间描述' ,
`AMSTARTHH`  int(2) NULL DEFAULT NULL COMMENT '上午开始小时' ,
`AMSTARTMI`  int(2) NULL DEFAULT NULL COMMENT '上午开始分钟' ,
`AMENDHH`  int(2) NULL DEFAULT NULL COMMENT '上午结束小时' ,
`AMEDNMI`  int(2) NULL DEFAULT NULL COMMENT '上午结束分钟' ,
`PMSTARTHH`  int(2) NULL DEFAULT NULL COMMENT '下午开始小时' ,
`PMSTARTMI`  int(2) NULL DEFAULT NULL COMMENT '下午开始分钟' ,
`PMENDHH`  int(2) NULL DEFAULT NULL COMMENT '下午结束小时' ,
`PMEDNMI`  int(2) NULL DEFAULT NULL COMMENT '下午结束分钟' ,
PRIMARY KEY (`NAME`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_definitiongroup
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_definitiongroup`;
CREATE TABLE `sfwf_definitiongroup` (
`DEFINITIONID`  varchar(50)  NOT NULL COMMENT '流程定义ID' ,
`DEFINITIONNAME`  varchar(200)  NULL DEFAULT NULL COMMENT '流程定义名称' ,
`GROUPID`  varchar(20)  NOT NULL COMMENT '流程定义所属分组id' ,
`GROUPTYPE`  varchar(100)  NOT NULL COMMENT '流程定义所属分组类型' ,
`ISDEFAULT`  int(1) NOT NULL COMMENT '流程定义是否是该分组的缺省模板' ,
`OPERATEUSERID`  varchar(50)  NULL DEFAULT NULL COMMENT '操作用户ID' ,
`OPERATEDATE`  varchar(19)  NULL DEFAULT NULL COMMENT '操作日期' ,
`DESCRIPTION`  varchar(50)  NULL DEFAULT NULL COMMENT '描述(闲置)' ,
`apptype`  varchar(10)  NULL DEFAULT NULL COMMENT '应用类型(闲置)' ,
PRIMARY KEY (`DEFINITIONID`, `GROUPID`, `GROUPTYPE`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_dm_msgsourcetype
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_dm_msgsourcetype`;
CREATE TABLE `sfwf_dm_msgsourcetype` (
`SOURCE_ID`  varchar(4)  NOT NULL ,
`SOURCE_NAME`  varchar(100)  NOT NULL ,
`ISUSE`  char(1)  NULL DEFAULT NULL ,
`DESCRIPTION`  varchar(200)  NULL DEFAULT NULL ,
PRIMARY KEY (`SOURCE_ID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_dm_sendrule
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_dm_sendrule`;
CREATE TABLE `sfwf_dm_sendrule` (
`ID`  varchar(50)  NOT NULL ,
`NAME`  varchar(200)  NOT NULL ,
`DESCRIPTION`  varchar(200)  NULL DEFAULT NULL ,
`ISUSE`  char(1)  NULL DEFAULT NULL ,
`PARENTID`  varchar(50)  NULL DEFAULT NULL ,
`EXTEND`  varchar(100)  NULL DEFAULT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_dm_triggertype
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_dm_triggertype`;
CREATE TABLE `sfwf_dm_triggertype` (
`TYPE_DM`  varchar(2)  NOT NULL ,
`TYPE_MC`  varchar(60)  NULL DEFAULT NULL ,
`XYBZ`  char(1)  NULL DEFAULT NULL ,
`YXBZ`  char(1)  NULL DEFAULT NULL ,
PRIMARY KEY (`TYPE_DM`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_doneinstance
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_doneinstance`;
CREATE TABLE `sfwf_doneinstance` (
`ID`  bigint(16) NOT NULL COMMENT '流程实例id' ,
`NAME`  varchar(200)  NULL DEFAULT NULL COMMENT '流程实例名称' ,
`STARTTIME`  timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '流程实例启动时间' ,
`ENDTIME`  timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '流程实例结束时间' ,
`INITIATOR`  varchar(50)  NULL DEFAULT NULL COMMENT '流程实例启动者' ,
`PROCESSDEFINITIONNAME`  varchar(50)  NOT NULL COMMENT '流程定义id' ,
`STATE`  int(1) NOT NULL COMMENT '流程实例状态' ,
`INSTANCECOMMENT`  varchar(1000)  NULL DEFAULT NULL COMMENT '流程实例描述' ,
`ISREMOVE`  char(1)  NULL DEFAULT 'N' COMMENT '是否软删除(Y软删除N没删除)' ,
`CALENDARID`  varchar(20)  NULL DEFAULT NULL ,
`TIMELIMIT`  varchar(50)  NULL DEFAULT NULL ,
`PLANCOMPLETETIME`  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ,
`ISOVERTIME`  char(1)  NULL DEFAULT 'N' ,
`OVERTIME`  varchar(50)  NULL DEFAULT NULL ,
`YWXTID`  varchar(50)  NULL DEFAULT NULL ,
`BUSINESS`  varchar(200)  NULL DEFAULT NULL ,
`EXTEND`  varchar(500)  NULL DEFAULT NULL ,
PRIMARY KEY (`ID`),
INDEX `SFWF_DONEINSTANCE_PDID` (`PROCESSDEFINITIONNAME`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_doneinstanceexpr
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_doneinstanceexpr`;
CREATE TABLE `sfwf_doneinstanceexpr` (
`INSTANCEID`  bigint(16) NOT NULL COMMENT '流程实例id' ,
`EXPR`  mediumtext  NOT NULL COMMENT '流程实例表达式' ,
`EXPR2`  varchar(4000)  NULL DEFAULT NULL COMMENT '流程实例表达式2' ,
PRIMARY KEY (`INSTANCEID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_doneinstancesuspend
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_doneinstancesuspend`;
CREATE TABLE `sfwf_doneinstancesuspend` (
`INSTANCEID`  bigint(16) NOT NULL ,
`TYPE`  varchar(2)  NULL DEFAULT NULL ,
`STARTTIME`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
`ENDTIME`  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ,
`SUSPENDUSR`  varchar(50)  NULL DEFAULT NULL ,
`RESUMEUSR`  varchar(50)  NULL DEFAULT NULL ,
`REMARK`  varchar(500)  NULL DEFAULT NULL ,
FOREIGN KEY (`INSTANCEID`) REFERENCES `sfwf_doneinstance` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FK_DONEINSTANCEID_SUSPEND` (`INSTANCEID`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_donetokenhistory
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_donetokenhistory`;
CREATE TABLE `sfwf_donetokenhistory` (
`ID`  bigint(16) NOT NULL COMMENT '任务实例id' ,
`INSTANCEID`  bigint(16) NOT NULL COMMENT '流程实例id' ,
`ACTIVITYID`  bigint(16) NOT NULL COMMENT '任务节点id' ,
`STATE`  int(1) NOT NULL COMMENT '任务实例状态' ,
`ASSIGNEE`  varchar(4000)  NULL DEFAULT NULL COMMENT '任务实例执行人' ,
`CONDITIONDESCRIPTION`  varchar(100)  NULL DEFAULT NULL COMMENT '任务实例执行人描述' ,
`EXTEND`  varchar(500)  NULL DEFAULT NULL COMMENT '扩展' ,
`VERSION`  varchar(50)  NULL DEFAULT NULL COMMENT '版本' ,
`TIME`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间' ,
`STARTTIME`  timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '任务开始时间' ,
`ENDTIME`  timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '任务结束时间' ,
`CHECKINOUT`  int(1) NULL DEFAULT NULL COMMENT '签入签出状态' ,
`ISDONE`  int(1) NULL DEFAULT NULL COMMENT '是否结束标志.0，ISNOTDONE；1，ISDONE' ,
`FIRSTPROPERTY`  varchar(4000)  NULL DEFAULT NULL COMMENT '表单字段' ,
`OTHERSTATE`  int(1) NULL DEFAULT NULL COMMENT '扩展' ,
`SECONDPROPERTY`  varchar(4000)  NULL DEFAULT NULL COMMENT '扩展' ,
`CHECKINOUTUSER`  varchar(50)  NULL DEFAULT NULL COMMENT '签入签出用户id' ,
`PREID`  varchar(200)  NULL DEFAULT NULL COMMENT '记录前继节点的tokenID' ,
`PRENODEID`  varchar(200)  NULL DEFAULT NULL COMMENT '该token对应的前继节点的nodeID' ,
`TYPE`  int(2) NOT NULL COMMENT '节点类型' ,
`GROUPID`  bigint(16) NULL DEFAULT NULL COMMENT '所属组结点ID' ,
`XBTYPE`  char(1)  NOT NULL DEFAULT 'n' COMMENT '协办（转办）属性 y是协办任务，n非协办任务' ,
`JOBID`  bigint(16) NOT NULL ,
`ITEMID`  varchar(4000)  NULL DEFAULT NULL ,
`TIMELIMIT`  varchar(50)  NULL DEFAULT NULL ,
`SEQUENCE`  int(4) NULL DEFAULT NULL COMMENT 'token排序' ,
`expr`  mediumtext  NULL ,
`URL`  varchar(200)  NULL DEFAULT NULL ,
`CALENDARID`  varchar(20)  NULL DEFAULT NULL ,
`PLANCOMPLETETIME`  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ,
`ISOVERTIME`  char(1)  NULL DEFAULT 'N' ,
`OVERTIME`  varchar(50)  NULL DEFAULT NULL ,
PRIMARY KEY (`ID`),
FOREIGN KEY (`INSTANCEID`) REFERENCES `sfwf_doneinstance` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `IDX_SFWF_DONETOKEN_ACTID` (`ACTIVITYID`) USING BTREE ,
INDEX `IDX_SFWF_DONETOKEN_INSTANCE` (`INSTANCEID`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_donetokensuspend
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_donetokensuspend`;
CREATE TABLE `sfwf_donetokensuspend` (
`TOKENID`  bigint(16) NOT NULL ,
`INSTANCEID`  bigint(16) NOT NULL ,
`TYPE`  varchar(2)  NULL DEFAULT NULL ,
`STARTTIME`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
`ENDTIME`  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ,
`SUSPENDUSR`  varchar(50)  NULL DEFAULT NULL ,
`RESUMEUSR`  varchar(50)  NULL DEFAULT NULL ,
`REMARK`  varchar(500)  NULL DEFAULT NULL ,
FOREIGN KEY (`TOKENID`) REFERENCES `sfwf_donetokenhistory` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FK_DONETOKENID_SUSPEND` (`TOKENID`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_entity
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_entity`;
CREATE TABLE `sfwf_entity` (
`INSTANCEID`  bigint(16) NOT NULL ,
`ENTITYID`  bigint(20) NOT NULL COMMENT '实体 id' ,
`EXPR`  mediumtext  NULL ,
`EXPR2`  varchar(4000)  NULL DEFAULT NULL ,
PRIMARY KEY (`INSTANCEID`, `ENTITYID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_event
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_event`;
CREATE TABLE `sfwf_event` (
`PDID`  varchar(50)  NOT NULL COMMENT '模板ID' ,
`NODEID`  bigint(16) NOT NULL COMMENT '任务节点ID' ,
`ISPD`  char(1)  NOT NULL DEFAULT 'P' COMMENT 'P,表示ID为流程ID;T,表示ID为任务ID' ,
`FORMTYPE`  int(2) NOT NULL DEFAULT 4 COMMENT '表单类型' ,
`ISPDFORM`  char(1)  NULL DEFAULT NULL COMMENT '1,表示该表单为流程表单;0,表示该表单为任务表单' ,
`SYSTEM`  varchar(100)  NULL DEFAULT NULL COMMENT '所属系统' ,
`YWHJ`  varchar(100)  NULL DEFAULT NULL COMMENT '业务环节' ,
`SJ_DM`  char(20)  NOT NULL DEFAULT 'Y' COMMENT '事件ID' ,
`SJQZSX`  char(1)  NULL DEFAULT 'Y' COMMENT '事件强制属性，必做：Y； 可选：N' ,
`SJCLSX`  char(1)  NULL DEFAULT 'n' COMMENT '事件处理属性, 一次处理:1, 多次处理:n' ,
`SJYLSX`  varchar(200)  NULL DEFAULT NULL COMMENT '事件依赖属性, 多个依赖事件之间用逗号分隔' ,
`FORMID`  varchar(50)  NULL DEFAULT NULL COMMENT '表单模版ID' ,
`FORMNAME`  varchar(200)  NULL DEFAULT NULL COMMENT '表单模版名称' ,
`FORMVERSION`  varchar(50)  NULL DEFAULT NULL COMMENT '表单模版版本号' ,
`ORGEXPR`  mediumtext  NULL COMMENT '表单域权限，当字段压缩后大于4000个字符，改用clob存储' ,
`ORGEXPR2`  varchar(4000)  NULL DEFAULT NULL COMMENT '表单域权限，当字段压缩后不大于4000个字符，用varchar存储' ,
`VAREXPR`  mediumtext  NULL COMMENT '表单流程变量映射，当字段压缩后大于4000个字符，改用clob存储' ,
`VAREXPR2`  varchar(4000)  NULL DEFAULT NULL COMMENT '表单域权限，当字段压缩后不大于4000个字符，用varchar存储' ,
`EXTEND`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
`GNMK`  varchar(100)  NULL DEFAULT NULL COMMENT '功能模块' ,
`URL`  varchar(500)  NULL DEFAULT NULL COMMENT '表单页面' ,
PRIMARY KEY (`PDID`, `NODEID`, `FORMTYPE`, `SJ_DM`),
INDEX `IDX_SFWF_EVENT_NODEID` (`NODEID`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_groupfordefinition
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_groupfordefinition`;
CREATE TABLE `sfwf_groupfordefinition` (
`GROUPID`  varchar(16)  NOT NULL COMMENT '流程定义分组id' ,
`GROUPNAME`  varchar(50)  NOT NULL COMMENT '流程定义分组名称' ,
`PARENTID`  varchar(16)  NOT NULL COMMENT '流程定义分组父分组id' ,
`GROUPTYPE`  varchar(1)  NULL DEFAULT NULL COMMENT '流程定义分组类型（扩展用）' ,
`FORDEFINITION`  int(1) NOT NULL COMMENT '是否包含流程定义' ,
`FORSUBGROUP`  int(1) NOT NULL COMMENT '是否包含子分组' ,
`ISVALID`  int(1) NOT NULL COMMENT '是否有效' ,
`DEPTH`  int(2) NOT NULL COMMENT '流程定义分组深度' ,
`APPTYPE`  varchar(10)  NULL DEFAULT NULL COMMENT '应用类型' ,
`OPERATEUSERID`  varchar(16)  NULL DEFAULT NULL COMMENT '操作用户id' ,
`OPERATEDATE`  varchar(19)  NULL DEFAULT NULL COMMENT '操作日期' ,
`DESCRIPTION`  varchar(100)  NULL DEFAULT NULL COMMENT '描述' ,
PRIMARY KEY (`GROUPID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_gzl_fz_jg
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_gzl_fz_jg`;
CREATE TABLE `sfwf_gzl_fz_jg` (
`ORGID`  varchar(16)  NOT NULL COMMENT '机构id' ,
`GROUPID`  varchar(20)  NOT NULL COMMENT '流程定义所属分组id' ,
`GROUPTYPE`  varchar(2)  NOT NULL COMMENT '流程定义所属分组类型' ,
`DEFINITIONID`  varchar(16)  NOT NULL COMMENT '流程定义ID' ,
`DEFINITIONNAME`  varchar(50)  NOT NULL COMMENT '流程定义名称' ,
`AUTOFLAG`  char(1)  NOT NULL COMMENT '是否自动流转标志,Y自动，N手动，默认为N' ,
`TIMELIMIT`  int(4) NOT NULL COMMENT '办理期限，单位为天' ,
`APPTYPE`  varchar(2)  NULL DEFAULT NULL COMMENT '应用类型' ,
`OPERATEUSERID`  varchar(50)  NULL DEFAULT NULL COMMENT '操作用户ID' ,
`OPERATEDATE`  varchar(19)  NULL DEFAULT NULL COMMENT '操作日期' ,
`DESCRIPTION`  varchar(50)  NULL DEFAULT NULL COMMENT '描述' ,
PRIMARY KEY (`ORGID`, `GROUPID`, `DEFINITIONID`),
INDEX `IDX_SFWF_GZLJG_GROUPIDORGID` (`GROUPID`, `ORGID`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_gzl_fz_qsmb
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_gzl_fz_qsmb`;
CREATE TABLE `sfwf_gzl_fz_qsmb` (
`GROUPID`  varchar(20)  NOT NULL COMMENT '流程定义所属分组id' ,
`GROUPTYPE`  varchar(2)  NOT NULL COMMENT '流程定义所属分组类型' ,
`DEFINITIONID`  varchar(16)  NOT NULL COMMENT '流程定义ID' ,
`DEFINITIONNAME`  varchar(50)  NOT NULL COMMENT '流程定义名称' ,
`GRADEID`  varchar(2)  NOT NULL COMMENT '机关级次id' ,
`AUTOFLAG`  char(1)  NOT NULL COMMENT '是否自动流转标志,Y自动，N手动，默认为N' ,
`TIMELIMIT`  int(4) NOT NULL COMMENT '办理期限，单位为天' ,
`APPTYPE`  varchar(2)  NULL DEFAULT NULL COMMENT '应用类型' ,
`OPERATEUSERID`  varchar(50)  NULL DEFAULT NULL COMMENT '操作用户ID' ,
`OPERATEDATE`  varchar(19)  NULL DEFAULT NULL COMMENT '操作日期' ,
`DESCRIPTION`  varchar(50)  NULL DEFAULT NULL COMMENT '描述' ,
PRIMARY KEY (`DEFINITIONID`, `GROUPID`, `GRADEID`),
INDEX `IDX_SFWF_GZLQS_GROUPIDGRADEID` (`GROUPID`, `GRADEID`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_instance
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_instance`;
CREATE TABLE `sfwf_instance` (
`ID`  bigint(16) NOT NULL COMMENT '流程实例id' ,
`NAME`  varchar(200)  NULL DEFAULT NULL COMMENT '流程实例名称' ,
`STARTTIME`  timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '流程实例启动时间' ,
`ENDTIME`  timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '流程实例结束时间' ,
`INITIATOR`  varchar(50)  NULL DEFAULT NULL COMMENT '流程实例启动者' ,
`PROCESSDEFINITIONNAME`  varchar(50)  NOT NULL COMMENT '流程定义id' ,
`STATE`  int(1) NOT NULL COMMENT '流程实例状态' ,
`INSTANCECOMMENT`  varchar(1000)  NULL DEFAULT NULL COMMENT '流程实例描述' ,
`ISREMOVE`  char(1)  NULL DEFAULT 'N' COMMENT '是否软删除(Y软删除N没删除)' ,
`CALENDARID`  varchar(20)  NULL DEFAULT NULL COMMENT '日历ID' ,
`TIMELIMIT`  varchar(50)  NULL DEFAULT NULL COMMENT '办理期限，单位为天D或小时H' ,
`PLANCOMPLETETIME`  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '计划完成日期' ,
`ISOVERTIME`  char(1)  NULL DEFAULT 'N' COMMENT '是否超时Y/N' ,
`OVERTIME`  varchar(50)  NULL DEFAULT NULL COMMENT '超时时间' ,
`YWXTID`  varchar(50)  NULL DEFAULT NULL COMMENT '业务系统ID' ,
`BUSINESS`  varchar(200)  NULL DEFAULT NULL COMMENT '业务属性' ,
`EXTEND`  varchar(500)  NULL DEFAULT NULL ,
PRIMARY KEY (`ID`),
FOREIGN KEY (`PROCESSDEFINITIONNAME`) REFERENCES `sfwf_processdefinition` (`NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FK_PROCESSDEFINITION` (`PROCESSDEFINITIONNAME`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_instanceexpr
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_instanceexpr`;
CREATE TABLE `sfwf_instanceexpr` (
`INSTANCEID`  bigint(16) NOT NULL COMMENT '流程实例id' ,
`EXPR`  mediumtext  NULL COMMENT '流程实例表达式' ,
`EXPR2`  varchar(4000)  NULL DEFAULT NULL COMMENT '流程实例表达式2' ,
PRIMARY KEY (`INSTANCEID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_instancesuspend
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_instancesuspend`;
CREATE TABLE `sfwf_instancesuspend` (
`INSTANCEID`  bigint(16) NOT NULL ,
`TYPE`  varchar(2)  NULL DEFAULT NULL ,
`STARTTIME`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
`ENDTIME`  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ,
`SUSPENDUSR`  varchar(50)  NULL DEFAULT NULL ,
`RESUMEUSR`  varchar(50)  NULL DEFAULT NULL ,
`REMARK`  varchar(500)  NULL DEFAULT NULL ,
FOREIGN KEY (`INSTANCEID`) REFERENCES `sfwf_instance` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FK_INSTANCEID_SUSPEND` (`INSTANCEID`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
;

-- ----------------------------
-- Table structure for sfwf_jgjc
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_jgjc`;
CREATE TABLE `sfwf_jgjc` (
`JCID`  varchar(15)  NOT NULL COMMENT '机关级次id' ,
`NAME`  varchar(100)  NOT NULL COMMENT '机关级次名称' ,
`DESCRIPTION`  varchar(100)  NULL DEFAULT NULL COMMENT '描述' ,
`OPEN`  int(1) NOT NULL COMMENT '是否有效' ,
PRIMARY KEY (`JCID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_managedefinition
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_managedefinition`;
CREATE TABLE `sfwf_managedefinition` (
`MANAGERID`  varchar(16)  NOT NULL COMMENT '权限对象id' ,
`MANAGERTYPE`  int(2) NOT NULL COMMENT '权限对象类型' ,
`MANAGERGROUPID`  varchar(16)  NOT NULL COMMENT '管理权限对象id' ,
`MANAGERGROUPTYPE`  int(2) NOT NULL COMMENT '管理权限对象类型' ,
`POTENCECONTENT`  varchar(50)  NOT NULL COMMENT '权限' ,
`DEFINITIONGROUPID`  varchar(16)  NOT NULL COMMENT '流程定义分组id' ,
`DEFINITIONGROUPTYPE`  int(2) NOT NULL COMMENT '流程定义分组类型' ,
`LOGINTYPE`  char(1)  NULL DEFAULT NULL COMMENT '用户登录身份,0,管理员,1.普通用户' ,
`OPERATEUSERID`  varchar(16)  NULL DEFAULT NULL COMMENT '操作用户id' ,
`OPERATEDATE`  varchar(19)  NULL DEFAULT NULL COMMENT '操作日期' ,
`DESCRIPTION`  varchar(100)  NULL DEFAULT NULL COMMENT '描述' ,
`apptype`  varchar(10)  NULL DEFAULT NULL COMMENT '应用类型' ,
PRIMARY KEY (`MANAGERID`, `MANAGERGROUPID`, `DEFINITIONGROUPID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_message_definition
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_message_definition`;
CREATE TABLE `sfwf_message_definition` (
`ID`  varchar(16)  NOT NULL ,
`TRIGGERTYPE`  varchar(50)  NOT NULL ,
`SYSTEMID`  varchar(50)  NULL DEFAULT NULL ,
`DEFINITION`  varchar(2000)  NULL DEFAULT NULL ,
`FORMATTYPE`  varchar(10)  NULL DEFAULT NULL ,
`DESCRIPTION`  varchar(200)  NULL DEFAULT NULL ,
`EXTEND`  varchar(500)  NULL DEFAULT NULL ,
PRIMARY KEY (`ID`),
FOREIGN KEY (`SYSTEMID`) REFERENCES `sfwf_system_msgsystem` (`SYSTEM_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`TRIGGERTYPE`) REFERENCES `sfwf_dm_triggertype` (`TYPE_DM`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FK_SYSTEMID_MESSAGE` (`SYSTEMID`) USING BTREE ,
INDEX `FK_TRIGGERTYPE_MESSAGE` (`TRIGGERTYPE`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_node
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_node`;
CREATE TABLE `sfwf_node` (
`ID`  bigint(16) NOT NULL COMMENT '任务节点id' ,
`NAME`  varchar(400) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务节点名称' ,
`TYPE`  int(2) NOT NULL COMMENT '节点类型' ,
`X`  int(5) NULL DEFAULT 0 COMMENT '节点x坐标' ,
`Y`  int(5) NULL DEFAULT 0 COMMENT '节点y坐标' ,
`width`  int(6) NULL DEFAULT NULL ,
`height`  int(6) NULL DEFAULT NULL ,
`PROCESSDEFINITIONNAME`  varchar(50)  NOT NULL COMMENT '流程定义id' ,
`PRIORITY`  int(1) NULL DEFAULT 0 COMMENT '优先级(扩展)' ,
`ICON`  varchar(50)  NULL DEFAULT NULL COMMENT '任务所属业务环节' ,
`NODECOMMENT`  varchar(4000)  NULL DEFAULT NULL COMMENT '节点描述' ,
`ASSIGNEE`  varchar(4000)  NULL DEFAULT NULL COMMENT '节点执行人' ,
`CONDITIONDESCRIPTION`  varchar(100)  NULL DEFAULT NULL COMMENT '节点执行人描述' ,
`EXTEND`  varchar(500)  NULL DEFAULT NULL COMMENT '扩展字段' ,
`FIRSTPROPERTY`  varchar(4000)  NULL DEFAULT NULL COMMENT '会签通过数量，结束条件，与属性2共同记录子流程配置信息' ,
`SECONDPROPERTY`  varchar(4000)  NULL DEFAULT NULL COMMENT '触发事件属性，与属性1共同记录子流程配置信息' ,
`EXPR`  varchar(4000)  NULL DEFAULT NULL COMMENT '节点属性之变量表达式' ,
`GROUPID`  bigint(16) NULL DEFAULT NULL COMMENT '所属组结点ID' ,
PRIMARY KEY (`ID`),
FOREIGN KEY (`PROCESSDEFINITIONNAME`) REFERENCES `sfwf_processdefinition` (`NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `IDX_SFWF_NODE_TYPE` (`TYPE`) USING BTREE ,
INDEX `IDX_SFWF_NODE_PDNAME` (`PROCESSDEFINITIONNAME`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_node_xx
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_node_xx`;
CREATE TABLE `sfwf_node_xx` (
`SWJGJC`  varchar(16)  NULL DEFAULT NULL COMMENT '组节点定义创立者所处的税务机关级次' ,
`SWJGDM`  varchar(16)  NULL DEFAULT NULL COMMENT '组节点定义创立者所处的税务机关代码' ,
`NODEID`  bigint(16) NOT NULL COMMENT '节点ID，系统自动生成' ,
`PROCESSDEFINITIONID`  varchar(50)  NOT NULL COMMENT '流程定义id' ,
`SUBPDNAME`  varchar(50)  NULL DEFAULT NULL COMMENT '子流程定义名称' ,
`SUBPDID`  varchar(50)  NULL DEFAULT NULL COMMENT '子流程定义ID' ,
`SUBPDVERSION`  varchar(50)  NULL DEFAULT NULL COMMENT '子流程版本号' ,
`SUBPDTIME`  bigint(16) NULL DEFAULT NULL COMMENT '子流程时间限制' ,
`SUBPDDES`  varchar(1000)  NULL DEFAULT NULL COMMENT '子流程描述' ,
`SUBPDDEFAULTVAR`  varchar(1)  NULL DEFAULT NULL COMMENT '主子流程是否按照名称对应变量关系' ,
`SUBPDVAR`  varchar(4000)  NULL DEFAULT NULL COMMENT '主子流程私有变量对应关系' ,
`SUBTYPE`  int(1) NULL DEFAULT NULL COMMENT '标注同步、异步子流程；0－同步子流程，1－异步子流程' ,
`EXTEND`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
`EXTEND1`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
`EXTEND2`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
PRIMARY KEY (`NODEID`),
FOREIGN KEY (`NODEID`) REFERENCES `sfwf_node` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_nodeformexpr
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_nodeformexpr`;
CREATE TABLE `sfwf_nodeformexpr` (
`VARNAME`  varchar(200)  NOT NULL COMMENT '绑定后变量的名称' ,
`NODEID`  varchar(50)  NOT NULL COMMENT '任务节点id' ,
`NODENAME`  varchar(50)  NULL DEFAULT NULL COMMENT '任务节点名称' ,
`BIND`  int(1) NOT NULL DEFAULT 0 COMMENT '表单变量的绑定方向，1为输入工作流的变量，0为输出工作流的变量' ,
`PROCESSDEFINITIONID`  varchar(50)  NOT NULL COMMENT '流程定义id' ,
`FORMNAME`  varchar(100)  NULL DEFAULT NULL COMMENT '表单模版名称' ,
`FORMVERSION`  varchar(50)  NULL DEFAULT NULL COMMENT '表单模版版本' ,
`PROCESSVARNAME`  varchar(500)  NULL DEFAULT NULL COMMENT '工作流流程变量的名称' ,
`FORMVARNAME`  varchar(500)  NULL DEFAULT NULL COMMENT '表单模版变量的名称' ,
`DESCRIPTION`  varchar(100)  NULL DEFAULT NULL COMMENT '表单模版变量的描述' ,
`TYPE`  varchar(50)  NOT NULL COMMENT '变量类型' ,
`AUTHOR`  varchar(100)  NULL DEFAULT NULL COMMENT '作者' ,
`EXTEND`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
PRIMARY KEY (`VARNAME`, `NODEID`, `BIND`),
INDEX `IDX_NODEFORMEXPR` (`VARNAME`, `NODEID`, `BIND`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_org
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_org`;
CREATE TABLE `sfwf_org` (
`ORGID`  varchar(36)  NOT NULL COMMENT '机构代码' ,
`NAME`  varchar(100)  NOT NULL COMMENT '机构名称' ,
`PARENTID`  varchar(36)  NOT NULL COMMENT '父机构id' ,
`JCID`  char(2)  NULL DEFAULT '03' COMMENT '级次id' ,
`DESCRIPTION`  varchar(100)  NULL DEFAULT NULL COMMENT '描述' ,
`OPEN`  int(1) NOT NULL COMMENT '是否有效' ,
PRIMARY KEY (`ORGID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_orgwspotence
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_orgwspotence`;
CREATE TABLE `sfwf_orgwspotence` (
`ORGID`  varchar(50)  NOT NULL ,
`WSID`  varchar(50)  NOT NULL ,
`POTENCECONTENT`  varchar(50)  NOT NULL ,
`OPERATEUSERID`  varchar(50)  NULL DEFAULT NULL ,
`OPERATEDATE`  varchar(19)  NULL DEFAULT NULL ,
`DESCRIPTION`  varchar(50)  NULL DEFAULT NULL ,
PRIMARY KEY (`ORGID`, `WSID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_param_viewconfig
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_param_viewconfig`;
CREATE TABLE `sfwf_param_viewconfig` (
`ID`  varchar(8)  NOT NULL ,
`FIELDID`  varchar(100)  NOT NULL COMMENT '字段标识' ,
`FIELDNAME`  varchar(200)  NOT NULL COMMENT '字段显示名称' ,
`ARRANGESN`  varchar(4)  NOT NULL COMMENT '字段排列序号' ,
`FIELDLENGTH`  varchar(4)  NOT NULL COMMENT '字段显示长度' ,
`DESCRIPTION`  varchar(500)  NULL DEFAULT NULL COMMENT '描述' ,
`TYPE`  int(2) NOT NULL COMMENT '参数配置类型' ,
`ISVISIBLE`  char(1)  NULL DEFAULT NULL COMMENT '是否可见(Y可见N不可见)' ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_parameter
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_parameter`;
CREATE TABLE `sfwf_parameter` (
`KEYNAME`  varchar(20)  NOT NULL COMMENT '参数名' ,
`KEYVALUE`  varchar(50)  NOT NULL COMMENT '参数值' ,
`DESCRIPTION`  varchar(50)  NULL DEFAULT NULL COMMENT '描述' ,
`ISVISIBLE`  char(1)  NULL DEFAULT NULL COMMENT '是否可见(Y可见N不可见)' 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_post
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_post`;
CREATE TABLE `sfwf_post` (
`POSTID`  varchar(36)  NOT NULL COMMENT '岗位代码' ,
`NAME`  varchar(100)  NOT NULL COMMENT '岗位名称' ,
`DESCRIPTION`  varchar(100)  NULL DEFAULT NULL COMMENT '描述' ,
`ORGID`  varchar(36)  NOT NULL COMMENT '所属机构id' ,
`OPEN`  int(1) NOT NULL COMMENT '是否有效' ,
PRIMARY KEY (`POSTID`),
FOREIGN KEY (`ORGID`) REFERENCES `sfwf_org` (`ORGID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FK_SFWF_P_O` (`ORGID`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_processalarm
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_processalarm`;
CREATE TABLE `sfwf_processalarm` (
`PROCESSID`  varchar(50)  NOT NULL COMMENT '流程定义或者任务编号' ,
`ALARMTIME`  int(4) NULL DEFAULT NULL COMMENT '任务提醒时限' ,
`ALARMPERCENT`  int(3) NULL DEFAULT NULL COMMENT '百分比(和ALARMTIME字段只能选其一，如果有值以该字段为准)' ,
`ALARMTIMEUNIT`  char(1)  NULL DEFAULT NULL COMMENT '任务提醒时间单位(小时：H、天：D)' ,
`SENDINTERVAL`  int(4) NULL DEFAULT NULL COMMENT '重发时间间隔' ,
`SENDINTERVALUNIT`  char(1)  NULL DEFAULT NULL COMMENT '重发时间间隔单位(小时、天)' ,
`MAXSENDNUMBER`  int(4) NULL DEFAULT NULL COMMENT '重发次数' ,
`APPTYPE`  varchar(50)  NULL DEFAULT NULL COMMENT '应用(邮件etc.)' ,
`ARGVALUESTR`  varchar(1000)  NULL DEFAULT NULL COMMENT '方法参数及值(以某种格式存储，例如特殊符号分隔或者xml格式)' ,
`RETURNVALUE`  varchar(256)  NULL DEFAULT NULL COMMENT '返回值' ,
`ISINUSE`  char(1)  NOT NULL COMMENT '是否启用(Y、N)' ,
`PROCESSFLAG`  char(1)  NOT NULL COMMENT '流程(P)任务(J)标志，需要根据该字段去不同表中取得时间时限' ,
`REMARK`  varchar(20)  NULL DEFAULT NULL COMMENT '备用' ,
`DEMO`  varchar(20)  NULL DEFAULT NULL COMMENT '备用' ,
`RHFLAG`  char(1)  NOT NULL DEFAULT 'R' COMMENT '提醒或催办标识' ,
`ISFROZEN`  char(1)  NULL DEFAULT NULL COMMENT '是否冻结' ,
PRIMARY KEY (`PROCESSID`, `RHFLAG`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_processchangenotify
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_processchangenotify`;
CREATE TABLE `sfwf_processchangenotify` (
`PROCESSDEFINITIONID`  varchar(50)  NOT NULL COMMENT '流程定义id' ,
`PATH`  varchar(4000)  NOT NULL COMMENT '模版定义所在路径“文书大类/文书小类/文书”' ,
`MODIFYTIME`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '模版修改日期 ' ,
`MODIFYAUTH`  varchar(500)  NOT NULL COMMENT '模版修改人员信息' ,
`DESCRIPTION`  varchar(4000)  NULL DEFAULT NULL COMMENT '描述信息' ,
`EXTEND`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
`EXTEND1`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
`EXTEND2`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
FOREIGN KEY (`PROCESSDEFINITIONID`) REFERENCES `sfwf_processdefinition` (`NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `SFWF_PROCESSCHANGENOTIFY_FK` (`PROCESSDEFINITIONID`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_processdefinition
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_processdefinition`;
CREATE TABLE `sfwf_processdefinition` (
`NAME`  varchar(50)  NOT NULL COMMENT '流程定义id' ,
`DESCRIPTION`  varchar(50)  NULL DEFAULT NULL COMMENT '流程定义描述' ,
`CREATEDATE`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期' ,
`AUTHOR`  varchar(50)  NOT NULL COMMENT '流程定义作者' ,
`VALIDFROMDATE`  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '有效开始时间' ,
`VALIDTODATE`  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '有效终止时间' ,
`PRIORITY`  int(1) NULL DEFAULT 0 COMMENT '优先级(扩展)' ,
`ICON`  varchar(50)  NULL DEFAULT NULL COMMENT '图标(扩展)' ,
`LASTMODIFYTIME`  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '最近修改日期' ,
`OPENED`  int(1) NULL DEFAULT NULL COMMENT '是否打开状态(扩展)' ,
`PDGROUP`  varchar(50)  NULL DEFAULT NULL COMMENT '组名称' ,
`GROUPDESCRIPTION`  varchar(50)  NULL DEFAULT NULL COMMENT '组描述' ,
`STATE`  int(1) NULL DEFAULT NULL COMMENT '状态' ,
`ASSIGNEE`  varchar(50)  NULL DEFAULT NULL COMMENT '启动者' ,
`PROCESSDEFINITIONNAME`  varchar(200)  NULL DEFAULT NULL COMMENT '流程定义名称' ,
`VERSION`  int(4) NULL DEFAULT NULL COMMENT '流程定义版本' ,
`CARLANDERID`  varchar(50)  NULL DEFAULT NULL COMMENT '引用的日历id' ,
PRIMARY KEY (`NAME`),
UNIQUE INDEX `PROCESSDEFINITIONNAME` (`PROCESSDEFINITIONNAME`, `VERSION`) USING BTREE ,
INDEX `IDX_SFWF_PD_PDNAME` (`PROCESSDEFINITIONNAME`) USING BTREE ,
INDEX `IDX_SFWF_PD_VERSION` (`VERSION`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_processdefinition_xx
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_processdefinition_xx`;
CREATE TABLE `sfwf_processdefinition_xx` (
`SWJGJC`  varchar(16)  NULL DEFAULT NULL COMMENT '模版定义创立者所处的税务机关级次' ,
`SWJGDM`  varchar(16)  NULL DEFAULT NULL COMMENT '模版定义创立者所处的税务机关代码' ,
`PROCESSDEFINITIONID`  varchar(50)  NOT NULL COMMENT '流程定义id' ,
`PARENTID`  varchar(4000)  NULL DEFAULT NULL COMMENT '继承的父流程定义id' ,
`EXTEND`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
`EXTEND1`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
`EXTEND2`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
PRIMARY KEY (`PROCESSDEFINITIONID`),
FOREIGN KEY (`PROCESSDEFINITIONID`) REFERENCES `sfwf_processdefinition` (`NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_processexpr
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_processexpr`;
CREATE TABLE `sfwf_processexpr` (
`PROCESSDEFINITIONNAME`  varchar(50)  NOT NULL COMMENT '流程定义id' ,
`EXPR`  mediumtext  NOT NULL COMMENT '流程定义表达式' ,
`EXPR2`  varchar(4000)  NULL DEFAULT NULL COMMENT '流程定义表达式2' ,
PRIMARY KEY (`PROCESSDEFINITIONNAME`),
FOREIGN KEY (`PROCESSDEFINITIONNAME`) REFERENCES `sfwf_processdefinition` (`NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_processformexpr
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_processformexpr`;
CREATE TABLE `sfwf_processformexpr` (
`VARNAME`  varchar(200)  NOT NULL COMMENT '绑定后变量的名称' ,
`PROCESSDEFINITIONID`  varchar(50)  NOT NULL COMMENT '流程定义id' ,
`BIND`  int(1) NOT NULL DEFAULT 0 COMMENT '表单变量的绑定方向，1为输入工作流的变量，0为输出工作流的变量' ,
`FORMNAME`  varchar(100)  NULL DEFAULT NULL COMMENT '表单模版名称' ,
`FORMVERSION`  varchar(50)  NULL DEFAULT NULL COMMENT '表单模版版本' ,
`FROMFORMNAME`  varchar(100)  NULL DEFAULT NULL COMMENT '输入参数的表单模版名称' ,
`FROMFORMVERSION`  varchar(50)  NULL DEFAULT NULL COMMENT '输入参数的表单模版版本' ,
`PROCESSVARNAME`  varchar(500)  NULL DEFAULT NULL COMMENT '工作流流程变量的名称' ,
`FORMVARNAME`  varchar(500)  NULL DEFAULT NULL COMMENT '表单模版变量的名称' ,
`DESCRIPTION`  varchar(100)  NULL DEFAULT NULL COMMENT '表单模版变量的描述' ,
`TYPE`  varchar(50)  NOT NULL COMMENT '变量类型' ,
`AUTHOR`  varchar(100)  NULL DEFAULT NULL COMMENT '作者' ,
`EXTEND`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
PRIMARY KEY (`VARNAME`, `PROCESSDEFINITIONID`, `BIND`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_processnotify
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_processnotify`;
CREATE TABLE `sfwf_processnotify` (
`ID`  varchar(50)  NOT NULL COMMENT '流程实例id/TokenID' ,
`PROCESSID`  varchar(50)  NOT NULL COMMENT '流程定义id/任务ID' ,
`PROCESSNAME`  varchar(50)  NULL DEFAULT NULL COMMENT '流程定义名称/任务名称' ,
`PROCESSFLAG`  char(1)  NOT NULL COMMENT '流程(P)任务(T)标志' ,
`MESSAGE`  varchar(4000)  NULL DEFAULT NULL COMMENT '消息主题' ,
`FSRQ`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '发送日期' ,
`XXLX`  char(2)  NOT NULL COMMENT '信息类型：10通知，20提醒，30催办，40告警，50预警' ,
`BLQX`  timestamp NULL DEFAULT NULL COMMENT '办理期限' ,
`LCZT`  char(2)  NOT NULL COMMENT '流程状态' ,
`FSR`  varchar(50)  NOT NULL COMMENT '发送人' ,
`SENDEDNUMBER`  int(4) NULL DEFAULT NULL COMMENT '已发送次数' ,
`EXTEND`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
`EXTEND1`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
`EXTEND2`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_processreceipt
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_processreceipt`;
CREATE TABLE `sfwf_processreceipt` (
`PROCESSID`  varchar(50)  NOT NULL COMMENT '流程模板任务id' ,
`PROCESSFLAG`  char(1)  NOT NULL COMMENT '流程(P)任务(J)标志' ,
`RECEIPTTYPE`  varchar(2)  NOT NULL COMMENT '回执类型，10流程结束时给发起人发回执，20流程结束时给每个处理人发回执' ,
`ISINUSE`  char(1)  NOT NULL COMMENT '是否启用(Y、N)' ,
`APPTYPE`  varchar(50)  NULL DEFAULT NULL COMMENT '应用(邮件etc.)' ,
`ARGVALUESTR`  varchar(1000)  NULL DEFAULT NULL COMMENT '方法参数及值(以某种格式存储，例如特殊符号分隔或者xml格式)' ,
`RETURNVALUE`  varchar(256)  NULL DEFAULT NULL COMMENT '返回值' ,
`REMARK`  varchar(20)  NULL DEFAULT NULL COMMENT '备用' ,
PRIMARY KEY (`PROCESSID`),
FOREIGN KEY (`RECEIPTTYPE`) REFERENCES `sfwf_processreceipt_type` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FK_RECEIPTTYPE` (`RECEIPTTYPE`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_processreceipt_type
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_processreceipt_type`;
CREATE TABLE `sfwf_processreceipt_type` (
`ID`  varchar(2)  NOT NULL COMMENT '回执类型id' ,
`NAME`  varchar(200)  NOT NULL COMMENT '回执类型名称' ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_processtimer
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_processtimer`;
CREATE TABLE `sfwf_processtimer` (
`TIMERID`  varchar(16)  NOT NULL COMMENT '定时启动id' ,
`PROCESSDEFINENAME`  varchar(50)  NULL DEFAULT NULL COMMENT '流程定义id' ,
`TIMERTYPE`  int(2) NULL DEFAULT NULL COMMENT '时间类型(每周,具体日期,每天,每月)' ,
`TIMERSTARTTIME`  timestamp NULL DEFAULT NULL COMMENT '定时启动时间' ,
`TIMERSTARTDADOFWEEK`  int(2) NULL DEFAULT NULL COMMENT '每周定时启动时间' ,
PRIMARY KEY (`TIMERID`),
UNIQUE INDEX `PROCESSDEFINENAME` (`PROCESSDEFINENAME`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_role
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_role`;
CREATE TABLE `sfwf_role` (
`ROLEID`  varchar(36)  NOT NULL COMMENT '角色ID' ,
`NAME`  varchar(100)  NOT NULL COMMENT '角色名称' ,
`DESCRIPTION`  varchar(100)  NULL DEFAULT NULL COMMENT '描述' ,
`OPEN`  int(1) NOT NULL COMMENT '是否有效' ,
PRIMARY KEY (`ROLEID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_script
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_script`;
CREATE TABLE `sfwf_script` (
`ID`  bigint(16) NOT NULL COMMENT '节点id' ,
`PARAM`  varchar(50)  NULL DEFAULT NULL COMMENT '参数' ,
`RETURNVALUE`  varchar(50)  NULL DEFAULT NULL COMMENT '返回值' ,
`SCRIPT`  mediumtext  NULL COMMENT '脚本字符串' ,
`PROCESSFLAG`  char(1)  NULL DEFAULT NULL COMMENT '流程或任务标志' ,
`REMARK`  varchar(100)  NULL DEFAULT NULL COMMENT '备注' ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_seq
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_seq`;
CREATE TABLE `sfwf_seq` (
`id`  bigint(11) NOT NULL AUTO_INCREMENT ,
`name`  varchar(255)  NOT NULL COMMENT 'sequence名称' ,
`max2`  bigint(8) NOT NULL DEFAULT 1 COMMENT '最大id' ,
`length`  int(2) NOT NULL DEFAULT 1 COMMENT '生成序列后的长度,以0补全' ,
`next`  int(2) NOT NULL DEFAULT 1 COMMENT '增长的长度' ,
`rules`  varchar(255)  NULL DEFAULT NULL ,
PRIMARY KEY (`id`),
UNIQUE INDEX `fk_name` (`name`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8
AUTO_INCREMENT=5

;

-- ----------------------------
-- Table structure for sfwf_serverconfig
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_serverconfig`;
CREATE TABLE `sfwf_serverconfig` (
`NAME`  varchar(100)  NOT NULL COMMENT '服务连接名称' ,
`ID`  int(4) NOT NULL COMMENT '服务连接ID' ,
`TYPE`  int(2) NOT NULL COMMENT '服务器连接类型' ,
`FWQNAME`  varchar(4000)  NOT NULL COMMENT '服务器连接名称或者IP' ,
`FWQUSER`  varchar(100)  NULL DEFAULT NULL COMMENT '用户名' ,
`PASSWORD`  varchar(10)  NULL DEFAULT NULL COMMENT '用户密码' ,
`PORT`  varchar(100)  NOT NULL COMMENT '端口号' ,
`DOMAIN`  varchar(100)  NULL DEFAULT NULL COMMENT '域名称' ,
`EXTEND1`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
`EXTEND2`  varchar(100)  NULL DEFAULT NULL COMMENT '扩展字段' ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_service_category
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_service_category`;
CREATE TABLE `sfwf_service_category` (
`id`  varchar(32)  NULL DEFAULT NULL COMMENT '唯一表示，无业务含义' ,
`category_id`  varchar(32)  NULL DEFAULT NULL COMMENT '服务分类ID' ,
`category_name`  varchar(100)  NULL DEFAULT NULL COMMENT '服务分类名称' ,
`project`  varchar(100)  NULL DEFAULT NULL COMMENT '服务分类所属工程名称' ,
`parent`  varchar(32)  NULL DEFAULT NULL COMMENT '父分类ID' ,
`SYSTEM`  varchar(50)  NULL DEFAULT 'Branch' COMMENT '所属系统' 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_service_manage
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_service_manage`;
CREATE TABLE `sfwf_service_manage` (
`resource_id`  varchar(32)  NULL DEFAULT NULL COMMENT '唯一表示，无业务含义' ,
`component_id`  varchar(32)  NULL DEFAULT NULL COMMENT '服务ID，但不仅限于服务ID' ,
`nick_name`  varchar(100)  NULL DEFAULT NULL COMMENT '服务分类' ,
`resource_type`  varchar(40)  NULL DEFAULT NULL COMMENT '服务的别名，助记符' ,
`category`  varchar(40)  NULL DEFAULT NULL COMMENT '资源类型 ，值包含    proxyaseusiness 三种，分别表示代理服务（聚合服务、组合服务）、基础服务、业务服务' ,
`syn_type`  varchar(2)  NULL DEFAULT NULL ,
`project`  varchar(100)  NULL DEFAULT NULL COMMENT '资源所属工程名称' ,
`create_date`  datetime NULL DEFAULT NULL COMMENT '创建时间' ,
`last_modified`  datetime NULL DEFAULT NULL COMMENT '最后修改时间' ,
`state`  int(3) NULL DEFAULT NULL COMMENT '当前状态' ,
`create_user`  varchar(16)  NULL DEFAULT NULL COMMENT '资源创建者' 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_service_resourcetype
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_service_resourcetype`;
CREATE TABLE `sfwf_service_resourcetype` (
`resourcetype_id`  varchar(2)  NULL DEFAULT NULL COMMENT '资源类型ID' ,
`resourcetype_name`  varchar(100)  NULL DEFAULT NULL COMMENT '资源类型名称' ,
`description`  varchar(100)  NULL DEFAULT NULL COMMENT '资源类型描述' 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_service_state
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_service_state`;
CREATE TABLE `sfwf_service_state` (
`state_id`  int(3) NULL DEFAULT NULL COMMENT '服务状态ID' ,
`state_name`  varchar(100)  NULL DEFAULT NULL COMMENT '服务状态名称' ,
`description`  varchar(100)  NULL DEFAULT NULL COMMENT '服务状态描述' 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_service_syntype
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_service_syntype`;
CREATE TABLE `sfwf_service_syntype` (
`syntype_id`  varchar(2)  NULL DEFAULT NULL COMMENT '类型ID' ,
`syntype_name`  varchar(100)  NULL DEFAULT NULL COMMENT '类型名称' ,
`description`  varchar(100)  NULL DEFAULT NULL COMMENT '类型描述' 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_service_version
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_service_version`;
CREATE TABLE `sfwf_service_version` (
`id`  varchar(32)  NULL DEFAULT NULL COMMENT '唯一标识，无业务含义' ,
`resource_id`  varchar(32)  NULL DEFAULT NULL COMMENT '关联资源ID' ,
`create_date`  date NULL DEFAULT NULL ,
`version`  varchar(16)  NULL DEFAULT NULL COMMENT '版本' ,
`create_user`  varchar(16)  NULL DEFAULT NULL COMMENT '版本创建者' ,
`state`  int(1) NULL DEFAULT NULL COMMENT '当前状态：0：已过期        1：使用中' ,
`content`  blob NULL COMMENT '服务备份' 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_subsystem
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_subsystem`;
CREATE TABLE `sfwf_subsystem` (
`ACTIVITYID`  varchar(16)  NOT NULL COMMENT '子系统节点id' ,
`SERIALNO`  int(2) NULL DEFAULT 1 COMMENT '配置序号' ,
`STARTTYPE`  int(1) NULL DEFAULT 1 COMMENT '子系统启动类型' ,
`STARTPARAM`  varchar(2000)  NULL DEFAULT NULL COMMENT '子系统启动配置' ,
`EXTEND`  varchar(50)  NULL DEFAULT NULL COMMENT '备用' ,
`REMARK`  varchar(50)  NULL DEFAULT NULL COMMENT '备用' ,
`DESCRIPTION`  varchar(100)  NULL DEFAULT NULL COMMENT '描述' ,
PRIMARY KEY (`ACTIVITYID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_system_config
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_system_config`;
CREATE TABLE `sfwf_system_config` (
`ID`  varchar(32)  NULL DEFAULT NULL COMMENT '配置项ID' ,
`NAME`  varchar(100)  NULL DEFAULT NULL COMMENT '配置项名称' ,
`VALUE`  varchar(400)  NULL DEFAULT NULL COMMENT '配置项值' ,
`PARENTID`  varchar(32)  NULL DEFAULT NULL COMMENT '父配置项ID' ,
`DESCRIPTION`  varchar(100)  NULL DEFAULT NULL COMMENT '配置项描述' 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_system_msgservice
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_system_msgservice`;
CREATE TABLE `sfwf_system_msgservice` (
`ID`  varchar(50)  NOT NULL COMMENT 'ID' ,
`TRIGGERTYPE`  varchar(50)  NOT NULL COMMENT '服务触发时机' ,
`CATEGORY`  varchar(50)  NOT NULL COMMENT '服务分类' ,
`PROJECT`  varchar(50)  NOT NULL DEFAULT '' COMMENT '服务的项目' ,
`ISUSE`  char(1)  NULL DEFAULT NULL COMMENT '是否启用服务' ,
`PATH`  varchar(100)  NULL DEFAULT NULL COMMENT '程序调用路径' ,
`METHODNAME`  varchar(100)  NULL DEFAULT NULL COMMENT '调用方法名称' ,
`PARAS`  varchar(500)  NULL DEFAULT NULL COMMENT '参数名称' ,
`RETURNTYPE`  varchar(50)  NULL DEFAULT NULL COMMENT '方法返回值' ,
`URL`  varchar(100)  NULL DEFAULT NULL COMMENT 'URL(websvc:http://locahost:9301/sfwf/monitor;ejb:t3://localhost:9301)' ,
`USR`  varchar(50)  NULL DEFAULT NULL COMMENT '用户名' ,
`PSW`  varchar(50)  NULL DEFAULT NULL COMMENT '密码' ,
`OTHERPARAS`  varchar(100)  NULL DEFAULT NULL COMMENT '服务其他配置参数' ,
`TYPE`  varchar(10)  NULL DEFAULT NULL COMMENT '程序类型：java,ejb,websvc' ,
`MSGDEFINITION`  char(1)  NULL DEFAULT NULL ,
`DESCRIPTION`  varchar(200)  NULL DEFAULT NULL COMMENT '描述' ,
PRIMARY KEY (`ID`, `TRIGGERTYPE`, `CATEGORY`, `PROJECT`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_system_msgsystem
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_system_msgsystem`;
CREATE TABLE `sfwf_system_msgsystem` (
`SYSTEM_ID`  varchar(16)  NOT NULL ,
`SYSTEM_NAME`  varchar(50)  NOT NULL ,
`ISUSE`  char(1)  NULL DEFAULT NULL ,
`ISDEFAULT`  char(1)  NULL DEFAULT NULL ,
`DESCRIPTION`  varchar(200)  NULL DEFAULT NULL ,
PRIMARY KEY (`SYSTEM_ID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_system_sendrule
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_system_sendrule`;
CREATE TABLE `sfwf_system_sendrule` (
`ID`  varchar(50)  NOT NULL ,
`NAME`  varchar(1000)  NOT NULL ,
`DESCRIPTION`  varchar(4000)  NULL DEFAULT NULL ,
`PARENTID`  varchar(50)  NULL DEFAULT NULL ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_taskbusiness
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_taskbusiness`;
CREATE TABLE `sfwf_taskbusiness` (
`SCOPE`  varchar(50)  NOT NULL DEFAULT '' COMMENT '业务属性的作用域，如果作用域为NULL，表示在整个系统内有效' ,
`VARNAME`  varchar(50)  NOT NULL COMMENT '任务变量名' ,
`VARTYPE`  varchar(30)  NOT NULL COMMENT '任务变量类型' ,
`VARVALUE`  varchar(500)  NULL DEFAULT NULL COMMENT '变量值的来源。如果为NULL，表示变量没有初值。变量值的来源，可以是一个API，但是API的返回值一定是java.util.Collection,其中的对象是TaskBusinessInfo ' ,
`BUSINESSNAME`  varchar(20)  NOT NULL COMMENT '业务属性的名称，用于显示' ,
`EDITABLE`  int(1) NULL DEFAULT NULL COMMENT '界面组件是否可以编辑.0表示不可编辑,1表示可以编辑' ,
`SIZES`  varchar(20)  NULL DEFAULT NULL COMMENT '界面组件的尺寸' ,
`GRIDX`  int(2) NULL DEFAULT NULL COMMENT '界面组件在业务属性Tab页中网格布局中X方向的网格位置' ,
`GRIDY`  int(2) NULL DEFAULT NULL COMMENT '界面组件在业务属性Tab页中网格布局中Y方向的网格位置' ,
`GRIDWIDTH`  int(2) NULL DEFAULT NULL COMMENT '界面组件在业务属性Tab页中X方向的占据的网格数' ,
`GRIDHEIGHT`  int(2) NULL DEFAULT NULL COMMENT '界面组件在业务属性Tab页中在Y方向的占据的网格数' ,
`WEIGHTX`  int(2) NULL DEFAULT 1 COMMENT '界面组件在业务属性Tab页中X方向的权重' ,
`WEIGHTY`  int(2) NULL DEFAULT 1 COMMENT '界面组件在业务属性Tab页中Y方向的权重' ,
`ANCHOR`  varchar(20)  NULL DEFAULT NULL COMMENT '界面组件在业务属性Tab页中对齐的方位' ,
`FILL`  varchar(20)  NULL DEFAULT NULL COMMENT '界面组件在业务属性Tab页中的填充方式' ,
`IPADX`  int(3) NULL DEFAULT NULL COMMENT '界面组件在业务属性Tab页中实际的行宽' ,
`IPADY`  int(3) NULL DEFAULT NULL COMMENT '界面组件在业务属性Tab页中实际的列宽' ,
`TOP`  int(2) NULL DEFAULT NULL COMMENT '界面组件在业务属性Tab页中顶部的空白边界' ,
`LEFT`  int(2) NULL DEFAULT NULL COMMENT '界面组件在业务属性Tab页中左边的空白边界' ,
`BOTTOM`  int(2) NULL DEFAULT NULL COMMENT '界面组件在业务属性Tab页中底边的空白边界' ,
`RIGHT`  int(2) NULL DEFAULT NULL COMMENT '界面组件在业务属性Tab页中右边的空白边界' ,
PRIMARY KEY (`SCOPE`, `VARNAME`),
INDEX `IDX_SFWF_TASKBUSINESS_SCOPE` (`SCOPE`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_timelimit
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_timelimit`;
CREATE TABLE `sfwf_timelimit` (
`PROCESSID`  varchar(50)  NOT NULL COMMENT '流程模板任务流程实例任务实例id' ,
`PROCESSFLAG`  char(1)  NOT NULL COMMENT '流程(P)任务(J)流程实例(I)任务实例(T)标志' ,
`TIMELIMIT`  varchar(50)  NOT NULL COMMENT '时限(小时为数字，天为数字+D)' ,
`PRIORITY`  int(1) NOT NULL COMMENT '优先级(流程干预:3; 流程框架:2;建模工具:1)' ,
`ORGID`  varchar(50)  NULL DEFAULT NULL COMMENT '机构ID' ,
`CALENDARID`  varchar(20)  NULL DEFAULT NULL COMMENT '日历ID' ,
INDEX `IDX_SFWF_TIMELIMIT_PROCESSID` (`PROCESSID`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_token
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_token`;
CREATE TABLE `sfwf_token` (
`ID`  bigint(16) NOT NULL COMMENT '任务实例id' ,
`INSTANCEID`  bigint(16) NOT NULL COMMENT '流程实例id' ,
`ACTIVITYID`  bigint(16) NOT NULL COMMENT '任务节点id' ,
`STATE`  int(1) NOT NULL COMMENT '任务实例状态' ,
`ASSIGNEE`  varchar(4000)  NULL DEFAULT NULL COMMENT '任务实例执行人' ,
`CONDITIONDESCRIPTION`  varchar(100)  NULL DEFAULT NULL COMMENT '任务实例执行人描述' ,
`EXTEND`  varchar(500)  NULL DEFAULT NULL COMMENT '扩展' ,
`VERSION`  varchar(50)  NULL DEFAULT NULL COMMENT '版本' ,
`TIME`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间' ,
`STARTTIME`  timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '任务开始时间' ,
`ENDTIME`  timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '任务结束时间' ,
`CHECKINOUT`  int(1) NULL DEFAULT NULL COMMENT '签入签出状态' ,
`ISDONE`  int(1) NULL DEFAULT NULL COMMENT '是否结束标志.0，ISNOTDONE；1，ISDONE' ,
`FIRSTPROPERTY`  varchar(4000)  NULL DEFAULT NULL COMMENT '表单字段' ,
`OTHERSTATE`  int(1) NULL DEFAULT NULL COMMENT '扩展' ,
`SECONDPROPERTY`  varchar(4000)  NULL DEFAULT NULL COMMENT '扩展' ,
`CHECKINOUTUSER`  varchar(50)  NULL DEFAULT NULL COMMENT '签入签出用户id' ,
`PREID`  varchar(200)  NULL DEFAULT NULL COMMENT '记录前继节点的tokenID' ,
`PRENODEID`  varchar(200)  NULL DEFAULT NULL COMMENT '该token对应的前继节点的nodeID' ,
`TYPE`  int(2) NOT NULL COMMENT '节点类型' ,
`GROUPID`  bigint(16) NULL DEFAULT NULL COMMENT '所属组结点ID' ,
`XBTYPE`  char(1)  NOT NULL DEFAULT 'n' COMMENT '协办（转办）属性 y是协办任务，n非协办任务' ,
`JOBID`  bigint(16) NOT NULL ,
`ITEMID`  varchar(4000)  NULL DEFAULT NULL ,
`TIMELIMIT`  varchar(50)  NULL DEFAULT NULL ,
`SEQUENCE`  int(4) NULL DEFAULT NULL COMMENT 'token排序' ,
`expr`  mediumtext  NULL ,
`URL`  varchar(200)  NULL DEFAULT NULL COMMENT '表单链接' ,
`CALENDARID`  varchar(20)  NULL DEFAULT NULL COMMENT '日历ID' ,
`PLANCOMPLETETIME`  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '计划完成日期' ,
`ISOVERTIME`  char(1)  NULL DEFAULT 'N' COMMENT '是否超时' ,
`OVERTIME`  varchar(50)  NULL DEFAULT NULL COMMENT '超时时间' ,
PRIMARY KEY (`ID`),
FOREIGN KEY (`INSTANCEID`) REFERENCES `sfwf_instance` (`ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `SFWF_TOKEN_FK` (`INSTANCEID`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_tokensuspend
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_tokensuspend`;
CREATE TABLE `sfwf_tokensuspend` (
`TOKENID`  bigint(16) NOT NULL ,
`INSTANCEID`  bigint(16) NOT NULL ,
`TYPE`  varchar(2)  NULL DEFAULT NULL ,
`STARTTIME`  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP ,
`ENDTIME`  timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ,
`SUSPENDUSR`  varchar(50)  NULL DEFAULT NULL ,
`RESUMEUSR`  varchar(50)  NULL DEFAULT NULL ,
`REMARK`  varchar(500)  NULL DEFAULT NULL 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_transition
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_transition`;
CREATE TABLE `sfwf_transition` (
`ID_FROM`  bigint(16) NOT NULL COMMENT '连线开始节点id' ,
`ID_TO`  bigint(16) NOT NULL COMMENT '连线结束节点id' ,
`PROCESSDEFINITIONNAME`  varchar(50)  NOT NULL COMMENT '流程定义id' ,
`TF`  int(1) NULL DEFAULT NULL COMMENT '真假分支连线标志,默认为真(1/0)' ,
`LINESHOW`  char(1)  NULL DEFAULT NULL COMMENT '是否显示连线(Y/N)' ,
`EXPR`  varchar(4000)  NULL DEFAULT NULL COMMENT '连线属性之变量表达式' ,
`GROUPID`  bigint(16) NULL DEFAULT NULL COMMENT '所属组结点ID' ,
`polyline`  varchar(500)  NULL DEFAULT NULL COMMENT '连线点记录',
`type`  varchar(2)  NULL DEFAULT NULL  COMMENT '连线含义类型，1.顺序连线2消息连线3关联连线4数据关联连线,目前没使用，默认当成1',
`brokenType`  varchar(2)  NULL DEFAULT NULL COMMENT '连线显示类型，1.直线2直边多边线3曲边多边线,目前没使用，默认为1',
`color`  varchar(10)  NULL DEFAULT NULL COMMENT '连线颜色，未使用',
`HJRULE`  varchar(4000)  NULL DEFAULT NULL COMMENT '汇聚规则，未使用',
PRIMARY KEY (`ID_FROM`, `ID_TO`),
FOREIGN KEY (`PROCESSDEFINITIONNAME`) REFERENCES `sfwf_processdefinition` (`NAME`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `IDX_SFWF_TRANSITION_IDFROM` (`ID_FROM`) USING BTREE ,
INDEX `IDX_SFWF_TRANSITION_IDTO` (`ID_TO`) USING BTREE ,
INDEX `IDX_SFWF_TRANSITION_PDNAME` (`PROCESSDEFINITIONNAME`) USING BTREE ,
INDEX `IDX_SFWF_TRANSITION_CONDITION` (`TF`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_transition_brokentype
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_transition_brokentype`;
CREATE TABLE `sfwf_transition_brokentype` (
`BROKENTYPE_DM`  varchar(2)  NOT NULL ,
`BROKENTYPE_MC`  varchar(60)  NULL DEFAULT NULL ,
`XYBZ`  char(1)  NULL DEFAULT NULL ,
`YXBZ`  char(1)  NULL DEFAULT NULL ,
PRIMARY KEY (`BROKENTYPE_DM`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_transition_type
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_transition_type`;
CREATE TABLE `sfwf_transition_type` (
`TYPE_DM`  varchar(2)  NOT NULL ,
`TYPE_MC`  varchar(60)  NULL DEFAULT NULL ,
`XYBZ`  char(1)  NULL DEFAULT NULL ,
`YXBZ`  char(1)  NULL DEFAULT NULL ,
PRIMARY KEY (`TYPE_DM`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_user
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_user`;
CREATE TABLE `sfwf_user` (
`USERID`  varchar(36)  NOT NULL COMMENT '用户id' ,
`TNAME`  varchar(20)  NOT NULL COMMENT '用户真实名称' ,
`ORGID`  varchar(36)  NOT NULL COMMENT '所属机构id' ,
`NAME`  varchar(20)  NOT NULL COMMENT '登陆名称' ,
`PASSWORD`  varchar(40)  NOT NULL COMMENT '登陆密码' ,
`PWTYPE`  varchar(2)  NOT NULL COMMENT '加密类型' ,
`BEGINDATE`  timestamp NULL DEFAULT NULL COMMENT '口令开始时间' ,
`ENDDATE`  timestamp NULL DEFAULT NULL COMMENT '口令终止时间' ,
`OPEN`  int(1) NOT NULL COMMENT '是否有效' ,
PRIMARY KEY (`USERID`),
FOREIGN KEY (`ORGID`) REFERENCES `sfwf_org` (`ORGID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
UNIQUE INDEX `IDX_USERNAME` (`NAME`) USING BTREE ,
INDEX `FK_SFWF_U_O` (`ORGID`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_user_post`;
CREATE TABLE `sfwf_user_post` (
`USERID`  varchar(36)  NOT NULL COMMENT '用户id' ,
`POSTID`  varchar(36)  NOT NULL COMMENT '岗位id' ,
PRIMARY KEY (`USERID`, `POSTID`),
FOREIGN KEY (`POSTID`) REFERENCES `sfwf_post` (`POSTID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`USERID`) REFERENCES `sfwf_user` (`USERID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FK_SFWF_UP_P` (`POSTID`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_user_role`;
CREATE TABLE `sfwf_user_role` (
`USERID`  varchar(36)  NOT NULL COMMENT '用户id' ,
`ROLEID`  varchar(36)  NOT NULL COMMENT '角色id' ,
PRIMARY KEY (`USERID`, `ROLEID`),
FOREIGN KEY (`ROLEID`) REFERENCES `sfwf_role` (`ROLEID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`USERID`) REFERENCES `sfwf_user` (`USERID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FK_SFWF_UR_R` (`ROLEID`) USING BTREE 
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_userorg
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_userorg`;
CREATE TABLE `sfwf_userorg` (
`USERID`  varchar(50)  NOT NULL ,
`ORGID`  varchar(50)  NOT NULL ,
PRIMARY KEY (`USERID`, `ORGID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_wfapplicationresource
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_wfapplicationresource`;
CREATE TABLE `sfwf_wfapplicationresource` (
`NAME`  varchar(50)  NOT NULL COMMENT '应用程序名称' ,
`PATH`  varchar(100)  NULL DEFAULT NULL COMMENT '应用程序调用路径' ,
`METHODNAME`  varchar(50)  NULL DEFAULT NULL COMMENT '调用方法名称' ,
`PARAS`  varchar(500)  NULL DEFAULT NULL COMMENT '参数名称' ,
`WFPARAS`  varchar(500)  NULL DEFAULT NULL COMMENT '工作流变量' ,
`TYPE`  varchar(10)  NULL DEFAULT NULL COMMENT '应用程序类型' ,
`DESCRIPTION`  varchar(200)  NULL DEFAULT NULL COMMENT '描述' ,
PRIMARY KEY (`NAME`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- Table structure for sfwf_resendinfo
-- ----------------------------
DROP TABLE IF EXISTS `sfwf_resendinfo`;
CREATE TABLE `sfwf_resendinfo` (
`PROCESSID`  varchar(50)  NOT NULL COMMENT '流程定义或者任务编号' ,
`LASTSENDTIME`   Timestamp NULL  COMMENT '最近发送时间',
`SENDEDNUMBER`   int(4)	NULL COMMENT '已发送次数',
PRIMARY KEY (`PROCESSID`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8

;

-- ----------------------------
-- View structure for sfwf_v_alldonetokenitemview
-- ----------------------------
DROP VIEW IF EXISTS `sfwf_v_alldonetokenitemview`;
CREATE  VIEW `sfwf_v_alldonetokenitemview` AS select `sfwf_node`.`NAME` AS `NAME`,`sfwf_node`.`NODECOMMENT` AS `NODECOMMENT`,`sfwf_node`.`TYPE` AS `TYPE`,`sfwf_node`.`PROCESSDEFINITIONNAME` AS `PROCESSDEFINITIONNAME`,`sfwf_node`.`ASSIGNEE` AS `PREASSIGNEE`,`sfwf_node`.`ICON` AS `icon`,`sfwf_processdefinition`.`PDGROUP` AS `PDGROUP`,`sfwf_donetokenhistory`.`ACTIVITYID` AS `ACTIVITYID`,`sfwf_donetokenhistory`.`CHECKINOUT` AS `CHECKINOUT`,`sfwf_donetokenhistory`.`CHECKINOUTUSER` AS `CHECKINOUTUSER`,`sfwf_donetokenhistory`.`ASSIGNEE` AS `ASSIGNEE`,`sfwf_donetokenhistory`.`CONDITIONDESCRIPTION` AS `CONDITIONDESCRIPTION`,`sfwf_donetokenhistory`.`ENDTIME` AS `ENDTIME`,`sfwf_donetokenhistory`.`expr` AS `EXPR`,`sfwf_donetokenhistory`.`EXTEND` AS `EXTEND`,`sfwf_donetokenhistory`.`FIRSTPROPERTY` AS `FIRSTPROPERTY`,`sfwf_donetokenhistory`.`ID` AS `ID`,`sfwf_donetokenhistory`.`INSTANCEID` AS `INSTANCEID`,`sfwf_donetokenhistory`.`ISDONE` AS `ISDONE`,`sfwf_donetokenhistory`.`OTHERSTATE` AS `OTHERSTATE`,`sfwf_donetokenhistory`.`SECONDPROPERTY` AS `SECONDPROPERTY`,`sfwf_donetokenhistory`.`STARTTIME` AS `STARTTIME`,`sfwf_donetokenhistory`.`STATE` AS `STATE`,`sfwf_donetokenhistory`.`TIME` AS `TIME`,`sfwf_donetokenhistory`.`VERSION` AS `VERSION`,`sfwf_donetokenhistory`.`PREID` AS `PREID`,`sfwf_donetokenhistory`.`PRENODEID` AS `PRENODEID`,`sfwf_donetokenhistory`.`TIMELIMIT` AS `TIMELIMIT`,`sfwf_donetokenhistory`.`SEQUENCE` AS `SEQUENCE`,`sfwf_donetokenhistory`.`EXTEND` AS `URL`,`sfwf_processdefinition`.`PROCESSDEFINITIONNAME` AS `PDNAME` from ((`sfwf_processdefinition` join `sfwf_donetokenhistory`) join `sfwf_node`) where ((`sfwf_node`.`ID` = `sfwf_donetokenhistory`.`ACTIVITYID`) and (`sfwf_processdefinition`.`NAME` = `sfwf_node`.`PROCESSDEFINITIONNAME`)) ;

-- ----------------------------
-- View structure for sfwf_v_alltokenitemview
-- ----------------------------
DROP VIEW IF EXISTS `sfwf_v_alltokenitemview`;
CREATE  VIEW `sfwf_v_alltokenitemview` AS select `sfwf_node`.`NAME` AS `NAME`,`sfwf_node`.`NODECOMMENT` AS `NODECOMMENT`,`sfwf_node`.`TYPE` AS `TYPE`,`sfwf_node`.`PROCESSDEFINITIONNAME` AS `PROCESSDEFINITIONNAME`,`sfwf_node`.`ASSIGNEE` AS `PREASSIGNEE`,`sfwf_node`.`ICON` AS `icon`,`sfwf_processdefinition`.`PDGROUP` AS `PDGROUP`,`sfwf_token`.`ACTIVITYID` AS `ACTIVITYID`,`sfwf_token`.`CHECKINOUT` AS `CHECKINOUT`,`sfwf_token`.`CHECKINOUTUSER` AS `CHECKINOUTUSER`,`sfwf_token`.`ASSIGNEE` AS `ASSIGNEE`,`sfwf_token`.`CONDITIONDESCRIPTION` AS `CONDITIONDESCRIPTION`,`sfwf_token`.`ENDTIME` AS `ENDTIME`,`sfwf_token`.`expr` AS `EXPR`,`sfwf_token`.`EXTEND` AS `EXTEND`,`sfwf_token`.`FIRSTPROPERTY` AS `FIRSTPROPERTY`,`sfwf_token`.`ID` AS `ID`,`sfwf_token`.`INSTANCEID` AS `INSTANCEID`,`sfwf_token`.`ISDONE` AS `ISDONE`,`sfwf_token`.`OTHERSTATE` AS `OTHERSTATE`,`sfwf_token`.`SECONDPROPERTY` AS `SECONDPROPERTY`,`sfwf_token`.`STARTTIME` AS `STARTTIME`,`sfwf_token`.`STATE` AS `STATE`,`sfwf_token`.`TIME` AS `TIME`,`sfwf_token`.`VERSION` AS `VERSION`,`sfwf_token`.`PREID` AS `PREID`,`sfwf_token`.`PRENODEID` AS `PRENODEID`,`sfwf_token`.`TIMELIMIT` AS `TIMELIMIT`,`sfwf_token`.`SEQUENCE` AS `SEQUENCE`,`sfwf_token`.`EXTEND` AS `URL`,`sfwf_processdefinition`.`PROCESSDEFINITIONNAME` AS `PDNAME` from ((`sfwf_processdefinition` join `sfwf_token`) join `sfwf_node`) where ((`sfwf_node`.`ID` = `sfwf_token`.`ACTIVITYID`) and (`sfwf_processdefinition`.`NAME` = `sfwf_node`.`PROCESSDEFINITIONNAME`)) ;

-- ----------------------------
-- View structure for sfwf_v_doneinstanceitemview
-- ----------------------------
DROP VIEW IF EXISTS `sfwf_v_doneinstanceitemview`;
CREATE  VIEW `sfwf_v_doneinstanceitemview` AS select `sfwf_donetokenhistory`.`ACTIVITYID` AS `ACTIVITYID`,`sfwf_donetokenhistory`.`CHECKINOUT` AS `CHECKINOUT`,`sfwf_donetokenhistory`.`CHECKINOUTUSER` AS `CHECKINOUTUSER`,`sfwf_donetokenhistory`.`ASSIGNEE` AS `ASSIGNEE`,`sfwf_donetokenhistory`.`CONDITIONDESCRIPTION` AS `CONDITIONDESCRIPTION`,`sfwf_donetokenhistory`.`expr` AS `EXPR`,`sfwf_donetokenhistory`.`EXTEND` AS `EXTEND`,`sfwf_donetokenhistory`.`FIRSTPROPERTY` AS `FIRSTPROPERTY`,`sfwf_donetokenhistory`.`ID` AS `ID`,`sfwf_donetokenhistory`.`INSTANCEID` AS `INSTANCEID`,`sfwf_donetokenhistory`.`ISDONE` AS `ISDONE`,`sfwf_donetokenhistory`.`OTHERSTATE` AS `OTHERSTATE`,`sfwf_donetokenhistory`.`SECONDPROPERTY` AS `SECONDPROPERTY`,`sfwf_donetokenhistory`.`TIME` AS `TIME`,`sfwf_donetokenhistory`.`VERSION` AS `VERSION`,`sfwf_donetokenhistory`.`PREID` AS `PREID`,`sfwf_donetokenhistory`.`PRENODEID` AS `PRENODEID`,`sfwf_donetokenhistory`.`STARTTIME` AS `tokenStartTime`,`sfwf_donetokenhistory`.`ENDTIME` AS `tokenEndTime`,`sfwf_doneinstance`.`PROCESSDEFINITIONNAME` AS `PROCESSDEFINITIONNAME`,`sfwf_doneinstance`.`STARTTIME` AS `STARTTIME`,`sfwf_doneinstance`.`ENDTIME` AS `ENDTIME`,`sfwf_doneinstance`.`INSTANCECOMMENT` AS `INSTANCECOMMENT`,`sfwf_doneinstance`.`INITIATOR` AS `INITIATOR`,`sfwf_doneinstance`.`STATE` AS `STATE`,`sfwf_node`.`NAME` AS `name`,`sfwf_event`.`URL` AS `URL` from ((`sfwf_donetokenhistory` join `sfwf_doneinstance`) join (`sfwf_node` left join `sfwf_event` on((`sfwf_node`.`ID` = `sfwf_event`.`NODEID`)))) where ((`sfwf_doneinstance`.`ID` = `sfwf_donetokenhistory`.`INSTANCEID`) and (`sfwf_node`.`ID` = `sfwf_donetokenhistory`.`ACTIVITYID`)) ;

-- ----------------------------
-- View structure for sfwf_v_donetaskalarm
-- ----------------------------
DROP VIEW IF EXISTS `sfwf_v_donetaskalarm`;
CREATE  VIEW `sfwf_v_donetaskalarm` AS select `sfwf_resendinfo`.`PROCESSID` AS `processid`,`sfwf_resendinfo`.`LASTSENDTIME` AS `lastsendtime`,`sfwf_resendinfo`.`SENDEDNUMBER` AS `sendednumber`,`sfwf_node`.`ID` AS `taskid`,`sfwf_node`.`NAME` AS `taskname`,`sfwf_node`.`ICON` AS `timelimit`,`sfwf_donetokenhistory`.`ASSIGNEE` AS `assignee`,`sfwf_donetokenhistory`.`STARTTIME` AS `starttime`,`sfwf_donetokenhistory`.`ENDTIME` AS `endtime`,`sfwf_donetokenhistory`.`INSTANCEID` AS `instanceid`,`sfwf_processdefinition`.`NAME` AS `pdid`,`sfwf_processdefinition`.`PROCESSDEFINITIONNAME` AS `pdname`,`sfwf_processdefinition`.`CARLANDERID` AS `carlanderid`,`sfwf_definitiongroup`.`GROUPID` AS `groupid` from ((((`sfwf_resendinfo` join `sfwf_node`) join `sfwf_donetokenhistory`) join `sfwf_processdefinition`) join `sfwf_definitiongroup`) where ((`sfwf_resendinfo`.`PROCESSID` = `sfwf_donetokenhistory`.`ID`) and (`sfwf_donetokenhistory`.`ACTIVITYID` = `sfwf_node`.`ID`) and (`sfwf_node`.`PROCESSDEFINITIONNAME` = `sfwf_processdefinition`.`NAME`) and (`sfwf_processdefinition`.`PROCESSDEFINITIONNAME` = `sfwf_definitiongroup`.`DEFINITIONNAME`)) ;

-- ----------------------------
-- View structure for sfwf_v_doneworkitemview
-- ----------------------------
DROP VIEW IF EXISTS `sfwf_v_doneworkitemview`;
CREATE  VIEW `sfwf_v_doneworkitemview` AS select `sfwf_node`.`NAME` AS `NAME`,`sfwf_node`.`NODECOMMENT` AS `NODECOMMENT`,`sfwf_node`.`PROCESSDEFINITIONNAME` AS `PROCESSDEFINITIONNAME`,`sfwf_node`.`TYPE` AS `TYPE`,`sfwf_node`.`ASSIGNEE` AS `PREASSIGNEE`,`sfwf_node`.`ICON` AS `icon`,`sfwf_processdefinition`.`PDGROUP` AS `PDGROUP`,`sfwf_donetokenhistory`.`ACTIVITYID` AS `ACTIVITYID`,`sfwf_donetokenhistory`.`CHECKINOUT` AS `CHECKINOUT`,`sfwf_donetokenhistory`.`CHECKINOUTUSER` AS `CHECKINOUTUSER`,`sfwf_donetokenhistory`.`ASSIGNEE` AS `ASSIGNEE`,`sfwf_donetokenhistory`.`CONDITIONDESCRIPTION` AS `CONDITIONDESCRIPTION`,`sfwf_donetokenhistory`.`ENDTIME` AS `ENDTIME`,`sfwf_donetokenhistory`.`expr` AS `EXPR`,`sfwf_donetokenhistory`.`EXTEND` AS `EXTEND`,`sfwf_donetokenhistory`.`FIRSTPROPERTY` AS `FIRSTPROPERTY`,`sfwf_donetokenhistory`.`ID` AS `ID`,`sfwf_donetokenhistory`.`INSTANCEID` AS `INSTANCEID`,`sfwf_donetokenhistory`.`ISDONE` AS `ISDONE`,`sfwf_donetokenhistory`.`OTHERSTATE` AS `OTHERSTATE`,`sfwf_donetokenhistory`.`SECONDPROPERTY` AS `SECONDPROPERTY`,`sfwf_donetokenhistory`.`STARTTIME` AS `STARTTIME`,`sfwf_donetokenhistory`.`STATE` AS `STATE`,`sfwf_donetokenhistory`.`TIME` AS `TIME`,`sfwf_donetokenhistory`.`VERSION` AS `VERSION`,`sfwf_donetokenhistory`.`PREID` AS `PREID`,`sfwf_donetokenhistory`.`PRENODEID` AS `PRENODEID`,`sfwf_donetokenhistory`.`TIMELIMIT` AS `TIMELIMIT`,`sfwf_donetokenhistory`.`SEQUENCE` AS `SEQUENCE`,`sfwf_donetokenhistory`.`EXTEND` AS `URL`,`sfwf_processdefinition`.`PROCESSDEFINITIONNAME` AS `PDNAME` from ((`sfwf_processdefinition` join `sfwf_donetokenhistory`) join `sfwf_node`) where (((`sfwf_node`.`TYPE` = 3) or (`sfwf_node`.`TYPE` = 9) or (`sfwf_node`.`TYPE` = 11) or (`sfwf_node`.`TYPE` = 1)) and (`sfwf_node`.`ID` = `sfwf_donetokenhistory`.`ACTIVITYID`) and (`sfwf_processdefinition`.`NAME` = `sfwf_node`.`PROCESSDEFINITIONNAME`)) ;

-- ----------------------------
-- View structure for sfwf_v_instanceitemview
-- ----------------------------
DROP VIEW IF EXISTS `sfwf_v_instanceitemview`;
CREATE  VIEW `sfwf_v_instanceitemview` AS select `sfwf_token`.`ACTIVITYID` AS `ACTIVITYID`,`sfwf_token`.`CHECKINOUT` AS `CHECKINOUT`,`sfwf_token`.`CHECKINOUTUSER` AS `CHECKINOUTUSER`,`sfwf_token`.`ASSIGNEE` AS `ASSIGNEE`,`sfwf_token`.`CONDITIONDESCRIPTION` AS `CONDITIONDESCRIPTION`,`sfwf_token`.`expr` AS `EXPR`,`sfwf_token`.`EXTEND` AS `EXTEND`,`sfwf_token`.`FIRSTPROPERTY` AS `FIRSTPROPERTY`,`sfwf_token`.`ID` AS `ID`,`sfwf_token`.`INSTANCEID` AS `INSTANCEID`,`sfwf_token`.`ISDONE` AS `ISDONE`,`sfwf_token`.`OTHERSTATE` AS `OTHERSTATE`,`sfwf_token`.`SECONDPROPERTY` AS `SECONDPROPERTY`,`sfwf_token`.`TIME` AS `TIME`,`sfwf_token`.`VERSION` AS `VERSION`,`sfwf_token`.`PREID` AS `PREID`,`sfwf_token`.`PRENODEID` AS `PRENODEID`,`sfwf_token`.`STARTTIME` AS `tokenStartTime`,`sfwf_token`.`ENDTIME` AS `tokenEndTime`,`sfwf_instance`.`PROCESSDEFINITIONNAME` AS `PROCESSDEFINITIONNAME`,`sfwf_instance`.`STARTTIME` AS `STARTTIME`,`sfwf_instance`.`ENDTIME` AS `ENDTIME`,`sfwf_instance`.`INSTANCECOMMENT` AS `INSTANCECOMMENT`,`sfwf_instance`.`INITIATOR` AS `INITIATOR`,`sfwf_token`.`STATE` AS `STATE`,`sfwf_node`.`NAME` AS `name`,`sfwf_event`.`URL` AS `URL` from ((`sfwf_token` join `sfwf_instance`) join (`sfwf_node` left join `sfwf_event` on((`sfwf_node`.`ID` = `sfwf_event`.`NODEID`)))) where ((`sfwf_instance`.`ID` = `sfwf_token`.`INSTANCEID`) and (`sfwf_node`.`ID` = `sfwf_token`.`ACTIVITYID`)) ;

-- ----------------------------
-- View structure for sfwf_v_nextnodeview
-- ----------------------------
DROP VIEW IF EXISTS `sfwf_v_nextnodeview`;
CREATE  VIEW `sfwf_v_nextnodeview` AS select `sfwf_node`.`TYPE` AS `type`,`sfwf_transition`.`TF` AS `transitioncondition`,`sfwf_transition`.`ID_FROM` AS `id_from`,`sfwf_node`.`NAME` AS `nodename`,`sfwf_node`.`FIRSTPROPERTY` AS `FIRSTPROPERTY`,`sfwf_node`.`SECONDPROPERTY` AS `SECONDPROPERTY`,`sfwf_node`.`ASSIGNEE` AS `ASSIGNEE`,`sfwf_node`.`CONDITIONDESCRIPTION` AS `conditiondescription`,`sfwf_node`.`EXTEND` AS `extend`,`sfwf_node`.`PRIORITY` AS `priority`,`sfwf_activitymeta`.`NAME` AS `name`,`sfwf_node`.`ID` AS `id`,`sfwf_node`.`ICON` AS `ICON`,`sfwf_node`.`PROCESSDEFINITIONNAME` AS `PROCESSDEFINITIONNAME`,`sfwf_node`.`EXPR` AS `EXPR` from ((`sfwf_activitymeta` join `sfwf_node`) join `sfwf_transition`) where ((`sfwf_transition`.`ID_TO` = `sfwf_node`.`ID`) and (`sfwf_node`.`TYPE` = `sfwf_activitymeta`.`TYPE`)) ;

-- ----------------------------
-- View structure for sfwf_v_nodeview
-- ----------------------------
DROP VIEW IF EXISTS `sfwf_v_nodeview`;
CREATE  VIEW `sfwf_v_nodeview` AS select `sfwf_node`.`ID` AS `id`,`sfwf_node`.`NAME` AS `nodename`,`sfwf_node`.`FIRSTPROPERTY` AS `FIRSTPROPERTY`,`sfwf_node`.`SECONDPROPERTY` AS `SECONDPROPERTY`,`sfwf_node`.`ASSIGNEE` AS `ASSIGNEE`,`sfwf_node`.`CONDITIONDESCRIPTION` AS `conditiondescription`,`sfwf_node`.`EXTEND` AS `extend`,`sfwf_node`.`PRIORITY` AS `priority`,`sfwf_activitymeta`.`NAME` AS `name`,`sfwf_node`.`ICON` AS `ICON`,`sfwf_node`.`TYPE` AS `type`,`sfwf_node`.`GROUPID` AS `groupID`,`sfwf_node`.`PROCESSDEFINITIONNAME` AS `PROCESSDEFINITIONNAME`,`sfwf_event`.`URL` AS `URL`,`sfwf_node`.`EXPR` AS `EXPR` from (`sfwf_activitymeta` join (`sfwf_node` left join `sfwf_event` on((`sfwf_node`.`ID` = `sfwf_event`.`NODEID`)))) where (`sfwf_node`.`TYPE` = `sfwf_activitymeta`.`TYPE`) ;

-- ----------------------------
-- View structure for sfwf_v_prevnodeview
-- ----------------------------
DROP VIEW IF EXISTS `sfwf_v_prevnodeview`;
CREATE  VIEW `sfwf_v_prevnodeview` AS select `sfwf_transition`.`ID_TO` AS `id_to`,`sfwf_node`.`NAME` AS `nodename`,`sfwf_node`.`FIRSTPROPERTY` AS `FIRSTPROPERTY`,`sfwf_node`.`SECONDPROPERTY` AS `SECONDPROPERTY`,`sfwf_node`.`ASSIGNEE` AS `ASSIGNEE`,`sfwf_node`.`CONDITIONDESCRIPTION` AS `conditiondescription`,`sfwf_node`.`EXTEND` AS `extend`,`sfwf_node`.`PRIORITY` AS `priority`,`sfwf_activitymeta`.`NAME` AS `name`,`sfwf_node`.`ID` AS `id`,`sfwf_node`.`ICON` AS `ICON`,`sfwf_node`.`TYPE` AS `type`,`sfwf_node`.`GROUPID` AS `groupID`,`sfwf_node`.`PROCESSDEFINITIONNAME` AS `PROCESSDEFINITIONNAME`,`sfwf_node`.`EXPR` AS `EXPR` from ((`sfwf_activitymeta` join `sfwf_node`) join `sfwf_transition`) where ((`sfwf_transition`.`ID_FROM` = `sfwf_node`.`ID`) and (`sfwf_node`.`TYPE` = `sfwf_activitymeta`.`TYPE`)) ;

-- ----------------------------
-- View structure for sfwf_v_processalarm
-- ----------------------------
DROP VIEW IF EXISTS `sfwf_v_processalarm`;
CREATE  VIEW `sfwf_v_processalarm` AS select `sfwf_resendinfo`.`PROCESSID` AS `processid`,`sfwf_resendinfo`.`LASTSENDTIME` AS `lastsendtime`,`sfwf_resendinfo`.`SENDEDNUMBER` AS `sendednumber`,`sfwf_processdefinition`.`NAME` AS `taskid`,`sfwf_processdefinition`.`PROCESSDEFINITIONNAME` AS `taskname`,`sfwf_processdefinition`.`NAME` AS `pdid`,`sfwf_processdefinition`.`PROCESSDEFINITIONNAME` AS `pdname`,`sfwf_processdefinition`.`ASSIGNEE` AS `timelimit`,`sfwf_instance`.`INITIATOR` AS `assignee`,`sfwf_instance`.`STARTTIME` AS `starttime`,`sfwf_instance`.`ENDTIME` AS `endtime`,`sfwf_instance`.`ID` AS `instanceid`,`sfwf_processdefinition`.`CARLANDERID` AS `carlanderid`,`sfwf_definitiongroup`.`GROUPID` AS `groupid` from (((`sfwf_resendinfo` join `sfwf_instance`) join `sfwf_processdefinition`) join `sfwf_definitiongroup`) where ((`sfwf_resendinfo`.`PROCESSID` = `sfwf_instance`.`ID`) and (`sfwf_instance`.`PROCESSDEFINITIONNAME` = `sfwf_processdefinition`.`NAME`) and (`sfwf_processdefinition`.`PROCESSDEFINITIONNAME` = `sfwf_definitiongroup`.`DEFINITIONNAME`)) ;

-- ----------------------------
-- View structure for sfwf_v_taskalarm
-- ----------------------------
DROP VIEW IF EXISTS `sfwf_v_taskalarm`;
CREATE  VIEW `sfwf_v_taskalarm` AS select `sfwf_resendinfo`.`PROCESSID` AS `processid`,`sfwf_resendinfo`.`LASTSENDTIME` AS `lastsendtime`,`sfwf_resendinfo`.`SENDEDNUMBER` AS `sendednumber`,`sfwf_node`.`ID` AS `taskid`,`sfwf_node`.`NAME` AS `taskname`,`sfwf_node`.`ICON` AS `timelimit`,`sfwf_token`.`ASSIGNEE` AS `assignee`,`sfwf_token`.`STARTTIME` AS `starttime`,`sfwf_token`.`ENDTIME` AS `endtime`,`sfwf_token`.`INSTANCEID` AS `instanceid`,`sfwf_processdefinition`.`NAME` AS `pdid`,`sfwf_processdefinition`.`PROCESSDEFINITIONNAME` AS `pdname`,`sfwf_processdefinition`.`CARLANDERID` AS `carlanderid`,`sfwf_definitiongroup`.`GROUPID` AS `groupid` from ((((`sfwf_resendinfo` join `sfwf_node`) join `sfwf_token`) join `sfwf_processdefinition`) join `sfwf_definitiongroup`) where ((`sfwf_resendinfo`.`PROCESSID` = `sfwf_token`.`ID`) and (`sfwf_token`.`ACTIVITYID` = `sfwf_node`.`ID`) and (`sfwf_node`.`PROCESSDEFINITIONNAME` = `sfwf_processdefinition`.`NAME`) and (`sfwf_processdefinition`.`PROCESSDEFINITIONNAME` = `sfwf_definitiongroup`.`DEFINITIONNAME`)) ;

-- ----------------------------
-- View structure for sfwf_v_workitemview
-- ----------------------------
DROP VIEW IF EXISTS `sfwf_v_workitemview`;
CREATE  VIEW `sfwf_v_workitemview` AS select `sfwf_node`.`NAME` AS `NAME`,`sfwf_node`.`NODECOMMENT` AS `NODECOMMENT`,`sfwf_node`.`PROCESSDEFINITIONNAME` AS `PROCESSDEFINITIONNAME`,`sfwf_node`.`TYPE` AS `TYPE`,`sfwf_node`.`ASSIGNEE` AS `PREASSIGNEE`,`sfwf_node`.`ICON` AS `icon`,`sfwf_processdefinition`.`PDGROUP` AS `PDGROUP`,`sfwf_token`.`ACTIVITYID` AS `ACTIVITYID`,`sfwf_token`.`CHECKINOUT` AS `CHECKINOUT`,`sfwf_token`.`CHECKINOUTUSER` AS `CHECKINOUTUSER`,`sfwf_token`.`ASSIGNEE` AS `ASSIGNEE`,`sfwf_token`.`CONDITIONDESCRIPTION` AS `CONDITIONDESCRIPTION`,`sfwf_token`.`ENDTIME` AS `ENDTIME`,`sfwf_token`.`expr` AS `EXPR`,`sfwf_token`.`EXTEND` AS `EXTEND`,`sfwf_token`.`FIRSTPROPERTY` AS `FIRSTPROPERTY`,`sfwf_token`.`ID` AS `ID`,`sfwf_token`.`INSTANCEID` AS `INSTANCEID`,`sfwf_token`.`ISDONE` AS `ISDONE`,`sfwf_token`.`OTHERSTATE` AS `OTHERSTATE`,`sfwf_token`.`SECONDPROPERTY` AS `SECONDPROPERTY`,`sfwf_token`.`STARTTIME` AS `STARTTIME`,`sfwf_token`.`STATE` AS `STATE`,`sfwf_token`.`TIME` AS `TIME`,`sfwf_token`.`VERSION` AS `VERSION`,`sfwf_token`.`PREID` AS `PREID`,`sfwf_token`.`PRENODEID` AS `PRENODEID`,`sfwf_token`.`TIMELIMIT` AS `TIMELIMIT`,`sfwf_token`.`SEQUENCE` AS `SEQUENCE`,`sfwf_token`.`EXTEND` AS `URL`,`sfwf_processdefinition`.`PROCESSDEFINITIONNAME` AS `PDNAME` from ((`sfwf_processdefinition` join `sfwf_token`) join `sfwf_node`) where (((`sfwf_node`.`TYPE` = 3) or (`sfwf_node`.`TYPE` = 9) or (`sfwf_node`.`TYPE` = 11) or (`sfwf_node`.`TYPE` = 1)) and (`sfwf_node`.`ID` = `sfwf_token`.`ACTIVITYID`) and (`sfwf_processdefinition`.`NAME` = `sfwf_node`.`PROCESSDEFINITIONNAME`)) ;

-- ----------------------------
-- Function structure for currval
-- ----------------------------
DROP FUNCTION IF EXISTS `currval`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `currval`(seq_name VARCHAR(50)) RETURNS int(11)
BEGIN
DECLARE VALUE INTEGER;
SET VALUE=0;
SELECT current_value INTO VALUE
FROM sys_sequence 
WHERE NAME=seq_name;
RETURN VALUE;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for F_GET_AUTH
-- ----------------------------
DROP FUNCTION IF EXISTS `F_GET_AUTH`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `F_GET_AUTH`() RETURNS varchar(16) CHARSET gbk
begin 
	 declare max_id varchar(16); 
	 set max_id = sfwf_f_nextval('SEQ_AUTH') ;
   return max_id;  
end
;;
DELIMITER ;

-- ----------------------------
-- Function structure for F_GET_INSTANCE
-- ----------------------------
DROP FUNCTION IF EXISTS `F_GET_INSTANCE`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `F_GET_INSTANCE`() RETURNS varchar(16) CHARSET gbk
begin 
	 declare max_id varchar(16); 
	 set max_id = sfwf_f_nextval('SEQ_INSTANCE') ;
   return max_id;  
end
;;
DELIMITER ;

-- ----------------------------
-- Function structure for F_GET_PDNODE
-- ----------------------------
DROP FUNCTION IF EXISTS `F_GET_PDNODE`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `F_GET_PDNODE`() RETURNS varchar(16) CHARSET gbk
begin 
	 declare max_id varchar(16); 
	 set max_id = sfwf_f_nextval('SEQ_PDNODE') ;
	 return max_id;  
end
;;
DELIMITER ;

-- ----------------------------
-- Function structure for F_GET_TOKEN
-- ----------------------------
DROP FUNCTION IF EXISTS `F_GET_TOKEN`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `F_GET_TOKEN`() RETURNS varchar(16) CHARSET gbk
begin 
	 declare max_id varchar(16); 
	 set max_id = sfwf_f_nextval('SEQ_TOKEN') ;
   return max_id;   
end
;;
DELIMITER ;

-- ----------------------------
-- Function structure for nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `nextval`(seq_name varchar(50)) RETURNS int(11)
BEGIN
UPDATE sys_sequence
SET CURRENT_VALUE = CURRENT_VALUE + INCREMENT
where name=seq_name;
return currval(seq_name);
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for sfwf_f_maxvalue_byseqname
-- ----------------------------
DROP FUNCTION IF EXISTS `sfwf_f_maxvalue_byseqname`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `sfwf_f_maxvalue_byseqname`(seq_name varchar(50)) RETURNS varchar(16) CHARSET gbk
begin   
    declare max_id varchar(16);
    declare max_id_seq varchar(8);
    declare max_id_date varchar(8);    
  	#取序列
    select lpad(sfwf_seq.max2,8,'0') into max_id_seq from sfwf_seq where name =  seq_name ;   
  
    #取日期
    #set max_id_date = date_format(now(),'%Y%m%d');
    set max_id_date = date_format(now(),'%m');
    
    #替换规则   
    set max_id = concat(max_id_date, max_id_seq);
  
    return max_id;   
end
;;
DELIMITER ;

-- ----------------------------
-- Function structure for sfwf_f_nextval
-- ----------------------------
DROP FUNCTION IF EXISTS `sfwf_f_nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `sfwf_f_nextval`(seq_name varchar(50)) RETURNS varchar(16) CHARSET gbk
begin
	 declare seq_max bigint(8);   
	 select sfwf_seq.max2 into seq_max from sfwf_seq where name =  seq_name ;   
 	 if seq_max = 99999999 then  
 	 		update sfwf_seq set max2 = 1001  where name = seq_name;
	 else
   		update sfwf_seq set max2 = max2 + next  where name = seq_name;   
   end if;
   
   return sfwf_f_maxvalue_byseqname(seq_name);   
end
;;
DELIMITER ;

-- ----------------------------
-- Auto increment value for sfwf_seq
-- ----------------------------
ALTER TABLE `sfwf_seq` AUTO_INCREMENT=5;
