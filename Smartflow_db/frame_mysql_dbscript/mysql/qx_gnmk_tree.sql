-- Create table
create table QX_GNMK_TREE
(
  JD_DM    VARCHAR(21) not null comment'����ģ�����',
  FJD_DM   VARCHAR(21) not null comment'�ڵ����ʹ���',
  JD_MC    VARCHAR(80) not null comment'�ڵ�����',
  GNMK_DM  VARCHAR(256) comment'�ڵ����',
  JDLX_DM  VARCHAR(2) comment'���ڵ����',
  JD_ORDER INT not null comment'�ڵ�˳��',
  JD_BM    VARCHAR(80) comment'�ڵ����'
);

-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_GNMK_TREE
  add constraint PK_QX_GNMK_TREE primary key (JD_DM);
alter table QX_GNMK_TREE
  add constraint FK_QX_GNMK_TREE_GNMK_DM foreign key (GNMK_DM)
  references QX_GNMK (GNMK_DM);

insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('0', '0', '��Դ��', 'FFFFFFFFFFF', '0', 0, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage', '0', 'ϵͳ����', 'FFFFFFFFFFF', '01', 90, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000593258327193043', 'systemmanage', '����ͳ��', 'FFFFFFFFFFF', null, 8, '����ͳ��');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000605682849438546', '100000593258327193043', '�ڲ�����', 'monitor.innerService', '02', 0, '�ڲ�����');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000612864843660207', '100000593258327193043', '�ⲿAction����', 'monitor.actionService', '02', 1, '�ⲿAction����');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000628659833504745', '100000593258327193043', '������ù�ϵͳ��', 'monitor.serviceCall', '02', 2, '������ù�ϵͳ��');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage001', 'systemmanage', '������', 'FFFFFFFFFFF', '01', 1, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage001001', 'systemmanage001', '�����鶨��', 'systemmanage.batch.jobgroup', '02', 1, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage001002', 'systemmanage001', 'ʱ�����', 'systemmanage.batch.timer', null, 3, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage001003', 'systemmanage001', '���ȼ��', 'systemmanage.batch.console', null, 4, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage002', 'systemmanage', 'ҵ�񵼺�', 'FFFFFFFFFFF', null, 2, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage002001', 'systemmanage002', 'ҵ�񵼺�����', 'systemmanage.bussinessnav', '02', 1, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage003', 'systemmanage', 'ϵͳ��ʼ��', 'FFFFFFFFFFF', '01', 0, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage003001', 'systemmanage003', '������Ϣά��', 'message.common', null, 4, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage003003', 'systemmanage003', '��ӡģ�����', 'systemmanage.int.print', null, 3, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage003004', 'systemmanage003', 'ϵͳ������ʼ��', 'systemmanage.init.xtcs', null, 2, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage004', 'systemmanage', '������Ϣ����', 'FFFFFFFFFFF', null, 4, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage004001', 'systemmanage004', '������Ϣά��', 'systemmanage.fzxx', '02', 1, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage006', 'systemmanage', '��֯Ȩ��', 'FFFFFFFFFFF', null, 6, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('AUTH_ORG_MGT', 'systemmanage006', '��������', 'AUTH_ORG_MGT', '02', 3, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('NODE_MODEL_MGT', 'systemmanage006', '��Դ����', 'AUTH_MODEL_MGT', '02', 5, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('NODE_OPERATOR_MGT', 'systemmanage006', '������Ա����', 'AUTH_OPERATOR_MGT', '02', 4, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('NODE_POSITION_MGT', 'systemmanage006', '��λ����', 'AUTH_POSITION_MGT', null, 7, '��λ����');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('NODE_ROLE_MGT', 'systemmanage006', '��ɫ����', 'AUTH_ROLE_MGT', null, 6, '��ɫ����');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('NODE_USER_MGT', 'systemmanage006', '�û�����', 'AUTH_USER_MGT', '02', 8, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage006001', 'systemmanage006', '��Դ���߰���', 'systemmanage.security.resource.help', '02', 1, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage006002', 'systemmanage006', 'ҵ��ϵͳע��', 'systemmanage.security.system', '02', 2, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage007', 'systemmanage', '��ع���', 'FFFFFFFFFFF', null, 7, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000491189998073699', 'systemmanage007', '���ܼ��', 'manager.performance', '02', 5, '���ܼ��');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000504360607575889', 'systemmanage007', 'ҵ��������', 'monitor.businesslog', null, 6, 'ҵ��������');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000528149200777372', 'systemmanage007', '�û��������', 'monitor.user.operation', '02', 8, '�û��������');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000577330903720606', 'systemmanage007', '�������', 'monitor.switch', '02', 9, '�������');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000581499259200724', 'systemmanage007', '������', 'monitor.cache', '02', 10, '������');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000645637812010315', 'systemmanage007', '�쳣���', 'monitor_exception', null, 11, '�쳣���');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000879559155160338', 'systemmanage007', 'ҵ�����ͳ��', 'monitor.businesstj', '02', 12, 'ҵ�����ͳ��');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage007002', 'systemmanage007', '�����û�', 'systemmanage.monitoring.onlineusers', null, 2, '�����û�');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage007003', 'systemmanage007', '�����û���ʷ', 'systemmanage.monitoring.userlogs', null, 3, '�����û���ʷ');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage008', 'systemmanage', '�����Ѱ�', 'FFFFFFFFFFF', null, 9, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage008001', 'systemmanage008', '������������', 'systemmanage.todo.setting', null, 1, '������������');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage008002', 'systemmanage008', '�����Ѱ�����', 'systemmanage.todo.dashboard', null, 2, '��������');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009', 'systemmanage', '������', 'FFFFFFFFFFF', null, 10, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009001', 'systemmanage009', '����ϵͳ', 'help', '02', 1, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009002', 'systemmanage009', '�޸�����', 'changepwd', '02', 2, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009003', 'systemmanage009', '��������', 'setting', '02', 3, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009004', 'systemmanage009', '�˳�ϵͳ', 'exist', '02', 4, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009005', 'systemmanage009', '���µ�¼', 'relogin', '02', 5, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009006', 'systemmanage009', '������Ļ', 'lockscreen', '02', 6, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009007', 'systemmanage009', '��¼��Ϣ��ʾ', 'msg_login', '02', 7, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009008', 'systemmanage009', '������Ϣ', 'msg_ggxx', '02', 8, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009009', 'systemmanage009', '��������Ϣ', 'msg_text', '02', 9, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009010', 'systemmanage009', '��Ȩ����', 'Copyright', '02', 10, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('systemmanage009011', 'systemmanage009', '������Ϣ', 'messenger', '02', 11, null);
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000003847408994605', 'systemmanage006', '���������', 'AUTH_WORKINGGROUP_MGT', '02', 9, '���������');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100001008208728920581', 'systemmanage006', '���ݹ���', 'AUTH_DATA_MGT', null, 11, '���ݹ���');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000802570784421741', 'systemmanage006', '���ӹ���ϵͳ�û�����', 'AUTH_USER_MGT_ELEC', 02, 13, '���ӹ���ϵͳ�û�����');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000631392061127516', 'systemmanage006', '���ӹ���ϵͳ��λ����', 'AUTH_POSITION_MGT_ELECTRONIC', null, 12, '���ӹ���ϵͳ��λ����');
insert into QX_GNMK_TREE (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100000817293090209338', 'systemmanage006', '���ӹ���ϵͳ�û���Ȩ����', 'AUTH_USER_MGT_AUTH', null, 14, '���ӹ���ϵͳ�û���Ȩ');
commit;