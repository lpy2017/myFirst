-- Create table 业务统计表
create table FR_BUSINESS_TJ_LOG
(
  id         VARCHAR(20) not null comment 'ID',
  userid     VARCHAR(20) comment '用户ID，操作人员代码',
  req_type   VARCHAR(2) comment '请求方式10：电脑，20：手机',
  req_state  VARCHAR(20) comment '请求状态，success成功，fail失败，失败则记录异常信息',
  serviceid  VARCHAR(100) comment '请求服务ID',
  lrsj       TIMESTAMP not null comment 'sysdate数据录入时间',
  businessid VARCHAR(100) comment '业务ID',
  serial_no  VARCHAR(32) comment '交易流水号'
)comment '用户操作业务轨迹'
;
alter table FR_BUSINESS_TJ_LOG auto_increment = 10000000;
