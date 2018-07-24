package com.dc.appengine.appmaster.ws.server;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dc.appengine.appmaster.service.ITemplateService;

@Controller
@RequestMapping("/ws/template")
public class TemplateRestService {
	private static final Logger log = LoggerFactory
			.getLogger(TemplateRestService.class);
	
	@Resource
	ITemplateService templateService;
}
