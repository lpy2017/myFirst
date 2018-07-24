package com.dc.appengine.appmaster.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.constants.Constants;
import com.dc.appengine.appmaster.dao.IBlueprintTemplateDao;
import com.dc.appengine.appmaster.dao.IPackageDao;
import com.dc.appengine.appmaster.dao.impl.ResourceDao;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.IPackageService;
import com.dc.appengine.appmaster.utils.FileUtil;
import com.dc.appengine.appmaster.utils.FormatUtil;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.SensitiveDataUtil;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dc.appengine.appmaster.utils.Utils;
import com.dc.appengine.appmaster.utils.ZipUtil;

@Service("packageService")
public class PackageService implements IPackageService {


	@Autowired
	@Qualifier("packageDao")
	private IPackageDao packageDao;
	
	@Autowired
	@Qualifier("resourceDao")
	private ResourceDao resourceDao;
	@Autowired
	@Qualifier("blueprintTemplateDao")
	private IBlueprintTemplateDao blueprintTemplatedao;
	
	@Autowired
	@Qualifier("auditService")
	IAudit auditService;
	
	@Value(value = "${ftp.url}")
	String url;
	@Value(value = "${ftp.port}")
	int port;
	@Value(value = "${ftp.user}")
	String user;
	@Value(value = "${ftp.pwd}")
	String pwd;
	@Value(value = "${ftp.timeOut}")
	String timeOut;
	@Value(value = "${ftp.home.path}")
	String ftpHomePath;
	
	private static final Logger log = LoggerFactory.getLogger(PackageService.class);
	
//	@Override
//	public boolean uploadFile(InputStream input, String uuid, String fileName) {//uuid="packages"+File.separator+uuid
//		try {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
//			  
//			byte[] buffer = new byte[1024];  
//			int len;  
//			while ((len = input.read(buffer)) > -1 ) {  
//			    baos.write(buffer, 0, len);  
//			}  
//			baos.flush();  
//			InputStream ins1 = new ByteArrayInputStream(baos.toByteArray());   
//			
//			boolean uploadResult = Utils.uploadFileAndUnzip(url, port, user, pwd, uuid, fileName, ins1,ftpHomePath+"packages"+File.separator);
//			if(uploadResult){
//				File foler = new File(uuid);
//				foler.mkdirs();
//				File f = new File(foler.getPath()+File.separator+fileName);
//
//				try {
//					InputStream ins2 = new ByteArrayInputStream(baos.toByteArray());  
//					inputstreamtofile(ins2,f);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				//Map<String, Object> resultMap = Utils.unZipFile(f,fileName,foler.getPath());
//				List<String> unzipResult = new ArrayList<String>();
//				try {
//					String fName = f.getName();
//					unzipResult = Utils.unZip(f, foler.getPath()+File.separator+fName.substring(0,fName.lastIndexOf(".")));
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				//System.out.println(JSON.toJSONString(resultMap));
//				//if(Boolean.valueOf(resultMap.get("result").toString())){
//				//		System.out.println("FTP 解压文件成功：" + foler.getPath() + "/" + fileName);
//				//	}
//				if(unzipResult.size()>0){
//					System.out.println("解压文件成功：" + foler.getPath() + "/" + fileName);
//				}else{
//					System.out.println("解压文件失败：" + foler.getPath() + "/" + fileName);
//
//
//				}
//				return true;
//			}
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		return false;
//	}
	
	@Override
	public boolean uploadFile(InputStream input, String uuid, String fileName) {// uuid="packages"+File.separator+uuid
		try {
//			File foler = new File(uuid);
//			foler.mkdirs();
//			File f = new File(foler.getPath() + File.separator + fileName);
//			inputstreamtofile(input, f);
//			List<String> unzipResult = new ArrayList<String>();
//			String fName = f.getName();
//			unzipResult = Utils.unZip(f, foler.getPath() + File.separator + fName.substring(0, fName.lastIndexOf(".")));
//			if (unzipResult.size() > 0) {
//				log.debug("解压文件成功：{}", foler.getPath() + "/" + fileName);
//			} else {
//				log.debug("解压文件失败：{}", foler.getPath() + "/" + fileName);
//			}
//			InputStream ins1 = new FileInputStream(f);
			boolean uploadResult = Utils.uploadFileAndUnzip(url, port, user, pwd, uuid, fileName, input,
					ftpHomePath + "packages" + File.separator);
			return uploadResult;
		} catch (Throwable e) {
			log.error("", e);
		}
		return false;
	}

