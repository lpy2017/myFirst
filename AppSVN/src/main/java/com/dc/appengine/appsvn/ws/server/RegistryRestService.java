package com.dc.appengine.appsvn.ws.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appsvn.service.IRegistryService;
import com.dc.appengine.appsvn.utils.registry.Constant;
import com.dc.appengine.appsvn.utils.registry.RegistryClient;
import com.dc.appengine.appsvn.ws.client.MasterClient;

@RestController
@RequestMapping("/registry")
public class RegistryRestService {
	private static final Logger log = LoggerFactory.getLogger(RegistryRestService.class);
	
	@Autowired
	IRegistryService registryService;
	public List<Long> getUserIds(String userId) {
		MasterClient client = new MasterClient();
		String resultStr = client.getSonIds(userId);
		Map<Long, String> map = JSON.parseObject(resultStr, new TypeReference<Map<Long, String>>() {
		});
		List<Long> list = new ArrayList<Long>();
		for (long unit : map.keySet()) {
			list.add(unit);
		}
		return list;
	}

	public List<String> getUserNames(String userId) {
		MasterClient client = new MasterClient();
		String resultStr = client.getSonIds(userId);
		Map<Long, String> map = JSON.parseObject(resultStr, new TypeReference<Map<Long, String>>() {
		});
		List<String> list = new ArrayList<String>();
		for (long unit : map.keySet()) {
			list.add(map.get(unit));
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public String listToString(List list) {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		for (Object unit : list) {
			if (index != 0) {
				sb.append(",");
			}
			index++;
			sb.append(unit.toString());
		}
		return sb.toString();
	}

	/**
	 * 获取镜像仓库列表
	 **/
	@RequestMapping(value="/list",method= RequestMethod.GET)
	public String getAllRegistry() {
		List<Map<String, Object>> list = registryService.getAllRegistrys();
		return JSON.toJSONString(list,SerializerFeature.WriteDateUseDateFormat);
	}
	public Map<String, Object> getRegistryById(int id) {
		Map<String, Object> map = registryService.getRegistryById(id);
		if (map == null) {
			log.debug("none registry info");
		}
		return map;
	}
	/**
	 * 获取镜像仓库所有的镜像信息
	 * 
	 * @String registryId 仓库信息 id
	 * @String userName 用户名
	 **/
	@RequestMapping(value="/real/allImages",method= RequestMethod.GET)
	public String getAllImages(@RequestParam("registryId") int registryId, @RequestParam("userId") String userId) {
		Map<String, Object> registry = getRegistryById(registryId);
		if (registry == null) {
			return "{\"result\":fasle,\"reason\":\"non registry info\"}";
		}
		String domain = registry.get("domain").toString();
		String port = String.valueOf(registry.get("registry_port"));
		String aPort = String.valueOf(registry.get("nginx_port"));
		RegistryClient registryClient = new RegistryClient(domain, port, aPort);
		List<String> list = registryClient.getRepositories(Constant.catalogUser, Constant.catalogPassword);
		// RegistryData util = new RegistryData(domain, nginxPort);
		// List<String> list = util.getImagesForUser(userNames);
		List<String> resultList = new ArrayList<>();
		if (list != null) {
			List<String> userNames = getUserNames(userId);
			if (userNames != null) {
				if (userNames.contains("admin") || registryId == 2) {
					for (String rep : list) {
						List<String> tagList = registryClient.getTagList(Constant.catalogUser, Constant.catalogPassword,
								rep);
						if (tagList != null && tagList.size() > 0) {
							resultList.add(rep);
						}
					}
				} else {
					for (String user : userNames) {
						for (String rep : list) {
							if (rep.startsWith(user + "/")) {
								List<String> tagList = registryClient.getTagList(Constant.catalogUser,
										Constant.catalogPassword, rep);
								if (tagList != null && tagList.size() > 0) {
									resultList.add(rep);
								}
							}
						}
					}
				}
			}
		}
		return JSON.toJSONString(resultList,SerializerFeature.WriteDateUseDateFormat);
	}

	/**
	 * 获取镜像仓库指定镜像的版本
	 * 
	 * @String registry 仓库信息
	 * @String userName 用户名
	 * @String image 镜像名称
	 * 
	 */
	@RequestMapping(value="/real/allTags",method= RequestMethod.GET)
	public String getAllTags(@RequestParam("registryId") int registryId, @RequestParam("image") String image) {
		Map<String, Object> registry = getRegistryById(registryId);
		if (registry == null) {
			return "{\"result\":fasle,\"reason\":\"non registry info\"}";
		}
		String domain = registry.get("domain").toString();
		String port = String.valueOf(registry.get("registry_port"));
		String aPort = String.valueOf(registry.get("nginx_port"));
		RegistryClient registryClient = new RegistryClient(domain, port, aPort);
		List<String> list = registryClient.getTagList(Constant.catalogUser, Constant.catalogPassword, image);
		// RegistryData util = new RegistryData(domain, nginxPort);
		// List<String> list = util.getImageTags(image);
		if (list == null) {
			list = new ArrayList<String>();
		}
		// 解析resourceName
		String imageDescription = registryService.getImageDescription(image);
		//
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("rows", list);
		result.put("result", true);
		result.put("description_resource", imageDescription == null ? "" : imageDescription);
		return JSON.toJSONString(result,SerializerFeature.WriteDateUseDateFormat);

	}
	@RequestMapping(value="/real/checkBeforeDelete",method= RequestMethod.POST)
	public String checkBeforeDelete(@RequestParam("registryId") int registryId, @RequestParam("repository") String repository,
			@RequestParam("tag") String tag) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> registry = getRegistryById(registryId);
		if (registry == null) {
			return "{\"result\":fasle,\"reason\":\"non registry info\"}";
		}
		String domain = registry.get("domain").toString();
		String port = String.valueOf(registry.get("registry_port"));
		String aPort = String.valueOf(registry.get("nginx_port"));
		RegistryClient registryClient = new RegistryClient(domain, port, aPort);
		List<String> tagList = registryClient.checkBeforeDelete(Constant.catalogUser, Constant.catalogPassword,
				repository, tag);
		map.put("tags", tagList);
		List<String> applist = new LinkedList<>();
		map.put("apps", applist);
		return JSON.toJSONString(map,SerializerFeature.WriteDateUseDateFormat);
	}


	public String getImageUser(String imageName) {
		String[] tem = imageName.split("/");

		if (tem.length == 0 || tem.length == 3) {
			return null;
		} else if (tem.length == 1) {
			return "admin";
		} else if (tem.length == 2) {
			return tem[0];
		}
		return "";
	}
	/**
	 * 注册镜像信息到 resource version表
	 *  待删除的
	 */
	@RequestMapping(value="/registResource",method= RequestMethod.POST)
	public String registResource(@RequestParam("registry") String registry, @RequestParam("image") String image,
			@RequestParam("tag") String tag, @RequestParam("startPort") String startPort,
			@RequestParam("startScript") String startScript, @RequestParam("deploy_timeout") String deploy_timeout,
			@RequestParam("start_timeout") String start_timeout, @RequestParam("stop_timeout") String stop_timeout,
			@RequestParam("destroy_timeout") String destroy_timeout, @RequestParam("description") String description,
			@RequestParam("description_resource") String description_resource) {
		Map<String, Object> registryInfo = getRegistry(registry);
		if (registryInfo == null) {
			return "{\"result\":false,\"reason\":\"none registry info error\"}";
		}
		String userName = getImageUser(image);
		MasterClient client = new MasterClient();
		String userId = client.getUserId(userName);
		int registryId = Integer.valueOf(registryInfo.get("id").toString());
		// 检查资源是否被注册过
		String resourceName = image.replace("/", "_");
		Map<String, Object> resource = registryService.checkResource(userId, resourceName);
		int resourceId = 0;
		if (resource == null) {
			// 添加描述
			resourceId = registryService.saveResource(userId, resourceName,true, description_resource);
			log.debug("add resource" + resourceName + " success!");
		} else {
			// 更新描述
			resourceId = Integer.valueOf(resource.get("ID").toString());
			registryService.updateResource(resourceId, description_resource);
			log.debug("update  resource" + resourceName + " success!");
		}
		Map<String, Object> version = registryService.checkVersion( image, tag);
		if (version == null) {
			Map<String, Object> otherInfo = new HashMap<String, Object>();
			otherInfo.put("startPort", startPort);
			otherInfo.put("startScript", startScript);
			otherInfo.put("deploy_timeout", deploy_timeout);
			otherInfo.put("start_timeout", start_timeout);
			otherInfo.put("stop_timeout", stop_timeout);
			otherInfo.put("destroy_timeout", destroy_timeout);
			otherInfo.put("description", description);
			registryService.saveVersion(resourceId, image, tag, registryId, otherInfo);
		} else {
			return "{\"result\":false,\"reason\":\"you have already regist this image version\"}";
		}
		return "{\"result\":true }";
	}
	public static void testRegistry(String[] args) {
		RegistryRestService service= new RegistryRestService();
		String result=service.registResourceFromCI("package",
				"bash /bin.sh",
				"8005",	"8006","8007","8008",
				"version1","images1","admin",
				"registry.paas:443","hahaha","version1","8081",
				"hahaha","v1","/test/a.zip"
				);
		System.out.println(result);
	}
	@RequestMapping(value="/registResourceFromCI",method= RequestMethod.POST)
	public String registResourceFromCI(//---------公共参数--------------------
			@RequestParam("type") String type,
			@RequestParam("startScript") String startScript,
			@RequestParam("deploy_timeout") String deploy_timeout,
			@RequestParam("start_timeout") String start_timeout,
			@RequestParam("stop_timeout") String stop_timeout,
			@RequestParam("destroy_timeout") String destroy_timeout,
			@RequestParam("description") String description,
			@RequestParam("description_resource") String description_resource,
			@RequestParam("startPort") String startPort,
			@RequestParam("userName") String userName,
			//---------镜像--------------------
			@RequestParam("registry") String registry,
			@RequestParam("image") String image,
			@RequestParam("tag") String tag,
			//---------zip包--------------------
			@RequestParam("resourceName") String resourceName,
			@RequestParam("versionName") String versionName,
			@RequestParam("packagePath") String packagePath) {
		//默认方式为docker
		if(type==null || "".equals(type)){
			type="docker";
		}
		if(startPort==null || "".equals(startPort.trim())){
			startPort=null;
		}
		MasterClient client= new MasterClient();
		String pId=client.getUserId(userName);
		if("docker".equals(type)){
			String imageUser=getImageUser(image);
			String userId=client.getUserId(imageUser);
			if(userId==null || "".equals(userId)){
				return "{\"result\":false,\"reason\":\"none user info error\"}";
			}
			long numId=Long.parseLong(userId);
			if(!getUserIds(pId).contains(numId)){
				return "{\"result\":false,\"reason\":\"have no permission to regist resource in name space"+imageUser+"\"}";
			}
			if(image==null || !image.contains("/")){
				return "{\"result\":false,\"reason\":\"invalid image name\"}";
			}
			Map<String,Object> registryInfo=getRegistry(registry);
			if(registryInfo==null){
				return "{\"result\":false,\"reason\":\"none registry info error\"}";
			}
			int registryId=Integer.valueOf(registryInfo.get("id").toString());
			//检查资源是否被注册过
			//image=userName+"/"+image;
			resourceName=image.replace("/", "_");
			Map<String,Object> resource=registryService.checkResource(userId, resourceName);
			int resourceId=0;
			if(resource==null){
				//添加描述
				resourceId=registryService.saveResource(userId, resourceName,true,description_resource);
				log.debug("add resource"+resourceName+" success!");
			}else{
				//更新描述
				resourceId=Integer.valueOf(resource.get("ID").toString());
				registryService.updateResource(resourceId, description_resource);
				log.debug("update  resource"+resourceName+" success!");
			}
			Map<String,Object> version=registryService.checkVersion(  image , tag);
			if(version==null){
				Map<String,Object> otherInfo=new HashMap<String,Object>();
				otherInfo.put("startPort",startPort);
				otherInfo.put("startScript", startScript);
				otherInfo.put("deploy_timeout",deploy_timeout);
				otherInfo.put("start_timeout", start_timeout);
				otherInfo.put("stop_timeout", stop_timeout);
				otherInfo.put("destroy_timeout", destroy_timeout);
				otherInfo.put("description",description);
				registryService.saveVersion(resourceId, image, tag, registryId,otherInfo);
			}else{
				return "{\"result\":false,\"reason\":\"you have already regist this image version\"}";
			}
			return "{\"result\":true }";
		}else if("package".equals(type)){
			//检查 权限   用户test  -> /test/a/b/c.zip格式正确
			if(!"admin".equals(userName)){
				if(!packagePath.startsWith("/"+userName+"/")){
					return "{\"result\":false,\"reason\":\"you have no access to push file to this path!\"}";
				}
			}
			if(resourceName==null || "".equals(resourceName)){
				resourceName=packagePath.substring(packagePath.lastIndexOf("/")+1,packagePath.lastIndexOf(".zip"));
			}
			
			Map<String,Object> resource=registryService.checkResource(pId, resourceName);
			int resourceId=0;
			if(resource==null){
				//添加描述
				resourceId=registryService.saveResource(pId, resourceName,false,description_resource);
				log.debug("add package resource"+resourceName+" success!");
			}else{
				//更新描述
				resourceId=Integer.valueOf(resource.get("ID").toString());
				registryService.updateResource(resourceId, description_resource);
				log.debug("update  resource"+resourceName+" success!");
			}
			Map<String,Object> version=registryService.checkFtpVersion( versionName, packagePath,resourceId);
			if(version==null){
				Map<String,Object> otherInfo=new HashMap<String,Object>();
				otherInfo.put("startPort",startPort);
				otherInfo.put("startScript", startScript);
				otherInfo.put("deploy_timeout",deploy_timeout);
				otherInfo.put("start_timeout", start_timeout);
				otherInfo.put("stop_timeout", stop_timeout);
				otherInfo.put("destroy_timeout", destroy_timeout);
				otherInfo.put("description",description);
				otherInfo.put("versionName",versionName);
				registryService.saveFtpVersion(resourceId, packagePath, 3,otherInfo);
			}else{
				return "{\"result\":false,\"reason\":\"you have already regist this package version\"}";
			}
		}
		return "{\"result\":true }";
	}
	/**
	 * 根据仓库域名获取仓库的详细信息
	 * @param domainFull
	 * @return
	 */
	public Map<String,Object > getRegistry(String domainFull){
		String [] dos=domainFull.split(":");
		String domain="";
		String registryPort="";
		if(dos.length==1){
			domain=domainFull;
			registryPort="443";
		}else{
			domain=dos[0];
			registryPort=dos[1];
		}
		return registryService.getRegistryByDomain(domain, registryPort);
	}
	// 展示所需接口  分页
	@RequestMapping(value="/getResourcesByPage",method= RequestMethod.GET)
	public String getResourcesByPage(@RequestParam("userId") String userId, @RequestParam("registryId") int registryId, // 查询使用
			@RequestParam("keyword") String keyword, // 查询使用
			@RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum) {

		// total: rows:[]
		if (userId == null) {
			return "{\"result\":false,\"reason\":\"no user info\"}";
		}
		String userIds = listToString(getUserIds(userId));
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userIds.split(","));
		param.put("keyword", keyword);
		param.put("pageSize", pageSize);
		param.put("pageNum", pageNum);
		if (registryId == 0) {
			registryId = 1;
		}
		param.put("registryId", registryId);
		int startNum = pageSize * (pageNum - 1);
		param.put("startNum", startNum);
		Map<String, Object> registry = getRegistryById(registryId);
		if (registry == null) {
			return "{\"result\":false,\"reason\":\"no registry info\"}";
		}
		String url = registry.get("url").toString();
		String registryPort = registry.get("registry_port").toString();

		Map<String, Object> result = registryService.getImageForUser(param);
		result.put("registry", url + ":" + registryPort);
		return JSON.toJSONString(result,SerializerFeature.WriteDateUseDateFormat);
	}

