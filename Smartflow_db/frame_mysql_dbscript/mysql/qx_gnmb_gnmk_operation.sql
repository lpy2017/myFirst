-- Create table
create table QX_GNMB_GNMK_OPERATION
(
  GNMB_DM          VARCHAR(11) not null comment '����ģ�����',
  GNMK_DM          VARCHAR(256) not null comment '����ģ�����',
  OPERATION_DM     VARCHAR(256) not null comment '��������',
  OPERATIONAUTH_ID VARCHAR(21)
) comment 'Ȩ�޹���ģ�幦��ģ�����';

-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_GNMB_GNMK_OPERATION
  add constraint PK_QX_GNMB_GNMK_OPERATION primary key (GNMB_DM, GNMK_DM, OPERATION_DM);
alter table QX_GNMB_GNMK_OPERATION
  add constraint FK_QX_GNMB_GNMK_O_GNMB_DM foreign key (GNMB_DM)
  references QX_GNMB (GNMB_DM) on delete cascade;
alter table QX_GNMB_GNMK_OPERATION
  add constraint FK_QX_GNMB_GNMK_O_GNMK_DM foreign key (GNMK_DM)
  references QX_GNMK (GNMK_DM) on delete cascade;
