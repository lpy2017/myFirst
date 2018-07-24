package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.dc.appengine.appmaster.entity.BluePrintType;
import com.dc.appengine.appmaster.entity.Page;

public interface IBlueprintTemplateDao {

	Page listBlueprintTemplates(Map<String,Object> condition,int pageNum,int pageSize);
	int saveBlueprintTemplate(Map<String, String> param);
	int updateBlueprintTemplate(Map<String, String> param);
	void delBlueprintTemplate(String blueprint_id);
	List<Map<String,Object>> listBlueprintFlows(String blueprintInstanceId);
	int saveBlueprintTemplateFlow(BluePrintType bluePrintType);
	List<Map<String,Object>> getBlueprintTemplate(Map<String, String> param);
	int updateBlueprintTemplateFlow(Map<String, Object> param);
	void delBlueprintTemplateFlow(String flowId);
	Map<String, Object> getBlueprintTemplateFlowByFlowId(Long flowId);
	List<Map<String, Object>> getBlueprintTemplateFlows(String blueprint_id);
	Map<String, Object> getBlueprintTemplateFlowByCdFlowId(String cdFlowId);
	Map<String, Object> getBlueprintTemplateFlowByCdFlowId(String cdFlowId,Map<String, String> map);
	int updateBlueprintTemplateFlowByCdFlowId(Map<String, Object> param);
	void delBlueprintTemplateFlowByCdFlowId(String cdFlowId);
	List<Map<String,Object>> exportBlueprint(List tempIds);
	void delBlueprintTemplate4Import(String ID);
	int saveBlueprintTemplate4Import(Map<String, String> param);
	int countBlueprintInsBytemplateId(String templateId);
	Map<String, Object> getFlowByFlowName(Map<String, Object> param);
	void delBlueprintTemplateFlowByCdFlowId(String cdFlowId, Map<String, String> params);
	List<Map<String, Object>> listBlueprintTemplateByNameAndApp(Map<String, Object> params);
}