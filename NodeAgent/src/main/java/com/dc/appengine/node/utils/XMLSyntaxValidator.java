package com.dc.appengine.node.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLSyntaxValidator {
	public static void main(String[] args) {
		System.out.println(validate("app_protocol_template.xml"));
	}

	public static int validate(String file) {
		DocumentBuilder docBuilder = null;
		InputSource ins = null;
		try {
			docBuilder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			ins = new InputSource(new FileInputStream(new File(file)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (docBuilder == null || ins == null) {
			return -1;
		}
		try {
			docBuilder.parse(ins);
			return 0;
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 1;
	}
}
