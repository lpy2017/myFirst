package com.dc.appengine.plugins.utils;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailUtil {

	
	public static boolean sendMail(Map<String,Object> params){
				try {
					String sender=(String)params.get("sender");//邮件发送者账户
					String recipient=(String)params.get("recipient");//邮件接收者账户
					String password=(String)params.get("password");//邮件接收者账户密码
					String subject=(String)params.get("subject");//发送主题
					String content=(String)params.get("content");//发送内容
					String smtpHost=(String)params.get("smtpHost");//邮件服务器
					String smtpPort=(String)params.get("smtpPort");//服务器端口
					Properties props = new Properties();//环境变量设置。发送邮件时才需要
					props.setProperty("mail.transport.protocol", "smtp");//发送使用的协议
					props.setProperty("mail.host", smtpHost);//发送服务器的主机地址
					props.setProperty("mail.smtp.port", smtpPort);
					props.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");//这行是关键，设置后才能使用qq邮箱
			        props.setProperty("mail.smtp.socketFactory.fallback", "false");
			        props.setProperty("mail.smtp.socketFactory.port", smtpPort);
					props.setProperty("mail.smtp.auth", "true");//请求身份验证
					props.setProperty("mail.debug", "true");//调试模式
					Session session = Session.getDefaultInstance(props);
					
					MimeMessage message = new MimeMessage(session);//代表一封邮件
					message.setFrom(new InternetAddress(sender));//设置发件人
					message.setRecipients(Message.RecipientType.TO, recipient);//设置收件人
					message.setSubject(subject);//设置主体
					//设置邮件的正文内容
					MimeBodyPart textPart = new MimeBodyPart();
					textPart.setContent(content, "text/html;charset=UTF-8");
					MimeMultipart mpart = new MimeMultipart();
					mpart.addBodyPart(textPart);
					message.setContent(mpart);
					message.saveChanges();
					//发送邮件
					Transport ts = session.getTransport();//得到火箭
					ts.connect(sender,password);//连接
					ts.sendMessage(message, message.getAllRecipients());
					ts.close();
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				} 
		}
	public static void main(String[] args) {
		String sender="smartcdadmin@163.com";//邮件发送者账户
		String recipient="hsn060407307@163.com";//邮件接收者账户
		String password="smartAdmin123";//邮件接收者账户密码
		String subject="邮件通知";//发送主题
		String content="你好:<br><pre>    邮件通知</pre>";//发送内容
		String smtpHost="smtp.163.com";//邮件服务器
		String smtpPort="465";//服务器端口
		Map<String,Object> params = new HashMap<>();
		params.put("sender", sender);
		params.put("password", password);
		params.put("smtpHost", smtpHost);
		params.put("smtpPort", smtpPort);
		params.put("subject", subject);
		params.put("recipient", recipient);
		params.put("content", content);
		System.out.println(sendMail(params));
	}
	
}
