package com.dc.appengine.appmaster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.servlet.Filter;

import org.apache.zookeeper.server.quorum.QuorumPeerMain;
import org.jasypt.intf.service.JasyptStatelessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.dc.appengine.appmaster.entity.SystemENV;
import com.dc.appengine.appmaster.filter.SessionFilter;
import com.dc.appengine.appmaster.quartz.QuartzManager;
import com.dc.appengine.appmaster.quartz.application.QuartzManager4App;
import com.dc.appengine.appmaster.utils.LicenceUtil;

@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@EnableRedisHttpSession
@ComponentScan(basePackages = "com.dc.appengine.appmaster")
public class AppMaster extends SpringBootServletInitializer implements ApplicationListener<ApplicationEvent> {
	private static final Logger log = LoggerFactory
			.getLogger(AppMaster.class);
	@Bean
	public static ConfigureRedisAction configureRedisAction() {
	    return ConfigureRedisAction.NO_OP;
	}
	@Bean
	public DefaultCookieSerializer defaultCookieSerializer(){
	    DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
	    defaultCookieSerializer.setCookiePath("/");
	    return defaultCookieSerializer;
	}
	@Bean
	public FilterRegistrationBean sessionFilterRegistration() {

		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(sessionFilter());
		registration.addUrlPatterns("/*");
		// registration.addInitParameter("paramName", "paramValue");
		registration.setName("sessionFilter");
		registration.setOrder(1);
		return registration;
	}

	@Bean(name = "sessionFilter")
	public Filter sessionFilter() {
		return new SessionFilter();
	}
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AppMaster.class);
    }
    public static void main(String[] args) throws Exception {
    	encryptApplicationProPerties();
    	//校验licence
    	LicenceUtil.scheduledTask4Licence();
    	if (System.getProperty("zoo.cfg.path") != null) {
    		Thread thread = new Thread(new Runnable() {
    			
    			@Override
    			public void run() {
    				QuorumPeerMain.main(new String[] {System.getProperty("zoo.cfg.path")});
    			}
    		});
    		thread.start();
		}
        SpringApplication.run(AppMaster.class, args);
        QuartzManager.start();
//        QuartzManager4App.start();
    }
    
	private static void encryptApplicationProPerties() throws Exception {
		System.setProperty("jasypt.encryptor.password", "smartcd");
		String file = System.getProperty("spring.config.location");
		File config = null;
		if(file == null){
			//本地启动
			String path = AppMaster.class.getProtectionDomain().getCodeSource().getLocation().toURI().getSchemeSpecificPart();
			config = new File(path + File.separator + "application.properties");
		}
		else{
			//服务器启动
			config = new File(file);
		}
		if(!config.exists()){
			throw new Exception("master配置文件[" + config.getAbsolutePath() + "] not exists!");
		}
		StringBuilder sb = new StringBuilder();
		boolean needEncrypt = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(config), "UTF-8"));
		String line = null;
		while((line = br.readLine()) != null){
			if(!line.startsWith("#") && line.indexOf("=") != -1){
				String[] kv = line.split("=");
				String key = kv[0];
				String value = kv[1];
				if(value.startsWith("DEC(")){
					needEncrypt = true;
					value = value.substring(4, value.length() - 1);
					JasyptStatelessService service = new JasyptStatelessService();
					value = service.encrypt(value, System.getProperty("jasypt.encryptor.password"), null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
					sb.append(key + "=ENC(" + value + ")");
					sb.append(System.getProperty("line.separator"));
					continue;
				}
			}
			sb.append(line);
			sb.append(System.getProperty("line.separator"));
		}
		if(needEncrypt){
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(config), "UTF-8"));
			bw.write(sb.toString());
			bw.flush();
			bw.close();
		}
		else{
			sb.delete(0, sb.length());
		}
		br.close();
	}
	
	@Autowired
    private RedisOperationsSessionRepository redisOperation;
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
            redisOperation.setDefaultMaxInactiveInterval(SystemENV.sessionTimeout);
        }
	}
	
//	@Bean
//	MultipartConfigElement multipartConfigElement() {
//		MultipartConfigFactory factory = new MultipartConfigFactory();
//		factory.setLocation(SystemENV.uploadFileTempPath);
//		return factory.createMultipartConfig();
//	}
}