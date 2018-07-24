package com.dc.appengine.appmaster.ws.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.tools.ant.util.ReaderInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dc.appengine.appmaster.utils.Utils;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("/ws/logo")
public class LogoRestService {
	
	private static final Logger log = LoggerFactory.getLogger(LogoRestService.class);
	
	@Value("${ftp.url:localhost}")
	private String hostname;
	@Value("${ftp.port:21}")
	private int port;
	@Value("${ftp.usr:paas}")
	private String username;
	@Value("${ftp.pwd:123456}")
	private String password;
	@Value("${logo.pathname:/logo}")
	private String pathname;
	@Value("${logo.big:big.png}")
	private String logoBigName;
	@Value("${logo.small:small.png}")
	private String logoSmallName;
//	@Value("${logo.localPath:./logo}")
//	private String localPath;
	@Value("${logo.infoName:info.json}")
	private String infoName;
	@Value("${logo.icon:icon.png}")
	private String iconName;
	
	@Autowired
	@Qualifier("auditService")
	IAudit auditService;
	
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public Object upload(@RequestParam(value = "big", required = false) MultipartFile big, @RequestParam(value = "small", required = false) MultipartFile small, @RequestParam(value = "icon", required = false) MultipartFile icon, @RequestParam("info") String info,
			@Context HttpServletRequest request) {
//		Map<String, Object> result = new HashMap<>();
//		try {
//			boolean bigResult = Utils.uploadFile(hostname, port, username, password, pathname, logoBigName, big.getInputStream());
//			boolean smallResult = Utils.uploadFile(hostname, port, username, password, pathname, logoSmallName, small.getInputStream());
//			boolean iconResult = Utils.uploadFile(hostname, port, username, password, pathname, iconName, icon.getInputStream());
//			boolean infoResult = Utils.uploadFile(hostname, port, username, password, pathname, infoName, new ReaderInputStream(new StringReader(info)));
//			if (bigResult && smallResult && infoResult && iconResult) {
//				result.put("result", true);
//			} else {
//				result.put("result", false);
//				result.put("info", "上传文件失败");
//				log.info("big result:{}, small result:{}, info result:{}, icon result:{}", bigResult, smallResult, infoResult, iconResult);
//			}
//			return result;
//		} catch (IOException e) {
//			log.error("", e);
//			result.put("result", false);
//			result.put("info", e.getMessage());
//			return result;
//		}
		JSONObject oldInfo = JSONObject.parseObject(JSONObject.toJSONString(info()));
		Map<String, Object> result = new HashMap<>();
		final User user = (User) request.getSession().getAttribute("user");
		try {
			boolean flag = true;
			if (big != null) {
				boolean bigResult = Utils.uploadFile(hostname, port, username, password, pathname, logoBigName, big.getInputStream());
				if (!bigResult) {
					flag = false;
					log.info("big result:{}", bigResult);
				}
			}
			if (small != null) {
				boolean smallResult = Utils.uploadFile(hostname, port, username, password, pathname, logoSmallName, small.getInputStream());
				if (!smallResult) {
					flag = false;
					log.info("small result:{}", smallResult);
				}
			}
			if (icon != null) {
				boolean iconResult = Utils.uploadFile(hostname, port, username, password, pathname, iconName, icon.getInputStream());
				if (!iconResult) {
					flag = false;
					log.info("icon result:{}", iconResult);
				}
			}
			boolean infoResult = Utils.uploadFile(hostname, port, username, password, pathname, infoName, new ReaderInputStream(new StringReader(info)));
			if (!infoResult) {
				flag = false;
				log.info("info result:{}", infoResult);
			}
			if (flag) {
				result.put("result", true);
			} else {
				result.put("result", false);
				result.put("info", "上传文件失败");
			}
		} catch (IOException e) {
			log.error("", e);
			result.put("result", false);
			result.put("info", e.getMessage());
			
		}
		int resultInt = (Boolean)result.get("result")?1:0;
		//==============添加审计start===================
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				String modify ="";
				String oldName = oldInfo.getString("name");
				String oldDescription = oldInfo.getString("description");
				JSONObject jo = JSONObject.parseObject(info);
				String name = jo.getString("name");
				String description = jo.getString("description");
				if(!name.equals(oldName)){
					modify=modify+" 系统名称被修改 ";
				}
				if(!description.equals(oldDescription)){
					modify=modify+" 系统简介被修改 ";
				}
				if(big!= null){
					modify=modify+" 企业LOGO被修改 ";
				}
				if(small!= null){
					modify=modify+" 左侧菜单收缩时显示的LOGO被修改 ";
				}
				if(icon != null){
					modify=modify+" favicon图标被修改 ";
				}
				auditService.save(new AuditEntity(user.getName(), ResourceCode.CUSTOMIZATION, name, ResourceCode.Operation.UPDATE, resultInt, "修改项:" + modify));
			}
		});
		//==============添加审计end=====================
		return result;
	}
	
	@RequestMapping(value = "download", method = RequestMethod.GET)
	public ResponseEntity<Resource> download(@RequestParam(value = "size", defaultValue = "small") String size) {
		String fileName = logoSmallName;
		if ("big".equals(size)) {
			fileName = logoBigName;
		}
		if ("icon".equals(size)) {
			fileName = iconName;
		}
//		boolean flag = Utils.downloadFile(hostname, port, username, password, pathname, fileName, localPath);
//		if (flag) {
//			try {
//				Resource resource = new InputStreamResource(new FileInputStream(localPath + File.separator + fileName));
//				return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
//			} catch (FileNotFoundException e) {
//				log.error("", e);
//			}
//		}
		Resource resource = new InputStreamResource(Utils.getInputStream(hostname, port, username, password, pathname, fileName));
		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
	}
	
	@RequestMapping(value = "info", method = RequestMethod.GET)
	public Object info() {
//		boolean flag = Utils.downloadFile(hostname, port, username, password, pathname, infoName, localPath);
//		if (flag) {
//			StringBuilder sb = new StringBuilder();
//			BufferedReader br = null;
//			try {
//				br = new BufferedReader(new FileReader(localPath + File.separator + infoName));
//				String str;
//				while ((str = br.readLine()) != null) {
//					sb.append(str);
//				}
//				return JSON.parse(sb.toString());
//			} catch (FileNotFoundException e) {
//				log.error("", e);
//			} catch (IOException e) {
//				log.error("", e);
//			} finally {
//				if (br != null) {
//					try {
//						br.close();
//					} catch (IOException e) {
//						log.error("", e);
//					}
//				}
//			}
//		}
		BufferedReader br = new BufferedReader(new InputStreamReader(Utils.getInputStream(hostname, port, username, password, pathname, infoName)));
		StringBuilder sb = new StringBuilder();
		String str;
		try {
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			return JSON.parse(sb.toString());
		} catch (IOException e) {
			log.error("", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					log.error("", e);
				}
			}
		}
		return null;
	}

}
