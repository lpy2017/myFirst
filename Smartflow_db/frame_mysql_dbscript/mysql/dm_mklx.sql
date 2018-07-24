-- Create table
create table DM_MKLX
(
  MKLX_DM VARCHAR(2) not null comment '模块类型代码',
  MKLX_MC VARCHAR(20) not null comment '模块类型名称',
  XYBZ    CHAR(1) default 'Y' not null comment '选用标志',
  YXBZ    CHAR(1) default 'Y' not null comment '有效标志'
);

-- Create/Recreate primary, unique and foreign key constraints 
alter table DM_MKLX
  add constraint PK_DM_MKLX primary key (MKLX_DM);

insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('01', '专用系统URL', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('02', 'MDI窗口', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('03', 'SHEET窗口', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('04', 'EXE文件', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('05', '通用系统URL', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('06', '脚本', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('07', '工具项', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('10', 'web资源', 'Y', 'Y');
insert into DM_MKLX (MKLX_DM, MKLX_MC, XYBZ, YXBZ)
values ('11', '业务对象', 'Y', 'Y');
commit;