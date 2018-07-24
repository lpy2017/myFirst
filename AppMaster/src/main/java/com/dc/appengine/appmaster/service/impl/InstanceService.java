package com.dc.appengine.appmaster.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.dao.IApplicationDao;
import com.dc.appengine.appmaster.dao.IInstanceDao;
import com.dc.appengine.appmaster.dao.IRollBackDao;
import com.dc.appengine.appmaster.dao.ITemplateDao;
import com.dc.appengine.appmaster.entity.Instance;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.service.IInstanceService;
import com.dc.appengine.appmaster.service.IResourceService;
import com.dc.appengine.appmaster.utils.FileUtil;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dc.appengine.appmaster.utils.Utils;

/**
 * instance主要业务逻辑service
 * 
 * @author yangleiv
 * 
 */

@Service("instanceService")
public class InstanceService implements IInstanceService {

	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory
			.getLogger(InstanceService.class);

	@Autowired
	@Qualifier("instanceDao")
	private IInstanceDao instanceDao;
	
	@Autowired
	@Qualifier("instanceService")
	private IInstanceService instanceService;
	
	@Autowired
	@Qualifier("templateDao")
	private ITemplateDao templateDao;
	
	@Autowired
	@Qualifier("applicationDao")
	private IApplicationDao applicationDao;
	
	@Autowired
	@Qualifier("resourceService")
	private IResourceService resourceService;
	
	@Autowired
	@Qualifier("rollBackDao")
	IRollBackDao rollBackDao;
	
	@Autowired
	@Qualifier("rollBackService")
	private RollBackService rollBackService;

	@Override
	public Map<String, Object> findByInstanceId(String instanceId) {
		return instanceDao.findByInstanceId(instanceId);
	}

	// 已部署应用的实例列表
	@Override
	public Page listInstances(int pageSize, int pageNum, long appId,String sortName,String sortOrder) {
		// TODO Auto-generated method stub
		Page oldPage = new Page(pageSize, instanceDao.countInstancesByAppId(appId));
		oldPage.setAppId(String.valueOf(appId));
		List<Map<String, String>> instances = instanceDao.listInstances(oldPage);
		List<Map<String, String>> deleteInstances = new ArrayList<>();
		if(instances != null && instances.size() > 0){
			for(Map<String,String> m : instances){
				String resVersionId = m.get("resVersionId");
				if(resVersionId != null && !"".equals(resVersionId)){
					//获取版本名称
					String versionName = instanceDao.getVersionName(resVersionId);
					m.put("versionName", versionName);
				}
				String status = m.get("status");
				if(BlueprintService.RESOURCE_POOL_DELETE.equals(status)){
					deleteInstances.add(m);
				}
			}
		}
		for(Map<String, String> deleteInstance : deleteInstances){
			instances.remove(deleteInstance);
		}
		SortUtil.sort(instances, SortUtil.getColunmName("bluePrintInsComIns", sortName), sortOrder);
		Page newPage = new Page(pageSize, instances.size());
		newPage.setStartRowNum(pageSize*(pageNum-1));
		newPage.setEndRowNum(pageSize*pageNum);
		List rowsPage=new ArrayList<>();
		int endNum = newPage.getEndRowNum()>newPage.getTotal()?newPage.getTotal():newPage.getEndRowNum();
		for(int start=newPage.getStartRowNum();start<endNum;start++){
			rowsPage.add(instances.get(start));
		}
		newPage.setRows(rowsPage);
		return newPage;
	}
	
	public String deleteInstance(String instanceId){
		//查询版本id
		//Map<String, Object> m = instanceDao.findByInstanceId(instanceId);
		//是否存在
		boolean flag = instanceDao.isExitInstance(instanceId);
		if(flag){
			Map<String,Object> instance = instanceDao.findByInstanceId(instanceId);
			//Map<String,Object> app = instanceDao.findAppByInstanceId(instanceId);
			String status = instance.get("STATUS").toString();
			if(status.equals("UNDEPLOYED")){
				int temp = instanceDao.delInstance(instanceId);
				String path = Utils.TMPDIR;
				File file  = new File(path+instanceId);
				FileUtil.deleteAllFilesOfDir(file);
				if(temp > 0){
					return "{\"result\":true,\"message\":\"" + "卸载实例成功" + "\"}";
				}
				return "{\"result\":false,\"message\":\"" + "卸载实例删除" + "\"}";
			}
			return "{\"result\":false,\"message\":\"" + "实例已部署不能删除" + "\"}";
		}
		return "{\"result\":false,\"message\":\"" + "该实例不存在" + "\"}";
	}

