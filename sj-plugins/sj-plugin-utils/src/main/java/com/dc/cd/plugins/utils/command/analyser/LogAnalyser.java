package com.dc.cd.plugins.utils.command.analyser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.cd.plugins.utils.utils.ConfigHelper;

public class LogAnalyser extends AbstractLogAnalyser<String> {
	private static Logger LOG = LoggerFactory.getLogger(LogAnalyser.class);

	public void analysis(InputStream is) throws Exception {
		String[] sREs = null;
		String[] fREs =null;
		if(this.successRE ==null || "".equals(this.successRE)){
			if(!"CMD".equals(this.type)){
				this.successRE=ConfigHelper.getValue("saltSuccessRE");
				sREs = this.successRE.split("#");//正确标识集合
			}
		}else{
			if(!"CMD".equals(this.type)){
				this.successRE = this.successRE+"#"+ConfigHelper.getValue("saltSuccessRE");
				sREs = this.successRE.split("#");//正确标识集合
			}
		}
		
		if(this.failRE ==null || "".equals(this.failRE)){
			if(!"CMD".equals(this.type)){
				this.failRE=ConfigHelper.getValue("saltFailRE");
			}else{
				this.failRE="(?i)error";
			}
		}else{
			if(!"CMD".equals(this.type)){
				this.failRE=this.failRE+"#"+ConfigHelper.getValue("saltFailRE");
			}else{
				this.failRE=this.failRE+"#"+"(?i)error";
			}
		}
		fREs=this.failRE.split("#");
		BufferedReader reader = null;
		this.result="true";//如果正确和错误标识都没匹配上，默认执行成功
		InputStreamReader isReader = null;;
			try {
				if(this.logPath != null && !"".equals(this.logPath)){
					isReader = new InputStreamReader(new FileInputStream(logPath));
				}else if(is != null){
					isReader = new InputStreamReader(is);
				}
				reader = new BufferedReader(isReader);
				String temp = "";
				while ((temp = reader.readLine()) != null) {
					if(sREs !=null){
						Boolean succR=mathLine(temp, sREs);
						if(succR){
							this.result="true";
							break;
						}else{
							Boolean failR=mathLine(temp, fREs);
							if(failR){
								this.result="false";
								break;
							}
						}
					}else{
						Boolean failR=mathLine(temp, fREs);
						if(failR){
							this.result="false";
							break;
						}
					}
				}
			} finally {
				if (reader != null) {
					reader.close();
					reader = null;
				}
			}
	}
	
	public static Boolean mathLine(String line,String[] REs){
		Boolean result=false;
		for(String re : REs){
			Pattern p = Pattern.compile(re);
			  Matcher mat = p.matcher(line);
			  while(mat.find()){
//				  result= line.split(":")[1].trim() == "0"? true:false;
				  result= true;
				  break;
			  }
		}
		return result;
	}
	
	public static void main(String[] args){
//		String FailRE="(?i)error";
//		String[] REs = FailRE.split("#");
//		String line="ERROR 1045 (28000): Access denied for user 'root'@'10.1.108.100' (using password: YES)";
//		System.out.println(mathLine(line, REs));
//		ConfigHelper.getInstance();
//		System.out.println(ConfigHelper.getValue("saltSuccessRE"));
		String a= null;
		String b="aaa";
		System.out.println(a+b);
	}
}
