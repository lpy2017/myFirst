-- Create table
create table QX_GZZ
(
  GZZ_DM    VARCHAR(15) not null comment '���������',
  GZZ_MC    VARCHAR(80) not null comment '����������',
  GZZLX     CHAR(1) not null comment '����������',
  GZZZT     CHAR(1) default 'Y' not null comment '������״̬',
  SJ_GZZ_DM VARCHAR(15) not null comment '�ϼ����������',
  JD_ORDER  INT not null AUTO_INCREMENT unique comment '������˳��'
) comment '������';


-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_GZZ
  add constraint PK_GZZ_GW primary key (GZZ_DM);

  
insert into QX_GZZ (GZZ_DM, GZZ_MC, GZZLX, GZZZT, SJ_GZZ_DM, JD_ORDER)
values ('000000000000000', '���ҹ�������', '0', '0', '999999999999999', 1);
commit;