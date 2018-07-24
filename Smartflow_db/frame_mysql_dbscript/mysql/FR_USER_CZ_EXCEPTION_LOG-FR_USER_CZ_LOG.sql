-- Create table 异常日志表
-- Create table
create table FR_USER_CZ_EXCEPTION_LOG
(
  ID               VARCHAR(20) not null comment 'ID' ,
  RTNCODE          VARCHAR(10) comment '异常码',
  RTNMESSAGE       VARCHAR(255) comment '异常信息',
  EXCEPTIONTYPE    VARCHAR(50) comment '异常类型',
  EXCEPTIONMESSAGE LONGTEXT comment '异常详细信息',
  USERID           VARCHAR(20) comment '用户ID',
  SERVICEID        VARCHAR(100) comment '服务ID',
  LRSJ             TIMESTAMP not null comment '录入日期',
  REQ_PARAMS       LONGTEXT comment '请求参数',
  REQ_TYPE         VARCHAR(2) comment '请求方式10：电脑，20：手机',
  BUSINESSID       VARCHAR(100) comment '业务ID',
  SERIAL_NO        VARCHAR(32) not null comment '交易流水号'
)

;
alter table FR_USER_CZ_EXCEPTION_LOG auto_increment = 10000000;

-- Create table 用户操作日志表
-- Create table
create table FR_USER_CZ_LOG
(
  id            VARCHAR(20) not null comment 'ID',
  userid        VARCHAR(20) comment '用户ID，操作人员代码',
  req_type      VARCHAR(2) comment '请求方式10：电脑，20：手机',
  serviceid     VARCHAR(100) comment '请求服务ID',
  req_params    LONGTEXT comment '请求参数',
  req_begintime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '请求开始时间',
  req_endttime  TIMESTAMP NOT NULL DEFAULT '1999-01-01 01:01:01' comment '请求结束时间',
  req_state     VARCHAR(20) comment '请求状态，success成功，fail失败，失败则记录异常信息',
  res_result    LONGTEXT comment '返回数据',
  lrsj          TIMESTAMP NOT NULL DEFAULT '1999-01-01 01:01:01' comment 'sysdate数据录入时间',
  businessid    VARCHAR(100) comment  '业务ID',
  serial_no     VARCHAR(32) comment '交易流水号'
)comment = '用户操作轨迹'

;
alter table FR_USER_CZ_LOG auto_increment = 10000000;