	@Override
	public String savePackage(Map<String, Object> param) {
		String id = String.valueOf(param.get("id"));
		String resourceName = String.valueOf(param.get("resourceName"));
		String password = null;
		try {
			password = "ENC(" + SensitiveDataUtil.encryptDesText(pwd) + ")";
		} catch (Exception e) {
			System.out.println("加密失败[" + e.getMessage() + "]");
			e.printStackTrace();
			password = pwd;
		}
		//拼接ftp的绝对路径  ftp://root:123456@10.1.108.33/paas/packages/8f3cc911-c67c-464e-a34c-09e8161e4a73/freemarker.jar
		String resourcePath = "ftp://" + user + ":" + password + "@" + url + "/" + "packages"+ "/" + id + "/" + resourceName;
		param.put("resourcePath", resourcePath);
		boolean save = packageDao.savePackage(param);
		if(save){
			return MessageHelper.wrap("result",true,"message","添加工件包成功！","resourcePath",resourcePath);
		}
		return MessageHelper.wrap("result",false,"message","添加工件包失败！");
	}

	@Override
	public Page listPackagesByPage(Map<String, Object> condition, int pageNum,
			int pageSize) {
		Page page = packageDao.listPackagesByPage(condition,pageNum,pageSize);
		List<Map> rows = page.getRows();
		for(Map row : rows){
			//ftp://paas:123456@10.1.108.33/packages/0c5f7107-da64-4e7d-9680-617ef20d4310/weblogic.zip
			//脱敏resourcePath
			Long resourceSize = (Long) row.get("resourceSize");
			if(resourceSize !=null && resourceSize !=0){
				row.put("resourceSize",FormatUtil.formSize(resourceSize));
			}else{
				row.remove("resourceSize");
			}
			row.put("resourcePath", "ftp://" + SensitiveDataUtil.concealUser(user) + ":" + SensitiveDataUtil.concealPassword(pwd) 
					+ "@" + SensitiveDataUtil.concealIp(url) + File.separator + "packages" + File.separator + row.get("id") 
					+ File.separator + row.get("resourceName"));
		}
		return page;
	}

	@Override
	public String deletePackage(String id) {
		// TODO Auto-generated method stub
		Map<String,Object> packageMap= packageDao.findPackageById(id);
		if(packageMap ==null){
			return MessageHelper.wrap("result",false,"message","id="+id+"的工件不存在！");
		}
		String resourcePath= packageMap.get("RESOURCE_PATH").toString();
		// ftp://root:123456@10.1.108.33/paas/packages/8f3cc911-c67c-464e-a34c-09e8161e4a73/freemarker.jar
		String[] files = resourcePath.split("/");
		new Thread(){
			@Override
			public void run() {
				String folderPath = "ftl/" + files[files.length-3]+File.separator +files[files.length-2];
				FileUtil.deleteAllFilesOfDir(new File(folderPath));
				
				folderPath = files[files.length - 3] + File.separator + files[files.length - 2];
				FileUtil.deleteAllFilesOfDir(new File(folderPath));

				Utils.deleteFolder(timeOut,url, port, user, pwd, files[files.length-3],files[files.length-2]);
			}
		}.start();
		boolean delete = packageDao.deletePackage(id);
		if(delete){
			return MessageHelper.wrap("result",true,"message","删除工件包成功！");
		}
		return MessageHelper.wrap("result",false,"message","删除工件包失败！");
	}

	@Override
	public String downloadFile(String id,String resourceParent) {
		Map<String,Object> packageMap= packageDao.findPackageById(id);
		if(packageMap ==null){
			return MessageHelper.wrap("result",false,"message","id="+id+"的工件不存在！");
		}
		String pId = (String) packageMap.get("ID");
		String fileName = (String) packageMap.get("RESOURCE_NAME");
		// ftp://root:123456@10.1.108.33/paas/packages/8f3cc911-c67c-464e-a34c-09e8161e4a73/freemarker.jar
		if(null == resourceParent){
			resourceParent = new File("packages"+File.separator+pId).getAbsolutePath();
		}
		
		boolean result = Utils.downloadFile(url, port, user, pwd, "packages/"+pId, fileName, resourceParent);
		if(result){
			//  /packages/8f3cc911-c67c-464e-a34c-09e8161e4a73/freemarker.jar
			return resourceParent+File.separator+fileName;
		}
		return null;
	}

