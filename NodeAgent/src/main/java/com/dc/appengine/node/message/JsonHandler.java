/**
 * Copyright 2000-2013 DigitalChina. All Rights Reserved.
 */
package com.dc.appengine.node.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.dc.appengine.node.configuration.Context;

/**
 * JSON报文处理器
 * <b>
 * <p>
 * 实现了JSON报文的组包与解包方法。
 * </p>
 * @author liubingj
 */
public class JsonHandler implements MessageHandler {

	private static Logger LOG = LoggerFactory.getLogger( JsonHandler.class );

	/* (non-Javadoc)
	 * @see com.dc.appengine.node.message.MessageHandler#assemble(com.dc.appengine.Context)
	 */
	public void assemble( Context context ) throws Exception {
		checkNull( context.getPayload() );

		if ( ! ( context.getPayload() instanceof InnerMessage< ? > ) ) {
			throw new IllegalArgumentException( "The payload of context must be an instance of " + InnerMessage.class.getName() );
		}
		final InnerMessage< ? > innerObj = context.getPayload();
		final byte[] message = JSON.toJSONBytes( innerObj, SerializerFeature.WriteMapNullValue );
		if ( LOG.isDebugEnabled() ) {
			LOG.debug( "Current assembled message is " + new String( message ) );
		}
		context.setPayload( message );
	}

	/* (non-Javadoc)
	 * @see com.dc.appengine.node.message.MessageHandler#disassemble(com.dc.appengine.Context)
	 */
	public void disassemble( Context context ) throws Exception {
		checkNull( context.getPayload() );
		
		if ( ! ( context.getPayload() instanceof byte[] ) ) {
			throw new IllegalArgumentException( "The payload of context must be an array of byte." );
		}
		final byte[] message = context.getPayload();
		final InnerMessage< ? > innerObj = JSON.parseObject( message, InnerMessage.class, Feature.AllowComment );
		if ( LOG.isDebugEnabled() ) {
			LOG.debug( "Current disassembled message is " + new String( message ) );
		}
		context.setPayload( innerObj );
	}
	
	private void checkNull( Object object ) {
		if ( object == null ) {
			LOG.error("The mom address may be error,please check!!!");
			throw new IllegalArgumentException( "The payload of context can not be null." );
		}
	}

}
