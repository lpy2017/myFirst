
create table QX_FAV_GNMK_TREE
(
  JD_DM    VARCHAR(21) not null comment '功能模块代码',
  FJD_DM   VARCHAR(21) not null comment '节点类型代码',
  JD_MC    VARCHAR(80) not null comment '节点名称',
  GNMK_DM  VARCHAR(256) comment '节点代码',
  JDLX_DM  VARCHAR(2) comment '父节点代码',
  JD_ORDER INT(5) not null comment '节点顺序',
  USERID   VARCHAR(11) not null comment '用户ID'
)
;
alter table QX_FAV_GNMK_TREE
  add constraint PK_QX_FAV_GNMK_TREE primary key (JD_DM,USERID);
-- alter table QX_FAV_GNMK_TREE
--  add constraint FK_QX_FAV_GNMK_TREE_USERID foreign key (USERID)
--  references QX_USER (USERID);


