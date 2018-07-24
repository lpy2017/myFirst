-- Create table
create table QX_GW_GNMB
(
  GW_DM   VARCHAR(15) not null comment '��λ����',
  GNMB_DM VARCHAR(11) not null comment '����ģ�����'
);

-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_GW_GNMB
  add constraint PK_QX_GW_GNMB primary key (GW_DM, GNMB_DM);
alter table QX_GW_GNMB
  add constraint FK_QX_GW_GNMB_GNMB_DM foreign key (GNMB_DM)
  references QX_GNMB (GNMB_DM);
alter table QX_GW_GNMB
  add constraint FK_QX_GW_GNMB_GW_DM foreign key (GW_DM)
  references QX_GW (GW_DM);
  
insert into QX_GW_GNMB (GW_DM, GNMB_DM) values ('000000000000000', '00000000001');
insert into QX_GW_GNMB (GW_DM, GNMB_DM)
values ('000000000000010', '00000000011');

insert into QX_GW_GNMB (GW_DM, GNMB_DM)
values ('000000000000011', '00000000012');

insert into QX_GW_GNMB (GW_DM, GNMB_DM)
values ('000000000000012', '00000000013');
commit;