package com.dc.appengine.appmaster.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.dao.IApplicationDao;
import com.dc.appengine.appmaster.dao.IBlueprintDao;
import com.dc.appengine.appmaster.dao.IInstanceDao;
import com.dc.appengine.appmaster.entity.AppVersion;
import com.dc.appengine.appmaster.entity.AppVersionStrategy;
import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.entity.EnvVarInfo;
import com.dc.appengine.appmaster.entity.Item;
import com.dc.appengine.appmaster.entity.OperationApp;
import com.dc.appengine.appmaster.entity.OperationResource;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.Strategy;
import com.dc.appengine.appmaster.entity.StrategyItem;
import com.dc.appengine.appmaster.orchestration.envs.EnvParserFactory;
import com.dc.appengine.appmaster.service.IApplicationService;
import com.dc.appengine.appmaster.service.IEnvParamService;
import com.dc.appengine.appmaster.service.IInstanceService;
import com.dc.appengine.appmaster.service.IStrategyService;
import com.dc.appengine.appmaster.service.IUserService;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dc.appengine.appmaster.utils.StrategyHelper;
import com.dc.appengine.appmaster.ws.client.service.IAdapterService;

@Service("applicationService")
public class ApplicationService implements IApplicationService{
	private static final Logger log=LoggerFactory.getLogger(ApplicationService.class);  
	@Autowired
	@Qualifier("applicationDao")
	IApplicationDao dao;
	
	@Autowired
	@Qualifier("blueprintDao")
	private IBlueprintDao blueprintDao;

	@Autowired
	@Qualifier("instanceService")
	IInstanceService instanceService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("adapterService")
	private IAdapterService adapterService;	
	
	@Transactional(isolation=Isolation.READ_COMMITTED)
	public Page listOperationApps(int pageSize,int pageNum,Long userId,int blueInstanceId,String sortName,String sortOrder){
		Application app = new Application();
		//权限管理userId
		String resultStr= userService.getSonsOfUser(userId);
		Map<Long,String> map=JSON.parseObject(resultStr, new TypeReference<Map<Long,String>>(){});
		StringBuilder userIds= new StringBuilder();
		int index=0;
		for (long unit:map.keySet()){
			if(index!=0){
				userIds.append(",");
			}
			index++;
			userIds.append(unit+"");
		}
		app.setUserIds(userIds.toString().split(","));
		app.setBlueInstanceId(blueInstanceId);
		int num = dao.countAppsByUserIdSearch(app);
		Page page = new Page(pageSize, num);
		page.setStartRowNum(pageSize*(pageNum-1));
		page.setEndRowNum(pageSize*pageNum);
		page.setObjCondition(app);
		List<Application> apps = dao.listOperationApps(page);
		List rows=null;
		if(apps !=null && apps.size()>0){
			rows=JSONArray.parseArray(JSONArray.toJSONString(apps));
		}
//		int roleId = userService.getUserRole(userId);
//		List<Map<String, Object>> clustersList = new ArrayList<>();
		/*if(roleId == 4){
			clustersList = adapterService.getUserCluster(String.valueOf(1));
		}else{
			clustersList = adapterService.getUserCluster(userId.toString());
		}*/
		//为了兼容旧版本没有nodeName
		Map<String, Object> blueprint = blueprintDao.getBlueprintInstanceById(blueInstanceId);
		String blueprintInfo = "" + blueprint.get("INFO");
		Map<String, Object> json = JSON.parseObject(blueprintInfo,new TypeReference<Map<String, Object>>(){});
		List<Map<String, Object>>  nodeDataArray =  (List<Map<String, Object>>) json.get("nodeDataArray");
		for (int i=0;i<rows.size();i++) {
			Map appMap = (Map)rows.get(i);
			Long id = Long.valueOf(appMap.get("id").toString());
			String clusterId = appMap.get("clusterId").toString();
			//获取应用实例数
			//oneApp.setInstancesNumber(dao.countInstanceNum(oneApp.getId()));
			int instancesSize = 0;
			List<Map<String, Object>> instances = instanceService.getInstanceList(id);
			if(instances != null || instances.size() > 0){
				int delete = 0;
				for(Map<String, Object> instance : instances){
					String status = "" + instance.get("status");
					if(BlueprintService.RESOURCE_POOL_DELETE.equals(status)){
						delete++;
					}
				}
				instancesSize = (instances.size() - delete);
			}
			appMap.put("instancesNumber",instancesSize);
			//获取所属集群名(应用的app_id唯一，只对应一个集群)
			//获取ClusterName
			Map<String,Object> m = new HashMap<>();
			m.put("clusterId", clusterId);
			m.put("appId",id+"");
			String clusterName = dao.getClusterNameByClusterId(m);
			appMap.put("clusterName",clusterName);	
			//获取最新时间
			if((Integer)appMap.get("instancesNumber") > 0){
				String newTime = dao.getAppUpdateTime(id);
				appMap.put("updateTime",newTime.substring(0, 19));
				String status = instanceService.getAppStatus(id);
				appMap.put("status",status);
			}

			String componentName = (String)appMap.get("componentName");
			//如果数据库中componentName为空，在去蓝图中寻找nodeName
			if(componentName == null || "".equals(componentName)){
				for(Map<String, Object> nodeData : nodeDataArray){
					String appName = (String)appMap.get("appName");
					String text = "" + nodeData.get("text");
					if(appName.equals(text)){
						Object nodeName = nodeData.get("nodeName");
						if(nodeName != null && !"".equals(("" + nodeName).trim())){
							componentName = "" + nodeName;
						}
						//如果蓝图中nodeName也为空
						else{
							//从组件名截，组件名不允许有"-"
							int mark = text.indexOf("-");
							if(mark != -1){
								componentName = text.substring(0, mark);
							}
							else{
								componentName = text;
							}
						}
						break;
					}
				}
				appMap.put("componentName",componentName);
			}
		}
		SortUtil.sort(rows, SortUtil.getColunmName("bluePrintInsCom", sortName), sortOrder);
		List rowsPage=new ArrayList<>();
		int endNum = page.getEndRowNum()>num?num:page.getEndRowNum();
		for(int start=page.getStartRowNum();start<endNum;start++){
			rowsPage.add(rows.get(start));
		}
		page.setRows(rowsPage);
		return page;
	}


