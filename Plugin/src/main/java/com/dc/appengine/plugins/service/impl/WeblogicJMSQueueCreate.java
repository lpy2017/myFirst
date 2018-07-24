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

public class WeblogicJMSQueueCreate extends AbstractPlugin {
	private static Logger log = LoggerFactory.getLogger(WeblogicJMSQueueCreate.class);

	private static final String DOMAIN_DIR = "domainDir";
	private static final String QUEUE_NAME = "queueName";
	private static final String QUEUE_JNDINAME = "queueJNDIName";
	private static final String SUB_DEPLOYNAME = "subDeployName";
	private static final String JMS_MODULE_DESCRIPTOR = "JMSModuleDescriptor";

	private String domainDir;
	private String queueName;
	private String queueJNDIName;
	private String JMSModuleDescriptor;
	private String subDeployName;

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
		if(JudgeUtil.isEmpty(this.subDeployName)){
			resultMap.put("result", false);
			resultMap.put("message", "参数subDeployName为空");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(this.queueName)){
			resultMap.put("result", false);
			resultMap.put("message", "参数queueName为空");
			return JSON.toJSONString(resultMap);
		}
		if(JudgeUtil.isEmpty(this.queueJNDIName)){
			resultMap.put("result", false);
			resultMap.put("message", "参数queueJNDIName为空");
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
			//判断是否存在同名队列
			List queues = root.elements("queue");
			Iterator it = queues.iterator();
			while(it.hasNext()){
				Element element = (Element) it.next();
				String value =element.attribute("name").getValue();
				if(value.equals(this.queueName)){
					resultMap.put("result", false);
					resultMap.put("message", this.JMSModuleDescriptor+"存在同名队列 "+this.queueName);
					return JSON.toJSONString(resultMap);
				}
			}
			//修改document,添加队列节点
			Element queue = root.addElement("queue");
			queue.addAttribute("name", this.queueName);
			Element subDeployName =queue.addElement("sub-deployment-name");
			subDeployName.setText(this.subDeployName);
			Element jndiName =queue.addElement("jndi-name");
			jndiName.setText(this.queueJNDIName);
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
			resultMap.put("message", "添加队列 "+this.queueName+" 发生异常："+System.lineSeparator()+LogRecord.getStackTrace(e));
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
		resultMap.put("message", "添加队列成功");
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

	
	public void setSubDeployName(String subDeployName) {
		this.subDeployName = subDeployName;
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
		
		if (!JudgeUtil.isEmpty(this.pluginInput.get(SUB_DEPLOYNAME))) {
			this.subDeployName = this.pluginInput.get(SUB_DEPLOYNAME).toString();
		}
	}
	
	public static void main(String[]args){
		WeblogicJMSQueueCreate queueCreate = new WeblogicJMSQueueCreate();
		queueCreate.setDomainDir("C:/Users/hansn/Desktop/123/");
		queueCreate.setJMSModuleDescriptor("jms/SystemModule-2-jms.xml");
		queueCreate.setQueueJNDIName("Queue_JNDI_hsn");
		queueCreate.setQueueName("Queue_hsn");
		queueCreate.setSubDeployName("hsn_sub");
		try {
			System.out.println(queueCreate.doAgent());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
