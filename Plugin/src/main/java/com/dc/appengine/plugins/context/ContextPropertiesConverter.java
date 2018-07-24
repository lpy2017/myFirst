package com.dc.appengine.plugins.context;

import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;


/**
 * 上下文和消息头相互转换
 * @author yangleiv
 *
 */
public class ContextPropertiesConverter {
	
	/**
	 * 上下文转换成消息头
	 * @param context
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Properties context2Properties(Context context) {
		Properties pro = new Properties();
		
		Map map=context.getAttributes();
		Iterator iterator=map.entrySet().iterator();
		while(iterator.hasNext()){
			Entry entry=(Entry) iterator.next();
			if(entry.getKey()!=null && entry.getValue()!=null){
				pro.setProperty(entry.getKey().toString(), entry.getValue().toString());
			}			
		}
		return pro;
	}
	
	/**
	 * 消息头转换成上下文
	 * @param props
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Context properties2Context(Map props) {
		Context context = new JMSContext((String) props.get("id"));
		context.setAttributes(props);
		return context;
	}
}
