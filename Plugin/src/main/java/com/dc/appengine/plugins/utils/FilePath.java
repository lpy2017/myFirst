package com.dc.appengine.plugins.utils;

import java.io.File;
import java.util.regex.Pattern;

import com.dc.appengine.plugins.constants.Constants;

public class FilePath {
	public static String getFilePath(String path) {
		Pattern p = Pattern.compile("^[a-zA-Z]:");
		if (path != null) {
			if(!path.startsWith("/") && !p.matcher(path).find()){
				path = Constants.DC_INSTALL_PATH + "/" + Constants.DEPLOY_DIR + "/"
						+ path;
			}
		}
		return path;
	}
	public static void main(String[] arg){
		String path = "F:\\test\\itsm_db.zip";
		Pattern p = Pattern.compile("^[a-zA-Z]:");
        if(p.matcher(path).find()){ //windows路径
			System.out.println("ddd");
		}
        File file = new File(path);
        if(file.exists()){
        	System.out.println("aa");
        }
        
	}
}