
-- 创建表: 行政区划代码
create table DM_XZQH
(
  XZQH_DM    VARCHAR(15) not null comment '行政区划代码',
  XZQH_MC    VARCHAR(80) comment '行政区划名称',
  XZQH_JC    VARCHAR(50) comment '行政区划简称',
  XZQH_QC    VARCHAR(512) comment '行政区划全称',
  JCDM       CHAR(1) comment '级次代码',
  JBDM       VARCHAR(15) comment '级别代码',
  SJ_XZQH_DM VARCHAR(15) comment '上级行政区划代码',
  XYBZ       CHAR(1) default 'Y' not null comment '选用标志',
  YXBZ       CHAR(1) default 'Y' not null comment '有效标志',
  DWLSGX_DM  VARCHAR(2) comment '单位隶属关系代码'
);
alter table DM_XZQH
  add constraint PK_DM_XZQH primary key (XZQH_DM);
create index IDX_DM_XZQH_JBDM on DM_XZQH (JBDM);
create index IDX_DM_XZQH_JCDM on DM_XZQH (JCDM);
create index IDX_DM_XZQH_SJ on DM_XZQH (SJ_XZQH_DM);



insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140824000000000', '稷山县', '稷山县', '稷山县', '3', '140824', '140800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140922000000000', '五台县', '五台县', '五台县', '3', '140922', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140829000000000', '平陆县', '平陆县', '平陆县', '3', '140829', '140800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140901000000000', '市辖区', '市辖区', '市辖区', '3', '140901', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140902000000000', '忻府区', '忻府区', '忻府区', '3', '140902', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140923000000000', '代县', '代县', '代县', '3', '140923', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140828000000000', '夏县', '夏县', '夏县', '3', '140828', '140800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140921000000000', '定襄县', '定襄县', '定襄县', '3', '140921', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140924000000000', '繁峙县', '繁峙县', '繁峙县', '3', '140924', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140827000000000', '垣曲县', '垣曲县', '垣曲县', '3', '140827', '140800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140881000000000', '永济市', '永济市', '永济市', '3', '140881', '140800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140882000000000', '河津市', '河津市', '河津市', '3', '140882', '140800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140925000000000', '宁武县', '宁武县', '宁武县', '3', '140925', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141027000000000', '浮山县', '浮山县', '浮山县', '3', '141027', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141026000000000', '安泽县', '安泽县', '安泽县', '3', '141026', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140928000000000', '五寨县', '五寨县', '五寨县', '3', '140928', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140929000000000', '岢岚县', '岢岚县', '岢岚县', '3', '140929', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140930000000000', '河曲县', '河曲县', '河曲县', '3', '140930', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141001000000000', '市辖区', '市辖区', '市辖区', '3', '141001', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141002000000000', '尧都区', '尧都区', '尧都区', '3', '141002', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141022000000000', '翼城县', '翼城县', '翼城县', '3', '141022', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141023000000000', '襄汾县', '襄汾县', '襄汾县', '3', '141023', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141024000000000', '洪洞县', '洪洞县', '洪洞县', '3', '141024', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140931000000000', '保德县', '保德县', '保德县', '3', '140931', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140926000000000', '静乐县', '静乐县', '静乐县', '3', '140926', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140932000000000', '偏关县', '偏关县', '偏关县', '3', '140932', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140981000000000', '原平市', '原平市', '原平市', '3', '140981', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140927000000000', '神池县', '神池县', '神池县', '3', '140927', '140900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141025000000000', '古县', '古县', '古县', '3', '141025', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141021000000000', '曲沃县', '曲沃县', '曲沃县', '3', '141021', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141029000000000', '乡宁县', '乡宁县', '乡宁县', '3', '141029', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141101000000000', '市辖区', '市辖区', '市辖区', '3', '141101', '141100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141102000000000', '离石区', '离石区', '离石区', '3', '141102', '141100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141082000000000', '霍州市', '霍州市', '霍州市', '3', '141082', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141123000000000', '兴县', '兴县', '兴县', '3', '141123', '141100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150301000000000', '市辖区', '市辖区', '市辖区', '3', '150301', '150300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150302000000000', '海勃湾区', '海勃湾区', '海勃湾区', '3', '150302', '150300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141030000000000', '大宁县', '大宁县', '大宁县', '3', '141030', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141033000000000', '蒲县', '蒲县', '蒲县', '3', '141033', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141034000000000', '汾西县', '汾西县', '汾西县', '3', '141034', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141122000000000', '交城县', '交城县', '交城县', '3', '141122', '141100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141126000000000', '石楼县', '石楼县', '石楼县', '3', '141126', '141100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141128000000000', '方山县', '方山县', '方山县', '3', '141128', '141100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141081000000000', '侯马市', '侯马市', '侯马市', '3', '141081', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141125000000000', '柳林县', '柳林县', '柳林县', '3', '141125', '141100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141028000000000', '吉县', '吉县', '吉县', '3', '141028', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141032000000000', '永和县', '永和县', '永和县', '3', '141032', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141031000000000', '隰县', '隰县', '隰县', '3', '141031', '141000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141124000000000', '临县', '临县', '临县', '3', '141124', '141100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141121000000000', '文水县', '文水县', '文水县', '3', '141121', '141100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141127000000000', '岚县', '岚县', '岚县', '3', '141127', '141100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150223000000000', '达尔罕茂明安联合旗', '达尔罕茂明安联合旗', '达尔罕茂明安联合旗', '3', '150223', '150200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150204000000000', '青山区', '青山区', '青山区', '3', '150204', '150200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150401000000000', '市辖区', '市辖区', '市辖区', '3', '150401', '150400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150402000000000', '红山区', '红山区', '红山区', '3', '150402', '150400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150403000000000', '元宝山区', '元宝山区', '元宝山区', '3', '150403', '150400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150104000000000', '玉泉区', '玉泉区', '玉泉区', '3', '150104', '150100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150121000000000', '土默特左旗', '土默特左旗', '土默特左旗', '3', '150121', '150100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150203000000000', '昆都仑区', '昆都仑区', '昆都仑区', '3', '150203', '150200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150221000000000', '土默特右旗', '土默特右旗', '土默特右旗', '3', '150221', '150200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150201000000000', '市辖区', '市辖区', '市辖区', '3', '150201', '150200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150202000000000', '东河区', '东河区', '东河区', '3', '150202', '150200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150101000000000', '市辖区', '市辖区', '市辖区', '3', '150101', '150100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150102000000000', '新城区', '新城区', '新城区', '3', '150102', '150100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150122000000000', '托克托县', '托克托县', '托克托县', '3', '150122', '150100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150124000000000', '清水河县', '清水河县', '清水河县', '3', '150124', '150100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150222000000000', '固阳县', '固阳县', '固阳县', '3', '150222', '150200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150205000000000', '石拐区', '石拐区', '石拐区', '3', '150205', '150200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141130000000000', '交口县', '交口县', '交口县', '3', '141130', '141100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150105000000000', '赛罕区', '赛罕区', '赛罕区', '3', '150105', '150100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150303000000000', '海南区', '海南区', '海南区', '3', '150303', '150300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141129000000000', '中阳县', '中阳县', '中阳县', '3', '141129', '141100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141182000000000', '汾阳市', '汾阳市', '汾阳市', '3', '141182', '141100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150206000000000', '白云矿区', '白云矿区', '白云矿区', '3', '150206', '150200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150207000000000', '九原区', '九原区', '九原区', '3', '150207', '150200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141181000000000', '孝义市', '孝义市', '孝义市', '3', '141181', '141100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150304000000000', '乌达区', '乌达区', '乌达区', '3', '150304', '150300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150404000000000', '松山区', '松山区', '松山区', '3', '150404', '150400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150103000000000', '回民区', '回民区', '回民区', '3', '150103', '150100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150123000000000', '和林格尔县', '和林格尔县', '和林格尔县', '3', '150123', '150100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150125000000000', '武川县', '武川县', '武川县', '3', '150125', '150100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150429000000000', '宁城县', '宁城县', '宁城县', '3', '150429', '150400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150424000000000', '林西县', '林西县', '林西县', '3', '150424', '150400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150430000000000', '敖汉旗', '敖汉旗', '敖汉旗', '3', '150430', '150400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150523000000000', '开鲁县', '开鲁县', '开鲁县', '3', '150523', '150500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150422000000000', '巴林左旗', '巴林左旗', '巴林左旗', '3', '150422', '150400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150421000000000', '阿鲁科尔沁旗', '阿鲁科尔沁旗', '阿鲁科尔沁旗', '3', '150421', '150400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150501000000000', '市辖区', '市辖区', '市辖区', '3', '150501', '150500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150502000000000', '科尔沁区', '科尔沁区', '科尔沁区', '3', '150502', '150500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150428000000000', '喀喇沁旗', '喀喇沁旗', '喀喇沁旗', '3', '150428', '150400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150426000000000', '翁牛特旗', '翁牛特旗', '翁牛特旗', '3', '150426', '150400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150521000000000', '科尔沁左翼中旗', '科尔沁左翼中旗', '科尔沁左翼中旗', '3', '150521', '150500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150522000000000', '科尔沁左翼后旗', '科尔沁左翼后旗', '科尔沁左翼后旗', '3', '150522', '150500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150423000000000', '巴林右旗', '巴林右旗', '巴林右旗', '3', '150423', '150400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150425000000000', '克什克腾旗', '克什克腾旗', '克什克腾旗', '3', '150425', '150400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150727000000000', '新巴尔虎右旗', '新巴尔虎右旗', '新巴尔虎右旗', '3', '150727', '150700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150724000000000', '鄂温克族自治旗', '鄂温克族自治旗', '鄂温克族自治旗', '3', '150724', '150700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150784000000000', '额尔古纳市', '额尔古纳市', '额尔古纳市', '3', '150784', '150700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150621000000000', '达拉特旗', '达拉特旗', '达拉特旗', '3', '150621', '150600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150701000000000', '市辖区', '市辖区', '市辖区', '3', '150701', '150700000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150702000000000', '海拉尔区', '海拉尔区', '海拉尔区', '3', '150702', '150700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150722000000000', '莫力达瓦达斡尔族自治旗', '莫力达瓦达斡尔族自治旗', '莫力达瓦达斡尔族自治旗', '3', '150722', '150700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150624000000000', '鄂托克旗', '鄂托克旗', '鄂托克旗', '3', '150624', '150600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150626000000000', '乌审旗', '乌审旗', '乌审旗', '3', '150626', '150600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150781000000000', '满洲里市', '满洲里市', '满洲里市', '3', '150781', '150700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150783000000000', '扎兰屯市', '扎兰屯市', '扎兰屯市', '3', '150783', '150700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150723000000000', '鄂伦春自治旗', '鄂伦春自治旗', '鄂伦春自治旗', '3', '150723', '150700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150725000000000', '陈巴尔虎旗', '陈巴尔虎旗', '陈巴尔虎旗', '3', '150725', '150700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150785000000000', '根河市', '根河市', '根河市', '3', '150785', '150700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150801000000000', '市辖区', '市辖区', '市辖区', '3', '150801', '150800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150802000000000', '临河区', '临河区', '临河区', '3', '150802', '150800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150581000000000', '霍林郭勒市', '霍林郭勒市', '霍林郭勒市', '3', '150581', '150500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150721000000000', '阿荣旗', '阿荣旗', '阿荣旗', '3', '150721', '150700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150625000000000', '杭锦旗', '杭锦旗', '杭锦旗', '3', '150625', '150600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150627000000000', '伊金霍洛旗', '伊金霍洛旗', '伊金霍洛旗', '3', '150627', '150600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150782000000000', '牙克石市', '牙克石市', '牙克石市', '3', '150782', '150700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150525000000000', '奈曼旗', '奈曼旗', '奈曼旗', '3', '150525', '150500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150526000000000', '扎鲁特旗', '扎鲁特旗', '扎鲁特旗', '3', '150526', '150500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150601000000000', '市辖区', '市辖区', '市辖区', '3', '150601', '150600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150602000000000', '东胜区', '东胜区', '东胜区', '3', '150602', '150600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150726000000000', '新巴尔虎左旗', '新巴尔虎左旗', '新巴尔虎左旗', '3', '150726', '150700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150524000000000', '库伦旗', '库伦旗', '库伦旗', '3', '150524', '150500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150622000000000', '准格尔旗', '准格尔旗', '准格尔旗', '3', '150622', '150600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150623000000000', '鄂托克前旗', '鄂托克前旗', '鄂托克前旗', '3', '150623', '150600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152223000000000', '扎赉特旗', '扎赉特旗', '扎赉特旗', '3', '152223', '152200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152224000000000', '突泉县', '突泉县', '突泉县', '3', '152224', '152200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152523000000000', '苏尼特左旗', '苏尼特左旗', '苏尼特左旗', '3', '152523', '152500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150822000000000', '磴口县', '磴口县', '磴口县', '3', '150822', '150800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150922000000000', '化德县', '化德县', '化德县', '3', '150922', '150900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150929000000000', '四子王旗', '四子王旗', '四子王旗', '3', '150929', '150900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152202000000000', '阿尔山市', '阿尔山市', '阿尔山市', '3', '152202', '152200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150927000000000', '察哈尔右翼中旗', '察哈尔右翼中旗', '察哈尔右翼中旗', '3', '150927', '150900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152221000000000', '科尔沁右翼前旗', '科尔沁右翼前旗', '科尔沁右翼前旗', '3', '152221', '152200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152201000000000', '乌兰浩特市', '乌兰浩特市', '乌兰浩特市', '3', '152201', '152200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152222000000000', '科尔沁右翼中旗', '科尔沁右翼中旗', '科尔沁右翼中旗', '3', '152222', '152200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152522000000000', '阿巴嘎旗', '阿巴嘎旗', '阿巴嘎旗', '3', '152522', '152500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150826000000000', '杭锦后旗', '杭锦后旗', '杭锦后旗', '3', '150826', '150800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150901000000000', '市辖区', '市辖区', '市辖区', '3', '150901', '150900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150902000000000', '集宁区', '集宁区', '集宁区', '3', '150902', '150900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150823000000000', '乌拉特前旗', '乌拉特前旗', '乌拉特前旗', '3', '150823', '150800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150924000000000', '兴和县', '兴和县', '兴和县', '3', '150924', '150900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150926000000000', '察哈尔右翼前旗', '察哈尔右翼前旗', '察哈尔右翼前旗', '3', '150926', '150900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152524000000000', '苏尼特右旗', '苏尼特右旗', '苏尼特右旗', '3', '152524', '152500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150824000000000', '乌拉特中旗', '乌拉特中旗', '乌拉特中旗', '3', '150824', '150800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150825000000000', '乌拉特后旗', '乌拉特后旗', '乌拉特后旗', '3', '150825', '150800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150821000000000', '五原县', '五原县', '五原县', '3', '150821', '150800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150981000000000', '丰镇市', '丰镇市', '丰镇市', '3', '150981', '150900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150921000000000', '卓资县', '卓资县', '卓资县', '3', '150921', '150900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150923000000000', '商都县', '商都县', '商都县', '3', '150923', '150900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150928000000000', '察哈尔右翼后旗', '察哈尔右翼后旗', '察哈尔右翼后旗', '3', '150928', '150900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152525000000000', '东乌珠穆沁旗', '东乌珠穆沁旗', '东乌珠穆沁旗', '3', '152525', '152500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152501000000000', '二连浩特市', '二连浩特市', '二连浩特市', '3', '152501', '152500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152502000000000', '锡林浩特市', '锡林浩特市', '锡林浩特市', '3', '152502', '152500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150925000000000', '凉城县', '凉城县', '凉城县', '3', '150925', '150900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210115000000000', '棋盘山开发区', '棋盘山开发区', '棋盘山开发区', '3', '210115', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210116000000000', '蒲河新城', '蒲河新城', '蒲河新城', '3', '210116', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210203000000000', '西岗区', '西岗区', '西岗区', '3', '210203', '210200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152530000000000', '正蓝旗', '正蓝旗', '正蓝旗', '3', '152530', '152500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210105000000000', '皇姑区', '皇姑区', '皇姑区', '3', '210105', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210204000000000', '沙河口区', '沙河口区', '沙河口区', '3', '210204', '210200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210211000000000', '甘井子区', '甘井子区', '甘井子区', '3', '210211', '210200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210113000000000', '新城子区', '新城子区', '新城子区', '3', '210113', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210117000000000', '浑南新区', '浑南新区', '浑南新区', '3', '210117', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210112000000000', '东陵区', '东陵区', '东陵区', '3', '210112', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210181000000000', '新民市', '新民市', '新民市', '3', '210181', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210122000000000', '辽中县', '辽中县', '辽中县', '3', '210122', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210124000000000', '法库县', '法库县', '法库县', '3', '210124', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152529000000000', '正镶白旗', '正镶白旗', '正镶白旗', '3', '152529', '152500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210104000000000', '大东区', '大东区', '大东区', '3', '210104', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152526000000000', '西乌珠穆沁旗', '西乌珠穆沁旗', '西乌珠穆沁旗', '3', '152526', '152500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152531000000000', '多伦县', '多伦县', '多伦县', '3', '152531', '152500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152922000000000', '阿拉善右旗', '阿拉善右旗', '阿拉善右旗', '3', '152922', '152900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210111000000000', '苏家屯区', '苏家屯区', '苏家屯区', '3', '210111', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210123000000000', '康平县', '康平县', '康平县', '3', '210123', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152528000000000', '镶黄旗', '镶黄旗', '镶黄旗', '3', '152528', '152500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210106000000000', '铁西区', '铁西区', '铁西区', '3', '210106', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210103000000000', '沈河区', '沈河区', '沈河区', '3', '210103', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210202000000000', '中山区', '中山区', '中山区', '3', '210202', '210200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210212000000000', '旅顺口区', '旅顺口区', '旅顺口区', '3', '210212', '210200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152527000000000', '太仆寺旗', '太仆寺旗', '太仆寺旗', '3', '152527', '152500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152921000000000', '阿拉善左旗', '阿拉善左旗', '阿拉善左旗', '3', '152921', '152900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152923000000000', '额济纳旗', '额济纳旗', '额济纳旗', '3', '152923', '152900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210102000000000', '和平区', '和平区', '和平区', '3', '210102', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210114000000000', '于洪区', '于洪区', '于洪区', '3', '210114', '210100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210422000000000', '新宾满族自治县', '新宾满族自治县', '新宾满族自治县', '3', '210422', '210400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210504000000000', '明山区', '明山区', '明山区', '3', '210504', '210500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210302000000000', '铁东区', '铁东区', '铁东区', '3', '210302', '210300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210403000000000', '东洲区', '东洲区', '东洲区', '3', '210403', '210400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210381000000000', '海城市', '海城市', '海城市', '3', '210381', '210300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210503000000000', '溪湖区', '溪湖区', '溪湖区', '3', '210503', '210500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210521000000000', '本溪满族自治县', '本溪满族自治县', '本溪满族自治县', '3', '210521', '210500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210215000000000', '大连金石滩国家旅游度假区', '大连金石滩国家旅游度假区', '大连金石滩国家旅游度假区', '3', '210215', '210200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('000000000000000', '全国', '全国', '全国', '0', '', '999999999999999', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110103000000000', '崇文区', '崇文区', '崇文区', '3', '110103', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110101000000000', '东城区', '东城区', '东城区', '3', '110101', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110102000000000', '西城区', '西城区', '西城区', '3', '110102', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110105000000000', '朝阳区', '朝阳区', '朝阳区', '3', '110105', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110104000000000', '宣武区', '宣武区', '宣武区', '3', '110104', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110106000000000', '丰台区', '丰台区', '丰台区', '3', '110106', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110108000000000', '海淀区', '海淀区', '海淀区', '3', '110108', '110100000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110107000000000', '石景山区', '石景山区', '石景山区', '3', '110107', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110109000000000', '门头沟区', '门头沟区', '门头沟区', '3', '110109', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110111000000000', '房山区', '房山区', '房山区', '3', '110111', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110112000000000', '通州区', '通州区', '通州区', '3', '110112', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110113000000000', '顺义区', '顺义区', '顺义区', '3', '110113', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110114000000000', '昌平区', '昌平区', '昌平区', '3', '110114', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110115000000000', '大兴区', '大兴区', '大兴区', '3', '110115', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110116000000000', '怀柔区', '怀柔区', '怀柔区', '3', '110116', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110117000000000', '平谷区', '平谷区', '平谷区', '3', '110117', '110100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110228000000000', '密云县', '密云县', '密云县', '3', '110228', '110200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120102000000000', '河东区', '河东区', '河东区', '3', '120102', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110229000000000', '延庆县', '延庆县', '延庆县', '3', '110229', '110200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120101000000000', '和平区', '和平区', '和平区', '3', '120101', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120103000000000', '河西区', '河西区', '河西区', '3', '120103', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120104000000000', '南开区', '南开区', '南开区', '3', '120104', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120106000000000', '红桥区', '红桥区', '红桥区', '3', '120106', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120105000000000', '河北区', '河北区', '河北区', '3', '120105', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120107000000000', '塘沽区', '塘沽区', '塘沽区', '3', '120107', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120108000000000', '汉沽区', '汉沽区', '汉沽区', '3', '120108', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120112000000000', '津南区', '津南区', '津南区', '3', '120112', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120110000000000', '东丽区', '东丽区', '东丽区', '3', '120110', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120109000000000', '大港区', '大港区', '大港区', '3', '120109', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120111000000000', '西青区', '西青区', '西青区', '3', '120111', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120113000000000', '北辰区', '北辰区', '北辰区', '3', '120113', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130121000000000', '井陉县', '井陉县', '井陉县', '3', '130121', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130104000000000', '桥西区', '桥西区', '桥西区', '3', '130104', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120223000000000', '静海县', '静海县', '静海县', '3', '120223', '120200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120221000000000', '宁河县', '宁河县', '宁河县', '3', '120221', '120200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130105000000000', '新华区', '新华区', '新华区', '3', '130105', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130102000000000', '长安区', '长安区', '长安区', '3', '130102', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130106000000000', '开发区', '开发区', '开发区', '3', '130106', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130107000000000', '井陉矿区', '井陉矿区', '井陉矿区', '3', '130107', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130108000000000', '裕华区', '裕华区', '裕华区', '3', '130108', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130123000000000', '正定县', '正定县', '正定县', '3', '130123', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130103000000000', '桥东区', '桥东区', '桥东区', '3', '130103', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120114000000000', '武清区', '武清区', '武清区', '3', '120114', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120225000000000', '蓟县', '蓟县', '蓟县', '3', '120225', '120200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120115000000000', '宝坻区', '宝坻区', '宝坻区', '3', '120115', '120100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130182000000000', '藁城市', '藁城市', '藁城市', '3', '130182', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130183000000000', '晋州市', '晋州市', '晋州市', '3', '130183', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130185000000000', '鹿泉市', '鹿泉市', '鹿泉市', '3', '130185', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130129000000000', '赞皇县', '赞皇县', '赞皇县', '3', '130129', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130132000000000', '元氏县', '元氏县', '元氏县', '3', '130132', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130203000000000', '路北区', '路北区', '路北区', '3', '130203', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130205000000000', '开平区', '开平区', '开平区', '3', '130205', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130126000000000', '灵寿县', '灵寿县', '灵寿县', '3', '130126', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130184000000000', '新乐市', '新乐市', '新乐市', '3', '130184', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130125000000000', '行唐县', '行唐县', '行唐县', '3', '130125', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130131000000000', '平山县', '平山县', '平山县', '3', '130131', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130127000000000', '高邑县', '高邑县', '高邑县', '3', '130127', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130181000000000', '辛集市', '辛集市', '辛集市', '3', '130181', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130133000000000', '赵县', '赵县', '赵县', '3', '130133', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130204000000000', '古冶区', '古冶区', '古冶区', '3', '130204', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130202000000000', '路南区', '路南区', '路南区', '3', '130202', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130207000000000', '丰南区', '丰南区', '丰南区', '3', '130207', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130124000000000', '栾城县', '栾城县', '栾城县', '3', '130124', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130128000000000', '深泽县', '深泽县', '深泽县', '3', '130128', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130130000000000', '无极县', '无极县', '无极县', '3', '130130', '130100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130283000000000', '迁安市', '迁安市', '迁安市', '3', '130283', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130208000000000', '丰润区', '丰润区', '丰润区', '3', '130208', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130224000000000', '滦南县', '滦南县', '滦南县', '3', '130224', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130225000000000', '乐亭县', '乐亭县', '乐亭县', '3', '130225', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130229000000000', '玉田县', '玉田县', '玉田县', '3', '130229', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130281000000000', '遵化市', '遵化市', '遵化市', '3', '130281', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130227000000000', '迁西县', '迁西县', '迁西县', '3', '130227', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130223000000000', '滦县', '滦县', '滦县', '3', '130223', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130230000000000', '唐海县', '唐海县', '唐海县', '3', '130230', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130406000000000', '峰峰矿区', '峰峰矿区', '峰峰矿区', '3', '130406', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130291000000000', '芦台经济技术开发区', '芦台经济技术开发区', '芦台经济技术开发区', '3', '130291', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130403000000000', '丛台区', '丛台区', '丛台区', '3', '130403', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130302000000000', '海港区', '海港区', '海港区', '3', '130302', '130300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130322000000000', '昌黎县', '昌黎县', '昌黎县', '3', '130322', '130300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130404000000000', '复兴区', '复兴区', '复兴区', '3', '130404', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130423000000000', '临漳县', '临漳县', '临漳县', '3', '130423', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130292000000000', '汉沽管理区', '汉沽管理区', '汉沽管理区', '3', '130292', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130293000000000', '高新技术开发区', '高新技术开发区', '高新技术开发区', '3', '130293', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130294000000000', '南堡开发区', '南堡开发区', '南堡开发区', '3', '130294', '130200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130425000000000', '大名县', '大名县', '大名县', '3', '130425', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130303000000000', '山海关区', '山海关区', '山海关区', '3', '130303', '130300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130325000000000', '秦皇岛市开发区', '秦皇岛市开发区', '秦皇岛市开发区', '3', '130325', '130300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130421000000000', '邯郸县', '邯郸县', '邯郸县', '3', '130421', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130323000000000', '抚宁县', '抚宁县', '抚宁县', '3', '130323', '130300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130304000000000', '北戴河区', '北戴河区', '北戴河区', '3', '130304', '130300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130321000000000', '青龙满族自治县', '青龙满族自治县', '青龙满族自治县', '3', '130321', '130300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130402000000000', '邯山区', '邯山区', '邯山区', '3', '130402', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130424000000000', '成安县', '成安县', '成安县', '3', '130424', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130324000000000', '卢龙县', '卢龙县', '卢龙县', '3', '130324', '130300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130427000000000', '磁县', '磁县', '磁县', '3', '130427', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130430000000000', '邱县', '邱县', '邱县', '3', '130430', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130432000000000', '广平县', '广平县', '广平县', '3', '130432', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130503000000000', '桥西区', '桥西区', '桥西区', '3', '130503', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130428000000000', '肥乡县', '肥乡县', '肥乡县', '3', '130428', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130435000000000', '曲周县', '曲周县', '曲周县', '3', '130435', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130429000000000', '永年县', '永年县', '永年县', '3', '130429', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130431000000000', '鸡泽县', '鸡泽县', '鸡泽县', '3', '130431', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130434000000000', '魏县', '魏县', '魏县', '3', '130434', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130504000000000', '高开区', '高开区', '高开区', '3', '130504', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130433000000000', '馆陶县', '馆陶县', '馆陶县', '3', '130433', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130482000000000', '马头生态工业城', '马头生态工业城', '马头生态工业城', '3', '130482', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130483000000000', '高开区', '高开区', '高开区', '3', '130483', '130400000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130502000000000', '桥东区', '桥东区', '桥东区', '3', '130502', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130505000000000', '大曹庄管理区', '大曹庄管理区', '大曹庄管理区', '3', '130505', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130521000000000', '邢台县', '邢台县', '邢台县', '3', '130521', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130426000000000', '涉县', '涉县', '涉县', '3', '130426', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130481000000000', '武安市', '武安市', '武安市', '3', '130481', '130400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130528000000000', '宁晋县', '宁晋县', '宁晋县', '3', '130528', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130523000000000', '内丘县', '内丘县', '内丘县', '3', '130523', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130524000000000', '柏乡县', '柏乡县', '柏乡县', '3', '130524', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130529000000000', '巨鹿县', '巨鹿县', '巨鹿县', '3', '130529', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130530000000000', '新河县', '新河县', '新河县', '3', '130530', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130581000000000', '南宫市', '南宫市', '南宫市', '3', '130581', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130534000000000', '清河县', '清河县', '清河县', '3', '130534', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130535000000000', '临西县', '临西县', '临西县', '3', '130535', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130602000000000', '新市区', '新市区', '新市区', '3', '130602', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130527000000000', '南和县', '南和县', '南和县', '3', '130527', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130533000000000', '威县', '威县', '威县', '3', '130533', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130532000000000', '平乡县', '平乡县', '平乡县', '3', '130532', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130522000000000', '临城县', '临城县', '临城县', '3', '130522', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130582000000000', '沙河市', '沙河市', '沙河市', '3', '130582', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130525000000000', '隆尧县', '隆尧县', '隆尧县', '3', '130525', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130526000000000', '任县', '任县', '任县', '3', '130526', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130531000000000', '广宗县', '广宗县', '广宗县', '3', '130531', '130500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130634000000000', '曲阳县', '曲阳县', '曲阳县', '3', '130634', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130637000000000', '博野县', '博野县', '博野县', '3', '130637', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130604000000000', '南市区', '南市区', '南市区', '3', '130604', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130625000000000', '徐水县民政局', '徐水县民政局', '徐水县民政局', '3', '130625', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130622000000000', '清苑县', '清苑县', '清苑县', '3', '130622', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130626000000000', '定兴县', '定兴县', '定兴县', '3', '130626', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130628000000000', '高阳县', '高阳县', '高阳县', '3', '130628', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130635000000000', '蠡县', '蠡县', '蠡县', '3', '130635', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130638000000000', '雄县', '雄县', '雄县', '3', '130638', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130621000000000', '满城县', '满城县', '满城县', '3', '130621', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130624000000000', '阜平县', '阜平县', '阜平县', '3', '130624', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130629000000000', '容城县', '容城县', '容城县', '3', '130629', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130633000000000', '易县', '易县', '易县', '3', '130633', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130632000000000', '安新县', '安新县', '安新县', '3', '130632', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130623000000000', '涞水县', '涞水县', '涞水县', '3', '130623', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130603000000000', '北市区', '北市区', '北市区', '3', '130603', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130630000000000', '涞源县', '涞源县', '涞源县', '3', '130630', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130631000000000', '望都县', '望都县', '望都县', '3', '130631', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130627000000000', '唐县', '唐县', '唐县', '3', '130627', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130636000000000', '顺平县', '顺平县', '顺平县', '3', '130636', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130725000000000', '尚义县', '尚义县', '尚义县', '3', '130725', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130682000000000', '定州市汇总', '定州市汇总', '定州市汇总', '3', '130682', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130702000000000', '桥东区', '桥东区', '桥东区', '3', '130702', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130724000000000', '沽源县', '沽源县', '沽源县', '3', '130724', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130727000000000', '阳原县', '阳原县', '阳原县', '3', '130727', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130728000000000', '怀安县', '怀安县', '怀安县', '3', '130728', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130706000000000', '下花园区', '下花园区', '下花园区', '3', '130706', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130703000000000', '桥西区', '桥西区', '桥西区', '3', '130703', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130726000000000', '蔚县', '蔚县', '蔚县', '3', '130726', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130681000000000', '涿州市', '涿州市', '涿州市', '3', '130681', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130721000000000', '宣化县', '宣化县', '宣化县', '3', '130721', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130684000000000', '高碑店市', '高碑店市', '高碑店市', '3', '130684', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130705000000000', '宣化区', '宣化区', '宣化区', '3', '130705', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130722000000000', '张北县', '张北县', '张北县', '3', '130722', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130683000000000', '安国市', '安国市', '安国市', '3', '130683', '130600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130723000000000', '康保县', '康保县', '康保县', '3', '130723', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130826000000000', '丰宁满族自治县', '丰宁满族自治县', '丰宁满族自治县', '3', '130826', '130800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130734000000000', '察北管理区', '察北管理区', '察北管理区', '3', '130734', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130735000000000', '塞北管理区', '塞北管理区', '塞北管理区', '3', '130735', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130736000000000', '高新区', '高新区', '高新区', '3', '130736', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130827000000000', '宽城满族自治县', '宽城满族自治县', '宽城满族自治县', '3', '130827', '130800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130802000000000', '双桥区', '双桥区', '双桥区', '3', '130802', '130800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130823000000000', '平泉县', '平泉县', '平泉县', '3', '130823', '130800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130733000000000', '崇礼县', '崇礼县', '崇礼县', '3', '130733', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130822000000000', '兴仑', '兴仑', '兴仑', '3', '130822', '130800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130729000000000', '万全县', '万全县', '万全县', '3', '130729', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130730000000000', '怀来县', '怀来县', '怀来县', '3', '130730', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130803000000000', '双滦区', '双滦区', '双滦区', '3', '130803', '130800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130825000000000', '隆化县', '隆化县', '隆化县', '3', '130825', '130800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130902000000000', '新华区', '新华区', '新华区', '3', '130902', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130903000000000', '运河区', '运河区', '运河区', '3', '130903', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130731000000000', '涿鹿县', '涿鹿县', '涿鹿县', '3', '130731', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130732000000000', '赤城县', '赤城县', '赤城县', '3', '130732', '130700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130804000000000', '鹰手营子矿区', '鹰手营子矿区', '鹰手营子矿区', '3', '130804', '130800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130824000000000', '滦平县', '滦平县', '滦平县', '3', '130824', '130800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130821000000000', '承德县', '承德县', '承德县', '3', '130821', '130800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130828000000000', '围场满族蒙古族自治县', '围场满族蒙古族自治县', '围场满族蒙古族自治县', '3', '130828', '130800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130981000000000', '泊头市', '泊头市', '泊头市', '3', '130981', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130924000000000', '海兴县', '海兴县', '海兴县', '3', '130924', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130928000000000', '吴桥县', '吴桥县', '吴桥县', '3', '130928', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130927000000000', '南皮县', '南皮县', '南皮县', '3', '130927', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131022000000000', '固安县', '固安县', '固安县', '3', '131022', '131000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130982000000000', '任丘市', '任丘市', '任丘市', '3', '130982', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130923000000000', '东光县', '东光县', '东光县', '3', '130923', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130926000000000', '肃宁县', '肃宁县', '肃宁县', '3', '130926', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130930000000000', '孟村回族自治县', '孟村回族自治县', '孟村回族自治县', '3', '130930', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130921000000000', '沧县', '沧县', '沧县', '3', '130921', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130922000000000', '青县', '青县', '青县', '3', '130922', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130929000000000', '献县', '献县', '献县', '3', '130929', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130925000000000', '盐山县', '盐山县', '盐山县', '3', '130925', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131028000000000', '大厂回族自治县', '大厂回族自治县', '大厂回族自治县', '3', '131028', '131000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130984000000000', '河间市', '河间市', '河间市', '3', '130984', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131081000000000', '霸州市', '霸州市', '霸州市', '3', '131081', '131000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131102000000000', '桃城区', '桃城区', '桃城区', '3', '131102', '131100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130985000000000', '中捷农场', '中捷农场', '中捷农场', '3', '130985', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130986000000000', '南大岗农场', '南大岗农场', '南大岗农场', '3', '130986', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131002000000000', '安次区', '安次区', '安次区', '3', '131002', '131000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131026000000000', '文安县', '文安县', '文安县', '3', '131026', '131000000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131023000000000', '永清县', '永清县', '永清县', '3', '131023', '131000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131082000000000', '三河市', '三河市', '三河市', '3', '131082', '131000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131003000000000', '广阳区', '广阳区', '广阳区', '3', '131003', '131000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130983000000000', '黄骅市', '黄骅市', '黄骅市', '3', '130983', '130900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131024000000000', '香河县', '香河县', '香河县', '3', '131024', '131000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131025000000000', '大城县', '大城县', '大城县', '3', '131025', '131000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131128000000000', '阜城县', '阜城县', '阜城县', '3', '131128', '131100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131182000000000', '深州市', '深州市', '深州市', '3', '131182', '131100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131122000000000', '武邑县', '武邑县', '武邑县', '3', '131122', '131100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131126000000000', '故城县', '故城县', '故城县', '3', '131126', '131100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131125000000000', '安平县', '安平县', '安平县', '3', '131125', '131100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131103000000000', '高新区', '高新区', '高新区', '3', '131103', '131100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131121000000000', '枣强县', '枣强县', '枣强县', '3', '131121', '131100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131124000000000', '饶阳县', '饶阳县', '饶阳县', '3', '131124', '131100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131123000000000', '武强县', '武强县', '武强县', '3', '131123', '131100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131181000000000', '冀州市', '冀州市', '冀州市', '3', '131181', '131100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131127000000000', '景县', '景县', '景县', '3', '131127', '131100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140223000000000', '广灵县', '广灵县', '广灵县', '3', '140223', '140200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140101000000000', '市辖区', '市辖区', '市辖区', '3', '140101', '140100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140105000000000', '小店区', '小店区', '小店区', '3', '140105', '140100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140107000000000', '杏花岭区', '杏花岭区', '杏花岭区', '3', '140107', '140100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140121000000000', '清徐县', '清徐县', '清徐县', '3', '140121', '140100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140212000000000', '新荣区', '新荣区', '新荣区', '3', '140212', '140200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140181000000000', '古交市', '古交市', '古交市', '3', '140181', '140100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140226000000000', '左云县', '左云县', '左云县', '3', '140226', '140200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140109000000000', '万柏林区', '万柏林区', '万柏林区', '3', '140109', '140100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140106000000000', '迎泽区', '迎泽区', '迎泽区', '3', '140106', '140100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140211000000000', '南郊区', '南郊区', '南郊区', '3', '140211', '140200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140203000000000', '矿区', '矿区', '矿区', '3', '140203', '140200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140224000000000', '灵丘县', '灵丘县', '灵丘县', '3', '140224', '140200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140123000000000', '娄烦县', '娄烦县', '娄烦县', '3', '140123', '140100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140110000000000', '晋源区', '晋源区', '晋源区', '3', '140110', '140100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140225000000000', '浑源县', '浑源县', '浑源县', '3', '140225', '140200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140122000000000', '阳曲县', '阳曲县', '阳曲县', '3', '140122', '140100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140221000000000', '阳高县', '阳高县', '阳高县', '3', '140221', '140200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140222000000000', '天镇县', '天镇县', '天镇县', '3', '140222', '140200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140201000000000', '市辖区', '市辖区', '市辖区', '3', '140201', '140200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140202000000000', '城区', '城区', '城区', '3', '140202', '140200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140108000000000', '尖草坪区', '尖草坪区', '尖草坪区', '3', '140108', '140100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140428000000000', '长子县', '长子县', '长子县', '3', '140428', '140400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140401000000000', '市辖区', '市辖区', '市辖区', '3', '140401', '140400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140402000000000', '城区', '城区', '城区', '3', '140402', '140400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140429000000000', '武乡县', '武乡县', '武乡县', '3', '140429', '140400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140303000000000', '矿区', '矿区', '矿区', '3', '140303', '140300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140430000000000', '沁县', '沁县', '沁县', '3', '140430', '140400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140431000000000', '沁源县', '沁源县', '沁源县', '3', '140431', '140400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140322000000000', '盂县', '盂县', '盂县', '3', '140322', '140300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140301000000000', '市辖区', '市辖区', '市辖区', '3', '140301', '140300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140302000000000', '城区', '城区', '城区', '3', '140302', '140300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140411000000000', '郊区', '郊区', '郊区', '3', '140411', '140400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140227000000000', '大同县', '大同县', '大同县', '3', '140227', '140200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140321000000000', '平定县', '平定县', '平定县', '3', '140321', '140300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140426000000000', '黎城县', '黎城县', '黎城县', '3', '140426', '140400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140427000000000', '壶关县', '壶关县', '壶关县', '3', '140427', '140400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140311000000000', '郊区', '郊区', '郊区', '3', '140311', '140300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140421000000000', '长治县', '长治县', '长治县', '3', '140421', '140400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140423000000000', '襄垣县', '襄垣县', '襄垣县', '3', '140423', '140400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140424000000000', '屯留县', '屯留县', '屯留县', '3', '140424', '140400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140425000000000', '平顺县', '平顺县', '平顺县', '3', '140425', '140400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140525000000000', '泽州县', '泽州县', '泽州县', '3', '140525', '140500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140581000000000', '高平市', '高平市', '高平市', '3', '140581', '140500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140623000000000', '右玉县', '右玉县', '右玉县', '3', '140623', '140600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140624000000000', '怀仁县', '怀仁县', '怀仁县', '3', '140624', '140600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140522000000000', '阳城县', '阳城县', '阳城县', '3', '140522', '140500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140621000000000', '山阴县', '山阴县', '山阴县', '3', '140621', '140600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140622000000000', '应县', '应县', '应县', '3', '140622', '140600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140481000000000', '潞城市', '潞城市', '潞城市', '3', '140481', '140400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140501000000000', '市辖区', '市辖区', '市辖区', '3', '140501', '140500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140502000000000', '城区', '城区', '城区', '3', '140502', '140500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140601000000000', '市辖区', '市辖区', '市辖区', '3', '140601', '140600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140602000000000', '朔城区', '朔城区', '朔城区', '3', '140602', '140600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140701000000000', '市辖区', '市辖区', '市辖区', '3', '140701', '140700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140702000000000', '榆次区', '榆次区', '榆次区', '3', '140702', '140700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140521000000000', '沁水县', '沁水县', '沁水县', '3', '140521', '140500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140524000000000', '陵川县', '陵川县', '陵川县', '3', '140524', '140500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140603000000000', '平鲁区', '平鲁区', '平鲁区', '3', '140603', '140600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140821000000000', '临猗县', '临猗县', '临猗县', '3', '140821', '140800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140726000000000', '太谷县', '太谷县', '太谷县', '3', '140726', '140700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140822000000000', '万荣县', '万荣县', '万荣县', '3', '140822', '140800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140721000000000', '榆社县', '榆社县', '榆社县', '3', '140721', '140700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140722000000000', '左权县', '左权县', '左权县', '3', '140722', '140700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140725000000000', '寿阳县', '寿阳县', '寿阳县', '3', '140725', '140700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140728000000000', '平遥县', '平遥县', '平遥县', '3', '140728', '140700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140729000000000', '灵石县', '灵石县', '灵石县', '3', '140729', '140700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140781000000000', '介休市', '介休市', '介休市', '3', '140781', '140700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140823000000000', '闻喜县', '闻喜县', '闻喜县', '3', '140823', '140800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140723000000000', '和顺县', '和顺县', '和顺县', '3', '140723', '140700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140724000000000', '昔阳县', '昔阳县', '昔阳县', '3', '140724', '140700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140727000000000', '祁县', '祁县', '祁县', '3', '140727', '140700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140801000000000', '市辖区', '市辖区', '市辖区', '3', '140801', '140800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140802000000000', '盐湖区', '盐湖区', '盐湖区', '3', '140802', '140800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140826000000000', '绛县', '绛县', '绛县', '3', '140826', '140800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140825000000000', '新绛县', '新绛县', '新绛县', '3', '140825', '140800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140830000000000', '芮城县', '芮城县', '芮城县', '3', '140830', '140800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321182000000000', '扬中市', '扬中市', '扬中市', '3', '321182', '321100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321284000000000', '姜堰市', '姜堰市', '姜堰市', '3', '321284', '321200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321282000000000', '靖江市', '靖江市', '靖江市', '3', '321282', '321200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321003000000000', '邗江区', '邗江区', '邗江区', '3', '321003', '321000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321302000000000', '宿城区', '宿城区', '宿城区', '3', '321302', '321300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320982000000000', '大丰市', '大丰市', '大丰市', '3', '320982', '320900000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321181000000000', '丹阳市', '丹阳市', '丹阳市', '3', '321181', '321100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321101000000000', '市辖区', '市辖区', '市辖区', '3', '321101', '321100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321102000000000', '京口区', '京口区', '京口区', '3', '321102', '321100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321001000000000', '市辖区', '市辖区', '市辖区', '3', '321001', '321000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321002000000000', '广陵区', '广陵区', '广陵区', '3', '321002', '321000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321011000000000', '郊区', '郊区', '郊区', '3', '321011', '321000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321023000000000', '宝应县　', '宝应县　', '宝应县　', '3', '321023', '321000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321081000000000', '仪征市', '仪征市', '仪征市', '3', '321081', '321000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321203000000000', '高港区', '高港区', '高港区', '3', '321203', '321200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321111000000000', '润州区', '润州区', '润州区', '3', '321111', '321100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321183000000000', '句容市', '句容市', '句容市', '3', '321183', '321100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321311000000000', '宿豫区', '宿豫区', '宿豫区', '3', '321311', '321300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321281000000000', '兴化市', '兴化市', '兴化市', '3', '321281', '321200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321112000000000', '丹徒区', '丹徒区', '丹徒区', '3', '321112', '321100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330104000000000', '江干区', '江干区', '江干区', '3', '330104', '330100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321324000000000', '泗洪县', '泗洪县', '泗洪县', '3', '321324', '321300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330108000000000', '滨江区', '滨江区', '滨江区', '3', '330108', '330100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330105000000000', '拱墅区', '拱墅区', '拱墅区', '3', '330105', '330100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330183000000000', '富阳市', '富阳市', '富阳市', '3', '330183', '330100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321323000000000', '泗阳县', '泗阳县', '泗阳县', '3', '321323', '321300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330109000000000', '萧山区', '萧山区', '萧山区', '3', '330109', '330100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330102000000000', '上城区', '上城区', '上城区', '3', '330102', '330100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330106000000000', '西湖区', '西湖区', '西湖区', '3', '330106', '330100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330182000000000', '建德市', '建德市', '建德市', '3', '330182', '330100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330110000000000', '余杭区', '余杭区', '余杭区', '3', '330110', '330100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321322000000000', '沭阳县', '沭阳县', '沭阳县', '3', '321322', '321300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330127000000000', '淳安县', '淳安县', '淳安县', '3', '330127', '330100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330122000000000', '桐庐县', '桐庐县', '桐庐县', '3', '330122', '330100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330103000000000', '下城区', '下城区', '下城区', '3', '330103', '330100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330302000000000', '鹿城区', '鹿城区', '鹿城区', '3', '330302', '330300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330283000000000', '奉化市', '奉化市', '奉化市', '3', '330283', '330200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330204000000000', '江东区', '江东区', '江东区', '3', '330204', '330200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330205000000000', '江北区', '江北区', '江北区', '3', '330205', '330200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330226000000000', '宁海县', '宁海县', '宁海县', '3', '330226', '330200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330281000000000', '余姚市', '余姚市', '余姚市', '3', '330281', '330200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330185000000000', '临安市', '临安市', '临安市', '3', '330185', '330100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330304000000000', '瓯海区', '瓯海区', '瓯海区', '3', '330304', '330300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330206000000000', '北仑区', '北仑区', '北仑区', '3', '330206', '330200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330211000000000', '镇海区', '镇海区', '镇海区', '3', '330211', '330200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330212000000000', '鄞州区', '鄞州区', '鄞州区', '3', '330212', '330200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330282000000000', '慈溪市', '慈溪市', '慈溪市', '3', '330282', '330200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330303000000000', '龙湾区', '龙湾区', '龙湾区', '3', '330303', '330300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330203000000000', '海曙区', '海曙区', '海曙区', '3', '330203', '330200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330225000000000', '象山县', '象山县', '象山县', '3', '330225', '330200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330381000000000', '瑞安市', '瑞安市', '瑞安市', '3', '330381', '330300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330326000000000', '平阳县', '平阳县', '平阳县', '3', '330326', '330300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330329000000000', '泰顺县', '泰顺县', '泰顺县', '3', '330329', '330300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330322000000000', '洞头县', '洞头县', '洞头县', '3', '330322', '330300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330324000000000', '永嘉县', '永嘉县', '永嘉县', '3', '330324', '330300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330328000000000', '文成县', '文成县', '文成县', '3', '330328', '330300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330327000000000', '苍南县', '苍南县', '苍南县', '3', '330327', '330300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330382000000000', '乐清市', '乐清市', '乐清市', '3', '330382', '330300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330482000000000', '平湖市', '平湖市', '平湖市', '3', '330482', '330400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330602000000000', '绍兴市越城区', '绍兴市越城区', '绍兴市越城区', '3', '330602', '330600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330522000000000', '长兴县', '长兴县', '长兴县', '3', '330522', '330500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330523000000000', '安吉县', '安吉县', '安吉县', '3', '330523', '330500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330401000000000', '经济开发区', '经济开发区', '经济开发区', '3', '330401', '330400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330421000000000', '嘉善县', '嘉善县', '嘉善县', '3', '330421', '330400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330402000000000', '秀城区', '秀城区', '秀城区', '3', '330402', '330400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330481000000000', '海宁市', '海宁市', '海宁市', '3', '330481', '330400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330521000000000', '德清县', '德清县', '德清县', '3', '330521', '330500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330621000000000', '绍兴县', '绍兴县', '绍兴县', '3', '330621', '330600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330624000000000', '新昌县', '新昌县', '新昌县', '3', '330624', '330600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330503000000000', '南浔区', '南浔区', '南浔区', '3', '330503', '330500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330502000000000', '吴兴区', '吴兴区', '吴兴区', '3', '330502', '330500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330424000000000', '海盐县', '海盐县', '海盐县', '3', '330424', '330400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330411000000000', '秀洲区', '秀洲区', '秀洲区', '3', '330411', '330400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330483000000000', '桐乡市', '桐乡市', '桐乡市', '3', '330483', '330400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330726000000000', '浦江县', '浦江县', '浦江县', '3', '330726', '330700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330702000000000', '婺城区', '婺城区', '婺城区', '3', '330702', '330700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330703000000000', '金东区', '金东区', '金东区', '3', '330703', '330700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330727000000000', '磐安县', '磐安县', '磐安县', '3', '330727', '330700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330723000000000', '武义县', '武义县', '武义县', '3', '330723', '330700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330681000000000', '诸暨市', '诸暨市', '诸暨市', '3', '330681', '330600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330682000000000', '上虞市', '上虞市', '上虞市', '3', '330682', '330600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330683000000000', '嵊州市', '嵊州市', '嵊州市', '3', '330683', '330600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330881000000000', '江山市', '江山市', '江山市', '3', '330881', '330800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330822000000000', '常山县', '常山县', '常山县', '3', '330822', '330800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330783000000000', '东阳市', '东阳市', '东阳市', '3', '330783', '330700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330803000000000', '衢江区', '衢江区', '衢江区', '3', '330803', '330800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330802000000000', '柯城区', '柯城区', '柯城区', '3', '330802', '330800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330824000000000', '开化县', '开化县', '开化县', '3', '330824', '330800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330781000000000', '兰溪市', '兰溪市', '兰溪市', '3', '330781', '330700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330784000000000', '永康市', '永康市', '永康市', '3', '330784', '330700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330782000000000', '义乌市', '义乌市', '义乌市', '3', '330782', '330700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330825000000000', '龙游县', '龙游县', '龙游县', '3', '330825', '330800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331023000000000', '天台县', '天台县', '天台县', '3', '331023', '331000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331002000000000', '椒江区', '椒江区', '椒江区', '3', '331002', '331000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331022000000000', '三门县', '三门县', '三门县', '3', '331022', '331000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331021000000000', '玉环县', '玉环县', '玉环县', '3', '331021', '331000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331024000000000', '仙居县', '仙居县', '仙居县', '3', '331024', '331000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331004000000000', '路桥区', '路桥区', '路桥区', '3', '331004', '331000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330922000000000', '嵊泗县', '嵊泗县', '嵊泗县', '3', '330922', '330900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330902000000000', '定海区', '定海区', '定海区', '3', '330902', '330900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331003000000000', '黄岩区', '黄岩区', '黄岩区', '3', '331003', '331000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330903000000000', '普陀区', '普陀区', '普陀区', '3', '330903', '330900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331081000000000', '温岭市', '温岭市', '温岭市', '3', '331081', '331000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330921000000000', '岱山县', '岱山县', '岱山县', '3', '330921', '330900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331101000000000', '市辖区', '市辖区', '市辖区', '3', '331101', '331100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331102000000000', '莲都区', '莲都区', '莲都区', '3', '331102', '331100000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331123000000000', '遂昌县', '遂昌县', '遂昌县', '3', '331123', '331100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331127000000000', '景宁县', '景宁县', '景宁县', '3', '331127', '331100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331181000000000', '龙泉市', '龙泉市', '龙泉市', '3', '331181', '331100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331126000000000', '庆元县', '庆元县', '庆元县', '3', '331126', '331100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331124000000000', '松阳县', '松阳县', '松阳县', '3', '331124', '331100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331121000000000', '青田县', '青田县', '青田县', '3', '331121', '331100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331125000000000', '云和县', '云和县', '云和县', '3', '331125', '331100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331082000000000', '临海市', '临海市', '临海市', '3', '331082', '331000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331122000000000', '缙云县', '缙云县', '缙云县', '3', '331122', '331100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340521000000000', '当涂县', '当涂县', '当涂县', '3', '340521', '340500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340105000000000', '经济技术开发区', '经济技术开发区', '经济技术开发区', '3', '340105', '340100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340101000000000', '市辖区', '市辖区', '市辖区', '3', '340101', '340100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340102000000000', '瑶海区', '瑶海区', '瑶海区', '3', '340102', '340100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340106000000000', '高新技术开发区', '高新技术开发区', '高新技术开发区', '3', '340106', '340100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340107000000000', '新站综合开发试验区', '新站综合开发试验区', '新站综合开发试验区', '3', '340107', '340100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340108000000000', '政务区社会事业管理处', '政务区社会事业管理处', '政务区社会事业管理处', '3', '340108', '340100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340111000000000', '包河区', '包河区', '包河区', '3', '340111', '340100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340122000000000', '肥西县', '肥西县', '肥西县', '3', '340122', '340100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340207000000000', '鸠江区', '鸠江区', '鸠江区', '3', '340207', '340200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340208000000000', '三山区', '三山区', '三山区', '3', '340208', '340200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340305000000000', '蚌埠市经济开发区', '蚌埠市经济开发区', '蚌埠市经济开发区', '3', '340305', '340300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340306000000000', '蚌埠市高新技术开发区', '蚌埠市高新技术开发区', '蚌埠市高新技术开发区', '3', '340306', '340300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340311000000000', '淮上区', '淮上区', '淮上区', '3', '340311', '340300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340604000000000', '烈山区', '烈山区', '烈山区', '3', '340604', '340600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340502000000000', '金家庄区', '金家庄区', '金家庄区', '3', '340502', '340500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340702000000000', '铜官山区', '铜官山区', '铜官山区', '3', '340702', '340700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340404000000000', '谢家集区', '谢家集区', '谢家集区', '3', '340404', '340400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340123000000000', '肥东县', '肥东县', '肥东县', '3', '340123', '340100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340321000000000', '怀远县', '怀远县', '怀远县', '3', '340321', '340300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340221000000000', '芜湖县', '芜湖县', '芜湖县', '3', '340221', '340200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340222000000000', '繁昌县', '繁昌县', '繁昌县', '3', '340222', '340200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340223000000000', '南陵县', '南陵县', '南陵县', '3', '340223', '340200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340302000000000', '龙子湖区', '龙子湖区', '龙子湖区', '3', '340302', '340300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340503000000000', '花山区', '花山区', '花山区', '3', '340503', '340500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340621000000000', '濯县', '濯县', '濯县', '3', '340621', '340600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340421000000000', '凤台县', '凤台县', '凤台县', '3', '340421', '340400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340103000000000', '庐阳区', '庐阳区', '庐阳区', '3', '340103', '340100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340323000000000', '固镇县', '固镇县', '固镇县', '3', '340323', '340300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340402000000000', '大通区', '大通区', '大通区', '3', '340402', '340400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340602000000000', '杜集区', '杜集区', '杜集区', '3', '340602', '340600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340405000000000', '八公山区', '八公山区', '八公山区', '3', '340405', '340400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340406000000000', '潘集区', '潘集区', '潘集区', '3', '340406', '340400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340703000000000', '狮子山区', '狮子山区', '狮子山区', '3', '340703', '340700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340202000000000', '镜湖区', '镜湖区', '镜湖区', '3', '340202', '340200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340303000000000', '蚌山区', '蚌山区', '蚌山区', '3', '340303', '340300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340504000000000', '雨山区', '雨山区', '雨山区', '3', '340504', '340500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340711000000000', '郊区', '郊区', '郊区', '3', '340711', '340700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340104000000000', '蜀山区', '蜀山区', '蜀山区', '3', '340104', '340100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340121000000000', '长丰县', '长丰县', '长丰县', '3', '340121', '340100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340322000000000', '五河县', '五河县', '五河县', '3', '340322', '340300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340403000000000', '田家庵区', '田家庵区', '田家庵区', '3', '340403', '340400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340203000000000', '弋江区', '弋江区', '弋江区', '3', '340203', '340200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340304000000000', '禹会区', '禹会区', '禹会区', '3', '340304', '340300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340603000000000', '相山区', '相山区', '相山区', '3', '340603', '340600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341124000000000', '全椒县', '全椒县', '全椒县', '3', '341124', '341100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341122000000000', '来安县', '来安县', '来安县', '3', '341122', '341100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341126000000000', '凤阳县', '凤阳县', '凤阳县', '3', '341126', '341100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340804000000000', '宜秀区', '宜秀区', '宜秀区', '3', '340804', '340800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340828000000000', '岳西县', '岳西县', '岳西县', '3', '340828', '340800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341021000000000', '歙县', '歙县', '歙县', '3', '341021', '341000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341002000000000', '屯溪区', '屯溪区', '屯溪区', '3', '341002', '341000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341102000000000', '琅琊区', '琅琊区', '琅琊区', '3', '341102', '341100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340822000000000', '怀宁县', '怀宁县', '怀宁县', '3', '340822', '340800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340721000000000', '铜陵县', '铜陵县', '铜陵县', '3', '340721', '340700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341022000000000', '休宁县', '休宁县', '休宁县', '3', '341022', '341000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341003000000000', '黄山区', '黄山区', '黄山区', '3', '341003', '341000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341023000000000', '黟县', '黟县', '黟县', '3', '341023', '341000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341103000000000', '南谯区', '南谯区', '南谯区', '3', '341103', '341100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340805000000000', '开发区', '开发区', '开发区', '3', '340805', '340800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340824000000000', '潜山县', '潜山县', '潜山县', '3', '340824', '340800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340802000000000', '迎江区', '迎江区', '迎江区', '3', '340802', '340800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340881000000000', '桐城市', '桐城市', '桐城市', '3', '340881', '340800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340823000000000', '枞阳县', '枞阳县', '枞阳县', '3', '340823', '340800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340827000000000', '望江县', '望江县', '望江县', '3', '340827', '340800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341125000000000', '定远县', '定远县', '定远县', '3', '341125', '341100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341024000000000', '祁门县', '祁门县', '祁门县', '3', '341024', '341000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340803000000000', '大观区', '大观区', '大观区', '3', '340803', '340800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340825000000000', '太湖县', '太湖县', '太湖县', '3', '340825', '340800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341004000000000', '徽州区', '徽州区', '徽州区', '3', '341004', '341000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341202000000000', '颍州区', '颍州区', '颍州区', '3', '341202', '341200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341181000000000', '天长市', '天长市', '天长市', '3', '341181', '341100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341221000000000', '临泉县', '临泉县', '临泉县', '3', '341221', '341200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341222000000000', '太和县', '太和县', '太和县', '3', '341222', '341200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341225000000000', '阜南县', '阜南县', '阜南县', '3', '341225', '341200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341302000000000', '墉桥区', '墉桥区', '墉桥区', '3', '341302', '341300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341226000000000', '颍上县', '颍上县', '颍上县', '3', '341226', '341200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341282000000000', '界首市', '界首市', '界首市', '3', '341282', '341200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341321000000000', '砀山县', '砀山县', '砀山县', '3', '341321', '341300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341204000000000', '颍泉区', '颍泉区', '颍泉区', '3', '341204', '341200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341323000000000', '灵璧县', '灵璧县', '灵璧县', '3', '341323', '341300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341322000000000', '萧县', '萧县', '萧县', '3', '341322', '341300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341182000000000', '明光市', '明光市', '明光市', '3', '341182', '341100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341203000000000', '颍东区', '颍东区', '颍东区', '3', '341203', '341200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341524000000000', '金寨县', '金寨县', '金寨县', '3', '341524', '341500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341422000000000', '无为县', '无为县', '无为县', '3', '341422', '341400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341423000000000', '含山县', '含山县', '含山县', '3', '341423', '341400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341421000000000', '庐江县', '庐江县', '庐江县', '3', '341421', '341400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341521000000000', '寿县', '寿县', '寿县', '3', '341521', '341500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341502000000000', '金安区', '金安区', '金安区', '3', '341502', '341500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341503000000000', '裕安区', '裕安区', '裕安区', '3', '341503', '341500000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341702000000000', '贵池区', '贵池区', '贵池区', '3', '341702', '341700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341721000000000', '东至县', '东至县', '东至县', '3', '341721', '341700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341402000000000', '居巢区', '居巢区', '居巢区', '3', '341402', '341400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341522000000000', '霍邱县', '霍邱县', '霍邱县', '3', '341522', '341500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341424000000000', '和县', '和县', '和县', '3', '341424', '341400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341523000000000', '舒城县', '舒城县', '舒城县', '3', '341523', '341500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341324000000000', '泗县', '泗县', '泗县', '3', '341324', '341300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341621000000000', '涡阳县', '涡阳县', '涡阳县', '3', '341621', '341600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350123000000000', '罗源县', '罗源县', '罗源县', '3', '350123', '350100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341602000000000', '谯城区', '谯城区', '谯城区', '3', '341602', '341600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341822000000000', '广德县', '广德县', '广德县', '3', '341822', '341800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341824000000000', '绩溪县', '绩溪县', '绩溪县', '3', '341824', '341800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341622000000000', '蒙城县', '蒙城县', '蒙城县', '3', '341622', '341600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350124000000000', '闽清县', '闽清县', '闽清县', '3', '350124', '350100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350102000000000', '鼓楼区', '鼓楼区', '鼓楼区', '3', '350102', '350100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341825000000000', '旌德县', '旌德县', '旌德县', '3', '341825', '341800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341723000000000', '青阳县', '青阳县', '青阳县', '3', '341723', '341700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350122000000000', '连江县', '连江县', '连江县', '3', '350122', '350100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350121000000000', '闽侯县', '闽侯县', '闽侯县', '3', '350121', '350100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341823000000000', '泾县', '泾县', '泾县', '3', '341823', '341800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341623000000000', '利辛县', '利辛县', '利辛县', '3', '341623', '341600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350103000000000', '台江区', '台江区', '台江区', '3', '350103', '350100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350105000000000', '马尾区', '马尾区', '马尾区', '3', '350105', '350100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341525000000000', '霍山县', '霍山县', '霍山县', '3', '341525', '341500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341821000000000', '郎溪县', '郎溪县', '郎溪县', '3', '341821', '341800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341881000000000', '宁国市', '宁国市', '宁国市', '3', '341881', '341800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350104000000000', '仓山区', '仓山区', '仓山区', '3', '350104', '350100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341722000000000', '石台县', '石台县', '石台县', '3', '341722', '341700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350111000000000', '晋安区', '晋安区', '晋安区', '3', '350111', '350100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341802000000000', '宣州区', '宣州区', '宣州区', '3', '341802', '341800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350213000000000', '翔安区', '翔安区', '翔安区', '3', '350213', '350200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350205000000000', '海沧区', '海沧区', '海沧区', '3', '350205', '350200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350423000000000', '清流县', '清流县', '清流县', '3', '350423', '350400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350427000000000', '沙县', '沙县', '沙县', '3', '350427', '350400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350429000000000', '泰宁县', '泰宁县', '泰宁县', '3', '350429', '350400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350206000000000', '湖里区', '湖里区', '湖里区', '3', '350206', '350200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350211000000000', '集美区', '集美区', '集美区', '3', '350211', '350200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350402000000000', '梅列区', '梅列区', '梅列区', '3', '350402', '350400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350302000000000', '城厢区', '城厢区', '城厢区', '3', '350302', '350300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350304000000000', '荔城区', '荔城区', '荔城区', '3', '350304', '350300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350322000000000', '仙游县', '仙游县', '仙游县', '3', '350322', '350300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350426000000000', '尤溪县', '尤溪县', '尤溪县', '3', '350426', '350400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350425000000000', '大田县', '大田县', '大田县', '3', '350425', '350400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350502000000000', '鲤城区', '鲤城区', '鲤城区', '3', '350502', '350500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350430000000000', '建宁县', '建宁县', '建宁县', '3', '350430', '350400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350212000000000', '同安区', '同安区', '同安区', '3', '350212', '350200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350181000000000', '福清市', '福清市', '福清市', '3', '350181', '350100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350403000000000', '三元区', '三元区', '三元区', '3', '350403', '350400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350428000000000', '将乐县', '将乐县', '将乐县', '3', '350428', '350400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350128000000000', '平潭县', '平潭县', '平潭县', '3', '350128', '350100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350424000000000', '宁化县', '宁化县', '宁化县', '3', '350424', '350400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350481000000000', '永安市', '永安市', '永安市', '3', '350481', '350400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350503000000000', '丰泽区', '丰泽区', '丰泽区', '3', '350503', '350500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350125000000000', '永泰县', '永泰县', '永泰县', '3', '350125', '350100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350203000000000', '思明区', '思明区', '思明区', '3', '350203', '350200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350303000000000', '涵江区', '涵江区', '涵江区', '3', '350303', '350300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350182000000000', '长乐市', '长乐市', '长乐市', '3', '350182', '350100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350421000000000', '明溪县', '明溪县', '明溪县', '3', '350421', '350400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350305000000000', '秀屿区', '秀屿区', '秀屿区', '3', '350305', '350300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350681000000000', '龙海市', '龙海市', '龙海市', '3', '350681', '350600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350521000000000', '惠安县', '惠安县', '惠安县', '3', '350521', '350500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350524000000000', '安溪县', '安溪县', '安溪县', '3', '350524', '350500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350627000000000', '南靖县', '南靖县', '南靖县', '3', '350627', '350600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350602000000000', '芗城区', '芗城区', '芗城区', '3', '350602', '350600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350721000000000', '顺昌县', '顺昌县', '顺昌县', '3', '350721', '350700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350581000000000', '石狮市', '石狮市', '石狮市', '3', '350581', '350500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350623000000000', '漳浦县', '漳浦县', '漳浦县', '3', '350623', '350600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350504000000000', '洛江区', '洛江区', '洛江区', '3', '350504', '350500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350625000000000', '长泰县', '长泰县', '长泰县', '3', '350625', '350600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350526000000000', '德化县', '德化县', '德化县', '3', '350526', '350500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350603000000000', '龙文区', '龙文区', '龙文区', '3', '350603', '350600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350629000000000', '华安县', '华安县', '华安县', '3', '350629', '350600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350702000000000', '延平区', '延平区', '延平区', '3', '350702', '350700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350628000000000', '平和县', '平和县', '平和县', '3', '350628', '350600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350525000000000', '永春县', '永春县', '永春县', '3', '350525', '350500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350505000000000', '泉港区', '泉港区', '泉港区', '3', '350505', '350500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350582000000000', '晋江市', '晋江市', '晋江市', '3', '350582', '350500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350604000000000', '常山开发区', '常山开发区', '常山开发区', '3', '350604', '350600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350622000000000', '云霄县', '云霄县', '云霄县', '3', '350622', '350600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350624000000000', '诏安县', '诏安县', '诏安县', '3', '350624', '350600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350626000000000', '东山县', '东山县', '东山县', '3', '350626', '350600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350583000000000', '南安市', '南安市', '南安市', '3', '350583', '350500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350723000000000', '光泽县', '光泽县', '光泽县', '3', '350723', '350700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350784000000000', '建阳市', '建阳市', '建阳市', '3', '350784', '350700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350724000000000', '松溪县', '松溪县', '松溪县', '3', '350724', '350700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350923000000000', '屏南县', '屏南县', '屏南县', '3', '350923', '350900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350902000000000', '蕉城区', '蕉城区', '蕉城区', '3', '350902', '350900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350921000000000', '霞浦县', '霞浦县', '霞浦县', '3', '350921', '350900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350722000000000', '浦城县', '浦城县', '浦城县', '3', '350722', '350700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350925000000000', '周宁县', '周宁县', '周宁县', '3', '350925', '350900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350922000000000', '古田县', '古田县', '古田县', '3', '350922', '350900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350924000000000', '寿宁县', '寿宁县', '寿宁县', '3', '350924', '350900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350926000000000', '柘荣县', '柘荣县', '柘荣县', '3', '350926', '350900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350781000000000', '邵武市', '邵武市', '邵武市', '3', '350781', '350700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350783000000000', '建瓯市', '建瓯市', '建瓯市', '3', '350783', '350700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350881000000000', '漳平市', '漳平市', '漳平市', '3', '350881', '350800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350725000000000', '政和县', '政和县', '政和县', '3', '350725', '350700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350981000000000', '福安市', '福安市', '福安市', '3', '350981', '350900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350821000000000', '长拓', '长拓', '长拓', '3', '350821', '350800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350825000000000', '连城县', '连城县', '连城县', '3', '350825', '350800000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350782000000000', '武夷山市', '武夷山市', '武夷山市', '3', '350782', '350700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350802000000000', '新罗区', '新罗区', '新罗区', '3', '350802', '350800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350822000000000', '永定县', '永定县', '永定县', '3', '350822', '350800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350823000000000', '上杭县', '上杭县', '上杭县', '3', '350823', '350800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350824000000000', '武平县', '武平县', '武平县', '3', '350824', '350800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350982000000000', '福鼎市', '福鼎市', '福鼎市', '3', '350982', '350900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360104000000000', '青云谱区', '青云谱区', '青云谱区', '3', '360104', '360100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360112000000000', '南昌经济技术开发区', '南昌经济技术开发区', '南昌经济技术开发区', '3', '360112', '360100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360113000000000', '南昌高新区', '南昌高新区', '南昌高新区', '3', '360113', '360100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360122000000000', '新建县', '新建县', '新建县', '3', '360122', '360100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360123000000000', '安义县', '安义县', '安义县', '3', '360123', '360100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360322000000000', '上栗县', '上栗县', '上栗县', '3', '360322', '360300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360402000000000', '庐山区', '庐山区', '庐山区', '3', '360402', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360103000000000', '西湖区', '西湖区', '西湖区', '3', '360103', '360100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360114000000000', '红谷滩区', '红谷滩区', '红谷滩区', '3', '360114', '360100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360222000000000', '浮梁县', '浮梁县', '浮梁县', '3', '360222', '360200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360105000000000', '湾里区', '湾里区', '湾里区', '3', '360105', '360100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360121000000000', '南昌县', '南昌县', '南昌县', '3', '360121', '360100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360302000000000', '安源区', '安源区', '安源区', '3', '360302', '360300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360423000000000', '武宁县', '武宁县', '武宁县', '3', '360423', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360102000000000', '东湖区', '东湖区', '东湖区', '3', '360102', '360100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360321000000000', '莲花县', '莲花县', '莲花县', '3', '360321', '360300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360111000000000', '青山湖区', '青山湖区', '青山湖区', '3', '360111', '360100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360202000000000', '昌江区', '昌江区', '昌江区', '3', '360202', '360200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360403000000000', '浔阳区', '浔阳区', '浔阳区', '3', '360403', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360124000000000', '进贤县', '进贤县', '进贤县', '3', '360124', '360100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360323000000000', '芦溪县', '芦溪县', '芦溪县', '3', '360323', '360300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360424000000000', '修水县', '修水县', '修水县', '3', '360424', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360203000000000', '珠山区', '珠山区', '珠山区', '3', '360203', '360200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360281000000000', '乐平市', '乐平市', '乐平市', '3', '360281', '360200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360313000000000', '湘东区', '湘东区', '湘东区', '3', '360313', '360300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360408000000000', '庐山管理局民政处', '庐山管理局民政处', '庐山管理局民政处', '3', '360408', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360409000000000', '开发区', '开发区', '开发区', '3', '360409', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360421000000000', '九江县', '九江县', '九江县', '3', '360421', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360722000000000', '信丰县', '信丰县', '信丰县', '3', '360722', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360502000000000', '渝水区', '渝水区', '渝水区', '3', '360502', '360500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360428000000000', '都昌县', '都昌县', '都昌县', '3', '360428', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360504000000000', '高新开发区', '高新开发区', '高新开发区', '3', '360504', '360500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360429000000000', '湖口县', '湖口县', '湖口县', '3', '360429', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360521000000000', '分宜县', '分宜县', '分宜县', '3', '360521', '360500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360725000000000', '崇义县', '崇义县', '崇义县', '3', '360725', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360723000000000', '大余县', '大余县', '大余县', '3', '360723', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360425000000000', '永修县', '永修县', '永修县', '3', '360425', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360729000000000', '全南县', '全南县', '全南县', '3', '360729', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360431000000000', '共青城', '共青城', '共青城', '3', '360431', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360481000000000', '瑞昌市', '瑞昌市', '瑞昌市', '3', '360481', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360702000000000', '章贡区', '章贡区', '章贡区', '3', '360702', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360681000000000', '贵溪市', '贵溪市', '贵溪市', '3', '360681', '360600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360727000000000', '龙南县', '龙南县', '龙南县', '3', '360727', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360730000000000', '宁都县', '宁都县', '宁都县', '3', '360730', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360426000000000', '德安县', '德安县', '德安县', '3', '360426', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360430000000000', '彭泽县', '彭泽县', '彭泽县', '3', '360430', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360601000000000', '市辖区(龙虎山)', '市辖区(龙虎山)', '市辖区(龙虎山)', '3', '360601', '360600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360602000000000', '月湖区', '月湖区', '月湖区', '3', '360602', '360600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360724000000000', '上犹县', '上犹县', '上犹县', '3', '360724', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360726000000000', '安远县', '安远县', '安远县', '3', '360726', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360728000000000', '定南县', '定南县', '定南县', '3', '360728', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360427000000000', '星子县', '星子县', '星子县', '3', '360427', '360400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360703000000000', '赣州经济技术开发区', '赣州经济技术开发区', '赣州经济技术开发区', '3', '360703', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360721000000000', '赣县', '赣县', '赣县', '3', '360721', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360503000000000', '仙女湖区', '仙女湖区', '仙女湖区', '3', '360503', '360500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360622000000000', '余江县', '余江县', '余江县', '3', '360622', '360600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360732000000000', '兴国县', '兴国县', '兴国县', '3', '360732', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360735000000000', '石城县', '石城县', '石城县', '3', '360735', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360824000000000', '新干县', '新干县', '新干县', '3', '360824', '360800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360828000000000', '万安县', '万安县', '万安县', '3', '360828', '360800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360821000000000', '吉安县', '吉安县', '吉安县', '3', '360821', '360800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360733000000000', '会昌县', '会昌县', '会昌县', '3', '360733', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360734000000000', '寻乌县', '寻乌县', '寻乌县', '3', '360734', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360802000000000', '吉州区', '吉州区', '吉州区', '3', '360802', '360800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360782000000000', '南康市', '南康市', '南康市', '3', '360782', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360731000000000', '于都县', '于都县', '于都县', '3', '360731', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360781000000000', '瑞金市', '瑞金市', '瑞金市', '3', '360781', '360700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360827000000000', '遂川县', '遂川县', '遂川县', '3', '360827', '360800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360825000000000', '永丰县', '永丰县', '永丰县', '3', '360825', '360800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360826000000000', '泰和县', '泰和县', '泰和县', '3', '360826', '360800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360803000000000', '青原区', '青原区', '青原区', '3', '360803', '360800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360822000000000', '吉水县', '吉水县', '吉水县', '3', '360822', '360800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360823000000000', '峡江县', '峡江县', '峡江县', '3', '360823', '360800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360925000000000', '靖安县', '靖安县', '靖安县', '3', '360925', '360900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361024000000000', '崇仁县', '崇仁县', '崇仁县', '3', '361024', '361000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361022000000000', '黎川县', '黎川县', '黎川县', '3', '361022', '361000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360830000000000', '永新县', '永新县', '永新县', '3', '360830', '360800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361026000000000', '宜黄县', '宜黄县', '宜黄县', '3', '361026', '361000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361028000000000', '资溪县', '资溪县', '资溪县', '3', '361028', '361000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360829000000000', '安福县', '安福县', '安福县', '3', '360829', '360800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360922000000000', '万载县', '万载县', '万载县', '3', '360922', '360900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360924000000000', '宜丰县', '宜丰县', '宜丰县', '3', '360924', '360900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360921000000000', '奉新县', '奉新县', '奉新县', '3', '360921', '360900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361021000000000', '南城县', '南城县', '南城县', '3', '361021', '361000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361029000000000', '东乡县', '东乡县', '东乡县', '3', '361029', '361000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360926000000000', '铜鼓县', '铜鼓县', '铜鼓县', '3', '360926', '360900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360902000000000', '袁州区', '袁州区', '袁州区', '3', '360902', '360900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361023000000000', '南丰县', '南丰县', '南丰县', '3', '361023', '361000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361025000000000', '乐安县', '乐安县', '乐安县', '3', '361025', '361000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210224000000000', '长海县', '长海县', '长海县', '3', '210224', '210200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210311000000000', '千山区', '千山区', '千山区', '3', '210311', '210300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210411000000000', '顺城区', '顺城区', '顺城区', '3', '210411', '210400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210323000000000', '岫岩满族自治县', '岫岩满族自治县', '岫岩满族自治县', '3', '210323', '210300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210505000000000', '南芬区', '南芬区', '南芬区', '3', '210505', '210500000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210282000000000', '普兰店市', '普兰店市', '普兰店市', '3', '210282', '210200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210213000000000', '金州区', '金州区', '金州区', '3', '210213', '210200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210214000000000', '开发区', '开发区', '开发区', '3', '210214', '210200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210303000000000', '铁西区', '铁西区', '铁西区', '3', '210303', '210300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210321000000000', '台安县', '台安县', '台安县', '3', '210321', '210300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210423000000000', '清原满族自治县', '清原满族自治县', '清原满族自治县', '3', '210423', '210400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210283000000000', '庄河市', '庄河市', '庄河市', '3', '210283', '210200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210281000000000', '瓦房店市', '瓦房店市', '瓦房店市', '3', '210281', '210200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210404000000000', '望花区', '望花区', '望花区', '3', '210404', '210400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210402000000000', '新抚区', '新抚区', '新抚区', '3', '210402', '210400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210421000000000', '抚顺县', '抚顺县', '抚顺县', '3', '210421', '210400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210304000000000', '立山区', '立山区', '立山区', '3', '210304', '210300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210502000000000', '平山区', '平山区', '平山区', '3', '210502', '210500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210602000000000', '元宝区', '元宝区', '元宝区', '3', '210602', '210600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210921000000000', '阜新蒙古族自治县', '阜新蒙古族自治县', '阜新蒙古族自治县', '3', '210921', '210900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210726000000000', '黑山县', '黑山县', '黑山县', '3', '210726', '210700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210782000000000', '北镇市', '北镇市', '北镇市', '3', '210782', '210700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210781000000000', '凌海市', '凌海市', '凌海市', '3', '210781', '210700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210902000000000', '海州区', '海州区', '海州区', '3', '210902', '210900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210811000000000', '营口市老边区民政局', '营口市老边区民政局', '营口市老边区民政局', '3', '210811', '210800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210604000000000', '振安区', '振安区', '振安区', '3', '210604', '210600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210903000000000', '新邱区', '新邱区', '新邱区', '3', '210903', '210900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210702000000000', '古塔区', '古塔区', '古塔区', '3', '210702', '210700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210681000000000', '东港市', '东港市', '东港市', '3', '210681', '210600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210703000000000', '凌河区', '凌河区', '凌河区', '3', '210703', '210700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210882000000000', '大石桥市', '大石桥市', '大石桥市', '3', '210882', '210800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210904000000000', '太平区', '太平区', '太平区', '3', '210904', '210900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210802000000000', '站前区', '站前区', '站前区', '3', '210802', '210800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210881000000000', '盖州市', '盖州市', '盖州市', '3', '210881', '210800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210603000000000', '振兴区', '振兴区', '振兴区', '3', '210603', '210600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210522000000000', '桓仁满族自治县', '桓仁满族自治县', '桓仁满族自治县', '3', '210522', '210500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210682000000000', '凤城市', '凤城市', '凤城市', '3', '210682', '210600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210711000000000', '太和区', '太和区', '太和区', '3', '210711', '210700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210905000000000', '清河门区', '清河门区', '清河门区', '3', '210905', '210900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210803000000000', '西市区', '西市区', '西市区', '3', '210803', '210800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210624000000000', '宽甸满族自治县', '宽甸满族自治县', '宽甸满族自治县', '3', '210624', '210600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210911000000000', '细河区', '细河区', '细河区', '3', '210911', '210900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210727000000000', '义县', '义县', '义县', '3', '210727', '210700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210804000000000', '鲅鱼圈区', '鲅鱼圈区', '鲅鱼圈区', '3', '210804', '210800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211282000000000', '开原市', '开原市', '开原市', '3', '211282', '211200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211011000000000', '太子河区', '太子河区', '太子河区', '3', '211011', '211000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210922000000000', '彰武县', '彰武县', '彰武县', '3', '210922', '210900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211002000000000', '白塔区', '白塔区', '白塔区', '3', '211002', '211000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211102000000000', '双台子区', '双台子区', '双台子区', '3', '211102', '211100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211103000000000', '兴隆台区', '兴隆台区', '兴隆台区', '3', '211103', '211100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211224000000000', '昌图县', '昌图县', '昌图县', '3', '211224', '211200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211204000000000', '清河区', '清河区', '清河区', '3', '211204', '211200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211321000000000', '朝阳县', '朝阳县', '朝阳县', '3', '211321', '211300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211223000000000', '西丰县', '西丰县', '西丰县', '3', '211223', '211200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211221000000000', '铁岭县', '铁岭县', '铁岭县', '3', '211221', '211200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211302000000000', '双塔区', '双塔区', '双塔区', '3', '211302', '211300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211005000000000', '弓长岭区', '弓长岭区', '弓长岭区', '3', '211005', '211000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211021000000000', '辽阳县', '辽阳县', '辽阳县', '3', '211021', '211000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211121000000000', '大洼县', '大洼县', '大洼县', '3', '211121', '211100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211202000000000', '银州区', '银州区', '银州区', '3', '211202', '211200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211322000000000', '建平县', '建平县', '建平县', '3', '211322', '211300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211281000000000', '调兵山市', '调兵山市', '调兵山市', '3', '211281', '211200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211003000000000', '文圣区', '文圣区', '文圣区', '3', '211003', '211000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211004000000000', '宏伟区', '宏伟区', '宏伟区', '3', '211004', '211000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211081000000000', '灯塔市', '灯塔市', '灯塔市', '3', '211081', '211000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211122000000000', '盘山县', '盘山县', '盘山县', '3', '211122', '211100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211303000000000', '龙城区', '龙城区', '龙城区', '3', '211303', '211300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220182000000000', '榆树市', '榆树市', '榆树市', '3', '220182', '220100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211404000000000', '南票区', '南票区', '南票区', '3', '211404', '211400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211381000000000', '北票市', '北票市', '北票市', '3', '211381', '211300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220183000000000', '德惠市', '德惠市', '德惠市', '3', '220183', '220100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211421000000000', '绥中县', '绥中县', '绥中县', '3', '211421', '211400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220103000000000', '宽城区', '宽城区', '宽城区', '3', '220103', '220100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220106000000000', '绿园区', '绿园区', '绿园区', '3', '220106', '220100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220122000000000', '农安县', '农安县', '农安县', '3', '220122', '220100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211324000000000', '喀喇沁左翼蒙古族自治县', '喀喇沁左翼蒙古族自治县', '喀喇沁左翼蒙古族自治县', '3', '211324', '211300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220104000000000', '朝阳区', '朝阳区', '朝阳区', '3', '220104', '220100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211422000000000', '建昌县', '建昌县', '建昌县', '3', '211422', '211400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220107000000000', '经济技术开发区', '经济技术开发区', '经济技术开发区', '3', '220107', '220100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220108000000000', '净月旅游开发区', '净月旅游开发区', '净月旅游开发区', '3', '220108', '220100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220109000000000', '高新技术开发区', '高新技术开发区', '高新技术开发区', '3', '220109', '220100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220112000000000', '双阳区', '双阳区', '双阳区', '3', '220112', '220100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220181000000000', '九台市', '九台市', '九台市', '3', '220181', '220100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211382000000000', '凌源市', '凌源市', '凌源市', '3', '211382', '211300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211402000000000', '连山区', '连山区', '连山区', '3', '211402', '211400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211403000000000', '龙港区', '龙港区', '龙港区', '3', '211403', '211400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220105000000000', '二道区', '二道区', '二道区', '3', '220105', '220100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211481000000000', '兴城市', '兴城市', '兴城市', '3', '211481', '211400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220102000000000', '南关区', '南关区', '南关区', '3', '220102', '220100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220403000000000', '西安区', '西安区', '西安区', '3', '220403', '220400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220203000000000', '龙潭区', '龙潭区', '龙潭区', '3', '220203', '220200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220322000000000', '梨树县', '梨树县', '梨树县', '3', '220322', '220300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220284000000000', '磐石市', '磐石市', '磐石市', '3', '220284', '220200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220402000000000', '龙山区', '龙山区', '龙山区', '3', '220402', '220400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220281000000000', '蛟河市', '蛟河市', '蛟河市', '3', '220281', '220200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220302000000000', '铁西区', '铁西区', '铁西区', '3', '220302', '220300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220382000000000', '双辽市', '双辽市', '双辽市', '3', '220382', '220300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220421000000000', '东丰县', '东丰县', '东丰县', '3', '220421', '220400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220381000000000', '公主岭市', '公主岭市', '公主岭市', '3', '220381', '220300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220205000000000', '经济技术开发区', '经济技术开发区', '经济技术开发区', '3', '220205', '220200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220206000000000', '高新技术开发区', '高新技术开发区', '高新技术开发区', '3', '220206', '220200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220211000000000', '丰满区', '丰满区', '丰满区', '3', '220211', '220200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220202000000000', '昌邑区', '昌邑区', '昌邑区', '3', '220202', '220200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220303000000000', '铁东区', '铁东区', '铁东区', '3', '220303', '220300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220323000000000', '伊通满族自治县', '伊通满族自治县', '伊通满族自治县', '3', '220323', '220300000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220221000000000', '永吉县', '永吉县', '永吉县', '3', '220221', '220200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220204000000000', '船营区', '船营区', '船营区', '3', '220204', '220200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220304000000000', '辽河农垦管理区', '辽河农垦管理区', '辽河农垦管理区', '3', '220304', '220300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220282000000000', '桦甸市', '桦甸市', '桦甸市', '3', '220282', '220200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220283000000000', '舒兰市', '舒兰市', '舒兰市', '3', '220283', '220200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220602000000000', '八道江区', '八道江区', '八道江区', '3', '220602', '220600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220623000000000', '长白朝鲜族自治县', '长白朝鲜族自治县', '长白朝鲜族自治县', '3', '220623', '220600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220502000000000', '东昌区', '东昌区', '东昌区', '3', '220502', '220500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220822000000000', '通榆县', '通榆县', '通榆县', '3', '220822', '220800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220724000000000', '扶余县', '扶余县', '扶余县', '3', '220724', '220700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220523000000000', '辉南县', '辉南县', '辉南县', '3', '220523', '220500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220581000000000', '梅河口市', '梅河口市', '梅河口市', '3', '220581', '220500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220702000000000', '宁江区', '宁江区', '宁江区', '3', '220702', '220700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220625000000000', '江源县', '江源县', '江源县', '3', '220625', '220600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220821000000000', '镇赉县', '镇赉县', '镇赉县', '3', '220821', '220800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220422000000000', '东辽县', '东辽县', '东辽县', '3', '220422', '220400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220582000000000', '集安市', '集安市', '集安市', '3', '220582', '220500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220524000000000', '柳河县', '柳河县', '柳河县', '3', '220524', '220500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220622000000000', '靖宇县', '靖宇县', '靖宇县', '3', '220622', '220600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220621000000000', '抚松县', '抚松县', '抚松县', '3', '220621', '220600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220802000000000', '洮北区', '洮北区', '洮北区', '3', '220802', '220800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220722000000000', '长岭县', '长岭县', '长岭县', '3', '220722', '220700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220723000000000', '乾安县', '乾安县', '乾安县', '3', '220723', '220700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220881000000000', '洮南市', '洮南市', '洮南市', '3', '220881', '220800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220503000000000', '二道江区', '二道江区', '二道江区', '3', '220503', '220500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220521000000000', '通化县', '通化县', '通化县', '3', '220521', '220500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220721000000000', '前郭尔罗斯蒙古族自治县', '前郭尔罗斯蒙古族自治县', '前郭尔罗斯蒙古族自治县', '3', '220721', '220700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220681000000000', '临江市', '临江市', '临江市', '3', '220681', '220600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230123000000000', '依兰县', '依兰县', '依兰县', '3', '230123', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230125000000000', '宾县', '宾县', '宾县', '3', '230125', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('222405000000000', '龙井市', '龙井市', '龙井市', '3', '222405', '222400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('222424000000000', '汪清县', '汪清县', '汪清县', '3', '222424', '222400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230127000000000', '木兰县', '木兰县', '木兰县', '3', '230127', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230129000000000', '延寿县', '延寿县', '延寿县', '3', '230129', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230103000000000', '南岗区', '南岗区', '南岗区', '3', '230103', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230108000000000', '平房区', '平房区', '平房区', '3', '230108', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230128000000000', '通河县', '通河县', '通河县', '3', '230128', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230124000000000', '方正县', '方正县', '方正县', '3', '230124', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('222402000000000', '图们市', '图们市', '图们市', '3', '222402', '222400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('222403000000000', '敦化市', '敦化市', '敦化市', '3', '222403', '222400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220882000000000', '大安市', '大安市', '大安市', '3', '220882', '220800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('222404000000000', '珲春市', '珲春市', '珲春市', '3', '222404', '222400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230102000000000', '道里区', '道里区', '道里区', '3', '230102', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230109000000000', '松北区', '松北区', '松北区', '3', '230109', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('222406000000000', '和龙市', '和龙市', '和龙市', '3', '222406', '222400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230126000000000', '巴彦县', '巴彦县', '巴彦县', '3', '230126', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('222401000000000', '延吉市', '延吉市', '延吉市', '3', '222401', '222400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230104000000000', '道外区', '道外区', '道外区', '3', '230104', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230106000000000', '香坊区', '香坊区', '香坊区', '3', '230106', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230111000000000', '呼兰区', '呼兰区', '呼兰区', '3', '230111', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('222426000000000', '安图县', '安图县', '安图县', '3', '222426', '222400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230382000000000', '密山市', '密山市', '密山市', '3', '230382', '230300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230406000000000', '东山区', '东山区', '东山区', '3', '230406', '230400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230302000000000', '鸡冠区', '鸡冠区', '鸡冠区', '3', '230302', '230300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230307000000000', '麻山区', '麻山区', '麻山区', '3', '230307', '230300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230182000000000', '双城市', '双城市', '双城市', '3', '230182', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230502000000000', '尖山区', '尖山区', '尖山区', '3', '230502', '230500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230521000000000', '集贤县', '集贤县', '集贤县', '3', '230521', '230500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230203000000000', '建华区', '建华区', '建华区', '3', '230203', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230223000000000', '依安县', '依安县', '依安县', '3', '230223', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230281000000000', '讷河市', '讷河市', '讷河市', '3', '230281', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230229000000000', '克山县', '克山县', '克山县', '3', '230229', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230321000000000', '鸡东县', '鸡东县', '鸡东县', '3', '230321', '230300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230407000000000', '兴山区', '兴山区', '兴山区', '3', '230407', '230400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230421000000000', '萝北县', '萝北县', '萝北县', '3', '230421', '230400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230709000000000', '金山屯区', '金山屯区', '金山屯区', '3', '230709', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230303000000000', '恒山区', '恒山区', '恒山区', '3', '230303', '230300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230503000000000', '岭东区', '岭东区', '岭东区', '3', '230503', '230500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230207000000000', '碾子山区', '碾子山区', '碾子山区', '3', '230207', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230505000000000', '四方台区', '四方台区', '四方台区', '3', '230505', '230500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230506000000000', '宝山区', '宝山区', '宝山区', '3', '230506', '230500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230181000000000', '阿城市', '阿城市', '阿城市', '3', '230181', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230208000000000', '梅里斯达斡尔族区', '梅里斯达斡尔族区', '梅里斯达斡尔族区', '3', '230208', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230184000000000', '五常市', '五常市', '五常市', '3', '230184', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230202000000000', '龙沙区', '龙沙区', '龙沙区', '3', '230202', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230225000000000', '甘南县', '甘南县', '甘南县', '3', '230225', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230204000000000', '铁锋区', '铁锋区', '铁锋区', '3', '230204', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230231000000000', '拜泉县', '拜泉县', '拜泉县', '3', '230231', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230304000000000', '滴道区民政局', '滴道区民政局', '滴道区民政局', '3', '230304', '230300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230422000000000', '绥滨县', '绥滨县', '绥滨县', '3', '230422', '230400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230221000000000', '龙江县', '龙江县', '龙江县', '3', '230221', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230205000000000', '昂昂溪区', '昂昂溪区', '昂昂溪区', '3', '230205', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230206000000000', '富拉尔基区', '富拉尔基区', '富拉尔基区', '3', '230206', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230402000000000', '向阳区', '向阳区', '向阳区', '3', '230402', '230400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230227000000000', '富裕县', '富裕县', '富裕县', '3', '230227', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230381000000000', '虎林市', '虎林市', '虎林市', '3', '230381', '230300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230403000000000', '工农区', '工农区', '工农区', '3', '230403', '230400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230404000000000', '南山区', '南山区', '南山区', '3', '230404', '230400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230305000000000', '梨树区', '梨树区', '梨树区', '3', '230305', '230300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230183000000000', '尚志市', '尚志市', '尚志市', '3', '230183', '230100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230405000000000', '兴安区', '兴安区', '兴安区', '3', '230405', '230400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230224000000000', '泰来县', '泰来县', '泰来县', '3', '230224', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230306000000000', '城子河区', '城子河区', '城子河区', '3', '230306', '230300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230230000000000', '克东县', '克东县', '克东县', '3', '230230', '230200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230921000000000', '勃利县', '勃利县', '勃利县', '3', '230921', '230900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230606000000000', '大同区', '大同区', '大同区', '3', '230606', '230600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231024000000000', '东宁县', '东宁县', '东宁县', '3', '231024', '231000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230602000000000', '萨尔图区', '萨尔图区', '萨尔图区', '3', '230602', '230600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230605000000000', '红岗区', '红岗区', '红岗区', '3', '230605', '230600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230702000000000', '伊春区', '伊春区', '伊春区', '3', '230702', '230700000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230708000000000', '美溪区', '美溪区', '美溪区', '3', '230708', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230805000000000', '东风区', '东风区', '东风区', '3', '230805', '230800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230781000000000', '铁力市', '铁力市', '铁力市', '3', '230781', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231005000000000', '西安区', '西安区', '西安区', '3', '231005', '231000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230882000000000', '富锦市', '富锦市', '富锦市', '3', '230882', '230800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230902000000000', '新兴区', '新兴区', '新兴区', '3', '230902', '230900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231025000000000', '林口县', '林口县', '林口县', '3', '231025', '231000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230524000000000', '饶河县', '饶河县', '饶河县', '3', '230524', '230500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230703000000000', '南岔区', '南岔区', '南岔区', '3', '230703', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230710000000000', '五营区', '五营区', '五营区', '3', '230710', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230711000000000', '乌马河区', '乌马河区', '乌马河区', '3', '230711', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230811000000000', '郊区', '郊区', '郊区', '3', '230811', '230800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230623000000000', '林甸县', '林甸县', '林甸县', '3', '230623', '230600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230833000000000', '抚远县', '抚远县', '抚远县', '3', '230833', '230800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230828000000000', '汤原县', '汤原县', '汤原县', '3', '230828', '230800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230903000000000', '桃山区', '桃山区', '桃山区', '3', '230903', '230900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230621000000000', '肇州县', '肇州县', '肇州县', '3', '230621', '230600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230522000000000', '友谊县', '友谊县', '友谊县', '3', '230522', '230500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230712000000000', '汤旺河区', '汤旺河区', '汤旺河区', '3', '230712', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230713000000000', '带岭区', '带岭区', '带岭区', '3', '230713', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230624000000000', '杜尔伯特蒙古自治县', '杜尔伯特蒙古自治县', '杜尔伯特蒙古自治县', '3', '230624', '230600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230714000000000', '乌伊岭区', '乌伊岭区', '乌伊岭区', '3', '230714', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230704000000000', '友好区', '友好区', '友好区', '3', '230704', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231002000000000', '东安区', '东安区', '东安区', '3', '231002', '231000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230881000000000', '同江市', '同江市', '同江市', '3', '230881', '230800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230715000000000', '红星区', '红星区', '红星区', '3', '230715', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230716000000000', '上甘岭区', '上甘岭区', '上甘岭区', '3', '230716', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230803000000000', '向阳区', '向阳区', '向阳区', '3', '230803', '230800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231003000000000', '阳明区', '阳明区', '阳明区', '3', '231003', '231000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230904000000000', '茄子河区', '茄子河区', '茄子河区', '3', '230904', '230900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230603000000000', '龙凤区', '龙凤区', '龙凤区', '3', '230603', '230600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230604000000000', '让胡路区', '让胡路区', '让胡路区', '3', '230604', '230600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230622000000000', '肇源县', '肇源县', '肇源县', '3', '230622', '230600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230523000000000', '宝清县', '宝清县', '宝清县', '3', '230523', '230500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230822000000000', '桦南县', '桦南县', '桦南县', '3', '230822', '230800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230826000000000', '桦川县', '桦川县', '桦川县', '3', '230826', '230800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230705000000000', '西林区', '西林区', '西林区', '3', '230705', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230706000000000', '翠峦区', '翠峦区', '翠峦区', '3', '230706', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230707000000000', '新青区', '新青区', '新青区', '3', '230707', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230722000000000', '嘉荫县', '嘉荫县', '嘉荫县', '3', '230722', '230700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230804000000000', '前进区', '前进区', '前进区', '3', '230804', '230800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231004000000000', '爱民区', '爱民区', '爱民区', '3', '231004', '231000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('232702000000000', '松岭区民政局', '松岭区民政局', '松岭区民政局', '3', '232702', '232700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('232703000000000', '新林区', '新林区', '新林区', '3', '232703', '232700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('232704000000000', '呼中区', '呼中区', '呼中区', '3', '232704', '232700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('239903000000000', '建三江分局', '建三江分局', '建三江分局', '3', '239903', '239900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231124000000000', '孙吴县', '孙吴县', '孙吴县', '3', '231124', '231100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231182000000000', '五大连池市', '五大连池市', '五大连池市', '3', '231182', '231100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231121000000000', '嫩江县', '嫩江县', '嫩江县', '3', '231121', '231100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('232721000000000', '呼玛县', '呼玛县', '呼玛县', '3', '232721', '232700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('232722000000000', '塔河县', '塔河县', '塔河县', '3', '232722', '232700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231084000000000', '宁安市', '宁安市', '宁安市', '3', '231084', '231000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('239902000000000', '红兴隆分局', '红兴隆分局', '红兴隆分局', '3', '239902', '239900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231225000000000', '明水县', '明水县', '明水县', '3', '231225', '231200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231281000000000', '安达市', '安达市', '安达市', '3', '231281', '231200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231221000000000', '望奎县', '望奎县', '望奎县', '3', '231221', '231200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231223000000000', '青冈县', '青冈县', '青冈县', '3', '231223', '231200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('232723000000000', '漠河县', '漠河县', '漠河县', '3', '232723', '232700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('239901000000000', '宝泉岭分局', '宝泉岭分局', '宝泉岭分局', '3', '239901', '239900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231181000000000', '北安市', '北安市', '北安市', '3', '231181', '231100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('239904000000000', '牡丹江分局', '牡丹江分局', '牡丹江分局', '3', '239904', '239900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231283000000000', '海伦市', '海伦市', '海伦市', '3', '231283', '231200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231081000000000', '绥芬河市', '绥芬河市', '绥芬河市', '3', '231081', '231000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231102000000000', '爱辉区', '爱辉区', '爱辉区', '3', '231102', '231100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231083000000000', '海林市', '海林市', '海林市', '3', '231083', '231000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231123000000000', '逊克县', '逊克县', '逊克县', '3', '231123', '231100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231085000000000', '穆棱市', '穆棱市', '穆棱市', '3', '231085', '231000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231202000000000', '北林区', '北林区', '北林区', '3', '231202', '231200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231226000000000', '绥棱县', '绥棱县', '绥棱县', '3', '231226', '231200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231282000000000', '肇东市', '肇东市', '肇东市', '3', '231282', '231200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231222000000000', '兰西县', '兰西县', '兰西县', '3', '231222', '231200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231224000000000', '庆安县', '庆安县', '庆安县', '3', '231224', '231200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('232701000000000', '加格达奇区', '加格达奇区', '加格达奇区', '3', '232701', '232700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('239907000000000', '齐齐哈尔分局', '齐齐哈尔分局', '齐齐哈尔分局', '3', '239907', '239900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310113000000000', '宝山区', '宝山区', '宝山区', '3', '310113', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310109000000000', '虹口区', '虹口区', '虹口区', '3', '310109', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310116000000000', '金山区', '金山区', '金山区', '3', '310116', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310117000000000', '松江区', '松江区', '松江区', '3', '310117', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310104000000000', '徐汇区', '徐汇区', '徐汇区', '3', '310104', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310106000000000', '静安区', '静安区', '静安区', '3', '310106', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310108000000000', '闸北区', '闸北区', '闸北区', '3', '310108', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310115000000000', '浦东新区', '浦东新区', '浦东新区', '3', '310115', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('239909000000000', '哈尔滨分局', '哈尔滨分局', '哈尔滨分局', '3', '239909', '239900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310101000000000', '黄浦区', '黄浦区', '黄浦区', '3', '310101', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310103000000000', '卢湾区', '卢湾区', '卢湾区', '3', '310103', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('239906000000000', '九三分局', '九三分局', '九三分局', '3', '239906', '239900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310107000000000', '普陀区', '普陀区', '普陀区', '3', '310107', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310112000000000', '闵行区', '闵行区', '闵行区', '3', '310112', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310105000000000', '长宁区', '长宁区', '长宁区', '3', '310105', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310114000000000', '嘉定区', '嘉定区', '嘉定区', '3', '310114', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('239908000000000', '绥化分局', '绥化分局', '绥化分局', '3', '239908', '239900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('239905000000000', '北安分局', '北安分局', '北安分局', '3', '239905', '239900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310110000000000', '杨浦区', '杨浦区', '杨浦区', '3', '310110', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310120000000000', '奉贤区', '奉贤区', '奉贤区', '3', '310120', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320303000000000', '云龙区', '云龙区', '云龙区', '3', '320303', '320300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320102000000000', '玄武区', '玄武区', '玄武区', '3', '320102', '320100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320106000000000', '鼓楼区', '鼓楼区', '鼓楼区', '3', '320106', '320100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320205000000000', '锡山区', '锡山区', '锡山区', '3', '320205', '320200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310230000000000', '崇明县', '崇明县', '崇明县', '3', '310230', '310200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320211000000000', '滨湖区', '滨湖区', '滨湖区', '3', '320211', '320200000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320117000000000', '沿江开发区', '沿江开发区', '沿江开发区', '3', '320117', '320100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320304000000000', '九里区', '九里区', '九里区', '3', '320304', '320300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320283000000000', '新区', '新区', '新区', '3', '320283', '320200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320305000000000', '贾汪区', '贾汪区', '贾汪区', '3', '320305', '320300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320113000000000', '栖霞区', '栖霞区', '栖霞区', '3', '320113', '320100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320103000000000', '白下区', '白下区', '白下区', '3', '320103', '320100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320202000000000', '崇安区', '崇安区', '崇安区', '3', '320202', '320200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320124000000000', '溧水县', '溧水县', '溧水县', '3', '320124', '320100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320281000000000', '江阴市', '江阴市', '江阴市', '3', '320281', '320200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310118000000000', '青浦区', '青浦区', '青浦区', '3', '310118', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310119000000000', '南汇区', '南汇区', '南汇区', '3', '310119', '310100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320107000000000', '下关区', '下关区', '下关区', '3', '320107', '320100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320311000000000', '泉山区', '泉山区', '泉山区', '3', '320311', '320300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320111000000000', '浦口区', '浦口区', '浦口区', '3', '320111', '320100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320104000000000', '秦淮区', '秦淮区', '秦淮区', '3', '320104', '320100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320203000000000', '南长区', '南长区', '南长区', '3', '320203', '320200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320114000000000', '雨花台区', '雨花台区', '雨花台区', '3', '320114', '320100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320206000000000', '惠山区', '惠山区', '惠山区', '3', '320206', '320200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320116000000000', '六合区', '六合区', '六合区', '3', '320116', '320100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320302000000000', '鼓楼区', '鼓楼区', '鼓楼区', '3', '320302', '320300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320282000000000', '宜兴市', '宜兴市', '宜兴市', '3', '320282', '320200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320105000000000', '建邺区', '建邺区', '建邺区', '3', '320105', '320100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320204000000000', '北塘区', '北塘区', '北塘区', '3', '320204', '320200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320115000000000', '江宁区', '江宁区', '江宁区', '3', '320115', '320100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320125000000000', '高淳县', '高淳县', '高淳县', '3', '320125', '320100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320505000000000', '虎丘区（高新区）', '虎丘区（高新区）', '虎丘区（高新区）', '3', '320505', '320500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320502000000000', '沧浪区', '沧浪区', '沧浪区', '3', '320502', '320500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320506000000000', '吴中区', '吴中区', '吴中区', '3', '320506', '320500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320412000000000', '武进区', '武进区', '武进区', '3', '320412', '320400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320324000000000', '睢宁县', '睢宁县', '睢宁县', '3', '320324', '320300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320404000000000', '钟楼区', '钟楼区', '钟楼区', '3', '320404', '320400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320481000000000', '溧阳市', '溧阳市', '溧阳市', '3', '320481', '320400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320581000000000', '常熟市', '常熟市', '常熟市', '3', '320581', '320500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320482000000000', '金坛市', '金坛市', '金坛市', '3', '320482', '320400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320312000000000', '金山桥开发区', '金山桥开发区', '金山桥开发区', '3', '320312', '320300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320503000000000', '平江区', '平江区', '平江区', '3', '320503', '320500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320323000000000', '铜山县', '铜山县', '铜山县', '3', '320323', '320300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320321000000000', '丰县', '丰县', '丰县', '3', '320321', '320300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320382000000000', '邳州市', '邳州市', '邳州市', '3', '320382', '320300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320381000000000', '新沂市', '新沂市', '新沂市', '3', '320381', '320300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320405000000000', '戚墅堰区', '戚墅堰区', '戚墅堰区', '3', '320405', '320400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320504000000000', '金阊区', '金阊区', '金阊区', '3', '320504', '320500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320322000000000', '沛县', '沛县', '沛县', '3', '320322', '320300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320411000000000', '新北区', '新北区', '新北区', '3', '320411', '320400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320507000000000', '相城区', '相城区', '相城区', '3', '320507', '320500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320402000000000', '天宁区', '天宁区', '天宁区', '3', '320402', '320400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320705000000000', '新浦区', '新浦区', '新浦区', '3', '320705', '320700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320582000000000', '张家港市', '张家港市', '张家港市', '3', '320582', '320500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320683000000000', '通州市', '通州市', '通州市', '3', '320683', '320600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320722000000000', '东海县', '东海县', '东海县', '3', '320722', '320700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320585000000000', '太仓市', '太仓市', '太仓市', '3', '320585', '320500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320706000000000', '海州区', '海州区', '海州区', '3', '320706', '320700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320707000000000', '开发区', '开发区', '开发区', '3', '320707', '320700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320611000000000', '港闸区', '港闸区', '港闸区', '3', '320611', '320600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320621000000000', '海安县', '海安县', '海安县', '3', '320621', '320600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320623000000000', '如东县', '如东县', '如东县', '3', '320623', '320600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320681000000000', '启东市', '启东市', '启东市', '3', '320681', '320600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320721000000000', '赣榆县', '赣榆县', '赣榆县', '3', '320721', '320700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320684000000000', '海门市', '海门市', '海门市', '3', '320684', '320600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320685000000000', '开发区', '开发区', '开发区', '3', '320685', '320600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320703000000000', '连云区', '连云区', '连云区', '3', '320703', '320700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320583000000000', '昆山市', '昆山市', '昆山市', '3', '320583', '320500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320586000000000', '工业园区', '工业园区', '工业园区', '3', '320586', '320500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320584000000000', '吴江市', '吴江市', '吴江市', '3', '320584', '320500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320602000000000', '崇川区', '崇川区', '崇川区', '3', '320602', '320600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320682000000000', '如皋市', '如皋市', '如皋市', '3', '320682', '320600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320802000000000', '清河区', '清河区', '清河区', '3', '320802', '320800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320724000000000', '灌南县', '灌南县', '灌南县', '3', '320724', '320700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320922000000000', '滨海县', '滨海县', '滨海县', '3', '320922', '320900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320831000000000', '金湖县', '金湖县', '金湖县', '3', '320831', '320800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320928000000000', '盐都县', '盐都县', '盐都县', '3', '320928', '320900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320921000000000', '响水县', '响水县', '响水县', '3', '320921', '320900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320811000000000', '清浦区', '清浦区', '清浦区', '3', '320811', '320800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320830000000000', '盱眙县', '盱眙县', '盱眙县', '3', '320830', '320800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320923000000000', '阜宁县', '阜宁县', '阜宁县', '3', '320923', '320900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320924000000000', '射阳县', '射阳县', '射阳县', '3', '320924', '320900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320925000000000', '建湖县', '建湖县', '建湖县', '3', '320925', '320900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320803000000000', '楚州区', '楚州区', '楚州区', '3', '320803', '320800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320804000000000', '淮阴区', '淮阴区', '淮阴区', '3', '320804', '320800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320899000000000', '开发区', '开发区', '开发区', '3', '320899', '320800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320901000000000', '市辖区', '市辖区', '市辖区', '3', '320901', '320900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320902000000000', '城区', '城区', '城区', '3', '320902', '320900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320723000000000', '灌云县', '灌云县', '灌云县', '3', '320723', '320700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320826000000000', '涟水县', '涟水县', '涟水县', '3', '320826', '320800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320829000000000', '洪泽县', '洪泽县', '洪泽县', '3', '320829', '320800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320981000000000', '东台市', '东台市', '东台市', '3', '320981', '320900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321283000000000', '泰兴市', '泰兴市', '泰兴市', '3', '321283', '321200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321088000000000', '江都市', '江都市', '江都市', '3', '321088', '321000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321084000000000', '高邮市', '高邮市', '高邮市', '3', '321084', '321000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321202000000000', '海陵区', '海陵区', '海陵区', '3', '321202', '321200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410222000000000', '通许县', '通许县', '通许县', '3', '410222', '410200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410204000000000', '鼓楼区', '鼓楼区', '鼓楼区', '3', '410204', '410200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410223000000000', '尉氏县', '尉氏县', '尉氏县', '3', '410223', '410200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410205000000000', '禹王台区', '禹王台区', '禹王台区', '3', '410205', '410200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410182000000000', '荥阳市', '荥阳市', '荥阳市', '3', '410182', '410100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410110000000000', '郑州经济技术开发区', '郑州经济技术开发区', '郑州经济技术开发区', '3', '410110', '410100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410111000000000', '郑州高新区', '郑州高新区', '郑州高新区', '3', '410111', '410100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410183000000000', '新密市', '新密市', '新密市', '3', '410183', '410100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410185000000000', '登封市', '登封市', '登封市', '3', '410185', '410100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410302000000000', '老城区', '老城区', '老城区', '3', '410302', '410300000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410411000000000', '湛河区', '湛河区', '湛河区', '3', '410411', '410400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410306000000000', '吉利区', '吉利区', '吉利区', '3', '410306', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410307000000000', '洛龙区', '洛龙区', '洛龙区', '3', '410307', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410329000000000', '伊川县', '伊川县', '伊川县', '3', '410329', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410422000000000', '叶县', '叶县', '叶县', '3', '410422', '410400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410326000000000', '汝阳县', '汝阳县', '汝阳县', '3', '410326', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410402000000000', '新华区', '新华区', '新华区', '3', '410402', '410400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410303000000000', '西工区', '西工区', '西工区', '3', '410303', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410421000000000', '宝丰县', '宝丰县', '宝丰县', '3', '410421', '410400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410304000000000', '廛河回族区', '廛河回族区', '廛河回族区', '3', '410304', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410308000000000', '高新开发区', '高新开发区', '高新开发区', '3', '410308', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410323000000000', '新安县', '新安县', '新安县', '3', '410323', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410328000000000', '洛宁县', '洛宁县', '洛宁县', '3', '410328', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410305000000000', '涧西区', '涧西区', '涧西区', '3', '410305', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410403000000000', '卫东区', '卫东区', '卫东区', '3', '410403', '410400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410325000000000', '嵩县', '嵩县', '嵩县', '3', '410325', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410322000000000', '孟津县', '孟津县', '孟津县', '3', '410322', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410324000000000', '栾川县', '栾川县', '栾川县', '3', '410324', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410404000000000', '石龙区', '石龙区', '石龙区', '3', '410404', '410400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410327000000000', '宜阳县', '宜阳县', '宜阳县', '3', '410327', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410381000000000', '偃师市', '偃师市', '偃师市', '3', '410381', '410300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410425000000000', '郏县', '郏县', '郏县', '3', '410425', '410400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410503000000000', '北关区', '北关区', '北关区', '3', '410503', '410500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410507000000000', '开发区', '开发区', '开发区', '3', '410507', '410500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410522000000000', '安阳县', '安阳县', '安阳县', '3', '410522', '410500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410423000000000', '鲁山县', '鲁山县', '鲁山县', '3', '410423', '410400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410482000000000', '汝州市', '汝州市', '汝州市', '3', '410482', '410400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410505000000000', '殷都区', '殷都区', '殷都区', '3', '410505', '410500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410481000000000', '舞钢市', '舞钢市', '舞钢市', '3', '410481', '410400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410523000000000', '汤阴县', '汤阴县', '汤阴县', '3', '410523', '410500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410526000000000', '滑县', '滑县', '滑县', '3', '410526', '410500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410527000000000', '内黄县', '内黄县', '内黄县', '3', '410527', '410500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410502000000000', '文峰区', '文峰区', '文峰区', '3', '410502', '410500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410506000000000', '龙安区', '龙安区', '龙安区', '3', '410506', '410500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410581000000000', '林州市', '林州市', '林州市', '3', '410581', '410500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410602000000000', '鹤山区', '鹤山区', '鹤山区', '3', '410602', '410600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410621000000000', '浚县', '浚县', '浚县', '3', '410621', '410600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410721000000000', '新乡县', '新乡县', '新乡县', '3', '410721', '410700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410726000000000', '延津县', '延津县', '延津县', '3', '410726', '410700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410702000000000', '红旗区', '红旗区', '红旗区', '3', '410702', '410700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410604000000000', '开发区', '开发区', '开发区', '3', '410604', '410600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410703000000000', '卫滨区', '卫滨区', '卫滨区', '3', '410703', '410700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410725000000000', '原阳县', '原阳县', '原阳县', '3', '410725', '410700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410728000000000', '长垣县', '长垣县', '长垣县', '3', '410728', '410700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410622000000000', '淇县', '淇县', '淇县', '3', '410622', '410600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410603000000000', '山城区', '山城区', '山城区', '3', '410603', '410600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410704000000000', '凤泉区', '凤泉区', '凤泉区', '3', '410704', '410700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410724000000000', '获嘉县', '获嘉县', '获嘉县', '3', '410724', '410700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410727000000000', '封丘县', '封丘县', '封丘县', '3', '410727', '410700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410611000000000', '淇滨区', '淇滨区', '淇滨区', '3', '410611', '410600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410711000000000', '牧野区', '牧野区', '牧野区', '3', '410711', '410700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410782000000000', '辉县市', '辉县市', '辉县市', '3', '410782', '410700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410802000000000', '解放区', '解放区', '解放区', '3', '410802', '410800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410821000000000', '修武县', '修武县', '修武县', '3', '410821', '410800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410883000000000', '孟州市', '孟州市', '孟州市', '3', '410883', '410800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410823000000000', '武陟县', '武陟县', '武陟县', '3', '410823', '410800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410926000000000', '范县', '范县', '范县', '3', '410926', '410900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410922000000000', '清丰县', '清丰县', '清丰县', '3', '410922', '410900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410923000000000', '南乐县', '南乐县', '南乐县', '3', '410923', '410900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410803000000000', '中站区', '中站区', '中站区', '3', '410803', '410800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410825000000000', '温县', '温县', '温县', '3', '410825', '410800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410902000000000', '华龙区', '华龙区', '华龙区', '3', '410902', '410900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410804000000000', '马村区', '马村区', '马村区', '3', '410804', '410800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410903000000000', '濮阳高新区', '濮阳高新区', '濮阳高新区', '3', '410903', '410900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410781000000000', '卫辉市', '卫辉市', '卫辉市', '3', '410781', '410700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410811000000000', '山阳区', '山阳区', '山阳区', '3', '410811', '410800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410882000000000', '沁阳市', '沁阳市', '沁阳市', '3', '410882', '410800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410822000000000', '博爱县', '博爱县', '博爱县', '3', '410822', '410800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411121000000000', '舞阳县', '舞阳县', '舞阳县', '3', '411121', '411100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411003000000000', '东城区管委会', '东城区管委会', '东城区管委会', '3', '411003', '411000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411004000000000', '经济开发区管委会', '经济开发区管委会', '经济开发区管委会', '3', '411004', '411000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411023000000000', '许昌县', '许昌县', '许昌县', '3', '411023', '411000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410927000000000', '台前县', '台前县', '台前县', '3', '410927', '410900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411103000000000', '郾城区', '郾城区', '郾城区', '3', '411103', '411100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411081000000000', '禹州市', '禹州市', '禹州市', '3', '411081', '411000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411024000000000', '鄢陵县', '鄢陵县', '鄢陵县', '3', '411024', '411000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411082000000000', '长葛市', '长葛市', '长葛市', '3', '411082', '411000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411102000000000', '源汇区', '源汇区', '源汇区', '3', '411102', '411100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411104000000000', '召陵区', '召陵区', '召陵区', '3', '411104', '411100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411002000000000', '魏都区', '魏都区', '魏都区', '3', '411002', '411000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410928000000000', '濮阳县', '濮阳县', '濮阳县', '3', '410928', '410900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411025000000000', '襄城县', '襄城县', '襄城县', '3', '411025', '411000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411122000000000', '临颖县', '临颖县', '临颖县', '3', '411122', '411100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411321000000000', '南召县', '南召县', '南召县', '3', '411321', '411300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411202000000000', '湖滨区', '湖滨区', '湖滨区', '3', '411202', '411200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411282000000000', '灵宝市', '灵宝市', '灵宝市', '3', '411282', '411200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411303000000000', '卧龙区', '卧龙区', '卧龙区', '3', '411303', '411300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411222000000000', '陕县', '陕县', '陕县', '3', '411222', '411200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411322000000000', '方城县', '方城县', '方城县', '3', '411322', '411300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411302000000000', '宛城区', '宛城区', '宛城区', '3', '411302', '411300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411323000000000', '西峡县', '西峡县', '西峡县', '3', '411323', '411300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411324000000000', '镇平县', '镇平县', '镇平县', '3', '411324', '411300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411224000000000', '卢氏县', '卢氏县', '卢氏县', '3', '411224', '411200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411326000000000', '淅川县', '淅川县', '淅川县', '3', '411326', '411300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411203000000000', '开发区', '开发区', '开发区', '3', '411203', '411200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411221000000000', '渑池县', '渑池县', '渑池县', '3', '411221', '411200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411281000000000', '义马市', '义马市', '义马市', '3', '411281', '411200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411325000000000', '内乡县', '内乡县', '内乡县', '3', '411325', '411300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411330000000000', '桐柏县', '桐柏县', '桐柏县', '3', '411330', '411300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411329000000000', '新野县', '新野县', '新野县', '3', '411329', '411300000000000', 'Y', 'Y', null);
commit;


insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411403000000000', '睢阳区', '睢阳区', '睢阳区', '3', '411403', '411400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411423000000000', '宁陵县', '宁陵县', '宁陵县', '3', '411423', '411400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411424000000000', '柘城县', '柘城县', '柘城县', '3', '411424', '411400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411422000000000', '钬', '钬', '钬', '3', '411422', '411400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411328000000000', '唐河县', '唐河县', '唐河县', '3', '411328', '411300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411421000000000', '民权县', '民权县', '民权县', '3', '411421', '411400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411327000000000', '社旗县', '社旗县', '社旗县', '3', '411327', '411300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411381000000000', '邓州市', '邓州市', '邓州市', '3', '411381', '411300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411402000000000', '梁园区', '梁园区', '梁园区', '3', '411402', '411400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411503000000000', '平桥区', '平桥区', '平桥区', '3', '411503', '411500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411425000000000', '虞城县', '虞城县', '虞城县', '3', '411425', '411400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411481000000000', '永城市', '永城市', '永城市', '3', '411481', '411400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411522000000000', '光山县', '光山县', '光山县', '3', '411522', '411500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411524000000000', '商城县', '商城县', '商城县', '3', '411524', '411500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411502000000000', '浉河区', '浉河区', '浉河区', '3', '411502', '411500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411426000000000', '夏邑县', '夏邑县', '夏邑县', '3', '411426', '411400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411523000000000', '新县', '新县', '新县', '3', '411523', '411500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411525000000000', '固始县', '固始县', '固始县', '3', '411525', '411500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411521000000000', '罗山县', '罗山县', '罗山县', '3', '411521', '411500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411621000000000', '扶沟县', '扶沟县', '扶沟县', '3', '411621', '411600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411627000000000', '太康县', '太康县', '太康县', '3', '411627', '411600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411622000000000', '西华县', '西华县', '西华县', '3', '411622', '411600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411602000000000', '川汇区', '川汇区', '川汇区', '3', '411602', '411600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411626000000000', '淮阳县', '淮阳县', '淮阳县', '3', '411626', '411600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411526000000000', '潢川县', '潢川县', '潢川县', '3', '411526', '411500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411527000000000', '淮滨县', '淮滨县', '淮滨县', '3', '411527', '411500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411528000000000', '息县', '息县', '息县', '3', '411528', '411500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411625000000000', '郸城县', '郸城县', '郸城县', '3', '411625', '411600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411623000000000', '商水县', '商水县', '商水县', '3', '411623', '411600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411624000000000', '沈丘县', '沈丘县', '沈丘县', '3', '411624', '411600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411727000000000', '汝南县', '汝南县', '汝南县', '3', '411727', '411700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411728000000000', '遂平县', '遂平县', '遂平县', '3', '411728', '411700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411723000000000', '平舆县', '平舆县', '平舆县', '3', '411723', '411700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420102000000000', '江岸区', '江岸区', '江岸区', '3', '420102', '420100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411702000000000', '驿城区', '驿城区', '驿城区', '3', '411702', '411700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411681000000000', '项城市', '项城市', '项城市', '3', '411681', '411600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411726000000000', '泌阳县', '泌阳县', '泌阳县', '3', '411726', '411700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411628000000000', '鹿邑县', '鹿邑县', '鹿邑县', '3', '411628', '411600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411703000000000', '高新区', '高新区', '高新区', '3', '411703', '411700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411725000000000', '确山县', '确山县', '确山县', '3', '411725', '411700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411721000000000', '西平县', '西平县', '西平县', '3', '411721', '411700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411722000000000', '上蔡县', '上蔡县', '上蔡县', '3', '411722', '411700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411729000000000', '新蔡县', '新蔡县', '新蔡县', '3', '411729', '411700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411724000000000', '正阳县', '正阳县', '正阳县', '3', '411724', '411700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420106000000000', '武昌区', '武昌区', '武昌区', '3', '420106', '420100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420321000000000', '郧县', '郧县', '郧县', '3', '420321', '420300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420112000000000', '东西湖区', '东西湖区', '东西湖区', '3', '420112', '420100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420111000000000', '洪山区', '洪山区', '洪山区', '3', '420111', '420100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420117000000000', '新洲区', '新洲区', '新洲区', '3', '420117', '420100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420116000000000', '黄陂区', '黄陂区', '黄陂区', '3', '420116', '420100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420201000000000', '市辖区', '市辖区', '市辖区', '3', '420201', '420200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420202000000000', '黄石港区', '黄石港区', '黄石港区', '3', '420202', '420200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420105000000000', '汉阳区', '汉阳区', '汉阳区', '3', '420105', '420100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420203000000000', '西塞山区', '西塞山区', '西塞山区', '3', '420203', '420200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420302000000000', '茅箭区', '茅箭区', '茅箭区', '3', '420302', '420300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420204000000000', '下陆区', '下陆区', '下陆区', '3', '420204', '420200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420103000000000', '江汉区', '江汉区', '江汉区', '3', '420103', '420100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420303000000000', '张湾区', '张湾区', '张湾区', '3', '420303', '420300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420113000000000', '汉南区', '汉南区', '汉南区', '3', '420113', '420100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420205000000000', '铁山区', '铁山区', '铁山区', '3', '420205', '420200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420222000000000', '阳新县', '阳新县', '阳新县', '3', '420222', '420200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420115000000000', '江夏区', '江夏区', '江夏区', '3', '420115', '420100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420107000000000', '青山区', '青山区', '青山区', '3', '420107', '420100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420104000000000', '桥口区', '桥口区', '桥口区', '3', '420104', '420100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420114000000000', '蔡甸区', '蔡甸区', '蔡甸区', '3', '420114', '420100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420281000000000', '大冶市', '大冶市', '大冶市', '3', '420281', '420200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420504000000000', '点军区', '点军区', '点军区', '3', '420504', '420500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420607000000000', '襄阳区', '襄阳区', '襄阳区', '3', '420607', '420600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420322000000000', '郧西县', '郧西县', '郧西县', '3', '420322', '420300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420505000000000', '猇亭区', '猇亭区', '猇亭区', '3', '420505', '420500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420527000000000', '秭归县', '秭归县', '秭归县', '3', '420527', '420500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420525000000000', '远安县', '远安县', '远安县', '3', '420525', '420500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420381000000000', '丹江口市', '丹江口市', '丹江口市', '3', '420381', '420300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420581000000000', '宜都市', '宜都市', '宜都市', '3', '420581', '420500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420506000000000', '夷陵区', '夷陵区', '夷陵区', '3', '420506', '420500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420324000000000', '竹溪县', '竹溪县', '竹溪县', '3', '420324', '420300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420325000000000', '房县', '房县', '房县', '3', '420325', '420300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420502000000000', '西陵区', '西陵区', '西陵区', '3', '420502', '420500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420602000000000', '襄城区', '襄城区', '襄城区', '3', '420602', '420600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420526000000000', '兴山县', '兴山县', '兴山县', '3', '420526', '420500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420529000000000', '五峰土家族自治县', '五峰土家族自治县', '五峰土家族自治县', '3', '420529', '420500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420528000000000', '长阳土家族自治县', '长阳土家族自治县', '长阳土家族自治县', '3', '420528', '420500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420582000000000', '当阳市', '当阳市', '当阳市', '3', '420582', '420500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420583000000000', '枝江市', '枝江市', '枝江市', '3', '420583', '420500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420323000000000', '竹山县', '竹山县', '竹山县', '3', '420323', '420300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420503000000000', '伍家岗区', '伍家岗区', '伍家岗区', '3', '420503', '420500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420606000000000', '樊城区', '樊城区', '樊城区', '3', '420606', '420600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420683000000000', '枣阳市', '枣阳市', '枣阳市', '3', '420683', '420600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420982000000000', '安陆市', '安陆市', '安陆市', '3', '420982', '420900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420682000000000', '老河口市', '老河口市', '老河口市', '3', '420682', '420600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420704000000000', '鄂城区', '鄂城区', '鄂城区', '3', '420704', '420700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420902000000000', '孝南区', '孝南区', '孝南区', '3', '420902', '420900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430527000000000', '绥宁县', '绥宁县', '绥宁县', '3', '430527', '430500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430604000000000', '屈原管理区', '屈原管理区', '屈原管理区', '3', '430604', '430600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430528000000000', '新宁县民政局', '新宁县民政局', '新宁县民政局', '3', '430528', '430500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430605000000000', '岳阳经济开发区', '岳阳经济开发区', '岳阳经济开发区', '3', '430605', '430600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430623000000000', '华容县', '华容县', '华容县', '3', '430623', '430600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430602000000000', '岳阳楼区', '岳阳楼区', '岳阳楼区', '3', '430602', '430600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430606000000000', '南湖风景区', '南湖风景区', '南湖风景区', '3', '430606', '430600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430611000000000', '君山区', '君山区', '君山区', '3', '430611', '430600000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430524000000000', '隆回县', '隆回县', '隆回县', '3', '430524', '430500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430529000000000', '城步苗族自治县', '城步苗族自治县', '城步苗族自治县', '3', '430529', '430500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430721000000000', '安乡县', '安乡县', '安乡县', '3', '430721', '430700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430682000000000', '临湘市', '临湘市', '临湘市', '3', '430682', '430600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430702000000000', '武陵区', '武陵区', '武陵区', '3', '430702', '430700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430723000000000', '澧县', '澧县', '澧县', '3', '430723', '430700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430722000000000', '汉寿县', '汉寿县', '汉寿县', '3', '430722', '430700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430624000000000', '湘阴县', '湘阴县', '湘阴县', '3', '430624', '430600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430626000000000', '平江县', '平江县', '平江县', '3', '430626', '430600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430724000000000', '临澧县', '临澧县', '临澧县', '3', '430724', '430700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430703000000000', '鼎城区', '鼎城区', '鼎城区', '3', '430703', '430700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430681000000000', '汩罗市', '汩罗市', '汩罗市', '3', '430681', '430600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430921000000000', '南县', '南县', '南县', '3', '430921', '430900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430726000000000', '石门县', '石门县', '石门县', '3', '430726', '430700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430802000000000', '永定区', '永定区', '永定区', '3', '430802', '430800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430903000000000', '赫山区', '赫山区', '赫山区', '3', '430903', '430900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430811000000000', '武陵源区', '武陵源区', '武陵源区', '3', '430811', '430800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430725000000000', '桃源县', '桃源县', '桃源县', '3', '430725', '430700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430781000000000', '津市市', '津市市', '津市市', '3', '430781', '430700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430821000000000', '慈利县', '慈利县', '慈利县', '3', '430821', '430800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430902000000000', '资阳区', '资阳区', '资阳区', '3', '430902', '430900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430822000000000', '桑植县', '桑植县', '桑植县', '3', '430822', '430800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431021000000000', '桂阳县', '桂阳县', '桂阳县', '3', '431021', '431000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431022000000000', '宜章县', '宜章县', '宜章县', '3', '431022', '431000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430981000000000', '沅江市', '沅江市', '沅江市', '3', '430981', '430900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431025000000000', '临武县', '临武县', '临武县', '3', '431025', '431000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431003000000000', '苏仙区', '苏仙区', '苏仙区', '3', '431003', '431000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431023000000000', '永兴县', '永兴县', '永兴县', '3', '431023', '431000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430923000000000', '安化县', '安化县', '安化县', '3', '430923', '430900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430922000000000', '桃江县', '桃江县', '桃江县', '3', '430922', '430900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431002000000000', '北湖区', '北湖区', '北湖区', '3', '431002', '431000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431024000000000', '嘉禾县', '嘉禾县', '嘉禾县', '3', '431024', '431000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431122000000000', '东安县', '东安县', '东安县', '3', '431122', '431100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431102000000000', '零陵区', '零陵区', '零陵区', '3', '431102', '431100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431081000000000', '资兴市', '资兴市', '资兴市', '3', '431081', '431000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431121000000000', '祁阳县', '祁阳县', '祁阳县', '3', '431121', '431100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431125000000000', '江永县', '江永县', '江永县', '3', '431125', '431100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431124000000000', '道县', '道县', '道县', '3', '431124', '431100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431028000000000', '安仁县', '安仁县', '安仁县', '3', '431028', '431000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431126000000000', '宁远县', '宁远县', '宁远县', '3', '431126', '431100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431123000000000', '双牌县', '双牌县', '双牌县', '3', '431123', '431100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431026000000000', '汝城县', '汝城县', '汝城县', '3', '431026', '431000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431027000000000', '桂东县', '桂东县', '桂东县', '3', '431027', '431000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431103000000000', '冷水滩区', '冷水滩区', '冷水滩区', '3', '431103', '431100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431128000000000', '新田县', '新田县', '新田县', '3', '431128', '431100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431202000000000', '鹤城区', '鹤城区', '鹤城区', '3', '431202', '431200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431222000000000', '沅陵县', '沅陵县', '沅陵县', '3', '431222', '431200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431225000000000', '会同县', '会同县', '会同县', '3', '431225', '431200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431226000000000', '麻阳苗族自治县', '麻阳苗族自治县', '麻阳苗族自治县', '3', '431226', '431200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431129000000000', '江华瑶族自治县', '江华瑶族自治县', '江华瑶族自治县', '3', '431129', '431100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431221000000000', '中方县', '中方县', '中方县', '3', '431221', '431200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431223000000000', '辰溪县', '辰溪县', '辰溪县', '3', '431223', '431200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431224000000000', '溆浦县', '溆浦县', '溆浦县', '3', '431224', '431200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431127000000000', '蓝山县', '蓝山县', '蓝山县', '3', '431127', '431100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431382000000000', '涟源市', '涟源市', '涟源市', '3', '431382', '431300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431381000000000', '冷水江市', '冷水江市', '冷水江市', '3', '431381', '431300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431230000000000', '通道侗族自治县', '通道侗族自治县', '通道侗族自治县', '3', '431230', '431200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431281000000000', '洪江市', '洪江市', '洪江市', '3', '431281', '431200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431322000000000', '新化县', '新化县', '新化县', '3', '431322', '431300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431227000000000', '新晃侗族自治县', '新晃侗族自治县', '新晃侗族自治县', '3', '431227', '431200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431321000000000', '双峰县', '双峰县', '双峰县', '3', '431321', '431300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431228000000000', '芷江侗族自治县', '芷江侗族自治县', '芷江侗族自治县', '3', '431228', '431200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431302000000000', '娄星区', '娄星区', '娄星区', '3', '431302', '431300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431229000000000', '靖州苗族侗族县', '靖州苗族侗族县', '靖州苗族侗族县', '3', '431229', '431200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('433124000000000', '花垣县', '花垣县', '花垣县', '3', '433124', '433100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('433125000000000', '保靖县', '保靖县', '保靖县', '3', '433125', '433100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('433130000000000', '龙山县', '龙山县', '龙山县', '3', '433130', '433100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440113000000000', '番禺区', '番禺区', '番禺区', '3', '440113', '440100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('433127000000000', '永顺县', '永顺县', '永顺县', '3', '433127', '433100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440106000000000', '天河区', '天河区', '天河区', '3', '440106', '440100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('433123000000000', '凤凰县', '凤凰县', '凤凰县', '3', '433123', '433100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440111000000000', '白云区', '白云区', '白云区', '3', '440111', '440100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431383000000000', '市经济技术开发区', '市经济技术开发区', '市经济技术开发区', '3', '431383', '431300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440104000000000', '越秀区', '越秀区', '越秀区', '3', '440104', '440100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('433101000000000', '吉首市', '吉首市', '吉首市', '3', '433101', '433100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440103000000000', '荔湾区', '荔湾区', '荔湾区', '3', '440103', '440100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('433122000000000', '泸溪县', '泸溪县', '泸溪县', '3', '433122', '433100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('433126000000000', '古丈县', '古丈县', '古丈县', '3', '433126', '433100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440105000000000', '海珠区', '海珠区', '海珠区', '3', '440105', '440100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440112000000000', '黄埔区', '黄埔区', '黄埔区', '3', '440112', '440100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440308000000000', '盐田区', '盐田区', '盐田区', '3', '440308', '440300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440304000000000', '福田区', '福田区', '福田区', '3', '440304', '440300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440513000000000', '潮阳区', '潮阳区', '潮阳区', '3', '440513', '440500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440114000000000', '花都区', '花都区', '花都区', '3', '440114', '440100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440116000000000', '萝岗区', '萝岗区', '萝岗区', '3', '440116', '440100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440204000000000', '浈江区', '浈江区', '浈江区', '3', '440204', '440200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440402000000000', '香洲区', '香洲区', '香洲区', '3', '440402', '440400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440233000000000', '新丰县', '新丰县', '新丰县', '3', '440233', '440200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440404000000000', '金湾区', '金湾区', '金湾区', '3', '440404', '440400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440502000000000', '濠江区', '濠江区', '濠江区', '3', '440502', '440500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440282000000000', '南雄市', '南雄市', '南雄市', '3', '440282', '440200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440303000000000', '罗湖区', '罗湖区', '罗湖区', '3', '440303', '440300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440305000000000', '南山区', '南山区', '南山区', '3', '440305', '440300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440503000000000', '龙湖区', '龙湖区', '龙湖区', '3', '440503', '440500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440307000000000', '龙岗区', '龙岗区', '龙岗区', '3', '440307', '440300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440205000000000', '曲江区', '曲江区', '曲江区', '3', '440205', '440200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440183000000000', '增城市', '增城市', '增城市', '3', '440183', '440100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440224000000000', '仁化县', '仁化县', '仁化县', '3', '440224', '440200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440232000000000', '乳源瑶族自治县', '乳源瑶族自治县', '乳源瑶族自治县', '3', '440232', '440200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440281000000000', '乐昌市', '乐昌市', '乐昌市', '3', '440281', '440200000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440403000000000', '斗门区', '斗门区', '斗门区', '3', '440403', '440400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440306000000000', '宝安区', '宝安区', '宝安区', '3', '440306', '440300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440115000000000', '南沙区', '南沙区', '南沙区', '3', '440115', '440100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440506000000000', '澄海区', '澄海区', '澄海区', '3', '440506', '440500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440511000000000', '金平区', '金平区', '金平区', '3', '440511', '440500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440203000000000', '武江区', '武江区', '武江区', '3', '440203', '440200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440222000000000', '始兴县', '始兴县', '始兴县', '3', '440222', '440200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440184000000000', '从化市', '从化市', '从化市', '3', '440184', '440100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440229000000000', '翁源县', '翁源县', '翁源县', '3', '440229', '440200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440515000000000', '澄海区', '澄海区', '澄海区', '3', '440515', '440500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440606000000000', '高明区', '高明区', '高明区', '3', '440606', '440600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440703000000000', '蓬江区', '蓬江区', '蓬江区', '3', '440703', '440700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440803000000000', '霞山区', '霞山区', '霞山区', '3', '440803', '440800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440823000000000', '遂溪县', '遂溪县', '遂溪县', '3', '440823', '440800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440784000000000', '鹤山市', '鹤山市', '鹤山市', '3', '440784', '440700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440783000000000', '开平市', '开平市', '开平市', '3', '440783', '440700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440607000000000', '顺德区', '顺德区', '顺德区', '3', '440607', '440600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440781000000000', '台山市', '台山市', '台山市', '3', '440781', '440700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440804000000000', '坡头区', '坡头区', '坡头区', '3', '440804', '440800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440523000000000', '南澳县', '南澳县', '南澳县', '3', '440523', '440500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440704000000000', '江海区', '江海区', '江海区', '3', '440704', '440700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440604000000000', '南海区', '南海区', '南海区', '3', '440604', '440600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440514000000000', '潮南区', '潮南区', '潮南区', '3', '440514', '440500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440811000000000', '麻章区', '麻章区', '麻章区', '3', '440811', '440800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440705000000000', '新会市', '新会市', '新会市', '3', '440705', '440700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440881000000000', '廉江市', '廉江市', '廉江市', '3', '440881', '440800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440605000000000', '三水区', '三水区', '三水区', '3', '440605', '440600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440785000000000', '恩平市', '恩平市', '恩平市', '3', '440785', '440700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440802000000000', '赤坎区', '赤坎区', '赤坎区', '3', '440802', '440800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440825000000000', '徐闻县', '徐闻县', '徐闻县', '3', '440825', '440800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440882000000000', '雷州市', '雷州市', '雷州市', '3', '440882', '440800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440602000000000', '禅城区', '禅城区', '禅城区', '3', '440602', '440600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440902000000000', '茂南区', '茂南区', '茂南区', '3', '440902', '440900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440923000000000', '电白县', '电白县', '电白县', '3', '440923', '440900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440983000000000', '信宜市', '信宜市', '信宜市', '3', '440983', '440900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441303000000000', '惠阳区', '惠阳区', '惠阳区', '3', '441303', '441300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441302000000000', '惠城区', '惠城区', '惠城区', '3', '441302', '441300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441202000000000', '端州区', '端州区', '端州区', '3', '441202', '441200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441283000000000', '高要市', '高要市', '高要市', '3', '441283', '441200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441284000000000', '四会市', '四会市', '四会市', '3', '441284', '441200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440883000000000', '吴川市', '吴川市', '吴川市', '3', '440883', '440800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441322000000000', '博罗县', '博罗县', '博罗县', '3', '441322', '441300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440981000000000', '高州市', '高州市', '高州市', '3', '440981', '440900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441203000000000', '鼎湖区', '鼎湖区', '鼎湖区', '3', '441203', '441200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441226000000000', '德庆县', '德庆县', '德庆县', '3', '441226', '441200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440903000000000', '茂港区', '茂港区', '茂港区', '3', '440903', '440900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440982000000000', '化州市', '化州市', '化州市', '3', '440982', '440900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441225000000000', '封开县', '封开县', '封开县', '3', '441225', '441200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441224000000000', '怀集县', '怀集县', '怀集县', '3', '441224', '441200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441223000000000', '广宁县', '广宁县', '广宁县', '3', '441223', '441200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441324000000000', '龙门县', '龙门县', '龙门县', '3', '441324', '441300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441427000000000', '蕉岭县', '蕉岭县', '蕉岭县', '3', '441427', '441400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441502000000000', '城区', '城区', '城区', '3', '441502', '441500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441581000000000', '陆丰市', '陆丰市', '陆丰市', '3', '441581', '441500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441421000000000', '梅县', '梅县', '梅县', '3', '441421', '441400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441602000000000', '源城', '源城', '源城', '3', '441602', '441600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441424000000000', '五华县民政局', '五华县民政局', '五华县民政局', '3', '441424', '441400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441323000000000', '惠东县', '惠东县', '惠东县', '3', '441323', '441300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441423000000000', '丰顺县', '丰顺县', '丰顺县', '3', '441423', '441400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441481000000000', '兴宁市', '兴宁市', '兴宁市', '3', '441481', '441400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441422000000000', '大埔县', '大埔县', '大埔县', '3', '441422', '441400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441623000000000', '连平县', '连平县', '连平县', '3', '441623', '441600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441622000000000', '龙川县', '龙川县', '龙川县', '3', '441622', '441600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441621000000000', '紫金县', '紫金县', '紫金县', '3', '441621', '441600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441426000000000', '平远县', '平远县', '平远县', '3', '441426', '441400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441521000000000', '海丰县', '海丰县', '海丰县', '3', '441521', '441500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441523000000000', '陆河县', '陆河县', '陆河县', '3', '441523', '441500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441402000000000', '梅江区', '梅江区', '梅江区', '3', '441402', '441400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441821000000000', '佛冈县', '佛冈县', '佛冈县', '3', '441821', '441800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445102000000000', '湘桥区', '湘桥区', '湘桥区', '3', '445102', '445100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441625000000000', '东源县', '东源县', '东源县', '3', '441625', '441600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445121000000000', '潮安县', '潮安县', '潮安县', '3', '445121', '445100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441702000000000', '江城区', '江城区', '江城区', '3', '441702', '441700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441825000000000', '连山壮族瑶族自治县', '连山壮族瑶族自治县', '连山壮族瑶族自治县', '3', '441825', '441800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441781000000000', '阳春市', '阳春市', '阳春市', '3', '441781', '441700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441823000000000', '阳山县', '阳山县', '阳山县', '3', '441823', '441800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441826000000000', '连南瑶族自治县', '连南瑶族自治县', '连南瑶族自治县', '3', '441826', '441800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441802000000000', '清城区', '清城区', '清城区', '3', '441802', '441800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445122000000000', '饶平县', '饶平县', '饶平县', '3', '445122', '445100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441721000000000', '阳西县', '阳西县', '阳西县', '3', '441721', '441700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441827000000000', '清新县', '清新县', '清新县', '3', '441827', '441800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441881000000000', '英德市', '英德市', '英德市', '3', '441881', '441800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441624000000000', '和平县', '和平县', '和平县', '3', '441624', '441600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441723000000000', '阳东县', '阳东县', '阳东县', '3', '441723', '441700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441882000000000', '连州市', '连州市', '连州市', '3', '441882', '441800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445281000000000', '普宁市', '普宁市', '普宁市', '3', '445281', '445200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450102000000000', '兴宁区', '兴宁区', '兴宁区', '3', '450102', '450100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445321000000000', '新兴县', '新兴县', '新兴县', '3', '445321', '445300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450107000000000', '西乡塘区', '西乡塘区', '西乡塘区', '3', '450107', '450100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450109000000000', '邕宁区', '邕宁区', '邕宁区', '3', '450109', '450100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450123000000000', '隆安县', '隆安县', '隆安县', '3', '450123', '450100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420822000000000', '沙洋县', '沙洋县', '沙洋县', '3', '420822', '420800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420625000000000', '谷城县', '谷城县', '谷城县', '3', '420625', '420600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420626000000000', '保康县', '保康县', '保康县', '3', '420626', '420600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420804000000000', '掇刀区', '掇刀区', '掇刀区', '3', '420804', '420800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420702000000000', '粱子湖区', '粱子湖区', '粱子湖区', '3', '420702', '420700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420881000000000', '钟祥市', '钟祥市', '钟祥市', '3', '420881', '420800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420624000000000', '南漳县民政局', '南漳县民政局', '南漳县民政局', '3', '420624', '420600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420684000000000', '宜城市', '宜城市', '宜城市', '3', '420684', '420600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420821000000000', '京山县', '京山县', '京山县', '3', '420821', '420800000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420703000000000', '华容区', '华容区', '华容区', '3', '420703', '420700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420802000000000', '东宝区', '东宝区', '东宝区', '3', '420802', '420800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421003000000000', '荆州区', '荆州区', '荆州区', '3', '421003', '421000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421023000000000', '监利县', '监利县', '监利县', '3', '421023', '421000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420921000000000', '孝昌县', '孝昌县', '孝昌县', '3', '420921', '420900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421022000000000', '公安县', '公安县', '公安县', '3', '421022', '421000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420981000000000', '应城市', '应城市', '应城市', '3', '420981', '420900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420984000000000', '汉川市', '汉川市', '汉川市', '3', '420984', '420900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421024000000000', '江陵县', '江陵县', '江陵县', '3', '421024', '421000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420922000000000', '大悟县', '大悟县', '大悟县', '3', '420922', '420900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420923000000000', '云梦县', '云梦县', '云梦县', '3', '420923', '420900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421002000000000', '沙市区', '沙市区', '沙市区', '3', '421002', '421000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421083000000000', '洪湖市', '洪湖市', '洪湖市', '3', '421083', '421000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421087000000000', '松滋市', '松滋市', '松滋市', '3', '421087', '421000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421124000000000', '英山县', '英山县', '英山县', '3', '421124', '421100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421123000000000', '黄冈市罗田县', '黄冈市罗田县', '黄冈市罗田县', '3', '421123', '421100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421081000000000', '石首市', '石首市', '石首市', '3', '421081', '421000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421102000000000', '黄州区', '黄州区', '黄州区', '3', '421102', '421100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421126000000000', '黄冈市蕲春县', '黄冈市蕲春县', '黄冈市蕲春县', '3', '421126', '421100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421122000000000', '红安县民政局', '红安县民政局', '红安县民政局', '3', '421122', '421100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421121000000000', '团风县', '团风县', '团风县', '3', '421121', '421100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421125000000000', '浠水县', '浠水县', '浠水县', '3', '421125', '421100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421222000000000', '通城县', '通城县', '通城县', '3', '421222', '421200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421202000000000', '咸宁市咸安区民政局', '咸宁市咸安区民政局', '咸宁市咸安区民政局', '3', '421202', '421200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421181000000000', '麻城市', '麻城市', '麻城市', '3', '421181', '421100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421224000000000', '通山县', '通山县', '通山县', '3', '421224', '421200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421223000000000', '崇阳县', '崇阳县', '崇阳县', '3', '421223', '421200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421281000000000', '赤壁市', '赤壁市', '赤壁市', '3', '421281', '421200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421221000000000', '嘉鱼县', '嘉鱼县', '嘉鱼县', '3', '421221', '421200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421182000000000', '武穴市', '武穴市', '武穴市', '3', '421182', '421100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421302000000000', '曾都区', '曾都区', '曾都区', '3', '421302', '421300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421127000000000', '黄梅县', '黄梅县', '黄梅县', '3', '421127', '421100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('422822000000000', '建始县', '建始县', '建始县', '3', '422822', '422800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('422825000000000', '宣恩县', '宣恩县', '宣恩县', '3', '422825', '422800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('422826000000000', '咸丰县', '咸丰县', '咸丰县', '3', '422826', '422800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('422823000000000', '巴东县', '巴东县', '巴东县', '3', '422823', '422800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('429004000000000', '仙桃市', '仙桃市', '仙桃市', '3', '429004', '429000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('422801000000000', '恩施市', '恩施市', '恩施市', '3', '422801', '422800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('429005000000000', '潜江市', '潜江市', '潜江市', '3', '429005', '429000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('422828000000000', '鹤峰县', '鹤峰县', '鹤峰县', '3', '422828', '422800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421381000000000', '广水市', '广水市', '广水市', '3', '421381', '421300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('422802000000000', '利川市', '利川市', '利川市', '3', '422802', '422800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('429006000000000', '天门市', '天门市', '天门市', '3', '429006', '429000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('422827000000000', '来凤县', '来凤县', '来凤县', '3', '422827', '422800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430111000000000', '雨花区', '雨花区', '雨花区', '3', '430111', '430100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430104000000000', '岳麓区', '岳麓区', '岳麓区', '3', '430104', '430100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430225000000000', '炎陵县', '炎陵县', '炎陵县', '3', '430225', '430200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430204000000000', '石峰区', '石峰区', '石峰区', '3', '430204', '430200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430223000000000', '攸县', '攸县', '攸县', '3', '430223', '430200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430102000000000', '芙蓉区', '芙蓉区', '芙蓉区', '3', '430102', '430100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430281000000000', '醴陵市', '醴陵市', '醴陵市', '3', '430281', '430200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430122000000000', '望城县', '望城县', '望城县', '3', '430122', '430100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430181000000000', '浏阳市', '浏阳市', '浏阳市', '3', '430181', '430100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430105000000000', '开福区', '开福区', '开福区', '3', '430105', '430100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430121000000000', '长沙县', '长沙县', '长沙县', '3', '430121', '430100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430211000000000', '天元区', '天元区', '天元区', '3', '430211', '430200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('429021000000000', '神农架林区', '神农架林区', '神农架林区', '3', '429021', '429000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430202000000000', '荷塘区', '荷塘区', '荷塘区', '3', '430202', '430200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430103000000000', '天心区', '天心区', '天心区', '3', '430103', '430100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430203000000000', '芦淞区民政局', '芦淞区民政局', '芦淞区民政局', '3', '430203', '430200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430124000000000', '宁乡县', '宁乡县', '宁乡县', '3', '430124', '430100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430221000000000', '株洲县', '株洲县', '株洲县', '3', '430221', '430200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430224000000000', '茶陵县', '茶陵县', '茶陵县', '3', '430224', '430200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430423000000000', '衡山县', '衡山县', '衡山县', '3', '430423', '430400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430305000000000', '高新区', '高新区', '高新区', '3', '430305', '430300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430321000000000', '湘潭县', '湘潭县', '湘潭县', '3', '430321', '430300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430424000000000', '衡东县', '衡东县', '衡东县', '3', '430424', '430400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430302000000000', '雨湖区', '雨湖区', '雨湖区', '3', '430302', '430300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430408000000000', '蒸湘区', '蒸湘区', '蒸湘区', '3', '430408', '430400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430405000000000', '珠晖区', '珠晖区', '珠晖区', '3', '430405', '430400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430304000000000', '岳塘区', '岳塘区', '岳塘区', '3', '430304', '430300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430412000000000', '南岳区', '南岳区', '南岳区', '3', '430412', '430400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430421000000000', '衡阳县', '衡阳县', '衡阳县', '3', '430421', '430400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430422000000000', '衡南县', '衡南县', '衡南县', '3', '430422', '430400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430406000000000', '雁峰区', '雁峰区', '雁峰区', '3', '430406', '430400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430381000000000', '湘乡市', '湘乡市', '湘乡市', '3', '430381', '430300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430407000000000', '石鼓区', '石鼓区', '石鼓区', '3', '430407', '430400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430382000000000', '韶山市', '韶山市', '韶山市', '3', '430382', '430300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430502000000000', '双清区', '双清区', '双清区', '3', '430502', '430500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430521000000000', '邵东县', '邵东县', '邵东县', '3', '430521', '430500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430523000000000', '邵阳县民政局', '邵阳县民政局', '邵阳县民政局', '3', '430523', '430500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430503000000000', '大祥区', '大祥区', '大祥区', '3', '430503', '430500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430426000000000', '祁东县', '祁东县', '祁东县', '3', '430426', '430400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430481000000000', '耒阳市', '耒阳市', '耒阳市', '3', '430481', '430400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430482000000000', '常宁市', '常宁市', '常宁市', '3', '430482', '430400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430522000000000', '新邵县', '新邵县', '新邵县', '3', '430522', '430500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430511000000000', '北塔区', '北塔区', '北塔区', '3', '430511', '430500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430603000000000', '云溪区', '云溪区', '云溪区', '3', '430603', '430600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430581000000000', '武冈市', '武冈市', '武冈市', '3', '430581', '430500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430525000000000', '洞口县', '洞口县', '洞口县', '3', '430525', '430500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430621000000000', '岳阳县', '岳阳县', '岳阳县', '3', '430621', '430600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361027000000000', '金溪县', '金溪县', '金溪县', '3', '361027', '361000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360881000000000', '井冈山市', '井冈山市', '井冈山市', '3', '360881', '360800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360923000000000', '上高县', '上高县', '上高县', '3', '360923', '360900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361002000000000', '临川区', '临川区', '临川区', '3', '361002', '361000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360981000000000', '丰城市', '丰城市', '丰城市', '3', '360981', '360900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360982000000000', '樟树市', '樟树市', '樟树市', '3', '360982', '360900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360983000000000', '高安市', '高安市', '高安市', '3', '360983', '360900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361129000000000', '万年县', '万年县', '万年县', '3', '361129', '361100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361102000000000', '信州区', '信州区', '信州区', '3', '361102', '361100000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361125000000000', '横峰县', '横峰县', '横峰县', '3', '361125', '361100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361122000000000', '广丰县', '广丰县', '广丰县', '3', '361122', '361100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370103000000000', '市中区', '市中区', '市中区', '3', '370103', '370100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361181000000000', '德兴市', '德兴市', '德兴市', '3', '361181', '361100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370104000000000', '槐荫区', '槐荫区', '槐荫区', '3', '370104', '370100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361126000000000', '弋阳县', '弋阳县', '弋阳县', '3', '361126', '361100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361124000000000', '铅山县', '铅山县', '铅山县', '3', '361124', '361100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370113000000000', '长清区', '长清区', '长清区', '3', '370113', '370100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361123000000000', '玉山县', '玉山县', '玉山县', '3', '361123', '361100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361130000000000', '婺源县', '婺源县', '婺源县', '3', '361130', '361100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361030000000000', '广昌县', '广昌县', '广昌县', '3', '361030', '361000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361121000000000', '上饶县', '上饶县', '上饶县', '3', '361121', '361100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361128000000000', '鄱阳县', '鄱阳县', '鄱阳县', '3', '361128', '361100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370105000000000', '天桥区', '天桥区', '天桥区', '3', '370105', '370100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370112000000000', '历城区', '历城区', '历城区', '3', '370112', '370100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370102000000000', '历下区', '历下区', '历下区', '3', '370102', '370100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361127000000000', '余干县', '余干县', '余干县', '3', '361127', '361100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370211000000000', '黄岛区', '黄岛区', '黄岛区', '3', '370211', '370200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370213000000000', '李沧区', '李沧区', '李沧区', '3', '370213', '370200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370281000000000', '胶州市', '胶州市', '胶州市', '3', '370281', '370200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370125000000000', '济阳县', '济阳县', '济阳县', '3', '370125', '370100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370203000000000', '市北区', '市北区', '市北区', '3', '370203', '370200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370114000000000', '济南市开发区', '济南市开发区', '济南市开发区', '3', '370114', '370100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370214000000000', '城阳区', '城阳区', '城阳区', '3', '370214', '370200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370212000000000', '崂山区', '崂山区', '崂山区', '3', '370212', '370200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370205000000000', '四方区', '四方区', '四方区', '3', '370205', '370200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370126000000000', '商河县', '商河县', '商河县', '3', '370126', '370100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370202000000000', '市南区', '市南区', '市南区', '3', '370202', '370200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370124000000000', '平阴县', '平阴县', '平阴县', '3', '370124', '370100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370181000000000', '章丘市', '章丘市', '章丘市', '3', '370181', '370100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370284000000000', '胶南市', '胶南市', '胶南市', '3', '370284', '370200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370302000000000', '淄川区', '淄川区', '淄川区', '3', '370302', '370300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370285000000000', '莱西市', '莱西市', '莱西市', '3', '370285', '370200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370282000000000', '即墨市', '即墨市', '即墨市', '3', '370282', '370200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370283000000000', '平度市', '平度市', '平度市', '3', '370283', '370200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370323000000000', '沂源县', '沂源县', '沂源县', '3', '370323', '370300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370321000000000', '桓台县', '桓台县', '桓台县', '3', '370321', '370300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370402000000000', '市中区', '市中区', '市中区', '3', '370402', '370400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370404000000000', '峄城区', '峄城区', '峄城区', '3', '370404', '370400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370405000000000', '台儿庄区', '台儿庄区', '台儿庄区', '3', '370405', '370400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370304000000000', '博山区', '博山区', '博山区', '3', '370304', '370300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370322000000000', '高青县', '高青县', '高青县', '3', '370322', '370300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370403000000000', '薛城区', '薛城区', '薛城区', '3', '370403', '370400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370303000000000', '张店区', '张店区', '张店区', '3', '370303', '370300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370306000000000', '周村区', '周村区', '周村区', '3', '370306', '370300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370305000000000', '临淄区', '临淄区', '临淄区', '3', '370305', '370300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370307000000000', '淄博市高新技术开发区', '淄博市高新技术开发区', '淄博市高新技术开发区', '3', '370307', '370300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370611000000000', '福山区', '福山区', '福山区', '3', '370611', '370600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370523000000000', '广饶县', '广饶县', '广饶县', '3', '370523', '370500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370602000000000', '芝罘区', '芝罘区', '芝罘区', '3', '370602', '370600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370522000000000', '利津县', '利津县', '利津县', '3', '370522', '370500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370521000000000', '垦利县', '垦利县', '垦利县', '3', '370521', '370500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370406000000000', '山亭区', '山亭区', '山亭区', '3', '370406', '370400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370481000000000', '滕州市', '滕州市', '滕州市', '3', '370481', '370400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370503000000000', '河口区', '河口区', '河口区', '3', '370503', '370500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370613000000000', '莱山区', '莱山区', '莱山区', '3', '370613', '370600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370612000000000', '牟平区', '牟平区', '牟平区', '3', '370612', '370600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370499000000000', '枣庄市高新技术开发区', '枣庄市高新技术开发区', '枣庄市高新技术开发区', '3', '370499', '370400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370502000000000', '东营区', '东营区', '东营区', '3', '370502', '370500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370634000000000', '长岛县', '长岛县', '长岛县', '3', '370634', '370600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370681000000000', '龙口市', '龙口市', '龙口市', '3', '370681', '370600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370686000000000', '栖霞市', '栖霞市', '栖霞市', '3', '370686', '370600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370682000000000', '莱阳市', '莱阳市', '莱阳市', '3', '370682', '370600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370684000000000', '蓬莱市', '蓬莱市', '蓬莱市', '3', '370684', '370600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370614000000000', '开发区', '开发区', '开发区', '3', '370614', '370600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370683000000000', '莱州市', '莱州市', '莱州市', '3', '370683', '370600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370685000000000', '招远市', '招远市', '招远市', '3', '370685', '370600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370705000000000', '奎文区', '奎文区', '奎文区', '3', '370705', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370707000000000', '潍坊市海洋技术开发区', '潍坊市海洋技术开发区', '潍坊市海洋技术开发区', '3', '370707', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370702000000000', '潍城区', '潍城区', '潍城区', '3', '370702', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370724000000000', '临朐县', '临朐县', '临朐县', '3', '370724', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370725000000000', '昌乐县', '昌乐县', '昌乐县', '3', '370725', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370704000000000', '坊子区', '坊子区', '坊子区', '3', '370704', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370687000000000', '海阳市', '海阳市', '海阳市', '3', '370687', '370600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370706000000000', '潍坊市高新技术开发区', '潍坊市高新技术开发区', '潍坊市高新技术开发区', '3', '370706', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370703000000000', '寒亭区', '寒亭区', '寒亭区', '3', '370703', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370781000000000', '青州市', '青州市', '青州市', '3', '370781', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370782000000000', '诸城市', '诸城市', '诸城市', '3', '370782', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370783000000000', '寿光市', '寿光市', '寿光市', '3', '370783', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370784000000000', '安丘市', '安丘市', '安丘市', '3', '370784', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370786000000000', '昌邑市', '昌邑市', '昌邑市', '3', '370786', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370799000000000', '潍坊经济技术开发区', '潍坊经济技术开发区', '潍坊经济技术开发区', '3', '370799', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370802000000000', '市中区', '市中区', '市中区', '3', '370802', '370800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370827000000000', '鱼台县', '鱼台县', '鱼台县', '3', '370827', '370800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370828000000000', '金乡县', '金乡县', '金乡县', '3', '370828', '370800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370812000000000', '济宁市高新技术开发区', '济宁市高新技术开发区', '济宁市高新技术开发区', '3', '370812', '370800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370826000000000', '微山县', '微山县', '微山县', '3', '370826', '370800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370829000000000', '嘉祥县', '嘉祥县', '嘉祥县', '3', '370829', '370800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370785000000000', '高密市', '高密市', '高密市', '3', '370785', '370700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370811000000000', '任城区', '任城区', '任城区', '3', '370811', '370800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370883000000000', '邹城市', '邹城市', '邹城市', '3', '370883', '370800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370902000000000', '泰山区', '泰山区', '泰山区', '3', '370902', '370900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370831000000000', '泗水县', '泗水县', '泗水县', '3', '370831', '370800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370832000000000', '梁山县', '梁山县', '梁山县', '3', '370832', '370800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370903000000000', '岱岳区', '岱岳区', '岱岳区', '3', '370903', '370900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370830000000000', '汶上县', '汶上县', '汶上县', '3', '370830', '370800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370882000000000', '兖州市', '兖州市', '兖州市', '3', '370882', '370800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370881000000000', '曲阜市', '曲阜市', '曲阜市', '3', '370881', '370800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371003000000000', '威海市经济技术开发区', '威海市经济技术开发区', '威海市经济技术开发区', '3', '371003', '371000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371002000000000', '环翠区', '环翠区', '环翠区', '3', '371002', '371000000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371004000000000', '威海市高新技术开发区', '威海市高新技术开发区', '威海市高新技术开发区', '3', '371004', '371000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370921000000000', '宁阳县', '宁阳县', '宁阳县', '3', '370921', '370900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371081000000000', '文登市', '文登市', '文登市', '3', '371081', '371000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370923000000000', '东平县', '东平县', '东平县', '3', '370923', '370900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370904000000000', '泰安市高新技术开发区', '泰安市高新技术开发区', '泰安市高新技术开发区', '3', '370904', '370900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370983000000000', '肥城市', '肥城市', '肥城市', '3', '370983', '370900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370982000000000', '新泰市', '新泰市', '新泰市', '3', '370982', '370900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371122000000000', '莒县', '莒县', '莒县', '3', '371122', '371100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371083000000000', '乳山市', '乳山市', '乳山市', '3', '371083', '371000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371082000000000', '荣成市', '荣成市', '荣成市', '3', '371082', '371000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371102000000000', '东港区民政局', '东港区民政局', '东港区民政局', '3', '371102', '371100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371121000000000', '五莲县', '五莲县', '五莲县', '3', '371121', '371100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371103000000000', '岚山区', '岚山区', '岚山区', '3', '371103', '371100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371323000000000', '沂水县', '沂水县', '沂水县', '3', '371323', '371300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371204000000000', '开发区', '开发区', '开发区', '3', '371204', '371200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371202000000000', '莱城区', '莱城区', '莱城区', '3', '371202', '371200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371302000000000', '兰山区', '兰山区', '兰山区', '3', '371302', '371300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371203000000000', '钢城区', '钢城区', '钢城区', '3', '371203', '371200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371312000000000', '河东区', '河东区', '河东区', '3', '371312', '371300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371199000000000', '日照市开发区', '日照市开发区', '日照市开发区', '3', '371199', '371100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371311000000000', '罗庄区', '罗庄区', '罗庄区', '3', '371311', '371300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371322000000000', '郯城县', '郯城县', '郯城县', '3', '371322', '371300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371321000000000', '沂南县', '沂南县', '沂南县', '3', '371321', '371300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371326000000000', '平邑县', '平邑县', '平邑县', '3', '371326', '371300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371324000000000', '苍山县', '苍山县', '苍山县', '3', '371324', '371300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371327000000000', '莒南县', '莒南县', '莒南县', '3', '371327', '371300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371325000000000', '费县', '费县', '费县', '3', '371325', '371300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371424000000000', '临邑县', '临邑县', '临邑县', '3', '371424', '371400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371328000000000', '蒙阴县', '蒙阴县', '蒙阴县', '3', '371328', '371300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371422000000000', '宁津县', '宁津县', '宁津县', '3', '371422', '371400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371421000000000', '陵县', '陵县', '陵县', '3', '371421', '371400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371398000000000', '临沂市经济开发区', '临沂市经济开发区', '临沂市经济开发区', '3', '371398', '371300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371329000000000', '临沭县', '临沭县', '临沭县', '3', '371329', '371300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371423000000000', '庆云县', '庆云县', '庆云县', '3', '371423', '371400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371399000000000', '临沂市高新技术开发区', '临沂市高新技术开发区', '临沂市高新技术开发区', '3', '371399', '371300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371402000000000', '德城区', '德城区', '德城区', '3', '371402', '371400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371426000000000', '平原县', '平原县', '平原县', '3', '371426', '371400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371482000000000', '禹城市', '禹城市', '禹城市', '3', '371482', '371400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371428000000000', '武城县', '武城县', '武城县', '3', '371428', '371400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371425000000000', '齐河县', '齐河县', '齐河县', '3', '371425', '371400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371481000000000', '乐陵市', '乐陵市', '乐陵市', '3', '371481', '371400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371427000000000', '夏津县', '夏津县', '夏津县', '3', '371427', '371400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371524000000000', '东阿县', '东阿县', '东阿县', '3', '371524', '371500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371498000000000', '德州市商贸区', '德州市商贸区', '德州市商贸区', '3', '371498', '371400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371499000000000', '德州市经济开发区', '德州市经济开发区', '德州市经济开发区', '3', '371499', '371400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371525000000000', '冠县', '冠县', '冠县', '3', '371525', '371500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371523000000000', '茌平县', '茌平县', '茌平县', '3', '371523', '371500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371522000000000', '莘县', '莘县', '莘县', '3', '371522', '371500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371502000000000', '东昌府区', '东昌府区', '东昌府区', '3', '371502', '371500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371521000000000', '阳谷县', '阳谷县', '阳谷县', '3', '371521', '371500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371621000000000', '惠民县', '惠民县', '惠民县', '3', '371621', '371600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371622000000000', '阳信县', '阳信县', '阳信县', '3', '371622', '371600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371599000000000', '聊城市经济技术开发区', '聊城市经济技术开发区', '聊城市经济技术开发区', '3', '371599', '371500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371581000000000', '临清市', '临清市', '临清市', '3', '371581', '371500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371623000000000', '无棣县', '无棣县', '无棣县', '3', '371623', '371600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371526000000000', '高唐县', '高唐县', '高唐县', '3', '371526', '371500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371602000000000', '滨城区', '滨城区', '滨城区', '3', '371602', '371600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371624000000000', '沾化县', '沾化县', '沾化县', '3', '371624', '371600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371702000000000', '牡丹区', '牡丹区', '牡丹区', '3', '371702', '371700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371625000000000', '博兴县', '博兴县', '博兴县', '3', '371625', '371600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371712000000000', '菏泽市开发区', '菏泽市开发区', '菏泽市开发区', '3', '371712', '371700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371699000000000', '滨州市经济技术开发区', '滨州市经济技术开发区', '滨州市经济技术开发区', '3', '371699', '371600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371722000000000', '单县', '单县', '单县', '3', '371722', '371700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371626000000000', '邹平县', '邹平县', '邹平县', '3', '371626', '371600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371721000000000', '曹县', '曹县', '曹县', '3', '371721', '371700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371727000000000', '定陶县', '定陶县', '定陶县', '3', '371727', '371700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371725000000000', '郓城县', '郓城县', '郓城县', '3', '371725', '371700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371724000000000', '巨野县', '巨野县', '巨野县', '3', '371724', '371700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371726000000000', '鄄城县', '鄄城县', '鄄城县', '3', '371726', '371700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410105000000000', '金水区', '金水区', '金水区', '3', '410105', '410100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371728000000000', '东明县', '东明县', '东明县', '3', '371728', '371700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410103000000000', '二七区', '二七区', '二七区', '3', '410103', '410100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371723000000000', '成武县', '成武县', '成武县', '3', '371723', '371700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410104000000000', '管城回族区', '管城回族区', '管城回族区', '3', '410104', '410100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410102000000000', '中原区', '中原区', '中原区', '3', '410102', '410100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410211000000000', '金明区', '金明区', '金明区', '3', '410211', '410200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410225000000000', '兰考县', '兰考县', '兰考县', '3', '410225', '410200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410122000000000', '中牟县', '中牟县', '中牟县', '3', '410122', '410100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410184000000000', '新郑市', '新郑市', '新郑市', '3', '410184', '410100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410221000000000', '杞县', '杞县', '杞县', '3', '410221', '410200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410202000000000', '龙亭区', '龙亭区', '龙亭区', '3', '410202', '410200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410203000000000', '顺河回族区', '顺河回族区', '顺河回族区', '3', '410203', '410200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410224000000000', '开封县', '开封县', '开封县', '3', '410224', '410200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410181000000000', '巩义市', '巩义市', '巩义市', '3', '410181', '410100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410106000000000', '上街区', '上街区', '上街区', '3', '410106', '410100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410108000000000', '惠济区', '惠济区', '惠济区', '3', '410108', '410100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520327000000000', '凤冈县', '凤冈县', '凤冈县', '3', '520327', '520300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520328000000000', '湄潭县', '湄潭县', '湄潭县', '3', '520328', '520300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520329000000000', '余庆县', '余庆县', '余庆县', '3', '520329', '520300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520330000000000', '习水县', '习水县', '习水县', '3', '520330', '520300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520421000000000', '平坝县', '平坝县', '平坝县', '3', '520421', '520400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520422000000000', '普定县', '普定县', '普定县', '3', '520422', '520400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520382000000000', '仁怀市', '仁怀市', '仁怀市', '3', '520382', '520300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520402000000000', '西秀区', '西秀区', '西秀区', '3', '520402', '520400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520425000000000', '紫云苗族布依族自治县', '紫云苗族布依族自治县', '紫云苗族布依族自治县', '3', '520425', '520400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520423000000000', '镇宁布依族苗族自治县', '镇宁布依族苗族自治县', '镇宁布依族苗族自治县', '3', '520423', '520400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520424000000000', '关岭布依族苗族自治县', '关岭布依族苗族自治县', '关岭布依族苗族自治县', '3', '520424', '520400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522225000000000', '思南县', '思南县', '思南县', '3', '522225', '522200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522201000000000', '铜仁市民政局', '铜仁市民政局', '铜仁市民政局', '3', '522201', '522200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522222000000000', '江口县', '江口县', '江口县', '3', '522222', '522200000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522223000000000', '玉屏侗族自治县', '玉屏侗族自治县', '玉屏侗族自治县', '3', '522223', '522200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522224000000000', '石阡县', '石阡县', '石阡县', '3', '522224', '522200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522228000000000', '沿河土家族自治县', '沿河土家族自治县', '沿河土家族自治县', '3', '522228', '522200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522226000000000', '印江土家族苗族自治县', '印江土家族苗族自治县', '印江土家族苗族自治县', '3', '522226', '522200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522227000000000', '德江县', '德江县', '德江县', '3', '522227', '522200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522230000000000', '万山特区民政局', '万山特区民政局', '万山特区民政局', '3', '522230', '522200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522301000000000', '兴义市', '兴义市', '兴义市', '3', '522301', '522300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522229000000000', '松桃苗族自治县', '松桃苗族自治县', '松桃苗族自治县', '3', '522229', '522200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522325000000000', '贞丰县', '贞丰县', '贞丰县', '3', '522325', '522300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522322000000000', '兴仁县', '兴仁县', '兴仁县', '3', '522322', '522300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522323000000000', '普安县', '普安县', '普安县', '3', '522323', '522300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522324000000000', '晴仑', '晴仑', '晴仑', '3', '522324', '522300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522401000000000', '毕节市', '毕节市', '毕节市', '3', '522401', '522400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522326000000000', '望谟县', '望谟县', '望谟县', '3', '522326', '522300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522327000000000', '册亨县', '册亨县', '册亨县', '3', '522327', '522300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522328000000000', '安龙县', '安龙县', '安龙县', '3', '522328', '522300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522422000000000', '大方县', '大方县', '大方县', '3', '522422', '522400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522423000000000', '黔西县', '黔西县', '黔西县', '3', '522423', '522400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522426000000000', '纳雍县', '纳雍县', '纳雍县', '3', '522426', '522400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522424000000000', '金沙县', '金沙县', '金沙县', '3', '522424', '522400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522425000000000', '织金县', '织金县', '织金县', '3', '522425', '522400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522624000000000', '三穗县', '三穗县', '三穗县', '3', '522624', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522428000000000', '赫章县', '赫章县', '赫章县', '3', '522428', '522400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522427000000000', '威宁彝族回族苗族自治县', '威宁彝族回族苗族自治县', '威宁彝族回族苗族自治县', '3', '522427', '522400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522623000000000', '施秉县', '施秉县', '施秉县', '3', '522623', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522601000000000', '凯里市', '凯里市', '凯里市', '3', '522601', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522622000000000', '黄平县', '黄平县', '黄平县', '3', '522622', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522629000000000', '剑河县', '剑河县', '剑河县', '3', '522629', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522625000000000', '镇远县', '镇远县', '镇远县', '3', '522625', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522626000000000', '岑巩县', '岑巩县', '岑巩县', '3', '522626', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522627000000000', '天柱县', '天柱县', '天柱县', '3', '522627', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522628000000000', '锦屏县', '锦屏县', '锦屏县', '3', '522628', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522633000000000', '从江县', '从江县', '从江县', '3', '522633', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522630000000000', '台江县', '台江县', '台江县', '3', '522630', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522631000000000', '黎平县', '黎平县', '黎平县', '3', '522631', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522632000000000', '榕江县', '榕江县', '榕江县', '3', '522632', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522723000000000', '贵定县', '贵定县', '贵定县', '3', '522723', '522700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522722000000000', '荔波县', '荔波县', '荔波县', '3', '522722', '522700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522725000000000', '瓮安县', '瓮安县', '瓮安县', '3', '522725', '522700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542323000000000', '江孜县', '江孜县', '江孜县', '3', '542323', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542324000000000', '定日县', '定日县', '定日县', '3', '542324', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522701000000000', '都匀市', '都匀市', '都匀市', '3', '522701', '522700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522702000000000', '福泉市', '福泉市', '福泉市', '3', '522702', '522700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522729000000000', '长顺县', '长顺县', '长顺县', '3', '522729', '522700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522730000000000', '龙里县', '龙里县', '龙里县', '3', '522730', '522700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522731000000000', '惠水县', '惠水县', '惠水县', '3', '522731', '522700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522726000000000', '独山县', '独山县', '独山县', '3', '522726', '522700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522727000000000', '平塘县', '平塘县', '平塘县', '3', '522727', '522700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522728000000000', '罗甸县', '罗甸县', '罗甸县', '3', '522728', '522700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530121000000000', '呈贡县', '呈贡县', '呈贡县', '3', '530121', '530100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530122000000000', '晋宁县', '晋宁县', '晋宁县', '3', '530122', '530100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530124000000000', '富民县', '富民县', '富民县', '3', '530124', '530100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522732000000000', '三都水族自治县', '三都水族自治县', '三都水族自治县', '3', '522732', '522700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530102000000000', '五华区', '五华区', '五华区', '3', '530102', '530100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530103000000000', '盘龙区', '盘龙区', '盘龙区', '3', '530103', '530100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530111000000000', '官渡区', '官渡区', '官渡区', '3', '530111', '530100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530112000000000', '西山区', '西山区', '西山区', '3', '530112', '530100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530113000000000', '东川区', '东川区', '东川区', '3', '530113', '530100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530321000000000', '马龙县', '马龙县', '马龙县', '3', '530321', '530300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530322000000000', '陆良县', '陆良县', '陆良县', '3', '530322', '530300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530323000000000', '师宗县', '师宗县', '师宗县', '3', '530323', '530300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530324000000000', '罗平县', '罗平县', '罗平县', '3', '530324', '530300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530125000000000', '宜良县', '宜良县', '宜良县', '3', '530125', '530100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530126000000000', '石林彝族自治县', '石林彝族自治县', '石林彝族自治县', '3', '530126', '530100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530127000000000', '嵩明县', '嵩明县', '嵩明县', '3', '530127', '530100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530128000000000', '禄劝彝族苗族自治县', '禄劝彝族苗族自治县', '禄劝彝族苗族自治县', '3', '530128', '530100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530129000000000', '寻甸回族彝族自治县', '寻甸回族彝族自治县', '寻甸回族彝族自治县', '3', '530129', '530100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530181000000000', '安宁市', '安宁市', '安宁市', '3', '530181', '530100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530302000000000', '麒麟区', '麒麟区', '麒麟区', '3', '530302', '530300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530402000000000', '红塔区', '红塔区', '红塔区', '3', '530402', '530400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530325000000000', '富源县', '富源县', '富源县', '3', '530325', '530300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530326000000000', '会泽县', '会泽县', '会泽县', '3', '530326', '530300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530328000000000', '沾益县', '沾益县', '沾益县', '3', '530328', '530300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530381000000000', '宣威市', '宣威市', '宣威市', '3', '530381', '530300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530623000000000', '盐津县', '盐津县', '盐津县', '3', '530623', '530600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530521000000000', '施甸县', '施甸县', '施甸县', '3', '530521', '530500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530421000000000', '江川县', '江川县', '江川县', '3', '530421', '530400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530422000000000', '澄江县', '澄江县', '澄江县', '3', '530422', '530400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530423000000000', '通海县', '通海县', '通海县', '3', '530423', '530400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530424000000000', '华宁县', '华宁县', '华宁县', '3', '530424', '530400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530425000000000', '易门县', '易门县', '易门县', '3', '530425', '530400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530426000000000', '峨山县', '峨山县', '峨山县', '3', '530426', '530400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530427000000000', '新平县', '新平县', '新平县', '3', '530427', '530400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530428000000000', '元江县', '元江县', '元江县', '3', '530428', '530400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530502000000000', '隆阳区', '隆阳区', '隆阳区', '3', '530502', '530500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530624000000000', '大关县', '大关县', '大关县', '3', '530624', '530600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530625000000000', '永善县', '永善县', '永善县', '3', '530625', '530600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530626000000000', '绥江县', '绥江县', '绥江县', '3', '530626', '530600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530627000000000', '镇雄县', '镇雄县', '镇雄县', '3', '530627', '530600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530628000000000', '彝良县', '彝良县', '彝良县', '3', '530628', '530600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530629000000000', '威信县', '威信县', '威信县', '3', '530629', '530600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530630000000000', '水富县', '水富县', '水富县', '3', '530630', '530600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530702000000000', '古城区民政局', '古城区民政局', '古城区民政局', '3', '530702', '530700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530523000000000', '龙陵县', '龙陵县', '龙陵县', '3', '530523', '530500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530524000000000', '昌宁县', '昌宁县', '昌宁县', '3', '530524', '530500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530602000000000', '昭阳区', '昭阳区', '昭阳区', '3', '530602', '530600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530621000000000', '鲁甸县', '鲁甸县', '鲁甸县', '3', '530621', '530600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530622000000000', '巧家县', '巧家县', '巧家县', '3', '530622', '530600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530824000000000', '景谷傣族彝族自治县', '景谷傣族彝族自治县', '景谷傣族彝族自治县', '3', '530824', '530800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530825000000000', '镇沅彝族哈尼族拉祜族自治县', '镇沅彝族哈尼族拉祜族自治县', '镇沅彝族哈尼族拉祜族自治县', '3', '530825', '530800000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530522000000000', '腾冲县', '腾冲县', '腾冲县', '3', '530522', '530500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530721000000000', '玉龙纳西族自治县', '玉龙纳西族自治县', '玉龙纳西族自治县', '3', '530721', '530700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530722000000000', '永胜县', '永胜县', '永胜县', '3', '530722', '530700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530723000000000', '华坪县', '华坪县', '华坪县', '3', '530723', '530700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530724000000000', '宁蒗县', '宁蒗县', '宁蒗县', '3', '530724', '530700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530802000000000', '翠云区', '翠云区', '翠云区', '3', '530802', '530800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530821000000000', '普洱哈尼族彝族自治县', '普洱哈尼族彝族自治县', '普洱哈尼族彝族自治县', '3', '530821', '530800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530822000000000', '墨江哈尼族自治县', '墨江哈尼族自治县', '墨江哈尼族自治县', '3', '530822', '530800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530823000000000', '景东彝族自治县', '景东彝族自治县', '景东彝族自治县', '3', '530823', '530800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530924000000000', '镇康县', '镇康县', '镇康县', '3', '530924', '530900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530925000000000', '双江拉祜族佤族布朗族傣族自治县', '双江拉祜族佤族布朗族傣族自治县', '双江拉祜族佤族布朗族傣族自治县', '3', '530925', '530900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530926000000000', '耿马傣族佤族县', '耿马傣族佤族县', '耿马傣族佤族县', '3', '530926', '530900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530826000000000', '江城哈尼族彝族自治县', '江城哈尼族彝族自治县', '江城哈尼族彝族自治县', '3', '530826', '530800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530827000000000', '孟连傣族拉祜族佤族自治县', '孟连傣族拉祜族佤族自治县', '孟连傣族拉祜族佤族自治县', '3', '530827', '530800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530828000000000', '澜沧拉祜族自治县', '澜沧拉祜族自治县', '澜沧拉祜族自治县', '3', '530828', '530800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530829000000000', '西盟佤族自治县', '西盟佤族自治县', '西盟佤族自治县', '3', '530829', '530800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530902000000000', '临翔区', '临翔区', '临翔区', '3', '530902', '530900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530921000000000', '凤庆县.', '凤庆县.', '凤庆县.', '3', '530921', '530900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530922000000000', '云县', '云县', '云县', '3', '530922', '530900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530923000000000', '永德县', '永德县', '永德县', '3', '530923', '530900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532328000000000', '元谋县', '元谋县', '元谋县', '3', '532328', '532300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532329000000000', '武定县', '武定县', '武定县', '3', '532329', '532300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532331000000000', '禄丰县', '禄丰县', '禄丰县', '3', '532331', '532300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530927000000000', '沧源佤族自治县', '沧源佤族自治县', '沧源佤族自治县', '3', '530927', '530900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532301000000000', '楚雄市', '楚雄市', '楚雄市', '3', '532301', '532300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532322000000000', '双柏县', '双柏县', '双柏县', '3', '532322', '532300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532323000000000', '牟定县', '牟定县', '牟定县', '3', '532323', '532300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532324000000000', '南华县', '南华县', '南华县', '3', '532324', '532300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532325000000000', '姚安县', '姚安县', '姚安县', '3', '532325', '532300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532326000000000', '大姚县', '大姚县', '大姚县', '3', '532326', '532300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532327000000000', '永仁县', '永仁县', '永仁县', '3', '532327', '532300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532528000000000', '元阳县', '元阳县', '元阳县', '3', '532528', '532500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532529000000000', '红河县', '红河县', '红河县', '3', '532529', '532500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532530000000000', '金平苗族瑶族傣族自治县', '金平苗族瑶族傣族自治县', '金平苗族瑶族傣族自治县', '3', '532530', '532500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532501000000000', '个旧市', '个旧市', '个旧市', '3', '532501', '532500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532502000000000', '开远市', '开远市', '开远市', '3', '532502', '532500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532522000000000', '蒙自县', '蒙自县', '蒙自县', '3', '532522', '532500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532523000000000', '屏边苗族自治县', '屏边苗族自治县', '屏边苗族自治县', '3', '532523', '532500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532524000000000', '建水县', '建水县', '建水县', '3', '532524', '532500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532525000000000', '石屏县', '石屏县', '石屏县', '3', '532525', '532500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532526000000000', '弥勒县', '弥勒县', '弥勒县', '3', '532526', '532500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532527000000000', '泸西县', '泸西县', '泸西县', '3', '532527', '532500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532628000000000', '富宁县', '富宁县', '富宁县', '3', '532628', '532600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532801000000000', '景洪市', '景洪市', '景洪市', '3', '532801', '532800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532531000000000', '绿春县', '绿春县', '绿春县', '3', '532531', '532500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532532000000000', '河口县', '河口县', '河口县', '3', '532532', '532500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532621000000000', '文山县', '文山县', '文山县', '3', '532621', '532600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532622000000000', '砚山县', '砚山县', '砚山县', '3', '532622', '532600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532623000000000', '西畴县', '西畴县', '西畴县', '3', '532623', '532600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532624000000000', '麻栗坡县', '麻栗坡县', '麻栗坡县', '3', '532624', '532600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532625000000000', '马关县', '马关县', '马关县', '3', '532625', '532600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532626000000000', '丘北县', '丘北县', '丘北县', '3', '532626', '532600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532627000000000', '广南县', '广南县', '广南县', '3', '532627', '532600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532926000000000', '南涧彝族自治县', '南涧彝族自治县', '南涧彝族自治县', '3', '532926', '532900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532927000000000', '巍山彝族回族自治县', '巍山彝族回族自治县', '巍山彝族回族自治县', '3', '532927', '532900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532928000000000', '永平县', '永平县', '永平县', '3', '532928', '532900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532929000000000', '云龙县', '云龙县', '云龙县', '3', '532929', '532900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532822000000000', '勐海县', '勐海县', '勐海县', '3', '532822', '532800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532823000000000', '勐腊县', '勐腊县', '勐腊县', '3', '532823', '532800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532901000000000', '大理市', '大理市', '大理市', '3', '532901', '532900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532922000000000', '漾濞彝族自治县', '漾濞彝族自治县', '漾濞彝族自治县', '3', '532922', '532900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532923000000000', '祥云县', '祥云县', '祥云县', '3', '532923', '532900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532924000000000', '宾川县', '宾川县', '宾川县', '3', '532924', '532900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532925000000000', '弥渡县', '弥渡县', '弥渡县', '3', '532925', '532900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533124000000000', '陇川县', '陇川县', '陇川县', '3', '533124', '533100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533321000000000', '泸水县', '泸水县', '泸水县', '3', '533321', '533300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533323000000000', '福贡县', '福贡县', '福贡县', '3', '533323', '533300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533324000000000', '贡山独龙族怒族自治县', '贡山独龙族怒族自治县', '贡山独龙族怒族自治县', '3', '533324', '533300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533325000000000', '兰坪白族普米族自治县', '兰坪白族普米族自治县', '兰坪白族普米族自治县', '3', '533325', '533300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532930000000000', '洱源县', '洱源县', '洱源县', '3', '532930', '532900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532931000000000', '剑川县', '剑川县', '剑川县', '3', '532931', '532900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532932000000000', '鹤庆县', '鹤庆县', '鹤庆县', '3', '532932', '532900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533102000000000', '瑞丽市', '瑞丽市', '瑞丽市', '3', '533102', '533100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533103000000000', '潞西市', '潞西市', '潞西市', '3', '533103', '533100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533122000000000', '梁河县', '梁河县', '梁河县', '3', '533122', '533100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533123000000000', '盈江县', '盈江县', '盈江县', '3', '533123', '533100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542123000000000', '贡觉县', '贡觉县', '贡觉县', '3', '542123', '542100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542124000000000', '类乌齐县', '类乌齐县', '类乌齐县', '3', '542124', '542100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542125000000000', '丁青县', '丁青县', '丁青县', '3', '542125', '542100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542126000000000', '察雅县', '察雅县', '察雅县', '3', '542126', '542100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533421000000000', '香格里拉县民政局', '香格里拉县民政局', '香格里拉县民政局', '3', '533421', '533400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533422000000000', '德钦县', '德钦县', '德钦县', '3', '533422', '533400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533423000000000', '维西傈僳族自治县', '维西傈僳族自治县', '维西傈僳族自治县', '3', '533423', '533400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('540101000000000', '市辖区', '市辖区', '市辖区', '3', '540101', '540100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('540102000000000', '城关区', '城关区', '城关区', '3', '540102', '540100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('540121000000000', '林周县', '林周县', '林周县', '3', '540121', '540100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('540122000000000', '当雄县', '当雄县', '当雄县', '3', '540122', '540100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('540123000000000', '尼木县', '尼木县', '尼木县', '3', '540123', '540100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('540124000000000', '曲水县', '曲水县', '曲水县', '3', '540124', '540100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('540125000000000', '堆龙德庆县', '堆龙德庆县', '堆龙德庆县', '3', '540125', '540100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('540126000000000', '达孜县', '达孜县', '达孜县', '3', '540126', '540100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('540127000000000', '墨竹工卡县', '墨竹工卡县', '墨竹工卡县', '3', '540127', '540100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542121000000000', '昌都县', '昌都县', '昌都县', '3', '542121', '542100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542122000000000', '江达县', '江达县', '江达县', '3', '542122', '542100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542229000000000', '加查县', '加查县', '加查县', '3', '542229', '542200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542231000000000', '隆子县', '隆子县', '隆子县', '3', '542231', '542200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542232000000000', '错那县', '错那县', '错那县', '3', '542232', '542200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542233000000000', '浪卡子县', '浪卡子县', '浪卡子县', '3', '542233', '542200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542301000000000', '日喀则市', '日喀则市', '日喀则市', '3', '542301', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542322000000000', '南木林县', '南木林县', '南木林县', '3', '542322', '542300000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542127000000000', '八宿县', '八宿县', '八宿县', '3', '542127', '542100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542128000000000', '左贡县', '左贡县', '左贡县', '3', '542128', '542100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542129000000000', '芒康县', '芒康县', '芒康县', '3', '542129', '542100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542132000000000', '洛仑', '洛仑', '洛仑', '3', '542132', '542100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542133000000000', '边坝县', '边坝县', '边坝县', '3', '542133', '542100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542221000000000', '乃东县', '乃东县', '乃东县', '3', '542221', '542200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542222000000000', '扎囊县', '扎囊县', '扎囊县', '3', '542222', '542200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542223000000000', '贡嘎县', '贡嘎县', '贡嘎县', '3', '542223', '542200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542224000000000', '桑日县', '桑日县', '桑日县', '3', '542224', '542200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542225000000000', '琼结县', '琼结县', '琼结县', '3', '542225', '542200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542226000000000', '曲松县', '曲松县', '曲松县', '3', '542226', '542200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542227000000000', '措美县', '措美县', '措美县', '3', '542227', '542200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542228000000000', '洛扎县', '洛扎县', '洛扎县', '3', '542228', '542200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610523000000000', '大荔县', '大荔县', '大荔县', '3', '610523', '610500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610623000000000', '子长县', '子长县', '子长县', '3', '610623', '610600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542325000000000', '萨迦县', '萨迦县', '萨迦县', '3', '542325', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542326000000000', '拉孜县', '拉孜县', '拉孜县', '3', '542326', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610526000000000', '蒲城县', '蒲城县', '蒲城县', '3', '610526', '610500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610602000000000', '宝塔区', '宝塔区', '宝塔区', '3', '610602', '610600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542331000000000', '康马县', '康马县', '康马县', '3', '542331', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542332000000000', '定结县', '定结县', '定结县', '3', '542332', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542335000000000', '吉仑', '吉仑', '吉仑', '3', '542335', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542336000000000', '聂拉木县', '聂拉木县', '聂拉木县', '3', '542336', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542334000000000', '亚东县', '亚东县', '亚东县', '3', '542334', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542421000000000', '那曲县', '那曲县', '那曲县', '3', '542421', '542400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542337000000000', '萨嘎县', '萨嘎县', '萨嘎县', '3', '542337', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542338000000000', '岗巴县', '岗巴县', '岗巴县', '3', '542338', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542422000000000', '嘉黎县', '嘉黎县', '嘉黎县', '3', '542422', '542400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542523000000000', '噶尔县', '噶尔县', '噶尔县', '3', '542523', '542500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610621000000000', '延长县', '延长县', '延长县', '3', '610621', '610600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610582000000000', '华阴市', '华阴市', '华阴市', '3', '610582', '610500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610601000000000', '市辖区', '市辖区', '市辖区', '3', '610601', '610600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542428000000000', '班戈县', '班戈县', '班戈县', '3', '542428', '542400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542429000000000', '巴青县', '巴青县', '巴青县', '3', '542429', '542400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542430000000000', '尼玛县', '尼玛县', '尼玛县', '3', '542430', '542400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542521000000000', '普兰县', '普兰县', '普兰县', '3', '542521', '542500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542522000000000', '札达县', '札达县', '札达县', '3', '542522', '542500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542524000000000', '日土县', '日土县', '日土县', '3', '542524', '542500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542525000000000', '革吉县', '革吉县', '革吉县', '3', '542525', '542500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542423000000000', '比如县', '比如县', '比如县', '3', '542423', '542400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610622000000000', '延川县', '延川县', '延川县', '3', '610622', '610600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542627000000000', '朗县', '朗县', '朗县', '3', '542627', '542600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610101000000000', '市辖区', '市辖区', '市辖区', '3', '610101', '610100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610102000000000', '新城区', '新城区', '新城区', '3', '610102', '610100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610103000000000', '碑林区', '碑林区', '碑林区', '3', '610103', '610100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610104000000000', '莲湖区', '莲湖区', '莲湖区', '3', '610104', '610100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610111000000000', '灞桥区', '灞桥区', '灞桥区', '3', '610111', '610100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542526000000000', '改则县', '改则县', '改则县', '3', '542526', '542500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542327000000000', '昂仁县', '昂仁县', '昂仁县', '3', '542327', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542328000000000', '谢通门县', '谢通门县', '谢通门县', '3', '542328', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542330000000000', '仁布县', '仁布县', '仁布县', '3', '542330', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542329000000000', '白朗县', '白朗县', '白朗县', '3', '542329', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542333000000000', '仲巴县', '仲巴县', '仲巴县', '3', '542333', '542300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610114000000000', '阎良区', '阎良区', '阎良区', '3', '610114', '610100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610115000000000', '临潼区', '临潼区', '临潼区', '3', '610115', '610100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610116000000000', '长安区', '长安区', '长安区', '3', '610116', '610100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542527000000000', '措勤县', '措勤县', '措勤县', '3', '542527', '542500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542621000000000', '林芝县', '林芝县', '林芝县', '3', '542621', '542600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542424000000000', '聂荣县', '聂荣县', '聂荣县', '3', '542424', '542400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542425000000000', '安多县', '安多县', '安多县', '3', '542425', '542400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542426000000000', '申扎县', '申扎县', '申扎县', '3', '542426', '542400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542427000000000', '索县', '索县', '索县', '3', '542427', '542400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610122000000000', '蓝田县', '蓝田县', '蓝田县', '3', '610122', '610100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610124000000000', '周至县', '周至县', '周至县', '3', '610124', '610100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542622000000000', '工布江达县', '工布江达县', '工布江达县', '3', '542622', '542600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542623000000000', '米林县', '米林县', '米林县', '3', '542623', '542600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542624000000000', '墨脱县', '墨脱县', '墨脱县', '3', '542624', '542600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542625000000000', '波密县', '波密县', '波密县', '3', '542625', '542600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542626000000000', '察隅县', '察隅县', '察隅县', '3', '542626', '542600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610126000000000', '高陵县', '高陵县', '高陵县', '3', '610126', '610100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610201000000000', '市辖区', '市辖区', '市辖区', '3', '610201', '610200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610202000000000', '王益区', '王益区', '王益区', '3', '610202', '610200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610203000000000', '印台区', '印台区', '印台区', '3', '610203', '610200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610204000000000', '耀州区', '耀州区', '耀州区', '3', '610204', '610200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610112000000000', '未央区', '未央区', '未央区', '3', '610112', '610100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610113000000000', '雁塔区', '雁塔区', '雁塔区', '3', '610113', '610100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610321000000000', '陈仓区', '陈仓区', '陈仓区', '3', '610321', '610300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610322000000000', '凤翔县', '凤翔县', '凤翔县', '3', '610322', '610300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610327000000000', '陇县', '陇县', '陇县', '3', '610327', '610300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610328000000000', '千阳县', '千阳县', '千阳县', '3', '610328', '610300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610329000000000', '麟游县', '麟游县', '麟游县', '3', '610329', '610300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610125000000000', '户县', '户县', '户县', '3', '610125', '610100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610422000000000', '三原县', '三原县', '三原县', '3', '610422', '610400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610423000000000', '泾阳县', '泾阳县', '泾阳县', '3', '610423', '610400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610330000000000', '凤县', '凤县', '凤县', '3', '610330', '610300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610222000000000', '宜君县', '宜君县', '宜君县', '3', '610222', '610200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610301000000000', '市辖区', '市辖区', '市辖区', '3', '610301', '610300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610302000000000', '渭滨区', '渭滨区', '渭滨区', '3', '610302', '610300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610303000000000', '金台区', '金台区', '金台区', '3', '610303', '610300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610426000000000', '永寿县', '永寿县', '永寿县', '3', '610426', '610400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610427000000000', '彬县', '彬县', '彬县', '3', '610427', '610400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610323000000000', '岐山县', '岐山县', '岐山县', '3', '610323', '610300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610324000000000', '扶风县', '扶风县', '扶风县', '3', '610324', '610300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610326000000000', '眉县', '眉县', '眉县', '3', '610326', '610300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610430000000000', '淳化县', '淳化县', '淳化县', '3', '610430', '610400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610431000000000', '武功县', '武功县', '武功县', '3', '610431', '610400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610424000000000', '乾县', '乾县', '乾县', '3', '610424', '610400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610331000000000', '太白县', '太白县', '太白县', '3', '610331', '610300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610401000000000', '市辖区', '市辖区', '市辖区', '3', '610401', '610400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610402000000000', '秦都区', '秦都区', '秦都区', '3', '610402', '610400000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610404000000000', '渭城区', '渭城区', '渭城区', '3', '610404', '610400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610521000000000', '华县', '华县', '华县', '3', '610521', '610500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610425000000000', '礼泉县', '礼泉县', '礼泉县', '3', '610425', '610400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610524000000000', '合阳县', '合阳县', '合阳县', '3', '610524', '610500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610525000000000', '澄城县', '澄城县', '澄城县', '3', '610525', '610500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610428000000000', '长武县', '长武县', '长武县', '3', '610428', '610400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610429000000000', '旬邑县', '旬邑县', '旬邑县', '3', '610429', '610400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610527000000000', '白水县', '白水县', '白水县', '3', '610527', '610500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610528000000000', '富平县', '富平县', '富平县', '3', '610528', '610500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610581000000000', '韩城市', '韩城市', '韩城市', '3', '610581', '610500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610522000000000', '潼关县', '潼关县', '潼关县', '3', '610522', '610500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610481000000000', '兴平市', '兴平市', '兴平市', '3', '610481', '610400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610501000000000', '市辖区', '市辖区', '市辖区', '3', '610501', '610500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610502000000000', '临渭区', '临渭区', '临渭区', '3', '610502', '610500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610626000000000', '吴起县', '吴起县', '吴起县', '3', '610626', '610600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610627000000000', '甘泉县', '甘泉县', '甘泉县', '3', '610627', '610600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610628000000000', '富县', '富县', '富县', '3', '610628', '610600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620502000000000', '秦州区', '秦州区', '秦州区', '3', '620502', '620500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610624000000000', '安塞县', '安塞县', '安塞县', '3', '610624', '610600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610625000000000', '志丹县', '志丹县', '志丹县', '3', '610625', '610600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610631000000000', '黄龙县', '黄龙县', '黄龙县', '3', '610631', '610600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610632000000000', '黄陵县', '黄陵县', '黄陵县', '3', '610632', '610600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610629000000000', '洛川县', '洛川县', '洛川县', '3', '610629', '610600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610630000000000', '宜川县', '宜川县', '宜川县', '3', '610630', '610600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610723000000000', '洋县', '洋县', '洋县', '3', '610723', '610700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610701000000000', '汉中经济开发区', '汉中经济开发区', '汉中经济开发区', '3', '610701', '610700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610702000000000', '汉台区', '汉台区', '汉台区', '3', '610702', '610700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610721000000000', '南郑县', '南郑县', '南郑县', '3', '610721', '610700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610722000000000', '城固县', '城固县', '城固县', '3', '610722', '610700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610726000000000', '宁强县', '宁强县', '宁强县', '3', '610726', '610700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610724000000000', '西乡县', '西乡县', '西乡县', '3', '610724', '610700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610725000000000', '勉县', '勉县', '勉县', '3', '610725', '610700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610730000000000', '佛坪县', '佛坪县', '佛坪县', '3', '610730', '610700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610801000000000', '市辖区', '市辖区', '市辖区', '3', '610801', '610800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610802000000000', '榆阳区', '榆阳区', '榆阳区', '3', '610802', '610800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610727000000000', '略阳县', '略阳县', '略阳县', '3', '610727', '610700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610728000000000', '镇巴县', '镇巴县', '镇巴县', '3', '610728', '610700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610729000000000', '留坝县', '留坝县', '留坝县', '3', '610729', '610700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610822000000000', '府谷县', '府谷县', '府谷县', '3', '610822', '610800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610821000000000', '神木县', '神木县', '神木县', '3', '610821', '610800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610825000000000', '定边县', '定边县', '定边县', '3', '610825', '610800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610823000000000', '横山县', '横山县', '横山县', '3', '610823', '610800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610824000000000', '靖边县', '靖边县', '靖边县', '3', '610824', '610800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610828000000000', '佳县', '佳县', '佳县', '3', '610828', '610800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610826000000000', '绥德县', '绥德县', '绥德县', '3', '610826', '610800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610827000000000', '米脂县', '米脂县', '米脂县', '3', '610827', '610800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610830000000000', '清涧县', '清涧县', '清涧县', '3', '610830', '610800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610829000000000', '吴堡县', '吴堡县', '吴堡县', '3', '610829', '610800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610901000000000', '市辖区', '市辖区', '市辖区', '3', '610901', '610900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610902000000000', '汉滨区', '汉滨区', '汉滨区', '3', '610902', '610900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610831000000000', '子洲县', '子洲县', '子洲县', '3', '610831', '610800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610923000000000', '宁陕县', '宁陕县', '宁陕县', '3', '610923', '610900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610924000000000', '紫阳县', '紫阳县', '紫阳县', '3', '610924', '610900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610921000000000', '汉阴县', '汉阴县', '汉阴县', '3', '610921', '610900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610922000000000', '石泉县', '石泉县', '石泉县', '3', '610922', '610900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610929000000000', '白河县', '白河县', '白河县', '3', '610929', '610900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610925000000000', '岚皋县', '岚皋县', '岚皋县', '3', '610925', '610900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610926000000000', '平利县', '平利县', '平利县', '3', '610926', '610900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610927000000000', '镇坪县', '镇坪县', '镇坪县', '3', '610927', '610900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610928000000000', '旬阳县', '旬阳县', '旬阳县', '3', '610928', '610900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('611022000000000', '丹凤县', '丹凤县', '丹凤县', '3', '611022', '611000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('611001000000000', '市辖区', '市辖区', '市辖区', '3', '611001', '611000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('611002000000000', '商州区', '商州区', '商州区', '3', '611002', '611000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('611021000000000', '洛南县', '洛南县', '洛南县', '3', '611021', '611000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('611025000000000', '镇安县', '镇安县', '镇安县', '3', '611025', '611000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('611026000000000', '柞水县', '柞水县', '柞水县', '3', '611026', '611000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('611101000000000', '杨陵区', '杨陵区', '杨陵区', '3', '611101', '611100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('611023000000000', '商南县', '商南县', '商南县', '3', '611023', '611000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('611024000000000', '山阳县', '山阳县', '山阳县', '3', '611024', '611000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620122000000000', '皋兰县', '皋兰县', '皋兰县', '3', '620122', '620100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620123000000000', '榆中县', '榆中县', '榆中县', '3', '620123', '620100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620102000000000', '城关区', '城关区', '城关区', '3', '620102', '620100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620103000000000', '七里河区', '七里河区', '七里河区', '3', '620103', '620100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620104000000000', '西固区', '西固区', '西固区', '3', '620104', '620100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450125000000000', '上林县', '上林县', '上林县', '3', '450125', '450100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450103000000000', '青秀区', '青秀区', '青秀区', '3', '450103', '450100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445323000000000', '云安县', '云安县', '云安县', '3', '445323', '445300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450122000000000', '武鸣县', '武鸣县', '武鸣县', '3', '450122', '450100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450126000000000', '宾阳县', '宾阳县', '宾阳县', '3', '450126', '450100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445222000000000', '揭西县', '揭西县', '揭西县', '3', '445222', '445200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445202000000000', '榕城区', '榕城区', '榕城区', '3', '445202', '445200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445302000000000', '云城区', '云城区', '云城区', '3', '445302', '445300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445322000000000', '郁南县', '郁南县', '郁南县', '3', '445322', '445300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450124000000000', '马山县', '马山县', '马山县', '3', '450124', '450100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445224000000000', '惠来县', '惠来县', '惠来县', '3', '445224', '445200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445221000000000', '揭东县', '揭东县', '揭东县', '3', '445221', '445200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445381000000000', '罗定市', '罗定市', '罗定市', '3', '445381', '445300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450108000000000', '良庆区', '良庆区', '良庆区', '3', '450108', '450100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450105000000000', '江南区', '江南区', '江南区', '3', '450105', '450100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450127000000000', '横县', '横县', '横县', '3', '450127', '450100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450221000000000', '柳江县', '柳江县', '柳江县', '3', '450221', '450200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450223000000000', '鹿寨县', '鹿寨县', '鹿寨县', '3', '450223', '450200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450321000000000', '阳朔县', '阳朔县', '阳朔县', '3', '450321', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450323000000000', '灵川县', '灵川县', '灵川县', '3', '450323', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450225000000000', '融水苗族自治县', '融水苗族自治县', '融水苗族自治县', '3', '450225', '450200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450423000000000', '蒙山县', '蒙山县', '蒙山县', '3', '450423', '450400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450403000000000', '万秀区', '万秀区', '万秀区', '3', '450403', '450400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450222000000000', '柳城县', '柳城县', '柳城县', '3', '450222', '450200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450404000000000', '蝶山区', '蝶山区', '蝶山区', '3', '450404', '450400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450422000000000', '藤县', '藤县', '藤县', '3', '450422', '450400000000000', 'Y', 'Y', null);
commit;


insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450202000000000', '城中区', '城中区', '城中区', '3', '450202', '450200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450203000000000', '鱼峰区', '鱼峰区', '鱼峰区', '3', '450203', '450200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450502000000000', '海城区', '海城区', '海城区', '3', '450502', '450500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450224000000000', '融安县', '融安县', '融安县', '3', '450224', '450200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450302000000000', '秀峰区', '秀峰区', '秀峰区', '3', '450302', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450322000000000', '临桂县', '临桂县', '临桂县', '3', '450322', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450329000000000', '资源县', '资源县', '资源县', '3', '450329', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450303000000000', '叠彩区', '叠彩区', '叠彩区', '3', '450303', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450327000000000', '灌阳县', '灌阳县', '灌阳县', '3', '450327', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450481000000000', '岑溪市', '岑溪市', '岑溪市', '3', '450481', '450400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450405000000000', '长洲区', '长洲区', '长洲区', '3', '450405', '450400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450421000000000', '苍梧县', '苍梧县', '苍梧县', '3', '450421', '450400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450602000000000', '港口区', '港口区', '港口区', '3', '450602', '450600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450204000000000', '柳南区', '柳南区', '柳南区', '3', '450204', '450200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450503000000000', '银海区', '银海区', '银海区', '3', '450503', '450500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450304000000000', '象山区', '象山区', '象山区', '3', '450304', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450330000000000', '平乐县', '平乐县', '平乐县', '3', '450330', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450324000000000', '全州县', '全州县', '全州县', '3', '450324', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450325000000000', '兴安县', '兴安县', '兴安县', '3', '450325', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450305000000000', '七星区', '七星区', '七星区', '3', '450305', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450332000000000', '恭城瑶族自治县', '恭城瑶族自治县', '恭城瑶族自治县', '3', '450332', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450512000000000', '铁山港区', '铁山港区', '铁山港区', '3', '450512', '450500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450603000000000', '防城区', '防城区', '防城区', '3', '450603', '450600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450205000000000', '柳北区', '柳北区', '柳北区', '3', '450205', '450200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450521000000000', '合浦县', '合浦县', '合浦县', '3', '450521', '450500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450226000000000', '三江侗族自治县', '三江侗族自治县', '三江侗族自治县', '3', '450226', '450200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450311000000000', '雁山区', '雁山区', '雁山区', '3', '450311', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450328000000000', '龙胜各族自治县', '龙胜各族自治县', '龙胜各族自治县', '3', '450328', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450331000000000', '荔浦县', '荔浦县', '荔浦县', '3', '450331', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450326000000000', '永福县', '永福县', '永福县', '3', '450326', '450300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450681000000000', '东兴市', '东兴市', '东兴市', '3', '450681', '450600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450702000000000', '钦南区', '钦南区', '钦南区', '3', '450702', '450700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450802000000000', '港北区', '港北区', '港北区', '3', '450802', '450800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450922000000000', '陆川县', '陆川县', '陆川县', '3', '450922', '450900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451023000000000', '平果县', '平果县', '平果县', '3', '451023', '451000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450881000000000', '桂平市', '桂平市', '桂平市', '3', '450881', '450800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451021000000000', '田阳县', '田阳县', '田阳县', '3', '451021', '451000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451402000000000', '江州区', '江州区', '江州区', '3', '451402', '451400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450921000000000', '容县', '容县', '容县', '3', '450921', '450900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450821000000000', '平南县', '平南县', '平南县', '3', '450821', '450800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450803000000000', '港南区', '港南区', '港南区', '3', '450803', '450800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451024000000000', '德保县', '德保县', '德保县', '3', '451024', '451000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450902000000000', '玉州区', '玉州区', '玉州区', '3', '450902', '450900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451022000000000', '田东县', '田东县', '田东县', '3', '451022', '451000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450981000000000', '北流市', '北流市', '北流市', '3', '450981', '450900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451002000000000', '右江区', '右江区', '右江区', '3', '451002', '451000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450621000000000', '上思县', '上思县', '上思县', '3', '450621', '450600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450703000000000', '钦北区', '钦北区', '钦北区', '3', '450703', '450700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450721000000000', '灵山县', '灵山县', '灵山县', '3', '450721', '450700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450722000000000', '浦北县', '浦北县', '浦北县', '3', '450722', '450700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450923000000000', '博白县', '博白县', '博白县', '3', '450923', '450900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450804000000000', '覃塘区', '覃塘区', '覃塘区', '3', '450804', '450800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450924000000000', '兴业县', '兴业县', '兴业县', '3', '450924', '450900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451324000000000', '金秀瑶族自治县', '金秀瑶族自治县', '金秀瑶族自治县', '3', '451324', '451300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451028000000000', '乐业县', '乐业县', '乐业县', '3', '451028', '451000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451422000000000', '宁明县', '宁明县', '宁明县', '3', '451422', '451400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451423000000000', '龙州县', '龙州县', '龙州县', '3', '451423', '451400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451031000000000', '隆林各族自治县', '隆林各族自治县', '隆林各族自治县', '3', '451031', '451000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451322000000000', '象州县', '象州县', '象州县', '3', '451322', '451300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451281000000000', '宜州市', '宜州市', '宜州市', '3', '451281', '451200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451381000000000', '合山市', '合山市', '合山市', '3', '451381', '451300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451025000000000', '靖西县', '靖西县', '靖西县', '3', '451025', '451000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451027000000000', '凌云县', '凌云县', '凌云县', '3', '451027', '451000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451122000000000', '钟山县', '钟山县', '钟山县', '3', '451122', '451100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451227000000000', '巴马瑶族自治县', '巴马瑶族自治县', '巴马瑶族自治县', '3', '451227', '451200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451202000000000', '金城江区', '金城江区', '金城江区', '3', '451202', '451200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451222000000000', '天峨县', '天峨县', '天峨县', '3', '451222', '451200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451225000000000', '罗城仫佬族自治县', '罗城仫佬族自治县', '罗城仫佬族自治县', '3', '451225', '451200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451323000000000', '武宣县', '武宣县', '武宣县', '3', '451323', '451300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451026000000000', '那坡县', '那坡县', '那坡县', '3', '451026', '451000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451421000000000', '扶绥县', '扶绥县', '扶绥县', '3', '451421', '451400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451029000000000', '田林县', '田林县', '田林县', '3', '451029', '451000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451228000000000', '都安瑶族自治县', '都安瑶族自治县', '都安瑶族自治县', '3', '451228', '451200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451229000000000', '大化瑶族自治县', '大化瑶族自治县', '大化瑶族自治县', '3', '451229', '451200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451223000000000', '凤山县', '凤山县', '凤山县', '3', '451223', '451200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451321000000000', '忻城县', '忻城县', '忻城县', '3', '451321', '451300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451424000000000', '大新县', '大新县', '大新县', '3', '451424', '451400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451123000000000', '富川瑶族自治县', '富川瑶族自治县', '富川瑶族自治县', '3', '451123', '451100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451102000000000', '八步区', '八步区', '八步区', '3', '451102', '451100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451121000000000', '昭平县', '昭平县', '昭平县', '3', '451121', '451100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451030000000000', '西林县', '西林县', '西林县', '3', '451030', '451000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451221000000000', '南丹县', '南丹县', '南丹县', '3', '451221', '451200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451226000000000', '环江毛南族自治县', '环江毛南族自治县', '环江毛南族自治县', '3', '451226', '451200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451224000000000', '东兰县', '东兰县', '东兰县', '3', '451224', '451200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451302000000000', '兴宾区', '兴宾区', '兴宾区', '3', '451302', '451300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('460201000000000', '市辖区', '市辖区', '市辖区', '3', '460201', '460200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('460107000000000', '琼山区', '琼山区', '琼山区', '3', '460107', '460100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469025000000000', '定安县', '定安县', '定安县', '3', '469025', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469036000000000', '琼中黎族苗族自治县', '琼中黎族苗族自治县', '琼中黎族苗族自治县', '3', '469036', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469001000000000', '五指山市', '五指山市', '五指山市', '3', '469001', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469028000000000', '临高县', '临高县', '临高县', '3', '469028', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469031000000000', '昌江黎族自治县', '昌江黎族自治县', '昌江黎族自治县', '3', '469031', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469007000000000', '东方市', '东方市', '东方市', '3', '469007', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('460108000000000', '美兰区', '美兰区', '美兰区', '3', '460108', '460100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451481000000000', '凭祥市', '凭祥市', '凭祥市', '3', '451481', '451400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('460101000000000', '市轄区', '市轄区', '市轄区', '3', '460101', '460100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('460105000000000', '秀英区', '秀英区', '秀英区', '3', '460105', '460100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469026000000000', '屯昌县', '屯昌县', '屯昌县', '3', '469026', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469034000000000', '陵水黎族自治县', '陵水黎族自治县', '陵水黎族自治县', '3', '469034', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469003000000000', '儋州市', '儋州市', '儋州市', '3', '469003', '469000000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469005000000000', '文昌市', '文昌市', '文昌市', '3', '469005', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469006000000000', '万宁市', '万宁市', '万宁市', '3', '469006', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469037000000000', '西沙群岛', '西沙群岛', '西沙群岛', '3', '469037', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469038000000000', '南沙群岛', '南沙群岛', '南沙群岛', '3', '469038', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469039000000000', '中沙群岛的岛礁及其海域', '中沙群岛的岛礁及其海域', '中沙群岛的岛礁及其海域', '3', '469039', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500101000000000', '万州区', '万州区', '万州区', '3', '500101', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('460106000000000', '龙华区', '龙华区', '龙华区', '3', '460106', '460100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469027000000000', '澄迈县', '澄迈县', '澄迈县', '3', '469027', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469002000000000', '琼海市', '琼海市', '琼海市', '3', '469002', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451425000000000', '天等县', '天等县', '天等县', '3', '451425', '451400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469030000000000', '白沙黎族自治县', '白沙黎族自治县', '白沙黎族自治县', '3', '469030', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469033000000000', '乐东黎族自治县', '乐东黎族自治县', '乐东黎族自治县', '3', '469033', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469035000000000', '保亭黎族苗族自治县', '保亭黎族苗族自治县', '保亭黎族苗族自治县', '3', '469035', '469000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500102000000000', '涪陵区', '涪陵区', '涪陵区', '3', '500102', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500105000000000', '江北区', '江北区', '江北区', '3', '500105', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500110000000000', '万盛区', '万盛区', '万盛区', '3', '500110', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500116000000000', '高新区', '高新区', '高新区', '3', '500116', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500117000000000', '经开区', '经开区', '经开区', '3', '500117', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500222000000000', '綦江县', '綦江县', '綦江县', '3', '500222', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500103000000000', '渝中区', '渝中区', '渝中区', '3', '500103', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500227000000000', '璧山县', '璧山县', '璧山县', '3', '500227', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500113000000000', '巴南区', '巴南区', '巴南区', '3', '500113', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500111000000000', '双桥区', '双桥区', '双桥区', '3', '500111', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500115000000000', '长寿区', '长寿区', '长寿区', '3', '500115', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500223000000000', '潼南县', '潼南县', '潼南县', '3', '500223', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500226000000000', '荣昌县', '荣昌县', '荣昌县', '3', '500226', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500106000000000', '沙坪坝区', '沙坪坝区', '沙坪坝区', '3', '500106', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500107000000000', '九龙坡区', '九龙坡区', '九龙坡区', '3', '500107', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500104000000000', '大渡口区', '大渡口区', '大渡口区', '3', '500104', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500112000000000', '渝北区', '渝北区', '渝北区', '3', '500112', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500109000000000', '北碚区', '北碚区', '北碚区', '3', '500109', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500225000000000', '大足县', '大足县', '大足县', '3', '500225', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500224000000000', '铜梁县', '铜梁县', '铜梁县', '3', '500224', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500228000000000', '梁平县', '梁平县', '梁平县', '3', '500228', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500108000000000', '南岸区', '南岸区', '南岸区', '3', '500108', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500114000000000', '黔江区', '黔江区', '黔江区', '3', '500114', '500100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500241000000000', '秀山土家族苗族自治县', '秀山土家族苗族自治县', '秀山土家族苗族自治县', '3', '500241', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500230000000000', '丰都县', '丰都县', '丰都县', '3', '500230', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500237000000000', '巫山县', '巫山县', '巫山县', '3', '500237', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500235000000000', '云阳县', '云阳县', '云阳县', '3', '500235', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500238000000000', '巫溪县', '巫溪县', '巫溪县', '3', '500238', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500236000000000', '奉节县', '奉节县', '奉节县', '3', '500236', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500229000000000', '城口县', '城口县', '城口县', '3', '500229', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500233000000000', '忠县', '忠县', '忠县', '3', '500233', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500234000000000', '开县', '开县', '开县', '3', '500234', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500240000000000', '石柱土家族自治县', '石柱土家族自治县', '石柱土家族自治县', '3', '500240', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500231000000000', '垫江县', '垫江县', '垫江县', '3', '500231', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500232000000000', '武仑', '武仑', '武仑', '3', '500232', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500384000000000', '南川市', '南川市', '南川市', '3', '500384', '500300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510132000000000', '新津县', '新津县', '新津县', '3', '510132', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500383000000000', '永川市', '永川市', '永川市', '3', '500383', '500300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510105000000000', '青羊区', '青羊区', '青羊区', '3', '510105', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510113000000000', '青白江区', '青白江区', '青白江区', '3', '510113', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510131000000000', '蒲江县', '蒲江县', '蒲江县', '3', '510131', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510123000000000', '温江区', '温江区', '温江区', '3', '510123', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500242000000000', '酉阳土家族自治县', '酉阳土家族自治县', '酉阳土家族自治县', '3', '500242', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510108000000000', '成华区', '成华区', '成华区', '3', '510108', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510106000000000', '金牛区', '金牛区', '金牛区', '3', '510106', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510125000000000', '新都区', '新都区', '新都区', '3', '510125', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500243000000000', '彭水苗族土家族自治县', '彭水苗族土家族自治县', '彭水苗族土家族自治县', '3', '500243', '500200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510122000000000', '双流县', '双流县', '双流县', '3', '510122', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510129000000000', '大邑县', '大邑县', '大邑县', '3', '510129', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500381000000000', '江津市', '江津市', '江津市', '3', '500381', '500300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500382000000000', '合川市', '合川市', '合川市', '3', '500382', '500300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510121000000000', '金堂县', '金堂县', '金堂县', '3', '510121', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510112000000000', '龙泉驿区', '龙泉驿区', '龙泉驿区', '3', '510112', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510124000000000', '郫县', '郫县', '郫县', '3', '510124', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510181000000000', '都江堰市', '都江堰市', '都江堰市', '3', '510181', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510104000000000', '锦江区', '锦江区', '锦江区', '3', '510104', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510107000000000', '武侯区', '武侯区', '武侯区', '3', '510107', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510503000000000', '纳溪区', '纳溪区', '纳溪区', '3', '510503', '510500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510182000000000', '彭州市', '彭州市', '彭州市', '3', '510182', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510303000000000', '贡井区', '贡井区', '贡井区', '3', '510303', '510300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510411000000000', '仁和区', '仁和区', '仁和区', '3', '510411', '510400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510502000000000', '江阳区', '江阳区', '江阳区', '3', '510502', '510500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510321000000000', '荣县', '荣县', '荣县', '3', '510321', '510300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510184000000000', '崇州市', '崇州市', '崇州市', '3', '510184', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510521000000000', '泸县', '泸县', '泸县', '3', '510521', '510500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510421000000000', '米易县', '米易县', '米易县', '3', '510421', '510400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510402000000000', '东区', '东区', '东区', '3', '510402', '510400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510322000000000', '富顺县', '富顺县', '富顺县', '3', '510322', '510300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510199000000000', '高新区', '高新区', '高新区', '3', '510199', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510301000000000', '高新区', '高新区', '高新区', '3', '510301', '510300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510311000000000', '沿滩区', '沿滩区', '沿滩区', '3', '510311', '510300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510522000000000', '合江县', '合江县', '合江县', '3', '510522', '510500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510183000000000', '邛崃市', '邛崃市', '邛崃市', '3', '510183', '510100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510302000000000', '自流井区', '自流井区', '自流井区', '3', '510302', '510300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510304000000000', '大安区', '大安区', '大安区', '3', '510304', '510300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510403000000000', '西区', '西区', '西区', '3', '510403', '510400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510422000000000', '盐边县', '盐边县', '盐边县', '3', '510422', '510400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510504000000000', '龙马潭区', '龙马潭区', '龙马潭区', '3', '510504', '510500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510683000000000', '绵竹市', '绵竹市', '绵竹市', '3', '510683', '510600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510703000000000', '涪城区', '涪城区', '涪城区', '3', '510703', '510700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510623000000000', '中江县', '中江县', '中江县', '3', '510623', '510600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510603000000000', '旌阳区', '旌阳区', '旌阳区', '3', '510603', '510600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510626000000000', '罗江县', '罗江县', '罗江县', '3', '510626', '510600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510525000000000', '古蔺县', '古蔺县', '古蔺县', '3', '510525', '510500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510524000000000', '叙永县', '叙永县', '叙永县', '3', '510524', '510500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510681000000000', '广汉市', '广汉市', '广汉市', '3', '510681', '510600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510682000000000', '什邡市', '什邡市', '什邡市', '3', '510682', '510600000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510722000000000', '三台县', '三台县', '三台县', '3', '510722', '510700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510704000000000', '游仙区', '游仙区', '游仙区', '3', '510704', '510700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510904000000000', '安居区', '安居区', '安居区', '3', '510904', '510900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510923000000000', '大英县', '大英县', '大英县', '3', '510923', '510900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511002000000000', '市中区', '市中区', '市中区', '3', '511002', '511000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511024000000000', '威远县', '威远县', '威远县', '3', '511024', '511000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510725000000000', '梓潼县', '梓潼县', '梓潼县', '3', '510725', '510700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510802000000000', '市中区', '市中区', '市中区', '3', '510802', '510800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510812000000000', '朝天区', '朝天区', '朝天区', '3', '510812', '510800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510803000000000', '广元经济开发区利州管委会', '广元经济开发区利州管委会', '广元经济开发区利州管委会', '3', '510803', '510800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510811000000000', '元坝区', '元坝区', '元坝区', '3', '510811', '510800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510823000000000', '剑阁县民政局', '剑阁县民政局', '剑阁县民政局', '3', '510823', '510800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510922000000000', '射洪县', '射洪县', '射洪县', '3', '510922', '510900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510921000000000', '蓬溪县', '蓬溪县', '蓬溪县', '3', '510921', '510900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511011000000000', '东兴区', '东兴区', '东兴区', '3', '511011', '511000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510723000000000', '盐亭县', '盐亭县', '盐亭县', '3', '510723', '510700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511025000000000', '资中县', '资中县', '资中县', '3', '511025', '511000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510724000000000', '安县', '安县', '安县', '3', '510724', '510700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510781000000000', '江油市', '江油市', '江油市', '3', '510781', '510700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510726000000000', '北川羌族自治县', '北川羌族自治县', '北川羌族自治县', '3', '510726', '510700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510727000000000', '平武县', '平武县', '平武县', '3', '510727', '510700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510821000000000', '旺苍县民政局', '旺苍县民政局', '旺苍县民政局', '3', '510821', '510800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510822000000000', '青川县', '青川县', '青川县', '3', '510822', '510800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510824000000000', '苍溪县', '苍溪县', '苍溪县', '3', '510824', '510800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510903000000000', '船山区', '船山区', '船山区', '3', '510903', '510900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511526000000000', '珙县', '珙县', '珙县', '3', '511526', '511500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511602000000000', '广安区', '广安区', '广安区', '3', '511602', '511600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511681000000000', '华蓥市', '华蓥市', '华蓥市', '3', '511681', '511600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511702000000000', '通川区', '通川区', '通川区', '3', '511702', '511700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511529000000000', '屏山县', '屏山县', '屏山县', '3', '511529', '511500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511621000000000', '岳池县', '岳池县', '岳池县', '3', '511621', '511600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511112000000000', '五通桥区', '五通桥区', '五通桥区', '3', '511112', '511100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511623000000000', '邻水县', '邻水县', '邻水县', '3', '511623', '511600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511111000000000', '乐山市沙湾区', '乐山市沙湾区', '乐山市沙湾区', '3', '511111', '511100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511721000000000', '达县', '达县', '达县', '3', '511721', '511700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511181000000000', '峨眉山市', '峨眉山市', '峨眉山市', '3', '511181', '511100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511302000000000', '顺庆区', '顺庆区', '顺庆区', '3', '511302', '511300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511126000000000', '夹江县', '夹江县', '夹江县', '3', '511126', '511100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511303000000000', '高坪区', '高坪区', '高坪区', '3', '511303', '511300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511129000000000', '沐川县', '沐川县', '沐川县', '3', '511129', '511100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511028000000000', '隆昌县', '隆昌县', '隆昌县', '3', '511028', '511000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511113000000000', '金口河区', '金口河区', '金口河区', '3', '511113', '511100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511102000000000', '市中区', '市中区', '市中区', '3', '511102', '511100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511123000000000', '犍为县', '犍为县', '犍为县', '3', '511123', '511100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511124000000000', '乐山市井研县', '乐山市井研县', '乐山市井研县', '3', '511124', '511100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511321000000000', '南部县', '南部县', '南部县', '3', '511321', '511300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511132000000000', '峨边彝族自治县', '峨边彝族自治县', '峨边彝族自治县', '3', '511132', '511100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511133000000000', '马边彝族自治县', '马边彝族自治县', '马边彝族自治县', '3', '511133', '511100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511322000000000', '营山县', '营山县', '营山县', '3', '511322', '511300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511324000000000', '仪陇县', '仪陇县', '仪陇县', '3', '511324', '511300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511304000000000', '嘉陵区', '嘉陵区', '嘉陵区', '3', '511304', '511300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511325000000000', '西充县', '西充县', '西充县', '3', '511325', '511300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511421000000000', '仁寿县', '仁寿县', '仁寿县', '3', '511421', '511400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511323000000000', '蓬安县', '蓬安县', '蓬安县', '3', '511323', '511300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511402000000000', '东坡区', '东坡区', '东坡区', '3', '511402', '511400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511422000000000', '彭山县', '彭山县', '彭山县', '3', '511422', '511400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511502000000000', '翠屏区', '翠屏区', '翠屏区', '3', '511502', '511500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511521000000000', '宜宾县', '宜宾县', '宜宾县', '3', '511521', '511500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511381000000000', '阆中市', '阆中市', '阆中市', '3', '511381', '511300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511523000000000', '江安县', '江安县', '江安县', '3', '511523', '511500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511524000000000', '长宁县', '长宁县', '长宁县', '3', '511524', '511500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511527000000000', '筠连县', '筠连县', '筠连县', '3', '511527', '511500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511528000000000', '兴文县', '兴文县', '兴文县', '3', '511528', '511500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511423000000000', '洪雅县', '洪雅县', '洪雅县', '3', '511423', '511400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511424000000000', '丹棱县', '丹棱县', '丹棱县', '3', '511424', '511400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511425000000000', '青神县', '青神县', '青神县', '3', '511425', '511400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511525000000000', '高县', '高县', '高县', '3', '511525', '511500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511522000000000', '南溪县', '南溪县', '南溪县', '3', '511522', '511500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511622000000000', '武胜县', '武胜县', '武胜县', '3', '511622', '511600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522634000000000', '雷山县', '雷山县', '雷山县', '3', '522634', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520221000000000', '水城县', '水城县', '水城县', '3', '520221', '520200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520123000000000', '修文县', '修文县', '修文县', '3', '520123', '520100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511722000000000', '宣汉县', '宣汉县', '宣汉县', '3', '511722', '511700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511723000000000', '开江县', '开江县', '开江县', '3', '511723', '511700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511724000000000', '大竹县', '大竹县', '大竹县', '3', '511724', '511700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511725000000000', '渠县', '渠县', '渠县', '3', '511725', '511700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511822000000000', '荥经县', '荥经县', '荥经县', '3', '511822', '511800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511823000000000', '汉源县', '汉源县', '汉源县', '3', '511823', '511800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511781000000000', '万源市', '万源市', '万源市', '3', '511781', '511700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511802000000000', '雨城区', '雨城区', '雨城区', '3', '511802', '511800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511821000000000', '名山县', '名山县', '名山县', '3', '511821', '511800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511824000000000', '石棉县', '石棉县', '石棉县', '3', '511824', '511800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511825000000000', '天全县', '天全县', '天全县', '3', '511825', '511800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511826000000000', '芦山县', '芦山县', '芦山县', '3', '511826', '511800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511827000000000', '宝兴县', '宝兴县', '宝兴县', '3', '511827', '511800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511902000000000', '巴州区', '巴州区', '巴州区', '3', '511902', '511900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511907000000000', '巴中市经济技术开发区', '巴中市经济技术开发区', '巴中市经济技术开发区', '3', '511907', '511900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511921000000000', '通江县', '通江县', '通江县', '3', '511921', '511900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511922000000000', '南江县', '南江县', '南江县', '3', '511922', '511900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511923000000000', '平昌县', '平昌县', '平昌县', '3', '511923', '511900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('512002000000000', '雁江区', '雁江区', '雁江区', '3', '512002', '512000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('512022000000000', '乐至县', '乐至县', '乐至县', '3', '512022', '512000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('512021000000000', '安岳县', '安岳县', '安岳县', '3', '512021', '512000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('512081000000000', '简阳市', '简阳市', '简阳市', '3', '512081', '512000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513226000000000', '金川县', '金川县', '金川县', '3', '513226', '513200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513227000000000', '小金县', '小金县', '小金县', '3', '513227', '513200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513228000000000', '黑水县', '黑水县', '黑水县', '3', '513228', '513200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513221000000000', '汶川县', '汶川县', '汶川县', '3', '513221', '513200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513222000000000', '理县', '理县', '理县', '3', '513222', '513200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513223000000000', '茂县', '茂县', '茂县', '3', '513223', '513200000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513224000000000', '松潘县', '松潘县', '松潘县', '3', '513224', '513200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513225000000000', '九寨沟县', '九寨沟县', '九寨沟县', '3', '513225', '513200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513324000000000', '九龙县', '九龙县', '九龙县', '3', '513324', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513325000000000', '雅江县', '雅江县', '雅江县', '3', '513325', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513326000000000', '道孚县', '道孚县', '道孚县', '3', '513326', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513229000000000', '马尔康县', '马尔康县', '马尔康县', '3', '513229', '513200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513230000000000', '壤塘县', '壤塘县', '壤塘县', '3', '513230', '513200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513232000000000', '若尔盖县', '若尔盖县', '若尔盖县', '3', '513232', '513200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513233000000000', '红原县', '红原县', '红原县', '3', '513233', '513200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513321000000000', '康定县', '康定县', '康定县', '3', '513321', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513322000000000', '泸定县', '泸定县', '泸定县', '3', '513322', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513323000000000', '丹巴县', '丹巴县', '丹巴县', '3', '513323', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513339000000000', '海螺沟景区管理局', '海螺沟景区管理局', '海螺沟景区管理局', '3', '513339', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513401000000000', '西昌市', '西昌市', '西昌市', '3', '513401', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513327000000000', '炉霍县', '炉霍县', '炉霍县', '3', '513327', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513328000000000', '甘孜县', '甘孜县', '甘孜县', '3', '513328', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513231000000000', '阿坝县', '阿坝县', '阿坝县', '3', '513231', '513200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513329000000000', '新龙县', '新龙县', '新龙县', '3', '513329', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513331000000000', '白玉县', '白玉县', '白玉县', '3', '513331', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513333000000000', '色达县', '色达县', '色达县', '3', '513333', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513334000000000', '理塘县', '理塘县', '理塘县', '3', '513334', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513335000000000', '巴塘县', '巴塘县', '巴塘县', '3', '513335', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513336000000000', '乡城县', '乡城县', '乡城县', '3', '513336', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513337000000000', '稻城县', '稻城县', '稻城县', '3', '513337', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513338000000000', '得荣县', '得荣县', '得荣县', '3', '513338', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513426000000000', '会东县', '会东县', '会东县', '3', '513426', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513422000000000', '木里藏族自治县', '木里藏族自治县', '木里藏族自治县', '3', '513422', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513423000000000', '盐源县', '盐源县', '盐源县', '3', '513423', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513330000000000', '德格县', '德格县', '德格县', '3', '513330', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513332000000000', '石渠县', '石渠县', '石渠县', '3', '513332', '513300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513424000000000', '德昌县', '德昌县', '德昌县', '3', '513424', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513425000000000', '会理县', '会理县', '会理县', '3', '513425', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513432000000000', '喜德县', '喜德县', '喜德县', '3', '513432', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513433000000000', '冕宁县', '冕宁县', '冕宁县', '3', '513433', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513427000000000', '宁南县', '宁南县', '宁南县', '3', '513427', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513429000000000', '布拖县', '布拖县', '布拖县', '3', '513429', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513430000000000', '金阳县', '金阳县', '金阳县', '3', '513430', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513431000000000', '昭觉县', '昭觉县', '昭觉县', '3', '513431', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513437000000000', '雷波县', '雷波县', '雷波县', '3', '513437', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513428000000000', '普格县', '普格县', '普格县', '3', '513428', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513435000000000', '甘洛县', '甘洛县', '甘洛县', '3', '513435', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513436000000000', '美姑县', '美姑县', '美姑县', '3', '513436', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520113000000000', '白云区', '白云区', '白云区', '3', '520113', '520100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520114000000000', '小河区', '小河区', '小河区', '3', '520114', '520100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520121000000000', '开阳县', '开阳县', '开阳县', '3', '520121', '520100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520122000000000', '息烽县', '息烽县', '息烽县', '3', '520122', '520100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513434000000000', '越西县', '越西县', '越西县', '3', '513434', '513400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520102000000000', '南明区', '南明区', '南明区', '3', '520102', '520100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520103000000000', '云岩区', '云岩区', '云岩区', '3', '520103', '520100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520111000000000', '花溪区', '花溪区', '花溪区', '3', '520111', '520100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520112000000000', '乌当区', '乌当区', '乌当区', '3', '520112', '520100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520203000000000', '六枝特区', '六枝特区', '六枝特区', '3', '520203', '520200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520181000000000', '清镇市', '清镇市', '清镇市', '3', '520181', '520100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520201000000000', '钟山区', '钟山区', '钟山区', '3', '520201', '520200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522635000000000', '麻江县', '麻江县', '麻江县', '3', '522635', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522636000000000', '丹寨县', '丹寨县', '丹寨县', '3', '522636', '522600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520222000000000', '盘县', '盘县', '盘县', '3', '520222', '520200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520322000000000', '桐梓县', '桐梓县', '桐梓县', '3', '520322', '520300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520323000000000', '绥阳县', '绥阳县', '绥阳县', '3', '520323', '520300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520302000000000', '红花岗区', '红花岗区', '红花岗区', '3', '520302', '520300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520321000000000', '遵义县', '遵义县', '遵义县', '3', '520321', '520300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520303000000000', '汇川区', '汇川区', '汇川区', '3', '520303', '520300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520381000000000', '赤水市', '赤水市', '赤水市', '3', '520381', '520300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520324000000000', '正安县', '正安县', '正安县', '3', '520324', '520300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520325000000000', '道真仡佬族苗族自治县', '道真仡佬族苗族自治县', '道真仡佬族苗族自治县', '3', '520325', '520300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520326000000000', '务川仡佬族苗族自治县', '务川仡佬族苗族自治县', '务川仡佬族苗族自治县', '3', '520326', '520300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640423000000000', '隆德县', '隆德县', '隆德县', '3', '640423', '640400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650103000000000', '沙依巴克区', '沙依巴克区', '沙依巴克区', '3', '650103', '650100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652826000000000', '焉耆县', '焉耆县', '焉耆县', '3', '652826', '652800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632523000000000', '贵德县', '贵德县', '贵德县', '3', '632523', '632500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640425000000000', '彭阳县', '彭阳县', '彭阳县', '3', '640425', '640400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653225000000000', '策勒县', '策勒县', '策勒县', '3', '653225', '653200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653201000000000', '和田市', '和田市', '和田市', '3', '653201', '653200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660613000000000', '军户农场', '军户农场', '军户农场', '3', '660613', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660614000000000', '共青团农场', '共青团农场', '共青团农场', '3', '660614', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660615000000000', '土墩子农场', '土墩子农场', '土墩子农场', '3', '660615', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660617000000000', '红旗农场', '红旗农场', '红旗农场', '3', '660617', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660618000000000', '奇台农场', '奇台农场', '奇台农场', '3', '660618', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660619000000000', '北塔山牧场', '北塔山牧场', '北塔山牧场', '3', '660619', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660701000000000', '一二三团', '一二三团', '一二三团', '3', '660701', '660700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660702000000000', '一二四团', '一二四团', '一二四团', '3', '660702', '660700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660703000000000', '一二五团', '一二五团', '一二五团', '3', '660703', '660700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660704000000000', '一二六团', '一二六团', '一二六团', '3', '660704', '660700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660705000000000', '一二八团', '一二八团', '一二八团', '3', '660705', '660700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660706000000000', '一二七团', '一二七团', '一二七团', '3', '660706', '660700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660707000000000', '一二九团', '一二九团', '一二九团', '3', '660707', '660700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660708000000000', '一三零团', '一三零团', '一三零团', '3', '660708', '660700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660709000000000', '一三一团', '一三一团', '一三一团', '3', '660709', '660700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660710000000000', '一三七团', '一三七团', '一三七团', '3', '660710', '660700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660711000000000', '九建', '九建', '九建', '3', '660711', '660700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660801000000000', '一二一团', '一二一团', '一二一团', '3', '660801', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660802000000000', '一二二团', '一二二团', '一二二团', '3', '660802', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660803000000000', '一三二团', '一三二团', '一三二团', '3', '660803', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660804000000000', '一三三团', '一三三团', '一三三团', '3', '660804', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660805000000000', '一三四团', '一三四团', '一三四团', '3', '660805', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660806000000000', '一三五团', '一三五团', '一三五团', '3', '660806', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660807000000000', '一三六团', '一三六团', '一三六团', '3', '660807', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660808000000000', '一四一团', '一四一团', '一四一团', '3', '660808', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660809000000000', '一四二团', '一四二团', '一四二团', '3', '660809', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660810000000000', '一四三团', '一四三团', '一四三团', '3', '660810', '660800000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660811000000000', '一四四团', '一四四团', '一四四团', '3', '660811', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660812000000000', '石河子总场', '石河子总场', '石河子总场', '3', '660812', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660813000000000', '一四七团', '一四七团', '一四七团', '3', '660813', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660814000000000', '一四八团', '一四八团', '一四八团', '3', '660814', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660815000000000', '一四九团', '一四九团', '一四九团', '3', '660815', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660816000000000', '一五零团', '一五零团', '一五零团', '3', '660816', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660817000000000', '一五一团', '一五一团', '一五一团', '3', '660817', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660818000000000', '一五二团', '一五二团', '一五二团', '3', '660818', '660800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660901000000000', '一六一团', '一六一团', '一六一团', '3', '660901', '660900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660902000000000', '一六二团', '一六二团', '一六二团', '3', '660902', '660900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660903000000000', '一六三团', '一六三团', '一六三团', '3', '660903', '660900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660904000000000', '一六四团', '一六四团', '一六四团', '3', '660904', '660900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660905000000000', '一六五团', '一六五团', '一六五团', '3', '660905', '660900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660906000000000', '一六六团', '一六六团', '一六六团', '3', '660906', '660900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660907000000000', '一六七团', '一六七团', '一六七团', '3', '660907', '660900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660908000000000', '一六八团', '一六八团', '一六八团', '3', '660908', '660900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660909000000000', '一六九团', '一六九团', '一六九团', '3', '660909', '660900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660910000000000', '一七零团', '一七零团', '一七零团', '3', '660910', '660900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660911000000000', '团结农场', '团结农场', '团结农场', '3', '660911', '660900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661001000000000', '一八一团', '一八一团', '一八一团', '3', '661001', '661000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661002000000000', '一八二团', '一八二团', '一八二团', '3', '661002', '661000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661003000000000', '一八三团', '一八三团', '一八三团', '3', '661003', '661000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661004000000000', '一八四团', '一八四团', '一八四团', '3', '661004', '661000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661005000000000', '一八五团', '一八五团', '一八五团', '3', '661005', '661000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661006000000000', '一八六团', '一八六团', '一八六团', '3', '661006', '661000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661007000000000', '一八七团', '一八七团', '一八七团', '3', '661007', '661000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661008000000000', '一八八团', '一八八团', '一八八团', '3', '661008', '661000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661009000000000', '一八九团', '一八九团', '一八九团', '3', '661009', '661000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661010000000000', '一九零团', '一九零团', '一九零团', '3', '661010', '661000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661011000000000', '青河农场', '青河农场', '青河农场', '3', '661011', '661000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661104000000000', '四团', '四团', '四团', '3', '661104', '661100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661105000000000', '五团', '五团', '五团', '3', '661105', '661100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661106000000000', '六团', '六团', '六团', '3', '661106', '661100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661107000000000', '七团', '七团', '七团', '3', '661107', '661100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661201000000000', '西山农场', '西山农场', '西山农场', '3', '661201', '661200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661202000000000', '一四团', '一四团', '一四团', '3', '661202', '661200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661203000000000', '头屯河农场', '头屯河农场', '头屯河农场', '3', '661203', '661200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661204000000000', '三坪农场', '三坪农场', '三坪农场', '3', '661204', '661200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661205000000000', '五一农场', '五一农场', '五一农场', '3', '661205', '661200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661206000000000', '养禽场', '养禽场', '养禽场', '3', '661206', '661200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661207000000000', '二二一团', '二二一团', '二二一团', '3', '661207', '661200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661301000000000', '红星一场', '红星一场', '红星一场', '3', '661301', '661300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661302000000000', '红星二场', '红星二场', '红星二场', '3', '661302', '661300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661303000000000', '红星三场', '红星三场', '红星三场', '3', '661303', '661300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661304000000000', '红星四场', '红星四场', '红星四场', '3', '661304', '661300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661305000000000', '火箭农场', '火箭农场', '火箭农场', '3', '661305', '661300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661306000000000', '柳树泉农场', '柳树泉农场', '柳树泉农场', '3', '661306', '661300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661307000000000', '黄田农场', '黄田农场', '黄田农场', '3', '661307', '661300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661308000000000', '红山农场', '红山农场', '红山农场', '3', '661308', '661300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661309000000000', '红星一牧场', '红星一牧场', '红星一牧场', '3', '661309', '661300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661310000000000', '红星二牧场', '红星二牧场', '红星二牧场', '3', '661310', '661300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661312000000000', '淖毛湖农场', '淖毛湖农场', '淖毛湖农场', '3', '661312', '661300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661330000000000', '县直辖村（虚拟编码）', '县直辖村（虚拟编码）', '县直辖村（虚拟编码）', '3', '661330', '661300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652323000000000', '呼图壁县', '呼图壁县', '呼图壁县', '3', '652323', '652300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652324000000000', '玛纳斯县', '玛纳斯县', '玛纳斯县', '3', '652324', '652300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654022000000000', '察布查尔县', '察布查尔县', '察布查尔县', '3', '654022', '654000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650106000000000', '头屯河区', '头屯河区', '头屯河区', '3', '650106', '650100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632722000000000', '杂多县', '杂多县', '杂多县', '3', '632722', '632700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640522000000000', '海原县', '海原县', '海原县', '3', '640522', '640500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652123000000000', '托克逊县', '托克逊县', '托克逊县', '3', '652123', '652100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652201000000000', '哈密市', '哈密市', '哈密市', '3', '652201', '652200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632322000000000', '尖扎县', '尖扎县', '尖扎县', '3', '632322', '632300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652824000000000', '若羌县', '若羌县', '若羌县', '3', '652824', '652800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654325000000000', '青河县', '青河县', '青河县', '3', '654325', '654300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654326000000000', '吉木乃县', '吉木乃县', '吉木乃县', '3', '654326', '654300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('659001000000000', '石河子市', '石河子市', '石河子市', '3', '659001', '659000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('659002000000000', '阿拉尔市', '阿拉尔市', '阿拉尔市', '3', '659002', '659000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('659003000000000', '图木舒克市', '图木舒克市', '图木舒克市', '3', '659003', '659000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('659004000000000', '五家渠市', '五家渠市', '五家渠市', '3', '659004', '659000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660101000000000', '一团', '一团', '一团', '3', '660101', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660102000000000', '二团', '二团', '二团', '3', '660102', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660103000000000', '三团', '三团', '三团', '3', '660103', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660104000000000', '四团', '四团', '四团', '3', '660104', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660105000000000', '五团', '五团', '五团', '3', '660105', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660106000000000', '六团', '六团', '六团', '3', '660106', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660107000000000', '七团', '七团', '七团', '3', '660107', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660108000000000', '八团', '八团', '八团', '3', '660108', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660109000000000', '九团', '九团', '九团', '3', '660109', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660110000000000', '十团', '十团', '十团', '3', '660110', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660111000000000', '十一团', '十一团', '十一团', '3', '660111', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660210000000000', '三十团', '三十团', '三十团', '3', '660210', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660211000000000', '三十一团', '三十一团', '三十一团', '3', '660211', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660212000000000', '三十二团', '三十二团', '三十二团', '3', '660212', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660213000000000', '三十三团', '三十三团', '三十三团', '3', '660213', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652723000000000', '温泉县', '温泉县', '温泉县', '3', '652723', '652700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632726000000000', '曲麻莱县', '曲麻莱县', '曲麻莱县', '3', '632726', '632700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632801000000000', '格尔木市', '格尔木市', '格尔木市', '3', '632801', '632800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652825000000000', '且末县', '且末县', '且末县', '3', '652825', '652800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652925000000000', '新和县', '新和县', '新和县', '3', '652925', '652900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653223000000000', '皮山县', '皮山县', '皮山县', '3', '653223', '653200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640424000000000', '泾源县', '泾源县', '泾源县', '3', '640424', '640400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660112000000000', '十二团', '十二团', '十二团', '3', '660112', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660113000000000', '十三团', '十三团', '十三团', '3', '660113', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660114000000000', '十四团', '十四团', '十四团', '3', '660114', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660115000000000', '十五团', '十五团', '十五团', '3', '660115', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660116000000000', '十六团', '十六团', '十六团', '3', '660116', '660100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660201000000000', '二十一团', '二十一团', '二十一团', '3', '660201', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660202000000000', '二十二团', '二十二团', '二十二团', '3', '660202', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660203000000000', '二十三团', '二十三团', '二十三团', '3', '660203', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660204000000000', '二十四团', '二十四团', '二十四团', '3', '660204', '660200000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660205000000000', '二十五团', '二十五团', '二十五团', '3', '660205', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660206000000000', '二十六团', '二十六团', '二十六团', '3', '660206', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660207000000000', '二十七团', '二十七团', '二十七团', '3', '660207', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660208000000000', '二十八团', '二十八团', '二十八团', '3', '660208', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660209000000000', '二十九团', '二十九团', '二十九团', '3', '660209', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660215000000000', '三十五团', '三十五团', '三十五团', '3', '660215', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660216000000000', '三十六团', '三十六团', '三十六团', '3', '660216', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660217000000000', '二二三团', '二二三团', '二二三团', '3', '660217', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660230000000000', '金三角商贸集团公司', '金三角商贸集团公司', '金三角商贸集团公司', '3', '660230', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660231000000000', '天宇公司', '天宇公司', '天宇公司', '3', '660231', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660301000000000', '四十一团', '四十一团', '四十一团', '3', '660301', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660302000000000', '四十二团', '四十二团', '四十二团', '3', '660302', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660303000000000', '四十三团', '四十三团', '四十三团', '3', '660303', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660304000000000', '四十四团', '四十四团', '四十四团', '3', '660304', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660305000000000', '四十五团', '四十五团', '四十五团', '3', '660305', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660306000000000', '四十六团', '四十六团', '四十六团', '3', '660306', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660307000000000', '四十八团', '四十八团', '四十八团', '3', '660307', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660308000000000', '四十九团', '四十九团', '四十九团', '3', '660308', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660309000000000', '五十团', '五十团', '五十团', '3', '660309', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660310000000000', '五十一团', '五十一团', '五十一团', '3', '660310', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660311000000000', '五十二团', '五十二团', '五十二团', '3', '660311', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660312000000000', '五十三团', '五十三团', '五十三团', '3', '660312', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660313000000000', '伽师农场', '伽师农场', '伽师农场', '3', '660313', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660314000000000', '东风农场', '东风农场', '东风农场', '3', '660314', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660315000000000', '红旗农场', '红旗农场', '红旗农场', '3', '660315', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660317000000000', '叶城农场', '叶城农场', '叶城农场', '3', '660317', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660318000000000', '托云农场', '托云农场', '托云农场', '3', '660318', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660319000000000', '工程团', '工程团', '工程团', '3', '660319', '660300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660401000000000', '六十一团', '六十一团', '六十一团', '3', '660401', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660402000000000', '六十二团', '六十二团', '六十二团', '3', '660402', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660403000000000', '六十三团', '六十三团', '六十三团', '3', '660403', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660404000000000', '六十四团', '六十四团', '六十四团', '3', '660404', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660405000000000', '六十五团', '六十五团', '六十五团', '3', '660405', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660406000000000', '六十六团', '六十六团', '六十六团', '3', '660406', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660407000000000', '六十七团', '六十七团', '六十七团', '3', '660407', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660408000000000', '六十八团', '六十八团', '六十八团', '3', '660408', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660409000000000', '六十九团', '六十九团', '六十九团', '3', '660409', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660410000000000', '七十团', '七十团', '七十团', '3', '660410', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660411000000000', '七十一团', '七十一团', '七十一团', '3', '660411', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660412000000000', '七十二团', '七十二团', '七十二团', '3', '660412', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660413000000000', '七十三团', '七十三团', '七十三团', '3', '660413', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660414000000000', '七十四团', '七十四团', '七十四团', '3', '660414', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660415000000000', '七十五团', '七十五团', '七十五团', '3', '660415', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660416000000000', '七十六团', '七十六团', '七十六团', '3', '660416', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660417000000000', '七十七团', '七十七团', '七十七团', '3', '660417', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660418000000000', '七十八团', '七十八团', '七十八团', '3', '660418', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660419000000000', '七十九团', '七十九团', '七十九团', '3', '660419', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660420000000000', '拜什墩农场', '拜什墩农场', '拜什墩农场', '3', '660420', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660421000000000', '粮繁场', '粮繁场', '粮繁场', '3', '660421', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660430000000000', '县直辖村（虚拟编码）', '县直辖村（虚拟编码）', '县直辖村（虚拟编码）', '3', '660430', '660400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660501000000000', '八十一团', '八十一团', '八十一团', '3', '660501', '660500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660502000000000', '八十二团', '八十二团', '八十二团', '3', '660502', '660500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660503000000000', '八十三团', '八十三团', '八十三团', '3', '660503', '660500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660504000000000', '八十四团', '八十四团', '八十四团', '3', '660504', '660500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660505000000000', '八十五团', '八十五团', '八十五团', '3', '660505', '660500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660506000000000', '八十六团', '八十六团', '八十六团', '3', '660506', '660500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211100000000000', '盘锦市', '盘锦市', '盘锦市', '2', '2111', '210000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211300000000000', '朝阳市', '朝阳市', '朝阳市', '2', '2113', '210000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211200000000000', '铁岭市', '铁岭市', '铁岭市', '2', '2112', '210000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211400000000000', '葫芦岛市', '葫芦岛市', '葫芦岛市', '2', '2114', '210000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220100000000000', '长春市', '长春市', '长春市', '2', '2201', '220000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220400000000000', '辽源市', '辽源市', '辽源市', '2', '2204', '220000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220300000000000', '四平市', '四平市', '四平市', '2', '2203', '220000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220200000000000', '吉林市', '吉林市', '吉林市', '2', '2202', '220000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220600000000000', '白山市', '白山市', '白山市', '2', '2206', '220000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220500000000000', '通化市', '通化市', '通化市', '2', '2205', '220000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220700000000000', '松原市', '松原市', '松原市', '2', '2207', '220000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220800000000000', '白城市', '白城市', '白城市', '2', '2208', '220000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230100000000000', '哈尔滨市', '哈尔滨市', '哈尔滨市', '2', '2301', '230000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('222400000000000', '延边朝鲜族自治州', '延边朝鲜族自治州', '延边朝鲜族自治州', '2', '2224', '220000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230300000000000', '鸡西市', '鸡西市', '鸡西市', '2', '2303', '230000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230500000000000', '双鸭山市', '双鸭山市', '双鸭山市', '2', '2305', '230000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230200000000000', '齐齐哈尔市', '齐齐哈尔市', '齐齐哈尔市', '2', '2302', '230000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230400000000000', '鹤岗市', '鹤岗市', '鹤岗市', '2', '2304', '230000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230600000000000', '大庆市', '大庆市', '大庆市', '2', '2306', '230000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230700000000000', '伊春市', '伊春市', '伊春市', '2', '2307', '230000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230900000000000', '七台河市', '七台河市', '七台河市', '2', '2309', '230000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231000000000000', '牡丹江市', '牡丹江市', '牡丹江市', '2', '2310', '230000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230800000000000', '佳木斯市', '佳木斯市', '佳木斯市', '2', '2308', '230000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('239900000000000', '黑龙江垦区', '黑龙江垦区', '黑龙江垦区', '2', '2399', '230000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231200000000000', '绥化市', '绥化市', '绥化市', '2', '2312', '230000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('231100000000000', '黑河市', '黑河市', '黑河市', '2', '2311', '230000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('232700000000000', '大兴安岭地区', '大兴安岭地区', '大兴安岭地区', '2', '2327', '230000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310100000000000', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '2', '3101', '310000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320100000000000', '南京市', '南京市', '南京市', '2', '3201', '320000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310200000000000', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '2', '3102', '310000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320200000000000', '无锡市', '无锡市', '无锡市', '2', '3202', '320000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320300000000000', '徐州市', '徐州市', '徐州市', '2', '3203', '320000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320500000000000', '苏州市', '苏州市', '苏州市', '2', '3205', '320000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320400000000000', '常州市', '常州市', '常州市', '2', '3204', '320000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320700000000000', '连云港市', '连云港市', '连云港市', '2', '3207', '320000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320600000000000', '南通市', '南通市', '南通市', '2', '3206', '320000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320800000000000', '淮安市', '淮安市', '淮安市', '2', '3208', '320000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320900000000000', '盐城市', '盐城市', '盐城市', '2', '3209', '320000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321200000000000', '泰州市', '泰州市', '泰州市', '2', '3212', '320000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321300000000000', '宿迁市', '宿迁市', '宿迁市', '2', '3213', '320000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321100000000000', '镇江市', '镇江市', '镇江市', '2', '3211', '320000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('321000000000000', '扬州市', '扬州市', '扬州市', '2', '3210', '320000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330100000000000', '杭州市', '杭州市', '杭州市', '2', '3301', '330000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330300000000000', '温州市', '温州市', '温州市', '2', '3303', '330000000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330200000000000', '宁波市', '宁波市', '宁波市', '2', '3302', '330000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330600000000000', '绍兴市', '绍兴市', '绍兴市', '2', '3306', '330000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330400000000000', '嘉兴市', '嘉兴市', '嘉兴市', '2', '3304', '330000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330500000000000', '湖州市', '湖州市', '湖州市', '2', '3305', '330000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330700000000000', '金华市', '金华市', '金华市', '2', '3307', '330000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330800000000000', '衢州市', '衢州市', '衢州市', '2', '3308', '330000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331000000000000', '台州市', '台州市', '台州市', '2', '3310', '330000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330900000000000', '舟山市', '舟山市', '舟山市', '2', '3309', '330000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('331100000000000', '丽水市', '丽水市', '丽水市', '2', '3311', '330000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340100000000000', '合肥市', '合肥市', '合肥市', '2', '3401', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340500000000000', '马鞍山市', '马鞍山市', '马鞍山市', '2', '3405', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340700000000000', '铜陵市', '铜陵市', '铜陵市', '2', '3407', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340300000000000', '蚌埠市', '蚌埠市', '蚌埠市', '2', '3403', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340400000000000', '淮南市', '淮南市', '淮南市', '2', '3404', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340600000000000', '淮北市', '淮北市', '淮北市', '2', '3406', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340200000000000', '芜湖市', '芜湖市', '芜湖市', '2', '3402', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341000000000000', '黄山市', '黄山市', '黄山市', '2', '3410', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341100000000000', '滁州市', '滁州市', '滁州市', '2', '3411', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340800000000000', '安庆市', '安庆市', '安庆市', '2', '3408', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341200000000000', '阜阳市', '阜阳市', '阜阳市', '2', '3412', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341300000000000', '宿州市', '宿州市', '宿州市', '2', '3413', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341500000000000', '六安市', '六安市', '六安市', '2', '3415', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341700000000000', '池州市', '池州市', '池州市', '2', '3417', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341400000000000', '巢湖市', '巢湖市', '巢湖市', '2', '3414', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341600000000000', '亳州市', '亳州市', '亳州市', '2', '3416', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350100000000000', '福州市', '福州市', '福州市', '2', '3501', '350000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('341800000000000', '宣城市', '宣城市', '宣城市', '2', '3418', '340000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350400000000000', '三明市', '三明市', '三明市', '2', '3504', '350000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350300000000000', '莆田市', '莆田市', '莆田市', '2', '3503', '350000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350500000000000', '泉州市', '泉州市', '泉州市', '2', '3505', '350000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350200000000000', '厦门市', '厦门市', '厦门市', '2', '3502', '350000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350600000000000', '漳州市', '漳州市', '漳州市', '2', '3506', '350000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350700000000000', '南平市', '南平市', '南平市', '2', '3507', '350000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350900000000000', '宁德市　', '宁德市　', '宁德市　', '2', '3509', '350000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350800000000000', '龙岩市', '龙岩市', '龙岩市', '2', '3508', '350000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360400000000000', '九江市', '九江市', '九江市', '2', '3604', '360000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360300000000000', '萍乡市', '萍乡市', '萍乡市', '2', '3603', '360000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360100000000000', '南昌市', '南昌市', '南昌市', '2', '3601', '360000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360200000000000', '景德镇市', '景德镇市', '景德镇市', '2', '3602', '360000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360500000000000', '新余市', '新余市', '新余市', '2', '3605', '360000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360700000000000', '赣州市', '赣州市', '赣州市', '2', '3607', '360000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360600000000000', '鹰潭市', '鹰潭市', '鹰潭市', '2', '3606', '360000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360800000000000', '吉安市', '吉安市', '吉安市', '2', '3608', '360000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360900000000000', '宜春市', '宜春市', '宜春市', '2', '3609', '360000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361000000000000', '抚州市', '抚州市', '抚州市', '2', '3610', '360000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('361100000000000', '上饶市', '上饶市', '上饶市', '2', '3611', '360000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370100000000000', '济南市', '济南市', '济南市', '2', '3701', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370200000000000', '青岛市', '青岛市', '青岛市', '2', '3702', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370300000000000', '淄博市', '淄博市', '淄博市', '2', '3703', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370400000000000', '枣庄市', '枣庄市', '枣庄市', '2', '3704', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370600000000000', '烟台市', '烟台市', '烟台市', '2', '3706', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370500000000000', '东营市', '东营市', '东营市', '2', '3705', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370700000000000', '潍坊市', '潍坊市', '潍坊市', '2', '3707', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370800000000000', '济宁市', '济宁市', '济宁市', '2', '3708', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370900000000000', '泰安市', '泰安市', '泰安市', '2', '3709', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371000000000000', '威海市', '威海市', '威海市', '2', '3710', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371100000000000', '日照市', '日照市', '日照市', '2', '3711', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371200000000000', '莱芜市', '莱芜市', '莱芜市', '2', '3712', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371300000000000', '临沂市', '临沂市', '临沂市', '2', '3713', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371400000000000', '德州市', '德州市', '德州市', '2', '3714', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371500000000000', '聊城市', '聊城市', '聊城市', '2', '3715', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371600000000000', '滨州市', '滨州市', '滨州市', '2', '3716', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('371700000000000', '菏泽市', '菏泽市', '菏泽市', '2', '3717', '370000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410100000000000', '郑州市', '郑州市', '郑州市', '2', '4101', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410200000000000', '开封市', '开封市', '开封市', '2', '4102', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410300000000000', '洛阳市', '洛阳市', '洛阳市', '2', '4103', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410400000000000', '平顶山市', '平顶山市', '平顶山市', '2', '4104', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410500000000000', '安阳市', '安阳市', '安阳市', '2', '4105', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410600000000000', '鹤壁市', '鹤壁市', '鹤壁市', '2', '4106', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410700000000000', '新乡市', '新乡市', '新乡市', '2', '4107', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410800000000000', '焦作市', '焦作市', '焦作市', '2', '4108', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410900000000000', '濮阳市', '濮阳市', '濮阳市', '2', '4109', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411100000000000', '漯河市', '漯河市', '漯河市', '2', '4111', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411000000000000', '许昌市', '许昌市', '许昌市', '2', '4110', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411200000000000', '三门峡市', '三门峡市', '三门峡市', '2', '4112', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411300000000000', '南阳市', '南阳市', '南阳市', '2', '4113', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411400000000000', '商丘市', '商丘市', '商丘市', '2', '4114', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411500000000000', '信阳市', '信阳市', '信阳市', '2', '4115', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411600000000000', '周口市', '周口市', '周口市', '2', '4116', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('412900000000000', '济源市', '济源市', '济源市', '2', '4129', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420100000000000', '武汉市', '武汉市', '武汉市', '2', '4201', '420000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('411700000000000', '驻马店市', '驻马店市', '驻马店市', '2', '4117', '410000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420200000000000', '黄石市', '黄石市', '黄石市', '2', '4202', '420000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420300000000000', '十堰市', '十堰市', '十堰市', '2', '4203', '420000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420500000000000', '宜昌市', '宜昌市', '宜昌市', '2', '4205', '420000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420600000000000', '襄樊市', '襄樊市', '襄樊市', '2', '4206', '420000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420900000000000', '孝感市', '孝感市', '孝感市', '2', '4209', '420000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420700000000000', '鄂州市', '鄂州市', '鄂州市', '2', '4207', '420000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420800000000000', '荆门市', '荆门市', '荆门市', '2', '4208', '420000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421000000000000', '荆州市', '荆州市', '荆州市', '2', '4210', '420000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421100000000000', '黄冈市', '黄冈市', '黄冈市', '2', '4211', '420000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421300000000000', '随州市', '随州市', '随州市', '2', '4213', '420000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('421200000000000', '咸宁市', '咸宁市', '咸宁市', '2', '4212', '420000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('429000000000000', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '2', '4290', '420000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('422800000000000', '恩施土家族苗族自治州', '恩施土家族苗族自治州', '恩施土家族苗族自治州', '2', '4228', '420000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430100000000000', '长沙市', '长沙市', '长沙市', '2', '4301', '430000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430200000000000', '株洲市', '株洲市', '株洲市', '2', '4302', '430000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430300000000000', '湘潭市', '湘潭市', '湘潭市', '2', '4303', '430000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430400000000000', '衡阳市', '衡阳市', '衡阳市', '2', '4304', '430000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430500000000000', '邵阳市', '邵阳市', '邵阳市', '2', '4305', '430000000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430600000000000', '岳阳市', '岳阳市', '岳阳市', '2', '4306', '430000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430700000000000', '常德市', '常德市', '常德市', '2', '4307', '430000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430800000000000', '张家界市', '张家界市', '张家界市', '2', '4308', '430000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430900000000000', '益阳市', '益阳市', '益阳市', '2', '4309', '430000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431000000000000', '郴州市', '郴州市', '郴州市', '2', '4310', '430000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431100000000000', '永州市', '永州市', '永州市', '2', '4311', '430000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431200000000000', '怀化市', '怀化市', '怀化市', '2', '4312', '430000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('431300000000000', '娄底市', '娄底市', '娄底市', '2', '4313', '430000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('433100000000000', '湘西土家族苗族自治州', '湘西土家族苗族自治州', '湘西土家族苗族自治州', '2', '4331', '430000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440100000000000', '广州市', '广州市', '广州市', '2', '4401', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440400000000000', '珠海市', '珠海市', '珠海市', '2', '4404', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440500000000000', '汕头市', '汕头市', '汕头市', '2', '4405', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440300000000000', '深圳市', '深圳市', '深圳市', '2', '4403', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440200000000000', '韶关市', '韶关市', '韶关市', '2', '4402', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440700000000000', '江门市', '江门市', '江门市', '2', '4407', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440800000000000', '湛江市', '湛江市', '湛江市', '2', '4408', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440600000000000', '佛山市', '佛山市', '佛山市', '2', '4406', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440900000000000', '茂名市', '茂名市', '茂名市', '2', '4409', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441300000000000', '惠州市', '惠州市', '惠州市', '2', '4413', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441200000000000', '肇庆市', '肇庆市', '肇庆市', '2', '4412', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441500000000000', '汕尾市', '汕尾市', '汕尾市', '2', '4415', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441600000000000', '河源市', '河源市', '河源市', '2', '4416', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441400000000000', '梅州市', '梅州市', '梅州市', '2', '4414', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445100000000000', '潮州市', '潮州市', '潮州市', '2', '4451', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441700000000000', '阳江市', '阳江市', '阳江市', '2', '4417', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('442000000000000', '中山市', '中山市', '中山市', '2', '4420', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441800000000000', '清远市', '清远市', '清远市', '2', '4418', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('441900000000000', '东莞市', '东莞市', '东莞市', '2', '4419', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450100000000000', '南宁市', '南宁市', '南宁市', '2', '4501', '450000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445200000000000', '揭阳市', '揭阳市', '揭阳市', '2', '4452', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('445300000000000', '云浮市', '云浮市', '云浮市', '2', '4453', '440000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450400000000000', '梧州市', '梧州市', '梧州市', '2', '4504', '450000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450200000000000', '柳州市', '柳州市', '柳州市', '2', '4502', '450000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450500000000000', '北海市', '北海市', '北海市', '2', '4505', '450000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450300000000000', '桂林市', '桂林市', '桂林市', '2', '4503', '450000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450600000000000', '防城港市', '防城港市', '防城港市', '2', '4506', '450000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450700000000000', '钦州市', '钦州市', '钦州市', '2', '4507', '450000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450800000000000', '贵港市', '贵港市', '贵港市', '2', '4508', '450000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451400000000000', '崇左市', '崇左市', '崇左市', '2', '4514', '450000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450900000000000', '玉林市', '玉林市', '玉林市', '2', '4509', '450000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451000000000000', '百色市', '百色市', '百色市', '2', '4510', '450000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451200000000000', '河池市', '河池市', '河池市', '2', '4512', '450000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451300000000000', '来宾市', '来宾市', '来宾市', '2', '4513', '450000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('451100000000000', '贺州市', '贺州市', '贺州市', '2', '4511', '450000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('460200000000000', '三亚市', '三亚市', '三亚市', '2', '4602', '460000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('469000000000000', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '2', '4690', '460000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('460100000000000', '海口市', '海口市', '海口市', '2', '4601', '460000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500100000000000', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '2', '5001', '500000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500200000000000', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '2', '5002', '500000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500300000000000', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '2', '5003', '500000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510100000000000', '成都市', '成都市', '成都市', '2', '5101', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510500000000000', '泸州市', '泸州市', '泸州市', '2', '5105', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510400000000000', '攀枝花市', '攀枝花市', '攀枝花市', '2', '5104', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510300000000000', '自贡市', '自贡市', '自贡市', '2', '5103', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510700000000000', '绵阳市', '绵阳市', '绵阳市', '2', '5107', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510600000000000', '德阳市', '德阳市', '德阳市', '2', '5106', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511000000000000', '内江市', '内江市', '内江市', '2', '5110', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510800000000000', '广元市', '广元市', '广元市', '2', '5108', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660507000000000', '八十七团', '八十七团', '八十七团', '3', '660507', '660500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660508000000000', '八十八团', '八十八团', '八十八团', '3', '660508', '660500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660509000000000', '八十九团', '八十九团', '八十九团', '3', '660509', '660500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660510000000000', '九十团', '九十团', '九十团', '3', '660510', '660500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660511000000000', '九十一团', '九十一团', '九十一团', '3', '660511', '660500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660601000000000', '一零一团', '一零一团', '一零一团', '3', '660601', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660602000000000', '一零二团', '一零二团', '一零二团', '3', '660602', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660603000000000', '一零三团', '一零三团', '一零三团', '3', '660603', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660604000000000', '一零五团', '一零五团', '一零五团', '3', '660604', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660605000000000', '一零六团', '一零六团', '一零六团', '3', '660605', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640521000000000', '中宁县', '中宁县', '中宁县', '3', '640521', '640500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653227000000000', '民丰县', '民丰县', '民丰县', '3', '653227', '653200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654002000000000', '伊宁市', '伊宁市', '伊宁市', '3', '654002', '654000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653121000000000', '疏附县', '疏附县', '疏附县', '3', '653121', '653100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640104000000000', '兴庆区民政局', '兴庆区民政局', '兴庆区民政局', '3', '640104', '640100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650105000000000', '水磨沟区', '水磨沟区', '水磨沟区', '3', '650105', '650100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652927000000000', '乌什县', '乌什县', '乌什县', '3', '652927', '652900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660214000000000', '三十四团', '三十四团', '三十四团', '3', '660214', '660200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632624000000000', '达日县', '达日县', '达日县', '3', '632624', '632600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652122000000000', '鄯善县', '鄯善县', '鄯善县', '3', '652122', '652100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653022000000000', '阿克陶县', '阿克陶县', '阿克陶县', '3', '653022', '653000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654028000000000', '尼勒克县', '尼勒克县', '尼勒克县', '3', '654028', '654000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654221000000000', '额敏县', '额敏县', '额敏县', '3', '654221', '654200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640324000000000', '同心县', '同心县', '同心县', '3', '640324', '640300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652327000000000', '吉木萨尔县', '吉木萨尔县', '吉木萨尔县', '3', '652327', '652300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653124000000000', '泽普县', '泽普县', '泽普县', '3', '653124', '653100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654025000000000', '新源县', '新源县', '新源县', '3', '654025', '654000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654301000000000', '阿勒泰市', '阿勒泰市', '阿勒泰市', '3', '654301', '654300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654321000000000', '布尔津县', '布尔津县', '布尔津县', '3', '654321', '654300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654322000000000', '富蕴县', '富蕴县', '富蕴县', '3', '654322', '654300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654323000000000', '福海县', '福海县', '福海县', '3', '654323', '654300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640181000000000', '灵武市', '灵武市', '灵武市', '3', '640181', '640100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632802000000000', '德令哈市', '德令哈市', '德令哈市', '3', '632802', '632800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650102000000000', '天山区', '天山区', '天山区', '3', '650102', '650100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652823000000000', '尉犁县', '尉犁县', '尉犁县', '3', '652823', '652800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632521000000000', '共和县民政局', '共和县民政局', '共和县民政局', '3', '632521', '632500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652828000000000', '和硕县', '和硕县', '和硕县', '3', '652828', '652800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652829000000000', '博湖县', '博湖县', '博湖县', '3', '652829', '652800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652901000000000', '阿克苏市', '阿克苏市', '阿克苏市', '3', '652901', '652900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654324000000000', '哈巴河县', '哈巴河县', '哈巴河县', '3', '654324', '654300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640402000000000', '原州区', '原州区', '原州区', '3', '640402', '640400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652722000000000', '精河县', '精河县', '精河县', '3', '652722', '652700000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653127000000000', '麦盖提县', '麦盖提县', '麦盖提县', '3', '653127', '653100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653222000000000', '墨玉县', '墨玉县', '墨玉县', '3', '653222', '653200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640221000000000', '平罗县', '平罗县', '平罗县', '3', '640221', '640200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652302000000000', '阜康市', '阜康市', '阜康市', '3', '652302', '652300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652303000000000', '米泉市', '米泉市', '米泉市', '3', '652303', '652300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632824000000000', '茫崖行委', '茫崖行委', '茫崖行委', '3', '632824', '632800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632825000000000', '大柴旦行委', '大柴旦行委', '大柴旦行委', '3', '632825', '632800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110100000000000', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '2', '1101', '110000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110200000000000', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '2', '1102', '110000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120100000000000', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '2', '1201', '120000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120200000000000', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '2', '1202', '120000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130100000000000', '石家庄市', '石家庄市', '石家庄市', '2', '1301', '130000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130200000000000', '唐山市', '唐山市', '唐山市', '2', '1302', '130000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130300000000000', '秦皇岛市', '秦皇岛市', '秦皇岛市', '2', '1303', '130000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130400000000000', '邯郸市', '邯郸市', '邯郸市', '2', '1304', '130000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130500000000000', '邢台市', '邢台市', '邢台市', '2', '1305', '130000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130600000000000', '保定市', '保定市', '保定市', '2', '1306', '130000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130700000000000', '张家口市', '张家口市', '张家口市', '2', '1307', '130000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130800000000000', '承德市', '承德市', '承德市', '2', '1308', '130000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130900000000000', '沧州市', '沧州市', '沧州市', '2', '1309', '130000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131100000000000', '衡水市', '衡水市', '衡水市', '2', '1311', '130000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('131000000000000', '廊坊市', '廊坊市', '廊坊市', '2', '1310', '130000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140100000000000', '太原市', '太原市', '太原市', '2', '1401', '140000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140200000000000', '大同市', '大同市', '大同市', '2', '1402', '140000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140400000000000', '长治市', '长治市', '长治市', '2', '1404', '140000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140300000000000', '阳泉市', '阳泉市', '阳泉市', '2', '1403', '140000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140500000000000', '晋城市', '晋城市', '晋城市', '2', '1405', '140000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140600000000000', '朔州市', '朔州市', '朔州市', '2', '1406', '140000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140700000000000', '晋中市', '晋中市', '晋中市', '2', '1407', '140000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140800000000000', '运城市', '运城市', '运城市', '2', '1408', '140000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140900000000000', '忻州市', '忻州市', '忻州市', '2', '1409', '140000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141000000000000', '临汾市', '临汾市', '临汾市', '2', '1410', '140000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('141100000000000', '吕梁市', '吕梁市', '吕梁市', '2', '1411', '140000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150300000000000', '乌海市', '乌海市', '乌海市', '2', '1503', '150000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150400000000000', '赤峰市', '赤峰市', '赤峰市', '2', '1504', '150000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150200000000000', '包头市', '包头市', '包头市', '2', '1502', '150000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150100000000000', '呼和浩特市', '呼和浩特市', '呼和浩特市', '2', '1501', '150000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150500000000000', '通辽市', '通辽市', '通辽市', '2', '1505', '150000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150700000000000', '呼伦贝尔市', '呼伦贝尔市', '呼伦贝尔市', '2', '1507', '150000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150800000000000', '巴彦淖尔市', '巴彦淖尔市', '巴彦淖尔市', '2', '1508', '150000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150600000000000', '鄂尔多斯市', '鄂尔多斯市', '鄂尔多斯市', '2', '1506', '150000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152200000000000', '兴安盟', '兴安盟', '兴安盟', '2', '1522', '150000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150900000000000', '乌兰察布市', '乌兰察布市', '乌兰察布市', '2', '1509', '150000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152500000000000', '锡林郭勒盟', '锡林郭勒盟', '锡林郭勒盟', '2', '1525', '150000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210200000000000', '大连市', '大连市', '大连市', '2', '2102', '210000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('152900000000000', '阿拉善盟', '阿拉善盟', '阿拉善盟', '2', '1529', '150000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210100000000000', '沈阳市', '沈阳市', '沈阳市', '2', '2101', '210000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210300000000000', '鞍山市', '鞍山市', '鞍山市', '2', '2103', '210000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210400000000000', '抚顺市', '抚顺市', '抚顺市', '2', '2104', '210000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210500000000000', '本溪市', '本溪市', '本溪市', '2', '2105', '210000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210600000000000', '丹东市', '丹东市', '丹东市', '2', '2106', '210000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210900000000000', '阜新市', '阜新市', '阜新市', '2', '2109', '210000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210700000000000', '锦州市', '锦州市', '锦州市', '2', '2107', '210000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210800000000000', '营口市', '营口市', '营口市', '2', '2108', '210000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('211000000000000', '辽阳市', '辽阳市', '辽阳市', '2', '2110', '210000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620105000000000', '安宁区', '安宁区', '安宁区', '3', '620105', '620100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620111000000000', '红古区', '红古区', '红古区', '3', '620111', '620100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620121000000000', '永登县', '永登县', '永登县', '3', '620121', '620100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620423000000000', '景泰县', '景泰县', '景泰县', '3', '620423', '620400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620201000000000', '市辖区', '市辖区', '市辖区', '3', '620201', '620200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620302000000000', '金川区', '金川区', '金川区', '3', '620302', '620300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620321000000000', '永昌县', '永昌县', '永昌县', '3', '620321', '620300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620402000000000', '白银区', '白银区', '白银区', '3', '620402', '620400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620403000000000', '平川区', '平川区', '平川区', '3', '620403', '620400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620421000000000', '靖远县', '靖远县', '靖远县', '3', '620421', '620400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620422000000000', '会宁县', '会宁县', '会宁县', '3', '620422', '620400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620521000000000', '清水县', '清水县', '清水县', '3', '620521', '620500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620522000000000', '秦安县', '秦安县', '秦安县', '3', '620522', '620500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632625000000000', '久治县', '久治县', '久治县', '3', '632625', '632600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632626000000000', '玛多县', '玛多县', '玛多县', '3', '632626', '632600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632721000000000', '玉树县', '玉树县', '玉树县', '3', '632721', '632700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632221000000000', '门源县', '门源县', '门源县', '3', '632221', '632200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632222000000000', '祁连县', '祁连县', '祁连县', '3', '632222', '632200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632321000000000', '同仁县', '同仁县', '同仁县', '3', '632321', '632300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632223000000000', '海晏县', '海晏县', '海晏县', '3', '632223', '632200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632224000000000', '刚察县', '刚察县', '刚察县', '3', '632224', '632200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620503000000000', '麦积区', '麦积区', '麦积区', '3', '620503', '620500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620524000000000', '武山县', '武山县', '武山县', '3', '620524', '620500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620523000000000', '甘谷县', '甘谷县', '甘谷县', '3', '620523', '620500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620621000000000', '民勤县', '民勤县', '民勤县', '3', '620621', '620600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620622000000000', '古浪县', '古浪县', '古浪县', '3', '620622', '620600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620525000000000', '张家川回族自治县', '张家川回族自治县', '张家川回族自治县', '3', '620525', '620500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620602000000000', '凉州区', '凉州区', '凉州区', '3', '620602', '620600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620723000000000', '临泽县', '临泽县', '临泽县', '3', '620723', '620700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620724000000000', '高台县', '高台县', '高台县', '3', '620724', '620700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620623000000000', '天祝藏族自治县', '天祝藏族自治县', '天祝藏族自治县', '3', '620623', '620600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620702000000000', '甘州区', '甘州区', '甘州区', '3', '620702', '620700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620721000000000', '肃南裕固族自治县', '肃南裕固族自治县', '肃南裕固族自治县', '3', '620721', '620700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620722000000000', '民乐县', '民乐县', '民乐县', '3', '620722', '620700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620823000000000', '崇信县', '崇信县', '崇信县', '3', '620823', '620800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620824000000000', '华亭县', '华亭县', '华亭县', '3', '620824', '620800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620825000000000', '庄浪县', '庄浪县', '庄浪县', '3', '620825', '620800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620725000000000', '山丹县', '山丹县', '山丹县', '3', '620725', '620700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620802000000000', '崆峒区', '崆峒区', '崆峒区', '3', '620802', '620800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620821000000000', '泾川县', '泾川县', '泾川县', '3', '620821', '620800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620822000000000', '灵台县', '灵台县', '灵台县', '3', '620822', '620800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620921000000000', '金塔县', '金塔县', '金塔县', '3', '620921', '620900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620922000000000', '安西县', '安西县', '安西县', '3', '620922', '620900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620923000000000', '肃北蒙古族自治县', '肃北蒙古族自治县', '肃北蒙古族自治县', '3', '620923', '620900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620924000000000', '阿克塞哈萨克自治县', '阿克塞哈萨克自治县', '阿克塞哈萨克自治县', '3', '620924', '620900000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620981000000000', '玉门市', '玉门市', '玉门市', '3', '620981', '620900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620826000000000', '静宁县', '静宁县', '静宁县', '3', '620826', '620800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620902000000000', '肃州区', '肃州区', '肃州区', '3', '620902', '620900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621024000000000', '合水县', '合水县', '合水县', '3', '621024', '621000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621025000000000', '正宁县', '正宁县', '正宁县', '3', '621025', '621000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621026000000000', '宁县', '宁县', '宁县', '3', '621026', '621000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620982000000000', '敦煌市', '敦煌市', '敦煌市', '3', '620982', '620900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621002000000000', '西峰区', '西峰区', '西峰区', '3', '621002', '621000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621021000000000', '庆城县', '庆城县', '庆城县', '3', '621021', '621000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621022000000000', '环县', '环县', '环县', '3', '621022', '621000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621023000000000', '华池县', '华池县', '华池县', '3', '621023', '621000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621122000000000', '陇西县', '陇西县', '陇西县', '3', '621122', '621100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621027000000000', '镇原县', '镇原县', '镇原县', '3', '621027', '621000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621102000000000', '安定区', '安定区', '安定区', '3', '621102', '621100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621121000000000', '通渭县', '通渭县', '通渭县', '3', '621121', '621100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621126000000000', '岷县', '岷县', '岷县', '3', '621126', '621100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621123000000000', '渭源县', '渭源县', '渭源县', '3', '621123', '621100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621124000000000', '临洮县', '临洮县', '临洮县', '3', '621124', '621100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621125000000000', '漳县', '漳县', '漳县', '3', '621125', '621100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621221000000000', '成县', '成县', '成县', '3', '621221', '621200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621222000000000', '文县', '文县', '文县', '3', '621222', '621200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621201000000000', '武都区', '武都区', '武都区', '3', '621201', '621200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621225000000000', '西和县', '西和县', '西和县', '3', '621225', '621200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621223000000000', '宕昌县', '宕昌县', '宕昌县', '3', '621223', '621200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621224000000000', '康县', '康县', '康县', '3', '621224', '621200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621228000000000', '两当县', '两当县', '两当县', '3', '621228', '621200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621226000000000', '礼县', '礼县', '礼县', '3', '621226', '621200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621227000000000', '徽县', '徽县', '徽县', '3', '621227', '621200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('622926000000000', '东乡县', '东乡县', '东乡县', '3', '622926', '622900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('630105000000000', '城北区', '城北区', '城北区', '3', '630105', '630100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('622927000000000', '积石山县', '积石山县', '积石山县', '3', '622927', '622900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('630121000000000', '大通县', '大通县', '大通县', '3', '630121', '630100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('622901000000000', '临夏市', '临夏市', '临夏市', '3', '622901', '622900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('622921000000000', '临夏县', '临夏县', '临夏县', '3', '622921', '622900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('622922000000000', '康乐县', '康乐县', '康乐县', '3', '622922', '622900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('622923000000000', '永靖县', '永靖县', '永靖县', '3', '622923', '622900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('622924000000000', '广河县', '广河县', '广河县', '3', '622924', '622900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('622925000000000', '和政县', '和政县', '和政县', '3', '622925', '622900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('630103000000000', '城中区', '城中区', '城中区', '3', '630103', '630100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('630104000000000', '城西区', '城西区', '城西区', '3', '630104', '630100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('623001000000000', '合作市', '合作市', '合作市', '3', '623001', '623000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('623021000000000', '临潭县', '临潭县', '临潭县', '3', '623021', '623000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('623022000000000', '卓尼县', '卓尼县', '卓尼县', '3', '623022', '623000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('623023000000000', '舟曲县', '舟曲县', '舟曲县', '3', '623023', '623000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('623024000000000', '迭部县', '迭部县', '迭部县', '3', '623024', '623000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('623025000000000', '玛曲县', '玛曲县', '玛曲县', '3', '623025', '623000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('623026000000000', '碌曲县', '碌曲县', '碌曲县', '3', '623026', '623000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('623027000000000', '夏河县', '夏河县', '夏河县', '3', '623027', '623000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('630102000000000', '城东区', '城东区', '城东区', '3', '630102', '630100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632122000000000', '民和县', '民和县', '民和县', '3', '632122', '632100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('630122000000000', '湟中县', '湟中县', '湟中县', '3', '630122', '630100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('630123000000000', '湟源县', '湟源县', '湟源县', '3', '630123', '630100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632121000000000', '平安县', '平安县', '平安县', '3', '632121', '632100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632128000000000', '循化县', '循化县', '循化县', '3', '632128', '632100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632123000000000', '乐都县', '乐都县', '乐都县', '3', '632123', '632100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632126000000000', '互助县', '互助县', '互助县', '3', '632126', '632100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632127000000000', '化仑', '化仑', '化仑', '3', '632127', '632100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654024000000000', '巩留县', '巩留县', '巩留县', '3', '654024', '654000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632621000000000', '玛沁县', '玛沁县', '玛沁县', '3', '632621', '632600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640121000000000', '永宁县', '永宁县', '永宁县', '3', '640121', '640100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652822000000000', '轮台县', '轮台县', '轮台县', '3', '652822', '652800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653129000000000', '伽师县', '伽师县', '伽师县', '3', '653129', '653100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632827000000000', '冷湖行委', '冷湖行委', '冷湖行委', '3', '632827', '632800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632828000000000', '青海油田', '青海油田', '青海油田', '3', '632828', '632800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640323000000000', '盐池县', '盐池县', '盐池县', '3', '640323', '640300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654224000000000', '托里县', '托里县', '托里县', '3', '654224', '654200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654225000000000', '裕民县', '裕民县', '裕民县', '3', '654225', '654200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654226000000000', '和布克赛尔蒙古自治县', '和布克赛尔蒙古自治县', '和布克赛尔蒙古自治县', '3', '654226', '654200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640122000000000', '贺兰县', '贺兰县', '贺兰县', '3', '640122', '640100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653001000000000', '阿图什市', '阿图什市', '阿图什市', '3', '653001', '653000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654027000000000', '特克斯县', '特克斯县', '特克斯县', '3', '654027', '654000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632522000000000', '同德县', '同德县', '同德县', '3', '632522', '632500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632622000000000', '班玛县', '班玛县', '班玛县', '3', '632622', '632600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640502000000000', '沙坡头区', '沙坡头区', '沙坡头区', '3', '640502', '640500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640382000000000', '红寺堡开发区', '红寺堡开发区', '红寺堡开发区', '3', '640382', '640300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653126000000000', '叶城县', '叶城县', '叶城县', '3', '653126', '653100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632723000000000', '称多县', '称多县', '称多县', '3', '632723', '632700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652301000000000', '昌吉市', '昌吉市', '昌吉市', '3', '652301', '652300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653101000000000', '喀什市', '喀什市', '喀什市', '3', '653101', '653100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654021000000000', '伊宁县', '伊宁县', '伊宁县', '3', '654021', '654000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632823000000000', '天峻县', '天峻县', '天峻县', '3', '632823', '632800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650104000000000', '新市区', '新市区', '新市区', '3', '650104', '650100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653224000000000', '洛浦县', '洛浦县', '洛浦县', '3', '653224', '653200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632623000000000', '甘德县', '甘德县', '甘德县', '3', '632623', '632600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640106000000000', '金凤区', '金凤区', '金凤区', '3', '640106', '640100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650202000000000', '独山子区', '独山子区', '独山子区', '3', '650202', '650200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650203000000000', '克拉玛依区', '克拉玛依区', '克拉玛依区', '3', '650203', '650200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652923000000000', '库车县', '库车县', '库车县', '3', '652923', '652900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640205000000000', '惠农区', '惠农区', '惠农区', '3', '640205', '640200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654202000000000', '乌苏市', '乌苏市', '乌苏市', '3', '654202', '654200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653123000000000', '英吉沙县', '英吉沙县', '英吉沙县', '3', '653123', '653100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640381000000000', '青铜峡市', '青铜峡市', '青铜峡市', '3', '640381', '640300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652701000000000', '博乐市', '博乐市', '博乐市', '3', '652701', '652700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650204000000000', '白碱滩区', '白碱滩区', '白碱滩区', '3', '650204', '650200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650205000000000', '乌尔禾区', '乌尔禾区', '乌尔禾区', '3', '650205', '650200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652101000000000', '吐鲁番市', '吐鲁番市', '吐鲁番市', '3', '652101', '652100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652929000000000', '柯坪县', '柯坪县', '柯坪县', '3', '652929', '652900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640422000000000', '西吉县', '西吉县', '西吉县', '3', '640422', '640400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652924000000000', '沙雅县', '沙雅县', '沙雅县', '3', '652924', '652900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632323000000000', '泽库县', '泽库县', '泽库县', '3', '632323', '632300000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652827000000000', '和静县', '和静县', '和静县', '3', '652827', '652800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652922000000000', '温宿县', '温宿县', '温宿县', '3', '652922', '652900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661401000000000', '皮山农场', '皮山农场', '皮山农场', '3', '661401', '661400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661402000000000', '四十七团', '四十七团', '四十七团', '3', '661402', '661400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653023000000000', '阿合奇县', '阿合奇县', '阿合奇县', '3', '653023', '653000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653024000000000', '乌秦', '乌秦', '乌秦', '3', '653024', '653000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654003000000000', '奎屯市', '奎屯市', '奎屯市', '3', '654003', '654000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632822000000000', '都兰县', '都兰县', '都兰县', '3', '632822', '632800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652926000000000', '拜城县', '拜城县', '拜城县', '3', '652926', '652900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653130000000000', '巴楚县', '巴楚县', '巴楚县', '3', '653130', '653100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632524000000000', '兴海县', '兴海县', '兴海县', '3', '632524', '632500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650107000000000', '达板城区', '达板城区', '达板城区', '3', '650107', '650100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650108000000000', '东山区', '东山区', '东山区', '3', '650108', '650100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650121000000000', '乌鲁木齐县', '乌鲁木齐县', '乌鲁木齐县', '3', '650121', '650100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652928000000000', '阿瓦提县', '阿瓦提县', '阿瓦提县', '3', '652928', '652900000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653221000000000', '和田县', '和田县', '和田县', '3', '653221', '653200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661403000000000', '一牧场', '一牧场', '一牧场', '3', '661403', '661400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661404000000000', '二二四团', '二二四团', '二二四团', '3', '661404', '661400000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('662003000000000', '石河子大学', '石河子大学', '石河子大学', '3', '662003', '662000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('662004000000000', '农科院', '农科院', '农科院', '3', '662004', '662000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('662005000000000', '塔里木农大', '塔里木农大', '塔里木农大', '3', '662005', '662000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('662008000000000', '二二二团', '二二二团', '二二二团', '3', '662008', '662000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('662030000000000', '直辖单位', '直辖单位', '直辖单位', '3', '662030', '662000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632724000000000', '治多县', '治多县', '治多县', '3', '632724', '632700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632725000000000', '囊谦县', '囊谦县', '囊谦县', '3', '632725', '632700000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652222000000000', '巴里坤哈萨克自治县', '巴里坤哈萨克自治县', '巴里坤哈萨克自治县', '3', '652222', '652200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652223000000000', '伊吾县', '伊吾县', '伊吾县', '3', '652223', '652200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653128000000000', '岳普湖县', '岳普湖县', '岳普湖县', '3', '653128', '653100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654201000000000', '塔城市', '塔城市', '塔城市', '3', '654201', '654200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640302000000000', '利通区', '利通区', '利通区', '3', '640302', '640300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653122000000000', '疏勒县', '疏勒县', '疏勒县', '3', '653122', '653100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654023000000000', '霍城县', '霍城县', '霍城县', '3', '654023', '654000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632525000000000', '贵南县', '贵南县', '贵南县', '3', '632525', '632500000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640105000000000', '西夏区', '西夏区', '西夏区', '3', '640105', '640100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652328000000000', '木垒县', '木垒县', '木垒县', '3', '652328', '652300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653226000000000', '于田县', '于田县', '于田县', '3', '653226', '653200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640202000000000', '大武口区', '大武口区', '大武口区', '3', '640202', '640200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652801000000000', '库尔勒市', '库尔勒市', '库尔勒市', '3', '652801', '652800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632324000000000', '河南县', '河南县', '河南县', '3', '632324', '632300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652325000000000', '奇台县', '奇台县', '奇台县', '3', '652325', '652300000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654223000000000', '沙湾县', '沙湾县', '沙湾县', '3', '654223', '654200000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653125000000000', '莎车县', '莎车县', '莎车县', '3', '653125', '653100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653131000000000', '塔什库尔干县', '塔什库尔干县', '塔什库尔干县', '3', '653131', '653100000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654026000000000', '昭苏县', '昭苏县', '昭苏县', '3', '654026', '654000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660606000000000', '一零七团', '一零七团', '一零七团', '3', '660606', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660607000000000', '一零八团', '一零八团', '一零八团', '3', '660607', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660608000000000', '一零九团', '一零九团', '一零九团', '3', '660608', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660609000000000', '一一零团', '一一零团', '一一零团', '3', '660609', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660610000000000', '一一一团', '一一一团', '一一一团', '3', '660610', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660611000000000', '芳草湖农场', '芳草湖农场', '芳草湖农场', '3', '660611', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660612000000000', '新湖农场', '新湖农场', '新湖农场', '3', '660612', '660600000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632821000000000', '乌兰县', '乌兰县', '乌兰县', '3', '632821', '632800000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510900000000000', '遂宁市', '遂宁市', '遂宁市', '2', '5109', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511600000000000', '广安市', '广安市', '广安市', '2', '5116', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511700000000000', '达州市', '达州市', '达州市', '2', '5117', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511300000000000', '南充市', '南充市', '南充市', '2', '5113', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511100000000000', '乐山市', '乐山市', '乐山市', '2', '5111', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511400000000000', '眉山市', '眉山市', '眉山市', '2', '5114', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511500000000000', '宜宾市', '宜宾市', '宜宾市', '2', '5115', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511800000000000', '雅安市', '雅安市', '雅安市', '2', '5118', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('511900000000000', '巴中市', '巴中市', '巴中市', '2', '5119', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('512000000000000', '资阳市', '资阳市', '资阳市', '2', '5120', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513200000000000', '阿坝藏族羌族自治州', '阿坝藏族羌族自治州', '阿坝藏族羌族自治州', '2', '5132', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513300000000000', '甘孜藏族自治州', '甘孜藏族自治州', '甘孜藏族自治州', '2', '5133', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('513400000000000', '凉山彝族自治州', '凉山彝族自治州', '凉山彝族自治州', '2', '5134', '510000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520100000000000', '贵阳市', '贵阳市', '贵阳市', '2', '5201', '520000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520200000000000', '六盘水市', '六盘水市', '六盘水市', '2', '5202', '520000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520300000000000', '遵义市', '遵义市', '遵义市', '2', '5203', '520000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520400000000000', '安顺市', '安顺市', '安顺市', '2', '5204', '520000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522200000000000', '铜仁地区', '铜仁地区', '铜仁地区', '2', '5222', '520000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522300000000000', '黔西南布依族苗族自治州', '黔西南布依族苗族自治州', '黔西南布依族苗族自治州', '2', '5223', '520000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522400000000000', '毕节地区', '毕节地区', '毕节地区', '2', '5224', '520000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522600000000000', '黔东南苗族侗族自治州', '黔东南苗族侗族自治州', '黔东南苗族侗族自治州', '2', '5226', '520000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('522700000000000', '黔南布依族苗族自治州', '黔南布依族苗族自治州', '黔南布依族苗族自治州', '2', '5227', '520000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530100000000000', '昆明市', '昆明市', '昆明市', '2', '5301', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530300000000000', '曲靖市', '曲靖市', '曲靖市', '2', '5303', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530400000000000', '玉溪市', '玉溪市', '玉溪市', '2', '5304', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530500000000000', '保山市', '保山市', '保山市', '2', '5305', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530700000000000', '丽江市', '丽江市', '丽江市', '2', '5307', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530600000000000', '昭通市', '昭通市', '昭通市', '2', '5306', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530800000000000', '思茅市', '思茅市', '思茅市', '2', '5308', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530900000000000', '临沧市', '临沧市', '临沧市', '2', '5309', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532300000000000', '楚雄彝族自治州', '楚雄彝族自治州', '楚雄彝族自治州', '2', '5323', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532500000000000', '红河哈尼族彝族自治州', '红河哈尼族彝族自治州', '红河哈尼族彝族自治州', '2', '5325', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532800000000000', '西双版纳傣族自治州', '西双版纳傣族自治州', '西双版纳傣族自治州', '2', '5328', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532600000000000', '文山壮族苗族自治州', '文山壮族苗族自治州', '文山壮族苗族自治州', '2', '5326', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('532900000000000', '大理白族自治州', '大理白族自治州', '大理白族自治州', '2', '5329', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533300000000000', '怒江傈僳族自治州', '怒江傈僳族自治州', '怒江傈僳族自治州', '2', '5333', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533100000000000', '德宏傣族景颇族自治州', '德宏傣族景颇族自治州', '德宏傣族景颇族自治州', '2', '5331', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('533400000000000', '迪庆藏族自治州', '迪庆藏族自治州', '迪庆藏族自治州', '2', '5334', '530000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('540100000000000', '拉萨市', '拉萨市', '拉萨市', '2', '5401', '540000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542100000000000', '昌都地区', '昌都地区', '昌都地区', '2', '5421', '540000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542300000000000', '日喀则地区', '日喀则地区', '日喀则地区', '2', '5423', '540000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542200000000000', '山南地区', '山南地区', '山南地区', '2', '5422', '540000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542400000000000', '那曲地区', '那曲地区', '那曲地区', '2', '5424', '540000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610600000000000', '延安市', '延安市', '延安市', '2', '6106', '610000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542500000000000', '阿里地区', '阿里地区', '阿里地区', '2', '5425', '540000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610100000000000', '西安市', '西安市', '西安市', '2', '6101', '610000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('542600000000000', '林芝地区', '林芝地区', '林芝地区', '2', '5426', '540000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610200000000000', '铜川市', '铜川市', '铜川市', '2', '6102', '610000000000000', 'Y', 'Y', null);
commit;

insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610300000000000', '宝鸡市', '宝鸡市', '宝鸡市', '2', '6103', '610000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610400000000000', '咸阳市', '咸阳市', '咸阳市', '2', '6104', '610000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610500000000000', '渭南市', '渭南市', '渭南市', '2', '6105', '610000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620500000000000', '天水市', '天水市', '天水市', '2', '6205', '620000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610700000000000', '汉中市', '汉中市', '汉中市', '2', '6107', '610000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610800000000000', '榆林市', '榆林市', '榆林市', '2', '6108', '610000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610900000000000', '安康市', '安康市', '安康市', '2', '6109', '610000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('611000000000000', '商洛市', '商洛市', '商洛市', '2', '6110', '610000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('611100000000000', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '2', '6111', '610000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620100000000000', '兰州市', '兰州市', '兰州市', '2', '6201', '620000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620200000000000', '嘉峪关市', '嘉峪关市', '嘉峪关市', '2', '6202', '620000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620300000000000', '金昌市', '金昌市', '金昌市', '2', '6203', '620000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620400000000000', '白银市', '白银市', '白银市', '2', '6204', '620000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632700000000000', '玉树州', '玉树州', '玉树州', '2', '6327', '630000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632200000000000', '海北州', '海北州', '海北州', '2', '6322', '630000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632300000000000', '黄南州', '黄南州', '黄南州', '2', '6323', '630000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620600000000000', '武威市', '武威市', '武威市', '2', '6206', '620000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620700000000000', '张掖市', '张掖市', '张掖市', '2', '6207', '620000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620800000000000', '平凉市', '平凉市', '平凉市', '2', '6208', '620000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620900000000000', '酒泉市', '酒泉市', '酒泉市', '2', '6209', '620000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621000000000000', '庆阳市', '庆阳市', '庆阳市', '2', '6210', '620000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621100000000000', '定西市', '定西市', '定西市', '2', '6211', '620000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('621200000000000', '陇南市', '陇南市', '陇南市', '2', '6212', '620000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('622900000000000', '临夏回族自治州', '临夏回族自治州', '临夏回族自治州', '2', '6229', '620000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('623000000000000', '甘南藏族自治州', '甘南藏族自治州', '甘南藏族自治州', '2', '6230', '620000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('630100000000000', '西宁市', '西宁市', '西宁市', '2', '6301', '630000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632100000000000', '海东地区', '海东地区', '海东地区', '2', '6321', '630000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632600000000000', '果洛州', '果洛州', '果洛州', '2', '6326', '630000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653000000000000', '克州', '克州', '克州', '2', '6530', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640500000000000', '中卫市', '中卫市', '中卫市', '2', '6405', '640000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652300000000000', '昌吉州', '昌吉州', '昌吉州', '2', '6523', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653100000000000', '喀什地区', '喀什地区', '喀什地区', '2', '6531', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650200000000000', '克拉玛依市', '克拉玛依市', '克拉玛依市', '2', '6502', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652700000000000', '博尔塔拉蒙古自治州', '博尔塔拉蒙古自治州', '博尔塔拉蒙古自治州', '2', '6527', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652100000000000', '吐鲁番地区', '吐鲁番地区', '吐鲁番地区', '2', '6521', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661400000000000', '农十四师', '农十四师', '农十四师', '2', '6614', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('662000000000000', '市直辖乡（虚拟编码）', '市直辖乡（虚拟编码）', '市直辖乡（虚拟编码）', '2', '6620', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654200000000000', '塔城地区', '塔城地区', '塔城地区', '2', '6542', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640300000000000', '吴忠市', '吴忠市', '吴忠市', '2', '6403', '640000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640200000000000', '石嘴山市', '石嘴山市', '石嘴山市', '2', '6402', '640000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652800000000000', '巴州', '巴州', '巴州', '2', '6528', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('653200000000000', '和田地区', '和田地区', '和田地区', '2', '6532', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660700000000000', '农七师', '农七师', '农七师', '2', '6607', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660800000000000', '农八师', '农八师', '农八师', '2', '6608', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660900000000000', '农九师', '农九师', '农九师', '2', '6609', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661000000000000', '农十师', '农十师', '农十师', '2', '6610', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661100000000000', '建工师', '建工师', '建工师', '2', '6611', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661200000000000', '农十二师', '农十二师', '农十二师', '2', '6612', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('661300000000000', '农十三师', '农十三师', '农十三师', '2', '6613', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652200000000000', '哈密地区', '哈密地区', '哈密地区', '2', '6522', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('659000000000000', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '省直辖区（虚拟编码）', '2', '6590', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660100000000000', '农一师', '农一师', '农一师', '2', '6601', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632800000000000', '海西州', '海西州', '海西州', '2', '6328', '630000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660200000000000', '农二师', '农二师', '农二师', '2', '6602', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660300000000000', '农三师', '农三师', '农三师', '2', '6603', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660400000000000', '农四师', '农四师', '农四师', '2', '6604', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660500000000000', '农五师', '农五师', '农五师', '2', '6605', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660600000000000', '农六师', '农六师', '农六师', '2', '6606', '660000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654000000000000', '伊犁州', '伊犁州', '伊犁州', '2', '6540', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640100000000000', '银川市', '银川市', '银川市', '2', '6401', '640000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('654300000000000', '阿勒泰地区', '阿勒泰地区', '阿勒泰地区', '2', '6543', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650100000000000', '乌鲁木齐市', '乌鲁木齐市', '乌鲁木齐市', '2', '6501', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('632500000000000', '海南州', '海南州', '海南州', '2', '6325', '630000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('652900000000000', '阿克苏地区', '阿克苏地区', '阿克苏地区', '2', '6529', '650000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640400000000000', '固原市', '固原市', '固原市', '2', '6404', '640000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('110000000000000', '北京市', '北京市', '北京市', '1', '11', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('120000000000000', '天津市', '天津市', '天津市', '1', '12', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('130000000000000', '河北省', '河北省', '河北省', '1', '13', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('140000000000000', '山西省', '山西省', '山西省', '1', '14', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('150000000000000', '内蒙古自治区', '内蒙古自治区', '内蒙古自治区', '1', '15', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('210000000000000', '辽宁省', '辽宁省', '辽宁省', '1', '21', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('220000000000000', '吉林省', '吉林省', '吉林省', '1', '22', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('230000000000000', '黑龙江省', '黑龙江省', '黑龙江省', '1', '23', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('310000000000000', '上海市', '上海市', '上海市', '1', '31', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('320000000000000', '江苏省', '江苏省', '江苏省', '1', '32', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('330000000000000', '浙江省', '浙江省', '浙江省', '1', '33', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('340000000000000', '安徽省', '安徽省', '安徽省', '1', '34', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('350000000000000', '福建省', '福建省', '福建省', '1', '35', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('360000000000000', '江西省', '江西省', '江西省', '1', '36', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('370000000000000', '山东省', '山东省', '山东省', '1', '37', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('410000000000000', '河南省', '河南省', '河南省', '1', '41', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('420000000000000', '湖北省', '湖北省', '湖北省', '1', '42', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('430000000000000', '湖南省', '湖南省', '湖南省', '1', '43', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('440000000000000', '广东省', '广东省', '广东省', '1', '44', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('450000000000000', '广西壮族自治区', '广西壮族自治区', '广西壮族自治区', '1', '45', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('460000000000000', '海南省', '海南省', '海南省', '1', '46', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('500000000000000', '重庆市', '重庆市', '重庆市', '1', '50', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('510000000000000', '四川省', '四川省', '四川省', '1', '51', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('520000000000000', '贵州省', '贵州省', '贵州省', '1', '52', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('530000000000000', '云南省', '云南省', '云南省', '1', '53', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('540000000000000', '西藏自治区', '西藏自治区', '西藏自治区', '1', '54', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('610000000000000', '陕西省', '陕西省', '陕西省', '1', '61', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('620000000000000', '甘肃省', '甘肃省', '甘肃省', '1', '62', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('630000000000000', '青海省', '青海省', '青海省', '1', '63', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('660000000000000', '新疆兵团', '新疆兵团', '新疆兵团', '1', '66', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('640000000000000', '宁夏回族自治区', '宁夏回族自治区', '宁夏回族自治区', '1', '64', '000000000000000', 'Y', 'Y', null);
insert into DM_XZQH (XZQH_DM, XZQH_MC, XZQH_JC, XZQH_QC, JCDM, JBDM, SJ_XZQH_DM, XYBZ, YXBZ, DWLSGX_DM)
values ('650000000000000', '新疆维吾尔自治区', '新疆维吾尔自治区', '新疆维吾尔自治区', '1', '65', '000000000000000', 'Y', 'Y', null);
commit;