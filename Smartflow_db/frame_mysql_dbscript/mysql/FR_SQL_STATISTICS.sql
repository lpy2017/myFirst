-- Create table
create table FR_SQL_STATISTICS
(
  xh            VARCHAR(20) not null comment '����',
  service_id    VARCHAR(100) comment '����ID',
  sql_statement VARCHAR(4000) comment 'sql���', 
  start_time    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '��ʼʱ��',
  end_time      TIMESTAMP NOT NULL DEFAULT '1999-01-01 01:01:01' comment '����ʱ��',
  period        INT comment 'ִ��ʱ�������룩',
  serial_no  VARCHAR(32) comment '������ˮ��'
) comment 'SQLִ�����ͳ��';

-- Create/Recreate primary, unique and foreign key constraints 
alter table FR_SQL_STATISTICS
  add constraint PK_FR_SQL_STATISTICS_XH primary key (XH),
  add unique index (XH);
