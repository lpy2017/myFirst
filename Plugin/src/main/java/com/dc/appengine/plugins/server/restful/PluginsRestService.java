package com.dc.appengine.plugins.server.restful;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.plugins.manager.service.impl.PluginMService;
import com.dc.appengine.plugins.service.impl.CMD;
import com.dc.appengine.plugins.service.impl.DownloadArtifact;
import com.dc.appengine.plugins.service.impl.Salt;
import com.dc.appengine.plugins.service.impl.SaltSSH;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.MessageHelper;


@Controller
@RequestMapping("/plugin")
public class PluginsRestService {
	private static final Logger log = LoggerFactory
			.getLogger(PluginsRestService.class);
	PluginMService pluginService = new PluginMService();
	/*
	 * 上传插件包，注册插件，动态加载插�?
	 * @RequestParam("pluginType") 插件类型
	 * @RequestParam("label") 插件标签
	 * @RequestParam("description") 插件描述
	 * @RequestParam("file") 插件文件�?
	 */
	@RequestMapping(value = "registPlugin",method = RequestMethod.POST)
	public void registPlugin(HttpServletRequest request,HttpServletResponse res){
		String result=null;
		try {
			start:{
			request.setCharacterEncoding("UTF-8");
			CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
	                request.getSession().getServletContext());
			MultipartFile file =null;
			MultipartHttpServletRequest multipartRequest =null;
			if(multipartResolver.isMultipart(request)){
				multipartRequest = multipartResolver
		                .resolveMultipart(request);
				//取得request中的所有文件名  
				Iterator<String> iter = multipartRequest.getFileNames();
		        while (iter.hasNext()) {
		            file = multipartRequest.getFile((String) iter.next());
		        }
			}
			String param=multipartRequest.getParameter("param");
			String fileName = file.getOriginalFilename();
			if(fileName==null ||"".equals(fileName)){
				result =MessageHelper.wrap("result",false,"message","上传文件为空！");
			}else{
				InputStream fileInput= file.getInputStream();
				Map<String, Object> paramMap= JSON.parseObject(param);
				Map<String, Object> params= new HashMap<>();
				if(JudgeUtil.isEmpty(paramMap.get("preAction"))){
					result = MessageHelper.wrap("result",false,"message","插件前处理方法为null！");
					break start;
				}
				if(JudgeUtil.isEmpty(paramMap.get("invoke"))){
					result = MessageHelper.wrap("result",false,"message","插件行为方法为null！");
					break start;
				}
				if(JudgeUtil.isEmpty(paramMap.get("agent"))){
					result = MessageHelper.wrap("result",false,"message","插件Agent方法为null！");
					break start;
				}
				if(JudgeUtil.isEmpty(paramMap.get("postAction"))){
					result = MessageHelper.wrap("result",false,"message","插件后处理方法为null！");
					break start;
				}
				params.put("fileName", fileName);
				params.put("fileInput", fileInput);
				params.putAll(paramMap);
				result=pluginService.registPlugin(params);
			}
		}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("插件注册失败", e);
			result=MessageHelper.wrap("result",false,"message","插件注册失败!");
		}
		 success(res, result);
	}
	
	/*
	 * 此方法也可以用，不过需要配置MultipartFile
	 */

