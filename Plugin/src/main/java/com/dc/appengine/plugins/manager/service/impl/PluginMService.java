package com.dc.appengine.plugins.manager.service.impl;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.plugins.manager.service.IPluginMService;
import com.dc.appengine.plugins.message.sender.CommandSender;
import com.dc.appengine.plugins.service.impl.PluginService;
import com.dc.appengine.plugins.transfer.ITransferer;
import com.dc.appengine.plugins.transfer.impl.FtpTransferer;
import com.dc.appengine.plugins.utils.ConfigHelper;
import com.dc.appengine.plugins.utils.FileUtil;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.MessageHelper;
import com.dc.appengine.plugins.utils.SortUtil;

public class PluginMService extends CommandSender implements IPluginMService {
	private static final Logger log = LoggerFactory.getLogger(PluginMService.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private LoadPlugin loadPlugin = new LoadPlugin();

	public PluginMService() {
	}

	@Override
	public synchronized String registPlugin(Map<String, Object> param) {
		// TODO Auto-generated method stub
		String resultM = "";
		String localTmp = UUID.randomUUID().toString();
		String remotePath = UUID.randomUUID().toString();
		String fileParent = ConfigHelper.getValue("ftp.tmpDir")+File.separator+localTmp;
		ITransferer transferer = null;
		try {
			String fileName = param.get("fileName").toString();
//			Map<String, Object> map = getPlugin(param.get("pluginName").toString());
			Map<String, Object> map = PluginService.getInstance().getPlugin(param.get("pluginName").toString());
			if (map != null && !map.isEmpty()) {
				return MessageHelper.wrap("result", false, "message", "存在同名插件！");
			}
			// 生成plugin.json文件
			param.put("path", remotePath + "/" + fileName.replace(".jar", ".zip"));
			param.put("createTime", sdf.format(new Date()));
			String localJsonFilePath = createPluginJson(fileParent, param);
			// 将jar包封装成zip包
			String localZipFile = createPluginZip(fileParent, param);
			// 将插件zip包上传到ftp上
			transferer = new FtpTransferer();
			boolean testResult = transferer.open();
			if (!testResult) {
				log.error("无法连接存储目标！");
				return MessageHelper.wrap("result", false, "message", "无法连接存储目标！");
			}
			String resultUpload = transferer.upload(localZipFile, remotePath, fileName.replace(".jar", ".zip"));
			if (resultUpload.equals("false")) {
				log.error("上传失败，插件包上传失败！");
				return MessageHelper.wrap("result", false, "message", "插件包上传失败！");
			} else {
				// 修改plugins.json
				File pluginsJson = transferer.download("plugins.json", localTmp+File.separator+"plugins.json");
				if (pluginsJson ==null) {
					return MessageHelper.wrap("result", false, "message", "下载plugins.json失败！");
				}
				FileUtil.modifyPluginsJson(localJsonFilePath, pluginsJson.getAbsolutePath());
				transferer.delete("plugins.json");//删除ftp上的plugins.json
				String jsonUpl = transferer.upload(pluginsJson.getAbsolutePath(), "", "plugins.json");
				if (jsonUpl.equals("false")) {
					log.error("上传plugins.json失败！");
					return MessageHelper.wrap("result", false, "message", "上传plugins.json失败！");
				}
			}
			loadPlugin.dealPlugin(param, "add");// 本地load插件
			sendNodeTopic(param, "add");
			resultM = MessageHelper.wrap("result", true, "message", "插件创建成功！");
		} catch (Exception e) {
			// TODO: handle exception
			log.error("插件包创建失败！", e.getMessage());
			resultM = MessageHelper.wrap("result", false, "message", "插件包创建失败！");
		} finally {
			if (fileParent != null) {
				File f = new File(fileParent);
				if (f.exists()) {
					FileUtil.delDirectory(f);
				}
			}
			if (transferer != null) {
				transferer.close();
			}
		}
		return resultM;
	}

	@Override
	public synchronized String updatePlugin(Map<String, Object> param) {
		// TODO Auto-generated method stub
		String resultM = MessageHelper.wrap("result", false, "message", "插件更新失败！");
		String parentPath = null;
		ITransferer transferer = null;
		try {
			String pluginName = param.get("pluginName").toString();
			Map<String, Object> map = getPlugin(pluginName);
			map.putAll(param);
			param=map;
			String filePath = map.get("path").toString();
			String fileName = map.get("fileName").toString();
			// 测试连接
			transferer = new FtpTransferer();
			boolean testResult = transferer.open();
			if (!testResult) {
				log.error("无法连接存储目标！");
				resultM = MessageHelper.wrap("result", false, "message", "无法连接存储目标！");
				return resultM;
			}
			// 修改plugins.json
			File pluginsJson = transferer.download("plugins.json", "plugins.json");
			if (filePath == null || "".endsWith(filePath)) {// 内置插件
				FileUtil.modifyPluginsJson(pluginsJson.getAbsolutePath(), param, "update");
			} else {
				String remotePath = filePath.substring(0, filePath.lastIndexOf("/"));
				String localTmp = remotePath;
				File pluginFile = transferer.download(filePath,
						localTmp + File.separator + fileName.replace(".jar", ".zip"));
				parentPath = pluginFile.getParent();
				if (FileUtil.unZipFile(pluginFile.getAbsolutePath(), parentPath)) {
					FileUtil.modifyPluginsJson(parentPath + "/" + "plugin.json", param, "update");
					Boolean resultZ = FileUtil.createZipFile(parentPath, parentPath,
							fileName.substring(0, fileName.lastIndexOf(".jar")));
					String localZipFile = parentPath + File.separator
							+ fileName.substring(0, fileName.lastIndexOf(".jar")) + ".zip";
					String resultUpl = transferer.upload(localZipFile, remotePath, fileName.replace(".jar", ".zip"));
					if (resultUpl.equals("false")) {
						log.error("上传失败，插件包上传失败！");
						resultM = MessageHelper.wrap("result", false, "message", "插件包上传失败！");
						return resultM;
					}
					FileUtil.modifyPluginsJson(parentPath + "/" + "plugin.json", pluginsJson.getAbsolutePath());
					transferer.delete("plugins.json");
				};
			}
			String jsonUpl = transferer.upload(pluginsJson.getAbsolutePath(), "", "plugins.json");
			if (jsonUpl.equals("false")) {
				log.error("上传plugins.json失败！");
				resultM = MessageHelper.wrap("result", false, "message", "上传plugins.json失败！");
			}else{
				loadPlugin.dealPlugin(param, "update");// 本地load插件
				sendNodeTopic(param, "update");
				resultM = MessageHelper.wrap("result", true, "message", "插件更新成功！");
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error("更新插件失败！", e.getMessage());
		} finally {
			if (parentPath != null) {
				File f = new File(parentPath);
				if (f.exists()) {
					FileUtil.delDirectory(f);
				}
			}
			if (transferer != null) {
				transferer.close();
			}
		}
		return resultM;
	}

	@Override
	public synchronized String deletePlugin(Map<String, Object> param) {
		// TODO Auto-generated method stub
		String resultM = MessageHelper.wrap("result", false, "message", "插件删除失败！");
		String parentPath = null;
		ITransferer transferer = null;
		try {
			String pluginName = param.get("pluginName").toString();
			Map<String, Object> map = getPlugin(pluginName);
			map.putAll(param);
			param=map;
			String filePath = param.get("path").toString();
			String fileName = param.get("fileName").toString();
			if (filePath == null || "".equals(filePath)) {
				return MessageHelper.wrap("result", false, "message", "内置插件不允许删除！");
			}
			// 测试连接
			// ITransferer transferer =
			// TransfererFactory.getInstance().getTransferer();
			transferer = new FtpTransferer();
			boolean testResult = transferer.open();
			if (!testResult) {
				log.error("无法连接存储目标！");
				resultM = MessageHelper.wrap("result", false, "message", "无法连接存储目标！");
			}
			String dir =filePath.substring(0, filePath.indexOf("/"));
			if(JudgeUtil.isEmpty(dir)){
				return MessageHelper.wrap("result", false, "message", "不能删除空目录！");
			}
			transferer.deleteDir(dir);
			// transferer.delete(filePath);
			// 修改plugins.json
			File pluginsJson = transferer.download("plugins.json", "plugins.json");
			FileUtil.modifyPluginsJson(pluginsJson.getAbsolutePath(), param, "delete");
			transferer.delete("plugins.json");
			String jsonUpl = transferer.upload(pluginsJson.getAbsolutePath(), "", "plugins.json");
			if (jsonUpl.equals("false")) {
				log.error("上传plugins.json失败！");
				resultM = MessageHelper.wrap("result", false, "message", "上传plugins.json失败！");
			}
			loadPlugin.dealPlugin(param, "delete");
			sendNodeTopic(param, "delete");
			resultM = MessageHelper.wrap("result", true, "message", "插件删除成功！");
		} catch (Exception e) {
			// TODO: handle exception
			log.error("插件删除失败", e.getMessage());
		} finally {
			if (transferer != null) {
				transferer.close();
			}
		}
		return resultM;
	}

	@Override
	public Map<String, Object> listPugins(JSONObject param) {
		String pluginName=param.getString("pluginName");
		String pluginNames=param.getString("pluginNames");
		String hasLabel=param.getString("hasLabel");
		String sortName=JudgeUtil.isEmpty(param.getString("sortName"))?"pluginName":param.getString("sortName");
		String sortOrder=param.getString("sortOrder");
		int pageNum=param.getInteger("pageNum");
		int pageSize=param.getInteger("pageSize");
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();//返回给页面的list
		Map<String, Object> map=new HashMap<String, Object>();//记录plugins.json中的所有插件
		int total =0;//符合条件的插件总个数
		Map<String,Map<String, Object>> keyList = new HashMap<>();//每页展示的插件名集合
		map = PluginService.getInstance().getPuginsRecordMap();
		Boolean likeSelect = false;
		Pattern p = null;
		List<Map<String, Object>> listTmp = new ArrayList<Map<String, Object>>();//用于排序
		if (pluginName != null && !"".equals(pluginName)) {
			p = Pattern.compile("(?i)"+pluginName);
			likeSelect = true;
		}
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Map<String, Object> value = (Map<String, Object>) entry.getValue();
			//1.模糊过滤
			if (likeSelect) {
				Matcher m= p.matcher(key);
				if (m.find()) {
					listTmp.add(value);
					keyList.put(key, value);
				}
			}else{
				listTmp.add(value);
				keyList.put(key, value);
			}
			//2.标签过滤
			if(!JudgeUtil.isEmpty(JSONArray.parseArray(pluginNames))){
				JSONArray pluginNamesList = JSONArray.parseArray(pluginNames);//pluginNamesList标识有标签的插件名集合
				if(JudgeUtil.isEmpty(hasLabel)){//hasLabel，标识查询类型，非空时，标识查询有标签，为空时，标识查询没有标签的插件
					//若查询有标签的插件
					if(!pluginNamesList.contains(key)){
						int index = listTmp.indexOf(keyList.get(key));
						listTmp.remove(index);
						keyList.remove(key);
					}
				}else{
					//若查询没有标签的插件
					if(pluginNamesList.contains(key)){
						int index = listTmp.indexOf(keyList.get(key));
						listTmp.remove(index);
						keyList.remove(key);
					}
				}
			}
		}
		//3.分页
		if (!listTmp.isEmpty()) {
			total=listTmp.size();
			SortUtil.sort(listTmp, SortUtil.getColunmName("plugin", sortName), sortOrder);
			if (pageSize != 0 && pageNum != 0) {
				int startIndex = pageSize * (pageNum - 1);
				int endIndex = pageSize * pageNum - 1;
				if (startIndex < total) {
					endIndex = endIndex < total - 1 ? endIndex : total - 1;
					if (endIndex != startIndex) {
						listTmp = listTmp.subList(startIndex, endIndex + 1);
					} else {
						List<Map<String, Object>> tmpKeyList = new ArrayList<Map<String, Object>>();
						tmpKeyList.add(listTmp.get(endIndex));
						listTmp = tmpKeyList;
					}
				}
			}
		}
		result.put("pageNumber", pageNum);
		result.put("pageSize", pageSize);
		result.put("rows", listTmp);
		result.put("total", total);
		return result;
	}

	@Override
	public Map<String, Object> getPlugin(String pluginName) {
		Map<String, Object> map = new HashMap<>();
		ITransferer transferer = null;
		String localTmp = UUID.randomUUID().toString();
		String fileParent = ConfigHelper.getValue("ftp.tmpDir")+File.separator+localTmp;
		try {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			// 测试连接
			transferer = new FtpTransferer();
			boolean testResult = transferer.open();
			if (!testResult) {
				log.error("无法连接存储目标！");
				return map;
			}
			File pluginsJson = transferer.download("plugins.json", localTmp+File.separator+"plugins.json");
			if (pluginsJson != null) {
				list = FileUtil.readPluginsJson(pluginsJson.getAbsolutePath());
				for (Map<String, Object> one : list) {
					if (pluginName.equals(one.get("pluginName").toString())) {
						map = one;
					}
				}
			}
		} finally {
			if (transferer != null) {
				transferer.close();
			}
			if (fileParent != null) {
				File f = new File(fileParent);
				if (f.exists()) {
					FileUtil.delDirectory(f);
				}
			}
		}
		// TODO Auto-generated method stub
		return map;

	}

	public String createPluginJson(String localPath, Map<String, Object> param) {
		String fileName = param.get("fileName").toString();
		String fileParent = localPath + File.separator + fileName.substring(0, fileName.lastIndexOf(".jar"));
		File fileParentP = new File(fileParent);
		File file = new File(fileParent + File.separator + "plugin.json");
		Map<String, Object> pluginM = new HashMap<String, Object>();
		Map<String, Object> jsonM = new HashMap<String, Object>();
		jsonM.putAll(param);
		jsonM.remove("fileInput");
		pluginM.put(param.get("pluginName").toString(), jsonM);
		Boolean result = FileUtil.writeFile(JSON.toJSONString(pluginM), fileParentP.getAbsolutePath(), "plugin.json");
		if (result) {
			return file.getAbsolutePath();
		}
		return null;
	}

	public String createPluginZip(String localPath, Map<String, Object> param) {
		String fileName = param.get("fileName").toString();
		String fileParent = localPath + File.separator + fileName.substring(0, fileName.lastIndexOf(".jar"));
		File fileParentP = new File(fileParent);
		Boolean resultW = FileUtil.writeFileIO((InputStream) param.get("fileInput"), fileParentP.getAbsolutePath(),
				fileName);
		Boolean resultZ = null;
		if (resultW) {
			resultZ = FileUtil.createZipFile(fileParentP.getAbsolutePath(), fileParentP.getAbsolutePath(),
					fileName.substring(0, fileName.lastIndexOf(".jar")));
		}
		if (resultZ) {
			return fileParentP.getAbsolutePath() + File.separator + fileName.substring(0, fileName.lastIndexOf(".jar"))
					+ ".zip";
		}
		return null;
	}

	public static void main(String[] args) {
		// ITransferer transferer = new FtpTransferer("127.0.0.1",
		// Integer.valueOf("21"), "hansn", "123456", "30000", "/","../tmp");
		// boolean testResult = transferer.open();
		// if (!testResult) {
		// log.error("无法连接存储目标！");
		// }
		// transferer.deleteDir("216f0c54-bd50-4177-a1dd-ed21581753f4");
//		int pageSize = 3;
//		int pageNum = 2;
//		int startIndex = pageSize * (pageNum - 1);
//		int toIndex = pageSize * pageNum - 1;
//		List<String> keyList = new ArrayList<String>();
//		keyList.add("1");
//		keyList.add("2");
//		keyList.add("3");
//		keyList.add("4");
//		// keyList.add("5");
//		if (startIndex < keyList.size()) {
//			toIndex = toIndex < keyList.size() ? toIndex : keyList.size();
//			keyList = keyList.subList(startIndex, toIndex);
//			System.out.println(JSON.toJSONString(keyList));
//		} else {
//			System.out.println("下标越界");
//		}
		String pluginName = "cmd";
		String key="ffffCMDDDD";
		Pattern p = Pattern.compile("(?i)"+pluginName);
		if(p.matcher(key).find()){
			System.out.println("dddd");
		}
	}
}
