-- prompt '正在添加数据TABLE ---SFWF_CALENDARWORKTIME'
-- insert data for init and test  
insert into SFWF_CALENDARWORKTIME (NAME, DESCRIPTION, AMSTARTHH, AMSTARTMI, AMENDHH, AMEDNMI, PMSTARTHH, PMSTARTMI, PMENDHH, PMEDNMI)
values ('summer', '朝九晚五 ', 9, 0, 12, 0, 12, 0, 17, 0);
insert into SFWF_CALENDARWORKTIME (NAME, DESCRIPTION, AMSTARTHH, AMSTARTMI, AMENDHH, AMEDNMI, PMSTARTHH, PMSTARTMI, PMENDHH, PMEDNMI)
values ('default', '默认安排', 9, 0, 12, 0, 13, 0, 18, 0);
insert into SFWF_CALENDAR (NAME, DESCRIPTION, ISDEFAULT)
values ('system', 'system default calendar', 'Y');
insert into SFWF_CALENDARWORKTIME (NAME, DESCRIPTION, AMSTARTHH, AMSTARTMI, AMENDHH, AMEDNMI, PMSTARTHH, PMSTARTMI, PMENDHH, PMEDNMI)
values ('24hour', '24hour', 0, 0, 12, 0, 12, 0, 24, 0);
insert into SFWF_CALENDAR (NAME, DESCRIPTION, ISDEFAULT)
values ('7*24', '7*24hour', 'N');


-- prompt '正在添加数据TABLE ---SFWF_CALENDARRULE'
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('001', 'system', 'Y', 'W', 0, null, null, 2, 'default');
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('002', 'system', 'Y', 'W', 1, null, null, 3, 'default');
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('003', 'system', 'Y', 'W', 2, null, null, 4, 'default');
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('004', 'system', 'Y', 'W', 3, null, null, 5, 'default');
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('005', 'system', 'Y', 'W', 4, null, null, 6, 'default');
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('006', 'system', 'N', 'P', 5, str_to_date('01/05/2005', '%d/%m/%Y'), str_to_date('07/05/2005', '%d/%m/%Y'), null, 'default');
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('007', 'system', 'Y', 'D', 6, str_to_date('08/05/2005', '%d/%m/%Y'), null, null, 'default');
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('008', 'system', 'Y', 'D', 7, str_to_date('30/04/2005', '%d/%m/%Y'), null, null, 'default');
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('011', '7*24', 'Y', 'W', 0, null, null, 1, 'default');
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('012', '7*24', 'Y', 'W', 1, null, null, 2, 'default');
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('013', '7*24', 'Y', 'W', 2, null, null, 3, 'default');
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('014', '7*24', 'Y', 'W', 3, null, null, 4, 'default');
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('015', '7*24', 'Y', 'W', 4, null, null, 5, 'default');
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('016', '7*24', 'Y', 'W', 5, null, null, 6, 'default');
insert into SFWF_CALENDARRULE (RULEID, CALENDARID, ISBUSY, RULETYPE, PRIORITY, STARTDATE, ENDDATE, VALIDDAYOFWEEK, WORKTIME)
values ('017', '7*24', 'Y', 'W', 6, null, null, 7, 'default');

