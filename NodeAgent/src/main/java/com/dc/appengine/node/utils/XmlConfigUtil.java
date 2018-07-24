package com.dc.appengine.node.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlConfigUtil {
	public static String doc2XmlFile(Document document, File file) {
		// boolean flag = true;
		ByteArrayOutputStream baos = null;
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			baos = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(baos);
			transformer.transform(source, result);
		} catch (Exception ex) {
			// flag = false;
			ex.printStackTrace();
		}
		return baos == null ? "" : baos.toString();
	}

	public static Document load(File file) {
		Document document = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse(file);
			document.normalize();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}

	public static String xmlUpdateDemo(File file, String key, String value) {
		Document document = load(file);
		Node root = document.getDocumentElement();

		if (root.hasChildNodes()) {

			NodeList ftpnodes = root.getChildNodes();

			for (int i = 0; i < ftpnodes.getLength(); i++) {
				NodeList ftplist = ftpnodes.item(i).getChildNodes();
				for (int k = 0; k < ftplist.getLength(); k++) {
					Node subnode = ftplist.item(k);
					if (subnode.getNodeType() == Node.ELEMENT_NODE
							&& subnode.getNodeName().equals(key)) {
						subnode.getFirstChild().setNodeValue(value);
					}
				}

			}
		}

		return doc2XmlFile(document, file);
	}
}
