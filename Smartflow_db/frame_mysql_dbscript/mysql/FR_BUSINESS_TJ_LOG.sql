-- Create table ҵ��ͳ�Ʊ�
create table FR_BUSINESS_TJ_LOG
(
  id         VARCHAR(20) not null comment 'ID',
  userid     VARCHAR(20) comment '�û�ID��������Ա����',
  req_type   VARCHAR(2) comment '����ʽ10�����ԣ�20���ֻ�',
  req_state  VARCHAR(20) comment '����״̬��success�ɹ���failʧ�ܣ�ʧ�����¼�쳣��Ϣ',
  serviceid  VARCHAR(100) comment '�������ID',
  lrsj       TIMESTAMP not null comment 'sysdate����¼��ʱ��',
  businessid VARCHAR(100) comment 'ҵ��ID',
  serial_no  VARCHAR(32) comment '������ˮ��'
)comment '�û�����ҵ��켣'
;
alter table FR_BUSINESS_TJ_LOG auto_increment = 10000000;
