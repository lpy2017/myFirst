package com.dc.appengine.plugins.service.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;

public class SqlServer extends AbstractPlugin {
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(SqlServer.class);

	private static final String FAILRE_KEY  = "failRE";
	private static final String SUCCESSRE_KEY  = "successRE";
	private static final String LONGTASK = "longTask";
	private static final String USERNAME = "userName";
	private static final String PASSWORD = "password";
	private static final String PORT = "port";
	private static final String IP = "ip";

	private String FAILRE;
	private String SUCCESSRE;
	private String dbip;
	private String dbport;
	private String dbname;
	private String dbusername;
	private String dbpasswd;
	private String sql;



	public String doPreAction(){
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke(){
		sendMessageByMOM(this.paramMap, this.messageMap,paramMap.get(Constants.Plugin.PLUGINNAME).toString());
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doPostAction() {
		return JSON.toJSONString(paramMap);
	}


	@Override
	public String doAgent() throws Exception {
		final Map<String,Object> result = new HashMap<>();
		String dbip = this.dbip;
		String dbport = this.dbport;
		String dbname = this.dbname;
		String dbusername = this.dbusername;
		String dbpasswd = this.dbpasswd;
		String sql = this.sql;
		Connection conn;
		Statement stmt;
		ResultSet rs;
		String url;

		Map<String,Object> resultCmd= new HashMap<>();
		try {
			if(JudgeUtil.isEmpty(sql)){
				resultCmd.put(Constants.Plugin.RESULT,false);
				resultCmd.put(Constants.Plugin.MESSAGE,"sql语句为空");
				return JSON.toJSONString(resultCmd);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			resultCmd.put(Constants.Plugin.RESULT,false);
			resultCmd.put(Constants.Plugin.MESSAGE,"解密sql语句异常"+e.getMessage());
			return JSON.toJSONString(resultCmd);
		}

		String url1 = "jdbc:sqlserver://"+dbip+":"+dbport+";";
		String url2 = "jdbc:sqlserver://"+dbip+":"+dbport+";"+"DatabaseName="+dbname+";";

		String[] sqls =sql.split(";");

		String noDatabaseSql = "";
		String DatabaseSql = "";
		for (String subsql : sqls) {
			if(subsql.contains("CREATE DATABASE")||subsql.contains("DROP DATABASE")||subsql.contains("RESTORE DATABASE")){
				noDatabaseSql = noDatabaseSql.concat(subsql).concat(";");

			}else{
				DatabaseSql = DatabaseSql.concat(subsql+";");
			}
		}
		boolean res1 = true;
		boolean res2 = true;
		if (noDatabaseSql != null && !"".equals(noDatabaseSql)) {

			res1 = exectueSql(url1, dbusername, dbpasswd, noDatabaseSql);
		}
		if (DatabaseSql != null && !"".equals(DatabaseSql)) {

			res2 = exectueSql(url2, dbusername, dbpasswd, DatabaseSql); 
		}

		if (res1&&res2) {

			result.put(Constants.Plugin.RESULT, true);
		}else{

			result.put(Constants.Plugin.RESULT, false);
		}


		return JSON.toJSONString(result);

	}

	public  boolean exectueSql(String url,String dbusername,String dbpasswd,String sql){
		try {
			Connection conn = DriverManager.getConnection(url, dbusername , dbpasswd);
			Statement stmt = conn.createStatement();
			stmt.execute(sql);

			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
			if (conn != null) {
				conn.close();
				conn = null;
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("数据库连接失败");
			return false;
		}
	}

	@Override
	public void setFields() {
		if(!JudgeUtil.isEmpty(this.pluginInput.get(FAILRE_KEY ))){
			this.FAILRE=this.pluginInput.get(FAILRE_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(SUCCESSRE_KEY ))){
			this.SUCCESSRE=this.pluginInput.get(SUCCESSRE_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get("dbip"))){
			this.dbip=this.pluginInput.get("dbip").toString();
		}

		if(!JudgeUtil.isEmpty(this.pluginInput.get("dbport"))){
			this.dbport=this.pluginInput.get("dbport").toString();
		}

		if(!JudgeUtil.isEmpty(this.pluginInput.get("dbname"))){
			this.dbname=this.pluginInput.get("dbname").toString();
		}

		if(!JudgeUtil.isEmpty(this.pluginInput.get("dbusername"))){
			this.dbusername=this.pluginInput.get("dbusername").toString();
		}

		if(!JudgeUtil.isEmpty(this.pluginInput.get("dbpasswd"))){
			this.dbpasswd=this.pluginInput.get("dbpasswd").toString();
		}

		if(!JudgeUtil.isEmpty(this.pluginInput.get("sql"))){
			this.sql=this.pluginInput.get("sql").toString();
		}

	}

}
