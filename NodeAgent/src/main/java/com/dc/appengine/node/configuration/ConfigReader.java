/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.configuration;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

import javax.xml.bind.JAXBContext;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.configuration.model.AbstractDefinition;
import com.dc.appengine.node.configuration.model.AbstractPropDefinition;

/**
 * CnofigReader.java
 * 
 * @author liubingj
 */
public class ConfigReader {

	private static Logger LOG = LoggerFactory.getLogger(ConfigReader.class);

	private static ConfigReader instance;

	private static String MANAGMANT = "management";
	private static String NATIVE_SUFFIX = "N";
	private static String NODE_NAME = "nodeName";

	private ConfigReader() {
	}

	public static ConfigReader getInstance() {
		synchronized (ConfigReader.class) {
			if (instance == null) {
				instance = new ConfigReader();
			}
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractDefinition> T parseXmlToModel(File file, String package_info, ClassLoader cl) {
		T definition = null;
		try {
			final JAXBContext context = JAXBContext.newInstance(package_info, cl);
			definition = (T) context.createUnmarshaller().unmarshal(file);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
		return definition;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends AbstractPropDefinition> T parsePropToModel(File file, Class<T> clazz, List<Class> subclazzlist) {
		T definition = null;
		boolean ismanagement = false;
		try {
			PropertiesConfiguration prop = PropWriter.getprop(file);
			// reflect
			Object omanagement = prop.getString(MANAGMANT);
			if (omanagement != null) {
				ismanagement = Boolean.parseBoolean((String) omanagement);
			}
			definition = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				// 如果management=true 或非下发项目\management都跳过。实现 如果没有下发项目又不是本地设置项目
				// 则属性为空
				if (ismanagement) {
					if (!field.getName().endsWith(NATIVE_SUFFIX)) {
						if (!field.getName().equals(MANAGMANT)) {
							if (!field.getName().equals(NODE_NAME))
								continue;
						}
					}
				}

				if (subclazzlist != null && subclazzlist.size() > 0) {
					if (!field.getType().isPrimitive()
							&& field.getType().getSuperclass().equals(AbstractPropDefinition.class)) {
						AbstractPropDefinition subdefine = null;

						for (Class tmpclazz : subclazzlist) {
							if (field.getType().equals(tmpclazz)) {
								subdefine = parsePropToModel(file, tmpclazz, null);
								field.set(definition, subdefine);
								subclazzlist.remove(tmpclazz);
								break;
							}
						}
					} else {
						Object obj = prop.getProperty(field.getName());
						if (obj != null) {
							obj = propTransByType(field, obj);
							field.set(definition, obj);
						}
					}
				} else {
					Object obj = prop.getProperty(field.getName());
					if (obj != null) {
						obj = propTransByType(field, obj);
						field.set(definition, obj);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return definition;
	}

	@SuppressWarnings("unchecked")
	public <T extends AbstractDefinition> T parseXmlToModel(File file, String package_info) {
		return (T) parseXmlToModel(file, package_info, Thread.currentThread().getContextClassLoader());
	}

	private Object propTransByType(Field field, Object obj) {
		String content = "";
		if (obj instanceof List) {
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) obj;
			StringBuilder strb = new StringBuilder();
			for (String tmp : list) {
				strb.append(tmp);
				strb.append(",");
			}
			content = strb.toString().substring(0,strb.length()-1);
		} else {
			content = (String) obj;
			content = content.trim();
		}
		if (field.getType().getName().equals("int")) {
			obj = Integer.parseInt(content);
		} else if (field.getType().getName().equals("boolean")) {
			obj = Boolean.parseBoolean(content);
		}else{
			obj = content;
		}
		return obj;
	}
}
