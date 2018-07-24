-- Create table
create table FR_ACTION_STATISTICS
(
  xh            VARCHAR(20) not null comment '����',
  service_id    VARCHAR(100) comment '����ID',
  start_time   	timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '��ʼʱ��',
  end_time      timestamp NOT NULL DEFAULT '1999-01-01 01:01:01' comment '����ʱ��',
  period        INT comment 'ִ��ʱ�������룩',
  serial_no  VARCHAR(32) comment '������ˮ��'
) comment 'Actionִ��ʱ��ͳ��';

-- Create/Recreate primary, unique and foreign key constraints 
alter table FR_ACTION_STATISTICS
  add constraint PK_FR_ACTION_STATISTICS_XH primary key (XH),
  add unique index (XH);
