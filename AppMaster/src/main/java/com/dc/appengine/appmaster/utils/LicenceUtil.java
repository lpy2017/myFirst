package com.dc.appengine.appmaster.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.codec.Check;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.appengine.appmaster.entity.SystemENV;

public class LicenceUtil {
	private static final Logger log = LoggerFactory
			.getLogger(LicenceUtil.class);
	private static final int scanIntvl=30;//扫描周期，单位分钟
	private static final int writeTipIntvl=180;//将要超期的日志记录周期，单位分钟
	
	public static void scheduledTask4Licence(){
		AtomicInteger count=new AtomicInteger(0);
		ScheduledExecutorService executorService=  Executors.newScheduledThreadPool(2);
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					if(SystemENV.tryTimeOut < 0){//等待超时间的读取
						return;
					}
					Map<String,String> flags =getFlags(count);//
					String tryOut =flags.get("tryOut");
					String licenceOut = flags.get("licenceOut");
					if(JudgeUtil.isEmpty(tryOut)||"00".equals(tryOut)) {
						if(checkPropertyFile(flags,count)) {//
							writeFlags(flags);//记录tryOut
							checkLicenceFile(flags,count);
						};
					}else {
						if("01".equals(tryOut)&&"11".equals(licenceOut)) {
							writeFlags(flags);//记录licence信息
							//退出程序
							System.out.println("试用期已过,请申请licence续期！");
							log.info("试用期已过,请申请licence续期！");
							System.exit(-100);
						}
					}
					writeFlags(flags);//记录licence信息
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				
			}
		});
		executorService.scheduleAtFixedRate(thread, 1, scanIntvl, TimeUnit.MINUTES);
	}
	public static void  writeFlags(Map<String,String> flags){
		String tmpDir = System.getProperty("java.io.tmpdir");
		File file = new File(tmpDir+File.separator+".ck.properties");
		BufferedWriter wr =null;
		try {
			wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			for(Map.Entry<String, String> entry:flags.entrySet()) {
				wr.write(entry.getKey()+"="+entry.getValue()+System.lineSeparator());
			}
			wr.flush();
			wr.close();
			wr=null;
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(wr!=null) {
					wr.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static Map<String,String> readFlags(){
		Map<String,String> map = new HashMap<String, String>();
		String tmpDir = System.getProperty("java.io.tmpdir");
		File file = new File(tmpDir+File.separator+".ck.properties");
		BufferedReader rd =null;
		try {
			if(file.exists()) {
				rd = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line=null;
                while((line=rd.readLine()) !=null) {
                	String [] arr=line.split("=");
                	if(arr !=null && arr.length ==2) {
                		map.put(arr[0], arr[1]);
                	}
                }
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	public static Boolean checkPropertyFile(Map<String,String>flags,AtomicInteger count) {
		BufferedWriter wr = null;
		BufferedReader rd = null;
		Boolean outTime=false;
		Long accumulateTime=0L;
		try {
			File file = new File(".ck.property");//存放累计时间的文件,单位是毫秒
			if(!file.exists()){
				file.createNewFile();
			}else{
				rd = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
				Long totalTime=Long.valueOf(SystemENV.tryTimeOut*24L*60*60*1000);
				String line=rd.readLine();
				if(JudgeUtil.isEmpty(line)) {
					accumulateTime=0L;
				}else {
					String[] str =line.split("=");
					if(str !=null && str.length==2) {
						accumulateTime=Long.valueOf(str[1]);
					}else {
						accumulateTime=0L;
					}
				}
				outTime=accumulateTime>totalTime;
				if(!outTime) {
					long remaindTime = totalTime-accumulateTime;
					BigDecimal b1 =new BigDecimal(remaindTime);
					BigDecimal b2 =new BigDecimal(60*60*24L*1000);
					double remiandDay=b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
					if(remiandDay<3) {
						int remaind =(count.get()*scanIntvl)%(writeTipIntvl);//间隔writeTipIntvl 打印一次日志
						if(remaind==0) {
							System.out.println("试用期将满，还有【"+remiandDay+"天】,请注意续期！");
							log.info("试用期将满，还有【"+remiandDay+"天】,请注意续期！");
						}
						count.incrementAndGet();
					};
				}
				rd.close();
				rd=null;
			}
			if(!outTime){
				wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
				accumulateTime=accumulateTime+scanIntvl*60L*1000;
				wr.write("useTime="+accumulateTime);//累计使用时间,和定时任务的周期一致
				wr.flush();
				wr.close();
				wr=null;
			}
			flags.put("tryOut", outTime?"01":"00");
			flags.put("tryTime", SystemENV.tryTimeOut+"");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			try {
				if(rd!=null){
					rd.close();
				}
				if(wr!=null){
					wr.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		}
		  return outTime;
	}
	
	public static void checkLicenceFile(Map<String,String> flags,AtomicInteger count) {
		Check rcv = new Check();
		rcv.readFile();
		String licenceOut=flags.get("licenceOut");
		String deadTime=flags.get("deadTime");
		String deadTimeNew=rcv.timeLimit;
		if(!rcv.result&&JudgeUtil.isEmpty(deadTimeNew)){
			System.exit(-100);
		}
		if("11".equals(licenceOut)) {
			if(!JudgeUtil.isEmpty(deadTime)) {
				if(deadTime.compareTo(deadTimeNew)<0) {
					flags.put("licenceOut", "10");
					flags.put("deadTime", deadTimeNew);
				}
			}
		}else if(!"11".equals(licenceOut)||JudgeUtil.isEmpty(deadTimeNew)){
			if(!rcv.result) {
				flags.put("licenceOut", "11");
			}else if(rcv.result){
				flags.put("licenceOut", "10");
				double remiandDay=rcv.remiandDay;
				if(remiandDay>=0&&remiandDay<3) {
					int remaind =(count.get()*scanIntvl)%(writeTipIntvl);//间隔writeTipIntvl 打印一次日志
					if(remaind==0) {
						System.out.println("licence使用期将满，还有【"+remiandDay+"天】,请注意续期！");
						log.info("licence使用期将满，还有【"+remiandDay+"天】,请注意续期！");
					}
					count.incrementAndGet();
				}
			}
			flags.put("deadTime", deadTimeNew);
		}
	}
	public static Map<String,String> getFlags(AtomicInteger count){
		//判断是否超期
		Map<String,String> flags =readFlags();
		if(flags.size()>0) {
			String tryOut =flags.get("tryOut");
			String tryTime = flags.get("tryTime");
			if("01".equals(tryOut)) {//试用已过期
				if(Integer.valueOf(tryTime)<SystemENV.tryTimeOut) {
					flags.put("tryOut", "00");
					flags.put("tryTime", SystemENV.tryTimeOut+"");
				}else {
					checkLicenceFile(flags,count);
				}
			}else {
				flags.put("tryOut", "00");
				flags.put("tryTime", SystemENV.tryTimeOut+"");
			}
		}
		return flags;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
