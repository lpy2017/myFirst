-- Create table
create table DM_YWHJ
(
  YWHJ_DM VARCHAR(6) not null comment 'ҵ�񻷽ڴ���',
  YWHJ_MC VARCHAR(80) not null comment 'ҵ�񻷽�����',
  XYBZ    CHAR(1) not null comment 'ѡ�ñ�־',
  YXBZ    CHAR(1) not null comment '��Ч��־'
)comment 'ҵ�񻷽ڴ���';

-- Create/Recreate primary, unique and foreign key constraints 
alter table DM_YWHJ
  add constraint PK_DM_YWHJ primary key (YWHJ_DM);
  
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('010000', '����������', 'N', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('020000', '���������', 'N', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('030400', '���ϵ�������', 'Y', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('040000', '���ݹ���', 'Y', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('040100', '���ݲɼ�', 'Y', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('040200', '���ݷ������', 'Y', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('000000', '�ۺ�', 'Y', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('090000', 'ϵͳά��', 'Y', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('090100', 'Ȩ�޹���', 'Y', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('090200', '�����ֵ�', 'Y', 'Y');

commit;