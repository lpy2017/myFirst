package com.dc.appengine.cloudui.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dc.appengine.cloudui.entity.MasterEnv;
import com.dcits.Common.entity.User;

public class SessionFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String className = request.getParameter("className");
		String methodName = request.getParameter("methodName");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String path = req.getRequestURI();
		boolean isAjax = "XMLHttpRequest".equalsIgnoreCase(req
				.getHeader("X-Requested-With"));
		boolean isNotLogin = className != null
				&& methodName != null
				&& !(className.equals("loginAction") && methodName
						.equals("login"));
		if ((isAjax && isNotLogin)
		// || (path.endsWith(".jsp") && !path.endsWith("login.jsp") && !path
		// .endsWith("index.jsp"))
		) {
			//for cop start
			Object userId = req.getSession().getAttribute("userId");
			if (userId != null) {
				User user = new User();
				user.setId(1L);
				user.setName("admin");
				req.getSession().setAttribute("user", user);
			}
			//for cop end
			Object user = req.getSession().getAttribute("user");
			if (user == null) {
				// 如果是ajax请求
				if (isAjax && isNotLogin) {
					res.setHeader("sessionTimeout", "true");
					// 错误码为400，表示客户端错误
					res.setStatus(401);
					// res.sendRedirect(ConfigHelper.getValue("server.name") +
					// "/matrix/r.html");
					// res.getWriter().write("{}");
				} else {
					res.sendRedirect(MasterEnv.server_name
							+ "/matrix/r.html");
				}
				return;
			}
		}
		//for cop start
		Object userId = req.getSession().getAttribute("userId");
		if (userId != null) {
			User user = new User();
			user.setId(1L);
			user.setName("admin");
			req.getSession().setAttribute("user", user);
		}
		//for cop end
		Object user = req.getSession().getAttribute("user");
		if (user == null) {
			String restFilter[] = MasterEnv.restFilter
					.split(",");
			boolean flag = false;//  “/wb/*表示springboot 的资源路径
			if (path.contains(MasterEnv.server_name + "/ws/")) {
				boolean isAllow = restFilter[0].equals("true");
				flag = !isAllow;
				for (int i = 1; i < restFilter.length; i++) {
					String pathAndParam[] = restFilter[i].split("\\?");
					if (path.contains(MasterEnv.server_name
							+ "/ws" + pathAndParam[0])) {
						if (pathAndParam.length > 1) {
							String params[] = pathAndParam[1].split("&");
							int j = 0;
							for (j = 0; j < params.length; j++) {
								String kv[] = params[j].split("=");
								String value = req.getParameter(kv[0]);
								if (value == null || kv.length < 2
										|| !value.equals(kv[1])) {
									break;
								}
							}
							if (j == params.length) {
								flag = isAllow;
							}
						} else {
							flag = isAllow;
						}
						break;
					}

				}
				if (flag == false) {
					res.setHeader("sessionTimeout", "true");
					res.setStatus(401);
					return;
				}
			}
			if (path.endsWith(".html")) {
				String pageFilter[] = MasterEnv.pageFilter
						.split(",");
				boolean isAllow = pageFilter[0].equals("true");
				flag = !isAllow;
				for (int i = 1; i < pageFilter.length; i++) {
					if (path.endsWith(pageFilter[i])) {
						flag = isAllow;
						break;
					}
				}
				String loginPath = MasterEnv.loginPage;
				if (path.contains(loginPath)) {
					flag = true;
				}
				if (flag == false) {
					res.sendRedirect(loginPath);//
					return;
				}
			}
		}
//		req.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
