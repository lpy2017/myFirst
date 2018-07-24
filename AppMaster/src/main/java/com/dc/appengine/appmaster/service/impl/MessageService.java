package com.dc.appengine.appmaster.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.dao.IApplicationDao;
import com.dc.appengine.appmaster.dao.IBlueprintDao;
import com.dc.appengine.appmaster.dao.IInstanceDao;
import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.entity.BluePrint;
import com.dc.appengine.appmaster.entity.Element;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.IInstanceService;
import com.dc.appengine.appmaster.service.IMessageService;
import com.dc.appengine.appmaster.service.IResourceService;
import com.dc.appengine.appmaster.utils.DesUtils;
import com.dc.appengine.appmaster.utils.PatternUtil;

@Service("messageService")
public class MessageService implements IMessageService {

	public static final String ENCRYPT_PREFIX = "encrypt[";
	public static final String ENCRYPT_SUFFIX = "]";

	public static  Pattern p1 = Pattern.compile(PatternUtil.P1);
	public static  Pattern p2 = Pattern.compile(PatternUtil.P2);
	public static  Pattern p3 = Pattern.compile(PatternUtil.P3);
	public static  Pattern p8 = Pattern.compile(PatternUtil.P8);
	public static  Pattern p9 = Pattern.compile(PatternUtil.P9);
	public static  Pattern p10 = Pattern.compile(PatternUtil.P10);
	public static  Pattern p11 = Pattern.compile(PatternUtil.P11);

	@Autowired
	@Qualifier("instanceService")
	IInstanceService instanceService;

	@Autowired
	@Qualifier("resourceService")
	IResourceService resourceService;

	@Autowired
	@Qualifier("configservice")
	ConfigsService configservice;

	@Autowired
	@Qualifier("blueprintService")
	IBlueprintService blueprintService;

	@Autowired
	@Qualifier("applicationDao")
	private IApplicationDao applicationDao;

	@Autowired
	@Qualifier("blueprintDao")
	private IBlueprintDao bluePrintdao;

	@Autowired
	@Qualifier("instanceDao")
	private IInstanceDao instancedao;

	@Override
	public List<Object> messages(List<String> instancelist,String currentOp) {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Object> messagesList = new ArrayList<>();
		for (String instanceId : instancelist) {
			Map<String, Object> tempMap = instanceService.findMessage(instanceId);
			Map<String, Object> resourceVersionDetail = resourceService
					.getResourceVersionDetail(tempMap.get("resourceVersionId").toString());			
			Map<String, Object> map = new HashMap<>();
			Map<String,String> flows = (Map<String,String>)resourceVersionDetail.get("flows");
			//当前操作是部署
			//String currentOp = "deploy";
			long flowId = -1;
			if(flows.containsKey(currentOp)){
				flowId = Long.valueOf(flows.get(currentOp).toString());
			}
			Map<String,Object> inMap = new HashMap<>();
			Map<String,Object> outMap = new HashMap<>();
			String componentName = tempMap.get("app_name").toString();
			map.put("instanceId", instanceId);
			map.put("operation", currentOp);
			map.put("nodeIp", tempMap.get("nodeIp"));
			//map.put("deployPath", instanceId);
			map.put("resouceUrl", resourceVersionDetail.get("url"));
			map.put("version", tempMap.get("resourceVersionId"));
			map.put("componentName", componentName);


			//进行动态替换
			String input = tempMap.get("componentInputTemp").toString();
			String output = tempMap.get("componentOutputTemp").toString();
			Map<String, Object>  inputMap = JSON.parseObject(input);  
			Map<String, Object>  outputMap = JSON.parseObject(output); 

			Map<String, Object>  tempMapConfig = new HashMap<>();
			tempMapConfig.putAll(inputMap);
			tempMapConfig.putAll(outputMap);

			inMap = dynamicReplace(input,instanceId,tempMapConfig);				
			outMap = dynamicReplace(output,instanceId,tempMapConfig);				

			Map<String, Object>  configMap = new HashMap<>();
			configMap.putAll(inMap);
			configMap.putAll(outMap);
			map.put("componentInput", inMap);
			map.put("componentOutput", outMap);

			Map<String,Object> subFlowInfo = blueprintService.getBlueprintTypeByFlowId(flowId);
			JSONObject  flowInfo = JSONObject.parseObject((String) subFlowInfo.get("FLOW_INFO"));  
			String nodeArrayString = flowInfo.getString("nodeDataArray");
			JSONArray nodeArray = JSONArray.parseArray(nodeArrayString);
			if(nodeArray.size()>0){
				for(int i=0;i<nodeArray.size();i++){
					JSONObject node = ((JSONArray) nodeArray).getJSONObject(i);  
					if(node.get("flowcontroltype").toString().equals("9")){
						Map<String,String> params = (Map<String, String>) node.get("params");
						if(params.containsKey("CMD")){
							String cmd = params.get("CMD");
							try {
								DesUtils des = new DesUtils();
								String encryptCmd = des.encrypt(cmd);
								params.put("CMD", encryptCmd);
							} catch (Exception e) {
								System.out.println(e.getMessage());
								params.put("CMD", "");
							}
						}
						map.put((String) node.get("pluginName")+"_"+node.get("key").toString(),params);
					}
				}
			}

			//稍后替换为从组件子流程中读取
			Map<String,Object> pluginInputMap = new HashMap<>();
			map.put("pluginInput", pluginInputMap);

			Map<String,String> templateMap = (Map<String, String>)resourceVersionDetail.get("templates");
			Map<String, String> templateMap1 = templateReplace(templateMap,instanceId,configMap);

			map.put("deployPath", inMap.get("deployPath"));

			map.put("configTemplate", templateMap1);
			messagesList.add(map);
		}
		return messagesList;
	}

