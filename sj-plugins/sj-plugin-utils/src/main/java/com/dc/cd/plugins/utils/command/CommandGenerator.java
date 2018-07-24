/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.cd.plugins.utils.command;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;


/**
 * 命令生成器
 * <p>
 * 用于生成操作命令，根据不同的操作系统生成不同的命令前缀，
 * 并将传入的参数以空格作为分隔符进行拼接，并返回拼接后的内容
 * </p>
 * @author liubingj
 */
public class CommandGenerator {
	
	private static final String SPACE = " ";

	private static volatile CommandGenerator _instance;
	
	private static String osType="linux";
	private CommandGenerator() {
		Properties prop = System.getProperties();
		osType = prop.getProperty("os.name").toLowerCase();
	}
	
	public String getCommandName(String command) {
		//String command="bash /bin/bashaaadasdas/a.sh  dasdajksd dasdjdas ff33";
		String [] commandsp=command.split("\\s+");
		if(commandsp.length>1){
			String commandFile=commandsp[1];
			if(!commandFile.contains("/")){
				commandFile=commandsp[2];
			}
		 
			File f= new File(commandFile);
			return f.getName();
			
		}
		return null;
	}
	
	public static CommandGenerator getInstance() {
		if ( _instance == null ) {
			synchronized ( CommandGenerator.class ) {
				if ( _instance == null ) {
					_instance = new CommandGenerator();
				}
			}
		}
		return _instance;
	}

	public String generate( String commandFile, boolean suffix, String... params ) {
		final StringBuffer buffer = generateByOs();
		if ( suffix ) {
			commandFile = commandFile + getSuffix();
		}
		String execMethod="ssh";
		if( "ssh".equals(execMethod)){
			commandFile = commandFile + getSuffix();
		}
		
		buffer.append( SPACE );
		buffer.append( commandFile );
		if ( params != null ) {
			for ( String item : params ) {
				buffer.append( SPACE );
				buffer.append( item );
			}
		}
		return buffer.toString();
	}
	
	public String generate( String commandFile, boolean suffix, List<Integer> list, String... params ) {
		final StringBuffer buffer = generateByOs();
		if ( suffix ) {
			commandFile = commandFile + getSuffix();
		}
		buffer.append( SPACE );
		buffer.append( commandFile );
		if ( params != null ) {
			for ( String item : params ) {
				buffer.append( SPACE );
				buffer.append( item );
			}
		}
		if(list!=null){
			for(int i=2;i<list.size();i++){
				buffer.append( SPACE );
				buffer.append( list.get(i) );
			}
		}
		return buffer.toString();
	}
	
	/**
	 * @param os
	 * @return
	 */
	private StringBuffer generateByOs() {
		
		StringBuffer buffer = new StringBuffer();
		if ( osType.contains( "windows" ) ) {
			buffer.append( "CMD /C" );
		} else if(osType.contains("aix")){
		} else{
			buffer = new StringBuffer( "bash" );
		}
		return buffer;
	}
	
	private String getSuffix() {
		String suffix = null;
		if (osType.contains( "windows" )) {
			suffix = ".bat";
		} else {
			suffix = ".sh";
		}
		return suffix;
	}
	public static void main(String[] args) {
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		System.out.println(os);
	}

}
