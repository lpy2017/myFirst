package com.dc.appengine.cloudui.ws.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dc.appengine.cloudui.entity.ClusterENV;
import com.dcits.Common.entity.User;
import com.dc.appengine.cloudui.master.service.IClusterService;
import com.dc.appengine.cloudui.master.service.impl.ClusterService;
import com.dc.appengine.cloudui.ws.client.WSRestClient;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

@Path("cluster")
public class ClusterRestService {
	
	private Logger log = LoggerFactory.getLogger(ClusterRestService.class);
		
	
	@Autowired
	@Qualifier("clusterService")
	IClusterService clusterService;
	
	@GET
	public String list(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		String pageNum = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("cluster").path("ui").queryParam("user_id", user.getId()).queryParam("pageNum", pageNum).queryParam("pageSize", pageSize).request().get(String.class);
		log.debug("clusters {}", result);
		return result;
	}
	
	@GET
	@Path("clusters")
	public String clusters(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("cluster").path("clusters").queryParam("user_id", user.getId()).request().get(String.class);
		log.debug("clusters {}", result);
		return result;
	}
	
//	@GET
//	@Path("network_kind")
//	public String networkKind(@Context HttpServletRequest request, @Context HttpServletResponse response) {
//		WebTarget target = WSRestClient.getAdapterWebTarget();
//		String result = target.path("cluster").path("network_kind").request().get(String.class);
//		return result;
//	}
	
	@POST
	public String insert(@Context HttpServletRequest request, @Context HttpServletResponse response, String body) {
		User user = (User) request.getSession().getAttribute("user");
//		String name = request.getParameter("name");
//		Form form = new Form();
//		form.param("name", name);
//		form.param("user_id", user.getId().toString());
//		form.param("network_type", request.getParameter("network_type"));
//		form.param("network", request.getParameter("network"));
//		form.param("network_kind", request.getParameter("network_kind"));
		WebTarget target = WSRestClient.getAdapterWebTarget();
		JSONObject jo = JSON.parseObject(body);
		jo.put("user_id", user.getId() + "");
//		String result = target.path("cluster").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		String result = target.path("cluster").request().post(Entity.entity(jo, MediaType.APPLICATION_JSON), String.class);
		return result;
	}
	
	@Path("{id}")
	@GET
	public String one(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("cluster").path(id).request().get(String.class);
		return result;
	}
	
	@Path("{id}")
	@DELETE
	public String delete(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("cluster").path(id).request().delete(String.class);
		return result;
	}
	
	@Path("{id}")
	@PUT
	public String update(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id, String body) {
//		String name = request.getParameter("name");
//		Form form = new Form();
//		form.param("name", name);
		WebTarget target = WSRestClient.getAdapterWebTarget();
//		String result = target.path("cluster").path(id).request().put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		String result = target.path("cluster").path(id).request().put(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
		return result;
	}
	
	@Path("{id}/nodes")
	@GET
	public String nodes(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id) {
		String cluster_id = id;
		String labels = request.getParameter("labels");
		String pageNum = request.getParameter("pageNum");
		String pageSize = request.getParameter("pageSize");
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("node").path("ui").queryParam("cluster_id", cluster_id).queryParam("labels", labels).queryParam("pageNum", pageNum).queryParam("pageSize", pageSize).request().get(String.class);
		log.debug("nodes {}", result);
		return result;
	}
	
	@Path("{id}/nodelist")
	@GET
	public String nodelist(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("node").path("nodelist").queryParam("cluster_id", id).request().get(String.class);
		log.debug("nodes {}", result);
		return result;
	}
	
	@Path("{id}/nodelistselected")
	@POST
	public String nodelistselected(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id, String body) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("node").path("nodelistselected").path(id).request().post(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
		log.debug("nodes {}", result);
		return result;
	}
	
	@Path("{id}/setlabel")
	@PUT
	public String setlabel(@Context HttpServletRequest request, @Context HttpServletResponse response, String body, @PathParam("id") String id) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("label").path(id).path("setlabel").request().put(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
		log.debug("setlabel {}", result);
		return result;
	}
	
	@Path("{cluster_id}/node/{node_id}")
	@GET
	public String nodeOne(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("cluster_id") String cluster_id, @PathParam("node_id") String node_id) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("node").path(node_id).request().get(String.class);
		return result;
	}
	
	@Path("deleteNode")
	@DELETE
	public String deleteNodes(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("node").path("multiDelete").queryParam("ids", request.getParameter("ids")).request().delete(String.class);
		return result;
	}
	
	@Path("{cluster_id}/node/{node_id}/updatelabel")
	@PUT
	public String updateNodeLabel(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("cluster_id") String cluster_id, @PathParam("node_id") String node_id, String body) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
