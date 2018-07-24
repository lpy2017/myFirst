package com.dc.bank.appengine.plugins.utils;

public class SystemUtil {
	public static Boolean isWin = System.getProperty("os.name").toLowerCase().startsWith("win");

	
	public static String getLineSeparator(){
		String lineSeparator="\r\n";
		if(!isWin){
			lineSeparator="\n";
		}
		return lineSeparator;
	}

	public static void main(String[] args) {
		System.out.println("aa"+SystemUtil.getLineSeparator()+"bbb");
		// TODO Auto-generated method stub

	}

}
