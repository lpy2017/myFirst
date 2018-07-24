package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;
import com.dc.appengine.plugins.utils.WeblogicCryptUtil;

public class AppDeploy4Weblogic extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(AppDeploy4Weblogic.class);

	private static final String WLSUSR = "wlsUsr";
	private static final String WLSPWD = "wlsPwd";
	private static final String APPNAME = "appName";
	private static final String APPPATH = "appPath";
	private static final String APPTARGET = "appTarget";
	private static final String WLSDOMAINPATH = "wlsDomainPath";
	private static final String WLSVERSION = "wlsVersion";
	private static final String WLSENCODING = "wlsEncoding";

	private String wlsUsr;
	private String wlsPwd;
	private String appName;
	private String appPath;
	private String appTarget;
	private String wlsDomainPath;
	private String wlsVersion;
	private String wlsEncoding;

	@Override
	public String doPreAction() {
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke() {
		sendMessageByMOM(this.paramMap, this.messageMap, paramMap.get(Constants.Plugin.PLUGINNAME).toString());
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doPostAction() {
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent() throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		if (JudgeUtil.isEmpty(wlsUsr)) {
			log.error("wlsUsr is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsUsr is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(wlsPwd)) {
			log.error("wlsPwd is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsPwd is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(appName)) {
			log.error("appName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "appName is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(appPath)) {
			log.error("appPath is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "appPath is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(appTarget)) {
			log.error("appTarget is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "appTarget is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(wlsDomainPath)) {
			log.error("wlsDomainPath is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsDomainPath is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(wlsVersion)) {
			log.error("wlsVersion is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsVersion is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(wlsEncoding)) {
			log.error("wlsEncoding is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsEncoding is null");
			return JSON.toJSONString(resultMap);
		}
		if(!"12".equals(wlsVersion)){
			log.error("weblogic版本是[" + wlsVersion + "]，当前插件只支持weblogic12版本！");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "weblogic版本是[" + wlsVersion + "]，当前插件只支持weblogic12版本！");
			return JSON.toJSONString(resultMap);
		}
		boolean verifyUsrPwd = false;
		try {
			verifyUsrPwd = WeblogicCryptUtil.verifyUsrPwdAES(wlsDomainPath, wlsUsr, wlsPwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!verifyUsrPwd){
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE,
					"weblogic domain登录的用户名[" + wlsUsr + "]密码[" + "wlsPwd]" + "校验失败，请检查！");
			return JSON.toJSONString(resultMap);
		}
		String wlsConfig = wlsDomainPath + File.separator + "config" + File.separator + "config.xml";
		File config = new File(wlsConfig);
		if (!config.exists()) {
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "weblogic配置文件[" + wlsConfig + "]不存在！");
			return JSON.toJSONString(resultMap);
		}
		
		Document doc = DocumentHelper.createDocument();
		SAXReader reader = new SAXReader();
		try {
			doc = reader.read(config);
			Element root = doc.getRootElement();
			List<Element> list = root.elements();
			List<Element> apps = root.elements("app-deployment");
			int mark = -1;
			Element cv = root.element("configuration-version");
			int index = list.indexOf(cv);
			if (apps.size() == 0) {
				mark = index;
			} else {
				for (Element app : apps) {
					if (appName.equals(app.element("name").getText())) {
						throw new Exception("应用[" + appName + "]已经存在！");
					}
				}
				mark = index + apps.size();
			}
			System.out.println("size=" + list.size());
			System.out.println("mark=" + mark);
			String namespace = root.getNamespaceURI();
			Element appElement = DocumentFactory.getInstance().createElement("app-deployment", namespace);
			Element nameElement = appElement.addElement("name");
			nameElement.setText(appName);
			Element targetElement = appElement.addElement("target");
			targetElement.setText(appTarget);
			Element moduleTypeElement = appElement.addElement("module-type");
			String moduleType = "";
			File app = new File(appPath);
			if(!app.exists()){
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "appPath[" + appPath + "]不存在，无法完成部署！");
				return JSON.toJSONString(resultMap);
			}
			else{
				if(app.isDirectory()){
					//校验文件目录是否符合weblogic对于程序的规范
					String appPathWar = appPath + File.separator + "WEB-INF" + File.separator + "web.xml";
					File war = new File(appPathWar);
					String appPathEar = appPath + File.separator + "META-INF" + File.separator + "application.xml";
					File ear = new File(appPathEar);
					if(war.exists()){
						moduleType = "war";
					}
					else if(ear.exists()){
						moduleType = "ear";
					}
					else{
						resultMap.put(Constants.Plugin.RESULT, false);
						resultMap.put(Constants.Plugin.MESSAGE, "应用[" + appPath + "]既不符合weblogic对于web应用程序的目录规范也不符合企业级应用程序的目录规范，无法完成部署！");
						return JSON.toJSONString(resultMap);
					}
				}
				else if(app.isFile()){
					if(!appPath.endsWith(".war") && !appPath.endsWith(".ear")){
						resultMap.put(Constants.Plugin.RESULT, false);
						resultMap.put(Constants.Plugin.MESSAGE, "当前只支持后缀为war和ear的应用程序归档文件！");
						return JSON.toJSONString(resultMap);
					}
					else{
						ZipFile zip = new ZipFile(appPath);
						String warEntry_linux = "WEB-INF/web.xml";
						String warEntry_windows = "WEB-INF\\web.xml";
						String earEntry_linux = "META-INF/application.xml";
						String earEntry_windows = "META-INF\\application.xml";
						if(zip.getEntry(warEntry_linux) != null || zip.getEntry(warEntry_windows) != null){
							moduleType = "war";
						}
						else if(zip.getEntry(earEntry_linux) != null || zip.getEntry(earEntry_windows) != null){
							moduleType = "ear";
						}
						else{
							resultMap.put(Constants.Plugin.RESULT, false);
							resultMap.put(Constants.Plugin.MESSAGE, "应用[" + appPath + "]既不符合weblogic对于web应用程序的归档文件规范也不符合企业级应用程序的归档文件规范，无法完成部署！");
							return JSON.toJSONString(resultMap);
						}
					}
				}else{
				}
			}
			moduleTypeElement.setText(moduleType);
			Element sourcePathElement = appElement.addElement("source-path");
			sourcePathElement.setText(appPath);
			Element securityElement = appElement.addElement("security-dd-model");
			securityElement.setText("DDOnly");
			Element stagingModeElement = appElement.addElement("staging-mode");
			stagingModeElement.setText("");
			stagingModeElement.addAttribute("xsi:nil", "true");
			Element planStagingModeElement = appElement.addElement("plan-staging-mode");
			planStagingModeElement.setText("");
			planStagingModeElement.addAttribute("xsi:nil", "true");
			Element cacheInAppDirectoryElement = appElement.addElement("cache-in-app-directory");
			cacheInAppDirectoryElement.setText("false");
			list.add(mark + 1, appElement);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(wlsEncoding);
			XMLWriter xmlWriter = new XMLWriter(new OutputStreamWriter(new FileOutputStream(config), wlsEncoding),
					format);
			xmlWriter.write(doc);
			xmlWriter.close();
			resultMap.put(Constants.Plugin.RESULT, true);
			resultMap.put(Constants.Plugin.MESSAGE, "应用部署成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "应用部署失败，reason:" + LogRecord.getStackTrace(e));
		}
		return JSON.toJSONString(resultMap);
	}

	public void setWlsUsr(String wlsUsr) {
		this.wlsUsr = wlsUsr;
	}
	public void setWlsPwd(String wlsPwd) {
		this.wlsPwd = wlsPwd;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setAppPath(String appPath) {
		this.appPath = appPath;
	}
	public void setAppTarget(String appTarget) {
		this.appTarget = appTarget;
	}

	public void setWlsDomainPath(String wlsDomainPath) {
		this.wlsDomainPath = wlsDomainPath;
	}

	public void setWlsVersion(String wlsVersion) {
		this.wlsVersion = wlsVersion;
	}

	public void setWlsEncoding(String wlsEncoding) {
		this.wlsEncoding = wlsEncoding;
	}

	@Override
	public void setFields() {
		if (!JudgeUtil.isEmpty(this.pluginInput.get(WLSUSR))) {
			this.wlsUsr = this.pluginInput.get(WLSUSR).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(WLSPWD))) {
			this.wlsPwd = this.pluginInput.get(WLSPWD).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(APPNAME))) {
			this.appName = this.pluginInput.get(APPNAME).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(APPPATH))) {
			this.appPath = this.pluginInput.get(APPPATH).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(APPTARGET))) {
			this.appTarget = this.pluginInput.get(APPTARGET).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(WLSDOMAINPATH))) {
			this.wlsDomainPath = this.pluginInput.get(WLSDOMAINPATH).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(WLSVERSION))) {
			this.wlsVersion = this.pluginInput.get(WLSVERSION).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(WLSENCODING))) {
			this.wlsEncoding = this.pluginInput.get(WLSENCODING).toString();
		}
	}

}
