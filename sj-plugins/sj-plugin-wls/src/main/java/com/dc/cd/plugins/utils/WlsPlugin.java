package com.dc.cd.plugins.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
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
import com.dc.appengine.plugins.utils.FileUtil;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.WeblogicCryptUtil;
import com.dc.bd.plugin.BaseAgentPlugin;
import com.dc.bd.plugin.JobDetailDto;
import com.dc.bd.plugin.JobExecResultDto;

public class WlsPlugin extends BaseAgentPlugin {
	private static final Logger log = LoggerFactory.getLogger(WlsPlugin.class);
	public static String sysEncoding = System.getProperty("sun.jnu.encoding");// 操作系统编码

	public JobExecResultDto execute(JobDetailDto detailDTO) {
		log.info("demoplugin receive job");
		log.info("action:{}", detailDTO.getAction());
		log.info("deviceIP:{}", detailDTO.getDeviceIp());
		log.info("JobDetailParam:{}", detailDTO.getJobDetailParam());
		log.info("Encode:{}", detailDTO.getEncode());
		log.info("jobid:{}", detailDTO.getJobId());
		log.info("JobInstId:{}", detailDTO.getJobInstId());
		log.info("jobname:{}", detailDTO.getJobName());
		log.info("NodeGrpId:{}", detailDTO.getNodeGrpId());
		log.info("nodeid:{}", detailDTO.getNodeId());
		log.info("PluginCode:{}", detailDTO.getPluginCode());
		log.info("ProtocolList:{}", detailDTO.getProtocolList());
		log.info("Timeout:{}", detailDTO.getTimeout());
		log.info("TypeCode:{}", detailDTO.getTypeCode());
		log.info("uuid:{}", detailDTO.getUuid());
		/*
		 * 执行作业的代码省略
		 */
		Map<String, Object> pluginInput = JSON.parseObject(detailDTO.getJobDetailParam());

		if (Constants.Plugin.APPDEPLOY4WEBLOGIC.equals(detailDTO.getTypeCode())) {
			return appDeploy4Weblogic(detailDTO, pluginInput);
		} else if (Constants.Plugin.APPUNDEPLOY4WEBLOGIC.equals(detailDTO.getTypeCode())) {
			return appUndeploy4Weblogic(detailDTO, pluginInput);
		} else if (Constants.Plugin.WEBLOGICJMSQUEUECREATE.equals(detailDTO.getTypeCode())) {
			return weblogicJMSQueueCreate(detailDTO, pluginInput);
		} else if (Constants.Plugin.WEBLOGICJMSQUEUEUPDATE.equals(detailDTO.getTypeCode())) {
			return weblogicJMSQueueUpdate(detailDTO, pluginInput);
		} else if (Constants.Plugin.WEBLOGICJMSQUEUEDELETE.equals(detailDTO.getTypeCode())) {
			return weblogicJMSQueueDelete(detailDTO, pluginInput);
		} else if (Constants.Plugin.DATASOURCENEW4WEBLOGIC.equals(detailDTO.getTypeCode())) {
			return dataSourceNew4Weblogic(detailDTO, pluginInput);
		} else if (Constants.Plugin.DATASOURCEDELETE4WEBLOGIC.equals(detailDTO.getTypeCode())) {
			return dataSourceDelete4Weblogic(detailDTO, pluginInput);
		} else {
		}
		// 未找到匹配的typecode
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(false);
		execResultDto.setMsg("未找到对应的typecode");
		return execResultDto;
	}

