-- 创建表: 消息字段展示定义
create table MESSAGE_FIELD_DISPLAY
(
  USER_ID       VARCHAR(11) not null comment '用户id（操作人员代码）',
  FIELD_NAME    VARCHAR(128) not null comment '通用消息字段名称',
  DISPLAY_ORDER INTEGER not null comment '显示顺序，数值越小越靠前',
  DISPLAY_NAME  VARCHAR(128) comment '显示名称',
  WIDTH         VARCHAR(16) comment '显示宽度',
  SORTORDER     VARCHAR(2) comment '缺省排序顺序',
  SORTDIRECTION  VARCHAR(2) comment '排序方式:Y升序,N降序'
) comment '消息字段展示定义（分用户）'
;
alter table MESSAGE_FIELD_DISPLAY
  add constraint PK_MESSAGE_FIELD_DISPLAY primary key (USER_ID, FIELD_NAME, DISPLAY_ORDER);
alter table MESSAGE_FIELD_DISPLAY
  add constraint FK_MESSAGE_FIELD_DISPLAY foreign key (FIELD_NAME)
  references MESSAGE_FIELD_DEFINITION (NAME);
