package com.dc.appengine.appmaster.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.dc.appengine.appmaster.entity.Element;

public class TreeNode {
	Element e;
	public TreeNode(Element e){
		this.e=e;
	}
	List<TreeNode> children=new ArrayList<TreeNode>();
	TreeNode parent=null;
	public List<TreeNode> getChildren(){
		return this.children;
	}
	public int getChildrenSize(){
		return this.children.size();
	}
	public void add(TreeNode node){
		this.children.add(node);
		node.parent=this;
	}
	public TreeNode remove(TreeNode node){
		for(int i=0;i<this.children.size();i++){
			if(this.children.get(i).equals(node)){
				return this.children.remove(i);
			}
		}
		return null;
	}
	public Element getE(){
		return this.e;
	}
	public List<TreeNode> getParentList(){
		List<TreeNode> parents=new ArrayList<TreeNode>();
		TreeNode temp=this.parent;
		while(temp!=null){
			parents.add(temp);
			temp=temp.parent;
		}
		return parents;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		sb.append("{").append(String.valueOf(this.e.toString()+"\n\r["));
		for(int i=0;i<children.size();i++){
			sb.append(children.get(i).toString());
			if(i<children.size()-1){
				sb.append(",");
			}
		}
		sb.append("]").append("}");
		return sb.toString();
	}
	
}
