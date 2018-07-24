package com.dc.appengine.plugins.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultComment;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultText;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUpdateUtil {
	public static void main(String[] args) throws Exception {
		// String filePath = "D:/xmltest/NewFile.xml";
		// String xPath = "//contact[@id='001']/name";

		String filePath = "D:/xmltest/AdapterServer.xml";
		// String xPath = "//phone[@id='022']";

		// //Service[@Name='服务参数']/comment()[3]此xpath 代表
		// 该标签//Service[@Name='服务参数']内部的第三个注释
		String xPath = "//MaxTCPClients/@MinValue";
		// String xPath = "//phone";
		Document doc = getDocument(filePath, false);
		doc = editAttributeValue(doc, xPath, "22222");
		writeBack(doc, filePath, "utf-8");
	
		 
	}

	public static Document getDocument(String filePath, boolean isCheckDTD) throws DocumentException {

		SAXReader xppReader = new SAXReader();
		if (!isCheckDTD) {
			xppReader.setValidation(false);// 不验证xml文件内的dtd
			xppReader.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
					InputStream stream = this.getClass().getClassLoader().getResourceAsStream("***.dtd");
					InputSource is = new InputSource(stream);
					is.setPublicId(publicId);
					is.setSystemId(systemId);
					return is;
				}
			});
		}
		Document doc = xppReader.read(new File(filePath));
		return doc;
	}

	/**
	 * 添加注释， ，注释在标签上面另起一行
	 * 
	 * @param filePath
	 * @param xPath
	 * @param comment
	 * @return
	 * @throws DocumentException
	 */
	public static Document addComment(Document doc, String xPath, String comment) {
		List<Node> nodeList = doc.selectNodes(xPath);
		Element tempEle = null;
		List<Node> contentList = null;
		String path;
		for (Node node : nodeList) {
			path = node.getUniquePath();
			tempEle = node.getParent();
			contentList = tempEle.content();
			for (int j = 0; j < contentList.size(); j++) {
				if (contentList.get(j).getUniquePath().equals(path)) {
					// 注释另起一行
					contentList.add(j, new DefaultText("\n"));
					contentList.add(j, new DefaultComment(comment + j));
					j = j + 2;
				}

			}
			tempEle.setContent(contentList);
		}
		return doc;
	}

	/**
	 * 修改注释 前提条件： 元素最近（前面的）位置的注释作为修改的注释 节点上面单个注释
	 * 
	 * @param doc
	 * @param xPath
	 * @param comment
	 * @return
	 */
	public static Document editComment(Document doc, String xPath, String comment) {
		List<Node> nodeList = doc.selectNodes(xPath);
		Element tempEle = null;
		List<Node> contentList = null;
		String path;
		int commentIndex = -1;
		for (Node node : nodeList) {
			path = node.getUniquePath();
			tempEle = node.getParent();
			contentList = tempEle.content();
			commentIndex = -1;
			for (int j = 0; j < contentList.size(); j++) {
				if (contentList.get(j).getNodeType() == Node.COMMENT_NODE) {
					commentIndex = j;
				}
				if (contentList.get(j).getUniquePath().equals(path)) {
					// 注释另起一行
					if (commentIndex != -1) {
						Node commentNode = contentList.get(commentIndex);
						commentNode.setText(comment);
						contentList.set(commentIndex, commentNode);
					}
				}

			}
			tempEle.setContent(contentList);
		}
		return doc;
	}

	/**
	 * 获取多个注释中的某一个注释
	 * 
	 * @param filePath
	 * @param xPath
	 * @param comment
	 * @param index
	 *            从0开始
	 * @return
	 * @throws DocumentException
	 */
	public static Document editCommentByIndex(String filePath, String xPath, String comment, int index)
			throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(filePath));
		List<Node> nodeList = doc.selectNodes(xPath);
		Element tempEle = null;
		List<Node> contentList = null;
		String path;
		int preNodeIndex = -1;
		for (Node node : nodeList) {
			path = node.getUniquePath();
			tempEle = node.getParent();
			contentList = tempEle.content();
			preNodeIndex = -1;
			for (int j = 0; j < contentList.size(); j++) {
				if (contentList.get(j).getNodeType() == Node.COMMENT_NODE) {
				}

				if (contentList.get(j).getUniquePath().equals(path)) {
					List<Node> list = new ArrayList<>();
					for (int i = preNodeIndex; i < j; i++) {
						if (contentList.get(i).getNodeType() == Node.COMMENT_NODE) {
							list.add(contentList.get(i));
						}
					}
					for (int k = 0; k < list.size(); k++) {
						if (index == k) {
							Node n = list.get(k);
							n.setText(comment);
							contentList.set(j, n);
						}
					}
				}

				// 前一个标签的位置
				if (contentList.get(j).getNodeType() == Node.ELEMENT_NODE) {
					preNodeIndex = j;
				}

			}
			tempEle.setContent(contentList);
		}
		return doc;
	}

	/**
	 * comment()[1] 定位到具体的注释，通过该注释在该节点父节点中注释排序的位置
	 * 
	 * @param filePath
	 * @param xPath
	 * @param comment
	 * @return
	 * @throws DocumentException
	 */
	public static Document editCommentByXPath(String filePath, String xPath, String comment) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document doc = reader.read(new File(filePath));
		List<Node> nodeList = doc.selectNodes(xPath);
		for (Node node : nodeList) {
			node.setText(comment);
		}
		return doc;
	}

	/**
	 * 前提条件： 元素最近（前面的）位置的注释作为修改的注释
	 * 
	 * @param doc
	 * @param xPath
	 *            节点路径
	 * @return
	 */
	public static Document removeComment(Document doc, String xPath) {
		List<Node> nodeList = doc.selectNodes(xPath);
		Element tempEle = null;
		List<Node> contentList = null;
		String path;
		int commentIndex = -1;
		for (Node node : nodeList) {
			path = node.getUniquePath();
			tempEle = node.getParent();
			contentList = tempEle.content();
			commentIndex = -1;
			for (int j = 0; j < contentList.size(); j++) {
				if (contentList.get(j).getNodeType() == Node.COMMENT_NODE) {
					commentIndex = j;
				}
				if (contentList.get(j).getUniquePath().equals(path)) {
					// 注释另起一行
					if (commentIndex != -1) {
						tempEle.remove(contentList.get(commentIndex));
					}
				}

			}
		}
		return doc;
	}

	public static Document addNode(Document doc, String xPath, String nodeName) {
		// 如果没有返回[]
		List<Node> selectList = doc.selectNodes(xPath);
		Node tempNode = null;
		Element tempEle = null;
		for (int i = 0; i < selectList.size(); i++) {
			tempNode = selectList.get(i);
			tempEle = (Element) tempNode;

			// 添加节点
			tempEle.add(new DefaultElement(nodeName));
			;
		}
		return doc;
	}

	public static Document editNodeName(Document doc, String xPath, String reName) {
		// 如果没有返回[]
		List<Node> selectList = doc.selectNodes(xPath);
		Node tempNode = null;
		Element tempEle = null;
		for (int i = 0; i < selectList.size(); i++) {
			tempNode = selectList.get(i);
			tempEle = (Element) tempNode;
			// 修改节点名称
			tempNode.setName(reName);

		}
		return doc;
	}

	public static Document removeNode(Document doc, String xPath) {
		// 如果没有返回[]
		List<Node> selectList = doc.selectNodes(xPath);
		Node tempNode = null;
		Element tempEle = null;
		for (int i = 0; i < selectList.size(); i++) {
			tempNode = selectList.get(i);
			tempEle = (Element) tempNode.getParent();

			tempEle.remove(tempNode);

		}
		return doc;
	}

	public static Document addAttribute(Document doc, String xPath, String name, String value) {
		List<Node> selectList = doc.selectNodes(xPath);
		Node tempNode = null;
		Element tempEle = null;
		for (int i = 0; i < selectList.size(); i++) {
			tempNode = selectList.get(i);
			tempEle = (Element) tempNode;
			// 该方式添加属性，若标签没有该属性，添加到后面；若有该属性，修改属性值。 值可以空字符串但不可为null.
			tempEle.addAttribute(name, value);
		}
		return doc;
	}

	/**
	 * 
	 * @param doc
	 * @param xPath
	 *            节点路径
	 * @param sourceName
	 * @param reName
	 * @return
	 */
	public static Document editAttributeName(Document doc, String xPath, String sourceName, String reName) {
		List<Node> selectList = doc.selectNodes(xPath);
		Node tempNode = null;
		Element tempEle = null;
		for (int i = 0; i < selectList.size(); i++) {
			tempNode = selectList.get(i);
			tempEle = (Element) tempNode;
			// Iterator attributeIterator = tempEle.attributeIterator();
			Attribute attribute = tempEle.attribute(sourceName);

			// 不能直接修改 Exception in thread "main" java.lang.UnsupportedOperationException:
			// This node cannot be modified
			// attribute.setName("newattrname");

			// 先删除后修改
			String value = attribute.getValue();
			tempEle.addAttribute(sourceName, null);
			tempEle.addAttribute(reName, value);

		}
		return doc;
	}

	/**
	 * 
	 * @param doc
	 * @param xPath
	 *            属性路径
	 * @param newValue
	 * @return
	 */
	public static Document editAttributeValue(Document doc, String xPath, String newValue) {
		List<Node> selectList = doc.selectNodes(xPath);
		Attribute attribute = null;
		for (int i = 0; i < selectList.size(); i++) {
			// Iterator attributeIterator = tempEle.attributeIterator();
			attribute = (Attribute) selectList.get(i);
			attribute.setValue(newValue);

		}
		return doc;
	}

	/**
	 * 
	 * @param doc
	 * @param xPath
	 *            属性路径
	 * @return
	 */
	public static Document removeAttribute(Document doc, String xPath) {
		List<Node> selectList = doc.selectNodes(xPath);
		Attribute attribute = null;
		for (int i = 0; i < selectList.size(); i++) {
			attribute = (Attribute) selectList.get(i);
			attribute.getParent().remove(attribute);
		}
		return doc;
	}

	public static Document addText(Document doc, String xPath, String text) {
		// 如果没有返回[]
		List<Node> nodeList = doc.selectNodes(xPath);
		Element tempEle = null;
		for (int i = 0; i < nodeList.size(); i++) {
			tempEle = (Element) nodeList.get(i);
			// tempEle.add(new FlyweightCDATA("<[CDATA[SF.><]]>asf第三方长达他cdata<>?"));
			tempEle.addText(text);
		}

		return doc;

	}

	public static Document editOrRemoveText(Document doc, String xPath, String text) {
		// 如果没有返回[]
		List<Node> nodeList = doc.selectNodes(xPath);
		Element tempEle = null;
		for (int i = 0; i < nodeList.size(); i++) {
			tempEle = (Element) nodeList.get(i);
			tempEle.setText(text);
		}
		return doc;

	}

	public static Document editText(Document doc, String xPath, String text) {
		// 如果没有返回[]
		List<Node> nodeList = doc.selectNodes(xPath);
		Element tempEle = null;
		for (int i = 0; i < nodeList.size(); i++) {
			tempEle = (Element) nodeList.get(i);
			tempEle.setText(text);
		}
		return doc;

	}

	public static Document removeText(Document doc, String xPath) {
		// 如果没有返回[]
		List<Node> nodeList = doc.selectNodes(xPath);
		Element tempEle = null;
		for (int i = 0; i < nodeList.size(); i++) {
			tempEle = (Element) nodeList.get(i);
			tempEle.setText("");
		}
		return doc;

	}

	/**
	 * Document 回写到文件中
	 * 
	 * @param doc
	 * @param fiLePathh
	 * @param isFormatXML
	 *            是否格式化文件
	 */
	public static void writeBack(Document doc, String filePath, String encoding) {
		try {
			XMLWriter writer = null;
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(encoding);
			writer = new XMLWriter(new FileOutputStream(new File(filePath)), format);
			// 针对n2e.setText("<![CDATA[补水首选水密码水保湿美白护洗护组合三件]]>")情况，需要设置
			writer.setEscapeText(false);
			writer.write(doc);
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