-- prompt '正在载入数据 ---SFWF_ACTIVITYMETA' 
-- insert data for init
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('AutoTask', 10, 'images/obj35/automatic.png', '自动');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('ManualTaskGroup', 9, 'images/obj35/taskgroup.png', '任务组');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('AndSplit', 4, 'images/obj35/andSplit.png', '与分支');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('AndJoin', 5, 'images/obj35/andJoin.png', '与汇合');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('XorSplit', 6, 'images/obj35/orSplit.png', '或分支');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('OrJoin', 7, 'images/obj35/orJoin.png', '或汇合');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('ConditionSplit', 8, 'images/obj35/conSplit.png', '判断');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('Start', 1, 'images/obj35/start.png', '开始');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('End', 2, 'images/obj35/end.png', '结束');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('ManualTask', 3, 'images/obj35/task.png', '任务');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION) 
values('JoinTask',11,'images/obj35/jointask.png','会签任务');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('SubFlow', 0, 'images/obj35/subflow.png', '子流程');
-- insert data yuxxa 2005-11-8
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('ConditionMulti', 12, 'images/obj35/conMulti.png', '多分支判断节点');
-- insert data gaoyz 2008-8-1
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('SubSys', 13, 'images/obj35/subsys.png', '子系统节点');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('EndImm', 14, 'images/obj35/end_imm.png', '立即结束');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('Group', 20, 'images/obj35/group.png', '组节点');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('GroupStart', 21, 'images/obj35/group_start.png', '组开始');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('GroupEnd', 22, 'images/obj35/group_end.png', '组结束');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('Receive', 41, 'images/obj35/receive.png', 'Receive');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('Invoke', 42, 'images/obj35/invoke.png', 'Invoke');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('Reply', 43, 'images/obj35/reply.png', 'Reply');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('Assign', 44, 'images/obj35/assign.png', 'Assign');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('Switch', 23, 'images/obj35/group.png', '分支组');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('While', 24, 'images/obj35/group.png', '循环组');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('Loop', 25, 'images/obj35/group.png', '循环组');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('Try', 26, 'images/obj35/group.png', 'Try');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('Catch', 27, 'images/obj35/group.png', 'Catch');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('Finally', 28, 'images/obj35/group.png', 'Finally');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('EventGate', 50, 'images/obj35/Event_Gate.png', '事件网关');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('EventMsgInter', 51, 'images/obj35/Event_MsgInter.png', '交互消息事件');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('EventTimerInter', 52, 'images/obj35/Event_TimerInter.png', '交互定时事件');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('EventMsgStart', 53, 'images/obj35/startMsg.png', '消息启动开始节点');
insert into SFWF_ACTIVITYMETA (NAME, TYPE, ICON, DESCRIPTION)
values ('ParallelGateWay', 54, 'images/obj35/parallel.png', '并行网关');

INSERT INTO sfwf_transition_Type (TYPE_DM, TYPE_MC,XYBZ,YXBZ)
VALUES ('1', '顺序连线', 'Y', 'Y');
INSERT INTO sfwf_transition_Type (TYPE_DM, TYPE_MC,XYBZ,YXBZ)
VALUES ('2', '消息连线', 'Y', 'Y');
INSERT INTO sfwf_transition_Type (TYPE_DM, TYPE_MC,XYBZ,YXBZ)
VALUES ('3', '关联连线', 'Y', 'Y');
INSERT INTO sfwf_transition_Type (TYPE_DM, TYPE_MC,XYBZ,YXBZ)
VALUES ('4', '数据关联连线', 'Y', 'Y');

INSERT INTO sfwf_transition_brokenType (BROKENTYPE_DM, BROKENTYPE_MC,XYBZ,YXBZ)
VALUES ('1', '直线', 'Y', 'Y');
INSERT INTO sfwf_transition_brokenType (BROKENTYPE_DM, BROKENTYPE_MC,XYBZ,YXBZ)
VALUES ('2', '直边多边线', 'Y', 'Y');
INSERT INTO sfwf_transition_brokenType (BROKENTYPE_DM, BROKENTYPE_MC,XYBZ,YXBZ)
VALUES ('3', '曲边多边线', 'Y', 'Y');

INSERT INTO sfwf_system_sendRule (ID, NAME,DESCRIPTION,PARENTID)
VALUES ('1', '群发单审', '按照所选人员或者岗位进行发送，只发送一条待办', '0');
INSERT INTO sfwf_system_sendRule (ID, NAME,DESCRIPTION,PARENTID)
VALUES ('2', '规则发送', '按照某种规则进行发送，根据所选规则产生的人数不同，生成多条待办', '0');
INSERT INTO sfwf_system_sendRule (ID, NAME,DESCRIPTION,PARENTID)
VALUES ('3', '全部下发', '所选规则查找出的所有人都生成不同的待办，每人独立处理', '2');


-- prompt '正在添加数据TABLE ---SFWF_PARAMETER'
-- insert data for init and test  
insert into SFWF_PARAMETER(KEYNAME,KEYVALUE,DESCRIPTION,ISVISIBLE) 
values('SUSPENDFLAG','Y','是否启用冻结功能(Y开启N关闭)','Y');
insert into SFWF_PARAMETER(KEYNAME,KEYVALUE,DESCRIPTION,ISVISIBLE) 
values('ALARMSLEEPINTERVAL','10','任务提醒扫描间隔(单位:分钟)','Y');

