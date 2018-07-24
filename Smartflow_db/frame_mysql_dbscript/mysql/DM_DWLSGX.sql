-- Create table
create table DM_DWLSGX
(
  DWLSGX_DM VARCHAR(2) not null comment '单位隶属关系代码',
  DWLSGX_MC VARCHAR(16) not null comment '单位隶属关系名称',
  DWLSGX_SM VARCHAR(256) comment '单位隶属关系说明',
  XYBZ      CHAR(1) default 'Y' not null comment '选用标志',
  YXBZ      CHAR(1) default 'Y' not null comment '有效标志'
) comment '单位隶属关系代码';

  insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('10', '中央            ', '包括全国人大常委会、中共中央、国务院各部委及其所属机构，国务院各直属机构、办事机构及其所属机构', 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('20', '省              ', '包括自治区、直辖市', 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('40', '市、地区        ', '包括自治州、盟、省辖市、直辖市辖区（县）', 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('50', '县              ', '包括地(州、盟)辖市、省辖市辖区、自治县（旗）、旗、县级市', 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('60', '街道、镇、乡    ', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('61', '街道            ', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('62', '镇              ', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('63', '乡              ', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('70', '居民、村民委员会', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('71', '居民委员会      ', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('72', '村民委员会      ', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('80', '组              ', null, 'Y', 'Y');
insert into DM_DWLSGX (DWLSGX_DM, DWLSGX_MC, DWLSGX_SM, XYBZ, YXBZ)
values ('90', '其他            ', null, 'Y', 'Y');
commit;