package com.dc.appengine.appmaster.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.dao.IBlueprintDao;
import com.dc.appengine.appmaster.dao.IReleaseManageDao;
import com.dc.appengine.appmaster.dao.impl.BluePrintTypeDao;
import com.dc.appengine.appmaster.entity.BluePrint;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.IReleaseManageService;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dc.appengine.appmaster.utils.UUIDGenerator;
import com.dc.appengine.appmaster.ws.server.BlueprintRestService;
@Service("releaseeManageService")
public class ReleaseManageService implements IReleaseManageService {
	@Autowired
	@Qualifier("releaseManageDao") 
	IReleaseManageDao releaseManageDao;
	
	@Autowired
	@Qualifier("blueprintDao")
	private IBlueprintDao blueprintDao;
	
	@Autowired
	@Qualifier("bluePrintTypeDao")
	private BluePrintTypeDao bluePrintTypeDao;
	
	@Autowired
	@Qualifier("blueprintDao")
	private IBlueprintDao blueprintdao;
	
	@Resource
	IBlueprintService blueprintService;
	
	@Override
	@Transactional
	public String saveRelease(String name, String description, String lifecycleId,long userId) {
		Map<String,Object> params = new HashMap<>();
		String releaseId =UUID.randomUUID().toString();
		params.put("id", releaseId);
		params.put("name", name);
		params.put("description", description);
		params.put("lifecycleId", lifecycleId);
		params.put("userId", userId);
		//保存发布
		int count =releaseManageDao.saveRelease(params);
		Map<String,Object> result = new HashMap<>();
		if(count==1){
			result.put("result", true);
			result.put("message", "发布创建成功！");
		}else{
			result.put("result", false);
			result.put("message", "发布创建失败！");
		}
		return JSON.toJSONString(result);
	}

