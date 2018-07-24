package com.dc.appengine.appmaster.entity;

import java.util.ArrayList;
import java.util.List;

public final class ResourceCode {

	public static final String USERMANAGER = "用户";
	public static final String LOGIN = "login";
	public static final String LOGOUT = "logout";
	public static final String ENVIRONMENT = "部署环境";
	public static final String BLUEPRINTTEMPLATE = "蓝图模板";
	public static final String BLUEPRINTINSTANCE = "蓝图实例";
	public static final String BLUEPRINTFLOW = "蓝图流程";
	public static final String BLUEPRINTAPPMANAGE = "蓝图实例组件";
	public static final String COMPONENT = "通用组件";
	public static final String COMPONENTFLOW = "通用组件流程";
	public static final String COMPONENTITSM = "ITSM组件流程";
	public static final String AUTHORITY = "权限";
	public static final String PLUGIN = "插件";
	public static final String ARTIFACT = "工件";
	public static final String SCRIPT = "脚本";
	public static final String CUSTOMIZATION  = "定制";
	public static final String TIMETASK  = "定时任务";
	public static final String LABELMNG  = "标签";
	
	public static final List<String> MENUACTION = new ArrayList<String>();
	static{
		MENUACTION.add(USERMANAGER);
		MENUACTION.add(ENVIRONMENT);
		MENUACTION.add(BLUEPRINTTEMPLATE);
		MENUACTION.add(BLUEPRINTINSTANCE);
		MENUACTION.add(BLUEPRINTFLOW);
		MENUACTION.add(BLUEPRINTAPPMANAGE);
		MENUACTION.add(COMPONENT);
		MENUACTION.add(COMPONENTFLOW);
		MENUACTION.add(COMPONENTITSM);
		MENUACTION.add(AUTHORITY);
		MENUACTION.add(PLUGIN);
		MENUACTION.add(ARTIFACT);
		MENUACTION.add(SCRIPT);
		MENUACTION.add(CUSTOMIZATION);
		MENUACTION.add(TIMETASK);
		MENUACTION.add(LABELMNG);
		
	}
	public final class Operation {
		public static final String ADD = "add";
		public static final String UPDATE = "update";
		public static final String DELETE = "delete";
		public static final String IMPORT = "import";
		public static final String EXPORT = "export";
		public static final String CLONE = "clone";
		public static final String EXECUTE = "execute";
		public static final String REDUCE = "reduce";
		public static final String INCREASE = "increase";
		public static final String MANAGE = "manage";
		public static final String DEPLOY = "deploy";
		public static final String START = "start";
		public static final String STOP = "stop";
		public static final String DESTROY = "destroy";
		public static final String UPGRADE = "upgrade";
		public static final String ROLLBACK = "rollback";
		public static final String SNAPSHOT = "snapshot";
		public static final String ITSMRELEASE = "itsmRelease";
		public static final String ITSMROLLBACK = "itsmRollback";
		public static final String ITSMSTART = "itsmStart";
		public static final String ITSMSTOP = "itsmStop";
		public static final String ITSMBATCHRELEASE = "itsmBatchRelease";
	}
	public final class User {
		public static final String LOGIN = "login";
		public static final String LOGOUT = "logout";
	}
}
