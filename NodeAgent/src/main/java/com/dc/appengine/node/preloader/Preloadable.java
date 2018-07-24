/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.preloader;

/**
 * 预加载接口
 * <b>
 * <p>所有要被预加载的类都要实现此接口.</p>
 * @author liubingj
 */
public interface Preloadable {
	
	/**
	 * 预加载执行方法
	 * @throws Exception
	 */
	public void preload() throws Exception;

}
