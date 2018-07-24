package com.dc.appengine.plugins.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessCMDHelper {
	String cmd;
	public ProcessCMDHelper(String cmd){
		this.cmd = cmd;
	}
	
	public Map<String,Object> execute() throws IOException, InterruptedException{
		List<String> list = new ArrayList<>();
		list.add("sh");
		list.add("-c");
		list.add(this.cmd);
		System.out.println(list);
		ProcessBuilder pb = new ProcessBuilder(list);
		Process p = pb.start();
		final InputStream is = p.getInputStream();
		final InputStream err = p.getErrorStream();
		final StringBuilder out = new StringBuilder();
		final StringBuilder error = new StringBuilder();
		Thread t1 = new Thread(){
			@Override
			public void run() {
				try(BufferedReader br = new BufferedReader(new InputStreamReader(is))){
					String str = null;
					while((str = br.readLine())!=null){
						out.append(str).append(System.lineSeparator());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t1.start();
		Thread t2 = new Thread(){
			@Override
			public void run() {
				try(BufferedReader br = new BufferedReader(new InputStreamReader(err))){
					String str = null;
					while((str = br.readLine())!=null){
						error.append(str).append(System.lineSeparator());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		t2.start();	
		t1.join();
		t2.join();
		Map<String,Object> result = new HashMap<>();
		result.put("out", out.toString());
		result.put("err", error.toString());
		return result;
	}
}
