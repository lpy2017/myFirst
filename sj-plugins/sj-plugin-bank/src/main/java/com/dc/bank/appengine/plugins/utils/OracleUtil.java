package com.dc.bank.appengine.plugins.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.dc.bank.appengine.plugins.constants.Constants;
import com.dc.bank.appengine.plugins.jaxb.Filter;
import com.dc.bank.appengine.plugins.jaxb.FilterList;
import com.dc.bank.appengine.plugins.jaxb.Item;
import com.dc.bank.appengine.plugins.jaxb.SqlFile;
import com.dc.bank.appengine.plugins.jaxb.SqlFileList;

public class OracleUtil {
	String oracleUrl;
	int timeOut=60;
	String SUCCESSRE=null;
	String FAILRE=null;
	
	public OracleUtil(String oracleUrl,int timeOut){
		this.oracleUrl=oracleUrl;
		this.timeOut=timeOut;
	}
	public Map<String,Object> parsePatchPackage(String patchFileRootPath,String appName,String bakPath){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		File patchRootFile = new File(patchFileRootPath);
		if(patchRootFile.exists()){
			File[] appFiles = patchRootFile.listFiles(new FileFilterClass("isDirectory",appName));//多个应用
			for(int i=0;i<appFiles.length;i++){
				String bakRootPath=bakPath+File.separator+appFiles[i].getName();
				File[] fileXMLFiles = appFiles[i].listFiles(new FileFilterClass("isFile",Constants.FILTER,Constants.FILE_SUFFIX_XML));
				if(fileXMLFiles.length!=0){
					resultMap= generateBakFile(fileXMLFiles[0],bakRootPath);
					if(!(Boolean)resultMap.get("result")){
						return resultMap;
					}
				}else{
					continue;
				}
			}
		}else{
			resultMap.put("result", false);
			resultMap.put("message", "补丁的根目录不存在");
		}
		return resultMap;
	}
	public Map<String,Object> generateBakFile(File xmlFile,String bakRootPath){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		InputStream xml=null;
		StringBuffer sb = new StringBuffer();
		try {
			xml = new FileInputStream(xmlFile);
			XMLParser xp = new XMLParser();
			FilterList filters = (FilterList) xp.unmarshall(xml, FilterList.class);
			List<Filter> list =filters.getFilter();
			for(Filter filter:list){
				String id = filter.getId();
				Item item =filter.getItem();
				if(item ==null){
					continue;
				}
				if(!Constants.FITER_TABLE.equals(id)&&!Constants.FITER_PKB.equals(id)){
					continue;
				}
				resultMap = bake(item, id,bakRootPath);
				sb.append("==============="+resultMap.get("message")+SystemUtil.getLineSeparator()+"==============="+SystemUtil.getLineSeparator());
				if(!(Boolean)resultMap.get("result")){
					break;
				}
			}
			resultMap.put("message", sb.toString());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "补丁描述文件不存在");
		} finally {
			if(xml!=null)
				try {
					xml.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return resultMap;
	}
	
	public Map<String,Object> bake(Item item,String type,String bakPath){
		Boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
		Map<String,Object> result = new HashMap<String,Object>();
		String CMD="";
		String filePath="";
		String name=item.getName();
		String SUCCESSRE=null;
		String FAILRE=null;
		if(Constants.FITER_TABLE.equals(type)){
			SUCCESSRE="导出终止成功";
			FAILRE="导出终止失败";
			String isBackupData=item.getIsBackupData();
			String query="\\\" where 1=1\\\"";
			if(!"true".equals(isBackupData)){
				query="\\\" where 1=2\\\"";
			}
			filePath=bakPath+File.separator+name+".dmp";
			if(!isWin&&filePath.contains("\\")){
				filePath=filePath.replace("\\", File.separator);
			}else if(isWin&&filePath.contains("//")){
				filePath=filePath.replace("//", File.separator);
			}
			File file = new File(filePath);
			File fileParent = new File(bakPath);
			if(!fileParent.exists()){
				fileParent.mkdirs();
			}else{
				if(file.exists()){
					file.delete();
				}
			}
			CMD="exp "+oracleUrl+" tables="+name+" file="+filePath+" query="+query;
		}else if(Constants.FITER_PKB.equals(type)){
			//执行pkb的备份
			filePath=bakPath+File.separator+name+".pkb";
			if(!isWin&&filePath.contains("\\")){
				filePath=filePath.replace("\\", File.separator);
			}else if(isWin&&filePath.contains("//")){
				filePath=filePath.replace("//", File.separator);
			}
			File file = new File(filePath);
			File fileParent = new File(bakPath);
			if(!fileParent.exists()){
				fileParent.mkdirs();
			}else{
				if(file.exists()){
					file.delete();
				}
			}
			String params=filePath+" "+Constants.PKBObject+" "+name+" "+Constants.PKB;
			String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
			String pathR;
			String confPath="";
			try {
				pathR = URLDecoder.decode(path, "utf-8");
				confPath=pathR.substring(0, pathR.lastIndexOf("/"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			File templateFile = new File(confPath+File.separator+"../template"+File.separator+"scriptTemplate"+File.separator+"PkbBakTemplate.sql");
			if(!templateFile.exists()){
				result.put("result", false);
				result.put("message", "pkb的模板文件："+templateFile.getPath()+" 不存在");
				return result;
			}
			//执行备份命令
			CMD="sqlplus "+oracleUrl+" @"+templateFile+" "+params;
		}
		//执行备份命令
		String message="";
		System.out.println("==========CMD "+CMD);
		JSONObject cmdParams = new JSONObject();
		cmdParams.put("CMD", CMD);
		cmdParams.put("timeOut", timeOut+"");
		cmdParams.put("SUCCESSRE", SUCCESSRE);
		cmdParams.put("FAILRE", FAILRE);
		result =CommandUtil.excuteCommmand(cmdParams,null);
		if((Boolean)result.get("result")){
			result.put("result", true);
			String messageTmp=type+"["+name+"]"+"备份成功："+result.get("message");
			message=message+SystemUtil.getLineSeparator()+messageTmp;
			result.put("message", message);
		}else{
			result.put("result", false);
			result.put("message", type+"["+name+"]"+"备份失败："+result.get("message"));
		}
		return result;
	}
	
	public Map<String,Object> parseXml(String patchRootPath, String xmlFile) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		InputStream xml=null;
		StringBuffer sb = new StringBuffer();
		try {
			xml = new FileInputStream(xmlFile);
			XMLParser xp = new XMLParser();
			SqlFileList sqlFileList = (SqlFileList) xp.unmarshall(xml, SqlFileList.class);
			List<SqlFile> sqlFiles =sqlFileList.getSqlFiles();
			Collections.sort(sqlFiles);//按seq对sqlFiles进行排序
			for(SqlFile sqlFile:sqlFiles){
				String abPath = patchRootPath+File.separator+sqlFile.getPath();
				//修改脚本，追加命令退出标识"exit"
				if(abPath.endsWith(".pkb")){
					FileUtil.modifyFile(abPath, Constants.FILE_SUFFIX_PKB);
				}else{
					FileUtil.modifyFile(abPath, Constants.FILE_SUFFIX_SQL);
				}
				resultMap =excuteUpgrade(abPath);
				sb.append("==============="+resultMap.get("message")+SystemUtil.getLineSeparator()+"==============="+SystemUtil.getLineSeparator());
				if(!(Boolean)resultMap.get("result")){
					break;
				}
			}
			resultMap.put("message", sb.toString());
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "补丁的升级规则文件不存在");
		} finally {
			if(xml!=null)
				try {
					xml.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return resultMap;
	}

	private Map<String,Object> excuteUpgrade(String sqlPath) {
		Boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
		Map<String,Object> result = new HashMap<String,Object>();
		String CMD="";
		String filePath=sqlPath;
		//执行pkb的备份
		if(!isWin&&filePath.contains("\\")){
			filePath=filePath.replace("\\", File.separator);
		}else if(isWin&&filePath.contains("//")){
			filePath=filePath.replace("//", File.separator);
		}
		//执行命令
		CMD="sqlplus "+oracleUrl+" @"+filePath;
		//执行命令
		String message="";
		System.out.println("==========CMD "+CMD);
		JSONObject cmdParams = new JSONObject();
		cmdParams.put("CMD", CMD);
		cmdParams.put("timeOut", timeOut+"");
		cmdParams.put("SUCCESSRE", SUCCESSRE);
		cmdParams.put("FAILRE", FAILRE);
		result =CommandUtil.excuteCommmand(cmdParams,null);
		if((Boolean)result.get("result")){
			result.put("result", true);
			String messageTmp="["+filePath+"]"+"执行成功："+result.get("message");
			message=message+SystemUtil.getLineSeparator()+messageTmp;
			result.put("message", message);
		}else{
			result.put("result", false);
			result.put("message", "["+filePath+"]"+"执行失败："+result.get("message"));
		}
		return result;
	}
	
	public Map<String,Object> excuteRollBack(String sqlPath) {
		Boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
		Map<String,Object> result = new HashMap<String,Object>();
		String CMD="";
		String filePath=sqlPath;
		//执行pkb的备份
		if(!isWin&&filePath.contains("\\")){
			filePath=filePath.replace("\\", File.separator);
		}else if(isWin&&filePath.contains("//")){
			filePath=filePath.replace("//", File.separator);
		}
		if(filePath.endsWith(Constants.FILE_SUFFIX_PKB)){
			CMD="sqlplus "+oracleUrl+" @"+filePath;
			FileUtil.modifyFile(filePath, Constants.FILE_SUFFIX_PKB);
		}else if(filePath.endsWith(Constants.FILE_SUFFIX_DMP)){
			CMD="imp "+this.oracleUrl+" file="+filePath+" full=y";
		}
		//执行命令
		String message="";
		System.out.println("==========CMD "+CMD);
		JSONObject cmdParams = new JSONObject();
		cmdParams.put("CMD", CMD);
		cmdParams.put("timeOut", timeOut+"");
		cmdParams.put("SUCCESSRE", SUCCESSRE);
		cmdParams.put("FAILRE", FAILRE);
		result =CommandUtil.excuteCommmand(cmdParams,null);
		if((Boolean)result.get("result")){
			result.put("result", true);
			String messageTmp="["+filePath+"]"+"执行成功："+result.get("message");
			message=message+SystemUtil.getLineSeparator()+messageTmp;
			result.put("message", message);
		}else{
			result.put("result", false);
			result.put("message", "["+filePath+"]"+"执行失败："+result.get("message"));
		}
		return result;
	}
	
	
	public static void main(String[] args){
		URL url = OracleUtil.class.getResource("/");
		System.out.println(url.getPath());
		File file = new File("../conf"+File.separator+"test.txt");
		if(file.exists()){
			System.out.println("2222");
		}
//		URL url1 = OracleUtil.class.getResource("../conf"+File.separator+"test.txt");
//		System.out.println(url1.getPath());
		
	}
}
