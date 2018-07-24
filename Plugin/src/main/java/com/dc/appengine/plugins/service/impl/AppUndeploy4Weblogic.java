package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.FileUtil;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;
import com.dc.appengine.plugins.utils.WeblogicCryptUtil;

public class AppUndeploy4Weblogic extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(AppUndeploy4Weblogic.class);

	private static final String WLSUSR = "wlsUsr";
	private static final String WLSPWD = "wlsPwd";
	private static final String APPNAME = "appName";
	private static final String WLSDOMAINPATH = "wlsDomainPath";
	private static final String WLSVERSION = "wlsVersion";
	private static final String WLSENCODING = "wlsEncoding";

	private String wlsUsr;
	private String wlsPwd;
	private String appName;
	private String wlsDomainPath;
	private String wlsVersion;
	private String wlsEncoding;

	@Override
	public String doPreAction() {
		return null;
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

		if (!"12".equals(wlsVersion)) {
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
			// 删除缓存目录
			String server = wlsDomainPath + File.separator + "servers" + File.separator + "AdminServer" + File.separator
					+ "tmp" + File.separator + "_WL_user" + File.separator + appName;
			File tmp = new File(server);
			if (tmp.exists()) {
				FileUtil.deleteDirectory(tmp);
			}
			//删除配置
			doc = reader.read(config);
			Element root = doc.getRootElement();
			int mark = -1;
			List<Element> list = root.elements("app-deployment");
			for (int i = 0; i < list.size(); i++) {
				Element item = list.get(i);
				if (appName.equals(item.element("name").getText())) {
					mark = i;
					break;
				}
			}
			if (mark == -1) {
				throw new Exception("应用[" + appName + "]不存在！");
			} else {
				list.remove(mark);
			}
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(wlsEncoding);
			XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(config), wlsEncoding), format);
			writer.write(doc);
			writer.close();

			resultMap.put(Constants.Plugin.RESULT, true);
			resultMap.put(Constants.Plugin.MESSAGE, "应用卸载成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "应用卸载失败，reason:" + LogRecord.getStackTrace(e));
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

	public void setWlsDomainPath(String wlsDomainPath) {
		this.wlsDomainPath = wlsDomainPath;
	}

	public String getWlsVersion() {
		return wlsVersion;
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
