-- Create table
create table FR_SWITCH
(
  ZJJC VARCHAR(255) comment '���ƴ�����',
  ZJMC VARCHAR(255) comment '�������',
  ZJZT VARCHAR(10) default 'on' comment '���״̬'
);

insert into FR_SWITCH (ZJJC, ZJMC, ZJZT)
values ('ywtj', 'ҵ��ͳ��', 'on');
insert into FR_SWITCH (ZJJC, ZJMC, ZJZT)
values ('xnzd', '�������', 'on');
insert into FR_SWITCH (ZJJC, ZJMC, ZJZT)
values ('yhjk', '�û����', 'on');
insert into FR_SWITCH (ZJJC, ZJMC, ZJZT)
values ('yhsj', '�û����', 'on');
insert into FR_SWITCH (ZJJC, ZJMC, ZJZT)
values ('ycjk', '�쳣���', 'on');
insert into FR_SWITCH (ZJJC, ZJMC, ZJZT)
values ('fwjc', '������', 'on');
insert into FR_SWITCH (ZJJC, ZJMC, ZJZT)
values ('qbjk', 'ȫ�����', 'on');
commit;
