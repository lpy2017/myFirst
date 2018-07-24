-- Create table
create table QX_GNMB
(
  GNMB_DM  VARCHAR(11) not null comment '����ģ�����',
  GNMB_MC  VARCHAR(80) not null comment '����ģ������',
  SS_GW_DM VARCHAR(15) comment '������λ����',
  JSSX_DM  VARCHAR(2) not null comment '��ɫ����',
  JG_DM    VARCHAR(15) default '00000000000' not null,
  SFGXJS   CHAR(1) default 'N' not null,
  JSLX     VARCHAR(15) default '1' comment '��ɫ���ͣ�0��ϵͳ��ɫ��1���û���ɫ'
)comment '����ģ(��ɫ)';
-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_GNMB
  add constraint PK_QX_GNMB primary key (GNMB_DM);

insert into QX_GNMB (GNMB_DM, GNMB_MC, SS_GW_DM, JSSX_DM, JG_DM, SFGXJS)
  values ('00000000001', '��������Ա��ɫ', null, '01', '000000000000000', 'N');
insert into QX_GNMB (GNMB_DM, GNMB_MC, SS_GW_DM, JSSX_DM, JG_DM, SFGXJS, JSLX)
values ('00000000011', 'ϵͳ����Ա', null, '02', '000000000000000', 'Y', '1');
insert into QX_GNMB (GNMB_DM, GNMB_MC, SS_GW_DM, JSSX_DM, JG_DM, SFGXJS, JSLX)
values ('00000000012', '��ȫ���ܹ���Ա', null, '03', '000000000000000', 'Y', '1');
insert into QX_GNMB (GNMB_DM, GNMB_MC, SS_GW_DM, JSSX_DM, JG_DM, SFGXJS, JSLX)
values ('00000000013', '��ȫ���Ա', null, '04', '000000000000000', 'Y', '1');
commit;
