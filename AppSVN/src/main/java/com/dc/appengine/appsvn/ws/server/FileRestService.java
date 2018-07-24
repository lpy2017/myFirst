package com.dc.appengine.appsvn.ws.server;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.appsvn.service.impl.FtpResourceService;
import com.dc.appengine.appsvn.utils.FileWorker;
import com.dc.appengine.appsvn.utils.MessageUtil;

@RestController
@RequestMapping("/file")
public class FileRestService {
	private static Logger log = LoggerFactory.getLogger(FileRestService.class);
	@Autowired
	FtpResourceService ftpService;
	/**
	 * http上传文件到目录（根据svn配置workPath）
	 * 
	 * @param headers
	 * @param request
	 * @param is
	 * @param disposition
	 * @return 文件uuid
	 */
	@RequestMapping(value="/upload",method= RequestMethod.POST)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String upload(@Context HttpHeaders headers, @Context HttpServletRequest request,
			@FormDataParam("file") InputStream is, @FormDataParam("file") FormDataContentDisposition disposition) {

		log.debug("get file Name" + disposition.getFileName());
		String tmpid = FileWorker.getInstance().uploadFileForNextOp(is, disposition.getFileName());

		return tmpid;
	}

	/**
	 * @param type
	 *            类型 app/topo 组件/拓扑包
	 * @param appName
	 *            应用Name
	 * @param resourceName
	 *            资源名
	 * @param versionName
	 *            版本名称
	 * @param startPort
	 *            启动端口 默认
	 * @param startScript
	 *            启动命令
	 * @param deploy_timeout
	 *            部署超时时间
	 * @param start_timeout
	 * @param stop_timeout
	 * @param destroy_timeout
	 * @param description
	 *            版本描述
	 * @param description_resource
	 *            资源描述
	 * @return <br>
	 *         {"result":true,"resourceName":"rname","tag":"versionName","id":
	 *         "versionId","resourceId":resourceId} <br>
	 *         {"result":false,"msg": "error message"}
	 */
	@RequestMapping(value="/registResource",method= RequestMethod.POST)
	public String registResource(@RequestParam("fileid") String workId,
			@RequestParam(value="type",defaultValue="app") String type, // app topo
			@RequestParam("appId") String APPID, @RequestParam("userName") String userName,
			@RequestParam("resourceName") String resourceName, @RequestParam("versionName") String versionName,
			@RequestParam(value="startPort",defaultValue="0") @DefaultValue("0") String startPort, @RequestParam("startScript") String startScript,
			@RequestParam(value="deploy_timeout",defaultValue="60000") String deploy_timeout,
			@RequestParam(value="start_timeout",defaultValue="60000") String start_timeout,
			@RequestParam(value="stop_timeout",defaultValue="60000") String stop_timeout,
			@RequestParam(value="destroy_timeout",defaultValue="60000") String destroy_timeout,
			@RequestParam("description") String description,
			@RequestParam("description_resource") String description_resource) {

		if (workId == null || "".equals(workId)) {
			return "{\"result\":false,\"msg\":\"file not exist\"}";
		}
		// 是否公有 默认组件包公有 topo包私有
		boolean isPublic = false;

		if (type == null || "".equals(type) || "app".equals(type)) {
			isPublic = true;
		}
		if (APPID == null || "".equals(APPID)) {
			APPID = "0";
		}
		log.debug("put file to ftp success!");

		Map<String, Object> otherInfo = new HashMap<String, Object>();
		if (startPort == null || "".equals(startPort.trim())) {
			startPort = "0";
		}
		otherInfo.put("startPort", startPort);
		otherInfo.put("startScript", startScript);
		otherInfo.put("deployTimeout", deploy_timeout);
		otherInfo.put("startTimeout", start_timeout);
		otherInfo.put("stopTimeout", stop_timeout);
		otherInfo.put("destroyTimeout", destroy_timeout);

		return ftpService.saveResourceVersion(resourceName, userName, Integer.valueOf(APPID), description_resource,
				workId, versionName, description, otherInfo, isPublic);

	}

	/**
	 * 展示所有资源信息
	 * 
	 * @param type
	 *            组件：app 拓扑：topo
	 * @param pageSize
	 *            如果为0则不分页
	 * @param pageNum
	 *            页码
	 * @param keyword
	 *            搜索关键词 可以为空
	 * @param userId
	 *            用户id
	 * @param appId
	 *            外部应用id
	 * @return <br>
	 *         {"keyword":"keyword","pageNum":1,"pageSize":1,"total": 2,
	 *         "rows":[{ "id": 1, "last_id":3, "last_time":
	 *         "2017-01-23 10:59:03", "last_version": "v3.0", "resourceName":
	 *         "tttooo1", "version_num": 3 }] }
	 */
	@RequestMapping(value="/resourceList",method= RequestMethod.GET)
	public String getAppResourceList(@QueryParam("type") @DefaultValue("app") String type,
			@QueryParam("pageSize") @DefaultValue("0") int pageSize, @QueryParam("pageNum") int pageNum,
			@QueryParam("keyword") String keyword, @QueryParam("userId") String userId,
			@QueryParam("appId") String appId) {

		if ("app".equals(type) || "".equals(type)) {
			return ftpService.getPublicResourceList(keyword, pageNum, pageSize);
		} else {
			String userIds = listToString(ftpService.getUserIds(userId));
			return ftpService.getUserResourceList(userIds, appId, keyword, pageNum, pageSize);
		}
	}

