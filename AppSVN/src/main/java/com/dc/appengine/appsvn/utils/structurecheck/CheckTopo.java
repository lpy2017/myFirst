package com.dc.appengine.appsvn.utils.structurecheck;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class CheckTopo {
	private static Logger log = LoggerFactory.getLogger(CheckTopo.class);

	public static boolean check(String topoPath, Map<String, Object> payload) {
		// 读取模板文件
		// Map<String, Object> payload = new HashMap<String, Object>();
		InputStream is = CheckTopo.class.getClassLoader().getResourceAsStream("topo_template.xml");
		if (is == null) {
			payload.put("result", false);
			payload.put("errorMsg", "template[topo_template.xml] not found!");
			return false;
		}
		Checkers checkers = null;
		try {
			JAXBContext context = JAXBContext.newInstance(Checkers.class);
			Unmarshaller her = context.createUnmarshaller();
			Object obj = her.unmarshal(is);
			if (obj instanceof Checkers) {
				checkers = (Checkers) obj;
				log.debug("get " + checkers.getCheckers().size() + " rules");
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		if (checkers == null) {
			payload.put("result", false);
			payload.put("errorMsg", "checker not found!");
			return false;
		}
		boolean checkOk = false;
		String errorMsg = "";
		// 读取topo包文件
		// 先解析包基本结构
		 
		check: {

			Iterator<CheckerDefinition> iterator = checkers.getCheckers().iterator();
			while (iterator.hasNext()) {
				CheckerDefinition checker = iterator.next();
				String name = checker.getName();
				String checkerType = checker.getType();
				String checkerName = name;

				if ("folder".equals(checkerType)) {
					checkerName = name + "/";
				}

				boolean checkExist = false;
				try (InputStream in = new BufferedInputStream(new FileInputStream(topoPath));
						ZipInputStream zin = new ZipInputStream(in)) {
					ZipEntry ent;
					while ((ent = zin.getNextEntry()) != null) {

						if ((checkerName).equals(ent.getName())) { // get head
							// System.out.println("==get head:" + checkerName);
							checkExist = true;
							break;
						}
					}

					if (!checkExist) {
						errorMsg = "required file/folder " + name + " not found";
						break check;
					}

					// filetype
					if ("folder".equals(checkerType)) {
						String[] innerFolderType = checker.getFiletype().split("\\|");
						List<String> types = new ArrayList<String>();
						for (String t : innerFolderType) {
							types.add(t);
						}
						// System.out.println(types);
						// 开始逐个文件检查
						while ((ent = zin.getNextEntry()) != null) {
							String fileName = ent.getName();
							if (fileName.startsWith(checkerName)) {
								String ft = fileName.substring(fileName.lastIndexOf(".") + 1);
								// System.out.println("----------------"+ft);
								if (!types.contains(ft)) {
									errorMsg = "file type :[" + ft + " ]in " + checkerName + " is not available";
									break check;
								}
							} else {
								continue;
							}
						}
					}
					// other checkers
					String op = checker.getOp();
					if (op != null) {
						try {
							Object o = Class.forName(op).newInstance();
							if (o instanceof IChecker) {
								IChecker cker = (IChecker) o;
								boolean c = cker.check(topoPath, checker, payload);
								if (!c) {
									errorMsg = payload.get("errorMsg").toString();
									break check;
								}
							} else {
								errorMsg = "Illegal checker type:" + op;
								break check;
							}
						} catch (Exception e) {
							errorMsg = "checker " + op + " not found";
							break check;
						}

					}

				} catch (Exception e) {
					errorMsg = "zip IO error!";
					break check;
				}

			}
			checkOk = true;

		}
		payload.put("result", checkOk);
		if (checkOk) {
			log.debug("checkOk");
		} else {
			log.debug("check error:" + errorMsg);
			payload.put("errorMsg", errorMsg);
		}
		log.debug("================check result=======================");
		log.debug("check result:"+JSON.toJSONString(payload));
		return checkOk;
	}
}
