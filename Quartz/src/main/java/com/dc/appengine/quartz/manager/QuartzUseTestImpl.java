package com.dc.appengine.quartz.manager;

import java.util.Date;

import org.apache.log4j.Logger;

import com.dc.appengine.quartz.manager.QuartzJobFactory;
import com.dc.appengine.quartz.manager.QuartzManager;



public class QuartzUseTestImpl{
	 private static final Logger log = Logger.getLogger("");  
	 private static final String JOB_GROUP = "MY_JOBGROUP_NAME";
	    /** 
	     * @param 
	     * @return 
	     */  
	    public static boolean timerTask(String msgId,String desc,String cron) {  
	        //String cron = QuartzManager.getQuartzTime(Util.toString(sendTime));//获得quartz时间表达式，此方法自己写  
	        ScheduleJob job = new ScheduleJob();  
	        String jobName = msgId+"_job";  
	        job.setJobId(msgId);  
	        job.setJobName(jobName);  
	        job.setCreTime((new Date()).toString());  
	        job.setJobCron(cron);  
	        job.setJobGroup(JOB_GROUP);  
	        job.setJobDesc(desc);  
	        try {  
	            //删除已有的定时任务
	            QuartzManager.removeJob(jobName);  
	            //添加定时任务  
	            QuartzManager.addJob(jobName, QuartzJobFactory.class, cron,job);  
	            return true;  
	        } catch (Exception e) {  
	            log.info("加载定时器错误："+e);  
	            return false;  
	        }  
	    }  
	    public static void main(String[] args){
	    	//"0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发 
	    	for(int i=1;i<10 ;i++){
	    		boolean flag=timerTask(i+"", "test"+i, "0 * 15 * * ?");
	    	}
	    }
}
