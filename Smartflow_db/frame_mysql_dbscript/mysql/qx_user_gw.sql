-- Create table
create table QX_USER_GW
(
  USERID VARCHAR(11) not null comment '用户ID',
  GW_DM  VARCHAR(15) not null comment '岗位代码'
);

-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_USER_GW
  add constraint PK_QX_USER_GW primary key (USERID, GW_DM);
alter table QX_USER_GW
  add constraint FK_QX_USER_GW_GW_DM foreign key (GW_DM)
  references QX_GW (GW_DM);
alter table QX_USER_GW
  add constraint FK_QX_USER_GW_USERID foreign key (USERID)
  references QX_USER (USERID);
  
insert into QX_USER_GW (USERID, GW_DM) values ('00000000000', '000000000000000');  

commit;
