package com.dc.appengine.appmaster.ws.client.service.impl;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.ws.client.AdapterWSRestClient;
import com.dc.appengine.appmaster.ws.client.service.IAdapterService;

@Service("adapterService")
public class AdapterService implements IAdapterService {

	private static String RESULT = "result";
	
	private static final Logger log = LoggerFactory.getLogger( AdapterService.class );

	@Override
	public List<Map<String, Object>> getUserCluster(String userId) {
		// TODO Auto-generated method stub
		String jsonStr=null;
		try {
			jsonStr = AdapterWSRestClient.getWebResource().path("cluster").path("master")
					.queryParam("user_id", URLEncoder.encode(userId, "UTF-8")).request().get(String.class);
//			if (log.isDebugEnabled()) {
//				log.debug(" receive adapter  message  : "+jsonStr);
//			}
			if (jsonStr != null) {
				return JSON.parseObject(jsonStr, new TypeReference<List<Map<String, Object>>>(){});
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("获取用户集群异常"+e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getNodesFromCluster(String clusterId) {
		// TODO Auto-generated method stub
		String jsonStr =null;
		try {
			jsonStr = AdapterWSRestClient.getWebResource().path("node").path("master")
					.queryParam("cluster_id", URLEncoder.encode(clusterId, "UTF-8"))
					.request().get(String.class);
			if (log.isDebugEnabled()) {
				log.debug(" receive adapter  message  : "+jsonStr);
			}
			if (jsonStr != null) {
				return JSON.parseObject(jsonStr, new TypeReference<List<Map<String, Object>>>(){});
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String getAdminClusters() {
		String jsonStr = null;
		try {
			jsonStr = AdapterWSRestClient.getWebResource().path("cluster").path("clusters")
					.queryParam("user_id", URLEncoder.encode("1", "UTF-8"))
					.request().get(String.class);
			log.debug(" receive adapter message:"+jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonStr;
	}
}
