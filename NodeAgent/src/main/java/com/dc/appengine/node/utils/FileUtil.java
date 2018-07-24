/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * 文件工具类
 * @author liubingj
 */
public class FileUtil {
	
	private static Logger LOG = Logger.getLogger( FileUtil.class );
	
	private static final FileUtil SINGLE_INSTANCE = new FileUtil();
	
	public static FileUtil getInstance() {
		return SINGLE_INSTANCE;
	}
	
	/**
	 * 获得文件
	 * @param fileName 文件相对路径
	 * @param propertyName 定义文件路径的系统属性名称
	 * @return
	 * @throws URISyntaxException 
	 * @throws Exception
	 */
	public File getFile( String fileName, String propertyName )  throws FileNotFoundException, URISyntaxException{
		String filePath = null;
		if ( propertyName != null && ! "".equals( propertyName ) ) {
			filePath = System.getProperty( propertyName );
		}
		File file = null;
		
		if ( filePath == null || "".equals( filePath ) ) {
			URL url = FileUtil.class.getClassLoader().getResource( fileName );
			if ( url == null ) {
				throw new FileNotFoundException( fileName + " not found!" );
			}
			file = new File( url.toURI() );
		} else {
			filePath = filePath.endsWith( "/" ) ? filePath.concat( fileName ) 
					: filePath.concat( "/" ).concat(  fileName );
			file = new File( filePath );
		}
		return file;
	}

	/**
	 * @param file
	 */
	public String read( File file, String charset ) {
		final byte[] content = read( file );
		return content == null ? "" : new String( content );
	}
	
	public byte[] read( File file ) {
		if ( ! ( file.exists() && file.isFile() ) ) {
			throw new IllegalArgumentException( "The file not exist or not a file" );
		}
		FileInputStream fis = null;
		byte[] content = null;
		try {
			fis = new FileInputStream( file );
			content = new byte[ fis.available() ];
			fis.read( content );
		} catch ( FileNotFoundException e ) {
			LOG.error( e.getMessage(), e );
		} catch ( IOException e ) {
			LOG.error( e.getMessage(), e );
		} finally {
			if ( fis != null ) {
				try {
					fis.close();
				} catch ( IOException e ) {
					LOG.error( e.getMessage(), e );
				}
				fis = null;
			}
		}
		return content;
	}

}
