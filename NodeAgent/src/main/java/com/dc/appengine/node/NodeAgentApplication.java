package com.dc.appengine.node;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import com.dc.appengine.node.configuration.PAASConfig;
import com.dc.appengine.node.preloader.PreloaderExecutor;
import com.dcfs.impls.esb.ESBConfig;

@SpringBootApplication
@ComponentScan(basePackages = "com.dc")
public class NodeAgentApplication extends SpringBootServletInitializer {
	private static final String rootPath = System
			.getProperty("com.dc.install_path");
	private static Logger log = Logger.getLogger(NodeAgentApplication.class);

	public static void main(final String[] args) throws Exception {
		SpringApplication springApplication = new SpringApplication(
				NodeAgentApplication.class);
		springApplication.run(args);
		try {
			log.info("===================================================");
			log.info("App-Engine NodeAgent is starting......");
			ESBConfig.setRoot(rootPath);
			PAASConfig.setRoot(rootPath);

			PreloaderExecutor.getInstance().execute(null);
			StartNode.start();
			log.info("===================================================");
			log.info("App-Engine NodeAgent has been started successfully.");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.info("===================================================");
			log.info("The instance of Node-Agent has been shutdown.");
			log.info("===================================================");
			log.error("App-Engine NodeAgent has been shutdown successfully.");
			System.exit(0);
		}
	}
}