	public Map<String, String>  templateReplace(Map<String, String> templateMap,String instanceId,Map<String, Object>  tempMapConfig){
		Map<String, String> templateMap1 = new HashMap<String, String>();
		String template = JSONObject.toJSONString(templateMap);
		Pattern p = Pattern.compile(PatternUtil.P1);
		Matcher mat = p.matcher(template);
		List<String> group=new ArrayList<String>();
		if (template.contains("${instanceId?if_exists}")) {
			template = template.replace("${instanceId?if_exists}", instanceId);
		}else if (mat.find()) {
			group.add(mat.group());
			if (group.size()>0) {
				for(int i=0;i<group.size();i++){
					Matcher mat1Temp = p.matcher(group.get(i));
					if (mat1Temp.find()) {
						String configKey = mat1Temp.group(1);
						String configValue = (String) tempMapConfig.get(configKey);
						template = template.replace(group.get(i),configValue);
					}
				}
			}
		}
		templateMap1 = (Map<String, String>) JSON.parse(template);
		return templateMap1;

	}
	public Map<String, Object> pluginVaribleReplace(Map<String, Object> params, Map<String, Object> ConfigMap) {
		Map<String, Object> bpInsConfigValue = JSON.parseObject((String)ConfigMap.get("blueprintConfig"));
		for (Map.Entry<String,Object> entry : params.entrySet()) {
			String key = entry.getKey();
			String value = (String) entry.getValue();
			Matcher mat2 = p2.matcher(value);
			List<String> matgroup2=new ArrayList<String>();
			while (mat2.find()) {
				matgroup2.add(mat2.group());
			}
			if (matgroup2.size()>0) {
				for(int i=0;i<matgroup2.size();i++){
					Matcher mat2Temp = p2.matcher(matgroup2.get(i));
					if (mat2Temp.find()) {
						String bpConfigKey = mat2Temp.group(2);
						String actualValue = (String) bpInsConfigValue.get(bpConfigKey);
						value = value.replace(matgroup2.get(i),actualValue);
						params.put(key,value);
					}
				}
			}
		}
		return params;
	}


