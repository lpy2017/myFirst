-- ������: ��Ϣ�ֶ�չʾ����
create table MESSAGE_FIELD_DISPLAY
(
  USER_ID       VARCHAR(11) not null comment '�û�id��������Ա���룩',
  FIELD_NAME    VARCHAR(128) not null comment 'ͨ����Ϣ�ֶ�����',
  DISPLAY_ORDER INTEGER not null comment '��ʾ˳����ֵԽСԽ��ǰ',
  DISPLAY_NAME  VARCHAR(128) comment '��ʾ����',
  WIDTH         VARCHAR(16) comment '��ʾ���',
  SORTORDER     VARCHAR(2) comment 'ȱʡ����˳��',
  SORTDIRECTION  VARCHAR(2) comment '����ʽ:Y����,N����'
) comment '��Ϣ�ֶ�չʾ���壨���û���'
;
alter table MESSAGE_FIELD_DISPLAY
  add constraint PK_MESSAGE_FIELD_DISPLAY primary key (USER_ID, FIELD_NAME, DISPLAY_ORDER);
alter table MESSAGE_FIELD_DISPLAY
  add constraint FK_MESSAGE_FIELD_DISPLAY foreign key (FIELD_NAME)
  references MESSAGE_FIELD_DEFINITION (NAME);
