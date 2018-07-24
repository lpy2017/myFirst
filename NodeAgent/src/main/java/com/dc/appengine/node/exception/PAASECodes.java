package com.dc.appengine.node.exception;

import com.dc.esb.container.exception.CodeDesc;

public class PAASECodes {

	@CodeDesc(code = "PAAS-E-NA-000000", description = "PAAS-NODEAGENT调用出现未知异常")
	public static final String CODE_NA_E000000 = "PAAS-E-NA-000000";

	// NodeAgent 启动错误
	@CodeDesc(code = "PAAS-E-NA-000100", description = "PAAS-NODEAGENT启动出现未知异常")
	public static final String CODE_NA_E000100 = "PAAS-E-NA-000100";

	@CodeDesc(code = "PAAS-E-NA-000101", description = "端口参数异常")
	public static final String CODE_NA_E000101 = "PAAS-E-NA-000101";
	@CodeDesc(code = "PAAS-E-NA-000102", description = "LXC参数异常")
	public static final String CODE_NA_E000102 = "PAAS-E-NA-000102";
	@CodeDesc(code = "PAAS-E-NA-000103", description = "config初始参数异常")
	public static final String CODE_NA_E000103 = "PAAS-E-NA-000103";
	@CodeDesc(code = "PAAS-E-NA-000104", description = "master数据下发连接异常")
	public static final String CODE_NA_E000104 = "PAAS-E-NA-000104";
	@CodeDesc(code = "PAAS-E-NA-000105", description = "master数据下发参数内容缺失")
	public static final String CODE_NA_E000105 = "PAAS-E-NA-000105";
	@CodeDesc(code = "PAAS-E-NA-000106", description = "master数据下发参数mom地址错误")
	public static final String CODE_NA_E000106 = "PAAS-E-NA-000106";
	@CodeDesc(code = "PAAS-E-NA-000107", description = "下发地址非uri标准格式")
	public static final String CODE_NA_E000107 = "PAAS-E-NA-000107";
	@CodeDesc(code = "PAAS-E-NA-000120", description = "无法获取下发的配置文件")
	public static final String CODE_NA_E000120 = "PAAS-E-NA-000120";
	@CodeDesc(code = "PAAS-E-NA-000121", description = "无法加载下发的配置文件,检查config或重新下发配置")
	public static final String CODE_NA_E000121 = "PAAS-E-NA-000121";
	// NodeAgent LXC错误
	@CodeDesc(code = "PAAS-E-NA-000200", description = "PAAS-NODEAGENT调用LXC  出现未知异常")
	public static final String CODE_NA_E000200 = "PAAS-E-NA-000200";
	@CodeDesc(code = "PAAS-E-NA-000201", description = "LXC 启动异常")
	public static final String CODE_NA_E000201 = "PAAS-E-NA-000201";
	@CodeDesc(code = "PAAS-E-NA-000202", description = "调用了已启动的LXC")
	public static final String CODE_NA_E000202 = "PAAS-E-NA-000202";
	@CodeDesc(code = "PAAS-E-NA-000203", description = "LXC ssh连接超时")
	public static final String CODE_NA_E000203 = "PAAS-E-NA-000203";
	@CodeDesc(code = "PAAS-E-NA-000204", description = "LXC 用户名/密码错误")
	public static final String CODE_NA_E000204 = "PAAS-E-NA-000204";
	@CodeDesc(code = "PAAS-E-NA-000205", description = "LXC 停止异常")
	public static final String CODE_NA_E000205 = "PAAS-E-NA-000205";
	@CodeDesc(code = "PAAS-E-NA-000206", description = "LXC 容器不存在")
	public static final String CODE_NA_E000206 = "PAAS-E-NA-000206";
	@CodeDesc(code = "PAAS-E-NA-000207", description = "LXC 容器内生成自启动文件失败")
	public static final String CODE_NA_E000207 = "PAAS-E-NA-000207";
	@CodeDesc(code = "PAAS-E-NA-000208", description = "LXC 容器却没提供至少route的对外端口开放")
	public static final String CODE_NA_E000208 = "PAAS-E-NA-000208";
	@CodeDesc(code = "PAAS-E-NA-000209", description = "Docker镜像为空或错误")
	public static final String CODE_NA_E000209 = "PAAS-E-NA-000209";
	@CodeDesc(code = "PAAS-E-NA-000210", description = "Docker容器创建失败")
	public static final String CODE_NA_E000210 = "PAAS-E-NA-000210";
	@CodeDesc(code = "PAAS-E-NA-000211", description = "Docker容器IP地址与网桥IP地址不在同一网段")
	public static final String CODE_NA_E000211 = "PAAS-E-NA-000211";
	// NodeAgent 应用/服务 错误
	@CodeDesc(code = "PAAS-E-NA-000300", description = "PAAS-NODEAGENT 调用应用/服务 未知异常")
	public static final String CODE_NA_E000300 = "PAAS-E-NA-000300";
	
