package com.dc.appengine.node.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PropWriter {

	public static PropertiesConfiguration getprop(File file) {
		PropertiesConfiguration prop = new PropertiesConfiguration();
		InputStream fis = null;

		try {
			if (file != null) {
				prop.load(file);
			}
		} catch (ConfigurationException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

		return prop;
	}

	public static void writetoprop(File file, PropertiesConfiguration prop) {

		OutputStream fos = null;
		try {
			if (file != null) {
				prop.save(file);

			}
		} catch (ConfigurationException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

	}

}
