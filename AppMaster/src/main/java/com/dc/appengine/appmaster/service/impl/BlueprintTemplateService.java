package com.dc.appengine.appmaster.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.dao.IBlueprintTemplateDao;
import com.dc.appengine.appmaster.dao.IResourceDao;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.BluePrintType;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.IBlueprintTemplateService;
import com.dc.appengine.appmaster.service.IResourceService;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.RequestClient;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dc.appengine.appmaster.utils.ZipUtil;

@Service("blueprintTemplateService")
public class BlueprintTemplateService implements IBlueprintTemplateService{

	@Autowired
	@Qualifier("blueprintTemplateDao")
	private IBlueprintTemplateDao blueprintTemplatedao;
	
	@Value(value = "${flowServerUrl}")
	String flowServerUrl;
	
	@Autowired
	@Qualifier("blueprintService")
	private IBlueprintService blueprintService;
	
	@Autowired
	@Qualifier("resourceDao")
	private IResourceDao resourceDao;
	
	@Resource
	IAudit auditService;
	
	@Resource
	IResourceService resourceService;
	
	@Override
	public Page listBlueprintTemplates(Map<String, Object> condition, int pageNum,
			int pageSize) {
		return blueprintTemplatedao.listBlueprintTemplates(condition, pageNum, pageSize);
	}
	
	@Override
	public int saveBlueprintTemplate(Map<String, String> param,String op) {
		if("add".equalsIgnoreCase(op)){
			return blueprintTemplatedao.saveBlueprintTemplate(param);
		}else{
			return blueprintTemplatedao.updateBlueprintTemplate(param);
		}
	}

	@Transactional
	@Override
	public String delBlueprintTemplate(String blueprint_id) {
		//检测蓝图模板下是否存在实例
		int countBlueprintIns = blueprintTemplatedao.countBlueprintInsBytemplateId(blueprint_id);
		if(countBlueprintIns>0){
			return MessageHelper.wrap("result",false,"message","该蓝图模板下存在蓝图实例！");
		}
		//删除蓝图模板
		blueprintTemplatedao.delBlueprintTemplate(blueprint_id);
		//删除蓝图模板流程 根据blueprint_id查找flowid。循环调用工作流接口删除流程
		List<Map<String, Object>> blueprintTemplateFlows = blueprintTemplatedao.getBlueprintTemplateFlows(blueprint_id);
		for(Map<String,Object> map : blueprintTemplateFlows){
			boolean delSuccess = removeFromFrame(String.valueOf(map.get("FLOW_ID")));
			if(delSuccess){
				blueprintTemplatedao.delBlueprintTemplateFlowByCdFlowId(String.valueOf(map.get("ID")));
			}else{
				return MessageHelper.wrap("result",false,"message","删除蓝图模版失败");
			}
		}
		return MessageHelper.wrap("result",true,"message","删除蓝图模版成功");
	}
	
	@Override
	public boolean checkBlueprintFlowUnique(String bpInstanceId, String flowName) {
		List<Map<String,Object>> flows = blueprintTemplatedao.listBlueprintFlows(bpInstanceId);
		for(Map<String,Object> flow:flows){
			if(flow.get("FLOW_NAME").equals(flowName)){
				return false;
			}
		}
		return true;
	}

