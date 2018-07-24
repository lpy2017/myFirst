package com.dc.appengine.plugins.context;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.dc.appengine.plugins.message.bean.InnerMessage;

public class JMSContext implements Context {


	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> attributes=new HashMap<String, Object>();
	@SuppressWarnings("unchecked")
	private InnerMessage payload;
	
	public JMSContext(){
		
		attributes.put("id", UUID.randomUUID().toString());
		
	}
	public JMSContext(String id){
		
		attributes.put("id", id);
		
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key) {

		return (T) attributes.get(key);
	
	}

	public Map<String, Object> getAttributes() {

		return this.attributes;

	}

	public String getId() {
	
		return (String) this.attributes.get("id");
	
	}

	@SuppressWarnings("unchecked")
	public <T> T getPayload() {

		return (T) this.payload;

	}

	public long getStartTime() {

		return 0;

	}

	public <T> void setAttribute(String key, T value) {


		attributes.put(key, value);
		
	}

	public void setAttributes(Map<String, Object> attributes) {
		
		for (String key : attributes.keySet()) {
			this.setAttribute(key, attributes.get(key));
		}
		
	}

	@SuppressWarnings("unchecked")
	public <T> void setPayload(T payload) {
		
		this.payload=(InnerMessage) payload;

	}

}
