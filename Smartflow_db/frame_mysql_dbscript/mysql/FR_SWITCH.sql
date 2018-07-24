-- Create table
create table FR_SWITCH
(
  ZJJC VARCHAR(255) comment '组件拼音简称',
  ZJMC VARCHAR(255) comment '组件名称',
  ZJZT VARCHAR(10) default 'on' comment '组件状态'
);

insert into FR_SWITCH (ZJJC, ZJMC, ZJZT)
values ('ywtj', '业务统计', 'on');
insert into FR_SWITCH (ZJJC, ZJMC, ZJZT)
values ('xnzd', '性能诊断', 'on');
insert into FR_SWITCH (ZJJC, ZJMC, ZJZT)
values ('yhjk', '用户监控', 'on');
insert into FR_SWITCH (ZJJC, ZJMC, ZJZT)
values ('yhsj', '用户审计', 'on');
insert into FR_SWITCH (ZJJC, ZJMC, ZJZT)
values ('ycjk', '异常监控', 'on');
insert into FR_SWITCH (ZJJC, ZJMC, ZJZT)
values ('fwjc', '服务检测', 'on');
insert into FR_SWITCH (ZJJC, ZJMC, ZJZT)
values ('qbjk', '全部监控', 'on');
commit;
