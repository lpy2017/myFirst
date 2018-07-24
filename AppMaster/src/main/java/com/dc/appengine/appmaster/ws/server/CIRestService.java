package com.dc.appengine.appmaster.ws.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.dc.appengine.appmaster.service.impl.JenkinsService;

@RestController
@RequestMapping("/ws/ci")
public class CIRestService {
	
	@Resource
	private JenkinsService jenkinsService;
	@Value("${jenkins.need:true}")
	private boolean needJenkins;
	
	@RequestMapping(method = RequestMethod.GET, value = "dashboard/count")
	public Object count(@RequestParam("series") String serieArray,
			@RequestParam("start") long start,
			@RequestParam("end") long end,
			@RequestParam("intervalType") String intervalType) {
		List<Map<String, Object>> list = new ArrayList<>();
		boolean flag = true;
		List<String> x = new ArrayList<>();
		List<String> series = JSON.parseObject(serieArray, new TypeReference<List<String>>(){});
		for (String serie : series) {
			Map<String, Object> map = new HashMap<>();
			map.put("name", serie);
			map.put("data", jenkinsService.count(start, end, serie, intervalType));
			list.add(map);
			if (flag) {
				x = jenkinsService.x(start, end, intervalType);
				flag = false;
			}
		}
		Map<String, Object> result = new HashMap<>();
		result.put("x", x);
		result.put("series", list);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "dashboard/successCount")
	public Object successCount(@RequestParam("series") String serieArray,
			@RequestParam("start") long start,
			@RequestParam("end") long end,
			@RequestParam("intervalType") String intervalType) {
		List<Map<String, Object>> list = new ArrayList<>();
		boolean flag = true;
		List<String> x = new ArrayList<>();
		List<String> series = JSON.parseObject(serieArray, new TypeReference<List<String>>(){});
		for (String serie : series) {
			Map<String, Object> map = new HashMap<>();
			map.put("name", serie);
			map.put("data", jenkinsService.successCount(start, end, serie, intervalType));
			list.add(map);
			if (flag) {
				x = jenkinsService.x(start, end, intervalType);
				flag = false;
			}
		}
		Map<String, Object> result = new HashMap<>();
		result.put("x", x);
		result.put("series", list);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "dashboard/averageDuration")
	public Object averageDuration(@RequestParam("series") String serieArray,
			@RequestParam("start") long start,
			@RequestParam("end") long end,
			@RequestParam("intervalType") String intervalType) {
		List<Map<String, Object>> list = new ArrayList<>();
		boolean flag = true;
		List<String> x = new ArrayList<>();
		List<String> series = JSON.parseObject(serieArray, new TypeReference<List<String>>(){});
		for (String serie : series) {
			Map<String, Object> map = new HashMap<>();
			map.put("name", serie);
			map.put("data", jenkinsService.averageDuration(start, end, serie, intervalType));
			list.add(map);
			if (flag) {
				x = jenkinsService.x(start, end, intervalType);
				flag = false;
			}
		}
		Map<String, Object> result = new HashMap<>();
		result.put("x", x);
		result.put("series", list);
		return result;
	}
	
	private List<String> x(long start, long end) {
		List<String> list = new ArrayList<>();
		Calendar calendar = new GregorianCalendar();
		calendar.setTimeInMillis(start);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
		while (true) {
			if (calendar.getTimeInMillis() >= end) {
				break;
			}
			list.add(sdf.format(calendar.getTime()));
			calendar.add(Calendar.DATE, 1);
		}
		return list;
	}
	
	private List<Long> createEmptyList(int size) {
		List<Long> list = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			list.add(0L);
		}
		return list;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "dashboard/duration")
	public Object duration(@RequestParam("series") String serieArray,
			@RequestParam("start") long start,
			@RequestParam("end") long end) {
		Map<String, Object> result = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		List<String> series = JSON.parseObject(serieArray, new TypeReference<List<String>>(){});
		Map<String, Long> map = new HashMap<>();
		Map<String, Map<String, Long>> serieMap = new HashMap<>();
		for (String serie : series) {
			serieMap.put(serie, jenkinsService.duration(start, end, serie));
			map.putAll(serieMap.get(serie));
		}
		if (map.isEmpty()) {
			List<String> x = x(start, end);
			List<Long> empty = createEmptyList(x.size());
			result.put("x", x);
			for (String serie : series) {
				Map<String, Object> one = new HashMap<>();
				one.put("name", serie);
				one.put("data", empty);
				list.add(one);
			}
			result.put("series", list);
			return result;
		}
		List<String> x = new ArrayList<>(map.keySet());
		result.put("x", x);
		for (String serie : series) {
			List<Long> data = new ArrayList<>();
			Map<String, Long> one = serieMap.get(serie);
			for (int i = 0; i < x.size(); i++) {
				if (one.containsKey(x.get(i))) {
					data.add(one.get(x.get(i)));
				} else {
					data.add(0L);
				}
			}
			Map<String, Object> dataOne = new HashMap<>();
			dataOne.put("name", serie);
			dataOne.put("data", data);
			list.add(dataOne);
		}
		result.put("series", list);
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "dashboard/needJenkins")
	public Object needJenkins() {
		Map<String, Object> result = new HashMap<>();
		result.put("result", needJenkins);
		return result;
	}

}
