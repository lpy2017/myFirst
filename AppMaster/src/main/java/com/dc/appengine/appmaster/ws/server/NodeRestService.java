package com.dc.appengine.appmaster.ws.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.INodeService;
import com.dc.appengine.appmaster.service.impl.ClusterService;
import com.dc.appengine.appmaster.service.impl.NodeNewService;
import com.dc.appengine.appmaster.utils.AESUtil;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("/ws/node")
public class NodeRestService {
	private static final Logger log = LoggerFactory
			.getLogger(NodeRestService.class);
	
	@Resource
	INodeService nodeService;
	
	@Resource
	private NodeNewService nodeNewService;
	
	@Resource
	IAudit auditService;
	
	@Resource
	private ClusterService service;
	
	@Value(value = "${lookup.object:node}")
	String lookupObject;
	
	@RequestMapping(value = "test",method = RequestMethod.GET)
	@ResponseBody
	public String test(){
		String content = "{'id': '31f068c0-18c5-421d-87c4-e309d5e2bea7','name': 'node123','cluster_id': '22f068c0-18c5-421d-87c4-e309d5e2bea7'," +
				"'ip': '10.1.1.111','cpu': '2','memory': '2048','disk': '100'," +
				"'labels': [{'key': 'db1','value': 'mysql1','type': '1'}]}";
		String uuid="31f068c0-18c5-421d-87c4-e309d5e2bea7";
		String ip ="";
		return nodeService.deleteNode(uuid,ip);
	}
	@RequestMapping(value = "addnode",method = RequestMethod.POST)
	@ResponseBody
	//mediatype：application/x-www-form-urlencoded
	public String addNode(@RequestParam("node_info") String content){
		try {
			nodeService.saveNode(content);
			return MessageHelper.wrap("result", true);
		} catch (Exception e) {
			log.error("failed to add node", e);
			return MessageHelper.wrap("result", false, "info", e.getMessage());
		}
	}
	
	@RequestMapping(value = "updatenode",method = RequestMethod.PUT)
	@ResponseBody
	//mediatype：application/x-www-form-urlencoded
	public String updateNode(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String nodeInfo){
		try {
			String result = nodeService.saveNode(nodeInfo);
			return result;
		} catch (Exception e) {
			log.error("failed to add node", e);
			return MessageHelper.wrap("result", false, "info", "node in use");
		}
	}
	
