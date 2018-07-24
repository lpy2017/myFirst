package com.dc.appengine.cloudui.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FrameENV {
	
	public static String FRAME_URL;
	public static String upload_chunk_size;
	
	public FrameENV(@Value("${frameRest}") String frameRest,
			@Value("${upload.chunk.size}") String upload_chunk_size) {
		FrameENV.FRAME_URL = frameRest;
		FrameENV.upload_chunk_size = upload_chunk_size;
	}

}
