-- Create table
create table DM_MKLX
(
  MKLX_DM VARCHAR(2) not null comment 'ģ�����ʹ���',
  MKLX_MC VARCHAR(20) not null comment 'ģ����������',
  XYBZ    CHAR(1) default 'Y' not null comment 'ѡ�ñ�־',
  YXBZ    CHAR(1) default 'Y' not null comment '��Ч��־'
);

-- Create/Recreate primary, unique and foreign key constraints 
alter table DM_MKLX
  add constraint PK_DM_MKLX primary key (MKLX_DM);

insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('01', 'ר��ϵͳURL', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('02', 'MDI����', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('03', 'SHEET����', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('04', 'EXE�ļ�', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('05', 'ͨ��ϵͳURL', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('06', '�ű�', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('07', '������', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('10', 'web��Դ', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('11', 'ҵ�����', 'Y', 'Y');
commit;