-- Create table
create table FR_ONLINE_USER
(
  userid     VARCHAR(255) comment '用户ID',
  sessionid  VARCHAR(255) comment '用户登录的SESSIONID',
  ip         VARCHAR(255) comment '用户登录的IP地址',
  stateno    INT comment '用户的登录状态，1表示登录中，2表示已退出等',
  logindate  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '用户登录的时间',
  logoutdate TIMESTAMP NOT NULL DEFAULT '1999-01-01 01:01:01' comment '用户退出登录的时间'
);
create index INDEX_SESSIONID on FR_ONLINE_USER (SESSIONID);