	@Override
	public void saveInstance(Instance instance) {
		instanceDao.saveInstance(instance);
	}

	@Override
	public List<Map<String, Object>> listInstancesByAppId(long appId) {
		return instanceDao.listInstancesByAppId(appId);
	}
	
	public void updateInstance(String instanceId,String status){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("instanceId", instanceId);
		map.put("status", status);
		instanceDao.updateInstance(map);
	}
	
	@Override
	public String updateStatus(String message) {
		Map<String,Object> map = JSON.parseObject(message, new TypeReference<Map<String,Object>>(){});
		if(map.size() > 0){
			Map<String,Object> insvarMap = JSON.parseObject(map.get("insvarMap").toString(), new TypeReference<Map<String,Object>>(){});
			boolean isReduce = "true".equals(insvarMap.get("_rc_reduce")) ? true : false;
			boolean flowState = (Boolean)map.get("flow_result");
			String instanceId = map.get("instanceId").toString();
			String op = map.get("operation").toString();
			String resourceVersionId = map.get("resourceVersionId").toString();
			Map<String,Object> m = new HashMap<String, Object>();
			m.put("instanceId", instanceId);
			m.put("resourceVersionId", resourceVersionId);
			if(flowState){
				//收缩就彻底删除
				if(isReduce){
					if(op.equalsIgnoreCase("destroy")){
						instanceDao.delInstance(instanceId);
					}
				}
				//其他只更新状态
				else{
					if(op.equalsIgnoreCase("deploy") || op.equalsIgnoreCase("stop") 
							|| op.equalsIgnoreCase("upgrade") || op.equalsIgnoreCase("rollback")){
						m.put("status", "DEPLOYED");
					}else if(op.equalsIgnoreCase("start")){
						m.put("status", "RUNNING");
					}else if(op.equalsIgnoreCase("destroy")){
						m.put("status", "UNDEPLOYED");
					}else if(op.equalsIgnoreCase("snapshot")){
					}else{
						return MessageHelper.wrap("result", false, "massage", "错误操作命令");
					}
					instanceDao.updateInstance(m);
				}
			}
			return MessageHelper.wrap("result", true, "massage", "更新成功");
		}
		else{
			return MessageHelper.wrap("result", true, "massage", "更新失败，message为空！");
		}
	}