	public Map<String, Object> varibleReplace(String type,Map<String,Object> map,Map<String,Object> snapshotDetail){
		String blueprintInstanceId = (String) map.get("blueprintId");
		Map<String, Object> bpInsConfigValue = JSON.parseObject((String)map.get("blueprintConfig"));
		Map<String, Object>  configMap = null;
		if(map.get(type) instanceof Map){
			configMap = (Map<String, Object>) map.get(type);
			replacePluginConfig(configMap,bpInsConfigValue);
		}
		else{
			configMap = JSON.parseObject((String)map.get(type));
			//把组消息的hostIp作为当前组件的配置参数
			configMap.put("hostIp", map.get("hostIp"));
			Map<String, Application> appConfig = new HashMap<String,Application>();
			//非快照，组件配置从ma_application获取
			if(snapshotDetail == null){
				List<Application> apps = applicationDao.getBlueprintComponents(blueprintInstanceId);
				for (Application application : apps) {
					//appConfig.put(application.getComponentName()+"_"+application.getKey(), application);
					appConfig.put(application.getNodeDisplay(), application);
				}
			}
			//快照，组件配置从快照中获取
			else{
				Iterator<Entry<String, Object>> iter = snapshotDetail.entrySet().iterator();
				while(iter.hasNext()){
					Entry<String, Object> entry = iter.next();
					String key = entry.getKey();
					if(!"KEY_VALUE".equals(key)){
						Application appSnapshot = new Application();
						String value = "" + entry.getValue();
						Map<String, Object> appJson = JSON.parseObject(value, new TypeReference<Map<String, Object>>(){});
						appSnapshot.setId((Integer)appJson.get("APP_ID"));
						appSnapshot.setCurrentInput(JSON.toJSONString(appJson.get("CURRENT_INPUT")));
						appSnapshot.setCurrentOutput(JSON.toJSONString(appJson.get("CURRENT_OUTPUT")));
						appSnapshot.setTargetInput(JSON.toJSONString(appJson.get("TARGET_INPUT")));
						appSnapshot.setTargetOutput(JSON.toJSONString(appJson.get("TARGET_OUTPUT")));
						//appConfig.put(appJson.get("COMPONENT_NAME") + "_" + appJson.get("KEY"), appSnapshot);
						appConfig.put("" + appJson.get("NODE_NAME"), appSnapshot);
					}
				}
			}
			Map<String, List<Object>> configMapKeyTraces = new HashMap<>();
			Set<String> keys = configMap.keySet();
			for(String key : keys){
				List<Object> list = new ArrayList<>();
				configMapKeyTraces.put(key, list);
			}
			replaceConfig(configMap, bpInsConfigValue, appConfig, configMapKeyTraces);
		}
		map.put(type,configMap);
		return map;

	}
	
	public Map<String,Object> replacePluginConfig(Map<String,Object> configMap,Map<String, Object> bpInsConfigValue){
		for(Map.Entry<String,Object> entry:configMap.entrySet()){
			String configKey = entry.getKey();
			String configValue = (String) entry.getValue();
			Matcher mat11 = p11.matcher(configValue);
			List<String> matgroup11=new ArrayList<String>();
			while (mat11.find()) {
				matgroup11.add(mat11.group());
			}
			if (matgroup11.size()>0) {
				for(int i=0;i<matgroup11.size();i++){
					Matcher mat11Temp = p11.matcher(matgroup11.get(i));
					if (mat11Temp.find()) {
						String bpConfigKey = mat11Temp.group(2);
						String actualValue = (String) bpInsConfigValue.get(bpConfigKey);
						if(actualValue != null){
							configValue = configValue.replace(matgroup11.get(i),actualValue);
							configMap.put(configKey,configValue);
						}
					}
				}
			}
		}
		return configMap;
	}

	public Map<String,Object> replaceConfig(Map<String,Object> configMap,Map<String, Object> bpInsConfigValue,
			Map<String, Application> appConfig, Map<String, List<Object>> configMapKeyTraces){
		boolean isContinue = replaceConfigDo(configMap, bpInsConfigValue, appConfig, configMapKeyTraces);
		if(isContinue){
			for(Map.Entry<String,Object> entry:configMap.entrySet()){
				String value = (String) entry.getValue();
				Matcher mat1 = p1.matcher(value);
				Matcher mat11 = p11.matcher(value);
				Matcher mat10 = p10.matcher(value);
				while ( mat1.find() || mat11.find() || mat10.find()) {
					replaceConfig(configMap, bpInsConfigValue,appConfig, configMapKeyTraces);
				}
			}
		}
		return configMap;
	}


