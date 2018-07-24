-- ������: ��Ϣ
create table MESSAGE
(
  ID                VARCHAR(20) not null comment '��Ϣid',
  SYSTEM_NAME       VARCHAR(50) comment 'ϵͳ���ƣ�����qx_system��',
  MESSAGE_SYSTEM_ID VARCHAR(4) comment '��Ϣϵͳid',
  TOPIC             VARCHAR(512) not null comment '��Ϣ����',
  TOPIC_URL         VARCHAR(512) comment '��Ϣ��������url',
  TYPE              CHAR(6) comment '��Ϣ����',
  PRIORITY          CHAR(1) comment '���ȼ�����H�����ߣ���M�����еȣ���L������',
  ALLOW_DELETE      CHAR(1) comment '�Ƿ�����ɾ������Y����������N����������',
  CREATE_TIME       TIMESTAMP default now() not null comment '����ʱ��',
  CREATED_BY        CHAR(11) comment '������',
  LAST_RECEIVED_BY  CHAR(11) comment '��������',
  LAST_RECEIVE_TIME DATE comment '������ʱ��',
  IS_ARCHIVED       CHAR(1) default 'N' not null comment '�Ƿ�鵵����Y�����ѹ鵵����N����δ�鵵',
  ARCHIVE_TIME      DATE comment '�鵵ʱ��',
  AVAILABLE_UNTIL   DATE comment '����İ�����ޣ���Ϣ����Ч����',
  COMMENTS          VARCHAR(4000) comment 'ע��'
) comment 'ͨ����Ϣ';

alter table MESSAGE
  add constraint PK_MESSAGE primary key (ID);
alter table MESSAGE
  add constraint FK_MESSAGE_SYSTEM foreign key (SYSTEM_NAME)
  references QX_SYSTEM (SYSTEMNAME);
alter table MESSAGE
  add constraint FK_MESSAGE_TYPE foreign key (TYPE)
  references MESSAGE_TYPE (ID);