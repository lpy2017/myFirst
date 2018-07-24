/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.preloader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.node.preloader.model.PreloaderDefinition;
import com.dc.appengine.node.preloader.model.PreloadersDefinition;

/**
 * PreloadersProcessor.java
 * @author liubingj
 */
public class PreloadersProcessor extends AbstractProcessor< PreloadersDefinition > {
	
	private static Logger LOG = LoggerFactory.getLogger( PreloadersProcessor.class );
	
	/**
	 * @param definition
	 */
	public PreloadersProcessor( PreloadersDefinition definition ) {
		super( definition );
	}
	
	@SuppressWarnings( "unchecked" )
	private void mergeSort( Object[] src, Object[] dest, int low, int high,
			int off ) {
		int length = high - low;

		if ( length <= 2 ) {
			if ( ( ( Comparable< Object > ) dest[ high - 1 ] ).compareTo( dest[ low ] ) < 0 ) {
				swap( dest, low, high - 1 );
			}
			return;
		}

		// Recursively sort halves of dest into src
		int destLow = low;
		int destHigh = high;
		low += off;
		high += off;
		int mid = ( low + high ) >>> 1;
		mergeSort( dest, src, low, mid, -off );
		mergeSort( dest, src, mid, high, -off );

		// If list is already sorted, just copy from src to dest. This is an
		// optimization that results in faster sorts for nearly ordered lists.
		if ( ( ( Comparable< Object > ) src[ mid - 1 ] ).compareTo( src[ mid ] ) <= 0 ) {
			System.arraycopy( src, low, dest, destLow, length );
			return;
		}

		// Merge sorted halves (now in src) into dest
		for ( int i = destLow, p = low, q = mid; i < destHigh; i++ ) {
			if ( q >= high || p < mid
					&& ( ( Comparable< Object > ) src[ p ] ).compareTo( src[ q ] ) <= 0 )
				dest[ i ] = src[ p++ ];
			else
				dest[ i ] = src[ q++ ];
		}
		
	}
	
	private void swap( Object[] x, int a, int b ) {
		Object t = x[ a ];
		x[ a ] = x[ b ];
		x[ b ] = t;
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.configuration.processor.Processor#process(java.lang.Object)
	 */
	public < T, R > R process( T t ) throws Exception {
		if ( getDefinition() != null && ! getDefinition().getPreloaders().isEmpty() ) {
			PreloaderDefinition[] preloaders = new PreloaderDefinition[ getDefinition().getPreloaders().size() ];
			getDefinition().getPreloaders().toArray( preloaders );
			getDefinition().getPreloaders().clear();
			PreloaderDefinition[] aux = preloaders.clone();
			mergeSort( aux, preloaders, 0, preloaders.length, 0 );
			for ( PreloaderDefinition preloader : preloaders ) {
				Object instance = Class.forName( preloader.getClassPath() ).newInstance();
				if ( Preloadable.class.isInstance( instance ) ) {
					( ( Preloadable )instance ).preload();
				} else {
					LOG.error( preloader.getClassPath() + " is not an instance of Preloadable." );
				}
			}
		}
		return null;
	}

}
