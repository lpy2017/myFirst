package com.dc.appengine.appmaster.service.impl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.swing.InputMap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.appmaster.constants.Constants;
import com.dc.appengine.appmaster.dao.IApplicationDao;
import com.dc.appengine.appmaster.dao.IBlueprintDao;
import com.dc.appengine.appmaster.dao.IBlueprintTemplateDao;
import com.dc.appengine.appmaster.dao.IInstanceDao;
import com.dc.appengine.appmaster.dao.IResourceDao;
import com.dc.appengine.appmaster.dao.ITemplateDao;
import com.dc.appengine.appmaster.dao.impl.BluePrintTypeDao;
import com.dc.appengine.appmaster.dao.impl.ComponentUpdateDAO;
import com.dc.appengine.appmaster.dao.impl.LogDao;
import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.BluePrint;
import com.dc.appengine.appmaster.entity.BluePrintType;
import com.dc.appengine.appmaster.entity.Element;
import com.dc.appengine.appmaster.entity.Element.Version;
import com.dc.appengine.appmaster.entity.Instance;
import com.dc.appengine.appmaster.entity.Node;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.entity.StartBluePrint;
import com.dc.appengine.appmaster.entity.VersionFtl;
import com.dc.appengine.appmaster.service.IApplicationService;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.IInstanceService;
import com.dc.appengine.appmaster.service.INodeService;
import com.dc.appengine.appmaster.service.IResourceService;
import com.dc.appengine.appmaster.utils.AESUtil;
import com.dc.appengine.appmaster.utils.CreateComponentFile;
import com.dc.appengine.appmaster.utils.FileUtil;
import com.dc.appengine.appmaster.utils.FormatUtil;
import com.dc.appengine.appmaster.utils.FtpUtils;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.PatternUtil;
import com.dc.appengine.appmaster.utils.RequestClient;
import com.dc.appengine.appmaster.utils.SensitiveDataUtil;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dc.appengine.appmaster.utils.Utils;
import com.dc.appengine.appmaster.ws.client.service.IAdapterService;
import com.dc.sdk.SDKUtil;

@Service("blueprintService")
public class BlueprintService implements IBlueprintService {
	private static final Logger log = LoggerFactory
			.getLogger(BlueprintService.class);
	private static final HttpComponentsClientHttpRequestFactory httpRequestFactory;

	private static final ConcurrentHashMap<String,String> locks = new ConcurrentHashMap<>();
	private static final ConcurrentHashMap<String,AtomicInteger> records = new ConcurrentHashMap<>();
	public static final Collection<String> COMMONFLOW;

	public static  Pattern p2 = Pattern.compile(PatternUtil.P2);
	public static  Pattern p10 = Pattern.compile(PatternUtil.P10);
	
	public static String RESOURCE_POOL_ADD = "PADD";
	public static String RESOURCE_POOL_DELETE = "PDELETE";
	public static String RESOURCE_INITIAL = "UNDEPLOYED";
	private Lock restLock = new ReentrantLock();
	private Lock restLock4Page = new ReentrantLock();

	static {
		httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(3000);
		httpRequestFactory.setConnectTimeout(3000);
		httpRequestFactory.setReadTimeout(3000);
		COMMONFLOW=new ArrayList<>();
		COMMONFLOW.add("deploy");
		COMMONFLOW.add("start");
		COMMONFLOW.add("stop");
		COMMONFLOW.add("destroy");
		COMMONFLOW.add("snapshot");
		COMMONFLOW.add("upgrade");
	}

	@Value(value = "${ftp.url}")
	String url;
	@Value(value = "${ftp.port}")
	int port;
	@Value(value = "${ftp.user}")
	String user;
	@Value(value = "${ftp.pwd}")
	String pwd;
	@Value(value = "${ftp.timeOut}")
	String timeOut;
	

	@Autowired
	@Qualifier("blueprintDao")
	private IBlueprintDao dao;

	@Autowired
	@Qualifier("resourceService")
	private IResourceService resourceService;

	@Autowired
	@Qualifier("applicationService")
	private IApplicationService applicationService;

	@Autowired
	@Qualifier("nodeLabelService")
	NodeLabelService nodeLabelService;

	@Autowired
	@Qualifier("messageService")
	MessageService messageService;

	@Resource
	ConfigsService configService;

	@Autowired
	@Qualifier("instanceService")
	private IInstanceService instanceService;

	@Autowired
	@Qualifier("bluePrintTypeDao")
	BluePrintTypeDao bluePrintTypeDao;

	@Value(value = "${flowServerUrl}")
	String flowServerUrl;

	@Autowired
	@Qualifier("applicationDao")
	private IApplicationDao applicationDao;

	@Autowired
	@Qualifier("configservice")
	private ConfigsService configservice;

	@Autowired
	@Qualifier("templateDao")
	ITemplateDao templateDao;

	@Autowired
	@Qualifier("rcService")
	private RCService rcService;

	@Autowired
	@Qualifier("componentUpdateDAO")
	ComponentUpdateDAO componentUpdateDAO;

	@Autowired
	@Qualifier("logDao")
	LogDao logDao;

	@Autowired
	@Qualifier("nodeService")
	INodeService nodeService;

	@Autowired
	@Qualifier("resourceDao")
	private IResourceDao resourceDao;

	@Autowired
	@Qualifier("adapterService")
	private IAdapterService adapterService;

	@Autowired
	@Qualifier("instanceDao")
	private IInstanceDao instanceDao;

	@Value(value = "${adapterUrl}")
	String adapterUrl;

	@Autowired
	@Qualifier("blueprintTemplateDao")
	private IBlueprintTemplateDao blueprintTemplatedao;
	
	@Resource
	IAudit auditService;
	
//	@Autowired
//    private StringRedisTemplate redisTemplate;

	@Override
	public int saveAllBlueprint(Map<String, String> param) {
		return dao.saveAllBlueprint(param);
	}

	@Override
	public String getAllBlueprint(String blueprint_name) {
		return dao.getAllBlueprint(blueprint_name);
	}

	@Override
	public int updateAllBlueprint(Map<String, String> param) {
		return dao.updateAllBlueprint(param);

	}

	@Override
	public int saveBluePrintType(BluePrintType bluePrintType) {
		return dao.saveBluePrintType(bluePrintType);
	}

	@Override
	public String generateStartFlow(BluePrint bp) throws Throwable {
		BluePrint copy = (BluePrint) Utils.deepClone(bp);
		copy.setOp("start");
		List<Element> elements = copy.getNodeDataArray();
		for (Element e : elements) {
			if (e.getEleType().equals(Element.RESOURCE)) {
				// pooltype=1是静态资源 pooltype=2是云资源层 pooltype=3是动态资源
				if (e.getPooltype() == Element.DYNAMICRESOURCE) {
					e.setIns(1);
				}
			}
		}
		Map<String, Object> params = new HashMap<>();
		params.put("CZRY_DM", "00000000000");
		params.put("PDJSON", copy);
		return JSON.toJSONString(params);
	}

	@Override
	public String generateStopFlow(BluePrint bp, String blueprint_id)
			throws Throwable {
		BluePrint copy = (BluePrint) Utils.deepClone(bp);
		copy.setBluePrintId(blueprint_id);
		// 停止流程也不需要依赖线
		copy.setLinkDataArray(new ArrayList<StartBluePrint>());
		this.generateFlow(copy, "stop");
		return blueprint_id;
	}

	@Override
	public void generateDestroyFlow(BluePrint bp, String blueprint_id)
			throws Throwable {
		BluePrint copy = (BluePrint) Utils.deepClone(bp);
		copy.setBluePrintId(blueprint_id);
		// 卸载流程不需要依赖线
		copy.setLinkDataArray(new ArrayList<StartBluePrint>());
		this.generateFlow(copy, "destroy");
	}

	private void generateFlow(BluePrint copy, String flowType) {
		// 生成启动流程
		List<Element> elements = copy.getNodeDataArray();
		Iterator<Element> iterator = elements.iterator();
		while (iterator.hasNext()) {
			Element element = iterator.next();
			String eleType = element.getEleType();
			if (eleType.equals(Element.RESOURCE)) {
				iterator.remove();
			} else if (eleType.equals(Element.COMPONENT)) {
				// 获取组件的启动ID，如果没有，就remove这个组件
				String resourceVersionId = element.getResourceVersionId();
				if (String.valueOf(element.getDeployId()) != null) {
					continue;
				}
				Map<String, Object> detail = resourceService
						.getResourceVersionDetail(resourceVersionId);
				Properties flows = (Properties) detail.get("flows");
				if (flows != null && flows.contains(flowType)) {
					element.setDeployId(Long.valueOf(flows
							.getProperty(flowType)));
				} else {
					iterator.remove();
				}
			} else if (eleType.equals(Element.WORKPIECE)) {
				iterator.remove();
			}
		}
		long flowId = Utils.getUniqueFlowId();
		copy.setId(flowId);
		Map<String, Object> params = new HashMap<>();
		params.put("CZRY_DM", "00000000000");
		params.put("PDJSON", copy);
		RestTemplate restUtil = new RestTemplate();
		String jsonObj = JSON.toJSONString(params);
		System.out.println(jsonObj);
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("goflow", jsonObj);
		String result = restUtil.postForObject(flowServerUrl
				+ "/WFService/goflowToSmartflow.wf", requestEntity,
				String.class);
		if (result.contains("true")) {
			// 生成流程成功
			BluePrintType bpt = new BluePrintType();
			bpt.setBlueprint_id(copy.getBluePrintId());
			bpt.setFlow_id(flowId);
			bpt.setFlow_type(flowType);
			bpt.setFlow_info(JSON.toJSONString(copy));
			dao.saveBluePrintType(bpt);
		} else {
			// 流程生成失败
			System.out.println("流程生成失败....." + result);
		}
	}

	@Override
	public void generateDeployFlow(BluePrint bluePrint, String blueprint_id)
			throws Throwable {
		BluePrint bp = (BluePrint) Utils.deepClone(bluePrint);
		// 先计算出实例数返会修改实例数之后的蓝图
		// bp = this.generateInstances(bp);
		bp.setLinkDataArray(new ArrayList<StartBluePrint>());
		bp.setBluePrintId(blueprint_id);
		this.generateFlow(bp, "deploy");
	}

	public int getBlueprint_id(String blueprint_name) {
		return dao.getBlueprint_id(blueprint_name);
	}

	@Override
	public void delBluePrintType(String blueprint_id) {
		dao.delBluePrintType(blueprint_id);
	}

	public static List<Element> getRootElements(List<Element> elements) {
		List<Element> elementRoots = new ArrayList<>();
		for (Element element : elements) {
			if (element.getIsGroup() != null && element.getIsGroup() == true
					&& "0".equals(String.valueOf(element.getGroup()))) {
				elementRoots.add(element);
			}
		}
		return elementRoots;
	}

	public List<Element> getElementChilds(Element root,
			List<Element> allElements) {
		List<Element> elementRoot = new ArrayList<>();
		elementRoot.add(root);
		for (Element e : allElements) {
			if (!"0".equals(String.valueOf(e.getGroup()))
					&& e.getGroup() == root.getKey()) {
				elementRoot.addAll(getElementChilds(e, allElements));
			}
		}
		return elementRoot;
	}

	public Map<String, List<Instance>> createInstance(List<TreeNode> roots,
			Map<String, Object> params) throws Exception{
		Map<String, List<Instance>> rs = new HashMap<String, List<Instance>>();

		for (TreeNode tnode : roots) {
			createInstance(tnode, rs, params);
		}
		return rs;
	}

	public void createInstance(TreeNode node, Map<String, List<Instance>> rs,
			Map<String, Object> params) throws Exception {
		List<TreeNode> parents = node.getParentList();
		List<Instance> ins = createInstance(parents, node, params);
		BluePrint bp = (BluePrint) params.get("bp");

		if (ins.size() > 0) {
			// 此处的Key可自己根据需要替换
			rs.put(bp.getBluePrintId() + "_"
					+ String.valueOf(node.getE().getKey()), ins);
		}
		for (TreeNode subnode : node.getChildren()) {
			createInstance(subnode, rs, params);
		}
	}

	public List<Instance> createInstance(List<TreeNode> parents, TreeNode node,
			Map<String, Object> params) throws Exception {
		List<Instance> instances = new ArrayList<Instance>();
		if (parents.size() == 0)// 如果是资源映射节点就跳过，只处理组件节点，此处需要计算组件的实例数的。
			return instances;
		TreeNode topNode = parents.get(parents.size() - 1);// 获取顶级资源映射节点
		if (Integer.valueOf(topNode.getE().getPooltype()) == 1) {
			String nodeType = "1";
			Element element = node.getE();
			String appName = element.getText();
			String label = "";
			// 根据标签获取资源列表
			label = parents.get(parents.size() - 1).getE().getLabel();
			String clusterId = parents.get(parents.size() - 1).getE().getClusterId();
			String ips = parents.get(parents.size() - 1).getE().getNodes();
			if(ips==null || clusterId==null){
				throw new Exception("环境或者节点为空，请双击资源池图标配置环境和节点！");
			}
			//此处为自动化测试(通过json直接保存蓝图实例)自动保存label；正常保存蓝图实例前必须手动保存label，
			saveResourcePoolLabel(clusterId, label, ips, nodeType);
			List<Node> nodes = getNodes(clusterId, label, nodeType);
			if(nodes.size() == 0){
				throw new Exception("环境[" + clusterId + "]、节点[" + ips + "]不匹配，请双击资源池图标重新配置环境和节点！");
			}
			this.saveApplication(element, params, nodes);

			long instanceCountInit = element.getIns();// 获取当前节点的实例数信息
			long instanceCount = element.getIns();// 获取当前节点的实例数信息
			for (int i = 0; i < parents.size() - 1; i++) {
				instanceCount *= parents.get(i).getE().getIns();
			}

			// 外层循环资源节点i，内层循环自己的所有上级的总体实例数（me.inc*parent.inc ...）
			long appId = applicationDao.findAppIdformAppName(appName,
					(int) params.get("bluePrintInstanceId"), element.getKey());
			List<Version> versions = element.getVersionlist();
			long insTimes = instanceCount / instanceCountInit;// 当前节点实例数的倍数
			for (int i = 0; i < nodes.size(); i++) {
				for (int j = 0; j < insTimes; j++) {
					Instance instance = new Instance();
					String instanceId = UUID.randomUUID().toString();
					instance.setId(instanceId);
					instance.setAppId(String.valueOf(appId));
					instance.setNodeId(nodes.get(i).getAdapterNodeId());
					instanceService.saveInstance(instance);
					instances.add(instance);
				}
			}
			//			for (int i = 0; i < nodes.size(); i++) {
			//				log.debug("nodeid: " + nodes.get(i).getAdapterNodeId());
			//				for (Version version : versions) {
			//					Map<String, Object> map = JSON.parseObject(version.getConfig(),
			//							new TypeReference<Map<String, Object>>() {
			//					});
			//					Map<String, String> inputConfig = (Map<String, String>) map
			//							.get("input");
			//					Map<String, String> outputConfig = (Map<String, String>) map
			//							.get("output");
			//					Map<String, String>  config = new HashMap<String,String>();
			//					config.putAll(inputConfig);
			//					config.putAll(outputConfig);
			//					long versionIns = version.getIns();
			//					long actuaVersionIns = versionIns * insTimes;
			//					List<Map<String, Object>> instanceList = instanceService
			//							.getInstances(appId, version.getResourceVersionId(),nodes.get(i).getAdapterNodeId());
			//					for (int j = 0; j < actuaVersionIns; j++) {
			//						if (actuaVersionIns == instanceList.size()) {
			//							for (Map<String, Object> instanceMap : instanceList) {
			//								Instance instance = new Instance();
			//								String instanceId = (String) instanceMap
			//										.get("id");
			//								instance.setId(instanceId);
			//								// instance.setAppId(String.valueOf(appId));
			//								// instance.setNodeId(nodes.get(i).getAdapterNodeId());
			//								// instance.setAppVersionId(version.getResourceVersionId());
			//								// instance.setComponentInput(inputConfig);
			//								// instance.setComponentOutput(outputConfig);
			//								instanceService.updateInstanceAll(instance);
			//								instances.add(instance);
			//							}
			//						} else {
			//							Instance instance = new Instance();
			//							String instanceId = UUID.randomUUID().toString();
			//							instance.setId(instanceId);
			//							instance.setAppId(String.valueOf(appId));
			//							instance.setNodeId(nodes.get(i).getAdapterNodeId());
			//							instance.setAppVersionId(version
			//									.getResourceVersionId());
			//							instance.setComponentInput(inputConfig);
			//							instance.setComponentOutput(outputConfig);
			//							instanceService.saveInstance(instance);
			//							instances.add(instance);
			//							//生成saltDir下的组件包文件
			//							com.dc.appengine.appmaster.entity.Version versionDetail = resourceDao.getResourceVersion(version.getResourceVersionId());
			//							String componentName = versionDetail.getResourceName();//appName从text获取，其不是组件名，而是组件名+key，逻辑修改为由组件版本从资源表中获取
			//							String versionName = versionDetail.getVersionName();
			//							generateInstanceFile(instanceId,componentName,config,versionName);
			//						}
			//					}
			//				}
			//			}

		} else if (Integer.valueOf(topNode.getE().getPooltype()) == 2) {
			// 调用原有PAAS说的master的计算逻辑，动态分配实例到资源上，直接从数据库查表就可以了，再封装成实例
		} else {
			// 暂时没有可能的情况
		}

		return instances;
	}

	public void saveApplication(Element element1, Map<String, Object> params,
			List<Node> nodes) throws Exception {

		long userId = (long) params.get("userId");
		String cluster_id = (String) nodes.get(0).getClusterId();
		int bluePrintInstanceId = (int) params.get("bluePrintInstanceId");
		String appName = element1.getText();
		Application app = new Application();
		long key = element1.getKey();
		String resourceVersionId = element1.getResourceVersionId();
		app.setUserId(userId);
		app.setAppName(appName);
		app.setClusterId(cluster_id);
		String versionId = "-1";
		if (StringUtils.isNotEmpty(element1.getConfig())) {
			Map<String, Object> map = JSON.parseObject(element1.getConfig(),
					new TypeReference<Map<String, Object>>() {
			});
			Map<String, String> inputConfig = (Map<String, String>) map
					.get("input");
			List<Map<String, String>> configList = new ArrayList<>();
			for (Map.Entry<String, String> entry : inputConfig.entrySet()) {
				String configKey = entry.getKey();
				String configValue = entry.getValue();
				String type = "input";
				String description = "desc";
				Map<String, String> singleConfig = new HashMap<>();
				singleConfig.put("key", configKey);
				singleConfig.put("value", configValue);
				singleConfig.put("type", type);
				singleConfig.put("description", description);
				configList.add(singleConfig);
			}
		}
		app.setVersionid(versionId);
		app.setKey(element1.getKey());
		app.setBlueInstanceId(bluePrintInstanceId);
		String componentName = element1.getNodeName();
		if(componentName == null || "".equals(componentName)){
			componentName = appName;
		}
		app.setComponentName(componentName);
		String nodeDisplay = element1.getNodeDisplay();
		if(nodeDisplay == null || "".equals(nodeDisplay)){
			nodeDisplay = componentName;
		}
		app.setNodeDisplay(nodeDisplay);
		long appId = applicationDao.findAppIdformAppName(appName,
				bluePrintInstanceId, element1.getKey());
		if (appId == 0) {
			applicationDao.save(app);
		}
	}

	public Map<String,Object> generateDployInstance(String instance_id) {
		Map<String, Object> blueprintInstance = dao.getBlueprintInstance(instance_id);
		int blueprint_instance_id = ((Integer) blueprintInstance.get("ID")).intValue();
		List<Integer> keys = getBPFlowKeys(instance_id, "deploy");
		long userId = Long.valueOf((String) blueprintInstance.get("USERID"));
		/*String bluePrint = (String) blueprintInstance.get("INFO");

		BluePrint bp = JSON.parseObject(bluePrint, BluePrint.class);
		// bp.setBluePrintId(blueprint_id);
		// 获取node标签
		List<Element> elementsall = bp.getNodeDataArray();
		List<Element> roots = getRootElements(elementsall);
		List<List<Element>> rootandsubs = new ArrayList<List<Element>>();
		for (Element root : roots) {
			rootandsubs.add(getElementChilds(root, elementsall));
		}
		List<TreeNode> nodes = generateMap1(roots, elementsall);
		System.out.println(nodes);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("bluePrintInstanceId", blueprint_instance_id);
		params.put("bp", bp);*/

		Map<String, List<? extends Object>> message = new HashMap<String, List<? extends Object>>();
		//Map<String, List<Instance>> rs = createInstance(nodes, params);
		/*for (Entry<String, List<Instance>> entry : rs.entrySet()) {
		int _index = entry.getKey().lastIndexOf("_");
		int key = Integer.valueOf(entry.getKey().substring(_index+1));
		if(!keys.contains(key)){
			continue;
		}
		List<String> instanceIds = new ArrayList<>();
		for (Instance ins : entry.getValue()) {
			instanceIds.add(ins.getId());
		}
		List<Object> insMessage = messageService.messages(instanceIds,"deploy");
		message.put(entry.getKey(), insMessage);
	}*/
		Map<String, Object> map = dao.getBlueprintInstance(instance_id);
		int bpid = (int) map.get("ID");
		List<Integer> set = getBPFlowKeys(instance_id,"deploy");
		List<Application> apps = applicationService
				.findAppByBlueprintInstIdAndKey(bpid, 0L);
		for (Application app : apps) {
			int key = (int) app.getKey();
			List<Map<String, Object>> instances = instanceService
					.listInstancesByAppId(app.getId());
			List<String> insts = new ArrayList<>();
			for (Map<String, Object> instance : instances) {
				String id = (String) instance.get("instanceId");
				insts.add(id);
			}
			//如果启动流程中包含这个组件，那么将这个组件放在消息管理器中
			if(set.contains(key)){
				message.put(instance_id + "_" + key,messageService.messages(insts, "deploy"));
			}
		}


		// 放置到消息管理器中
		System.out.println(message);
		this.putMessage(message);
		String flowType = "deploy";

		Map<String, Object> bpt = dao.getBlueprintType(instance_id, flowType);
		long flowId = Long.valueOf(bpt.get("FLOW_ID").toString());
		// String flowId = "2017041800002901";
		RestTemplate rest = new RestTemplate();
		String startUrl = flowServerUrl
				+ "/WFService/startPdByPidAndUsers.wf?pdId=" + flowId
				+ "&initiator=admin";
		String startResult = rest.getForObject(startUrl, String.class);
		System.out.println(startResult);
		handleResult(startResult, "deploy", blueprint_instance_id);
		System.out.println(JSON.toJSONString(message));
		return bpInstanceOper(startResult);
	}

	public List<Node> getNodes(String clusterId, String label, String nodeType) {
		List<Node> keyLabel = new ArrayList<Node>();
		Map<String, String> map = new HashMap<String, String>();
		map = JSON.parseObject(new String(label),
				new TypeReference<Map<String, String>>() {
		});
		String key = map.get("key");
		String value = map.get("value");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", nodeType);
		param.put("key", key);
		param.put("value", value);
		param.put("clusterId", clusterId);
		List<Map<String, Object>> nodes = new ArrayList<>();
		nodes = nodeLabelService.findNodesByLabel(param);

		// List<Map<String,Object>> nodes=new ArrayList<Map<String,Object>>();
		// nodes.add(new HashMap<String,Object>());
		// nodes.get(0).put("IP", "10.1.108.100");
		// newnodes.get(0).put("IP", "10.1.108.100");

		for (Map<String, Object> node : nodes) {
			Node n = new Node();
			n.setIp(String.valueOf(node.get("IP")));
			n.setAdapterNodeId(String.valueOf(node.get("ADAPTERNODEID")));
			n.setClusterId(String.valueOf(node.get("CLUSTERID")));
			keyLabel.add(n);
		}
		return keyLabel;
	}

	// 根据资源层组件获取 key-value的对应关系 v1.0
	public HashMap<Long, Long> generateMap(Element element,
			HashMap<Long, Long> map) {

		HashMap<Long, Long> resultmap = new HashMap<Long, Long>();
		List<String> valueSet = new ArrayList<>();
		ArrayList List = new ArrayList();
		Set<Long> set = map.keySet();
		Collection<Long> values = map.values();
		for (Long s : values) {
			List.add(s);
		}
		boolean isTrue = true;
		Long key = element.getKey();// 资源层的key
		while (isTrue) {

			for (Entry<Long, Long> entry : map.entrySet()) {
				if (key.equals(entry.getValue())) {
					resultmap.put(entry.getValue(), entry.getKey());
					key = entry.getKey();
					if (!List.contains(key)) {
						isTrue = false;
						break;
					}
				}
			}
		}

		return resultmap;
	}

	// public static void main(String...els){
	// String bluePrint="{ \"class\": \"go.GraphLinksModel\","+
	// "\"linkFromPortIdProperty\": \"fromPort\","+
	// "\"linkToPortIdProperty\": \"toPort\","+
	// "\"modelData\": {\"position\":\"-613.7561613901954 -587.1715764822264\"},"+
	// "\"nodeDataArray\": [ "+
	// "{\"text\":\"静态资源池\", \"pooltype\":1, \"isGroup\":true, \"category\":\"OfNodes\", \"ins\":3, \"des\":\"静态资源池\", \"source\":\"img/server.jpg\", \"color\":\"#FFCC00\", \"key\":-1, \"loc\":\"-459.2561613901954 -325.52157648222646\", \"eleType\":\"resource\", \"label\":{\"key\":\"lakey\", \"value\":\"lavalue\"}},"+
	// "{\"text\":\"JDK\", \"isGroup\":true, \"category\":\"OfNodes\", \"ins\":1, \"des\":\"JDK\", \"source\":\"img/jdk.jpg\", \"key\":-5, \"loc\":\"-459.2561613901954 -285.52157648222646\", \"group\":-1, \"eleType\":\"component\"},"+
	// "{\"text\":\"Tomcat\", \"isGroup\":true, \"category\":\"OfNodes\", \"ins\":2, \"des\":\"tomcat 1111\", \"source\":\"img/tomcat.png\", \"key\":-4, \"loc\":\"-459.2561613901954 -245.5215764822265\", \"group\":-5, \"eleType\":\"component\"},"+
	// "{\"category\":\"simple\", \"text\":\"WAR包\", \"des\":\"WAR包\", \"loc\":\"-459.2561613901954 -245.52157648222652\", \"ins\":1, \"source\":\"img/zip.jpg\", \"color\":\"#fff\", \"figure\":\"Circle\", \"fill\":\"#00AD5F\", \"key\":-11, \"group\":-4, \"eleType\":\"component\"}"+
	// "],"+
	// "\"linkDataArray\": []}";
	// bluePrint="{ \"class\": \"go.GraphLinksModel\","+
	// "\"linkFromPortIdProperty\": \"fromPort\","+
	// "\"linkToPortIdProperty\": \"toPort\","+
	// "\"modelData\": {\"position\":\"-336.2660547925002 -845.523890946914\"},"+
	// "\"nodeDataArray\": [ "+
	// "{\"text\":\"静态资源池\", \"pooltype\":1, \"isGroup\":true, \"category\":\"OfNodes\", \"ins\":3, \"des\":\"静态资源池\", \"source\":\"img/server.jpg\", \"color\":\"#FFCC00\", \"key\":-1, \"loc\":\"-459.2561613901954 -325.52157648222646\", \"eleType\":\"resource\", \"label\":{\"key\":\"lakey\", \"value\":\"lavalue\"}},"+
	// "{\"text\":\"JDK\", \"isGroup\":true, \"category\":\"OfNodes\", \"ins\":1, \"des\":\"JDK\", \"source\":\"img/jdk.jpg\", \"key\":-5, \"loc\":\"-459.2561613901954 -285.52157648222646\", \"group\":-1, \"eleType\":\"component\"},"+
	// "{\"text\":\"Tomcat\", \"isGroup\":true, \"category\":\"OfNodes\", \"ins\":2, \"des\":\"tomcat 1111\", \"source\":\"img/tomcat.png\", \"key\":-4, \"loc\":\"-459.2561613901954 -245.5215764822265\", \"group\":-5, \"eleType\":\"component\"},"+
	// "{\"category\":\"simple\", \"text\":\"WAR包\", \"des\":\"WAR包\", \"loc\":\"-459.2561613901954 -245.52157648222652\", \"ins\":1, \"source\":\"img/zip.jpg\", \"color\":\"#fff\", \"figure\":\"Circle\", \"fill\":\"#00AD5F\", \"key\":-11, \"group\":-4, \"eleType\":\"component\"}"+
	// "{\"text\":\"静态资源池\", \"pooltype\":1, \"isGroup\":true, \"category\":\"OfNodes\", \"ins\":2, \"des\":\"静态资源池\", \"source\":\"img/server.jpg\", \"color\":\"#FFCC00\", \"key\":-16, \"loc\":\"-59.77129742556633 -521.475009704375\", \"eleType\":\"resource\", \"label\":{\"key\":\"lakey\", \"value\":\"lavalue\"}},"+
	// "{\"text\":\"Oracle\", \"isGroup\":true, \"category\":\"OfNodes\", \"ins\":1, \"des\":\"Oracle\", \"source\":\"img/oracle.png\", \"key\":-7, \"loc\":\"-59.77129742556633 -481.475009704375\", \"group\":-16, \"eleType\":\"component\"},"+
	// "{\"category\":\"simple\", \"text\":\"INIT_Database\", \"des\":\"INIT_Database\", \"loc\":\"-60.51002514746108 -527.2761284618359\", \"ins\":1, \"source\":\"img/database.jpg\", \"color\":\"#fff\", \"figure\":\"Circle\", \"fill\":\"#00AD5F\", \"key\":-17, \"group\":-7, \"eleType\":\"component\"},"+
	// "{\"category\":\"simple\", \"text\":\"INIT_Database\", \"des\":\"INIT_Database\", \"loc\":\"-59.032569703671584 -435.67389094691407\", \"ins\":1, \"source\":\"img/database.jpg\", \"color\":\"#fff\", \"figure\":\"Circle\", \"fill\":\"#00AD5F\", \"key\":-18, \"group\":-7, \"eleType\":\"component\"}"+
	// "],"+
	// "\"linkDataArray\": []}";
	// BluePrint bp = JSON.parseObject(bluePrint,BluePrint.class);
	// //获取node标签
	// List<Element> elementsall = bp.getNodeDataArray();
	//
	// List<Element> roots=getRootElements(elementsall);
	//
	// List<List<Element>> rootandsubs=new ArrayList<List<Element>>();
	//
	//
	// List<TreeNode> nodes=this.generateMap1(roots,elementsall);
	// //System.out.println(nodes);
	// // Map<String,List<Instance>> rs=createInstance(nodes,bp);
	// // System.out.println(rs);
	// }

