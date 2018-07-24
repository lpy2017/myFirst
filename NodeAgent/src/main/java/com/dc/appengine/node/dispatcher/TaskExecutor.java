package com.dc.appengine.node.dispatcher;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.node.configuration.Context;
import com.dc.appengine.node.flow.DefaultNodeFlowContext;
import com.dc.appengine.node.flow.SimpleFlowEngine;
import com.dc.appengine.node.message.InnerMessage;
import com.dcfs.impls.esb.admin.ManagerFactory;
import com.dcfs.interfaces.esb.admin.IManageComponent;

/**
 * 存储实例的处理消息队列
 * 
 * @author xukqa
 */
public class TaskExecutor {

	private static Logger LOG = LoggerFactory.getLogger(TaskExecutor.class);
	private AtomicBoolean running = new AtomicBoolean(false);
	@SuppressWarnings("rawtypes")
	private BlockingQueue<Map> blockQueue = new LinkedBlockingQueue<Map>();
	private String appInstanceId;

	public TaskExecutor() {
		init();
	}

	private void init() {
		// running.set(true);
	}

	public void start() {
		running.set(true);
		TaskRunning task = new TaskRunning();
		QueueManager.pool.execute(task);

	}

	public void stop() {
		running.set(false);
	}

	public void set(String id) {
		this.appInstanceId = id;
	}

	/**
	 * 向实例队列中添加待处理的消息
	 */
	@SuppressWarnings("rawtypes")
	public void put(Map context) {
		LOG.debug("put op into " + this.appInstanceId
				+ " queue, waiting for processing...");
		blockQueue.offer(context);
	}

	/**
	 * 取出一条待处理消息，执行
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> get() {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			msg = blockQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// System.out.println("TaskExecutor the number of pool "+
		// blockQueue.size());
		return msg;
	}

	class TaskRunning implements Runnable {

		@SuppressWarnings("rawtypes")
		@Override
		public void run() {
			Map<String, Object> msg = null;
			Map<String, Object> msgOld = null;

			while (running.get()) {
				try {
					msg = get();
					if (null != msg && !msg.equals(msgOld)) {
						msgOld = msg;
						final Context context = (Context) msg.get("context");
						final Properties headers = (Properties) msg
								.get("header");

						SimpleFlowEngine.getInstance().exec("sync_request",
								context);

						// 重新组装消息，不然报 消息体非innermessage错误
						byte[] bytes = (byte[]) context.getPayload();
						Map bodyMessage = JSON.parseObject(new String(bytes));
						String content = bodyMessage.get("content").toString();
						String op = bodyMessage.get("op").toString();

						@SuppressWarnings("unchecked")
						Map<String, String> conMap = JSON.parseObject(content,
								new TypeReference<Map>() {
								});
						InnerMessage<Map<String, String>> message = new InnerMessage<Map<String, String>>();
						message.setOp(op);
						message.setContent(conMap);
						// System.out.println("=====================op ========="+
						// op);
						final Context sendContext = new DefaultNodeFlowContext();
						sendContext.setPayload(message);
						headers.remove("MXSD_REPLY_TAG");
						headers.remove("MXSD_DISTINCT_FROM");
						headers.remove("MXSD_DISTINCT_TO");
						headers.remove("MXSD_FROM");// 出错！
						headers.remove("MXSD_TO");// 出错！
						headers.setProperty("MXSD_ADMIN_SUB_TYPE",
								"NodeSendInvoker");
						headers.setProperty("MXSD_MSG_TYPE", "ADMIN_MESSAGE");
						// LOG.debug("------------header before into nodeSendInvoker: "+headers);
						IManageComponent manager = ManagerFactory.getManager();
						manager.process(
								JSONObject.toJSONString(
										sendContext.getPayload()).getBytes(),
								headers);
						if (blockQueue.isEmpty()) {// &&
													// op.equals(OpEnum.DOCKER_DELETE)){
							QueueManager.getInstance().delOperate(message);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error(e.getMessage() + " MsgQueue-thread stoped");
				}
			}
		}

	}

}
