package com.dc.appengine.node.configuration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class ConfigWriter {

	private static String nlspreff = "nstep";

	public static Document getdoc(File file) {
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(file);// 将现有的xml文件读进内存
			String strNameSpace = doc.getRootElement().getNamespace().getStringValue();
			if (strNameSpace != null && strNameSpace.trim().length() > 0) {
				Map<String, String> map = new HashMap<String, String>();
				map.put(nlspreff, strNameSpace);
				reader.getDocumentFactory().setXPathNamespaceURIs(map);
				doc = reader.read(file);
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}

	public static void update(Document doc, String nodename, String nodeValue) {

		if (doc != null) {
			Node node = doc.selectSingleNode("//*[name()='" + nodename + "']");
			node.setText(nodeValue);
		}
	}

	public static void update(Document doc, String nodenameparent, String nodename, String nodeValue) {

		if (doc != null) {
			Node node = doc.selectSingleNode("//*[name()='" + nodenameparent + "']/*[name()='" + nodename + "']");
			node.setText(nodeValue);
		}
	}
	
	public static void updateAtt(Node node, String attname, String attvalue) {
		((Element)node).attribute(attname).setValue(attvalue);
	}

	public static Node getChindNodeByParent(Document doc, Node parentNode, String relateChildPath, String childAtt,
			String childAttValue) {
		Node node = null;
		node = parentNode.selectSingleNode(changePathByIsNameSpace(doc, relateChildPath) + combinTheAtt(childAtt,childAttValue));
		return node;
	}
	
	public static String getMomUrl(Document doc,String parentNode,String momadd,String queortopicname){
		@SuppressWarnings("unchecked")
		List<Node> nodes = doc.selectNodes("//"+parentNode);
		StringBuilder sbtr = new StringBuilder();
		for(Node node:nodes){
			sbtr.append(node.selectSingleNode(momadd).getText());
			sbtr.append("?");
			sbtr.append(node.selectSingleNode(queortopicname).getText());
			sbtr.append(",");
		}
		return sbtr.substring(0,sbtr.length()-1);
	}
	
	public static Node getChindNodeByParent(Document doc, String parentNode,String parentAtt,String parentValue, String relateChildPath, String childAtt,
			String childAttValue) {
		Node node = null;
		StringBuilder strb = new StringBuilder("//");
		strb.append(changePathByIsNameSpace(doc, parentNode));
		strb.append(combinTheAtt(parentAtt,parentValue));
		strb.append("/");
		strb.append(changePathByIsNameSpace(doc,relateChildPath));
		strb.append(combinTheAtt(childAtt,childAttValue));
		
		node = doc.selectSingleNode(strb.toString());
		return node;
	}
	
	private static StringBuilder combinTheAtt(String strAttName,String strAttValue){
		StringBuilder strb = new StringBuilder();
		strb.append("[@");
		strb.append(strAttName);
		strb.append("='");
		strb.append(strAttValue);
		strb.append("']");
		return strb;
	}

	public static String changePathByIsNameSpace(Document doc, String relatePath) {
		String strPath = "";

		boolean isNameSpace = isdefedNameSpace(doc);
		if (isNameSpace) {
			relatePath = nlspreff + ":" + relatePath;
			strPath = relatePath.replaceAll("/", "/" + nlspreff + ":");
		} else {
			strPath = relatePath;
		}

		return strPath;
	}

	public static Node getNodeByNamenAtt(Document doc, String nodeName, String protocalName, String protocalValue) {
		Node node = null;

		node = doc.selectSingleNode("//" + changePathByIsNameSpace(doc, nodeName) + combinTheAtt(protocalName,protocalValue));

		return node;
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, String>> getNodeChildAttr(Document doc, String nodeName, String protocalName,
			String protocalValue) {
		List<Map<String, String>> list = null;
		Node node = getNodeByNamenAtt(doc, nodeName, protocalName, protocalValue);
		Iterator<Element> ite = ((Element) node).elementIterator();
		if (ite.hasNext()) {
			list = new ArrayList<Map<String, String>>();
		}
		while (ite.hasNext()) {
			Element tmpElement = ite.next();
			List<Attribute> atlist = tmpElement.attributes();
			if (atlist != null && atlist.size() > 0) {
				Map<String, String> map = new HashMap<String, String>();
				for (Attribute tmpAtt : atlist) {
					map.put(tmpAtt.getName(), tmpAtt.getValue());
				}
				list.add(map);
			}
		}
		return list;
	}

	public static void writetoxml(Document doc, File file) {
		try {
			XMLWriter writer = new XMLWriter(new FileWriter(file));
			writer.write(doc);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean isdefedNameSpace(Document doc) {
		String strNameSpace = doc.getRootElement().getNamespace().getStringValue();
		return strNameSpace != null && strNameSpace.trim().length() > 0;
	}

}
