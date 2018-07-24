package com.dc.appengine.appmaster.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.dao.IBlueprintTemplateDao;
import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.BluePrintType;
import com.dc.appengine.appmaster.entity.Page;

@Component("blueprintTemplateDao")
public class BlueprintTemplateDao extends PageQuery implements IBlueprintTemplateDao{

	@Override
	public Page listBlueprintTemplates(Map<String, Object> condition, int pageNum, int pageSize) {
		return pageQuery(condition, pageNum, pageSize, "blueprint_template.listBlueprintTemplates");
	}
	
	@Override
	public List<Map<String, Object>> getBlueprintTemplate(Map<String, String> param) {
		return sqlSessionTemplate.selectList("blueprint_template.getBlueprintTemplate",param);
	}
	
	@Override
	public int saveBlueprintTemplate(Map<String, String> param) {
		return sqlSessionTemplate.insert("blueprint_template.saveBlueprintTemplate",param);
	}
	
	@Override
	public int updateBlueprintTemplate(Map<String, String> param) {
		return sqlSessionTemplate.update("blueprint_template.updateBlueprintTemplate",param);
	}

	@Override
	public void delBlueprintTemplate(String blueprint_id) {
		sqlSessionTemplate.delete("blueprint_template.delBlueprintTemplate",blueprint_id);
	}
	
	@Override
	public List<Map<String, Object>> listBlueprintFlows(String blueprintInstanceId){
		return sqlSessionTemplate.selectList("blueprint_template.listBpFlows", blueprintInstanceId);
	}

	@Override
	public int saveBlueprintTemplateFlow(BluePrintType bluePrintType) {
		if(null == bluePrintType.getId()){
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			bluePrintType.setId(format.format(new Date()));
		}
		return sqlSessionTemplate.insert("blueprint_template.saveBlueprintTemplateFlow",bluePrintType);
	}

	@Override
	public int updateBlueprintTemplateFlow(Map<String, Object> param) {
		return sqlSessionTemplate.update("blueprint_template.updateBlueprintTemplateFlow",param);
	}

	@Override
	public void delBlueprintTemplateFlow(String flowId) {
		sqlSessionTemplate.delete("blueprint_template.delBlueprintTemplateFlow",flowId);
	}

	@Override
	public Map<String, Object> getBlueprintTemplateFlowByFlowId(Long flowId) {
		return sqlSessionTemplate.selectOne("blueprint_template.getBlueprintTemplateFlowByFlowId", flowId);
	}
	
	@Override
	public List<Map<String, Object>> getBlueprintTemplateFlows(String blueprint_id) {
		return sqlSessionTemplate.selectList("blueprint_template.getBlueprintTemplateFlows",blueprint_id);
	}
	
	@Override
	public Map<String, Object> getBlueprintTemplateFlowByCdFlowId(String cdFlowId) {
		return sqlSessionTemplate.selectOne("blueprint_template.getBlueprintTemplateFlowByCdFlowId", cdFlowId);
	}
	
	@Override
	public Map<String, Object> getBlueprintTemplateFlowByCdFlowId(String cdFlowId,Map<String,String> map) {
		return sqlSessionTemplate.selectOne("blueprint_template.getBlueprintTemplateFlowBymap", map);
	}
	
	@Override
	public int updateBlueprintTemplateFlowByCdFlowId(Map<String, Object> param) {
		return sqlSessionTemplate.update("blueprint_template.updateBlueprintTemplateFlowByCdFlowId",param);
	}
	
	@Override
	public void delBlueprintTemplateFlowByCdFlowId(String cdFlowId) {
		sqlSessionTemplate.delete("blueprint_template.delBlueprintTemplateFlowByCdFlowId",cdFlowId);
	}
	@Override
	public void delBlueprintTemplateFlowByCdFlowId(String cdFlowId, Map<String, String> params) {
		sqlSessionTemplate.delete("blueprint_template.delBlueprintTemplateFlowBymap",params);
	}

	@Override
	public List<Map<String,Object>> exportBlueprint(List tempIds) {
		return sqlSessionTemplate.selectList("blueprint_template.exportBlueprint",tempIds);
	}

	@Override
	public void delBlueprintTemplate4Import(String ID) {
		sqlSessionTemplate.delete("blueprint_template.delBlueprintTemplate4Import",ID);		
	}

	@Override
	public int saveBlueprintTemplate4Import(Map<String, String> param) {
		return sqlSessionTemplate.insert("blueprint_template.saveBlueprintTemplate4Import",param);
	}

	@Override
	public int countBlueprintInsBytemplateId(String templateId) {
		return sqlSessionTemplate.selectOne("blueprint_template.countBlueprintInsBytemplateId",templateId);
	}
	
	@Override
	public Map<String, Object> getFlowByFlowName(Map<String, Object> param) {
		return sqlSessionTemplate.selectOne("blueprint_template.getFlowByFlowName", param);
	}

	@Override
	public List<Map<String, Object>> listBlueprintTemplateByNameAndApp(Map<String, Object> params) {
		return sqlSessionTemplate.selectList("blueprint_template.listBlueprintTemplateByNameAndApp", params);
	}

}
