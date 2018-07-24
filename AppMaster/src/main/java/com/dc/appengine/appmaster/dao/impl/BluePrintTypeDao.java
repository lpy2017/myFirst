package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.dc.appengine.appmaster.entity.BluePrintType;

@Component("bluePrintTypeDao")
public class BluePrintTypeDao{
	
	@Resource
	SqlSessionTemplate sqlSessionTemplate;

	public int getFlowIdByBlueId(Map<String, Object> m) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("blueprinttype.getFlowIdByBlueInstanceId", m);
	}

	public long getUserIdByBlueInstanceId(String blueInstanceId) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("blueprinttype.getUserId", blueInstanceId);
	}
	
	public String getSubFlowInfo(long flowId){
		return sqlSessionTemplate.selectOne("blueprinttype.getSubFlowInfo", flowId);
	}
	
	public Map<String,Object> getBlueprintTypeByFlowId(long flowId){
		return sqlSessionTemplate.selectOne("blueprint.getBlueprintTypeByFlowId", flowId);
	}
	
	public Map<String,Object> getBlueprintTypeById(String id){
		return sqlSessionTemplate.selectOne("blueprint.getBlueprintTypeById", id);
	}
	
	public long getEmptyFlow(){
		Long l = sqlSessionTemplate.selectOne("blueprinttype.getEmptyFlow");
		return l==null?0:l;
	}
	
	public void delBluePrintTypeByFlowId(Long flow_id) {
		sqlSessionTemplate.delete("blueprinttype.delBluePrintTypeByFlowId",flow_id);
	}
	
	public List<Map<String,Object>> listBpInstanceFlows(String blueprintInstanceId){
		return sqlSessionTemplate.selectList("blueprinttype.listBpInstanceFlows", blueprintInstanceId);
	}
	
	public boolean updateBluePrintTypeByFlowId(Map<String, Object> params){
		int result = sqlSessionTemplate.update("blueprinttype.updateBluePrintTypeByFlowId", params);
		if(result > 0){
			return true;
		}
		return false;
	}
	
	public String getFlowNameByFlowId(long flowId){
		return sqlSessionTemplate.selectOne("blueprinttype.getFlowNameByFlowId", flowId);
	}
	
	public List getFlowIdByBlueInstanceId(String blueprintId) {
		return sqlSessionTemplate.selectList("blueprinttype.getFlowIdByBlueInstanceId2", blueprintId);
	}
	
	public List<Map<String, Object>> getFlowsByBlueInstanceId(String blueprintInstanceId) {
		return sqlSessionTemplate.selectList("blueprinttype.getFlowsByBlueInstanceId", blueprintInstanceId);
	}

	public List<Map<String, Object>> getSecondFlowsInstanceList(Map<String, Object> paraMap) {
		return sqlSessionTemplate.selectList("blueprinttype.getSecondFlowsInstanceList",paraMap);
	}

	public List<Map<String, String>> getAllBlueprintFlow() {
		return sqlSessionTemplate.selectList("blueprinttype.getAllBlueprintFlow");
	}
}
