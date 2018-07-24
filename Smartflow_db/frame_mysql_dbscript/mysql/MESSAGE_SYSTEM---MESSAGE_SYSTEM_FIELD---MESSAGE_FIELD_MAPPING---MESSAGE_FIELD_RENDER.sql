-- ������: ��Ϣϵͳ
create table MESSAGE_SYSTEM
(
  ID                    VARCHAR(4) not null comment '��Ϣϵͳid',
  NAME                  VARCHAR(64) not null comment '��Ϣϵͳ����',
  KEY_NAME              VARCHAR(16) not null comment '��Ϣϵͳkey��key_idΪ��ϢUID',
  IS_LEGACY             CHAR(1) not null comment '�Ƿ�Ϊ����ϵͳ����Y��������ϵͳ����N����������ϵͳ',
  HANDLER_CLASS         VARCHAR(256) not null comment '��Ϣ�����࣬����̳���MessageHandler',
  MAPPING_BUILDER_CLASS VARCHAR(256) not null comment 'ӳ�䴦���࣬����ʵ��MappingBuilder�ӿ�',
  DESCRIPTION           VARCHAR(256) comment '��Ϣϵͳ����',
  IS_ENABLED            CHAR(1) default 'Y' not null comment '�Ƿ����ô���Ϣϵͳ'
)comment '��Ϣϵͳ'
;
alter table MESSAGE_SYSTEM
  add constraint PK_MESSAGE_SYSTEM primary key (ID);
alter table MESSAGE_SYSTEM
  add constraint UNI_MESSAGE_SYSTEM_KEY unique (KEY_NAME);


insert into MESSAGE_SYSTEM (ID, NAME, KEY_NAME, IS_LEGACY, HANDLER_CLASS, MAPPING_BUILDER_CLASS, DESCRIPTION, IS_ENABLED)
  values ('1000', 'ȱʡ��Ϣϵͳ', 'GENERIC', 'N', 'com.digitalchina.framework.task.handler.GenericMessageHandler', 'com.digitalchina.framework.task.builder.GenericORMappingBuilder', null, 'Y');

  commit;


-- ������: ��Ϣϵͳ����Ϣ�ֶ�
create table MESSAGE_SYSTEM_FIELD
(
  MESSAGE_SYSTEM_ID VARCHAR(4) not null comment '��Ϣϵͳid',
  FIELD_NAME        VARCHAR(128) not null comment '��Ϣ�ֶ�����'
)comment '��Ϣϵͳ����Ϣ�ֶζ��չ�ϵ'
;
alter table MESSAGE_SYSTEM_FIELD
  add constraint PK_MESSAGE_SYSTEM_FIELD primary key (MESSAGE_SYSTEM_ID, FIELD_NAME);
alter table MESSAGE_SYSTEM_FIELD
  add constraint FK_MESSAGE_SYSTEM_FIELD_ID foreign key (MESSAGE_SYSTEM_ID)
  references MESSAGE_SYSTEM (ID);
alter table MESSAGE_SYSTEM_FIELD
  add constraint FK_MESSAGE_SYSTEM_FIELD_NAME foreign key (FIELD_NAME)
  references MESSAGE_FIELD_DEFINITION (NAME);



insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'ALLOWDELETE');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'ARCHIVETIME');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'AVAILABLEUNTIL');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'COMMENTS');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'CREATEDBY');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'CREATETIME');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'ID');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'ISARCHIVED');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'LASTRECEIVEDBY');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'LASTRECEIVETIME');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'MESSAGESYSTEMID');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'PRIORITY');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'STATUS');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'SYSTEMNAME');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'TOPIC');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'TOPICURL');
insert into MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME)
values ('1000', 'TYPE');
commit;


-- ������: ��Ϣ�ֶ�ӳ��
create table MESSAGE_FIELD_MAPPING
(
  MESSAGE_SYSTEM_ID VARCHAR(4) comment '��Ϣϵͳid',
  FIELD_NAME        VARCHAR(128) comment '�ֶ�����',
  LEGACY_TABLE_NAME VARCHAR(32) not null comment '��Ӧ������ϵͳ������',
  LEGACY_FIELD_EXP  VARCHAR(256) not null comment '��Ӧ������ϵͳ�ֶα��ʽ��SQL�﷨��'
)comment 'ͨ����Ϣ�ֶ�ӳ��'
;
alter table MESSAGE_FIELD_MAPPING
  add constraint FK_MSG_FIELD_MAPPING_FN foreign key (FIELD_NAME)
  references MESSAGE_FIELD_DEFINITION (NAME);
alter table MESSAGE_FIELD_MAPPING
  add constraint FK_MSG_FIELD_MAPPING_ID foreign key (MESSAGE_SYSTEM_ID, FIELD_NAME)
  references MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME);


-- ������: ��Ϣ�ֶ���Ⱦ��
create table MESSAGE_FIELD_RENDER
(
  ID                VARCHAR(10) comment '��Ⱦ��id',
  NAME              VARCHAR(32) comment '��Ⱦ������',
  MESSAGE_SYSTEM_ID VARCHAR(4) comment '��Ϣϵͳid',
  DESCRIPTION       VARCHAR(256) comment '��Ⱦ������',
  FIELD_NAME        VARCHAR(128) comment '�ֶ�����',
  RENDER_CLASS      VARCHAR(256) comment '��Ⱦ����'
)comment 'ͨ����Ϣ�ֶ���Ⱦ��'
;
alter table MESSAGE_FIELD_RENDER
  add constraint FK_MESSAGE_FIELD_RENDER foreign key (FIELD_NAME)
  references MESSAGE_FIELD_DEFINITION (NAME);




insert into MESSAGE_FIELD_RENDER (ID, NAME, MESSAGE_SYSTEM_ID, DESCRIPTION, FIELD_NAME, RENDER_CLASS)
values ('00000001', 'SGYTOPIC', '9001', null, 'TOPIC', 'ctais.business.message.common.SgyTopicMessageRender');
insert into MESSAGE_FIELD_RENDER (ID, NAME, MESSAGE_SYSTEM_ID, DESCRIPTION, FIELD_NAME, RENDER_CLASS)
values ('00000002', 'TODOTOPIC', '9000', null, 'TOPIC', 'com.digitalchina.framework.task.render.TodoTopicMessageRender');
insert into MESSAGE_FIELD_RENDER (ID, NAME, MESSAGE_SYSTEM_ID, DESCRIPTION, FIELD_NAME, RENDER_CLASS)
values ('00000003', 'TODOLSTOPIC', '9002', null, 'TOPIC', 'com.digitalchina.framework.task.render.TodoTopicMessageRender');
insert into MESSAGE_FIELD_RENDER (ID, NAME, MESSAGE_SYSTEM_ID, DESCRIPTION, FIELD_NAME, RENDER_CLASS)
values ('00000004', 'reptodotopic', '1000', null, 'TOPIC', 'com.digitalchina.framework.task.render.TodoTopicMessageRender');
commit;