	@Override
	public String getAppStatus(long id) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> instanceList = instanceDao.getInstanceList(id);
		if(instanceList != null && instanceList.size() != 0){
			int runningNum = 0;
			int stopNum = 0;
			int deployNum = 0;
			for(Map<String,Object> m: instanceList){
				if(m.get("status").toString().equals("RUNNING") || 
						m.get("status").toString().equals("running")){
					runningNum ++;
				}else if(m.get("status").toString().equals("STOP") || 
						m.get("status").toString().equals("stop")){
					stopNum ++;
				}else if(m.get("status").toString().equals("DEPLOYED") ||
						m.get("status").toString().equals("deployed")){
					deployNum ++;
				}
			}
			if(stopNum > 0){
				return "STOP";
			}else if(deployNum > 0){
				return "DEPLOYED";
			}else if(runningNum > 0){
				return "RUNNING";
			}
		}
		return "UNDEPLOYED";
	}
	
	public String getBlueInstanceStatus(int blueInstanceId){
		List<Map<String,Object>> map = getAppList(blueInstanceId);
		if(map != null && map.size() != 0){
			int runningNum = 0;
			int stopNum = 0;
			int deployNum = 0;
			for(Map<String,Object> m: map){
				String status = getAppStatus((int)m.get("id"));
				if(status == null){
					return null;
				}else if(status.equals("RUNNING") || status.equals("running")){
					runningNum ++;
				}else if(status.equals("UNDEPLOYED") || status.equals("undeployed")){
					stopNum ++;
				}else if(status.equals("DEPLOYED") || status.equals("deployed")){
					deployNum ++;
				}
			}
			if(stopNum > 0){
				return "UNDEPLOYED";
			}else if(deployNum > 0){
				return "DEPLOYED";
			}else if(runningNum > 0){
				return "RUNNING";
			}
		}
		return "UNDEPLOYED";
	}

	@Override
	public List<Map<String, Object>> getAppList(int blueInstanceId) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> appList = instanceDao.getAppList(blueInstanceId);
		if(appList != null && appList.size() > 0){
			for(Map<String,Object> m : appList){
				String status = getAppStatus(Long.parseLong(m.get("id").toString()));
				m.put("status", status);
			}
		}
		return appList;
	}

	@Override
	public String findversionId(String instanceId) {
		// TODO Auto-generated method stub
		boolean flag = instanceDao.isExitInstance(instanceId);
		if(flag){
			String versionId = instanceDao.findversionId(instanceId);
			return versionId;
		}
		return null;
	}

	@Override
	public String findNodeIP(String instanceId) {
		boolean flag = instanceDao.isExitInstance(instanceId);
		if(flag){
			String nodeId = instanceDao.findNodeIP(instanceId);
			return nodeId;
		}
		return null;
	}

	@Override
	public String findresourceVersionId(String instanceId) {
		boolean flag = instanceDao.isExitInstance(instanceId);
		if(flag){
			String resourceVersionId = instanceDao.findresourceVersionId(instanceId);
			return resourceVersionId;
		}
		return null;
	}
	
	@Override
	public Map<String, Object> findMessage(String instanceId) {
		boolean flag = instanceDao.isExitInstance(instanceId);
		if(flag){
			Map<String, Object> map = instanceDao.findMessage(instanceId);
			return map;
		}
		return null;
	}

	@Override
	public String findComponentInput(String blueId, String bluekey, String instanceId1) {
		// TODO Auto-generated method stub
		String ComponentInput = instanceDao.findComponentInput(blueId,bluekey,instanceId1);
		return ComponentInput;
	}

	@Override
	public String getAppVersionList(int pageSize, int pageNum, long appId) {
		// TODO Auto-generated method stub
		Page page = new Page(pageSize, instanceDao.countVersionByAppId(appId));
		page.setStartRowNum(pageSize*(pageNum-1));
		page.setEndRowNum(pageSize*pageNum);
		page.setAppId(String.valueOf(appId));
		List<Map<String, String>> versions = instanceDao.listVersions(page);
		if(versions != null && versions.size() > 0){
			for(Map<String,String> m : versions){
				String versionId = m.get("RESOURCE_VERSION_ID");
				int instanceNum = instanceDao.countInstancesByVersion(versionId,appId);
				com.dc.appengine.appmaster.entity.Version version = resourceService.getResourceVersion(versionId);
				m.put("versionName", version.getVersionName());
				m.put("num", instanceNum+"");
				m.put("ftpLocation", version.ftpLocation);
				m.put("url", version.url);
			}
		}
		page.setRows(versions);
		return JSON.toJSONString(page);
	}

	@Override
	public String getConfigInfoByVersionId(String configId,long appId) {
		// TODO Auto-generated method stub
		String input = instanceDao.getConfigInput(configId,appId);
		List<Map<String,String>> list = new ArrayList<>();
		if(input != null){
			Map<String,String> map = JSON.parseObject(input,
					new TypeReference<Map<String,String>>() {
					});
			Set<Entry<String, String>> set=map.entrySet();
	          Iterator<Entry<String, String>> it=set.iterator();
	          while(it.hasNext()){
	              Entry<String, String> entry=it.next();
	              String key=entry.getKey();
	              String value=entry.getValue();
	              System.out.println(key+"-"+value);
	              Map<String,String> map1 = new HashMap<String, String>();
					map1.put("key", key);
					map1.put("value", value);
					list.add(map1);
	          }

		}
		
		return JSON.toJSONString(list);
	}

	@Override
	public String updateComponentInputTemp(String resId, long appId,
			String key, String value) {
		// TODO Auto-generated method stub
		String input = instanceDao.getConfigInput(resId,appId);
		Map<String,String> map = JSON.parseObject(input,new TypeReference<Map<String, String>>(){});
		map.put(key, value);
		String newInputTemp = JSON.toJSONString(map);
		int temp = instanceDao.updateComponentInputTemp(newInputTemp,resId,appId);
		return MessageHelper.wrap("result", true, "massage", "保存成功");
	}
	
	@Override
	public String removeLine(String resId, long appId, String key, String value) {
		// TODO Auto-generated method stub
		String input = instanceDao.getConfigInput(resId,appId);
		Map<String,String> map = JSON.parseObject(input,new TypeReference<Map<String, String>>(){});
		map.remove(key);
		String newInputTemp = JSON.toJSONString(map);
		int temp = instanceDao.updateComponentInputTemp(newInputTemp,resId,appId);
		return MessageHelper.wrap("result", true, "massage", "保存成功");
	}
	
	public static void main(String[] args){
		String input = "{'deployPath':'/usr/share/tomcat/webapps/','deployPath':'/usr/share/tomcat/webapps/'}";
		Map<String,String> map = JSON.parseObject(input,new TypeReference<Map<String, String>>(){});
		String key = "depPath";
		String value = "/usr";
		map.put(key, value);
		String newInputTemp = JSON.toJSONString(map);
		System.out.println(newInputTemp);
	}

	@Override
	public String updateResVersionInstance(long appId, String resVersionId, String oldResVersionId) {
		// TODO Auto-generated method stub
		List<Map<String,Object>> instances = instanceDao.getInstanceListByResVerionId(appId,oldResVersionId);
		if(instances != null && instances.size() > 0){
			for(Map<String,Object> m : instances){
				String insId = m.get("id").toString();
				instanceDao.updateInstanceResVersionId(resVersionId,insId);
			}
		}
		return MessageHelper.wrap("result", true, "massage", "更新成功");
	}

	@Override
	public List<Map<String, Object>> getInstanceList(long appId) {
		List<Map<String,Object>> instanceList = instanceDao.getInstanceList(appId);
		return instanceList;
	}
	@Override
	public List<Map<String, Object>> getInstances(long appId, String resourceVersionId,String nodeId) {
		List<Map<String,Object>> instanceList = instanceDao.getInstances(appId,resourceVersionId,nodeId);
		return instanceList;
	}

	@Override
	public void updateInstanceAll(Instance instance) {
		instanceDao.updateInstanceAll(instance);
	}

	@Override
	public String findresVerId(String componentName, String version) {
		return instanceDao.findresVerId(componentName,version);
	}

	@Override
	public List<Map<String, Object>> findInstances(String instanceId, String componentName, String resourceVersionId) {
		return instanceDao.findInstances(instanceId,componentName,resourceVersionId);
	}
	
	public void delInstanceAndFile(String instanceId){
		instanceDao.delInstance(instanceId);
		String path = Utils.TMPDIR;
		File file  = new File(path+instanceId);
		FileUtil.deleteAllFilesOfDir(file);
	}

	@Override
	public List<Map<String, Object>> findInstancesBybp(String blueInstanceName, String componentName,
			String resourceVersionId) {
		return instanceDao.findInstancesBybp(blueInstanceName,componentName,resourceVersionId);
	}

	@Override
	public String getBlueprintComponentInstanceParams(String instanceId) throws Exception {
		Map<String, Object> detail = instanceDao.getInstanceDetailById(instanceId);
		if(detail == null){
			throw new Exception("组件实例[" + instanceId + "]不存在！");
		}
		else{
			if(detail.get("params") == null){
				return null;
			}
			else{
				return "" + detail.get("params");
			}
		}
	}

	@Override
	public void updateBlueprintComponentInstanceParams(Map<String, Object> param) {
		instanceDao.updateBlueprintComponentInstanceParams(param);
	}
}
 