	public boolean replaceConfigDo(Map<String,Object> configMap, Map<String, Object> bpInsConfigValue,
			Map<String, Application> appConfig, Map<String, List<Object>> configMapKeyTraces){
		boolean isSuccess = false;
		for(Map.Entry<String,Object> entry:configMap.entrySet()){
			String configKey = entry.getKey();
			String configValue = (String) entry.getValue();
			List<Object> list = configMapKeyTraces.get(configKey);
			if(list.contains(configValue)){
				//存在循环引用，停止替换
				isSuccess = (isSuccess || false);
				continue;
			}
			else{
				if(configValue.startsWith("$")){
					list.add(configValue);
				}
			}
			Matcher mat1 = p1.matcher(configValue);
			Matcher mat11 = p11.matcher(configValue);
			Matcher mat10 = p10.matcher(configValue);
			List<String> matgroup1=new ArrayList<String>();
			List<String> matgroup11=new ArrayList<String>();
			List<String> matgroup10=new ArrayList<String>();
			while (mat1.find()) {
				matgroup1.add(mat1.group());
			}
			if (matgroup1.size()>0) {
				for(int i=0;i<matgroup1.size();i++){
					Matcher mat1Temp = p1.matcher(matgroup1.get(i));
					if (mat1Temp.find()) {
						String bpConfigKey = mat1Temp.group(1);
						String actualValue = (String) configMap.get(bpConfigKey);
						if(actualValue != null){
							configValue = configValue.replace(matgroup1.get(i),actualValue);
							configMap.put(configKey,configValue);
							isSuccess = (isSuccess || true);
						}
					}
				}
			}
			while (mat11.find()) {
				matgroup11.add(mat11.group());
			}
			if (matgroup11.size()>0) {
				for(int i=0;i<matgroup11.size();i++){
					Matcher mat2Temp = p11.matcher(matgroup11.get(i));
					if (mat2Temp.find()) {
						String bpConfigKey = mat2Temp.group(2);
						String actualValue = (String) bpInsConfigValue.get(bpConfigKey);
						if(actualValue != null){
							configValue = configValue.replace(matgroup11.get(i),actualValue);
							configMap.put(configKey,configValue);
							isSuccess = (isSuccess || true);
						}
					}
				}
			}
			while(mat10.find()){
				matgroup10.add(mat10.group());
			}
			if(matgroup10.size()>0) {
				for(int i=0;i<matgroup10.size();i++){
					Matcher tempmat =p10.matcher(matgroup10.get(i));
					if (tempmat.find()) {
						//1.获取组件名和版本
						String componentName = tempmat.group(1);
						String version = tempmat.group(2);//current或者target
						String key = tempmat.group(3);
						Application app = (Application) appConfig.get(componentName);
						String	actualValue = getConfigValueNew(key,version,app);
						if(actualValue != null){
							configValue = configValue.replace(matgroup10.get(i),actualValue);
							configMap.put(configKey,configValue);
							isSuccess = (isSuccess || true);
						} 
					}
				}
			}
		}
		return isSuccess;
	}

	public String getConfigValueNew(String configKey,String version,Application app){
		String configValue = "";
		Map<String, Object> configMap = new HashMap<String,Object>();
		if (configKey.equals("hostIp")) {
			long appId =  app.getId();
			List<Map<String, Object>> instances= instancedao.listInstancesByAppId(appId);
			configValue =(String) instances.get(0).get("nodeIp");
			return configValue;
		}else{
			if ("current".equals(version)) {
				Map<String, Object>  currentInput = (Map<String, Object>) JSON.parse(app.getCurrentInput());
				Map<String, Object>  currentOutput = (Map<String, Object>) JSON.parse(app.getCurrentOutput());
				configMap.putAll(currentInput);
				configMap.putAll(currentOutput);
				configValue = (String)configMap.get(configKey);
			}else{
				Map<String, Object>  targetInput = (Map<String, Object>) JSON.parse(app.getTargetInput());
				Map<String, Object>  targetOutput = (Map<String, Object>) JSON.parse(app.getTargetOutput());
				configMap.putAll(targetInput);
				configMap.putAll(targetOutput);
				configValue = (String )configMap.get(configKey);

			}
			return configValue;
		}



	}

	public Map<String,Object> newDynamicReplace(String input,String instanceId,Map<String,Object> configMap){
		Map<String,Object> configMap1 =new HashMap<String, Object>();
		configMap1 = dynamicReplace(input, instanceId, configMap);
		for(Map.Entry<String,Object> entry:configMap1.entrySet()){
			String value = (String) entry.getValue();

			//			Matcher mat3 = p3.matcher(value);
			Matcher mat1 = p1.matcher(value);
			Matcher mat8 = p8.matcher(value);
			Matcher mat9 = p9.matcher(value);
			while (mat1.find() || mat8.find() || mat9.find()) {
				JSONObject jsonObject = (JSONObject)JSONObject.toJSON(configMap1);
				String mes =jsonObject.toString();
				return newDynamicReplace(mes, instanceId, configMap);
			}
		}
		return configMap1;
	}