insert into SFWF_PROCESSALARM(PROCESSID, ALARMTIME, ALARMTIMEUNIT, SENDINTERVAL, SENDINTERVALUNIT, MAXSENDNUMBER, ISINUSE, PROCESSFLAG, RHFLAG, ISFROZEN) 
values('00',1, 'H',1, 'H', 1, 'S', 'P', 'R', 'Y');
insert into SFWF_PROCESSALARM(PROCESSID, ALARMTIME, ALARMTIMEUNIT, SENDINTERVAL, SENDINTERVALUNIT, MAXSENDNUMBER, ISINUSE, PROCESSFLAG, RHFLAG, ISFROZEN) 
values('00',1, 'H',1, 'H', 1, 'S', 'P', 'H', 'N');
insert into SFWF_PROCESSALARM(PROCESSID, ALARMTIME, ALARMTIMEUNIT, SENDINTERVAL, SENDINTERVALUNIT, MAXSENDNUMBER, ISINUSE, PROCESSFLAG, RHFLAG, ISFROZEN) 
values('11',1, 'H',1, 'H', 1, 'S', 'J', 'R', 'Y');
insert into SFWF_PROCESSALARM(PROCESSID, ALARMTIME, ALARMTIMEUNIT, SENDINTERVAL, SENDINTERVALUNIT, MAXSENDNUMBER, ISINUSE, PROCESSFLAG, RHFLAG, ISFROZEN) 
values('11',1, 'H',1, 'H', 1, 'S', 'J', 'H', 'N');

-- prompt '正在载入数据 ---sfwf_groupfordefinition' 
-- insert data for init
insert into sfwf_groupfordefinition (GROUPID, GROUPNAME, PARENTID , GROUPTYPE, FORDEFINITION , FORSUBGROUP, ISVALID,  DEPTH, APPTYPE, OPERATEUSERID,  OPERATEDATE , DESCRIPTION)
values ('0', '流程分组', '0', '', 0,1,1,0,'3' ,'00000000000', '', '根节点');

-- prompt '正在载入数据 ---SFWF_ORG' 
-- Insert a default root org record
insert into SFWF_ORG (ORGID, NAME, PARENTID, JCID, DESCRIPTION, OPEN)
values('00000000000', '神州数码控股', '0', '00', '', 1);

-- prompt '正在载入数据 ---SFWF_POST' 
-- Insert a default ds_admin record
insert into SFWF_POST (POSTID, NAME, DESCRIPTION, ORGID, OPEN)
values('000000000000000', '超级用户岗', null, '00000000000', 1)
;
insert into SFWF_POST (POSTID, NAME, DESCRIPTION, ORGID, OPEN)
values('000000000000001', '测试岗', null, '00000000000', 1)
;

insert into SFWF_ROLE (ROLEID, NAME, DESCRIPTION, OPEN)
values('000000000000000', '超级用户角色', null, 1)
;
insert into SFWF_ROLE (ROLEID, NAME, DESCRIPTION, OPEN)
values('000000000000001', '测试角色', null, 1)
;

-- prompt '正在载入数据 ---SFWF_USER' 
-- Insert a default ds_admin record
insert into SFWF_USER (USERID, TNAME, ORGID, NAME, PASSWORD, PWTYPE, BEGINDATE, ENDDATE, OPEN)
values('00000000000', '超级管理员', '00000000000', 'admin', '888888', '1', null, null, 1)
;
insert into SFWF_USER (USERID, TNAME, ORGID, NAME, PASSWORD, PWTYPE, BEGINDATE, ENDDATE, OPEN)
values('00000000001', '测试用户', '00000000000', 'test', '888888', '1', null, null, 1)
;

-- PROMPT '正在载入数据 ---SFWF_JGJC'
insert into SFWF_JGJC (JCID, NAME, DESCRIPTION, OPEN)
values('00', '国家级', '',  1);
insert into SFWF_JGJC (JCID, NAME, DESCRIPTION, OPEN)
values('01', '省级', '',  1);
insert into SFWF_JGJC (JCID, NAME, DESCRIPTION, OPEN)
values('03', '地市级', '',  1);
insert into SFWF_JGJC (JCID, NAME, DESCRIPTION, OPEN)
values('05', '区县级', '',  1);
insert into SFWF_JGJC (JCID, NAME, DESCRIPTION, OPEN)
values('07', '乡镇级', '',  1);
insert into SFWF_JGJC (JCID, NAME, DESCRIPTION, OPEN)
values('09', '组级', '',  1);

