-- Create table
create table DM_DWLSGX
(
  DWLSGX_DM VARCHAR(2) not null comment '��λ������ϵ����',
  DWLSGX_MC VARCHAR(16) not null comment '��λ������ϵ����',
  DWLSGX_SM VARCHAR(256) comment '��λ������ϵ˵��',
  XYBZ      CHAR(1) default 'Y' not null comment 'ѡ�ñ�־',
  YXBZ      CHAR(1) default 'Y' not null comment '��Ч��־'
) comment '��λ������ϵ����';

  insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('10', '����            ', '����ȫ���˴�ί�ᡢ�й����롢����Ժ����ί������������������Ժ��ֱ�����������»���������������', 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('20', 'ʡ              ', '������������ֱϽ��', 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('40', '�С�����        ', '���������ݡ��ˡ�ʡϽ�С�ֱϽ��Ͻ�����أ�', 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('50', '��              ', '������(�ݡ���)Ͻ�С�ʡϽ��Ͻ���������أ��죩���졢�ؼ���', 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('60', '�ֵ�������    ', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('61', '�ֵ�            ', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('62', '��              ', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('63', '��              ', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('70', '���񡢴���ίԱ��', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('71', '����ίԱ��      ', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('72', '����ίԱ��      ', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('80', '��              ', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('90', '����            ', null, 'Y', 'Y');
commit;