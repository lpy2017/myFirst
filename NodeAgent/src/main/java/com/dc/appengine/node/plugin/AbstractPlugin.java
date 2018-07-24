package com.dc.appengine.node.plugin;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.node.message.InnerMessage;

public abstract class AbstractPlugin<T> {
	@SuppressWarnings("rawtypes")
	public static AbstractPlugin getInstance(Class<? extends AbstractPlugin> c)
			throws Exception {
		return c.newInstance();
	}

	public abstract T excute(T message) throws Exception;
	public static void errorResult(InnerMessage<Map<String, Object>> message,String error){
		Map<String, Object> mMap = (JSON
				.parseObject(message.getContent()
						.get("message").toString()));
		Map<String, Object> rMap = new HashMap<String, Object>();
		rMap.put("result", false);
		rMap.put("message", error);
		mMap.put("result", rMap);
//		message.getContent().put("message",
//				JSON.toJSONString(mMap));
		message.getContent().put("message",
				mMap);
	}
}
