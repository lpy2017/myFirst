-- Create table
create table QX_SYSTEM
(
  SYSTEMNAME        VARCHAR(80) not null comment 'ϵͳ����',
  DESCRIPTION       VARCHAR(50) comment 'ϵͳ����',
  ICONURL           VARCHAR(50),
  VIRTRULROOTNAME   VARCHAR(50),
  COOKIENAME        VARCHAR(50),
  REALROOTURL       VARCHAR(200),
  WELCOMETITTLE     VARCHAR(50) comment '��ӭ����',
  WELCOMEURL        VARCHAR(200) comment '��ӭҳurl',
  LOGINURL          VARCHAR(500) comment '��¼��ַ',
  LOGINTYPE         VARCHAR(10) default 'U' not null comment '��¼����',
  LOGOUTURL         VARCHAR(200) comment '�˳���ַ',
  LOGOUTTYPE        VARCHAR(10) default 'U' not null comment '�˳�����',
  USERPARAMNAME     VARCHAR(50),
  PASSWORDPARAMNAME VARCHAR(50),
  LOGINSUCCESSTAG   VARCHAR(200) default ':CONSOLE:' comment '��¼�ɹ���־',
  TESTURL           VARCHAR(200),
  SORTORDER         VARCHAR(2) default '99' not null,
  XYBZ              VARCHAR(1) default 'Y' not null,
  YXBZ              VARCHAR(1) default 'Y' not null,
  BASEURL           VARCHAR(100) default '/ctais' comment '����·��',
  LOGINTIME         VARCHAR(1) default 'S',
  SCRIPT            BLOB comment 'ִ�нű�',
  SESSIONKEEP       VARCHAR(1) default 'Y',
  SESSIONKEEPTYPE   VARCHAR(10) default 'U',
  SESSIONKEEPURL    VARCHAR(500),
  UNIUSERTYPE       VARCHAR(10) default 'L',
  UNIUSERON         VARCHAR(1) default 'N',
  UNIUSERDATA       VARCHAR(200)
) comment 'ϵͳ';

-- Create/Recreate primary, unique and foreign key constraints 
alter table QX_SYSTEM
  add primary key (SYSTEMNAME);
  
  insert into QX_SYSTEM (SYSTEMNAME, DESCRIPTION, ICONURL, VIRTRULROOTNAME, COOKIENAME, REALROOTURL, WELCOMETITTLE, WELCOMEURL, LOGINURL, LOGINTYPE, LOGOUTURL, LOGOUTTYPE, USERPARAMNAME, PASSWORDPARAMNAME, LOGINSUCCESSTAG, TESTURL, SORTORDER, XYBZ, YXBZ, BASEURL, LOGINTIME, SCRIPT, SESSIONKEEP, SESSIONKEEPTYPE, SESSIONKEEPURL, UNIUSERTYPE, UNIUSERON, UNIUSERDATA)
  values ('ϵͳ����', 'ϵͳ����', null, null, null, null, null, null, null, 'U', '../entry/loginOut?type=ipc&purpose=LogInService&module=Entry', 'U', null, null, ':CONSOLE:', null, '00', 'Y', 'Y', '/adp', 'F', null, 'Y', 'U', '../index.htm', 'L', 'N', null);

commit;
