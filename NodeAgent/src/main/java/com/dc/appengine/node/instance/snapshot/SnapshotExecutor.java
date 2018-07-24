/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.instance.snapshot;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.command.analyser.impl.SnapkillAnalyser;
import com.dc.appengine.node.configuration.Constants;
import com.dc.appengine.node.instance.InstanceModel;
import com.dc.appengine.node.instance.RuntimeInstanceRegistry;
import com.dc.appengine.node.preloader.impl.ScheduledPreloader;
import com.dc.appengine.node.scanner.ResourceFilter;
import com.dc.appengine.node.scanner.impl.FileScanner;
import com.dc.appengine.node.utils.FileUtil;
import com.dc.appengine.plugins.command.CommandGenerator;
import com.dc.appengine.plugins.command.Commands;
import com.dc.appengine.plugins.command.executor.CommandWaitExecutor;

/**
 * SnapshotExecutor.java
 * 
 * @author liubingj
 */
public class SnapshotExecutor implements Runnable {

	private static Logger LOG = LoggerFactory.getLogger(SnapshotExecutor.class);

	private static final String PATH = "snapshot/autobak/";

	private static final SnapshotExecutor _instance = new SnapshotExecutor();

	private AtomicBoolean running = new AtomicBoolean(false);

	private JAXBContext context = null;
	private ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors
			.newFixedThreadPool(200);

	private ExecutorService single = Executors
			.newSingleThreadExecutor(new ThreadFactory() {
				public Thread newThread(Runnable r) {
					return new Thread(r, "Snapshot-Thread");
				}
			});

	private SnapshotExecutor() {
	}

	public static SnapshotExecutor getInstance() {
		return _instance;
	}

	public void start() {
		if (running.get()) {
			return;
		}
		running.set(true);
		final StringBuffer buff = new StringBuffer();
		buff.append(InstanceModel.class.getPackage().getName());
		buff.append(":");
		buff.append(InstanceModel.class.getPackage().getName());

		try {
			context = JAXBContext.newInstance(buff.toString());
		} catch (JAXBException e) {
			LOG.error(e.getMessage(), e);
		}
		resume();
		if (single.isShutdown()) {
			LOG.debug("snapshot executor is shutdown:" + single.isShutdown());
			single = Executors.newSingleThreadExecutor(new ThreadFactory() {
				public Thread newThread(Runnable r) {
					return new Thread(r, "Snapshot-Thread");
				}
			});
		}
		if (pool.isShutdown()) {
			pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(200);
		}
		single.execute(_instance);
	}

	@Override
	public void run() {
		while (running.get()) {
			try {
				SnapshotParam param = Provider.getInstance().get();
				if (param != null) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Current snapshot is "
								.concat(param.getName()).concat(", op is ")
								.concat(param.getOp().name()));
					}
					pool.execute(new SnapshotTask(param));
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error(e.getMessage() + "snapshot thread stopped");
			}
		}
	}

	private void resume() {
		// TODO Auto-generated method stub
		LOG.debug("resume");
	}

	public void delsnapshot() {
		SnapkillAnalyser stopanalyser = new SnapkillAnalyser();
		final String command = CommandGenerator.getInstance().generate(
				Commands.getInstance().get("snap-kill"), false);
		CommandWaitExecutor stopexecutor = new CommandWaitExecutor(stopanalyser);
		stopexecutor.exec(command);

		final FileScanner scanner = new FileScanner();
		final ResourceFilter filter = new ResourceFilter() {

			private List<File> files = new LinkedList<File>();

			public <T> void doFilter(T t) throws Exception {
				if (!(t instanceof File)) {
					throw new IllegalArgumentException(
							"The argument must be an instance of java.io.File");
				}
				File file = (File) t;
				if (file.getName().endsWith(".snapshot")) {
					files.add(file);
				}
			}

			@SuppressWarnings("unchecked")
			public <T> T getResult() {
				return (T) files;
			}
		};
		try {
			File root = FileUtil.getInstance().getFile(PATH,
					Constants.Env.BASE_CONF);
			scanner.scan(root, filter);
			List<File> files = filter.getResult();
			if (files != null) {
				for (File file : files) {
					file.delete();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void stop() {
		if (!running.get()) {
			return;
		}
		running.set(false);
		context = null;
		pool.shutdown();
		single.shutdownNow();
		ScheduledPreloader.getInstance().cancel();
		while (!single.isTerminated()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				LOG.debug("snapshot executor has been stopped!");
			}
		}
		LOG.debug("snap-shot thread :" + single.isTerminated());
	}

	private class SnapshotTask implements Runnable {

		private SnapshotParam param;

		SnapshotTask(SnapshotParam param) {
			this.param = param;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {

			if (param.getOp().equals(Op.ADD)) {
				update(0);
			}

			if (param.getOp().equals(Op.UPDATE)) {
				update(1);
			}
			if (param.getOp().equals(Op.DELETE)) {
				delete();
			}
		}

		private void delete() {
			try {
				final File file = FileUtil.getInstance().getFile(
						PATH.concat(param.getName()).concat(".snapshot"),
						Constants.Env.BASE_CONF);
				if (file.exists()) {
					file.delete();
				} else {
					LOG.error("the file:" + file.getName() + " is not exists");
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}

		private void update(int flag) {

			try {

				final InstanceModel instanceModel = RuntimeInstanceRegistry
						.getInstance().get(param.getName());
				synchronized (instanceModel) {
					final File file = FileUtil.getInstance().getFile(
							PATH.concat(param.getName()).concat(".snapshot"),
							Constants.Env.BASE_CONF);
					if (!file.getParentFile().exists()) {
						file.getParentFile().mkdirs();
					}

					if (flag == 0 || (flag == 1 && file.exists())) {
						// if (!instanceModel.getModel().getNeuopstat()
						// .equals(NeuStateConst.NEU_INIT)) {
						context.createMarshaller().marshal(instanceModel, file);
						// } else {
						// LOG.error("wrong neuopstat do not update the snap");
						// }
					}
				}
				LOG.debug("the file:" + param.getName() + " is ready");
			} catch (JAXBException e) {
				LOG.error(e.getMessage(), e);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}

	}

	public boolean isRunning() {
		return running.get();
	}

	public void setRunning(boolean running) {
		this.running.set(running);
	}
}
