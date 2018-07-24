package com.dc.appengine.node.configuration.model;


public class NodeEnvDefinition extends AbstractPropDefinition {
	// base
	private boolean isNewNode;
	private boolean isRegister;
	private String nodeName;
	private String nodeip;
	private RepositoryDefinition repository;

	public boolean isNewNode() {
		return isNewNode;
	}

	public void setNewNode(boolean isNewNode) {
		this.isNewNode = isNewNode;
	}

	public boolean isRegister() {
		return isRegister;
	}

	public void setRegister(boolean isRegister) {
		this.isRegister = isRegister;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeip() {
		return nodeip;
	}

	public void setNodeip(String nodeip) {
		this.nodeip = nodeip;
	}

	public RepositoryDefinition getRepository() {
		return repository;
	}

	public void setRepository(RepositoryDefinition repositoryDefinition) {
		this.repository = repositoryDefinition;
	}

}