	//不分页
	@RequestMapping(value="/getResourceListForDeploy",method= RequestMethod.GET)
	public String getResourceListForDeploy(@RequestParam("registryId") int registryId, @RequestParam("userId") String userId) {
		if (registryId == 0) {
			registryId = 1;
		}
		Map<String, Object> registry = getRegistryById(registryId);
		if (registry == null) {
			return "{\"result\":false,\"reason\":\"no registry info\"}";
		}
		String url = registry.get("url").toString();
		String registryPort = registry.get("registry_port").toString();
		String userIds = listToString(getUserIds(userId));
		// 获取镜像列表
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("registry", url + ":" + registryPort);
		List<String> rows = registryService.getImagesForUser(userIds, registryId);
		result.put("rows", rows);
		return JSON.toJSONString(result,SerializerFeature.WriteDateUseDateFormat);
	}
	/**
	 * 不分页的资源列表
	 * @param registryId 仓库id 
	 * @param resourceName 资源名称  镜像admin/test   package 则直接填资源名称
	 * @return
	 */
	@RequestMapping(value="/getResourceVersionListForDeploy",method= RequestMethod.GET)
	public String getResourceVersionListForDeploy(@RequestParam("registryId") int registryId,
			@RequestParam("resourceName") String resourceName) {
		if (registryId == 0) {
			registryId = 1;
		}
		Map<String, Object> registry = getRegistryById(registryId);
		if (registry == null) {
			return "{\"result\":false,\"reason\":\"no registry info\"}";
		}

		List<Map<String, Object>> rows = registryService.getTagsForUser(registryId, resourceName);
		return JSON.toJSONString(rows, SerializerFeature.WriteDateUseDateFormat);
	}
	
