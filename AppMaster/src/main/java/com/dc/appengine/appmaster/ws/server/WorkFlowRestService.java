package com.dc.appengine.appmaster.ws.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dc.appengine.appmaster.utils.MessageHelper;

@Controller
@RequestMapping("/ws/workFlow")
public class WorkFlowRestService {
	private static final Logger log = LoggerFactory
			.getLogger(WorkFlowRestService.class);
	/*
	 * 插件后处理回调master方法
	 */
	@RequestMapping(value = "postAction",method = RequestMethod.POST)
	@ResponseBody
	public String postAction(@RequestParam("postAction") String postAction){
		return MessageHelper.wrap("result",true,"message","postAction回调成功！");
	}
	
	/*
	 * 触发流程
	 */
	@RequestMapping(value = "instantiateWorkflow",method = RequestMethod.GET)
	@ResponseBody
	public String instantiateWorkflow(@RequestParam("appId") String appId){
		return MessageHelper.wrap("result",true,"message","postAction回调成功！");
	}
}
