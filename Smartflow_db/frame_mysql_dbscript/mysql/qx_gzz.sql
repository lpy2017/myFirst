-- Create table
create table QX_GZZ
(
  GZZ_DM    VARCHAR(15) not null comment '工作组代码',
  GZZ_MC    VARCHAR(80) not null comment '工作组名称',
  GZZLX     CHAR(1) not null comment '工作组类型',
  GZZZT     CHAR(1) default 'Y' not null comment '工作组状态',
  SJ_GZZ_DM VARCHAR(15) not null comment '上级工作组代码',
  JD_ORDER  INT not null AUTO_INCREMENT unique comment '工作组顺序'
) comment '工作组';


-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_GZZ
  add constraint PK_GZZ_GW primary key (GZZ_DM);

  
insert into QX_GZZ (GZZ_DM, GZZ_MC, GZZLX, GZZZT, SJ_GZZ_DM, JD_ORDER)
values ('000000000000000', '国家管理中心', '0', '0', '999999999999999', 1);
commit;