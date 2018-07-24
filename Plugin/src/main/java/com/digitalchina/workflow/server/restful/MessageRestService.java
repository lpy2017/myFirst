package com.digitalchina.workflow.server.restful;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.servicejet.ASClient4CD;
import com.dc.workflow.utils.MessageHelper;
import com.dc.workflow.utils.MessageReceiver;

@Controller
@RequestMapping("cd")
public class MessageRestService {
	
	@RequestMapping(value = "message", method = RequestMethod.POST, consumes = {"text/plain", "application/*"})
	@ResponseBody
	public String receive(HttpServletRequest request, HttpServletResponse response, @RequestParam("message") String message) {
		Map<String, List<Object>> map = JSON.parseObject(message, new TypeReference<Map<String, List<Object>>>(){});
		boolean flag = MessageReceiver.put((Map<String, List<Object>>)map);
		return flag + "";
	}
	
	@RequestMapping(value = "listmessage", method = RequestMethod.GET)
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response) {
		return JSON.toJSONString(MessageHelper.print());
	}
	
	@RequestMapping(value = "deletemessage", method = RequestMethod.DELETE)
	@ResponseBody
	public String delete(HttpServletRequest request, HttpServletResponse response) {
		boolean flag = true;
		try {
			MessageHelper.clear(request.getParameter("id_pre"));
		} catch (Exception e) {
			flag = false;
		}
		return flag + "";
	}
	
	@RequestMapping(value = "nodes", method = RequestMethod.GET)
	@ResponseBody
	public String nodes() {
		return JSON.toJSONString(ASClient4CD.getInstance().getActiveAgents());
	}

}
