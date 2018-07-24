package com.dc.appengine.cloudui.ws.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.cloudui.entity.MasterEnv;
import com.dc.appengine.cloudui.master.service.impl.LogService;
import com.dc.appengine.cloudui.utils.MessageHelper;
import com.dc.appengine.cloudui.ws.client.WSRestClient;

@Path("log")
public class LogRestService {

	private static final Logger log = LoggerFactory.getLogger(LogRestService.class);

	@Autowired
	@Qualifier("LogService")
	private LogService logService;

	/*
	 * 获取流程节点树
	 */
	@GET
	@Path("getFlowNodes")
	public String getFlowNodes(@QueryParam("id") String id, @QueryParam("flowId") String flowId) {
		try {
			return logService.getFlowNodes(id, flowId);
		} catch (Exception e) {
			log.error("获取流程节点异常！", e.getMessage());
			return MessageHelper.wrap("result", false, "message", "获取流程节点异常！");
		}
	}

	/*
	 * 获取流程的运行实例
	 */
	@GET
	@Path("getFlowInstanceId")
	public String getFlowInstanceId(@QueryParam("flowId") String flowId, @QueryParam("latestLog") Boolean latestLog) {
		try {
			Map<String, Object> map = new HashMap<>();
			Form form = new Form();
			map.put("flowid", flowId);
			form.param("paramMap", JSON.toJSONString(map));
			String result = WSRestClient.getFrameWebTarget().path("WFService").path("findCDInstanceIdsByMap.wf")
					.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
			Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
			});
			List<Map<String, Object>> list = JSON.parseObject(resultMap.get("data") + "",
					new TypeReference<List<Map<String, Object>>>() {
					});
			List<Map<String, Object>> listResult = new ArrayList<>();
			if (latestLog != null && latestLog && list.size() > 0) {
				listResult.add(list.get(0));
			} else {
				for (Map one : list) {
					Map mapOne = new HashMap<>();
					mapOne.put("instanceId", one.get("ID"));
					mapOne.put("startTime", one.get("STARTTIME"));
					mapOne.put("endTime", one.get("ENDTTIME"));
					listResult.add(mapOne);
				}
			}
			return JSON.toJSONString(listResult);
		} catch (Exception e) {
			// TODO: handle exception
			log.error("获取流程实例失败！", e.getMessage());
			return MessageHelper.wrap("result", false, "message", "获取流程实例失败！");
		}
	}

	/*
	 * 获取流程监控信息
	 */
	@GET
	@Path("getProcessMonitorInfo")
	public String getProcessMonitorInfo(@QueryParam("id") String id, @QueryParam("flowId") String flowId) {
		try {
			return logService.getProcessMonitorInfo(id, flowId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("获取流程监控信息失败！", e.getMessage());
			return MessageHelper.wrap("result", false, "message", "获取流程监控信息失败！");
		}
	}

	/*
	 * 获取插件执行日志
	 */
	@GET
	@Path("getPluginNodeLogInfo")
	public String getPluginNodeLogInfo(@QueryParam("flowInstanceId") String flowInstanceId,
			@QueryParam("flowNodeId") String flowNodeId) {
		try {
			log.debug("查看流程节点日志信息，请求流程实例 flowInstanceId = " + flowInstanceId + "flowNodeId =" + flowNodeId);
			return logService.getPluginNodeLogInfo(flowInstanceId, flowNodeId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("查看流程节点日志信息失败！", e.getMessage());
			return MessageHelper.wrap("result", false, "message", "查看流程节点日志信息失败！");
		}
	}
	
	/*
	 * 通过蓝图实例id,获取流程的运行实例
	 */
	@GET
	@Path("getFlowInstanceIds")
	public String getFlowInstanceIds(@QueryParam("bluePrintInsId") String bluePrintInsId,@QueryParam("flowName") String flowName ) {
		try {
			RestTemplate restUtil = new RestTemplate();
			String query = "bluePrintInsId="+bluePrintInsId+"&"+"flowName="+flowName;
			String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/getFlowInstanceIds?"+query, String.class);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("获取流程实例失败！", e.getMessage());
			return MessageHelper.wrap("result", false, "message", "获取流程实例失败！");
		}
	}
}
