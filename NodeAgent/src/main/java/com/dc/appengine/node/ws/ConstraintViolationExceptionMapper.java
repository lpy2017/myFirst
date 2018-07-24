package com.dc.appengine.node.ws;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException>{

	@Override
	public Response toResponse(ConstraintViolationException arg0) {
		Set<ConstraintViolation<?>> violantions = arg0.getConstraintViolations();
		StringBuilder buffer = new StringBuilder();
		for(ConstraintViolation<?> vl : violantions){
			buffer.append(","+vl.getMessage());
		}
		if(buffer.length()>0)
			buffer.deleteCharAt(0);
		
		return Response.status(400).entity(buffer.toString()).type(MediaType.TEXT_PLAIN_TYPE).build();
	}

}