-- prompt '正在载入数据 ---SFWF_USER_POST' 
-- Insert admin user and admin post
insert into SFWF_USER_POST (USERID, POSTID)
values('00000000000', '000000000000000')
;
insert into SFWF_USER_POST (USERID, POSTID)
values('00000000001', '000000000000001')
;

insert into SFWF_USER_ROLE (USERID, ROLEID)
values('00000000000', '000000000000000')
;
insert into SFWF_USER_ROLE (USERID, ROLEID)
values('00000000001', '000000000000001')
;


-- prompt '正在添加数据TABLE ---sfwf_param_viewconfig'
-- insert data for init and test  
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000001', 'dbjk_workitemid', '待办标识', '0', '10', '待办事宜标识', 1, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000002', 'dbjk_workitemname', '待办名称', '1', '10', '待办事宜名称', 1, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000003', 'dbjk_instancename', '流程名称', '2', '10', '流程名称', 1, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000004', 'dbjk_initiator', '发起人', '3', '10', '流程发起人', 1, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000005', 'dbjk_state', '状态', '4', '10', '任务状态', 1, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000006', 'dbjk_checkinoutuser', '签入签出用户', '5', '10', '签入签出用户', 1, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000007', 'dbjk_checkinoutstate', '签入签出状态', '6', '10', '签入签出状态', 1, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000008', 'dbjk_starttime', '发起时间', '7', '10', '发起时间', 1, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000009', 'dbjk_url', 'URL', '8', '10', 'URL', 1, 'Y');


insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000021', 'gzcx_instanceid', '流程标识', '0', '10', '流程标识', 2, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000022', 'gzcx_instancename', '流程名称', '1', '10', '流程名称', 2, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000023', 'gzcx_version', '流程版本', '2', '10', '流程版本号', 2, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000024', 'gzcx_initiator', '发起人', '3', '10', '流程发起人', 2, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000025', 'gzcx_state', '状态', '4', '10', '流程状态', 2, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000026', 'gzcx_starttime', '发起时间', '5', '10', '流程发起时间', 2, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000027', 'gzcx_attach', '附件', '6', '10', '流程附件', 2, 'Y');


insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000041', 'gzjk_instanceid', '流程标识', '0', '10', '流程标识', 3, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000042', 'gzjk_instancename', '流程名称', '1', '10', '流程名称', 3, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000043', 'gzjk_version', '流程版本', '2', '10', '流程版本号', 3, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000044', 'gzjk_initiator', '发起人', '3', '10', '流程发起人', 3, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000045', 'gzjk_state', '状态', '4', '10', '流程状态', 3, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000046', 'gzjk_starttime', '发起时间', '5', '10', '流程发起时间', 3, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000047', 'gzjk_currentstep', '当前步骤', '6', '10', '当前步骤', 3, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000048', 'gzjk_currentuser', '当前主办人', '7', '10', '当前主办人', 3, 'Y');
insert into sfwf_param_viewconfig (ID, FIELDID, FIELDNAME, ARRANGESN, FIELDLENGTH, DESCRIPTION, TYPE, ISVISIBLE)
values ('00000049', 'gzjk_currentprocess', '办理时间', '8', '10', '当前任务办理时间', 3, 'Y');
commit;

insert into SFWF_PROCESSRECEIPT_TYPE(ID, NAME) values('10', '流程结束时给发起人发回执');
insert into SFWF_PROCESSRECEIPT_TYPE(ID, NAME) values('20', '流程结束时给每个处理人发回执');

INSERT INTO SFWF_DM_TRIGGERTYPE (TYPE_DM, TYPE_MC,XYBZ,YXBZ)
VALUES ('10', '任务创建前', 'Y', 'Y');
INSERT INTO SFWF_DM_TRIGGERTYPE (TYPE_DM, TYPE_MC,XYBZ,YXBZ)
VALUES ('20', '任务创建后', 'Y', 'Y');
INSERT INTO SFWF_DM_TRIGGERTYPE (TYPE_DM, TYPE_MC,XYBZ,YXBZ)
VALUES ('30', '任务激活前', 'Y', 'Y');
INSERT INTO SFWF_DM_TRIGGERTYPE (TYPE_DM, TYPE_MC,XYBZ,YXBZ)
VALUES ('40', '任务激活后', 'Y', 'Y');
INSERT INTO SFWF_DM_TRIGGERTYPE (TYPE_DM, TYPE_MC,XYBZ,YXBZ)
VALUES ('70', '任务结束前', 'Y', 'Y');

