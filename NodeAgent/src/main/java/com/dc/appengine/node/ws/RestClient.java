package com.dc.appengine.node.ws;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.utils.MD5Util;

public class RestClient {

	/**
	 * 已知ip转换为webtarget对象
	 */
	public WebTarget ipToWegt(String ip){
		Client client = ClientBuilder.newClient();
		client.register(ExceptionResponseFilter.class);
		WebTarget rs = client.target( ip );
		return rs;
	}
	
	/**
	 * 添加头信息认证
	 * @param date
	 * @param userName
	 * @param authorization
	 * @param res
	 * @return
	 */
	public Builder addAuthHeader(Builder res, Date date, String userName, String pwd, String... params){
		
		String gmtStr = this.getGMTString(new Date());
		String authorization = this.sign(gmtStr, pwd, params);
		return res.header("date", gmtStr).header("userName", userName).header("authorization", authorization);
		
	}
	
	/**
	 * 产生一个GMT格式的字符串
	 * @param date
	 * @return
	 */
	public String getGMTString(Date date){
		
		DateFormat df = new SimpleDateFormat( "EEE, d MMM yyyy HH:mm:ss 'GMT'", Locale.US );
		return df.format(date);
		
	}
	
	/**
	 *产生一个认证序列号，md5(参数总长度+md5(PassWord)+GMT格式时间)
	 * @param date
	 * @param params
	 * @return
	 */
	public String sign(String date,String password,String... params){
		
		int len = 0;
		if( params != null ){
			for(String param : params){
				if( param != null ){
					len = param.length() + len;
				}
			}
		}
		return MD5Util.md5(len + MD5Util.md5(password) + date); 
		
	}
}

/**
 * rest调用异常处理
 * @author xuxyc
 */
class ExceptionResponseFilter implements ClientResponseFilter {
	private static Logger log = LoggerFactory
			.getLogger(ExceptionResponseFilter.class);

	@Override
	public void filter(ClientRequestContext arg0, ClientResponseContext arg1)
			throws IOException {
		if (arg1.getStatus() / 100 != 2) {
			if (log.isDebugEnabled()) {
				log.debug(arg0.getUri() + " with status: " + arg1.getStatus()
						+ ", " + arg1.getStatusInfo());
			}
		}
	}

}