	@RequestMapping(value = "removenode",method = RequestMethod.DELETE)
	@ResponseBody
	//mediatype：application/x-www-form-urlencoded
	//id=uuid&ip=127.0.0.1
	public String removeNode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("id") String uuid,
			@RequestParam("ip") String ip){
		try {
			String result = nodeService.deleteNode(uuid,ip);
			return result;
		} catch (Exception e) {
			log.error("failed to add node", e);
			return MessageHelper.wrap("result", false, "info", "node in use");
		}
	}
	
	@RequestMapping(value="removeLabels",method=RequestMethod.POST)
	@ResponseBody
	public String removeLabels(@RequestParam("labels") String content){
		try {
			log.debug("receive labels to delete: "+content);
			String result=nodeService.deleteLabels(content);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("failed to delete labels", e);
			return MessageHelper.wrap("result", false, "info", "fail to delete labels");
		}
	}
	
	@RequestMapping(value="addLabels",method=RequestMethod.POST)
	@ResponseBody
	public String addLabels(@RequestParam("labels") String content){
		try {
			log.debug("receive labels to add: "+content);
			String result=nodeService.saveLabels(content);
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			log.error("failed to add labels", e);
			return MessageHelper.wrap("result", false, "info", "fail to add labels");
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String insert(HttpServletRequest request,@RequestBody Map<String, Object> body) {
		body.put("userPassword", AESUtil.defaultEncrypt((String) body.get("userPassword")));
		body.put("id", UUID.randomUUID().toString());
		Map<String, Object> clusterMap = service.one(""+body.get("clusterId"));
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int i = nodeNewService.insert(body);
			if (i == 1) {
				result.put("result", true);
			} else {
				result.put("info", "数据库插入失败");
				result.put("result", false);
			}
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					User user = (User) request.getSession().getAttribute("user");
					auditService.save(new AuditEntity(user.getName(), ResourceCode.ENVIRONMENT, ""+clusterMap.get("name"), ResourceCode.Operation.ADD, i == 1?1:0, "新增主机:" + body.get("name")));
				}
			});
			//==============添加审计end=====================
		} catch (Exception e) {
			log.error("", e);
			
			//==============添加审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					User user = (User) request.getSession().getAttribute("user");
					auditService.save(new AuditEntity(user.getName(), ResourceCode.ENVIRONMENT, ""+clusterMap.get("name"), ResourceCode.Operation.ADD, 0, "新增主机:" + body.get("name")));
				}
			});
			//==============添加审计end=====================
			
			result.put("info", e.getMessage());
			result.put("result", false);
			return JSON.toJSONString(result);
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	public Object delete(HttpServletRequest request) {
		String nodeId = request.getParameter("nodeId");
		Map<String, Object> result = new HashMap<String, Object>();
		if (nodeNewService.deleteCheck(nodeId) > 0) {
			result.put("info", "主机上有未卸载组件实例，请先卸载组件实例！");
			result.put("result", false);
			return result;
		}
		int i = nodeNewService.delete(nodeId);
		
		if (i == 1) {
			result.put("result", true);
		} else {
			result.put("info", "数据库删除失败");
			result.put("result", false);
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public Object update(@RequestBody Map<String, Object> body,HttpServletRequest request) {
		body.put("userPassword", AESUtil.defaultEncrypt((String) body.get("userPassword")));
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String nodeId = (String) body.get("id");
			if (nodeNewService.deleteCheck(nodeId) > 0) {
				result.put("info", "主机上有未卸载组件实例，请先卸载组件实例！");
				result.put("result", false);
				return result;
			}
			Map<String, Object> formerNode = nodeNewService.one(nodeId);
			String clusterId = ""+formerNode.get("clusterId");
			Map<String, Object> clusterMap = service.one(clusterId);
			String clusterName = ""+clusterMap.get("name");
			int i = nodeNewService.update(body);
			if (i == 1) {
				//==============添加审计start===================
				ThreadPool.service.execute(new Runnable(){
					@Override
					public void run(){
						User user = (User) request.getSession().getAttribute("user");
						auditService.save(new AuditEntity(user.getName(), ResourceCode.ENVIRONMENT, clusterName, ResourceCode.Operation.UPDATE, 1, "【"+JSON.toJSONString(formerNode)+"】 更新主机:【" + JSON.toJSONString(body)+"】"));
					}
				});
				//==============添加审计end=====================
				result.put("result", true);
			} else {
				//==============添加审计start===================
				ThreadPool.service.execute(new Runnable(){
					@Override
					public void run(){
						User user = (User) request.getSession().getAttribute("user");
						auditService.save(new AuditEntity(user.getName(), ResourceCode.ENVIRONMENT, clusterName, ResourceCode.Operation.UPDATE, 0, "【"+JSON.toJSONString(formerNode)+"】 更新主机:【" + JSON.toJSONString(body)+"】"));
					}
				});
				//==============添加审计end=====================
				result.put("info", "数据库更新失败");
				result.put("result", false);
			}
		} catch (Exception e) {
			log.error("", e);
			result.put("info", e.getMessage());
			result.put("result", false);
			return result;
		}
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String list(HttpServletRequest request) {
		int pageNum = Integer.valueOf(request.getParameter("pageNum"));
		int pageSize = Integer.valueOf(request.getParameter("pageSize"));
		//先查询所有的，然后排序，最后分页
		int tmpPageNum=1;
		int tmpPageSize=100000;
		String clusterId = request.getParameter("clusterId");
		String sortName=SortUtil.getColunmName("node",request.getParameter("sortName"));
		String sortOrder= request.getParameter("sortOrder");
		Map<String, Object> condition = new HashMap<>();
		condition.put("clusterId", clusterId);
//		condition.put("sortName", SortUtil.getColunmName("node",request.getParameter("sortName")));
//		condition.put("sortOrder", request.getParameter("sortOrder"));
		Page page = nodeNewService.list(condition, tmpPageNum, tmpPageSize);
		List<Map<String,Object>> nodeList = page.getRows();
		page.setPageSize(pageSize);
		page.setPageNumber(pageNum);
		page.setStartRowNum(pageSize*(pageNum-1));
		page.setEndRowNum(pageSize*pageNum);
		if(!JudgeUtil.isEmpty(nodeList)){
			List selectors =null;
			if("node".equals(lookupObject)){
				selectors = nodeNewService.lookupAliveNode();//获取监听mom中NodeService.out队列的节点数
			}else{
				selectors = nodeNewService.lookupAliveAutoAgent();
			}
			for(Map<String,Object> nodeMap : nodeList){
				if(selectors.contains(nodeMap.get("ip"))){
					nodeMap.put("isConnected", "已连接");
				}else{
					nodeMap.put("isConnected", "未连接");
				}
			}
			//排序
			SortUtil.sort(nodeList, sortName, sortOrder);
			//分页
			List rows = new ArrayList<>();
			int start =page.getStartRowNum();
			int end = page.getEndRowNum()>page.getTotal()?page.getTotal():page.getEndRowNum();
			for(int i=start;i<end;i++){
				Map nodeMap = nodeList.get(i);
				rows.add(nodeMap);
			}
			page.setRows(rows);
		}
		
		return JSON.toJSONString(page);
	}
	
	@RequestMapping(value = "listAll", method = RequestMethod.GET)
	public String listAll(HttpServletRequest request) {
		String clusterId = request.getParameter("clusterId");
		return JSON.toJSONString(nodeNewService.listAll(clusterId));
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public String one(@PathVariable("id") String nodeId) {
		return JSON.toJSONString(nodeNewService.one(nodeId));
	}
	
	@RequestMapping(value = "multiDelete", method = RequestMethod.DELETE)
	public Object multiDelete(HttpServletRequest request) {
		String[] nodes = request.getParameter("nodes").split(",");
		String clusterId = request.getParameter("clusterId");
		Map<String, Object> clusterMap = service.one(clusterId);
		Map<String, Object> result = new HashMap<>();
		boolean check = true;
		for (String nodeId : nodes) {
			if (nodeNewService.deleteCheck(nodeId) > 0) {
				check = false;
				break;
			}
		}
		Map<String,String> param = new HashMap();
		if (!check) {
			result.put("result", false);
			result.put("info", "主机上有未卸载组件实例，请先卸载组件实例！");
			return result;
		}
		if (nodes.length > 0) {
//			if (i == nodes.length) {
				result.put("result", true);
				for(int k=0;k<nodes.length;k++){
					Map<String, Object> nodeMap = nodeNewService.one(nodes[k]);
					int i = nodeNewService.multiDelete(new String[]{nodes[k]});
					//==============添加审计start===================
					ThreadPool.service.execute(new Runnable(){
						@Override
						public void run(){
							User user = (User) request.getSession().getAttribute("user");
							auditService.save(new AuditEntity(user.getName(), ResourceCode.ENVIRONMENT, ""+clusterMap.get("name"), ResourceCode.Operation.DELETE, 1, "删除主机:" + nodeMap.get("name")));
						}
					});
					//==============添加审计end=====================
				}
//			} else {
//				result.put("result", false);
//				result.put("info", "删除的主机数不一致");
//			}
		} else {
			result.put("result", false);
			result.put("info", "主机数不合法");
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "checkIP", method = RequestMethod.GET)
	public Object checkIP(HttpServletRequest request) {
		int i = nodeNewService.checkIP(request.getParameter("ip"));
		Map<String, Object> result = new HashMap<>();
		if (i > 0) {
			result.put("result", false);
			result.put("info", "IP已经存在");
		} else {
			result.put("result", true);
		}
		return result;
	}
	
	@RequestMapping(value = "checkOtherIP", method = RequestMethod.GET)
	public Object checkOtherIP(HttpServletRequest request) {
		Map<String, Object> node = new HashMap<>();
		node.put("id", request.getParameter("id"));
		node.put("ip", request.getParameter("ip"));
		int i = nodeNewService.checkOtherIP(node);
		Map<String, Object> result = new HashMap<>();
		if (i > 0) {
			result.put("result", false);
			result.put("info", "IP已经存在");
		} else {
			result.put("result", true);
		}
		return result;
	}
	
	@RequestMapping(value = "detectNode",method = RequestMethod.POST)
	@ResponseBody
	public String detectNode(@RequestBody JSONObject content){
		try {
			return nodeService.detectNode(content);
		} catch (Exception e) {
			log.error("failed to detect node", e);
			return MessageHelper.wrap("result", false, "info", e.getMessage());
		}
	}
}
