package com.dc.appengine.node.command.analyser.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dc.appengine.node.cache.NodeOsStatCache;
import com.dc.appengine.plugins.command.analyser.AbstractAnalyser;
/**
 * 获取linux 内核数量 （/proc/cpuinfo）
 * @author hannn
 * physical id 的行表示这个物理cpuid  统计id数量即可
 * cpu cores   的行表示每个物理cpu中的核数
 * 
 * 如果以上两个数字都为0则统计processor的数量
 *
 */
public class NodeCpuCoreAnalyser extends AbstractAnalyser<String> {
	private int cpuCores=0;
	@Override
	public void analysis(InputStream is) throws Exception {
		Set<String> phyic_cpuids=new HashSet<String>();
		int percpuCores=0;
		int phyicCpuNum=0;
		int processorNum=0;
		Pattern p=Pattern.compile("^physical\\s+id\\s*\\:\\s*([0-9])+");
		Pattern p2=Pattern.compile("^cpu\\s+cores.");
		Scanner scann= new Scanner(is);
		while(scann.hasNext()){
			String line = scann.nextLine();
			Matcher m=p.matcher(line);
			if(m.find()){
				phyic_cpuids.add(m.group(1));
				continue;
			}
			if(percpuCores ==0){
				Matcher m2=p2.matcher(line);
				if(m2.find()){
					String s=getValue(line);
					percpuCores=Integer.valueOf(s);
				}
				if(line.contains("processor")){
					processorNum++;
				}
			}
		}
		phyicCpuNum=phyic_cpuids.size();
		if(percpuCores==0 && phyicCpuNum ==0){
			cpuCores=processorNum;
		}else{
			cpuCores=phyicCpuNum*percpuCores;
		}
		NodeOsStatCache node=NodeOsStatCache.getInstance();
		node.setCpuCore(cpuCores);
		scann.close();
		
	}
	private String getValue(String line){
		String regex="[.A-Za-z0-9]+\\s*\\:\\s*([.A-Za-z0-9]+)";
		Pattern p=Pattern.compile(regex);
		Matcher m=p.matcher(line);
		if(m.find()){
			return m.group(1);
		}
		return null;
	}
	public static void main(String[] args) throws FileNotFoundException, Exception {
		File f= new File("/root/proc/cpuinfo1");
		new NodeCpuCoreAnalyser().analysis(new FileInputStream(f));
	}
}
