package com.dc.appengine.appmaster.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.dao.ILabelsDAO;
import com.dc.appengine.appmaster.dao.PageQuery;
import com.dc.appengine.appmaster.entity.Page;

@Component("labelsDAO")
public class  LabelsDAO extends PageQuery implements ILabelsDAO{

	@Resource
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int saveLabelType(JSONObject params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("ma_label.saveLabelType", params);
	}

	@Override
	public int deleteLabelType(String id) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.delete("ma_label.deleteLabelType", id);
	}

	@Override
	public int updateLabelType(JSONObject params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.update("ma_label.updateLabelType", params);
	}

	@Override
	public Page listLabelTypes(int pageSize, int pageNum, JSONObject params) {
		// TODO Auto-generated method stub
		return pageQuery(params, pageNum, pageSize, "ma_label.listLabelTypes");
	}

	@Override
	public int saveLabel(JSONObject params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("ma_label.saveLabel", params);
	}

	@Override
	public int deleteLabel(String id) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("ma_label.deleteLabel", id);
	}

	@Override
	public int updateLabel(JSONObject params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("ma_label.updateLabel", params);
	}

	@Override
	public Page listLabels(int pageSize, int pageNum, JSONObject params) {
		// TODO Auto-generated method stub
		return pageQuery(params, pageNum, pageSize, "ma_label.listLabels");
	}

	@Override
	public int deleteLabelResMap(String id) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.delete("ma_label.deleteLabelResMap", id);
	}

	public int checkLabel(JSONObject condition) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectOne("ma_label.checkLabel", condition);
	}

	@Override
	public int saveLabelResMap(JSONObject params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.insert("ma_label.saveLabelResMap", params);
	}

	@Override
	public int deleteLabelResMapByParams(JSONObject params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.delete("ma_label.deleteLabelResMapByParams", params);
	}

	@Override
	public List findResourceLabels(JSONObject params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("ma_label.findLabelsByResourceId", params);
	}

	@Override
	public List getLabels4Group(JSONObject params) {
		// TODO Auto-generated method stub
		return sqlSessionTemplate.selectList("ma_label.getLabels4Group", params);
	}

	@Override
	public List listAllLabels(JSONObject params) {
		return sqlSessionTemplate.selectList("ma_label.listLabels", params);
	}

	@Override
	public Map findComResourceById(String id) {
		return sqlSessionTemplate.selectOne("ma_label.findComResourceById", id);
	}
}
