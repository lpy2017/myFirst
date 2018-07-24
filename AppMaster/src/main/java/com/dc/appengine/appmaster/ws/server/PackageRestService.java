package com.dc.appengine.appmaster.ws.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.IPackageService;
import com.dc.appengine.appmaster.utils.FileUtil;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dcits.Common.entity.User;

@Controller
@RequestMapping("/ws/packageResource")
public class PackageRestService {

	private static final Logger log = LoggerFactory.getLogger(PackageRestService.class);
	
	@Autowired
	@Qualifier("packageService")
	IPackageService packageService;
	
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
	@Value(value = "${ftp.home.path}")
	String ftpHomePath;
	/*
	 * 上传工件包
	 * @RequestParam("file") 工件包文件流
	 */
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public String uploadPackage(@RequestParam("file") MultipartFile uploadFile,
								@RequestParam("description") String description,
								@Context HttpServletRequest request){
		if (uploadFile == null) {
			return MessageHelper.wrap("result", false, "message", "文件为空");
		}
		String fileName = uploadFile.getOriginalFilename();
		Map<String,Object> resource = packageService.findPackageByName(fileName);
		if (null!=resource && !resource.isEmpty()) {
			return MessageHelper.wrap("result",false,"message","存在同名工件包！");
		}
		if(!(fileName.endsWith(".zip")||fileName.endsWith(".jar")||fileName.endsWith(".war")||fileName.endsWith(".tar")||fileName.endsWith(".gz"))){
			return MessageHelper.wrap("result",false,"message","工件包必须是zip/jar/war/tar/gz格式！");
		}
		String uuid = UUID.randomUUID().toString();
		final User user = (User) request.getSession().getAttribute("user");
		try (InputStream input = uploadFile.getInputStream()){
			String resourceSize = "";
			Long size = uploadFile.getSize();
//			resourceSize = formSize(size);
			String uuids="packages"+File.separator+uuid;
			boolean b = packageService.uploadFile(input, uuids, fileName);
			if(b){
//				String releativePath = uuids + File.separator+fileName;
				Map<String,Object> param = new HashMap<>();
				param.put("id", uuid);
				param.put("resourceName", fileName);
				param.put("resourceDesc", description);
//				param.put("resourcePath", releativePath);
				param.put("resourceSize", size);
				String res = packageService.savePackage(param);
				JSONObject jsob = (JSONObject) JSONObject.parse(res);
				if (jsob.getBoolean("result")) {
					String fileFtpUrl = jsob.getString("resourcePath");
					//==============添加审计start===================
					ThreadPool.service.execute(new Runnable(){
						@Override
						public void run(){
							String userName = null;
							if (user == null) {
								userName = "external";
							} else {
								userName = user.getName();
							}
							auditService.save(new AuditEntity(userName, ResourceCode.ARTIFACT,fileName, ResourceCode.Operation.ADD, 1, "上传工件:" + fileName));
						}
					});
					//==============添加审计end=====================
					return MessageHelper.wrap("result", true, "message", "工件包上传成功","file",fileFtpUrl,"fileName",fileName);				
				}
			}
		} catch (IOException e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(user.getName(), ResourceCode.ARTIFACT,fileName, ResourceCode.Operation.ADD, 0, "上传工件:" + fileName));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
		}
		//==============添加审计start===================
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				auditService.save(new AuditEntity(user.getName(), ResourceCode.ARTIFACT,fileName, ResourceCode.Operation.ADD, 0, "上传工件:" + fileName));
			}
		});
		//==============添加审计end=====================
		return MessageHelper.wrap("result", false, "message", "工件包上传失败");
	}
	
	@RequestMapping(value = "listPackagesByPage", method = RequestMethod.GET)
	@ResponseBody
	public String listPackagesByPage(
			@RequestParam(name="pageSize",required=false,defaultValue="10") int pageSize, 
			@RequestParam(name="pageNum",required=false,defaultValue="1") int pageNum,
			@RequestParam(name="name",defaultValue="") String name,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName){
		
		Map<String,Object> condition = new HashMap<>();
		condition.put("resourceName", name);
		condition.put("sortName", SortUtil.getColunmName("artifact", sortName));
	    condition.put("sortOrder", sortOrder);
		Page page = packageService.listPackagesByPage(condition, pageNum, pageSize);
		return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
	}
	
	@RequestMapping(value = "deletePackage", method = RequestMethod.DELETE)
	@ResponseBody
	public String deletePackage(@RequestParam("id") String id,@Context HttpServletRequest request){
		Map<String,Object> packageMap = packageService.findPackageById(id);
		final User user = (User) request.getSession().getAttribute("user");
		try {
			String returnMsg = packageService.deletePackage(id);
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					JSONObject jo = JSONObject.parseObject(returnMsg);
					boolean result = jo.getBoolean("result");
					auditService.save(new AuditEntity(user.getName(), ResourceCode.ARTIFACT,""+packageMap.get("RESOURCE_NAME"), ResourceCode.Operation.DELETE, result?1:0, "删除工件:" + ""+packageMap.get("RESOURCE_NAME")));
				}
			});
			//==============添加审计end=====================
			return returnMsg;
		} catch (Exception e) {
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(user.getName(), ResourceCode.ARTIFACT,""+packageMap.get("RESOURCE_NAME"), ResourceCode.Operation.DELETE, 0, "删除工件:" + ""+packageMap.get("RESOURCE_NAME")));
				}
			});
			//==============添加审计end=====================
			e.printStackTrace();
			return MessageHelper.wrap("result", false, "message", "删除工件包失败:message:"+e.getMessage());
		}
	}
	
	@RequestMapping(value = "download", method = RequestMethod.GET)
	@ResponseBody
	public void downloadPackage(HttpServletResponse resp,@RequestParam("id") String id){
		try {
			String filePath = packageService.downloadFile(id,null);
			File file = new File(filePath);
			resp.setHeader("content-type", "application/octet-stream");
			resp.setContentType("application/octet-stream");
			resp.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getName(),"UTF8"));
			
			OutputStream os = resp.getOutputStream();
			BufferedOutputStream bos = new BufferedOutputStream(os);
			
			InputStream is = null;
			
			is = new FileInputStream(File.separator+filePath);
			BufferedInputStream bis = new BufferedInputStream(is);
			
			int length = 0;
			byte[] temp = new byte[1 * 1024 * 10];
			
			while ((length = bis.read(temp)) != -1) {
				bos.write(temp, 0, length);
			}
			bos.flush();
			bis.close();
			bos.close();
			is.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="exportPackage",method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void exportPackage(@Context HttpServletResponse resp,@RequestParam("ids") String ids){
		String result =  packageService.exportPackage(ids);
		Map<String,String> resultMap = JSONObject.parseObject(result,Map.class);
		if("true".equals(String.valueOf(resultMap.get("result")))){
			String filePath = resultMap.get("message");
			File file = new File(filePath);
			resp.setHeader("content-type", "application/octet-stream");
			resp.setContentType("application/octet-stream");
			try {
				resp.setHeader("Content-Disposition", "attachment; filename=" + new String(file.getName().getBytes("ISO-8859-1"), "UTF-8"));
				
				OutputStream os = resp.getOutputStream();
				BufferedOutputStream bos = new BufferedOutputStream(os);
				
				InputStream is = null;
				
				is = new FileInputStream(File.separator+filePath);
				BufferedInputStream bis = new BufferedInputStream(is);
				
				int length = 0;
				byte[] temp = new byte[1 * 1024 * 10];
				
				while ((length = bis.read(temp)) != -1) {
					bos.write(temp, 0, length);
				}
				bos.flush();
				bis.close();
				bos.close();
				is.close();	
			} catch (Exception e1) {
				e1.printStackTrace();
			}

	  }
	}
	
	@RequestMapping(value="importPackage",method = RequestMethod.POST)
	@ResponseBody
	public String importPackage(@RequestParam("packageList") String packageList,@RequestParam("zipMap") String zip,@RequestParam("userId") String userId){
		return packageService.importPackage(packageList,zip,userId);
	}
	
	@RequestMapping(value="checkPackageUnique",method = RequestMethod.GET)
	@ResponseBody
	public String checkPackageUnique(@RequestParam("fileName") String fileName){
		Map<String,Object> resource = packageService.findPackageByName(fileName);
		if(JudgeUtil.isEmpty(resource)){
			return MessageHelper.wrap("result", true, "message", "success");
		}else{
			return MessageHelper.wrap("result", false, "message", "存在同名相同工件：" + fileName);
		}
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
	}
	
	@RequestMapping(value="checkImportPackage",method = RequestMethod.POST)
	@ResponseBody
	public String checkImportPackage(@RequestParam("file") MultipartFile uploadFile,
			@RequestParam("fileType") String fileType,@Context HttpServletRequest request){
		InputStream fileInputStream=null;
		OutputStream fileOutputStream = null;
		File localFile=null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String now = format.format(new Date());
			if(uploadFile == null){
				return MessageHelper.wrap("result",false,"message","导入工件为空，请选择工件！");
			}
			String filename =uploadFile.getOriginalFilename(); 
			if(!filename.endsWith(".zip")){
				return MessageHelper.wrap("result",false,"message","工件包必须是.zip格式！");
			}
			String tmpdir = System.getProperty("java.io.tmpdir");
			tmpdir = tmpdir.endsWith(File.separator)?tmpdir:tmpdir+File.separator;
			File targetFolder = new File(tmpdir+"im_pkTmp"+File.separator+now);
			targetFolder.mkdirs();
			String targetFile = targetFolder.getPath()+File.separator+filename;
			localFile=new File(targetFile);
			fileInputStream=uploadFile.getInputStream();
			fileOutputStream = new FileOutputStream(localFile);
			IOUtils.copy(fileInputStream, fileOutputStream);
			Map<String,Object> record = FileUtil.parsePackage(targetFile,fileType);
			if(JudgeUtil.isEmpty(record)){
				return MessageHelper.wrap("result",false,"message","包解析异常！");
			}
			return packageService.checkPackage(record,fileType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return MessageHelper.wrap("result",false,"message","检查失败！");
		}finally{
			IOUtils.closeQuietly(fileOutputStream);
			IOUtils.closeQuietly(fileInputStream);
			if(localFile !=null && localFile.exists()){
				localFile.delete();
			}
		}
	}
	
}
