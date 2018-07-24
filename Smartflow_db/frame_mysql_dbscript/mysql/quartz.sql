DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;  
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;  
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;  
DROP TABLE IF EXISTS QRTZ_LOCKS;  
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;  
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;  
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;  
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;  
DROP TABLE IF EXISTS QRTZ_TRIGGERS;  
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;  
DROP TABLE IF EXISTS QRTZ_CALENDARS;  
DROP TABLE IF EXISTS QX_TASKGROUP;
  
CREATE TABLE QRTZ_JOB_DETAILS(  
SCHED_NAME VARCHAR(120) NOT NULL,  
JOB_NAME VARCHAR(200) NOT NULL,  
JOB_GROUP VARCHAR(200) NOT NULL,  
DESCRIPTION VARCHAR(250) NULL,  
JOB_CLASS_NAME VARCHAR(250) NOT NULL,  
IS_DURABLE VARCHAR(1) NOT NULL,  
IS_NONCONCURRENT VARCHAR(1) NOT NULL,  
IS_UPDATE_DATA VARCHAR(1) NOT NULL,  
REQUESTS_RECOVERY VARCHAR(1) NOT NULL,  
JOB_DATA BLOB NULL,  
PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP))  
ENGINE=InnoDB;  
  
CREATE TABLE QRTZ_TRIGGERS (  
SCHED_NAME VARCHAR(120) NOT NULL,  
TRIGGER_NAME VARCHAR(200) NOT NULL,  
TRIGGER_GROUP VARCHAR(200) NOT NULL,  
JOB_NAME VARCHAR(200) NOT NULL,  
JOB_GROUP VARCHAR(200) NOT NULL,  
DESCRIPTION VARCHAR(250) NULL,  
NEXT_FIRE_TIME BIGINT(13) NULL,  
PREV_FIRE_TIME BIGINT(13) NULL,  
PRIORITY INTEGER NULL,  
TRIGGER_STATE VARCHAR(16) NOT NULL,  
TRIGGER_TYPE VARCHAR(8) NOT NULL,  
START_TIME BIGINT(13) NOT NULL,  
END_TIME BIGINT(13) NULL,  
CALENDAR_NAME VARCHAR(200) NULL,  
MISFIRE_INSTR SMALLINT(2) NULL,  
JOB_DATA BLOB NULL,  
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),  
FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)  
REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP))  
ENGINE=InnoDB;  
  
CREATE TABLE QRTZ_SIMPLE_TRIGGERS (  
SCHED_NAME VARCHAR(120) NOT NULL,  
TRIGGER_NAME VARCHAR(200) NOT NULL,  
TRIGGER_GROUP VARCHAR(200) NOT NULL,  
REPEAT_COUNT BIGINT(7) NOT NULL,  
REPEAT_INTERVAL BIGINT(12) NOT NULL,  
TIMES_TRIGGERED BIGINT(10) NOT NULL,  
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),  
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)  
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))  
ENGINE=InnoDB;  
  
CREATE TABLE QRTZ_CRON_TRIGGERS (  
SCHED_NAME VARCHAR(120) NOT NULL,  
TRIGGER_NAME VARCHAR(200) NOT NULL,  
TRIGGER_GROUP VARCHAR(200) NOT NULL,  
CRON_EXPRESSION VARCHAR(120) NOT NULL,  
TIME_ZONE_ID VARCHAR(80),  
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),  
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)  
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))  
ENGINE=InnoDB;  
  
CREATE TABLE QRTZ_SIMPROP_TRIGGERS  
  (            
    SCHED_NAME VARCHAR(120) NOT NULL,  
    TRIGGER_NAME VARCHAR(200) NOT NULL,  
    TRIGGER_GROUP VARCHAR(200) NOT NULL,  
    STR_PROP_1 VARCHAR(512) NULL,  
    STR_PROP_2 VARCHAR(512) NULL,  
    STR_PROP_3 VARCHAR(512) NULL,  
    INT_PROP_1 INT NULL,  
    INT_PROP_2 INT NULL,  
    LONG_PROP_1 BIGINT NULL,  
    LONG_PROP_2 BIGINT NULL,  
    DEC_PROP_1 NUMERIC(13,4) NULL,  
    DEC_PROP_2 NUMERIC(13,4) NULL,  
    BOOL_PROP_1 VARCHAR(1) NULL,  
    BOOL_PROP_2 VARCHAR(1) NULL,  
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),  
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)   
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))  
ENGINE=InnoDB;  
  
CREATE TABLE QRTZ_BLOB_TRIGGERS (  
SCHED_NAME VARCHAR(120) NOT NULL,  
TRIGGER_NAME VARCHAR(200) NOT NULL,  
TRIGGER_GROUP VARCHAR(200) NOT NULL,  
BLOB_DATA BLOB NULL,  
PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),  
INDEX (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP),  
FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)  
REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))  
ENGINE=InnoDB;  
  
CREATE TABLE QRTZ_CALENDARS (  
SCHED_NAME VARCHAR(120) NOT NULL,  
CALENDAR_NAME VARCHAR(200) NOT NULL,  
CALENDAR BLOB NOT NULL,  
PRIMARY KEY (SCHED_NAME,CALENDAR_NAME))  
ENGINE=InnoDB;  
  
CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (  
SCHED_NAME VARCHAR(120) NOT NULL,  
TRIGGER_GROUP VARCHAR(200) NOT NULL,  
PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP))  
ENGINE=InnoDB;  
  
