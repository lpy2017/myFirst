package com.dc.appengine.appmaster.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dc.appengine.appmaster.entity.Page;

public interface IBlueprintTemplateService {

	Page listBlueprintTemplates(Map<String,Object> condition,int pageNum,int pageSize);
	public int saveBlueprintTemplate(Map<String, String> param,String op);
	public String delBlueprintTemplate(String blueprint_id);
	public boolean checkBlueprintFlowUnique(String bpInstanceId, String flowName);
	public String addBlueprintTemplateFlow(String blueprintId, String flowName, String flowInfo,String flowInfoGroup,String appName);
	public String listBlueprintTemplateFlow(String blueprintId,String sortName,String sortOrder);
	String updateBlueprintTemplateFlow(String flowInfoGroup, String flowInfo,String flowId);
	public boolean delBlueprintTemplateFlow(String flowId);
	Map<String, Object> getBlueprintTemplateFlowInfo(String flowId);
	String exportBlueprint(String blueprintIds);
	String importBlueprint(String template, String blueprintFlow,String resource,String flow2, String vrsion, String ftl, String packages,String zip,String userId)throws Exception;
	List<Map<String, Object>> getBlueprintTemplate(Map<String, String> param);
	Map<String, Object> getFlowByFlowName(Map<String, Object> param);
	boolean delBlueprintTemplateFlow(String cdFlowId, String blueprintId, String flowName);
	List<Map<String, Object>> listBlueprintTemplateByNameAndApp(Map<String, Object> params);
	public void delBlueprintTemplate4Import(String ID);
	public int saveBlueprintTemplate4Import(Map<String, String> param);
	public List<Map<String,Object>> exportBlueprint(List tempIds);
	public List<Map<String, Object>> getBlueprintTemplateFlows(String blueprint_id);
	Set<String> getAppNameByBlueprintName(String blueprint_name);
}
