package com.dc.sw.appengine.plugins.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.alibaba.fastjson.JSON;
import com.dc.sw.appengine.plugins.jaxb.AppFile;
import com.dc.sw.appengine.plugins.jaxb.AppFiles;

public class XMLParser {
	
	public static Object unmarshall(InputStream xml,Class<?> clazz){
		Object obj=null;
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller u = jc.createUnmarshaller();
			obj = u.unmarshal(xml);
		} catch (JAXBException  e) {
			e.printStackTrace();
            throw new RuntimeException("Can't unmarshal the XML file, error message: " + e.getMessage());  
		}
		return obj;
	}
	
	public static String marshall(Object obj,Class<?> clazz){
		String result = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
			StringWriter writer =new StringWriter();
			m.marshal(obj, writer);
			result = writer.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
            throw new RuntimeException("Can't marshal the XML file, error message: " + e.getMessage());  
		}
		return result;
	}

	public static void main(String[] args) {
//		testUnMarshall();
		testMarshall();
	}
	
	public static void testMarshall(){
		AppFile file1 = new AppFile();
		file1.setIsApendDeploy(false);
		file1.setPath("test1");
		AppFile file2 = new AppFile();
		file2.setIsApendDeploy(true);
		file2.setPath("test2");
		List<AppFile> list = new ArrayList<AppFile>();
		list.add(file1);
		list.add(file2);
		AppFiles appFiles = new AppFiles();
		appFiles.setFiles(list);
		
		XMLParser xp = new XMLParser();
		xp.marshall(appFiles, AppFiles.class);
	}
	public static void testUnMarshall(){
		try {
			InputStream xml = new FileInputStream(new File("F:/test/cups_app_zl.xml"));
			XMLParser xp = new XMLParser();
			AppFiles root = (AppFiles) xp.unmarshall(xml, AppFiles.class);
			System.out.println(JSON.toJSONString(root));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
