/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.instance.snapshot;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 交换通道类
 * <p>
 * 定义了用于数据交换的内存队列IN与OUT
 * </p>
 * 
 * @author liubingj
 */
public class ExchangeChannel {

	private static Logger LOG = LoggerFactory.getLogger(ExchangeChannel.class);

	private static volatile ExchangeChannel _instance;

	private BlockingQueue<SnapshotParam> queue = new LinkedBlockingQueue<SnapshotParam>();

	private ExchangeChannel() {
	}

	public static ExchangeChannel getInstance() {
		synchronized (ExchangeChannel.class) {
			if (_instance == null) {
				_instance = new ExchangeChannel();
			}
		}
		return _instance;
	}

	public void put(SnapshotParam param) {
		if (SnapshotExecutor.getInstance().isRunning()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Put the SnapshotParam " + param.getOp().name() + " ["
						+ param.getName() + "] to queue.");
			}
			queue.offer(param);
		}
	}

	public SnapshotParam get() {
		SnapshotParam param = null;
		try {
			if (SnapshotExecutor.getInstance().isRunning()) {
				param = queue.take();
			}
		} catch (InterruptedException e) {
			LOG.error("the snapshot has been stopped");
		}
		return param;
	}

}
