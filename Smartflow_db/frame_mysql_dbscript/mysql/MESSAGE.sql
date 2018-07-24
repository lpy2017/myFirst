-- 创建表: 消息
create table MESSAGE
(
  ID                VARCHAR(20) not null comment '消息id',
  SYSTEM_NAME       VARCHAR(50) comment '系统名称（参照qx_system）',
  MESSAGE_SYSTEM_ID VARCHAR(4) comment '消息系统id',
  TOPIC             VARCHAR(512) not null comment '消息主题',
  TOPIC_URL         VARCHAR(512) comment '消息主题链接url',
  TYPE              CHAR(6) comment '消息类型',
  PRIORITY          CHAR(1) comment '优先级，‘H’：高，‘M’：中等，‘L’：低',
  ALLOW_DELETE      CHAR(1) comment '是否允许删除，‘Y’：允许，‘N’：不允许',
  CREATE_TIME       TIMESTAMP default now() not null comment '创建时间',
  CREATED_BY        CHAR(11) comment '创建人',
  LAST_RECEIVED_BY  CHAR(11) comment '最后接收人',
  LAST_RECEIVE_TIME DATE comment '最后接收时间',
  IS_ARCHIVED       CHAR(1) default 'N' not null comment '是否归档，‘Y’：已归档，‘N’：未归档',
  ARCHIVE_TIME      DATE comment '归档时间',
  AVAILABLE_UNTIL   DATE comment '任务的办结期限，消息的有效期限',
  COMMENTS          VARCHAR(4000) comment '注释'
) comment '通用消息';

alter table MESSAGE
  add constraint PK_MESSAGE primary key (ID);
alter table MESSAGE
  add constraint FK_MESSAGE_SYSTEM foreign key (SYSTEM_NAME)
  references QX_SYSTEM (SYSTEMNAME);
alter table MESSAGE
  add constraint FK_MESSAGE_TYPE foreign key (TYPE)
  references MESSAGE_TYPE (ID);