	public Map<String, Object>  dynamicReplace(String input,String instanceId,Map<String, Object>  tempMapConfig){
		Map<String, Object> messageMap = transcomponentInput(input);
		for(Map.Entry<String,Object> entry:messageMap.entrySet()){
			String key = entry.getKey();
			String value = (String) entry.getValue();

			//${deployPath}
			List<String> matgroup1=new ArrayList<String>();
			Pattern p1 = Pattern.compile(PatternUtil.P1);
			Matcher mat1 = p1.matcher(value);
			while (mat1.find()) {
				matgroup1.add(mat1.group());
			}
			if (matgroup1.size()>0) {
				for(int i=0;i<matgroup1.size();i++){
					Matcher mat1Temp = p1.matcher(matgroup1.get(i));
					if (mat1Temp.find()) {
						String configKey = mat1Temp.group(1);
						String configValue = (String) tempMapConfig.get(configKey);
						value = value.replace(matgroup1.get(i),configValue);
						messageMap.put(key, value);
					}
				}
			}

			//${componentName#version#configKey}
			//${componentName#version#HOST_IP}
			List<String> matgroup8=new ArrayList<String>();
			Pattern p8 = Pattern.compile(PatternUtil.P8);
			Matcher mat = p8.matcher(value);
			while(mat.find()){
				matgroup8.add(mat.group());
			}
			if(matgroup8.size()>0) {
				for(int i=0;i<matgroup8.size();i++){
					Matcher tempmat =p8.matcher(matgroup8.get(i));
					if (tempmat.find()) {
						//1.获取组件名和版本
						String componentName = tempmat.group(1);
						String version = tempmat.group(2);
						String configKey = tempmat.group(3);
						//2.根据instanceId获取同一蓝图下的componentName的实例信息
						String resourceVersionId  = instanceService.findresVerId(componentName,version);
						List<Map<String, Object>> instances = new ArrayList<>();
						instances = instanceService.findInstances(instanceId,componentName,resourceVersionId);
						//Map<String, Object> instance = instanceService.findByInstanceId(instanceId);
						//instances.add(instance);
						//3.从第二步结果中获取配置
						String	configValue = getConfigValue(instances,configKey);
						//4.进行变量替换
						if(!(configValue == null || "".equals(configValue))){
							value = value.replace(matgroup8.get(i),configValue);
							messageMap.put(key, value);
						} 
					}
				}
			}

			//#{blueinstance#component#version#configKey}
			List<String> matgroup9=new ArrayList<String>();
			Pattern p9 = Pattern.compile(PatternUtil.P9);
			Matcher mat9 = p9.matcher(value);
			while(mat9.find()){
				matgroup9.add(mat9.group());
			}
			if (matgroup9.size()>0) {
				for(int i=0;i<matgroup9.size();i++){
					Matcher tempmat =p9.matcher(matgroup9.get(i));
					if (tempmat.find()) {
						//1.获取组件名和版本等信息
						String blueInstanceName = tempmat.group(1);
						String componentName = tempmat.group(2);
						String version = tempmat.group(3);
						String configKey = tempmat.group(4);
						//2.获取blueInstanceName蓝图下的componentName的实例信息
						String resourceVersionId  = instanceService.findresVerId(componentName,version);
						List<Map<String, Object>> instances = new ArrayList<>();
						instances = instanceService.findInstancesBybp(blueInstanceName,componentName,resourceVersionId);
						//3.从第二步结果中获取配置
						String	configValue = getConfigValue(instances,configKey);
						//4.进行变量替换
						if(!(configValue == null || "".equals(configValue))){
							value = value.replace(matgroup9.get(i),configValue);
							messageMap.put(key, value);
						} 
					}
				}
			}

		}
		return messageMap;
	}


