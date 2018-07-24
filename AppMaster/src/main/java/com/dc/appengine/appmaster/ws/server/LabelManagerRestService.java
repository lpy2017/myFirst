package com.dc.appengine.appmaster.ws.server;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

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
import com.dc.appengine.appmaster.service.ILabelsService;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("/ws/labelManager")
public class LabelManagerRestService {
	
	@Resource
	private ILabelsService service;
	
	@Resource
	IAudit auditService;
	
	@RequestMapping(value = "/saveLabelType",method = RequestMethod.POST)
	public String saveLabelType(@RequestBody JSONObject params,@Context HttpServletRequest request) {
		return service.saveLabelType(params);
	}
	
	@RequestMapping(value = "/deleteLabelType",method = RequestMethod.DELETE)
	public String deleteLabelType(HttpServletRequest request) {
		return service.deleteLabelType(request.getParameter("id"));
	}
	
	@RequestMapping(value = "/listLabelTypes",method = RequestMethod.GET)
	public String listLabelTypes(@RequestParam(name="pageSize",required = true,defaultValue="10") int pageSize, 
			@RequestParam(name="pageNum",required = true,defaultValue="1") int pageNum,
			@RequestParam(name="typeName",required=false) String typeName) {
		return service.listLabelTypes(pageSize, pageNum, typeName);
	}
	
	@RequestMapping(value = "/updateLabelType",method = RequestMethod.PUT)
	public String updateLabelType(@RequestBody JSONObject params) {
		return service.updateLabelType(params);
	}
	
