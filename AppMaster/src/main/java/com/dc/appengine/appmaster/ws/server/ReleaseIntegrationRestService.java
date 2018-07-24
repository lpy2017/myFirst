package com.dc.appengine.appmaster.ws.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ReleaseIntegration;
import com.dc.appengine.appmaster.service.IReleaseIntegrationService;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("/ws/releaseIntegration")
public class ReleaseIntegrationRestService {

	private static final Logger log = LoggerFactory.getLogger(ReleaseIntegrationRestService.class);

	@Autowired
	@Qualifier("releaseIntegrationService")
	private IReleaseIntegrationService releaseIntegrationService;

	@RequestMapping(value = "listReleaseIntegrations", method = RequestMethod.POST)
	@ResponseBody
	public String listReleaseIntegrations(
			@RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize,
			@RequestParam(name = "pageNum", required = false, defaultValue = "1") int pageNum,
			@RequestParam(name = "sortOrder", required = false) String sortOrder,
			@RequestParam(name = "sortName", required = false) String sortName, @Context HttpServletRequest request) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("sortOrder", sortOrder);
			params.put("sortName", sortName);
			Page page = releaseIntegrationService.listReleaseIntegrations(params, pageSize, pageNum);
			return MessageHelper.wrap("result", true, "message", "查询发布集成成功", "page", page);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "查询发布集成失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "getReleaseIntegrationById", method = RequestMethod.POST)
	@ResponseBody
	public String getReleaseIntegrationById(@RequestParam("integrationId") int integrationId) {
		try {
			ReleaseIntegration task = releaseIntegrationService.getReleaseIntegrationById(integrationId);
			return MessageHelper.wrap("result", true, "message", "获取集成详情成功", "data", task);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "获取集成详情失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "saveReleaseIntegration", method = RequestMethod.POST)
	@ResponseBody
	public String saveReleaseIntegration(@RequestParam("name") String name, @RequestParam("type") String type,
			@RequestParam("systemId") String systemId, 
			@RequestParam(name = "description", required = false, defaultValue = "") String description,
			@RequestParam("url") String url, @RequestParam("user") String user,
			@RequestParam(name = "password", required = false, defaultValue = "") String password,
			@Context HttpServletRequest request) {
		try {
			if (name == null || "".equals(name.trim())) {
				throw new Exception("发布集成名称不能为空，请重新命名！");
			}
			List<ReleaseIntegration> names = releaseIntegrationService.getReleaseIntegrationByName(name);
			if (names != null && names.size() > 0) {
				throw new Exception("发布集成名称[" + name + "]已存在，请重新命名！");
			}
			User requestUser = (User) request.getSession().getAttribute("user");
			ReleaseIntegration task = new ReleaseIntegration(name, type, systemId,
					description, url, user, password, requestUser.getName());
			int rows = releaseIntegrationService.saveReleaseIntegration(task);
			return MessageHelper.wrap("result", true, "message", "保存发布集成成功", "id", task.getId());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "保存发布集成失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "updateReleaseIntegration", method = RequestMethod.POST)
	@ResponseBody
	public String updateReleaseIntegration(@RequestParam("id") int id, @RequestParam("name") String name, 
			@RequestParam("type") String type,
			@RequestParam("systemId") String systemId, 
			@RequestParam(name = "description", required = false, defaultValue = "") String description,
			@RequestParam("url") String url, @RequestParam("user") String user,
			@RequestParam(name = "password", required = false, defaultValue = "") String password,
			@Context HttpServletRequest request) {
		try {
			if (name == null || "".equals(name.trim())) {
				throw new Exception("发布集成名称不允许为空，请检查！");
			}
			ReleaseIntegration oldTask = releaseIntegrationService.getReleaseIntegrationById(id);
			if (oldTask == null) {
				throw new Exception("原发布集成不存在，无法更新，请检查！");
			} else {
				if (!name.equals(oldTask.getName())) {
					List<ReleaseIntegration> names = releaseIntegrationService.getReleaseIntegrationByName(name);
					if (names != null && names.size() > 0) {
						throw new Exception("新的发布集成名称[" + name + "]已存在，请重新命名！");
					}
				}
			}

			User requestUser = (User) request.getSession().getAttribute("user");
			if (!"admin".equals(requestUser.getName()) && !requestUser.getName().equals(oldTask.getCreater())) {
				throw new Exception("目前登录用户[" + requestUser.getName() + "]不是集成创建人，无权修改！");
			}
			ReleaseIntegration updateTask = new ReleaseIntegration(id, name, type, 
					systemId, description, url, user, password);
			releaseIntegrationService.updateReleaseIntegration(updateTask);
			return MessageHelper.wrap("result", true, "message", "编辑发布集成成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "编辑发布集成失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "deleteReleaseIntegration", method = RequestMethod.POST)
	@ResponseBody
	public String deleteReleaseIntegration(@RequestParam("integrationId") int integrationId, @Context HttpServletRequest request) {
		try {
			ReleaseIntegration task = releaseIntegrationService.getReleaseIntegrationById(integrationId);
			if (task == null) {
				throw new Exception("当前发布集成已不存在，无法删除！");
			}
			User user = (User) request.getSession().getAttribute("user");
			if (!"admin".equals(user.getName()) && !user.getName().equals(task.getCreater())) {
				throw new Exception("目前登录用户[" + user.getName() + "]不是发布集成创建人，无权删除！");
			}
			releaseIntegrationService.deleteReleaseIntegration(integrationId);
			return MessageHelper.wrap("result", true, "message", "删除发布集成成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "删除发布集成失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "synReleaseIntegration", method = RequestMethod.POST)
	@ResponseBody
	public String synReleaseIntegration(@RequestParam("integrationId") int integrationId, @Context HttpServletRequest request) {
		try {
			ReleaseIntegration task = releaseIntegrationService.getReleaseIntegrationById(integrationId);
			if (task == null) {
				throw new Exception("当前集成已不存在，无法同步！");
			}
			User user = (User) request.getSession().getAttribute("user");
			if (!"admin".equals(user.getName()) && !user.getName().equals(task.getCreater())) {
				throw new Exception("目前登录用户[" + user.getName() + "]不是集成创建人，无权同步！");
			}
			int synSize = releaseIntegrationService.synReleaseIntegration(integrationId);
			return MessageHelper.wrap("result", true, "message", "同步集成成功，更新[" + synSize + "]条记录");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "同步集成失败:" + e.getMessage());
		}
	}
	
	@RequestMapping(value = "releaseIntegrationConnectionTest", method = RequestMethod.POST)
	@ResponseBody
	public String releaseIntegrationConnectionTest(@RequestParam("type") String type, @RequestParam("url") String url, 
			@RequestParam("user") String user, 
			@RequestParam(name = "password", required = false, defaultValue = "") String password,
			@Context HttpServletRequest request) {
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("type", type);
			params.put("url", url);
			params.put("user", user);
			params.put("password", password);
			boolean result = releaseIntegrationService.releaseIntegrationConnectionTest(params);
			return MessageHelper.wrap("result", result, "message", "集成连接测试" + (result ? "成功" : "失败"));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", "集成连接测试失败:" + e.getMessage());
		}
	}


}
