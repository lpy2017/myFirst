package com.dc.appengine.quartz.manager;
import static org.quartz.JobBuilder.newJob;
import java.text.ParseException;  
import java.text.SimpleDateFormat;  
import java.util.Date;  
  
import org.quartz.CronScheduleBuilder;  
import org.quartz.CronTrigger;  
import org.quartz.JobDetail;  
import org.quartz.JobKey;  
import org.quartz.Scheduler;  
import org.quartz.SchedulerException;  
import org.quartz.SchedulerFactory;  
import org.quartz.Trigger;  
import org.quartz.TriggerBuilder;  
import org.quartz.TriggerKey;

import com.dc.appengine.quartz.StdSchedulerFactory;
import com.dc.appengine.quartz.utils.MessageHelper;
  
/** 
 *  管理器
 *  任务的增加、删除
 * @Description 
 */  
public class QuartzManager {  
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();    
    private static String JOB_GROUP_NAME = "CD_JOBGROUP_NAME";    
    private static String TRIGGER_GROUP_NAME = "CD_TRIGGERGROUP_NAME";    
    
    /** 
     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名 
     * @param jobName 任务名 
     * @param cls 任务 
     * @param time 时间设置，参考quartz说明文档  
     */  
    @SuppressWarnings("unchecked")    
    public static String addJob(String jobName, Class cls, String time,Object scheduleJob) {
    	int i = 0;
        try {    
            Scheduler sched = gSchedulerFactory.getScheduler();    
            JobDetail job = newJob(cls)  
                    .withIdentity(jobName, JOB_GROUP_NAME)  
                    .build();  
            // 添加具体任务方法  
            job.getJobDataMap().put("scheduleJob", scheduleJob);  
            // 表达式调度构建器  
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time).withMisfireHandlingInstructionDoNothing();  
            // 按新的cronExpression表达式构建一个新的trigger  
            Trigger trigger = TriggerBuilder  
                    .newTrigger()  
                    .withIdentity(jobName, TRIGGER_GROUP_NAME)  
                            .withSchedule(scheduleBuilder).build();  
  
            //交给scheduler去调度  
            sched.scheduleJob(job, trigger);  
            i++;
            // 启动    
            System.out.println(sched.isShutdown());
            if (sched.isShutdown()) {    
                sched.start();    
                
            }
            return MessageHelper.wrap("result",true,"message","创建job成功");
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }finally{
        	if(i == 0){
        		return MessageHelper.wrap("result",false,"message","cron表达式可能永远不会执行，创建job失败");
        	}
        }    
    }    
    /** 
     * @Description: 添加一个定时任务  
     * @param jobName 任务名 
     * @param jobGroupName 任务组名 
     * @param triggerName 触发器名  
     * @param triggerGroupName 触发器组名 
     * @param jobClass 任务 
     * @param time 时间设置，参考quartz说明文档 
     */  
    @SuppressWarnings("unchecked")    
    public static void addJob(String jobName, String jobGroupName,    
            String triggerName, String triggerGroupName, Class jobClass,    
            String time) {    
        try {    
            Scheduler sched = gSchedulerFactory.getScheduler();    
            JobDetail job = newJob(jobClass)  
                    .withIdentity(jobName, jobGroupName)  
                    .build();  
            // 表达式调度构建器  
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time);  
            // 按新的cronExpression表达式构建一个新的trigger  
            Trigger trigger = TriggerBuilder  
                    .newTrigger()  
                    .withIdentity(triggerName, triggerGroupName)  
                            .withSchedule(scheduleBuilder).build();  
            sched.scheduleJob(job, trigger);   
            // 启动    
            if (sched.isShutdown()) {    
                sched.start();    
            }    
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }    
    }    
    
    /** 
     * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)  
     * @param jobName 
     * @param time 
     */  
    @SuppressWarnings("unchecked")    
    public static String modifyJobTime(String jobName, String time) {   
        TriggerKey triggerKey = TriggerKey.triggerKey(  
                jobName, TRIGGER_GROUP_NAME); 
        //i为标志是否有异常
    	int i = 0;  
        try {
            Scheduler sched = gSchedulerFactory.getScheduler();    
            CronTrigger trigger =(CronTrigger) sched.getTrigger(triggerKey);  
            if (trigger == null) {   
            	i++;
                return MessageHelper.wrap("result",false,"message","job不存在");  
            }    
            String oldTime = trigger.getCronExpression();    
            if (!oldTime.equalsIgnoreCase(time)) {  
                CronScheduleBuilder scheduleBuilder =CronScheduleBuilder.cronSchedule(time);  
                //按新的cronExpression表达式重新构建trigger  
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)  
                .withSchedule(scheduleBuilder).build();  
                //按新的trigger重新设置job执行  
                sched.rescheduleJob(triggerKey, trigger);  
            }  
            i++;
            return MessageHelper.wrap("result",true,"message","job更新成功");
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        } finally{
        	if(i == 0){
        		return MessageHelper.wrap("result",false,"message","job不会再执行，已清理");
        	}
        }   
    }    
    
    /** 
     * @Description: 修改一个任务的触发时间  
     * @param triggerName 
     * @param triggerGroupName 
     * @param time 
     */  
    public static void modifyJobTime(String triggerName,    
            String triggerGroupName, String time) {   
        TriggerKey triggerKey = TriggerKey.triggerKey(  
                triggerName, triggerGroupName);  
        try {    
            Scheduler sched = gSchedulerFactory.getScheduler();    
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);    
            if (trigger == null) {    
                return;    
            }    
            String oldTime = trigger.getCronExpression();    
            if (!oldTime.equalsIgnoreCase(time)) {    
                // trigger已存在，则更新相应的定时设置  
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder  
              .cronSchedule(time);  
                // 按新的cronExpression表达式重新构建trigger  
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)  
                        .withSchedule(scheduleBuilder).build();  
                // 按新的trigger重新设置job执行  
                sched.resumeTrigger(triggerKey);  
            }    
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }    
    }    
    
    /** 
     * @Description 移除一个任务(使用默认的任务组名，触发器名，触发器组名) 
     * @param jobName 
     */  
    public static void removeJob(String jobName) {   
        TriggerKey triggerKey = TriggerKey.triggerKey(  
                jobName, TRIGGER_GROUP_NAME);  
        JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);  
        try {    
            Scheduler sched = gSchedulerFactory.getScheduler();  
            Trigger trigger = (Trigger) sched.getTrigger(triggerKey);    
            if (trigger == null) {    
                return;    
            }  
            sched.pauseTrigger(triggerKey);;// 停止触发器    
            sched.unscheduleJob(triggerKey);// 移除触发器    
            sched.deleteJob(jobKey);// 删除任务    
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }    
    }    
    
    /**  
     * @Description: 移除一个任务  
     * @param jobName  
     * @param jobGroupName  
     * @param triggerName  
     * @param triggerGroupName  
     */    
    public static void removeJob(String jobName, String jobGroupName,    
            String triggerName, String triggerGroupName) {   
        TriggerKey triggerKey = TriggerKey.triggerKey(  
                jobName, triggerGroupName);  
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);  
        try {    
            Scheduler sched = gSchedulerFactory.getScheduler();   
            sched.pauseTrigger(triggerKey);// 停止触发器    
            sched.unscheduleJob(triggerKey);// 移除触发器    
            sched.deleteJob(jobKey);// 删除任务  
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }    
    }   
    /** 
     * @Description:暂停一个任务 
     * @param jobName 
     * @param jobGroupName 
     */  
    public static void pauseJob(String jobName, String jobGroupName) {  
        JobKey jobKey =JobKey.jobKey(jobName, jobName);  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();  
            sched.pauseJob(jobKey);  
        } catch (SchedulerException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
    /** 
     * @Description:暂停一个任务(使用默认组名) 
     * @param jobName 
     * @param jobGroupName 
     */  
    public static void pauseJob(String jobName) {  
        JobKey jobKey =JobKey.jobKey(jobName, JOB_GROUP_NAME);  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();  
            sched.pauseJob(jobKey);  
        } catch (SchedulerException e) {  
            e.printStackTrace();  
        }  
    }  
    /**  
     * @Description:启动所有定时任务  
     */  
    public static void startJobs() {    
    	
        try {    
            Scheduler sched = gSchedulerFactory.getScheduler();    
            sched.start();    
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }    
    }    
    
    /**  
     * @Description:启动一个定时任务  
     */  
    public static void startJob(String jobName) {    
    	JobKey jobKey =JobKey.jobKey(jobName, JOB_GROUP_NAME);  
        try {    
            Scheduler sched = gSchedulerFactory.getScheduler();    
            sched.resumeJob(jobKey);
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }    
    } 
    
  
    /** 
     * @Description 关闭所有定时任务  
     */  
    public static void shutdownJobs() {    
        try {    
            Scheduler sched = gSchedulerFactory.getScheduler();    
            if (!sched.isShutdown()) {    
                sched.shutdown();    
            }    
        } catch (Exception e) {    
            throw new RuntimeException(e);    
        }    
    }   
}
