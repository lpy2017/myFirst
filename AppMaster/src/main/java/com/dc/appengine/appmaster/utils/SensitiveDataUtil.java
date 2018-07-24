package com.dc.appengine.appmaster.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.entity.Version;

public class SensitiveDataUtil {
	
	public static String encryptDesText(String text) throws Exception{
		DesUtils uitl = new DesUtils();
		return uitl.encrypt(text);
	}
	
	public static String decryptDesText(String text) throws Exception{
		DesUtils uitl = new DesUtils();
		return uitl.decrypt(text);
	}
	
	public static String concealUser(String usr) {
		char[] chars = new char[usr.length()];
		for (int i = 0; i < usr.length(); i++) {
			if (i == 0) {
				chars[i] = usr.charAt(i);
			} else {
				chars[i] = '*';
			}
		}
		return String.valueOf(chars);
	}

	public static String concealPassword(String pwd) {
		char[] chars = new char[pwd.length()];
		for (int i = 0; i < pwd.length(); i++) {
			chars[i] = '*';
		}
		return String.valueOf(chars);
	}

	public static String concealIp(String ip) {
		char[] chars = new char[ip.length()];
		for (int i = 0; i < ip.length(); i++) {
			if (ip.charAt(i) == '.' || i == ip.length() - 1) {
				chars[i] = ip.charAt(i);
			} else {
				chars[i] = '*';
			}
		}
		return String.valueOf(chars);
	}
	
	public static void decryptVersionConfig(Version item) {
		String input = item.getInput();
		if (input != null && input.indexOf("ENC") != -1) {
			String decInput = decryptConfig(input);
			if(decInput != null){
				item.setInput(decInput);
			}
		}
		String output = item.getOutput();
		if (output != null && output.indexOf("ENC") != -1) {
			String decOutput = decryptConfig(output);
			if(decOutput != null){
				item.setOutput(decOutput);
			}
		}
	}

	public static void decryptApplicationConfig(Application application){
		String currentInput = application.getCurrentInput();
		if (currentInput != null && currentInput.indexOf("ENC") != -1) {
			String decCurrentInput = decryptConfig(currentInput);
			if(decCurrentInput != null){
				application.setCurrentInput(decCurrentInput);
			}
		}
		String currentOutput = application.getCurrentOutput();
		if (currentOutput != null && currentOutput.indexOf("ENC") != -1) {
			String decCurrentOutput = decryptConfig(currentOutput);
			if(decCurrentOutput != null){
				application.setCurrentOutput(decCurrentOutput);
			}
		}
		String targetInput = application.getTargetInput();
		if (targetInput != null && targetInput.indexOf("ENC") != -1) {
			String decTargetInput = decryptConfig(targetInput);
			if(decTargetInput != null){
				application.setTargetInput(decTargetInput);
			}
		}
		String targetOutput = application.getTargetOutput();
		if (targetOutput != null && targetOutput.indexOf("ENC") != -1) {
			String decTargetOutput = decryptConfig(targetOutput);
			if(decTargetOutput != null){
				application.setTargetOutput(decTargetOutput);
			}
		} 
	}

