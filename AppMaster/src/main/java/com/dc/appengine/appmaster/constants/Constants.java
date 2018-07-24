package com.dc.appengine.appmaster.constants;

public final class Constants {
	public final class Monitor{
		/*节点状态码*/
		public static final int RUNNING = 0;//正在执行
		public static final int FAILED = 7;//失败
		public static final int SUCCESS = 2;//成功
		
		public static final String OverTime = "Y";//超时
		
		public static final String FINISHED = "已结束";
		public static final String UNFINISHED = "未结束";
		
		public static final int INSTANCE_RUNNING = 1;//正在执行
	}
	public final class Label{
		/*标签类型码*/
		public static final int CLUSTER = 1;//环境
		public static final int BLUEPRINT = 2;//蓝图
		public static final int COMPONENT = 3;//组件
		public static final int PLUGIN = 4;//插件
	}
	
	public final class audit4Resource{
		/*操作类型 ,发布、状态更新、追加描述、回退*/
		public static final String OP_NEW = "新建";
		public static final String OP_PUBLIC = "发布";
		public static final String OP_UPDATE = "更新";
		public static final String OP_ADD_DESCRIPTION = "追加描述";
		public static final String OP_BACK = "回退";
		public static final String OP_UPDATE_STATUS = "更新状态";
		public static final String OP_START = "启动";
		public static final String OP_STOP = "停止";
		public static final String OP_BATCH_RELEASE = "批量发布";
		
		/*状态类型,发布、状态更新、追加描述、回退*/
		public static final String STATUS_NEW = "00";//新建
		public static final String STATUS_TEST_PASS = "01";//测试通过
		public static final String STATUS_TEST_PASS_PART = "10";//部分测试通过
		public static final String STATUS_TEST_NOPASS = "11";//测试未通过
		public static final String STATUS_PUBLICED = "30";//发布中...
		public static final String STATUS_BACKED = "40";//回退中...
		public static final String STATUS_PUBLICED_SUCCESSFUL = "31";//发布成功
		public static final String STATUS_BACKED_SUCCESSFUL = "41";//回退成功
		public static final String STATUS_PUBLICED_FAILED = "32";//发布失败
		public static final String STATUS_BACKED_FAILED = "42";//回退失败
		public static final String STATUS_PUBLICED_FINISH = "33";//发布完成
		public static final String STATUS_BACKED_FINISH = "43";//回退完成
		public static final String STATUS_START = "50";//启动中...
		public static final String STATUS_STOP = "60";//停止中...
		public static final String STATUS_START_SUCCESSFUL = "51";//启动成功
		public static final String STATUS_STOP_SUCCESSFUL = "61";//停止成功
		public static final String STATUS_START_FAILED = "52";//启动失败
		public static final String STATUS_STOP_FAILED = "62";//停止失败
		public static final String STATUS_BATCH_RELEASE = "70";//批量发布中...
		public static final String STATUS_BATCH_RELEASE_SUCCESSFUL = "71";//批量发布成功
		public static final String STATUS_BATCH_RELEASE_FAILED = "72";//批量发布失败
		public static final String STATUS_BATCH_RELEASE_FINISH = "73";//批量完成
	}
	
	public final class Package{
		public static final String WORKPIECE = "workpiece";//工件标识
		public static final String COMPONET = "resource";//组件标识
		public static final String BLUEPRINT = "template";//工件标识
		public static final String WORKPIECEJSON = "workpiece.json";//工件的json文件名
		public static final String COMPONETJSON = "resource.json";//工件的json文件名
		public static final String BLUEPRINTJSON = "template.json";//工件的json文件名
	}
	
}
