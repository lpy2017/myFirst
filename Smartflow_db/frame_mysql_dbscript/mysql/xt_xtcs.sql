-- 创建表: 系统参数
create table XT_XTCS
(
  CSXH   VARCHAR(5) not null comment '参数序号',
  JG_DM  VARCHAR(15) not null comment '机构代码',
  CSMC   VARCHAR(80) not null comment '参数名称',
  CSNR   VARCHAR(500) not null comment '参数内容',
  SYSM   VARCHAR(200) comment '使用说明',
  XYBZ   CHAR(1) not null comment '选用标志',
  JZSZBZ CHAR(1) comment '集中设置标志'
) comment '系统参数'
;

alter table XT_XTCS
  add constraint PK_XT_XTCS primary key (CSXH, JG_DM);


insert into XT_XTCS (CSXH, JG_DM, CSMC, CSNR, SYSM, XYBZ, JZSZBZ)
	values ('10000', 'PUBLIC', '系统名称', 'Sm@rtFrame5.0 集成工作平台', '设置系统名称', 'Y', 'Y');
insert into XT_XTCS (CSXH, JG_DM, CSMC, CSNR, SYSM, XYBZ, JZSZBZ)
  values ('10001', 'PUBLIC', '序号生成器', '9', '9', 'Y', 'Y');
insert into XT_XTCS (CSXH, JG_DM, CSMC, CSNR, SYSM, XYBZ, JZSZBZ)
	values ('10002', 'PUBLIC', '密码规则正则表达式', '\d+', '用于校验用户修改密码时新密码组成规则("\d+"为全数字,禁止删除,设置值为0时为不限制)', 'Y', 'Y');
insert into XT_XTCS (CSXH, JG_DM, CSMC, CSNR, SYSM, XYBZ, JZSZBZ)
	values ('10003', 'PUBLIC', '允许最大密码输入错误次数', '5', '在一段时间内用户可尝试的最大密码输入错误次数 (禁止删除,设置值为0时为不限制)', 'Y', 'Y');
insert into XT_XTCS (CSXH, JG_DM, CSMC, CSNR, SYSM, XYBZ, JZSZBZ)
	values ('10004', 'PUBLIC', '密码输入错误连续重试限制时间', '30', '密码输入错误连续重试的限制时间,单位:分钟 (禁止删除,设置值为0时为不限制)', 'Y', 'Y');
insert into XT_XTCS (CSXH, JG_DM, CSMC, CSNR, SYSM, XYBZ, JZSZBZ)
	values ('10005', 'PUBLIC', '达到最大密码输入错误次数后处理方式', '1', '1:指定时间后允许重试;2:锁定用户 (禁止删除,设置值为0时为不处理)', 'Y', 'Y');
insert into XT_XTCS (CSXH, JG_DM, CSMC, CSNR, SYSM, XYBZ, JZSZBZ)
	values ('10006', 'PUBLIC', '限制用户重试时间', '30', '用户达到最大密码输入错误次数后限制登录的时间段,单位:分钟 (禁止删除,设置值为0时为不限制)', 'Y', 'Y');

commit;