	public List<TreeNode> generateMap1(List<Element> rootList, List<Element> els) {
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		for (Element root : rootList) {
			nodes.add(generateMap2(root, els));
		}
		return nodes;
	}

	public TreeNode generateMap2(Element element, List<Element> els) {
		TreeNode node = e2Node(element);
		for (Element e : els) {
			if (!"0".equals(String.valueOf(e.getGroup()))
					&& e.getGroup() == element.getKey()) {
				TreeNode subnode = generateMap2(e, els);
				node.add(subnode);
			}
		}
		return node;
	}

	public List<LinkedHashMap<Long, Long>> generateMap1(Element element,
			HashMap<Long, Long> map) throws Throwable {

		HashMap<Long, Long> map2 = (HashMap<Long, Long>) Utils.deepClone(map);
		List<LinkedHashMap<Long, Long>> lists = new ArrayList<LinkedHashMap<Long, Long>>();
		List<LinkedHashMap<Long, Long>> resultList = new ArrayList<LinkedHashMap<Long, Long>>();
		Set<Long> keys = map.keySet();
		Collection<Long> values = map.values();
		Collection<Long> values2 = map2.values();
		boolean isTrue = true;
		Long key = Long.valueOf(element.getKey());// 资源层的key
		LinkedHashMap<Long, Long> resultmap = new LinkedHashMap<Long, Long>();

		while (isTrue) {
			for (Entry<Long, Long> entry : map.entrySet()) {
				if (key.equals(entry.getValue())) {
					resultmap.put(entry.getValue(), entry.getKey());
					key = entry.getKey();

					if (!values.contains(key)) {
						map.remove(entry.getKey());
						System.out.println(resultmap);
						if (!lists.contains(resultmap)) {
							lists.add(resultmap);
						}
						key = Long.valueOf(element.getKey());// 资源层的key，重头遍历
						if (map.size() == 1) {
							isTrue = false;
						}
						break;
					}
					break;
				}
			}
		}
		for (LinkedHashMap<Long, Long> map3 : lists) {
			Entry<Long, Long> entry = getTailByReflection(map3);
			for (Entry<Long, Long> entry1 : map.entrySet()) {
				if (!values2.contains(entry.getValue())) {
					resultList.add(map3);
				}
			}
		}
		System.out.println(resultList);
		return resultList;
	}

	// 获取map中的最后一个entry
	public Entry<Long, Long> getTailByReflection(LinkedHashMap<Long, Long> map)
			throws NoSuchFieldException, IllegalAccessException {
		Field tail = map.getClass().getDeclaredField("tail");
		tail.setAccessible(true);
		return (Entry<Long, Long>) tail.get(map);
	}

	// 测试方法，生成工作流使用的报文
	public static void main(String[] args) {
		Map<String, String> varMap = new HashMap<String, String>();
		varMap.put("_taskBKey", "gghsfsjkafhskzgjsl24235" + "_" + "-2");
		String insvarMap = JSON.toJSONString(varMap);
		JSONObject jsonVar = JSONObject.parseObject(insvarMap);
		System.out.println(jsonVar);
		/*
		 * byte[] buffer = new byte[1024*100]; String s=null; try
		 * (FileInputStream fis = new
		 * FileInputStream("C:\\Users\\yangzhec\\Desktop\\bluePrint.txt")){ int
		 * i = IOUtils.read(fis, buffer); s = new String(buffer,0,i); } catch
		 * (IOException e) { e.printStackTrace(); } //System.out.println(s);
		 * BluePrint bp = JSON.parseObject(s, BluePrint.class);
		 * bp.setBluePrintId(UUID.randomUUID().toString()); bp.setIssub(false);
		 * List<Element> list = bp.getNodeDataArray(); for(Element e:list){
		 * if(e.getEleType().equals(Element.RESOURCE)){ e.setDeployId(-1);
		 * e.setStartId(-1); e.setStopId(-1); e.setDestroyId(-1); }else
		 * if(e.getEleType().equals(Element.COMPONENT)){ String config =
		 * e.getConfig(); System.out.println(config); Map<String,Object> map =
		 * JSON.parseObject(config, new TypeReference<Map<String,Object>>(){});
		 * System.out.println(map.get("input") instanceof Map); } }
		 */

		// System.out.println(JSON.toJSONString(bp));
		// System.out.println(bp.getBluePrintId());
	}

	//启动的时候封装消息去掉不需要的组件
	@Override
	public Map<String,Object> startBlueprintInstance(String instance_id) {
		Map<String, Object> map = dao.getBlueprintInstance(instance_id);
		int bpid = (int) map.get("ID");
		String flowType = "start";
		Map<String, Object> bpt = dao.getBlueprintType(instance_id, flowType);
		List<Integer> set = getBPFlowKeys(instance_id,"start");
		Map<String, List<? extends Object>> message = new HashMap<>();
		long flowId = Long.valueOf(bpt.get("FLOW_ID").toString());
		List<Application> apps = applicationService
				.findAppByBlueprintInstIdAndKey(bpid, 0L);
		for (Application app : apps) {
			int key = (int) app.getKey();
			List<Map<String, Object>> instances = instanceService
					.listInstancesByAppId(app.getId());
			List<String> insts = new ArrayList<>();
			for (Map<String, Object> instance : instances) {
				String id = (String) instance.get("instanceId");
				insts.add(id);
			}
			//如果启动流程中包含这个组件，那么将这个组件放在消息管理器中
			if(set.contains(key)){
				message.put(instance_id + "_" + key,messageService.messages(insts, "start"));
			}
		}
		// 放置到消息管理器中
		this.putMessage(message);
		RestTemplate rest = new RestTemplate();
		String startUrl = flowServerUrl
				+ "/WFService/startPdByPidAndUsers.wf?pdId=" + flowId
				+ "&initiator=admin";
		String startResult = rest.getForObject(startUrl, String.class);
		System.out.println(startResult);
		handleResult(startResult, "start", bpid);
		return bpInstanceOper(startResult);
	}

	private Map<String,Object> bpInstanceOper(String str){
		Map<String, Object> result = JSON.parseObject(str,new TypeReference<Map<String, Object>>() {});
		boolean state = (boolean) result.get("state");
		Map<String,Object> resu = new HashMap<>();
		resu.put("result", state);
		if(state){
			Map<String, String> data = (Map<String, String>) result.get("data");
			resu.put("id", data.get("instanceid"));
			resu.put("message", "成功发送命令");
		}else{
			resu.put("message", "调用流程失败");
		}
		return resu;
	}

	private List<Integer> getBPFlowKeys(String bpInstanceId,String flowType){
		Map<String, Object> bpt = dao.getBlueprintType(bpInstanceId, flowType);
		String flowInfo = (String) bpt.get("FLOW_INFO");
		Map<String,Object> flowDef = JSON.parseObject(flowInfo, new TypeReference<Map<String,Object>>(){});
		List<Map<String,Object>> nodeDataArray = (List<Map<String, Object>>) flowDef.get("nodeDataArray");
		List<Integer> set = new ArrayList<>();
		for(Map<String,Object> m:nodeDataArray){
			Integer k = (Integer) m.get("key");
			set.add(k);
		}
		return set;
	}

	private void handleResult(String opResult, String op, int bpInstanceId) {
		Map<String, Object> result = JSON.parseObject(opResult,
				new TypeReference<Map<String, Object>>() {
		});
		boolean state = (boolean) result.get("state");
		if (state) {
			Map<String, String> data = (Map<String, String>) result.get("data");
			Map<String, Object> info = new HashMap<>();
			info.put("blueprintInstanceId", bpInstanceId);
			//info.put(op + "Id", data.get("instanceid"));
			//this.updateBlueprintInstance(info);
		} else {
			System.out.println("执行流程不成功....");
		}
	}

	private void saveInstanceId(String opResult, String bpInstanceId,String cdFlowId,Map<String, String> params) {
		Map<String, Object> result = JSON.parseObject(opResult,
				new TypeReference<Map<String, Object>>() {
		});
		boolean state = (boolean) result.get("state");
		if (state) {
			Map<String,Object> flowInfo = blueprintTemplatedao.getBlueprintTemplateFlowByCdFlowId(cdFlowId);
			String flowId = String.valueOf(flowInfo.get("FLOW_ID"));
			String flowName = flowInfo.get("FLOW_NAME").toString();
			String appName = ""+flowInfo.get("APP_NAME");
			Map<String, Object> map = dao.getBlueprintInstance(bpInstanceId);
			Map<String, String> data = (Map<String, String>) result.get("data");
			String appVersions = getFlowInstanceAppVersion(bpInstanceId, (String)flowInfo.get("FLOW_INFO_GROUP"));
			Map<String, Object> param = new HashMap<>();
			param.put("id", bpInstanceId);
			param.put("instanceId", data.get("instanceid").toString());
			param.put("flowName", flowName);
			param.put("appName", appName);
			param.put("flowId", flowId);
			param.put("appVersions", appVersions);
			JSONObject reSourcePoolConfig = JSON.parseObject(map.get("RESOURCE_POOL_CONFIG").toString());
			if(reSourcePoolConfig !=null){
				if(params != null){
					String _rc_reduce = params.get("_rc_reduce");
					if(_rc_reduce !=null && "true".equals(_rc_reduce)){
						JSONObject reducePoolConfig = JSON.parseObject(map.get("REDUCE_POOL_CONFIG").toString());
						if(reducePoolConfig !=null){
							reSourcePoolConfig.putAll(reducePoolConfig);//实例收缩时，做资源池合并
						}
					}
				}
				param.put("poolConfig", reSourcePoolConfig.toString());
			}
			dao.saveFlowInstanceId(param);
		}
	}
	private String getFlowInstanceAppVersion(String bpInstanceId, String flowInfo) {
		List<Map<String, Object>> appList = new ArrayList<>();
		Map<String ,Application> appsMap = new HashMap<>();
		List<Application> apps = applicationDao.getBlueprintComponents(bpInstanceId);
		for(Application app : apps){
			appsMap.put(app.getAppName(), app);
		}
		Map<String, Object> flow = JSON.parseObject(flowInfo, new TypeReference<Map<String, Object>>(){});
		List<Map<String, Object>> nodeDataArray = (List<Map<String, Object>>)flow.get("nodeDataArray");
		for(Map<String, Object> node : nodeDataArray){
			if((int)node.get("flowcontroltype") == 0){
				String nodeDisplay = (String)node.get("nodeDisplay");
				String text = (String)node.get("text");
				String nodeName = (String)node.get("nodeName");
				String versionConfig = (String)node.get("versionConfig");
				String subflowName = (String)node.get("subflowName");
				String cdFlowId = (String)node.get("cdFlowId");
				if(appsMap.containsKey(text)){
					Application appDetail = appsMap.get(text);
					if(appDetail.getExecuteFlag() == 1){
						Map<String, Object> appVersion = new HashMap<>();
						String resourceId = appDetail.getResourceId();
						Map<String, Object> resourceDetail = resourceDao.getResourceInfo(resourceId);
						appVersion.put("componentId", resourceDetail.get("ID"));
						appVersion.put("componentName", resourceDetail.get("RESOURCE_NAME"));
						appVersion.put("nodeDisplay", nodeDisplay);
						Map<String, Object> subFlowInfo = resourceDao.getNewFlowDetailByFlowId(cdFlowId);
						appVersion.put("flowId", cdFlowId);
						appVersion.put("flowName", subFlowInfo.get("FLOW_NAME"));
						List<Map<String, Object>> versionInfos = getVersionDetail(appDetail, versionConfig, subflowName);
						appVersion.put("versionInfos", versionInfos);
						appList.add(appVersion);
					}
				}
			}
		}
		return JSON.toJSONString(appList);
	}

	private List<Map<String, Object>> getVersionDetail(Application appDetail, String versionConfig, String subflowName) {
		List<Map<String, Object>> versions = new ArrayList<>();
		if("current+target".equals(versionConfig)){
			if(appDetail.getCurrentVersion() != null){
				Map<String, Object> cVersionInfo = resourceDao.getVersionDetailByVersionId(appDetail.getCurrentVersion());
				if(cVersionInfo != null){
					Map<String, Object> cVersion = new HashMap<>();
					cVersion.put("versionId", cVersionInfo.get("ID"));
					cVersion.put("versionName", cVersionInfo.get("VERSION_NAME"));
					cVersion.put("versionNum", cVersionInfo.get("VERSIONNUM"));
					versions.add(cVersion);
				}
			}
			if(appDetail.getTargetVersion() != null){
				Map<String, Object> tVersionInfo = resourceDao.getVersionDetailByVersionId(appDetail.getTargetVersion());
				if(tVersionInfo != null){
					Map<String, Object> tVersion = new HashMap<>();
					tVersion.put("versionId", tVersionInfo.get("ID"));
					tVersion.put("versionName", tVersionInfo.get("VERSION_NAME"));
					tVersion.put("versionNum", tVersionInfo.get("VERSIONNUM"));
					versions.add(tVersion);
				}
			}
		}
		else{
			String versionId = null;
			if(null == versionConfig || "none".equals(versionConfig)){
				if("destroy".equalsIgnoreCase(subflowName) || "stop".equalsIgnoreCase(subflowName)){
					versionId = appDetail.getCurrentVersion();
				}
				else{
					versionId = appDetail.getTargetVersion();
				}
			}
			else if("current".equals(versionConfig)){
				versionId = appDetail.getCurrentVersion();
			}
			else if("target".equals(versionConfig)){
				versionId = appDetail.getCurrentVersion();
			}
			else{
			}
			if(versionId != null){
				Map<String, Object> versionInfo = resourceDao.getVersionDetailByVersionId(versionId);
				if(versionInfo != null){
					Map<String, Object> version = new HashMap<>();
					version.put("versionId", versionInfo.get("ID"));
					version.put("versionName", versionInfo.get("VERSION_NAME"));
					version.put("versionNum", versionInfo.get("VERSIONNUM"));
					versions.add(version);
				}
			}
		}
		return versions;
	}

	private String putMessage(Map<String, List<? extends Object>> message) {
		//RestTemplate rest = new RestTemplate(httpRequestFactory);
		RestTemplate rest = RequestClient.getInstance().getRestParamTemplate();
		HttpHeaders requestHeaders = new HttpHeaders();
		//requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		requestHeaders.setContentType(MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8"));
		MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
		mvm.add("message", JSON.toJSONString(message));
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(
				mvm, requestHeaders);
		String messageResult = rest.postForObject(flowServerUrl
				+ "/cd/message.wf", httpEntity, String.class);
		return messageResult;
	}

	@Override
	public Map<String,Object> stopBlueprintInstance(String instance_id) {
		Map<String, Object> map = dao.getBlueprintInstance(instance_id);
		List<Integer> keys = this.getBPFlowKeys(instance_id, "stop");
		int bpid = (int) map.get("ID");
		String flowType = "stop";
		Map<String, Object> bpt = dao.getBlueprintType(instance_id + "",
				flowType);
		Map<String, List<? extends Object>> message = new HashMap<>();
		long flowId = Long.valueOf(bpt.get("FLOW_ID").toString());
		List<Application> apps = applicationService
				.findAppByBlueprintInstIdAndKey(bpid, 0L);
		for (Application app : apps) {
			int key = (int) app.getKey();
			if(!keys.contains(key)){
				continue;
			}
			List<Map<String, Object>> instances = instanceService
					.listInstancesByAppId(app.getId());
			List<String> insts = new ArrayList<>();
			for (Map<String, Object> instance : instances) {
				String id = (String) instance.get("instanceId");
				insts.add(id);
			}
			message.put(instance_id + "_" + key,
					messageService.messages(insts, "stop"));
		}
		// 放置到消息管理器中
		this.putMessage(message);
		RestTemplate rest = new RestTemplate();
		String startUrl = flowServerUrl
				+ "/WFService/startPdByPidAndUsers.wf?pdId=" + flowId
				+ "&initiator=admin";
		String startResult = rest.getForObject(startUrl, String.class);
		System.out.println(startResult);
		handleResult(startResult, "stop", bpid);
		System.out.println(JSON.toJSONString(message));
		return bpInstanceOper(startResult);
	}

	@Override
	public Map<String,Object> destroyBlueprintInstance(String instance_id) {
		Map<String, Object> map = dao.getBlueprintInstance(instance_id);
		List<Integer> keys = this.getBPFlowKeys(instance_id, "destroy");
		int bpid = (int) map.get("ID");
		String flowType = "destroy";
		Map<String, Object> bpt = dao.getBlueprintType(instance_id + "",flowType);
		Map<String, List<? extends Object>> message = new HashMap<>();
		long flowId = Long.valueOf(bpt.get("FLOW_ID").toString());
		List<Application> apps = applicationService
				.findAppByBlueprintInstIdAndKey(bpid, 0L);
		for (Application app : apps) {
			int key = (int) app.getKey();
			if(!keys.contains(key)){
				continue;
			}
			List<Map<String, Object>> instances = instanceService
					.listInstancesByAppId(app.getId());
			List<String> insts = new ArrayList<>();
			for (Map<String, Object> instance : instances) {
				String id = (String) instance.get("instanceId");
				insts.add(id);
			}
			message.put(instance_id + "_" + key,
					messageService.messages(insts, "destroy"));
		}
		// 放置到消息管理器中
		this.putMessage(message);
		RestTemplate rest = new RestTemplate();
		String startUrl = flowServerUrl
				+ "/WFService/startPdByPidAndUsers.wf?pdId=" + flowId
				+ "&initiator=admin";
		String startResult = rest.getForObject(startUrl, String.class);
		System.out.println(startResult);
		handleResult(startResult, "destroy", bpid);
		System.out.println(JSON.toJSONString(message));
		return bpInstanceOper(startResult);
	}

	public TreeNode e2Node(Element e) {
		TreeNode node = new TreeNode(e);
		return node;
	}

	@Override
	public String getSubFlowInfo(long flowId) {
		return bluePrintTypeDao.getSubFlowInfo(flowId);
	}

	@Override
	public Map<String, Object> getBlueprintTypeByFlowId(long flowId) {
		return bluePrintTypeDao.getBlueprintTypeByFlowId(flowId);
	}

	@Override
	public Page listBlueprints(Map<String, Object> condition, int pageNum,
			int pageSize) {
		return dao.listBlueprints(condition, pageNum, pageSize);
	}

	@Override
	public List<Map<String,Object>> listAllBlueprintInstances(Map<String, Object> condition) {
		return dao.listAllBlueprintInstances(condition);
	}

	@Override
	public Page listBlueprintInstances(Map<String, Object> condition,
			int pageNum, int pageSize) {
		return dao.listBlueprintInstances(condition, pageNum, pageSize);
	}

	@Override
	public void updateBlueprintInstance(Map<String, Object> info) {
		this.dao.updateBlueprintInstance(info);
	}

	@Override
	public int getBlueprintInstanceNum(String blueprint_id) {
		return dao.getBlueprintInstanceNum(blueprint_id);
	}

	@Override
	public long getEmptyFlowId() {
		return dao.getEmptyFlowId();
	}

