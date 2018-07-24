-- 创建表: 消息系统
create table MESSAGE_SYSTEM
(
  ID                    VARCHAR(4) not null comment '消息系统id',
  NAME                  VARCHAR(64) not null comment '消息系统名称',
  KEY_NAME              VARCHAR(16) not null comment '消息系统key，key_id为消息UID',
  IS_LEGACY             CHAR(1) not null comment '是否为遗留系统，‘Y’：遗留系统，‘N’：非遗留系统',
  HANDLER_CLASS         VARCHAR(256) not null comment '消息处理类，必须继承自MessageHandler',
  MAPPING_BUILDER_CLASS VARCHAR(256) not null comment '映射处理类，必须实现MappingBuilder接口',
  DESCRIPTION           VARCHAR(256) comment '消息系统描述',
  IS_ENABLED            CHAR(1) default 'Y' not null comment '是否启用此消息系统'
)comment '消息系统'
;
alter table MESSAGE_SYSTEM
  add constraint PK_MESSAGE_SYSTEM primary key (ID);
alter table MESSAGE_SYSTEM
  add constraint UNI_MESSAGE_SYSTEM_KEY unique (KEY_NAME);


insert into MESSAGE_SYSTEM (ID, NAME, KEY_NAME, IS_LEGACY, HANDLER_CLASS, MAPPING_BUILDER_CLASS, DESCRIPTION, IS_ENABLED)
  values ('1000', '缺省消息系统', 'GENERIC', 'N', 'com.digitalchina.framework.task.handler.GenericMessageHandler', 'com.digitalchina.framework.task.builder.GenericORMappingBuilder', null, 'Y');

  commit;


-- 创建表: 消息系统的消息字段
create table MESSAGE_SYSTEM_FIELD
(
  MESSAGE_SYSTEM_ID VARCHAR(4) not null comment '消息系统id',
  FIELD_NAME        VARCHAR(128) not null comment '消息字段名称'
)comment '消息系统和消息字段对照关系'
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


-- 创建表: 消息字段映射
create table MESSAGE_FIELD_MAPPING
(
  MESSAGE_SYSTEM_ID VARCHAR(4) comment '消息系统id',
  FIELD_NAME        VARCHAR(128) comment '字段名称',
  LEGACY_TABLE_NAME VARCHAR(32) not null comment '对应的遗留系统表名称',
  LEGACY_FIELD_EXP  VARCHAR(256) not null comment '对应的遗留系统字段表达式（SQL语法）'
)comment '通用消息字段映射'
;
alter table MESSAGE_FIELD_MAPPING
  add constraint FK_MSG_FIELD_MAPPING_FN foreign key (FIELD_NAME)
  references MESSAGE_FIELD_DEFINITION (NAME);
alter table MESSAGE_FIELD_MAPPING
  add constraint FK_MSG_FIELD_MAPPING_ID foreign key (MESSAGE_SYSTEM_ID, FIELD_NAME)
  references MESSAGE_SYSTEM_FIELD (MESSAGE_SYSTEM_ID, FIELD_NAME);


-- 创建表: 消息字段渲染器
create table MESSAGE_FIELD_RENDER
(
  ID                VARCHAR(10) comment '渲染器id',
  NAME              VARCHAR(32) comment '渲染器名称',
  MESSAGE_SYSTEM_ID VARCHAR(4) comment '消息系统id',
  DESCRIPTION       VARCHAR(256) comment '渲染器描述',
  FIELD_NAME        VARCHAR(128) comment '字段名称',
  RENDER_CLASS      VARCHAR(256) comment '渲染器类'
)comment '通用消息字段渲染器'
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