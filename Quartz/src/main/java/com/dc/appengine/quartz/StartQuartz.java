package com.dc.appengine.quartz;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.dc.appengine.quartz.entity.SystemENV;

@SpringBootApplication
@EnableRedisHttpSession
@ComponentScan(basePackages="com.dc")
public class StartQuartz extends SpringBootServletInitializer implements ApplicationListener<ApplicationEvent> {
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(StartQuartz.class);
    }
	@Bean
	public DefaultCookieSerializer defaultCookieSerializer(){
	    DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
	    defaultCookieSerializer.setCookiePath("/");
	    return defaultCookieSerializer;
	}
    public static void main(String[] args) throws Exception {
        SpringApplication.run(StartQuartz.class, args);
     // 获取Scheduler实例
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
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