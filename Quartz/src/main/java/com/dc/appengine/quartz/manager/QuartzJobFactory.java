package com.dc.appengine.quartz.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.dc.appengine.quartz.utils.MasterEnv;


public class QuartzJobFactory implements Job{

	/**
	 * 定时执行的动作
	 * 在execute方法中添加要执行的代码
	 */
//	private static final Logger log = Logger.getLogger("");
	private static final Logger log = LoggerFactory.getLogger(QuartzJobFactory.class);

    @Override  
    public void execute(JobExecutionContext context) throws JobExecutionException {  
          
        log.info("任务运行开始-------- start --------");   
        try {  
            //ScheduleJob任务运行时具体参数，可自定义  
            ScheduleJob scheduleJob =(ScheduleJob) context.getMergedJobDataMap().get(  
                    "scheduleJob"); 
            //获取执行体消息
            Map<String,Object> excuteInfo = scheduleJob.getExcuteInfo();
            String flowName = excuteInfo.get("flowName").toString();//暂不用
            String flowId = excuteInfo.get("flowId").toString();
            String instanceId = excuteInfo.get("instanceId").toString();//暂不用
            //调用真正的执行接口,由于所有的流程执行使用同一个入口卡接口，realExcute（）方法暂不用
            //String result = realExcute(flowName, flowId, instanceId);
            RestTemplate restUtil = new RestTemplate();
//			String query = "flowId="+flowId;
			Map<String, String> param = new HashMap<>();
			param.put("cdFlowId", flowId);
			param.put("blueprintInstanceId", instanceId);
			String query = this.paramUtil(param);
			String result = restUtil.getForObject(MasterEnv.MASTER_REST+"/blueprint/executeBlueprintFlow?"+query, String.class);
			log.info(result);
            System.out.println("excute:" + scheduleJob.getJobDesc()); 
        }catch (Exception e) {  
            log.info("捕获异常===", e);  
        }  
        log.info("任务运行结束-------- end --------");   
    }  
    
    private String paramUtil(Map<String, String> param) {
    	Set<String> keys = param.keySet();
    	StringBuilder sb = new StringBuilder();
    	for (String key : keys) {
			sb.append("&").append(key).append("=").append(param.get(key));
		}
    	return sb.toString().substring(1);
    }
    
}
