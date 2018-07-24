package com.dc.appengine.cloudui.ws.server;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.propertyeditors.URLEditor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.dc.appengine.cloudui.entity.MasterEnv;
import com.dc.appengine.cloudui.utils.BluePrint2Toolbar;
import com.dcits.Common.entity.User;

@Path("blueprint")
public class BluePrintRestService {
	@POST
	@Path("saveAllBlueprint")
	public String saveComponentFlow(@FormParam("blueprint_info") String  blueprint_info,
			@FormParam("blueprint_name") String  blueprint_name,
			@FormParam("user_id") String  user_id){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("blueprint_info", blueprint_info);
		requestEntity.add("blueprint_name", blueprint_name);
		requestEntity.add("user_id", user_id);
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/blueprint/saveAllBlueprint",
						requestEntity, String.class);
		return result;
	}
	@POST
	@Path("saveBlueprintTemplate")
	public String saveBlueprintTemplate(@FormParam("blueprint_info") String  blueprint_info,
			@FormParam("blueprint_name") String  blueprint_name,
			@FormParam("user_id") String  user_id,
			@FormParam("blueprintDesc") String  blueprintDesc){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("blueprint_info", blueprint_info);
		requestEntity.add("blueprint_name", blueprint_name);
		requestEntity.add("blueprintDesc", blueprintDesc);
		requestEntity.add("user_id", user_id);
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/blueprint/saveBlueprintTemplate",
						requestEntity, String.class);
		return result;
	}
	@POST
	@Path("delBlueprintTemplate")
	public String delBlueprintTemplate(@FormParam("blueprintId") String  blueprint_id){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("blueprint_id", blueprint_id);
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/blueprint/delBlueprintTemplate",
						requestEntity, String.class);
		return result;
	}
	@GET
	@Path("deployBlueprintInstance")
	public String deployBlueprintInstance(@QueryParam("instance_id") String instance_id){
		RestTemplate restUtil = new RestTemplate();
		String query = "instance_id="+instance_id;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/sendDeployMessage?"+query, String.class);
		return result;
	}
	
	@GET
	@Path("startBlueprintInstance")
	public String startBlueprintInstance(@QueryParam("instance_id") String instanceId){
		RestTemplate restUtil = new RestTemplate();
		String query = "instance_id="+instanceId;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/startBlueprintInstance?"+query, String.class);
		return result;
	}
	
	@GET
	@Path("stopBlueprintInstance")
	public String stopBlueprintInstance(@QueryParam("instance_id") String instanceId){
		RestTemplate restUtil = new RestTemplate();
		String query = "instance_id="+instanceId;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/stopBlueprintInstance?"+query, String.class);
		return result;
	}
	
	@GET
	@Path("destroyBlueprintInstance")
	public String destroyBlueprintInstance(@QueryParam("instance_id") String instanceId){
		RestTemplate restUtil = new RestTemplate();
		String query = "instance_id="+instanceId;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/destroyBlueprintInstance?"+query, String.class);
		return result;
	}
	
	@GET
	@Path("listBlueprints")
	public String listBlueprints(@Context HttpServletRequest request,
			@QueryParam("pageSize") @DefaultValue("10") int pageSize, 
			@QueryParam("pageNum") @DefaultValue("1") int pageNum,
			@QueryParam("blueprintName") @DefaultValue("") String blueprintName){
		User user = (User) request.getSession().getAttribute("user");
		if(user == null){
			return "{\"result\":false,\"message\":\"" + "用户未登录" + "\"}";
		}
		RestTemplate restUtil = new RestTemplate();
		String query = "pageSize="+pageSize+"&pageNum="+pageNum+"&blueprintName="+blueprintName+"&userId="+user.getId();
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/listBlueprints?"+query, String.class);
		return result;
	}
	
	@GET
	@Path("listBlueprintInstances")
	public String listBlueprintInstances(
			@QueryParam("pageSize") @DefaultValue("10") int pageSize, 
			@QueryParam("pageNum") @DefaultValue("1") int pageNum,
			@QueryParam("blueprintId") @DefaultValue("") String blueprintId,
			@QueryParam("instanceName") @DefaultValue("") String instanceName){
		RestTemplate restUtil = new RestTemplate();
		String query = "pageSize="+pageSize+"&pageNum="+pageNum
				+"&blueprintId="+blueprintId+"&instanceName="+instanceName;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/listBlueprintInstances?"+query, String.class);
		return result;
	}
	@GET
	@Path("findLabelsByCluster")
	public String findLabelsByCluster(
			@QueryParam("cluster_id") @DefaultValue("") String cluster_id){
		RestTemplate restUtil = new RestTemplate();
		String query = "cluster_id="+cluster_id;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/findLabelsByCluster?"+query, String.class);
		return result;
	}


	@GET
	@Path("findNodesByLabel")
	public String findNodesByLabel(
			@QueryParam("cluster_id") @DefaultValue("") String cluster_id,
			@QueryParam("label_key") @DefaultValue("") String label_key,
			@QueryParam("label_value") @DefaultValue("") String label_value){
		RestTemplate restUtil = new RestTemplate();
		String query = "cluster_id="+cluster_id+"&label_key="+label_key
				+"&label_value="+label_value;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/findNodesByLabel?"+query, String.class);
		return result;
	}
	
	@GET
	@Path("startBlueprintInstanceOne")
	public String startBlueprintInstanceOne(@QueryParam("appId") long appId){
		RestTemplate restUtil = new RestTemplate();
		String query = "appId="+appId;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/startBlueprintInstanceOne?"+query, String.class);
		return result;
	}
	
	@GET
	@Path("stopBlueprintInstanceOne")
	public String stopBlueprintInstanceOne(@QueryParam("appId") long appId){
		RestTemplate restUtil = new RestTemplate();
		String query = "appId="+appId;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/stopBlueprintInstanceOne?"+query, String.class);
		return result;
	}
	
	@GET
	@Path("destroyBlueprintInstanceOne")
	public String destroyBlueprintInstanceOne(@QueryParam("appId") long appId){
		RestTemplate restUtil = new RestTemplate();
		String query = "appId="+appId;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/destroyBlueprintInstanceOne?"+query, String.class);
		return result;
	}
	
	@GET
	@Path("dupdateBlueprintInstanceOne")
	public String dupdateBlueprintInstanceOne(@QueryParam("appId") long appId){
		RestTemplate restUtil = new RestTemplate();
		String query = "appId="+appId;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/dupdateBlueprintInstanceOne?"+query, String.class);
		return result;
	}
	
	@GET
	@Path("deployBlueprintInstanceOne")
	public String deployBlueprintInstanceOne(@QueryParam("appId") long appId){
		RestTemplate restUtil = new RestTemplate();
		String query = "appId="+appId;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/deployBlueprintInstanceOne?"+query, String.class);
		return result;
	}
	
	@POST
	@Path("saveBlueprintInstance")
	public String saveBlueprintInstance(
			@FormParam("blueprint_info") String  blueprint_info,
			@FormParam("blueprint_name") String  blueprint_name,
			@FormParam("blueprintdesc") String  blueprint_desc,
			@FormParam("user_id") String  user_id){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("blueprint_info", blueprint_info);
		requestEntity.add("blueprint_name", blueprint_name);
		requestEntity.add("blueprint_desc", blueprint_desc);
		requestEntity.add("user_id", user_id);
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/blueprint/saveBlueprintInstance",
				requestEntity, String.class);
		return result;
	}
	
	@POST
	@Path("delBlueprintInstance")
	public String delBlueprintInstance(@FormParam("blueprintInstanceId") int id){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("blueprintInstanceId", id+"");
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/blueprint/delBlueprintInstance",
				requestEntity, String.class);
		return result;
	}
	
	@POST
	@Path("generateSpecificBPFlow")
	public String generateSpecificBPFlow(@FormParam("bp") String bp){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("bp", bp);
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/blueprint/generateSpecificBPFlow",
				requestEntity, String.class);
		return result;
	}
	
	@GET
	@Path("viewBluePrintInstanceFlow")
	public String viewBluePrintInstanceFlow(
			@QueryParam("flowId")String flowId){
		RestTemplate restUtil = new RestTemplate();
		String queryStr = "flowId="+flowId;
		return restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/viewBluePrintInstanceFlow?"+queryStr,String.class);
	}
	
	@POST
	@Path("addBlueprintFlow")
	public String addBlueprintFlow(
			@FormParam("flowType")String flowType,
			@FormParam("flowInfo")String flowInfo,
			@FormParam("blueprintInstanceId")String blueprintInstanceId){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("flowType", flowType);
		requestEntity.add("flowInfo", flowInfo);
		requestEntity.add("blueprintInstanceId", blueprintInstanceId);
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/blueprint/addBlueprintFlow",
				requestEntity, String.class);
		return result;
	}
	
	@GET
	@Path("listBpInstanceFlows")
	public String listBpInstanceFlows(@QueryParam("blueprintInstanceId") String blueprintInstanceId){
		RestTemplate restUtil = new RestTemplate();
		String query = "blueprintInstanceId="+blueprintInstanceId;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+
				"/blueprint/listBpInstanceFlows?"+query, String.class);
		return result;
	}
	
	@GET
	@Path("executeBlueprintFlow")
	@Produces("application/json;charset=UTF-8")
	public String executeBlueprintFlow(@QueryParam("flowId") String flowId){
		RestTemplate restUtil = new RestTemplate();
		String query = "flowId="+flowId;
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+
				"/blueprint/executeBlueprintFlow?"+query, String.class);
		return result;
	}
	
	@POST
	@Path("updateBlueprintFlow")
	public String updateBlueprintFlow(@FormParam("flowInfo")String flowInfo,
			@FormParam("flowId") String flowId){
		RestTemplate restUtil = new RestTemplate();
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("flowId", flowId);
		requestEntity.add("flowInfo", flowInfo);
		String result = restUtil.postForObject(MasterEnv.MASTER_REST+"/blueprint/updateBlueprintFlow",
				requestEntity, String.class);
		return result;
	}
	
	@GET
	@Path("checkBlueprintFlowUnique")
	@Produces("application/json; charset=UTF-8")
	public String checkBlueprintFlowUnique(@QueryParam("bpInstanceId") String bpInstanceId,
			@QueryParam("type") String type) throws RestClientException, UnsupportedEncodingException{
		if(type==null || type.equals("")){
			return "{\"result\":false,\"message\":\"" + "" + "\"}";
		}
		RestTemplate restUtil = new RestTemplate();
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+
				"/blueprint/checkBlueprintFlowUnique?bpInstanceId="+bpInstanceId+"&type="+type, String.class);
		return result;
	}
	
	@GET
	@Path("getBlueprintTemplate")
	public String getBlueprintTemplate(@QueryParam("bpName") String bpName){
		RestTemplate restUtil = new RestTemplate();
		String result = restUtil.getForObject(MasterEnv.MASTER_REST+
				"/blueprint/getBlueprintTemplate?bpName="+bpName, String.class);
		return result;
	}
}