//		Form form = new Form();
//		form.param("labels", request.getParameter("labels"));
//		String result = target.path("node").path(node_id).path("updateLabel").request().put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		String result = target.path("node").path(node_id).path("updateLabel").request().put(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
		return result;
	}
	
//	@Path("{cluster_id}/node/{node_id}/updatecluster")
//	@PUT
//	public String udpateNodeCluster(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("cluster_id") String cluster_id, @PathParam("node_id") String node_id, String body) {
//		WebTarget target = WSRestClient.getAdapterWebTarget();
////		Form form = new Form();
////		form.param("cluster_id", request.getParameter("cluster_id"));
////		String result = target.path("node").path(node_id).request().put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
//		String result = target.path("node").path(node_id).request().put(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
//		return result;
//	}
	
//	@Path("{cluster_id}/node/{node_id}/updatename")
//	@PUT
//	public String updateNodeName(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("cluster_id") String cluster_id, @PathParam("node_id") String node_id, String body) {
//		WebTarget target = WSRestClient.getAdapterWebTarget();
////		Form form = new Form();
////		form.param("name", request.getParameter("name"));
////		String result = target.path("node").path(node_id).request().put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
//		String result = target.path("node").path(node_id).request().put(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
//		return result;
//	}
	
//	@Path("{cluster_id}/node/{node_id}/updateresource")
//	@PUT
//	public String updateNodeResource(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("cluster_id") String cluster_id, @PathParam("node_id") String node_id, String body) {
//		WebTarget target = WSRestClient.getAdapterWebTarget();
////		Form form = new Form();
////		form.param("cpu", request.getParameter("cpu"));
////		form.param("memory", request.getParameter("memory"));
////		form.param("disk", request.getParameter("disk"));
////		String result = target.path("node").path(node_id).request().put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
//		String result = target.path("node").path(node_id).request().put(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
//		return result;
//	}
	
//	@Path("{id}/otherclusters")
//	@GET
//	public String otherCluster(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id) {
//		User user = (User) request.getSession().getAttribute("user");
//		String cluster_id = id;
//		WebTarget target = WSRestClient.getAdapterWebTarget();
//		String result = target.path("cluster").path("otherCluster").queryParam("user_id", user.getId().toString()).queryParam("cluster_id", cluster_id).request().get(String.class);
//		return result;
//	}
	
	@Path("checkName")
	@GET
	public String checkName(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		String name = request.getParameter("name");
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("cluster").path("checkName").queryParam("user_id", user.getId().toString()).queryParam("name", name).request().get(String.class);
		return result;
	}
	
	@Path("checkNameByID")
	@GET
	public String checkNameByID(@Context HttpServletRequest request, @Context HttpServletResponse response) {
		User user = (User) request.getSession().getAttribute("user");
		String name = request.getParameter("name");
		String id = request.getParameter("id");
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("cluster").path("checkName").queryParam("id", id).queryParam("name", name).queryParam("user_id", user.getId().toString()).request().get(String.class);
		return result;
	}
	
	@Path("{cluster_id}/checkName")
	@GET
	public String checkNodeName(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("cluster_id") String cluster_id) {
		String name = request.getParameter("name");
		WebTarget target = WSRestClient.getAdapterWebTarget();
		String result = target.path("node").path("checkName").queryParam("cluster_id", cluster_id).queryParam("name", name).request().get(String.class);
		return result;
	}
	
//	@Path("{cluster_id}/checkNameByID")
//	@GET
//	public String checkNodeNameByID(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("cluster_id") String cluster_id) {
//		String name = request.getParameter("name");
//		String id = request.getParameter("id");
//		WebTarget target = WSRestClient.getAdapterWebTarget();
//		String result = target.path("node").path("checkName").queryParam("cluster_id", cluster_id).queryParam("name", name).queryParam("id", id).request().get(String.class);
//		return result;
//	}
	
//	@Path("{cluster_id}/addNode")
//	@POST
//	public String addNode(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("cluster_id") String cluster_id, String body) {
////		String name = request.getParameter("name");
////		String ip = request.getParameter("ip");
////		String labels = request.getParameter("labels");
////		Form form = new Form();
////		form.param("name", name);
////		form.param("ip", ip);
////		form.param("labels", labels);
////		form.param("cluster_id", cluster_id);
//		WebTarget target = WSRestClient.getAdapterWebTarget();
////		String result = target.path("node").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
//		String result = target.path("node").request().post(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
//		return result;
//	}
	
