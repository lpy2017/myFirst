-- 创建表: 消息扩展
create table MESSAGE_EXTENSION
(
  MESSAGE_ID        VARCHAR(20) not null comment '消息id',
  CUSTOM_FIELD_NAME VARCHAR(128) not null comment '自定义字段名称',
  FIELD_VALUE       VARCHAR(4000) comment '自定义字段值'
) comment '通用消息扩展'
;

alter table MESSAGE_EXTENSION
  add constraint PK_MESSAGE_EXT primary key (MESSAGE_ID, CUSTOM_FIELD_NAME);
alter table MESSAGE_EXTENSION
  add constraint FK_MESSAGE_EXT_FIELD foreign key (CUSTOM_FIELD_NAME)
  references MESSAGE_FIELD_DEFINITION (NAME);
alter table MESSAGE_EXTENSION
  add constraint FK_MESSAGE_EXT_MESSAGE foreign key (MESSAGE_ID)
  references MESSAGE (ID);