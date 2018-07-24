/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.configuration;

import java.io.Serializable;
import java.util.Map;

/**
 * 上下文接口
 * <b>
 * <p>定义系统内部上下文方法。</p>
 * <p>
 * 上下文中包括Attribute与payload。
 * Attribute用于保存上下文中设置的一些非数据对象。例如fromId、toId、routeStack等
 * payload用于承载上下文传递的数据对象（VO/BO）。数据对象可以使byte[],String或其他Object。
 * </p>
 * @author liubingj
 */
public interface Context extends Serializable {
	
	/**
	 * 获得当前上下文唯一标识
	 * @return
	 */
	public String getId();
	
	/**
	 * 获得当前上下文创建时的Timestamp
	 * @return
	 */
	public long getStartTime();
	
	/**
	 * 获得上下文属性
	 * @return
	 */
	public Map< String, Object > getAttributes();
	
	/**
	 * 根据指定Key获得上下文属性值
	 * @param key
	 * @return
	 */
	public < T > T getAttribute( String key );
	
	/**
	 * 设置上下文属性
	 * @param attributes
	 */
	public void setAttributes( Map< String, Object > attributes );
	
	/**
	 * 根据给定的Key-Value设置上下文属性
	 * @param key
	 * @param value
	 */
	public < T > void setAttribute( String key, T value );
	
	/**
	 * 获得上下文对象的有效载荷
	 * @return
	 */
	public < T > T getPayload();
	
	/**
	 * 设置上下文的有效载荷
	 * @param payload
	 */
	public < T > void setPayload( T payload );

}
