create table SEC_PARAM
(
  CS_BZ  VARCHAR(50) not null,
  CS_MC  VARCHAR(50) not null,
  CS_LX  VARCHAR(20) not null,
  CS_VAL VARCHAR(200) not null
);

alter table SEC_PARAM
  add primary key (CS_BZ);
 
insert into SEC_PARAM (CS_BZ, CS_MC, CS_LX, CS_VAL)
values ('ACT_PERIOD', '�˺ż�������(��)', '��ֵ', '10');
insert into SEC_PARAM (CS_BZ, CS_MC, CS_LX, CS_VAL)
values ('PASS_PERIOD', '�����޸�����(��)', '��ֵ', '111');
insert into SEC_PARAM (CS_BZ, CS_MC, CS_LX, CS_VAL)
values ('INIT_PASSWORD', '��ʼ������', '�ַ���', 'KU0FmPu4SO8SHfsI9R5E6+VDtGCJUYayCNQ1qg==');
commit;
