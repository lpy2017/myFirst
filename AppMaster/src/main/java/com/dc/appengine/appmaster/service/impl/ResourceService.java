package com.dc.appengine.appmaster.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.taskdefs.Input;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
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
import com.dc.appengine.appmaster.dao.ILabelsDAO;
import com.dc.appengine.appmaster.dao.IPackageDao;
import com.dc.appengine.appmaster.dao.impl.BluePrintTypeDao;
import com.dc.appengine.appmaster.dao.impl.ResourceDao;
import com.dc.appengine.appmaster.entity.Application;
import com.dc.appengine.appmaster.entity.AuditEntity;
import com.dc.appengine.appmaster.entity.BluePrint;
import com.dc.appengine.appmaster.entity.BluePrintType;
import com.dc.appengine.appmaster.entity.Page;
import com.dc.appengine.appmaster.entity.Resource;
import com.dc.appengine.appmaster.entity.ResourceCode;
import com.dc.appengine.appmaster.entity.Version;
import com.dc.appengine.appmaster.entity.VersionFtl;
import com.dc.appengine.appmaster.service.IAudit;
import com.dc.appengine.appmaster.service.IBlueprintService;
import com.dc.appengine.appmaster.service.IPackageService;
import com.dc.appengine.appmaster.service.IResourceService;
import com.dc.appengine.appmaster.utils.ComponentConvert;
import com.dc.appengine.appmaster.utils.FileUtil;
import com.dc.appengine.appmaster.utils.FtpUtils;
import com.dc.appengine.appmaster.utils.ITSMBatchReleasePool;
import com.dc.appengine.appmaster.utils.ITSMVersionSort;
import com.dc.appengine.appmaster.utils.JudgeUtil;
import com.dc.appengine.appmaster.utils.MessageHelper;
import com.dc.appengine.appmaster.utils.RequestClient;
import com.dc.appengine.appmaster.utils.SensitiveDataUtil;
import com.dc.appengine.appmaster.utils.SortUtil;
import com.dc.appengine.appmaster.utils.ThreadPool;
import com.dc.appengine.appmaster.utils.UUIDGenerator;
import com.dc.appengine.appmaster.utils.Utils;
import com.dc.appengine.appmaster.utils.ZipUtil;

@Component("resourceService")
public class ResourceService implements IResourceService {

	@Autowired
	@Qualifier("resourceDao")
	private ResourceDao resourceDao;

	@Autowired
	@Qualifier("labelsDAO")
	private ILabelsDAO labelsDAO;

	@Autowired
	@Qualifier("packageDao")
	private IPackageDao packageDao;

	@Autowired
	@Qualifier("packageService")
	IPackageService packageService;

	@Autowired
	@Qualifier("auditService")
	IAudit auditService;

	@Value(value = "${ftp.url}")
	String url;
	@Value(value = "${ftp.port}")
	int port;
	@Value(value = "${ftp.user}")
	String user;
	@Value(value = "${ftp.pwd}")
	String pwd;
	@Value(value = "${ftp.home.path}")
	String ftpPath;

	@Value(value = "${flowServerUrl}")
	String flowServerUrl;

	@Value(value = "${saltDir}") // @Value(value="${saltDir:defaultValue}"
	String saltDir;

	@Autowired
	@Qualifier("blueprintService")
	IBlueprintService blueprintService;

	@Autowired
	@Qualifier("bluePrintTypeDao")
	private BluePrintTypeDao bluePrintTypeDao;

	@Autowired
	@Qualifier("applicationDao")
	private IApplicationDao applicationDao;

	@Value(value = "${itsm.single.timeout}")
	Long itsmTimeout;

	@PostConstruct
	public void setSaltDir() {
		boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");
		if (isWin) {
			Utils.TMPDIR = "D:/testSalt/";
		} else {
			Utils.TMPDIR = this.saltDir.endsWith(File.separator) ? this.saltDir : this.saltDir + File.separator;
		}
		File f = new File(Utils.TMPDIR);
		if (!f.exists()) {
			f.mkdir();
		}
	}

	@Transactional
	@Override
	public boolean registResource(Resource resource) {
		int temp = resourceDao.findResourceByName(resource.getResourceName().trim());
		if (temp >= 1) {
			return false;
		}
		resourceDao.saveResource(resource);
		return true;
		/*
		 * for(Version version:resource.getVersions()){
		 * resourceDao.saveVersion(version); }
		 */
	}

	@Override
	public Page listResource(Map<String, Object> condition, int pageNum, int pageSize) {
		return resourceDao.listResource(condition, pageNum, pageSize);
	}

	@Override
	public List<Version> listResourceVersion(String resourceId) {
		List<Version> list = resourceDao.listResourceVersion(resourceId);
		for (Version v : list) {
			addFtpLocation(v);
		}
		return list;
	}

	@Transactional
	@Override
	public void deleteResourceVersion(String resourceId, String id) {
		List<Map<String, Object>> list = resourceDao.getAllFlowByVersionId(id);
		for (Map<String, Object> map : list) {
			bluePrintTypeDao.delBluePrintTypeByFlowId(Long.parseLong("" + map.get("FLOWID")));
		}
		resourceDao.deleteVersionFlow(id);
		deleteFtpResource(id);
		resourceDao.deleteResourceVersion(resourceId, id);
	}

	private void deleteFtpResource(String versionId) {
		Version v = resourceDao.getResourceVersion(versionId);
		String[] strs = v.getResourcePath().split("/");
		boolean success = Utils.deleteFile(this.url, this.port, this.user, this.pwd, strs[0], strs[1]);
		if (!success) {
			throw new RuntimeException("删除ftp资源版本失败");
		}
	}

	@Transactional
	@Override
	public void deleteResource(String id) {
		List<Version> versions = this.listResourceVersion(id);
		for (Version v : versions) {
			this.deleteResourceVersion(id, v.getId());
			List<Map<String, Object>> list = resourceDao.getAllFlowByVersionId(v.getId());
			for (Map<String, Object> map : list) {
				bluePrintTypeDao.delBluePrintTypeByFlowId(Long.parseLong("" + map.get("FLOWID")));
			}
			resourceDao.deleteVersionFlow(v.getId());
		}
		resourceDao.deleteResource(id);
	}

