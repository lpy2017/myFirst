-- Create table
create table DM_YWHJ
(
  YWHJ_DM VARCHAR(6) not null comment '业务环节代码',
  YWHJ_MC VARCHAR(80) not null comment '业务环节名称',
  XYBZ    CHAR(1) not null comment '选用标志',
  YXBZ    CHAR(1) not null comment '有效标志'
)comment '业务环节代码';

-- Create/Recreate primary, unique and foreign key constraints 
alter table DM_YWHJ
  add constraint PK_DM_YWHJ primary key (YWHJ_DM);
  
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('010000', '行政管理环节', 'N', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('020000', '党务管理环节', 'N', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('030400', '资料档案管理', 'Y', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('040000', '数据管理', 'Y', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('040100', '数据采集', 'Y', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('040200', '数据分析审计', 'Y', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('000000', '综合', 'Y', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('090000', '系统维护', 'Y', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('090100', '权限管理', 'Y', 'Y');
insert into DM_YWHJ (YWHJ_DM, YWHJ_MC, XYBZ, YXBZ)
values ('090200', '数据字典', 'Y', 'Y');

commit;