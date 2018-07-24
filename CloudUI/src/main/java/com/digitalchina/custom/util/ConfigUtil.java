package com.digitalchina.custom.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by yangzhec on 2016/12/12.
 * 这个类是从配置中心拉取配置的工具类
 */
public class ConfigUtil {

    public static Properties loadFromPaasConfigCenter(){
        //下面的环境变量在部署的时候都写在容器的环境变量中，可以直接使用，
        // 如果是debug环境，那么就从命令行参数中读取这些配置
        String host,user,password,appName = "cloudui",type;
        int port;
        boolean debug = System.getenv("ConfigHost") == null;
        if(debug){
            //如果是在调试环境下
            host = System.getProperty("ConfigHost");
            user = System.getProperty("ConfigUser");
            password = System.getProperty("ConfigPassword");
            port = Integer.valueOf(System.getProperty("ConfigPort"));
//            appName = System.getProperty("Config_app");
            type = "password";
        }else{
            host = System.getenv("ConfigHost");
            user = System.getenv("ConfigUser");
            password = System.getenv("ConfigPassword");
            port = Integer.valueOf(System.getenv("ConfigPort"));
//            appName = System.getenv("Config_app");
            //写在环境变量的是md5加密的password
            type = "md5";
        }
        Map<String,Object> map = new HashMap<String,Object>();
        ConfigClient configClient = new ConfigClient(host,user,password,appName,port);
        configClient.setType(type);
        Properties properties = configClient.getPropertyConfig("normal");
        for(Map.Entry<Object,Object> entry:properties.entrySet()){
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            map.put(key,value);
            System.out.println(key+"--->"+value);
        }
        return properties;
    }
}
