package com.dc.appengine.appmaster.dao.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

@Component("configDao")
public class ConfigDao{
	
	@Resource
	SqlSessionTemplate sqlSessionTemplate;
	public boolean checkName( String configName,String type){
		 Map<String,Object> param= new HashMap<String,Object>();
		 param.put("type", type);
		 param.put("configName", configName);
		 Object obj=sqlSessionTemplate.selectOne("configs.checkName",param);
		 if(obj!=null){
			 int num=Integer.valueOf(obj.toString());
			 return num >0;
		 }
		return false;
	}
	
	public int addNewConfig(String configName ,String type,String userId,String description){
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("configName",configName);
		param.put("description",description);
		param.put("type",type);
		param.put("userId",userId);
		Object obj=sqlSessionTemplate.insert("configs.addNewConfig",param);
		if(obj==null){
			return 0;
		}
		return Integer.valueOf(param.get("id").toString());
	}
	
	public int addComponnetConfigs(String configName,String userId,String blueprintId,String bluekey,String componentVision){
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("configName",configName);
		param.put("userId",userId);
		param.put("blueprintId",blueprintId);
		param.put("bluekey",bluekey);
		param.put("componentvision",componentVision);
		Object obj=sqlSessionTemplate.insert("configs.addComponnetConfigs",param);
		if(obj==null){
			return 0;
		}
		return Integer.valueOf(param.get("id").toString());
	}
	public boolean checkVersionName(int configId,String versionName){
		 Map<String,Object> param= new HashMap<String,Object>();
		 param.put("configId", configId);
		 param.put("versionName", versionName);
		 Object obj=sqlSessionTemplate.selectOne("configs.checkVersion",param);
		 if(obj!=null){
			 int num=Integer.valueOf(obj.toString());
			 return num >0;
		 }
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getVersionList(int configId ){
		 Map<String,Object> param= new HashMap<String,Object>();
		 param.put("configId", configId);
		 Object obj=sqlSessionTemplate.selectList("configs.getVersionList",param);
		 if(obj==null){
			 return Collections.emptyList();
		 }
		 return (List<Map<String,Object>>)obj;
		 
 
	}
	public int addConfigVersion(int configId,String versionName){
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("configId", configId);
		param.put("versionName", versionName);
		Object obj=sqlSessionTemplate.insert("configs.addVersion",param);
		if(obj==null){
			return 0;
		}
		Object ID=param.get("id");
		if(ID!=null){
			return Integer.valueOf(ID.toString());
		}
		return 0;
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getConfigList(Map<String,Object> param){
		Object obj=sqlSessionTemplate.selectList("configs.getConfigList",param);
		if(obj==null){
			return Collections.EMPTY_LIST;
		}
		return (List<Map<String,Object>>)obj;
	}
	@SuppressWarnings("unchecked")
	public Map<String,Object> checkPair(int versionId){
		Object obj=sqlSessionTemplate.selectOne("configs.checkPair",versionId);
		if(obj==null){
			return null;
		}
		return (Map<String,Object> ) obj;
	}
	public int getVersionId(int configId){
		Object obj=sqlSessionTemplate.selectOne("configs.checkVersionId",configId);
		if(obj==null){
			return 0;
		}
		return (Integer)obj;
	}
	public int addPair(int versionId){
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("versionId", versionId);
		param.put("id", -1);
		Object obj=sqlSessionTemplate.insert("configs.addPair",param);
		if(obj==null){
			return 0;
		}
		Object ID=param.get("id");
		return (Integer)ID;
	}
	@SuppressWarnings("unchecked")
	public Map<String,Object> checkKey(Map<String,Object> param){
		Object obj=null;
		if(param.containsKey("id")){
			obj=sqlSessionTemplate.selectOne("configs.checkUpdateKey",param);
		}else{
			obj=sqlSessionTemplate.selectOne("configs.checkKey",param);
		}
		if(obj==null){
			return null;
		}
		return (Map<String,Object>)obj;
	}
	public String getValue(String key,int versionId){
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("versionId", versionId);
		param.put("key",key);
		Object obj=sqlSessionTemplate.selectOne("configs.getValue",param);
		if(obj==null){
			return null;
		}
		return (String)obj;
	}
	public String setValue(int versionId,String key,String value){
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("versionId", versionId);
		param.put("key",key);
		param.put("value",value);
		Object obj=sqlSessionTemplate.selectOne("configs.setValue",param);
		if(obj==null){
			return null;
		}
		return (String)obj;
	}
	public void addLine(Map<String,Object> param){
		sqlSessionTemplate.insert("configs.addLine",param);
		int versionId=Integer.valueOf(param.get("versionId").toString());
		sqlSessionTemplate.update("configs.updateOpTime",versionId);
	}
	public  void updateLine(Map<String,Object> param){
		sqlSessionTemplate.update("configs.updateLine",param);
		int versionId=Integer.valueOf(param.get("versionId").toString());
		sqlSessionTemplate.update("configs.updateOpTime",versionId);
	}
	public void delLine(Map<String,Object> param){
		long id=Long.valueOf(param.get("id").toString());
		sqlSessionTemplate.delete("configs.delLine",id);
	 
	}
	public List<String> getClients(int configId){
		Object obj=sqlSessionTemplate.selectList("configs.getClients",configId);
		if(obj==null){
			return Collections.emptyList();
		}
		@SuppressWarnings("unchecked")
		List<String> list=(List<String>)obj;
		return list;
	}
	public void addClient(int configId,String clientUrl){
		Map<String,Object> map= new HashMap<String,Object>();
		map.put("configId", configId);
		map.put("clientUrl", clientUrl);
		sqlSessionTemplate.insert("configs.addClient",map);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getApps(String userId){
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("userId",userId.split(","));
		Object obj=sqlSessionTemplate.selectList("configs.getApps",param);
		if(obj==null){
			return Collections.emptyList();
		}
		return (List<Map<String,Object>>)obj;
	}
	public List<Map<String,Object>> getVersions(int configId){
		Object obj=sqlSessionTemplate.selectList("configs.getVersions",configId);
		if(obj==null){
			return Collections.emptyList();
		}
		List<Map<String,Object>> list=(List<Map<String,Object>>)obj;
		return list;
	}
	public void clearClient(int configId){
		sqlSessionTemplate.delete("configs.clearClient",configId);
	}
	public void clearConfig(int verionId){
		sqlSessionTemplate.delete("configs.clearConfig",verionId);
	}
	public void delConfig(int configId){
		sqlSessionTemplate.delete("configs.delConfig",configId);
	}
	public void deleteVersion(int versionId){
		sqlSessionTemplate.delete("configs.deleteVersion",versionId);
	}
	public boolean clone(int fromId,int toId){
		if(checkInit(toId)){
			return false;
		}
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("fromId", fromId);
		param.put("toId", toId);
		sqlSessionTemplate.selectOne("configs.clone",param);
		return checkInit(toId);
	}
	public boolean checkInit(int versionId){
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("versionId", versionId);
		Object obj=sqlSessionTemplate.selectOne("configs.checkInit",param);
		if(obj==null){
			return true;
		}
		int num=Integer.valueOf(obj.toString());
		return num>0;
	}
	public Map<String,Object> getVersionInfo(int versionId){
		Object obj=sqlSessionTemplate.selectOne("configs.getVersionInfo",versionId);
		if(obj==null){
			return null;
		}
		return (Map<String,Object>)obj;
	}
	public int getVersionIdByName(int configId,String versionName){
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("configId",configId);
		param.put("versionName", versionName);
		Object obj=sqlSessionTemplate.selectOne("configs.getVersionIdByName",param);
		if(obj==null){
			return 0;
		}
		return Integer.valueOf(obj.toString());
	}
	public Map<String,Object> getConfigInfo(int configId){
		Object obj=sqlSessionTemplate.selectOne("configs.getConfigInfo",configId);
		if(obj==null){
			return null;
		}
		Map<String,Object> map=(Map<String,Object>)obj;
		return map;
	}
	public List<Map<String,Object>> getConfigByPage( Map<String,Object> param){
		Object obj=sqlSessionTemplate.selectList("configs.getConfigsByPage",param);
		if(obj==null){
			return Collections.emptyList();
		}
		return (List<Map<String,Object>>)obj;
	}
	public int getConfigNum( Map<String,Object> param){
		Object obj=sqlSessionTemplate.selectOne("configs.getConfigsNum",param);
		if(obj==null){
			return 0;
		}
		return (Integer)obj;
	}
	
	public int getConfigIdByVersion( int versionId){
		Object obj=sqlSessionTemplate.selectOne("configs.getConfigIdByVersion",versionId);
		if(obj==null){
			return 0;
		}
		return (Integer)obj;
	}
	public int getConfigIdByKey( long keyId){
		Object obj=sqlSessionTemplate.selectOne("configs.getConfigIdByKey",keyId);
		if(obj==null){
			return 0;
		}
		return (Integer)obj;
	}
	public Map<String,Object> getConfigInfoByVersionId(int versionId){
		Object obj=sqlSessionTemplate.selectOne("configs.getConfigInfoByVersionId",versionId);
		if(obj==null){
			return null;
		}
		Map<String,Object> map=(Map<String,Object>)obj;
		return map;
	}
	public int getconfigIdByName(String configName){
		Object obj=sqlSessionTemplate.selectOne("configs.getconfigIdByName",configName);
		if(obj==null){
			return 0;
		}
		return (Integer)obj;
	}
	
	public String findValue(String blueprintId,String componentName,String componnetVision,String bluekey,String key){
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("blueprintId",blueprintId);
		param.put("componentName",componentName);
		param.put("componnetVision",componnetVision);
		param.put("bluekey",bluekey);
		param.put("key", key);
		Object obj=sqlSessionTemplate.selectOne("configs.findValue",param);
		if(obj==null){
			return null;
		}
		return obj.toString();
	}
	
	public String findValue2(String versionId,String key){
		Map<String,Object> param= new HashMap<String,Object>();
		param.put("versionId",versionId);
		param.put("key", key);
		Object obj=sqlSessionTemplate.selectOne("configs.getkeyValue",param);
		if(obj==null){
			return null;
		}
		return obj.toString();
	}
	
	public String updateConfigs(String versionId,Map<String,Object> map){
		for(Map.Entry<String, Object> entry:map.entrySet()){
			String key=entry.getKey();
			Object value=entry.getValue();
			Map<String,Object> param= new HashMap<String,Object>();
			param.put("versionId",versionId);
			param.put("key", key);
			param.put("svalue", value);
			sqlSessionTemplate.update("configs.updateConfigs",param);
		}
		return "true";
	}

	public int getNum(Map<String, Object> param) {
		Object obj=sqlSessionTemplate.selectOne("configs.getNum",param);
		if(obj==null){
			return 0;
		}
		return (Integer)obj;
	}
	
	public List<Map<String,Object>> getConfigs( Map<String,Object> param){
		Object obj=sqlSessionTemplate.selectList("configs.getConfigs",param);
		if(obj==null){
			return Collections.emptyList();
		}
		return (List<Map<String,Object>>)obj;
	}

	public Map<String, Object> configinfos(Map<String, Object> param) {
		// TODO Auto-generated method stub
		Object obj=sqlSessionTemplate.selectOne("configs.configinfos",param);
		if(obj==null || "".equals(obj)){
			return null;
		}
		return (Map<String, Object>)obj;
	}
	
	public Map<String, Object> findFlowId(Map<String, Object> param) {
		// TODO Auto-generated method stub
		Object obj=sqlSessionTemplate.selectOne("configs.findFlowId",param);
		if(obj==null || "".equals(obj)){
			return null;
		}
		return (Map<String, Object>)obj;
	}
	
	public String findNewFlowId(Map<String, String> param) {
		// TODO Auto-generated method stub
		Object obj=sqlSessionTemplate.selectList("configs.findNewFlowId",param);
		if(obj==null || "".equals(obj)){
			return null;
		}
		return JSON.toJSONString(obj);
	}
}