-- Create table
create table FR_ONLINE_USER
(
  userid     VARCHAR(255) comment '�û�ID',
  sessionid  VARCHAR(255) comment '�û���¼��SESSIONID',
  ip         VARCHAR(255) comment '�û���¼��IP��ַ',
  stateno    INT comment '�û��ĵ�¼״̬��1��ʾ��¼�У�2��ʾ���˳���',
  logindate  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '�û���¼��ʱ��',
  logoutdate TIMESTAMP NOT NULL DEFAULT '1999-01-01 01:01:01' comment '�û��˳���¼��ʱ��'
);
create index INDEX_SESSIONID on FR_ONLINE_USER (SESSIONID);

