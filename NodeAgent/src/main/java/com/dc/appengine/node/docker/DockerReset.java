package com.dc.appengine.node.docker;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.NodeEnv;
import com.dc.appengine.node.command.analyser.impl.DockerListAnalyser;
import com.dc.appengine.plugins.command.CommandGenerator;
import com.dc.appengine.plugins.command.Commands;
import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;
import com.dc.appengine.plugins.command.analyser.DefaultAnalyser;
import com.dc.appengine.plugins.command.executor.CommandWaitExecutor;

public class DockerReset {
	private static DockerReset _instatnce = new DockerReset();
	private static Logger LOG = LoggerFactory.getLogger(DockerReset.class);

	public static DockerReset getInstance() {
		return _instatnce;
	}

	public void resetDocker() throws InterruptedException {
		String command = CommandGenerator.getInstance().generate(Commands.getInstance().get("docker-psall"), false);
		AbstractAnalyser<List<String>> analyser = new DockerListAnalyser();
		CommandWaitExecutor executor = new CommandWaitExecutor(analyser);
		executor.exec(command);
		List<String> dockerList = analyser.getResult();
		executor.setAnalytic(new DefaultAnalyser());
		if (dockerList != null && dockerList.size() > 0) {
			CountDownLatch cntLatch = new CountDownLatch(dockerList.size());
			for (String dockerID : dockerList) {
				Thread thread = new Thread(new lxcDesThread(cntLatch, dockerID, executor), "lxcRest-" + dockerID);
				thread.start();
			}
			cntLatch.await();
			// 删除部署目录 部署时候还会再生成

			try {
				String deployDir = NodeEnv.getInstance().getNodeEnvDefinition().getRepository().getDeployDir();

				command = CommandGenerator.getInstance()
						.generate(Commands.getInstance().get("rm"), false, deployDir);
				executor.exec(command);
			} catch (NullPointerException nex) {
				LOG.error("cant get the deploy path ");
			}
		}
	}

	class lxcDesThread implements Runnable {
		private CountDownLatch cntLatch;
		private String dockerID;
		private CommandWaitExecutor executor;

		public lxcDesThread(CountDownLatch cntLatch, String dockerID, CommandWaitExecutor executor) {
			this.cntLatch = cntLatch;
			this.dockerID = dockerID;
			this.executor = executor;
		}

		@Override
		public void run() {

			String commands = "";

			commands = CommandGenerator.getInstance()
					.generate(Commands.getInstance().get("docker-rm"), false, dockerID);
			executor.exec(commands);
			cntLatch.countDown();
		}

	}
}
