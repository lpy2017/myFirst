-- Create table
create table QX_CZRY_GZZ
(
  CZRY_DM VARCHAR(11) not null comment '操作人员代码',
  GZZ_DM  VARCHAR(15) not null  comment '工作组代码'
);

-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_CZRY_GZZ
  add constraint PK_QX_CZRY_GZZ primary key (CZRY_DM, GZZ_DM);
alter table QX_CZRY_GZZ
  add constraint FK_QX_CZRY_GZZ_GZZ_DM foreign key (GZZ_DM)
  references QX_GZZ (GZZ_DM);
alter table QX_CZRY_GZZ
  add constraint FK_QX_CZRY_GZZ_CZRY_DM foreign key (CZRY_DM)
  references DM_CZRY (CZRY_DM);