package com.yangzhe.hello;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * Created by Administrator on 2017/3/5.
 */
@Controller
public class HelloController {
    /*// 从 application.properties 中读取配置，如取不到默认值为Hello Shanhy
    @Value("${application.hello:Hello Angel}")
    private String hello;*/
    @RequestMapping("/register")
    public String register(Map<String,Object> map){
        System.out.println("HelloController.helloJsp().hello="+"papapa");
        map.put("hello", "papapa");
        return "register";
    }

}