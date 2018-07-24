package com.dc.appengine.appmaster.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class FormatUtil {

	
	 /**
     * 打印输入到控制台
     * 
     * @param jsonStr
     */
    public static void printJson(String jsonStr) {
        System.out.println(formatJson(jsonStr));
    }

    /**
     * 格式化
     * 
     * @param jsonStr
     * @return
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr))
            return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        char next = '\0';
        int indent = 0;
        boolean isInQuotationMarks = false;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            if(i+1 < jsonStr.length()){
            	next = jsonStr.charAt(i+1);
            }else{
            	next = current;
            }
            switch (current) {
            case '"':
                                if (last != '\\'){
                    isInQuotationMarks = !isInQuotationMarks;
                                }
                sb.append(current);
                break;
            case '{':
            case '[':
                sb.append(current);
                if (!isInQuotationMarks) {
                	if(('['==current&&'{'==next)||('{'==current&&'\"'==next)){
                		sb.append('\n');
                		indent++;
                		addIndentBlank(sb, indent);
                    }
                }
                break;
            case '}':
            case ']':
                if (!isInQuotationMarks) {
                	if((']'==current&&'}'==last)||('}'==current&&'\"'==last)){
                		sb.append('\n');
                		indent--;
                		addIndentBlank(sb, indent);
                	}
                }
                sb.append(current);
                break;
            case ',':
                sb.append(current);
                if (last != '\\' && !isInQuotationMarks) {
                	if(','==current&&('{'==next||'\"'==next)){
                		sb.append('\n');
                		addIndentBlank(sb, indent);
                	}
                }
                break;
            default:
                sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     * 
     * @param sb
     * @param indent
     * @author lizhgb
     * @Date 2015-10-14 上午10:38:04
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
    
    public static String printStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		try {
			t.printStackTrace(pw);
			return sw.toString();
		} finally {
			pw.close();
		}
	}
	
    public static String formSize(Long size) {
		DecimalFormat df = new DecimalFormat("###,###,###.##");
		int GB = 1024 * 1024 * 1024;// 定义GB的计算常量
		int MB = 1024 * 1024;// 定义MB的计算常量
		int KB = 1024;// 定义KB的计算常量
		String resultSize = "";
		if (size / GB >= 1) {
			// 如果当前Byte的值大于等于1GB
			resultSize = df.format(size / (float) GB) + "GB   ";
		} else if (size / MB >= 1) {
			// 如果当前Byte的值大于等于1MB
			resultSize = df.format(size / (float) MB) + "MB   ";
		} else if (size / KB >= 1) {
			// 如果当前Byte的值大于等于1KB
			resultSize = df.format(size / (float) KB) + "KB   ";
		} else {
			resultSize = size + "B   ";
		}
		return resultSize;
	}
	
	
	public static Long unFormSize(String resultSize) {
		DecimalFormat df = new DecimalFormat("###,###,###.##");
		int GB = 1024 * 1024 * 1024;// 定义GB的计算常量
		int MB = 1024 * 1024;// 定义MB的计算常量
		int KB = 1024;// 定义KB的计算常量
		Long size = 0L;
		Double d =null;
		if(resultSize.endsWith("GB")){
			resultSize=resultSize.substring(0, resultSize.indexOf("GB"));
			d=new Double((Float.valueOf(resultSize)*(float) GB));
			BigDecimal dec = new BigDecimal(d);
			size = dec.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
		}else if(resultSize.endsWith("MB")){
			resultSize=resultSize.substring(0, resultSize.indexOf("MB"));
			d=new Double((Float.valueOf(resultSize)*(float) MB));
			BigDecimal dec = new BigDecimal(d);
			size = dec.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();

		}else if(resultSize.endsWith("KB")){
			resultSize=resultSize.substring(0, resultSize.indexOf("KB"));
			d=new Double((Float.valueOf(resultSize)*(float) KB));
			BigDecimal dec = new BigDecimal(d);
			size = dec.setScale(0, BigDecimal.ROUND_HALF_UP).longValue();

		}else if(resultSize.endsWith("B")){
			size=Long.valueOf(resultSize.substring(0, resultSize.indexOf("B")));
		}
		return size;
	}
    
	public static void main(String[] args) throws IOException {
		String str = "preAction test = "+System.lineSeparator()+"{\"SaltConfig\":{\"agent\":\"com.dc.appengine.plugins.service.impl.SaltConfig#doActive\",\"createTime\":\"2017-04-24 13:51:24\",\"description\":\"SaltConfig package\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.SaltConfig#invoke\",\"label\":{\"os\":\"linux\",\"type\":\"docker\"},\"params\":{\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"gf_variable\":\"\"},\"path\":\"\",\"pluginName\":\"SaltConfig\",\"postAction\":\"com.dc.appengine.plugins.service.impl.SaltConfig#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.SaltConfig#preAction\"},\"config\":{\"agent\":\"com.dc.appengine.plugins.service.impl.Config#doActive\",\"createTime\":\"2017-04-10 14:53:24\",\"description\":\"config package\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.Config#invoke\",\"label\":{\"os\":\"linux\",\"type\":\"docker\"},\"params\":{\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"gf_variable\":\"\"},\"path\":\"\",\"pluginName\":\"config\",\"postAction\":\"com.dc.appengine.plugins.service.impl.Config#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.Config#preAction\"},\"destroy\":{\"agent\":\"com.dc.appengine.plugins.service.impl.Destroy#doActive\",\"createTime\":\"2017-04-10 14:53:24\",\"description\":\"destroy package\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.Destroy#invoke\",\"label\":{\"os\":\"linux\",\"type\":\"docker\"},\"params\":{\"destroyPath\":\"${destroyPath }\",\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"gf_variable\":\"\"},\"path\":\"\",\"pluginName\":\"destroy\",\"postAction\":\"com.dc.appengine.plugins.service.impl.Destroy#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.Destroy#preAction\"},\"download\":{\"agent\":\"com.dc.appengine.plugins.service.impl.Download#doActive\",\"createTime\":\"2017-04-10 14:53:24\",\"description\":\"download package\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.Download#invoke\",\"label\":{\"os\":\"linux\",\"type\":\"docker\"},\"params\":{\"resouceUrl\":\"\",\"deployPath\":\"${deployPath}\",\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"gf_variable\":\"\"},\"path\":\"\",\"pluginName\":\"download\",\"postAction\":\"com.dc.appengine.plugins.service.impl.Download#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.Download#preAction\"},\"salt\":{\"agent\":\"com.dc.appengine.plugins.service.impl.Salt#doActive\",\"createTime\":\"2017-04-21 10:20:24\",\"description\":\"salt package\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.Salt#invoke\",\"label\":{\"os\":\"linux\",\"type\":\"docker\"},\"params\":{\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"successRE\":\"Failed:    0\",\"failRE\":\"ERROR:#Exception\",\"gf_variable\":\"\"},\"path\":\"\",\"pluginName\":\"salt\",\"postAction\":\"com.dc.appengine.plugins.service.impl.Salt#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.Salt#preAction\"},\"start\":{\"agent\":\"com.dc.appengine.plugins.service.impl.Start#doActive\",\"createTime\":\"2017-04-10 14:53:24\",\"description\":\"start package\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.Start#invoke\",\"label\":{\"os\":\"linux\",\"type\":\"docker\"},\"params\":{\"startPath\":\"${startPath }\",\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"successRE\":\"\",\"failRE\":\"\",\"gf_variable\":\"\"},\"path\":\"\",\"pluginName\":\"start\",\"postAction\":\"com.dc.appengine.plugins.service.impl.Start#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.Start#preAction\"},\"stop\":{\"agent\":\"com.dc.appengine.plugins.service.impl.Stop#doActive\",\"createTime\":\"2017-04-10 14:53:24\",\"description\":\"stop package\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.Stop#invoke\",\"label\":{\"os\":\"linux\",\"type\":\"docker\"},\"params\":{\"stopPath\":\"${stopPath}\",\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"successRE\":\"\",\"failRE\":\"\",\"gf_variable\":\"\"},\"path\":\"\",\"pluginName\":\"stop\",\"postAction\":\"com.dc.appengine.plugins.service.impl.Stop#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.Stop#preAction\"},\"unzip\":{\"agent\":\"com.dc.appengine.plugins.service.impl.Unzip#doActive\",\"createTime\":\"2017-04-07 13:52:25\",\"description\":\"unzip package\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.Unzip#invoke\",\"label\":{\"os\":\"windows\",\"type\":\"docker\"},\"params\":{\"packagePath\":\"${filePath}\",\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"gf_variable\":\"\"},\"path\":\"\",\"pluginName\":\"unzip\",\"postAction\":\"com.dc.appengine.plugins.service.impl.Unzip#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.Unzip#preAction\"},\"CMD\":{\"agent\":\"com.dc.appengine.plugins.service.impl.CMD#doActive\",\"createTime\":\"2017-04-07 13:52:25\",\"description\":\"exec command\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.CMD#invoke\",\"label\":{\"os\":\"windows\",\"type\":\"docker\"},\"params\":{\"CMD\":\"\",\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"successRE\":\"\",\"failRE\":\"\",\"gf_variable\":\"\",\"longTask\":\"false\",\"userName\":\"\",\"password\":\"\",\"port\":\"22\",\"ip\":\"\",\"protocolType\":\"\"},\"path\":\"\",\"pluginName\":\"CMD\",\"postAction\":\"com.dc.appengine.plugins.service.impl.CMD#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.CMD#preAction\"},\"Snapshot\":{\"agent\":\"com.dc.appengine.plugins.service.impl.Snapshot#doActive\",\"createTime\":\"2017-04-07 13:52:25\",\"description\":\"generate Snapshot\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.Snapshot#invoke\",\"label\":{\"os\":\"windows\",\"type\":\"docker\"},\"params\":{\"ss_variable\":\"\",\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"gf_variable\":\"\"},\"path\":\"\",\"pluginName\":\"Snapshot\",\"postAction\":\"com.dc.appengine.plugins.service.impl.Snapshot#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.Snapshot#preAction\"},\"Dump\":{\"agent\":\"com.dc.appengine.plugins.service.impl.Dump#doActive\",\"createTime\":\"2017-04-07 13:52:25\",\"description\":\"Dump Datebase\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.Dump#invoke\",\"label\":{\"os\":\"windows\",\"type\":\"docker\"},\"params\":{\"CMD\":\"\",\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"successRE\":\"\",\"failRE\":\"\",\"gf_variable\":\"\"},\"path\":\"\",\"pluginName\":\"Dump\",\"postAction\":\"com.dc.appengine.plugins.service.impl.Dump#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.Dump#preAction\"},\"SqlServer\":{\"agent\":\"com.dc.appengine.plugins.service.impl.SqlServer#doActive\",\"createTime\":\"2017-04-07 13:52:25\",\"description\":\"SqlServer Datebase\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.SqlServer#invoke\",\"label\":{\"os\":\"windows\",\"type\":\"docker\"},\"params\":{\"dbip\":\"\",\"dbport\":\"\",\"dbname\":\"\",\"dbusername\":\"\",\"dbpasswd\":\"\",\"sql\":\"\",\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"gf_variable\":\"\"},\"path\":\"\",\"pluginName\":\"SqlServer\",\"postAction\":\"com.dc.appengine.plugins.service.impl.SqlServer#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.SqlServer#preAction\"},\"CMD4Analysis\":{\"agent\":\"com.dc.appengine.plugins.service.impl.CMD4Analysis#doActive\",\"createTime\":\"2017-04-07 13:52:25\",\"description\":\"exec command and analysis\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.CMD4Analysis#invoke\",\"label\":{\"os\":\"windows\",\"type\":\"docker\"},\"params\":{\"CMD\":\"\",\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"successRE\":\"\",\"failRE\":\"\",\"gf_variable\":\"\",\"logPath\":\"\",\"timeout\":\"60000\"},\"path\":\"\",\"pluginName\":\"CMD4Analysis\",\"postAction\":\"com.dc.appengine.plugins.service.impl.CMD4Analysis#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.CMD4Analysis#preAction\"},\"LoopCount\":{\"agent\":\"com.dc.appengine.plugins.service.impl.LoopCount#doActive\",\"createTime\":\"2017-07-25 12:00:00\",\"description\":\"loop count ++/--\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.LoopCount#invoke\",\"label\":{\"os\":\"windows\",\"type\":\"docker\"},\"params\":{\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"counter_variable\":\"\",\"counter_operation\":\"+1\"},\"path\":\"\",\"pluginName\":\"LoopCount\",\"postAction\":\"com.dc.appengine.plugins.service.impl.LoopCount#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.LoopCount#preAction\"},\"CMD4Script\":{\"agent\":\"com.dc.appengine.plugins.service.impl.CMD4Script#doActive\",\"createTime\":\"2017-04-07 13:52:25\",\"description\":\"exec script command\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.CMD4Script#invoke\",\"label\":{\"os\":\"windows\",\"type\":\"docker\"},\"params\":{\"scriptPath\":\"\",\"scriptParams\":\"\",\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"successRE\":\"\",\"failRE\":\"\",\"gf_variable\":\"\",\"longTask\":\"false\",\"userName\":\"\",\"password\":\"\",\"port\":\"22\",\"ip\":\"\",\"protocolType\":\"\"},\"path\":\"\",\"pluginName\":\"CMD4Script\",\"postAction\":\"com.dc.appengine.plugins.service.impl.CMD4Script#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.CMD4Script#preAction\"},\"CMD4ScriptAndAnalysis\":{\"agent\":\"com.dc.appengine.plugins.service.impl.CMD4ScriptAndAnalysis#doActive\",\"createTime\":\"2017-04-07 13:52:25\",\"description\":\"exec command\",\"fileName\":\"\",\"invoke\":\"com.dc.appengine.plugins.service.impl.CMD4ScriptAndAnalysis#invoke\",\"label\":{\"os\":\"windows\",\"type\":\"docker\"},\"params\":{\"scriptPath\":\"\",\"logPath\":\"\",\"forceContinue\":\"false\",\"forceEnd\":\"true\",\"successRE\":\"\",\"failRE\":\"\",\"gf_variable\":\"\",\"timeout\":\"60000\"},\"path\":\"\",\"pluginName\":\"CMD4ScriptAndAnalysis\",\"postAction\":\"com.dc.appengine.plugins.service.impl.CMD4ScriptAndAnalysis#postAction\",\"preAction\":\"com.dc.appengine.plugins.service.impl.CMD4ScriptAndAnalysis#preAction\"}}";
        Map<String,Object> map1 = new HashMap<String, Object>();
        map1.put("test1", "aaa");
        map1.put("test2", "aaa1");
		String test1="2017-08-17 11:11:22,609 frame [localhost-startStop-1] INFO";
		Map<String,Object> map2 = new HashMap<String, Object>();
		map2.put("test3", "aaa");
		map2.put("test4", "aaa1");
		map2.put("test5", JSON.toJSONString(map1));
		map2.put("test5", test1);
        String str1= "preAction test = "+System.lineSeparator()+JSON.toJSONString(map2);
		FormatUtil.printJson(str1);

//        FormatUtil.printJson(test1);
//		String outString = FormatUtil.formatJson(str);
//		ByteArrayInputStream bai = new ByteArrayInputStream(outString.getBytes());
//		File file = new File("F:/test/plugins.json");
//		try {
//			OutputStream out = new FileOutputStream(file);
//			byte[] buffer = new byte[1024];
//			int resp=0;
//			while((resp = bai.read(buffer))!=-1) {
//				out.write(buffer, 0, resp);      
//			}
//			out.flush();
//			out.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

}
