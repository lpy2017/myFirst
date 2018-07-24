package com.dc.appengine.node.message;

import com.dc.appengine.node.preloader.Preloadable;



public class InitInvokerPreloader implements Preloadable{

	@Override
	public void preload() throws Exception  {
		InvokerInit.init();	
	}

}