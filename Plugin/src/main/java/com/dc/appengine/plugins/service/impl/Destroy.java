package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.FilePath;
import com.dc.appengine.plugins.utils.JudgeUtil;


public class Destroy extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(Destroy.class);
	private static final String DESTROYPATH_KEY="destroyPath";
	private String destroyPath;
	/*
	 * map中需要两个值，
		流程id,组件名称
		(节点属性中的一个值) 
	 */
	@Override
	public String doPreAction(){
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke(){
		sendMessageByMOM(this.paramMap, this.messageMap,paramMap.get(Constants.Plugin.PLUGINNAME).toString());
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doPostAction(){
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent() {
		Map<String, Object> map = new HashMap<String, Object>();
		if(JudgeUtil.isEmpty(destroyPath)){
			map.put(Constants.Plugin.RESULT, false);
			map.put(Constants.Plugin.RESULT_MESSAGE, "destroyPath 为空="+destroyPath);
			return JSON.toJSONString(map);
		}
		List<String> list = new ArrayList<>();
		list.add(destroyPath);
		Map<String,Object> deleteResult = delete(list);
		return JSON.toJSONString(deleteResult);
	}
	@SuppressWarnings("unchecked")
	private Map<String, Object> delete(List<String> list) {
//		Map<String, Object> params = JSON.parseObject(param);
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> failedList = new LinkedList<>();
		List<String> successList = new LinkedList<>();
//		List<String> list = (List<String>) params.get(Constants.Plugin.DELETE_FILES);
		if (list != null) {
			for (String s : list) {
				s = FilePath.getFilePath(s);
				File file = new File(s);
				if (file.exists()) {
					if (file.isDirectory()) {
						if (!deleteDirectory(file.getAbsolutePath())) {
							failedList.add(s);
						} else {
							successList.add(s);
						}
					} else {
						if (!deleteFile(file.getAbsolutePath())) {
							failedList.add(s);
						} else {
							successList.add(s);
						}
					}
				} else {
					failedList.add(s);
				}
			}
		}
		log.debug("delete success: " + successList);
		if (failedList.size() != 0) {
			map.put(Constants.Plugin.RESULT, false);
			map.put(Constants.Plugin.RESULT_MESSAGE, failedList);
			log.error("delete failed: " + failedList);
		} else {
			map.put(Constants.Plugin.RESULT, true);
		}
		return map;
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public boolean DeleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(sPath);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setFields() {
		// TODO Auto-generated method stub
		if(!JudgeUtil.isEmpty(this.pluginInput.get(DESTROYPATH_KEY ))){
			this.destroyPath=this.pluginInput.get(DESTROYPATH_KEY ).toString();
		}
	}

	public void setDestroyPath(String destroyPath) {
		this.destroyPath = destroyPath;
	}
	
}