	/**
	 * 资源版本列表
	 * 
	 * @param resourceId
	 *            资源id
	 * @param keyword
	 *            搜索关键词
	 * @param pageSize
	 *            页面条数 0为不分页
	 * @param pageNum
	 *            页码
	 * @return <br>
	 *         {"pageNum":0,"pageSize":0,"total": 2,"rows":[{ { "id":3,
	 *         "REGISTRY_ID":3, "createDate":"2017-01-23 10:59:03",
	 *         "deploy_timeout":"60000", "destroy_timeout":"60000",
	 *         "packagePath":"/test/topo3.zip", "resourceId":1,
	 *         "start_timeout":"60000", "stop_timeout":"60000", "tag":"v3.0"}
	 * 
	 *         },{}]}
	 */
	@RequestMapping(value="/versionList",method= RequestMethod.GET)
	public String getVersionList(@QueryParam("resourceId") int resourceId, @QueryParam("keyword") String keyword,
			@QueryParam("pageSize") @DefaultValue("0") int pageSize, @QueryParam("pageNum") int pageNum) {
		if (pageNum == 0) {
			pageNum = 1;
		}
		return ftpService.getVersionList(resourceId, keyword, pageSize, pageNum);
	}

	/**
	 * 删除资源版本
	 * 
	 * @param versionId
	 * @return <br>
	 *         {"result":true}
	 */
	@RequestMapping(value="/delVersion/{versionId}",method= RequestMethod.GET)
	public String delVersion(@PathParam("versionId") int versionId) {
		// check used!
		// System.out.println("versionId"+versionId);
		ftpService.deleteVersion(versionId);
		return MessageUtil.mkBooleanMessage(true, "ok");
	}

	/**
	 * 资源版本详细信息
	 * 
	 * @param versionId
	 * @return <br>
	 *         失败{"result":false,"reason":"find no resourceId named:1"} <br>
	 *         <br>
	 *         成功 { "REGISTRY_ID":3, "deploy_timeout":"", "destroy_timeout":"",
	 *         "id":17, "last_time":"2017-01-23 10:59:03", "num":1,
	 *         "packagePath":"/test/topo_appengine.v1.zip", "resourceId":1,
	 *         "resourceName":"qa", "sourcePath":"/test/appengine.jar",
	 *         "startPort":0, "start_timeout":"", "stop_timeout":"", "url":
	 *         "ftp://admin:admin@10.1.108.125/test/topo_appengine.v1.zip",
	 *         "versionName":"v1" }
	 */
	@RequestMapping(value="/resourceVersionInfo",method= RequestMethod.GET)
	public String resourceVersionInfo(@QueryParam("versionId") int versionId) {
		Map<String, Object> result = ftpService.getResourceVersionInfo(versionId);
		if (result == null) {
			return "{\"result\":false,\"reason\":\"find no resourceId named:" + versionId + "\"}";
		}

		return JSON.toJSONString(result, true);
	}

	/**
	 * 更新版本信息
	 * 
	 * @param versionId
	 * @param startPort
	 * @param startScript
	 * @param deploy_timeout
	 * @param start_timeout
	 * @param stop_timeout
	 * @param destroy_timeout
	 * @param description
	 * @return <br>
	 *         { "result ":false, "msg ": "db error"} <br>
	 *         { "result ":true}
	 */
	@RequestMapping(value="/updateVersion",method= RequestMethod.POST)
	public String updateVersion(@RequestParam("versionId") int versionId,
			@RequestParam("startPort") @DefaultValue("0") String startPort, @RequestParam("startScript") String startScript,
			@RequestParam("deploy_timeout") @DefaultValue("60000") int deploy_timeout,
			@RequestParam("start_timeout") @DefaultValue("60000") int start_timeout,
			@RequestParam("stop_timeout") @DefaultValue("60000") int stop_timeout,
			@RequestParam("destroy_timeout") @DefaultValue("60000") int destroy_timeout,
			@RequestParam("description") String description) {
		try {
			ftpService.updateVersionInfo(versionId, startScript, startPort, deploy_timeout, start_timeout, stop_timeout,
					destroy_timeout, description);
		} catch (Exception e) {
			return "{\"result\":false,\"msg\":\"db error:" + e.getMessage() + "\"}";
		}
		return "{\"result\":true}";

	}

	/**
	 * 资源详细信息列表
	 * 
	 * @param resourceId
	 * @return <br>
	 *         { "id":1, "description":"a", "last_id":17, "last_time":
	 *         "2017-01-23 10:59:03", "last_version":"v1", "resourceName":"qa",
	 *         "version_num":1 }
	 */
	@RequestMapping(value="/getResourceInfo",method= RequestMethod.GET)
	public String resourceInfo(@QueryParam("resourceId") int resourceId) {

		return JSON.toJSONString(ftpService.getResourceInfo(resourceId));
	}

	private String listToString(List<?> list) {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (Object obj : list) {
			if (index != 0) {
				sb.append(",");
			}
			index++;
			sb.append(obj.toString());
		}
		return sb.toString();
	}

}
