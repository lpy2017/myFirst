-- ������: ϵͳ����
create table XT_XTCS
(
  CSXH   VARCHAR(5) not null comment '�������',
  JG_DM  VARCHAR(15) not null comment '��������',
  CSMC   VARCHAR(80) not null comment '��������',
  CSNR   VARCHAR(500) not null comment '��������',
  SYSM   VARCHAR(200) comment 'ʹ��˵��',
  XYBZ   CHAR(1) not null comment 'ѡ�ñ�־',
  JZSZBZ CHAR(1) comment '�������ñ�־'
) comment 'ϵͳ����'
;

alter table XT_XTCS
  add constraint PK_XT_XTCS primary key (CSXH, JG_DM);


insert into XT_XTCS (CSXH, JG_DM, CSMC, CSNR, SYSM, XYBZ, JZSZBZ)
	values ('10000', 'PUBLIC', 'ϵͳ����', 'Sm@rtFrame5.0 ���ɹ���ƽ̨', '����ϵͳ����', 'Y', 'Y');
insert into XT_XTCS (CSXH, JG_DM, CSMC, CSNR, SYSM, XYBZ, JZSZBZ)
  values ('10001', 'PUBLIC', '���������', '9', '9', 'Y', 'Y');
insert into XT_XTCS (CSXH, JG_DM, CSMC, CSNR, SYSM, XYBZ, JZSZBZ)
	values ('10002', 'PUBLIC', '�������������ʽ', '\d+', '����У���û��޸�����ʱ��������ɹ���("\d+"Ϊȫ����,��ֹɾ��,����ֵΪ0ʱΪ������)', 'Y', 'Y');
insert into XT_XTCS (CSXH, JG_DM, CSMC, CSNR, SYSM, XYBZ, JZSZBZ)
	values ('10003', 'PUBLIC', '���������������������', '5', '��һ��ʱ�����û��ɳ��Ե������������������ (��ֹɾ��,����ֵΪ0ʱΪ������)', 'Y', 'Y');
insert into XT_XTCS (CSXH, JG_DM, CSMC, CSNR, SYSM, XYBZ, JZSZBZ)
	values ('10004', 'PUBLIC', '�����������������������ʱ��', '30', '������������������Ե�����ʱ��,��λ:���� (��ֹɾ��,����ֵΪ0ʱΪ������)', 'Y', 'Y');
insert into XT_XTCS (CSXH, JG_DM, CSMC, CSNR, SYSM, XYBZ, JZSZBZ)
	values ('10005', 'PUBLIC', '�ﵽ���������������������ʽ', '1', '1:ָ��ʱ�����������;2:�����û� (��ֹɾ��,����ֵΪ0ʱΪ������)', 'Y', 'Y');
insert into XT_XTCS (CSXH, JG_DM, CSMC, CSNR, SYSM, XYBZ, JZSZBZ)
	values ('10006', 'PUBLIC', '�����û�����ʱ��', '30', '�û��ﵽ����������������������Ƶ�¼��ʱ���,��λ:���� (��ֹɾ��,����ֵΪ0ʱΪ������)', 'Y', 'Y');

commit;