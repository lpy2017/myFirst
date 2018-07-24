package com.dc.appengine.appmaster.ws.server;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dc.appengine.appmaster.service.IReleaseManageService;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("/ws/releaseManage")
public class ReleaseManageRestService {
	private static final Logger log = LoggerFactory.getLogger(ReleaseManageRestService.class);
	
	@Autowired
	@Qualifier("releaseeManageService")
	IReleaseManageService service;
	@RequestMapping(value="saveRelease",method=RequestMethod.POST)
	public String saveRelease(@Context HttpServletRequest request,@RequestParam("name") String name,
			@RequestParam(name="description",required=false) String description,@RequestParam("lifecycleId") String lifecycleId){
		User user = (User) request.getSession().getAttribute("user");
		return service.saveRelease(name, description, lifecycleId,user.getId());
	}
	
	@RequestMapping(value="listReleases",method=RequestMethod.GET)
	public String listReleases(@RequestParam(name="pageSize",required = true,defaultValue="10") int pageSize, 
			@RequestParam(name="pageNum",required = true,defaultValue="1") int pageNum,
			@RequestParam(name="releaseName",required=false) String releaseName,
			@RequestParam(name="lifecycleName",required=false) String lifecycleName,
			@RequestParam(name="description",required=false) String description,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName){
		return service.listReleases(releaseName, lifecycleName, description, pageSize, pageNum, sortName, sortOrder);
	}
	
	@RequestMapping(value="deleteRelease", method=RequestMethod.DELETE)
	public String deleteRelease(@Context HttpServletRequest request){
		String id=request.getParameter("id");
		return service.deleteRelease(id);
	}
	@RequestMapping(value="updateRelease",method=RequestMethod.PUT)
	public String updateRelease(@Context HttpServletRequest request,
			@RequestParam("id") String id,@RequestParam("name") String name,
			@RequestParam(name="description",required=false) String description,@RequestParam("lifecycleId") String lifecycleId){
		return service.updateRelease(id, name, description, lifecycleId);
	}
	
	@RequestMapping(value = "/checkReleaseName",method = RequestMethod.GET)
	public String checkReleaseName(@RequestParam("releaseName") String releaseName, @RequestParam(name="id",required=false) String id) {
		return service.checkReleaseName(releaseName,id);
	}
	
	@RequestMapping(value = "/listLifecycles",method = RequestMethod.GET)
	public String listLifecycles(@Context HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		return service.listLifecycles(user.getId());
	}
	
	@RequestMapping(value="addReleaseApps",method=RequestMethod.POST)
	public String addReleaseApps(@RequestParam("releaseId") String releaseId,@RequestParam("resourceIds") String resourceIds){
		return service.addReleaseApps(releaseId, resourceIds);
	}
	
	@RequestMapping(value = "/getReleaseDetail",method = RequestMethod.GET)
	public String getReleaseDetail(@Context HttpServletRequest request,@RequestParam("releaseId") String releaseId) {
//		User user = (User) request.getSession().getAttribute("user");
		return service.getReleaseDetail(releaseId);
	}
	
	@RequestMapping(value="updateReleaseStageEnv",method=RequestMethod.PUT)
	public String updateReleaseEnv(
			@RequestParam("releaseName") String releaseName,
			@RequestParam("blueInstanceId") String blueprintInsId,
			@RequestParam("cdFlowId") String cdFlowId){
		return service.updateReleaseEnvFlow(releaseName,blueprintInsId,cdFlowId);
	}
	
	@RequestMapping(value="deleteReleaseApp", method=RequestMethod.DELETE)
	public String deleteReleaseApp(@Context HttpServletRequest request){
		String releaseId=request.getParameter("releaseId");
		String resourceId=request.getParameter("resourceId");
		return service.deleteReleaseApp(releaseId,resourceId);
	}
	
	@RequestMapping(value = "/listReleaseApps",method = RequestMethod.GET)
	public String listReleaseApps(@Context HttpServletRequest request,@RequestParam("releaseName") String releaseName) {
//		User user = (User) request.getSession().getAttribute("user");
		return service.listReleaseApps(releaseName);
	}
	@RequestMapping(value = "/getReleaseStageEnvs",method = RequestMethod.GET)
	public String getReleaseStageEnvs(@Context HttpServletRequest request,@RequestParam("releaseName") String releaseName
			,@RequestParam("resourceName") String resourceName) {
//		User user = (User) request.getSession().getAttribute("user");
		return service.getReleaseStageEnvs(releaseName,resourceName);
	}
	
	@RequestMapping(value="saveReleaseStageEnv",method=RequestMethod.POST)
	public String saveReleaseStageEnv(@Context HttpServletRequest request,@RequestParam("releaseName") String releaseName,
			@RequestParam("resourceName") String resourceName,@RequestParam("blueprintInsName") String blueprintInsName,
			@RequestParam("phaseId") String phaseId,@RequestParam("blueprintId") String blueprintId
			,@RequestParam("cdFlowId") String cdFlowId,@RequestParam("resPoolConfig") String resPoolConfig){
		User user = (User) request.getSession().getAttribute("user");
		return service.saveReleaseStageEnv(releaseName,resourceName,blueprintInsName,phaseId,blueprintId,cdFlowId,resPoolConfig,user.getId()+"");
	}
	
	@RequestMapping(value="relieveReleaseStageEnv",method=RequestMethod.PUT)
	public String relieveReleaseStageEnv(@Context HttpServletRequest request,@RequestParam("releaseName") String releaseName,
			@RequestParam("resourceName") String resourceName,@RequestParam("phaseId") String phaseId){
		return service.relieveReleaseStageEnv(releaseName, resourceName, phaseId);
	}
}