	// 调用工作流的接口删除蓝图模板流程
	private boolean removeFromFrame(String flowId){
		boolean result = true;
		try {
			//RestTemplate restUtil = new RestTemplate();
			RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("pdId", flowId);
			map.add("includeInstance", "true");
			//解决传输过程中中文乱码
			HttpHeaders headers = new HttpHeaders();
			MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
			headers.setContentType(type);
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(map,
					headers);
			restUtil.postForObject(flowServerUrl + "/WFService/deleteDefinitionByPdId.wf", requestEntity,
					String.class);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	@Override
	public String addBlueprintTemplateFlow(String blueprintId, String flowName,String flowInfo,String flowInfoGroup,String appName) {
		try{
			checkCdFlowId(flowInfo);
			saveBluePrintFlows(flowName,flowInfo,blueprintId,flowInfoGroup,appName);
		}catch(Exception e){
			e.printStackTrace();
			return MessageHelper.wrap("result",false,"message","保存蓝图流程失败。 reason[" + e.getMessage() + "]");
		}
		return MessageHelper.wrap("result",true,"message","保存蓝图流程成功");
	}

	private void checkCdFlowId(String flowInfo) throws Exception {
		JSONObject json = JSON.parseObject(flowInfo);
		JSONArray nodes = json.getJSONArray("nodeDataArray");
		for(int i = 0; i < nodes.size(); i++){
			JSONObject node = nodes.getJSONObject(i);
			int flowcontroltype = node.getIntValue("flowcontroltype");
			//组件子流程
			if(flowcontroltype == 0){
				//组件子流程在master的cdFlowId
				String component = node.getString("text");
				String cdFlowId = node.getString("cdFlowId");
				Map<String, Object> flowDetail = resourceDao.getNewFlowDetailByFlowId(cdFlowId);
				if(flowDetail ==null){
					throw new Exception("组件[" + component + "]已不存在!");
				}
				if(cdFlowId == null || "".equals(cdFlowId)){
					throw new Exception("组件[" + component + "]的[操作流程]未配置!");
				}
			}
		}
	}

	@Override
	public String updateBlueprintTemplateFlow(String flowInfoGroup,String flowInfo, String cdFlowId) {
		try {
			checkCdFlowId(flowInfo);
		} catch (Exception e) {
			return MessageHelper.wrap("result",false,"message","流程校验失败。 reason[" + e.getMessage() + "]");
		}
		Map<String,Object> old = blueprintTemplatedao.getBlueprintTemplateFlowByCdFlowId(cdFlowId);
		String flowId = String.valueOf(old.get("FLOW_ID"));
		String bluePrintInsId = old.get("BLUEPRINT_INSTANCE_ID").toString();
		String flowType = old.get("FLOW_TYPE").toString();
		Map<String,Object> group = JSON.parseObject(flowInfoGroup, new TypeReference<Map<String,Object>>(){});
		group.put("bluePrintId", bluePrintInsId);
		group.put("type", flowType);
		group.put("issub", false);
		Map<String,Object> flow = JSON.parseObject(flowInfo, new TypeReference<Map<String,Object>>(){});
		flow.put("bluePrintId", bluePrintInsId);
		flow.put("type", flowType);
		flow.put("issub", false);
		flow.put("workFlowId", flowId);
		String info = blueprintService.cdflowToSmartflow(JSON.toJSONString(flow));
		//如果info==flowId，说明工作流有此flowId，模型转换flowId不变
		//如果info!=flowId，说明工作流无此流程id记录、模型转换新建了个流程模板，此时master更新为工作流返回的最新流程id
		if(info == null){
			return MessageHelper.wrap("result",false,"message","流程转换失败");
		}else{
			Map<String,Object> params = new HashMap<>();
			params.put("blueprintInstanceId", bluePrintInsId);
			params.put("flowInfo", JSON.toJSONString(flow));
			params.put("flowInfoGroup", JSON.toJSONString(group));
			params.put("flowId", info);
			params.put("cdFlowId", cdFlowId);
			blueprintTemplatedao.updateBlueprintTemplateFlowByCdFlowId(params);
			return MessageHelper.wrap("result",true,"message","流程更新成功");
		}
	}
	
	private void saveBluePrintFlows(String flowName,String flowInfo,String bluePrintId,String flowInfoGroup,String appName) {
		Map<String,Object> group = JSON.parseObject(flowInfoGroup, new TypeReference<Map<String,Object>>(){});
		group.put("bluePrintId", bluePrintId);
		group.put("type", flowName);
		group.put("issub", false);
		Map<String,Object> flow = JSON.parseObject(flowInfo, new TypeReference<Map<String,Object>>(){});
		flow.put("bluePrintId", bluePrintId);
		flow.put("type", flowName);
		flow.put("issub", false);
		String info = blueprintService.cdflowToSmartflow(JSON.toJSONString(flow));
		if(info == null){
			throw new RuntimeException("保存"+flowName+"失败");
		}else{
			BluePrintType bluePrintType = new BluePrintType();
			bluePrintType.setBlueprint_id(bluePrintId);
			bluePrintType.setFlow_id(Long.valueOf(info));
			bluePrintType.setFlow_name(flowName);
			bluePrintType.setFlow_type(flowName);
			bluePrintType.setFlow_info(JSON.toJSONString(flow));
			bluePrintType.setFlow_info_group(JSON.toJSONString(group));
			bluePrintType.setApp_name(appName);
			blueprintTemplatedao.saveBlueprintTemplateFlow(bluePrintType);
		}
	}
	
	@Override
	public String listBlueprintTemplateFlow(String blueprintId,String sortName,String sortOrder) {
		List<Map<String,Object>> list = blueprintTemplatedao.listBlueprintFlows(blueprintId);
		SortUtil.sort(list, SortUtil.getColunmName("bluePrintFlow", sortName), sortOrder);
		List<Map<String,Object>> result = new ArrayList<>();
		for(Map<String,Object> flow:list){
			Map<String,Object> m = new HashMap<>();
			m.put("flowName", flow.get("FLOW_NAME"));
			m.put("flowId", flow.get("FLOW_ID"));
			m.put("cdFlowId", flow.get("ID"));
			result.add(m);
		}
		return JSON.toJSONString(result);
	}


	@Override
	public boolean delBlueprintTemplateFlow(String cdFlowId, String blueprintId, String flowName) {//删除蓝图模板流程
		Map<String,String> params = new HashMap<String,String>();
		params.put("cdFlowId", cdFlowId);
		params.put("blueprintId", blueprintId);
		params.put("flowName", flowName);
		Map<String,Object> old = blueprintTemplatedao.getBlueprintTemplateFlowByCdFlowId(cdFlowId,params);
		if(old!=null && !old.isEmpty()){
			String blueprint_id = old.get("BLUEPRINT_INSTANCE_ID").toString();
			//检测蓝图模板下是否存在实例
			int countBlueprintIns = blueprintTemplatedao.countBlueprintInsBytemplateId(blueprint_id);
			if(countBlueprintIns>0){
				return false;
			}
			String flowId = String.valueOf(old.get("FLOW_ID"));
			boolean delSuccess = removeFromFrame(flowId);
			if(delSuccess){
//				blueprintTemplatedao.delBlueprintTemplateFlowByCdFlowId(cdFlowId);
				blueprintTemplatedao.delBlueprintTemplateFlowByCdFlowId(cdFlowId,params);
				return true;
			}
			return false;
		}
		return true;
	}
	
	@Override
	public boolean delBlueprintTemplateFlow(String cdFlowId) {
		Map<String,Object> old = blueprintTemplatedao.getBlueprintTemplateFlowByCdFlowId(cdFlowId);
		if(old!=null && !old.isEmpty()){
			String blueprint_id = old.get("BLUEPRINT_INSTANCE_ID").toString();
			//检测蓝图模板下是否存在实例
			int countBlueprintIns = blueprintTemplatedao.countBlueprintInsBytemplateId(blueprint_id);
			if(countBlueprintIns>0){
				return false;
			}
			String flowId = String.valueOf(old.get("FLOW_ID"));
			boolean delSuccess = removeFromFrame(flowId);
			if(delSuccess){
//				blueprintTemplatedao.delBlueprintTemplateFlowByCdFlowId(cdFlowId);
				blueprintTemplatedao.delBlueprintTemplateFlowByCdFlowId(cdFlowId);
				return true;
			}
			return false;
		}
		return true;
	}
	
	@Override
	public Map<String, Object> getBlueprintTemplateFlowInfo(String cdFlowId) {
		return blueprintTemplatedao.getBlueprintTemplateFlowByCdFlowId(cdFlowId);
	}

	@Override
	public String exportBlueprint(String ids) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String now = format.format(new Date());
		String absPath = File.separator + "ex_bpTmp" + File.separator + now;
		File folder = new File(absPath);
		folder.mkdirs();
		
		//1.蓝图描述
		List<Map<String,Object>> paramList = JSONObject.parseObject(ids, List.class);
		for(Map<String,Object> paramMap : paramList){//遍历蓝图
			List<String> idList = new ArrayList<String>();
			String blueprintId = ""+paramMap.get("blueprintId");
			idList.add(blueprintId);
			List<Map<String,Object>> return_blueprint = blueprintTemplatedao.exportBlueprint(idList);
			try {
				for(Map<String,Object> bpMap : return_blueprint){
					String blueprintName = (String) bpMap.get("NAME");
					String blueprint_id = (String) bpMap.get("ID");

					File blueprintParent = new File(absPath + File.separator + blueprintName);
					blueprintParent.mkdirs();
					
					//组件 工件
					Set<String> appSet = this.getAppNameByBlueprintName(""+bpMap.get("NAME"));
					for(String appName : appSet){
						String resourceId = resourceService.getResourceIdByName(appName);
						List<String> resourceIdList = new ArrayList<String>();
						resourceIdList.add(resourceId);
						//1.sv_resource sv_package
						resourceService.zipResourceAndPackage(resourceIdList,blueprintParent,""+paramMap.get("exportPackageId"));
					}
					
					//2.蓝图流程
					List<Map<String, Object>> blueprintTemplateFlows = blueprintTemplatedao.getBlueprintTemplateFlows(blueprint_id);
					for(Map<String, Object> flowMap : blueprintTemplateFlows){
						String flowName = (String) flowMap.get("FLOW_NAME");
//					flowMap.put("blueprintName", blueprintName);
						String flowJson = JSON.toJSONString(flowMap, true);
						File f = new File(blueprintParent.getPath() + File.separator +blueprintName+"."+ flowName + ".blueprintFlow.json");
						FileOutputStream fos = new FileOutputStream(f);
						IOUtils.write(flowJson, fos);
					}
					
					String blueprintJson = JSON.toJSONString(bpMap, true);
					File f = new File(blueprintParent.getPath() + File.separator + blueprintName + ".template.json");
					FileOutputStream fos = new FileOutputStream(f);
					IOUtils.write(blueprintJson, fos);
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//3 打包zip
		String zipPath = folder.getParent() + File.separator + "blueprint"+now+".zip";
		ZipUtil.doCompress(folder, new File(zipPath));
//		ZipUtil.createZipFile(folder.getPath(), folder.getParent(), "blueprint"+now);
		return MessageHelper.wrap("result",true,"message",zipPath);
	}

	@Transactional
	@Override
	public String importBlueprint(String template,String blueprintFlow,String resource,String flow,String version,String ftl,String packages,String zip,String userId) throws Exception {
		
		resourceService.importResources(resource, flow, version, ftl, packages, zip, userId);//导入组件和工件
		
		List<Map<String,Object>> templateList = JSON.parseObject(template, new TypeReference<List<Map<String,Object>>>(){});
		for(Map<String,Object> templates : templateList){
			for ( Entry<String,Object> entry: templates.entrySet()) {
//				String temFileName = entry.getKey();
				Map<String,String> templateMap = ( Map<String,String> )entry.getValue();
//				String templateName = temFileName.substring(0, temFileName.lastIndexOf("template")-1);
//				boolean isNewBluePrint = false;
				String templateId = templateMap.get("ID");
				String templateName = templateMap.get("NAME");
//				String newTemplateId = "";
//				if (null != templateId && !templateId.equals("")) {
//					blueprintTemplatedao.delBlueprintTemplate4Import(templateId);
//					blueprintTemplatedao.saveBlueprintTemplate4Import(templateMap);
//					newTemplateId = templateId;
//				}else{
//					isNewBluePrint = true;
					Map<String, String> param= new HashMap<String, String>();
					param.put("blueprint_name", templateName);
					List<Map<String, Object>> existTemplateList = blueprintTemplatedao.getBlueprintTemplate(param);
					if(existTemplateList.isEmpty()){
//						newTemplateId = UUIDGenerator.getUUID();
//						templateMap.put("ID", newTemplateId);
//						templateMap.put("NAME", templateName);
						blueprintTemplatedao.saveBlueprintTemplate4Import(templateMap);
					}else{
						blueprintTemplatedao.delBlueprintTemplate4Import(templateId);
						blueprintTemplatedao.saveBlueprintTemplate4Import(templateMap);
//						return MessageHelper.wrap("result",false,"message","存在同名的蓝图模版！");
					}
//				}
				//保存蓝图流程
					List<Map<String,Object>> blueprintFlowList = JSON.parseObject(blueprintFlow, new TypeReference<List<Map<String,Object>>>(){});
				for(Map<String,Object> flows : blueprintFlowList){
					for ( Entry<String,Object> entry1: flows.entrySet()) {
						String flowFileName = entry1.getKey();
						Map<String,String> flowMap = ( Map<String,String> )entry1.getValue();
						
						String blueprintName = flowFileName.substring(0, flowFileName.indexOf(File.separator));
						String cdFlowId = flowMap.get("ID");
//						String blueprintId = newTemplateId;
						String blueprintId = templateId;
						String flowName = flowMap.get("FLOW_NAME");
						String flowInfo = flowMap.get("FLOW_INFO");
						String flowInfoGroup = flowMap.get("FLOW_INFO_GROUP");
						String appName = flowMap.get("APP_NAME");
//						String blueprintName = flowMap.get("blueprintName");
						if (blueprintName.equals(templateName)) {
							boolean delResult = delBlueprintTemplateFlow(cdFlowId,blueprintId,flowName);
//							if(isNewBluePrint || delResult){
							if(delResult){
								String result = addBlueprintTemplateFlow(blueprintId, flowName, flowInfo, flowInfoGroup,appName);
								Map<String,Object> msg = JSONObject.parseObject(result, Map.class);
								if(!Boolean.valueOf(msg.get("result").toString())){
									//==============添加审计start===================
									ThreadPool.service.execute(new Runnable(){
										@Override
										public void run(){
											auditService.save(new AuditEntity(userId, ResourceCode.BLUEPRINTTEMPLATE, templateName, ResourceCode.Operation.IMPORT, 0, "导入蓝图流程:"+flowName));
										}
									});
									//==============添加审计end=====================
									throw new RuntimeException((String)msg.get("message"));//spring boot 需抛出runtimeException才能回滚
//									return MessageHelper.wrap("result",false,"message",msg.get("message"));
								}else{
									//==============添加审计start===================
									ThreadPool.service.execute(new Runnable(){
										@Override
										public void run(){
											auditService.save(new AuditEntity(userId, ResourceCode.BLUEPRINTTEMPLATE, templateName, ResourceCode.Operation.IMPORT, 1, "导入蓝图流程:"+flowName));
										}
									});
									//==============添加审计end=====================
								}
							}
						}
					}
//					flowList.remove(flows);
				}
				//==============添加审计start===================
				ThreadPool.service.execute(new Runnable(){
					@Override
					public void run(){
						auditService.save(new AuditEntity(userId, ResourceCode.BLUEPRINTTEMPLATE, templateName, ResourceCode.Operation.IMPORT, 1, "导入蓝图模板:"+templateName));
					}
				});
				//==============添加审计end=====================
			}
		}
		return MessageHelper.wrap("result",true,"message","导入蓝图成功");
	}

	@Override
	public List<Map<String, Object>> getBlueprintTemplate(
			Map<String, String> param) {
		return blueprintTemplatedao.getBlueprintTemplate(param);
	}

	@Override
	public Map<String, Object> getFlowByFlowName(Map<String, Object> param) {
		return blueprintTemplatedao.getFlowByFlowName(param);
	}

	@Override
	public List<Map<String, Object>> listBlueprintTemplateByNameAndApp(Map<String, Object> params) {
		return blueprintTemplatedao.listBlueprintTemplateByNameAndApp(params);
	}

	@Override
	public void delBlueprintTemplate4Import(String ID) {
		blueprintTemplatedao.delBlueprintTemplate4Import(ID);		
	}

	@Override
	public int saveBlueprintTemplate4Import(Map<String, String> param) {
		return blueprintTemplatedao.saveBlueprintTemplate4Import(param);
	}

	@Override
	public List<Map<String, Object>> exportBlueprint(List tempIds) {
		return blueprintTemplatedao.exportBlueprint(tempIds);
	}

	@Override
	public List<Map<String, Object>> getBlueprintTemplateFlows(
			String blueprint_id) {
		return blueprintTemplatedao.getBlueprintTemplateFlows(blueprint_id);
	}

	@Override
	public Set<String> getAppNameByBlueprintName(String blueprint_name){
		Set<String> returnSet = new HashSet<String>();
		Map<String, String> param= new HashMap<String, String>();
		param.put("blueprint_name", blueprint_name);
		List<Map<String, Object>> templateList = this.getBlueprintTemplate(param);
		
		for(Map<String,Object> template : templateList){
			JSONObject info = JSON.parseObject("" + template.get("INFO"));
			JSONArray nodes = info.getJSONArray("nodeDataArray");
			for(int j = 0; j < nodes.size(); j++){
				JSONObject node = nodes.getJSONObject(j);
				if("component".equals(node.getString("eleType"))){
					returnSet.add(node.getString("nodeName"));
				}
			}
		}
		return returnSet;
	}
}