insert into SFWF_SYSTEM_MSGSERVICE (ID, TRIGGERTYPE, CATEGORY, PROJECT, ISUSE, PATH, METHODNAME, PARAS, RETURNTYPE, URL, USR, PSW, OTHERPARAS, TYPE,MSGDEFINITION, DESCRIPTION)
values ('000400', 'TOKEN_DONE','SENDMSG','BPM','N','com.digitalchina.dcflexwork.extend.bpm.pojo.BPMManagerPojo', 'sendTokenDone', 
	'com.digitalchina.dcflexwork.extend.bpm.ISendPojo send', 'void', '', '', '', '', 'java', 'N','TOKEN强制结束消息');
insert into SFWF_SYSTEM_MSGSERVICE (ID, TRIGGERTYPE, CATEGORY, PROJECT, ISUSE, PATH, METHODNAME, PARAS, RETURNTYPE, URL, USR, PSW, OTHERPARAS, TYPE, MSGDEFINITION,DESCRIPTION)
values ('000401', 'TOKEN_CREATE','SENDMSG','BPM','N','com.digitalchina.dcflexwork.extend.bpm.pojo.BPMManagerPojo', 'sendTokenPojo', 
	'com.digitalchina.dcflexwork.extend.bpm.ISendPojo send', 'java.util.List', '', '', '', '', 'java','N', 'TOKEN创建消息');

insert into SFWF_SYSTEM_MSGSERVICE (ID, TRIGGERTYPE, CATEGORY, PROJECT, ISUSE, PATH, METHODNAME, PARAS, RETURNTYPE, URL, USR, PSW, OTHERPARAS, TYPE, MSGDEFINITION,DESCRIPTION)
values ('000200', 'TOKEN_ACTIVE','SENDMSG','SZFX','N','com.wf.bpm.driver.WfListener', 'onMessage', 
	'java.lang.String msg', 'void', '', '', '', '', 'java', 'N','TOKEN激活消息');
insert into SFWF_SYSTEM_MSGSERVICE (ID, TRIGGERTYPE, CATEGORY, PROJECT, ISUSE, PATH, METHODNAME, PARAS, RETURNTYPE, URL, USR, PSW, OTHERPARAS, TYPE, MSGDEFINITION,DESCRIPTION)
values ('000300', 'TOKEN_ACTIVE','SENDMSG','SZYW','N','com.wf.bpm.driver.WfListener', 'onMessage', 
	'java.lang.String msg', 'void', '', '', '', '', 'java', 'N','TOKEN激活消息');
insert into SFWF_SYSTEM_MSGSERVICE (ID, TRIGGERTYPE, CATEGORY, PROJECT, ISUSE, PATH, METHODNAME, PARAS, RETURNTYPE, URL, USR, PSW, OTHERPARAS, TYPE, MSGDEFINITION,DESCRIPTION)
values ('000200', 'TOKEN_ACTIVE','SENDMSG','LCKJ','N','com.digitalchina.bpm.lcgl.wskj.workflow.msg.MsgSend', 'sendMsg', 
	'java.lang.String msg', 'void', '', '', '', '', 'java','N', 'TOKEN激活消息');



insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10','COMMON', 'true', '','通用配置');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10100001','ADMINORG', '000000000000000', '10','根机构ID');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10100002','ADMIN', '00000000000', '10','超级用户ID');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10100003','ADMINPOST', '00000000000', '10','超级用户岗位ID');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10100004','IntService', 'com.digitalchina.dcflexwork.org.operate.WfIntegration', '10','数据集成类全路径');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10100005','IsNewAuth', 'true', '10','是否新权限');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10100006','AuthService', 'com.digitalchina.dcflexwork.org.operate.WfAuthSelf', '10','权限集成bean类全路径');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10100007','AuthServiceHome', '', '10','权限集成类home接口全路径');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10100008','AuthServiceJNDI', '', '10','权限集成类JNDI名称');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10100009','AuthServiceURL', '', '10','权限集成类服务地址"t3://192.168.1.1:9003"');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10100010','AuthServiceUser', '', '10','权限集成类服务用户名');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10100011','AuthServicePsw', '', '10','权限集成类服务密码');

insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10200001','isSort', 'false', '10','流程图是否排序');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10200002','VmlPointX', '50', '10','流程图排序的话起点位置X');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10200003','VmlPointY', '50', '10','流程图排序的话起点位置Y');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10200004','Width', '20', '10','流程图排序的话横向间隔距离');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10200005','Height', '15', '10','流程图排序的话纵向间隔距离');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10300001','ISBRANCH', 'true', '10','是否调用Branch服务');
insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('10300002','ExtService', 'com.digitalchina.dcits.comm.wf.RequestData', '10','是否调用扩展服务');

insert into SFWF_SYSTEM_CONFIG(ID,NAME,VALUE,PARENTID,DESCRIPTION)
values ('20','SERVER', 'true', '','服务端配置');
	

	
INSERT INTO SFWF_DM_SENDRULE(ID, NAME,DESCRIPTION,ISUSE,PARENTID,EXTEND)
VALUES ('0', '无辅助规则', '', 'Y', '', '');
INSERT INTO SFWF_DM_SENDRULE(ID, NAME,DESCRIPTION,ISUSE,PARENTID,EXTEND)
VALUES ('1', '与流程发起人同机构', '', 'Y','', '');
INSERT INTO SFWF_DM_SENDRULE(ID, NAME,DESCRIPTION,ISUSE,PARENTID,EXTEND)
VALUES ('2', '通过变量指定机构', '通过流程变量 _jgId 指定', 'Y','', '');
INSERT INTO SFWF_DM_SENDRULE(ID, NAME,DESCRIPTION,ISUSE,PARENTID,EXTEND)
VALUES ('3', '与本任务处理人同机构', '','Y', '', '');
INSERT INTO SFWF_DM_SENDRULE(ID, NAME,DESCRIPTION,ISUSE,PARENTID,EXTEND)
VALUES ('4', '与流程发起人相同', '', 'Y','', '');
INSERT INTO SFWF_DM_SENDRULE(ID, NAME,DESCRIPTION,ISUSE,PARENTID,EXTEND)
VALUES ('5', '本任务处理人的上级机构', '', 'Y','', '');
INSERT INTO SFWF_DM_SENDRULE (id,name,Description,isuse) values ('6','本任务处理人的下级机构','','Y');
INSERT INTO SFWF_DM_SENDRULE (id,name,Description,isuse) values ('7','本任务处理人同单位','','Y');
INSERT INTO SFWF_DM_SENDRULE (id,name,Description,isuse) values ('8','与流程发起人同单位','','Y');
INSERT INTO SFWF_DM_SENDRULE (id,name,Description,isuse) values ('9','本任务处理人的下级单位','','Y');
INSERT INTO SFWF_DM_SENDRULE (id,name,Description,isuse) values ('10','本任务处理人的上级单位','','Y');


INSERT INTO SFWF_SYSTEM_MSGSYSTEM(SYSTEM_ID, SYSTEM_NAME,ISUSE,ISDEFAULT,DESCRIPTION)
VALUES ('BPM', '流程管理平台V1.0', 'Y', 'N', '');
INSERT INTO SFWF_SYSTEM_MSGSYSTEM(SYSTEM_ID, SYSTEM_NAME,ISUSE,ISDEFAULT,DESCRIPTION)
VALUES ('SZFX', '深圳风险', 'Y', 'Y', '');
INSERT INTO SFWF_SYSTEM_MSGSYSTEM(SYSTEM_ID, SYSTEM_NAME,ISUSE,ISDEFAULT,DESCRIPTION)
VALUES ('SZYW', '深圳运维', 'Y', 'N', '');
INSERT INTO SFWF_SYSTEM_MSGSYSTEM(SYSTEM_ID, SYSTEM_NAME,ISUSE,ISDEFAULT,DESCRIPTION)
VALUES ('LCKJ', '流程框架', 'Y', 'N', '');