	@CodeDesc(code = "PAAS-E-NA-000301", description = "没有足够的端口")
	public static final String CODE_NA_E000301 = "PAAS-E-NA-000301";
	
	@CodeDesc(code = "PAAS-E-NA-000302", description = "PAAS-NODEAGENT 应用/服务 部署未知异常")
	public static final String CODE_NA_E000302 = "PAAS-E-NA-000302";
	
	@CodeDesc(code = "PAAS-E-NA-000303", description = "PAAS-NODEAGENT 应用/服务 启动未知异常")
	public static final String CODE_NA_E000303 = "PAAS-E-NA-000303";
	
	@CodeDesc(code = "PAAS-E-NA-000304", description = "没有足够的ip")
	public static final String CODE_NA_E000304 = "PAAS-E-NA-000304";
	
	@CodeDesc(code = "PAAS-E-NA-000305", description = "容器类型为空或错误")
	public static final String CODE_NA_E000305 = "PAAS-E-NA-000305";
	
	@CodeDesc(code = "PAAS-E-NA-000306", description = "No such neu named by")
	public static final String CODE_NA_E000306 = "PAAS-E-NA-000306";

	@CodeDesc(code = "PAAS-E-NA-000310", description = "环境部署异常 ")
	public static final String CODE_NA_E000310 = "PAAS-E-NA-000310";
	@CodeDesc(code = "PAAS-E-NA-000311", description = " 连接异常")
	public static final String CODE_NA_E000311 = "PAAS-E-NA-000311";
	@CodeDesc(code = "PAAS-E-NA-000312", description = "无法获得根目录下rt文件")
	public static final String CODE_NA_E000312 = "PAAS-E-NA-000312";
	@CodeDesc(code = "PAAS-E-NA-000313", description = "rt文件下载失败")
	public static final String CODE_NA_E000313 = "PAAS-E-NA-000313";
	@CodeDesc(code = "PAAS-E-NA-000314", description = "UNZIPrt文件失败")
	public static final String CODE_NA_E000314 = "PAAS-E-NA-000314";
	@CodeDesc(code = "PAAS-E-NA-000315", description = "deploy.xml 设置错误")
	public static final String CODE_NA_E000315 = "PAAS-E-NA-000316";
	@CodeDesc(code = "PAAS-E-NA-000316", description = "已部署成功不能再次部署")
	public static final String CODE_NA_E000316 = "PAAS-E-NA-000316";
	@CodeDesc(code = "PAAS-E-NA-000320", description = "启动环境异常 ")
	public static final String CODE_NA_E000320 = "PAAS-E-NA-000320";
	@CodeDesc(code = "PAAS-E-NA-000321", description = "启动环境未设置JAVA_HOME")
	public static final String CODE_NA_E000321 = "PAAS-E-NA-000321";
	@CodeDesc(code = "PAAS-E-NA-000322", description = "启动环境找不到启动文件")
	public static final String CODE_NA_E000322 = "PAAS-E-NA-000322";
	@CodeDesc(code = "PAAS-E-NA-000323", description = "端口占用JVM_BIND")
	public static final String CODE_NA_E000323 = "PAAS-E-NA-000323";
	@CodeDesc(code = "PAAS-E-NA-000324", description = "应用/服务启动中还未开放服务端口")
	public static final String CODE_NA_E000324 = "PAAS-E-NA-000324";
	@CodeDesc(code = "PAAS-E-NA-000330", description = "已启动应用/服务意外停止")
	public static final String CODE_NA_E000330 = "PAAS-E-NA-000330";
	@CodeDesc(code = "PAAS-E-NA-000340", description = "停止应用/服务异常")
	public static final String CODE_NA_E000340 = "PAAS-E-NA-000340";
	@CodeDesc(code = "PAAS-E-NA-000350", description = "卸载应用/服务异常")
	public static final String CODE_NA_E000350 = "PAAS-E-NA-000350";
	@CodeDesc(code = "PAAS-E-NA-000351", description = "应用或服务正在运行,卸载异常")
	public static final String CODE_NA_E000351 = "PAAS-E-NA-000351";
	//处理超时
	@CodeDesc(code = "PAAS-E-NA-000410", description = "部署实例超时")
	public static final String CODE_NA_E000410 = "PAAS-E-NA-000410";
	@CodeDesc(code = "PAAS-E-NA-000420", description = "启动实例超时")
	public static final String CODE_NA_E000420 = "PAAS-E-NA-000420";
	@CodeDesc(code = "PAAS-E-NA-000430", description = "停止实例超时")
	public static final String CODE_NA_E000430 = "PAAS-E-NA-000430";
	@CodeDesc(code = "PAAS-E-NA-000440", description = "删除实例超时")
	public static final String CODE_NA_E000440 = "PAAS-E-NA-000440";
	@CodeDesc(code = "PAAS-E-NA-000355", description = "热部署备份失败")
	public static final String CODE_NA_E000355 = "PAAS-E-NA-000355";
	@CodeDesc(code = "PAAS-E-NA-000356", description = "热部署拷贝失败")
	public static final String CODE_NA_E000356 = "PAAS-E-NA-000356";
	
	
	// NodeAgent Docker错误
	@CodeDesc(code = "PAAS-E-NA-000510", description = "PAAS-NODEAGENT创建Docker容器出现未知异常")
	public static final String CODE_NA_E000510 = "PAAS-E-NA-000510";
	@CodeDesc(code = "PAAS-E-NA-000511", description = "PAAS-NODEAGENT删除容器calico网络失败")
	public static final String CODE_NA_E000511 = "PAAS-E-NA-000511";
	@CodeDesc(code = "PAAS-E-NA-000512", description = "Docker容器加入一级负载失败")
	public static final String CODE_NA_E000512 = "PAAS-E-NA-000512";
	@CodeDesc(code = "PAAS-E-NA-000513", description = "Docker容器到外部ip添加映射失败")
	public static final String CODE_NA_E000513 = "PAAS-E-NA-000513";
	@CodeDesc(code = "PAAS-E-NA-000514", description = "Docker容器到外部ip删除映射失败")
	public static final String CODE_NA_E000514 = "PAAS-E-NA-000514";
	
