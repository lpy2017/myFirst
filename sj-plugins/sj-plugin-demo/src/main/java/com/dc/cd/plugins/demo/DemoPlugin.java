package com.dc.cd.plugins.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dc.bd.plugin.BaseAgentPlugin;
import com.dc.bd.plugin.JobDetailDto;
import com.dc.bd.plugin.JobExecResultDto;

public class DemoPlugin extends BaseAgentPlugin {
	
	private static final Logger log = LoggerFactory.getLogger(DemoPlugin.class);
	
	public JobExecResultDto execute(JobDetailDto detailDTO) {
		log.info("demoplugin receive job");
		log.info("action:{}", detailDTO.getAction());
		log.info("deviceIP:{}", detailDTO.getDeviceIp());
		log.info("JobDetailParam:{}", detailDTO.getJobDetailParam());
		log.info("Encode:{}", detailDTO.getEncode());
		log.info("jobid:{}", detailDTO.getJobId());
		log.info("JobInstId:{}", detailDTO.getJobInstId());
		log.info("jobname:{}", detailDTO.getJobName());
		log.info("NodeGrpId:{}", detailDTO.getNodeGrpId());
		log.info("nodeid:{}", detailDTO.getNodeId());
		log.info("PluginCode:{}", detailDTO.getPluginCode());
		log.info("ProtocolList:{}", detailDTO.getProtocolList());
		log.info("Timeout:{}", detailDTO.getTimeout());
		log.info("TypeCode:{}", detailDTO.getTypeCode());
		log.info("uuid:{}", detailDTO.getUuid());
		/*
		 * 执行作业的代码省略
		 */
		// 创建作业执行结果
		JobExecResultDto execResultDto = new JobExecResultDto(detailDTO);
		execResultDto.setCode("100");
		execResultDto.setUuid(detailDTO.getUuid());
		execResultDto.setSuccess(true);
		execResultDto.setMsg(detailDTO.getJobDetailParam());
		execResultDto.setData("demoplugin_作业执行数据信息(直接返回接收的消息):\n" + detailDTO.getJobDetailParam());
		return execResultDto;
	}

}
