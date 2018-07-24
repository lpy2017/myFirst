package com.dc.appengine.appmaster.ws.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.impl.MonitorService;
import com.dc.appengine.appmaster.utils.MessageHelper;

@Controller
@RequestMapping("/ws/monitor")
public class MonitorRestService {

	private static final Logger log = LoggerFactory.getLogger(MonitorRestService.class);

	@Resource
	private MonitorService monitorService;
	@Resource
	IBlueprintService blueprintService;

	/*
	 * 获取流程节点树
	 */
	@RequestMapping(value = "/getFlowNodes", method = RequestMethod.GET)
	@ResponseBody
	public String getFlowNodes(@RequestParam("id") String id, @RequestParam("flowId") String flowId) {
		try {
			return monitorService.getFlowNodes(id, flowId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取流程节点异常！", e.getMessage());
			return MessageHelper.wrap("result", false, "message", "获取流程节点异常！");
		}
	}

	@RequestMapping(value = "/getProcessMonitorInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getProcessMonitorInfo(@RequestParam("id") String id, @RequestParam("flowId") String flowId) {
		try {
			return monitorService.getProcessMonitorInfo(id, flowId);
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
	@RequestMapping(value = "/getPluginNodeLogInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getPluginNodeLogInfo(@RequestParam("id") String tokenId) {
		try {
			return blueprintService.getNodesLogRecord(tokenId);
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
	@RequestMapping(value = "/getFlowInstanceIds", method = RequestMethod.GET)
	@ResponseBody
	public String getFlowInstanceIds(@RequestParam(name="pageSize",required = true,defaultValue="10") int pageSize, 
			@RequestParam(name="pageNum",required = true,defaultValue="1") int pageNum,
			@RequestParam("bluePrintInsId") String bluePrintInsId,@RequestParam("flowId") String flowId,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName,
			@RequestParam(name="instanceId",required=false) String instanceId,
			@RequestParam(name="flag",required=false) String flag) {
		try {
			String result = blueprintService.listFlowInstanceIds(pageSize,pageNum,bluePrintInsId, flowId,sortName,sortOrder,instanceId,flag);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("获取流程实例失败！", e.getMessage());
			return MessageHelper.wrap("result", false, "message", "获取流程实例失败！");
		}
	}
	
	/*
	 * id:流程tokenid
	 * state:"7"状态
	 */
	@RequestMapping(value = "/meddleFlow", method = RequestMethod.GET)
	@ResponseBody
	public String meddleFlow(@RequestParam("id") String tokenId,@RequestParam("state") String state ) {
		try {
			String result = monitorService.meddleFlow(tokenId, state);
			return result;
		} catch (Exception e) {
			log.error("流程干预失败！", e.getMessage());
			return MessageHelper.wrap("result", false, "message", "流程干预失败！");
		}
	}
	
	/*
	 * 结束流程
	 * id:流程instanceId
	 */
	@RequestMapping(value = "/endInstance", method = RequestMethod.GET)
	@ResponseBody
	public String endInstance(@RequestParam("id") String instanceId) {
		try {
			String result = monitorService.endInstance(instanceId);
			return result;
		} catch (Exception e) {
			log.error("结束流程失败！", e.getMessage());
			return MessageHelper.wrap("result", false, "message", "结束流程失败！");
		}
	}
	
	/*
	 * 通过蓝图实例id,获取流程的运行实例
	 * 组件级别的
	 */
	@RequestMapping(value = "/getSecondFlowInstanceDetail", method = RequestMethod.GET)
	@ResponseBody
	public String getSecondFlowInstanceDetail(@RequestParam(name="pageSize",required = true,defaultValue="10") int pageSize, 
										      @RequestParam(name="pageNum",required = true,defaultValue="1") int pageNum,
										      @RequestParam("bluePrintInsId") String bluePrintInsId,
										      @RequestParam("flowName") String flowName,
										      @RequestParam("appName") String appName,
										      @RequestParam(name="sortOrder",required=false) String sortOrder,
											  @RequestParam(name="sortName",required=false) String sortName
										      ) {
		try {
			String result = blueprintService.getFlowInstanceIds(pageSize,pageNum,bluePrintInsId, flowName,appName,sortName,sortOrder);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("获取流程实例失败！", e.getMessage());
			return MessageHelper.wrap("result", false, "message", "获取流程实例失败！");
		}
	}
	
	/*
	 * 获取流程节点树
	 */
	@RequestMapping(value = "/getFlowTree", method = RequestMethod.GET)
	@ResponseBody
	public String getFlowTree(@RequestParam("flowInstanceId") String flowInstanceId, @RequestParam("flowId") String flowId) {
		try {
			return monitorService.getFlowTree(flowInstanceId, flowId);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取流程树异常！", e.getMessage());
			return MessageHelper.wrap("result", false, "message", "获取流程树异常！");
		}
	}

	@RequestMapping(value = "/getFlowMonitorInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getFlowMonitorInfo(@RequestParam("flowInstanceId") String flowInstanceId, @RequestParam("flowId") String flowId) {
		try {
			return monitorService.getFlowMonitorInfo(flowInstanceId, flowId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("获取流程监控信息失败！", e.getMessage());
			return MessageHelper.wrap("result", false, "message", "获取流程监控信息失败！");
		}
	}
	
	/*
	 * 保存流程执行记录
	 */
	@RequestMapping(value = "/saveFlowInstances", method = RequestMethod.GET)
	@ResponseBody
	public String saveFlowInstances(@RequestParam("bluePrintInstanceId") String bluePrintInstanceId,
			@RequestParam("instanceId") String instanceId,
			@RequestParam("flowName") String flowName) {
		try {
			return blueprintService.saveInstanceId(bluePrintInstanceId,instanceId,flowName);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("查看流程节点日志信息失败！", e.getMessage());
			return MessageHelper.wrap("result", false, "message", "查看流程节点日志信息失败！");
		}
	}
	/*
	 * 获取部署的监控指标
	 */
	@RequestMapping(value="getDeployIndicators",method=RequestMethod.POST)
	@ResponseBody
	public String getDeployIndicators(@RequestParam("series") String series,
			@RequestParam(name="releaseTypes",required=false) String releaseTypes,@RequestParam("intervalType") String intervalType
			,@RequestParam("start") long start,@RequestParam("end") long end,@RequestParam("indicatorType") String indicatorType){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		if(releaseTypes==null){
			releaseTypes = JSON.toJSONString(new ArrayList<>());
		}
		String result = monitorService.getDeployIndicators(JSON.parseArray(series, String.class), JSON.parseArray(releaseTypes, String.class), intervalType, 
				sf.format(start), sf.format(end), indicatorType);
//		System.out.println(result);
		return result;
	}
}