	//Router调用异常
	@CodeDesc(code = "PAAS-E-ROUTER-000100", description = "PAAS-ROUTER调用出现未知异常")
	public static final String CODE_ROUTER_E000100 = "PAAS-E-ROUTER-000100";
	@CodeDesc(code = "PAAS-E-ROUTER-000101", description = "无法取得appid")
	public static final String CODE_ROUTER_E000101 = "PAAS-E-ROUTER-000101";
	@CodeDesc(code = "PAAS-E-ROUTER-000102", description = "负载均衡没有找到instanceid")
	public static final String CODE_ROUTER_E000102 = "PAAS-E-ROUTER-000102";
	@CodeDesc(code = "PAAS-E-ROUTER-000103", description = "找不到appid对应的路由")
	public static final String CODE_ROUTER_E000103 = "PAAS-E-ROUTER-000103";
	@CodeDesc(code = "PAAS-E-ROUTER-000104", description = "接出链接获取超时")
	public static final String CODE_ROUTER_E000104 = "PAAS-E-ROUTER-000104";
	@CodeDesc(code = "PAAS-E-ROUTER-000105", description = "后端无响应")
	public static final String CODE_ROUTER_E000105 = "PAAS-E-ROUTER-000105";
	@CodeDesc(code = "PAAS-E-ROUTER-000106", description = "应用/服务不在时间有效期内")
	public static final String CODE_ROUTER_E000106 = "PAAS-E-ROUTER-000106";
	@CodeDesc(code = "PAAS-E-ROUTER-000107", description = "应用/服务当前不允许访问")
	public static final String CODE_ROUTER_E000107 = "PAAS-E-ROUTER-000107";
	//Master 异常
	// successful response
	@CodeDesc(code ="PAAS-E-MASTER-000200", description="成功响应")
	public static final String CODE_MASTER_E000200 = "PAAS-E-MASTER-000200";
	// generating rt file failed
	@CodeDesc(code ="PAAS-E-MASTER-000300", description="创建部署包失败")
	public static final String CODE_MASTER_E000300 = "PAAS-E-MASTER-000300";
	// app mapping resource failed
	@CodeDesc(code ="PAAS-E-MASTER-000301", description="应用关联资源失败")
	public static final String CODE_MASTER_E000301 = "PAAS-E-MASTER-000301";
	// no app resource found
	@CodeDesc(code ="PAAS-E-MASTER-000302", description="未找到应用关联的资源")
	public static final String CODE_MASTER_E000302 = "PAAS-E-MASTER-000302";
	// no resource op timeout params found
	@CodeDesc(code ="PAAS-E-MASTER-000303", description="应用关联的资源没有操作超时信息")
	public static final String CODE_MASTER_E000303 = "PAAS-E-MASTER-000303";
	// returning resources failed
	@CodeDesc(code ="PAAS-E-MASTER-000304", description="返回资源信息失败")
	public static final String CODE_MASTER_E000304 ="PAAS-E-MASTER-000304";
	// saving strategy failed
	@CodeDesc(code ="PAAS-E-MASTER-000400", description="保存应用策略失败")
	public static final String CODE_MASTER_E000400 = "PAAS-E-MASTER-000400";
	// no deploy strategy found
	@CodeDesc(code ="PAAS-E-MASTER-000401", description="未找到应用部署策略")
	public static final String CODE_MASTER_E000401 = "PAAS-E-MASTER-000401";
	// app not found
	@CodeDesc(code ="PAAS-E-MASTER-000500", description="未找到应用")
	public static final String CODE_MASTER_E000500 = "PAAS-E-MASTER-000500";
	// deploying app failed, app is not free
	@CodeDesc(code ="PAAS-E-MASTER-000600", description="应用不是free状态")
	public static final String CODE_MASTER_E000600 = "PAAS-E-MASTER-000600";
	// deploying app failed, exception occured
	@CodeDesc(code ="PAAS-E-MASTER-000601", description="部署发生异常")
	public static final String CODE_MASTER_E000601 = "PAAS-E-MASTER-000601";
	// deploying app failed, no node to use
	@CodeDesc(code ="PAAS-E-MASTER-000700", description="没有可用节点")
	public static final String CODE_MASTER_E000700 = "PAAS-E-MASTER-000700";
	// deploying app failed, no docker ip to use
	@CodeDesc(code ="PAAS-E-MASTER-000701", description="没有可用的docker ip")
	public static final String CODE_MASTER_E000701 = "PAAS-E-MASTER-000701";
	// deploying app failed, creating instance failed
	@CodeDesc(code ="PAAS-E-MASTER-000702", description="无法保存实例")
	public static final String CODE_MASTER_E000702 = "PAAS-E-MASTER-000702";
	// deploying app failed, op timeout
	@CodeDesc(code ="PAAS-E-MASTER-000703", description="部署或启动超时")
	public static final String CODE_MASTER_E000703 = "PAAS-E-MASTER-000703";
	
	
	//cache异常
	@CodeDesc(code ="PAAS-E-CACHE-000000", description="success.")
	public static final String CODE_CACHE_E000000 ="PAAS-E-CACHE-000000";
	
	@CodeDesc(code ="PAAS-E-CACHE-001001", description="connect cache instance failed.")
	public static final String CODE_CACHE_E001001 ="PAAS-E-CACHE-001001";
	
	@CodeDesc(code ="PAAS-E-CACHE-001002", description="illegal op.")
	public static final String CODE_CACHE_E001002 ="PAAS-E-CACHE-001002";
	
	//基础服务异常
	@CodeDesc(code ="PAAS-E-BASESRV-000000", description="success.")
	public static final String CODE_BASESRV_E000000 ="PAAS-E-CACHE-000000";
	
	@CodeDesc(code ="PAAS-E-MYSQL-001001", description="operater failed.")
	public static final String CODE_BASESRV_E001001 ="PAAS-E-BASESRV-001001";
	
	@CodeDesc(code ="PAAS-E-PORT-001001", description="add port failed.")
	public static final String CODE_PORT_E001001 ="PAAS-E-PORT-001001";
	
	@CodeDesc(code ="PAAS-E-PORT-001002", description="delete port failed.")
	public static final String CODE_PORT_E001002 ="PAAS-E-PORT-001002";

}
