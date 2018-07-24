package com.yangzhe.custom;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by yangzhec on 2016/12/12.
 */
@Component
public class MyPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    @Override
    protected Properties mergeProperties() throws IOException {
        return loadFromPaasConfigCenter();
    }

    //模拟从配置中心拉取配置
    private static Properties loadFromPaasConfigCenter(){
        Properties prop = new Properties();
        try {
            prop.load(MyPropertyPlaceholderConfigurer.class
                    .getClassLoader().getResourceAsStream("jdbc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
