package com.dc.appengine.cloudui.main;

import javax.servlet.Filter;

import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
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

import com.dc.appengine.cloudui.entity.SystemENV;
import com.dc.appengine.cloudui.filter.SessionFilter;
import com.dc.appengine.cloudui.jersey.JerseyConfig;

@SpringBootApplication
//@Configuration
@EnableRedisHttpSession
@ComponentScan(basePackages = "com.dc")
public class Main extends SpringBootServletInitializer implements ApplicationListener<ApplicationEvent> {

	private static Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws Exception {
		log.info("cloudui starting");
		new Main().configure(new SpringApplicationBuilder(Main.class)).run(args);
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
	@Bean
    public ServletRegistrationBean jerseyServlet() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), "/ws/*");
        registration.addInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, JerseyConfig.class.getName());
        registration.setName("jerseyServlet");
        return registration;
    }
	
	@Bean
	public static ConfigureRedisAction configureRedisAction() {
	    return ConfigureRedisAction.NO_OP;
	}

	@Autowired
    private RedisOperationsSessionRepository redisOperation;
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {
            redisOperation.setDefaultMaxInactiveInterval(SystemENV.sessionTimeout);
        }
	}
}
