package com.dc.appengine.node.configuration;

import java.io.File;

public class PropFilegeter {
	public static File getFile(String fileName, String filePath) {

		File file = null;

		filePath = filePath.endsWith("/") ? filePath.concat(fileName)
				: filePath.concat("/").concat(fileName);
		file = new File(filePath);

		return file;
	}
}
