-- ������: ������Ϣ
create table MESSAGE_COMMON
(
  ID         VARCHAR(40) not null,
  QX_JG_DM   VARCHAR(15) not null,
  CZRY_MC    VARCHAR(60),
  CZRY_DM    VARCHAR(11) not null,
  CZ_DATE    TIMESTAMP default now() not null,
  ISSUE_FLAG CHAR(1) default '0' not null comment '������ʶ��0�ݸ� 1����',
  PRIORITY   CHAR(1) default '0' not null comment '��Ϣ���� 0��ͨ 3��Ҫ',
  CONTENT    VARCHAR(400)
) comment '������Ϣ'
;
alter table MESSAGE_COMMON
  add primary key (ID);