	// 版本详情页面接口
	    @RequestMapping(value="/resourceInfo",method= RequestMethod.GET)
		public String resourceInfo(
				@RequestParam("registryId")int registryId,
				@RequestParam("resourceName")String resourceName){
			Map<String, Object> result=registryService.getResourceInfo(registryId,resourceName);
			if(result==null){
				return "{\"result\":false,\"reason\":\"find no image named:"+resourceName+"\"}";
			}
			return JSON.toJSONString(result,SerializerFeature.WriteDateUseDateFormat);
		}
	// 获取镜像的版本列表详情
	    @RequestMapping(value="/getResourceVersionListByPage",method= RequestMethod.GET)
		public String getResourceVersionListByPage(
				@RequestParam("registryId")int registryId,
				@RequestParam("resourceName")String resourceName,
				@RequestParam("pageSize") int pageSize,
				@RequestParam("pageNum") int pageNum,
				@RequestParam("keyword")String keyword){
			Map<String, Object> result=registryService.getResourceTagListByPage(registryId, resourceName, pageSize, pageNum, keyword);
			return JSON.toJSONString(result,SerializerFeature.WriteDateUseDateFormat);
		}
	    @RequestMapping(value="/deleteVersion",method= RequestMethod.GET)
		public String deleteVersion(@RequestParam("versionId")int versionId,@RequestParam("userId") String userId){
		if (userId == null) {
			return "{\"result\":false,\"reason\":\"no user info error\"}";
		}
		String userIds = listToString(getUserIds(userId));
		boolean permit = registryService.hasPermission(userIds, 0, versionId);

		if (!permit) {
			return "{\"result\":false,\"reason\":\"have no permission to del this version\"}";
		}
		registryService.deleteVersion(versionId);
		return "{\"result\":true }";
	}
		