	public Map<String,Object> opBlueprintInstanceOne(long appId, String op) {
		long bPinstanceId = applicationService.getApp(appId)
				.getBlueInstanceId();
		Map<String, Object> map = dao
				.getBlueprintInstanceById((int) bPinstanceId);
		// String blueprintId = (String) map.get("BLUEPRINT_ID");
		int bpInstanceId = (int) map.get("ID");
		String bpId = map.get("INSTANCE_ID").toString();
		String flowType = op;
		Map<String, Object> bpt = dao.getBlueprintType(bpInstanceId + "",
				flowType);
		Map<String, List<? extends Object>> message = new HashMap<>();
		Application app = applicationService.getAppInfo(appId + "");
		Long key = app.getKey();
		List<Map<String, Object>> instances = instanceService
				.listInstancesByAppId(app.getId());
		List<String> insts = new ArrayList<>();
		for (Map<String, Object> instance : instances) {
			String id = (String) instance.get("instanceId");
			insts.add(id);
		}
		message.put(bpId + "_" + key, messageService.messages(insts, op));

		// 放置到消息管理器中
		this.putMessage(message);
		String startResult = "";
		String tempId = "";
		for (Map<String, Object> instance : instances) {
			RestTemplate rest = new RestTemplate();
			String id = (String) instance.get("instanceId");
			long flowId = getFlowIdOne(id, op);
			tempId = flowId + "";
			Map<String, String> varMap = new HashMap<String, String>();
			varMap.put("_taskBKey", bpId + "_" + key);
			String insvarMap = JSON.toJSONString(varMap);
			String encoderJson = "";
			try {
				encoderJson = URLEncoder.encode(insvarMap, HTTP.UTF_8);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String startUrl = flowServerUrl
					+ "/WFService/startPdByPidAndUsers.wf?pdId=" + flowId
					+ "&initiator=admin" + "&insvarMap=" + encoderJson;
			System.out.println(insvarMap);
			startResult = rest.getForObject(startUrl, String.class);
			System.out.println(startResult);
			handleResult(startResult, op, bpInstanceId);
		}
		System.out.println(JSON.toJSONString(message));
		Map<String,Object> mess = bpInstanceOper(startResult);
		mess.put("flowId", tempId);
		return mess;
	}

	public Map<String,Object> testDeployOne(List<Map<String, Object>> instances,
			Application app) {
		long bPinstanceId = app.getBlueInstanceId();
		List<String> insts = new ArrayList<>();
		for (Map<String, Object> instance : instances) {
			String id = (String) instance.get("instanceId");
			insts.add(id);
		}
		Long key = app.getKey();
		// insts.add("1aac7a31-b0a3-49e6-92c5-d33f4ae2a646");
		Map<String, List<? extends Object>> message = new HashMap<>();
		Map<String, Object> map = dao
				.getBlueprintInstanceById((int) bPinstanceId);
		String bpInstanceId = map.get("INSTANCE_ID").toString();
		int bpid = (int) map.get("ID");
		// String blueprintId = (String) map.get("BLUEPRINT_ID");
		message.put(bpInstanceId + "_" + key,
				messageService.messages(insts, "deploy"));
		this.putMessage(message);
		RestTemplate rest = new RestTemplate();
		String flowType = "deploy";
		Map<String, Object> bpt = dao.getBlueprintType(bpid + "", flowType);
		// long flowId = Long.valueOf(bpt.get("FLOW_ID").toString());
		// String flowId = "2017041800002901";
		String startResult = "";
		String tempId = "";
		for (Map<String, Object> instance : instances) {
			String id = (String) instance.get("instanceId");
			long flowId = getFlowIdOne(id, flowType);
			tempId = flowId + "";
			Map<String, String> varMap = new HashMap<String, String>();
			varMap.put("_taskBKey", bPinstanceId + "_" + key);
			String insvarMap = JSON.toJSONString(varMap);
			String encoderJson = "";
			try {
				encoderJson = URLEncoder.encode(insvarMap, HTTP.UTF_8);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String startUrl = flowServerUrl
					+ "/WFService/startPdByPidAndUsers.wf?pdId=" + flowId
					+ "&initiator=admin" + "&insvarMap=" + encoderJson;
			startResult = rest.getForObject(startUrl, String.class);
			System.out.println(startResult);
			handleResult(startResult, flowType, bpid);
		}
		System.out.println(JSON.toJSONString(message));
		Map<String,Object> mess = bpInstanceOper(startResult);
		mess.put("flowId", tempId);
		return mess;
	}

	public long getFlowIdOne(String instanceId, String op) {
		String versionId = instanceService.findresourceVersionId(instanceId);
		Map<String, Object> detail = resourceService
				.getResourceVersionDetail(versionId);
		String flowInfo = detail.get("flows").toString();

		Map<String, String> flowMap = JSON.parseObject(
				flowInfo.replace("=", ":"),
				new TypeReference<Map<String, String>>() {
				});
		String flowId = flowMap.get(op);
		return Long.valueOf(flowId);
	}

	@Override
	public int saveBlueprintTemplate(Map<String, String> param) {
		return dao.saveBlueprintTemplate(param);
	}

	@Transactional
	@Override
	public int saveBlueprintInstance(Map<String, String> param){
		dao.saveBlueprintInstance(param);
		//创建蓝图实例时生成组件实例并存库
		String instance_id = param.get("blue_instance_id").toString();
		Map<String, Object> blueprintInstance = dao.getBlueprintInstance(instance_id);
		int blueprint_instance_id = ((Integer) blueprintInstance.get("ID")).intValue();
		long userId = Long.valueOf((String) blueprintInstance.get("USERID"));
		String bluePrint = (String) blueprintInstance.get("INFO");

		BluePrint bp = JSON.parseObject(bluePrint, BluePrint.class);
		// bp.setBluePrintId(blueprint_id);
		// 获取node标签
		List<Element> elementsall = bp.getNodeDataArray();
		List<Element> roots = getRootElements(elementsall);
		List<List<Element>> rootandsubs = new ArrayList<List<Element>>();
		for (Element root : roots) {
			rootandsubs.add(getElementChilds(root, elementsall));
		}
		List<TreeNode> nodes = generateMap1(roots, elementsall);
		System.out.println(nodes);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("bluePrintInstanceId", blueprint_instance_id);
		params.put("bp", bp);
		try {
			Map<String, List<Instance>> rs = createInstance(nodes, params);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return 1;
	}

	@Override
	public String getBluePrintTree(Integer blueprintInstanceId, String flowId,String op) {
		// TODO Auto-generated method stub
		Map<String, Object> blueInstance = dao.getBlueprintInstanceById(blueprintInstanceId);
		String info = blueInstance.get("INFO").toString();
		op=op.toLowerCase();
		//		for(Map.Entry<String, Object> one : blueInstance.entrySet()){
		//			String key=one.getKey();
		//			Object value=one.getValue();
		//			if(value instanceof String && flowId.equals(value)){
		//				op=key.substring(0, key.indexOf("ID")).toLowerCase();//deploy,start,stop,destroy
		//				break;
		//			}
		//		}
		Map<String, Object> json = JSON.parseObject(info,new TypeReference<Map<String, Object>>(){});
		List<Map<String, Object>>  nodeDataArray =  (List<Map<String, Object>>) json.get("nodeDataArray");
		List<Map<String, Object>> list= new ArrayList<>();
		String bluePrintId=json.get("bluePrintId").toString();
		for(Map<String, Object> nodeData : nodeDataArray){
			Boolean pool=false;
			int group = (Integer) nodeData.get("group");
			if(group ==0){//root节点
				String resourceVersionId=null;
				if(nodeData.get("resourceVersionId") !=null){
					resourceVersionId= nodeData.get("resourceVersionId").toString();
				}
				Object pooltype = nodeData.get("pooltype");
				int key =  (Integer) nodeData.get("key");
				int curins=1;
				int instotal=(Integer) nodeData.get("ins");
				if(pooltype !=null && "1".equals(pooltype.toString())){//静态资源池
					pool =true;
				}else if(pooltype !=null && !"1".equals(pooltype.toString())){//动态资源池
					pool =true;
					instotal=1;
				}
				int count=instotal;
				while (count !=0){//创建instotal个节点
					count --;
					List<Map<String, Object>> steps= new ArrayList<>();
					List<Map<String, Object>> children= new ArrayList<>();
					Map<String, Object> root = new HashMap<>();
					root.put("label",  nodeData.get("text"));
					root.put("description",nodeData.get("des") );
					root.put("key",  nodeData.get("key"));
					root.put("identityMark",  bluePrintId+"_"+nodeData.get("key"));//组件唯一标识
					root.put("curins", curins);
					root.put("instotal",instotal );
					root.put("pool", pool);
					root.put("steps", steps);
					root.put("state", "00");
					if(pool){
						//						steps.add(this.constructStep("下载", "2"));
						//						steps.add(this.constructStep("解压", "2"));
						//						steps.add(this.constructStep("配置", "2"));
					}else{
						if(resourceVersionId !=null){
							//获取组件信息
							Map<String, Object>	componentJson = resourceService.getResourceVersionDetail(resourceVersionId);
							Map<String, Object> flows = (Map<String, Object>)componentJson.get("flows");
							int cpFlowId = (Integer)flows.get(op);
							Map<String, Object> blueprintType=  this.getBlueprintTypeByFlowId(cpFlowId);
							String flowType=blueprintType.get("FLOW_TYPE").toString();
							String subBluePrintId= "";
							if("subFlow".equals(flowType)){
								subBluePrintId= blueprintType.get("BLUEPRINT_INSTANCE_ID").toString();
							}
							String flowinfo = (String) blueprintType.get("FLOW_INFO");
							if(flowinfo !=null){
								Map<String, Object> cpjson = JSON.parseObject(flowinfo,new TypeReference<Map<String, Object>>(){});
								List<Map<String, Object>>  plnodeDataArray =  (List<Map<String, Object>>) cpjson.get("nodeDataArray");
								for(Map<String, Object> plnodeData : plnodeDataArray){
									steps.add(this.constructStep(plnodeData.get("text").toString(), "00",subBluePrintId+"_"+plnodeData.get("key")));//初始化状态为null
								}
							}
						}
					}
					children=getChildrenTree(key, nodeDataArray,op,bluePrintId);
					if(children !=null && children.size()!=0){
						root.put("children", children);
					}
					list.add(root);
				}
			}

		}
		return JSON.toJSONString(list);
	}
	public List<Map<String,Object>> getChildrenTree(int key,List<Map<String, Object>>  nodeDataArray,String op ,String bluePrintId){
		List<Map<String,Object>> list=new ArrayList<>();
		for(Map nodeData:nodeDataArray){
			Boolean pool=false;
			int group = (Integer) nodeData.get("group");
			if(group == key){//子节点
				String resourceVersionId=null;
				if(nodeData.get("resourceVersionId") !=null){
					resourceVersionId= nodeData.get("resourceVersionId").toString();
				}
				int nodeKey =  (Integer) nodeData.get("key");
				int curins=1;
				int instotal=(Integer) nodeData.get("ins");
				int count =instotal;
				while (count !=0){//创建instotal个节点
					count --;
					List<Map<String, Object>> steps= new ArrayList<>();
					List<Map<String, Object>> children= new ArrayList<>();
					Map<String, Object> root = new HashMap<>();
					root.put("label",  nodeData.get("text"));
					root.put("description",nodeData.get("des") );
					root.put("key",  nodeData.get("key"));
					root.put("identityMark",  bluePrintId+"_"+nodeData.get("key"));//组件唯一标识
					root.put("curins", curins);
					root.put("instotal",instotal );
					root.put("pool", pool);
					root.put("state", "00");
					if(resourceVersionId !=null){
						//获取组件信息
						Map<String, Object>	componentJson = resourceService.getResourceVersionDetail(resourceVersionId);
						Map<String, Object> flows = (Map<String, Object>)componentJson.get("flows");
						String cpFlowId = (String)flows.get(op);
						Map<String, Object> blueprintType=  this.getBlueprintTypeByFlowId(Long.valueOf(cpFlowId));
						String flowType=blueprintType.get("FLOW_TYPE").toString();
						String subBluePrintId= "";
						if("subFlow".equals(flowType)){
							subBluePrintId= blueprintType.get("BLUEPRINT_INSTANCE_ID").toString();
						}
						String flowinfo = (String) blueprintType.get("FLOW_INFO");
						if(flowinfo !=null){
							Map<String, Object> cpjson = JSON.parseObject(flowinfo,new TypeReference<Map<String, Object>>(){});
							List<Map<String, Object>>  plnodeDataArray =  (List<Map<String, Object>>) cpjson.get("nodeDataArray");
							for(Map<String, Object> plnodeData : plnodeDataArray){
								steps.add(this.constructStep(plnodeData.get("text").toString(), "00",subBluePrintId+"_"+plnodeData.get("key")));//初始化状态为null
							}
						}
					}
					root.put("steps", steps);
					children=getChildrenTree(nodeKey, nodeDataArray,op,bluePrintId);
					if(children !=null && children.size()!=0){
						root.put("children", children);
					}
					list.add(root);
				}
			}
		}
		return list;
	}
	public Map<String,Object> constructStep(String label,String state,String identityMark){
		Map<String, Object> step= new HashMap<>();
		step.put("label", label);
		step.put("state", state);
		step.put("identityMark", identityMark);
		return step;
	}
	public void delBlueprintTemplate(String blueprint_id) {
		dao.delBlueprintTemplate(blueprint_id);
	}

	@Transactional
	@Override
	public void delBlueInstance(int blueprintInstanceId) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> apps = instanceService.getAppList(blueprintInstanceId);
		for(Map<String,Object> app: apps){
			long appId = Long.parseLong(app.get("id").toString());
			List<Map<String,Object>> instanceList = instanceService.getInstanceList(appId);

			for(Map<String,Object> ins: instanceList){
				String insId = ins.get("id").toString();
				//删除组件实例
				instanceService.delInstanceAndFile(insId);
				//删除salt组件缓存
				File folder = new File("/srv/salt", insId);
				FileUtil.deleteAllFilesOfDir(folder);
			}
			//删除蓝图组件
			applicationDao.delApp(appId);
			//删除ftp上缓存
			String unsaltPath = "/srv/unsalt/"+appId;
			FtpUtils.deleteFolder(timeOut, url, port, user, pwd, unsaltPath);
			String saltPath = "/srv/salt/"+appId;
			FtpUtils.deleteFolder(timeOut, url, port, user, pwd, saltPath);
		}
		//流程不能删
		//templateDao.delBlueInstanceType(blueprintInstanceId);
		
		//删除此蓝图内组件所有相关的快照
		//templateDao.deleteAppSnapShotByBlueInstanceId(blueprintInstanceId);
		//		//获取蓝图实例所有快照
		//		List<Map<String, Object>> bpSnaplist = templateDao.getSnapShotInfoByBlueInstanceId(blueprintInstanceId);
		//		//删除蓝图实例快照内保存的对应组件快照,此处没有删除此蓝图实例包含的组件的其他快照(例如流程内组件单独做的快照)
		//		for(Map<String, Object> bpSnap : bpSnaplist){
		//			String bpSnapInfo = "" + bpSnap.get("SNAPSHOT_INFO");
		//			if(bpSnapInfo != null && !"".equals(bpSnapInfo.trim())){
		//				List<String> appSnapList = JSON.parseObject(bpSnapInfo, new TypeReference<List>(){});
		//				for(String appSnap : appSnapList){
		//					templateDao.deleteSnap(appSnap);
		//				}
		//			}
		//		}
		//删除蓝图实例的快照
		//templateDao.deleteSnapShotByBlueInstanceId(blueprintInstanceId);
		
		//删除1.5的蓝图快照
		Map<String, Object> blueprintDetail = dao.getBlueprintInstanceById(blueprintInstanceId);
		String instanceId = "" + blueprintDetail.get("INSTANCE_ID");
		this.deleteSnapshotByBlueprintInstanceId(instanceId);
		//删除蓝图实例内所有资源池内的label
		this.deleteResourcePoolLabelByBlueprintInstanceId(blueprintInstanceId);
		//删除蓝图实例
		templateDao.delBlueInstance(blueprintInstanceId);
		//hansn删除蓝图运行实例记录
		dao.deleteFlowInstanceIds(instanceId);
	}

	/*
	 * rc调用，维护实例数使用接口
	 * 
	 * @see
	 * com.dc.appengine.appmaster.service.IBlueprintService#operateFlow(long,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public String operateFlow(long appId, String instanceId, String op) {
		// TODO Auto-generated method stub
		// 蓝图实例id
		long blueinstanceId = applicationService.getApp(appId)
				.getBlueInstanceId();
		// 蓝图实例
		Map<String, Object> map = dao
				.getBlueprintInstanceById((int) blueinstanceId);
		int biId = (int) map.get("ID");
		String bpId = map.get("INSTANCE_ID").toString();
		Map<String, List<? extends Object>> message = new HashMap<>();
		Application app = applicationService.getAppInfo(appId + "");
		Long key = app.getKey();
		List<String> insts = new ArrayList<>();
		insts.add(instanceId);
		message.put(bpId + "_" + key, messageService.messages(insts, op));
		// 放置到消息管理器中
		this.putMessage(message);
		RestTemplate rest = new RestTemplate();
		// insts列表中只有一个实例的id
		String id = insts.get(0);
		long flowId = getFlowIdOne(id, op);
		// 子流程需要的参数
		Map<String, String> varMap = new HashMap<String, String>();
		varMap.put("_taskBKey", bpId + "_" + key);
		String insvarMap = JSON.toJSONString(varMap);
		String encoderJson = "";
		try {
			encoderJson = URLEncoder.encode(insvarMap, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String startUrl = flowServerUrl
				+ "/WFService/startPdByPidAndUsers.wf?pdId=" + flowId
				+ "&initiator=admin" + "&insvarMap=" + encoderJson;
		System.out.println(insvarMap);
		String startResult = rest.getForObject(startUrl, String.class);
		System.out.println(startResult);
		handleResult(startResult, op, biId);
		System.out.println(JSON.toJSONString(message));
		return MessageHelper.wrap("result",true,"message",op+" 操作已成功發送！");
	}

	@Override
	public String operateFlowDeploy(long appId, String versionId,
			String instanceId, String nodeId) {
		// 蓝图实例id
		long blueinstanceId = applicationService.getApp(appId)
				.getBlueInstanceId();
		// 蓝图实例
		Map<String, Object> map = dao
				.getBlueprintInstanceById((int) blueinstanceId);
		int biId = (int) map.get("ID");
		String bpId = map.get("INSTANCE_ID").toString();
		Map<String, List<? extends Object>> message = new HashMap<>();
		Application app = applicationService.getAppInfo(appId + "");
		Long key = app.getKey();
		List<String> insts = new ArrayList<>();
		// 同版本的实例信息
		List<Map<String, Object>> oldInsList = rcService
				.getInstanceOfAppAndVersion(appId, versionId);

		// 部署
		Map<String, Object> oldIns = oldInsList.get(0);
		// 创建实例，入库ma_instance
		Instance ins = new Instance();
		ins.setId(instanceId);
		ins.setAppId(String.valueOf(appId));
		ins.setNodeId(nodeId);
		ins.setAppVersionId(versionId);
		ins.setComponentInput(JSON.parseObject(oldIns.get("inputTemp")
				.toString(), new TypeReference<Map<String, String>>() {
		}));
		ins.setComponentOutput(JSON.parseObject(oldIns.get("outputTemp")
				.toString(), new TypeReference<Map<String, String>>() {
		}));
		instanceService.saveInstance(ins);
		insts.add(instanceId);
		// 修改蓝图中的信息

		message.put(bpId + "_" + key, messageService.messages(insts, "deploy"));
		// 放置到消息管理器中
		this.putMessage(message);
		RestTemplate rest = new RestTemplate();
		// insts列表中只有一个实例的id
		String id = insts.get(0);
		long flowId = getFlowIdOne(id, "deploy");
		// 子流程需要的参数
		Map<String, String> varMap = new HashMap<String, String>();
		varMap.put("_taskBKey", bpId + "_" + key);
		String insvarMap = JSON.toJSONString(varMap);
		String encoderJson = "";
		try {
			encoderJson = URLEncoder.encode(insvarMap, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String startUrl = flowServerUrl
				+ "/WFService/startPdByPidAndUsers.wf?pdId=" + flowId
				+ "&initiator=admin" + "&insvarMap=" + encoderJson;
		System.out.println(insvarMap);
		String startResult = rest.getForObject(startUrl, String.class);
		System.out.println(startResult);
		handleResult(startResult, "deploy", biId);
		System.out.println(JSON.toJSONString(message));
		return MessageHelper.wrap("result",true,"message","deploy 操作已成功發送！");
	}

	@Override
	public long getEmptyFlow() {
		return bluePrintTypeDao.getEmptyFlow();
	}

	public void generateInstanceFile(String instanceId,String elementName,Map<String, String> config, String versionName){
		String path = Utils.TMPDIR;
		Map<String, String> fileMap = new HashMap<>();
		fileMap = generaComponentInstance(path, elementName ,instanceId,versionName);
		Map<String,Object> map  = new  HashMap<String,Object>();
		Map<String,String> extreMap  = new  HashMap<String,String>();
		map = CreateComponentFile.commonMap(instanceId);
		for(Map.Entry<String,String> entry:config.entrySet()){
			String key = entry.getKey();
			String freeVlaue = 	"${".concat(key).concat("?if_exists}");
			extreMap.put(key, freeVlaue);
		}
		map.putAll(extreMap);
		SDKUtil sdkUtil = new SDKUtil();
		Map<String,Object> resultMap  = sdkUtil.configSdk(map,fileMap,"UTF-8");
		if((boolean) resultMap.get("result")){
			log.info((String)resultMap.get("message"));
		}
	}
	public  Map<String, String> generaComponentInstance(String path,String elementName,String instanceId, String versionName){
		Map<String, String> fileMap = new HashMap<>();
		String parentPath = path + elementName + File.separator + versionName;
		File parentPathFolder = new File(parentPath);
		//2.0版本以后蓝图保存时还没有此缓存目录
		if(!parentPathFolder.exists()){
			parentPathFolder.mkdirs();
			Map<String, Object> instanceDetail = instanceService.findByInstanceId(instanceId);
			com.dc.appengine.appmaster.entity.Version versionDetail = resourceDao
					.getResourceVersion("" + instanceDetail.get("RESOURCE_VERSION_ID"));
			String resourcePath = versionDetail.getResourcePath();
			int index = resourcePath.indexOf("/");
			String resourceParent = resourcePath.substring(0, index);
			String resourceFile = resourcePath.substring(index + 1);
			try{
				boolean download = Utils.downloadFile(url, port, user, pwd, resourceParent, resourceFile, parentPath);
				if (download) {
					File file = new File(parentPath + File.separator + resourceFile);
					Utils.unZip(file, parentPath);
					file.deleteOnExit();
				}
				String md5 = versionDetail.getMd5();
				File f = new File(parentPath + File.separator + versionName + ".md5");
				f.createNewFile();
				FileOutputStream fos = new FileOutputStream(f);
				IOUtils.write(md5, fos);
			}catch(Exception e){
				log.error(e.getMessage(),e);
			}
		}
		FileUtil fileutil = new FileUtil();
		List<String>  fileList = fileutil.traverseFolder2(parentPath);
		File file = new File(path+instanceId);
		File filesFile = new File(path+instanceId+"/files");
		File confFile = new File(path+instanceId+"/conf");
		File templatesFile = new File(path+instanceId+"/templates");
		file.mkdirs();
		filesFile.mkdirs();
		confFile.mkdirs();
		templatesFile.mkdirs();
		//		if (!file.exists()) {
		//			file.mkdirs();
		//		}
		for (String string : fileList) {
			//			string = string.replace("\\", "/");
			String newFilePath = string.replace("/"+elementName+"/", "/"+instanceId+"/");
			newFilePath = newFilePath.replace("/" + versionName, "");
			fileMap.put(string, newFilePath);

		}
		return fileMap;
	}




	//将页面上的蓝图流程转为中间流程
	public Map<String,Object> generateSpecificBPFlow(BluePrint bp,String flowType){
		//首先生成简单的部署停止卸载
		List<Element> elements = bp.getNodeDataArray();
		List<Element> roots = getRootElements(elements);
		List<List<Element>> rootandsubs = new ArrayList<List<Element>>();
		for (Element root : roots) {
			rootandsubs.add(getElementChilds(root, elements));
		}
		List<TreeNode> nodes = generateMap1(roots, elements);
		System.out.println(nodes);
		System.out.println("-----------------------");
		List<NodeData> nodeDataArray = new ArrayList<>();
		List<LinkData> linkDataArray = new ArrayList<>();
		NodeData start = new NodeData.Builder().key(Integer.MIN_VALUE).text("开始").flowcontroltype(1)
				.category("event").eventType(1).eventDimension(1).item("start").build();
		NodeData end = new NodeData.Builder().key(Integer.MAX_VALUE).text("结束").flowcontroltype(2)
				.category("event").eventType(1).eventDimension(8).item("End").build();
		nodeDataArray.add(start);
		for(TreeNode treeNode:nodes){
			linkDataArray.add(new LinkData(Integer.MIN_VALUE,(int) treeNode.getE().getKey(),null));
			recur(treeNode,flowType,nodeDataArray,linkDataArray);
		}
		nodeDataArray.add(end);
		//处理生成的流程，没有子流程的组件要从里面剔除出去，处理逻辑是找到没有子流程的组件，找到它的前继和后继
		Iterator<NodeData> it = nodeDataArray.iterator();
		while(it.hasNext()){
			NodeData nd = it.next();
			if(StringUtils.isNotEmpty(nd.componetName) 
					&& StringUtils.isNotEmpty(nd.versionId) 
					&& StringUtils.isEmpty(nd.subflowid)){
				//如果是组件并且组件的子流程是空的
				int key = nd.key;
				LinkData before = null;
				List<LinkData> afters = new ArrayList<>();
				//一个前继，可能多个后继
				Iterator<LinkData> ldit=linkDataArray.iterator();
				while(ldit.hasNext()){
					LinkData ld = ldit.next();
					if(ld.to == key){
						before = ld;
						ldit.remove();
					}else if(ld.from == key){
						afters.add(ld);
						ldit.remove();
					}
				}
				//新增线
				for(LinkData after:afters){
					linkDataArray.add(new LinkData(before.from,after.to,null));
				}
				it.remove();
			}
		}
		return newBp(nodeDataArray,linkDataArray,flowType);
	}

	private void recur(TreeNode treeNode,String flowType,List<NodeData> nodeDataArray,List<LinkData> linkDataArray){
		nodeDataArray.add(convertElementToNode(treeNode.getE(),flowType));
		List<TreeNode> children = treeNode.getChildren();
		if(children==null || children.size()==0){
			//指向结束，或者指向聚合节点
			linkDataArray.add(new LinkData((int) treeNode.getE().getKey(),Integer.MAX_VALUE,null));
			return;
		}else{
			for(TreeNode child:children){
				recur(child,flowType,nodeDataArray,linkDataArray);
				int from = (int) treeNode.getE().getKey();
				int to = (int) child.getE().getKey();
				double[] points = new double[]{};
				linkDataArray.add(new LinkData(from,to,null));
			}
		}
	}

	/*
	 * 根据界面传递过来的蓝图模型生成一个启动的流程
	 * 生成规则，如果有启动线，那么就按启动线处理，如果没有启动线，默认并发启动所有组件
	 * 生成的流程图中有开始节点和结束节点
	 */
	private Map<String,Object> generateStartBPFlow(BluePrint immutable) throws Exception{
		BluePrint bp = (BluePrint) Utils.deepClone(immutable);
		List<StartBluePrint> links = bp.getLinkDataArray();
		//首先生成简单的部署停止卸载
		List<Element> elements = bp.getNodeDataArray();
		List<Element> roots = getRootElements(elements);
		List<List<Element>> rootandsubs = new ArrayList<List<Element>>();
		for (Element root : roots) {
			rootandsubs.add(getElementChilds(root, elements));
		}
		List<TreeNode> nodes = generateMap1(roots, elements);
		List<NodeData> nodeData1 = new ArrayList<>();
		for(TreeNode tn:nodes){
			Element e = tn.getE();
			//动态资源设置为1
			if(e.getPooltype() == Element.DYNAMICRESOURCE)
				e.setIns(1);
			recur(tn,nodeData1);
		}
		//数目设置完成
		List<LinkData> linkData = new ArrayList<>();
		List<NodeData> nodeData = new ArrayList<>();
		NodeData start = new NodeData.Builder().key(Integer.MIN_VALUE).text("开始").flowcontroltype(1)
				.category("event").eventType(1).eventDimension(1).item("start").build();
		NodeData end = new NodeData.Builder().key(Integer.MAX_VALUE).text("结束").flowcontroltype(2)
				.category("event").eventType(1).eventDimension(8).item("End").build();
		List<NodeData> allNodes = new ArrayList<>();
		allNodes.add(start);
		allNodes.addAll(nodeData1);
		allNodes.add(end);
		if(links.size()==0){
			//如果蓝图中没有线，那么就默认并发启动所有组件节点
			Iterator<NodeData> it = nodeData1.iterator();
			while(it.hasNext()){
				//首先移除掉所有没有启动的节点，之所以没有在外层移除，是考虑到用户可能某个组件没有画启动流程，但是也加了启动线
				if(StringUtils.isEmpty(it.next().subflowid)){
					it.remove();
				}
			}
			for(NodeData nd:nodeData1){
				linkData.add(new LinkData(start.key,nd.key, null));
				if(nd.ins>1){
					Integer newKey = this.getNewKey(allNodes);
					NodeData join = new NodeData.Builder().key(newKey).text("聚合").category("gateway").flowcontroltype(5).build();
					join.gatewayType=2;
					allNodes.add(join);
					linkData.add(new LinkData(nd.key, join.key, null));
					linkData.add(new LinkData(join.key,end.key, null));
				}else{
					linkData.add(new LinkData(nd.key,end.key, null));
				}
			}
			nodeData=allNodes;
			Collections.sort(nodeData,new Comparator<NodeData>(){
				@Override
				public int compare(NodeData o1, NodeData o2) {
					long k1 = o1.key;
					long k2 = o2.key;
					long r = k1 - k2;
					if(r==0) 
						return 0;
					else
						return r>0?1:-1;
				}
			});
		}else{
			//如果有线，只考虑线 ，首先去掉无用的线
			removeRedundant(links);
			Set<LinkData> ldSet = new HashSet<>();
			Set<NodeData> ndSet = new TreeSet<>(new Comparator<NodeData>(){
				@Override
				public int compare(NodeData o1, NodeData o2) {
					long k1 = o1.key;
					long k2 = o2.key;
					long r = k1 - k2;
					if(r==0) 
						return 0;
					else
						return r>0?1:-1;
				}
			});
			Map<Integer,NodeData> map=new HashMap<>();
			for(StartBluePrint sbp:links){
				//反向考虑
				int to = (int) sbp.getFrom();
				int from = (int) sbp.getTo();
				if(!map.containsKey(from)){
					Integer newKey = this.getNewKey(allNodes);
					NodeData join = new NodeData.Builder().key(newKey).text("聚合").category("gateway").flowcontroltype(5).build();
					join.gatewayType=2;
					allNodes.add(join);
					map.put(from, join);
				}
				if(!map.containsKey(to)){
					Integer newKey = this.getNewKey(allNodes);
					NodeData join = new NodeData.Builder().key(newKey).text("聚合").category("gateway").flowcontroltype(5).build();
					join.gatewayType=2;
					allNodes.add(join);
					map.put(from, join);
				}
			}
			for(StartBluePrint sbp:links){
				//反向考虑
				int to = (int) sbp.getFrom();
				int from = (int) sbp.getTo();
				NodeData fromNd = getNodeDataByKey(allNodes, from);
				NodeData toNd = getNodeDataByKey(allNodes, to);
				ndSet.add(fromNd);
				ndSet.add(toNd);
				//如果已存在指向to节点
				boolean existPointToNd = false;
				LinkData existNode = null;
				for(LinkData ld:ldSet){
					if(ld.to == toNd.key){
						existPointToNd=true;
						existNode = ld;
					}
				}
				if(existPointToNd){
					ndSet.add(map.get(from));
					ldSet.add(new LinkData(from,map.get(from).key,null));
					ldSet.add(new LinkData(map.get(from).key,existNode.from,null));
				}else{
					ndSet.add(map.get(from));
					ldSet.add(new LinkData(from,map.get(from).key,null));
					ldSet.add(new LinkData(map.get(from).key,to,null));
				}
			}
			//无前继加开始，无后继加结束
			for(NodeData nd:ndSet){
				boolean hasBefore = false;
				boolean hasAfter = false;
				for(LinkData ld:ldSet){
					if(ld.from == nd.key)
						hasAfter = true;
					if(ld.to == nd.key)
						hasBefore = true;
				}
				if(!hasBefore)
					ldSet.add(new LinkData(start.key,nd.key,null));
				if(!hasAfter)
					ldSet.add(new LinkData(nd.key,end.key,null));
			}
			ndSet.add(start);
			ndSet.add(end);
			linkData.addAll(ldSet);
			nodeData.addAll(ndSet);
		}
		return newBp(nodeData,linkData,"start");
	}

	private void removeRedundant(List<StartBluePrint> links){
		MultiValuedMap<Integer, Integer> map = new ArrayListValuedHashMap<>();
		for(StartBluePrint l:links){
			int to =(int) l.getFrom();
			int from = (int) l.getTo();
			map.put(from, to);
		}
		List<Integer> keys = new ArrayList<>();
		keys.addAll(map.keySet());
		for(Integer key:keys){
			Collection<Integer> collection = map.get(key);
			Iterator<StartBluePrint> it = links.iterator();
			while(it.hasNext()){
				StartBluePrint sbp = it.next();
				int from = (int) sbp.getTo();
				int to = (int) sbp.getFrom();
				//如果某个key的孩子
				if(from != key
						&&collection.contains(from)
						&&collection.contains(to)){
					collection.remove(to);
				}
			}
		}
		Iterator<StartBluePrint> it = links.iterator();
		while(it.hasNext()){
			StartBluePrint sbp = it.next();
			int from = (int) sbp.getTo();
			int to = (int) sbp.getFrom();
			if(!map.get(from).contains(to)){
				it.remove();
			}
		}
	}

	private Map<String,Object> generateStopBPFlow(BluePrint immutable,String flowType) throws Exception{
		BluePrint bp = (BluePrint) Utils.deepClone(immutable);
		List<NodeData> nodes = new ArrayList<>();
		List<LinkData> links = new ArrayList<>();
		List<NodeData> nodeData1 = new ArrayList<>();
		//首先需要设置正确的数目，这样才能有正确的子流程数
		List<Element> elements = bp.getNodeDataArray();
		List<Element> roots = getRootElements(elements);
		List<List<Element>> rootandsubs = new ArrayList<List<Element>>();
		for (Element root : roots) {
			rootandsubs.add(getElementChilds(root, elements));
		}
		List<TreeNode> treeNodes = generateMap1(roots, elements);
		for(TreeNode tn:treeNodes){
			Element e = tn.getE();
			//动态资源设置为1
			if(e.getPooltype() == Element.DYNAMICRESOURCE)
				e.setIns(1);
			recur(tn,nodeData1);
		}
		//
		NodeData start = new NodeData.Builder().key(Integer.MIN_VALUE).text("开始").flowcontroltype(1)
				.category("event").eventType(1).eventDimension(1).item("start").build();
		NodeData end = new NodeData.Builder().key(Integer.MAX_VALUE).text("结束").flowcontroltype(2)
				.category("event").eventType(1).eventDimension(8).item("End").build();
		for(Element ele:elements){
			if(ele.getEleType().equals(Element.COMPONENT)){
				Version v = ele.getVersionlist().get(0);
				String resourceVersionId = v.getResourceVersionId();
				Map<String,Object> detail=resourceService.getResourceVersionDetail(resourceVersionId);
				Map<String,String> flows = (Map<String, String>) detail.get("flows");
				if(flows.containsKey(flowType) && flows.get(flowType)!=null){
					//有子流程的组件才添加到流程中
					nodes.add(convertElementToNode(ele, flowType));
				}
			}
		}

		for(NodeData nd:nodes){
			links.add(new LinkData(start.key,nd.key,null));
			links.add(new LinkData(nd.key,end.key,null));
		}
		if(links.size()==0){
			links.add(new LinkData(start.key,end.key,null));
		}
		nodes.add(start);
		nodes.add(end);
		Collections.sort(nodes, new Comparator<NodeData>(){
			@Override
			public int compare(NodeData o1, NodeData o2) {
				long k1 = o1.key;
				long k2 = o2.key;
				long r = k1 - k2;
				if(r==0) 
					return 0;
				else
					return r>0?1:-1;
			}
		});
		return newBp(nodes, links, flowType);

	}

	private static Map<String,Object> newBp(List<NodeData> nodeData,List<LinkData> linkData,String type){
		Map<String,Object> newBp = new HashMap<String,Object>();
		newBp.put("class", "go.GraphLinksModel");
		newBp.put("issub", false);
		newBp.put("modelData", JSON.parseObject("{\"position\": \"0 0\"}",new TypeReference<Map<String,Object>>(){}));
		newBp.put("nodeDataArray", nodeData);
		newBp.put("linkDataArray", linkData);
		newBp.put("type", type);
		return newBp;
	}

	private NodeData getNodeDataByKey(List<NodeData> allNodes,int key){
		for(NodeData nd:allNodes){
			if(nd.key == key){
				return nd;
			}
		}
		return null;
	}

	private void recur(TreeNode treeNode,List<NodeData> nodeData1){
		Element father = treeNode.getE();
		boolean isComponent = father.getEleType().equals(Element.COMPONENT); 
		if(isComponent){
			nodeData1.add(convertElementToNode(father, "start"));
		}
		long myIns = father.getIns();
		List<TreeNode> children = treeNode.getChildren();
		for(TreeNode child:children){
			//对每一个孩子设置Ins
			child.getE().setIns(child.getE().getIns()*myIns);
		}
		for(TreeNode child:children){
			recur(child,nodeData1);
		}
	}

	@Override
	public Map<String,Object> generateSpecificBPFlows(BluePrint bp) throws Exception {
		Map<String,Object> deploy = this.generateSpecificBPFlow(bp, "deploy");
		Map<String,Object> start = this.generateStartBPFlow(bp);
		Map<String,Object> stop = this.generateStopBPFlow(bp, "stop");
		Map<String,Object> destroy = this.generateStopBPFlow(bp, "destroy");
		Map<String,Object> result = new HashMap<>();
		result.put("deploy", deploy);
		result.put("start", start);
		result.put("stop", stop);
		result.put("destroy", destroy);
		return result;
	}

	private Integer getNewKey(List<NodeData> nodes){
		Set<Integer> set = new HashSet<>();
		for(NodeData nd:nodes){
			set.add(nd.key);
		}
		int i = 0;
		while(i<100){
			int key = (int) (Math.random()*1000000);
			if(!set.contains(key)){
				return key;
			}
			i++;
		}
		throw new RuntimeException("不能生成新的key值");
	}

	private NodeData convertElementToNode(Element ele,String flowType){
		NodeData nd = new NodeData.Builder().key((int)ele.getKey()).text(ele.getText())
				.category("subflow").item("service task").build();
		if(ele.getEleType().equals(Element.RESOURCE)){
			if(ele.getPooltype()==1){
				nd.text="静态资源池";
			}else if(ele.getPooltype()==2){
				nd.text="动态资源池";
			}else if(ele.getPooltype()==3){
				nd.text="云资源池";
			}
			nd.flowcontroltype=10;
			nd.ins=(int) ele.getIns();
			nd.taskType=6;
		}else if(ele.getEleType().equals(Element.COMPONENT)){
			Version v = ele.getVersionlist().get(0);
			nd.flowcontroltype=0;
			String resourceVersionId = v.getResourceVersionId();
			Map<String,Object> resourceVersionDetail = resourceService.getResourceVersionDetail(resourceVersionId);
			Map<String,String> flows = (Map<String, String>) resourceVersionDetail.get("flows");
			nd.componetName=ele.getText();
			nd.ins=(int) ele.getIns();
			nd.subflowName=flowType;
			nd.subflowid=flows.get(flowType);
			nd.versionId=resourceVersionId;
			nd.config=new HashMap<String,Object>();
			nd.taskType=6;
		}
		return nd;
	}


	@Override
	public String getFlowInfo(String flowId) {
		Map<String,Object> result= new HashMap<>();
		Map<String,Object> flowInfoMap = dao.getFlowInfoById(flowId);
		String flowInfo =null;
		if(flowInfoMap !=null && !flowInfoMap.isEmpty()){
			flowInfo =	(String) flowInfoMap.get("FLOW_INFO");//转换前流程信息
		}
		result.put("flowInfo", JSONObject.parseObject(flowInfo));
		return JSON.toJSONString(result);
	}

	//add by dipj 模型转换 from cd to workflow
	//注意此api中flow需要给出bluePrintId(蓝图实例id、组件子流程id、自定义流程id)，issub(蓝图子流程为false，组件子流程为true，自定义流程为false)，type(蓝图子流程和组件子流程最好给出类型，如此子流程是deploy、start、stop、destroy，自定义流程可有可无)
	public String cdflowToSmartflow(String flow) {
		flow = putSubFlowId(flow);
		Map<String, Object> params = new HashMap<>();
		params.put("CZRY_DM", "00000000000");//工作流用户
		params.put("PDJSON", flow);//流程
		//RestTemplate restUtil = new RestTemplate();
		RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();
		String json = JSON.toJSONString(params);
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("cdflow", json);
		//解决传输过程中中文乱码
		HttpHeaders headers = new HttpHeaders();
		MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
		headers.setContentType(type);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(body,headers);
		String result = restUtil.postForObject(flowServerUrl + "/WFService/cdflowToSmartflow.wf",
				requestEntity, String.class);
		JSONObject resultJson = JSON.parseObject(result);
		if (resultJson.getBooleanValue("state")) {
			System.out.println("流程生成成功....." + result);
			return resultJson.getString("data");
		} else {
			System.out.println("流程生成失败....." + result);
			return null;
		}
	}

	private String putSubFlowId(String flow) {
		JSONObject json = JSON.parseObject(flow);
		//蓝图子流程
		if(!json.getBoolean("issub")){
			JSONArray nodes = json.getJSONArray("nodeDataArray");
			for(int i = 0; i < nodes.size(); i++){
				JSONObject node = nodes.getJSONObject(i);
				int flowcontroltype = node.getIntValue("flowcontroltype");
				//组件子流程
				if(flowcontroltype == 0){
					//组件子流程在master的cdFlowId
					String cdFlowId = node.getString("cdFlowId");
					Map<String, Object> flowDetail = resourceDao.getNewFlowDetailByFlowId(cdFlowId);
					String subflowid = "" + flowDetail.get("FLOW_ID");
					//加入组件子流程在工作流的subflowid
					node.put("subflowid", subflowid);
				}
			}
			return JSON.toJSONString(json);
		}
		//组件子流程
		else{
			return flow;
		}
	}

	@Override
	public void saveBluePrintFlows(Map<String, Object> flows,String bluePrintInsId) {
		for(Entry<String,Object> entry:flows.entrySet()){
			String flowType = entry.getKey();
			Map<String,Object> flow = (Map<String, Object>) entry.getValue();
			flow.put("bluePrintId", bluePrintInsId);
			flow.put("type", flowType);
			flow.put("issub", false);
			String info = this.cdflowToSmartflow(JSON.toJSONString(flow));
			if(info == null){
				throw new RuntimeException("保存"+flowType+"失败");
			}else{
				BluePrintType bluePrintType = new BluePrintType();
				bluePrintType.setBlueprint_id(bluePrintInsId);
				bluePrintType.setFlow_id(Long.valueOf(info));
				bluePrintType.setFlow_type(flowType);
				bluePrintType.setFlow_info(JSON.toJSONString(flow));
				this.saveBluePrintType(bluePrintType);
			}
		}
	}

	@Override
	public String logRecord(String logJson) {
		JSONObject json= JSON.parseObject(logJson);
		String state = json.get("state").toString();
		String tokenId = json.get("id").toString();//tokenid
		String flowNodeLog= json.get("flowNodeLog").toString();
		//先查询该节点的日志是否存在
		Map<String, Object> nodeLog= logDao.findFlowNodeLog(tokenId);
		int count=0;
		String log = "node proccess log:";
		if(nodeLog != null && !nodeLog.isEmpty()){
			String id = nodeLog.get("id").toString();
			if(nodeLog.get("log") !=null ){
				log = nodeLog.get("log").toString();
			}
			log=log+System.lineSeparator()+flowNodeLog;
			count= logDao.updateFlowNodeLog(id, log,state);
		}else{
			log=log+System.lineSeparator()+flowNodeLog;
			json.put("flowNodeLog", log);
			count = logDao.saveFlowNodeLog(json);
		}
		if(count >0){
			return MessageHelper.wrap("result",true,"message","节点日志保存成功！");
		}
		return MessageHelper.wrap("result",false,"message","节点日志保存失败！");
	}

	@Override
	public Map<String, Object> getBlueprintType(String flowId) {
		return bluePrintTypeDao.getBlueprintTypeByFlowId(Long.valueOf(flowId));
	}

	@Override
	public String getNodesLogRecord(String tokenId) {
		Map<String,Object> result= new HashMap<>();
		Map<String,Object> logMap = logDao.findFlowNodeLog(tokenId);
		String pluginLog =null;
		String curMethod="";
		String subStartLog= "";
		String preLog= "";
		String postLog= "";
		String invokeLog= "";
		String activeLog= "";
		String regex = "ENC\\(\\w*\\)";
		Pattern pattern = Pattern.compile(regex);
		if(logMap !=null){
			StringBuffer stringBuilderSubStart= new StringBuffer();
			StringBuffer stringBuilderPre= new StringBuffer();
			StringBuffer stringBuilderPost= new StringBuffer();
			StringBuffer stringBuilderInoke= new StringBuffer();
			StringBuffer stringBuilderActive= new StringBuffer();
			pluginLog = logMap.get("log").toString();
			ByteArrayInputStream bai= new ByteArrayInputStream(pluginLog.getBytes());
			BufferedReader br = new BufferedReader(new InputStreamReader(bai));
			String tmp;
			try {
				while ((tmp = br.readLine()) != null) {
					//区别日志阶段标识
					if(tmp.contains("do start")){//子流程开始节点日志
						curMethod="subStart";
					}else if(tmp.contains("do preAction")){
						curMethod="preAction";
					}else if(tmp.contains("do invoke")){
						curMethod="invoke";
					}else if(tmp.contains("do doActive")){
						curMethod="doActive";
					}else if(tmp.contains("do postAction")){
						curMethod="postAction";
					}
					if(tmp.indexOf("ENC(") != -1){
						Matcher matcher = pattern.matcher(tmp);
						tmp = matcher.replaceAll("*********");
					}
					//根据阶段标识分类日志
					if(curMethod.equals("subStart")){
						stringBuilderSubStart.append(tmp).append(System.lineSeparator());
					}else if(curMethod.equals("preAction")){
						stringBuilderPre.append(tmp).append(System.lineSeparator());
					}else if(curMethod.equals("invoke")){
						stringBuilderInoke.append(tmp).append(System.lineSeparator());
					}else if(curMethod.equals("doActive")){
						stringBuilderActive.append(tmp).append(System.lineSeparator());
					}else if(curMethod.equals("postAction")){
						stringBuilderPost.append(tmp).append(System.lineSeparator());
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			subStartLog=stringBuilderSubStart.toString();
			preLog= stringBuilderPre.toString();
			postLog= stringBuilderPost.toString();
			invokeLog= stringBuilderInoke.toString();
			activeLog= stringBuilderActive.toString();
		}
		result.put("start", FormatUtil.formatJson(subStartLog));
		result.put("preLog", FormatUtil.formatJson(preLog));
		result.put("postLog", FormatUtil.formatJson(postLog));
		result.put("invokeLog", FormatUtil.formatJson(invokeLog));
		result.put("activeLog", FormatUtil.formatJson(activeLog));
		return JSON.toJSONString(result);
	}

	@Override
	public String prepareSubFlowMessage(Map subFlowInfo) throws Exception{
		Map<String,Object> info = subFlowInfo;
		//流程干预专用
		String regetmsg = info.get("regetmsg").toString();
		String cdInstanceId = info.get("cdInstanceId").toString();
		//开始节点组消息失败记录日志专用
		String startTokenId = info.get("tokenId").toString();
		//workflow父流程id
		String flowInstanceId = info.get("flowInstanceId").toString();
		//子流程key
		String flowKey = info.get("flowKey").toString();
		//流程id
		String flowId = info.get("flowId").toString();
		//流程全局变量，蓝图实例、快照从流程变量中获取
		Map<String,Object> insvarMap = (Map<String, Object>) info.get("insvarMap");
		
		String userId = "" + insvarMap.get("_userName");
		String cdFlowName = null;
		String operation = null;
		String operationMessage = null;
		String componentName = null;
		
		String extended = "" + insvarMap.get("_extendedAttributes");
		Map<String, Object>  extendedAttributes = JSON.parseObject(extended, new TypeReference<Map<String, Object>>(){});
		
		String lock = flowInstanceId+"_"+flowKey;
		if(records.containsKey(lock)){
			AtomicInteger ai = records.get(lock);
			int i = ai.decrementAndGet();
			if(i == 0){
				records.remove(lock);
			}
			return MessageHelper.wrap("result",true,"message","message already ok.");
		}else{
			tryLock(lock);
			try {
				//上锁成功之后再次判断记录map中是否包含该key，如果包含，说明前面已经有放置消息了，返回即可
				if(records.containsKey(lock)){
					AtomicInteger ai = records.get(lock);
					int i = ai.decrementAndGet();
					if(i == 0){
						records.remove(lock);
					}
					return MessageHelper.wrap("result",true,"message","message already ok.");
				}
				//查询实例信息，向消息管理器中放消息
				String blueprintInstanceId = (String) insvarMap.get("_blueprintId");
				Map<String, Object> blueInstance = dao.getBlueprintInstance(blueprintInstanceId);
				String blueInstanceName = (String) blueInstance.get("INSTANCE_NAME");
				String blueInstanceInfo = "" + blueInstance.get("INFO");
				JSONObject infoMap = JSON.parseObject(blueInstanceInfo);
				JSONArray blueprintNodeArray = infoMap.getJSONArray("nodeDataArray");
				String blueprintId = "" + blueInstance.get("ID");
				Map<String,Object> flowType = bluePrintTypeDao.getBlueprintTypeByFlowId(Long.valueOf(flowId));
				String fi = (String) flowType.get("FLOW_INFO");
				Map<String,Object> flowInfo = JSON.parseObject(fi,new TypeReference<Map<String,Object>>(){});
				List<Map<String,Object>> nodeDataArray = (List<Map<String, Object>>) flowInfo.get("nodeDataArray");
				int messageNum = 0;
				Map<String, List<? extends Object>> message = new HashMap<>();
				List<Object> messages = new ArrayList<>();
				for(Map<String,Object> m:nodeDataArray){
					if(m.get("key").toString().equals(flowKey)){
						//找到那个key对应的组件子流程，然后封装消息
						String appName =  (String) m.get("text");
						String subflowName = (String) m.get("subflowName");
						String cdFlowId = "" + m.get("cdFlowId");
						String versionConfig = m.get("versionConfig").toString();
						Map<String, String> param = new HashMap<>();
						param.put("appName", appName);
						param.put("id", blueprintId);
						Map<String, Object> appConfig = dao.getBluerintAppByAppName(param);
						String appNodeDisplay = "" + appConfig.get("NODE_NAME");
						int poolSize = 0;
						boolean isMeddle = "true".equals(regetmsg) ? true : false;//指定实例干预或无实例id干预
						boolean isSingleMeddle = !"".equals(cdInstanceId) ? true : false;//指定实例干预
						boolean isIncrease = "true".equals(insvarMap.get("_rc_increase")) ? true : false;
						boolean isReduce = "true".equals(insvarMap.get("_rc_reduce")) ? true : false;
						
						Map<String, Object> flowDetail = resourceDao.getNewFlowDetailByFlowId(cdFlowId);
						cdFlowName = "" + flowDetail.get("FLOW_NAME");
						componentName = "" + appConfig.get("COMPONENT_NAME");
						if("deploy".equals(subflowName)){
							operation = ResourceCode.Operation.DEPLOY;
							operationMessage = "部署";
						}
						else if("start".equals(subflowName)){
							operation = ResourceCode.Operation.START;
							operationMessage = "启动";
						}
						else if("stop".equals(subflowName)){
							operation = ResourceCode.Operation.STOP;
							operationMessage = "停止";
						}
						else if("destroy".equals(subflowName)){
							operation = ResourceCode.Operation.DESTROY;
							operationMessage = "卸载";
						}
						else if("upgrade".equals(subflowName)){
							operation = ResourceCode.Operation.UPGRADE;
							operationMessage = "升级";
						}
						else if("rollback".equals(subflowName)){
							operation = ResourceCode.Operation.ROLLBACK;
							operationMessage = "回滚";
						}
						else{
						}
						
						JSONObject appPool = getPoolByApp(blueprintNodeArray, appName);
						Map<String, Object> ipMap = new HashMap<>();
						//干预
						if(isSingleMeddle){
							poolSize = 1;
						}
						//收缩
						else if(isReduce){
							ipMap = getNewReduceSize(blueprintInstanceId, appPool.getString("key"));
							int size = (Integer)ipMap.get("ins");
							if(size == 0){
								ipMap = getPoolSize(blueprintInstanceId, appPool.getString("key"));
								poolSize = (Integer)ipMap.get("ins");
							}
							else{
								poolSize = size;
							}
						}
						//扩展和普通相同
						else{
							ipMap = getPoolSize(blueprintInstanceId, appPool.getString("key"));
							poolSize = (Integer)ipMap.get("ins");
						}
						List<Map<String, Object>> instances = new ArrayList<Map<String, Object>>();
						{
							//流程干预，根据cdInstanceId实例组消息
							if(isSingleMeddle){
								Map<String, Object> cdInstanceMap = instanceService.findByInstanceId(cdInstanceId);
								instances.add(cdInstanceMap);
								if(instances == null || instances.size() == 0){
									String error = "流程干预失败:蓝图实例[" + blueInstanceName + "]中组件[" + appNodeDisplay + "]的实例[" + cdInstanceId + "]不存在记录！";
									throw new Exception(error);
								}
							}
							else{
								//master查询ma_instance表获取当前组件的所有实例(资源池的ins数和组件的实例数相同)，为每个实例构造Map，组成list封装成消息对象
								instances = instanceService.findInstancesBybp(blueInstanceName, appName, null);
								if(instances == null || instances.size() == 0){
									String error = "蓝图实例[" + blueInstanceName + "]中组件[" + appNodeDisplay + "]不存在实例记录！";
									throw new Exception(error);
								}
							}
						}
						
						//部署、启动、停止、卸载、升级、快照组装消息机制相同，其中只有nodeIp从数据库实例信息中获取，其他从蓝图实例的组件配置中获取、插件配置在组件子流程中获得
						//回滚消息从快照中获取蓝图实例的配置和蓝图组件的配置
						{
							Map<String,Object> snapshotDetail = null;
							//if(COMMONFLOW.contains(subflowName)){
								//snapshotDetail = null;
							//}
							//如果是回滚或者快照不为空字符串
							String snapshotId = (String)insvarMap.get("_blueprintSnapshotId");
							if(snapshotId != null){
								//快照id是否存在，不管子流程是否为回滚，兼容所有回滚流程(而不是只为蓝图回滚服务)"rollback".equalsIgnoreCase(subflowName)
								if(!"".equals(snapshotId)){
									//获取snapshotId
									if("".equals(snapshotId)){
										String error = "蓝图实例[" + blueInstanceName + "]的回滚流程[" + flowType.get("FLOW_NAME") + "]未指定快照ID";
										throw new Exception(error);
									}
									
									Map<String,Object> snapshot = dao.getSnapshotDetailById(snapshotId);
									if(snapshot == null){
										String error = "蓝图实例[" + blueInstanceName + "]的回滚流程[" + flowType.get("FLOW_NAME") + "]的快照ID[" + snapshotId + "]不存在";
										throw new Exception(error);
									}
									else{
										String snapshotInfo = (String) snapshot.get("SNAPSHOT_INFO");
										snapshotDetail = JSON.parseObject(snapshotInfo,new TypeReference<Map<String,Object>>(){});
									}
								}
							}
							for(Map<String,Object> instanceConfig : instances){
								String status = "" + instanceConfig.get("STATUS");
								//收缩
								if(isReduce){
									if(!RESOURCE_POOL_DELETE.equals(status)){
										continue;
									}
								}
								//扩展和普通相同
								else{
									if(RESOURCE_POOL_DELETE.equals(status)){
										continue;
									}
								}
								if(versionConfig==null ||"".equals(versionConfig)||"none".equals(versionConfig)|| "current".equals(versionConfig)||"target".equals(versionConfig)){
									Map<String, Object> map = getPreMap(isMeddle, subflowName, versionConfig, cdFlowId, instanceConfig, appConfig, blueInstance, snapshotDetail, extendedAttributes);
									messages.add(map);
									if(!isSingleMeddle){
										String hostIp = (String)map.get("hostIp");
										((List)ipMap.get("ips")).remove(hostIp);
									}
								}
								else if("current+target".equals(versionConfig)){
									Map<String,Map<String, Object>> ctMap = new HashMap<String,Map<String, Object>>();
									ctMap.put("current", getPreMap(isMeddle, subflowName, "current", cdFlowId, instanceConfig, appConfig, blueInstance, snapshotDetail, extendedAttributes));
									ctMap.put("target", getPreMap(isMeddle, subflowName, "target", cdFlowId, instanceConfig, appConfig, blueInstance, snapshotDetail, extendedAttributes));
									messages.add(ctMap);
									if(!isSingleMeddle){
										String hostIp = (String)(ctMap.get("current").get("hostIp"));
										((List)ipMap.get("ips")).remove(hostIp);
									}
								}
							}
							//messages按ip排序
							//messages = sortMessage(messages);
							//如果静态资源池动态获取的ins个数大于消息个数，则不足的组空消息
							if(poolSize > messages.size()){
								int messagesSize = messages.size();
								for(int i = 0; i < poolSize - messagesSize; i++){
									Map<String, Object> hostIpMap = new HashMap<String, Object>();
									hostIpMap.put("hostIp", ((List)ipMap.get("ips")).get(i));
									messages.add(hostIpMap);
								}
							}
							message.put(flowInstanceId+"_"+flowKey, messages);
							messageNum = messages.size();
						}
						
						System.out.println("*********************workflowMap*******************************");
						System.out.println(subFlowInfo);
						System.out.println("*********************workflowMap*******************************");
						
						System.out.println("*************************master封装消息正常******************************");
						System.out.println("消息个数==" + poolSize);
						System.out.println("消息内容==" + messages);
						System.out.println("*************************master封装消息正常******************************");
						
						System.out.println("*************************开始发送消息到frame******************************");
						String putResult = this.putMessage(message);
						System.out.println("消息发送后frame返回结果==" + putResult);
						System.out.println("*************************发送消息到frame结束******************************");
						//放到记录当中，并减一
						//ins==1不用上锁，ins>1上锁
						if(messageNum - 1 > 0){
							records.put(lock, new AtomicInteger(messageNum - 1));
						}
						try {
							auditService.save(
									new AuditEntity(userId, ResourceCode.COMPONENTFLOW, componentName, operation, 1,
											"组件[" + componentName + "]执行[" + operationMessage + "]类型的流程[" + cdFlowName + "]"));
						} catch (Exception e) {
							e.printStackTrace();
						}
						return MessageHelper.wrap("result",true,"message","success");
					}
				}
				if(message.size() == 0 || messages.size() == 0){
					String error = "蓝图流程中未找到匹配的子流程节点key[" + flowKey + "]";
					throw new Exception(error);
				}
			}catch(Exception e){
				System.out.println("*************************master封装消息异常******************************");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				String error = format.format(date) + " do start 封装消息遇到异常，流程实例id_key:"+lock + "! reason[" + FormatUtil.printStackTrace(e) + "]";
				e.printStackTrace();
				log.error(error, e);
				//记录开始节点错误日志，供流程监控查看
				Map<String, String> errorMap = new HashMap<String, String>();
				errorMap.put("state", "false");
				errorMap.put("id", startTokenId);
				errorMap.put("flowNodeLog", error);
				String errorJson = JSON.toJSONString(errorMap);
				logRecord(errorJson);
				System.out.println("*************************master封装消息异常******************************");
				try {
					auditService.save(
							new AuditEntity(userId, ResourceCode.COMPONENTFLOW, componentName, operation, 0,
									"组件[" + componentName + "]执行[" + operationMessage + "]类型的流程[" + cdFlowName + "]失败"));
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				throw e;
			}finally{
				locks.remove(lock);
				System.out.println("移除锁："+lock);
			}
		}
		return MessageHelper.wrap("result",false,"message","failed");
	}

	@Override
	public String prepareSubFlowMessageTest(Map subFlowInfo) throws Exception{
		Map<String,Object> info = subFlowInfo;
		//流程干预专用
//		String cdInstanceId = info.get("cdInstanceId").toString();
		String cdInstanceId = "";
		//开始节点组消息失败记录日志专用
//		String startTokenId = info.get("tokenId").toString();
		//workflow父流程id
		String flowInstanceId = info.get("flowId").toString();
		//子流程key
		String flowKey = info.get("flowKey").toString();
		//流程id
//		String flowId = info.get("flowId").toString();
		//流程全局变量，蓝图实例、快照从流程变量中获取
//		Map<String,Object> insvarMap = (Map<String, Object>) info.get("insvarMap");
		Map<String, Object> insvarMap = new HashMap<>();
		insvarMap.put("_blueprintId", info.get("blueprintInstanceId"));

//		String lock = flowInstanceId+"_"+flowKey;
//		if(records.containsKey(lock)){
//			AtomicInteger ai = records.get(lock);
//			int i = ai.decrementAndGet();
//			if(i == 0){
//				records.remove(lock);
//			}
//			return MessageHelper.wrap("result",true,"message","message already ok.");
//		}else{
//			tryLock(lock);
//			//上锁成功之后再次判断记录map中是否包含该key，如果包含，说明前面已经有放置消息了，返回即可
//			if(records.containsKey(lock)){
//				AtomicInteger ai = records.get(lock);
//				int i = ai.decrementAndGet();
//				if(i == 0){
//					records.remove(lock);
//				}
//				return MessageHelper.wrap("result",true,"message","message already ok.");
//			}
			//查询实例信息，向消息管理器中放消息
			try {
				String blueprintInstanceId = (String) insvarMap.get("_blueprintId");
				Map<String, Object> blueInstance = dao.getBlueprintInstance(blueprintInstanceId);
				String blueInstanceName = (String) blueInstance.get("INSTANCE_NAME");
				String blueprintId = "" + blueInstance.get("ID");

//				Map<String,Object> flowType = bluePrintTypeDao.getBlueprintTypeByFlowId(Long.valueOf(flowId));
				Map<String,Object> flowType = bluePrintTypeDao.getBlueprintTypeById(flowInstanceId);
				String fi = (String) flowType.get("FLOW_INFO");
				Map<String,Object> flowInfo = JSON.parseObject(fi,new TypeReference<Map<String,Object>>(){});
				List<Map<String,Object>> nodeDataArray = (List<Map<String, Object>>) flowInfo.get("nodeDataArray");
				int messageNum = 0;
				Map<String, List<? extends Object>> message = new HashMap<>();
				List<Object> messages = new ArrayList<>();
				for(Map<String,Object> m:nodeDataArray){
					if(m.get("key").toString().equals(flowKey)){
						//找到那个key对应的组件子流程，然后封装消息
						String appName =  (String) m.get("text");
						String subflowName = (String) m.get("subflowName");
						String cdFlowId = "" + m.get("cdFlowId");
						String versionConfig = m.get("versionConfig").toString();
						List<Map<String, Object>> instances = new ArrayList<Map<String, Object>>();
						List<Map<String,String>> instancesList = new ArrayList<>();
						//RC
						if("true".equals(insvarMap.get("_rc_increase"))||"true".equals(insvarMap.get("_rc_reduce"))){
							Map<String, Object> rcDetail = dao.getRcDetailsByBlueprintId(blueprintId);
							if("true".equals(insvarMap.get("_rc_increase"))){
								String increaseAppInstance = "" + rcDetail.get("INCREASE_APP_INSTANCE");
								Map<String, List<Map<String, String>>> increaseAppInstanceMap = JSON.parseObject(increaseAppInstance,new TypeReference<Map<String, List<Map<String,String>>>>(){});
								instancesList = increaseAppInstanceMap.get(appName);
							}
							else if("true".equals(insvarMap.get("_rc_reduce"))){
								String reduceAppInstance = "" + rcDetail.get("REDUCE_APP_INSTANCE");
								Map<String, List<Map<String, String>>> reduceAppInstanceMap = JSON.parseObject(reduceAppInstance,new TypeReference<Map<String, List<Map<String,String>>>>(){});
								instancesList = reduceAppInstanceMap.get(appName);
							}
							else{
							}
							if(instancesList == null || instancesList.size() == 0){
								String error = "蓝图实例[" + blueInstanceName + "]中组件[" + appName + "]不存在RC实例！";
								throw new Exception(error);
							}
						}
						//非RC
						else{
							//根据cdInstanceId为流程干预组织特定的组件实例
							if(!"".equals(cdInstanceId)){
								Map<String, Object> cdInstanceMap = instanceService.findByInstanceId(cdInstanceId);
								instances.add(cdInstanceMap);
								if(instances == null || instances.size() == 0){
									String error = "流程干预失败:蓝图实例[" + blueInstanceName + "]中组件[" + appName + "]的实例[" + cdInstanceId + "]不存在记录！";
									throw new Exception(error);
								}
							}
							else{
								//master查询ma_instance表获取当前组件的所有实例(资源池的ins数和组件的实例数相同)，为每个实例构造Map，组成list封装成消息对象
								instances = instanceService.findInstancesBybp(blueInstanceName, appName, null);
								if(instances == null || instances.size() == 0){
									String error = "蓝图实例[" + blueInstanceName + "]中组件[" + appName + "]不存在实例记录！";
									throw new Exception(error);
								}
							}
							for(Map<String, Object> inst:instances){
								Map<String,String> map = new HashMap();
								map.put("instanceId", "" + inst.get("ID"));
								map.put("appId", String.valueOf(inst.get("APP_ID")));
								instancesList.add(map);
							}
						}
						
						//部署、启动、停止、卸载、升级、快照组装消息机制相同，其中只有nodeIp从数据库实例信息中获取，其他从蓝图实例的组件配置中获取、插件配置在组件子流程中获得
						//回滚消息从快照中获取蓝图实例的配置和蓝图组件的配置
						{
							Map<String,Object> snapshotDetail = null;
							//if(COMMONFLOW.contains(subflowName)){
								//snapshotDetail = null;
							//}
							//如果是回滚或者快照不为空字符串
							String snapshotId = (String)insvarMap.get("_blueprintSnapshotId");
							if(snapshotId != null){
								if("rollback".equalsIgnoreCase(subflowName) || !"".equals(snapshotId)){
									//获取snapshotId
									if("".equals(snapshotId)){
										String error = "蓝图实例[" + blueInstanceName + "]的回滚流程[" + flowType.get("FLOW_NAME") + "]未指定快照ID";
										throw new Exception(error);
									}
									
									Map<String,Object> snapshot = dao.getSnapshotDetailById(snapshotId);
									if(snapshot == null){
										String error = "蓝图实例[" + blueInstanceName + "]的回滚流程[" + flowType.get("FLOW_NAME") + "]的快照ID[" + snapshotId + "]不存在";
										throw new Exception(error);
									}
									else{
										String snapshotInfo = (String) snapshot.get("SNAPSHOT_INFO");
										snapshotDetail = JSON.parseObject(snapshotInfo,new TypeReference<Map<String,Object>>(){});
									}
								}
							}
							for(Map<String,String> rv:instancesList){
								long appId = Long.parseLong(rv.get("appId"));
								String instanceId = rv.get("instanceId");
								Map<String, Object> tempMap = new HashMap<String, Object>();
								//RC
								if("true".equals(insvarMap.get("_rc_increase"))||"true".equals(insvarMap.get("_rc_reduce"))){
									tempMap.putAll(rv);
								}
								//非RC
								else{
									tempMap.putAll(instanceService.findMessage(instanceId));
								}
								Map<String, Object> tempMap2  = (Map<String, Object>) Utils.deepClone(tempMap);
								if(versionConfig==null ||"none".equals(versionConfig)|| "current".equals(versionConfig)||"target".equals(versionConfig)){
									Map<String, Object> map = getPreMap(instanceId,subflowName,versionConfig,appId,cdFlowId,blueprintInstanceId,tempMap,snapshotDetail);
									messages.add(map);
								}
								else if("current+target".equals(versionConfig)){
									Map<String,Map<String, Object>> ctMap = new HashMap<String,Map<String, Object>>();
									ctMap.put("current", getPreMap(instanceId, subflowName,  "current",  appId,cdFlowId,blueprintInstanceId,tempMap,snapshotDetail));
									ctMap.put("target", getPreMap(instanceId, subflowName,  "target",  appId,cdFlowId,blueprintInstanceId,tempMap2,snapshotDetail));
									messages.add(ctMap);
								}
							}
							message.put(flowInstanceId+"_"+flowKey, messages);
							messageNum = messages.size();
						}
//						this.putMessage(message);
						//放到记录当中，并减一
						//ins==1不用上锁，ins>1上锁
//						if(messageNum - 1 > 0){
//							records.put(lock, new AtomicInteger(messageNum - 1));
//						}
						return MessageHelper.wrap("result",true,"message",messages);
					}
				}
				if(message.size() == 0 || messages.size() == 0){
					String error = "蓝图流程中未找到匹配的子流程节点key[" + flowKey + "]";
					throw new Exception(error);
				}
			}catch(Exception e){
//				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				Date date = new Date();
//				String error = format.format(date) + " do start 封装消息遇到异常，流程实例id_key:"+lock + "! reason[" + FormatUtil.printStackTrace(e) + "]";
//				e.printStackTrace();
//				log.error(error, e);
//				//记录开始节点错误日志，供流程监控查看
//				Map<String, String> errorMap = new HashMap<String, String>();
//				errorMap.put("state", "false");
//				errorMap.put("id", startTokenId);
//				errorMap.put("flowNodeLog", error);
//				String errorJson = JSON.toJSONString(errorMap);
//				logRecord(errorJson);
				throw e;
			}finally{
//				locks.remove(lock);
//				System.out.println("移除锁："+lock);
			}
//		}
		return MessageHelper.wrap("result",false,"message","failed");
	}

	private Map<String, Object> getPreMap(boolean isMeddle, String subflowName, String versionConfig, String cdFlowId, Map<String, Object> instanceConfig, 
						Map<String, Object> appConfig, Map<String, Object> blueprintConfig, Map<String,Object> snapshotDetail, 
						Map<String,Object> extendedAttributes) throws Exception {
		String instanceId = "" + instanceConfig.get("ID");
		String instanceNodeId = "" + instanceConfig.get("NODE_ID");
		String instanceStatus = "" + instanceConfig.get("STATUS");
		String instanceResourceVersionId = "" + instanceConfig.get("RESOURCE_VERSION_ID");
		String instanceParams = instanceConfig.get("PARAMS") == null ? null : "" + instanceConfig.get("PARAMS");
		String appNodeDisplay = "" + appConfig.get("NODE_NAME");
		String appName = "" + appConfig.get("APP_NAME");
		String appId = "" + appConfig.get("ID");
		Node node = nodeService.findNodeById(instanceNodeId);
		if(node == null){
			String error = "组件[" + appNodeDisplay + "]的实例[" + instanceId + "]的node["+ instanceNodeId + "]对应的node不存在！";
			throw new Exception(error);
		}
		String hostIp = node.getIp();
		boolean isSmart = false;
		boolean isExecute = true;
		String blueprintInstanceId = "" + blueprintConfig.get("INSTANCE_ID");
		Map<String, Object> map = new HashMap<>();
		Map<String,Object> appSnapshotMap = null;
		if(snapshotDetail != null){
			if(snapshotDetail.containsKey(appName)){
				String appSnapshotInfo = "" + snapshotDetail.get(appName);
				appSnapshotMap = JSON.parseObject(appSnapshotInfo,new TypeReference<Map<String,Object>>(){});
				isSmart = false;//回滚时智能模式关闭
				isExecute = true;//回滚时执行开关打开
			}
		}
		else{
			isSmart = (Boolean)appConfig.get("SMART_FLAG");
			isExecute = (Boolean)appConfig.get("EXECUTE_FLAG");
		}
		//流程执行、伸缩、干预按照组件配置中执行开关处理，回滚时必须执行
		if(!isExecute){
			Map<String, Object> emptyMap = new HashMap<>();
			emptyMap.put("hostIp", hostIp);
			return emptyMap;
		}
		//非快照，组件配置从ma_application中获取；快照，组件配置从快照中获取
		if(null == versionConfig || "none".equals(versionConfig)){
			if("destroy".equalsIgnoreCase(subflowName) || "stop".equalsIgnoreCase(subflowName)){//current
				map.put("componentInput", appSnapshotMap == null ? "" + appConfig.get("CURRENT_INPUT") : JSON.toJSONString(appSnapshotMap.get("CURRENT_INPUT")));
				map.put("componentOutput", appSnapshotMap == null ? "" + appConfig.get("CURRENT_OUTPUT") : JSON.toJSONString(appSnapshotMap.get("CURRENT_OUTPUT")));
				map.put("resourceVersionId", appSnapshotMap == null ? "" + appConfig.get("CURRENT_VERSION") : appSnapshotMap.get("CURRENT_VERSION"));
				map.put("logicalVersion", "current");
			}else{//target
				map.put("componentInput", appSnapshotMap == null ? "" + appConfig.get("TARGET_INPUT") : JSON.toJSONString(appSnapshotMap.get("TARGET_INPUT")));
				map.put("componentOutput", appSnapshotMap == null ? "" + appConfig.get("TARGET_OUTPUT") : JSON.toJSONString(appSnapshotMap.get("TARGET_OUTPUT")));
				map.put("resourceVersionId", appSnapshotMap == null ? "" + appConfig.get("TARGET_VERSION") : appSnapshotMap.get("TARGET_VERSION"));
				map.put("logicalVersion", "target");
			}
		}else if(versionConfig.contains("current")){
			map.put("componentInput", appSnapshotMap == null ? "" + appConfig.get("CURRENT_INPUT") : JSON.toJSONString(appSnapshotMap.get("CURRENT_INPUT")));
			map.put("componentOutput", appSnapshotMap == null ? "" + appConfig.get("CURRENT_OUTPUT") : JSON.toJSONString(appSnapshotMap.get("CURRENT_OUTPUT")));
			map.put("resourceVersionId", appSnapshotMap == null ? "" + appConfig.get("CURRENT_VERSION") : appSnapshotMap.get("CURRENT_VERSION"));
			map.put("logicalVersion", "current");
		}else if(versionConfig.contains("target")){
			map.put("componentInput", appSnapshotMap == null ? "" + appConfig.get("TARGET_INPUT") : JSON.toJSONString(appSnapshotMap.get("TARGET_INPUT")));
			map.put("componentOutput", appSnapshotMap == null ? "" + appConfig.get("TARGET_OUTPUT") : JSON.toJSONString(appSnapshotMap.get("TARGET_OUTPUT")));
			map.put("resourceVersionId", appSnapshotMap == null ? "" + appConfig.get("TARGET_VERSION") : appSnapshotMap.get("TARGET_VERSION"));
			map.put("logicalVersion", "target");
		}
		if(map.get("resourceVersionId") == null || "".equals((String)map.get("resourceVersionId")) || "null".equals((String)map.get("resourceVersionId"))){
			String error = "蓝图实例中组件[" + appNodeDisplay + "]的[" + map.get("logicalVersion") + "]版本未配置！";
			throw new Exception(error);
		}
		if(appSnapshotMap != null){
			boolean instanceSnapshotParams = false;
			if(appSnapshotMap.get("INSTANCES") != null){
				List<Map<String, Object>> appSnapshotInstances = (List<Map<String, Object>>)appSnapshotMap.get("INSTANCES");
				for(Map<String, Object> appSnapshotInstance : appSnapshotInstances){
					if(instanceId.equals((String)appSnapshotInstance.get("INSTANCE_ID"))){
						instanceParams = (String)appSnapshotInstance.get("PARAMS");
						instanceSnapshotParams = true;
						break;
					}
				}
			}
			if(!instanceSnapshotParams){
				instanceParams = null;
			}
		}
		map.put("blueprintId", blueprintInstanceId);
		String logicalVersion = (String)map.get("logicalVersion");
		//根据resourceVersion查询sv_version表ID
		com.dc.appengine.appmaster.entity.Version version = resourceService.getResourceVersion((String)map.get("resourceVersionId"));
		if(version == null){
			String error = "组件[" + appNodeDisplay + "]的" + logicalVersion + "版本id[" + map.get("resourceVersionId") + "]不存在！";
			throw new Exception(error);
		}
		int resourceType = version.getType();
		StringBuffer compURL = new StringBuffer("ftp://");
		compURL.append(user).append(":ENC(").append(SensitiveDataUtil.encryptDesText(pwd)).append(")@").append(url).append(File.separator);
		StringBuffer ftpPath  = new StringBuffer();
		ftpPath.append("srv").append(File.separator).append(resourceType==1?"salt":"unsalt").append(File.separator).append(appId).append(File.separator).append(logicalVersion).append(File.separator);
		map.put("compURL", compURL.append(ftpPath));
		map.put("artifactURL", version.getFtpLocation());
		//非快照，蓝图配置从ma_blueprint_instance中获取
		if(snapshotDetail == null){
			map.put("blueprintConfig", blueprintConfig.get("KEY_VALUE"));
		}
		//快照，蓝图配置从快照中获取
		else{
			map.put("blueprintConfig", JSON.toJSONString(snapshotDetail.get("KEY_VALUE")));
		}
		map.put("instanceId", instanceId);
		map.put("operation", subflowName);
		map.put("extendedAttributes", extendedAttributes);
		map.put("hostIp", hostIp);
		map.put("nodeUserName", node.getUserName());
		if (node.getUserPassword() != null) {
			String password = AESUtil.defaultDecrpyt(node.getUserPassword());
			password = "ENC(" + SensitiveDataUtil.encryptDesText(password) + ")";
			map.put("nodePassword", password);
		}
		else{
			map.put("nodePassword", "");
		}
		map.put("version", version.getVersionName());
		map.put("componentName", appName);
		//add模板templates.properties
		addConfigTemplate(ftpPath.toString(),map);
		//实例私有配置优先级最高进行变量替换并覆盖组件配置
		if(instanceParams != null){
			map.put("componentParams", instanceParams);
			//实例私有的参数配置的变量替换
			messageService.varibleReplace("componentParams",map,snapshotDetail);
			//实例私有的参数配置覆盖合并到input和output中
			Map<String, Object> inputMap = JSON.parseObject("" + map.get("componentInput"), new TypeReference<Map<String, Object>>() {});
			Map<String, Object> outputMap = JSON.parseObject("" + map.get("componentOutput"), new TypeReference<Map<String, Object>>() {});
			inputMap.putAll((Map<String, Object>)map.get("componentParams"));
			outputMap.putAll((Map<String, Object>)map.get("componentParams"));
			map.put("componentInput", JSON.toJSONString(inputMap));
			map.put("componentOutput", JSON.toJSONString(outputMap));
			map.remove("componentParams");
		}
		//组件配置变量替换
		if(map.get("componentInput") != null && !"".equals((String)map.get("componentInput")) && !"null".equals((String)map.get("componentInput"))){
			messageService.varibleReplace("componentInput",map,snapshotDetail);
		}
		else{
			map.put("componentInput", new HashMap<String, Object>());
		}
		if(map.get("componentOutput") != null && !"".equals((String)map.get("componentOutput")) && !"null".equals((String)map.get("componentOutput"))){
			messageService.varibleReplace("componentOutput",map,snapshotDetail);
		}
		else{
			map.put("componentOutput", new HashMap<String, Object>());
		}
		//添加插件参数
		appendPluginParam(cdFlowId,map,snapshotDetail);
		//干预无视智能模式？
		//if(!isMeddle) 
		{
			//智能判断：版本+状态 
			//FIXME 是否比较组件实例配置，如果比较，结束节点回调需要更新实例配置，而且要在动态变量替换后再比较配置
			if(isSmart){
				if(((String)map.get("resourceVersionId")).equals(instanceResourceVersionId)){
					Map<String, Object> emptyMap = new HashMap<>();
					emptyMap.put("hostIp", hostIp);
					if(subflowName.equals("deploy") || subflowName.equals("stop") || subflowName.equals("upgrade") 
							|| subflowName.equals("rollback")){
						if(instanceStatus.equals("DEPLOYED")){
							//组出的是空消息
							return emptyMap;
						}
					}
					else if(subflowName.equals("start")){
						if(instanceStatus.equals("RUNNING")){
							//组出的是空消息
							return emptyMap;
						}
					}
					else if(subflowName.equals("destroy")){
						if(instanceStatus.equals("UNDEPLOYED")){
							//组出的是空消息
							return emptyMap;
						}
					}
					else if(subflowName.equals("snapshot")){
					}
					else{
					}
				}
			}
		}
		if(resourceType==1){
			String deployPath = "/srv/salt/"+instanceId+"/"+ map.get("logicalVersion");
			map.put("deployPath", deployPath);
		}else{
			Map<String, Object> jobj = (Map<String, Object>)map.get("componentInput");
			map.put("deployPath", jobj.get("deployPath"));
		}
		return map;
	}
	
	private Map<String, Object> getPreMap(String instanceId,String subflowName, String versionConfig, Long appId,String cdFlowId,String blueprintInstanceId,Map<String, Object> tempMap, Map<String,Object> snapshotDetail) throws Exception {
		Application appInfo = applicationService.getApp(appId);
		Map<String, Object> map = prepareInputOutput(subflowName,versionConfig,appId,snapshotDetail);//input output
		map.put("blueprintId", blueprintInstanceId);

		String logicalVersion = (String)map.get("logicalVersion");
		//根据resourceVersion查询sv_version表ID
		com.dc.appengine.appmaster.entity.Version version = resourceService.getResourceVersion((String)map.get("resourceVersionId"));
		if(version == null){
			String error = "组件[" + appInfo.getAppName() + "]的" + logicalVersion + "版本id[" + map.get("resourceVersionId") + "]不存在！";
			throw new Exception(error);
		}
		int resourceType = version.getType();

		StringBuffer compURL = new StringBuffer("ftp://");
		compURL.append(user)
		.append(":")
		.append(pwd)
		.append("@")
		.append(url)
		.append(File.separator);
		StringBuffer ftpPath  = new StringBuffer();
		ftpPath.append("srv")
		.append(File.separator)
		.append(resourceType==1?"salt":"unsalt")
		.append(File.separator)
		.append(appId)
		.append(File.separator)
		.append(logicalVersion)
		.append(File.separator);

		map.put("compURL", compURL.append(ftpPath));
		map.put("artifactURL", version.getFtpLocation());

		//非快照，蓝图配置从ma_blueprint_instance中获取
		if(snapshotDetail == null){
			Map<String, Object> bp = dao.getBlueprintInstanceByInstanceId(blueprintInstanceId);
			map.put("blueprintConfig", bp.get("KEY_VALUE"));
		}
		//快照，蓝图配置从快照中获取
		else{
			map.put("blueprintConfig", JSON.toJSONString(snapshotDetail.get("KEY_VALUE")));
		}

		map.put("instanceId", instanceId);
		map.put("operation", subflowName);
		map.put("hostIp", tempMap.get("nodeIp"));
		map.put("userName", tempMap.get("userName"));
		if (tempMap.get("userPassword") != null) {
			tempMap.put("userPassword", AESUtil.defaultDecrpyt((String) tempMap.get("userPassword")));
		}
		map.put("password", tempMap.get("userPassword"));
		map.put("version", version.getVersionName());
		map.put("componentName", tempMap.get("app_name").toString());

		//add模板templates.properties
		addConfigTemplate(ftpPath.toString(),map);
		//变量替换
		messageService.varibleReplace("componentInput",map,snapshotDetail);
		messageService.varibleReplace("componentOutput",map,snapshotDetail);
		//添加插件参数
		appendPluginParam(cdFlowId,map,snapshotDetail);

		if(resourceType==1){
			String deployPath = "/srv/salt/"+instanceId+"/"+ map.get("logicalVersion");
			map.put("deployPath", deployPath);
		}else{
			Map<String, Object> jobj = (Map<String, Object>)map.get("componentInput");
			map.put("deployPath", jobj.get("deployPath"));
		}

		return map;
	}
	
	private Map<String, Object> prepareInputOutput(String subflowName,String versionConfig,long appId,Map<String,Object> snapshotDetail) throws Exception{
		Map<String, Object> map = new HashMap();
		Application appInfo = applicationService.getApp(appId);
		Map<String,Object> appSnapshotMap = null;
		if(snapshotDetail != null){
			String appName = appInfo.getAppName();
			if(snapshotDetail.containsKey(appName)){
				String appSnapshotInfo = "" + snapshotDetail.get(appName);
				appSnapshotMap = JSON.parseObject(appSnapshotInfo,new TypeReference<Map<String,Object>>(){});
			}
		}
		//非快照，组件配置从ma_application中获取；快照，组件配置从快照中获取
		if(null == versionConfig || "none".equals(versionConfig)){
			if("destroy".equalsIgnoreCase(subflowName) || "stop".equalsIgnoreCase(subflowName)){//current
				map.put("componentInput", appSnapshotMap == null ? appInfo.getCurrentInput() : JSON.toJSONString(appSnapshotMap.get("CURRENT_INPUT")));
				map.put("componentOutput", appSnapshotMap == null ? appInfo.getCurrentOutput() : JSON.toJSONString(appSnapshotMap.get("CURRENT_OUTPUT")));
				map.put("resourceVersionId", appSnapshotMap == null ? appInfo.getCurrentVersion() : appSnapshotMap.get("CURRENT_VERSION"));
				map.put("logicalVersion", "current");
			}else{//target
				map.put("componentInput", appSnapshotMap == null ? appInfo.getTargetInput() : JSON.toJSONString(appSnapshotMap.get("TARGET_INPUT")));
				map.put("componentOutput", appSnapshotMap == null ? appInfo.getTargetOutput() : JSON.toJSONString(appSnapshotMap.get("TARGET_OUTPUT")));
				map.put("resourceVersionId", appSnapshotMap == null ? appInfo.getTargetVersion() : appSnapshotMap.get("TARGET_VERSION"));
				map.put("logicalVersion", "target");
			}
		}else if(versionConfig.contains("current")){
			map.put("componentInput", appSnapshotMap == null ? appInfo.getCurrentInput() : JSON.toJSONString(appSnapshotMap.get("CURRENT_INPUT")));
			map.put("componentOutput", appSnapshotMap == null ? appInfo.getCurrentOutput() : JSON.toJSONString(appSnapshotMap.get("CURRENT_OUTPUT")));
			map.put("resourceVersionId", appSnapshotMap == null ? appInfo.getCurrentVersion() : appSnapshotMap.get("CURRENT_VERSION"));
			map.put("logicalVersion", "current");
		}else if(versionConfig.contains("target")){
			map.put("componentInput", appSnapshotMap == null ? appInfo.getTargetInput() : JSON.toJSONString(appSnapshotMap.get("TARGET_INPUT")));
			map.put("componentOutput", appSnapshotMap == null ? appInfo.getTargetOutput() : JSON.toJSONString(appSnapshotMap.get("TARGET_OUTPUT")));
			map.put("resourceVersionId", appSnapshotMap == null ? appInfo.getTargetVersion() : appSnapshotMap.get("TARGET_VERSION"));
			map.put("logicalVersion", "target");
		}
		if(map.get("resourceVersionId") == null){
			String error = "蓝图实例中组件[" + appInfo.getAppName() + "]的[" + map.get("logicalVersion") + "]版本未配置！";
			throw new Exception(error);
		}
		return map;
	}

	private void addConfigTemplate(String ftpPath,Map<String,Object> map) {
		String downlaodParentPath = "/tmp/"+UUID.randomUUID();
		boolean download = Utils.downloadFile(url, port, user, pwd, ftpPath, "templates.properties", downlaodParentPath);
		if(download){
			File templatesFile = new File(downlaodParentPath + File.separator + "templates.properties");
			Properties configTemplate = this.loadProperties(templatesFile);
			map.put("configTemplate", configTemplate);
			FileUtil.deleteAllFilesOfDir(new File(downlaodParentPath));
		}else{
			log.error("=============下载templates.properties失败==============");
		}
	}

	private Properties loadProperties(File file){
		Properties p = new Properties();
		if(file.exists()){
			try(FileReader fr = new FileReader(file)){
				p.load(fr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;
	}

	private void appendPluginParam(String cdFlowId,Map<String,Object> map,Map<String,Object> snapshotDetail){
		Map<String, Object> flowDetail = resourceDao.getNewFlowDetailByFlowId(cdFlowId);
		JSONObject  componentFlowInfo = JSONObject.parseObject((String) flowDetail.get("FLOW_INFO"));  
		String nodeArrayString = componentFlowInfo.getString("nodeDataArray");
		JSONArray nodeArray = JSONArray.parseArray(nodeArrayString);
		if(nodeArray.size()>0){
			for(int i=0;i<nodeArray.size();i++){
				JSONObject node = ((JSONArray) nodeArray).getJSONObject(i);  
				if(node.get("flowcontroltype").toString().equals("9")){
					Map<String,Object> params = (Map<String, Object>) node.get("params");
					String pluginParamKey = (String) node.get("pluginName")+"_"+node.get("key").toString();
					map.put(pluginParamKey,params);
					messageService.varibleReplace(pluginParamKey,map,snapshotDetail);
					
				}
			}
		}
	}

	private void tryLock(String lock){
		System.out.println("尝试上锁："+lock);
		int i=1000000;
		while(i>0){
			if(locks.containsKey(lock)){
				try {
					Thread.sleep(10L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				i--;
			}else{
				String uuid = UUID.randomUUID().toString();
				String value = locks.putIfAbsent(lock, uuid);
				if(null == value){
					//上锁成功
					System.out.println("上锁成功："+lock);
					break;
				}
			}
		}
	}

	@Override
	public String addBlueprintFlow(String blueprintInstanceId, String flowType, String flowInfo) {
		Map<String,Object> flow = JSON.parseObject(flowInfo, new TypeReference<Map<String,Object>>(){});
		Map<String, Object> flows = new HashMap<>();
		flows.put(flowType, flow);
		this.saveBluePrintFlows(flows,blueprintInstanceId);
		return MessageHelper.wrap("result",true,"message","保存蓝图流程成功");
	}

	@Override
	public String listBpInstanceFlows(String blueprintInstanceId) {
		List<Map<String,Object>> list = bluePrintTypeDao.listBpInstanceFlows(blueprintInstanceId);
		List<Map<String,Object>> result = new ArrayList<>();
		for(Map<String,Object> flow:list){
			Map<String,Object> m = new HashMap<>();
			m.put("flowType", flow.get("FLOW_TYPE"));
			m.put("flowId", flow.get("FLOW_ID"));
			result.add(m);
		}
		return JSON.toJSONString(result);
	}

	@Override
	public String executeBlueprintFlow(String cdFlowId, String blueprintInstanceId, Map<String, String> params) {
		try {
			checkAppConfig(blueprintInstanceId, cdFlowId);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return MessageHelper.wrap("result", false, "message", e.getMessage());
		}
		Map<String,Object> flowInfo = blueprintTemplatedao.getBlueprintTemplateFlowByCdFlowId(cdFlowId);
		String flowId = String.valueOf(flowInfo.get("FLOW_ID"));
		String flowName = flowInfo.get("FLOW_NAME").toString();
		String appName = ""+flowInfo.get("APP_NAME");
		Map<String, Object> map = dao.getBlueprintInstance(blueprintInstanceId);
		int bpid = (int) map.get("ID");
		Map<String, String> insvarMap = new HashMap<String, String>();
		insvarMap.put("_blueprintId", blueprintInstanceId);
		if(params != null){
			insvarMap.putAll(params);
		}
		//RestTemplate rest = new RestTemplate();
		RestTemplate rest = RequestClient.getInstance().getRestTemplate();
		String startUrl = flowServerUrl
				+ "/WFService/startPdByPidAndUsers.wf";
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("pdId", flowId);
		requestEntity.add("initiator", "admin");
		requestEntity.add("insvarMap", JSON.toJSONString(insvarMap));
		String startResult = rest.postForObject(startUrl, requestEntity, String.class);

		System.out.println(startResult);
		saveInstanceId(startResult, blueprintInstanceId,cdFlowId,params);
		//handleResult(startResult, flowType, bpid);
		return JSON.toJSONString(bpInstanceOper(startResult));
	}

	@Override
	public String updateBlueprintFlow(String flowInfo, String flowId) {
		Map<String,Object> old = bluePrintTypeDao.getBlueprintTypeByFlowId(Long.valueOf(flowId));
		String bluePrintInsId = old.get("BLUEPRINT_INSTANCE_ID").toString();
		String flowType = old.get("FLOW_TYPE").toString();
		Map<String,Object> flow = JSON.parseObject(flowInfo, new TypeReference<Map<String,Object>>(){});
		flow.put("bluePrintId", bluePrintInsId);
		flow.put("type", flowType);
		flow.put("issub", false);
		flow.put("workFlowId", flowId);
		String info = this.cdflowToSmartflow(JSON.toJSONString(flow));
		if(info == null){
			return MessageHelper.wrap("result",false,"message","流程转换失败");
		}else{
			Map<String,Object> params = new HashMap<>();
			params.put("blueprintInstanceId", bluePrintInsId);
			params.put("flowInfo", JSON.toJSONString(flow));
			params.put("flowId", flowId);
			bluePrintTypeDao.updateBluePrintTypeByFlowId(params);
			return MessageHelper.wrap("result",true,"message","流程更新成功");
		}
	}

	private String getSnapshotKey(String versionId) {
		String snapshotKey = null;
		Map<String, String> ssFlowMap = new HashMap<String, String>();
		ssFlowMap.put("versionId",versionId);
		ssFlowMap.put("flowType","snapshot");
		String ssFlowId = resourceDao.getFlowByVersionIdAndFlowType(ssFlowMap);
		String ssFlowInfo = this.getSubFlowInfo(Long.parseLong(ssFlowId));
		Map<String,Object> ssFlowInfoMap = JSON.parseObject(ssFlowInfo,new TypeReference<Map<String,Object>>(){});
		List<Map<String,Object>> ssFlowInfoDatas = (List<Map<String, Object>>) ssFlowInfoMap.get("nodeDataArray");
		for(Map<String,Object> data:ssFlowInfoDatas){
			if("plugin".equals(data.get("eleType").toString())){
				if("Snapshot".equals(data.get("pluginName").toString())){
					Map<String, String> paras = (Map<String ,String>)(data.get("params"));
					if(paras.containsKey("ss_variable")){
						snapshotKey = paras.get("ss_variable");
						break;
					}
				}
			}
		}
		return snapshotKey;
	}

	@Override
	//blueflowId参数已废弃
	public String getFlowNodeState(String nodes,String blueflowId) {
		List<Map<String,Object>> nodesList = JSON.parseObject(nodes, new TypeReference<List<Map<String,Object>>>(){});
		List<Map<String,Object>> states=null;
		for(Map<String,Object> node : nodesList){
			String type = node.get("TYPE").toString();
			String nodeId = node.get("ACTIVITYID").toString();
			String flowInstanceId = "";//子流程实例id
			String flowId=node.get("FLOWID").toString();
			//			String nodeName=node.get("NAME").toString();
			if("0".equals(type)){//子流程
				flowInstanceId = node.get("ID").toString();
				nodeId="subFlowNodeId";//存取一致，子流程实例的flowNodeId存的是默认值subFlowNodeId
			}else if("9".equals(type)){//插件
				flowInstanceId = node.get("INSTANCEID").toString();
			}
			//			String nodeText = getNodeText(flowId, type, nodeName);//暂时不用
			states=logDao.findFlowNodeState(flowInstanceId);
			if(states !=null){
				for(Map<String,Object> state : states){
					String flowNodeId=state.get("flowNodeId").toString();
					Object nodeState=state.get("state");
					if(flowNodeId.equals(nodeId)){
						if(nodeState !=null){
							if(nodeState.toString().endsWith("7") || "false".equals(nodeState.toString())){
								nodeState = "7";
								node.put("STATE", Integer.valueOf(nodeState.toString()));
								node.put("update_time", ((Timestamp)(state.get("update_time"))).getTime());
							}
						}
						break;
					}
				}
			}
			Object pluginList = node.get("PLUGINLOG");//node包含插件记录
			if(pluginList !=null){
				List<Map<String,Object>> plugins = (List<Map<String,Object>>)pluginList;
				for(Map<String,Object> plugin : plugins){
					String pluginName = plugin.get("NAME").toString();
					flowId=plugin.get("FLOWID").toString();
					String nodeText = getNodeAlias(flowId, "9", pluginName);
					if(nodeText !=null){
						plugin.put("NAME", nodeText);
					}
					String pluginNodeId = plugin.get("ACTIVITYID").toString();
					for(Map<String,Object> state : states){
						String flowNodeId=state.get("flowNodeId").toString();
						Object nodeState=state.get("state");
						if(flowNodeId.equals(pluginNodeId)){
							if(nodeState !=null){
								if(nodeState.toString().endsWith("7")){
									nodeState = "7";
									plugin.put("STATE", Integer.valueOf(nodeState.toString()));
								}
							}
							break;
						}
					}
				}
			}

		}
		return JSON.toJSONString(nodesList);
	}
	public String getNodeAlias(String flowId,String type,String nodeName){
		//查找plugin的text字段，并将名称替换为text的值
		String text=null;
		Map<String, Object> blueprintType=  this.getBlueprintTypeByFlowId(Long.valueOf(flowId));
		String flowInfo = (String) blueprintType.get("FLOW_INFO");
		Map<String, Object> flowMap = JSONObject.parseObject(flowInfo);
		List<Map<String, Object>>  nodeDataArray =  (List<Map<String, Object>>) flowMap.get("nodeDataArray");//流程节点
		String namekey = "";
		String nameValue = "";
		if("9".equals(type)){
			namekey ="pluginName";
		}else if("0".equals(type)){
			namekey ="componentName";
		}
		for(Map<String, Object> node : nodeDataArray){
			if(node.containsKey(namekey)){
				String name = node.get(namekey).toString();
				String key = node.get("key").toString();
				if("9".equals(type)){
					nameValue =name+"_"+key;
				}else if("0".equals(type)){
					nameValue =name;
				}
				if(nodeName.equals(nameValue)){
					text = (String)node.get("description");
					break;
				}
			}
		}
		return text;
	}

	@Override
	public List<String> getBlueprints() {
		return dao.getBlueprints();
	}

	@Override
	public boolean checkBlueprintFlowUnique(String bpInstanceId,String type) {
		List<Map<String,Object>> flows = bluePrintTypeDao.listBpInstanceFlows(bpInstanceId);
		for(Map<String,Object> flow:flows){
			if(flow.get("FLOW_TYPE").equals(type)){
				return false;
			}
		}
		return true;
	}

	@Override
	public Map<String, Object> getBlueprintTemplate(String bpName) {
		return dao.getBlueprint(bpName);
	}

	private String getSnapshotKeyByFLowId(String flowId) {
		String snapshotKey = null;
		String flowInfo = resourceDao.getNewFlowInfoByFlowId(flowId);
		Map<String,Object> ssFlowInfoMap = JSON.parseObject(flowInfo,new TypeReference<Map<String,Object>>(){});
		List<Map<String,Object>> ssFlowInfoDatas = (List<Map<String, Object>>) ssFlowInfoMap.get("nodeDataArray");
		for(Map<String,Object> data:ssFlowInfoDatas){
			if("plugin".equals(data.get("eleType").toString())){
				if("Snapshot".equals(data.get("pluginName").toString())){
					Map<String, String> paras = (Map<String ,String>)(data.get("params"));
					if(paras.containsKey("ss_variable")){
						snapshotKey = paras.get("ss_variable");
						break;
					}
				}
			}
		}
		return snapshotKey;
	}

	@Override
	public List<Map<String, Object>> getFlowsByBlueInstanceId(String blueprintInstanceId) {
		return bluePrintTypeDao.getFlowsByBlueInstanceId(blueprintInstanceId);
	}

	@Override
	public List<Application> getBlueprintComponents(String blueprintId) {
		List<Application>  apps = applicationDao.getBlueprintComponents(blueprintId);
		for (Application application : apps) {
			SensitiveDataUtil.decryptApplicationConfig(application);
			application.setCurrentVersionName(resourceDao.getVersionName(application.getCurrentVersion()));
			application.setTargetVersionName(resourceDao.getVersionName(application.getTargetVersion()));
		}
		
		return apps;
	}

	@Override
	public Map<String, Object> getBlueprintComponentConfig(String blueprintId, String componentId, String version,
			String resourceVersionId) {
		Application app = applicationDao.findByAppId(componentId);
		SensitiveDataUtil.decryptApplicationConfig(app);
		String currentResourceVersion = app.getCurrentVersion();
		String targetResourceVersion = app.getTargetVersion();
		Map<String, Object> appConfigInput = new HashMap<>();
		Map<String, Object> appConfigOutput = new HashMap<>();

		//1.获取组件版本sv_version 中的input和output getComponentVersionConfig
		com.dc.appengine.appmaster.entity.Version resourceVersion = resourceService.getResourceVersion(resourceVersionId);
		Map<String, Object>  versionconfigInput = new HashMap<>();
		Map<String, Object>  versionconfigOutput = new HashMap<>();
		versionconfigInput = JSON.parseObject(resourceVersion.getInput());
		versionconfigOutput = JSON.parseObject(resourceVersion.getOutput());

		Map<String, Object>  configInput = new HashMap<>();
		Map<String, Object>  configOutput = new HashMap<>();
		
		//2.获取蓝图实例中组件ma_application的input和output 
		if ("current".equals(version)) {
			appConfigInput = JSON.parseObject(app.getCurrentInput());
			appConfigOutput = JSON.parseObject(app.getCurrentOutput());
			if (resourceVersionId.equals(currentResourceVersion)) {
				configInput = getFinalConfig(appConfigInput, versionconfigInput);
				configOutput = getFinalConfig(appConfigOutput, versionconfigOutput);
			}else{
				configInput = versionconfigInput;
				configOutput = versionconfigOutput;
			}
		}else {
			appConfigInput = JSON.parseObject(app.getTargetInput());
			appConfigOutput = JSON.parseObject(app.getTargetOutput());
			if (resourceVersionId.equals(targetResourceVersion)) {
				configInput = getFinalConfig(appConfigInput, versionconfigInput);
				configOutput = getFinalConfig(appConfigOutput, versionconfigOutput);
			}else{
				configInput = versionconfigInput;
				configOutput = versionconfigOutput;
			}
		}
		
		Map<String, Object>  result = new HashMap<>();
		result.put("input", new TreeMap<String, Object>(configInput));
		result.put("output", new TreeMap<String, Object>(configOutput));
		return result;

	}

	public Map<String, Object> getFinalConfig(Map<String, Object> appConfig,Map<String, Object> versionconfig){
		if (null == appConfig) {
			return versionconfig;
		}else{
			for (Entry<String, Object> entry : versionconfig.entrySet()) {
				String key = entry.getKey();
				if ((null !=(String) appConfig.get(key))  && !((String) appConfig.get(key)).isEmpty()) {
					String value = (String) appConfig.get(key);
					versionconfig.put(key, value);
				}


			}
			return versionconfig;
		}
	}

	@Override
	public int updateBlueprintComponentConfig(Map<String, Object> param) {
		return dao.updateBlueprintComponentConfig(param);

	}

	@Override
	public int updateBpInsKeyConfig(Map<String, Object> param) {
		return dao.updateBpInsKeyConfig(param);
	}

	public Map<String, String> computeBlueprintValues(String blueprintId){
		List<Application> apps = applicationDao.getBlueprintComponents(blueprintId);
		Map<String, Object> bp = dao.getBlueprintInstanceByInstanceId(blueprintId);
		Map<String, Object> bpInsConfigValue = JSON.parseObject((String)bp.get("KEY_VALUE"));
		SensitiveDataUtil.decryptMapConfig(bpInsConfigValue);
		Map<String, String> configValue = new HashMap<String,String>();
		for (Application application : apps) {
			Map<String, Object> currentInput = JSON.parseObject(application.getCurrentInput());
			Map<String, Object> currentOutput = JSON.parseObject(application.getCurrentOutput());
			Map<String, Object> targetInput = JSON.parseObject(application.getTargetInput());
			Map<String, Object> targetOutput = JSON.parseObject(application.getTargetOutput());
			getBlueprintValues(currentInput, configValue,bpInsConfigValue);
			getBlueprintValues(currentOutput, configValue,bpInsConfigValue);
			getBlueprintValues(targetInput, configValue,bpInsConfigValue);
			getBlueprintValues(targetOutput, configValue,bpInsConfigValue);
		}
		return configValue;
	}

	public Map<String, String>  getBlueprintValues(Map<String, Object> configs,Map<String, String> configValue,
			Map<String, Object>  bpInsConfigValue){
		if (null!=configs) {
			for (Entry<String,Object> entry : configs.entrySet()) {
				String value = (String) entry.getValue();
				Matcher mat2 = p2.matcher(value);
				while (mat2.find()) {
					value = value.substring(value.lastIndexOf("#")+1, value.lastIndexOf("}"));
					//if (!configValue.contains(value)) {
					if (null != bpInsConfigValue) {
						String bpInsConfig = (String) bpInsConfigValue.get(value);
						if (null != bpInsConfig && !bpInsConfig.isEmpty()) {
							configValue.put(value,bpInsConfig);
						}else{
							configValue.put(value,"");

						}
					}else{
						configValue.put(value,"");
					}
				}
			}
		}

		return configValue;
	}

	@Override
	public Map<String, Object> getBlueprintResourcePoolIns(String message) throws Exception {
		Map<String, String> messageMap = JSON.parseObject(message, new TypeReference<Map<String, String>>(){});
		String key = messageMap.get("nodeKey");
		String blueprintId = messageMap.get("blueprintId");
		String insvar = messageMap.get("insvarMap");
		Map<String, Object> blueInstance = dao.getBlueprintInstance(blueprintId);
		Map<String, String> insvarMap = JSON.parseObject(insvar, new TypeReference<Map<String, String>>(){});
		if(insvarMap != null && "true".equals(insvarMap.get("_rc_reduce"))){
			Map<String, Object> reduceMap = getNewReduceSize(blueprintId, key);
			if((Integer)reduceMap.get("ins") == 0){
				return getPoolSize(blueprintId, key);
			}
			else{
				return reduceMap;
			}
		}
		else{
			return getPoolSize(blueprintId, key);
		}
	}
	
	private Map<String, Object> getPoolSize(String blueprintInatanceId, String key) {
		Map<String, Object> result = new HashMap<>();
		List<String> ips = new ArrayList<>();
		String resourcePoolConfigs = dao.getResourcePoolConfigsByInstanceId(blueprintInatanceId);
		Map<String,Map<String,Object>> configs = JSON.parseObject(resourcePoolConfigs, new TypeReference<Map<String,Map<String,Object>>>(){});
		if(configs != null && configs.size() > 0){
			Map<String, Object> poolConfig = configs.get(key);
			if(poolConfig != null){
				String nodes = "" + poolConfig.get("nodes");
				JSONArray nodesArray = JSON.parseArray(nodes);
				for(int i = 0; i < nodesArray.size(); i++){
					JSONObject node = nodesArray.getJSONObject(i);
					String ip = node.getString("ip");
					ips.add(ip);
				}
			}
		}
		result.put("ins", ips.size());
		result.put("ips", ips);
		return result;
	}

	private Map<String, Object> getReduceSize(Map<String, Object> blueInstance, String key) throws Exception {
		Map<String, Object> result = new HashMap<>();
		int poolSize = 0;
		List<String> poolIps = new ArrayList<>();
		String bpId = "" + blueInstance.get("ID");
		String blueInfo = "" + blueInstance.get("INFO");
		JSONObject infoMap = JSON.parseObject(blueInfo);
		JSONArray blueprintNodeArray = infoMap.getJSONArray("nodeDataArray");
		//获取蓝图的key资源池下所有组件
		List<String> appList = computeAppByPoolKey(blueprintNodeArray, key);
		for(String appName : appList){
			int appSize = 0;
			List<String> appIps = new ArrayList<>();
			Map<String, String> param = new HashMap<>();
			param.put("id", bpId);
			param.put("appName", appName);
			//获取蓝图的组件的所有实例
			List<Map<String, String>> instances = dao.getBluerintAppInstances(param);
			for(Map<String, String> instance : instances){
				String status = "" + instance.get("STATUS");
				if(RESOURCE_POOL_DELETE.equals(status)){
					appSize++;
					String nodeId = "" + instance.get("NODE_ID");
					Node instanceNode = nodeService.findNodeById(nodeId);
					if(instanceNode == null){
						throw new Exception("组件[" + appName + "]的实例node节点[" + nodeId + "]不存在!");
					}
					appIps.add(instanceNode.getIp());
				}
			}
			//在所有组件中找到需要被收缩的最大值
			if(poolSize < appSize){
				poolSize = appSize;
				poolIps = appIps;
			}
		}
		result.put("ins", poolSize);
		result.put("ips", poolIps);
		return result;
	}

	@Override
	public String getBlueprintResourcePoolConfigs(String blueprintId) {
		//		String clusters = adapterService.getAdminClusters();
		//		Map<String, Object> clustersMap = JSON.parseObject(clusters, new TypeReference<Map<String, Object>>(){});
		List<Map<String, String>> clusters = dao.getClusters(1);
		String resourcePoolConfigs = dao.getResourcePoolConfigsById(blueprintId);
		Map<String,Map<String,Object>> configs = JSON.parseObject(resourcePoolConfigs, new TypeReference<Map<String,Map<String,Object>>>(){});
		if(configs != null && configs.size() > 0){
			Iterator<Entry<String, Map<String, Object>>> iter = configs.entrySet().iterator();
			while(iter.hasNext()){
				Entry<String, Map<String, Object>> config = iter.next();
				Map<String, Object> pool = config.getValue();
				String clusterId = "" + pool.get("cluster_id");
				pool.put("cluster_name","");
				for(Map<String, String> cluster : clusters){
					if(clusterId.equals(cluster.get("ID"))){
						pool.put("cluster_name", cluster.get("NAME"));
						break;
					}
				}
			}
			return JSON.toJSONString(configs);
		}
		return resourcePoolConfigs;
	}

	@Transactional
	@Override
	public void updateBlueprintResourcePoolConfigs(Map<String, String> map) {
		String blueprintId = map.get("blueprintId");
		Map<String, Object> blueprint = dao.getBlueprintInstanceById(Integer.parseInt(blueprintId));
		String blueprintinfo = "" + blueprint.get("INFO");
		JSONObject blueprintJson = JSON.parseObject(blueprintinfo);
		String originalConfigs = "" + blueprint.get("RESOURCE_POOL_CONFIG");
		String updateConfigs = map.get("resourcePoolConfig");
		Map<String, Map<String, Object>> originalMaps = JSON.parseObject(originalConfigs,new TypeReference<Map<String, Map<String, Object>>>(){});
		Map<String, Map<String, Object>> updateMaps = JSON.parseObject(updateConfigs, new TypeReference<Map<String, Map<String, Object>>>(){});
		Iterator<Entry<String, Map<String, Object>>> iter = updateMaps.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, Map<String, Object>> entry = iter.next();
			String updateKey = entry.getKey();
			Map<String, Object> updateValue = entry.getValue();
			Map<String, Object> originalValue = originalMaps.get(updateKey);
			if(!compareResourcePool(updateValue, originalValue)){
				JSONArray blueprintNodeArray = blueprintJson.getJSONArray("nodeDataArray");
				Object updateNodes = "" + updateValue.get("nodes");
				List<Map<String, String>> updateNodeList = JSON.parseObject("" + updateNodes, new TypeReference<List<Map<String,String>>>(){});
				//更新组件item
				String originalNodes = "" + originalValue.get("nodes");
				List<Map<String, String>> originalNodeList = JSON.parseObject(originalNodes, new TypeReference<List<Map<String,String>>>(){});
				updateComponentIns(blueprintId, updateKey, blueprintNodeArray, originalNodeList, updateNodeList, updateValue);
				//更新label
				updateNodeLabel(originalValue, updateValue, updateNodeList);
				//更新资源池配置和蓝图实例info
				originalValue.put("ins", updateNodeList.size());
				originalValue.put("label", updateValue.get("label"));
				originalValue.put("nodes", updateNodes);
				for(int i = 0; i < blueprintNodeArray.size(); i++){
					JSONObject item = blueprintNodeArray.getJSONObject(i);
					if("resource".equals(item.getString("eleType")) && updateKey.equals(item.getString("key"))){
						item.put("ins", updateNodeList.size());
						item.put("label", updateValue.get("label"));
						item.put("nodes", updateNodes);
						break;
					}
				}
				Map<String, String> poolConfig = new HashMap<String, String>();
				poolConfig.put("id", blueprintId);
				poolConfig.put("resourcePoolConfig", JSON.toJSONString(originalMaps));
				poolConfig.put("info", JSON.toJSONString(blueprintJson));
				dao.updateResourcePoolConfig(poolConfig);
			}
		}
	}

	private boolean updateNodeLabel(Map<String, Object> originalValue, Map<String, Object> updateConfig, 
			List<Map<String, String>> updateNodeList) throws RuntimeException{
		String oriClusterId = "" + originalValue.get("cluster_id");
		String oriLabel = "" + originalValue.get("label");
		JSONObject oriLabelJson = JSON.parseObject(oriLabel);
		String oriKey = oriLabelJson.getString("key");
		String oriLable = oriLabelJson.getString("value");
		Map<String, Object> delParam = new HashMap<>();
		delParam.put("clusterId", oriClusterId);
		delParam.put("key", oriKey);
		delParam.put("value", oriLable);
		delParam.put("type", "1");
		int d = nodeLabelService.delete(delParam);
		if (d == 0) {
			//throw new RuntimeException("label[" + oriLable + "] 删除失败！");
		}
		String newClusterId = "" + updateConfig.get("cluster_id");
		String newLabel = "" + updateConfig.get("label");
		JSONObject newLabelJson = JSON.parseObject(newLabel);
		String newKey = newLabelJson.getString("key");
		String newLable = newLabelJson.getString("value");
		Map<String, Object> checkParam = new HashMap<>();
		checkParam.put("clusterId", newClusterId);
		checkParam.put("key", newKey);
		checkParam.put("value", newLable);
		checkParam.put("type", "1");
		int c = nodeLabelService.check(checkParam);
		if (c > 0) {
			throw new RuntimeException("label[" + newLabel + "] 已经存在，请重新命名label！");
		}
		HashMap<String, Object> newParams = new HashMap<String, Object>();
		HashMap<String, String> newLabelParams = new HashMap<String, String>();
		newLabelParams.put("type", "1");
		newLabelParams.put("key", newKey);
		newLabelParams.put("value", newLable);
		newParams.put("label", newLabelParams);
		List<String> newNodesParams = new ArrayList<String>();
		for(Map<String, String> update : updateNodeList){
			String updateIp = update.get("ip");
			Map<String, String> param = new HashMap<>();
			param.put("ip", updateIp);
			param.put("clusterId", newClusterId);
			Node updateNode = nodeService.findNodeByClusterAndIp(param);
			if(updateNode == null){
				String error = "环境[" + newClusterId + "]内ip[" + updateIp + "]对应node节点不存在，请检查环境和ip节点配置！";
				throw new RuntimeException(error);
			}
			newNodesParams.add(updateNode.getAdapterNodeId());
		}
		//无node节点
		if(newNodesParams.size() == 0){
			newNodesParams.add("");
		}
		newParams.put("nodes", newNodesParams);
		int i = nodeLabelService.insert(newParams);
		if (i <= 0) {
			throw new RuntimeException("label[" + newLabel + "] 新增失败，请重试！");
		}
		return true;
	}

	private void updateComponentIns(String blueprintId, String updateKey, JSONArray blueprintNodeArray, 
			List<Map<String, String>> originalNodeList, List<Map<String, String>> updateNodeList, Map<String, Object> updatePool)
					throws RuntimeException{
		List<String> appList = computeAppByPoolKey(blueprintNodeArray, updateKey);
		for(String appName : appList){
			Map<String, String> appPara = new HashMap<String, String>();
			appPara.put("id", blueprintId);
			appPara.put("appName", appName);
			Map<String, Object> app = dao.getBluerintAppByAppName(appPara);
			String appId = "" + app.get("ID");
			List<Map<String, String>> instances = dao.getBluerintAppInstances(appPara);
			Map<String, List<String>> computeIpMap = computeUpdateIp(originalNodeList, updateNodeList);
			List<String> addList = computeIpMap.get("add");
			List<String> delList = computeIpMap.get("del");
			for(Map<String, String> instance : instances){
				String instanceId = instance.get("ID");
				String instanceStatus = instance.get("STATUS");
				String instanceNodeId = instance.get("NODE_ID");
				Node instanceNode = nodeService.findNodeById(instanceNodeId);
				if(instanceNode == null){
					String error = "蓝图[" + blueprintId + "]内组件[" + appName + "]实例[" + instanceId + "]的nodeId["+ instanceNodeId + "]对应的node不存在！";
					throw new RuntimeException(error);
				}
				else{
					String instanceNodeIp = instanceNode.getIp();
					if(constainIp(delList, instanceNodeIp)){
						if(RESOURCE_POOL_ADD.equals(instanceStatus)){
							//原状态是RESOURCE_POOL_ADD，合并add+delete，直接删除该实例
							instanceDao.delInstance(instanceId);
						}
						else if(RESOURCE_POOL_DELETE.equals(instanceStatus)){
							//原状态是RESOURCE_POOL_DELETE，合并delete+delete，不变delete
						}
						else{
							//原状态是正常生命周期，合并normal+delete,变更为delete
							Map<String, Object> param = new HashMap<>();
							param.put("instanceId", instance.get("ID"));
							param.put("status", RESOURCE_POOL_DELETE);
							instanceDao.updateInstanceStatus(param);
						}
					}
					if(constainIp(addList, instanceNodeIp)){
						if(RESOURCE_POOL_ADD.equals(instanceStatus)){
							//原状态是RESOURCE_POOL_ADD，合并add+add，不变add
						}
						else if(RESOURCE_POOL_DELETE.equals(instanceStatus)){
							//原状态是RESOURCE_POOL_DELETE，合并delete+add，更新为生命周期初始状态undeployed
							Map<String, Object> param = new HashMap<>();
							param.put("instanceId", instance.get("ID"));
							param.put("status", RESOURCE_INITIAL);
							instanceDao.updateInstanceStatus(param);
						}
						else{
							//原状态是正常生命周期，合并normal+add,不变normal
						}
						//扩容list中移除该ip
						addList.remove(instanceNodeIp);
					}
				}
			}
			//扩容list中剩余ip新增实例状态为RESOURCE_POOL_ADD
			for(String nodeIp : addList){
				Instance newInstance = new Instance();
				String instanceId = UUID.randomUUID().toString();
				newInstance.setId(instanceId);
				newInstance.setAppId(appId);
				newInstance.setStatus(RESOURCE_POOL_ADD);
				Map<String, String> param = new HashMap<>();
				param.put("ip", nodeIp);
				param.put("clusterId", "" + updatePool.get("cluster_id"));
				Node node = nodeService.findNodeByClusterAndIp(param);
				if(node == null){
					String error = "环境[" + param.get("clusterId") + "]内ip[" + nodeIp + "]对应node节点不存在，请检查环境和ip节点配置！";
					throw new RuntimeException(error);
				}
				newInstance.setNodeId(node.getAdapterNodeId());
				instanceDao.saveInstance(newInstance);
			}
		}
	}
	
	private JSONObject getPoolByApp(JSONArray blueprintNodeArray, String appName) {
		Map<Integer, JSONObject> keyMap = new HashMap<Integer, JSONObject>();
		Map<String, JSONObject> appNameMap = new HashMap<String, JSONObject>();
		for(int i = 0; i < blueprintNodeArray.size(); i++){
			JSONObject object = blueprintNodeArray.getJSONObject(i);
			keyMap.put(object.getIntValue("key"), object);
			appNameMap.put(object.getString("text"), object);
		}
		JSONObject app = appNameMap.get(appName);
		int group = app.getIntValue("group");
		return searchPoolByApp(group, keyMap);
	}

	private JSONObject searchPoolByApp(int key, Map<Integer, JSONObject> keyMap) {
		JSONObject keyObject = keyMap.get(key);
		if("resource".equals(keyObject.getString("eleType"))){
			return keyObject;
		}
		else if("component".equals(keyObject.getString("eleType"))){
			return searchPoolByApp(keyObject.getIntValue("group"), keyMap);
		}
		else{
			return null;
		}
	}

	private List<String> computeAppByPoolKey(JSONArray blueprintNodeArray, String updateKey) {
		List<String> appList = new ArrayList<String>();
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		for(int i = 0; i < blueprintNodeArray.size(); i++){
			JSONObject object = blueprintNodeArray.getJSONObject(i);
			map.put(object.getString("key"), object);
		}
		for(int j = 0; j < blueprintNodeArray.size(); j++){
			JSONObject item = blueprintNodeArray.getJSONObject(j);
			String key = item.getString("key");
			if(updateKey.equals(key)){
				continue;
			}
			if(isAppByPoolKey(updateKey, item, map)){
				appList.add(item.getString("text"));
			}
		}
		return appList;
	}

	private boolean isAppByPoolKey(String updateKey, JSONObject app, Map<String, JSONObject> map) {
		if("resource".equals(app.getString("eleType"))){
			if(updateKey.equals(app.getString("key"))){
				return true;
			}
			else{
				return false;
			}
		}
		else if("component".equals(app.getString("eleType"))){
			String group = app.getString("group");
			if(group != null){
				if(updateKey.equals(group)){
					return true;
				}
				else{
					return isAppByPoolKey(updateKey, map.get(group), map);
				}
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}

	private Map<String, List<String>> computeUpdateIp(List<Map<String, String>> originalNodeList, List<Map<String, String>> targetNodeList){
		Map<String, List<String>> computeMap = new HashMap<String, List<String>>();
		computeMap.put("add", new ArrayList<String>());
		computeMap.put("del", new ArrayList<String>());
		for(Map<String, String> oriMap : originalNodeList){
			boolean oriFlag = false;
			for(Map<String, String> tarMap :targetNodeList ){
				if(oriMap.get("ip").equals(tarMap.get("ip"))){
					oriFlag = true;
					break;
				}
			}
			if(!oriFlag){
				computeMap.get("del").add(oriMap.get("ip"));
			}
		}
		for(Map<String, String> tarMap : targetNodeList){
			boolean tarFlag = false;
			for(Map<String, String> oriMap :originalNodeList ){
				if(tarMap.get("ip").equals(oriMap.get("ip"))){
					tarFlag = true;
					break;
				}
			}
			if(!tarFlag){
				computeMap.get("add").add(tarMap.get("ip"));
			}
		}
		return computeMap;
	}

	private boolean constainIp(List<String> ipList, String ip){
		boolean result = false;
		for(String item : ipList){
			if(item.equals(ip)){
				result = true;
				break;
			}
		}
		return result;
	}

	private boolean compareResourcePool(Map<String, Object> updateValue, Map<String, Object> originalValue){
		String updateIns = "" + updateValue.get("ins");
		String updateLabel = "" + updateValue.get("label");
		Map<String, String> updateLabelMap = JSON.parseObject(updateLabel, new TypeReference<Map<String, String>>(){});
		String updateLabelValue = updateLabelMap.get("value");
		String updateNodes = "" + updateValue.get("nodes");
		List<Map<String, String>> updateNodeList = JSON.parseObject(updateNodes, new TypeReference<List<Map<String,String>>>(){});
		String originalIns = "" + originalValue.get("ins");
		String originalLabel = "" + originalValue.get("label");
		Map<String, String> originalLabelMap = JSON.parseObject(originalLabel, new TypeReference<Map<String, String>>(){});
		String originalLabelValue = originalLabelMap.get("value");
		String originalNodes = "" + originalValue.get("nodes");
		List<Map<String, String>> originalNodeList = JSON.parseObject(originalNodes, new TypeReference<List<Map<String,String>>>(){});
		if(updateNodeList.size() != (Integer.parseInt(originalIns)) || !updateLabelValue.equals(originalLabelValue) || !compareNodes(updateNodeList,originalNodeList)){
			return false;
		}
		return true;
	}

	private boolean compareNodes(List<Map<String, String>> original, List<Map<String, String>> update){
		boolean result = true;
		if(original.size() != update.size()){
			result = false;
		}
		else{
			for(Map<String, String> ori : original){
				boolean oriFlag = false;
				for(Map<String, String> upd : update){
					if(ori.get("ip").equals(upd.get("ip"))){
						oriFlag = true;
						break;
					}
				}
				if(!oriFlag){
					result = false;
					break;
				}
			}
			for(Map<String, String> upd : update){
				boolean updFlag = false;
				for(Map<String, String> ori : original){
					if(upd.get("ip").equals(ori.get("ip"))){
						updFlag = true;
						break;
					}
				}
				if(!updFlag){
					result = false;
					break;
				}
			}
		}
		return result;
	}

	@Override
	public void uploadComponentFile(String blueprintId, int componentId, 
			String currentVersion, String targetVersion) {
		if (null != currentVersion &&!currentVersion.isEmpty()) {
			String currentPath = "";
			com.dc.appengine.appmaster.entity.Version currentVsersion = resourceService.getResourceVersion(currentVersion);
			int currentType = currentVsersion.getType();
			if (currentType == 1) {
				currentPath = "/srv/salt/"+componentId+"/current/";
			}else if (currentType == 2) {
				currentPath = "/srv/unsalt/"+componentId+"/current/";
			}
			else{
				log.error("组件类型[" + currentType + "]非法，不会更新模板映射文件");
				return;
			}
			uploadComponenttool(currentVersion, currentPath);
		}
		if (null!=targetVersion && !targetVersion.isEmpty()) {
			String targetPath = "";
			com.dc.appengine.appmaster.entity.Version targetVsersion = resourceService.getResourceVersion(targetVersion);
			int targetType = targetVsersion.getType();
			if (targetType == 1) {
				targetPath = "/srv/salt/"+componentId+"/target/";
			}else if (targetType == 2) {
				targetPath = "/srv/unsalt/"+componentId+"/target/";
			}
			else{
				log.error("组件类型[" + targetType + "]非法，不会更新模板映射文件");
				return;
			}
			uploadComponenttool(targetVersion, targetPath);
		}
	}

	public void  uploadComponenttool(String resourceVersionId,String path){
		List<VersionFtl> currentFtl = resourceService.getVersionFtl(resourceVersionId);
		String temText = "" ;
		//先删除
		FtpUtils.deleteFolder(timeOut, url, port, user, pwd, path);
		
		for (VersionFtl versionFtl : currentFtl) {
			Map<String, Object> templates = JSON.parseObject(versionFtl.getTemplates());
			String ftlPath = "";
			String ftlName = "";
			for (Entry<String,Object> template : templates.entrySet()) {
				String key = (String)template.getKey();
				String value = (String)template.getValue();
				String fileSeparator = "/";
				if (key.contains("/")) {
					fileSeparator = "/";
				} else if (key.contains("\\")) {
					fileSeparator = "\\";
				}
				ftlPath = key.substring(2, key.lastIndexOf(fileSeparator)+1);
//				ftlPath = key.substring(2, key.lastIndexOf(File.separator)+1);
				temText += key+"="+value+"\n";
				ftlName = key.substring(key.lastIndexOf(fileSeparator)+1);
			}
			String ftlText = versionFtl.getFtlText();
			if("".equals(ftlName.trim())){
				ftlName = versionFtl.getFtlName();
				if (ftlName.contains(".")) {
					ftlName = ftlName.substring(0, ftlName.lastIndexOf("."));
				}
				ftlName = ftlName.concat(".").concat("ftl");
			}
			InputStream ftlIs = new ByteArrayInputStream(ftlText.getBytes()); 
			Utils.uploadDirsFile(url, port, user, pwd, path+ftlPath, ftlName, ftlIs);
		}

		InputStream templateIns = new ByteArrayInputStream(temText.getBytes()); 
		Utils.uploadDirsFile(url, port, user, pwd, path, "templates.properties", templateIns);
	}

	@Override
	public String listFlowInstanceIds(int pageSize,int pageNum,String id,String flowId,String sortName,String sortOrder,String instanceId,String flag) {
		if(!JudgeUtil.isEmpty(flag)&&JudgeUtil.isEmpty(instanceId)){
			Page page = new Page(pageSize, 0);
			page.setRows(new ArrayList<>());
			return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
		}
		Boolean lock =false;
		Map<String,Object> condition = new HashMap<>();
		condition.put("id", id);
		condition.put("flowId", flowId);
		condition.put("instanceId", instanceId);
		List list = dao.listFlowInstanceIds(condition);
		int num = list==null?0:list.size();
		Page page = new Page(pageSize, num);
		page.setStartRowNum(pageSize*(pageNum-1));
		page.setEndRowNum(pageSize*pageNum);
		int count=1;
		try {
			lock=restLock4Page.tryLock();
			while(!lock){//等待锁
				if(count>10){
					return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
				}
				Thread.sleep(500);
				count++;
				lock=restLock4Page.tryLock();
			}
//			lock=restLock4Page.tryLock();//页面频繁请求，rest客户端压力太大，使用lock实现同步
//			if(!lock){
//				JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
//			}
			//判断流程是否已结束以及实例状态
			int pageSizeTmp=500;
			int size =list.size();
			//判断流程是否已结束以及实例状态
			if(list.size()>0){
				//当数据量很大时，rest请求容易超时，因此，此处分页调用工作流接口，获取信息
				int pageNumTmp =size%pageSizeTmp==0?size/pageSizeTmp:size/pageSizeTmp+1;
				List resultState = new ArrayList<>();
				for(int i=0;i<pageNumTmp;i++){
					//RestTemplate restUtil = new RestTemplate();
//					System.out.println(System.currentTimeMillis()+"===1");
					List listTmp = new ArrayList<>();
					int fromIndex=i*pageSizeTmp;
					int toIndex=((i+1)*pageSizeTmp)<size?(i+1)*pageSizeTmp:size;
					listTmp.addAll(list.subList(fromIndex,toIndex));
					RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();
					MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
					//防止中文乱码
					MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
					HttpHeaders headers =new HttpHeaders();
					headers.setContentType(type);
					requestEntity.add("instancesList", JSON.toJSONString(listTmp,SerializerFeature.WriteDateUseDateFormat,
							SerializerFeature.DisableCircularReferenceDetect));
					HttpEntity<MultiValueMap<String, String>> requestHttpEntity = new HttpEntity<MultiValueMap<String, String>>(requestEntity,headers);
//					System.out.println(System.currentTimeMillis()+"===2");
					String result = restUtil.postForObject(flowServerUrl+"/WFService/getInstancesStatus.wf", requestHttpEntity, String.class);
//					System.out.println(System.currentTimeMillis()+"===3");
					Map<String, Object> jsonMap = JSON.parseObject(result);
					if((Boolean)jsonMap.get("state")){
						List resultStateTmp = JSON.parseArray(jsonMap.get("data").toString(), Map.class);
						resultState.addAll(resultStateTmp);
					}
//					System.out.println(System.currentTimeMillis()+"===4");
				}
				if(resultState.size()>0){
					SortUtil.sort(resultState, SortUtil.getColunmName("instancelog", sortName), sortOrder);
					List rowsPage=new ArrayList<>();
					int endNum = page.getEndRowNum()>num?num:page.getEndRowNum();
					for(int start=page.getStartRowNum();start<endNum;start++){
						rowsPage.add(resultState.get(start));
					}
					page.setRows(changeList(rowsPage));//获取蓝图中组件的版本详情
				}
			
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			if(restLock4Page !=null && lock){
				restLock4Page.unlock();
			}
		}
		return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
	}
	@Override
	public String getFlowInstanceIds(String id,String flowName) {
		Map<String, Object> param = new HashMap<>();
		param.put("id", id);
		param.put("flowName", flowName);
		List<Map<String,Object>> list = dao.getFlowInstanceIdsByMap(param);
		return JSON.toJSONString(list,SerializerFeature.WriteDateUseDateFormat);
	}
	@Override
	public String getBrotherBlueprintInstance(String blueprintId,String bluePrintInsId) {
		List<Map<String,Object>> list = dao.getBrotherBlueprintInstance(blueprintId,bluePrintInsId);
		return JSON.toJSONString(list);
	}

	@Override
	public String getBlueprintInstanceKV(String bluePrintInsId) {
		String returnStr = dao.getBlueprintInstanceKV(bluePrintInsId);
		Map<String, Object> returnMap = JSON.parseObject(returnStr, new TypeReference<Map<String,Object>>(){});
		return JSON.toJSONString(returnMap);
	}

	@Override
	public String existRunningInstance(String cdFlowId,String blueprintInstanceId) {
		Map<String,Object> flowInfo = blueprintTemplatedao.getBlueprintTemplateFlowByCdFlowId(cdFlowId);
		String flowName = flowInfo.get("FLOW_NAME").toString();
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<>();
		param.put("id", blueprintInstanceId);
		param.put("flowName", flowName);
		Map<String, Object> map = dao.getLatestFlowInstanceId(param);
		if(map !=null && map.get("instanceId") !=null){
			//RestTemplate restUtil = new RestTemplate();
			RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();
			MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
			requestEntity.add("instanceId", map.get("instanceId").toString());
			String result = restUtil.postForObject(flowServerUrl+"/WFService/isRunning.wf", requestEntity, String.class);
			Map<String, Object> jsonMap = JSON.parseObject(result);
			Boolean resultState = (Boolean) jsonMap.get("data");
			if(resultState){
				return MessageHelper.wrap("result", resultState, "message", "蓝图实例存在正在运行的实例");
			}else{
				return MessageHelper.wrap("result", false, "message", "蓝图实例不存在正在在运行的实例");
			}
		}else{
			return MessageHelper.wrap("result", false, "message", "蓝图实例不存在正在运行的实例，可以执行蓝图实例流程");
		}
	}
	
	@Override
	public int checkSnapshotNameOfBlueprintInstance(String blueInstanceId, String snapshotName, int userId) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("blueInstanceId", blueInstanceId);
		map.put("snapshotName", snapshotName);
		int i = dao.checkSnapshotNameOfBlueprintInstance(map);
		return i;
	}
	
	@Override
	public int saveSnapshotOfBlueprintInstance(String blueInstanceId, String snapshotName, int userId) {
		List<Application> apps = applicationDao.getBlueprintComponents(blueInstanceId);
		Map<String, Map<String, Object>> blueprintMap = new HashMap<String, Map<String, Object>>();
		for(Application app : apps){
			Map<String, Object> appMap = new HashMap<String, Object>();
			appMap.put("APP_ID", app.getId());
			appMap.put("COMPONENT_NAME", app.getComponentName());
			appMap.put("APP_NAME", app.getAppName());
			appMap.put("KEY", app.getKey());
			appMap.put("CURRENT_VERSION", app.getCurrentVersion());
			appMap.put("CURRENT_INPUT", json2Map(app.getCurrentInput()));
			appMap.put("CURRENT_OUTPUT", json2Map(app.getCurrentOutput()));
			appMap.put("TARGET_VERSION", app.getTargetVersion());
			appMap.put("TARGET_INPUT", json2Map(app.getTargetInput()));
			appMap.put("TARGET_OUTPUT", json2Map(app.getTargetOutput()));
			appMap.put("NODE_NAME", app.getNodeDisplay());
			appMap.put("SMART_FLAG", app.getSmartFlag());
			appMap.put("EXECUTE_FLAG", app.getExecuteFlag());
			List<Map<String, Object>> appInstances = new ArrayList<>();
			List<Map<String, Object>> instances = instanceDao.listInstancesByAppId(app.getId());
			for(Map<String, Object> instance : instances){
				//工作流回调更新的status、resource_version_id、component_input、component_output未保存
				if(instance.get("params") != null){
					Map<String, Object> appInstance = new HashMap<>();
					appInstance.put("INSTANCE_ID", instance.get("instanceId"));
					appInstance.put("APP_ID", instance.get("appId"));
					appInstance.put("NODE_ID", instance.get("nodeId"));
					appInstance.put("NODE_IP", instance.get("nodeIp"));
					appInstance.put("PARAMS", instance.get("params"));
					appInstances.add(appInstance);
				}
			}
			if(appInstances.size() > 0){
				appMap.put("INSTANCES", appInstances);
			}
			blueprintMap.put(app.getAppName(), appMap);
		}
		Map<String, Object> bp = dao.getBlueprintInstanceByInstanceId(blueInstanceId);
		if(bp.get("KEY_VALUE") != null){
			String keyValue = "" + bp.get("KEY_VALUE");
			blueprintMap.put("KEY_VALUE", json2Map(keyValue));
		}
		else{
			blueprintMap.put("KEY_VALUE", json2Map(null));
		}
		
		Map<String, Object> param = new HashMap<>();
		param.put("blueInstanceId", blueInstanceId);
		param.put("snapshotName", snapshotName);
		param.put("snapInfo", JSON.toJSONString(blueprintMap, SerializerFeature.WriteDateUseDateFormat));
		param.put("userId", userId);
		return dao.saveSnapshotOfBlueprintInstance(param);
	}
	
	private Map<String, Object> json2Map(String jsonString){
		if(jsonString == null || "".equals(jsonString.trim())){
			return new HashMap<String, Object>();
		}
		return JSON.parseObject(jsonString, new TypeReference<Map<String, Object>>(){});
	}
	
	@Override
	public List<Map<String, String>> listSnapshotOfBlueprintInstance(String blueInstanceId, int userId,String sortName,String sortOrder) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("blueInstanceId", blueInstanceId);
		List<Map<String, Object>> snapshots = dao.listSnapshotOfBlueprintInstance(param);
		for(Map<String, Object> snapshot : snapshots){
			Map<String, String> item = new HashMap<String, String>();
			item.put("snapshotId", "" + snapshot.get("ID"));
			item.put("snapshotName", "" + snapshot.get("SNAPSHOT_NAME"));
			list.add(item);
		}
		SortUtil.sort(list, SortUtil.getColunmName("bluePrintInsSnapShot", sortName), sortOrder);
		return list;
	}
	
	@Override
	public String getSnapshotInfoByBlueprintInstance(String blueInstanceId, String snapshotName) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("blueInstanceId", blueInstanceId);
		param.put("snapshotName", snapshotName);
		Map<String, Object> snapshot = dao.getSnapshotDetailByBlueprintInstance(param);
		return "" + snapshot.get("SNAPSHOT_INFO");
	}
	
	@Override
	public int deleteSnapshotByBlueprintInstanceId(String blueInstanceId) {
		int i  = dao.deleteSnapshotByBlueprintInstanceId(blueInstanceId);
		return i;
	}
	
	@Override
	public int deleteSnapshotByBlueprintInstance(String blueInstanceId, String snapshotName) {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("blueInstanceId", blueInstanceId);
		param.put("snapshotName", snapshotName);
		int count = dao.deleteSnapshotByBlueprintInstance(param);
		return count;
	}
	
	@Override
	public void deleteResourcePoolLabelByBlueprintInstanceId(int blueprintInstanceId) {
		String resourcePoolConfigs = dao.getResourcePoolConfigsById(String.valueOf(blueprintInstanceId));
		Map<String,Map<String,Object>> configs = JSON.parseObject(resourcePoolConfigs, new TypeReference<Map<String,Map<String,Object>>>(){});
		if(configs != null && configs.size() > 0){
			Iterator<Entry<String, Map<String, Object>>> iter = configs.entrySet().iterator();
			while(iter.hasNext()){
				Entry<String, Map<String, Object>> config = iter.next();
				Map<String, Object> pool = config.getValue();
				String clusterId = "" + pool.get("cluster_id");
				String label = "" + pool.get("label");
				JSONObject labelJson = JSON.parseObject(label);
				String key = labelJson.getString("key");
				String value = labelJson.getString("value");
				Map<String, Object> delParam = new HashMap<>();
				delParam.put("clusterId", clusterId);
				delParam.put("key", key);
				delParam.put("value", value);
				delParam.put("type", "1");
				nodeLabelService.delete(delParam);
			}
		}
	}

	@Override
	public String getRcResourcePoolConfigByBlueprintInstance(String blueInstanceId, int userId) {
		Map<String, Object> rcDetails = dao.getRcDetailsByBlueprintId(blueInstanceId);
		if(rcDetails != null){
			return "" + rcDetails.get("RC_RESOURCE_POOL");
		}
		else{
			return "{}";
		}
	}

	@Override
	public Map<String, Object> getBlueprintInstanceById(int blueprintId) {
		return dao.getBlueprintInstanceById(blueprintId);
	}
	
	private void saveResourcePoolLabel(String clusterId, String label, String ips, String nodeType) throws Exception {
		JSONObject labelJson = JSON.parseObject(label);
		String key = labelJson.getString("key");
		String lable = labelJson.getString("value");
		Map<String, Object> checkLabel = new HashMap<>();
		checkLabel.put("clusterId", clusterId);
		checkLabel.put("key", key);
		checkLabel.put("value", lable);
		checkLabel.put("type", nodeType);
		int c = nodeLabelService.check(checkLabel);
		//如果已经保存label，忽略
		if (c > 0) {
			return;
		}
		//通过json保存蓝图实例，则保存label
		else{
			HashMap<String, Object> insertLabel = new HashMap<String, Object>();
			HashMap<String, String> labelParams = new HashMap<String, String>();
			labelParams.put("type", nodeType);
			labelParams.put("key", key);
			labelParams.put("value", lable);
			insertLabel.put("label", labelParams);
			JSONArray nodes = JSON.parseArray(ips);
			List<String> nodesParams = new ArrayList<String>();
			for(Object node : nodes){
				String ip = ((Map<String, String>)node).get("ip");
				Map<String, String> param = new HashMap<>();
				param.put("ip", ip);
				param.put("clusterId", clusterId);
				Node nodeItem = nodeService.findNodeByClusterAndIp(param);
				if(nodeItem == null){
					String error = "ip[" + ip + "]的node节点不存在，请双击资源池图标重新配置node节点！";
					throw new Exception(error);
				}
				nodesParams.add(nodeItem.getAdapterNodeId());
			}
			//无node节点
			if(nodesParams.size() == 0){
				throw new Exception("资源池node节点不允许为空，请双击资源池图标配置node节点！");
			}
			insertLabel.put("nodes", nodesParams);
			int i = nodeLabelService.insert(insertLabel);
			if (i <= 0) {
				throw new Exception("label[" + lable + "] 新增失败，请重试！");
			}
		}
	}

	@Override
	public Map<String, Object> getBlueprintTemplateByInsId(String blueprintId) {
		return dao.getBlueprintTemplateByInsId(blueprintId);
	}

	@Override
	public String getFlowInstanceIds(int pageSize, int pageNum,String bluePrintInsId, String flowName, String appName,
			String sortName,String sortOrder) {
		Map<String,Object> condition = new HashMap<>();
		condition.put("id", bluePrintInsId);
		condition.put("flowName", flowName);
		condition.put("appName", appName);
		List list = dao.listFlowInstanceIds(condition);
		int num = list==null?0:list.size();
		Page page = new Page(pageSize, num);
		page.setStartRowNum(pageSize*(pageNum-1));
		page.setEndRowNum(pageSize*pageNum);
		//判断流程是否已结束以及实例状态
		if(list.size()>0){
			//RestTemplate restUtil = new RestTemplate();
			RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();
			MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
			requestEntity.add("instancesList", JSON.toJSONString(list,SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.DisableCircularReferenceDetect));
			String result = restUtil.postForObject(flowServerUrl+"/WFService/getInstancesStatus.wf", requestEntity, String.class);
			Map<String, Object> jsonMap = JSON.parseObject(result);
			if((Boolean)jsonMap.get("state")){
				List resultState = JSON.parseArray(jsonMap.get("data").toString(), Map.class);
				SortUtil.sort(resultState, SortUtil.getColunmName("instancelog", sortName), sortOrder);
				List rowsPage=new ArrayList<>();
				int endNum = page.getEndRowNum()>num?num:page.getEndRowNum();
				for(int start=page.getStartRowNum();start<endNum;start++){
					rowsPage.add(resultState.get(start));
				}
				page.setRows(rowsPage);
			}
		}
		return JSON.toJSONString(page,SerializerFeature.WriteDateUseDateFormat);
	}
	
	@Override
	public List<Map<String, Object>> getSecondFlowsInstanceList(String blueprintInstanceId,String appName) {
		Map<String,Object> paraMap = new HashMap();
		paraMap.put("blueprintInstanceId", blueprintInstanceId);
		paraMap.put("appName", appName);
		return bluePrintTypeDao.getSecondFlowsInstanceList(paraMap);
	}
	
	@Override
	public String getRcResourcePoolByBlueprintInstance(String blueInstanceId, int userId) throws Exception {
		Map<String, Object> blueInstance = dao.getBlueprintInstanceById(Integer.parseInt(blueInstanceId));
		String info = "" + blueInstance.get("INFO");
		String blueInstanceName = "" + blueInstance.get("INSTANCE_NAME");
		JSONObject infoMap = JSON.parseObject(info);
		JSONArray blueprintNodeArray = infoMap.getJSONArray("nodeDataArray");
		String pool = "" + blueInstance.get("RESOURCE_POOL_CONFIG");
		List<String> rcPoolList = new ArrayList<>();
		Map<String, Object> poolMap = JSON.parseObject(pool, new TypeReference<Map<String, Object>>(){});
		for(Entry<String, Object> poolItem : poolMap.entrySet()){
			String key = poolItem.getKey();
			Map<String, Object> value = (Map<String, Object>)poolItem.getValue();
			Map<String, String> increaseMap = new HashMap<>();
			Map<String, String> reduceMap = new HashMap<>();
			List<Map<String, String>> increase_nodes = new ArrayList<Map<String, String>>();
			List<Map<String, String>> reduce_nodes =  new ArrayList<Map<String, String>>();
			List<String> appList = computeAppByPoolKey(blueprintNodeArray, key);
			for(String app : appList){
				List<Map<String, Object>> instances = instanceService.findInstancesBybp(blueInstanceName, app, null);
				for(Map<String, Object> instance : instances){
					String status = "" + instance.get("STATUS");
					if(RESOURCE_POOL_ADD.equals(status) || RESOURCE_POOL_DELETE.equals(status)){
						String nodeId = "" + instance.get("NODE_ID");
						Node node = nodeService.findNodeById(nodeId);
						if(node == null){
							String error = "蓝图[" + blueInstanceName + "]内组件[" + app + "]实例[" + instance.get("ID") + "]的nodeId["+ nodeId + "]对应的node不存在！";
							throw new Exception(error);
						}
						else{
							String ip = node.getIp();
							if(RESOURCE_POOL_ADD.equals(status)){
								if(!increaseMap.containsKey(ip)){
									Map<String, String> map = new HashMap<>();
									map.put("ip", ip);
									increase_nodes.add(map);
									increaseMap.put(ip, "" + instance.get("ID"));
								}
							}
							else{
								if(!reduceMap.containsKey(ip)){
									Map<String, String> map = new HashMap<>();
									map.put("ip", ip);
									reduce_nodes.add(map);
									reduceMap.put(ip, "" + instance.get("ID"));
								}
							}
						}
					}
					else{
						continue;
					}
				}
			}
			if(increase_nodes.size() == 0 && reduce_nodes.size() == 0){
				rcPoolList.add(key);
			}
			value.put("increase_nodes", JSON.toJSONString(increase_nodes));
			value.put("reduce_nodes", JSON.toJSONString(reduce_nodes));
		}
		//移除没有伸缩的资源池配置
		for(String item : rcPoolList){
			poolMap.remove(item);
		}
		return JSON.toJSONString(poolMap);
	}
	
	public String saveInstanceId(String bpInstanceId,String instanceId,String flowName) {
		Map<String, Object> param = new HashMap<>();
		param.put("id", bpInstanceId);
		param.put("instanceId", instanceId);
		param.put("flowName", flowName);
		param.put("appName", "");
		dao.saveFlowInstanceId(param);
		return MessageHelper.wrap("result", true, "message", "流程执行记录保存成功！");
	}
	
	@Override
	public String prepareNewSubFlowMessage(Map subFlowInfo) throws Exception{
		Map<String,Object> info = subFlowInfo;
		//蓝图实例 
		String blueprintInstanceId = info.get("blueprintInstanceId").toString();
		//流程id
		String cdFlowId = info.get("flowId").toString();
		//组件+key
		String appName = info.get("textKey").toString();
		//版本
		String versionConfig = info.get("versionConfig").toString();
		//流程类型
		String subflowName = info.get("subflowName").toString();
		try {
			Map<String, Object> blueInstance = dao.getBlueprintInstance(blueprintInstanceId);
			String blueInstanceName = (String) blueInstance.get("INSTANCE_NAME");
			String blueInstanceInfo = "" + blueInstance.get("INFO");
			JSONObject infoMap = JSON.parseObject(blueInstanceInfo);
			JSONArray blueprintNodeArray = infoMap.getJSONArray("nodeDataArray");
			String blueprintId = "" + blueInstance.get("ID");
			List<Object> messages = new ArrayList<>();
			Map<String, String> param = new HashMap<>();
			param.put("appName", appName);
			param.put("id", blueprintId);
			Map<String, Object> appConfig = dao.getBluerintAppByAppName(param);
			int poolSize = 0;
			JSONObject appPool = getPoolByApp(blueprintNodeArray, appName);
			poolSize = Integer.parseInt("" + (getPoolSize(blueprintInstanceId, appPool.getString("key")).get("ins")));
			List<Map<String, Object>> instances = new ArrayList<Map<String, Object>>();
			{
				instances = instanceService.findInstancesBybp(blueInstanceName, appName, null);
				if(instances == null || instances.size() == 0){
					String error = "蓝图实例[" + blueInstanceName + "]中组件[" + appName + "]不存在实例记录！";
					throw new Exception(error);
				}
			}
			{
				Map<String,Object> snapshotDetail = null;
				for(Map<String,Object> instanceConfig : instances){
					String status = "" + instanceConfig.get("STATUS");
					if(RESOURCE_POOL_DELETE.equals(status)){
						continue;
					}
					if(versionConfig==null ||"none".equals(versionConfig)|| "current".equals(versionConfig)||"target".equals(versionConfig)){
						Map<String, Object> map = getPreMap(false, subflowName, versionConfig, cdFlowId, instanceConfig, appConfig, blueInstance, snapshotDetail, new HashMap<String, Object>());
						messages.add(map);
					}
					else if("current+target".equals(versionConfig)){
						Map<String,Map<String, Object>> ctMap = new HashMap<String,Map<String, Object>>();
						ctMap.put("current", getPreMap(false, subflowName, "current", cdFlowId, instanceConfig, appConfig, blueInstance, snapshotDetail, new HashMap<String, Object>()));
						ctMap.put("target", getPreMap(false, subflowName, "target", cdFlowId, instanceConfig, appConfig, blueInstance, snapshotDetail, new HashMap<String, Object>()));
						messages.add(ctMap);
					}
				}
				//如果静态资源池动态获取的ins个数大于消息个数，则不足的组空消息
				if(poolSize > messages.size()){
					for(int i = 0; i < poolSize - messages.size(); i++){
						messages.add(new HashMap<String, Object>());
					}
				}
			}
			if(messages.size() == 0){
				String error = "蓝图实例中未找到匹配的组件实例!";
				throw new Exception(error);
			}
			return MessageHelper.wrap("result",true,"message",messages);
		}catch(Exception e){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String error = format.format(date) + " do start 封装消息遇到异常! reason[" + FormatUtil.printStackTrace(e) + "]";
			e.printStackTrace();
			log.error(error, e);
			throw e;
		}
	}
	
	//message按ip排序
	private List<Object> sortMessage(List<Object> messages) {
		List<Object> newMessages = new ArrayList<>();
		String[] ips = new String[messages.size()];
		Map<String, Object> messageMap = new HashMap<>();
		for(int i = 0; i < messages.size(); i++){
			Object message = messages.get(i);
			Map<String, Object> map = (Map<String, Object>)message;
			Object hostIp = map.get("hostIp");
			if(hostIp == null){
				hostIp = ((Map)map.get("current")).get("hostIp");
			}
			String ip = "" + hostIp;
			messageMap.put(ip, message);
			ip = ip.replaceAll("(\\d+)", "00$1");
			ip = ip.replaceAll("0*(\\d{3})", "$1");
			ips[i] = ip;
		}
		Arrays.sort(ips);
		for(String newIp : ips){
			newIp = newIp.replaceAll("0*(\\d+)", "$1");
			newMessages.add(messageMap.get(newIp));
		}
		return newMessages;
	}

	@Override
	public void updateBlueprintReducePoolConfig(String blueprintInstanceId) {
		Map<String, Object> blueInstance = dao.getBlueprintInstance(blueprintInstanceId);
		if(blueInstance != null && blueInstance.size() > 0){
			String poolConfig = "" + blueInstance.get("RESOURCE_POOL_CONFIG");
			Map<String, Map<String, Object>> poolMap = JSON.parseObject(poolConfig, new TypeReference<Map<String, Map<String, Object>>>(){});
			List<String> delKeys = new ArrayList<>();
			Set<String> keys = poolMap.keySet();
			for(String key : keys){
				try {
					Map<String, Object> keyMap = getReduceSize(blueInstance, key);
					if((Integer)keyMap.get("ins") == 0){
						delKeys.add(key);
					}
					else{
						Map<String, Object> valueMap = poolMap.get(key);
						List<Map<String, String>> ipList = new ArrayList<>();
						List<String> ips = (List)keyMap.get("ips");
						for(String ip : ips){
							Map<String, String> ipMap = new HashMap<>();
							ipMap.put("ip", ip);
							ipList.add(ipMap);
						}
						valueMap.put("ins", ips.size());
						valueMap.put("nodes", JSON.toJSONString(ipList));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for(String del : delKeys){
				poolMap.remove(del);
			}
			if(poolMap.size() > 0){
				Map<String, String> para = new HashMap<>();
				para.put("blueprintInstanceId", blueprintInstanceId);
				para.put("reducePoolConfig", JSON.toJSONString(poolMap));
				dao.updateBlueprintReducePoolConfig(para);
			}
		}
	}

	@Override
	public Map<String, Map<String, String>> getPluginConfigByBlueprintFlow(String blueprintFlowId, String pluginKey) {
		Map<String, Map<String, String>> pluginMap = new HashMap<>();
		String flowInfo = bluePrintTypeDao.getSubFlowInfo(Long.valueOf(blueprintFlowId));
		Map<String,Object> flowInfoMap = JSON.parseObject(flowInfo,new TypeReference<Map<String,Object>>(){});
		List<Map<String,Object>> nodeDataArray = (List<Map<String, Object>>) flowInfoMap.get("nodeDataArray");
		for(Map<String,Object> node : nodeDataArray){
			if(node.get("key").toString().equals(pluginKey)){
				String pluginName = node.get("pluginName").toString();
				Map<String, String> pluginParams = (Map<String, String>)node.get("params");
				pluginMap.put(pluginName + "_" + pluginKey, pluginParams);
			}
		}
		return pluginMap;
	}
	
	private Map<String, Object> getNewReduceSize(String blueprintInstanceId, String key) throws Exception {
		Map<String, Object> result = new HashMap<>();
		List<String> ips = new ArrayList<>();
		String reducePoolConfigs = dao.getBlueprintReducePoolSize(blueprintInstanceId);
		Map<String,Map<String,Object>> configs = JSON.parseObject(reducePoolConfigs, new TypeReference<Map<String,Map<String,Object>>>(){});
		if(configs != null && configs.size() > 0){
			Map<String, Object> poolConfig = configs.get(key);
			if(poolConfig != null){
				String nodes = "" + poolConfig.get("nodes");
				JSONArray nodesArray = JSON.parseArray(nodes);
				if(nodesArray != null){
					for(int i = 0; i < nodesArray.size(); i++){
						JSONObject node = nodesArray.getJSONObject(i);
						String ip = node.getString("ip");
						ips.add(ip);
					}
				}
			}
		}
		result.put("ins", ips.size());
		result.put("ips", ips);
		return result;
	}
	
	@Override
	public Map<String, Object> getBlueprintInstance(String blueprintInsId){
		return dao.getBlueprintInstance(blueprintInsId);
	}
	
	@Override
	public List<Map<String, Object>> listBlueprintByNameAndTemplateAndApp(Map<String, Object> params) {
		return dao.listBlueprintByNameAndTemplateAndApp(params);
	}
	
	private void checkAppConfig(String blueprintInstanceId, String cdFlowId) throws Exception {
		Map<String, Application> appMap = new HashMap<>();
		List<Application> apps = applicationDao.getBlueprintComponents(blueprintInstanceId);
		for(Application app : apps){
			appMap.put(app.getAppName(), app);
		}
		Map<String, Object> flowMap = blueprintTemplatedao.getBlueprintTemplateFlowByCdFlowId(cdFlowId);
		String flowInfo = "" + flowMap.get("FLOW_INFO_GROUP");
		JSONObject flowJson = JSON.parseObject(flowInfo);
		JSONArray nodes = flowJson.getJSONArray("nodeDataArray");
		for(int i = 0; i < nodes.size(); i++){
			JSONObject node = nodes.getJSONObject(i);
			int flowcontroltype = node.getIntValue("flowcontroltype");
			if(flowcontroltype == 0 || flowcontroltype == 100 || flowcontroltype == 101){
				String text = node.getString("text");
				String nodeDisplay = node.getString("nodeDisplay");
				if(!appMap.containsKey(text)){
					throw new Exception("流程中组件[" + nodeDisplay + "]在蓝图中不存在！");
				}
				Application appDetail = appMap.get(text);
				String version = node.getString("versionConfig");
				String subflowName = node.getString("subflowName");
				//蓝图实例组件执行开关为开时才去校验版本和子流程，否则不予校验
				if(appDetail.getExecuteFlag() == 1){
					if("none".equals(version)){
						if("stop".equals(subflowName) || "destroy".equals(subflowName)){
							if(appDetail.getCurrentVersion() == null || "".equals(appDetail.getCurrentVersion().trim())){
								throw new Exception("组件[" + nodeDisplay + "]子流程类型[" + subflowName + "]未配置当前版本！");
							}
						}
						if("deploy".equals(subflowName) || "start".equals(subflowName)){
							if(appDetail.getTargetVersion() == null || "".equals(appDetail.getTargetVersion().trim())){
								throw new Exception("组件[" + nodeDisplay + "]子流程类型[" + subflowName + "]未配置目标版本！");
							}
						}
					}
					if("current".equals(version)){
						if(appDetail.getCurrentVersion() == null || "".equals(appDetail.getCurrentVersion().trim())){
							throw new Exception("组件[" + nodeDisplay + "]未配置当前版本！");
						}
					}
					if("target".equals(version)){
						if(appDetail.getTargetVersion() == null || "".equals(appDetail.getTargetVersion().trim())){
							throw new Exception("组件[" + nodeDisplay + "]未配置目标版本！");
						}
					}
					if("current+target".equals(version)){
						if(appDetail.getCurrentVersion() == null || "".equals(appDetail.getCurrentVersion().trim())){
							throw new Exception("组件[" + nodeDisplay + "]未配置当前版本！");
						}
						if(appDetail.getTargetVersion() == null || "".equals(appDetail.getTargetVersion().trim())){
							throw new Exception("组件[" + nodeDisplay + "]未配置目标版本！");
						}
					}
					//校验组件子流程是否不存在
					String appCdFlowId = node.getString("cdFlowId");
					String appFlowDesc = node.getString("desc");
					Map<String, Object> appFlow = resourceDao.getNewFlowDetailByFlowId(appCdFlowId);
					if(appFlow == null){
						throw new Exception("组件[" + nodeDisplay + "]的子流程[" + appFlowDesc + "]不存在，请重新配置当前组件子流程！");
					}
				}
			}
		}
	}

	@Override
	public int getbluePrintInstanceId(String blueprint_id,
			String blueprint_instance_name) {
		return dao.getbluePrintInstanceId(blueprint_id, blueprint_instance_name);
	}

	@Transactional
	@Override
	public void updateBlueprintResourcePoolConfigs4COP(Map<String, String> map) throws Exception {
		String blueprintId = map.get("blueprintId");
		Map<String, Object> blueprint = dao.getBlueprintInstanceById(Integer.parseInt(blueprintId));
		String blueprintinfo = "" + blueprint.get("INFO");
		JSONObject blueprintJson = JSON.parseObject(blueprintinfo);
		String originalConfigs = "" + blueprint.get("RESOURCE_POOL_CONFIG");
		String updateConfigs = map.get("resourcePoolConfig");
		Map<String, Map<String, Object>> originalMaps = JSON.parseObject(originalConfigs,new TypeReference<Map<String, Map<String, Object>>>(){});
		Map<String, Map<String, Object>> updateMaps = JSON.parseObject(updateConfigs, new TypeReference<Map<String, Map<String, Object>>>(){});
		Iterator<Entry<String, Map<String, Object>>> iter = updateMaps.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String, Map<String, Object>> entry = iter.next();
			String updateKey = entry.getKey();
			Map<String, Object> updateValue = entry.getValue();
			Map<String, Object> originalValue = originalMaps.get(updateKey);
			if(!compareResourcePool(updateValue, originalValue)){
				JSONArray blueprintNodeArray = blueprintJson.getJSONArray("nodeDataArray");
				Object updateNodes = "" + updateValue.get("nodes");
				List<Map<String, String>> updateNodeList = JSON.parseObject("" + updateNodes, new TypeReference<List<Map<String,String>>>(){});
				//更新组件item
				String originalNodes = "" + originalValue.get("nodes");
				List<Map<String, String>> originalNodeList = JSON.parseObject(originalNodes, new TypeReference<List<Map<String,String>>>(){});
				updateComponentIns4COP(blueprintId, updateKey, blueprintNodeArray, originalNodeList, updateNodeList, updateValue);
				//更新label
				updateNodeLabel(originalValue, updateValue, updateNodeList);
				//更新资源池配置和蓝图实例info
				originalValue.put("ins", updateNodeList.size());
				originalValue.put("label", updateValue.get("label"));
				originalValue.put("nodes", updateNodes);
				for(int i = 0; i < blueprintNodeArray.size(); i++){
					JSONObject item = blueprintNodeArray.getJSONObject(i);
					if("resource".equals(item.getString("eleType")) && updateKey.equals(item.getString("key"))){
						item.put("ins", updateNodeList.size());
						item.put("label", updateValue.get("label"));
						item.put("nodes", updateNodes);
						break;
					}
				}
				Map<String, String> poolConfig = new HashMap<String, String>();
				poolConfig.put("id", blueprintId);
				poolConfig.put("resourcePoolConfig", JSON.toJSONString(originalMaps));
				poolConfig.put("info", JSON.toJSONString(blueprintJson));
				dao.updateResourcePoolConfig(poolConfig);
			}
		}
	}
	
	private void updateComponentIns4COP(String blueprintId, String updateKey, JSONArray blueprintNodeArray, 
			List<Map<String, String>> originalNodeList, List<Map<String, String>> updateNodeList, Map<String, Object> updatePool)
					throws RuntimeException{
		List<String> appList = computeAppByPoolKey(blueprintNodeArray, updateKey);
		for(String appName : appList){
			Map<String, String> appPara = new HashMap<String, String>();
			appPara.put("id", blueprintId);
			appPara.put("appName", appName);
			Map<String, Object> app = dao.getBluerintAppByAppName(appPara);
			String appId = "" + app.get("ID");
			List<Map<String, String>> instances = dao.getBluerintAppInstances(appPara);
			Map<String, List<String>> computeIpMap = computeUpdateIp(originalNodeList, updateNodeList);
			List<String> addList = computeIpMap.get("add");
			List<String> delList = computeIpMap.get("del");
			for(Map<String, String> instance : instances){
				String instanceId = instance.get("ID");
				String instanceNodeId = instance.get("NODE_ID");
				Node instanceNode = nodeService.findNodeById(instanceNodeId);
				if(instanceNode == null){
					String error = "蓝图[" + blueprintId + "]内组件[" + appName + "]实例[" + instanceId + "]的nodeId["+ instanceNodeId + "]对应的node不存在！";
					throw new RuntimeException(error);
				}
				else{
					String instanceNodeIp = instanceNode.getIp();
					if(constainIp(delList, instanceNodeIp)){
						instanceDao.delInstance(instanceId);
					}
					if(constainIp(addList, instanceNodeIp)){
						//此ip已经存在组件实例，扩容list中移除该ip
						addList.remove(instanceNodeIp);
					}
				}
			}
			//扩容list中剩余ip新增实例状态为RESOURCE_POOL_ADD
			for(String nodeIp : addList){
				Instance newInstance = new Instance();
				String instanceId = UUID.randomUUID().toString();
				newInstance.setId(instanceId);
				newInstance.setAppId(appId);
				newInstance.setStatus(RESOURCE_POOL_ADD);
				Map<String, String> param = new HashMap<>();
				param.put("ip", nodeIp);
				param.put("clusterId", "" + updatePool.get("cluster_id"));
				Node node = nodeService.findNodeByClusterAndIp(param);
				if(node == null){
					String error = "环境[" + param.get("clusterId") + "]内ip[" + nodeIp + "]对应node节点不存在，请检查环境和ip节点配置！";
					throw new RuntimeException(error);
				}
				newInstance.setNodeId(node.getAdapterNodeId());
				instanceDao.saveInstance(newInstance);
			}
		}
	}

	@Transactional
	@Override
	public void cloneBlueprintInstanceConfig(String resourceInstanceId, String targetInstanceId) throws Exception {
		try {
			//拷贝蓝图实例配置
			Map<String, Object> resourceDetail = dao.getBlueprintInstanceByInstanceId(resourceInstanceId);
			Object resourceKV = resourceDetail.get("KEY_VALUE");
			if (resourceKV != null) {
				Map<String, Object> BpInsparam = new HashMap<String, Object>();
				BpInsparam.put("blueprintId", targetInstanceId);
				BpInsparam.put("configValue", resourceKV);
				dao.updateBpInsKeyConfig(BpInsparam);
			}
			//拷贝蓝图组件配置
			List<Application> targetApps = applicationDao.getBlueprintComponents(targetInstanceId);
			List<Application> resourceApps = applicationDao.getBlueprintComponents(resourceInstanceId);
			Map<String, Application> resourceMap = new HashMap<>();
			for (Application resourceApp : resourceApps) {
				resourceMap.put(resourceApp.getAppName(), resourceApp);
			}
			Map<String, Application> targetMap = new HashMap<>();
			for (Application targetApp : targetApps) {
				targetMap.put(targetApp.getAppName(), targetApp);
			}
			for (Application targetApp : targetApps) {
				Application resource = resourceMap.get(targetApp.getAppName());
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("componentId", targetApp.getId());
				param.put("currentVersion", resource.getCurrentVersion());
				param.put("currentInput", resource.getCurrentInput());
				param.put("currentOutput", resource.getCurrentOutput());
				param.put("targetVersion", resource.getTargetVersion());
				param.put("targetInput", resource.getTargetInput());
				param.put("targetOutput", resource.getTargetOutput());
				param.put("smartFlag", resource.getSmartFlag());
				param.put("executeFlag", resource.getExecuteFlag());
				dao.updateBlueprintComponentConfig(param);
				//上传蓝图组件在ftp缓存的组件包内容
				uploadComponentFile(targetInstanceId, (int) targetApp.getId(), resource.getCurrentVersion(),
						resource.getTargetVersion());
			}
			//如果target蓝图的资源池node未修改，则克隆resource蓝图组件实例的私有配置
			{
				Map<String, Object> resourcePool = JSON.parseObject("" + resourceDetail.get("RESOURCE_POOL_CONFIG"),
						new TypeReference<Map<String, Object>>() {
						});
				Map<String, Object> targetDetail = dao.getBlueprintInstanceByInstanceId(targetInstanceId);
				Map<String, Object> targetPool = JSON.parseObject("" + targetDetail.get("RESOURCE_POOL_CONFIG"),
						new TypeReference<Map<String, Object>>() {
						});
				Set<String> resourcePoolSet = resourcePool.keySet();
				for (String key : resourcePoolSet) {
					if (targetPool.containsKey(key)) {
						Map<String, Object> resourcePoolItem = (Map<String, Object>) resourcePool.get(key);
						List<Map<String, String>> resourcePoolNodes = JSON.parseObject(
								"" + resourcePoolItem.get("nodes"), new TypeReference<List<Map<String, String>>>() {
								});
						Map<String, Object> targetPoolItem = (Map<String, Object>) targetPool.get(key);
						List<Map<String, String>> targetPoolNodes = JSON.parseObject("" + targetPoolItem.get("nodes"),
								new TypeReference<List<Map<String, String>>>() {
								});
						if (compareNodes(resourcePoolNodes, targetPoolNodes)) {
							String targetInfo = "" + targetDetail.get("INFO");
							JSONObject targetInfoMap = JSON.parseObject(targetInfo);
							JSONArray targetBlueprintNodeArray = targetInfoMap.getJSONArray("nodeDataArray");
							List<String> targetPoolApps = computeAppByPoolKey(targetBlueprintNodeArray, key);
							for (String targetPoolAppName : targetPoolApps) {
								Application targetPoolApp = targetMap.get(targetPoolAppName);
								long targetPoolAppId = targetPoolApp.getId();
								List<Map<String, Object>> targetPoolAppInss = instanceDao
										.listInstancesByAppId(targetPoolAppId);
								Application resourcePoolApp = resourceMap.get(targetPoolAppName);
								long resourcePoolAppId = resourcePoolApp.getId();
								List<Map<String, Object>> resourcePoolAppInss = instanceDao
										.listInstancesByAppId(resourcePoolAppId);
								Map<String, Map<String, Object>> resourceInssMap = new HashMap<>();
								for (Map<String, Object> resourcePoolAppIns : resourcePoolAppInss) {
									resourceInssMap.put("" + resourcePoolAppIns.get("nodeId"), resourcePoolAppIns);
								}
								for (Map<String, Object> targetPoolAppIns : targetPoolAppInss) {
									if (resourceInssMap.containsKey((String) targetPoolAppIns.get("nodeId"))) {
										if(resourceInssMap.get((String) targetPoolAppIns.get("nodeId")).get("params") != null){
											HashMap<String, Object> param = new HashMap<>();
											param.put("instanceId", targetPoolAppIns.get("instanceId"));
											param.put("params", resourceInssMap
													.get((String) targetPoolAppIns.get("nodeId")).get("params"));
											instanceDao.updateBlueprintComponentInstanceParams(param);
										}
									}
								}
							}
						}
					}
				}
			}
			//拷贝组件实例状态、版本和配置？
			//拷贝快照？
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	/*
	 * params: {"status":1/2/7}
	 * return [] or [{"instanceId": "2018013000000008","startTime": "2018-01-30 06:04:19","id": "ae428b61cb714954945dd41b06e4e306",
	           "endTime": "2018-01-30 06:05:33","state": "2","flowState": "已结束","flowId": "2018011800036609"}]
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public List getFlowInstanceIds(JSONObject params) {
		List<Map> rows=new ArrayList<>();
		try {
			Map<String,Object> condition = new HashMap<>();
			List list = dao.listFlowInstanceIds(condition);
			int size =list.size();
			if(size>0){
				List<Map> dataRow = new ArrayList<>();
				List<BigInteger> instanceIds = new ArrayList<>();
				for(int i=0;i<list.size();i++){
					String instanceId=((Map)list.get(i)).get("instanceId").toString();
					instanceIds.add(BigInteger.valueOf(Long.valueOf(instanceId)));
				}
				Map<String,Object> reqParams = new HashMap<>();
				reqParams.put("instanceIds", instanceIds);
				String statusCode=params.get("status")==null?"":params.get("status").toString();
				if(statusCode.equals(Constants.Monitor.RUNNING+"")){
					reqParams.put("state", Constants.Monitor.INSTANCE_RUNNING);
				}
				reqParams.put("isOvertime", "N");
				RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();
				MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
				requestEntity.add("params", JSON.toJSONString(reqParams));
				String result = restUtil.postForObject(flowServerUrl+"/WFService/getRunningInstances.wf", requestEntity, String.class);
				Map<String, Object> jsonMap = JSON.parseObject(result);
				if((Boolean)jsonMap.get("state")){
					dataRow = JSON.parseArray(jsonMap.get("data").toString(), Map.class);
				}
				rows.addAll(dataRow);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}
	//组织版本信息，nodeDisplay+componentName+versionInfos一样时，要合并
	public List changeList(List rows){
		for(int i=0;i<rows.size();i++){
			Map row = (Map)rows.get(i);
			String componentDetail = (String)row.get("componentDetail");
			StringBuffer sb = new StringBuffer();
			if(!JudgeUtil.isEmpty(componentDetail)){
				Map<String, List<String>> map = new HashMap<>();
				JSONArray comInfo = JSONArray.parseArray(componentDetail);
				for(int j=0;j<comInfo.size();j++){
					JSONObject component = (JSONObject) comInfo.get(j);
					String nodeDisplay=component.getString("nodeDisplay");
					String componentName=component.getString("componentName");
					if(!map.containsKey(componentName)){
						map.put(componentName, new ArrayList<>());
					}
					List<String> list = map.get(componentName);
//					sb.append("组件的信息--"+"组件别名:"+nodeDisplay+"(组件名:"+componentName+")"+System.lineSeparator());
					List versionInfos = component.getJSONArray("versionInfos");
					for(int k=0;k<versionInfos.size();k++){
						JSONObject version = (JSONObject) versionInfos.get(k);
						int versionNum=version.getIntValue("versionNum");
						String versionName=version.getString("versionName");
						String versionDetail = "版本名："+versionName+" 版本号: "+versionNum+";";
						if(list.contains(versionDetail)){
							continue;
						}else{
							list.add(versionDetail);
						}
					}
				}
				for(Entry<String, List<String>> entry:map.entrySet()){
					String key = entry.getKey();
					List<String> list = entry.getValue();
					String versionInfo=System.lineSeparator()+"---- 组件："+key+" ";
					for(int l=0;l<list.size();l++){
						versionInfo=versionInfo+list.get(l);
					}
					sb.append(versionInfo+System.lineSeparator());
				}
				String componentDetailNew = sb.toString();
				if(componentDetailNew.lastIndexOf(";")>0){
					componentDetailNew =componentDetailNew.substring(0, componentDetailNew.lastIndexOf(";"));
				}
				row.put("componentDetail", componentDetailNew);
			}
		}
		return rows;
	}

	@Override
	public List getFlowInstanceIdsByMap(Map<String, Object> params) {
		List<Map<String,Object>> list = dao.getFlowInstanceIdsByMapNew(params);
		if(list == null){
			list = new ArrayList<>();
		}
		return list;
	}
	@Override
	public List getFlowInstanceIds(Map<String, Object> params) {
		List<Map<String,Object>> list = dao.getFlowInstanceIds(params);
		if(list == null){
			list = new ArrayList<>();
		}
		return list;
	}

	@Override
	public List findBlueInstanceIds(Map<String, Object> params) {
		// TODO Auto-generated method stub
		List list= dao.findBlueInstanceIds(params);
		if(list == null){
			list = new ArrayList<>();
		}
		return list;
	}
	
}

















class NodeData implements Serializable{
	public static final long serialVersionUID = 1L;

	public Integer key;
	public Integer flowcontroltype;//启动是1 结束是2 子流程是0  自动任务是10  聚合是5 条件判断是8	
	public String text;
	public String category;
	public Integer eventType;
	public Integer eventDimension;
	public String item;
	public String loc;
	public Integer taskType;
	public Integer gatewayType;

	public Integer ins;
	public String subflowName;
	public String subflowid;
	public String versionId;
	public String componetName;
	public String desc;
	public Map<String,Object> config;

	public static class Builder{
		NodeData n;
		public Builder(){
			n = new NodeData();
		}
		public Builder key(Integer key){
			n.key=key;return this;
		}
		public Builder text(String text){
			n.text=text;return this;
		}
		public Builder flowcontroltype(Integer flowcontroltype){
			n.flowcontroltype=flowcontroltype;return this;
		}
		public Builder category(String category){
			n.category=category;return this;
		}
		public Builder taskType(Integer taskType){
			n.taskType = taskType;return this;
		}
		public Builder eventType(Integer eventType){
			n.eventType = eventType;return this;
		}
		public Builder eventDimension(Integer eventDimension){
			n.eventDimension = eventDimension;return this;
		}
		public Builder item(String item){
			n.item = item;return this;
		}
		public NodeData build(){
			return n;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeData other = (NodeData) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
}

class LinkData implements Serializable{
	public static final long serialVersionUID = 1L;
	public int from;
	public int to;
	public double[] points;
	public LinkData(int from,int to,double[] points){
		this.from = from;
		this.to = to;
		this.points = points;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + from;
		result = prime * result + to;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkData other = (LinkData) obj;
		if (from != other.from)
			return false;
		if (to != other.to)
			return false;
		return true;
	}

}