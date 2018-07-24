package com.dc.appengine.appmaster.ws.server;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dc.appengine.appmaster.service.impl.ITSMPatchService;
import com.dcits.Common.entity.User;

@RestController
@RequestMapping("ws/ITSMPatchStatistics")
public class ITSMPatchStatisticsRestService {
	
	private static final Logger log = LoggerFactory.getLogger(ITSMPatchStatisticsRestService.class);
	private static final Map<String, String> statusDict = new HashMap<>();
	private static final Map<String, Integer> width = new HashMap<>();
	
	static{
		statusDict.put("00", "新建");
		statusDict.put("01", "测试通过");
		statusDict.put("10", "部分测试通过");
		statusDict.put("11", "测试未通过");
//		statusDict.put("20", "不允许发布");
//		statusDict.put("21", "允许发布");
		statusDict.put("30", "发布中...");
		statusDict.put("31", "发布成功");
		statusDict.put("32", "发布失败");
		statusDict.put("40", "回退中...");
		statusDict.put("41", "回退");
		statusDict.put("42", "回退失败");
		width.put("opType", "追加描述".getBytes().length * 256);
		width.put("status", "部分测试通过".getBytes().length * 256);
		width.put("description", "中".getBytes().length * 10 * 256);
	}
	
	@Resource
	private ITSMPatchService service;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public Object list(@RequestParam(value = "patchName", defaultValue = "") String patchName,
			@RequestParam(value = "opType", defaultValue = "") String opType,
			@RequestParam(value = "statusType", defaultValue = "") String statusType,
			@RequestParam(value = "beginTime", defaultValue = "") String beginTime,
			@RequestParam(value = "endTime", defaultValue = "") String endTime,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "sortName", defaultValue = "updateTime") String sortName,
			@RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder,
			@RequestParam(value = "componentId", defaultValue = "") String componentId,
			HttpServletRequest request) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			Map<String, Object> map = new HashMap<>();
			map.put("patchName", patchName);
			map.put("opType", opType);
			map.put("statusType", statusType);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("sortName", sortName);
			map.put("sortOrder", sortOrder);
			map.put("userName", user.getName());
			map.put("componentId", componentId);
			return service.getPatchStatistics(map, pageSize, pageNum);
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}
	
	@RequestMapping(value = "listPatch/{componentId}", method = RequestMethod.GET)
	public Object listPatch(HttpServletRequest request, @PathVariable("componentId") String componetId) {
		try {
			User user = (User) request.getSession().getAttribute("user");
			Map<String, Object> param = new HashMap<>();
			param.put("userName", user.getName());
			param.put("componentId", componetId);
			return service.getPatchList(param);
		} catch (Exception e) {
			log.error("", e);
		}
		return null;
	}
	
	@RequestMapping(value = "export", method = RequestMethod.GET)
	public void export(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "ids", defaultValue = "") String ids) {
		response.setHeader("Content-Disposition", "attachment;filename=" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".xls");
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		try {
			writeExcel(service.export(ids.split(",")), response.getOutputStream());
		} catch (IOException e) {
			log.error("", e);
		}
	}
	
	@RequestMapping(value = "exportAll", method = RequestMethod.GET)
	public void exportAll(HttpServletRequest request,
			@RequestParam(value = "patchName", defaultValue = "") String patchName,
			@RequestParam(value = "opType", defaultValue = "") String opType,
			@RequestParam(value = "statusType", defaultValue = "") String statusType,
			@RequestParam(value = "beginTime", defaultValue = "") String beginTime,
			@RequestParam(value = "endTime", defaultValue = "") String endTime,
			@RequestParam(value = "sortName", defaultValue = "updateTime") String sortName,
			@RequestParam(value = "sortOrder", defaultValue = "desc") String sortOrder,
			@RequestParam(value = "componentId", defaultValue = "") String componentId,
			HttpServletResponse response,
			@RequestParam(value = "ids", defaultValue = "") String ids) {
		response.setHeader("Content-Disposition", "attachment;filename=" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".xls");
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		try {
			User user = (User) request.getSession().getAttribute("user");
			Map<String, Object> map = new HashMap<>();
			map.put("patchName", patchName);
			map.put("opType", opType);
			map.put("statusType", statusType);
			map.put("beginTime", beginTime);
			map.put("endTime", endTime);
			map.put("sortName", sortName);
			map.put("sortOrder", sortOrder);
			map.put("userName", user.getName());
			map.put("componentId", componentId);
			writeExcel(service.exportAll(map), response.getOutputStream());
		} catch (IOException e) {
			log.error("", e);
		}
	}
	
	private boolean writeExcel(List<Map<String, Object>> list, OutputStream os) {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("补丁统计");
		try {
			if (list.size() > 0) {
				Map<String, Object> map = list.get(0);
				String[] keys = map.keySet().toArray(new String[0]);
				HSSFRow title = sheet.createRow(0);
				for (int i = 0; i < keys.length; i++) {
					title.createCell(i).setCellValue(keys[i]);
				}
				for (int j = 0; j < list.size(); j++) {
					HSSFRow row = sheet.createRow(j + 1);
					for (int i = 0; i < keys.length; i++) {
						if (keys[i].equals("status")) {
							list.get(j).put("status", statusDict.get(list.get(j).get(keys[i]).toString()));
						}
						row.createCell(i).setCellValue(list.get(j).get(keys[i]).toString());
					}
				}
				for (int i = 0; i < keys.length; i++) {
					sheet.autoSizeColumn(i);
					switch (keys[i]) {
					case "opType":
					case "status":
					case "description":
						if (sheet.getColumnWidth(i) < width.get(keys[i])) {
							sheet.setColumnWidth(i, width.get(keys[i]));
						}
						break;

					default:
						break;
					}
				}
				wb.write(os);
			} else {
				wb.write(os);
			}
			return true;
		} catch (IOException e) {
			log.error("", e);
		} finally {
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					log.error("", e);
				}
			}
		}
		return false;
	}

}
