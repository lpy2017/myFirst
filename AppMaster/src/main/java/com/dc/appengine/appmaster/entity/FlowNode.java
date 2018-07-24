package com.dc.appengine.appmaster.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/*
 * 流程节点
 */
public class FlowNode {
	//节点状态
	public Integer state;
	public String startTime; //开始时间
	public String endTime; //结束时间
	public String name; // 节点名称
	public String id; // 
	public String flowId; // 流程id
	public String parentFlowId; // 父流程id
	public String parentInstanceId; // 父流程实例id
	public String instanceId;// 实例id
	public String activityId; // 节点id
	public String type; // 节点类型
	public String nodeComment; // 节点唯一标识
	public Boolean rootNode;// 是否是根节点
	public Boolean meddleError=false;// 是否需要干预
	/**
	 * 子节点
	 */
	private List children = new ArrayList();

	// 子节点排序
	public void sortChildren() {
		if (children != null && children.size() != 0) {
			Collections.sort(children, new NodeIDComparator());
			for (Iterator it = children.iterator(); it.hasNext();) {
				((FlowNode) it.next()).sortChildren();
			}
		}
	}
	//设置状态
	public void setState() {
		if (children != null && children.size() != 0) {
			for (Iterator it = children.iterator(); it.hasNext();) {
				FlowNode node = (FlowNode) it.next();
				node.setState();
				Integer childrenState = node.getState();
				if (childrenState == 7) {
					this.state = childrenState;
				}
			}
		}
	}
	
	public void setMeddleError() {
		if (children != null && children.size() != 0) {
			if ("0".equals(this.type) && this.state == 7) {
				for (Iterator it = children.iterator(); it.hasNext();) {
					FlowNode node = (FlowNode) it.next();
					node.setMeddleError();
					String type = node.type;
					int state = node.state;
					if("1".equals(type) && state == 2 && children.size() > 1){
						FlowNode nodeChild = (FlowNode)children.get(1);
						if("9".equals(nodeChild.getType())){
							node.setMeddleError(true);
						}
					}else if ("1".equals(type) && state == 7) {
						node.setMeddleError(true);
					}
					if( "9".equals(type)){
						node.setMeddleError(true);
					}
				}
			}
			for (Iterator it = children.iterator(); it.hasNext();) {
				FlowNode node = (FlowNode) it.next();
				node.setMeddleError();
			}
		}
	}
	public void addChild(FlowNode node) {
		this.children.add(node);
	}


	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFlowId() {
		return flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNodeComment() {
		return nodeComment;
	}

	public void setNodeComment(String nodeComment) {
		this.nodeComment = nodeComment;
	}

	public Boolean getRootNode() {
		return rootNode;
	}

	public void setRootNode(Boolean rootNode) {
		this.rootNode = rootNode;
	}

	public List getChildren() {
		return children;
	}

	public void setChildren(List children) {
		this.children = children;
	}
	public String getParentFlowId() {
		return parentFlowId;
	}
	public void setParentFlowId(String parentFlowId) {
		this.parentFlowId = parentFlowId;
	}
	public Boolean getMeddleError() {
		return meddleError;
	}
	public void setMeddleError(Boolean meddleError) {
		this.meddleError = meddleError;
	}
	public String getParentInstanceId() {
		return parentInstanceId;
	}
	public void setParentInstanceId(String parentInstanceId) {
		this.parentInstanceId = parentInstanceId;
	}
	
	
}

class NodeIDComparator implements Comparator {
	 public int compare(Object o1, Object o2) {
		 String j1=((FlowNode)o1).id;
		 String j2=((FlowNode)o2).id;
		 int result = j1.compareTo(j2);
		 return result;
	 }
}
