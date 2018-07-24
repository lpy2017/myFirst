package com.dc.appengine.appmaster.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.entity.Page;

public interface ILabelsDAO {
	
	public int saveLabelType(JSONObject params);
	public int deleteLabelType(String id);
	public int updateLabelType(JSONObject params);
	public Page listLabelTypes(int pageSize,int pageNum,JSONObject params);
	
	public int saveLabel(JSONObject params);
	public int deleteLabel(String id);
	public int deleteLabelResMap(String id);
	public int updateLabel(JSONObject params);
	public Page listLabels(int pageSize,int pageNum,JSONObject params);
	public List listAllLabels(JSONObject params);
	
	public int saveLabelResMap(JSONObject params);
	
	public int deleteLabelResMapByParams(JSONObject params);
	
	public List findResourceLabels(JSONObject params);
	
	public List getLabels4Group(JSONObject params);
	
	public Map findComResourceById(String id);
	
}