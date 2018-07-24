-- 创建表: 公共消息
create table MESSAGE_COMMON
(
  ID         VARCHAR(40) not null,
  QX_JG_DM   VARCHAR(15) not null,
  CZRY_MC    VARCHAR(60),
  CZRY_DM    VARCHAR(11) not null,
  CZ_DATE    TIMESTAMP default now() not null,
  ISSUE_FLAG CHAR(1) default '0' not null comment '发布标识：0草稿 1发布',
  PRIORITY   CHAR(1) default '0' not null comment '消息类型 0普通 3重要',
  CONTENT    VARCHAR(400)
) comment '公共消息'
;
alter table MESSAGE_COMMON
  add primary key (ID);
