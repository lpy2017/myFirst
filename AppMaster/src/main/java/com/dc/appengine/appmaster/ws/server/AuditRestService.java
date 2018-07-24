package com.dc.appengine.appmaster.ws.server;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.IUserService;
import com.dc.appengine.appmaster.utils.ExcelUtil;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.SortUtil;
@RestController
@RequestMapping("/ws/audit")
public class AuditRestService {
	private static final Logger log = LoggerFactory.getLogger(AuditRestService.class);

	@Resource
	IAudit auditService;
	
	@Resource
	IUserService userService;
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public String save(@RequestParam("userId") String  userId,
					   @RequestParam("resourceType") String  resourceType,
					   @RequestParam("resourceName") String  resourceName,
					   @RequestParam("operateType") String  operateType,
					   @RequestParam("operateResult") int  operateResult,
					   @RequestParam("detail") String  detail) {
		try{
			auditService.save(new AuditEntity(userId,resourceType, resourceName, operateType, operateResult, detail));
			return MessageHelper.wrap("result",true,"message","success");
		}catch(Exception e){
			e.printStackTrace();
			log.error("保存蓝图模版失败", e);
			return MessageHelper.wrap("result",false,"message","failure");		

		}
	}

	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
	public String list(
			@RequestParam(name="pageSize",required=false,defaultValue="10") int pageSize, 
			@RequestParam(name="pageNum",required=false,defaultValue="1") int pageNum,
			@RequestParam(name="userId",required=false) String userId,
			@RequestParam(name="resourceType",required=false) String resourceType,
			@RequestParam(name="operateType",required=false) String operateType,
			@RequestParam(name="operateResult",required=false) String operateResult,
			@RequestParam(name="startDate",required=false) String startDate,
			@RequestParam(name="endDate",required=false) String endDate,
			@RequestParam(name="sortOrder",required=false) String sortOrder,
			@RequestParam(name="sortName",required=false) String sortName){
		
		Map<String,Object> paraMap = new HashMap();
		paraMap.put("userId", userId);
		paraMap.put("resourceType", resourceType);
		paraMap.put("operateType", operateType);
		paraMap.put("operateResult", operateResult);
		paraMap.put("startDate", startDate);
		paraMap.put("endDate", endDate);
		paraMap.put("sortName", SortUtil.getColunmName("audit", sortName));
		paraMap.put("sortOrder", sortOrder);
		Page page = auditService.listAudit(paraMap,pageNum,pageSize);
		return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
	}
	
	@RequestMapping(value="export",method = RequestMethod.GET)
	@ResponseBody
	public void export(@Context HttpServletResponse resp,@RequestParam("ids") String ids){

		String[] idArray = ids.split(","); 
		List<String> idList = Arrays.asList(idArray);
		
		List<AuditEntity> auditList = auditService.exportAudit(idList);
		
		resp.setHeader("content-type", "application/octet-stream");
		resp.setContentType("application/octet-stream");
		try {
			resp.setHeader("Content-Disposition","attachment; filename=" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date())+".xls");
			ExcelUtil.getExeclData(auditList).write(resp.getOutputStream());
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("客户端取消导出！"+ e1.getMessage());
		}
	}
	
	@RequestMapping(value="getAllUsers",method = RequestMethod.GET)
	@ResponseBody
	public String getAllUsers(){
		return JSON.toJSONString(userService.listUsers());
	}
	
	@RequestMapping(value="getAllResourceType",method = RequestMethod.GET)
	@ResponseBody
	public String getAllResourceType(){
		return JSON.toJSONString(ResourceCode.MENUACTION);
	}
	
	@RequestMapping(value = "listAll", method = RequestMethod.GET)
	public Object listAll(@RequestParam(value = "total", defaultValue = "20") String total) {
		return auditService.listAll(Integer.valueOf(total));
	}
}