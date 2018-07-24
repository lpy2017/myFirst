create table QX_GW
(
  GW_DM    VARCHAR(15) not null comment'岗位代码',
  GW_MC    VARCHAR(80) not null  comment'岗位名称',
  GWLX     VARCHAR(2)  comment'岗位类型',
  YWBS     VARCHAR(5) comment'业务标识',
  SJ_GW_DM VARCHAR(15) comment'上级岗位代码',
  QX_JG_DM VARCHAR(15) not null comment'权限机关代码',
  JG_DM    VARCHAR(15) not null comment'机关代码',
  YWHJ_DM  VARCHAR(6) not null comment'业务环节代码',
  XGRQ     DATE comment'修改日期'
);
  
alter table QX_GW
  add constraint PK_QX_GW primary key (GW_DM);
alter table QX_GW
  add constraint FK_QX_GW_YWHJ_DM foreign key (YWHJ_DM)
  references DM_YWHJ (YWHJ_DM);
  
insert into QX_GW (GW_DM, GW_MC, GWLX, YWBS, SJ_GW_DM, QX_JG_DM, JG_DM, YWHJ_DM)
  values ('000000000000000', '超级用户岗', '01', '01   ', null, '000000000000000', '000000000000000', '000000');
insert into QX_GW (GW_DM, GW_MC, GWLX, YWBS, SJ_GW_DM, QX_JG_DM, JG_DM, YWHJ_DM, XGRQ)
values ('000000000000010', '系统管理员', '01', '01', null, '000000000000000', '000000000000000', '000000', str_to_date('05-08-2007', '%d-%m-%Y'));
insert into QX_GW (GW_DM, GW_MC, GWLX, YWBS, SJ_GW_DM, QX_JG_DM, JG_DM, YWHJ_DM, XGRQ)
values ('000000000000011', '安全保密管理员', '01', '01', null, '000000000000000', '000000000000000', '000000', str_to_date('05-08-2007', '%d-%m-%Y'));
insert into QX_GW (GW_DM, GW_MC, GWLX, YWBS, SJ_GW_DM, QX_JG_DM, JG_DM, YWHJ_DM, XGRQ)
values ('000000000000012', '安全审计员', '01', '01', null, '000000000000000', '000000000000000', '000000', str_to_date('05-08-2007', '%d-%m-%Y'));
  commit;