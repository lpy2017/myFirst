package com.dc.appengine.node.configuration;

import javax.naming.Context;

/**
 * 配置类接口
 * 
 * @author jiangchuan
 *
 */
public interface IPAASConfig {

	/**
	 * Defines the excutor thread pool for Router or ConsumerClient
	 */
	public static final String ROUTER_THREAD_POOL = "ROUTER_THREAD_POOL";
	/**
	 * 连接控制器的实现类配置。
	 */
	public static final String CONNECTION_CONTROL_IMPL = "connection_control_impl";

	/**
	 * Defines whether the security module activating
	 */
	public static final String IS_ENCRYPT = "encrypt";

	/**
	 * 使用bvm进行监控的开关
	 */
	public static final String BVM_SWITCH = "BVM_SWITCH";

	public static final String BVM_IMPL = "bvm-processor-impl";

	/**
	 * Intall root , should be a folder which containes a folder named "conf",
	 * all the configuration files will been included in the folder named "conf"
	 */
	public static final String INSTALL_ROOT = "install_root";

	/**
	 * @see Context#SECURITY_AUTHENTICATION
	 */
	public static final String SECURITY_AUTHENTICATION = Context.SECURITY_AUTHENTICATION;
	/**
	 * added by duan.yongjian<br>
	 * 2010-03-02<br>
	 * chongqing one second
	 */
	public static final String MQ_POLLINGINTERVAL = "pollingInterval";

	/**
	 * @see Context#SECURITY_PRINCIPAL
	 */
	public static final String SECURITY_PRINCIPAL = Context.SECURITY_PRINCIPAL;

	/**
	 * 
	 */
	public static final String MAX_SESSIONS = "MAX_SESSIONS";

	/**
	 * if there is an exception raise on a JMSconnection,the connection will
	 * rebuild at "SLEEP_TIME" later;
	 */
	public static final String SLEEP_TIME = "SLEEP_TIME";

	/**
	 * 流控下发令牌的超时时间
	 */
	public static final String FLOWCONTROL_TIMEOUT = "FLOWCONTROL_TIMEOUT";	
	/**
	 * Define the type of the Node
	 */
	public static final String NODE_TYPE = "node.type";

	/**
	 * implement of executor pool which specified for provider
	 */
	public static final String THREAD_POOL_IMPL = "thread_pool_impl";

	/**
	 * size of default executor pool in provider
	 */
	public static final String THREAD_POOL_SIZE = "thread_pool_size";

	/**
	 * Define LogInvokerService switch, off default;
	 */
	public static final String LOGINVOKER_SWITCH = "journal_switch";

	public static final String F5_PHYSICAL_SERVER_COUNT = "f5_physical_server_count";

	public static final String MESSAGE_BUFFER_SIZE = "message_buffer_size";

	/**
	 * 定义客户端接受错误消息和超时消息的IClientMessageListener的实现类
	 */
	public static final String CLIENT_MESSAGE_LISTENER = "com.dcfs.impls.esb.client.IClientMessageListener.impl";

	//
	/**
	 * 节点类型:ESB
	 */
	int NODE_ESB = 4;

	/**
	 * 节点类型:Consumer
	 */
	int NODE_CONSUMER = 1;

	/**
	 * 节点类型:Provider
	 */
	int NODE_PROVIDER = 2;

	/**
	 * 节点类型:Consumer+Provider
	 */
	int NODE_CP = 3;

	/**
	 * 节点类型:console
	 */
	int NODE_CONSOLE = -1;
	
	/**
	 * 节点类型:Admin
	 */
	int NODE_ADMIN = 5;

	public String getProperty(String name);

	public String getInstallRoot();

	/**
	 * 
	 * return whether use the security module for the communications
	 * 
	 * @return boolean
	 */
	public boolean isEncrypt();

	/**
	 * 
	 * Obtains the max size of the Session.
	 * 
	 * @return int
	 */
	public int getMaxSessions();

	/**
	 * 
	 * return the interval of the jmsconnection will rebuild.(Listener)
	 * 
	 * @return int
	 */
	public int getSleepTime();

	/**
	 * 
	 * obtains the node type <BR>
	 * <B>1=consumer;2=provider;3=consumer+Provider;4=ESB; 5=Admin</B>
	 * 
	 * @return int type
	 */
	public int getNodeType();

	/**
	 * 
	 * @return LogInvoker is enable or not
	 * 
	 * <P>
	 * modified by wu.bo 2009-3-5.Reactor from"LogInvokerIsEnable" to
	 * "isLogInvokerEnable"
	 * </p>
	 */
	public boolean isLogInvokerEnable();

	// add by wu.bo 2009-05-04
	/**
	 * the configurations can be modified by the Monitor, then the
	 * configurations should be reload after call this method.
	 * 
	 */
	public void reload();

	/**
	 * 获取console给流控下发令牌的超时时间
	 */
	public long getFlowControlTimeOut();
}
