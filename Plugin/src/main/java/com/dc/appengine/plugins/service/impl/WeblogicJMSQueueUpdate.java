package com.dc.appengine.plugins.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.plugins.constants.Constants;
import com.dc.appengine.plugins.utils.JudgeUtil;
import com.dc.appengine.plugins.utils.LogRecord;

public class WeblogicJMSQueueUpdate extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(WeblogicJMSQueueUpdate.class);

	private static final String DOMAIN_DIR = "domainDir";
	private static final String QUEUE_NAME = "queueName";
	private static final String QUEUE_JNDINAME = "queueJNDIName";
	private static final String THRESHOLDS_BYTES = "thresholds_bytes";
	private static final String THRESHOLDS_MESSAGES = "thresholds_messages";
	private static final String MAXIMUM_MESSAGE_SIZE = "maximum_message_size";
	private static final String JMS_MODULE_DESCRIPTOR = "JMSModuleDescriptor";
	
	private String domainDir;
	private String queueName;
	private String queueJNDIName;
	private String JMSModuleDescriptor;
	
	private String thresholds_messages;
	private String maximum_message_size;
	private String thresholds_bytes;
	
	private String bytes_high;
	private String bytes_low;
	private String messages_high;
	private String messages_low;

	@Override
	public String doPreAction() {
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doInvoke() {
		sendMessageByMOM(this.paramMap, this.messageMap, paramMap.get(Constants.Plugin.PLUGINNAME).toString());
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doPostAction() {
		return JSON.toJSONString(paramMap);
	}

	@Override
	public String doAgent() throws Exception {
		Map<String,Object> resultMap = new HashMap<>();
		if(JudgeUtil.isEmpty(this.domainDir)){
			resultMap.put("result", false);
			resultMap.put("message", "参数domainDir为空");
			return JSON.toJSONString(resultMap);
		}else{
			File domainDir = new File(this.domainDir);
			if(!domainDir.exists()){
				resultMap.put("result", false);
				resultMap.put("message", "weblogic的domain目录 "+this.domainDir+" 不存在");
				return JSON.toJSONString(resultMap);
			}
		}
		String JMSModuleDesPath =this.domainDir+File.separator+"config"+File.separator+this.JMSModuleDescriptor;
		if(JudgeUtil.isEmpty(this.JMSModuleDescriptor)){
			resultMap.put("result", false);
			resultMap.put("message", "参数JMSModuleDescriptor为空");
			return JSON.toJSONString(resultMap);
		}else{
			File JMSModuleDescriptorFile = new File(JMSModuleDesPath);
			if(!JMSModuleDescriptorFile.exists()){
				resultMap.put("result", false);
				resultMap.put("message", "JMS module的描述文件 "+this.JMSModuleDescriptor+" 不存在");
				return JSON.toJSONString(resultMap);
			}
		}
		if(JudgeUtil.isEmpty(this.queueName)){
			resultMap.put("result", false);
			resultMap.put("message", "参数queueName为空");
			return JSON.toJSONString(resultMap);
		}
		XMLWriter writer=null;
		File JMSModuleDescriptorFile=null;
		OutputStream out =null;
		try {
			JMSModuleDescriptorFile = new File(JMSModuleDesPath);
			Document document = DocumentHelper.createDocument();
			SAXReader reader = new SAXReader();
			document = reader.read(JMSModuleDescriptorFile);
			Element root =document.getRootElement();
			//判断是否存在队列queueName
			List queues = root.elements("queue");
			Iterator it = queues.iterator();
			Element queueElement=null;
			while(it.hasNext()){
				queueElement = (Element) it.next();
				String value =queueElement.attribute("name").getValue();
				if(value.equals(this.queueName)){
					 break;
				}else{
					queueElement=null;
				}
			}
			if(queueElement ==null){
				resultMap.put("result", false);
				resultMap.put("message", this.JMSModuleDescriptor+"不存在队列 "+this.queueName);
				return JSON.toJSONString(resultMap);
			}
			//修改document
			if(!JudgeUtil.isEmpty(this.queueJNDIName)){
				queueElement.element("jndi-name").setText(this.queueJNDIName);
			}
			if(!JudgeUtil.isEmpty(this.thresholds_bytes)){
				String[] byteRang=this.thresholds_bytes.split("-");
				if(byteRang.length>1){
					this.bytes_high=byteRang[1];
				}
				this.bytes_low=byteRang[0];
				Element thresholdElement =queueElement.element("thresholds");
				if(thresholdElement==null){
					thresholdElement=queueElement.addElement("thresholds");
				}
				if(!JudgeUtil.isEmpty(this.bytes_high)){
					Element bytesHighElement =thresholdElement.element("bytes-high");
					if(bytesHighElement==null){
						bytesHighElement=thresholdElement.addElement("bytes-high");
					}
					bytesHighElement.setText(this.bytes_high);
				}
				if(!JudgeUtil.isEmpty(this.bytes_low)){
					Element bytesLowElement =thresholdElement.element("bytes-low");
					if(bytesLowElement==null){
						bytesLowElement=thresholdElement.addElement("bytes-low");
					}
					bytesLowElement.setText(this.bytes_low);
				}
			}
			if(!JudgeUtil.isEmpty(this.thresholds_messages)){
				String[] messageRang=this.thresholds_messages.split("-");
				if(messageRang.length>1){
					this.messages_high=messageRang[1];
				}
				this.messages_low=messageRang[0];
				Element thresholdElement =queueElement.element("thresholds");
				if(thresholdElement==null){
					thresholdElement=queueElement.addElement("thresholds");
				}
				if(!JudgeUtil.isEmpty(this.messages_high)){
					Element messagesHighElement =thresholdElement.element("messages-high");
					if(messagesHighElement==null){
						messagesHighElement=thresholdElement.addElement("messages-high");
					}
					messagesHighElement.setText(this.messages_high);
				}
				if(!JudgeUtil.isEmpty(this.messages_low)){
					Element messagesLowElement =thresholdElement.element("messages-low");
					if(messagesLowElement==null){
						messagesLowElement=thresholdElement.addElement("messages-low");
					}
					messagesLowElement.setText(this.messages_low);
				}
				
			}
			if(!JudgeUtil.isEmpty(this.maximum_message_size)){
				Element element =queueElement.element("maximum-message-size");
				if(element==null){
					element=queueElement.addElement("maximum-message-size");
				}
				element.setText(this.maximum_message_size);
			}
			//重新写入文件
			OutputFormat format = OutputFormat.createPrettyPrint();
			out =new FileOutputStream(JMSModuleDescriptorFile);
			writer = new XMLWriter(out, format);
			writer.write(document);
			writer.close();
			writer=null;
			out =null;
		} catch(Exception e){
			e.printStackTrace();
			resultMap.put("result", false);
			resultMap.put("message", "修改队列 "+this.queueName+" 发生异常："+System.lineSeparator()+LogRecord.getStackTrace(e));
			return JSON.toJSONString(resultMap);
		}finally{
			// TODO: handle exception
			if(writer !=null){
				writer.close();
			}
			if(out !=null){
				out.close();
			}
		}
		resultMap.put("result", true);
		resultMap.put("message", "修改队列成功");
		return JSON.toJSONString(resultMap);
	}

	public void setDomainDir(String domainDir) {
		this.domainDir = domainDir;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public void setQueueJNDIName(String queueJNDIName) {
		this.queueJNDIName = queueJNDIName;
	}

	public void setJMSModuleDescriptor(String jMSModuleDescriptor) {
		JMSModuleDescriptor = jMSModuleDescriptor;
	}

	public void setThresholds_messages(String thresholds_messages) {
		this.thresholds_messages = thresholds_messages;
	}

	public void setMaximum_message_size(String maximum_message_size) {
		this.maximum_message_size = maximum_message_size;
	}

	public void setThresholds_bytes(String thresholds_bytes) {
		this.thresholds_bytes = thresholds_bytes;
	}

	@Override
	public void setFields() {
		if (!JudgeUtil.isEmpty(this.pluginInput.get(DOMAIN_DIR))) {
			this.domainDir = this.pluginInput.get(DOMAIN_DIR).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(QUEUE_NAME))) {
			this.queueName = this.pluginInput.get(QUEUE_NAME).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(QUEUE_JNDINAME))) {
			this.queueJNDIName = this.pluginInput.get(QUEUE_JNDINAME).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(JMS_MODULE_DESCRIPTOR))) {
			this.JMSModuleDescriptor = this.pluginInput.get(JMS_MODULE_DESCRIPTOR).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(THRESHOLDS_BYTES))) {
			this.thresholds_bytes = this.pluginInput.get(THRESHOLDS_BYTES).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(THRESHOLDS_MESSAGES))) {
			this.thresholds_messages = this.pluginInput.get(THRESHOLDS_MESSAGES).toString();
		}
		if (!JudgeUtil.isEmpty(this.pluginInput.get(MAXIMUM_MESSAGE_SIZE))) {
			this.maximum_message_size = this.pluginInput.get(MAXIMUM_MESSAGE_SIZE).toString();
		}
	}
	
	public static void main(String[]args){
		WeblogicJMSQueueUpdate queueCreate = new WeblogicJMSQueueUpdate();
		queueCreate.setDomainDir("C:/Users/hansn/Desktop/123/");
		queueCreate.setJMSModuleDescriptor("jms/SystemModule-2-jms.xml");
		queueCreate.setQueueJNDIName("Queue_JNDI_hsn_new");
		queueCreate.setQueueName("Queue_hsn");
		queueCreate.setThresholds_messages("10-20");
		queueCreate.setThresholds_bytes("15-25");
		queueCreate.setMaximum_message_size("10");
		try {
			System.out.println(queueCreate.doAgent());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
