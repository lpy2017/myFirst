package com.dc.appengine.appmaster.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.constants.Constants;
import com.dc.appengine.appmaster.dao.impl.LabelsDAO;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.service.ILabelsService;
import com.dc.appengine.appmaster.utils.JudgeUtil;

@Service("labelsService")
public class LabelsService implements ILabelsService {
	private static Logger log = LoggerFactory.getLogger(LabelsService.class);
	private static Map<String, String> colunmNameMap=new HashMap<>();
	static{
		colunmNameMap.put("name", "name");
		colunmNameMap.put("type", "type");
		colunmNameMap.put("description", "description");
		colunmNameMap.put("updateTime", "updateTime");
	}
	@Autowired
	@Qualifier("labelsDAO")
	private LabelsDAO dao;
	
	
	@Override
	public String saveLabelType(JSONObject params) {
		// TODO Auto-generated method stub
		int i = dao.saveLabelType(params);
		JSONObject result = new JSONObject();
		 if(i>0){
			 result.put("result", true);
			 result.put("message", "新建标签类型成功！");
		 }else{
			 result.put("result", false);
			 result.put("message", "新建标签类型失败！");
		 }
		 return result.toJSONString();
	}

	@Override
	public String deleteLabelType(String id) {
		// TODO Auto-generated method stub
		int i = dao.deleteLabelType(id);
		JSONObject result = new JSONObject();
		 if(i>0){
			 result.put("result", true);
			 result.put("message", "删除标签类型成功！");
		 }else{
			 result.put("result", false);
			 result.put("message", "删除标签类型失败！");
		 }
		 return result.toJSONString();
	}

	@Override
	public String updateLabelType(JSONObject params) {
		// TODO Auto-generated method stub
		int i = dao.updateLabelType(params);
		JSONObject result = new JSONObject();
		 if(i>0){
			 result.put("result", true);
			 result.put("message", "更新标签类型成功！");
		 }else{
			 result.put("result", false);
			 result.put("message", "更新标签类型失败！");
		 }
		 return result.toJSONString();
	}

	@Override
	public String listLabelTypes(int pageSize, int pageNum, String typeName) {
		// TODO Auto-generated method stub
		JSONObject condition = new JSONObject();
		condition.put("name", typeName);
		Page page =dao.listLabelTypes(pageSize, pageNum, condition);
		return JSON.toJSONString(page);
	}

	@Override
	public String saveLabel(JSONObject params) {
		// TODO Auto-generated method stub
		String id=UUID.randomUUID().toString();
		params.put("id", id);
		int i = dao.saveLabel(params);
		JSONObject result = new JSONObject();
		 if(i>0){
			 result.put("result", true);
			 result.put("message", "新建标签成功！");
		 }else{
			 result.put("result", false);
			 result.put("message", "新建标签失败！");
		 }
		 return result.toJSONString();
	}

	@Override
	public String deleteLabel(String id) {
		// TODO Auto-generated method stub
		int delMap =dao.deleteLabelResMap(id);
		int delLabel = dao.deleteLabel(id);
		JSONObject result = new JSONObject();
		 if(delLabel>0){
			 result.put("result", true);
			 result.put("message", "删除标签成功！");
		 }else{
			 result.put("result", false);
			 result.put("message", "删除标签失败！");
		 }
		 return result.toJSONString();
	}

	@Override
	public String updateLabel(JSONObject params) {
		// TODO Auto-generated method stub
		int i = dao.updateLabel(params);
		JSONObject result = new JSONObject();
		 if(i>0){
			 result.put("result", true);
			 result.put("message", "更新标签成功！");
		 }else{
			 result.put("result", false);
			 result.put("message", "更新标签失败！");
		 }
		 return result.toJSONString();
	}

	@Override
	public String listLabels(int pageSize, int pageNum, String labelName,Integer labelCode,String sortName,String sortOrder) {
		// TODO Auto-generated method stub
		JSONObject condition = new JSONObject();
		condition.put("name", labelName);
		condition.put("labelCode", labelCode);
		condition.put("sortName", getColunmName(sortName));
		condition.put("sortOrder", sortOrder);
		Page page =dao.listLabels(pageSize, pageNum, condition);
		return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
	}

	@Override
	public String checkLabel(String labelName, String id,int code) {
		// TODO Auto-generated method stub
		JSONObject condition = new JSONObject();
		condition.put("name", labelName);
		condition.put("code", code);
		condition.put("id", id);
		int i =dao.checkLabel(condition);
		JSONObject result = new JSONObject();
		 if(i>0){
			 result.put("result", false);
			 result.put("message", "同类型下的标签名已存在！");
		 }else{
			 result.put("result", true);
			 result.put("message", "该标签名可使用！");
		 }
		 return result.toJSONString();
	}

	@Override
	public Boolean saveResourceLabelMap(String resourceId, String labels) {
		// TODO Auto-generated method stub
		JSONObject params= new JSONObject();
		params.put("resourceId", resourceId);
		params.put("labelIds", labels.split(";"));
		int i = dao.saveLabelResMap(params);
		if(i>0){
			return true;
		}
		return false;
	}
	