	public String getConfigValue(List<Map<String, Object>> instances,String configKey){
		String configValue = null;
		if(instances.size() > 0){
			if (configKey.equals("HOST_IP")) {
				String componentName_instanceId =(String)instances.get(0).get("ID");
				Map<String, Object> tempMap = instanceService.findMessage(componentName_instanceId);
				configValue =(String) tempMap.get("nodeIp");
			}else{
				Map<String, String> configMap = new HashMap<String,String>();
				String inputConfig = (String)instances.get(0).get("COMPONENT_INPUT_TEMP");
				String outputConfig = (String)instances.get(0).get("COMPONENT_OUTPUT_TEMP");
				Map<String, Object>  inputMap = JSON.parseObject(inputConfig);  
				Map<String, Object>  outputMap = JSON.parseObject(outputConfig);  

				for (Map.Entry<String, Object> inputMapentry : inputMap.entrySet()) {
					String inputkey = inputMapentry.getKey();
					String inputvalue = (String) inputMapentry.getValue();
					configMap.put(inputkey, inputvalue);
				}
				for (Map.Entry<String, Object> outputMapentry : outputMap.entrySet()) {
					String ouptukey = outputMapentry.getKey();
					String ouptuvalue = (String) outputMapentry.getValue();
					configMap.put(ouptukey, ouptuvalue);
				}
				configValue = configMap.get(configKey);
			}
		}

		return configValue;

	}
	public Map<String, Object> transcomponentInput(String componentInput){
		Map<String, Object> messageMap = JSON.parseObject(componentInput,Map.class);
		/*Map<String,Object> messageMap = null;
		if(paramMap.get("message") instanceof String){
			messageMap =JSON.parseObject(paramMap.get("message").toString(), new TypeReference<Map<String, Object>>() {
			});
		}else{
			messageMap = (Map<String,Object>)paramMap.get("message");
		}*/

		return messageMap;
	}

	@Override
	public List<Object> messagesFromSnapShot(List<Map<String,Object>> instancelist, String currentOp) {
		List<Object> messagesList = new ArrayList<>();
		for (Map<String,Object> instance : instancelist) {
			String instanceId = instance.get("id").toString();
			String resVersionId = instance.get("resVersionId").toString();
			Map<String, Object> resourceVersionDetail = resourceService.getResourceVersionDetail(resVersionId);			
			Map<String, Object> map = new HashMap<>();
			Map<String,String> flows = (Map<String,String>)resourceVersionDetail.get("flows");
			//当前操作是部署
			//String currentOp = "deploy";
			long flowId = -1;
			if(flows.containsKey(currentOp)){
				flowId = Long.valueOf(flows.get(currentOp).toString());
			}
			if(flowId == -1){
				return messagesList;
			}
			Map<String, Object> tempMap = instanceService.findMessage(instanceId);
			Map<String,Object> inMap = new HashMap<>();
			Map<String,Object> outMap = new HashMap<>();
			String componentName = tempMap.get("app_name").toString();
			map.put("instanceId", instanceId);
			map.put("operation", currentOp);
			map.put("nodeIp", tempMap.get("nodeIp"));
			map.put("deployPath", instanceId);
			map.put("resouceUrl", resourceVersionDetail.get("url"));
			map.put("version", resourceVersionDetail.get("version"));
			map.put("componentName", componentName);


			//进行动态替换
			String input = instance.get("input").toString();;
			String output = instance.get("output").toString();;
			Map<String, Object>  inputMap = JSON.parseObject(input);  
			Map<String, Object>  outputMap = JSON.parseObject(output); 

			Map<String, Object>  configMap = new HashMap<>();
			configMap.putAll(inputMap);
			configMap.putAll(outputMap);
			map.put("componentInput", inputMap);
			map.put("componentOutput", outputMap);


			//获取空子流程ID
			long emptyFlowId = blueprintService.getEmptyFlowId();
			if(flowId != Long.valueOf(emptyFlowId)){
				Map<String,Object> subFlowInfo = blueprintService.getBlueprintTypeByFlowId(flowId);
				String flowInfo = (String) subFlowInfo.get("FLOW_INFO");
				BluePrint bp = JSON.parseObject(flowInfo, BluePrint.class);
				List<Element> elements = bp.getNodeDataArray();
				for(Element ele:elements){
					Map<String,String> params = ele.getParams();
					map.put(ele.getPluginName(),params);
				}
			}
			//稍后替换为从组件子流程中读取
			Map<String,Object> pluginInputMap = new HashMap<>();
			map.put("pluginInput", pluginInputMap);

			Map<String,String> templateMap = (Map<String, String>)resourceVersionDetail.get("templates");
			Map<String, String> templateMap1 = templateReplace(templateMap,instanceId,configMap);

			map.put("configTemplate", templateMap1);
			addParams(map, instance);
			messagesList.add(map);
		}
		return messagesList;
	}

