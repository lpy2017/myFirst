package com.dc.appengine.cloudui.ws.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dcits.Common.entity.User;
import com.dc.appengine.cloudui.ws.client.WSRestClient;

@Path("label")
public class LabelRestService {
	
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@GET
	public String list(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		String pageNum = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("label").queryParam("user_id", user.getId()).queryParam("pageNum", pageNum).queryParam("pageSize", pageSize).request().get(String.class);
		log.info("labels:{}", result);
		return result;
	}
	
	@PUT
	public String delete(@Context HttpServletRequest request, @Context HttpServletResponse response, String body) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("label").request().put(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
		log.info("label delete result: {}", result);
		return result;
	}
	
	@GET
	@Path("nodes")
	public String nodes(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		String pageNum = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		String type = request.getParameter("type");
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("label").path("nodes").queryParam("user_id", user.getId()).queryParam("pageNum", pageNum).queryParam("pageSize", pageSize).queryParam("key", key).queryParam("value", value).queryParam("type", type).request().get(String.class);
		return result;
	}
	
	@GET
	@Path("{cluster_id}/nodes")
	public String clusterNodes(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("cluster_id") String cluster_id) {
		String pageNum = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		String type = request.getParameter("type");
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("label").path(cluster_id).path("nodes").queryParam("pageNum", pageNum).queryParam("pageSize", pageSize).queryParam("key", key).queryParam("value", value).queryParam("type", type).request().get(String.class);
		return result;
	}
	
	@POST
	@Path("multiAdd")
	public String multiAdd(@Context HttpServletRequest request, @Context HttpServletResponse response, String body) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("label").path("multiAdd").request().post(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
		return result;
	}
	
	@DELETE
	@Path("multiDelete")
	public String multiDelete(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		String type = request.getParameter("type");
		String nodes = request.getParameter("nodes");
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("label").path("multiDelete").queryParam("key", key).queryParam("value", value).queryParam("type", type).queryParam("nodes", nodes).request().delete(String.class);
		return result;
	}

}
