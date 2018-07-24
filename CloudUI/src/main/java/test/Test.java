package test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import com.alibaba.fastjson.JSON;
import com.dc.appengine.cloudui.ws.client.WSRestClient;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//获取插件列表
//		int pageSize=0;
//		int pageNum=0;
//		String pluginName="";
//		String result = WSRestClient.getFrameWebTarget().path("plugin").
//		path("listPlugins.wf").queryParam("pageSize", pageSize+"")
//		.queryParam("pageNum", pageNum+"")
//		.queryParam("pluginName", pluginName).
//		request().get(String.class);
//		System.out.println(result);
		
		//删除插件
//		String pluginName="start";
//		Form form = new Form();
//		form.param("pluginName", pluginName);
//		String result =  WSRestClient.getFrameWebTarget()
//				.path("plugin").path("deletePlugin.wf")
//				.request()
//				.post(Entity.entity(form,
//						MediaType.APPLICATION_FORM_URLENCODED_TYPE),
//						String.class);
//		System.out.println(result);
		
		
		//更新插件
		String pluginName="unzip";
		Form form = new Form();
		form.param("pluginName", pluginName);
		form.param("label", "");
		form.param("description", "");
		form.param("postAction", "");
		form.param("preAction", "");
		form.param("invoke", "");
		String result =  WSRestClient.getFrameWebTarget()
				.path("plugin").path("updatePlugin.wf")
				.request()
				.post(Entity.entity(form,
						MediaType.APPLICATION_FORM_URLENCODED_TYPE),
						String.class);
		System.out.println(result);
	}

}