//	@RequestMapping(value = "registPlugin",method = RequestMethod.POST)
//	public void registPlugin(@RequestParam("pluginName") String pluginName,@RequestParam(value="label",required=false,defaultValue="{}") String label,
//			@RequestParam(value="params",required=false,defaultValue="{}") String params,
//				@RequestParam(value="description",required=false,defaultValue="") String description,@RequestParam("file") MultipartFile file,
//			@RequestParam(value="preAction") String preAction,@RequestParam(value="postAction") String postAction,
//				@RequestParam(value="invoke") String invoke,
//				@RequestParam(value="agent") String agent,HttpServletResponse res){
//		String result = null;
//		try {
//			start: {
//				if (JudgeUtil.isEmpty(preAction)) {
//					result = MessageHelper.wrap("result", false, "message", "插件前处理方法为null！");
//					break start;
//				}
//				if (JudgeUtil.isEmpty(invoke)) {
//					result = MessageHelper.wrap("result", false, "message", "插件行为方法为null！");
//					break start;
//				}
//				if (JudgeUtil.isEmpty(agent)) {
//					result = MessageHelper.wrap("result", false, "message", "插件Agent方法为null！");
//					break start;
//				}
//				if (JudgeUtil.isEmpty(postAction)) {
//					result = MessageHelper.wrap("result", false, "message", "插件后处理方法为null！");
//					break start;
//				}
//				Map<String, Object> param = new HashMap<String, Object>() {};
//				Map<String, Object> labelMap = JSON.parseObject(label, new TypeReference<Map<String, Object>>() {});
//				Map<String, Object> paramsMap = JSON.parseObject(params, new TypeReference<Map<String, Object>>() {});
//				param.put("pluginName", pluginName);
//				param.put("label", labelMap);
//				param.put("params", paramsMap);
//				param.put("description", description);
//				param.put("postAction", postAction);
//				param.put("preAction", preAction);
//				param.put("invoke", invoke);
//				param.put("agent", agent);
//				param.put("fileName", file.getOriginalFilename());
//				param.put("fileInput", file.getInputStream());
//				result = pluginService.registPlugin(param);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error("插件注册失败", e);
//			result = MessageHelper.wrap("result", false, "message", "插件注册失败!");
//		}
//		success(res, result);
//	}
	
	@RequestMapping(value = "listPlugins",method = RequestMethod.GET)
	public void listPlugins(@RequestParam("pageSize") int pageSize, 
			@RequestParam("pageNum") int pageNum,
			@RequestParam(value="pluginName",required = false) String pluginName,
			@RequestParam(value="pluginNames",required = false) String pluginNames,
			@RequestParam(value="hasLabel",required = false) String hasLabel,
			@RequestParam(value="sortName",required = false) String sortName,
			@RequestParam(value="sortOrder",required = false) String sortOrder,
			HttpServletResponse res){
		String result=null;
		try {
			JSONObject param = new JSONObject();
			param.put("pluginName", pluginName);
			param.put("pluginNames", pluginNames);
			param.put("hasLabel", hasLabel);
			param.put("pageNum", pageNum);
			param.put("pageSize", pageSize);
			param.put("sortName", sortName);
			param.put("sortOrder", sortOrder);
			result= JSON.toJSONString(pluginService.listPugins(param));
		} catch (Exception e) {
			// TODO: handle exception
			log.error("插件列表获取失败!", e.getMessage());
			result= MessageHelper.wrap("result",false,"message","插件列表获取失败!");
		}
		success(res, result);
	}
	
	@RequestMapping(value = "getPlugin",method = RequestMethod.GET)
	public void getPlugin(@RequestParam("pluginName") String pluginName,HttpServletResponse res){
		String result=null;
		try {
			result=JSON.toJSONString(pluginService.getPlugin(pluginName));
		} catch (Exception e) {
			// TODO: handle exception
			log.error("插件获取失败!", e.getMessage());
			result=MessageHelper.wrap("result",false,"message","插件获取失败!");
		}
		success(res, result);
	}
	
	
	@RequestMapping(value = "updatePlugin",method = RequestMethod.POST)
	public void updatePlugin(@RequestParam("pluginName") String pluginName,@RequestParam(value="label",required=false,defaultValue="") String label,
			@RequestParam(value="description",required=false,defaultValue="") String description,
			@RequestParam(value="preAction",required=false,defaultValue="") String preAction,@RequestParam(value="postAction",required=false,defaultValue="") String postAction,
			@RequestParam(value="params",required=false,defaultValue="") String params,
			@RequestParam(value="invoke",required=true) String invoke,
			@RequestParam(value="agent",required=true) String agent,HttpServletResponse res){
		String result=null;
		try {
			start: {
				Map<String, Object> param = new HashMap<String, Object>() {
				};
				Map<String, Object> labelMap = JSON.parseObject(label, new TypeReference<Map<String, Object>>() {
				});
				Map<String, Object> paramsMap = JSON.parseObject(params, new TypeReference<Map<String, Object>>() {
				});
				if (JudgeUtil.isEmpty(preAction)) {
					result = MessageHelper.wrap("result", false, "message", "插件前处理方法为null！");
					break start;
				}
				if (JudgeUtil.isEmpty(invoke)) {
					result = MessageHelper.wrap("result", false, "message", "插件行为方法为null！");
					break start;
				}
				if (JudgeUtil.isEmpty(agent)) {
					result = MessageHelper.wrap("result", false, "message", "插件Agent方法为null！");
					break start;
				}
				if (JudgeUtil.isEmpty(postAction)) {
					result = MessageHelper.wrap("result", false, "message", "插件后处理方法为null！");
					break start;
				}
				param.put("pluginName", pluginName);
				param.put("label", labelMap);
				param.put("params", paramsMap);
				param.put("description", description);
				param.put("postAction", postAction);
				param.put("preAction", preAction);
				param.put("invoke", invoke);
				param.put("agent", agent);
				result = pluginService.updatePlugin(param);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("插件更新失败!", e);
			result= MessageHelper.wrap("result",false,"message","插件更新失败!");
		}
		success(res, result);
	}
	
	@RequestMapping(value = "deletePlugin/{pluginName}",method = RequestMethod.DELETE)
	public void deletePlugin(@PathVariable("pluginName") String pluginName,HttpServletResponse res){
		String result=null;
		try {
			Map<String, Object> param= new HashMap<String, Object>(){};
			param.put("pluginName", pluginName);
			result= pluginService.deletePlugin(param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("插件删除失败!", e);
			result= MessageHelper.wrap("result",false,"message",pluginName+"插件删除失败!");
		}
		success(res, result);
	}
	public void success(HttpServletResponse response, String result)  {
        response.setContentType("text/plain; charset=UTF-8");
        try{
            PrintWriter out =response.getWriter();
            out.write(result);
            out.close();
        }catch (Exception e){
        	e.printStackTrace();
        }
    }
	
	@RequestMapping(value = "activePlugin",method = RequestMethod.POST)
	public void activePlugin(@RequestParam("pluginName") String pluginName,
			@RequestParam("pluginParams") String pluginParams,
			@RequestParam("messageParams") String messageParams,
			HttpServletResponse res){
		String result=null;
		try {
			JSONObject param = new JSONObject();
			Map<String, Object> pluginParam = JSON.parseObject(pluginParams, new TypeReference<Map<String, Object>>() {
			});
			Map<String, Object> messageParam = JSON.parseObject(messageParams, new TypeReference<Map<String, Object>>() {
			});
			messageParam.put(pluginName, pluginParam);
			param.put("pluginName", pluginName);
			param.put("message", messageParam);
			if("CMD".equals(pluginName)){
				CMD cmd = new CMD();
				cmd.initPlugin(param.toJSONString(),null);
				result=cmd.doAgent();
			}else if("salt".equals(pluginName)||"Salt".equals(pluginName)){
				Salt salt = new Salt();
				salt.initPlugin(param.toJSONString(),null);
				result=salt.doAgent();
			}else if("saltSSH".equals(pluginName)||"SaltSSH".equals(pluginName)){
				SaltSSH saltSSh = new SaltSSH();
				saltSSh.initPlugin(param.toJSONString(),null);
				result=saltSSh.doAgent();
			}else if("downloadArtifact".equals(pluginName)||"DownloadArtifact".equals(pluginName)){
				DownloadArtifact downloadArtifact = new DownloadArtifact();
				downloadArtifact.initPlugin(param.toJSONString(),null);
				result=downloadArtifact.doAgent();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("插件执行失败!", e);
			result= MessageHelper.wrap("result",false,"message","插件执行失败!");
		}
		success(res, result);
	}
}