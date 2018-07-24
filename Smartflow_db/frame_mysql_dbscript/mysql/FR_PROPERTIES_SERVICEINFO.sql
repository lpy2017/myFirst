-- Create table 异常日志表
create table FR_PROPERTIES_SERVICEINFO
(
  serviceid			VARCHAR(255) COMMENT '服务方法名称',
  fullname  	VARCHAR(255) COMMENT '服务方法的全名',
  actionName	VARCHAR(255) COMMENT '调用服务方法的Action名称',
  description	VARCHAR(255) COMMENT '服务描述信息'
);
