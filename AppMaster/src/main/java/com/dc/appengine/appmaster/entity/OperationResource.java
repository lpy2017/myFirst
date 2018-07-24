package com.dc.appengine.appmaster.entity;

import java.util.List;
import java.util.Map;

/**
 * 运维云资源实体
 * 
 * @author xuxyc
 *
 */
public class OperationResource {
	private String image;
	private int accessPort;
	private List<Map<String, Object>> volumes;
	private Map<String, String> portMappings;
	private String logDir;
	private String cmd;
	private Map<String, String> imageTag;
	private Long vesionId;

	public Long getVesionId() {
		return vesionId;
	}

	public void setVesionId(Long vesionId) {
		this.vesionId = vesionId;
	}

	public Map<String, String> getImageTag() {
		return imageTag;
	}

	public void setImageTag(Map<String, String> imageTag) {
		this.imageTag = imageTag;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getAccessPort() {
		return accessPort;
	}

	public void setAccessPort(int accessPort) {
		this.accessPort = accessPort;
	}

	public List<Map<String, Object>> getVolumes() {
		return volumes;
	}

	public void setVolumes(List<Map<String, Object>> volumes) {
		this.volumes = volumes;
	}

	public Map<String, String> getPortMappings() {
		return portMappings;
	}

	public void setPortMappings(Map<String, String> portMappings) {
		this.portMappings = portMappings;
	}

	public String getLogDir() {
		return logDir;
	}

	public void setLogDir(String logDir) {
		this.logDir = logDir;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
}
