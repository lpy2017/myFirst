package com.dc.appengine.appmaster.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RequestClientENV {

//	@Value("${PoolingHttpClientConnectionManager.timeToLive}")
	public static long timeToLive;
//	@Value("${PoolingHttpClientConnectionManager.maxTotal}")
	public static int maxTotal;
//	@Value("${PoolingHttpClientConnectionManager.defaultMaxPerRoute}")
	public static int defaultMaxPerRoute;
//	@Value("${HttpClientBuilder.retry}")
	public static int retry;
//	@Value("${HttpComponentsClientHttpRequestFactory.connectTimeout}")
	public static int connectTimeout;
//	@Value("${HttpComponentsClientHttpRequestFactory.readTimeout}")
	public static int readTimeout;
//	@Value("${HttpComponentsClientHttpRequestFactory.connectionRequestTimeout}")
	public static int connectionRequestTimeout;
//	@Value("${ftp.dataTimeout}")
	public static int ftpDataTimeout;
//	@Value("${ftp.connectTimeout}")
	public static int ftpConnectTimeout;

	public RequestClientENV(@Value("${PoolingHttpClientConnectionManager.timeToLive}") long timeToLive,
			@Value("${PoolingHttpClientConnectionManager.maxTotal}") int maxTotal,
			@Value("${PoolingHttpClientConnectionManager.defaultMaxPerRoute}") int defaultMaxPerRoute,
			@Value("${HttpClientBuilder.retry}") int retry,
			@Value("${HttpComponentsClientHttpRequestFactory.connectTimeout}") int connectTimeout,
			@Value("${HttpComponentsClientHttpRequestFactory.readTimeout}") int readTimeout,
			@Value("${HttpComponentsClientHttpRequestFactory.connectionRequestTimeout}") int connectionRequestTimeout,
			@Value("${ftp.dataTimeout}") int ftpDataTimeout,
			@Value("${ftp.connectTimeout}") int ftpConnectTimeout) {
		RequestClientENV.timeToLive = timeToLive;
		RequestClientENV.maxTotal = maxTotal;
		RequestClientENV.defaultMaxPerRoute = defaultMaxPerRoute;
		RequestClientENV.retry = retry;
		RequestClientENV.connectTimeout = connectTimeout;
		RequestClientENV.readTimeout = readTimeout;
		RequestClientENV.connectionRequestTimeout = connectionRequestTimeout;
		RequestClientENV.ftpDataTimeout = ftpDataTimeout;
		RequestClientENV.ftpConnectTimeout = ftpConnectTimeout;
	}

}
