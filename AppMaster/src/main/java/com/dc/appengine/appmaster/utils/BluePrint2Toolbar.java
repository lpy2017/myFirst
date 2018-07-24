package com.dc.appengine.appmaster.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class BluePrint2Toolbar {
	private static final String JSONObject = null;
	private int keyIndex=-1;
	private int replaceKeyIndex=-500000;
	private String modelData="modelData";
	private String linkDataArray="linkDataArray";
	private String nodeDataArray="nodeDataArray";
	public static void main(String...strings){
		main1();
//		main2();
	}
	public static void main1(String...strings){
		try{
			File f=new File("D:/testdata/errordata.json");
			StringBuilder sb=new StringBuilder();
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(f),"utf-8"));
			String line=null;
			while((line=br.readLine())!=null){
				sb.append(line).append(System.lineSeparator());
			}
			System.out.println(sb.toString());
			System.out.println("========================");
			BluePrint2Toolbar tool=new BluePrint2Toolbar();
			System.out.println(tool.convert(sb.toString()));
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main2(String...strings){
		try{
			File f=new File("D:/testdata/errordata.json");
			StringBuilder sb=new StringBuilder();
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String line=null;
			while((line=br.readLine())!=null){
				sb.append(line).append(System.lineSeparator());
			}
			System.out.println(sb.toString());
			System.out.println("========================");
			BluePrint2Toolbar tool=new BluePrint2Toolbar();
			System.out.println(tool.convertRuntimeInfo(sb.toString()));
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 根据蓝图信息生成蓝图流程左边工具栏的内容
	 * @param bpInfo
	 * @return
	 */
	public String convert(String bpInfo){
		JSONObject rsObj=JSON.parseObject(bpInfo);
		rsObj.remove(modelData);
		rsObj.remove(linkDataArray);
		rsObj.remove(nodeDataArray);
		rsObj.put(nodeDataArray, new JSONArray());
		JSONObject bpObj=JSON.parseObject(bpInfo);
		JSONArray nodes=bpObj.getJSONArray(nodeDataArray);
		for(int i=0;i<nodes.size();i++){
			JSONObject node=nodes.getJSONObject(i);
			JSONObject nodeCopy=JSON.parseObject(node.toJSONString());
			if("component".equals(node.getString("eleType"))){
				JSONObject parent=copyNodeAsNewKey(getParent(nodes,node));
				//System.out.println(parent.get("oldkey"));
				//System.out.println(parent);
				if(parent!=null){
					rsObj.getJSONArray(nodeDataArray).add(parent);
					nodeCopy.put("group", parent.getString("key"));
					nodeCopy.remove("isGroup");
					rsObj.getJSONArray(nodeDataArray).add(nodeCopy);
				}
			}
		}
		return rsObj.toJSONString();
	}
	/**
	 * 根据组件找出组件的根资源池节点
	 * @param nodes
	 * @param node
	 * @return
	 */
	public JSONObject getParent(JSONArray nodes,JSONObject node){
		for(int i=0;i<nodes.size();i++){
			//System.out.println("node["+i+"] key:"+nodes.getJSONObject(i).getIntValue("key")+"==node group:"+node.getIntValue("group")+"  node[key]:"+node.getIntValue("key"));
			if(nodes.getJSONObject(i).getIntValue("key")==node.getIntValue("group")){
				if("component".equals(nodes.getJSONObject(i).getString("eleType")))
					return getParent(nodes,nodes.getJSONObject(i));
				else if("resource".equals(nodes.getJSONObject(i).getString("eleType")))
					return nodes.getJSONObject(i);
			}
		}
		return null;
	}
	/**
	 * 生成新资源池节点
	 * @param node
	 * @return
	 */
	public JSONObject copyNodeAsNewKey(JSONObject node){
		JSONObject bpObj=JSON.parseObject(node.toJSONString());
		bpObj.put("oldkey", bpObj.getString("key"));
		bpObj.put("key", keyIndex++);
		return bpObj;
	}
	
	/**
	 * 根据组节点的中间态装换成静态资源池+聚合节点的模式
	 * @param bpInfo
	 * @return
	 * @throws Exception 
	 */
	String loopCount="{"
			+"	\"path\": \"\","
			+"	\"agent\": \"com.dc.appengine.plugins.service.impl.LoopCount#doActive\","
			+"	\"fileName\": \"\","
			+"	\"createTime\": \"2017-07-25 12:00:00\","
			+"	\"pluginName\": \"LoopCount\","
			+"	\"description\": \"loop count ++/--\","
			+"	\"preAction\": \"com.dc.appengine.plugins.service.impl.LoopCount#preAction\","
			+"	\"invoke\": \"com.dc.appengine.plugins.service.impl.LoopCount#invoke\","
			+"	\"postAction\": \"com.dc.appengine.plugins.service.impl.LoopCount#postAction\","
			+"	\"label\": {"
			+"		"
			+"	},"
			+"	\"params\": {"
			+"		\"counter_variable\": \"i_-14\","
			+"		\"counter_operation\": \"+1\""
			+"	},"
			+"	\"category\": \"activity\","
			+"	\"condition\": true,"
			+"	\"flowcontroltype\": 9,"
			+"	\"text\": \"LoopCount\","
			+"	\"textKey\": \"LoopCount\","
			+"	\"taskType\": 8,"
			+"	\"eleType\": \"plugin\","
			+"	\"color\": \"#9ef5ee\","
			+"	\"ins\": 1,"
			+"	\"key\": -14"
			+"}";
	String ifcondition="{"
			+"	\"key\": 204,"
			+"	\"eleType\": \"flowcontrol\","
			+"	\"category\": \"gateway\","
			+"	\"text\": \"单一条件\","
			+"	\"textKey\": \"单一条件\","
			+"	\"customtype\": \"condition\","
			+"	\"ins\": 1,"
			+"	\"flowcontroltype\": 8,"
			+"	\"gatewayType\": 4,"
			+"	\"loc\": \"559.15625 421\","
			+"	\"para\": \"i_-14\","
			+"	\"expression\": \"<5\""
			+"}";
	public String convertRuntimeInfo(String bpInfo) throws Exception{
		JSONObject rsObj=JSON.parseObject(bpInfo);
		validateEndLinks(rsObj);
		JSONArray nodes=rsObj.getJSONArray(nodeDataArray);
		//手动添加的聚合节点计算其count
		for(int i=0;i<nodes.size();i++){
			JSONObject node=nodes.getJSONObject(i);
			if(node.get("flowcontroltype")!=null&&node.getIntValue("flowcontroltype")==5){//聚合节点
				rsObj=setCount(rsObj,node);
			}
		}
		for(int i=0;i<nodes.size();i++){
			JSONObject node=nodes.getJSONObject(i);
			//处理组件点
			if(node.get("isGroup")!=null&&true==node.getBooleanValue("isGroup")){
				if("mutlic".equals(node.getString("grouptype"))){
					//添加静态资源层节点，添加聚合节点，并设置节点连线
					JSONObject pNode=JSON.parseObject("{\"key\":"+(--replaceKeyIndex)+",\"oldkey\":"+node.getIntValue("oldkey")+",\"groupkey\":"+node.getIntValue("key")+",\"eleType\":\"flowcontrol\",\"category\":\"subflow\",\"text\":\"[静态资源池]\",\"ins\":1,\"flowcontroltype\":10,\"item\":\"service task\",\"taskType\":6}");
					//自动添加的聚合节点无count
					JSONObject juheNode=JSON.parseObject("{\"key\":"+(--replaceKeyIndex)+",\"oldkey\":"+node.getIntValue("oldkey")+",\"eleType\":\"flowcontrol\",\"category\":\"gateway\",\"gatewayType\":2,\"ins\":1,\"flowcontroltype\":5,\"text\":\"聚合\"}");
					rsObj.getJSONArray(nodeDataArray).add(pNode);
					rsObj.getJSONArray(nodeDataArray).add(juheNode);
					node.put("pceng", pNode);
					node.put("juhe", juheNode);
					rsObj=connectBeginAndEnd(pNode,juheNode,rsObj,node);//  |````````````````````|true
				}else if("loop".equals(node.getString("grouptype"))){//     V                    |
					//添加静态资源层节点，添加轮询计数插件节点和条件判断节点，并设置节点连线   p--T---W--loopcountplugin--if(loopcount<ins)----false--->
					JSONObject pNode=JSON.parseObject("{\"key\":"+(--replaceKeyIndex)+",\"groupkey\":"+node.getIntValue("key")+",\"eleType\":\"flowcontrol\",\"category\":\"subflow\",\"text\":\"[静态资源池]\",\"ins\":1,\"count\":1,\"flowcontroltype\":10,\"item\":\"service task\",\"taskType\":6}");
					JSONObject loopCountNode=JSON.parseObject(loopCount);
					loopCountNode.put("key", (--replaceKeyIndex));
					JSONObject ifconditionNode=JSON.parseObject(ifcondition);
					ifconditionNode.put("key", (--replaceKeyIndex));
					//自动添加的计数插件节点和条件判断节点
					rsObj.getJSONArray(nodeDataArray).add(pNode);
					rsObj.getJSONArray(nodeDataArray).add(loopCountNode);
					rsObj.getJSONArray(nodeDataArray).add(ifconditionNode);
					node.put("pceng", pNode);
					node.put("juhe", ifconditionNode);
					//设置循环变量，配置循环插件，设置条件判断节点的判断条件
					String varName="i_"+node.getString("key");
					rsObj=addStartVar(rsObj,node,varName);
					loopCountNode.getJSONObject("params").put("counter_variable", varName);
					loopCountNode.getJSONObject("params").put("counter_operation", "+1");
					ifconditionNode.put("para", varName);
					ifconditionNode.put("expression", "<?");
					ifconditionNode.put("oldkey", node.getIntValue("oldkey"));
					//设置条件连线
					rsObj=connectBeginAndEnd(pNode,loopCountNode,ifconditionNode,rsObj,node);
				}
			}
		}
		
		JSONArray links=rsObj.getJSONArray(linkDataArray);
		//修改原始组节点的连线
		for(int i=0;i<links.size();i++){
			JSONObject link=links.getJSONObject(i);
			JSONObject from=getFromNode(rsObj,link);
			JSONObject to=getToNode(rsObj,link);
			if(from.get("isGroup")!=null&&true==from.getBooleanValue("isGroup")){
				link.put("from", from.getJSONObject("juhe").getIntValue("key"));
			}
			if(to.get("isGroup")!=null&&true==to.getBooleanValue("isGroup")){
				link.put("to", to.getJSONObject("pceng").getIntValue("key"));
			}
		}
		nodes=rsObj.getJSONArray(nodeDataArray);
		//移除组节点
		for(int i=0;i<nodes.size();i++){
			JSONObject node=nodes.getJSONObject(i);
			if(node.get("isGroup")!=null&&true==node.getBooleanValue("isGroup")){
				node.remove("pceng");
				node.remove("juhe");
				nodes.remove(i);
			}
		}
		//结束节点前如果没有单一判断节点且有多条线to结束节点，则结束节点前自动加入聚合节点
		rsObj = endAutoAddJoin(rsObj);
		rsObj=clearP(rsObj);
		return rsObj.toJSONString();
	}
	private com.alibaba.fastjson.JSONObject addStartVar(
			com.alibaba.fastjson.JSONObject rsObj,
			com.alibaba.fastjson.JSONObject nodeP, String varName) {
		// TODO Auto-generated method stub
		JSONArray nodes=rsObj.getJSONArray(nodeDataArray);
		//手动添加的聚合节点计算其count
		for(int i=0;i<nodes.size();i++){
			JSONObject node=nodes.getJSONObject(i);
			if(node.get("flowcontroltype")!=null&&node.getIntValue("flowcontroltype")==1){//聚合节点
				if(!node.containsKey("paras")){
					node.put("paras", new JSONObject());
				}
				node.getJSONObject("paras").put(varName, "0");
				break;
			}
		}
		return rsObj;
	}
	private JSONObject connectBeginAndEnd(
			JSONObject pNode,
			JSONObject loopCountNode,
			JSONObject ifconditionNode,
			JSONObject rsObj,
			JSONObject node) throws Exception {
		// TODO Auto-generated method stub
		//添加开始节点和组内的最左边的节点集合的连线
		//添加组内最右边节点集合到结束节点的连线
		JSONObject linkif2pnext_false=getfromLinkByNode(rsObj,node);
		linkif2pnext_false.put("visible", true);
		linkif2pnext_false.put("text", false);
		JSONObject linkloop2if=JSON.parseObject("{\"from\":"+loopCountNode.getIntValue("key")+",\"to\":"+ifconditionNode.getIntValue("key")+"}");
		JSONObject linkif2p_yes=JSON.parseObject("{\"from\":"+ifconditionNode.getIntValue("key")+",\"to\":"+pNode.getIntValue("key")+"}");
		linkif2p_yes.put("visible", true);
		linkif2p_yes.put("text", true);
		rsObj.getJSONArray("linkDataArray").add(linkloop2if);
		rsObj.getJSONArray("linkDataArray").add(linkif2p_yes);
		List<JSONObject> pRightNodes=getGroupFirstNode(rsObj, node);
		List<JSONObject> juheLeftNodes=getGroupLastNode(rsObj, node);
		for(int i=0;i<pRightNodes.size();i++){
			JSONObject pRightNode=pRightNodes.get(i);
			JSONObject pLink=JSON.parseObject("{\"from\":"+pNode.getIntValue("key")+",\"to\":"+pRightNode.getIntValue("key")+"}");
			rsObj.getJSONArray("linkDataArray").add(pLink);
		}
		if(juheLeftNodes.size()==1){
			JSONObject juheLiftNode=juheLeftNodes.get(0);
			JSONObject juheLink=JSON.parseObject("{\"from\":"+juheLiftNode.getIntValue("key")+",\"to\":"+loopCountNode.getIntValue("key")+"}");
			rsObj.getJSONArray("linkDataArray").add(juheLink);
		}else{
			/*暂时去掉自动生成聚合节点的能力，手动画节点
			 * 
			 * 
			//添加组内的开始节点和结束（聚合）节点
			JSONObject groupEnd=JSON.parseObject("{\"key\":"+(--replaceKeyIndex)+",\"eleType\":\"flowcontrol\",\"category\":\"gateway\",\"gatewayType\":2,\"ins\":1,\"flowcontroltype\":5,\"text\":\"聚合\"}");
			rsObj.getJSONArray("nodeDataArray").add(groupEnd);
			//添加组开始节点和P层节点的连线
			//添加组结束节点和聚合节点的连线
			rsObj.getJSONArray("linkDataArray").add(JSON.parseObject("{\"from\":"+groupEnd.getIntValue("key")+",\"to\":"+juheNode.getIntValue("key")+"}"));
			groupEnd.put("count", juheLiftNodes.size());
			for(int i=0;i<juheLiftNodes.size();i++){
				JSONObject juheLiftNode=juheLiftNodes.get(i);
				JSONObject juheLink=JSON.parseObject("{\"from\":"+juheLiftNode.getIntValue("key")+",\"to\":"+groupEnd.getIntValue("key")+"}");
				rsObj.getJSONArray("linkDataArray").add(juheLink);
			}
			*/
			throw new Exception("请组内的多并发节点后添加聚合节点");
		}
		
		return rsObj;
	}
	private com.alibaba.fastjson.JSONObject getfromLinkByNode(
			com.alibaba.fastjson.JSONObject rsObj, com.alibaba.fastjson.JSONObject node) {
		// TODO Auto-generated method stub
		JSONArray links=rsObj.getJSONArray("linkDataArray");
		for(int i=0;i<links.size();i++){
			if(links.getJSONObject(i).getIntValue("from")==node.getIntValue("key")){
				return links.getJSONObject(i);
			}
		}
		return null;
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
	private com.alibaba.fastjson.JSONObject endAutoAddJoin(com.alibaba.fastjson.JSONObject rsObj) {
		JSONObject endNode = getEndNode(rsObj);
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
	private List<com.alibaba.fastjson.JSONObject> getEndLinks(com.alibaba.fastjson.JSONObject rsObj, int endKey) {
		List<JSONObject> list = new ArrayList<>();
		JSONArray links = rsObj.getJSONArray("linkDataArray");
		for(int i = 0; i < links.size(); i++){
			com.alibaba.fastjson.JSONObject link = links.getJSONObject(i);
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
	private com.alibaba.fastjson.JSONObject getEndNode(com.alibaba.fastjson.JSONObject rsObj) {
		JSONObject end = null;
		JSONArray nodes = rsObj.getJSONArray("nodeDataArray");
		for(int i = 0; i < nodes.size(); i++){
			com.alibaba.fastjson.JSONObject node = nodes.getJSONObject(i);
			if(node.getIntValue("flowcontroltype") == 2){
				end = nodes.getJSONObject(i);
				break;
			}
		}
		return end;
	}
	private com.alibaba.fastjson.JSONObject setCount(
			com.alibaba.fastjson.JSONObject rsObj,
			com.alibaba.fastjson.JSONObject node) {
		// TODO Auto-generated method stub
		JSONArray links=rsObj.getJSONArray("linkDataArray");
		int count=0;
		for(int i=0;i<links.size();i++){
			if(links.getJSONObject(i).getIntValue("to")==node.getIntValue("key")){
				count++;
			}
		}
		JSONArray nodes=rsObj.getJSONArray("nodeDataArray");
		for(int i=0;i<nodes.size();i++){
			if(nodes.getJSONObject(i).getIntValue("key")==node.getIntValue("key")){
				nodes.getJSONObject(i).put("count", count);
			}
		}
		return rsObj;
	}
	/**
	 * 把当前的组模式的节点替换成并发模式的静态资源池+聚合节点的模式并自动添加连线
	 * 模式有调整：组内最开始的节点集合直接连接到P层节点，组内结尾节点集合汇聚到聚合节点上
	 * @param rsObj
	 * @param node
	 * @return
	 * @throws Exception 
	 */
	private JSONObject connectBeginAndEnd(JSONObject pNode,JSONObject juheNode,JSONObject rsObj, JSONObject node) throws Exception {
		// TODO Auto-generated method stub
		//添加开始节点和组内的最左边的节点集合的连线
		//添加组内最右边节点集合到结束节点的连线
		List<JSONObject> pRightNodes=getGroupFirstNode(rsObj, node);
		List<JSONObject> juheLeftNodes=getGroupLastNode(rsObj, node);
		for(int i=0;i<pRightNodes.size();i++){
			JSONObject pRightNode=pRightNodes.get(i);
			JSONObject pLink=JSON.parseObject("{\"from\":"+pNode.getIntValue("key")+",\"to\":"+pRightNode.getIntValue("key")+"}");
			rsObj.getJSONArray("linkDataArray").add(pLink);
		}
		if(juheLeftNodes.size()==1){
			JSONObject juheLiftNode=juheLeftNodes.get(0);
			JSONObject juheLink=JSON.parseObject("{\"from\":"+juheLiftNode.getIntValue("key")+",\"to\":"+juheNode.getIntValue("key")+"}");
			rsObj.getJSONArray("linkDataArray").add(juheLink);
		}else{
			/*暂时去掉自动生成聚合节点的能力，手动画节点
			 * 
			 * 
			//添加组内的开始节点和结束（聚合）节点
			JSONObject groupEnd=JSON.parseObject("{\"key\":"+(--replaceKeyIndex)+",\"eleType\":\"flowcontrol\",\"category\":\"gateway\",\"gatewayType\":2,\"ins\":1,\"flowcontroltype\":5,\"text\":\"聚合\"}");
			rsObj.getJSONArray("nodeDataArray").add(groupEnd);
			//添加组开始节点和P层节点的连线
			//添加组结束节点和聚合节点的连线
			rsObj.getJSONArray("linkDataArray").add(JSON.parseObject("{\"from\":"+groupEnd.getIntValue("key")+",\"to\":"+juheNode.getIntValue("key")+"}"));
			groupEnd.put("count", juheLiftNodes.size());
			for(int i=0;i<juheLiftNodes.size();i++){
				JSONObject juheLiftNode=juheLiftNodes.get(i);
				JSONObject juheLink=JSON.parseObject("{\"from\":"+juheLiftNode.getIntValue("key")+",\"to\":"+groupEnd.getIntValue("key")+"}");
				rsObj.getJSONArray("linkDataArray").add(juheLink);
			}
			*/
			throw new Exception("请组内的多并发节点后添加聚合节点");
		}
		
		return rsObj;
	}
	private com.alibaba.fastjson.JSONObject removeNode(
			com.alibaba.fastjson.JSONObject rsObj,
			com.alibaba.fastjson.JSONObject node) {
		// TODO Auto-generated method stub
		JSONArray nodes=rsObj.getJSONArray("nodeDataArray");
		for(int i=0;i<nodes.size();i++){
			if(nodes.getJSONObject(i).getIntValue("key")==node.getIntValue("key")){
				nodes.remove(i);
				return rsObj;
			}
		}
		return rsObj;
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
	private JSONObject getToNode(JSONObject rsObj,
			JSONObject link) {
		// TODO Auto-generated method stub
		JSONArray nodes=rsObj.getJSONArray(nodeDataArray);
		for(int j=0;j<nodes.size();j++){
			if(nodes.getJSONObject(j).getIntValue("key")==link.getIntValue("to")){
				return nodes.getJSONObject(j);
			}
		}
		return null;
	}
	private JSONObject getFromNode(JSONObject rsObj,
			JSONObject link) {
		// TODO Auto-generated method stub
		JSONArray nodes=rsObj.getJSONArray(nodeDataArray);
		for(int j=0;j<nodes.size();j++){
			if(nodes.getJSONObject(j).getIntValue("key")==link.getIntValue("from")){
				return nodes.getJSONObject(j);
			}
		}
		return null;
	}
	private List<JSONObject> getGroupFirstNode(
			JSONObject rsObj, JSONObject node) {
		// TODO Auto-generated method stub
		List<JSONObject> rs=new ArrayList<JSONObject>();
		JSONArray nodes=rsObj.getJSONArray(nodeDataArray);
		for(int i=0;i<nodes.size();i++){
			if(nodes.getJSONObject(i).containsKey("group")&&nodes.getJSONObject(i).getIntValue("group")==node.getIntValue("key")){
				int nodeKey=nodes.getJSONObject(i).getIntValue("key");
				JSONArray links=rsObj.getJSONArray(linkDataArray);
				int to=0,from=0;
				for(int j=0;j<links.size();j++){
					if(links.getJSONObject(j).getIntValue("from")==nodeKey){
						from++;
					}
					if(links.getJSONObject(j).getIntValue("to")==nodeKey){
						to++;
					}
				}
				if(to==0){
					rs.add(nodes.getJSONObject(i));
				}
			}
		}
		return rs;
	}
	private List<JSONObject> getGroupLastNode(
			JSONObject rsObj, JSONObject node) {
		// TODO Auto-generated method stub
		List<JSONObject> rs=new ArrayList<JSONObject>();
		JSONArray nodes=rsObj.getJSONArray(nodeDataArray);
		for(int i=0;i<nodes.size();i++){
			if(nodes.getJSONObject(i).containsKey("group")&&nodes.getJSONObject(i).getIntValue("group")==node.getIntValue("key")){
				int nodeKey=nodes.getJSONObject(i).getIntValue("key");
				JSONArray links=rsObj.getJSONArray(linkDataArray);
				int to=0,from=0;
				for(int j=0;j<links.size();j++){
					if(links.getJSONObject(j).getIntValue("from")==nodeKey){
						from++;
					}
					if(links.getJSONObject(j).getIntValue("to")==nodeKey){
						to++;
					}
				}
				if(from==0){
					rs.add(nodes.getJSONObject(i));
				}
			}
		}
		return rs;
	}
	
	public String generateGroupInfoWithStartAndEnd(String bpInfo,String appName,String cdFlowId,String versionConfig,String subflowName){
		
		String bpInfoStr = this.convert(bpInfo);
		JSONObject jo = JSON.parseObject(bpInfoStr);
		JSONArray nodeDataArray = jo.getJSONArray("nodeDataArray");
		JSONArray newNodeDataArray = new JSONArray();
		String groupKey = "";
		for(int i=0;i<nodeDataArray.size();i++){
			JSONObject jsonObjet = nodeDataArray.getJSONObject(i);
			if("component".equals(jsonObjet.getString("eleType")) && appName.equals(jsonObjet.getString("text"))){
				jsonObjet.put("grouptype", "mutlic");
				jsonObjet.put("flowcontroltype", 0);
				jsonObjet.put("taskType", 0);
				jsonObjet.remove("category");
				jsonObjet.put("category", "custom");
				jsonObjet.remove("eleType");
				jsonObjet.put("flowcontrol", "flowcontrol");
				jsonObjet.put("textKey", appName);
				jsonObjet.put("cdFlowId", cdFlowId);
				jsonObjet.put("versionConfig", versionConfig);
				jsonObjet.put("subflowName", subflowName);
				jsonObjet.put("loc", "371 152");
				groupKey = jsonObjet.getString("group");
				newNodeDataArray.add(jsonObjet);
				break;
			}
		}
		for(int i=0;i<nodeDataArray.size();i++){
			JSONObject jsonObjet = nodeDataArray.getJSONObject(i);
			if("resource".equals(jsonObjet.getString("eleType")) && groupKey.equals(""+jsonObjet.getString("key"))){
				jsonObjet.put("flowcontroltype", 10);
				jsonObjet.put("isSubProcess", true);
				jsonObjet.put("grouptype", "mutlic");
				jsonObjet.put("taskType", 0);
				jsonObjet.put("textKey", "静态资源池");
				jsonObjet.remove("eleType");
				jsonObjet.put("flowcontrol", "flowcontrol");
				jsonObjet.remove("ins");
				jsonObjet.put("ins", 1);
				jsonObjet.remove("category");
				jsonObjet.put("category", "custom");
				jsonObjet.put("loc", "371 152");
				newNodeDataArray.add(jsonObjet);
				break;
			}
		}
		//追加start和end
		Map<String,Object> startMap = new HashMap();
		startMap.put("category", "event");
		startMap.put("customtype", "start");
		startMap.put("eleType", "flowcontrol");
		startMap.put("eventDimension", 1);
		startMap.put("eventType", 1);
		startMap.put("flowcontroltype", 1);
		startMap.put("ins", 1);
		startMap.put("item", "start");
		startMap.put("key", 101);
		startMap.put("loc", "176 147");
		startMap.put("text", "开始");
		startMap.put("textKey", "开始");
		
		Map<String,Object> endMap = new HashMap();
		endMap.put("category", "event");
		endMap.put("customtype", "end");
		endMap.put("eleType", "flowcontrol");
		endMap.put("eventDimension", 8);
		endMap.put("eventType", 1);
		endMap.put("flowcontroltype", 2);
		endMap.put("ins", 1);
		endMap.put("item", "End");
		endMap.put("key", 104);
		endMap.put("loc", "726 147");
		endMap.put("text", "结束");
		endMap.put("textKey", "结束");
		newNodeDataArray.add(startMap);
		newNodeDataArray.add(endMap);
		
		JSONObject jsonObject = new JSONObject();
		
		Map<String,String> modeDate = new HashMap();
		modeDate.put("position", "-117 -33");
		jsonObject.put("modelData", modeDate);
		
		jsonObject.put("class", "go.GraphLinksModel");
		jsonObject.put("issub", false);
		jsonObject.put("nodeDataArray", newNodeDataArray);
		
		List linkDataArrayList = new ArrayList();
		Map<String,Object> map1 = new HashMap();
		map1.put("from", 101);
		map1.put("to", Integer.parseInt(groupKey));
		JSONArray ja = new JSONArray();
		ja.add(197);
		ja.add(147);
		ja.add(207);
		ja.add(147);
		ja.add(249);
		ja.add(147);
		ja.add(249);
		ja.add(147);
		ja.add(290);
		ja.add(147);
		ja.add(310);
		ja.add(147);
		map1.put("points", ja);
		linkDataArrayList.add(map1);
		
		Map<String,Object> map2 = new HashMap();
		map2.put("from", Integer.parseInt(groupKey));
		map2.put("to", 104);
		JSONArray ja2 = new JSONArray();
		ja2.add(432);
		ja2.add(147);
		ja2.add(442);
		ja2.add(147);
		ja2.add(482);
		ja2.add(147);
		ja2.add(482);
		ja2.add(147);
		ja2.add(522);
		ja2.add(147);
		ja2.add(542);
		ja2.add(147);
		map2.put("points", ja2);
		linkDataArrayList.add(map2);
		jsonObject.put("linkDataArray", linkDataArrayList);
		
		return jsonObject.toJSONString();
	}
}