	@RequestMapping(value = "/saveLabel",method = RequestMethod.POST)
	public String saveLabel(@RequestBody JSONObject params,@Context HttpServletRequest request) {
		
		String returnMsg = service.saveLabel(params);
		//==============添加审计start===================
		JSONObject jo = JSONObject.parseObject(returnMsg);
		boolean result = jo.getBoolean("result");
		String labelName = params.getString("name");
		int code = params.getIntValue("code");
		String labelType = null;
		String typeJson = service.listLabelTypes(9999, 1, null);
		Page page = JSON.parseObject(typeJson, Page.class);
		List<Map<String,Object>> pageList = page.getRows();
		for(Map<String,Object> map : pageList){
			if(code==(Integer)map.get("code")){
				labelType = (String)map.get("name");
				break;
			}
		}
		final String finalLabelName = labelType;
		final User user = (User) request.getSession().getAttribute("user");
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				auditService.save(new AuditEntity(user.getName(), ResourceCode.LABELMNG,labelName, ResourceCode.Operation.ADD, result?1:0, "新增标签:" + labelName+" 标签类型："+finalLabelName));
			}
		});
		//==============添加审计end=====================
		return returnMsg;
	}
	
	@RequestMapping(value = "/deleteLabel",method = RequestMethod.DELETE)
	public String deleteLabel(@Context HttpServletRequest request) {
		String labelName = request.getParameter("name");
		String typeJson = service.listAllLabels(new JSONObject());
		List<Map<String,Object>> labelList = JSON.parseObject(typeJson, List.class);
		String id = request.getParameter("id");
		String tempLabelType = null;
		for(Map<String,Object> map : labelList){
			if(id.equals((String)map.get("id"))){
				tempLabelType = (String)map.get("type");
				break;
			}
		}
		final String labelType = tempLabelType;
		String returnMsg = service.deleteLabel(request.getParameter("id"));
		//==============添加审计start===================
		JSONObject jo = JSONObject.parseObject(returnMsg);
		boolean result = jo.getBoolean("result");
		final User user = (User) request.getSession().getAttribute("user");
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				auditService.save(new AuditEntity(user.getName(), ResourceCode.LABELMNG,labelName, ResourceCode.Operation.DELETE, result?1:0, "删除标签:" + labelName+" 标签类型："+ labelType));
			}
		});
		//==============添加审计end=====================
		return returnMsg;
	}
	
	@RequestMapping(value = "/listLabels",method = RequestMethod.GET)
	public String listLabels(@RequestParam(name="pageSize",required = true,defaultValue="10") int pageSize, 
			@RequestParam(name="pageNum",required = true,defaultValue="1") int pageNum,
			@RequestParam(name="labelName",required=false) String labelName,@RequestParam(name="labelCode",required=false) Integer labelCode,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName) {
		return service.listLabels(pageSize, pageNum, labelName,labelCode,sortName,sortOrder);
	}
	
	@RequestMapping(value = "/updateLabel",method = RequestMethod.PUT)
	public String updateLabel(@RequestBody JSONObject params,@Context HttpServletRequest request) {
		String typeJson = service.listAllLabels(new JSONObject());
		List<Map<String,Object>> labelList = JSON.parseObject(typeJson, List.class);
		String id = params.getString("id");
		String tempLabelName = "";
		String tempDesc = "";
		for(Map<String,Object> map : labelList){
			if(id.equals((String)map.get("id"))){
				tempLabelName = ""+map.get("name");
				if(map.get("description")!=null){
					tempDesc = (String)map.get("description");
				}
				break;
			}
		}
		final String formerLabelName = tempLabelName;
		final String formerDesc = tempDesc;
		
		String returnMsg = service.updateLabel(params);
		//==============添加审计start===================
		String labelName = params.getString("name");
		String desc = params.getString("description");
		JSONObject jo = JSONObject.parseObject(returnMsg);
		boolean result = jo.getBoolean("result");
		final User user = (User) request.getSession().getAttribute("user");
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				auditService.save(new AuditEntity(user.getName(), ResourceCode.LABELMNG,labelName, ResourceCode.Operation.UPDATE, result?1:0, "更新前:name="+formerLabelName+ " 更新后标签:name=" + labelName+"更新前描述:description="+formerDesc+" 更新后描述:description="+desc));
			}
		});
		//==============添加审计end=====================
		return returnMsg;
	}

	
	@RequestMapping(value = "/checkLabel",method = RequestMethod.GET)
	public String checkLabel(@RequestParam("labelName") String labelName, 
			@RequestParam("code") int code, @RequestParam(name="id",required=false) String id) {
		return service.checkLabel(labelName, id,code);
	}
	
	@RequestMapping(value = "/updateResourceLabel",method = RequestMethod.PUT)
	public String updateResourceLabel(@RequestBody JSONObject params,@Context HttpServletRequest request) {
		JSONObject returnMsg = new JSONObject();
		try {
			//获取修改前的标签
			String resourceId = params.getString("resourceId");
			String labelIds = params.getString("labelIds");
			String oldLabels = service.getLabelsByResourceId(params);
			//修改后的标签
			JSONObject condition = new JSONObject();
			condition.put("resourceId", resourceId);
			condition.put("labelIds", labelIds.split(";"));
			String newLabels = service.getLabelsByLabelId(condition);
			//修改标签
			returnMsg = JSONObject.parseObject(service.updateResourceLabel(params));
			
			//==============添加审计start===================
			Map resource = service.getResourceInfo(params);
			String resourceName=(String)resource.get("name");
			String resourceCode = (String)resource.get("ResourceCode");
			boolean result = returnMsg.getBoolean("result");
			final User user = (User) request.getSession().getAttribute("user");
			
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(user.getName(), resourceCode,resourceName, ResourceCode.Operation.UPDATE, result?1:0, "更新前:labels="+oldLabels+ " 更新后标签:labels="+newLabels));
				}
			});
			//==============添加审计end=====================
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			returnMsg.put("result", true);
			returnMsg.put("message", "更新标签成功！");
		}
		return returnMsg.toJSONString();
	}
	
	@RequestMapping(value="getLabels4Group",method = RequestMethod.GET)
	@ResponseBody
	public String getLabels4Group(@RequestParam(name="labelCode",required=false) Integer labelCode){
		return JSONObject.toJSONString(service.getLabels4Group(labelCode));
	}
	@RequestMapping(value="getLabels4Plugin",method = RequestMethod.POST)
	@ResponseBody
	public String getLabels4Plugin(@RequestParam("rows") String rows){
		return service.getLabels4Plugin(rows);
	}
	
	@RequestMapping(value="saveResourceLabelMap",method = RequestMethod.POST)
	@ResponseBody
	public String saveResourceLabelMap(@RequestParam("resourceId") String resourceId
			,@RequestParam("labels") String labels){
		JSONObject result = new JSONObject();
		Boolean save =  service.saveResourceLabelMap(resourceId, labels);
		if(save){
			result.put("message", "保存资源和标签的关联关系成功！");
		}else{
			result.put("message", "保存资源和标签的关联关系失败！");
		}
		result.put("result", save);
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value="getLabelReources",method = RequestMethod.GET)
	@ResponseBody
	public String getLabelReources(@RequestParam(name="labelId",required=false) String labelId,
			@RequestParam(name="resourceId",required=false) String resourceId,@RequestParam(name="code",required=false) Integer code){
		JSONObject params = new JSONObject();
		params.put("labelId", labelId);
		params.put("resourceId", resourceId);
		params.put("code", code);
		return JSONObject.toJSONString(service.getLabelReources(params));
	}
}