	@Override
	public String listReleases(String releaseName, String lifecycleName, String description, int pageSize, int pageNum,
			String sortName, String sortOrder) {
		Map<String,Object> params = new HashMap<>();
		params.put("releaseName", releaseName);
		params.put("lifecycleName", lifecycleName);
		params.put("description", description);
		params.put("sortName", SortUtil.getColunmName("release", sortName));
		params.put("sortOrder", sortOrder);
		Page page = releaseManageDao.listReleasesByPage(pageSize, pageNum,params);
		return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.DisableCircularReferenceDetect);
	}

	@Override
	@Transactional
	public String deleteRelease(String id) {
		//删除发布记录
		int count = releaseManageDao.deleteReleaseById(id);
		Map<String,Object> result = new HashMap<>();
		if(count==1){
			//删除发布关联的应用
			Map<String,Object> params = new HashMap<>();
			params.put("releaseId",id);
			releaseManageDao.deleteReleaseApps(params);
			//删除发布阶段环境
			deleteReleaseStageEnv(id,null);
			result.put("result", true);
			result.put("message", "发布删除成功！");
		}else{
			result.put("result", false);
			result.put("message", "发布删除失败！");
		}
		return JSON.toJSONString(result);
	}
	public Map<String,Object> addReleaseStageEnv(String releaseId,String lifecycleId,List<String> resourceIds){
		//初始化应用与生命周期阶段的关联关系
		List phaseList=releaseManageDao.findPhaseBylifecycleId(lifecycleId);
		Map<String,Object> result = new HashMap<>();
		List stageMaps = new ArrayList<>();
		for(String resourceId:resourceIds){
			for(int i=0;i<phaseList.size();i++){
				Map phase = (Map)phaseList.get(i);
				Map<String,Object> stageEnvMap = new HashMap<>();
				stageEnvMap.put("id", UUID.randomUUID().toString());
				stageEnvMap.put("releaseId", releaseId);
				stageEnvMap.put("resourceId", resourceId);
				stageEnvMap.put("lifecycleStageId", phase.get("id"));
				stageMaps.add(stageEnvMap);
			}
		}
		if(stageMaps.size()==0){
			result.put("result", false);
			result.put("message", "关联的发布阶段为空！");
		}else{
			Map<String,Object> params = new HashMap<>();
			params.put("stageMaps", stageMaps);
			int addCount =releaseManageDao.addReleaseStageEnvs(params);
			if(addCount >0){
				result.put("result", true);
				result.put("message", "发布创建成功！");
			}else{
				result.put("result", false);
				result.put("message", "发布关联发布阶段失败！");
			}
		}
		return result;
	}
	public Boolean deleteReleaseStageEnv(String releaseId,String resourceId){
		Map<String,Object> params = new HashMap<>();
		params.put("releaseId",releaseId);
		params.put("resourceId",resourceId);
		List stageList = releaseManageDao.findReleaseStageEnvs(params);
		//删除蓝图实例
		if(!JudgeUtil.isEmpty(stageList)){
			deleteBlueprintIns(stageList);
		}
		//删除发布关联的发布过程
		releaseManageDao.deleteReleaseStageEnvs(params);
		return true;
	}
	@Override
	@Transactional
	public String updateRelease(String id, String name, String description, String lifecycleId) {
		// TODO Auto-generated method stub
		Map<String,Object> params = new HashMap<>();
		params.put("id", id);
		params.put("name", name);
		params.put("description", description);
		Map release =releaseManageDao.findReleaseById(id);
		String lifecycleIdOld = (String)release.get("lifecycleId");
		if(!lifecycleId.equals(lifecycleIdOld)){
			//删除发布过程
			deleteReleaseStageEnv(id,null);
			params.put("lifecycleId", lifecycleId);
		}
		int count =releaseManageDao.updateRelease(params);
		Map<String,Object> result = new HashMap<>();
		if(count==1){
			result.put("result", true);
			result.put("message", "发布更新成功！");
		}else{
			result.put("result", false);
			result.put("message", "发布更新失败！");
		}
		return JSON.toJSONString(result);
	}

	@Override
	public String checkReleaseName(String releaseName,String id) {
		Map<String,Object> params = new HashMap<>();
		Map<String,Object> result = new HashMap<>();
		params.put("id", id);
		params.put("releaseName", releaseName);
		Map map = releaseManageDao.checkRelease(params);
		if(map !=null){
			result.put("result", false);
			result.put("message", "存在同名的发布");
		}else{
			result.put("result", true);
		}
		return JSON.toJSONString(result);
	}

	@Override
	public String listLifecycles(long userId) {
		Map<String,Object> data = new HashMap<>();
		Map<String,Object> params = new HashMap<>();
		params.put("userId", userId);
		List list =releaseManageDao.listLifecycles(params);
		if(list==null){
			list = new ArrayList<>();
		}
		data.put("rows", list);
		return JSON.toJSONString(data);
	}

	@Override
	@Transactional
	public String addReleaseApps(String releaseId, String resourceIds) {
		Map<String,Object> result = new HashMap<>();
		List<String> resIds= Arrays.asList(resourceIds.split(","));
		List<String> resIdsNew= new ArrayList(resIds);
		Map<String,Object> params = new HashMap<>();
		params.put("releaseId", releaseId);
		params.put("resourceIds", resIdsNew);
		List list = releaseManageDao.findAppIds(params);
		if(list !=null && list.size()>0){
			resIdsNew.removeAll(list);
			if(resIdsNew.size()==0){
				result.put("result", true);
				result.put("message", "添加应用成功！");
				return JSON.toJSONString(result);
			}
		}
		//增加应用环境
		Map releaseInfo = releaseManageDao.findReleaseById(releaseId);
		addReleaseStageEnv(releaseId, (String)releaseInfo.get("lifecycleId"), resIdsNew);
		int count =releaseManageDao.addReleaseApps(params);
		if(count>0){
			result.put("result", true);
			result.put("message", "添加应用成功！");
		}else{
			result.put("result", false);
			result.put("message", "添加应用失败！");
		}
		return JSON.toJSONString(result);
	}

	@Override
	public String getReleaseDetail(String releaseId) {
		Map<String,Object> rleaseDetail = new HashMap<>();
		//获取发布的基本信息
		Map releaseInfo =releaseManageDao.findReleaseById(releaseId);
		//获取发布关联的应用信息
		List appList =releaseManageDao.findReleaseAppsById(releaseId);
		Map<String,Object> params = new HashMap<>();
		params.put("releaseId", releaseId);
		String resourceId="";
		String resourceName="";
		if(appList !=null && appList.size()>0){
			resourceId=(String)((Map)appList.get(0)).get("resourceId");
			resourceName=(String)((Map)appList.get(0)).get("resourceName");
			params.put("resourceId", resourceId);
		}
		//获取发布阶段信息（默认选择第一个应用的）
		List stageList = releaseManageDao.findReleaseStageEnvs(params);
		if(stageList==null ||stageList.size()==0){
			String lifecycleId = (String)releaseInfo.get("lifecycleId");
			stageList=releaseManageDao.findPhaseBylifecycleId(lifecycleId);
		}
		stageList=sort(stageList);
		for(int i=0;i<stageList.size();i++){
			Map stage = (Map)stageList.get(i);
			String blueInsId = (String)stage.get("blueInsId"); 
			String flowId = (String)stage.get("cdFlowId");
			if(blueInsId !=null){
				Map blueIns = blueprintDao.getBlueprintInstance(blueInsId);
				if(blueIns !=null){
					Map blueInsInfo = new HashMap<>();
					blueInsInfo.put("blueInstanceId", blueInsId);
					blueInsInfo.put("blueInstanceName", (String)blueIns.get("INSTANCE_NAME"));
					stage.put("blueInsInfo", blueInsInfo);
				}
			}
			if(flowId !=null){
				Map flowInfo =bluePrintTypeDao.getBlueprintTypeById(flowId);
				if(flowInfo !=null){
					Map flow = new HashMap<>();
					flow.put("flowId", flowId);
					flow.put("flowName", flowInfo.get("FLOW_NAME"));
				}
			}
		}
		rleaseDetail.put("releaseInfo", releaseInfo);
		rleaseDetail.put("appList", appList);
		rleaseDetail.put("stageList", stageList);
		rleaseDetail.put("currentApp", resourceName);
		rleaseDetail.put("currentAppId", resourceId);
		return JSON.toJSONString(rleaseDetail);
	}
	
	private List<Map<String, Object>> sort(List<Map<String, Object>> list) {
		if (list.isEmpty()) {
			return list;
		}
		Map<String, Map<String, Object>> dict = new HashMap<>();
		for (Map<String, Object> map : list) {
			if (map.containsKey("pre_stage_id")) {
				dict.put(map.get("pre_stage_id").toString(), map);
			} else {
				dict.put("first", map);
			}
		}
		List<Map<String, Object>> sorted = new ArrayList<>();
		sorted.add(dict.get("first"));
		while (sorted.size() < list.size()) {
			sorted.add(dict.get(sorted.get(sorted.size() - 1).get("id")));
		}
		return sorted;
	}

	@Override
	@Transactional
	public String updateReleaseEnvFlow(String releaseName,String blueprintInsId, String flowId) {
		Map<String,Object> params = new HashMap<>();
		params.put("blueprintInsId", blueprintInsId);
		params.put("cdFlowId", flowId);
		int count=releaseManageDao.updateReleaseEnvFlow(params);
		Map<String,Object> result=new HashMap<>();
		if(count>0){
			result.put("result", true);
			result.put("message", "更新蓝图过程成功！");
		}else{
			result.put("result", false);
			result.put("message", "更新蓝图过程失败！");
		}
		return JSON.toJSONString(result);
	}

	@Override
	@Transactional
	public String deleteReleaseApp(String releaseId, String resourceId) {
		Map<String,Object> params = new HashMap<>();
		Map<String,Object> result = new HashMap<>();
		params.put("releaseId", releaseId);
		params.put("resourceId", resourceId);
		int count =releaseManageDao.deleteReleaseApps(params);
		if(count>0){
			deleteReleaseStageEnv(releaseId, resourceId);
			result.put("result", true);
			result.put("message", "删除应用成功！");
		}else{
			result.put("result", false);
			result.put("message", "删除应用失败！");
		}
		return JSON.toJSONString(result);
	}

	public Map<String,Object> deleteBlueprintIns(List stageList){
		List<String> instanceIds= new ArrayList<>();
		for(int i=0;i<stageList.size();i++){
			String blueInsId = (String)((Map)stageList.get(i)).get("blueInsId");
			if(!JudgeUtil.isEmpty(blueInsId)){
				instanceIds.add(blueInsId);
			}
		}
		if(!instanceIds.isEmpty()){
			Map<String,Object> params = new HashMap<>();
			params.put("instanceIds", instanceIds);
			List ids =blueprintService.findBlueInstanceIds(params);
			if(ids.size()>0){
				for(int i=0;i<ids.size();i++){
					blueprintService.delBlueInstance(Integer.valueOf(ids.get(i)+""));
				}
			}
		}
		return null;
	}
	@Override
	public String listReleaseApps(String releaseName) {
		//获取发布的基本信息
		Map releaseInfo =releaseManageDao.findReleaseByName(releaseName);
		String releaseId = (String)releaseInfo.get("id");
		//获取发布关联的应用信息
		List<Map<String,Object>> appList =releaseManageDao.findReleaseAppsById(releaseId);
		return JSON.toJSONString(appList);
	}

	@Override
	public String getReleaseStageEnvs(String releaseName, String resourceName) {
		Map<String,Object> params = new HashMap<>();
		Map<String,Object> releaseInfo = releaseManageDao.findReleaseByName(releaseName);
		Map<String,Object> resourceInfo = releaseManageDao.findResourceInfo(resourceName);
		String releaseId = (String)releaseInfo.get("id");
		String resourceId=(String)resourceInfo.get("resourceId");
		params.put("releaseId", releaseId);
		params.put("resourceId", resourceId);
		List<Map<String,Object>> stageList = releaseManageDao.findReleaseStageEnvs(params);
		stageList=sort(stageList);
		for(int i=0;i<stageList.size();i++){
			Map stage = (Map)stageList.get(i);
			String blueInsId = (String)stage.get("blueInsId"); 
			String flowId = (String)stage.get("cdFlowId");
			Map envInfo = new HashMap<>();
			if(blueInsId !=null){
				Map blueIns = blueprintDao.getBlueprintInstance(blueInsId);
				if(blueIns !=null){
					envInfo.put("blueInstanceId", blueInsId);
					envInfo.put("blueInstanceName", (String)blueIns.get("INSTANCE_NAME"));
				}
			}
			if(flowId !=null){
				Map flowInfo =bluePrintTypeDao.getBlueprintTypeById(flowId);
				if(flowInfo !=null){
					envInfo.put("flowId", flowId);
					envInfo.put("flowName", flowInfo.get("FLOW_NAME"));
				}
			}
			stage.put("envInfo", envInfo);
		}
		return JSON.toJSONString(stageList);
	}

	@Override
	public String saveReleaseStageEnv(String releaseName,String resourceName,String blueprintInsName, String phaseId, String blueprintId, String cdFlowId,
			String resPoolConfig,String userId) {
		Map<String,Object> result = new HashMap<>();
	try {
		//校验蓝图实例名唯一性
		List<String> blueInstanceNames = blueprintService.getBlueprints();
		if(blueInstanceNames.contains(releaseName)){
			return MessageHelper.wrap("result",false,"message","蓝图实例名称不可重复");
		}
		Map<String,Object> resPoolConfigMap = JSON.parseObject(resPoolConfig, Map.class);
		//更新info
		Map<String,Object> blueprint = blueprintdao.getBlueprintById(blueprintId);
		JSONObject info = JSON.parseObject("" + blueprint.get("INFO"));
		JSONArray nodeData = info.getJSONArray("nodeDataArray");
		for(int j = 0; j < nodeData.size(); j++){
			JSONObject node = nodeData.getJSONObject(j);
			if("resource".equals(node.getString("eleType"))){
				String key=node.get("key")+"";
				Map<String,Object> map = (Map<String,Object>)resPoolConfigMap.get(key);
				String clusterId=(String)map.get("clusterId");
				List<String> nodeList = (List<String>) map.get("nodeIds");
				int ins = nodeList.size();
				Map<String,Object> params = new HashMap<>();
				params.put("nodeIds", nodeList);
				List nodeIps =releaseManageDao.findNodeIps(params);
				String nodes = JSON.toJSONString(nodeIps);
				node.put("ins", ins);
				node.put("cluster_id", clusterId);
				node.put("nodes", nodes);
				node.put("label", getLabel());
			}
		}
		String blueprint_info=JSON.toJSONString(info,true);
		blueprint_info = BlueprintRestService.formatUniqueLabel(blueprint_info, blueprintInsName);
		String bluePrintInsId = UUIDGenerator.getUUID();
		BluePrint bp = JSON.parseObject(blueprint_info, BluePrint.class);
		bp.setBluePrintId(bluePrintInsId);
		//保存蓝图实例
		Map<String,String> param = new HashMap<>();
		param.put("blue_instance_id", bluePrintInsId);
		param.put("blue_instance_name", blueprintInsName);
		param.put("blue_instance_info", blueprint_info);
		param.put("blue_instance_desc", blueprintInsName);
		param.put("blueprint_template_id", blueprintId);
		param.put("user_id", userId);
		param.put("resource_pool_config", BlueprintRestService.getResourceInfo(blueprint_info));
		blueprintService.saveBlueprintInstance(param);
		
		//保存关联关系
		Map<String,Object> releaseInfo = releaseManageDao.findReleaseByName(releaseName);
		Map<String,Object> resourceInfo = releaseManageDao.findResourceInfo(resourceName);
		String releaseId = (String)releaseInfo.get("id");
		String resourceId=(String)resourceInfo.get("resourceId");
		Map<String,Object> envParams = new HashMap<>();
		envParams.put("releaseId", releaseId);
		envParams.put("resourceId", resourceId);
		envParams.put("phaseId", phaseId);
//		envParams.put("nodeIds", nodeIds);
//		envParams.put("clusterId", clusterId);
		envParams.put("cdFlowId", cdFlowId);
		envParams.put("blueprintInsId", bluePrintInsId);
		envParams.put("blueprintId", blueprintId);
			releaseManageDao.updateReleaseEnv(envParams);
			result.put("result", true);
			result.put("message", "关联环境成功！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.put("result", false);
			result.put("message", "关联环境失败！");
		}
		return JSON.toJSONString(result);
	}
	public Map<String,String> getLabel(){
		try {
			Thread.sleep(1);//睡1毫秒，为了使value唯一
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,String> labelMap = new HashMap<>();
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMM");
		Calendar calendar = Calendar.getInstance();
		String yearM=format1.format(calendar.getTime());
		String key="compresmap";
		String value=yearM+calendar.get(Calendar.DAY_OF_MONTH)+
				calendar.get(Calendar.HOUR_OF_DAY)+calendar.get(Calendar.MINUTE)+calendar.get(Calendar.SECOND)
				+calendar.get(Calendar.MILLISECOND);
		labelMap.put("key","compresmap");
		labelMap.put("value",value);
		return labelMap;
	}
	private String getResourceInfo(String blueprintInfo) {
		JSONObject info = JSON.parseObject(blueprintInfo);
		List<Map<String, Object>> nodes = (List<Map<String, Object>>) info.get("nodeDataArray");
		Map<String, Object> resource = new HashMap<>();
		for (Map<String, Object> node : nodes) {
			if ("resource".equals(node.get("eleType"))) {
				resource.put(node.get("key") + "", node);
			}
		}
		return JSON.toJSONString(resource);
	}

	@Override
	public String relieveReleaseStageEnv(String releaseName, String resourceName, String phaseId) {
		Map<String,Object> result = new HashMap<>();
		Map<String,Object> params = new HashMap<>();
		Map<String,Object> releaseInfo = releaseManageDao.findReleaseByName(releaseName);
		Map<String,Object> resourceInfo = releaseManageDao.findResourceInfo(resourceName);
		String releaseId = (String)releaseInfo.get("id");
		String resourceId=(String)resourceInfo.get("resourceId");
		params.put("releaseId", releaseId);
		params.put("resourceId", resourceId);
		params.put("phaseId", phaseId);
		List<Map<String,Object>> stageList = releaseManageDao.findReleaseStageEnvs(params);
		if(stageList !=null && stageList.size()>0){
			//删除蓝图实例
			deleteBlueprintIns(stageList);
			//更新stageEnv
			Map<String,Object> envParams = new HashMap<>();
			envParams.put("releaseId", releaseId);
			envParams.put("resourceId", resourceId);
			envParams.put("phaseId", phaseId);
			envParams.put("nodeIds", null);
			envParams.put("clusterId", null);
			envParams.put("cdFlowId", null);
			envParams.put("blueprintInsId", null);
			envParams.put("blueprintId", null);
			releaseManageDao.updateReleaseEnv(envParams);
		}
		result.put("result", true);
		result.put("message", "应用环境解绑成功!");
		return JSON.toJSONString(result);
	}

	@Override
	public String deleteReleaseStage(String stageId) {
		Map<String,Object> result = new HashMap<>();
		try {
			Map<String,Object> params = new HashMap<>();
			params.put("phaseId", stageId);
			List<Map<String,Object>> stageList = releaseManageDao.findReleaseStageEnvs(params);
			if(stageList !=null && stageList.size()>0){
				//删除蓝图实例
				deleteBlueprintIns(stageList);
				releaseManageDao.deleteReleaseStageEnvByStageId(stageId);
			}
			result.put("result", true);
			result.put("message", "发布阶段删除成功!");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("message", "发布阶段删除失败!");
		}
		return JSON.toJSONString(result);
	}

	@Override
	public String addReleaseStage(String stageId, String lifecycleId) {
		//初始化应用与生命周期阶段的关联关系
		Map<String,Object> result = new HashMap<>();
		List<Map<String,Object>> list=releaseManageDao.findReleaseIdsBylifecycleId(lifecycleId);
		if(list==null || list.isEmpty()){
			result.put("reslut", true);
		}else{
			List stageMaps = new ArrayList<>();
			for(Map<String,Object> map:list){
				String releaseId=(String)map.get("id");
				String resourceId=(String)map.get("resourceId");
				Map<String,Object> stageEnvMap = new HashMap<>();
				stageEnvMap.put("id", UUID.randomUUID().toString());
				stageEnvMap.put("releaseId", releaseId);
				stageEnvMap.put("resourceId", resourceId);
				stageEnvMap.put("lifecycleStageId", stageId);
				stageMaps.add(stageEnvMap);
			}
			Map<String,Object> params = new HashMap<>();
			params.put("stageMaps", stageMaps);
			int addCount =releaseManageDao.addReleaseStageEnvs(params);
			if(addCount >0){
				result.put("result", true);
				result.put("message", "发布关联阶段成功！");
			}else{
				result.put("result", false);
				result.put("message", "发布关联阶段失败！");
			}
		}
		return JSON.toJSONString(result);
	}
}
