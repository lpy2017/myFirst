-- ������: ��Ϣ����
create table MESSAGE_TYPE
(
  ID   CHAR(6) not null comment '��Ϣ����id',
  NAME VARCHAR(64) not null comment '��Ϣ��������'
) comment '��Ϣ����'
;

alter table MESSAGE_TYPE
  add constraint PK_MESSAGE_TYPE primary key (ID);


insert into MESSAGE_TYPE (ID, NAME)
values ('100000', '����');
insert into MESSAGE_TYPE (ID, NAME)
values ('200000', '��ʾ');
insert into MESSAGE_TYPE (ID, NAME)
values ('300000', 'Ԥ��');
insert into MESSAGE_TYPE (ID, NAME)
values ('900000', '��Ϣ');

commit;