CREATE TABLE QRTZ_FIRED_TRIGGERS (  
SCHED_NAME VARCHAR(120) NOT NULL,  
ENTRY_ID VARCHAR(95) NOT NULL,  
TRIGGER_NAME VARCHAR(200) NOT NULL,  
TRIGGER_GROUP VARCHAR(200) NOT NULL,  
INSTANCE_NAME VARCHAR(200) NOT NULL,  
FIRED_TIME BIGINT(13) NOT NULL,  
SCHED_TIME BIGINT(13) NOT NULL,  
PRIORITY INTEGER NOT NULL,  
STATE VARCHAR(16) NOT NULL,  
JOB_NAME VARCHAR(200) NULL,  
JOB_GROUP VARCHAR(200) NULL,  
IS_NONCONCURRENT VARCHAR(1) NULL,  
REQUESTS_RECOVERY VARCHAR(1) NULL,  
PRIMARY KEY (SCHED_NAME,ENTRY_ID))  
ENGINE=InnoDB;  
  
CREATE TABLE QRTZ_SCHEDULER_STATE (  
SCHED_NAME VARCHAR(120) NOT NULL,  
INSTANCE_NAME VARCHAR(200) NOT NULL,  
LAST_CHECKIN_TIME BIGINT(13) NOT NULL,  
CHECKIN_INTERVAL BIGINT(13) NOT NULL,  
PRIMARY KEY (SCHED_NAME,INSTANCE_NAME))  
ENGINE=InnoDB;  
  
CREATE TABLE QRTZ_LOCKS (  
SCHED_NAME VARCHAR(120) NOT NULL,  
LOCK_NAME VARCHAR(40) NOT NULL,  
PRIMARY KEY (SCHED_NAME,LOCK_NAME))  
ENGINE=InnoDB; 
 
CREATE TABLE QX_TASKGROUP 
(	GROUP_DM VARCHAR(20) NOT NULL , 
	GROUP_NAME VARCHAR(20), 
	SJ_GROUP_DM VARCHAR(20), 
	JD_ORDER INTEGER, 
	 PRIMARY KEY (GROUP_DM)
)
  ENGINE=InnoDB; 
  
CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY ON QRTZ_JOB_DETAILS(SCHED_NAME,REQUESTS_RECOVERY);  
CREATE INDEX IDX_QRTZ_J_GRP ON QRTZ_JOB_DETAILS(SCHED_NAME,JOB_GROUP);  
  
CREATE INDEX IDX_QRTZ_T_J ON QRTZ_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);  
CREATE INDEX IDX_QRTZ_T_JG ON QRTZ_TRIGGERS(SCHED_NAME,JOB_GROUP);  
CREATE INDEX IDX_QRTZ_T_C ON QRTZ_TRIGGERS(SCHED_NAME,CALENDAR_NAME);  
CREATE INDEX IDX_QRTZ_T_G ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);  
CREATE INDEX IDX_QRTZ_T_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE);  
CREATE INDEX IDX_QRTZ_T_N_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);  
CREATE INDEX IDX_QRTZ_T_N_G_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);  
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME ON QRTZ_TRIGGERS(SCHED_NAME,NEXT_FIRE_TIME);  
CREATE INDEX IDX_QRTZ_T_NFT_ST ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);  
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);  
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);  
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);  
  
CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME);  
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);  
CREATE INDEX IDX_QRTZ_FT_J_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);  
CREATE INDEX IDX_QRTZ_FT_JG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_GROUP);  
CREATE INDEX IDX_QRTZ_FT_T_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);  
CREATE INDEX IDX_QRTZ_FT_TG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);  
  
commit;   

insert into qx_gnmk (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('task_manager', '�������', '../security/quartz/taskManager/index.jsp', '05', '000000', 'N', 'Y', 'Y', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '�������');

insert into qx_gnmk (GNMK_DM, GNMK_HZMC, GNMK_LJMC, MKLX_DM, YWHJ_DM, CYBJ, GZL_BZ, CFDK, DKWZ, SHOWLEFT, SHOWTOP, SHOWINTREE, SYSTEMNAME, YXBZ, GNMK_BM)
values ('task_listener', '������', '../security/quartz/taskListen/index.jsp', '05', '000000', 'N', 'Y', 'Y', '0', 'Y', 'Y', 'Y', 'ϵͳ����', 'Y', '������');


insert into qx_gnmk_tree (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100003602827378324041', 'systemmanage', '������ȹ���', 'FFFFFFFFFFF', '01', 20, 'quartz');

insert into qx_gnmk_tree (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100003804144413170007', '100003602827378324041', '�������', 'task_manager', '02', 0, '�������');

insert into qx_gnmk_tree (JD_DM, FJD_DM, JD_MC, GNMK_DM, JDLX_DM, JD_ORDER, JD_BM)
values ('100003819772021267391', '100003602827378324041', '������', 'task_listener', '02', 1, '������');



insert into qx_gnmb_gnmk (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'FFFFFFFFFFF', '100003602827378324041', 'systemmanage', 'quartz', 20);

insert into qx_gnmb_gnmk (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'task_manager', '100003804144413170007', '100003602827378324041', '�������', 0);

insert into qx_gnmb_gnmk (GNMB_DM, GNMK_DM, JD_DM, FJD_DM, JD_MC, JD_ORDER)
values ('00000000001', 'task_listener', '100003819772021267391', '100003602827378324041', '������', 1);


insert into QX_TASKGROUP (GROUP_DM, GROUP_NAME, SJ_GROUP_DM, JD_ORDER)
values ('000000000000000', '�������б�', null, 1);

insert into QX_TASKGROUP (GROUP_DM, GROUP_NAME, SJ_GROUP_DM, JD_ORDER)
values ('000000000000001', 'taskgroup1', '000000000000000', 1);

insert into QX_TASKGROUP (GROUP_DM, GROUP_NAME, SJ_GROUP_DM, JD_ORDER)
values ('000000000000002', 'taskgroup2', '000000000000000', 2);
commit;