	@Override
	public String getPackageTree(String resourcePath) {
		Map<String,Object> packageMap= packageDao.findPackageByPath(resourcePath);
		return (String) packageMap.get("RESOURCE_TREE");
	}

	@Override
	public String exportPackage(String ids) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String now = format.format(new Date());
		String absPath = File.separator + "ex_pkTmp" + File.separator + now;
		File folder = new File(absPath);
		folder.mkdirs();
		try {
	//		String[] idArray = ids.split(","); 
	//		List<String> idList = Arrays.asList(idArray);
			List<Map<String,Object>> packageList = new ArrayList();
			File f = new File(absPath + File.separator + "workpiece.json");
			FileOutputStream fos = new FileOutputStream(f,true);
			List<Map<String,Object>> paramList = JSONObject.parseObject(ids, List.class);
			for(Map<String,Object> paramMap : paramList){//遍历工件
				List<String> idList = new ArrayList<String>();
				String packageId = ""+paramMap.get("id");
				idList.add(packageId);
				List<Map<String,Object>> return_package = packageDao.exportPackage(idList);
				packageList.addAll(return_package);
				
				boolean needZipExport = Boolean.valueOf(""+paramMap.get("needZipExport"));
				if(needZipExport){
					this.downloadFile(packageId, absPath);
				}
			}
			String packageJson = JSON.toJSONString(packageList, true);
			IOUtils.write(packageJson, fos);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		//3 打包zip
		String zipPath = folder.getParent() + File.separator + "workpiece"+now+".zip";
		ZipUtil.doCompress(folder, new File(zipPath));
		return MessageHelper.wrap("result",true,"message",zipPath);
	}

