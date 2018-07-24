package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.ProcessHelper;

public class Dump extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(Config.class);
	private static final String CMD_KEY = "CMD";
	private static final String PATH_KEY= "path";
	private String CMD;
	private String path;
	@Override
	public String doPreAction() {
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
	public String doAgent() throws Exception{
		File file = new File(path).getParentFile();
		if(!file.exists()){
			file.mkdirs();
		}
		Map<String,Object> map = new ProcessHelper(CMD + " > "+ path).execute();
		//分析输入输出，确定是否执行成功
		String lastLine = readLastLine(new File(path), "utf-8");
		if(lastLine!=null && lastLine.contains("Dump completed")){
			map.put(Constants.Plugin.RESULT, true);
		}else{
			map.put(Constants.Plugin.RESULT, false);
		}
		if (!(Boolean) map.get(Constants.Plugin.RESULT)) {
			log.debug(CMD + "命令执行失败");
			map.put(Constants.Plugin.MESSAGE, "error:"+map.get("err"));
			return JSON.toJSONString(map);
		} else {
			log.debug(CMD + "命令执行成功");
			map.put(Constants.Plugin.MESSAGE, "success");
			return JSON.toJSONString(map);
		}
	}

	public static String readLastLine(File file, String charset) {
		if (!file.exists() || file.isDirectory() || !file.canRead()) {
			return null;
		}
		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "r");
			long len = raf.length();
			if (len == 0L) {
				return "";
			} else {
				byte[] bs = new byte[1];
				raf.seek(len - 1);
				raf.read(bs);
				if(bs[0] == '\n'){
					//如果最后一个字符是换行符，那么就读取倒数第二行，因为倒数第一行是空字符串
					len = len - 1;
				}
				long pos = len;
				while (pos > 0) {
					pos--;
					raf.seek(pos);
					if (raf.readByte() == '\n') {
						pos++;
						break;
					}
				}
				if (pos == 0) {
					raf.seek(0);
				}
				byte[] bytes = new byte[(int) (len - pos)];
				raf.read(bytes);
				if (charset == null) {
					return new String(bytes);
				} else {
					return new String(bytes, charset);
				}
			}
		} catch (Exception e) {
			
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (Exception e2) {
				}
			}
		}
		return null;
	}
	public static void main(String[] args) {
		
	}

	@Override
	public void setFields() {
		// TODO Auto-generated method stub
		if(!JudgeUtil.isEmpty(this.pluginInput.get(CMD_KEY ))){
			this.CMD=this.pluginInput.get(CMD_KEY ).toString();
		}
		if(!JudgeUtil.isEmpty(this.pluginInput.get(PATH_KEY ))){
			this.path=this.pluginInput.get(PATH_KEY  ).toString();
		}
	}

	public void setCMD(String cMD) {
		CMD = cMD;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
