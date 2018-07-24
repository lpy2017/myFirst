-- Create table
create table QX_OPERATION
(
  OPERATION_DM          VARCHAR(256) not null comment '��������',
  OPERATION_MC          VARCHAR(120) default '����' not null comment '��������',
  GNMK_DM               VARCHAR(256) not null comment '����ģ�����',
  OPERATION_DESCRIPTION VARCHAR(256) comment '��������',
  YXBZ                  CHAR(1) default 'Y' not null comment '��Ч��־',
  WSQCZCLFS_DM          VARCHAR(16) default '00' not null comment 'δ��Ȩ��������ʽ����'
) comment '����';

-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_OPERATION
  add constraint PK_QX_OPERATION primary key (OPERATION_DM, GNMK_DM);
alter table QX_OPERATION
  add constraint FK_QX_OPERATION_GNMK_DM foreign key (GNMK_DM)
  references QX_GNMK (GNMK_DM) on delete cascade;
