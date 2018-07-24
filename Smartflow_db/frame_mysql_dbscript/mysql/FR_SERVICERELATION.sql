-- Create table 异常日志表
create table FR_SERVICERELATION
(
  callersvs		VARCHAR(255) comment '调用服务的服务名',
  calleesvs		VARCHAR(255) comment '被调用的服务名'
);