	@Transactional
	@Override
	public String importPackage(String packageList,String zip,String userId) {
		
		try{
			List<Map<String,Object>> pkList = JSON.parseObject(packageList, new TypeReference<List<Map<String,Object>>>(){});
			if(pkList.isEmpty()){
				return MessageHelper.wrap("result",false,"message","文件内容或格式不正确");
			}
			Map<String,String> zipMap= JSON.parseObject(zip, new TypeReference<Map<String,String>>(){});
			//加密
			for(Map<String,Object> packaged : pkList){//替换ftpLocation
				String packageName = ""+packaged.get("RESOURCE_NAME");
				
				String id = ""+packaged.get("ID");
				String newPwd = null;
				try {
					newPwd = "ENC(" + SensitiveDataUtil.encryptDesText(pwd) + ")";
				} catch (Exception e) {
					e.printStackTrace();
				}
				String newResourcePath = "ftp://"+user+":"+newPwd+"@"+url+"/"+id+"/"+packageName;
				packaged.put("RESOURCE_PATH", newResourcePath);
			}

			//删除原始存量冲突文件
			for(Map<String,Object> pkMap : pkList){
				String id = (String) pkMap.get("ID");
				final Map<String,Object> packageMap= this.findPackageById(id);
				if(null != packageMap && !packageMap.isEmpty()){
					boolean delete = this.deletePackageOnly(id);
					if(!delete){
						return MessageHelper.wrap("result",false,"message","删除已有工件包失败！");
					}
				}
				//导入新包
				lowerMap(pkMap);
				String packageName = ""+pkMap.get("resourceName");
				boolean uploadFlag = true;
				if(zipMap.containsKey(packageName)){
					String packagePath = zipMap.get(packageName);
					uploadFlag = this.uploadFile(new FileInputStream(packagePath), "packages"+File.separator+id, packageName);//上传zip包
					new File(packagePath).getParentFile().getParentFile().deleteOnExit();//删除工件包在cloudui中的解压缓存目录
				}
				if(uploadFlag){
					String addResult = this.savePackage(pkMap);
					Map<String,Object> msg = JSONObject.parseObject(addResult, Map.class);
					if(!Boolean.valueOf(msg.get("result").toString())){
						//==============添加审计start===================
						ThreadPool.service.execute(new Runnable(){
							@Override
							public void run(){
								auditService.save(new AuditEntity(userId, ResourceCode.ARTIFACT,""+pkMap.get("resourceName"), ResourceCode.Operation.IMPORT, 0, "导入工件包:" + ""+pkMap.get("resourceName")));
							}
						});
						//==============添加审计end=====================
						return MessageHelper.wrap("result",false,"message","导入工件包失败");
					}
				}
				//==============添加审计start===================
				ThreadPool.service.execute(new Runnable(){
					@Override
					public void run(){
						auditService.save(new AuditEntity(userId, ResourceCode.ARTIFACT,""+pkMap.get("resourceName"), ResourceCode.Operation.IMPORT, 1, "导入工件包:" + ""+pkMap.get("resourceName")));
					}
				});
				//==============添加审计end=====================
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return MessageHelper.wrap("result",true,"message","导入工件包成功");
	}
	
	private void lowerMap(Map<String,Object> paramMap){
		paramMap.put("id", paramMap.get("ID"));
		paramMap.remove("ID");
		paramMap.put("resourceName", paramMap.get("RESOURCE_NAME"));
		paramMap.remove("RESOURCE_NAME");
		paramMap.put("resourceDesc", paramMap.get("RESOURCE_DESC"));
		paramMap.remove("RESOURCE_DESC");
		paramMap.put("resourcePath", paramMap.get("RESOURCE_PATH"));
		paramMap.remove("RESOURCE_PATH");
		paramMap.put("resourceSize", paramMap.get("RESOURCE_SIZE"));
		paramMap.remove("RESOURCE_SIZE");
	}
	
	public void inputstreamtofile(InputStream ins, File file) throws Exception {
		OutputStream os = new FileOutputStream(file);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
			os.write(buffer, 0, bytesRead);
		}
		os.close();
		ins.close();
	}

	@Override
	public Map<String, Object> findPackageByName(String fileName) {
		return packageDao.findPackageByName(fileName);
	}

	@Override
	public Map<String, Object> findPackageById(String id) {
		return packageDao.findPackageById(id);
	}

	@Override
	public boolean deletePackageOnly(String id) {
		return packageDao.deletePackage(id);
	}
	
	@Override
	public Map<String,Object> getWorkpiece(String resourcePath){
		return packageDao.findPackageByPath(resourcePath);
	}

	@Override
	public String checkPackage(Map<String, Object> record,String fileType) {
		JSONObject result = new JSONObject();
		JSONArray existWorkPFiles = new JSONArray();
		JSONArray existCompFiles = new JSONArray();
		JSONArray existTemFiles = new JSONArray();
		List<Map<String,Object>> wkpkList = (List<Map<String,Object>>) record.get(Constants.Package.WORKPIECE);
		List<Map<String,Object>> coppkList = (List<Map<String,Object>>) record.get(Constants.Package.COMPONET);
		List<Map<String,Object>> tempkList = (List<Map<String,Object>>) record.get(Constants.Package.BLUEPRINT);
		if(!JudgeUtil.isEmpty(tempkList)){
			for(Map<String,Object> packaged : tempkList){
				String name = ""+packaged.get("NAME");
				Map<String, String> param= new HashMap<String, String>();
				param.put("blueprint_name", name);
				List<Map<String, Object>> list =blueprintTemplatedao.getBlueprintTemplate(param);
				if (null!=list && !list.isEmpty()) {
					existTemFiles.add(name);
				}
			}
		}
		if(!JudgeUtil.isEmpty(coppkList)){
			for(Map<String,Object> packaged : coppkList){
				String name = ""+packaged.get("RESOURCE_NAME");
				int temp = resourceDao.findResourceByName(name);
				if (temp >= 1) {
					existCompFiles.add(name);
				}
			}
		}
		if(!JudgeUtil.isEmpty(wkpkList)){
			for(Map<String,Object> packaged : wkpkList){
				String name = ""+packaged.get("RESOURCE_NAME");
				Map<String,Object> resource = findPackageByName(name);
				if (null!=resource && !resource.isEmpty()) {
					existWorkPFiles.add(name);
				}
			}
		}
		String message="";
		if(!existWorkPFiles.isEmpty()||!existCompFiles.isEmpty()||!existTemFiles.isEmpty()){
			result.put("result", false);
			if(!existWorkPFiles.isEmpty()){
				message=message+"存在同名工件："+existWorkPFiles.toJSONString()+System.lineSeparator();
			}
			if(!existCompFiles.isEmpty()){
				message=message+"存在同名组件："+existCompFiles.toJSONString()+System.lineSeparator();
			}
			if(!existTemFiles.isEmpty()){
				message=message+"存在同名蓝图："+existTemFiles.toJSONString()+System.lineSeparator();
			}
			result.put("message", message);
		}else{
			result.put("result", true);
		}
		return result.toJSONString();
	}
	
}
