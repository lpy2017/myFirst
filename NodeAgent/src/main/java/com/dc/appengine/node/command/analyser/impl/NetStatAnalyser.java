/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.command.analyser.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;

/**
 * PsAnalyser.java
 * 
 * @author liubingj
 */
public class NetStatAnalyser extends AbstractAnalyser<String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.dc.appengine.node.command.Analytic#analysis(java.io.InputStream)
	 */
	public void analysis(InputStream input) throws Exception {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(input));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (this.filter.doFilter(line)) {
					break;
				}
			}

			if (line != null) {
				Matcher match = Pattern.compile("[0-9]{1,5}$").matcher(line);
				match.find();
				if (match != null) {
					this.result = new String(match.group().getBytes());
				}

			}
		} finally {
			if (reader != null) {
				reader.close();
				reader = null;
			}
		}
	}

}
