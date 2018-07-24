/**
 * Copyright 2000-2012 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.preloader.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dc.appengine.node.configuration.model.ProcessorDefinition;
import com.dc.appengine.node.preloader.PreloadersProcessor;
import com.dc.appengine.node.preloader.Processor;

/**
 * PreloadersDefinition.java
 * @author liubingj
 */
@XmlRootElement( name = "preloaders" )
public class PreloadersDefinition extends ProcessorDefinition {
	@XmlElement( name = "preloader" )
	private List< PreloaderDefinition > preloaders = new ArrayList< PreloaderDefinition >( 10 );

	public List< PreloaderDefinition > getPreloaders() {
		return preloaders;
	}

	public void setPreloaders( List< PreloaderDefinition > preloaders ) {
		this.preloaders = preloaders;
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.configuration.model.ProcessorDefinition#createProcessor()
	 */
	public Processor createProcessor() {
		return new PreloadersProcessor( this );
	}

}
