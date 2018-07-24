package com.dc.appengine.appmaster.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface ILabelsService {
	/*标签类型接口*/
	public String saveLabelType(JSONObject params);
	public String deleteLabelType(String id);
	public String updateLabelType(JSONObject params);
	public String listLabelTypes(int pageSize,int pageNum,String typeName);
	
	/*标签接口*/
	public String saveLabel(JSONObject params);
	public String deleteLabel(String id);
	public String updateLabel(JSONObject params);
	public String listLabels(int pageSize,int pageNum,String labelName,Integer labelCode,String sortName,String sortOrder);
	public String checkLabel(String labelName, String id,int code);
	public String listAllLabels(JSONObject params);
	
    /*管理资源和标签的关联关系*/
	public Boolean saveResourceLabelMap(String resourceId,String labels);
	public String updateResourceLabel(JSONObject params);
	public List getLabels4Group(Integer labelCode);
	public String getLabels4Plugin(String rows);
	public List getLabelReources(JSONObject params);
	public String getLabelsByLabelId(JSONObject params);
	public String getLabelsByResourceId(JSONObject params);
	public Map getResourceInfo(JSONObject params);
}