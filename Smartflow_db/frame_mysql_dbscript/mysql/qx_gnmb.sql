-- Create table
create table QX_GNMB
(
  GNMB_DM  VARCHAR(11) not null comment '功能模板代码',
  GNMB_MC  VARCHAR(80) not null comment '功能模板名称',
  SS_GW_DM VARCHAR(15) comment '所属岗位代码',
  JSSX_DM  VARCHAR(2) not null comment '角色属性',
  JG_DM    VARCHAR(15) default '00000000000' not null,
  SFGXJS   CHAR(1) default 'N' not null,
  JSLX     VARCHAR(15) default '1' comment '角色类型，0：系统角色，1：用户角色'
)comment '功能模(角色)';
-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_GNMB
  add constraint PK_QX_GNMB primary key (GNMB_DM);

insert into QX_GNMB (GNMB_DM, GNMB_MC, SS_GW_DM, JSSX_DM, JG_DM, SFGXJS)
  values ('00000000001', '超级管理员角色', null, '01', '000000000000000', 'N');
insert into QX_GNMB (GNMB_DM, GNMB_MC, SS_GW_DM, JSSX_DM, JG_DM, SFGXJS, JSLX)
values ('00000000011', '系统管理员', null, '02', '000000000000000', 'Y', '1');
insert into QX_GNMB (GNMB_DM, GNMB_MC, SS_GW_DM, JSSX_DM, JG_DM, SFGXJS, JSLX)
values ('00000000012', '安全保密管理员', null, '03', '000000000000000', 'Y', '1');
insert into QX_GNMB (GNMB_DM, GNMB_MC, SS_GW_DM, JSSX_DM, JG_DM, SFGXJS, JSLX)
values ('00000000013', '安全审计员', null, '04', '000000000000000', 'Y', '1');
commit;
