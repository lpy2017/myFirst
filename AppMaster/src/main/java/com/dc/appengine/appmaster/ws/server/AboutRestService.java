package com.dc.appengine.appmaster.ws.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dc.appengine.appmaster.utils.Utils;

@RestController
@RequestMapping("/ws/about")
public class AboutRestService {
	
	private static final Logger log = LoggerFactory.getLogger(AboutRestService.class);
	
	@Value("${ftp.url:localhost}")
	private String hostname;
	@Value("${ftp.port:21}")
	private int port;
	@Value("${ftp.usr:paas}")
	private String username;
	@Value("${ftp.pwd:123456}")
	private String password;
	@Value("${about.pathname:/about}")
	private String pathname;
	@Value("${about.version:version.txt}")
	private String version;
	@Value("${about.versionInfo:versionInfo.html}")
	private String versionInfo;
	
	@RequestMapping(value = "version", method = RequestMethod.GET)
	public Object version() {
		BufferedReader br = new BufferedReader(new InputStreamReader(Utils.getInputStream(hostname, port, username, password, pathname, version)));
		StringBuilder sb = new StringBuilder();
		String str;
		try {
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			return sb.toString();
		} catch (IOException e) {
			log.error("", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					log.error("", e);
				}
			}
		}
		return null;
	}
	
	@RequestMapping(value = "versionInfo", method = RequestMethod.GET)
	public Object versionInfo() {
		BufferedReader br = new BufferedReader(new InputStreamReader(Utils.getInputStream(hostname, port, username, password, pathname, versionInfo)));
		StringBuilder sb = new StringBuilder();
		String str;
		try {
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
			return sb.toString();
		} catch (IOException e) {
			log.error("", e);
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					log.error("", e);
				}
			}
		}
		return null;
	}

}
