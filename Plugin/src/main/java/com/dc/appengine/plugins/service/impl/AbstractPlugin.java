package com.dc.appengine.plugins.service.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.message.sender.CommandSender;
import com.dc.appengine.plugins.service.IAgent;
import com.dc.appengine.plugins.service.IProcess;
import com.dc.appengine.plugins.utils.ConfigHelper;
import com.dc.appengine.plugins.utils.DesUtils;
import com.dc.appengine.plugins.utils.JsonParseUtil;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;
import com.dc.appengine.plugins.utils.MailUtil;
/*
 * 插件的调用说明：通过实例化插件的父类，调用父类的方法，在父类的方法内，调用子类的实现，完成插件方法的调用
 */
public abstract class AbstractPlugin extends CommandSender implements IProcess, IAgent {
	private static Logger log = LoggerFactory.getLogger(Download.class);
	public static final String componentFlow_RESULT="flow_result"; //记录插件每一步的执行结果，用于ParamUtil.isSuccess方法
	public static final String PLUGIN_RESULT_KEY="plugin_result"; //记录插件每一步的执行结果，用于ParamUtil.isSuccess方法
	public static final String PLUGIN_RESULT_MESSAGE_KEY="plugin_result_message";
	public static final String GF_VARIABLE_KEY="gf_variable";
	public static final String SEND_MAIL_FLAG_KEY="sendMailFlag";
	public static final String SEND_MAIL_SUBJECT_KEY="subject";
	public static final String SEND_MAIL_RECIPIENT_KEY="recipient";
	public Map<String,Object> paramMap=new HashMap<>();
	public Map<String,Object> messageMap=new HashMap<>();
	public Map<String,Object> blueprintConfig=new HashMap<>();
	public Map<String,Object> componentInput=new HashMap<>();
	public Map<String,Object> pluginInput=new HashMap<>();
	public Map<String,Object> resultMap=new HashMap<>();
	public String gf_variable=null;
	public static String sysEncoding=System.getProperty("sun.jnu.encoding");//操作系统编码
	public String sendMailFlag;//00-不发邮件，11-发送邮件，01-插件执行成功时发送邮件，10-插件执行失败时发送邮件
	
	public static final String TASK_SUCCESS = "03";
	public static final String TASK_FAILED = "04";
	public static final String RELEASE_TASK_SUCCESS = "07";
	public static final String RELEASE_TASK_FAILED = "08";
	
	public ConcurrentHashMap<String, String> encryptMap = new ConcurrentHashMap<>();

