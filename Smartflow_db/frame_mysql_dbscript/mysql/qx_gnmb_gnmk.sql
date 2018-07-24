-- Create table
create table QX_GNMB_GNMK
(
  GNMB_DM  VARCHAR(11) not null comment'����ģ�����',
  GNMK_DM  VARCHAR(256) not null comment'����ģ�����',
  JD_DM    VARCHAR(21) not null comment'�ڵ����',
  FJD_DM   VARCHAR(21) not null comment'���ڵ����',
  JD_MC    VARCHAR(80) not null comment'�ڵ�����',
  JD_ORDER INT default 0 not null comment'�ڵ�˳��'
);

-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_GNMB_GNMK
  add constraint PK_QX_GNMB_GNMK primary key (GNMB_DM, GNMK_DM, JD_DM, FJD_DM);
alter table QX_GNMB_GNMK
  add constraint FK_QX_GNMB_GNMK_GNMB_DM foreign key (GNMB_DM)
  references QX_GNMB (GNMB_DM);
alter table QX_GNMB_GNMK
  add constraint FK_QX_GNMB_GNMK_GNMK_DM foreign key (GNMK_DM)
  references QX_GNMK (GNMK_DM);
  
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'FFFFFFFFFFF', 'systemmanage007', 'systemmanage', '��ع���', 7);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'FFFFFFFFFFF', 'systemmanage008', 'systemmanage', '�����Ѱ�', 9);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'systemmanage.todo.setting', 'systemmanage008001', 'systemmanage008', '������������', 1);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'systemmanage.todo.dashboard', 'systemmanage008002', 'systemmanage008', '�����Ѱ�����', 2);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'systemmanage.monitoring.onlineusers', 'systemmanage007002', 'systemmanage007', '�����û�', 2);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'systemmanage.monitoring.userlogs', 'systemmanage007003', 'systemmanage007', '�����û���ʷ', 3);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'systemmanage.security.system', 'systemmanage006002', 'systemmanage006', 'ҵ��ϵͳע��', 2);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'AUTH_ORG_MGT', 'AUTH_ORG_MGT', 'systemmanage006', '��������', 3);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'AUTH_OPERATOR_MGT', 'NODE_OPERATOR_MGT', 'systemmanage006', '������Ա����', 4);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'AUTH_MODEL_MGT', 'NODE_MODEL_MGT', 'systemmanage006', '��Դ����', 5);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'AUTH_ROLE_MGT', 'NODE_ROLE_MGT', 'systemmanage006', '��ɫ����', 6);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'AUTH_POSITION_MGT', 'NODE_POSITION_MGT', 'systemmanage006', '��λ����', 7);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'AUTH_USER_MGT', 'NODE_USER_MGT', 'systemmanage006', '�û�����', 8);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'manager.performance', '100000491189998073699', 'systemmanage007', '���ܼ��', 5);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'monitor.businesslog', '100000504360607575889', 'systemmanage007', 'ҵ��������', 6);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'monitor.businesstj', '100000879559155160338', 'systemmanage007', 'ҵ�����ͳ��', 12);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'monitor.user.operation', '100000528149200777372', 'systemmanage007', '�û��������', 8);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'monitor.switch', '100000577330903720606', 'systemmanage007', '�������', 9);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'FFFFFFFFFFF', '100000593258327193043', 'systemmanage', '����ͳ��', 8);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'monitor.cache', '100000581499259200724', 'systemmanage007', '������', 10);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'monitor.innerService', '100000605682849438546', '100000593258327193043', '�ڲ�����', 0);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'monitor.actionService', '100000612864843660207', '100000593258327193043', '�ⲿAction����', 1);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'monitor.serviceCall', '100000628659833504745', '100000593258327193043', '������ù�ϵͳ��', 2);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'monitor_exception', '100000645637812010315', 'systemmanage007', '�쳣���', 11);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'FFFFFFFFFFF', '100001429456985677820', 'systemmanage', '����', 12); 
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'AUTH_WORKINGGROUP_MGT', '100000003847408994605', 'systemmanage006', '���������', 9);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'message.common', 'systemmanage003001', 'systemmanage003', '������Ϣά��', 4);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'systemmanage.init.xtcs', 'systemmanage003004', 'systemmanage003', 'ϵͳ������ʼ��', 2); 
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'AUTH_DATA_MGT', '100001008208728920581', 'systemmanage006', '���ݹ���', 11);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000012', 'AUTH_USER_MGT_ELEC', '100000802570784421741', 'systemmanage006', '���ӹ���ϵͳ�û�����', 13);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000012', 'AUTH_POSITION_MGT_ELECTRONIC', '100000631392061127516', 'systemmanage006', '���ӹ���ϵͳ��λ����', 12);
insert into QX_GNMB_GNMK (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'AUTH_USER_MGT_AUTH', '100000817293090209338', 'systemmanage006', '���ӹ���ϵͳ�û���Ȩ����', 14);
commit;