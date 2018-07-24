package com.dc.appengine.node;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.log4j.Logger;

import com.dc.appengine.node.cache.NodeOsStatCache;
import com.dc.appengine.node.configuration.PAASConfig;
import com.dc.appengine.node.configuration.model.NodeProperties;
import com.dc.appengine.node.docker.DockerReset;
import com.dc.appengine.node.instance.snapshot.SnapshotExecutor;
import com.dc.appengine.node.message.ConsumerClient4PaaS;
import com.dc.appengine.node.message.InvokerInit;
import com.dc.appengine.node.message.MessageSenderScheduler;
import com.dc.appengine.node.message.topic.TopicInit;
import com.dc.appengine.node.preloader.PreloaderExecutor;
import com.dc.appengine.node.tcp.TcpServerStarter;
import com.dc.appengine.node.ws.client.AdapterClient;
import com.dcfs.impls.esb.ESBConfig;
import com.dcfs.impls.esb.admin.ClientHandlerReset;

public class StartNode {
	private static final String rootPath = System
			.getProperty("com.dc.install_path");
	private static Logger log = Logger.getLogger(StartNode.class);
	private static AdapterClient adapterClient = new AdapterClient();
	private static ExecutorService spool = Executors
			.newSingleThreadExecutor(new ThreadFactory() {
				private static final String name = "appengine-node-startup";

				public Thread newThread(Runnable r) {
					return new Thread(r, name);
				}

			});

	public static void start() throws Exception {

//		boolean isNew = NodeEnv.getInstance().getNodeEnvDefinition()
//				.isNewNode();
//		boolean isRegister = NodeEnv.getInstance().getNodeEnvDefinition()
//				.isRegister();
//		if (isNew) {
//			clean();
//		}
//		if (!isRegister) {
//			register();
//		}
		prepare();

	}// end start

	/**
	 * 停止、删除快照
	 * 
	 * 重置mom连接
	 * 
	 * docker ipinit dockerReset
	 * 
	 */
	private static void clean() throws InterruptedException {
		log.info("===================================================");
		log.info("cleaning node..........");
		SnapshotExecutor.getInstance().stop();
//		SnapshotExecutor.getInstance().delsnapshot();

//		ESBConfig.getConfig().reload();
		ClientHandlerReset.releaseQueueResource();
		ClientHandlerReset.resetConfig();
		if (TopicInit.client != null) {
			TopicInit.client.close();

		}

//		boolean isDocker = NodeProperties.isDocker();
//		if (isDocker) {
//			IpsInit.getInstance().initip();
//			DockerReset.getInstance().resetDocker();
//		}
		log.info("===================================================");
		log.info("finished clean node.");

	}// end clean

	/**
	 * 启动快照
	 * 
	 * mom初始化
	 * 
	 * 扫描momTask.properties文件，获得node之前停止时未完成的任务
	 * 
	 * 启动心跳
	 * 
	 **/
	private static void prepare() throws Exception {
		log.info("===================================================");
		log.info("preparing......");
		// 只注册一次
		// if (!isRegister()) {
		// adapterClient.getNodeIp();
		// }
		SnapshotExecutor.getInstance().start();
		
		ESBConfig.getConfig().reload();
		
		InvokerInit.init();
		TopicInit.init();
		MessageSenderScheduler scheder = new MessageSenderScheduler(PAASConfig
				.getConfig().getInstallRoot() + NodeConstant.NODE_MOM_TASKFILE,
				ConsumerClient4PaaS.getInstance(), 100, 1000, 3000);
		scheder.start();

//		int heartbeatPort = NodeProperties.getHeartBeatPort();
//		if (heartbeatPort > 0) {
//			TcpServerStarter.getInstance().shutdown();
//			TcpServerStarter.getInstance().startAlone();
//		}

		log.info("===================================================");
		log.info("finished to prepare");

	}// end prepare

	private static void register() {
		log.info("===================================================");
		log.info("registing to adapter......");

		// 只注册一次
		if (!isRegister()) {
			// 获取node所在机器的基本配置
			int cpu = NodeOsStatCache.getInstance().getCpuCore();
			double mem = NodeOsStatCache.getInstance().getMemory();
			double disk = NodeOsStatCache.getInstance().getDisk() / 1024;
			String dockerVersion = NodeOsStatCache.getInstance()
					.getDockerVersion();
			if (!adapterClient.preRegister((int) cpu + "", (int) mem + "",
					(int) disk + "", NodeEnv.getInstance().getOs(),
					dockerVersion, "1.0")) {
				log.error("register failed");
				System.exit(0);
			}

			// adapterClient.registerToAdapter(cpu + "", mem + "", disk + "",
			// NodeEnv.getInstance().getOs(), dockerVersion, "1.0");
			// boolean isDocker = NodeProperties.isDocker;
			// if (isDocker) {
			// int num = 1;
			// while (!CheckCadvisor.start) {
			// log.debug("start " + num + " times");
			// CheckCadvisor.check();
			// }
			// }
		}
		log.info("===================================================");
		log.info("finished to register");

	}// end register

	public static void reRegister() {

	}// end reregister

	private static boolean isRegister() {
		boolean isRegister = NodeEnv.getInstance().getNodeEnvDefinition()
				.isRegister();
		return isRegister;
	}

	public static void main(String args[]) {
		log.info("The instance of Node-Agent startup.");
		spool.execute(new Runnable() {
			public void run() {
				try {
					log.info("===================================================");
					log.info("App-Engine NodeAgent is starting......");
					ESBConfig.setRoot(rootPath);
					PAASConfig.setRoot(rootPath);
					PreloaderExecutor.getInstance().execute(null);
					start();
					log.info("===================================================");
					log.info("App-Engine NodeAgent has been started successfully.");
				} catch (Exception e) {
					log.error(e.getMessage(), e);
					System.exit(0);
				}
			}
		});
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				SnapshotExecutor.getInstance().stop();
				spool.shutdown();
				log.info("===================================================");
				log.info("The instance of Node-Agent has been shutdown.");
				log.info("===================================================");
				log.error("App-Engine NodeAgent has been shutdown successfully.");
			}
		}));
	}// end main
}
