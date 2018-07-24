-- Create table
create table FR_SERVICE_STATISTICS
(
  xh            VARCHAR(20) not null comment '主键',
  service_id    VARCHAR(100) comment '服务ID',
  start_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '开始时间',
  end_time      TIMESTAMP NOT NULL DEFAULT '1999-01-01 01:01:01' comment '结束时间',
  period        INT comment '执行时长（毫秒）',
  serial_no  VARCHAR(32) comment '交易流水号'
) comment 'Action执行时间统计';

-- Create/Recreate primary, unique and foreign key constraints 
alter table FR_SERVICE_STATISTICS
  add constraint PK_FR_SERVICE_STATISTICS_XH primary key (XH),
  add unique index (XH);
