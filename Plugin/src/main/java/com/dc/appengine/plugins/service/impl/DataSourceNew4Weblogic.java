package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class DataSourceNew4Weblogic extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(DataSourceNew4Weblogic.class);

	private static final String WLSUSR = "wlsUsr";
	private static final String WLSPWD = "wlsPwd";
	private static final String WLSDOMAINPATH = "wlsDomainPath";
	private static final String DSNAME = "dsName";
	private static final String DSJNDI = "dsJndi";

	private static final String DSTARGETSERVER = "dsTargetServer";
	private static final String DBTYPE = "dbType";
	private static final String DBJTA = "dbJTA";
	private static final String DBNAME = "dbName";
	private static final String DBIP = "dbIp";

	private static final String DBPORT = "dbPort";
	private static final String DBUSR = "dbUsr";
	private static final String DBPWD = "dbPwd";
	private static final String WLSVERSION = "wlsVersion";
	private static final String WLSENCODING = "wlsEncoding";

	private Map<String, String> dataSource = new HashMap<>();
	private String wlsUsr;
	private String wlsPwd;
	private String wlsDomainPath;
	private String dsName;
	private String dsJndi;

	private String dsTargetServer;
	private String dbType;
	private String dbJTA;
	private String dbName;
	private String dbIp;

	private String dbPort;
	private String dbUsr;
	private String dbPwd;
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
		if (JudgeUtil.isEmpty(dsJndi)) {
			log.error("dsJndi is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dsJndi is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(dsTargetServer)) {
			log.error("dsTargetServer is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dsTargetServer is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(dbType)) {
			log.error("dbType is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbType is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(dbJTA)) {
			log.error("dbJTA is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbJTA is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(dbName)) {
			log.error("dbName is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbName is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(dbIp)) {
			log.error("dbIp is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbIp is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(dbPort)) {
			log.error("dbPort is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbPort is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(dbUsr)) {
			log.error("dbUsr is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbUsr is null");
			return JSON.toJSONString(resultMap);
		}
		if (JudgeUtil.isEmpty(dbPwd)) {
			log.error("dbPwd is null!");
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE, "dbPwd is null");
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
			return JSON.toJSONString(resultMap);
		}
		boolean isConnected = WeblogicCryptUtil.testDBConnection(dbUsr, dbPwd, url, driver, testSql);
		if (!isConnected) {
			resultMap.put(Constants.Plugin.RESULT, false);
			resultMap.put(Constants.Plugin.MESSAGE,
					"url[" + url + "]driver[" + driver + "]user[" + dbUsr + "]的数据库连接失败，请检查数据库配置！");
			return JSON.toJSONString(resultMap);
		}
		Map<String, String> dataSourceMap = new HashMap<>();
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
				return JSON.toJSONString(resultMap);
			}
			if (dataSourceMap.containsValue(dsJndi)) {
				resultMap.put(Constants.Plugin.RESULT, false);
				resultMap.put(Constants.Plugin.MESSAGE, "数据源jndi[" + dsJndi + "]已经存在，请重新命名！");
				return JSON.toJSONString(resultMap);
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
			resultMap.put(Constants.Plugin.MESSAGE, "数据源新增失败，reason:" + LogRecord.getStackTrace(e));
		}
		return JSON.toJSONString(resultMap);
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

	public void setDsJndi(String dsJndi) {
		this.dsJndi = dsJndi;
	}

	public void setDsTargetServer(String dsTargetServer) {
		this.dsTargetServer = dsTargetServer;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public void setDbJTA(String dbJTA) {
		this.dbJTA = dbJTA;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public void setDbIp(String dbIp) {
		this.dbIp = dbIp;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public void setDbUsr(String dbUsr) {
		this.dbUsr = dbUsr;
	}

	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
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
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DSJNDI))) {
			this.dsJndi = this.pluginInput.get(DSJNDI).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DSTARGETSERVER))) {
			this.dsTargetServer = this.pluginInput.get(DSTARGETSERVER).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DBTYPE))) {
			this.dbType = this.pluginInput.get(DBTYPE).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DBJTA))) {
			this.dbJTA = this.pluginInput.get(DBJTA).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DBNAME))) {
			this.dbName = this.pluginInput.get(DBNAME).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DBIP))) {
			this.dbIp = this.pluginInput.get(DBIP).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DBPORT))) {
			this.dbPort = this.pluginInput.get(DBPORT).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DBUSR))) {
			this.dbUsr = this.pluginInput.get(DBUSR).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DBPWD))) {
			this.dbPwd = this.pluginInput.get(DBPWD).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(WLSVERSION))) {
			this.wlsVersion = this.pluginInput.get(WLSVERSION).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(WLSENCODING))) {
			this.wlsEncoding = this.pluginInput.get(WLSENCODING).toString();
		}
	}

}