	@Override
	public String updateResourceLabel(JSONObject param) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		JSONObject params = new JSONObject();
		String resourceId = param.getString("resourceId");
		params.put("resourceId", resourceId);
		String labelIds = param.getString("labelIds");
		if(JudgeUtil.isEmpty(labelIds)){
			//删除所有标签
			dao.deleteLabelResMapByParams(params);
		}else{
			//统计需要删除的标签
			String[] newlist = labelIds.split(";");
			List<String> newArray = new ArrayList<>();
			for(int i=0;i<newlist.length;i++){
				newArray.add(newlist[i]);
			}
			//统计需要插入的标签
			List<String> insert=new ArrayList<>();
			List<String> delete=new ArrayList<>();
			List<Map> oldList = dao.findResourceLabels(param);
			if(oldList==null || oldList.size()==0){
				//新的全部插入
				params.put("labelIds", newArray);
				dao.saveLabelResMap(params);
			}else{
				for(Map one : oldList){
					String labelId = (String)one.get("labelId");
					delete.add(labelId);
					if(newArray.contains(labelId)){
						newArray.remove(labelId);
						delete.remove(labelId);
					}
				}
				if(newArray.size()>0){
					//insert
					params.put("labelIds", newArray);
					dao.saveLabelResMap(params);
				}
				if(delete.size()>0){
					params.put("labelIds", delete);
					//delete
					dao.deleteLabelResMapByParams(params);
				}
			}
		}
		result.put("result", true);
		result.put("message", "更新标签成功！");
		return result.toJSONString();
	}

	@Override
	public List getLabels4Group(Integer labelCode) {
		// TODO Auto-generated method stub
		List result = new ArrayList<>();
		JSONObject params = new JSONObject();
		params.put("labelCode", labelCode);
		List<Map> resourceLabels = dao.getLabels4Group(params);
		for(Map one:resourceLabels){
			if(one.get("RESOURCE_ID") !=null){
				Map<String, Object> label = new HashMap<>();
				label.put("type", one.get("ID"));
				label.put("name", one.get("NAME"));
				if(!result.contains(label)){
					result.add(label);
				}
			}
		}
		Map<String, Object> emptyLabel = new HashMap<>();
		emptyLabel.put("type", "none");
		emptyLabel.put("name", "无标签分组");
		result.add(emptyLabel);
		return result;
	}

	@Override
	public String getLabels4Plugin(String rows) {
		// TODO Auto-generated method stub
		JSONArray list = JSON.parseArray(rows);
		for(int j=0;j<list.size();j++){
			String labels="";
			String labelIds="";
			JSONObject plugin = list.getJSONObject(j);
			String pluginName = plugin.getString("pluginName");
			JSONObject param = new JSONObject();
			param.put("resourceId", pluginName);
			List<Map> labelList= dao.findResourceLabels(param);
			if(labels !=null && labelList.size()>0){
				for(int i=0;i<labelList.size();i++){
					Map label = (Map)labelList.get(i);
					if(i==labelList.size()-1){
						labels +=label.get("name");
						labelIds +=label.get("labelId");
					}else{
						labels +=label.get("name")+" ";
						labelIds +=label.get("labelId")+";";
					}
				}
			}
			plugin.put("labels", labels);
			plugin.put("labelIds", labelIds);
		}
		return JSON.toJSONString(list);
	}

	@Override
	public List getLabelReources(JSONObject params) {
		// TODO Auto-generated method stub
		List<Map> labelList= dao.findResourceLabels(params);
		if(JudgeUtil.isEmpty(labelList)){
			labelList = new ArrayList<>();
		}
		return labelList;
	}
	
	public String getColunmName(String name){
		return colunmNameMap.get(name);
	}

	@Override
	public String listAllLabels(JSONObject params) {
		return JSON.toJSONString(dao.listAllLabels(params));
	}

	@Override
	public String getLabelsByLabelId(JSONObject params) {
		List<String> result = new ArrayList<>();
		List<Map> labelList= dao.listAllLabels(params);
		if(!JudgeUtil.isEmpty(labelList)){
			for(Map map : labelList){
				String name =(String)map.get("name");
				result.add(name);
			}
		}
		return JSONArray.toJSONString(result);
	}

	@Override
	public String getLabelsByResourceId(JSONObject params) {
		List<String> result = new ArrayList<>();
		// TODO Auto-generated method stub
		List<Map> list =getLabelReources(params);
		if(!JudgeUtil.isEmpty(list)){
			for(Map map : list){
				String name =(String)map.get("name");
				result.add(name);
			}
		}
		return JSONArray.toJSONString(result);
	}

	@Override
	public Map getResourceInfo(JSONObject params) {
		// TODO Auto-generated method stub
		JSONObject map = new JSONObject();
//		List<Map> list = getLabelReources(params);
		int typeCode = params.getInteger("labelCode");
//		if(!JudgeUtil.isEmpty(list)){
//			typeCode = Integer.valueOf(list.get(0).get("code").toString());
//		}
		//根据不同的标签类型，查询不同的资源表
		switch (typeCode) {
		    case Constants.Label.COMPONENT:
		    	Map compMap = dao.findComResourceById(params.getString("resourceId"));
		    	map.put("name",compMap.get("RESOURCE_NAME"));
		    	map.put("ResourceCode", ResourceCode.COMPONENT);
			    break;
		    case Constants.Label.PLUGIN:map.put("name", params.get("resourceId"));
		        map.put("ResourceCode", ResourceCode.PLUGIN);
			    break;
		   default: 
			   break;
		}
		return map;
	}
	
	public static void main(String [] args){
		Map map=null;
		JSONObject jo = new JSONObject();
		jo.putAll(map);
		
	}
	
}
