package com.dc.appengine.cloudui.ws.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.client.WebTarget;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.cloudui.entity.MasterEnv;
import com.dc.appengine.cloudui.utils.BluePrint2Toolbar;
import com.dc.appengine.cloudui.ws.client.WSRestClient;

@Path("blueprintconvert")
public class BluePrintInfoConvertService {
	private static final Logger log =LoggerFactory.getLogger(BluePrintInfoConvertService.class);
	@GET
	@Path("getnewinfo")
	public String getNewInfo(@QueryParam("blueInstanceId") int blueInstanceId){
		WebTarget target = WSRestClient.getMasterWebTarget();
		String rsStr = target.path("deployedApp").path("getBlueInstanceDetailInfo")
				.queryParam("blueInstanceId", blueInstanceId)
				.request().get(String.class); 
		JSONObject result = JSONObject.parseObject(rsStr);
		rsStr=result.getString("INFO");
		BluePrint2Toolbar tool=new BluePrint2Toolbar();
		if(log.isDebugEnabled()){
			log.debug("转换后的结果是："+rsStr);
		}
		return tool.convert(rsStr);
	}
	@GET
	@Path("getnewinfo_temp")
	public String getNewInfo_temp(@QueryParam("bpName") String bpName, @QueryParam("resourceName") String resourceName){
		RestTemplate restUtil = new RestTemplate();
		String rsStr = restUtil.getForObject(MasterEnv.MASTER_REST+
				"/blueprint/getBlueprintTemplate?bpName="+bpName, String.class);
		JSONObject result = JSONObject.parseObject(rsStr);
		rsStr=result.getString("INFO");
		BluePrint2Toolbar tool=new BluePrint2Toolbar();
		if(log.isDebugEnabled()){
			log.debug("转换后的结果是："+rsStr);
		}
		return filter(tool.convert(rsStr), resourceName);
	}
	
	private String filter(String data, String resourceName) {
		JSONObject jobj = JSON.parseObject(data);
		List<Map<String, Object>> nodes = (List<Map<String,Object>>) jobj.get("nodeDataArray");
		Map<String, Map<String, Object>> dict = new HashMap<>();
		for (Map<String, Object> node : nodes) {
			dict.put(node.get("key").toString(), node);
		}
		List<Map<String, Object>> filteredNodes = new ArrayList<>();
		Pattern pattern = Pattern.compile("(?i)" + resourceName);
		for (Map<String, Object> node : nodes) {
			if ("component".equals(node.get("eleType"))) {
				Matcher matcher = pattern.matcher(node.get("nodeName").toString());
				if (matcher.find()) {
					filteredNodes.add(node);
					filteredNodes.add(dict.get(node.get("group").toString()));
				}
			}
		}
		jobj.put("nodeDataArray", filteredNodes);
		return JSON.toJSONString(jobj);
	}
}