//	@Path("{cluster_id}/nodestatus")
//	@GET
//	public String nodestatus(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("cluster_id") String cluster_id) {
//		WebTarget target = WSRestClient.getAdapterWebTarget();
//		String result = target.path("node").path("status").queryParam("ip", request.getParameter("ip")).request().get(String.class);
//		return result;
//	}
	
	@Path("{cluster_id}/nodeurl")
	@GET
	public String nodeurl(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("cluster_id") String cluster_id) {
		String os_version = request.getParameter("os_version");
		String result = ClusterENV.NODE_INSTALL_SHELL;
		result = result.replaceAll("\\{os_version\\}", os_version);
//		WebTarget target = WSRestClient.getAdapterWebTarget();
//		String cluster_info = target.path("cluster").path(cluster_id).request().get(String.class);
//		Map<String, Object> map = JSON.parseObject(cluster_info, new TypeReference<Map<String, Object>>(){});
//		String network = (String) map.get("network_kind");
//		if ("OVS".equals(network)) {
//			result = result.replaceAll("node_install", "nodeovs_install");
//		}
//		if ("Calico".equals(network)) {
//			result = result.replaceAll("node_install", "nodecalico_install");
//		}
		return result;
	}
	
	@Path("{id}/users")
	@GET
	public String users(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id) {
		String page_num = request.getParameter("pageNum");
		String page_size = request.getParameter("pageSize");
		WebTarget target = WSRestClient.getAdapterWebTarget();
		return target.path("cluster").path(id).path("users").queryParam("pageNum", page_num).queryParam("pageSize", page_size).request().get(String.class);
	}
	
	@Path("{id}/checkuser")
	@POST
	public String checkUser(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id, String body) {
//		String user_name = request.getParameter("USERID");
		WebTarget target = WSRestClient.getAdapterWebTarget();
//		Form form = new Form();
//		form.param("USERID", user_name);
//		return target.path("cluster").path(id).path("checkuser").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		return target.path("cluster").path(id).path("checkuser").request().post(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
	}
	
	@Path("{id}/adduser")
	@POST
	public String addUser(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id, String body) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
//		Form form = new Form();
//		form.param("USERID", request.getParameter("USERID"));
//		form.param("USERPASSWORD", request.getParameter("USERPASSWORD"));
//		form.param("cpu", request.getParameter("cpu"));
//		form.param("memory", request.getParameter("memory"));
//		form.param("disk", request.getParameter("disk"));
//		return target.path("cluster").path(id).path("adduser").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		return target.path("cluster").path(id).path("adduser").request().post(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
	}
	
//	@Path("{id}/updateuser")
//	@PUT
//	public String updateUser(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id, String body) {
//		WebTarget target = WSRestClient.getAdapterWebTarget();
////		Form form = new Form();
////		form.param("ID", request.getParameter("ID"));
////		form.param("cpu", request.getParameter("cpu"));
////		form.param("memory", request.getParameter("memory"));
////		form.param("disk", request.getParameter("disk"));
////		return target.path("cluster").path(id).path("updateuser").request().put(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
//		return target.path("cluster").path(id).path("updateuser").request().put(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
//	}
	
	@Path("{id}/userlist")
	@GET
	public String userList(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
		return target.path("cluster").path(id).path("userlist").request().get(String.class);
	}
	
	@Path("{id}/selectuser")
	@POST
	public String selectUser(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id, String body) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
