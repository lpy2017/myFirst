package com.dc.appengine.appmaster.utils;

import java.util.Comparator;
import java.util.Map;

public class ITSMVersionSort implements Comparator<Map<String, Object>> {

	@Override
	public int compare(Map<String, Object> version0, Map<String, Object> version1) {
		int versionNum0 = (int)version0.get("VERSIONNUM");
		int versionNum1 = (int)version1.get("VERSIONNUM");
		return versionNum0 - versionNum1;
	}

}
