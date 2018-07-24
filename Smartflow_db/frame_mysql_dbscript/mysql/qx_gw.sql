create table QX_GW
(
  GW_DM    VARCHAR(15) not null comment'��λ����',
  GW_MC    VARCHAR(80) not null  comment'��λ����',
  GWLX     VARCHAR(2)  comment'��λ����',
  YWBS     VARCHAR(5) comment'ҵ���ʶ',
  SJ_GW_DM VARCHAR(15) comment'�ϼ���λ����',
  QX_JG_DM VARCHAR(15) not null comment'Ȩ�޻��ش���',
  JG_DM    VARCHAR(15) not null comment'���ش���',
  YWHJ_DM  VARCHAR(6) not null comment'ҵ�񻷽ڴ���',
  XGRQ     DATE comment'�޸�����'
);
  
alter table QX_GW
  add constraint PK_QX_GW primary key (GW_DM);
alter table QX_GW
  add constraint FK_QX_GW_YWHJ_DM foreign key (YWHJ_DM)
  references DM_YWHJ (YWHJ_DM);
  
insert into QX_GW (GW_DM, GW_MC, GWLX, YWBS, SJ_GW_DM, QX_JG_DM, JG_DM, YWHJ_DM)
  values ('000000000000000', '�����û���', '01', '01   ', null, '000000000000000', '000000000000000', '000000');
insert into QX_GW (GW_DM, GW_MC, GWLX, YWBS, SJ_GW_DM, QX_JG_DM, JG_DM, YWHJ_DM, XGRQ)
values ('000000000000010', 'ϵͳ����Ա', '01', '01', null, '000000000000000', '000000000000000', '000000', str_to_date('05-08-2007', '%d-%m-%Y'));
insert into QX_GW (GW_DM, GW_MC, GWLX, YWBS, SJ_GW_DM, QX_JG_DM, JG_DM, YWHJ_DM, XGRQ)
values ('000000000000011', '��ȫ���ܹ���Ա', '01', '01', null, '000000000000000', '000000000000000', '000000', str_to_date('05-08-2007', '%d-%m-%Y'));
insert into QX_GW (GW_DM, GW_MC, GWLX, YWBS, SJ_GW_DM, QX_JG_DM, JG_DM, YWHJ_DM, XGRQ)
values ('000000000000012', '��ȫ���Ա', '01', '01', null, '000000000000000', '000000000000000', '000000', str_to_date('05-08-2007', '%d-%m-%Y'));
  commit;