	@Override
	public Map<String, Properties> getInputAndOutput(String resourceVersionId) {
		Version v = resourceDao.getResourceVersion(resourceVersionId);
		try {
			prepare(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String unzipDir = v.getResourceName() + File.separator + v.getVersionName();
		File inputProp = new File(Utils.TMPDIR + unzipDir + File.separator + "input.properties");
		File outputProp = new File(Utils.TMPDIR + unzipDir + File.separator + "output.properties");
		Properties input = this.loadProperties(inputProp);
		Properties output = this.loadProperties(outputProp);
		Map<String, Properties> result = new HashMap<>();
		result.put("input", input);
		result.put("output", output);
		return result;
	}

	private Properties loadProperties(File file) {
		Properties p = new Properties();
		if (file.exists()) {
			try (FileReader fr = new FileReader(file)) {
				p.load(fr);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;
	}

	@Override
	public boolean uploadFile(InputStream input, String uuid, String fileName) {
		return Utils.uploadFile(url, port, user, pwd, uuid, fileName, input);
	}

	private void prepare(Version v) throws Exception {
		String resourcePath = v.getResourcePath();
		String versionName = v.getVersionName();
		String resourceName = v.getResourceName();
		String md5 = v.getMd5();
		String[] strs = resourcePath.split("/");
		String tmpdir = Utils.TMPDIR;
		if (!tmpdir.endsWith(File.separator))
			tmpdir = tmpdir + File.separator;
		String parentPath = tmpdir + resourceName + File.separator + versionName;
		File dir = new File(parentPath);
		if (!dir.exists())
			dir.mkdirs();
		File f = new File(parentPath + File.separator + versionName + ".md5");
		if (f.exists()) {
			// 存在这个文件，比对md5值
			try (FileInputStream fis = new FileInputStream(f)) {
				String existMd5 = IOUtils.toString(fis);
				if (existMd5.equals(md5)) {
					// 文件一样
					return;
				} else {
					// 删除该文件夹下的文件
					f.delete();
				}
			}
		}
		f.createNewFile();
		try (FileOutputStream fos = new FileOutputStream(f)) {
			boolean download = Utils.downloadFile(url, port, user, pwd, strs[0], strs[1], parentPath);
			if (download) {
				File file = new File(parentPath + File.separator + strs[1]);
				Utils.unZip(file, null);
			}
			IOUtils.write(md5, fos);
		}
	}

	@Override
	public List<Map<String, Object>> listAllResource(String resourceName, String labelId) {
		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> param = new HashMap<>();
		param.put("resourceName", resourceName);
		param.put("labelId", labelId);
		param.put("sortName", "resourceName");
		param.put("sortOrder", "ASC");
		Page page = this.listResource(param, 1, 9999);
		List<Map> resources = page.getRows();
		if (CollectionUtils.isNotEmpty(resources)) {
			for (Map r : resources) {
				Map<String, Object> map = new HashMap<>();
				map.put("id", r.get("resourceId"));
				map.put("text", r.get("resourceName"));
				map.put("des", r.get("resourceDesc"));
				list.add(map);
			}
		}
		return list;
	}
	// fdfdfdfdfdf

	@Override
	public Map<String, Object> getResourceVersionDetail(String resourceVersionId) {
		Version v = resourceDao.getResourceVersion(resourceVersionId);
		List<Map<String, Object>> allFlows = resourceDao.getAllFlowByVersionId(resourceVersionId);
		Properties flows = new Properties();
		for (Map<String, Object> map : allFlows) {
			String flowType = (String) map.get("FLOWTYPE");
			String flowId = map.get("FLOWID").toString();
			flows.setProperty(flowType, flowId);
		}
		String version = v.getVersionName();
		String unzipDir = v.getResourceName() + File.separator + v.getVersionName();
		Map<String, Object> result = new HashMap<>();
		Map<String, Properties> config = this.getInputAndOutput(resourceVersionId);
		File urlFile = new File(Utils.TMPDIR + unzipDir + File.separator + "url");
		File templatesFile = new File(Utils.TMPDIR + unzipDir + File.separator + "templates.properties");
		Properties templates = this.loadProperties(templatesFile);
		try (FileReader fr = new FileReader(urlFile)) {
			String url = IOUtils.toString(fr);
			result.put("url", url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.putAll(config);
		result.put("flows", flows);
		result.put("templates", templates);
		result.put("version", version);
		return result;
	}

	@Override
	public long saveFlow(BluePrint bp, String subFlowType) {
		String bluePrintId = UUIDGenerator.getUUID();
		bp.setBluePrintId(bluePrintId);
		bp.setIssub(true);
		Map<String, Object> params = new HashMap<>();
		params.put("CZRY_DM", "00000000000");
		params.put("PDJSON", bp);
		RestTemplate restUtil = new RestTemplate();
		String jsonObj = JSON.toJSONString(params);
		System.out.println(jsonObj);
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("goflow", jsonObj);
		String result = restUtil.postForObject(flowServerUrl + "/WFService/goflowToSmartflow.wf", requestEntity,
				String.class);
		Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
		});
		boolean b = (boolean) resultMap.get("state");
		if (b) {
			long subFlowId = Long.valueOf(resultMap.get("data").toString());
			BluePrintType bpt = new BluePrintType(bluePrintId, subFlowId, "subFlow");
			bpt.setFlow_info(JSON.toJSONString(bp));
			blueprintService.saveBluePrintType(bpt);
			return subFlowId;
		} else {
			return 0;
		}
	}

	public static void main(String[] args) {
//		Pattern p = Pattern.compile("/ws/(?!plugin)\\w+/*");
//		String s1 = "/ws/plugi3";
//		Matcher m = p.matcher(s1);
//		System.out.println(m.find());
//		FileInputStream ais;
//		try {
//			ais = new FileInputStream("F:\\test\\merge\\test\\bak.sql");
//			FileOutputStream aos = new FileOutputStream("F:\\test\\merge\\bak.sql");
//			IOUtils.copy(ais, aos);
//			aos.close();
//			ais.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		String url="ftp://paas:ENC(73bbed5957750e8c)@10.126.3.222/packages/d133ba67-a820-4f93-8fae-a788bc1574ef/oracleclient.zip";
		String[] str =url.split("/");
		System.out.println(str[0]);
		System.out.println(str[1]);
	}

	@Override
	public boolean upateResourceVersionStatus(String resourceVersionId, String status, String description,
			String userName) {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<>();
		param.put("status", status);
		param.put("versionId", resourceVersionId);
		Boolean update = resourceDao.updateResourceVersion(param);
		if (!JudgeUtil.isEmpty(userName)&&update) {
			if(JudgeUtil.isEmpty(description)){
				description="";
			}
			JSONObject params = new JSONObject();
			params.put("id", UUID.randomUUID().toString());
			params.put("userId", userName);
			params.put("resourceVersionId", resourceVersionId);
			params.put("opType", Constants.audit4Resource.OP_UPDATE_STATUS);
			params.put("status", status);
			params.put("description", description);
			JSONObject resultAudit = resourceDao.saveAudit(params);
			update = resultAudit.getBoolean("result");
		}
		return update;
	}

	@Override
	public String getResourceVersionStatus(String resourceVersionId) {
		// TODO Auto-generated method stub
		return resourceDao.getResourceVersionStatus(resourceVersionId);
	}

	@Override
	public Page listResourceVersionByPage(Map<String, Object> condition, int pageNum, int pageSize) {
		Page p = resourceDao.listResourceVersionByPage(condition, pageNum, pageSize);
		List<Version> list = p.getRows();
		for (Version v : list) {
			addFtpLocation(v);
		}
		return p;
	}

	@Override
	public void saveResourceVersion(Version version) {
		Map resource = null;
		int versionLock = 0;
		int newVersionLock = 0;
		String resourceId = "";
		Map resParams = new HashMap<>();
		Boolean updateLock = false;
		while (!updateLock) {
			resource = resourceDao.getResourceInfo(version.getResourceId());
			if (resource != null) {
				versionLock = (Integer) resource.get("VERSION");
				newVersionLock = versionLock + 1;
				resourceId = (String) resource.get("ID");
				resParams.put("resourceId", resourceId);
				resParams.put("version", versionLock);
				resParams.put("newVersion", newVersionLock);
				// 获取乐观锁
				updateLock = resourceDao.updateResourceInfo(resParams);
				if (!updateLock) {
					continue;
				} else {
					Integer versionNum = resourceDao.getResourceVersionMaxNum(resourceId);
					versionNum=versionNum==null?1:versionNum+1;
					version.setVersionNum(versionNum);
					resourceDao.saveVersion(version);
				}
			} else {
				break;
			}
		}
	}

	@Override
	public void saveVersionFtl(VersionFtl versionFtl) {
		resourceDao.saveVersionFtl(versionFtl);
	}

	public String getResourceIdByName(String name) {
		return resourceDao.getResourceIdByName(name);
	}

	@Override
	public long getEmptyFlow() {
		return blueprintService.getEmptyFlow();
	}

	@Transactional
	@Override
	public Map<String, Long> saveComponentFlows(Map<String, String> map) {
		String emptyFlow = "{\"class\":\"go.GraphLinksModel\",\"modelData\":{\"position\":\"-494.34375 -509\"},\"nodeDataArray\":[{\"key\":101,\"eleType\":\"flowcontrol\",\"category\":\"event\",\"text\":\"开始\",\"flowcontroltype\":1,\"eventType\":1,\"eventDimension\":1,\"item\":\"start\",\"loc\":\"-310.34375 -81\"},{\"key\":104,\"eleType\":\"flowcontrol\",\"category\":\"event\",\"text\":\"结束\",\"flowcontroltype\":2,\"eventType\":1,\"eventDimension\":8,\"item\":\"End\",\"loc\":\"-207.34375 -81\"}],\"linkDataArray\":[{\"from\":101,\"to\":104,\"points\":[-288.84375,-81,-278.84375,-81,-264.59375,-81,-264.59375,-81,-250.34375,-81,-230.34375,-81]}]}";
		// long emptyFlow = this.getEmptyFlow();
		Map<String, Long> result = new HashMap<>();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String flowType = entry.getKey();
			String flowDef = entry.getValue();
			JSONObject jsonSend;
			// 前端给出的流程json为空、为undefined、为无node和link的空保存json，则默认为空流程(只包含开始、结束及连线)
			if (isEmptyFlow(flowDef)) {
				jsonSend = JSON.parseObject(emptyFlow);
			} else {
				jsonSend = JSON.parseObject(flowDef);
			}
			jsonSend.put("issub", true);
			jsonSend.put("type", flowType);
			jsonSend.put("bluePrintId", UUIDGenerator.getUUID());
			String flowResult = blueprintService.cdflowToSmartflow(JSON.toJSONString(jsonSend));
			if (flowResult == null) {
				// 抛出异常，事务回滚,即使有一个保存失败,也不会改变数据库
				throw new RuntimeException(flowType + "保存失败");
			} else {
				long flowId = Long.parseLong(flowResult);
				result.put(flowType, flowId);
				BluePrintType bluePrintType = new BluePrintType(jsonSend.getString("bluePrintId"), flowId, flowType);
				bluePrintType.setFlow_info(JSON.toJSONString(jsonSend));
				blueprintService.saveBluePrintType(bluePrintType);
			}
		}
		return result;
	}

	@Transactional
	@Override
	public void saveVersionFlow(HashMap<String, Object> map) {
		resourceDao.saveVersionFlow(map);
	}

	@Override
	public String getResourceVersionFlows(String resourceVersionId) {
		JSONObject json = new JSONObject();
		List<Map<String, Object>> list = resourceDao.getAllFlowByVersionId(resourceVersionId);
		if (list != null && list.size() > 0) {
			for (Map<String, Object> map : list) {
				if (map.get("FLOWID").equals("0")) {
					json.put("" + map.get("FLOWTYPE"), "");
				} else {
					Long flowId = Long.parseLong("" + map.get("FLOWID"));
					String flowInfo = bluePrintTypeDao.getSubFlowInfo(flowId);
					if (flowInfo != null && flowInfo.length() == 0) {
						json.put("" + map.get("FLOWTYPE"), "");
					} else {
						json.put("" + map.get("FLOWTYPE"), flowInfo);
					}
				}
			}
		}
		return json.toJSONString();
	}

	@Transactional
	@Override
	public boolean updateResourceVersionFlows(String resourceVersionId, String flows) {
		try {
			String emptyFlow = "{\"class\":\"go.GraphLinksModel\",\"modelData\":{\"position\":\"-494.34375 -509\"},\"nodeDataArray\":[{\"key\":101,\"eleType\":\"flowcontrol\",\"category\":\"event\",\"text\":\"开始\",\"flowcontroltype\":1,\"eventType\":1,\"eventDimension\":1,\"item\":\"start\",\"loc\":\"-310.34375 -81\"},{\"key\":104,\"eleType\":\"flowcontrol\",\"category\":\"event\",\"text\":\"结束\",\"flowcontroltype\":2,\"eventType\":1,\"eventDimension\":8,\"item\":\"End\",\"loc\":\"-207.34375 -81\"}],\"linkDataArray\":[{\"from\":101,\"to\":104,\"points\":[-288.84375,-81,-278.84375,-81,-264.59375,-81,-264.59375,-81,-250.34375,-81,-230.34375,-81]}]}";
			Map<String, String> flowMap = JSON.parseObject(flows, new TypeReference<Map<String, String>>() {
			});
			// 查找sv_version_flow已记录的流程id
			List<Map<String, Object>> versionFlows = resourceDao.getAllFlowByVersionId(resourceVersionId);
			if (flowMap != null && flowMap.size() > 0) {
				Iterator<Entry<String, String>> iter = flowMap.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, String> entry = iter.next();
					String flowType = entry.getKey();
					String flowInfo = entry.getValue();// 前台返回最新流程信息
					Map<String, Object> versionFlowMap = matchVersionFlowMap(flowType, versionFlows);
					long flowId = -1;
					if (versionFlowMap != null) {
						flowId = (Long) versionFlowMap.get("FLOWID");
					}
					JSONObject jsonSend;
					// 前端返回最新流程信息为空、为undefined、为无node和link的空保存json，则默认为空流程(只包含开始、结束及连线)
					if (isEmptyFlow(flowInfo)) {
						jsonSend = JSON.parseObject(emptyFlow);
					} else {
						jsonSend = JSON.parseObject(flowInfo);
					}
					jsonSend.put("issub", true);
					jsonSend.put("type", flowType);
					jsonSend.put("bluePrintId", UUIDGenerator.getUUID());
					if (flowId != -1) {
						// 已进行过模型转换，则使用旧的workFlowId去更新工作流模型
						jsonSend.put("workFlowId", flowId);
					}
					String flowResult = blueprintService.cdflowToSmartflow(JSON.toJSONString(jsonSend));
					if (flowResult == null) {
						// 抛出异常，事务回滚,即使有一个保存失败,也不会改变数据库
						throw new RuntimeException(flowType + "保存失败");
					} else {
						flowId = Long.parseLong(flowResult);
						// sv_version_flow未有记录则新增，有则不动
						if (versionFlowMap == null) {
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("versionId", resourceVersionId);
							params.put("flowType", flowType);
							params.put("flowId", flowId);
							resourceDao.saveVersionFlow(params);
						}
						// ma_blueprint_type先删旧的
						bluePrintTypeDao.delBluePrintTypeByFlowId(flowId);
						// ma_blueprint_type在加新的
						BluePrintType bluePrintType = new BluePrintType(jsonSend.getString("bluePrintId"), flowId,
								flowType);
						bluePrintType.setFlow_info(JSON.toJSONString(jsonSend));
						blueprintService.saveBluePrintType(bluePrintType);
					}
				}
			}
			// Map<String,Long> saveMap = this.saveComponentFlows(flowMap);
			// updateVersionFlow(resourceVersionId, saveMap, 2);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Transactional
	public void saveVersionFlows(String resourceVersionId, Map<String, Long> saveMap) {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < 4; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("versionId", resourceVersionId);
			switch (i) {
			case 0:
				map.put("flowType", "deploy");
				map.put("flowId", saveMap.get("deploy"));
				break;
			case 1:
				map.put("flowType", "start");
				map.put("flowId", saveMap.get("start"));
				break;
			case 2:
				map.put("flowType", "stop");
				map.put("flowId", saveMap.get("stop"));
				break;
			case 3:
				map.put("flowType", "destroy");
				map.put("flowId", saveMap.get("destroy"));
				break;
			default:
				map.put("flowType", "");
				map.put("flowId", "0");
			}
			list.add(map);
		}
		for (HashMap<String, Object> item : list) {
			if (Long.parseLong("" + item.get("flowId")) != 0) {
				// 新增组件当前版本所有的新子流程id(非空json才保存)
				resourceDao.saveVersionFlow(item);
			}
		}
	}

	private boolean isEmptyFlow(String flowInfo) {
		if (flowInfo == null || "".equals(flowInfo.trim())) {
			return true;
		} else if ("undefined".equals(flowInfo)) {
			return true;
		} else {
			JSONObject json = JSON.parseObject(flowInfo);
			JSONArray nodes = json.getJSONArray("nodeDataArray");
			JSONArray links = json.getJSONArray("linkDataArray");
			if (nodes.size() == 0 && links.size() == 0) {
				return true;
			}
		}
		return false;
	}

	private Map<String, Object> matchVersionFlowMap(String flowType, List<Map<String, Object>> versionFlows) {
		for (Map<String, Object> map : versionFlows) {
			String versionFlowType = "" + map.get("FLOWTYPE");
			if (flowType.equals(versionFlowType)) {
				return map;
			}
		}
		return null;
	}

	@Override
	public String getAppSubflowVars(String appSubflowId) {
		List<String> paraList = new ArrayList<String>();
		String flowInfo = resourceDao.getAppSubflowInfo(appSubflowId);
		JSONObject flowJson = JSON.parseObject(flowInfo);
		JSONArray nodes = flowJson.getJSONArray("nodeDataArray");
		if (nodes.size() > 0) {
			for (int i = 0; i < nodes.size(); i++) {
				JSONObject node = (JSONObject) nodes.get(i);
				String flowcontroltype = node.getString("flowcontroltype");
				// 寻找开始节点
				if ("1".equals(flowcontroltype)) {
					String paras = node.getString("paras");
					if (paras != null && paras.length() > 0) {
						Map<String, String> parasMap = JSON.parseObject(paras,
								new TypeReference<Map<String, String>>() {
								});
						Iterator<Entry<String, String>> parasIter = parasMap.entrySet().iterator();
						while (parasIter.hasNext()) {
							Entry<String, String> paraEntry = parasIter.next();
							paraList.add(paraEntry.getKey());
						}
					}
					break;
				}
			}
		}
		StringBuffer sb = new StringBuffer();
		if (paraList.size() > 0) {
			for (int j = 0; j < paraList.size(); j++) {
				sb.append(paraList.get(j));
				if (j < paraList.size() - 1) {
					sb.append(";");
				}
			}
		}
		return sb.toString();
	}

	@Transactional
	@Override
	public Long saveNewComponentFlow(Map<String, String> map) {
		// String oldFlowId = resourceDao.getFlowByVersionIdAndFlowType(map);
		String oldFlowId = null;
		String emptyFlow = "{\"class\":\"go.GraphLinksModel\",\"modelData\":{\"position\":\"-494.34375 -509\"},\"nodeDataArray\":[{\"key\":101,\"eleType\":\"flowcontrol\",\"category\":\"event\",\"text\":\"开始\",\"flowcontroltype\":1,\"eventType\":1,\"eventDimension\":1,\"item\":\"start\",\"loc\":\"-310.34375 -81\"},{\"key\":104,\"eleType\":\"flowcontrol\",\"category\":\"event\",\"text\":\"结束\",\"flowcontroltype\":2,\"eventType\":1,\"eventDimension\":8,\"item\":\"End\",\"loc\":\"-207.34375 -81\"}],\"linkDataArray\":[{\"from\":101,\"to\":104,\"points\":[-288.84375,-81,-278.84375,-81,-264.59375,-81,-264.59375,-81,-250.34375,-81,-230.34375,-81]}]}";
		long flowId = -1;
		String flowType = map.get("flowType");
		String flowInfo = map.get("flowInfo");
		JSONObject jsonSend;
		// 前端给出的流程json为空、为undefined、为无node和link的空保存json，则默认为空流程(只包含开始、结束及连线)
		if (isEmptyFlow(flowInfo)) {
			jsonSend = JSON.parseObject(emptyFlow);
		} else {
			jsonSend = JSON.parseObject(flowInfo);
		}
		jsonSend.put("issub", true);
		jsonSend.put("type", flowType);
		jsonSend.put("bluePrintId", UUIDGenerator.getUUID());
		// 曾经保存过此类型流程，只更新内容，不改变flowId
		if (oldFlowId != null) {
			jsonSend.put("workFlowId", oldFlowId);
		}
		String flowResult = blueprintService.cdflowToSmartflow(JSON.toJSONString(jsonSend));
		if (flowResult == null) {
			throw new RuntimeException(flowType + "保存失败");
		} else {
			flowId = Long.parseLong(flowResult);
			// 如果已有先删
			if (oldFlowId != null) {
				bluePrintTypeDao.delBluePrintTypeByFlowId(flowId);
			}
			BluePrintType bluePrintType = new BluePrintType(jsonSend.getString("bluePrintId"), flowId, flowType);
			bluePrintType.setFlow_info(JSON.toJSONString(jsonSend));
			String flowName = map.get("flowName");
			if (flowName == null || "".equals(flowName)) {
				flowName = String.valueOf(flowId);
			}
			bluePrintType.setFlow_name(flowName);
			blueprintService.saveBluePrintType(bluePrintType);
			Map<String, Object> saveMap = new HashMap<String, Object>();
			String versionId = map.get("versionId");
			saveMap.put("versionId", versionId);
			saveMap.put("flowType", flowType);
			saveMap.put("flowId", flowId);
			// 如果没有，则新增
			if (oldFlowId == null) {
				resourceDao.saveVersionFlow(saveMap);
			}
			// 如果有，则继续保留
			else {
			}
		}
		return flowId;
	}

	@Override
	public Map<String, List<Map<String, String>>> getNewVersionOperations(String versionId) {
		Map<String, List<Map<String, String>>> maps = new HashMap<String, List<Map<String, String>>>();
		List<Map<String, Object>> list = resourceDao.getAllFlowByVersionId(versionId);
		if (list != null && list.size() > 0) {
			for (Map<String, Object> item : list) {
				String flowType = "" + item.get("FLOWTYPE");
				if (!maps.containsKey(flowType)) {
					List<Map<String, String>> listType = new ArrayList<Map<String, String>>();
					maps.put(flowType, listType);
				}
				Map<String, String> map = new HashMap<String, String>();
				String flowId = "" + item.get("FLOWID");
				String flowName = bluePrintTypeDao.getFlowNameByFlowId(Long.parseLong(flowId));
				if (flowName == null || "".equals(flowName.trim())) {
					flowName = flowId;
				}
				map.put("flowId", flowId);
				map.put("flowName", flowName);
				maps.get(flowType).add(map);
			}
		}
		return maps;
	}

	@Override
	public String getFlowInfoByFlowId(String flowId) {
		return bluePrintTypeDao.getSubFlowInfo(Long.parseLong(flowId));
	}

	@Transactional
	@Override
	public boolean updateFlowInfoByFlowId(String flowId, String flowInfo) {
		try {
			String flowType = null;
			Map<String, Object> map = bluePrintTypeDao.getBlueprintTypeByFlowId(Long.parseLong(flowId));
			// 获取操作类型
			if (map != null) {
				flowType = "" + map.get("FLOW_TYPE");
			}
			String emptyFlow = "{\"class\":\"go.GraphLinksModel\",\"modelData\":{\"position\":\"-494.34375 -509\"},\"nodeDataArray\":[{\"key\":101,\"eleType\":\"flowcontrol\",\"category\":\"event\",\"text\":\"开始\",\"flowcontroltype\":1,\"eventType\":1,\"eventDimension\":1,\"item\":\"start\",\"loc\":\"-310.34375 -81\"},{\"key\":104,\"eleType\":\"flowcontrol\",\"category\":\"event\",\"text\":\"结束\",\"flowcontroltype\":2,\"eventType\":1,\"eventDimension\":8,\"item\":\"End\",\"loc\":\"-207.34375 -81\"}],\"linkDataArray\":[{\"from\":101,\"to\":104,\"points\":[-288.84375,-81,-278.84375,-81,-264.59375,-81,-264.59375,-81,-250.34375,-81,-230.34375,-81]}]}";
			JSONObject jsonSend;
			// 前端返回最新流程信息为空、为undefined、为无node和link的空保存json，则默认为空流程(只包含开始、结束及连线)
			if (isEmptyFlow(flowInfo)) {
				jsonSend = JSON.parseObject(emptyFlow);
			} else {
				jsonSend = JSON.parseObject(flowInfo);
			}
			String blueprintInstanceId = UUIDGenerator.getUUID();
			jsonSend.put("issub", true);
			if (flowType != null) {
				jsonSend.put("type", flowType);
			}
			jsonSend.put("bluePrintId", blueprintInstanceId);
			// 已进行过模型转换，则使用旧的workFlowId去更新工作流模型
			jsonSend.put("workFlowId", flowId);
			String flowResult = blueprintService.cdflowToSmartflow(JSON.toJSONString(jsonSend));
			if (flowResult == null) {
				throw new RuntimeException(flowId + "工作流转换失败");
			} else {
				if (map != null) {
					// ma_blueprint_type更新
					Map<String, Object> paras = new HashMap<String, Object>();
					paras.put("flowId", flowResult);
					paras.put("flowInfo", flowInfo);
					paras.put("blueprintInstanceId", blueprintInstanceId);
					return bluePrintTypeDao.updateBluePrintTypeByFlowId(paras);
				} else {
					// BluePrintType bluePrintType = new
					// BluePrintType(blueprintInstanceId,
					// Long.parseLong(flowId), flowType);
					// bluePrintType.setFlow_info(JSON.toJSONString(jsonSend));
					// blueprintService.saveBluePrintType(bluePrintType);
					// return true;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateResourceVersion(String versionId, String resourcePath, String md5) {
		try {
			deleteFtpResource(versionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String, Object> map = new HashMap<>();
		map.put("resourcePath", resourcePath);
		map.put("versionId", versionId);
		map.put("md5", md5);
		return resourceDao.updateResourceVersion(map);
	}

	@Override
	public boolean updateVersion(String resourceVersionId, String input, String output, String resource_path) {
		Map<String, Object> map = new HashMap<>();
		map.put("resourceVersionId", resourceVersionId);
		map.put("input", input);
		map.put("output", output);
		map.put("resource_path", resource_path);
		return resourceDao.updateVersion(map);
	}

	@Override
	public String saveScriptResource(Map<String, Object> param) {
		// TODO Auto-generated method stub
		boolean save = resourceDao.saveScriptResource(param);
		if (save) {
			return MessageHelper.wrap("result", true, "message", "添加脚本包成功！");
		}
		return MessageHelper.wrap("result", false, "message", "添加脚本包失败！");
	}

	@Override
	public String deleteScriptResource(String id) {
		// TODO Auto-generated method stub
		Map<String, Object> script = resourceDao.findScriptById(id);
		if (script == null) {
			return MessageHelper.wrap("result", false, "message", "id=" + id + "的脚本资源不存在！");
		}
		String resourcePath = script.get("RESOURCE_PATH").toString();
		int i = resourcePath.lastIndexOf("/");
		String parentPath = resourcePath.substring(0, i);
		String fileName = resourcePath.substring(i + 1);
		if (Utils.deleteFile(url, port, user, pwd, parentPath, fileName)) {
			boolean delete = resourceDao.deleteScriptResource(id);
			if (delete) {
				return MessageHelper.wrap("result", true, "message", "删除脚本包成功！");
			}
			return MessageHelper.wrap("result", false, "message", "删除脚本包失败！");
		} else {
			return MessageHelper.wrap("result", false, "message", "删除ftp上脚本包失败！");
		}
	}

	@Override
	public Page listScriptResource(Map<String, Object> condition, int pageNum, int pageSize) {
		// TODO Auto-generated method stub
		Page page = resourceDao.listScriptResource(condition, pageNum, pageSize);
		List<Map> list = page.getRows();
		// Pattern p= Pattern.compile("^[a-zA-Z\\d]");
		for (Map one : list) {
			// ftp://paas:123456@10.1.108.33/347c2867-56be-492f-924f-0398e0129f10/script.zip
			one.put("absolutePath",
					"ftp://" + SensitiveDataUtil.concealUser(user) + ":" + SensitiveDataUtil.concealPassword(pwd) + "@"
							+ SensitiveDataUtil.concealIp(url) + File.separator + one.get("resourcePath").toString());
		}
		return page;
	}

	@Override
	public Version getResourceVersion(String versionId) {
		Version v = resourceDao.getResourceVersion(versionId);
		String input = v.getInput();
		if(!"{}".equals(input)) {
			Map<String, Object> inMap = JSON.parseObject(input, new TypeReference<Map<String, Object>>() {});
			v.setInput(JSON.toJSONString(new TreeMap<String, Object>(inMap)));
		}
		String output = v.getOutput();
		if(!"{}".equals(output)) {
			Map<String, Object> outMap = JSON.parseObject(output, new TypeReference<Map<String, Object>>() {});
			v.setOutput(JSON.toJSONString(new TreeMap<String, Object>(outMap)));
		}
		if (v != null) {
			addFtpLocation(v);
			v.setIsftpLegal(judgeWorkpieceUrl(v.getFtpLocation()));
			SensitiveDataUtil.decryptVersionConfig(v);
		}
		return v;
	}

	private void addFtpLocation(Version v) {
		v.ftpLocation = v.getResourcePath();
		v.url = v.getResourcePath();
		// try {
		// URL ftpLocation = new
		// URL("ftp",this.url,this.port,"/"+v.getResourcePath());
		// v.ftpLocation = ftpLocation.toString();
		// Map<String, Object> detail =
		// this.getResourceVersionDetail(v.getId());
		// v.url = (String)detail.get("url");
		// } catch (Exception e) {
		// e.printStackTrace();
		// v.ftpLocation="error";
		// v.url="error";
		// }
	}

	@Override
	public Map<String, Object> getResourceDetail(String resourceName) {
		Map<String, Object> resource = resourceDao.getResourceDetail(resourceName);
		String labels = "";
		String labelIds = "";
		if (!JudgeUtil.isEmpty(resource)) {
			String resourceId = resource.get("resourceId").toString();
			JSONObject params = new JSONObject();
			params.put("resourceId", resourceId);
			List<Map> labelList = labelsDAO.findResourceLabels(params);
			if (labels != null && labelList.size() > 0) {
				for (int i = 0; i < labelList.size(); i++) {
					Map label = (Map) labelList.get(i);
					if (i == labelList.size() - 1) {
						labels += label.get("name");
						labelIds += label.get("labelId");
					} else {
						labels += label.get("name") + " ";
						labelIds += label.get("labelId") + ";";
					}
				}
			}
			resource.put("labels", labels);
			resource.put("labelIds", labelIds);
		}
		return resource;
	}

	public String geNewtWorkpieceTree(String resourcePath) {
		FtpUtils f = new FtpUtils();
		List<String> fileNames = null;
		long x = System.currentTimeMillis();
		try {
			if (f.login(this.url, this.port, this.user, this.pwd)) {
				fileNames = f.List2Tree(resourcePath);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			f.disConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}

		long b = System.currentTimeMillis();
		System.out.println(b - x);
		String json = JSONArray.toJSONString(fileNames);

		json = json.replace("\\\"", "\"");
		json = json.replace("\"{", "{");
		json = json.replace("}\"", "}");
		return json;
	}

	@Override
	public String getWorkpieceTree(String resourceVersionId) {
		List<String> fileTree = new ArrayList<String>();
		Version resourceVersion = resourceDao.getResourceVersion(resourceVersionId);
		String ftpUrl = resourceVersion.getResourcePath();
		
		if(ftpUrl ==null || "".equals(ftpUrl)){
			String json = JSONArray.toJSONString(fileTree);
			return json;
		}
		// ftpUrl =
		// "ftp://paas:123456@10.1.108.33/packages/6bd52b8e-582e-4454-8c0a-315512876483/nginx_1.10.1.zip

		String resourcePath = getAvaiableFtpUrl(resourceVersionId);
		// local
		try {
			// /packages/42de3579-5d62-4976-9cbc-e40b0bd1648e/apache_2.4.6/
			fileTree = FtpUtils.ListLocal2Tree(resourcePath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// 本地获取不到从ftp下载获取
		if (fileTree.isEmpty()) {
//			String ftpResourcePath = resourcePath.substring(1, resourcePath.length());
			FtpUtils f = new FtpUtils();
			String fileName = ftpUrl.substring(ftpUrl.lastIndexOf("/") + 1, ftpUrl.length());
			String resourceParent = resourcePath.substring(0, resourcePath.lastIndexOf("/"));
			resourceParent = resourceParent.substring(4, resourceParent.lastIndexOf("/") + 1);
			Utils.downloadFile(url, port, user, pwd, "/" + resourceParent, fileName, "ftl/" + resourceParent);
			try {
				File unzipfile = new File("ftl/" + resourceParent + fileName);
				Utils.unZip(unzipfile,
						"ftl/" + resourceParent + fileName.substring(0, fileName.lastIndexOf(".")));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				fileTree = FtpUtils.ListLocal2Tree(resourcePath);
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		String json = JSONArray.toJSONString(fileTree);

		json = json.replace("\\\"", "\"");
		json = json.replace("\"{", "{");
		json = json.replace("}\"", "}");
		return json;
	}

	@Override
	public String getWorkpieceFile(String resourceVersionId, String filePath) {
		String fileText = "";
		String encoding = "";
		String newFtpUrl = getAvaiableFtpUrl(resourceVersionId);
		filePath = newFtpUrl + filePath;
		File file = new File(filePath);
		// FtpUtils f = new FtpUtils();
		// try {
		// if(f.login(this.url, this.port, this.user, this.pwd)){
		// fileText = f.getFileText(filePath);
		//
		// }
		// f.disConnection();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		try {
			StringBuilder builder = null;
			List<String> encodings = FileUtil.guessFileEncoding(file);
			if (encodings.size() > 0) {
				encoding = encodings.get(0);
			} else {
				encoding = "UTF-8";
			}
			InputStream ins = new FileInputStream(filePath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(ins, encoding));
			// byte[] b = new byte[1024];
			// try (InputStream in = ins) {
			// IOUtils.read(in, b);
			//
			// }
			String line;
			builder = new StringBuilder(150);
			while ((line = reader.readLine()) != null) {
				builder.append(line + "\n");
			}
			reader.close();
			// System.out.println(builder);
			if (ins != null) {
				ins.close();
			}
			// fileText = IOUtils.toString(new FileInputStream(filePath));
			fileText = builder.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileText;
	}

	private String getAvaiableFtpUrl(String resourceVersionId) {
		Version resourceVersion = resourceDao.getResourceVersion(resourceVersionId);
		String ftpUrl = resourceVersion.getResourcePath();
		// String ftpUrl =
		// "ftp://paas:123456@10.1.108.33/war/931675f8-2dd2-4203-b444-f8ceea2326b4/dangqun.war";
		String newFtpUrl = "ftl";
		int a = 100;
		String[] urls = ftpUrl.split("/");
		for (int i = 0; i < urls.length; i++) {
			if (urls[i].contains(this.url)) {
				a = i;
			}
			if (i > a) {
				newFtpUrl = newFtpUrl + File.separator + urls[i];
			}
		}
		newFtpUrl = newFtpUrl.substring(0, newFtpUrl.lastIndexOf(".")).concat(File.separator);
		System.out.println("工件包ftp路径:" + newFtpUrl);
		return newFtpUrl;
	}

	@Override
	public boolean judgeWorkpieceUrl(String url) {
		boolean result = true;
		String newFtpUrl = "";
		int a = 100;
		String[] urls = url.split("/");
		for (int i = 0; i < urls.length; i++) {
			if (urls[i].contains(this.url)) {
				a = i;
			}
			if (i > a) {
				newFtpUrl = newFtpUrl + File.separator + urls[i];
			}
		}

		FtpUtils f = new FtpUtils();
		try {
			if (f.login(this.url, this.port, this.user, this.pwd)) {
				result = f.isLegalUrl(newFtpUrl);
			}
			f.disConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<VersionFtl> getVersionFtl(String resourceVersionId) {
		List<VersionFtl> vF = resourceDao.getVersionFtl(resourceVersionId);

		return vF;
	}

	@Override
	public boolean updateVersionFtl(String id, String ftlText) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		// map.put("ftlName", ftlName);
		// map.put("templates", templates);
		map.put("ftlText", ftlText);
		return resourceDao.updateVersionFtl(map);
	}

	@Transactional
	@Override
	public Long addNewResourceFlow(Map<String, Object> map) {
		// resourceId、flowType、flowName、flowInfo
		String resourceId = "" + map.get("resourceId");
		String flowType = "" + map.get("flowType");
		String flowName = "" + map.get("flowName");
		String flowInfo_condition = "" + map.get("flowInfo");
		String emptyFlow = "{\"class\":\"go.GraphLinksModel\",\"modelData\":{\"position\":\"-494.34375 -509\"},\"nodeDataArray\":[{\"key\":101,\"eleType\":\"flowcontrol\",\"category\":\"event\",\"text\":\"开始\",\"flowcontroltype\":1,\"eventType\":1,\"eventDimension\":1,\"item\":\"start\",\"loc\":\"-310.34375 -81\"},{\"key\":104,\"eleType\":\"flowcontrol\",\"category\":\"event\",\"text\":\"结束\",\"flowcontroltype\":2,\"eventType\":1,\"eventDimension\":8,\"item\":\"End\",\"loc\":\"-207.34375 -81\"}],\"linkDataArray\":[{\"from\":101,\"to\":104,\"points\":[-288.84375,-81,-278.84375,-81,-264.59375,-81,-264.59375,-81,-250.34375,-81,-230.34375,-81]}]}";
		if (flowName == null || "".equals(flowName.trim())) {
			throw new RuntimeException("流程名称不允许为空，请重新填写");
		}
		map.put("flowName", flowName.trim());
		int i = resourceDao.findResourceFlowByTypeAndName(map);
		if (i > 0) {
			throw new RuntimeException("组件的流程类型[" + flowType + "]下已存在同名流程[" + flowName + "]请重新命名流程!");
		}
		long flowId = -1;
		JSONObject jsonCondition;
		JSONObject jsonSend;
		// 如果流程设计器给出的流程json为空/undefined/无node和link的空保存json，则默认为空流程(只包含开始、结束及连线)
		if (isEmptyFlow(flowInfo_condition)) {
			jsonCondition = JSON.parseObject(emptyFlow);
			jsonSend = JSON.parseObject(emptyFlow);
		} else {
			jsonCondition = JSON.parseObject(flowInfo_condition);
			ComponentConvert cc = new ComponentConvert();
			String flowInfo;
			try {
				flowInfo = cc.convertComFlow2Runtime(flowInfo_condition);
			} catch (Exception e) {
				throw new RuntimeException("组件子流程中间态转换失败，reason[" + e.getMessage() + "]");
			}
			jsonSend = JSON.parseObject(flowInfo);
		}
		jsonCondition.put("issub", true);
		jsonCondition.put("type", flowType);
		jsonCondition.put("bluePrintId", resourceId);
		jsonCondition.put("flowName", flowName);
		if (flowInfo_condition.indexOf("DEC(") != -1) {
			SensitiveDataUtil.encryptMapFlow(jsonCondition);
		}
		jsonSend.put("issub", true);
		jsonSend.put("type", flowType);
		jsonSend.put("bluePrintId", resourceId);
		jsonSend.put("flowName", flowName);
		if (flowInfo_condition.indexOf("DEC(") != -1) {
			SensitiveDataUtil.encryptMapFlow(jsonSend);
		}
		String flowResult = blueprintService.cdflowToSmartflow(JSON.toJSONString(jsonSend));
		if (flowResult == null) {
			throw new RuntimeException("模型转换失败，请检查流程信息！");
		} else {
			flowId = Long.parseLong(flowResult);
			map.put("flowId", String.valueOf(flowId));
			map.put("flowInfo_condition", JSON.toJSONString(jsonCondition));
			map.put("flowInfo", JSON.toJSONString(jsonSend));
			// 如果没有，则新增
			resourceDao.saveNewResourceFlow(map);
		}
		return flowId;
	}

	@Override
	public String getNewResourceFlows(String resourceId, String sortName, String sortOrder) {
		JSONObject json = new JSONObject();
		List<Map<String, Object>> list = resourceDao.getAllFlowByResourceId(resourceId);
		Map<String, List<Map<String, String>>> maps = new HashMap<String, List<Map<String, String>>>();
		if (list != null && list.size() > 0) {
			for (Map<String, Object> item : list) {
				String flowType = "" + item.get("FLOW_TYPE");
				if (!maps.containsKey(flowType)) {
					List<Map<String, String>> listType = new ArrayList<Map<String, String>>();
					maps.put(flowType, listType);
				}
				Map<String, String> map = new HashMap<String, String>();
				String cdFlowId = "" + item.get("ID");
				String flowId = "" + item.get("FLOW_ID");
				String flowName = "" + item.get("FLOW_NAME");
				map.put("cdFlowId", cdFlowId);
				map.put("flowId", flowId);
				map.put("flowName", flowName);
				maps.get(flowType).add(map);
			}
			for (Map.Entry<String, List<Map<String, String>>> one : maps.entrySet()) {
				List<Map<String, String>> typelist = one.getValue();
				SortUtil.sort(typelist, SortUtil.getColunmName("componentFlow", sortName), sortOrder);
			}
		}
		return json.toJSONString(maps, SerializerFeature.WriteDateUseDateFormat);
	}

	@Override
	public String getNewFlowInfoByFlowId(String cdFlowId) {
		String flowInfo = resourceDao.getNewFlowInfoByFlowId(cdFlowId);
		if (flowInfo != null && flowInfo.indexOf("ENC(") != -1) {
			Map<String, Object> mapFlow = JSON.parseObject(flowInfo, new TypeReference<Map<String, Object>>() {
			});
			SensitiveDataUtil.decryptMapFlow(mapFlow);
			return JSON.toJSONString(mapFlow);
		} else {
			return flowInfo;
		}
	}

	@Transactional
	@Override
	public boolean updateNewFlowInfoByFlowId(String cdFlowId, String flowInfo) {
		Map<String, Object> map = resourceDao.getNewFlowDetailByFlowId(cdFlowId);
		// 无此flowId
		if (map == null || map.size() == 0) {
			return false;
		}
		String flowId = "" + map.get("FLOW_ID");
		String resourceId = "" + map.get("RESOURCE_ID");
		String flowType = "" + map.get("FLOW_TYPE");
		String flowName = "" + map.get("FLOW_NAME");
		String emptyFlow = "{\"class\":\"go.GraphLinksModel\",\"modelData\":{\"position\":\"-494.34375 -509\"},\"nodeDataArray\":[{\"key\":101,\"eleType\":\"flowcontrol\",\"category\":\"event\",\"text\":\"开始\",\"flowcontroltype\":1,\"eventType\":1,\"eventDimension\":1,\"item\":\"start\",\"loc\":\"-310.34375 -81\"},{\"key\":104,\"eleType\":\"flowcontrol\",\"category\":\"event\",\"text\":\"结束\",\"flowcontroltype\":2,\"eventType\":1,\"eventDimension\":8,\"item\":\"End\",\"loc\":\"-207.34375 -81\"}],\"linkDataArray\":[{\"from\":101,\"to\":104,\"points\":[-288.84375,-81,-278.84375,-81,-264.59375,-81,-264.59375,-81,-250.34375,-81,-230.34375,-81]}]}";
		JSONObject jsonCondition;
		JSONObject jsonSend;
		// 流程信息为空/undefined/无node和link的空保存json，则默认为空流程(只包含开始、结束及连线)
		String flowInfo_condition = flowInfo;
		String flowInfo_workflow = null;
		if (isEmptyFlow(flowInfo_condition)) {
			jsonCondition = JSON.parseObject(emptyFlow);
			jsonSend = JSON.parseObject(emptyFlow);
		} else {
			jsonCondition = JSON.parseObject(flowInfo_condition);
			ComponentConvert cc = new ComponentConvert();
			try {
				flowInfo_workflow = cc.convertComFlow2Runtime(flowInfo_condition);
			} catch (Exception e) {
				throw new RuntimeException("组件子流程中间态转换失败，reason[" + e.getMessage() + "]");
			}
			jsonSend = JSON.parseObject(flowInfo_workflow);
		}
		jsonCondition.put("issub", true);
		jsonCondition.put("type", flowType);
		jsonCondition.put("bluePrintId", resourceId);
		jsonCondition.put("flowName", flowName);
		if (flowInfo_condition.indexOf("DEC(") != -1) {
			SensitiveDataUtil.encryptMapFlow(jsonCondition);
		}
		jsonSend.put("issub", true);
		jsonSend.put("type", flowType);
		jsonSend.put("bluePrintId", resourceId);
		jsonSend.put("flowName", flowName);
		if (flowInfo_condition.indexOf("DEC(") != -1) {
			SensitiveDataUtil.encryptMapFlow(jsonSend);
		}
		// 更新工作流模型转换
		jsonSend.put("workFlowId", flowId);
		String flowResult = blueprintService.cdflowToSmartflow(JSON.toJSONString(jsonSend));
		if (flowResult == null) {
			throw new RuntimeException(flowId + "工作流转换失败");
		} else {
			// 如果工作流已有流程flowId，则flowResult==flowId
			// 如果工作流无流程flowId，则flowResult!=flowId，此时flowResult是新的flowId
			// update全部兼容
			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put("cdFlowId", cdFlowId);
			updateMap.put("flowId", flowResult);
			updateMap.put("flowInfo_condition", JSON.toJSONString(jsonCondition));
			updateMap.put("flowInfo_workflow", JSON.toJSONString(jsonSend));
			resourceDao.updateNewFlowDetailById(updateMap);
		}
		return true;
	}

	@Transactional
	@Override
	public void deleteNewResourceFlow(String cdFlowId) {
		resourceDao.deleteNewResourceFlow(cdFlowId);
	}

	@Transactional
	@Override
	public void deleteNewResourceVersion(String versionId) {
		resourceDao.deleteResourceVersion("", versionId);
		resourceDao.deleteNewVersionFtlByVersionId(versionId);
	}

	@Transactional
	@Override
	public void deleteNewResource(String resourceId) {
		List<Version> versionList = resourceDao.listResourceVersion(resourceId);
		for (Version version : versionList) {
			resourceDao.deleteNewVersionFtlByVersionId(version.getId());
		}
		resourceDao.deleteResourceVersion(resourceId, "");
		resourceDao.deleteNewResourceFlowByResourceId(resourceId);
		resourceDao.deleteResource(resourceId);
		JSONObject params = new JSONObject();
		params.put("resourceId", resourceId);
		labelsDAO.deleteLabelResMapByParams(params);
	}

	@Override
	public String getNewFlowVarsByFlowId(String cdFlowId) {
		StringBuffer sb = new StringBuffer();
		List<String> paraList = new ArrayList<String>();
		String flowInfo = resourceDao.getNewFlowInfoByFlowId(cdFlowId);
		if (flowInfo != null) {
			JSONObject flowJson = JSON.parseObject(flowInfo);
			JSONArray nodes = flowJson.getJSONArray("nodeDataArray");
			if (nodes.size() > 0) {
				for (int i = 0; i < nodes.size(); i++) {
					JSONObject node = (JSONObject) nodes.get(i);
					String flowcontroltype = node.getString("flowcontroltype");
					// 寻找开始节点
					if ("1".equals(flowcontroltype)) {
						String paras = node.getString("paras");
						if (paras != null && paras.length() > 0) {
							Map<String, String> parasMap = JSON.parseObject(paras,
									new TypeReference<Map<String, String>>() {
									});
							Iterator<Entry<String, String>> parasIter = parasMap.entrySet().iterator();
							while (parasIter.hasNext()) {
								Entry<String, String> paraEntry = parasIter.next();
								paraList.add(paraEntry.getKey());
							}
						}
						break;
					}
				}
			}
		}
		if (paraList.size() > 0) {
			for (int j = 0; j < paraList.size(); j++) {
				sb.append(paraList.get(j));
				if (j < paraList.size() - 1) {
					sb.append(";");
				}
			}
		}
		return sb.toString();
	}

	@Override
	public Page listNewResourceVersionByPage(Map<String, Object> condition, int pageNum, int pageSize) {
		Page p = resourceDao.listResourceVersionByPage(condition, pageNum, pageSize);
		List<Version> list = p.getRows();
		for (Version v : list) {
			addFtpLocation(v);
		}
		for (Version item : list) {
			SensitiveDataUtil.decryptVersionConfig(item);
		}
		return p;
	}

	@Override
	public Map<String, Object> getNewResourceVersionDetail(String resourceVersionId) {
		Version v = resourceDao.getResourceVersion(resourceVersionId);
		String version = v.getVersionName();
		Map<String, Object> result = new HashMap<>();
		// FIXME template
		result.put("input", v.getInput());
		result.put("output", v.getOutput());
		result.put("version", version);
		result.put("ftpLocation", v.getResourcePath());
		result.put("url", v.getResourcePath());
		result.put("type", v.getType());
		result.put("versionName", v.getVersionName());
		result.put("versionDesc", v.getVersionDesc());
		return result;
	}

	@Override
	public void deleteVersionFtl(String id) {
		resourceDao.deleteVersionFtl(id);

	}

	@Override
	public String exportResources(String ids) {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String now = format.format(new Date());
		String absPath = File.separator + "ex_resTmp" + File.separator + now;
		File folder = new File(absPath);
		folder.mkdirs();

		List<Map<String, Object>> paramList = JSONObject.parseObject(ids, List.class);
		for (Map<String, Object> paramMap : paramList) {// 遍历组件
			List<String> idList = new ArrayList<String>();
			String resourceId = "" + paramMap.get("resourceId");
			idList.add(resourceId);
			zipResourceAndPackage(idList, folder, "" + paramMap.get("exportPackageId"));
		}
		// 3 打包zip
		String zipPath = folder.getParent() + File.separator + "resource" + now + ".zip";
		ZipUtil.doCompress(folder, new File(zipPath));
		return MessageHelper.wrap("result", true, "message", zipPath);
	}

	@Transactional
	@Override
	public String importResources(String resource, String flow, String version, String ftl, String packages, String zip,
			String userId) {

		packageService.importPackage(packages, zip, userId);// 导入工件包

		List<Map<String, Object>> resourceList = JSON.parseObject(resource,
				new TypeReference<List<Map<String, Object>>>() {
				});
		// sv_resoure
		for (Map<String, Object> resourceMap : resourceList) {
			String resourceName = (String) resourceMap.get("RESOURCE_NAME");
			String id = (String) resourceMap.get("ID");
			int temp = this.findResourceByName(resourceName);
			if (temp >= 1) {
				this.deleteNewResource(id);
			}
			this.importResource(resourceMap);
			// ==============添加审计start===================
			ThreadPool.service.execute(new Runnable() {
				@Override
				public void run() {
					auditService.save(new AuditEntity(userId, ResourceCode.COMPONENT, resourceName,
							ResourceCode.Operation.IMPORT, 1, "导入组件:" + resourceName));
				}
			});
			// ==============添加审计end=====================
		}
		// sv_version
		List<Map<String, Object>> versionList = JSON.parseObject(version,
				new TypeReference<List<Map<String, Object>>>() {
				});
		for (Map<String, Object> versionMap : versionList) {
			versionMap.put("UPDATE_TIME", new Date());
			String resource_path = "" + versionMap.get("RESOURCE_PATH");
			// "ftp://paas:123456@10.1.108.33/war/931675f8-2dd2-4203-b444-f8ceea2326b4/dangqun.war";
			String otherPath = resource_path.split("@")[1];
			String ip = otherPath.split("/")[0];
			String newPath = resource_path.replace(ip, url);
			versionMap.put("RESOURCE_PATH", newPath);
			this.importVersion(versionMap);
			// ==============添加审计start===================
			List<String> resourList = new ArrayList<String>();
			resourList.add("" + versionMap.get("RESOURCE_ID"));
			final List<Map<String, Object>> returnResourceList = this.exportResources(resourList);// 根据resource_id获取组件信息
			final String versionName = "" + versionMap.get("VERSION_NAME");
			ThreadPool.service.execute(new Runnable() {
				@Override
				public void run() {
					auditService.save(new AuditEntity(userId, ResourceCode.COMPONENT,
							"" + returnResourceList.get(0).get("RESOURCE_NAME"), ResourceCode.Operation.IMPORT, 1,
							"导入版本:" + versionName));
				}
			});
			// ==============添加审计end=====================
		}
		// sv_ftl
		List<Map<String, Object>> ftlList = JSON.parseObject(ftl, new TypeReference<List<Map<String, Object>>>() {
		});
		for (Map<String, Object> ftlMap : ftlList) {
			this.importFtl(ftlMap);
			// ==============添加审计start===================
			final Map<String, String> resoureMap = this.getResourceByFtlId("" + ftlMap.get("ID"));
			ThreadPool.service.execute(new Runnable() {
				@Override
				public void run() {
					auditService.save(new AuditEntity(userId, ResourceCode.COMPONENT,
							"" + resoureMap.get("RESOURCE_NAME"), ResourceCode.Operation.IMPORT, 1,
							"给版本:" + resoureMap.get("VERSION_NAME") + " 导入ftl文件:" + ftlMap.get("FTL_NAME")));
				}
			});
			// ==============添加审计end=====================
		}
		// sv_flow
		List<Map<String, Object>> flowList = JSON.parseObject(flow, new TypeReference<List<Map<String, Object>>>() {
		});
		for (Map<String, Object> flowMap : flowList) {
			Map<String, Object> cloneMap = lowerMap(flowMap);
			this.addNewResourceFlow(cloneMap);
			// ==============添加审计start===================
			String resourceId = "" + flowMap.get("RESOURCE_ID");
			List resourList = new ArrayList();
			resourList.add(resourceId);
			final List<Map<String, Object>> returnResourceList = this.exportResources(resourList);
			ThreadPool.service.execute(new Runnable() {
				@Override
				public void run() {
					auditService.save(new AuditEntity(userId, ResourceCode.COMPONENT,
							"" + returnResourceList.get(0).get("RESOURCE_NAME"), ResourceCode.Operation.IMPORT, 1,
							"导入流程:" + flowMap.get("FLOW_NAME")));
				}
			});
			// ==============添加审计end=====================
		}
		return MessageHelper.wrap("result", true, "message", "导入组件包成功");
	}

	private Map<String, Object> lowerMap(Map<String, Object> map) {
		Map<String, Object> newMap = new HashMap<String, Object>();
		newMap.put("id", map.get("ID"));
		newMap.put("resourceId", map.get("RESOURCE_ID"));
		newMap.put("flowType", map.get("FLOW_TYPE"));
		newMap.put("flowName", map.get("FLOW_NAME"));
		newMap.put("flowId", map.get("FLOW_ID"));
		newMap.put("flowInfo", map.get("FLOW_INFO_CONDITION"));
		return newMap;
	}

	@Override
	public List<Map<String, Object>> getsubFlowInfo(Map<String, Object> map) {
		return resourceDao.getsubFlowInfo(map);
	}

	// 判断脚本资源是否已存在
	@Override
	public Boolean checkResourceName(String name) {
		// TODO Auto-generated method stub
		Map<String, Object> param = new HashMap<>();
		param.put("resourceName", name);
		Map<String, Object> resource = resourceDao.findScriptResource(param);
		if (resource != null && resource.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map<String, List<Map<String, String>>> getCDAllFlows() {
		Map<String, List<Map<String, String>>> all = new HashMap<>();
		List<Map<String, String>> resourceFlows = resourceDao.getAllResourceFlow();
		List<Map<String, String>> blueprintFlows = bluePrintTypeDao.getAllBlueprintFlow();
		all.put("component", resourceFlows);
		all.put("blueprint", blueprintFlows);
		return all;
	}

	@Override
	public List<Map<String, Object>> exportResources(List<String> tempIds) {
		return resourceDao.exportResources(tempIds);
	}

	@Override
	public Map<String, Object> getNewFlowDetailByFlowId(String cdFlowId) {
		return resourceDao.getNewFlowDetailByFlowId(cdFlowId);
	}

	@Override
	public Map<String, String> getResourceByFtlId(String id) {
		return resourceDao.getResourceByFtlId(id);
	}

	@Override
	public int findResourceByName(String resourceName) {
		return resourceDao.findResourceByName(resourceName);
	}

	@Override
	public void importResource(Map<String, Object> map) {
		resourceDao.importResource(map);
	}

	@Override
	public void importVersion(Map<String, Object> map) {
		resourceDao.importVersion(map);
	}

	@Override
	public void importFtl(Map<String, Object> map) {
		resourceDao.importFtl(map);
	}

	@Override
	public Map<String, Object> findScriptById(String id) {
		return resourceDao.findScriptById(id);
	}

	public List<String> getPackageIdByResourceId(String resourceId) {
		// 获取组件下包括的全部工件
		List<String> returnList = new ArrayList<String>();
		List<Version> resourceversionList = resourceDao.listResourceVersion(resourceId);
		for (Version v : resourceversionList) {
			addFtpLocation(v);
		}
		for (Version version : resourceversionList) {
			Map<String, Object> packageMap = packageDao.findPackageByPath(version.getResourcePath());
			returnList.add("" + packageMap.get("ID"));
		}
		return returnList;
	}

	@Override
	public void zipResourceAndPackage(List<String> idList, File parentFolder, String exportPackageId) {
		// 1.sv_resource
		List<Map<String, Object>> returnResourceList = resourceDao.exportResources(idList);
		try {
			for (Map<String, Object> ResMap : returnResourceList) {// 封装组件中的资源
				String resourceName = (String) ResMap.get("RESOURCE_NAME");
				String resourceId = (String) ResMap.get("ID");
				File resourceParent = new File(parentFolder.getPath() + File.separator + resourceName);
				resourceParent.mkdirs();

				File f = new File(resourceParent.getPath() + File.separator + resourceName + ".resource.json");
				FileOutputStream fos = new FileOutputStream(f);
				String resourceJson = JSON.toJSONString(ResMap, true);
				IOUtils.write(resourceJson, fos);

				// 2.sv_flow
				List<Map<String, Object>> flowList = resourceDao.getResourecFlow(resourceId);
				for (Map<String, Object> flowMap : flowList) {
					String flowName = (String) flowMap.get("FLOW_NAME");
					String flowType = (String) flowMap.get("FLOW_TYPE");
					File versionFile = new File(
							resourceParent.getPath() + File.separator + flowName + "_" + flowType + ".flow.json");
					FileOutputStream versionFos = new FileOutputStream(versionFile);
					String flowJson = JSON.toJSONString(flowMap, true);
					IOUtils.write(flowJson, versionFos);
				}

				// 3.sv_version
				List<Map<String, Object>> versionList = resourceDao.getResourecVersion(resourceId);
				for (Map<String, Object> versionMap : versionList) {
					String versionName = (String) versionMap.get("VERSION_NAME");
					String versionJson = JSON.toJSONString(versionMap, true);
					File versionFile = new File(
							resourceParent.getPath() + File.separator + versionName + ".version.json");
					FileOutputStream versionFos = new FileOutputStream(versionFile);
					IOUtils.write(versionJson, versionFos);

					// 4.sv_ftl
					String versionId = (String) versionMap.get("ID");
					List<Map<String, Object>> ftlList = resourceDao.getResourecFtl(versionId);
					for (Map<String, Object> ftlMap : ftlList) {
						String ftlId = (String) ftlMap.get("ID");
						String ftlName = (String) ftlMap.get("FTL_NAME");

						File ftlFile = new File(
								resourceParent.getPath() + File.separator + ftlId + "_" + ftlName + ".ftl.json");
						FileOutputStream ftlFos = new FileOutputStream(ftlFile);
						String ftlJson = JSON.toJSONString(ftlMap, true);
						IOUtils.write(ftlJson, ftlFos);
					}
				}

				// 5.sv_package
				List<String> needExportPackageList = JSONObject.parseObject(exportPackageId, List.class);
				List<String> packageList = getPackageIdByResourceId(resourceId);
				for (String packageId : packageList) {
					Map<String, Object> packageMap = packageDao.findPackageById(packageId);
					String packageName = "" + packageMap.get("RESOURCE_NAME");// 工件名
					File packageFolder = new File(
							parentFolder.getPath() + File.separator + resourceName + File.separator + packageName);
					packageFolder.mkdirs();

					File packageFile = new File(
							packageFolder.getPath() + File.separator + packageName + ".workpiece.json");
					FileOutputStream packageFos = new FileOutputStream(packageFile);
					String packageJson = JSON.toJSONString(packageMap, true);
					IOUtils.write(packageJson, packageFos);// 导出工件描述

					if (needExportPackageList.contains(packageId)) {// 需要导出的工件包
						packageService.downloadFile(packageId, packageFolder.getPath());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public JSONObject saveAudit(JSONObject params) {
		// TODO Auto-generated method stub
		JSONObject result = resourceDao.saveAudit(params);
		return result;
	}

	@Override
	public void bindComponentFlow(Map<String, Object> params) {
		int type = (int) params.get("bindType");
		if (type == 0) {
			resourceDao.bindComponentReleaseFlow(params);
		} else if (type == 1) {
			resourceDao.bindComponentRollbackFlow(params);
		} else if(type == 2){
			resourceDao.bindComponentStartFlow(params);
		} else if(type == 3){
			resourceDao.bindComponentStopFlow(params);
		} else{
		}
	}

	@Override
	public String executeComponentVersionFlow(Map<String, Object> params, Object obj) {
		String versionId = (String) params.get("componentVersionId");
		int type = (int) params.get("componentExecuteType");
		//新增itsm组件版本审计为ing
		JSONObject audit = new JSONObject();
		String auditId = UUID.randomUUID().toString();
		audit.put("id", auditId);
		audit.put("userId", params.get("userId"));
		audit.put("resourceVersionId", versionId);
		String opType = null;
		String opStatusing = null;
		String opDescription = null;
		switch(type){
		case 0:
			opType = Constants.audit4Resource.OP_PUBLIC;
			opStatusing = Constants.audit4Resource.STATUS_PUBLICED;
			opDescription = Constants.audit4Resource.OP_PUBLIC;
			break;
		case 1:
			opType = Constants.audit4Resource.OP_BACK;
			opStatusing = Constants.audit4Resource.STATUS_BACKED;
			opDescription = Constants.audit4Resource.OP_BACK;
			break;
		case 2:
			opType = Constants.audit4Resource.OP_START;
			opStatusing = Constants.audit4Resource.STATUS_START;
			opDescription = Constants.audit4Resource.OP_START;
			break;
		case 3:
			opType = Constants.audit4Resource.OP_STOP;
			opStatusing = Constants.audit4Resource.STATUS_STOP;
			opDescription = Constants.audit4Resource.OP_STOP;
			break;
		default:
			break;
		}
		audit.put("opType", opType);
		audit.put("status", opStatusing);
		audit.put("description", opDescription);
		saveAudit(audit);
		if(type == 0 || type == 1){
			//更新itsm组件版本状态为ing
			Map<String, Object> param = new HashMap<>();
			param.put("status", opStatusing);
			param.put("versionId", versionId);
			resourceDao.updateResourceVersion(param);
		}
		else if(type == 2 || type == 3){
			//更新itsm组件状态为ing
			Map<String, Object> param = new HashMap<>();
			param.put("status", opStatusing);
			param.put("resourceId", versionId);
			resourceDao.updateResourceStatus(param);
		}
		//组件版本类型app、sql
		boolean isApp = false;
		boolean isSql = false;
		Map<String, Object> resourceDetail = null;
		//发布、回退
		if(type == 0 || type == 1){
			Map<String, Object> versionDetail = resourceDao.getVersionDetailByVersionId(versionId);
			String versionName = "" + versionDetail.get("VERSION_NAME");
			if(versionName.indexOf("app") != -1 || versionName.indexOf("App") != -1 || versionName.indexOf("APP") != -1 || versionName.indexOf("class") != -1 || versionName.indexOf("Class") != -1 || versionName.indexOf("CLASS") != -1){
				isApp = true;
			}
			if(versionName.indexOf("sql") != -1 || versionName.indexOf("Sql") != -1 || versionName.indexOf("SQL") != -1 || versionName.indexOf("db") != -1 || versionName.indexOf("Db") != -1 || versionName.indexOf("DB") != -1){
				isSql = true;
			}
			resourceDetail = resourceDao.getResourceDetailByVersionId(versionId);
		}
		//启动、停止
		else if(type == 2 || type == 3){
			isApp = true;
			resourceDetail = resourceDao.getResourceDetailById(versionId);
		}
		else{
		}
		int blueprintId = -1;
		String cdFlowId = null;
		if (type == 0) {
			blueprintId = (int) resourceDetail.get("RELEASE_BLUEPRINT_ID");
			cdFlowId = "" + resourceDetail.get("RELEASE_BLUEPRINT_FLOW");
		} else if (type == 1) {
			blueprintId = (int) resourceDetail.get("ROLLBACK_BLUEPRINT_ID");
			cdFlowId = "" + resourceDetail.get("ROLLBACK_BLUEPRINT_FLOW");
		} else if (type == 2) {
			blueprintId = (int) resourceDetail.get("START_BLUEPRINT_ID");
			cdFlowId = "" + resourceDetail.get("START_BLUEPRINT_FLOW");
		} else if (type == 3) {
			blueprintId = (int) resourceDetail.get("STOP_BLUEPRINT_ID");
			cdFlowId = "" + resourceDetail.get("STOP_BLUEPRINT_FLOW");
		} else{
		}
		if (blueprintId == -1 || cdFlowId == null) {
			Map<String, Object> result = new HashMap<>();
			result.put("result", false);
			if (type == 0) {
				result.put("message", "组件未绑定发布流程！");
			} else if (type == 1) {
				result.put("message", "组件未绑定回退流程！");
			} else if (type == 2) {
				result.put("message", "组件未绑定启动流程！");
			} else if (type == 3) {
				result.put("message", "组件未绑定停止流程！");
			} else {
			}
			return JSON.toJSONString(result);
		}
		Map<String, Object> blueprintDetail = blueprintService.getBlueprintInstanceById(blueprintId);
		String blueprintInstanceId = "" + blueprintDetail.get("INSTANCE_ID");
		// 更新蓝图组件实例配置
		List<Application> apps = blueprintService.getBlueprintComponents(blueprintInstanceId);
		for (Application app : apps) {
			String appResourceId = app.getResourceId();
			String appComponentName = app.getComponentName();
			String resourceId = "" + resourceDetail.get("ID");
			String resourceName = "" + resourceDetail.get("RESOURCE_NAME");
			if (appResourceId.equals(resourceId) && appComponentName.equals(resourceName)) {
				//组件子流程是否跳过
				int executeFlag = 0;
				String displayName = app.getNodeDisplay();
				if(isSql){
					if(displayName.indexOf("sql") != -1 || displayName.indexOf("Sql") != -1 || displayName.indexOf("SQL") != -1 || displayName.indexOf("db") != -1 || displayName.indexOf("Db") != -1 || displayName.indexOf("DB") != -1){
						executeFlag = 1;
					}
				}
				if(isApp){
					if(displayName.indexOf("app") != -1 || displayName.indexOf("App") != -1 || displayName.indexOf("APP") != -1 || displayName.indexOf("class") != -1 || displayName.indexOf("Class") != -1 || displayName.indexOf("CLASS") != -1){
						executeFlag = 1;
					}
				}
				Map<String, Object> map = new HashMap<>();
				long appId = app.getId();
				map.put("appId", appId);
				map.put("executeFlag", executeFlag);
				if(executeFlag == 1){
					//发布、回退
					if(type == 0 || type ==1){
						map.put("versionId", versionId);
					}
					//启动、停止
					else if(type == 2 || type == 3){
						List<Version> versions = resourceDao.listResourceVersion(versionId);
						map.put("versionId", versions.get(0).getId());
					}
					else{
					}
				}
				applicationDao.updateAppVersion(map);
			}
			else{
				Map<String, Object> map = new HashMap<>();
				long appId = app.getId();
				map.put("appId", appId);
				map.put("executeFlag", 0);
				applicationDao.updateAppVersion(map);
			}
		}
		String result = null;
		try {
			// 执行流程
			Map<String, String> flowParam = new HashMap<>();
			flowParam.put("_userName", "" + params.get("userId"));
			result = blueprintService.executeBlueprintFlow(cdFlowId, blueprintInstanceId, flowParam);
		} catch (Exception e) {
			e.printStackTrace();
			result = MessageHelper.wrap("result",false,"message","执行流程失败：" + e.getMessage());
		}
		Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
		});
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				int operateResult = ((boolean)resultMap.get("result")) ? 1 : 0;
				Map<String, Object> resource = null;
				String operation = null;
				String operationMessage = null;
				String versionName = null;
				if(type == 0 || type == 1){
					if(type == 0){
						operation = ResourceCode.Operation.ITSMRELEASE;
						operationMessage = "发布";
					}
					else if(type == 1){
						operation = ResourceCode.Operation.ITSMROLLBACK;
						operationMessage = "回退";
					}
					Map<String, Object> versionDetail = resourceDao.getVersionDetailByVersionId(versionId);
					versionName = "" + versionDetail.get("VERSION_NAME");
					resource = resourceDao.getResourceDetailByVersionId(versionId);
				}
				//启动、停止
				else if(type == 2 || type == 3){
					if(type == 2){
						operation = ResourceCode.Operation.ITSMSTART;
						operationMessage = "启动";
					}
					else if(type == 3){
						operation = ResourceCode.Operation.ITSMSTOP;
						operationMessage = "停止";
					}
					resource = resourceDao.getResourceDetailById(versionId);
				}
				auditService.save(new AuditEntity("" + params.get("userId"), ResourceCode.COMPONENTITSM, ((versionName == null) ? "" + resource.get("RESOURCE_NAME") : versionName), operation, operateResult, "ITSM组件[" + resource.get("RESOURCE_NAME") + "]" + ((versionName == null) ? "" : ("版本[" + versionName + "]"))  + operationMessage));
			}
		});
		
		//检查流程执行情况
		Runnable checkState = new Runnable(){
			@Override
			public void run() {
				String status = null;
				String statusSuccess = null;
				String statusFailed = null;
				switch(type){
				case 0:
					statusSuccess = Constants.audit4Resource.STATUS_PUBLICED_SUCCESSFUL;
					statusFailed = Constants.audit4Resource.STATUS_PUBLICED_FAILED;
					break;
				case 1:
					statusSuccess = Constants.audit4Resource.STATUS_BACKED_SUCCESSFUL;
					statusFailed = Constants.audit4Resource.STATUS_BACKED_FAILED;
					break;
				case 2:
					statusSuccess = Constants.audit4Resource.STATUS_START_SUCCESSFUL;
					statusFailed = Constants.audit4Resource.STATUS_START_FAILED;
					break;
				case 3:
					statusSuccess = Constants.audit4Resource.STATUS_STOP_SUCCESSFUL;
					statusFailed = Constants.audit4Resource.STATUS_STOP_FAILED;
					break;
				default:
					break;
				}
				boolean executeState = (boolean) resultMap.get("result");
				if (executeState) {
					String instanceId = (String)resultMap.get("id");
					Map<String, Object> singleInstanceMap = new HashMap<>();
					singleInstanceMap.put("instanceId", instanceId);
					List<Map> instanceList = new ArrayList<>();
					instanceList.add(singleInstanceMap);
					RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();
					MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
					requestEntity.add("instancesList",
							JSON.toJSONString(instanceList, SerializerFeature.WriteDateUseDateFormat,
									SerializerFeature.DisableCircularReferenceDetect));
					long times = -1L;
					if (itsmTimeout <= 0) {
						// 不设置超时时间，默认刷360次(30分钟 )
						times = 360L;
					} else {
						times = itsmTimeout / 5000L;
						if (itsmTimeout % 5000L != 0) {
							times++;
						}
					}
					while (times > 0) {
						try {
							// 5秒查一次
							Thread.sleep(5000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						String instanceResult = restUtil.postForObject(
								flowServerUrl + "/WFService/getInstancesStatus.wf", requestEntity, String.class);
						Map<String, Object> instanceResultMap = JSON.parseObject(instanceResult);
						if ((Boolean) instanceResultMap.get("state")) {
							List<Map> instanceResultData = JSON
									.parseArray(instanceResultMap.get("data").toString(), Map.class);
							String instanceFlowState = (String)instanceResultData.get(0).get("flowState");
							Object isOverTime = (String)instanceResultData.get(0).get("isOverTime");
							String instanceState = String.valueOf(instanceResultData.get(0).get("state"));
							//流程已结束
							if(Constants.Monitor.FINISHED.equals(instanceFlowState)){
								break;
							}
							//流程未结束
							else{
								//挂起超时
								if(Constants.Monitor.OverTime.equals(isOverTime)){
									times = 0;
									break;
								}
								//状态已结束
								if ("2".equals(instanceState)) {
									break;
								}
								//状态正在执行0/失败7/...
								else{
									times--;
								}
							}
						}
						else{
							times--;
						}
					}
					if (times == 0) {
						// 超时时间到了还没执行结束/流程超时
						status = statusFailed;
					}
					else{
						status = statusSuccess;
					}
				} else {
					status = statusFailed;
				}
				if(type == 0 || type == 1){
					//更新itsm组件版本最终状态
					Map<String, Object> param = new HashMap<>();
					param.put("status", status);
					param.put("versionId", versionId);
					resourceDao.updateResourceVersion(param);
				}
				else if(type == 2 || type == 3){
					//更新itsm组件版本最终状态
					Map<String, Object> param = new HashMap<>();
					param.put("status", status);
					param.put("resourceId", versionId);
					resourceDao.updateResourceStatus(param);
				}
				//更新itsm组件版本最终审计
				JSONObject audit = new JSONObject();
				audit.put("id", auditId);
				audit.put("status", status);
				updateAudit(audit);
				if(obj != null){
					synchronized(obj){
						obj.notifyAll();
					}
				}
			}};
			ITSMBatchReleasePool.getInstance().execute(checkState);
		return result;
	}
	
	@Override
	public void releaseComponentVersionsByBatch(Map<String, Object> params) {
		String componentVersionIds = "" + params.get("componentVersionIds");
		int type = (int) params.get("componentExecuteType");
		String userId = "" + params.get("userId");
		List versions = sortVersion(componentVersionIds);
		if (type == 0) {
			boolean batchReleaseSuccessful = true;
			//开始批量执行状态
			JSONObject auditBegin = new JSONObject();
			String auditId = UUID.randomUUID().toString();
			auditBegin.put("id", auditId);
			auditBegin.put("userId", params.get("userId"));
			Map<String, Object> versionDetail = resourceDao.getVersionDetailByVersionId("" + versions.get(0));
			String resourceId = "" + versionDetail.get("RESOURCE_ID");
			auditBegin.put("resourceVersionId", resourceId);
			auditBegin.put("opType", Constants.audit4Resource.OP_BATCH_RELEASE);
			auditBegin.put("status", Constants.audit4Resource.STATUS_BATCH_RELEASE);
			auditBegin.put("description", Constants.audit4Resource.OP_BATCH_RELEASE);
			saveAudit(auditBegin);
			Map<String, Object> paramBegin = new HashMap<>();
			paramBegin.put("status", Constants.audit4Resource.STATUS_BATCH_RELEASE);
			paramBegin.put("resourceId", resourceId);
			resourceDao.updateResourceStatus(paramBegin);
			Map<String, Object> resourceDetail = resourceDao.getResourceDetailById(resourceId);
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(userId, ResourceCode.COMPONENTITSM, "" + resourceDetail.get("RESOURCE_NAME"), ResourceCode.Operation.ITSMBATCHRELEASE, 1, "组件[" + resourceDetail.get("RESOURCE_NAME") + "]批量发布"));
				}
			});
			Object obj = new Object();
			for (int i = 0; i < versions.size(); i++) {
				Map<String, Object> map = new HashMap<>();
				map.put("userId", userId);
				map.put("componentVersionId", versions.get(i));
				map.put("componentExecuteType", type);
				String singleResult = executeComponentVersionFlow(map, obj);
				synchronized(obj){
					Map<String, Object> singleResultMap = JSON.parseObject(singleResult,
							new TypeReference<Map<String, Object>>() {
					});
					boolean singleExecuteState = (boolean) singleResultMap.get("result");
					if (singleExecuteState) {
						if (itsmTimeout <= 0) {
							// 不设置超时时间，默认30分钟
							itsmTimeout = 1800000L;
						}
						try {
							obj.wait(itsmTimeout);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
							Thread.currentThread().interrupt();
						}
					/*long times = -1L;
					if (itsmTimeout <= 0) {
						// 不设置超时时间，默认刷720次(1小时)
						times = 720L;
					}else {
						times = itsmTimeout / 5000L;
						if (itsmTimeout % 5000L != 0) {
							times++;
						}
					}
					while (times > 0) {
						try {
							// 5秒查一次
							Thread.sleep(5000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						String versionStatus = resourceDao.getResourceVersionStatus("" + versions.get(i));
						if (versionStatus != null) {
							//流程未结束，进行中
							if(Constants.audit4Resource.STATUS_PUBLICED.equals(versionStatus) || Constants.audit4Resource.STATUS_BACKED.equals(versionStatus)){
								times--;
							}
							//流程已结束，成功
							else if(Constants.audit4Resource.STATUS_PUBLICED_SUCCESSFUL.equals(versionStatus) || Constants.audit4Resource.STATUS_BACKED_SUCCESSFUL.equals(versionStatus)){
								break;
							}
							//流程已结束，失败
							else if(Constants.audit4Resource.STATUS_PUBLICED_FAILED.equals(versionStatus) || Constants.audit4Resource.STATUS_BACKED_FAILED.equals(versionStatus)){
								times = 0;
								break;
							}
							else{
							}
						}
						else{
							times--;
						}
					}
					if (times == 0) {
						// 超时时间到了还没执行成功或者流程失败，不再循环执行，跳出批量
						batchReleaseSuccessful = false;
						break;
					}*/
						String versionStatus = resourceDao.getResourceVersionStatus("" + versions.get(i));
						if (versionStatus != null) {
							//流程未结束，进行中(超时)
							if(Constants.audit4Resource.STATUS_PUBLICED.equals(versionStatus) || Constants.audit4Resource.STATUS_BACKED.equals(versionStatus)){
								batchReleaseSuccessful = false;
								break;
							}
							//流程已结束，成功
							else if(Constants.audit4Resource.STATUS_PUBLICED_SUCCESSFUL.equals(versionStatus) || Constants.audit4Resource.STATUS_BACKED_SUCCESSFUL.equals(versionStatus)){
								batchReleaseSuccessful = true;
							}
							//流程已结束，失败
							else if(Constants.audit4Resource.STATUS_PUBLICED_FAILED.equals(versionStatus) || Constants.audit4Resource.STATUS_BACKED_FAILED.equals(versionStatus)){
								batchReleaseSuccessful = false;
								break;
							}
							//其他情况
							else{
								batchReleaseSuccessful = false;
								break;
							}
						}
					} else {
						//工作流执行失败，跳出批量
						batchReleaseSuccessful = false;
						break;
					}
				}
			}
			//更新批量执行状态
			String statusEnd = batchReleaseSuccessful ? Constants.audit4Resource.STATUS_BATCH_RELEASE_SUCCESSFUL : Constants.audit4Resource.STATUS_BATCH_RELEASE_FAILED;
			JSONObject auditEnd = new JSONObject();
			auditEnd.put("id", auditId);
			auditEnd.put("status", statusEnd);
			updateAudit(auditEnd);
			Map<String, Object> paramEnd = new HashMap<>();
			paramEnd.put("status", statusEnd);
			paramEnd.put("resourceId", resourceId);
			resourceDao.updateResourceStatus(paramEnd);
		} else {
		}
	}

	private List<String> sortVersion(String componentVersionIds) {
		JSONArray versions = JSON.parseArray(componentVersionIds);
		List<Map<String, Object>> list = new ArrayList<>();
		for(int i = 0; i < versions.size(); i++){
			String versionId = (String)versions.get(i);
			Map<String, Object> versionDetail = resourceDao.getVersionDetailByVersionId(versionId);
			list.add(versionDetail);
		}
		ITSMVersionSort comparator = new ITSMVersionSort();
		Collections.sort(list, comparator);
		List<String> sortComponentVersionIds = new ArrayList<>();
		for(Map<String, Object> item : list){
			sortComponentVersionIds.add((String)item.get("ID"));
		}
		return sortComponentVersionIds;
	}

	@Override
	public Map<String, Object> getResourceBindDetail(String resourceId) {
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> resourceDetail = resourceDao.getResourceDetailById(resourceId);
		Integer releaseBlueprintId = (Integer)resourceDetail.get("RELEASE_BLUEPRINT_ID");
		String releaseBlueprintFlow = (String)resourceDetail.get("RELEASE_BLUEPRINT_FLOW");
		Integer rollbackBlueprintId = (Integer)resourceDetail.get("ROLLBACK_BLUEPRINT_ID");
		String rollbackBlueprintFlow = (String)resourceDetail.get("ROLLBACK_BLUEPRINT_FLOW");
		Integer startBlueprintId = (Integer)resourceDetail.get("START_BLUEPRINT_ID");
		String startBlueprintFlow = (String)resourceDetail.get("START_BLUEPRINT_FLOW");
		Integer stopBlueprintId = (Integer)resourceDetail.get("STOP_BLUEPRINT_ID");
		String stopBlueprintFlow = (String)resourceDetail.get("STOP_BLUEPRINT_FLOW");
		if(releaseBlueprintId != null){
			Map<String, Object> releaseBlueprintDetail = blueprintService.getBlueprintInstanceById(releaseBlueprintId);
			result.put("releaseBlueprint", releaseBlueprintDetail);
		}
		if(rollbackBlueprintId != null){
			Map<String, Object> rollbackBlueprintDetail = blueprintService.getBlueprintInstanceById(rollbackBlueprintId);
			result.put("rollbackBlueprint", rollbackBlueprintDetail);
		}
		if(startBlueprintId != null){
			Map<String, Object> startBlueprintDetail = blueprintService.getBlueprintInstanceById(startBlueprintId);
			result.put("startAppBlueprint", startBlueprintDetail);
		}
		if(stopBlueprintId != null){
			Map<String, Object> stopBlueprintDetail = blueprintService.getBlueprintInstanceById(stopBlueprintId);
			result.put("stopAppBlueprint", stopBlueprintDetail);
		}
		if(releaseBlueprintFlow != null){
			Map<String, Object> releaseBlueprintFlowDetail = bluePrintTypeDao.getBlueprintTypeById(releaseBlueprintFlow);
			result.put("releaseBlueprintFlow", releaseBlueprintFlowDetail);
		}
		if(rollbackBlueprintFlow != null){
			Map<String, Object> rollbackBlueprintFlowDetail = bluePrintTypeDao.getBlueprintTypeById(rollbackBlueprintFlow);
			result.put("rollbackBlueprintFlow", rollbackBlueprintFlowDetail);
		}
		if(startBlueprintFlow != null){
			Map<String, Object> startBlueprintFlowDetail = bluePrintTypeDao.getBlueprintTypeById(startBlueprintFlow);
			result.put("startAppBlueprintFlow", startBlueprintFlowDetail);
		}
		if(stopBlueprintFlow != null){
			Map<String, Object> stopBlueprintFlowDetail = bluePrintTypeDao.getBlueprintTypeById(stopBlueprintFlow);
			result.put("stopAppBlueprintFlow", stopBlueprintFlowDetail);
		}
		return result;
	}
	
	@Override
	public void updateAudit(JSONObject params) {
		resourceDao.updateAudit(params);
	}

	@Override
	public String exportPatches(String ids) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String now = format.format(new Date());
		String absPath = "ex_resTmp" + File.separator + now;
		File parentFolder = new File(absPath);
		parentFolder.mkdirs();
		List<String> paramList = Arrays.asList(ids.split(","));
		List<Map<String,Object>> list =resourceDao.getVersionDetailByVersionIdS(paramList);
		String resourceId = (String)list.get(0).get("RESOURCE_ID");
		Map<String, Object> resource=resourceDao.getResourceDetailById(resourceId);
		String resourceName=(String)resource.get("RESOURCE_NAME");
		List<File> classFile = new ArrayList<>();
		List<File> otherFile = new ArrayList<>();
		for (Map<String, Object> version : list) {// 遍历组件
			Boolean classPackage=false;
			String versionName = (String)version.get("VERSION_NAME");
			if(versionName.indexOf("app") > -1 || versionName.indexOf("App") > -1 || versionName.indexOf("APP") > -1||
					versionName.indexOf("class") > -1 || versionName.indexOf("Class") > -1 || versionName.indexOf("CLASS") > -1){
				classPackage=true;
			}
			String resourcePath = (String)version.get("RESOURCE_PATH");
			resourcePath=resourcePath.substring(0, resourcePath.lastIndexOf("/"));
			String packageId=resourcePath.substring(resourcePath.lastIndexOf("/")+1);
			File packageFolder = new File(parentFolder.getAbsolutePath() + File.separator + resourceName);
			packageFolder.mkdirs();
			String filePath =packageService.downloadFile(packageId, packageFolder.getAbsolutePath());
			if(classPackage){
				classFile.add(new File(filePath));
			}else{
				otherFile.add(new File(filePath));
			}
		}
		// 3 打包zip
		String zipPath = parentFolder.getParent() + File.separator + "resourceVersion" + now + ".zip";
		Map result = mergeZipEntry(classFile,otherFile, new File(zipPath),parentFolder.getAbsolutePath());
		//清理文件
		FileUtil.deleteAllFilesOfDir(parentFolder);
		if((Boolean)result.get("result")){
			return MessageHelper.wrap("result", true, "message", zipPath);
		}else{
			return MessageHelper.wrap("result", true, "message", result.get("message"));
		}
	}
	public static Map mergeZipEntry(List<File> files,List<File> otherFiles,File outFile,String unCompressPath){
		Map<String,Object> result = new HashMap<>();
		BufferedOutputStream bos=null;
		ArchiveOutputStream aos = null;
		BufferedOutputStream bosApp=null;
		ArchiveOutputStream aosApp = null;
		String appName ="app.zip";
		String rootPath = unCompressPath;
		File outAppFile = new File(rootPath+File.separator+appName);
		Map<String,String> tmp = new HashMap<>();
		if(!outFile.getParentFile().exists()) {
			outFile.getParentFile().mkdirs();
		}
		try {
			bos = new BufferedOutputStream(new FileOutputStream(outFile));
			aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, bos);
			bosApp = new BufferedOutputStream(new FileOutputStream(outAppFile));
			aosApp = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.ZIP, bosApp);
			if(files.size()>0){
				for(File file:files) {
					BufferedInputStream bis = null;
					ArchiveInputStream ais = null;
					try {
						bis = new BufferedInputStream(new FileInputStream(file));
						ais = new ArchiveStreamFactory().
								createArchiveInputStream(ArchiveStreamFactory.ZIP, bis, "GBK");
						ZipArchiveEntry entry = null;
						String entryName="";
						while((entry=(ZipArchiveEntry) ais.getNextEntry())!=null){
							entryName = entry.getName();
							if(!entry.isDirectory()){
								OutputStream fileOut = FileUtils.openOutputStream(new File(
										rootPath, entryName));
								IOUtils.copy(ais, fileOut);
								fileOut.close();
								tmp.put(rootPath+File.separator+entryName, entryName);
							}
						}
					}finally{
						if(ais!=null)
							ais.close();
						if(bis!=null)
							bis.close();
					}
				}
			}
			//压缩应用补丁
			if(tmp.size()>0){
				for(Map.Entry<String, String> entry:tmp.entrySet()) {
					String key=entry.getKey();
					String value = entry.getValue();
					aosApp.putArchiveEntry(new ZipArchiveEntry(value));
					FileInputStream input =new FileInputStream(key);
					IOUtils.copy(input, aosApp);
					aosApp.closeArchiveEntry();
					input.close();
				}
				tmp.clear();
				tmp.put(rootPath+File.separator+appName, appName);
				aosApp.close();
			    bosApp.close();
			}
			if(otherFiles.size()>0){
				for(File file:otherFiles){
					String entryName=file.getName();
					OutputStream fileOut = FileUtils.openOutputStream(new File(
							rootPath, entryName));
					FileInputStream input =new FileInputStream(file);
					IOUtils.copy(input, fileOut);
					fileOut.close();
					input.close();
					tmp.put(rootPath+File.separator+entryName, entryName);
				}
			}
			//压缩文件
			if(tmp.size()>0) {
				for(Map.Entry<String, String> entry:tmp.entrySet()) {
					String key=entry.getKey();
					String value = entry.getValue();
					aos.putArchiveEntry(new ZipArchiveEntry(value));
					FileInputStream input =new FileInputStream(key);
					IOUtils.copy(input, aos);
					aos.closeArchiveEntry();
					input.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			result.put("result", false);
			result.put("message", e.getMessage());
			return result;
		}finally{
			try {
				if(aosApp!=null)
					aosApp.close();
				if(bosApp!=null)
					bosApp.close();
				if(aos!=null)
					aos.close();
				if(bos!=null)
					bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		result.put("result", true);
		return result;
	}
	
	@Override
	public List<String> listResourceVersionStatuses(String resourceId) {
		List<String> list = resourceDao.listResourceVersionStatuses(resourceId);
		return list;
	}

	@Override
	public String getAuditStatusById(String auditId) {
		String status = resourceDao.getAuditStatusById(auditId);
		return status;
	}
	
	@Override
	public String getResourceStatus(String resourceId) {
		String status = resourceDao.getResourceStatus(resourceId);
		return status;
	}
	
	@Override
	public String executeNewComponentVersionFlow(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<>();
		String userId = "" + params.get("userId");
		String versionId = "" + params.get("componentVersionId");
		int type = (Integer)params.get("componentExecuteType");
		//超时时间
		Long timeout = itsmTimeout;
		if (itsmTimeout <= 0) {
			// 不设置超时时间，默认30分钟
			timeout = 1805000L;
		} else {
			timeout= itsmTimeout + 5000L;
		}
		//itsm审计
		JSONObject auditBegin = new JSONObject();
		String auditId = UUID.randomUUID().toString();
		auditBegin.put("id", auditId);
		auditBegin.put("userId", params.get("userId"));
		auditBegin.put("resourceVersionId", versionId);
		auditBegin.put("opType", type == 0 ? Constants.audit4Resource.OP_PUBLIC : Constants.audit4Resource.OP_BACK);
		auditBegin.put("status", type == 0 ? Constants.audit4Resource.STATUS_PUBLICED : Constants.audit4Resource.STATUS_BACKED);
		auditBegin.put("description", type == 0 ? Constants.audit4Resource.OP_PUBLIC : Constants.audit4Resource.OP_BACK);
		saveAudit(auditBegin);
		Map<String, Object> versionDetail = resourceDao.getVersionDetailByVersionId(versionId);
		//全局审计
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				auditService.save(new AuditEntity(userId, ResourceCode.COMPONENTITSM, "" + versionDetail.get("VERSION_NAME"), type == 0 ? ResourceCode.Operation.ITSMRELEASE : ResourceCode.Operation.ITSMROLLBACK, 1, "版本[" + versionDetail.get("VERSION_NAME") + "]" + (type == 0 ? "发布" : "回退")));
			}
		});
		//锁
		Object obj = new Object();
		Map<String, Object> param = new HashMap<>();
		param.put("versionId", versionId);
		try {
			execute4Wakeup(false, versionId, 3, userId, timeout, obj);
			execute4Wakeup(false, versionId, type, userId, timeout, obj);
			execute4Wakeup(false, versionId, 2, userId, timeout, obj);
			param.put("status", type == 0 ? Constants.audit4Resource.STATUS_PUBLICED_SUCCESSFUL : Constants.audit4Resource.STATUS_BACKED_SUCCESSFUL);
			resourceDao.updateResourceVersion(param);
			result.put("result", true);
			result.put("message", "版本[" + versionDetail.get("VERSION_NAME") + "]" +(type == 0 ? "发布" : "回退") + "成功！");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("result", false);
			result.put("message", e.getMessage());
		}
		String statusEnd = (type == 0 ? ((Boolean)result.get("result") ? Constants.audit4Resource.STATUS_PUBLICED_SUCCESSFUL : Constants.audit4Resource.STATUS_PUBLICED_FAILED) : ((Boolean)result.get("result") ? Constants.audit4Resource.STATUS_BACKED_SUCCESSFUL : Constants.audit4Resource.STATUS_BACKED_FAILED));
		JSONObject auditEnd = new JSONObject();
		auditEnd.put("id", auditId);
		auditEnd.put("status", statusEnd);
		updateAudit(auditEnd);
		return JSON.toJSONString(result);
	}
	
	private void execute4Wakeup(Boolean isBatch, String versionId, int type, String userId, Long timeout, Object obj) throws RuntimeException{
		String flowOp = "";
		String flowStatus = "";
		Map<String, Object> resourceDetail = new HashMap<>();
		Map<String, Object> versionDetail = new HashMap<>();
		if(type == 0 || type == 1) {
			versionDetail = resourceDao.getVersionDetailByVersionId(versionId);
			resourceDetail = resourceDao.getResourceDetailByVersionId(versionId);
		}
		else {
			if(isBatch) {
				resourceDetail = resourceDao.getResourceDetailById(versionId);
			}
			else {
				resourceDetail = resourceDao.getResourceDetailByVersionId(versionId);
				versionDetail = resourceDao.getVersionDetailByVersionId(versionId);
			}
		}
		if(type == 0) {
			flowOp = "发布";
			if(isBatch) {
				flowStatus = Constants.audit4Resource.STATUS_PUBLICED_SUCCESSFUL;
			}
			else {
				flowStatus = Constants.audit4Resource.STATUS_PUBLICED_FINISH;
			}
		}
		else if(type == 1) {
			flowOp = "回退";
			if(isBatch) {
				flowStatus = Constants.audit4Resource.STATUS_BACKED_SUCCESSFUL;
			}
			else {
				flowStatus = Constants.audit4Resource.STATUS_BACKED_FINISH;
			}
		}
		else if(type == 2) {
			flowOp = "启动";
			flowStatus = Constants.audit4Resource.STATUS_START_SUCCESSFUL;
		}
		else if(type == 3) {
			flowOp = "停止";
			flowStatus = Constants.audit4Resource.STATUS_STOP_SUCCESSFUL;
		}
		else {
		}
		try {
			executeItsmFlow(isBatch, versionId, type, userId, obj);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException((isBatch ? ("组件[" + resourceDetail.get("RESOURCE_NAME") + "]批量发布中" + (type == 0 || type == 1 ? "版本[" + versionDetail.get("VERSION_NAME") + "]" : "") + "[") : ("版本[" + versionDetail.get("VERSION_NAME") + "][")) + flowOp + "]流程执行异常：" + e.getMessage());
		}
		synchronized(obj) {
			try {
				obj.wait(timeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
				throw new RuntimeException((isBatch ? ("组件[" + resourceDetail.get("RESOURCE_NAME") + "]批量发布中" + (type == 0 || type == 1 ? "版本[" + versionDetail.get("VERSION_NAME") + "]" : "") + "[") : ("版本[" + versionDetail.get("VERSION_NAME") + "][")) + flowOp + "]流程执行异常：" + e.getMessage());
			}
		}
		String status = "";
		if(type == 0 || type == 1) {
			status = resourceDao.getResourceVersionStatus(versionId);
		}
		else {
			if(isBatch) {
				status = getResourceStatus(versionId);
			}
			else {
				status = resourceDao.getResourceVersionStatus(versionId);
			}
		}
		if (status != null) {
			//流程未结束，进行中(超时)/流程已结束，失败
			if(!flowStatus.equals(status)){
				throw new RuntimeException((isBatch ? ("组件[" + resourceDetail.get("RESOURCE_NAME") + "]批量发布中" + (type == 0 || type == 1 ? "版本[" + versionDetail.get("VERSION_NAME") + "]" : "") + "[") : ("版本[" + versionDetail.get("VERSION_NAME") + "][")) + flowOp + "]流程执行失败或超时！");
			}
		}
	}

	public void executeItsmFlow(Boolean isBatch, String versionId, int type, String userId, Object obj) throws Exception{
		String opType = null;
		String opStatusing = null;
		String opDescription = null;
		switch(type){
		case 0:
			opType = Constants.audit4Resource.OP_PUBLIC;
			opStatusing = Constants.audit4Resource.STATUS_PUBLICED;
			opDescription = Constants.audit4Resource.OP_PUBLIC;
			break;
		case 1:
			opType = Constants.audit4Resource.OP_BACK;
			opStatusing = Constants.audit4Resource.STATUS_BACKED;
			opDescription = Constants.audit4Resource.OP_BACK;
			break;
		case 2:
			opType = Constants.audit4Resource.OP_START;
			opStatusing = Constants.audit4Resource.STATUS_START;
			opDescription = Constants.audit4Resource.OP_START;
			break;
		case 3:
			opType = Constants.audit4Resource.OP_STOP;
			opStatusing = Constants.audit4Resource.STATUS_STOP;
			opDescription = Constants.audit4Resource.OP_STOP;
			break;
		default:
			break;
		}
		//新增itsm组件版本审计为ing
		JSONObject audit = new JSONObject();
		String auditId = UUID.randomUUID().toString();
		audit.put("id", auditId);
		audit.put("userId", userId);
		audit.put("resourceVersionId", versionId);
		audit.put("opType", opType);
		audit.put("status", opStatusing);
		audit.put("description", opDescription);
		saveAudit(audit);
		//更新itsm组件版本状态为ing
		Map<String, Object> param = new HashMap<>();
		param.put("status", opStatusing);
		param.put("versionId", versionId);
		if(type == 0 || type == 1) {
			resourceDao.updateResourceVersion(param);
		}
		else if(type == 2 || type == 3) {
			if(!isBatch) {
				resourceDao.updateResourceVersion(param);
			}
			else {
				param.put("resourceId", versionId);
				resourceDao.updateResourceStatus(param);
			}
		}
		else {
		}
		//组件版本类型app、sql
		boolean isApp = false;
		boolean isSql = false;
		Map<String, Object> resourceDetail = null;
		//发布、回退
		if(type == 0 || type == 1){
			Map<String, Object> versionDetail = resourceDao.getVersionDetailByVersionId(versionId);
			String versionName = "" + versionDetail.get("VERSION_NAME");
			if(versionName.indexOf("app") != -1 || versionName.indexOf("App") != -1 || versionName.indexOf("APP") != -1 || versionName.indexOf("class") != -1 || versionName.indexOf("Class") != -1 || versionName.indexOf("CLASS") != -1){
				isApp = true;
			}
			if(versionName.indexOf("sql") != -1 || versionName.indexOf("Sql") != -1 || versionName.indexOf("SQL") != -1 || versionName.indexOf("db") != -1 || versionName.indexOf("Db") != -1 || versionName.indexOf("DB") != -1){
				isSql = true;
			}
			resourceDetail = resourceDao.getResourceDetailByVersionId(versionId);
		}
		//启动、停止
		else if(type == 2 || type == 3){
			isApp = true;
			if(isBatch) {
				resourceDetail = resourceDao.getResourceDetailById(versionId);
			}
			else {
				resourceDetail = resourceDao.getResourceDetailByVersionId(versionId);
			}
		}
		else{
		}
		int blueprintId = -1;
		String cdFlowId = null;
		if (type == 0) {
			blueprintId = (int) resourceDetail.get("RELEASE_BLUEPRINT_ID");
			cdFlowId = "" + resourceDetail.get("RELEASE_BLUEPRINT_FLOW");
		} else if (type == 1) {
			blueprintId = (int) resourceDetail.get("ROLLBACK_BLUEPRINT_ID");
			cdFlowId = "" + resourceDetail.get("ROLLBACK_BLUEPRINT_FLOW");
		} else if (type == 2) {
			blueprintId = (int) resourceDetail.get("START_BLUEPRINT_ID");
			cdFlowId = "" + resourceDetail.get("START_BLUEPRINT_FLOW");
		} else if (type == 3) {
			blueprintId = (int) resourceDetail.get("STOP_BLUEPRINT_ID");
			cdFlowId = "" + resourceDetail.get("STOP_BLUEPRINT_FLOW");
		} else{
		}
		if (blueprintId == -1 || cdFlowId == null) {
			String errorMessage = null;
			if (type == 0) {
				errorMessage = "组件未绑定发布流程！";
			} else if (type == 1) {
				errorMessage = "组件未绑定回退流程！";
			} else if (type == 2) {
				errorMessage = "组件未绑定启动流程！";
			} else if (type == 3) {
				errorMessage = "组件未绑定停止流程！";
			} else {
			}
			throw new RuntimeException(errorMessage);
		}
		Map<String, Object> blueprintDetail = blueprintService.getBlueprintInstanceById(blueprintId);
		String blueprintInstanceId = "" + blueprintDetail.get("INSTANCE_ID");
		// 更新蓝图组件实例配置
		List<Application> apps = blueprintService.getBlueprintComponents(blueprintInstanceId);
		for (Application app : apps) {
			String appResourceId = app.getResourceId();
			String appComponentName = app.getComponentName();
			String resourceId = "" + resourceDetail.get("ID");
			String resourceName = "" + resourceDetail.get("RESOURCE_NAME");
			if (appResourceId.equals(resourceId) && appComponentName.equals(resourceName)) {
				//组件子流程是否跳过
				int executeFlag = 0;
				String displayName = app.getNodeDisplay();
				if(isSql){
					if(displayName.indexOf("sql") != -1 || displayName.indexOf("Sql") != -1 || displayName.indexOf("SQL") != -1 || displayName.indexOf("db") != -1 || displayName.indexOf("Db") != -1 || displayName.indexOf("DB") != -1){
						executeFlag = 1;
					}
				}
				if(isApp){
					if(displayName.indexOf("app") != -1 || displayName.indexOf("App") != -1 || displayName.indexOf("APP") != -1 || displayName.indexOf("class") != -1 || displayName.indexOf("Class") != -1 || displayName.indexOf("CLASS") != -1){
						executeFlag = 1;
					}
				}
				Map<String, Object> map = new HashMap<>();
				long appId = app.getId();
				map.put("appId", appId);
				map.put("executeFlag", executeFlag);
				if(executeFlag == 1){
					//发布、回退
					if(type == 0 || type ==1){
						map.put("versionId", versionId);
					}
					//启动、停止
					else if(type == 2 || type == 3){
						if(isBatch) {
							List<Version> versions = resourceDao.listResourceVersion(versionId);
							map.put("versionId", versions.get(versions.size() - 1).getId());
						}
						else {
							map.put("versionId", versionId);
						}
					}
					else{
					}
				}
				applicationDao.updateAppVersion(map);
			}
			else{
				Map<String, Object> map = new HashMap<>();
				long appId = app.getId();
				map.put("appId", appId);
				map.put("executeFlag", 0);
				applicationDao.updateAppVersion(map);
			}
		}
		String result = null;
		try {
			// 执行流程
			Map<String, String> flowParam = new HashMap<>();
			flowParam.put("_userName", userId);
			result = blueprintService.executeBlueprintFlow(cdFlowId, blueprintInstanceId, flowParam);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("执行流程异常：" + e.getMessage());
		}
		Map<String, Object> resultMap = JSON.parseObject(result, new TypeReference<Map<String, Object>>() {
		});
		
		//itsm审计
		ThreadPool.service.execute(new Runnable(){
			@Override
			public void run(){
				int operateResult = ((boolean)resultMap.get("result")) ? 1 : 0;
				Map<String, Object> resource = null;
				String operation = null;
				String operationMessage = null;
				String versionName = null;
				if(type == 0 || type == 1){
					if(type == 0){
						operation = ResourceCode.Operation.ITSMRELEASE;
						operationMessage = "发布";
					}
					else if(type == 1){
						operation = ResourceCode.Operation.ITSMROLLBACK;
						operationMessage = "回退";
					}
					Map<String, Object> versionDetail = resourceDao.getVersionDetailByVersionId(versionId);
					versionName = "" + versionDetail.get("VERSION_NAME");
					resource = resourceDao.getResourceDetailByVersionId(versionId);
				}
				//启动、停止
				else if(type == 2 || type == 3){
					if(type == 2){
						operation = ResourceCode.Operation.ITSMSTART;
						operationMessage = "启动";
					}
					else if(type == 3){
						operation = ResourceCode.Operation.ITSMSTOP;
						operationMessage = "停止";
					}
					if(isBatch) {
						resource = resourceDao.getResourceDetailById(versionId);
					}
					else {
						Map<String, Object> versionDetail = resourceDao.getVersionDetailByVersionId(versionId);
						versionName = "" + versionDetail.get("VERSION_NAME");
						resource = resourceDao.getResourceDetailByVersionId(versionId);
					}
				}
				auditService.save(new AuditEntity(userId, ResourceCode.COMPONENTITSM, ((versionName == null) ? "" + resource.get("RESOURCE_NAME") : versionName), operation, operateResult, "ITSM组件[" + resource.get("RESOURCE_NAME") + "]" + ((versionName == null) ? "" : ("版本[" + versionName + "]"))  + operationMessage));
			}
		});
		
		//检查流程执行情况
		ITSMBatchReleasePool.getInstance().execute(new Runnable(){
			@Override
			public void run() {
				boolean successful = false;
				String status = null;
				String statusSuccess = null;
				String statusFailed = null;
				switch(type){
				case 0:
					if(isBatch) {
						statusSuccess = Constants.audit4Resource.STATUS_PUBLICED_SUCCESSFUL;
					}
					else {
						statusSuccess = Constants.audit4Resource.STATUS_PUBLICED_FINISH;
					}
					statusFailed = Constants.audit4Resource.STATUS_PUBLICED_FAILED;
					break;
				case 1:
					if(isBatch) {
						statusSuccess = Constants.audit4Resource.STATUS_BACKED_SUCCESSFUL;
					}
					else {
						statusSuccess = Constants.audit4Resource.STATUS_BACKED_FINISH;
					}
					statusFailed = Constants.audit4Resource.STATUS_BACKED_FAILED;
					break;
				case 2:
					statusSuccess = Constants.audit4Resource.STATUS_START_SUCCESSFUL;
					statusFailed = Constants.audit4Resource.STATUS_START_FAILED;
					break;
				case 3:
					statusSuccess = Constants.audit4Resource.STATUS_STOP_SUCCESSFUL;
					statusFailed = Constants.audit4Resource.STATUS_STOP_FAILED;
					break;
				default:
					break;
				}
				boolean executeState = (boolean) resultMap.get("result");
				if (executeState) {
					String instanceId = (String)resultMap.get("id");
					Map<String, Object> singleInstanceMap = new HashMap<>();
					singleInstanceMap.put("instanceId", instanceId);
					List<Map> instanceList = new ArrayList<>();
					instanceList.add(singleInstanceMap);
					RestTemplate restUtil = RequestClient.getInstance().getRestTemplate();
					MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
					requestEntity.add("instancesList",
							JSON.toJSONString(instanceList, SerializerFeature.WriteDateUseDateFormat,
									SerializerFeature.DisableCircularReferenceDetect));
					long times = -1L;
					if (itsmTimeout <= 0) {
						// 不设置超时时间，默认刷360次(30分钟 )
						times = 360L;
					} else {
						times = itsmTimeout / 5000L;
						if (itsmTimeout % 5000L != 0) {
							times++;
						}
					}
					while (times > 0) {
						try {
							// 5秒查一次
							Thread.sleep(5000L);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						String instanceResult = restUtil.postForObject(
								flowServerUrl + "/WFService/getInstancesStatus.wf", requestEntity, String.class);
						Map<String, Object> instanceResultMap = JSON.parseObject(instanceResult);
						if ((Boolean) instanceResultMap.get("state")) {
							List<Map> instanceResultData = JSON
									.parseArray(instanceResultMap.get("data").toString(), Map.class);
							String instanceFlowState = (String)instanceResultData.get(0).get("flowState");
							Object isOverTime = (String)instanceResultData.get(0).get("isOverTime");
							String instanceState = String.valueOf(instanceResultData.get(0).get("state"));
							//流程已结束
							if(Constants.Monitor.FINISHED.equals(instanceFlowState)){
								break;
							}
							//流程未结束
							else{
								//挂起超时
								if(Constants.Monitor.OverTime.equals(isOverTime)){
									times = 0;
									break;
								}
								//状态已结束
								if ("2".equals(instanceState)) {
									break;
								}
								//状态正在执行0/失败7/...
								else{
									times--;
								}
							}
						}
						else{
							times--;
						}
					}
					if (times == 0) {
						// 超时时间到了还没执行结束/流程超时
						status = statusFailed;
						successful = false;
					}
					else{
						status = statusSuccess;
						successful = true;
					}
				} else {
					status = statusFailed;
					successful = false;
				}
				//更新itsm组件版本最终状态
				Map<String, Object> param = new HashMap<>();
				param.put("status", status);
				param.put("versionId", versionId);
				if(type == 0 || type == 1) {
					resourceDao.updateResourceVersion(param);
				}
				else if(type == 2 || type == 3){
					if(!isBatch) {
						resourceDao.updateResourceVersion(param);
					}
					else {
						param.put("resourceId", versionId);
						resourceDao.updateResourceStatus(param);
					}
				}
				else {
				}
				//更新itsm组件版本最终审计
				JSONObject audit = new JSONObject();
				audit.put("id", auditId);
				audit.put("status", status);
				updateAudit(audit);
				if(obj != null){
					synchronized(obj){
						obj.notifyAll();
					}
				}
			}});
	}
	
	@Override
	public String releaseNewComponentVersionsByBatch(Map<String, Object> params){
		String componentVersionIds = "" + params.get("componentVersionIds");
		int type = (int) params.get("componentExecuteType");
		String userId = "" + params.get("userId");
		boolean batchReleaseSuccessful = true;
		Map<String, Object> result = new HashMap<>();
		//批量发布
		if (type == 0) {
			//超时时间
			Long timeout = itsmTimeout;
			if (itsmTimeout <= 0) {
				// 不设置超时时间，默认30分钟
				timeout = 1805000L;
			} else {
				timeout= itsmTimeout + 5000L;
			}
			List<String> versions = sortVersion(componentVersionIds);
			Map<String, Object> versionDetail = resourceDao.getVersionDetailByVersionId("" + versions.get(0));
			String resourceId = "" + versionDetail.get("RESOURCE_ID");
			Map<String, Object> resourceDetail = resourceDao.getResourceDetailById(resourceId);
			//批量发布itsm审计
			JSONObject auditBegin = new JSONObject();
			String auditId = UUID.randomUUID().toString();
			auditBegin.put("id", auditId);
			auditBegin.put("userId", params.get("userId"));
			auditBegin.put("resourceVersionId", resourceId);
			auditBegin.put("opType", Constants.audit4Resource.OP_BATCH_RELEASE);
			auditBegin.put("status", Constants.audit4Resource.STATUS_BATCH_RELEASE);
			auditBegin.put("description", Constants.audit4Resource.OP_BATCH_RELEASE);
			saveAudit(auditBegin);
			//全局审计
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity(userId, ResourceCode.COMPONENTITSM, "" + resourceDetail.get("RESOURCE_NAME"), ResourceCode.Operation.ITSMBATCHRELEASE, 1, "组件[" + resourceDetail.get("RESOURCE_NAME") + "]批量发布"));
				}
			});
			Object obj = new Object();
			try {
				//执行停止
				execute4Wakeup(true, resourceId, 3, userId, timeout, obj);
				//批量发布itsm组件状态
				Map<String, Object> param = new HashMap<>();
				param.put("status", Constants.audit4Resource.STATUS_BATCH_RELEASE);
				param.put("resourceId", resourceId);
				resourceDao.updateResourceStatus(param);
				try {
					for (int i = 0; i < versions.size(); i++) {
						//批量执行
						execute4Wakeup(true, versions.get(i), type, userId, timeout, obj);
					} 
				} catch (Exception e) {
					e.printStackTrace();
					param.put("status", Constants.audit4Resource.STATUS_BATCH_RELEASE_FAILED);
					resourceDao.updateResourceStatus(param);
					throw e;
				}
				//批量完成
				param.put("status", Constants.audit4Resource.STATUS_BATCH_RELEASE_FINISH);
				resourceDao.updateResourceStatus(param);
				//执行启动
				execute4Wakeup(true, resourceId, 2, userId, timeout, obj);
				//更新批量发布全局状态
				param.put("status", Constants.audit4Resource.STATUS_BATCH_RELEASE_SUCCESSFUL);
				resourceDao.updateResourceStatus(param);
			} catch (Exception e) {
				e.printStackTrace();
				batchReleaseSuccessful = false;
			}
			//更新批量执行状态
			String statusEnd = batchReleaseSuccessful ? Constants.audit4Resource.STATUS_BATCH_RELEASE_SUCCESSFUL : Constants.audit4Resource.STATUS_BATCH_RELEASE_FAILED;
			JSONObject auditEnd = new JSONObject();
			auditEnd.put("id", auditId);
			auditEnd.put("status", statusEnd);
			updateAudit(auditEnd);
			result.put("result", batchReleaseSuccessful);
			result.put("message", "组件[" + resourceDetail.get("RESOURCE_NAME") + "]批量发布" + (batchReleaseSuccessful ? "成功！" : "失败！"));
		} else {
		}
		return JSON.toJSONString(result);
	}

	@Override
	public String getLatestVersionId(String resourceId) {
		// TODO Auto-generated method stub
		Map version = resourceDao.getLatestVersionById(resourceId);
		if(version==null){
			return "";
		}
		return (String)version.get("ID");
	}

	@Transactional
	@Override
	public void saveResourceVersionByRestApi(String resourceId, String versionName, String versionDesc, String fileName,
			int type) {
		try {
			Map<String, Object> resourceDetail = resourceDao.getResourceDetailById(resourceId);
			if(resourceDetail == null) {
				throw new RuntimeException("id[" + resourceId + "]的组件不存在");
			}
			String resourceName = "" + resourceDetail.get("RESOURCE_NAME");
			List<Version> versions = listResourceVersion(resourceId);
			for (Version version : versions) {
				if (versionName.equals(version.getVersionName())) {
					throw new RuntimeException("组件版本名[" + versionName + "]已存在，请重新命名！");
				}
			}
			if(!(fileName.endsWith(".zip") || fileName.endsWith(".jar") || fileName.endsWith(".war")
					|| fileName.endsWith(".tar") || fileName.endsWith(".tar.gz"))) {
				throw new RuntimeException("工件包[" + fileName + "]不满足后缀是zip/jar/war/tar/tar.gz，请重新选择！");
			}
			
			Map<String, Object> packageDetail = packageService.findPackageByName(fileName);
			String resourcePath = "" + packageDetail.get("RESOURCE_PATH");
			String pId = "" + packageDetail.get("ID");
			String resourceParent = new File("packages/" + pId).getAbsolutePath();
			File resourceFile = new File("packages/" + pId + "/" + fileName);
			if(!resourceFile.exists()) {
				Utils.downloadFile(url, port, user, pwd, "packages/" + pId, fileName, resourceParent);
			}
			
			//从压缩文件中读取配置
			String input = "";
			String output = "";
			Properties pro = new Properties();
			Map<String, String> templates = new HashMap<>();
			Map<String, String> ftls = new HashMap<>();
			FileInputStream fis = new FileInputStream(resourceFile);
			BufferedInputStream bis = null;
			ArchiveInputStream ais = null;
			ArchiveEntry entry = null;
			if(fileName.endsWith("tar.gz")) {
				bis = new BufferedInputStream(new GzipCompressorInputStream(fis));
			}
			else {
				bis = new BufferedInputStream(fis);
			}
			if(fileName.endsWith("tar.gz") || fileName.endsWith("tar")) {
				ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.TAR, bis,"UTF-8");
			}
			else {
				ais = new ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.JAR, bis,"UTF-8");
			}
			while ((entry = ais.getNextEntry()) != null) {
				String entryName = entry.getName();
				if(entryName.endsWith("input.properties")){
					pro.load(ais);
					input = JSON.toJSONString(pro);
					pro.clear();
				}
				else if(entryName.endsWith("output.properties")) {
					pro.load(ais);
					output = JSON.toJSONString(pro);
					pro.clear();
				}
				else if(entryName.endsWith("templates.properties")) {
					pro.load(ais);
					Enumeration<Object> keys = pro.keys();
					while(keys.hasMoreElements()) {
						String key = (String)keys.nextElement();
						String value = pro.getProperty(key);
						templates.put(key, value);
					}
					pro.clear();
				}
				else if(entryName.endsWith(".ftl")) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					IOUtils.copy(ais, bos);
					String content = new String(bos.toByteArray(), "UTF-8");
					ftls.put(entryName, content);
					bos.flush();
					bos.close();
				}
				else {
					continue;
				}
			}
			if(fis != null) {
				fis.close();
			}
			if(bis != null) {
				bis.close();
			}
			if(ais != null) {
				ais.close();
			}
			if(resourcePath.indexOf("ENC(") == -1){
				resourcePath = buildResourcePath(packageDetail);
			}
			String versionId = UUID.randomUUID().toString();
			input = SensitiveDataUtil.getEncryptConfig(input);
			output = SensitiveDataUtil.getEncryptConfig(output);
			Version v = new Version(versionId, resourceId, resourcePath, versionName, versionDesc,
					new Date(), 60000, 60000, 60000, 60000, 3, "", type, input, output);
			saveResourceVersion(v);
			//==============添加组件版本审计start===================
			ThreadPool.service.execute(new Runnable(){
				@Override
				public void run(){
					auditService.save(new AuditEntity("restApiUser", ResourceCode.COMPONENT, resourceName, ResourceCode.Operation.ADD, 1, "创建组件版本:" + versionName));
				}
			});
			//==============添加组件版本审计end=====================
			
			JSONObject params = new JSONObject();
			params.put("id", UUID.randomUUID().toString());
			params.put("userId", "restApiUser");
			params.put("resourceVersionId", versionId);
			params.put("opType", Constants.audit4Resource.OP_NEW);
			params.put("status", Constants.audit4Resource.STATUS_NEW);
			params.put("description", "新建");
			saveAudit(params);
			
			//保存映射文件
			Set<String> templateKeys = templates.keySet();
			for(String templateKey : templateKeys) {
				if(ftls.containsKey(templateKey.substring(2, templateKey.length()))) {
					String id = UUID.randomUUID().toString();
					String templateValue = templates.get(templateKey);
					JSONObject json = new JSONObject();
					json.put(templateKey, templateValue);
					String ftlName = templateValue.substring(templateValue.lastIndexOf("/") + 1, templateValue.length());
					String ftlText = ftls.get(templateKey.substring(2, templateKey.length()));
					VersionFtl versionFtl = new VersionFtl(id, versionId, JSON.toJSONString(json), ftlName, ftlText);
					saveVersionFtl(versionFtl);
					//==============添加模板映射审计start===================
					ThreadPool.service.execute(new Runnable(){
						@Override
						public void run(){
							auditService.save(new AuditEntity("restApiUser", ResourceCode.COMPONENT, resourceName, ResourceCode.Operation.ADD, 1, "版本:" + versionName + " 模版文件保存:" + ftlText));
						}
					});
					//==============添加模板映射审计end=====================
				}
			}
		} catch (Exception e) {
			//e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private String buildResourcePath(Map<String, Object> packageDetail) {
		String password = null;
		try {
			password = "ENC(" + SensitiveDataUtil.encryptDesText(pwd) + ")";
		} catch (Exception e) {
			System.out.println("加密失败[" + e.getMessage() + "]");
			e.printStackTrace();
			password = pwd;
		}
		String id = "" + packageDetail.get("ID");
		String resourceName = "" + packageDetail.get("RESOURCE_NAME");
		String resourcePath = "ftp://" + user + ":" + password + "@" + url + File.separator + "packages"
									+ File.separator + id + File.separator + resourceName;
		return resourcePath;
	}

	@Override
	public List<Resource> listAllSystems(Map<String, Object> param) {
		return resourceDao.listAllSystems(param);
	}
	
}
