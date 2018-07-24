package com.dc.appengine.appsvn.utils.registry;

import com.dc.appengine.appsvn.utils.ConfigHelper;

public class Constant {
	public static String success = "success";
	public static String catalogUser = ConfigHelper.getValue("catalogUser");
	public static String catalogPassword = ConfigHelper
			.getValue("catalogUserPassword");
}
