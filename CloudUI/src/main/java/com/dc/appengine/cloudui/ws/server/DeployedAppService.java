package com.dc.appengine.cloudui.ws.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dcits.Common.entity.User;
import com.dc.appengine.cloudui.ws.client.WSRestClient;

@Path("deployedApp")
public class DeployedAppService {

	@GET
	@Path("listApps")
	public String listDeployedApps(@Context HttpServletRequest request,
			@QueryParam("pageSize") int pageSize, 
			@QueryParam("pageNum") int pageNum,
			@QueryParam("blueInstanceId") int blueInstanceId,
			@QueryParam("sortName") @DefaultValue("") String sortName, 
			@QueryParam("sortOrder") @DefaultValue("") String sortOrder
			){
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return "{\"result\":false,\"message\":\"" + "用户未登录" + "\"}";
		}
		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("deployedApp").path("listApps").queryParam("userId", 
				user.getId().toString()).queryParam("pageSize", pageSize)
				.queryParam("pageNum", pageNum)
				.queryParam("blueInstanceId", blueInstanceId)
				.queryParam("sortName", sortName)
				.queryParam("sortOrder", sortOrder)
				.request().get(String.class);
	}
	
	@GET
	@Path("listInstances")
	public String listInstances(@Context HttpServletRequest request,
			@QueryParam("pageSize") int pageSize, 
			@QueryParam("pageNum") int pageNum,
			@QueryParam("appId") long appId,
			@QueryParam("sortName") @DefaultValue("") String sortName, 
		    @QueryParam("sortOrder") @DefaultValue("") String sortOrder
			){
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return "{\"result\":false,\"message\":\"" + "用户未登录" + "\"}";
		}
		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("deployedApp").path("listInstances").queryParam("appId", 
				appId).queryParam("pageSize", pageSize)
				.queryParam("pageNum", pageNum)
				.queryParam("sortName", sortName)
			    .queryParam("sortOrder", sortOrder)
				.request().get(String.class);
	}
	
	@POST
	@Path("delInstance")
	public String delInstance(@Context HttpServletRequest request,
			@FormParam("instanceId") String instanceId){
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return "{\"result\":false,\"message\":\"" + "用户未登录" + "\"}";
		}
		WebTarget target = WSRestClient.getMasterWebTarget();
		Form form= new Form();
		form.param("instanceId", instanceId);
		return target.path("deployedApp/delInstance").request().
				post(Entity.entity(form,
						MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	
	@GET
	@Path("getAppInfo")
	public String getAppInfo(@QueryParam("appId") String appId) {
		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("deployedApp").path("getAppInfo").queryParam("appId", 
				appId)
				.request().get(String.class);
	}
	
	@GET
	@Path("listBlueInstances")
	public String listBlueInstances(@Context HttpServletRequest request,
			@QueryParam("pageNum") @DefaultValue("1") int pageNum,
			@QueryParam("pageSize") int pageSize,
			@QueryParam("key") String key){
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return "{\"result\":false,\"message\":\"" + "用户未登录" + "\"}";
		}
		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("deployedApp").path("listBlueInstances")
				.queryParam("pageNum", pageNum)
				.queryParam("pageSize", pageSize)
				.queryParam("key", key)
				.queryParam("userId", user.getId())
				.request().get(String.class);
	}
	
	@GET
	@Path("getBlueInstanceDetailInfo")
	public String getBlueInstanceDetailInfo(@QueryParam("blueInstanceId") int blueInstanceId){
		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("deployedApp").path("getBlueInstanceDetailInfo")
				.queryParam("blueInstanceId", blueInstanceId)
				.request().get(String.class); 
	}
	
	// 蓝图实例快照
	@POST
	@Path("saveSnapshot")
	public String saveSnapshot(@Context HttpServletRequest request,
			@FormParam("blueInstanceId") int blueInstanceId) {
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return "{\"result\":false,\"message\":\"" + "用户未登录" + "\"}";
		}
		Form form= new Form();
		form.param("blueInstanceId", blueInstanceId+"");
		form.param("userId", user.getId()+"");
		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("deployedApp/saveSnapshot").request().
				post(Entity.entity(form,
						MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	
	@GET
	@Path("getAppVersionList")
	public String getAppVersion(@Context HttpServletRequest request,
			@QueryParam("pageSize") int pageSize, 
			@QueryParam("pageNum") int pageNum,
			@QueryParam("appId") long appId){
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return "{\"result\":false,\"message\":\"" + "用户未登录" + "\"}";
		}
		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("deployedApp").path("getAppVersionList").queryParam("appId", 
				appId).queryParam("pageSize", pageSize)
				.queryParam("pageNum", pageNum)
				.request().get(String.class); 
	}
	
	@GET
	@Path("getConfigInfoByVersionId")
	public String getConfigInfoByVersionId(@QueryParam("configId") String configId,
			@QueryParam("appId") long appId){
		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("deployedApp").path("getConfigInfoByVersionId")
				.queryParam("configId", configId)
				.queryParam("appId", appId)
				.request().get(String.class); 
	}
	
	@POST
	@Path("updateLine")
	public String updateLine(@FormParam("resId") String resId,
			@FormParam("appId") long appId,
			@FormParam("key") String key,
			@FormParam("value") String value){
		Form form= new Form();
		form.param("resId", resId);
		form.param("appId", appId+"");
		form.param("key", key);
		form.param("value", value);
		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("deployedApp/updateLine").request().
				post(Entity.entity(form,
						MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
	}
	
	@GET
	@Path("removeLine")
	public String removeLine(@QueryParam("resId") String resId,
			@QueryParam("appId") long appId,
			@QueryParam("key") String key,
			@QueryParam("value") String value){

		WebTarget target = WSRestClient.getMasterWebTarget();
		return target.path("deployedApp").path("removeLine")
				.queryParam("resId", resId)
				.queryParam("appId", appId)
				.queryParam("key", key)
				.queryParam("value", value)
				.request().get(String.class); 
	}
	
	//获取组件的版本列表
		@GET
		@Path("/getResourceVersion")
		public String getResourceVersion(@QueryParam("appId") long appId){
			
			WebTarget target = WSRestClient.getMasterWebTarget();
			return target.path("deployedApp").path("getResourceVersion")
					.queryParam("appId", appId)
					.request().get(String.class); 		
			}	

		//更新組件實例的版本
		@POST
		@Path("updateResVersionInstance")
		public String updateResVersionInstance(@FormParam("resVersionId") String resVersionId,
				@FormParam("appId") long appId,
				@FormParam("oldResVersionId") String oldResVersionId){
			
			Form form= new Form();
			form.param("resVersionId", resVersionId);
			form.param("appId", appId+"");
			form.param("oldResVersionId", oldResVersionId);
			WebTarget target = WSRestClient.getMasterWebTarget();
			return target.path("deployedApp/updateResVersionInstance").request().
					post(Entity.entity(form,
							MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);		
		}
		
		@POST
		@Path("maintainAppInstances")
		public String maintainAppInstances(@FormParam("resouceVersionId") String resouceVersionId,
				@FormParam("appId") long appId,
				@FormParam("targetCount") int targetCount){
			Form form= new Form();
			form.param("resouceVersionId", resouceVersionId);
			form.param("appId", appId+"");
			form.param("targetCount", targetCount+"");
			WebTarget target = WSRestClient.getMasterWebTarget();
			return target.path("RC/maintainAppInstances").request().
					post(Entity.entity(form,
							MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);		
		}
		
		@GET
		@Path("/updateRCScalable")
		public String updateRCScalable(@QueryParam("appId") long appId,
				@QueryParam("enable") boolean enable){
			
			WebTarget target = WSRestClient.getMasterWebTarget();
			return target.path("RC").path("updateRCScalable")
					.queryParam("appId", appId)
					.queryParam("enable", enable)
					.request().get(String.class); 		
		}
		
//--------------------------组件快照---------------------------
		//保存组件的快照
		@POST
		@Path("/saveSnapshotOfApp")
		public String saveSnapshotOfApp(@Context HttpServletRequest request,
				@FormParam("appId") int appId,
				@FormParam("snapshotName") String snapshotName) {
			User user = (User) request.getSession().getAttribute("user");
			if(user == null){
				return "{\"result\":false,\"message\":\"" + "用户未登录" + "\"}";
			}
			Form form= new Form();
			form.param("appId", appId+"");
			form.param("userId", user.getId()+"");
			form.param("snapshotName", snapshotName);
			WebTarget target = WSRestClient.getMasterWebTarget();
			return target.path("deployedApp/saveSnapshotOfApp").request().
					post(Entity.entity(form,
							MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
		}
		
		//删除组件的快照
		@DELETE
		@Path("/deleteSnapshotOfApp/{snapId}")
		public String deleteSnapshotOfApp(@Context HttpServletRequest request, @Context HttpServletResponse response,
				@PathParam("snapId") String snapId) {
			User user = (User) request.getSession().getAttribute("user");
			if(user == null){
				return "{\"result\":false,\"message\":\"" + "用户未登录" + "\"}";
			}
			Form form= new Form();
			form.param("snapId", snapId);
			WebTarget target = WSRestClient.getMasterWebTarget();
			return target.path("deployedApp/deleteSnapshotOfApp").path(snapId).request().
					delete(String.class);
		}
		
		//快照列表
		@GET
		@Path("listSnapshotsOfApp")
		public String listSnapshotsOfApp(@Context HttpServletRequest request,
				@QueryParam("appId") int appId,
				@QueryParam("pageSize") int pageSize,
				@QueryParam("pageNum") int pageNum) {
			User user = (User) request.getSession().getAttribute("user");
			if(user == null){
				return "{\"result\":false,\"message\":\"" + "用户未登录" + "\"}";
			}
			WebTarget target = WSRestClient.getMasterWebTarget();
			return target.path("deployedApp").path("listSnapshotsOfApp")
					.queryParam("appId", appId)
					.queryParam("pageSize", pageSize)
					.queryParam("pageNum", pageNum)
					.queryParam("userId", user.getId())
					.request().get(String.class); 	
		}
		
		@GET
		@Path("getComponentVersionList")
		public String getComponentVersionList(@QueryParam("appName") String appName){
			WebTarget target = WSRestClient.getMasterWebTarget();
			return target.path("deployedApp").path("getComponentVersionList")
					.queryParam("appName", appName)
					.request().get(String.class); 
		}
		
		//快照回滚
		@POST
		@Path("appSnapshotRollBack")
		public String appSnapshotRollBack(@FormParam("snapId") String snapId,
				@FormParam("appId") long appId,@FormParam("type") int type){
			Form form= new Form();
			form.param("snapId", snapId);
			form.param("appId", appId+"");
			form.param("type", type+"");
			WebTarget target = WSRestClient.getMasterWebTarget();
			return target.path("deployedApp/appSnapshotRollBack").request().
					post(Entity.entity(form,
							MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
		}
		
		@GET
		@Path("getSnapshotInsInfo")
		public String getSnapshotInsInfo(@QueryParam("appId") long appId,
				@QueryParam("snapId") String snapId){
			WebTarget target = WSRestClient.getMasterWebTarget();
			return target.path("deployedApp").path("getSnapshotInsInfo")
					.queryParam("appId", appId)
					.queryParam("snapId", snapId)
					.request().get(String.class); 
		}
		
		@GET
		@Path("getSnapshotRollResultInfo")
		public String getSnapshotRollResultInfo(@QueryParam("appId") long appId,
				@QueryParam("snapId") String snapId){
			WebTarget target = WSRestClient.getMasterWebTarget();
			return target.path("deployedApp").path("getSnapshotRollResultInfo")
					.queryParam("appId", appId)
					.queryParam("snapId", snapId)
					.request().get(String.class); 
		}
		
		@POST
		@Path("saveAppUpdateOrRollbackFlow")
		public String saveAppUpdateOrRollbackFlow(@Context HttpServletRequest request,
				@FormParam("appId") long appId,
				@FormParam("json") String json,
				@FormParam("type") String type,
				@FormParam("snapId") String snapId){
			User user = (User) request.getSession().getAttribute("user");
			if(user == null){
				return "{\"result\":false,\"message\":\"" + "用户未登录" + "\"}";
			}
			Form form= new Form();
			form.param("appId", appId+"");
			form.param("json", json);
			form.param("userId", user.getId()+"");
			//type为updateFlow（升级），rollbackFlow（滚动回滚），rollbackQuickFlow（快速回滚）
			form.param("type", type);
			form.param("snapId", snapId);
			WebTarget target = WSRestClient.getMasterWebTarget();
			return target.path("deployedApp/saveAppUpdateOrRollbackFlow").request().
						post(Entity.entity(form,
								MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
		}
		
		@POST
		@Path("updateAppAllIns")
		public String updateAppAllIns(@FormParam("appId") long appId,
				@FormParam("resourceVersionId") String resourceVersionId,
				@FormParam("config") String config,@FormParam("json") String json){
			Form form= new Form();
			form.param("appId", appId+"");
			form.param("resourceVersionId", resourceVersionId);
			form.param("config", config);
			form.param("json", json);
			WebTarget target = WSRestClient.getMasterWebTarget();
			return target.path("deployedApp/updateAppAllIns").request().
						post(Entity.entity(form,
								MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
		}
		
		@POST
		@Path("rollbackJsonTo")
		public String rollbackJsonTo(@FormParam("appId") long appId,
				@FormParam("snapId") String snapId,
				@FormParam("json") String json){
			Form form= new Form();
			form.param("appId", appId+"");
			form.param("snapId", snapId);
			form.param("json", json);
			WebTarget target = WSRestClient.getMasterWebTarget();
			return target.path("deployedApp/rollbackJsonTo").request().
						post(Entity.entity(form,
								MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
		}
	
}
