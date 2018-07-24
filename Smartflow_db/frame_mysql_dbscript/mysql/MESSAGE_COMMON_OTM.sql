-- 创建表: 
create table MESSAGE_COMMON_OTM
(
  ID       VARCHAR(40) not null,
  QX_JG_DM VARCHAR(15) not null
)
;
alter table MESSAGE_COMMON_OTM
  add constraint PK_MESSAGE_COMMON_OTM primary key (ID, QX_JG_DM);
alter table MESSAGE_COMMON_OTM
  add constraint FK_MESSAGE_COMMON_OTM foreign key (ID)
  references MESSAGE_COMMON (ID);


insert into MESSAGE_COMMON (ID, QX_JG_DM, CZRY_MC, CZRY_DM, CZ_DATE, ISSUE_FLAG, PRIORITY, CONTENT)
	values ('8a8118e2-15a11f28-0115-a122ce44-0001', '000000000000000', 'ds_admin', '00000000000', str_to_date('05-08-2007', '%d-%m-%Y'), '1', '0', '欢迎使用SmartFrame应用开发平台，请尽快完成系统初始化工作。');

insert into MESSAGE_COMMON_OTM (ID, QX_JG_DM)
	values ('8a8118e2-15a11f28-0115-a122ce44-0001', '000000000000000');

commit;
