/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.plugins.command.executor;

/**
 * 系统命令接口
 * @author liubingj
 */
public interface CommandExecutor {
	
	public boolean exec( String command );
	
}