//		Form form = new Form();
//		form.param("user_ids", request.getParameter("user_ids"));
//		return target.path("cluster").path(id).path("selectuser").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), String.class);
		return target.path("cluster").path(id).path("selectuser").request().post(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
	}
	
	@Path("{id}/deleteuser")
	@DELETE
	public String deleteUser(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
		return target.path("cluster").path(id).path("deleteuser").queryParam("user_ids", request.getParameter("user_ids")).request().delete(String.class);
	}
	
	public class MyUserInfo implements UserInfo {

		private String password;

		public MyUserInfo(String password) {
			this.password = password;
		}

		public String getPassphrase() {
			return null;
		}

		public String getPassword() {
			return password;
		}

		public boolean promptPassword(String message) {
			return true;
		}

		public boolean promptPassphrase(String message) {
			return true;
		}

		public boolean promptYesNo(String message) {
			return true;
		}

		public void showMessage(String message) {
		}

	}
	
	public class Executor implements Runnable {
		
		private String ip;
		private int port;
		private String userName;
		private String password;
		private String cmd;
		
		public Executor(String ip, int port, String userName, String password, String cmd) {
			this.ip = ip;
			this.port = port;
			this.userName = userName;
			this.password = password;
			this.cmd = cmd;
		}
		
		private void setSudoer() {
			try {
			    JSch jsch = new JSch();
			    Session session = jsch.getSession(userName, ip, port);
			    UserInfo ui = new MyUserInfo(password);
				session.setUserInfo(ui);
				session.connect();
//			    session.setConfig("StrictHostKeyChecking", "no");
//			    session.connect(60 * 1000);
			    Channel channel = session.openChannel("shell");
			    Expect expect = new Expect(channel.getInputStream(),
			            channel.getOutputStream());
			    channel.connect();
			    if ("root".equals(userName)) {
			    	expect.expect("#");
			    	log.info(expect.before + expect.match);
				    expect.send("sed -i 's/Defaults    requiretty/#Defaults    requiretty/g' /etc/sudoers\n");
				    expect.expect("#");
			    	log.info(expect.before + expect.match);
			    	expect.send("exit\n");
			    	expect.expectEOF();
			    	log.info(expect.before);
				} else {
					expect.expect("$");
					log.info(expect.before + expect.match);
					expect.send("sudo su -\n");
				    expect.expect(":");
				    log.info(expect.before + expect.match);
				    expect.send(password + "\n");
				    expect.expect("#");
				    log.info(expect.before + expect.match);
				    expect.send("sed -i 's/Defaults    requiretty/#Defaults    requiretty/g' /etc/sudoers\n");
				    expect.expect("#");
			    	log.info(expect.before + expect.match);
			    	expect.send("exit\n");
			    	expect.expect("$");
			    	log.info(expect.before + expect.match);
			    	expect.send("exit\n");
			    	expect.expectEOF();
			    	log.info(expect.before);
				}
			    expect.close();
			    channel.disconnect();
			    session.disconnect();
			} catch (JSchException e) {
			    e.printStackTrace();
			} catch (IOException e) {
			    e.printStackTrace();
			}
		}

		@Override
		public void run() {
			try {
				this.setSudoer();
				JSch jsch = new JSch();
				Session session = jsch.getSession(userName, ip, port);
				UserInfo ui = new MyUserInfo(password);
				session.setUserInfo(ui);
				session.connect();
				Channel channel = session.openChannel("exec");
				((ChannelExec) channel).setCommand(cmd);
				InputStream in = channel.getInputStream();
				channel.connect();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int i;
				while ((i = in.read()) != -1) {
					baos.write(i);
				}
				byte[] bs = baos.toByteArray();
				log.debug("{} ###result: {}", cmd, new String(bs));
				if (channel.isClosed()) {
//					System.out.println("exit-status: " + channel.getExitStatus());
					log.debug("exit-status: {}", channel.getExitStatus());
				}
				channel.disconnect();
				session.disconnect();
			} catch (JSchException e) {
				log.error("", e);
			} catch (IOException e) {
				log.error("", e);
			}
		}
		
	}
	
	@Path("{id}/nodeexec")
	@POST
	public String nodeExec(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("id") String id, String body) {
		JSONObject jsonobj = JSON.parseObject(body);
		String ip = (String) jsonobj.get("ip");
		String port = (String) jsonobj.get("port");
		String userName = (String) jsonobj.get("username");
		String password = (String) jsonobj.get("password");
		String cmd = (String) jsonobj.get("cmd");
//		String ip = request.getParameter("ip");
//		String port = request.getParameter("port");
//		String userName = request.getParameter("username");
//		String password = request.getParameter("password");
//		String cmd = request.getParameter("cmd");
		boolean flag = true;
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			JSch jsch = new JSch();
		    Session session = jsch.getSession(userName, ip, Integer.valueOf(port));
		    UserInfo ui = new MyUserInfo(password);
			session.setUserInfo(ui);
			session.connect();
			session.disconnect();
			Thread thread = new Thread(new Executor(ip, Integer.valueOf(port), userName, password, cmd));
			thread.start();
		} catch (Exception e) {
			log.error("", e);
			flag = false;
			result.put("info", "命令发送失败，" + e.getMessage());
		}
		if (flag) {
			result.put("result", true);
			result.put("info", "命令已发送");
//			clusterService.addRoster(ip,userName,password);
			return JSON.toJSONString(result);
		} else {
			result.put("result", false);
//			result.put("info", "命令发送失败");
			return JSON.toJSONString(result);
		}
	}
	
	@Path("{cluster_id}/{node_id}/getLabel")
	@GET
	public String getLabel(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("node_id") String id) {
		WebTarget target = WSRestClient.getAdapterWebTarget();
		return target.path("node").path(id).path("getLabel").request().get(String.class);
	}
	
	@Path("addClusterAndNodes")
	@POST
	public String addClusterAndNodes(@Context HttpServletRequest request, @Context HttpServletResponse response, String body) {
		User user = (User) request.getSession().getAttribute("user");
		Map<String, Object> info = JSON.parseObject(body);
		Map<String, Object> cluster = (Map<String, Object>) info.get("cluster");
		cluster.put("user_id", user.getId());
		info.put("cluster", cluster);
		body = JSON.toJSONString(info);
		WebTarget target = WSRestClient.getAdapterWebTarget();
		return target.path("cluster").path("addClusterAndNodes").request().post(Entity.entity(body, MediaType.APPLICATION_JSON), String.class);
	}

}
