package com.dc.appengine.cloudui.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ComponentConvert {
	private int keyIndex=-1;
	private int replaceKeyIndex=-300000;
	private String modelData="modelData";
	private String linkDataArray="linkDataArray";
	private String nodeDataArray="nodeDataArray";
	
	public static void main(String...strings){
		try{
			test1(strings);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void test1(String[] strings) {
		// TODO Auto-generated method stub
		try{
			File f=new File("D:/testdata/componentflowinfo.json");
			StringBuilder sb=new StringBuilder();
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(f),"GBK"));
			String line=null;
			while((line=br.readLine())!=null){
				sb.append(line).append(System.lineSeparator());
			}
			System.out.println(sb.toString());
			System.out.println("========================");
			ComponentConvert tool=new ComponentConvert();
			System.out.println(tool.convertComFlow2Runtime(sb.toString()));
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public String convertComFlow2Runtime(String info) throws Exception{
		JSONObject rsObj=JSON.parseObject(info);
		validateEndLinks(rsObj);
		JSONObject bpObj=JSON.parseObject(info);
		JSONArray nodes=bpObj.getJSONArray(nodeDataArray);
		for(int i=0;i<nodes.size();i++){
			JSONObject node=nodes.getJSONObject(i);
			//插件
			if(node.getIntValue("flowcontroltype") == 9){
				rsObj=autoSetNode(rsObj,node);
			}
		}
		//处理开始节点的连线，防止工作流挂起
		dealWithStartNode(rsObj);
		rsObj = endAutoAddJoin(rsObj);
		//处理聚合节点的count
		dealWithJoinNode(rsObj);
		return rsObj.toJSONString();
	}
	
	private void dealWithJoinNode(JSONObject rsObj) {
		List<JSONObject> joinNodes = getNodesByType(rsObj, 5);
		for(JSONObject joinNode : joinNodes){
			int count = getLinkNumsByToNode(rsObj, joinNode.getIntValue("key"));
			joinNode.put("count", count);
		}
	}
	
	private JSONObject endAutoAddJoin(JSONObject rsObj) {
		JSONObject endNode = getNode(rsObj, 2);
		if(endNode != null){
			int endKey = endNode.getIntValue("key");
			List<JSONObject> toEndLinks = getEndLinks(rsObj, endKey);
			if(toEndLinks.size() > 1){
				//聚合节点
				JSONObject endJoin = new JSONObject();
				endJoin.put("key", --replaceKeyIndex);
				endJoin.put("count", toEndLinks.size());
				endJoin.put("eleType", "flowcontrol");
				endJoin.put("category", "gateway");
				endJoin.put("gatewayType", 2);
				endJoin.put("ins", 1);
				endJoin.put("flowcontroltype", 5);
				endJoin.put("text", "聚合");
				rsObj.getJSONArray("nodeDataArray").add(endJoin);
				for(JSONObject link : toEndLinks){
					//to为结束节点的线全部转到聚合节点上
					link.put("to", endJoin.getIntValue("key"));
				}
				//新增聚合节点到结束节点的连线
				JSONObject endLink = new JSONObject();
				endLink.put("from", replaceKeyIndex);
				endLink.put("to", endKey);
				rsObj.getJSONArray("linkDataArray").add(endLink);
			}
		}
		return rsObj;
	}
	
	private List<JSONObject> getNodesByType(JSONObject rsObj, int nodeType){
		List<JSONObject> list = new ArrayList<>();
		JSONArray nodes = rsObj.getJSONArray(nodeDataArray);
		for(int j=0; j<nodes.size(); j++){
			JSONObject node = nodes.getJSONObject(j);
			int flowcontroltype = node.getIntValue("flowcontroltype");
			if(nodeType == flowcontroltype){
				list.add(node);
			}
		}
		return list;
	}
	
	private int getLinkNumsByToNode(JSONObject rsObj, int key) {
		int num = 0;
		JSONArray links = rsObj.getJSONArray("linkDataArray");
		for(int i = 0; i < links.size(); i++){
			JSONObject link = links.getJSONObject(i);
			int to = link.getIntValue("to");
			if(to == key){
				num++;
			}
		}
		return num;
	}
	
	private List<JSONObject> getEndLinks(JSONObject rsObj, int endKey) {
		List<JSONObject> list = new ArrayList<>();
		JSONArray links = rsObj.getJSONArray("linkDataArray");
		for(int i = 0; i < links.size(); i++){
			JSONObject link = links.getJSONObject(i);
			int to = link.getIntValue("to");
			if(to == endKey){
				String text = link.getString("text");
				if("N".equalsIgnoreCase(text) || "false".equalsIgnoreCase(text)
						||"no".equalsIgnoreCase(text) || "Y".equalsIgnoreCase(text) 
						|| "true".equalsIgnoreCase(text) ||"yes".equalsIgnoreCase(text)){
					list.clear();
					break;
				}
				else{
					list.add(link);
				}
			}
		}
		return list;
	}

	private void validateEndLinks(JSONObject rsObj) throws Exception {
		// TODO Auto-generated method stub
		JSONObject end=null;
		JSONArray nodes=rsObj.getJSONArray(nodeDataArray);
		for(int i=0;i<nodes.size();i++){
			if(nodes.getJSONObject(i).getIntValue("flowcontroltype")==2){
				end=nodes.getJSONObject(i);
				break;
			}
		}
		if(end==null){
			throw new Exception("过程没有定义结束节点");	
		}else{
			JSONArray links=rsObj.getJSONArray(linkDataArray);
			for(int i=0;i<links.size();i++){
				if(links.getJSONObject(i).getIntValue("from")==end.getIntValue("key")){
					throw new Exception("过程结束点不能再画与其它节点的连线");	
				}
			}
		}
	}
	
	//from为开始节点的连线默认全部为all连线
	//如果开始节点直接分支判断，手动画分支节点，并指定条件判断变量和表达式，此转换不支持开始节点的true/false连线转换
	private void dealWithStartNode(JSONObject rsObj) {
		JSONObject start = getNode(rsObj,1);
		Integer startKey = start.getInteger("key");
		List<JSONObject> list = new ArrayList<JSONObject>();
		JSONArray links = rsObj.getJSONArray(linkDataArray);
		for(int i = 0; i < links.size(); i++){
			if(startKey == links.getJSONObject(i).getIntValue("from")){
				list.add(links.getJSONObject(i));
			}
		}
		for(JSONObject link : list){
			link.remove("text");
			link.remove("visible");
		}
	}

	/**
	 * 转换插件节点，添加开始件点的流程变量，添加插件配置中的gf_varible=pluginName+key
	 * 根据需要自动添加条件判断节点，配置条件判断节点的条件表达式，添加插件节点和条件判断节点的连线
	 * 重新整理条件连线的from变更为条件判断节点，或去掉all标签（参看设计文档《组件子流程优化设计.docx》）
	 * @param rsObj
	 * @param node
	 */
	private JSONObject autoSetNode(JSONObject rsObj, JSONObject node) {
		// TODO Auto-generated method stub
		JSONArray nodes=rsObj.getJSONArray(nodeDataArray);
		JSONArray links=rsObj.getJSONArray(linkDataArray);
		//默认true/false只能一条
		JSONObject trueLink=getLine(rsObj,node,"true");
		JSONObject falseLink=getLine(rsObj,node,"false");
		//all线可能多条
		List<JSONObject> allLines = getAllLine(rsObj, node);
		//不合法线可能多条
		List<JSONObject> illegalLines = getIllegalLine(rsObj,node);
		//条件是true/false的线，增加"单一判断"节点及其连线
		if(trueLink != null || falseLink != null){
			JSONObject pluginNode=getNode(rsObj,node);
			JSONObject pluginParams = pluginNode.getJSONObject("params");
			String gf_variable = null;
			if(pluginParams.getString("gf_variable") != null && !"".equals(pluginParams.getString("gf_variable").trim())){
				gf_variable = pluginParams.getString("gf_variable");
			}
			int key=replaceKeyIndex-node.getIntValue("key");
			String flowvarname=node.getString("pluginName")+"_"+key;
			//如果插件的gf_variable值不是空，则单一判断节点变量使用gf_variable的值；如果是空，使用内置的flowvarname
			String nodeinfo="{\"key\":"+key+",\"eleType\":\"flowcontrol\",\"category\":\"gateway\",\"text\":\"单一条件\",\"ins\":1,\"flowcontroltype\":8,\"gatewayType\":4,\"para\":\""
								+ ((gf_variable == null) ? flowvarname : gf_variable) + "\",\"expression\":\"=\\\"true\\\"\"}";
			System.out.println(nodeinfo);
			//自动关联gf_variable为 流程变量的名称
			if(gf_variable == null){
				pluginParams.put("gf_variable", flowvarname);
			}
			//加入all参数
			pluginParams.put("all", "false");
			//创建条件判断节点
			JSONObject condition=JSON.parseObject(nodeinfo);
			//创建插件和条件判断节点的连线
			JSONObject pConditionLink=JSON.parseObject("{\"from\":"+node.getIntValue("key")+",\"to\":"+key+"}");
			nodes.add(condition);
			links.add(pConditionLink);
			//自动在开始节点添加流程流程变量
			JSONObject start=getNode(rsObj,1);
			if(start.get("paras")==null){
				start.put("paras", new JSONObject());
			}
			if(gf_variable == null){
				start.getJSONObject("paras").put(flowvarname, "");
			}
			else{
				start.getJSONObject("paras").put(gf_variable, "");
			}
			//重新整理条件连线的from变更为条件判断节点
			if(trueLink!=null){
				trueLink.put("from", key);
				trueLink.remove("points");
			}
			if(falseLink!=null){
				falseLink.put("from", key);
				falseLink.remove("points");
			}
			return rsObj;
		}
		//条件是all的线，转为普通线
		else if(allLines.size() > 0){
			for(JSONObject allLine : allLines){
				allLine.remove("text");
				allLine.remove("visible");
			}
			//加入all参数、gf_variable参数不修改
			JSONObject pluginNode=getNode(rsObj,node);
			JSONObject params = pluginNode.getJSONObject("params");
			params.put("all", "true");
			return rsObj;
		}
		//没条件的线/条件为空的线/tm乱写条件的线，容错为普通线
		else if(illegalLines != null){
			for(JSONObject illegalLine : illegalLines){
				illegalLine.remove("text");
				illegalLine.remove("visible");
			}
			//加入all参数、gf_variable参数不修改
			JSONObject pluginNode=getNode(rsObj,node);
			JSONObject params = pluginNode.getJSONObject("params");
			params.put("all", "true");
			return rsObj;
		}
		//其他情况(应该不存在)，不处理
		else{
			return rsObj;
		}
	}
	private JSONObject getLine(JSONObject rsObj,
			JSONObject node,String label) {
		// TODO Auto-generated method stub
		JSONArray links=rsObj.getJSONArray(linkDataArray);
		for(int j=0;j<links.size();j++){
			if(node.getIntValue("key")==links.getJSONObject(j).getIntValue("from")&&label.equalsIgnoreCase(links.getJSONObject(j).getString("text"))){
				return links.getJSONObject(j);
			}
		}
		return null;
	}
	private JSONObject getNode(JSONObject rsObj,int flowcontroltype) {
		// TODO Auto-generated method stub
		JSONArray nodes=rsObj.getJSONArray(nodeDataArray);
		for(int j=0;j<nodes.size();j++){
			if(flowcontroltype==nodes.getJSONObject(j).getIntValue("flowcontroltype")){
				return nodes.getJSONObject(j);
			}
		}
		return null;
	}
	private JSONObject getNode(JSONObject rsObj,JSONObject node) {
		// TODO Auto-generated method stub
		JSONArray nodes=rsObj.getJSONArray(nodeDataArray);
		for(int j=0;j<nodes.size();j++){
			if(node.getIntValue("key")==nodes.getJSONObject(j).getIntValue("key")){
				return nodes.getJSONObject(j);
			}
		}
		return null;
	}
	/**
	 * 移除所有的节点和线的位置坐标
	 * @param rsObj
	 * @return
	 */
	private JSONObject clearP(
			JSONObject rsObj) {
		// TODO Auto-generated method stub
		JSONArray nodes=rsObj.getJSONArray("nodeDataArray");
		for(int i=0;i<nodes.size();i++){
			nodes.getJSONObject(i).remove("loc");
		}
		JSONArray links=rsObj.getJSONArray("linkDataArray");
		for(int i=0;i<links.size();i++){
			links.getJSONObject(i).remove("points");
		}
		return rsObj;
	}
	
	private List<JSONObject> getIllegalLine(JSONObject rsObj, JSONObject node) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		JSONArray links=rsObj.getJSONArray(linkDataArray);
		for(int j=0; j<links.size(); j++){
			if(node.getIntValue("key")==links.getJSONObject(j).getIntValue("from")){
				if(!("all".equalsIgnoreCase(links.getJSONObject(j).getString("text"))
						||"true".equalsIgnoreCase(links.getJSONObject(j).getString("text"))
						||"false".equalsIgnoreCase(links.getJSONObject(j).getString("text")))){
					list.add(links.getJSONObject(j));
				}
			}
		}
		return list;
	}
	
	private List<JSONObject> getAllLine(JSONObject rsObj, JSONObject node) {
		List<JSONObject> list = new ArrayList<JSONObject>();
		JSONArray links = rsObj.getJSONArray(linkDataArray);
		for(int j=0; j<links.size(); j++){
			if(node.getIntValue("key") == links.getJSONObject(j).getIntValue("from")){
				if("all".equalsIgnoreCase(links.getJSONObject(j).getString("text"))){
					list.add(links.getJSONObject(j));
				}
			}
		}
		return list;
	}
}
