package com.dc.appengine.plugins.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.ConfigHelper;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;
import com.dc.bd.auto.client.JobExecDetail;
import com.dc.bd.ice.dto.NodeProtocolJdbcDto;
import com.dc.bd.ice.dto.NodeProtocolJmxDto;
import com.dc.bd.ice.dto.ProtocolDto;
import com.dc.servicejet.ASClient4CD;

public class ServiceJet extends AbstractPlugin {
	
	private String pluginCode;
	private String typeCode;
	private String targetIp;
	private String deviceIp;
	private String protocolList;
	private String tokenId;
	private List<String> strList = Arrays.asList("overwrite", "preserveLastModified", "followSymlinks", "caseSensitive", "failWithoutMatch", "ignoreDTD", "isUseData", "reuseExiFile", "fileAutoExtend", "nostage", "library");
	private List<String> localTypeCodes = Arrays.asList("DownloadSaltArtifact", "DownloadSaltComp", "SaltConfig","Salt","SaltSSH","InstallNode","CMD4Local","DetectPort");
	
	private static final Logger log = LoggerFactory.getLogger(ServiceJet.class);
	
	public String preAction(String param) throws Exception {
		Map<String, Object> jsonObj = JSON.parseObject(param);
		if (jsonObj.get("pluginName") != null) {
			if (jsonObj.get("pluginName").toString().startsWith("SJ-GetCompCurrentVersion")) {
				try {
					Boolean result=false;
					Map<String,Object> map= JSON.parseObject(param, new TypeReference<Map<String, Object>>() {
					});
					Map<String,Object> message = (Map<String, Object>)map.get("message");
					if((Map<String, Object>) message.get(Constants.Plugin.CurrentVersion_Key) !=null){
						message.putAll((Map<String, Object>) message.get(Constants.Plugin.CurrentVersion_Key));
						map.put(Constants.Plugin.MESSAGE, message);
						result=true;
					}
					super.initPlugin(JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect),Constants.Plugin.PHRASE_PREACTION);
					updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"start to do preAction inputParam==="+System.lineSeparator()+param);
					if(gf_variable != null){
						this.messageMap.put(gf_variable,result);
					}
					this.messageMap.put(PLUGIN_RESULT_KEY,result);
					this.paramMap.put(Constants.Plugin.MESSAGE, messageMap);
					updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction is end  outputParam==="+JSON.toJSONString(this.paramMap)+System.lineSeparator()+"plugin_result_preAction:"+result);
					return JSON.toJSONString(this.paramMap);
				} catch (Exception e) {
					if(gf_variable != null){
						messageMap.put(gf_variable,false);
					}
					messageMap.put(PLUGIN_RESULT_KEY,false);
					paramMap.put(Constants.Plugin.MESSAGE, messageMap);
					setGFVariable(gf_variable, Constants.Plugin.FAIL_STATE,paramMap);
					log.error(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction is error=="+e.getMessage());
					e.printStackTrace();
					updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction is error=="+System.lineSeparator()+LogRecord.getStackTrace(e)
					+System.lineSeparator()+"inputParam==="+param+System.lineSeparator()+"plugin_result_preAction:"+false);
					return JSON.toJSONString(this.paramMap);
				}
			}
			if (jsonObj.get("pluginName").toString().startsWith("SJ-GetCompTargetVersion")) {
				try {
					Boolean result=false;
					Map<String,Object> map= JSON.parseObject(param, new TypeReference<Map<String, Object>>() {
					});
					Map<String,Object> message = (Map<String, Object>)map.get("message");
					if((Map<String, Object>) message.get(Constants.Plugin.TargetVersion_Key) !=null){
						message.putAll((Map<String, Object>) message.get(Constants.Plugin.TargetVersion_Key));
						map.put(Constants.Plugin.MESSAGE, message);
						result=true;
					}
					super.initPlugin(JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect),Constants.Plugin.PHRASE_PREACTION);
					updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"start to do preAction inputParam==="+System.lineSeparator()+param);
					if(gf_variable != null){
						this.messageMap.put(gf_variable,result);
					}
					this.messageMap.put(PLUGIN_RESULT_KEY,result);
					this.paramMap.put(Constants.Plugin.MESSAGE, messageMap);
					updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction is end  outputParam==="+JSON.toJSONString(this.paramMap)+System.lineSeparator()+"plugin_result_preAction:"+result);
					return JSON.toJSONString(this.paramMap);
				
				} catch (Exception e) {
					if(gf_variable != null){
						messageMap.put(gf_variable,false);
					}
					messageMap.put(PLUGIN_RESULT_KEY,false);
					paramMap.put(Constants.Plugin.MESSAGE, messageMap);
					setGFVariable(gf_variable, Constants.Plugin.FAIL_STATE,paramMap);
					log.error(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction is error=="+e.getMessage());
					e.printStackTrace();
					updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction is error=="+System.lineSeparator()+LogRecord.getStackTrace(e)
					+System.lineSeparator()+"inputParam==="+param+System.lineSeparator()+"plugin_result_preAction:"+false);
					return JSON.toJSONString(this.paramMap);
				}
			}
		}
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
			return JSON.toJSONString(this.paramMap);
		} catch (Exception e) {
			messageMap.put(componentFlow_RESULT,false);
			if(gf_variable != null){
				messageMap.put(gf_variable,false);
			}
			messageMap.put(PLUGIN_RESULT_KEY,false);
			paramMap.put(Constants.Plugin.MESSAGE, messageMap);
			setGFVariable(gf_variable, Constants.Plugin.FAIL_STATE,paramMap);
			log.error(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction is error=="+e.getMessage());
			e.printStackTrace();
			updatePluginLog(this.paramMap.get(Constants.Plugin.PLUGINNAME)+"do preAction is error=="+System.lineSeparator()+LogRecord.getStackTrace(e)
			+System.lineSeparator()+"inputParam==="+param+System.lineSeparator()+"plugin_result_preAction:"+false);
			return JSON.toJSONString(this.paramMap);
		}
	}

	@Override
	public String doPreAction() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private String string2List(Map<String, Object> input) {
//		Map<String, Object> input = JSON.parseObject(JSON.toJSONString(this.pluginInput));
		Set<String> keys = input.keySet();
		for (String key : keys) {
			if (strList.contains(key)) {
				String value = input.get(key).toString();
				List<Object> valueList = JSON.parseArray(value);
				input.put(key, valueList);
			}
		}
		return JSON.toJSONString(input);
	}
	
	private void addMsg2Input(Map<String, Object> input) {
//		Map<String, Object> input = JSON.parseObject(JSON.toJSONString(this.pluginInput));
		input.put("cd_message", JSON.toJSONString(this.messageMap));
	}
	
	private void addParam2Input(Map<String, Object> input) {
//		Map<String, Object> input = JSON.parseObject(JSON.toJSONString(this.pluginInput));
		input.put("frame_param", JSON.toJSONString(this.paramMap));
	}
	
	@Override
	public String doInvoke() throws Exception {
		decrypt();
		setFields();
		Map<String, Object> input = JSON.parseObject(JSON.toJSONString(this.pluginInput));
		addMsg2Input(input);
		addParam2Input(input);
		String masterRest = ConfigHelper.getValue("masterRest");
		input.put("masterRest", masterRest);
		JobExecDetail execDetail = new JobExecDetail();
		execDetail.setUuid(tokenId);
		execDetail.setPluginCode(pluginCode);
		execDetail.setTypeCode(typeCode);
		execDetail.setTargetIp(targetIp);
		execDetail.setDeviceIp(deviceIp);
		execDetail.setDomain("SmartCD");
		execDetail.setMsg(string2List(input));
		execDetail.setSource("SMARTCD");
		if (protocolList != null) {
			try {
				if ("tomcat".equals(protocolList)) {
					NodeProtocolJmxDto dto = new NodeProtocolJmxDto();
					dto.setJmxType(3);
					dto.setUserName(pluginInput.get("userName").toString());
					dto.setPassword(pluginInput.get("password").toString());
					dto.setPort(Integer.valueOf(pluginInput.get("port").toString()));
					dto.setResource("SMARTCD");
					dto.setManager("manager/text");
					
					List<ProtocolDto> protocolDtos = new ArrayList<ProtocolDto>();
					ProtocolDto protocolDto = new ProtocolDto();
					protocolDto.setProtocolObj(dto);
					protocolDto.setProtocolType("8");
					protocolDtos.add(protocolDto);
					execDetail.setProtocolList(protocolDtos);
				}
				if ("jdbc".equals(protocolList)) {
					NodeProtocolJdbcDto jdbcDto = new NodeProtocolJdbcDto();
					jdbcDto.setDatasourceName(pluginInput.get("dataSource") == null ? "" : pluginInput.get("dataSource").toString());//informix 要填对应的数据源的名称,其它的保证唯一就行
					jdbcDto.setDbName(pluginInput.get("dbName").toString()); //数据库名
					jdbcDto.setDbType(Integer.valueOf(pluginInput.get("dataBaseType").toString())); //1:MYSQL; 2:ORACLE; 3:DB2; 4:SQL SERVER 5：informix
					jdbcDto.setIp(pluginInput.get("serverIp").toString()); //数据库所在服务器的IP地址
					jdbcDto.setPort(Integer.valueOf(pluginInput.get("serverPort").toString()));
					jdbcDto.setUserName(pluginInput.get("dbUserName").toString());
					jdbcDto.setPassword(pluginInput.get("dbPassword").toString());
					jdbcDto.setVersion(pluginInput.get("version") == null ? "" : pluginInput.get("version").toString());//数据库的版本
					jdbcDto.setCode(pluginInput.get("code") == null ? "" : pluginInput.get("code").toString());//编码
					
					List<ProtocolDto> protocolDtos = new ArrayList<ProtocolDto>();
					ProtocolDto protocolDto = new ProtocolDto();
					protocolDto.setProtocolObj(jdbcDto);
					protocolDto.setProtocolType("1");//1:JDBC,2:WMI,3:SSH,4:OS,5:SNMP,6:HTTP,7:TELNET,8:JMX,9:locale,10:FTP,
					protocolDtos.add(protocolDto);
					execDetail.setProtocolList(protocolDtos);
				}
				if ("weblogic".equals(protocolList)) {
					NodeProtocolJmxDto dto = new NodeProtocolJmxDto();
					dto.setJmxType(1);//1:weblogic 2:websphere 3:tomcat
					dto.setUserName(pluginInput.get("userName").toString());
					dto.setPassword(pluginInput.get("password").toString());
					dto.setPort(Integer.valueOf(pluginInput.get("port").toString()));
					dto.setResource("SMARTCD");
					
					List<ProtocolDto> protocolDtos = new ArrayList<ProtocolDto>();
					ProtocolDto protocolDto = new ProtocolDto();
					protocolDto.setProtocolObj(dto);
					protocolDto.setProtocolType("8");
					protocolDtos.add(protocolDto);
					execDetail.setProtocolList(protocolDtos);
				}
			} catch (Exception e) {
				log.error("", e);
				return null;
			}
		}
		if (localTypeCodes.contains(typeCode)) {
			execDetail.setTargetIp(ConfigHelper.getValue("salt.master.ip"));
		}
		ASClient4CD.getInstance().execJob(execDetail, null);
		encrypt();
		return null;
	}

	@Override
	public String doPostAction() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String doAgent() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFields() {
		if (!JudgeUtil.isEmpty(this.pluginInput.get("pluginCode"))) {
			this.pluginCode = this.pluginInput.get("pluginCode").toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get("typeCode"))) {
			this.typeCode = this.pluginInput.get("typeCode").toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get("deviceIp"))) {
			this.deviceIp = this.pluginInput.get("deviceIp").toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get("protocolList"))) {
			this.protocolList = this.pluginInput.get("protocolList").toString();
		}
		this.targetIp = (String) this.paramMap.get("_message_ip");
		this.tokenId = (String) this.paramMap.get("workitemId");
	}

}
