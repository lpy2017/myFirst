-- Create table
create table QX_GNMK
(
  GNMK_DM    VARCHAR(256) not null comment'����ģ�����',
  GNMK_HZMC  VARCHAR(80) default '����ģ��' not null comment'����ģ�麺������',
  GNMK_LJMC  VARCHAR(4000) not null comment'����ģ��·������',
  MKLX_DM    VARCHAR(2) default '00' not null comment'ģ�����ʹ���',
  YWHJ_DM    VARCHAR(6) not null comment'ҵ�񻷽ڴ���',
  CYBJ       CHAR(1) comment'���ñ��',
  GZL_BZ     CHAR(1),
  CFDK       CHAR(1) default 'Y' not null comment'�Ƿ������ظ���',
  DKWZ       CHAR(1) default '0' not null comment'��λ��',
  SHOWLEFT   CHAR(1) default 'Y' not null comment'��ʾLeft������',
  SHOWTOP    CHAR(1) default 'Y' not null comment'��ʾTop������',
  SHOWINTREE CHAR(1) default 'Y' not null comment'��ʾ����Դ����',
  SYSTEMNAME VARCHAR(80) default 'ϵͳ����' not null comment'����ģ�����',
  YXBZ       CHAR(1) default 'Y' not null comment'��Ч���',
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
values ('FFFFFFFFFFF', '�ļ���', 'FFFFFF', '00', '090100', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.innerService', '�ڲ�����', '../work/services/innerServiceStatistics.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '�ڲ�����');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.actionService', '�ⲿAction����', '../work/services/propertiesServiceStatistics.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '�ⲿAction����');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.serviceCall', '������ù�ϵͳ��', '../work/services/serviceRelation.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '������ù�ϵͳ��');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.batch.jobgroup', '�����鶨��', '../work/pi/rwzdy/RwzdyBndService.searchTaskGroup.do', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.batch.timer', 'ʱ�����', '../work/pi/sjgl/TimerdyBndService.initTimer.do', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.batch.console', '���ȼ��', '../work/pi/ddjk/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.bussinessnav', 'ҵ�񵼺�����', '../pageflow/index/index.jsp', '05', '000000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('message.common', '������Ϣά��', '../work/message/common/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.int.print', '��ӡģ�����', '../print/listPrintTemplate.iface', '05', '000000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.init.xtcs', 'ϵͳ������ʼ��', '../work/xtcsh/csh_xtcs/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.fzxx', '������Ϣά��', '../fzxx/dcone/xxgl/index.jsp', '05', '000000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_ORG_MGT', '��������', '../security/org/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_MODEL_MGT', '��Դ����', '../security/modeltree/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_OPERATOR_MGT', '������Ա����', '../security/operator/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_POSITION_MGT', '��λ����', '../security/position/index.jsp', '05', '090000', null, null, 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '��λ����');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_ROLE_MGT', '��ɫ����', '../security/role/index.jsp', '05', '090000', null, null, 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '��ɫ����');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_USER_MGT', '�û�����', '../security/user/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.security.resource.help', '��Դ���߰���', '../portal/help/GnmkBndService.select.do', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.security.system', 'ҵ��ϵͳע��', '../portal/system/index.jsp', '05', '090000', 'N', 'Y', '0', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('manager.performance', '���ܼ��', '../work/performance/index.jsp', '05', '000000', 'N', 'Y', 'Y', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '���ܼ��');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.businesslog', 'ҵ��������', '../work/businessTjLog/index.jsp', '05', '000000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', 'ҵ��������');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.user.operation', '�û��������', '../work/userCzLog/index.jsp', '05', '000000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '�û��������');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.switch', '�������', '../work/switch/switch.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '�������');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.cache', '������', '../work/cachemonitor/cachemonitor.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '������');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor_exception', '�쳣���', '../work/userCzExceptionLog/index.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '�쳣���');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('monitor.businesstj', 'ҵ�����ͳ��', '../work/businessTjLog/businessTj.jsp', '05', '000000', 'N', 'Y', 'Y', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', 'ҵ�����ͳ��');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.monitoring.onlineusers', '�����û�', '../work/usermonitor/onlineUser.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '�����û�');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.monitoring.userlogs', '�����û���ʷ', '../work/usermonitor/onlineUserHistory.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '�����û���ʷ');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.todo.setting', '������������', '../task/messageDisplay/index.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '������������');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('systemmanage.todo.dashboard', '�����Ѱ�����', '../task/message/index.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '��������');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('help', '����ϵͳ', 'help', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('changepwd', '�޸�����', 'changepwd', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('setting', '��������', 'setting', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('exist', '�˳�ϵͳ', 'exist', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('relogin', '���µ�¼', 'relogin', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('lockscreen', '������Ļ', 'lockscreen', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('msg_login', '��¼��Ϣ��ʾ', 'msg_login', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('msg_ggxx', '������Ϣ', 'msg_ggxx', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('msg_text', '��������Ϣ', 'msg_text', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('Copyright', '��Ȩ����', 'Copyright', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', 'ϵͳ����', 'Y', null);
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('messenger', '������Ϣ', 'messenger', '07', '090000', 'N', 'Y', '0', '0', 'Y', 'N', 'Y', 'ϵͳ����', 'Y', null); 
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_WORKINGGROUP_MGT', '���������', '../security/workinggroup/index.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '���������');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_DATA_MGT', '���ݹ���', '../work/datamanager/index.jsp', '05', '040000', 'N', 'Y', 'Y', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '���ݹ���');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_USER_MGT_ELEC', '���ӹ���ϵͳ�û�����', '../security/user_systemManager/index.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '���ӹ���ϵͳ�û�����');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_POSITION_MGT_ELECTRONIC', '���ӹ���ϵͳ��λ����', '../security/position_securityManager/index.jsp', '05', '090000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '���ӹ���ϵͳ��λ����');
insert into QX_GNMK (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('AUTH_USER_MGT_AUTH', '���ӹ���ϵͳ�û���Ȩ����', '../security/userauthorization/index.jsp', '05', '000000', 'N', 'Y', 'N', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '���ӹ���ϵͳ�û���Ȩ');
commit;