	private JobExecResultDto dataSourceDelete4Weblogic(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		JobExecResultDto execResultDto = getJobExecResultDto(detailDTO);
		String wlsUsr = null;
		String wlsPwd = null;
		String wlsDomainPath = null;
		String dsName = null;
		String wlsVersion = null;
		String wlsEncoding = null;
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSUSR))) {
			wlsUsr = pluginInput.get(Constants.Plugin.WLSUSR).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSPWD))) {
			wlsPwd = pluginInput.get(Constants.Plugin.WLSPWD).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSDOMAINPATH))) {
			wlsDomainPath = pluginInput.get(Constants.Plugin.WLSDOMAINPATH).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DSNAME))) {
			dsName = pluginInput.get(Constants.Plugin.DSNAME).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSVERSION))) {
			wlsVersion = pluginInput.get(Constants.Plugin.WLSVERSION).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSENCODING))) {
			wlsEncoding = pluginInput.get(Constants.Plugin.WLSENCODING).toString();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (JudgeUtil.isEmpty(wlsUsr)) {
			log.error("wlsUsr is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsUsr is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsPwd)) {
			log.error("wlsPwd is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsPwd is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsDomainPath)) {
			log.error("wlsDomainPath is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsDomainPath is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(dsName)) {
			log.error("dsName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dsName is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsVersion)) {
			log.error("wlsVersion is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsVersion is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsEncoding)) {
			log.error("wlsEncoding is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsEncoding is null");
			return result(execResultDto, resultMap);
		}
		if (!"12".equals(wlsVersion)) {
			log.error("weblogic版本是[" + wlsVersion + "]，当前插件只支持weblogic12版本！");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "weblogic版本是[" + wlsVersion + "]，当前插件只支持weblogic12版本！");
			return result(execResultDto, resultMap);
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
			return result(execResultDto, resultMap);
		}
		String wlsConfig = wlsDomainPath + File.separator + "config" + File.separator + "config.xml";
		File config = new File(wlsConfig);
		if (!config.exists()) {
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "weblogic配置文件[" + wlsConfig + "]不存在！");
			return result(execResultDto, resultMap);
		}

		try {
			File folder = new File(wlsDomainPath + File.separator + "config");
			boolean delete = deleteDataSource(folder, wlsEncoding, dsName);
			if (!delete) {
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "数据源[" + dsName + "]不存在！");
				return result(execResultDto, resultMap);
			}
			resultMap.put(Constants.Plugin.RESULT, true);
			resultMap.put(Constants.Plugin.MESSAGE, "删除数据源[" + dsName + "]成功！");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "删除数据源失败，reason:" + getStackTrace(e));
		}
		return result(execResultDto, resultMap);
	}

	private JobExecResultDto dataSourceNew4Weblogic(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		JobExecResultDto execResultDto = getJobExecResultDto(detailDTO);
		String wlsUsr = null;
		String wlsPwd = null;
		String wlsDomainPath = null;
		String dsName = null;
		String dsJndi = null;
		String dsTargetServer = null;
		String dbType = null;
		String dbJTA = null;
		String dbName = null;
		String dbIp = null;
		String dbPort = null;
		String dbUsr = null;
		String dbPwd = null;
		String wlsVersion = null;
		String wlsEncoding = null;
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSUSR))) {
			wlsUsr = pluginInput.get(Constants.Plugin.WLSUSR).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSPWD))) {
			wlsPwd = pluginInput.get(Constants.Plugin.WLSPWD).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSDOMAINPATH))) {
			wlsDomainPath = pluginInput.get(Constants.Plugin.WLSDOMAINPATH).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DSNAME))) {
			dsName = pluginInput.get(Constants.Plugin.DSNAME).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DSJNDI))) {
			dsJndi = pluginInput.get(Constants.Plugin.DSJNDI).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DSTARGETSERVER))) {
			dsTargetServer = pluginInput.get(Constants.Plugin.DSTARGETSERVER).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DBTYPE))) {
			dbType = pluginInput.get(Constants.Plugin.DBTYPE).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DBJTA))) {
			dbJTA = pluginInput.get(Constants.Plugin.DBJTA).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DBNAME))) {
			dbName = pluginInput.get(Constants.Plugin.DBNAME).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DBIP))) {
			dbIp = pluginInput.get(Constants.Plugin.DBIP).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DBPORT))) {
			dbPort = pluginInput.get(Constants.Plugin.DBPORT).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DBUSR))) {
			dbUsr = pluginInput.get(Constants.Plugin.DBUSR).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DBPWD))) {
			dbPwd = pluginInput.get(Constants.Plugin.DBPWD).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSVERSION))) {
			wlsVersion = pluginInput.get(Constants.Plugin.WLSVERSION).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSENCODING))) {
			wlsEncoding = pluginInput.get(Constants.Plugin.WLSENCODING).toString();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (JudgeUtil.isEmpty(wlsUsr)) {
			log.error("wlsUsr is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsUsr is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsPwd)) {
			log.error("wlsPwd is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsPwd is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsDomainPath)) {
			log.error("wlsDomainPath is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsDomainPath is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(dsName)) {
			log.error("dsName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dsName is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(dsJndi)) {
			log.error("dsJndi is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dsJndi is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(dsTargetServer)) {
			log.error("dsTargetServer is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dsTargetServer is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(dbType)) {
			log.error("dbType is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbType is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(dbJTA)) {
			log.error("dbJTA is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbJTA is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(dbName)) {
			log.error("dbName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbName is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(dbIp)) {
			log.error("dbIp is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbIp is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(dbPort)) {
			log.error("dbPort is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbPort is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(dbUsr)) {
			log.error("dbUsr is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbUsr is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(dbPwd)) {
			log.error("dbPwd is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbPwd is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsVersion)) {
			log.error("wlsVersion is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsVersion is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsEncoding)) {
			log.error("wlsEncoding is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsEncoding is null");
			return result(execResultDto, resultMap);
		}
		if (!"12".equals(wlsVersion)) {
			log.error("weblogic版本是[" + wlsVersion + "]，当前插件只支持weblogic12版本！");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "weblogic版本是[" + wlsVersion + "]，当前插件只支持weblogic12版本！");
			return result(execResultDto, resultMap);
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
			return result(execResultDto, resultMap);
		}
		String wlsConfig = wlsDomainPath + File.separator + "config" + File.separator + "config.xml";
		File config = new File(wlsConfig);
		if (!config.exists()) {
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "weblogic配置文件[" + wlsConfig + "]不存在！");
			return result(execResultDto, resultMap);
		}
		String url = null;
		String driver = null;
		String testSql = null;
		String transaction = null;
		if (dbType.equalsIgnoreCase("oracle")) {
			url = "jdbc:oracle:thin:@" + dbIp + ":" + dbPort + ":" + dbName;
			if (dbJTA.equals("true")) {
				driver = "oracle.jdbc.xa.client.OracleXADataSource";
				transaction = "TwoPhaseCommit";
			} else {
				driver = "oracle.jdbc.OracleDriver";
				transaction = "OnePhaseCommit";
			}
			testSql = "SQL ISVALID";
		} else if (dbType.equalsIgnoreCase("mysql")) {
			url = "jdbc:mysql://" + dbIp + ":" + dbPort + "/" + dbName;
			driver = "com.mysql.jdbc.Driver";
			testSql = "SQL SELECT 1";
			transaction = "OnePhaseCommit";
		} else if (dbType.equalsIgnoreCase("db2")) {
			url = "jdbc:db2://" + dbIp + ":" + dbPort + "/" + dbName;
			driver = "com.ibm.db2.jcc.DB2Driver";
			testSql = "";
			transaction = "OnePhaseCommit";
		} else {
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "不支持此数据库类型[" + dbType + "]！");
			return result(execResultDto, resultMap);
		}
		boolean isConnected = WeblogicCryptUtil.testDBConnection(dbUsr, dbPwd, url, driver, testSql);
		if (!isConnected) {
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE,
					"url[" + url + "]driver[" + driver + "]user[" + dbUsr + "]的数据库连接失败，请检查数据库配置！");
			return result(execResultDto, resultMap);
		}
		Map<String, String> dataSourceMap = new HashMap<String, String>();
		Document docConfig = DocumentHelper.createDocument();
		SAXReader readerConfig = new SAXReader();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding(wlsEncoding);
		try {
			docConfig = readerConfig.read(config);
			Element root = docConfig.getRootElement();
			List<Element> list = root.elements();
			List<Element> dataSources = root.elements("jdbc-system-resource");
			if (dataSources != null && dataSources.size() > 0) {
				for (Element ds : dataSources) {
					String dataSourceName = ds.elementText("name");
					String dsFileName = ds.elementText("descriptor-file-name");
					File dsFile = new File(wlsDomainPath + File.separator + "config" + File.separator + dsFileName);
					Document docDs = DocumentHelper.createDocument();
					SAXReader readerDs = new SAXReader();
					docDs = readerDs.read(dsFile);
					Element dsPara = docDs.getRootElement().element("jdbc-data-source-params");
					String dsJndiName = dsPara.elementText("jndi-name");
					dataSourceMap.put(dataSourceName, dsJndiName);
				}
			}
			if (dataSourceMap.containsKey(dsName)) {
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "数据源名称[" + dsName + "]已经存在，请重新命名！");
				return result(execResultDto, resultMap);
			}
			if (dataSourceMap.containsValue(dsJndi)) {
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "数据源jndi[" + dsJndi + "]已经存在，请重新命名！");
				return result(execResultDto, resultMap);
			}
			int mark = -1;
			Element asn = root.element("admin-server-name");
			int index = list.indexOf(asn);
			if (dataSources == null || dataSources.size() == 0) {
				mark = index;
			} else {
				mark = index + dataSources.size();
			}
			{
				String namespace = root.getNamespaceURI();
				Element dataSourceElement = DocumentFactory.getInstance().createElement("jdbc-system-resource",
						namespace);
				Element dataSourceNameElement = dataSourceElement.addElement("name");
				dataSourceNameElement.setText(dsName);
				Element dataSourceTargetElement = dataSourceElement.addElement("target");
				dataSourceTargetElement.setText(dsTargetServer);
				Element dataSourceFileElement = dataSourceElement.addElement("descriptor-file-name");
				dataSourceFileElement.setText("jdbc" + File.separator + dsName + "-jdbc.xml");
				list.add(mark + 1, dataSourceElement);
				OutputStreamWriter osWriterConfig = new OutputStreamWriter(new FileOutputStream(config), wlsEncoding);
				XMLWriter xmlWriterConfig = new XMLWriter(osWriterConfig, format);
				xmlWriterConfig.write(docConfig);
				osWriterConfig.close();
				xmlWriterConfig.close();
			}
			{
				String dsConfig = wlsDomainPath + File.separator + "config" + File.separator + "jdbc" + File.separator
						+ dsName + "-jdbc.xml";
				File dataSourceFile = new File(dsConfig);
				System.out.println(dataSourceFile.getParent());
				if (!dataSourceFile.getParentFile().exists()) {
					dataSourceFile.getParentFile().mkdir();
				}
				dataSourceFile.createNewFile();
				Document docDataSource = DocumentHelper.createDocument();
				Element rootDataSource = docDataSource.addElement("jdbc-data-source",
						"http://xmlns.oracle.com/weblogic/jdbc-data-source");
				rootDataSource.addNamespace("sec", "http://xmlns.oracle.com/weblogic/security");
				rootDataSource.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
				rootDataSource.addNamespace("wls", "http://xmlns.oracle.com/weblogic/security/wls");
				rootDataSource.addAttribute("xsi:schemaLocation",
						"http://xmlns.oracle.com/weblogic/jdbc-data-source http://xmlns.oracle.com/weblogic/jdbc-data-source/1.0/jdbc-data-source.xsd");
				Element nameElement = rootDataSource.addElement("name");
				nameElement.setText(dsName);
				Element driverElement = rootDataSource.addElement("jdbc-driver-params");
				Element urlElement = driverElement.addElement("url");
				urlElement.setText(url);
				Element driverNameElement = driverElement.addElement("driver-name");
				driverNameElement.setText(driver);
				Element propertiesElement = driverElement.addElement("properties");
				Element propertyElement = propertiesElement.addElement("property");
				Element propertyNameElement = propertyElement.addElement("name");
				propertyNameElement.setText("user");
				Element propertyValueElement = propertyElement.addElement("value");
				propertyValueElement.setText(dbUsr);
				Element passwordElement = driverElement.addElement("password-encrypted");
				passwordElement.setText(WeblogicCryptUtil.encryptAES(wlsDomainPath, dbPwd));
				Element connectionElement = rootDataSource.addElement("jdbc-connection-pool-params");
				Element testElement = connectionElement.addElement("test-table-name");
				testElement.setText(testSql);
				Element paramsElement = rootDataSource.addElement("jdbc-data-source-params");
				Element jndiNameElement = paramsElement.addElement("jndi-name");
				jndiNameElement.setText(dsJndi);
				Element transactionsElement = paramsElement.addElement("global-transactions-protocol");
				transactionsElement.setText(transaction);
				OutputStreamWriter osWriterDS = new OutputStreamWriter(new FileOutputStream(dsConfig), wlsEncoding);
				XMLWriter xmlWriterDS = new XMLWriter(osWriterDS, format);
				xmlWriterDS.write(docDataSource);
				osWriterDS.close();
				xmlWriterDS.close();
				resultMap.put(Constants.Plugin.RESULT, true);
				resultMap.put(Constants.Plugin.MESSAGE, "新增数据源[" + dsName + "]成功！");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "数据源新增失败，reason:" + getStackTrace(e));
		}
		return result(execResultDto, resultMap);
	}

	private JobExecResultDto weblogicJMSQueueDelete(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		JobExecResultDto execResultDto = getJobExecResultDto(detailDTO);
		String domainDir = null;
		String queueName = null;
		String JMSModuleDescriptor = null;
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DOMAIN_DIR))) {
			domainDir = pluginInput.get(Constants.Plugin.DOMAIN_DIR).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.QUEUE_NAME))) {
			queueName = pluginInput.get(Constants.Plugin.QUEUE_NAME).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.JMS_MODULE_DESCRIPTOR))) {
			JMSModuleDescriptor = pluginInput.get(Constants.Plugin.JMS_MODULE_DESCRIPTOR).toString();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (JudgeUtil.isEmpty(domainDir)) {
			resultMap.put("result", false);
			resultMap.put("message", "参数domainDir为空");
			return result(execResultDto, resultMap);
		} else {
			File domainDirFile = new File(domainDir);
			if (!domainDirFile.exists()) {
				resultMap.put("result", false);
				resultMap.put("message", "weblogic的domain目录 " + domainDir + " 不存在");
				return result(execResultDto, resultMap);
			}
		}
		String JMSModuleDesPath = domainDir + File.separator + "config" + File.separator + JMSModuleDescriptor;
		if (JudgeUtil.isEmpty(JMSModuleDescriptor)) {
			resultMap.put("result", false);
			resultMap.put("message", "参数JMSModuleDescriptor为空");
			return result(execResultDto, resultMap);
		} else {
			File JMSModuleDescriptorFile = new File(JMSModuleDesPath);
			if (!JMSModuleDescriptorFile.exists()) {
				resultMap.put("result", false);
				resultMap.put("message", "JMS module的描述文件 " + JMSModuleDescriptor + " 不存在");
				return result(execResultDto, resultMap);
			}
		}
		if (JudgeUtil.isEmpty(queueName)) {
			resultMap.put("result", false);
			resultMap.put("message", "参数queueName为空");
			return result(execResultDto, resultMap);
		}
		XMLWriter writer = null;
		File JMSModuleDescriptorFile = null;
		OutputStream out = null;
		try {
			JMSModuleDescriptorFile = new File(JMSModuleDesPath);
			Document document = DocumentHelper.createDocument();
			SAXReader reader = new SAXReader();
			document = reader.read(JMSModuleDescriptorFile);
			Element root = document.getRootElement();
			// 判断是否存在同名队列
			List queues = root.elements("queue");
			Iterator it = queues.iterator();
			Element queueElement = null;
			while (it.hasNext()) {
				queueElement = (Element) it.next();
				String value = queueElement.attribute("name").getValue();
				if (value.equals(queueName)) {
					break;
				} else {
					queueElement = null;
				}
			}
			if (queueElement == null) {
				resultMap.put("result", false);
				resultMap.put("message", JMSModuleDescriptor + "不存在队列 " + queueName);
				return result(execResultDto, resultMap);
			}
			// 修改document,删除队列节点
			root.remove(queueElement);
			// 重新写入文件
			OutputFormat format = OutputFormat.createPrettyPrint();
			out = new FileOutputStream(JMSModuleDescriptorFile);
			writer = new XMLWriter(out, format);
			writer.write(document);
			writer.close();
			writer = null;
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message",
					"删除队列 " + queueName + " 发生异常：" + System.getProperty("line.separator") + getStackTrace(e));
			return result(execResultDto, resultMap);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		resultMap.put("result", true);
		resultMap.put("message", "删除队列成功");
		return result(execResultDto, resultMap);
	}

	private JobExecResultDto weblogicJMSQueueUpdate(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		JobExecResultDto execResultDto = getJobExecResultDto(detailDTO);
		String domainDir = null;
		String queueName = null;
		String queueJNDIName = null;
		String JMSModuleDescriptor = null;

		String thresholds_messages = null;
		String maximum_message_size = null;
		String thresholds_bytes = null;

		String bytes_high = null;
		String bytes_low = null;
		String messages_high = null;
		String messages_low = null;
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DOMAIN_DIR))) {
			domainDir = pluginInput.get(Constants.Plugin.DOMAIN_DIR).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.QUEUE_NAME))) {
			queueName = pluginInput.get(Constants.Plugin.QUEUE_NAME).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.QUEUE_JNDINAME))) {
			queueJNDIName = pluginInput.get(Constants.Plugin.QUEUE_JNDINAME).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.JMS_MODULE_DESCRIPTOR))) {
			JMSModuleDescriptor = pluginInput.get(Constants.Plugin.JMS_MODULE_DESCRIPTOR).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.THRESHOLDS_BYTES))) {
			thresholds_bytes = pluginInput.get(Constants.Plugin.THRESHOLDS_BYTES).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.THRESHOLDS_MESSAGES))) {
			thresholds_messages = pluginInput.get(Constants.Plugin.THRESHOLDS_MESSAGES).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.MAXIMUM_MESSAGE_SIZE))) {
			maximum_message_size = pluginInput.get(Constants.Plugin.MAXIMUM_MESSAGE_SIZE).toString();
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (JudgeUtil.isEmpty(domainDir)) {
			resultMap.put("result", false);
			resultMap.put("message", "参数domainDir为空");
			return result(execResultDto, resultMap);
		} else {
			File domainDirFile = new File(domainDir);
			if (!domainDirFile.exists()) {
				resultMap.put("result", false);
				resultMap.put("message", "weblogic的domain目录 " + domainDir + " 不存在");
				return result(execResultDto, resultMap);
			}
		}
		String JMSModuleDesPath = domainDir + File.separator + "config" + File.separator + JMSModuleDescriptor;
		if (JudgeUtil.isEmpty(JMSModuleDescriptor)) {
			resultMap.put("result", false);
			resultMap.put("message", "参数JMSModuleDescriptor为空");
			return result(execResultDto, resultMap);
		} else {
			File JMSModuleDescriptorFile = new File(JMSModuleDesPath);
			if (!JMSModuleDescriptorFile.exists()) {
				resultMap.put("result", false);
				resultMap.put("message", "JMS module的描述文件 " + JMSModuleDescriptor + " 不存在");
				return result(execResultDto, resultMap);
			}
		}
		if (JudgeUtil.isEmpty(queueName)) {
			resultMap.put("result", false);
			resultMap.put("message", "参数queueName为空");
			return result(execResultDto, resultMap);
		}
		XMLWriter writer = null;
		File JMSModuleDescriptorFile = null;
		OutputStream out = null;
		try {
			JMSModuleDescriptorFile = new File(JMSModuleDesPath);
			Document document = DocumentHelper.createDocument();
			SAXReader reader = new SAXReader();
			document = reader.read(JMSModuleDescriptorFile);
			Element root = document.getRootElement();
			// 判断是否存在队列queueName
			List queues = root.elements("queue");
			Iterator it = queues.iterator();
			Element queueElement = null;
			while (it.hasNext()) {
				queueElement = (Element) it.next();
				String value = queueElement.attribute("name").getValue();
				if (value.equals(queueName)) {
					break;
				} else {
					queueElement = null;
				}
			}
			if (queueElement == null) {
				resultMap.put("result", false);
				resultMap.put("message", JMSModuleDescriptor + "不存在队列 " + queueName);
				return result(execResultDto, resultMap);
			}
			// 修改document
			if (!JudgeUtil.isEmpty(queueJNDIName)) {
				queueElement.element("jndi-name").setText(queueJNDIName);
			}
			if (!JudgeUtil.isEmpty(thresholds_bytes)) {
				String[] byteRang = thresholds_bytes.split("-");
				if (byteRang.length > 1) {
					bytes_high = byteRang[1];
				}
				bytes_low = byteRang[0];
				Element thresholdElement = queueElement.element("thresholds");
				if (thresholdElement == null) {
					thresholdElement = queueElement.addElement("thresholds");
				}
				if (!JudgeUtil.isEmpty(bytes_high)) {
					Element bytesHighElement = thresholdElement.element("bytes-high");
					if (bytesHighElement == null) {
						bytesHighElement = thresholdElement.addElement("bytes-high");
					}
					bytesHighElement.setText(bytes_high);
				}
				if (!JudgeUtil.isEmpty(bytes_low)) {
					Element bytesLowElement = thresholdElement.element("bytes-low");
					if (bytesLowElement == null) {
						bytesLowElement = thresholdElement.addElement("bytes-low");
					}
					bytesLowElement.setText(bytes_low);
				}
			}
			if (!JudgeUtil.isEmpty(thresholds_messages)) {
				String[] messageRang = thresholds_messages.split("-");
				if (messageRang.length > 1) {
					messages_high = messageRang[1];
				}
				messages_low = messageRang[0];
				Element thresholdElement = queueElement.element("thresholds");
				if (thresholdElement == null) {
					thresholdElement = queueElement.addElement("thresholds");
				}
				if (!JudgeUtil.isEmpty(messages_high)) {
					Element messagesHighElement = thresholdElement.element("messages-high");
					if (messagesHighElement == null) {
						messagesHighElement = thresholdElement.addElement("messages-high");
					}
					messagesHighElement.setText(messages_high);
				}
				if (!JudgeUtil.isEmpty(messages_low)) {
					Element messagesLowElement = thresholdElement.element("messages-low");
					if (messagesLowElement == null) {
						messagesLowElement = thresholdElement.addElement("messages-low");
					}
					messagesLowElement.setText(messages_low);
				}

			}
			if (!JudgeUtil.isEmpty(maximum_message_size)) {
				Element element = queueElement.element("maximum-message-size");
				if (element == null) {
					element = queueElement.addElement("maximum-message-size");
				}
				element.setText(maximum_message_size);
			}
			// 重新写入文件
			OutputFormat format = OutputFormat.createPrettyPrint();
			out = new FileOutputStream(JMSModuleDescriptorFile);
			writer = new XMLWriter(out, format);
			writer.write(document);
			writer.close();
			writer = null;
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message",
					"修改队列 " + queueName + " 发生异常：" + System.getProperty("line.separator") + getStackTrace(e));
			return result(execResultDto, resultMap);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		resultMap.put("result", true);
		resultMap.put("message", "修改队列成功");
		return result(execResultDto, resultMap);
	}

	private JobExecResultDto weblogicJMSQueueCreate(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		JobExecResultDto execResultDto = getJobExecResultDto(detailDTO);
		String domainDir = null;
		String queueName = null;
		String queueJNDIName = null;
		String JMSModuleDescriptor = null;
		String subDeployName = null;
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.DOMAIN_DIR))) {
			domainDir = pluginInput.get(Constants.Plugin.DOMAIN_DIR).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.QUEUE_NAME))) {
			queueName = pluginInput.get(Constants.Plugin.QUEUE_NAME).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.QUEUE_JNDINAME))) {
			queueJNDIName = pluginInput.get(Constants.Plugin.QUEUE_JNDINAME).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.JMS_MODULE_DESCRIPTOR))) {
			JMSModuleDescriptor = pluginInput.get(Constants.Plugin.JMS_MODULE_DESCRIPTOR).toString();
		}

		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.SUB_DEPLOYNAME))) {
			subDeployName = pluginInput.get(Constants.Plugin.SUB_DEPLOYNAME).toString();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (JudgeUtil.isEmpty(domainDir)) {
			resultMap.put("result", false);
			resultMap.put("message", "参数domainDir为空");
			return result(execResultDto, resultMap);
		} else {
			File domainDirFile = new File(domainDir);
			if (!domainDirFile.exists()) {
				resultMap.put("result", false);
				resultMap.put("message", "weblogic的domain目录 " + domainDir + " 不存在");
				return result(execResultDto, resultMap);
			}
		}
		String JMSModuleDesPath = domainDir + File.separator + "config" + File.separator + JMSModuleDescriptor;
		if (JudgeUtil.isEmpty(JMSModuleDescriptor)) {
			resultMap.put("result", false);
			resultMap.put("message", "参数JMSModuleDescriptor为空");
			return result(execResultDto, resultMap);
		} else {
			File JMSModuleDescriptorFile = new File(JMSModuleDesPath);
			if (!JMSModuleDescriptorFile.exists()) {
				resultMap.put("result", false);
				resultMap.put("message", "JMS module的描述文件 " + JMSModuleDescriptor + " 不存在");
				return result(execResultDto, resultMap);
			}
		}
		if (JudgeUtil.isEmpty(subDeployName)) {
			resultMap.put("result", false);
			resultMap.put("message", "参数subDeployName为空");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(queueName)) {
			resultMap.put("result", false);
			resultMap.put("message", "参数queueName为空");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(queueJNDIName)) {
			resultMap.put("result", false);
			resultMap.put("message", "参数queueJNDIName为空");
			return result(execResultDto, resultMap);
		}
		XMLWriter writer = null;
		File JMSModuleDescriptorFile = null;
		OutputStream out = null;
		try {
			JMSModuleDescriptorFile = new File(JMSModuleDesPath);
			Document document = DocumentHelper.createDocument();
			SAXReader reader = new SAXReader();
			document = reader.read(JMSModuleDescriptorFile);
			Element root = document.getRootElement();
			// 判断是否存在同名队列
			List queues = root.elements("queue");
			Iterator it = queues.iterator();
			while (it.hasNext()) {
				Element element = (Element) it.next();
				String value = element.attribute("name").getValue();
				if (value.equals(queueName)) {
					resultMap.put("result", false);
					resultMap.put("message", JMSModuleDescriptor + "存在同名队列 " + queueName);
					return result(execResultDto, resultMap);
				}
			}
			// 修改document,添加队列节点
			Element queue = root.addElement("queue");
			queue.addAttribute("name", queueName);
			Element subDeployNameElement = queue.addElement("sub-deployment-name");
			subDeployNameElement.setText(subDeployName);
			Element jndiName = queue.addElement("jndi-name");
			jndiName.setText(queueJNDIName);
			// 重新写入文件
			OutputFormat format = OutputFormat.createPrettyPrint();
			out = new FileOutputStream(JMSModuleDescriptorFile);
			writer = new XMLWriter(out, format);
			writer.write(document);
			writer.close();
			writer = null;
			out = null;
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message",
					"添加队列 " + queueName + " 发生异常：" + System.getProperty("line.separator") + getStackTrace(e));
			return result(execResultDto, resultMap);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		resultMap.put("result", true);
		resultMap.put("message", "添加队列成功");
		return result(execResultDto, resultMap);
	}

	private JobExecResultDto appUndeploy4Weblogic(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		JobExecResultDto execResultDto = getJobExecResultDto(detailDTO);
		String wlsUsr = null;
		String wlsPwd = null;
		String appName = null;
		String wlsDomainPath = null;
		String wlsVersion = null;
		String wlsEncoding = null;
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSUSR))) {
			wlsUsr = pluginInput.get(Constants.Plugin.WLSUSR).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSPWD))) {
			wlsPwd = pluginInput.get(Constants.Plugin.WLSPWD).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.APPNAME))) {
			appName = pluginInput.get(Constants.Plugin.APPNAME).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSDOMAINPATH))) {
			wlsDomainPath = pluginInput.get(Constants.Plugin.WLSDOMAINPATH).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSVERSION))) {
			wlsVersion = pluginInput.get(Constants.Plugin.WLSVERSION).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSENCODING))) {
			wlsEncoding = pluginInput.get(Constants.Plugin.WLSENCODING).toString();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (JudgeUtil.isEmpty(wlsUsr)) {
			log.error("wlsUsr is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsUsr is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsPwd)) {
			log.error("wlsPwd is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsPwd is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(appName)) {
			log.error("appName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "appName is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsDomainPath)) {
			log.error("wlsDomainPath is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsDomainPath is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsVersion)) {
			log.error("wlsVersion is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsVersion is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsEncoding)) {
			log.error("wlsEncoding is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsEncoding is null");
			return result(execResultDto, resultMap);
		}

		if (!"12".equals(wlsVersion)) {
			log.error("weblogic版本是[" + wlsVersion + "]，当前插件只支持weblogic12版本！");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "weblogic版本是[" + wlsVersion + "]，当前插件只支持weblogic12版本！");
			return result(execResultDto, resultMap);
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
			return result(execResultDto, resultMap);
		}
		String wlsConfig = wlsDomainPath + File.separator + "config" + File.separator + "config.xml";
		File config = new File(wlsConfig);
		if (!config.exists()) {
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "weblogic配置文件[" + wlsConfig + "]不存在！");
			return result(execResultDto, resultMap);
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
			// 删除配置
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
			resultMap.put(Constants.Plugin.MESSAGE, "应用卸载失败，reason:" + getStackTrace(e));
			return result(execResultDto, resultMap);
		}
		return result(execResultDto, resultMap);
	}

	private JobExecResultDto appDeploy4Weblogic(JobDetailDto detailDTO, Map<String, Object> pluginInput) {
		JobExecResultDto execResultDto = getJobExecResultDto(detailDTO);
		String wlsUsr = null;
		String wlsPwd = null;
		String appName = null;
		String appPath = null;
		String appTarget = null;
		String wlsDomainPath = null;
		String wlsVersion = null;
		String wlsEncoding = null;
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSUSR))) {
			wlsUsr = pluginInput.get(Constants.Plugin.WLSUSR).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSPWD))) {
			wlsPwd = pluginInput.get(Constants.Plugin.WLSPWD).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.APPNAME))) {
			appName = pluginInput.get(Constants.Plugin.APPNAME).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.APPPATH))) {
			appPath = pluginInput.get(Constants.Plugin.APPPATH).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.APPTARGET))) {
			appTarget = pluginInput.get(Constants.Plugin.APPTARGET).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSDOMAINPATH))) {
			wlsDomainPath = pluginInput.get(Constants.Plugin.WLSDOMAINPATH).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSVERSION))) {
			wlsVersion = pluginInput.get(Constants.Plugin.WLSVERSION).toString();
		}
		if (!JudgeUtil.isEmpty(pluginInput.get(Constants.Plugin.WLSENCODING))) {
			wlsEncoding = pluginInput.get(Constants.Plugin.WLSENCODING).toString();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (JudgeUtil.isEmpty(wlsUsr)) {
			log.error("wlsUsr is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsUsr is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsPwd)) {
			log.error("wlsPwd is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsPwd is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(appName)) {
			log.error("appName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "appName is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(appPath)) {
			log.error("appPath is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "appPath is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(appTarget)) {
			log.error("appTarget is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "appTarget is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsDomainPath)) {
			log.error("wlsDomainPath is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsDomainPath is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsVersion)) {
			log.error("wlsVersion is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsVersion is null");
			return result(execResultDto, resultMap);
		}
		if (JudgeUtil.isEmpty(wlsEncoding)) {
			log.error("wlsEncoding is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "wlsEncoding is null");
			return result(execResultDto, resultMap);
		}
		if (!"12".equals(wlsVersion)) {
			log.error("weblogic版本是[" + wlsVersion + "]，当前插件只支持weblogic12版本！");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "weblogic版本是[" + wlsVersion + "]，当前插件只支持weblogic12版本！");
			return result(execResultDto, resultMap);
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
			return result(execResultDto, resultMap);
		}
		String wlsConfig = wlsDomainPath + File.separator + "config" + File.separator + "config.xml";
		File config = new File(wlsConfig);
		if (!config.exists()) {
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "weblogic配置文件[" + wlsConfig + "]不存在！");
			return result(execResultDto, resultMap);
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
			if (!app.exists()) {
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "appPath[" + appPath + "]不存在，无法完成部署！");
				return result(execResultDto, resultMap);
			} else {
				if (app.isDirectory()) {
					// 校验文件目录是否符合weblogic对于程序的规范
					String appPathWar = appPath + File.separator + "WEB-INF" + File.separator + "web.xml";
					File war = new File(appPathWar);
					String appPathEar = appPath + File.separator + "META-INF" + File.separator + "application.xml";
					File ear = new File(appPathEar);
					if (war.exists()) {
						moduleType = "war";
					} else if (ear.exists()) {
						moduleType = "ear";
					} else {
						resultMap.put(Constants.Plugin.RESULT, false);
						resultMap.put(Constants.Plugin.MESSAGE,
								"应用[" + appPath + "]既不符合weblogic对于web应用程序的目录规范也不符合企业级应用程序的目录规范，无法完成部署！");
						return result(execResultDto, resultMap);
					}
				} else if (app.isFile()) {
					if (!appPath.endsWith(".war") && !appPath.endsWith(".ear")) {
						resultMap.put(Constants.Plugin.RESULT, false);
						resultMap.put(Constants.Plugin.MESSAGE, "当前只支持后缀为war和ear的应用程序归档文件！");
						return result(execResultDto, resultMap);
					} else {
						ZipFile zip = new ZipFile(appPath);
						String warEntry_linux = "WEB-INF/web.xml";
						String warEntry_windows = "WEB-INF\\web.xml";
						String earEntry_linux = "META-INF/application.xml";
						String earEntry_windows = "META-INF\\application.xml";
						if (zip.getEntry(warEntry_linux) != null || zip.getEntry(warEntry_windows) != null) {
							moduleType = "war";
						} else if (zip.getEntry(earEntry_linux) != null || zip.getEntry(earEntry_windows) != null) {
							moduleType = "ear";
						} else {
							resultMap.put(Constants.Plugin.RESULT, false);
							resultMap.put(Constants.Plugin.MESSAGE,
									"应用[" + appPath + "]既不符合weblogic对于web应用程序的归档文件规范也不符合企业级应用程序的归档文件规范，无法完成部署！");
							return result(execResultDto, resultMap);
						}
					}
				} else {
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
			resultMap.put(Constants.Plugin.MESSAGE, "应用部署失败，reason:" + getStackTrace(e));
			return result(execResultDto, resultMap);
		}
		return result(execResultDto, resultMap);
	}

	private boolean deleteDataSource(File folder, String wlsEncoding, String dsName) throws Exception {
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

	public String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}

	private JobExecResultDto getJobExecResultDto(JobDetailDto detailDTO) {
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		return execResultDto;
	}

	private JobExecResultDto result(JobExecResultDto execResultDto, Map<String, Object> resultMap) {
		execResultDto.setSuccess((Boolean) resultMap.get(Constants.Plugin.RESULT));
		execResultDto.setMsg((String) resultMap.get(Constants.Plugin.RESULT_MESSAGE));
		return execResultDto;
	}

}
