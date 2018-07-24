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
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;
import com.dc.appengine.plugins.utils.WeblogicCryptUtil;

public class DataSourceDelete4Weblogic extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(DataSourceDelete4Weblogic.class);

	private static final String WLSUSR = "wlsUsr";
	private static final String WLSPWD = "wlsPwd";
	private static final String WLSDOMAINPATH = "wlsDomainPath";
	private static final String DSNAME = "dsName";
	private static final String WLSVERSION = "wlsVersion";
	private static final String WLSENCODING = "wlsEncoding";

	private String wlsUsr;
	private String wlsPwd;
	private String wlsDomainPath;
	private String dsName;
	private String wlsVersion;
	private String wlsEncoding;

	@Override
	public String doPreAction() {
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke() throws Exception {
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
		if (JudgeUtil.isEmpty(wlsDomainPath)) {
			log.error("wlsDomainPath is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsDomainPath is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(dsName)) {
			log.error("dsName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dsName is null");
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
		if (!verifyUsrPwd) {
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

		try {
			File folder = new File(wlsDomainPath + File.separator + "config");
			boolean delete = deleteDataSource(folder);
			if (!delete) {
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "数据源[" + dsName + "]不存在！");
				return JSON.toJSONString(resultMap);
			}
			resultMap.put(Constants.Plugin.RESULT, true);
			resultMap.put(Constants.Plugin.MESSAGE, "删除数据源[" + dsName + "]成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "删除数据源失败，reason:" + LogRecord.getStackTrace(e));
		}
		return JSON.toJSONString(resultMap);
	}

	private boolean deleteDataSource(File folder) throws Exception {
		boolean isExist = false;
		Element deleteDS = null;
		String deleteDSFile = null;
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(wlsEncoding);
		File config = new File(folder, "config.xml");
		if (!config.exists()) {
			return isExist;
		}
		Document docConfig = DocumentHelper.createDocument();
		SAXReader readerConfig = new SAXReader();
		docConfig = readerConfig.read(config);
		Element root = docConfig.getRootElement();
		List<Element> list = root.elements("jdbc-system-resource");
		if (list != null && list.size() > 0) {
			for (Element ds : list) {
				String dataSourceName = ds.elementText("name");
				if (dsName.equals(dataSourceName)) {
					deleteDS = ds;
					deleteDSFile = ds.elementText("descriptor-file-name");
					isExist = true;
					break;
				}
			}
		}
		if (deleteDS != null) {
			list.remove(deleteDS);
			OutputStreamWriter osWriter = new OutputStreamWriter(new FileOutputStream(config), wlsEncoding);
			XMLWriter xmlWriter = new XMLWriter(osWriter, format);
			xmlWriter.write(docConfig);
			osWriter.close();
			xmlWriter.close();
			String dataSourcePath = folder.getAbsolutePath() + File.separator + deleteDSFile;
			File dataSourceFile = new File(dataSourcePath);
			if (dataSourceFile.exists()) {
				dataSourceFile.delete();
			}
		}
		return isExist;
	}

	public void setWlsUsr(String wlsUsr) {
		this.wlsUsr = wlsUsr;
	}

	public void setWlsPwd(String wlsPwd) {
		this.wlsPwd = wlsPwd;
	}

	public void setWlsDomainPath(String wlsDomainPath) {
		this.wlsDomainPath = wlsDomainPath;
	}

	public void setDsName(String dsName) {
		this.dsName = dsName;
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
		if (!JudgeUtil.isEmpty(this.pluginInput.get(WLSDOMAINPATH))) {
			this.wlsDomainPath = this.pluginInput.get(WLSDOMAINPATH).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DSNAME))) {
			this.dsName = this.pluginInput.get(DSNAME).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(WLSVERSION))) {
			this.wlsVersion = this.pluginInput.get(WLSVERSION).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(WLSENCODING))) {
			this.wlsEncoding = this.pluginInput.get(WLSENCODING).toString();
		}
	}

}
