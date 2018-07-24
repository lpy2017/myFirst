-- Create table
create table QX_OPERATION
(
  OPERATION_DM          VARCHAR(256) not null comment '操作代码',
  OPERATION_MC          VARCHAR(120) default '操作' not null comment '操作名称',
  GNMK_DM               VARCHAR(256) not null comment '功能模块代码',
  OPERATION_DESCRIPTION VARCHAR(256) comment '操作描述',
  YXBZ                  CHAR(1) default 'Y' not null comment '有效标志',
  WSQCZCLFS_DM          VARCHAR(16) default '00' not null comment '未授权操作处理方式代码'
) comment '操作';

-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_OPERATION
  add constraint PK_QX_OPERATION primary key (OPERATION_DM, GNMK_DM);
alter table QX_OPERATION
  add constraint FK_QX_OPERATION_GNMK_DM foreign key (GNMK_DM)
  references QX_GNMK (GNMK_DM) on delete cascade;
