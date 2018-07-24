-- Create table
create table FR_SQL_STATISTICS
(
  xh            VARCHAR(20) not null comment '主键',
  service_id    VARCHAR(100) comment '服务ID',
  sql_statement VARCHAR(4000) comment 'sql语句', 
  start_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '开始时间',
  end_time      TIMESTAMP NOT NULL DEFAULT '1999-01-01 01:01:01' comment '结束时间',
  period        INT comment '执行时长（毫秒）',
  serial_no  VARCHAR(32) comment '交易流水号'
) comment 'SQL执行情况统计';

-- Create/Recreate primary, unique and foreign key constraints 
alter table FR_SQL_STATISTICS
  add constraint PK_FR_SQL_STATISTICS_XH primary key (XH),
  add unique index (XH);
