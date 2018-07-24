-- ������: ��Ϣ��չ
create table MESSAGE_EXTENSION
(
  MESSAGE_ID        VARCHAR(20) not null comment '��Ϣid',
  CUSTOM_FIELD_NAME VARCHAR(128) not null comment '�Զ����ֶ�����',
  FIELD_VALUE       VARCHAR(4000) comment '�Զ����ֶ�ֵ'
) comment 'ͨ����Ϣ��չ'
;

alter table MESSAGE_EXTENSION
  add constraint PK_MESSAGE_EXT primary key (MESSAGE_ID, CUSTOM_FIELD_NAME);
alter table MESSAGE_EXTENSION
  add constraint FK_MESSAGE_EXT_FIELD foreign key (CUSTOM_FIELD_NAME)
  references MESSAGE_FIELD_DEFINITION (NAME);
alter table MESSAGE_EXTENSION
  add constraint FK_MESSAGE_EXT_MESSAGE foreign key (MESSAGE_ID)
  references MESSAGE (ID);