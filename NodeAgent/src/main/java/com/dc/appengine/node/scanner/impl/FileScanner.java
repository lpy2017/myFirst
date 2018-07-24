/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.scanner.impl;

import java.io.File;
import java.util.LinkedList;

import com.dc.appengine.node.scanner.ResourceFilter;
import com.dc.appengine.node.scanner.ResourceScanner;


/**
 * File Scanner Class
 * 
 * @author liubingj
 */
public class FileScanner implements ResourceScanner {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.dc.appengine.framework.resource.ResourceScanner#scan(java.lang.Object
	 * , com.dc.appengine.framework.resource.ResourceFilter)
	 */
	public < R > void scan( R resource, ResourceFilter filter )
			throws Exception {
		if ( !( resource instanceof File ) ) {
			throw new IllegalArgumentException(
					"The resource must be an instance of java.io.File." );
		}
		final File root = ( File ) resource;
		list: {
			if ( root.isFile() ) {
				filter.doFilter( root );
				break list;
			}
			final LinkedList< StackElement > stack = new LinkedList< StackElement >();
			File[] files = root.listFiles();
			int i = -1;
			do {
				if ( stack.size() != 0 ) {
					StackElement e = stack.remove( 0 );
					files = e.getFileList();
					i = e.getI();
				}
				i++;
				for ( ; files != null && i < files.length; i++ ) {
					if ( files[ i ].isDirectory() ) {
						stack.add( 0, new StackElement( i, files ) );
						files = files[ i ].listFiles();
						i = -1;
						continue ;
					}
					filter.doFilter( files[ i ] );
				}
			} while ( stack.size() != 0 );
		}
	}
	
	/**
	 * Stack Element Class
	 * @author liubingj
	 */
	private class StackElement {
		
		private int i;
		
		private File[] fileList;
		
		StackElement( int i, File[] fileList ) {
			this.i = i;
			this.fileList = fileList;
		}

		protected int getI() {
			return i;
		}

		protected File[] getFileList() {
			return fileList;
		}
		
	}

}
