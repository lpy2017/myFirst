-- 创建表: 消息类型
create table MESSAGE_TYPE
(
  ID   CHAR(6) not null comment '消息类型id',
  NAME VARCHAR(64) not null comment '消息类型名称'
) comment '消息类型'
;

alter table MESSAGE_TYPE
  add constraint PK_MESSAGE_TYPE primary key (ID);


insert into MESSAGE_TYPE (ID, NAME)
values ('100000', '任务');
insert into MESSAGE_TYPE (ID, NAME)
values ('200000', '提示');
insert into MESSAGE_TYPE (ID, NAME)
values ('300000', '预警');
insert into MESSAGE_TYPE (ID, NAME)
values ('900000', '消息');

commit;