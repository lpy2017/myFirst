package com.dc.appengine.appmaster.ws.client.service;

import java.util.List;
import java.util.Map;


public interface IAdapterService {
	public List<Map<String,Object>> getUserCluster(String userId);
	
	public List<Map<String,Object>> getNodesFromCluster(String clusterId);
	
	public String getAdminClusters();

}