	public Application getAppInfo(String appId){	
		Application app = new Application() ;
		app = dao.getAppInfo(appId);
		//获取应用实例数
		app.setInstancesNumber(dao.countInstanceNum(app.getId()));
		if(app!=null && app.getInstancesNumber() > 0){
			String newTime = dao.getAppUpdateTime(app.getId());
			app.setUpdateTime(newTime.substring(0, 19));
			//获取ClusterName
			Map<String,Object> m = new HashMap<>();
			m.put("clusterId", app.getClusterId());
			m.put("appId",app.getId()+"");
			String clusterName = dao.getClusterNameByClusterId(m);
			app.setClusterName(clusterName);
			String status = instanceService.getAppStatus(app.getId());
			app.setStatus(status);
		}
		return app;
			
		}

	//unuse
	@Override
	public int getConfigUsedCount(int versionId) {
		// TODO Auto-generated method stub
		return 0;
	}

	//unuse
	@Override
	public List<Map<String, Object>> findAppByNodeName(String nodeName) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Application> findAppByBlueprintInstIdAndKey(int blueprintInsId, long key) {
		return dao.findAppByBlueprintInstIdAndKey(blueprintInsId, key);
	}


	@Override
	public String listAppsOfCluster(String clusterId, long userId) {
		// TODO Auto-generated method stub
		Application app = new Application();
		String resultStr= userService.getSonsOfUser(userId);
		Map<Long,String> map=JSON.parseObject(resultStr, new TypeReference<Map<Long,String>>(){});
		StringBuilder userIds= new StringBuilder();
		int index=0;
		for (long unit:map.keySet()){
			if(index!=0){
				userIds.append(",");
			}
			index++;
			userIds.append(unit+"");
		}
		app.setUserIds(userIds.toString().split(","));
		app.setClusterId(clusterId);
		return JSON.toJSONString(dao.listAppsOfCluster(app));
	}


	@Override
	public void saveApplication(Application app) {
		// TODO Auto-generated method stub
		dao.save2(app);
	}

	public Application getApp(long appId){
		return dao.getAppInfo(appId+"");
	}


	@Override
	public List<Application> findByAutoMaintain() {
		// TODO Auto-generated method stub
		return null;
	}
	
		@Override
	public String listApps(long blue_instance_id,
			long userId) {
		// TODO Auto-generated method stub
		Application app = new Application();
		String resultStr= userService.getSonsOfUser(userId);
		Map<Long,String> map=JSON.parseObject(resultStr, new TypeReference<Map<Long,String>>(){});
		StringBuilder userIds= new StringBuilder();
		int index=0;
		for (long unit:map.keySet()){
			if(index!=0){
				userIds.append(",");
			}
			index++;
			userIds.append(unit+"");
		}
		app.setUserIds(userIds.toString().split(","));
		app.setBlueInstanceId(blue_instance_id);
		List<Application> apps = dao.listApps(app);
		//为了兼容旧版本没有nodeName
		Map<String, Object> blueprint = blueprintDao.getBlueprintInstanceById(Integer.parseInt(String.valueOf(blue_instance_id)));
		String blueprintInfo = "" + blueprint.get("INFO");
		Map<String, Object> json = JSON.parseObject(blueprintInfo,new TypeReference<Map<String, Object>>(){});
		List<Map<String, Object>>  nodeDataArray =  (List<Map<String, Object>>) json.get("nodeDataArray");
		for(Application item : apps){
			String componentName = item.getComponentName();
			//如果数据库中componentName为空，在去蓝图中寻找nodeName
			if(componentName == null || "".equals(componentName)){
				for(Map<String, Object> nodeData : nodeDataArray){
					String appName = item.getAppName();
					String text = "" + nodeData.get("text");
					if(appName.equals(text)){
						Object nodeName = nodeData.get("nodeName");
						if(nodeName != null && !"".equals(("" + nodeName).trim())){
							componentName = "" + nodeName;
						}
						//如果蓝图中nodeName也为空
						else{
							//组件名不允许有"-"
							int mark = text.indexOf("-");
							if(mark != -1){
								componentName = text.substring(0, mark);
							}
							else{
								componentName = text;
							}
						}
						break;
					}
				}
				item.setComponentName(componentName);
			}
		}
		return JSON.toJSONString(apps);
	}


		@Override
		public Application getNodeDisplayByAppName(Map paraMap) {
			return dao.getNodeDisplayByAppName(paraMap);
		}
}