	/*
	 * plugin 前处理
	 * 前处理如果遇到异常不执行跳转操作，原因是，此时token实例还没有落库，工作流不支持
	 * 前处理捕获异常后，不再抛出，在直接return
	 */
	@Override
	public String preAction(String param) throws Exception {
		String messageLog="";
		try {
			this.initPlugin(param,Constants.Plugin.PHRASE_PREACTION);
			updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"start to do preAction inputParam==="+System.lineSeparator()+param);
			String result = this.doPreAction();
			if(gf_variable != null){
				messageMap.put(gf_variable,true);
			}
			messageMap.put(componentFlow_RESULT,true);
			messageMap.put(PLUGIN_RESULT_KEY,true);
			paramMap.put(Constants.Plugin.MESSAGE, messageMap);
			updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction is end  outputParam==="+System.lineSeparator()+JSON.toJSONString(this.paramMap)+System.lineSeparator()+"plugin_result_preAction:"+true);
//			return JSON.toJSONString(this.paramMap);
			messageLog=this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction 执行成功";
		} catch (Exception e) {
			messageMap.put(componentFlow_RESULT,false);
			if(gf_variable != null){
				messageMap.put(gf_variable,false);
			}
			updateTaskStatus(paramMap, TASK_FAILED);
			updateReleaseTaskStatus(paramMap, RELEASE_TASK_FAILED);
			messageMap.put(PLUGIN_RESULT_KEY,false);
			paramMap.put(Constants.Plugin.MESSAGE, messageMap);
			setGFVariable(gf_variable, Constants.Plugin.FAIL_STATE,paramMap);
			log.error(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction is error=="+e.getMessage());
			e.printStackTrace();
			updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction is error=="+System.lineSeparator()+LogRecord.getStackTrace(e)
			+System.lineSeparator()+"inputParam==="+param+System.lineSeparator()+"plugin_result_preAction:"+false);
			messageLog=this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction 执行失败："+System.lineSeparator()+LogRecord.getStackTrace(e);
		}
		sendMail(this.pluginInput, (Boolean)messageMap.get(PLUGIN_RESULT_KEY),messageLog,"");
		return JSON.toJSONString(this.paramMap);
	}
	/*
	 * plugin inovke 
	 */
	@Override
	public String invoke(String param) throws Exception {
		String messageLog;
		try {
			this.initPlugin(param,Constants.Plugin.PHRASE_INVOKE);
			updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"start to do invoke inputParam==="+System.lineSeparator()+param);
			String result = this.doInvoke();
			if(gf_variable != null){
				messageMap.put(gf_variable,true);
			}
			Object flowState=messageMap.get(AbstractPlugin.componentFlow_RESULT);
			if((Boolean)flowState){
				messageMap.put(AbstractPlugin.componentFlow_RESULT,true);
			}
			messageMap.put(PLUGIN_RESULT_KEY,true);
			paramMap.put(Constants.Plugin.MESSAGE, messageMap);
//			updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do invoke is end  outputParam==="+System.lineSeparator()+JSON.toJSONString(this.paramMap)+System.lineSeparator()+"plugin_result_invoke:"+true);
//			return JSON.toJSONString(this.paramMap);
			messageLog=this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do invoke 执行成功";
		} catch (Exception e) {
			messageMap.put(componentFlow_RESULT,false);
			if(gf_variable != null){
				messageMap.put(gf_variable,false);
			}
			updateTaskStatus(paramMap, TASK_FAILED);
			updateReleaseTaskStatus(paramMap, RELEASE_TASK_FAILED);
			messageMap.put(PLUGIN_RESULT_KEY,false);
			paramMap.put(Constants.Plugin.MESSAGE, messageMap);
			setGFVariable(gf_variable, Constants.Plugin.FAIL_STATE,paramMap);
			log.error(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do invoke is error=="+e.getMessage());
			e.printStackTrace();
			updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do invoke is error=="+System.lineSeparator()+LogRecord.getStackTrace(e)
			+System.lineSeparator()+"inputParam==="+param+System.lineSeparator()+"plugin_result_invoke:"+false);
			messageLog=this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do invoke 执行失败："+System.lineSeparator()+LogRecord.getStackTrace(e);
		}
		sendMail(this.pluginInput, (Boolean)messageMap.get(PLUGIN_RESULT_KEY),messageLog,"");
		return JSON.toJSONString(this.paramMap);
	
	
	}
	/*
	 * plugin 后处理
	 */
	@Override
	public String postAction(String param) throws Exception {
		String messageLog;
		try {
			this.initPlugin(param,Constants.Plugin.PHRASE_POSTACTION);
			updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"start to do postAction inputParam==="+System.lineSeparator()+param);
			String result = this.doPostAction();
			if(gf_variable != null){
				messageMap.put(gf_variable,true);
			}
			Object flowState=messageMap.get(AbstractPlugin.componentFlow_RESULT);
			if((Boolean)flowState){
				messageMap.put(AbstractPlugin.componentFlow_RESULT,true);
			}
			messageMap.put(PLUGIN_RESULT_KEY,true);
			paramMap.put(Constants.Plugin.MESSAGE, messageMap);
			updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do postAction is end  outputParam==="+System.lineSeparator()+JSON.toJSONString(this.paramMap)+System.lineSeparator()+"plugin_result_postAction:"+true);
//			return JSON.toJSONString(this.paramMap);
			messageLog=this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do postAction 执行成功";
		} catch (Exception e) {
			messageMap.put(componentFlow_RESULT,false);
			if(gf_variable != null){
				messageMap.put(gf_variable,false);
			}
			updateTaskStatus(paramMap, TASK_FAILED);
			updateReleaseTaskStatus(paramMap, RELEASE_TASK_FAILED);
			messageMap.put(PLUGIN_RESULT_KEY,false);
			paramMap.put(Constants.Plugin.MESSAGE, messageMap);
			setGFVariable(gf_variable, Constants.Plugin.FAIL_STATE,paramMap);
			log.error(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do postAction is error=="+e.getMessage());
			e.printStackTrace();
			updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do postAction is error=="+System.lineSeparator()+LogRecord.getStackTrace(e)
					+System.lineSeparator()+"inputParam==="+param+System.lineSeparator()+"plugin_result_postAction:"+false);
			messageLog=this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do postAction 执行失败"+System.lineSeparator()+LogRecord.getStackTrace(e);
		}
		sendMail(this.pluginInput, (Boolean)messageMap.get(PLUGIN_RESULT_KEY),messageLog,"");
		return JSON.toJSONString(this.paramMap);
	}
	/*
	 *plugin Agent
	 */
	@Override
	public String doActive(String param) throws Exception {
		this.initPlugin(param,Constants.Plugin.PHRASE_ACTIVE);
		//解密
		decrypt();
		//更新插件解密后的value
		setFields();
		//只有agent执行用解密后的value
		String result = this.doAgent();
		//重新加密供其他环节使用
		encrypt();
		JSONObject resultMap = JSON.parseObject(result);
		Boolean doResult =resultMap.getBoolean(Constants.Plugin.RESULT);
		String doMessage =resultMap.getString(Constants.Plugin.MESSAGE);
		String messageLog=this.paramMap.get(Constants.Plugin.PLUGINNAME)+"执行"+(doResult?"成功 ":"失败 ")+"执行日志--"+doMessage;
		if(!doResult){
			updateTaskStatus(paramMap, TASK_FAILED);
			updateReleaseTaskStatus(paramMap, RELEASE_TASK_FAILED);
		}
		sendMail(this.pluginInput, doResult,messageLog,Constants.Plugin.PHRASE_ACTIVE);
		return result;
	}
	
	/*
	 * 子类实现方法
	 */
	
	public abstract String doPreAction();
	public abstract String doInvoke() throws Exception;
	public abstract String doPostAction();
	public abstract String doAgent() throws Exception;
	public abstract void setFields();
	
	
	/*
	 * 替换插件input的动态变量
	 */
	public  String replaceVarivable() throws Exception{
		Map<String, Object> paramMap = this.paramMap;
		Map<String,Object> messageMap = this.messageMap;
		Map<String, Object> pluginInput =this.pluginInput;
		Map<String, Object> componentInput = this.componentInput;
		//对插件所有key对应的value的变量，进行替换
		for(Map.Entry<String, Object> entry:pluginInput.entrySet()){
			String key= entry.getKey().toString();
			Object value= entry.getValue();
			if(value !=null){
				String variableValue= value.toString();
				Boolean replace=false;//是否需要替换
				Boolean writeBack=true;
				Pattern p1 = Pattern.compile(JsonParseUtil.p1);//存在变量
				Pattern p2 = Pattern.compile(JsonParseUtil.p2);//${deployPath}
				Pattern p3 = Pattern.compile(JsonParseUtil.p3);//${tomcat_-1#deployPath}
				Pattern p4 = Pattern.compile(JsonParseUtil.p4);//${blueprint#deployPath}
				Matcher mat1 = p1.matcher(variableValue);
				List<String> group=new ArrayList<String>();
				  while(mat1.find()){
					  group.add(mat1.group());
				  }
				if(group.size()>0){
					for(String one:group){
						String tmpValue=one;
						if(p2.matcher(one).find()){
							int indexF = one.indexOf("${");
							int indexL = one.lastIndexOf("}");
							String varivableKey = one.substring(indexF+2, indexL);
							if(componentInput.containsKey(varivableKey)){ //引用组件input定义的key且格式为pluginInput{"deployPath":"${deployPath"},componentInput{"deployPath":"${instanceId}"}
								writeBack=true;
								tmpValue= (String) componentInput.get(varivableKey);
							}
							tmpValue=JsonParseUtil.parseFromMap(messageMap, tmpValue);//所属message
							tmpValue=JsonParseUtil.parseFromMap(componentInput, tmpValue);//所属组件input
							tmpValue=JsonParseUtil.parseFromMap(pluginInput, tmpValue);//插件本身input
							variableValue=variableValue.replace(one, tmpValue);
							if(writeBack){
								componentInput.put(varivableKey, tmpValue);
								}
						}else if(p3.matcher(variableValue).find()){
							tmpValue=JsonParseUtil.parseFromFlow(paramMap, tmpValue);
							variableValue=variableValue.replace(one, tmpValue);
						}else if(p4.matcher(variableValue).find()){
							tmpValue=JsonParseUtil.parseFromMap(blueprintConfig, tmpValue);
							variableValue=variableValue.replace(one, tmpValue);
						}	
					}
					pluginInput.put(key, variableValue);
				}else{
					continue;
				}
			}
		}
		messageMap.put(paramMap.get(Constants.Plugin.PLUGINNAME).toString(), pluginInput);
		messageMap.put(Constants.Plugin.COMPONENTINPUT, componentInput);
		paramMap.put(Constants.Plugin.MESSAGE,  messageMap);
		return JSON.toJSONString(paramMap);
	}
	/*
	 * 插件初始化成员变量方法
	 */
	@SuppressWarnings("unchecked")
	public void initPlugin(String param,String phrase) throws Exception {
		Map<String, Object> paramMap = JSON.parseObject(param, new TypeReference<Map<String, Object>>() {
		});
		Map<String,Object> messageMap = null;
		if(!JudgeUtil.isEmpty(paramMap.get(Constants.Plugin.MESSAGE))){
			if(paramMap.get(Constants.Plugin.MESSAGE) instanceof String){
				messageMap =JSON.parseObject(paramMap.get(Constants.Plugin.MESSAGE).toString(), new TypeReference<Map<String, Object>>() {
				});
			}else{
				messageMap = (Map<String,Object>)paramMap.get(Constants.Plugin.MESSAGE);
			}
		}
		this.paramMap = paramMap;
		String pluginName = "" + paramMap.get("pluginName");
		boolean isBlueprintPlugin = paramMap.get("_taskPluginKey") != null ? true : false;
		if(!JudgeUtil.isEmpty(messageMap)){
			if(isBlueprintPlugin && messageMap.get(pluginName) == null){
				Map<String, Map<String, Object>> params = getBlueprintPluginConfig("" + paramMap.get("pdId"), "" + paramMap.get("_taskPluginKey"));
				messageMap.put(pluginName, params.get(pluginName));
			}
			this.blueprintConfig= JSON.parseObject("" + messageMap.get(Constants.Plugin.BLUEPRINTCONFG), new TypeReference<Map<String, Object>>(){});
			Map<String, Object> pluginInput =(Map<String, Object>) messageMap.get(paramMap.get(Constants.Plugin.PLUGINNAME));
			Map<String, Object> componentInput = (Map<String, Object>) messageMap.get(Constants.Plugin.COMPONENTINPUT);
			Object instanceId = messageMap.get(Constants.Plugin.INSTANCEID);
			if(!isBlueprintPlugin && instanceId == null){
				throw new Exception("封装的组件消息体-message为空!");
			}
			this.messageMap = messageMap;
			this.componentInput = componentInput;
			this.pluginInput = pluginInput;
			if(!JudgeUtil.isEmpty(this.pluginInput)){
				//变量替换
				if(Constants.Plugin.PHRASE_PREACTION.equals(phrase)||Constants.Plugin.PHRASE_ACTIVE.equals(phrase)){
					this.replaceVarivable();
				}
				if(!JudgeUtil.isEmpty(pluginInput.get(GF_VARIABLE_KEY))){
					this.gf_variable=pluginInput.get(GF_VARIABLE_KEY).toString();
				}
				//设置子类成员变量
				this.setFields();
			}else{
				throw new Exception("封装的组件消息体-message,插件参数为空!");
			}
		}else{
			if(isBlueprintPlugin){
				this.messageMap = new HashMap<String,Object>();
				blueprintConfig= new HashMap<String, Object>();
				componentInput = new HashMap<String, Object>();
				this.messageMap.put(Constants.Plugin.BLUEPRINTCONFG, blueprintConfig);
				this.messageMap.put(Constants.Plugin.COMPONENTINPUT, componentInput);
				Map<String, Map<String, Object>> params = getBlueprintPluginConfig("" + paramMap.get("pdId"), "" + paramMap.get("_taskPluginKey"));
				pluginInput = params.get(pluginName);
				this.messageMap.put(pluginName, pluginInput);
			}
			else{
				throw new Exception("封装的组件消息体-message为空!");
			}
		}
	}
	public void setGf_variable(String gf_variable) {
		this.gf_variable = gf_variable;
	}
	/*========================================插件调用其他模块方法===================================*/

	/*
	 * 更新插件执行状态，以及日志
	 */
	public void updatePluginLog(String message){
		LogRecord.send2Master(this.paramMap.get(Constants.Plugin.WORKITEMID).toString(), LogRecord.getCurrentTime()+" "+message);
	}

	
	//=========================调用工作流接口的方法======================
	/*
	 * 触发工作流执行next步的方法
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void invokeWorkflowServer(Map<String, Object> content) {
		Map<String, String> nextUserMap = new HashMap();
		Map<String, String> mapInsVarMap = new HashMap();
		Map<String, Object> mapTaskVarMap = new HashMap();
		try {
			//日志太长，把RESULT_MESSAGE移除掉
			Map<String,Object> messageMap = (Map<String,Object>)content.get("message");
			JSONObject result = JSON.parseObject(messageMap.get("result").toString());
			result.remove(Constants.Plugin.RESULT_MESSAGE);
			Object flowState=messageMap.get(AbstractPlugin.componentFlow_RESULT);
			if((Boolean)flowState){
				Boolean result_state = (Boolean)result.get("result");
				messageMap.put(AbstractPlugin.componentFlow_RESULT,result_state);
			}
			messageMap.put(Constants.Plugin.RESULT,result);
			content.put(Constants.Plugin.MESSAGE, messageMap);
			//组装参数
			String workitemId = content.get("workitemId").toString();
			String user = "admin";
			String mapInsVar = JSON.toJSONString(mapInsVarMap);
			String mapNextUsers = JSON.toJSONString(nextUserMap);
			Map<String, Object> map = new HashMap<>();
			map.put("_taskCP", content);
			mapTaskVarMap.put("ALL", map);
			String mapTaskVar = JSON.toJSONString(mapTaskVarMap);
			// 反射调用执行流程executeWorkitem
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			Class clzj = Class.forName("com.digitalchina.workflow.server.WorkflowServer", false, cl);
			Object instancej = clzj.getConstructor(new Class[] {}).newInstance(new Object[] {});
			Class[] cs = new Class[5];
			cs[0] = String.class;
			cs[1] = String.class;
			cs[2] = String.class;
			cs[3] = String.class;
			cs[4] = String.class;
			Method mj = clzj.getDeclaredMethod("executeWorkitem", cs);
			mj.setAccessible(true);
			Object[] values = new Object[5];
			values[0] = workitemId;
			values[1] = user;
			values[2] = mapInsVar;
			values[3] = mapNextUsers;
			values[4] = mapTaskVar;
			// 记录日志
			LogRecord.send2Master(content.get(Constants.Plugin.WORKITEMID).toString(),
					LogRecord.getCurrentTime() + System.lineSeparator() + "invoke workFlow 执行下一步 content="+System.lineSeparator()+JSON.toJSONString(content)
					+ System.lineSeparator() +"invoke workFlow params: workitemId=" + workitemId + " user=" + user + " mapInsVar="
							+ mapInsVar + " mapNextUsers=" + mapNextUsers + " mapTaskVar=" + mapTaskVar);
			log.debug("====invoke workFlow values: workitemId=" + workitemId + " user=" + user + " mapInsVar="
					+ mapInsVar + " mapNextUsers=" + mapNextUsers + " mapTaskVar=" + mapTaskVar);
			mj.invoke(instancej, values);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("Stop invoke is error==" + e.getMessage());
			e.printStackTrace();
			// 记录日志
			LogRecord.send2Master(content.get(Constants.Plugin.WORKITEMID).toString(),
					LogRecord.getCurrentTime() + "plugin_result_invokeWorkFlow:"+false+System.lineSeparator() + "invoke workFlow error"
							+LogRecord.getStackTrace(e) );
		}
	}
	
	/*
	 * 调用workFlow rest接口，set全局流程变量
	 */
	public static void setGFVariable(String varName,String varValue,Map<String,Object>paramMap){
		//更新任务流程的状态
		if("false".equals(varValue)){
			updateTaskStatus(paramMap,TASK_FAILED);
			updateReleaseTaskStatus(paramMap, RELEASE_TASK_FAILED);
		}
		if(!JudgeUtil.isEmpty(varName)){
			//调用flowworker流程变量set接口
			Client client = ClientBuilder.newClient();
			String frameRest = ConfigHelper.getValue("frameRest").toString();
			client.target(frameRest).path("WFService").path("setInstanceVariable.wf")
					.queryParam("instanceID", paramMap.get(Constants.Plugin.TOKENINSTANCEID).toString()).queryParam("variableName", varName).
					queryParam("variableValue", varValue).
					request().get(String.class);
			JSONObject insvarMap = (JSONObject)paramMap.get(Constants.Plugin.INSVARMAP);
			insvarMap.put(varName, varValue);
		}
	}
	
	public Map<String, Map<String, Object>> getBlueprintPluginConfig(String blueprintFlowId, String pluginKey){
		Map<String, String> pluginPara = new HashMap<>();
		pluginPara.put("blueprintFlowId", blueprintFlowId);
		pluginPara.put("pluginKey", pluginKey);
		Client client = ClientBuilder.newClient();
		WebTarget target = client
				.target(ConfigHelper.getValue("masterRest") + "/ws/blueprint/getPluginConfigByBlueprintFlow");
		String result = target.request().post(Entity.entity(JSON.toJSONString(pluginPara), MediaType.APPLICATION_JSON), String.class);
		Map<String, Map<String, Object>> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Map<String, Object>>>(){});
		return resultMap;
	}
	
	
	public void decrypt() throws Exception{
		if(this.messageMap !=null){
			decrypt(this.messageMap,this.messageMap.get(Constants.Plugin.COMPONENTNAME).toString());
		}
		if(this.componentInput !=null){
			decrypt(this.componentInput,this.messageMap.get(Constants.Plugin.COMPONENTNAME).toString());
		}
		if(this.pluginInput !=null){
			decrypt(this.pluginInput,this.paramMap.get(Constants.Plugin.PLUGINNAME).toString());
		}
	}
	public void decrypt(Map<String,Object> inputMap,String name) throws Exception{
		Pattern pattern = Pattern.compile("ENC\\(\\w*\\)");
		for(Map.Entry<String, Object> entry:inputMap.entrySet()){
			String key= entry.getKey().toString();
			Object valueObject= entry.getValue();
			if(!JudgeUtil.isEmpty(valueObject) && (valueObject instanceof String)){
				String value = valueObject.toString();
				Matcher matcher = pattern.matcher(value);
				List<String> encs = new ArrayList<String>();
				while(matcher.find()){
					encs.add(matcher.group());
				}
				if(encs.size() > 0){
					encryptMap.put(name + "_" + key, value);
					for(String enc : encs){
						enc = enc.substring(4, enc.length() - 1);//密文
						DesUtils des = new DesUtils();
						String dec = des.decrypt(enc);
						value = value.replace("ENC(" + enc + ")", dec);
					}
					inputMap.put(key, value);//存放解密后的消息
				}
			}
		}
	}
	public void encrypt(){
		encrypt(pluginInput,paramMap.get(Constants.Plugin.PLUGINNAME).toString());
		encrypt(componentInput,messageMap.get(Constants.Plugin.COMPONENTNAME).toString());
		encrypt(messageMap,messageMap.get(Constants.Plugin.COMPONENTNAME).toString());
		messageMap.put(paramMap.get(Constants.Plugin.PLUGINNAME).toString(), pluginInput);
		messageMap.put(Constants.Plugin.COMPONENTINPUT, componentInput);
		paramMap.put(Constants.Plugin.MESSAGE,  messageMap);
	}
	public void encrypt(Map<String,Object> inputMap,String name){
		for(Map.Entry<String, Object> entry:inputMap.entrySet()){
			String key= entry.getKey().toString();
			if(encryptMap.containsKey(name + "_" + key)){//曾经做过解密的key
				inputMap.put(key, encryptMap.get(name + "_" + key));
				encryptMap.remove(name + "_" + key);
			}
		}
	}
	public String decryptRoot(String key){
		String value = "" + messageMap.get(key);
		Pattern pattern = Pattern.compile("ENC\\(\\w*\\)");
		Matcher matcher = pattern.matcher(value);
		try{
			if(matcher.find()){
				String encValue = matcher.group();
				encValue = encValue.substring(4, encValue.length()-1);
				DesUtils des = new DesUtils();
				String decPwd = des.decrypt(encValue);
				value = value.replace("ENC("+encValue+")", decPwd);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return value;
	}
	
	/*
	 * 发送邮件
	 */
	public static void sendMail(Map<String,Object>pluginInput,Boolean result,String messageLog, String method){
		String sendMailFlag=null;
		if(!JudgeUtil.isEmpty(pluginInput.get(SEND_MAIL_FLAG_KEY))){
			sendMailFlag=pluginInput.get(SEND_MAIL_FLAG_KEY).toString();
		}
		if(!JudgeUtil.isEmpty(sendMailFlag)){
			if("00".equals(sendMailFlag)){
				return;
			}else if((!Constants.Plugin.PHRASE_ACTIVE.equals(method)&&!result&&("11".equals(sendMailFlag)||"10".equals(sendMailFlag)))//非active执行失败，且发邮件开关打开
					||(Constants.Plugin.PHRASE_ACTIVE.equals(method)&&("11".equals(sendMailFlag)||("01".equals(sendMailFlag)&&result)||("10".equals(sendMailFlag)&&!result)))){
							//active方法，遵循原则11-必须发邮件；01且成功，发送邮件；10且失败，发送邮件
				String subject=null;
				if(!JudgeUtil.isEmpty(pluginInput.get("subject"))){
					subject=pluginInput.get("subject").toString();
				}else{
					subject="Sm@rtCD邮件通知";
				}
				String recipient=null;
				if(!JudgeUtil.isEmpty(pluginInput.get("recipient"))){
					recipient=pluginInput.get("recipient").toString();
				}else{
					return;
				}
				String content="你好:<br>";
				Map<String,Object> params = new HashMap<>();
				params.put("sender", ConfigHelper.getInstance().getValue("sender"));
				params.put("password", ConfigHelper.getInstance().getValue("password"));
				params.put("smtpHost", ConfigHelper.getInstance().getValue("smtpHost"));
				params.put("smtpPort", ConfigHelper.getInstance().getValue("smtpPort"));
				params.put("subject", subject);
				params.put("recipient", recipient);
				params.put("content", content+"<pre>    "+messageLog+"</pre>");
				MailUtil.sendMail(params);
			}
		}
	}
	
	
	public static String updateTaskStatus(Map paramMap,String status){
		Map<String,Object> resultMap= new HashMap<>();
		try {
			Form form = new Form();
			Map insvarMap = (Map)paramMap.get("insvarMap");
			String flowInstanceId = (String)insvarMap.get("_fromMainInstanceId");
			form.param(Constants.Plugin.FLOW_INSTANCEID, flowInstanceId);
			form.param(Constants.Plugin.STATUS, status);//03-流程执行成功,04-流程执行失败
			Client client = ClientBuilder.newClient();
			WebTarget target = client
					.target(ConfigHelper.getValue("masterRest") + "/ws/release/updateReleaseTaskStatus");
			//更新任务状态
			String result = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", e.getMessage());
		}
		return JSON.toJSONString(resultMap);
	}
	
	public static String updateReleaseTaskStatus(Map paramMap,String status){
		Map<String,Object> resultMap= new HashMap<>();
		try {
			Form form = new Form();
			Map insvarMap = (Map)paramMap.get("insvarMap");
			String flowInstanceId = (String)insvarMap.get("_fromMainInstanceId");
			form.param(Constants.Plugin.FLOW_INSTANCEID, flowInstanceId);
			form.param(Constants.Plugin.STATUS, status);//07-流程执行成功,08-流程执行失败
			Client client = ClientBuilder.newClient();
			WebTarget target = client
					.target(ConfigHelper.getValue("masterRest") + "/ws/releaseTask/updateReleaseTaskOrderStatus");
			//更新任务状态
			String result = target.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", e.getMessage());
		}
		return JSON.toJSONString(resultMap);
	}
	
	public static void main(String[]args) throws Exception{
//		String aa = "encrypt[7a1b51705a80ac76dcde8783d2fe7439962aee2d1476b3851afffc3cebd0f3d57a8a17df7b5695107094bce5924f7cc6412bdbff2c85f4917ae129db339f732d37807dfdf25a0d2740d6bbca49562a3550d9b4e52f47c115b7b36d3f2c12b885fb4a5d9a4e0b0bae8142d9dcb7d4730ca76cfbea4c6b9e33f8d0d6a5ee7d21166dbf7c082b930e5574f5779e93c084f612c61885d59f11f6]";
//		Map<String,Object> inputMap1 = new HashMap<>();
//		inputMap1.put("test", aa);
//		String variableValue="${depoyPath}aaa${startPath}";
		String variableValue="${depoyPath}sss";
		Pattern p1 = Pattern.compile("^\\$\\{([a-zA-Z0-9_]+)\\}$");
		Pattern p2 = Pattern.compile("\\$\\{([a-zA-Z0-9_]+)\\}");
		Matcher mat1 = p1.matcher(variableValue);
		Matcher mat2 = p2.matcher(variableValue);
		Boolean oneVariable=false;
		Boolean replace=false;
		if(mat1.find()){//
			oneVariable=true;
			replace=true;
		}else if(mat2.find()){
			 replace=true;
		}
		System.out.println("oneVariable="+oneVariable+" replace="+replace);
	}
}