		@RequestMapping(value="/updateVersionInfo",method= RequestMethod.POST)
		public String updateVersionInfo(@RequestParam("versionId") int versionId,
				@RequestParam("userId") String userId,
				@RequestParam("startScript") String startScript, 
				@RequestParam("startPort") int startPort,
				@RequestParam("deploy_timeout") int deploy_timeout, 
				@RequestParam("start_timeout") int start_timeout,
				@RequestParam("stop_timeout") int stop_timeout, 
				@RequestParam("destroy_timeout") int destroy_timeout,
				@RequestParam("description") String description
				) {
			if (userId == null) {
				return "{\"result\":false,\"reason\":\"no user info error\"}";
			}
			String userIds = listToString(getUserIds(userId));
			boolean permit = registryService.hasPermission(userIds, 0, versionId);
			if (!permit) {
				return "{\"result\":false,\"reason\":\"have no permission to update this version\"}";
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("versionId", versionId);
			param.put("startScript", startScript);
			param.put("startPort", startPort);
			param.put("deploy_timeout", deploy_timeout);
			param.put("start_timeout", start_timeout);
			param.put("stop_timeout", stop_timeout);
			param.put("destroy_timeout", destroy_timeout);
			param.put("description", description);
			registryService.updateVersionInfo(param);
			return "{\"result\":true }";
		}
		public static void main(String[] args) {
			RegistryRestService service= new RegistryRestService();
			String result=service.resourceVersionInfo(2);
			System.out.println(result);
		}
		
		//版本详情页面接口
		@RequestMapping(value="/resourceVersionInfo",method= RequestMethod.GET)
		public String resourceVersionInfo(
				@RequestParam("versionId")int versionId){
			Map<String, Object> result=registryService.getResourceVersionInfo(versionId);
			if(result==null){
				return "{\"result\":false,\"reason\":\"find no resourceId named:"+versionId+"\"}";
			}
			return JSON.toJSONString(result,SerializerFeature.WriteDateUseDateFormat);
		}
}
