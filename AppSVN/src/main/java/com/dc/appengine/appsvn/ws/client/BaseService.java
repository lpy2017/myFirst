package com.dc.appengine.appsvn.ws.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.ws.rs.client.Invocation.Builder;

import com.dc.appengine.appsvn.utils.MD5Util;

public class BaseService {
	/**
	 * 产生一个认证序列号，md5(参数总长度+md5(PassWord)+GMT格式时间)
	 * @param date
	 * @param params
	 * @return
	 */
	public String sign(String date,String password,String... params){
		
		int len = 0;
		if( params != null ){
			for(String param : params){
				len = param.length() + len;
			}
		}
		return MD5Util.md5(len + MD5Util.md5(password) + date); 
		
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
}
