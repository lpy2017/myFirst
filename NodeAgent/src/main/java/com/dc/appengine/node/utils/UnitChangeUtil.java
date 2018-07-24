package com.dc.appengine.node.utils;

import java.text.DecimalFormat;

public class UnitChangeUtil {
	public static double changeUnit(double num, String unit) {
		long lu = 1;
		if (unit.toLowerCase().contains("g")) {
			lu = 1024 * 1024 * 1024;
		} else if (unit.toLowerCase().contains("m")) {
			lu = 1024 * 1024;
		} else if (unit.toLowerCase().contains("k")) {
			lu = 1024;
		}
		DecimalFormat model = new DecimalFormat("0.00");
		return Double.valueOf(model.format(num / lu));
	}
}
