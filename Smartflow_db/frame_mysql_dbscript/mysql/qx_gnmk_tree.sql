-- Create table
create table QX_GNMK_TREE
(
  JD_DM    VARCHAR(21) not null comment'功能模块代码',
  FJD_DM   VARCHAR(21) not null comment'节点类型代码',
  JD_MC    VARCHAR(80) not null comment'节点名称',
  GNMK_DM  VARCHAR(256) comment'节点代码',
  JDLX_DM  VARCHAR(2) comment'父节点代码',
  JD_ORDER INT not null comment'节点顺序',
  JD_BM    VARCHAR(80) comment'节点别名'
);

-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_GNMK_TREE
  add constraint PK_QX_GNMK_TREE primary key (JD_DM);
alter table QX_GNMK_TREE
  add constraint FK_QX_GNMK_TREE_GNMK_DM foreign key (GNMK_DM)
  references QX_GNMK (GNMK_DM);

insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('0', '0', '资源树', 'FFFFFFFFFFF', '0', 0, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage', '0', '系统管理', 'FFFFFFFFFFF', '01', 90, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000593258327193043', 'systemmanage', '服务统计', 'FFFFFFFFFFF', null, 8, '服务统计');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000605682849438546', '100000593258327193043', '内部服务', 'monitor.innerService', '02', 0, '内部服务');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000612864843660207', '100000593258327193043', '外部Action服务', 'monitor.actionService', '02', 1, '外部Action服务');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000628659833504745', '100000593258327193043', '服务调用关系统计', 'monitor.serviceCall', '02', 2, '服务调用关系统计');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage001', 'systemmanage', '批处理', 'FFFFFFFFFFF', '01', 1, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage001001', 'systemmanage001', '任务组定义', 'systemmanage.batch.jobgroup', '02', 1, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage001002', 'systemmanage001', '时间管理', 'systemmanage.batch.timer', null, 3, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage001003', 'systemmanage001', '调度监控', 'systemmanage.batch.console', null, 4, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage002', 'systemmanage', '业务导航', 'FFFFFFFFFFF', null, 2, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage002001', 'systemmanage002', '业务导航配置', 'systemmanage.bussinessnav', '02', 1, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage003', 'systemmanage', '系统初始化', 'FFFFFFFFFFF', '01', 0, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage003001', 'systemmanage003', '公共消息维护', 'message.common', null, 4, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage003003', 'systemmanage003', '打印模板管理', 'systemmanage.int.print', null, 3, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage003004', 'systemmanage003', '系统参数初始化', 'systemmanage.init.xtcs', null, 2, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage004', 'systemmanage', '辅助信息管理', 'FFFFFFFFFFF', null, 4, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage004001', 'systemmanage004', '辅助信息维护', 'systemmanage.fzxx', '02', 1, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage006', 'systemmanage', '组织权限', 'FFFFFFFFFFF', null, 6, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('AUTH_ORG_MGT', 'systemmanage006', '机构管理', 'AUTH_ORG_MGT', '02', 3, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('NODE_MODEL_MGT', 'systemmanage006', '资源管理', 'AUTH_MODEL_MGT', '02', 5, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('NODE_OPERATOR_MGT', 'systemmanage006', '操作人员管理', 'AUTH_OPERATOR_MGT', '02', 4, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('NODE_POSITION_MGT', 'systemmanage006', '岗位管理', 'AUTH_POSITION_MGT', null, 7, '岗位管理');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('NODE_ROLE_MGT', 'systemmanage006', '角色管理', 'AUTH_ROLE_MGT', null, 6, '角色管理');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('NODE_USER_MGT', 'systemmanage006', '用户管理', 'AUTH_USER_MGT', '02', 8, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage006001', 'systemmanage006', '资源在线帮助', 'systemmanage.security.resource.help', '02', 1, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage006002', 'systemmanage006', '业务系统注册', 'systemmanage.security.system', '02', 2, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage007', 'systemmanage', '监控管理', 'FFFFFFFFFFF', null, 7, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000491189998073699', 'systemmanage007', '性能监控', 'manager.performance', '02', 5, '性能监控');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000504360607575889', 'systemmanage007', '业务操作监控', 'monitor.businesslog', null, 6, '业务操作监控');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000528149200777372', 'systemmanage007', '用户操作监控', 'monitor.user.operation', '02', 8, '用户操作监控');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000577330903720606', 'systemmanage007', '组件开关', 'monitor.switch', '02', 9, '组件开关');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000581499259200724', 'systemmanage007', '缓存监控', 'monitor.cache', '02', 10, '缓存监控');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000645637812010315', 'systemmanage007', '异常监控', 'monitor_exception', null, 11, '异常监控');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000879559155160338', 'systemmanage007', '业务操作统计', 'monitor.businesstj', '02', 12, '业务操作统计');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage007002', 'systemmanage007', '在线用户', 'systemmanage.monitoring.onlineusers', null, 2, '在线用户');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage007003', 'systemmanage007', '在线用户历史', 'systemmanage.monitoring.userlogs', null, 3, '在线用户历史');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage008', 'systemmanage', '待办已办', 'FFFFFFFFFFF', null, 9, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage008001', 'systemmanage008', '待办事宜设置', 'systemmanage.todo.setting', null, 1, '待办事宜设置');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage008002', 'systemmanage008', '待办已办事宜', 'systemmanage.todo.dashboard', null, 2, '待办事宜');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009', 'systemmanage', '工具项', 'FFFFFFFFFFF', null, 10, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009001', 'systemmanage009', '帮助系统', 'help', '02', 1, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009002', 'systemmanage009', '修改密码', 'changepwd', '02', 2, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009003', 'systemmanage009', '个性设置', 'setting', '02', 3, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009004', 'systemmanage009', '退出系统', 'exist', '02', 4, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009005', 'systemmanage009', '重新登录', 'relogin', '02', 5, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009006', 'systemmanage009', '锁定屏幕', 'lockscreen', '02', 6, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009007', 'systemmanage009', '登录消息提示', 'msg_login', '02', 7, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009008', 'systemmanage009', '公共消息', 'msg_ggxx', '02', 8, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009009', 'systemmanage009', '工作区消息', 'msg_text', '02', 9, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009010', 'systemmanage009', '版权所有', 'Copyright', '02', 10, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009011', 'systemmanage009', '监听消息', 'messenger', '02', 11, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000003847408994605', 'systemmanage006', '工作组管理', 'AUTH_WORKINGGROUP_MGT', '02', 9, '工作组管理');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100001008208728920581', 'systemmanage006', '数据管理', 'AUTH_DATA_MGT', null, 11, '数据管理');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000802570784421741', 'systemmanage006', '电子公文系统用户管理', 'AUTH_USER_MGT_ELEC', 02, 13, '电子公文系统用户管理');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000631392061127516', 'systemmanage006', '电子公文系统岗位管理', 'AUTH_POSITION_MGT_ELECTRONIC', null, 12, '电子公文系统岗位管理');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000817293090209338', 'systemmanage006', '电子公文系统用户授权管理', 'AUTH_USER_MGT_AUTH', null, 14, '电子公文系统用户授权');
commit;