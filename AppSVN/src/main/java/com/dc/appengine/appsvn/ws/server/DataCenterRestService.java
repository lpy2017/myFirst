package com.dc.appengine.appsvn.ws.server;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dc.appengine.appsvn.service.IRegistryService;
import com.dc.appengine.appsvn.utils.FtpUtil;

@RestController
@RequestMapping("/datacenter")
public class DataCenterRestService {
	
	@Autowired
	IRegistryService registryService;
	
//	@RequestMapping(value="/updateRegistryUser",method= RequestMethod.POST)
//	public String updateRegistryUser(
//			@RequestParam("userName")String userName,
//			@RequestParam("password") String password){
//		
//		//创建1.0仓库用户 已废弃
//		//RegistryUtil.addRegistryUser(userName,password);
//		//在ftp上创建一级目录
//		Map<String,Object> ftp=registryService.getRegistryById(3);
//		Object ftpO=ftp.get("url");
//		if(ftpO!=null && !"".equals(ftpO.toString())){
//			FtpInfo info= new FtpInfo(ftpO.toString());
//			FtpUtil.mkDir(info, userName);
//		}
//		return "ok";
//	}
	@RequestMapping("/hello")
	public static String hello(){
		return "hello";
	}

	/**
	 * 更新用户信息
	 * @param userName
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/updateRegistryUser",method= RequestMethod.POST)
	public String updateRegistryUser(
			@RequestParam("userName")String userName,
			@RequestParam("password") String password){
		//通知ftp修改用户信息 旧版镜像仓库需要添加用户信息
		//return RegistryUtil.addRegistryUser(userName,password);
		
		return updateFtpUser(userName,password)+"";
		 
		
	}
	public boolean updateFtpUser(String userName,String password){
		Map<String,Object> ftp=registryService.getRegistryById(3);
		Object ftpN=ftp.get("userName");
		Object ftpP=ftp.get("password");
		Object ftpA=ftp.get("domain");
		Object ftpMP=ftp.get("managerport");
		if(ftpN==null || ftpP ==null || ftpA==null){
			return false;
		}
		if(ftpMP==null || "".equals(ftpMP)){
			ftpMP="9909";
		}
		String ret=FtpUtil.updateUser(ftpA.toString(),ftpMP.toString(),ftpN.toString(),ftpP.toString(),userName,password);
		return ret.contains("true");
	}
}