	@Override
	public List<Object> messagesForCurrentInstances(List<Map<String, Object>> instancelist, String currentOp) {
		List<Object> messagesList = new ArrayList<>();
		for (Map<String, Object> instance : instancelist) {
			String instanceId= instance.get("id").toString();
			Map<String, Object> tempMap = instanceService.findMessage(instanceId);
			Map<String, Object> resourceVersionDetail = resourceService
					.getResourceVersionDetail(tempMap.get("resourceVersionId").toString());			
			Map<String, Object> map = new HashMap<>();
			Map<String,String> flows = (Map<String,String>)resourceVersionDetail.get("flows");
			//当前操作是部署
			//String currentOp = "deploy";
			long flowId = -1;
			if(flows.containsKey(currentOp)){
				flowId = Long.valueOf(flows.get(currentOp).toString());
			}
			if(flowId == -1){
				return messagesList;
			}
			Map<String,Object> inMap = new HashMap<>();
			Map<String,Object> outMap = new HashMap<>();
			String componentName = tempMap.get("app_name").toString();
			map.put("instanceId", instanceId);
			map.put("operation", currentOp);
			map.put("nodeIp", tempMap.get("nodeIp"));
			map.put("deployPath", instanceId);
			map.put("resouceUrl", resourceVersionDetail.get("url"));
			map.put("version", resourceVersionDetail.get("version"));
			map.put("componentName", componentName);


			//进行动态替换
			String input = tempMap.get("componentInputTemp").toString();
			String output = tempMap.get("componentOutputTemp").toString();
			Map<String, Object>  inputMap = JSON.parseObject(input);  
			Map<String, Object>  outputMap = JSON.parseObject(output); 

			Map<String, Object>  tempMapConfig = new HashMap<>();
			tempMapConfig.putAll(inputMap);
			tempMapConfig.putAll(outputMap);

			inMap = dynamicReplace(input,instanceId,tempMapConfig);				
			outMap = dynamicReplace(output,instanceId,tempMapConfig);				

			Map<String, Object>  configMap = new HashMap<>();
			configMap.putAll(inMap);
			configMap.putAll(outMap);
			map.put("componentInput", inMap);
			map.put("componentOutput", outMap);


			//获取空子流程ID
			long emptyFlowId = blueprintService.getEmptyFlowId();
			if(flowId != Long.valueOf(emptyFlowId)){
				Map<String,Object> subFlowInfo = blueprintService.getBlueprintTypeByFlowId(flowId);
				String flowInfo = (String) subFlowInfo.get("FLOW_INFO");
				BluePrint bp = JSON.parseObject(flowInfo, BluePrint.class);
				List<Element> elements = bp.getNodeDataArray();
				for(Element ele:elements){
					Map<String,String> params = ele.getParams();
					map.put(ele.getPluginName(),params);
				}
			}
			//稍后替换为从组件子流程中读取
			Map<String,Object> pluginInputMap = new HashMap<>();
			map.put("pluginInput", pluginInputMap);

			Map<String,String> templateMap = (Map<String, String>)resourceVersionDetail.get("templates");
			Map<String, String> templateMap1 = templateReplace(templateMap,instanceId,configMap);

			map.put("configTemplate", templateMap1);
			addParams(map, instance);
			messagesList.add(map);
		}
		return messagesList;
	}

	void addParams(Map<String,Object> map,Map<String,Object>instance){
		Object isNew = instance.get("isNew");
		Object isNeedDeploy = instance.get("isNeedDeploy");
		Object sourceStatus = instance.get("sourceStatus");
		Object desStatus = instance.get("desStatus");
		Object isVersionEqual = instance.get("isVersionEqual");
		map.put("isNew", isNew);
		map.put("isNeedDeploy", isNeedDeploy);
		map.put("sourceStatus", sourceStatus);
		map.put("desStatus", desStatus);
		map.put("isVersionEqual", isVersionEqual);
	}

	public void encryptValue(Map<String,Object> map){
		Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, Object> entry = iter.next();
			String value = "" + entry.getValue();
			if(value.startsWith(ENCRYPT_PREFIX)){
				if(value.endsWith(ENCRYPT_SUFFIX)){
					int start = value.indexOf(ENCRYPT_PREFIX);
					int end = value.lastIndexOf(ENCRYPT_SUFFIX);
					String plain = value.substring(start + ENCRYPT_PREFIX.length(), end);
					try {
						DesUtils desUtil = new DesUtils();
						String cipher = desUtil.encrypt(plain);
						StringBuffer sb = new StringBuffer();
						sb.append(ENCRYPT_PREFIX).append(cipher).append(ENCRYPT_SUFFIX);
						entry.setValue(sb.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}


}