INSERT INTO SFWF_DM_MSGSOURCETYPE(SOURCE_ID, SOURCE_NAME,ISUSE,DESCRIPTION)
VALUES ('001', '任务实例', 'Y', '');
INSERT INTO SFWF_DM_MSGSOURCETYPE(SOURCE_ID, SOURCE_NAME,ISUSE,DESCRIPTION)
VALUES ('002', '流程实例变量', 'Y', '');
INSERT INTO SFWF_DM_MSGSOURCETYPE(SOURCE_ID, SOURCE_NAME,ISUSE,DESCRIPTION)
VALUES ('003', '任务实例变量', 'Y', '');
INSERT INTO SFWF_DM_MSGSOURCETYPE(SOURCE_ID, SOURCE_NAME,ISUSE,DESCRIPTION)
VALUES ('004', '流程过程变量', 'Y', '');

INSERT INTO SFWF_MESSAGE_DEFINITION(ID, TRIGGERTYPE,SYSTEMID,DEFINITION,FORMATTYPE,DESCRIPTION,EXTEND)
VALUES ('000100', '30', 'SZFX', '{	    "001":{		"TOKEN_TYPE": "MANUAL_ACTIVITY",                "TOKENID": "2015061500049510",                "NODEID": "2015061200001061",		"NODENAME": "签收登记",		"TOKENURL": "",		"ASSIGNEE": ",2,",		"TIMELIMITATION": "1.0D",		"INSTANCEID": "2015061500022905",		"CHECKINOUT": "7",		"ISHQEND": "false",		"CHECKINOUT": "7"            },	    "002":{		"SENDMSG_SYS": "_sendMsgSys",                "LC_DM": "_swwsDm",                "LC_ID": "LC_ID",		"ISTIMERSTART":"_isTimerStart",		"TOPIC":"_topic"            },	    "003":{		"HQTGL": "_taskHqtgl",		"HQRS": "_taskHqrs",		"ISALLASSIGN": "_taskIsAllSign",                "YWZJ_DM": "_taskYwzj", 		"BHYW_ID": "_taskBHYWID",		"LCZS": "_taskLczs",		"YWMK": "_taskYwmk",		"RECEIVER": "_taskReceiver",		"PREWIID": "_taskPreTokenId",		"PRENODEID": "_taskPreNodeId",		"PREWINAME": "_taskPreNodeId",		"PREWIUSERID": "_taskPreTokenUser",		"PRETOKENID": "_taskPreTokenId",		"USERLIST": "_taskUserList"            },	    "004":{		"TOKEN_TRANS": "ALL",		"INSTANCE_TRANS": "ALL"            }}', 'XML', '', '');
INSERT INTO SFWF_MESSAGE_DEFINITION(ID, TRIGGERTYPE,SYSTEMID,DEFINITION,FORMATTYPE,DESCRIPTION,EXTEND)
VALUES ('000200', '30', 'LCKJ', '{	    "001":{		"TOKEN_TYPE": "MANUAL_ACTIVITY",                "TOKENID": "2015061500049510",                "NODEID": "2015061200001061",		"NODENAME": "签收登记",		"TOKENURL": "",		"ASSIGNEE": ",2,",		"TIMELIMITATION": "1.0D",		"INSTANCEID": "2015061500022905",		"CHECKINOUT": "7",		"ISHQEND": "false",		"CHECKINOUT": "7"            },	    "002":{		"SENDMSG_SYS": "_sendMsgSys",                "LC_DM": "_swwsDm",                "LC_ID": "LC_ID",		"ISTIMERSTART":"_isTimerStart",		"TOPIC":"_topic"            },	    "003":{		"HQTGL": "_taskHqtgl",		"HQRS": "_taskHqrs",		"ISALLASSIGN": "_taskIsAllSign",                "YWZJ_DM": "_taskYwzj", 		"BHYW_ID": "_taskBHYWID",		"LCZS": "_taskLczs",		"YWMK": "_taskYwmk",		"RECEIVER": "_taskReceiver",		"PREWIID": "_taskPreTokenId",		"PRENODEID": "_taskPreNodeId",		"PREWINAME": "_taskPreNodeId",		"PREWIUSERID": "_taskPreTokenUser",		"PRETOKENID": "_taskPreTokenId",		"USERLIST": "_taskUserList"            },	    "004":{		"TOKEN_TRANS": "ALL",		"INSTANCE_TRANS": "ALL"            }}', 'JSON', '', '');


