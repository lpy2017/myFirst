-- Create table
create table QX_GNMK
(
  GNMK_DM    VARCHAR(256) not null comment'功能模块代码',
  GNMK_HZMC  VARCHAR(80) default '功能模块' not null comment'功能模块汉字名称',
  GNMK_LJMC  VARCHAR(4000) not null comment'功能模块路径名称',
  MKLX_DM    VARCHAR(2) default '00' not null comment'模块类型代码',
  YWHJ_DM    VARCHAR(6) not null comment'业务环节代码',
  CYBJ       CHAR(1) comment'常用标记',
  GZL_BZ     CHAR(1),
  CFDK       CHAR(1) default 'Y' not null comment'是否允许重复打开',
  DKWZ       CHAR(1) default '0' not null comment'打开位置',
  SHOWLEFT   CHAR(1) default 'Y' not null comment'显示Left导航区',
  SHOWTOP    CHAR(1) default 'Y' not null comment'显示Top导航区',
  SHOWINTREE CHAR(1) default 'Y' not null comment'显示在资源树上',
  SYSTEMNAME VARCHAR(80) default '系统管理' not null comment'功能模块别名',
  YXBZ       CHAR(1) default 'Y' not null comment'有效标记',
  GNMK_BM    VARCHAR(80)
);

-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_GNMK
  add constraint PK_QX_GNMK primary key (GNMK_DM);
alter table QX_GNMK
  add constraint FK_QX_GNMK_SYSTEMNAME foreign key (SYSTEMNAME)
  references QX_SYSTEM (SYSTEMNAME);
alter table QX_GNMK
  add constraint FK_QX_GNMK_YWHJ_DM foreign key (YWHJ_DM)
  references DM_YWHJ (YWHJ_DM);
 
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('FFFFFFFFFFF', '文件夹', 'FFFFFF', '00', '090100', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.innerService', '内部服务', '../work/services/innerServiceStatistics.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '内部服务');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.actionService', '外部Action服务', '../work/services/propertiesServiceStatistics.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '外部Action服务');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.serviceCall', '服务调用关系统计', '../work/services/serviceRelation.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '服务调用关系统计');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.batch.jobgroup', '任务组定义', '../work/pi/rwzdy/RwzdyBndService.searchTaskGroup.do', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.batch.timer', '时间管理', '../work/pi/sjgl/TimerdyBndService.initTimer.do', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.batch.console', '调度监控', '../work/pi/ddjk/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.bussinessnav', '业务导航配置', '../pageflow/index/index.jsp', '05', '000000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('message.common', '公共消息维护', '../work/message/common/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.int.print', '打印模板管理', '../print/listPrintTemplate.iface', '05', '000000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.init.xtcs', '系统参数初始化', '../work/xtcsh/csh_xtcs/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.fzxx', '辅助信息维护', '../fzxx/dcone/xxgl/index.jsp', '05', '000000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_ORG_MGT', '机构管理', '../security/org/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_MODEL_MGT', '资源管理', '../security/modeltree/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_OPERATOR_MGT', '操作人员管理', '../security/operator/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_POSITION_MGT', '岗位管理', '../security/position/index.jsp', '05', '090000', null, null, 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '岗位管理');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_ROLE_MGT', '角色管理', '../security/role/index.jsp', '05', '090000', null, null, 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '角色管理');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_USER_MGT', '用户管理', '../security/user/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.security.resource.help', '资源在线帮助', '../portal/help/GnmkBndService.select.do', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.security.system', '业务系统注册', '../portal/system/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('manager.performance', '性能监控', '../work/performance/index.jsp', '05', '000000', 'N', 'Y', 'Y', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '性能监控');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.businesslog', '业务操作监控', '../work/businessTjLog/index.jsp', '05', '000000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '业务操作监控');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.user.operation', '用户操作监控', '../work/userCzLog/index.jsp', '05', '000000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '用户操作监控');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.switch', '组件开关', '../work/switch/switch.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '组件开关');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.cache', '缓存监控', '../work/cachemonitor/cachemonitor.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '缓存监控');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor_exception', '异常监控', '../work/userCzExceptionLog/index.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '异常监控');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.businesstj', '业务操作统计', '../work/businessTjLog/businessTj.jsp', '05', '000000', 'N', 'Y', 'Y', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '业务操作统计');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.monitoring.onlineusers', '在线用户', '../work/usermonitor/onlineUser.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '在线用户');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.monitoring.userlogs', '在线用户历史', '../work/usermonitor/onlineUserHistory.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '在线用户历史');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.todo.setting', '待办事宜设置', '../task/messageDisplay/index.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '待办事宜设置');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.todo.dashboard', '待办已办事宜', '../task/message/index.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '待办事宜');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('help', '帮助系统', 'help', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('changepwd', '修改密码', 'changepwd', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('setting', '个性设置', 'setting', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('exist', '退出系统', 'exist', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('relogin', '重新登录', 'relogin', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('lockscreen', '锁定屏幕', 'lockscreen', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('msg_login', '登录消息提示', 'msg_login', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('msg_ggxx', '公共消息', 'msg_ggxx', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('msg_text', '工作区消息', 'msg_text', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('Copyright', '版权所有', 'Copyright', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', '系统管理', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('messenger', '监听消息', 'messenger', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', '系统管理', 'Y', null); 
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_WORKINGGROUP_MGT', '工作组管理', '../security/workinggroup/index.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '工作组管理');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_DATA_MGT', '数据管理', '../work/datamanager/index.jsp', '05', '040000', 'N', 'Y', 'Y', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '数据管理');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_USER_MGT_ELEC', '电子公文系统用户管理', '../security/user_systemManager/index.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '电子公文系统用户管理');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_POSITION_MGT_ELECTRONIC', '电子公文系统岗位管理', '../security/position_securityManager/index.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '电子公文系统岗位管理');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_USER_MGT_AUTH', '电子公文系统用户授权管理', '../security/userauthorization/index.jsp', '05', '000000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', '系统管理', 'Y', '电子公文系统用户授权');
commit;