	public static String decryptConfig(String config){
		if (config == null) {
			return null;
		} else {
			try {
				DesUtils des = new DesUtils();
				Map<String, String> map = JSON.parseObject(config, new TypeReference<Map<String, String>>() {
				});
				Set<String> keys = map.keySet();
				if (keys.size() > 0) {
					for (String key : keys) {
						String value = map.get(key);
						if (value.startsWith("ENC(") && value.endsWith(")")) {
							value = value.substring(4, value.length() - 1);
							value = "DEC(" + des.decrypt(value) + ")";
							map.put(key, value);
						}
					}
				}
				return JSON.toJSONString(map);
			} catch (Exception e) {
				System.out.println("DES解密失败[" + e.getMessage() + "]");
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static String encryptConfig(String config){
		if (config == null) {
			return null;
		} else {
			try {
				DesUtils des = new DesUtils();
				Map<String, String> map = JSON.parseObject(config, new TypeReference<Map<String, String>>() {
				});
				Set<String> keys = map.keySet();
				if (keys.size() > 0) {
					for (String key : keys) {
						String value = map.get(key);
						if (value.startsWith("DEC(") && value.endsWith(")")) {
							value = value.substring(4, value.length() - 1);
							value = "ENC(" + des.encrypt(value) + ")";
							map.put(key, value);
						}
					}
				}
				return JSON.toJSONString(map);
			} catch (Exception e) {
				System.out.println("DES加密失败[" + e.getMessage() + "]");
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static Map<String, Object> decryptMapConfig(Map<String, Object> mapConfig){
		if (mapConfig == null || mapConfig.size() == 0) {
			return mapConfig;
		} else {
			try {
				DesUtils des = new DesUtils();
				Set<String> keys = mapConfig.keySet();
				if (keys.size() > 0) {
					for (String key : keys) {
						String value = "" + mapConfig.get(key);
						if (value.startsWith("ENC(") && value.endsWith(")")) {
							value = value.substring(4, value.length() - 1);
							value = "DEC(" + des.decrypt(value) + ")";
							mapConfig.put(key, value);
						}
					}
				}
				return mapConfig;
			} catch (Exception e) {
				System.out.println("DES解密失败[" + e.getMessage() + "]");
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static Map<String, Object> encryptMapConfig(Map<String, Object> mapConfig){
		if (mapConfig == null || mapConfig.size() == 0) {
			return mapConfig;
		} else {
			try {
				DesUtils des = new DesUtils();
				Set<String> keys = mapConfig.keySet();
				if (keys.size() > 0) {
					for (String key : keys) {
						String value = "" + mapConfig.get(key);
						if (value.startsWith("DEC(") && value.endsWith(")")) {
							value = value.substring(4, value.length() - 1);
							value = "ENC(" + des.encrypt(value) + ")";
							mapConfig.put(key, value);
						}
					}
				}
				return mapConfig;
			} catch (Exception e) {
				System.out.println("DES加密失败[" + e.getMessage() + "]");
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static void decryptMapFlow(Map<String, Object> mapFlow){
		if (mapFlow == null || mapFlow.size() == 0) {
			return;
		} else {
			try {
				DesUtils des = new DesUtils();
				List<Map<String, Object>> nodes = (List<Map<String, Object>>)mapFlow.get("nodeDataArray");
				for(Map<String, Object> node : nodes){
					String flowcontroltype = "" + node.get("flowcontroltype");
					int type = Integer.parseInt(flowcontroltype);
					if(type == 9){
						Map<String, String> params = (Map<String, String>)node.get("params");
						Set<String> keys = params.keySet();
						if (keys.size() > 0) {
							for (String key : keys) {
								String value = "" + params.get(key);
								if (value.startsWith("ENC(") && value.endsWith(")")) {
									value = value.substring(4, value.length() - 1);
									value = "DEC(" + des.decrypt(value) + ")";
									params.put(key, value);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				System.out.println("DES解密失败[" + e.getMessage() + "]");
				e.printStackTrace();
				throw new RuntimeException("DES解密失败[" + e.getMessage() + "]");
			}
		}
	}
	
	public static void encryptMapFlow(Map<String, Object> mapFlow) throws RuntimeException{
		if (mapFlow == null || mapFlow.size() == 0) {
			return;
		} else {
			try {
				DesUtils des = new DesUtils();
				List<Map<String, Object>> nodes = (List<Map<String, Object>>)mapFlow.get("nodeDataArray");
				for(Map<String, Object> node : nodes){
					String flowcontroltype = "" + node.get("flowcontroltype");
					int type = Integer.parseInt(flowcontroltype);
					if(type == 9){
						Map<String, String> params = (Map<String, String>)node.get("params");
						Set<String> keys = params.keySet();
						if (keys.size() > 0) {
							for (String key : keys) {
								String value = "" + params.get(key);
								if (value.startsWith("DEC(") && value.endsWith(")")) {
									value = value.substring(4, value.length() - 1);
									value = "ENC(" + des.encrypt(value) + ")";
									params.put(key, value);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				System.out.println("DES加密失败[" + e.getMessage() + "]");
				e.printStackTrace();
				throw new RuntimeException("DES加密失败[" + e.getMessage() + "]");
			}
		}
	}
	
	public static String getDecryptConfig(String config) throws Exception{
		if(config != null && !"".equals(config) && config.indexOf("ENC(") != -1){
			String decryptConfig = SensitiveDataUtil.decryptConfig(config);
			if(decryptConfig == null){
				String error = "解密参数失败！";
				System.out.println(error);
				throw new Exception(error);
			}
			else{
				return decryptConfig;
			}
		}
		else{
			return config;
		}
	}
	
	public static String getEncryptConfig(String config) throws Exception{
		if(config != null && !"".equals(config) && config.indexOf("DEC(") != -1){
			String encryptConfig = SensitiveDataUtil.encryptConfig(config);
			if(encryptConfig == null){
				String error = "加密参数失败！";
				System.out.println(error);
				throw new Exception(error);
			}
			else{
				return encryptConfig;
			}
		}
		else{
			return config;
		}
	}
}
