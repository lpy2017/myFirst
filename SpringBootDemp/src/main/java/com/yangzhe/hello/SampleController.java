package com.yangzhe.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONUtils;
import com.yangzhe.entity.User;
import com.yangzhe.service.IUserService;

@Controller
@RequestMapping("/user")
public class SampleController {

    @Autowired
    IUserService userService;

    @RequestMapping(value = "/{userName}",method = RequestMethod.GET)
    @ResponseBody
    String home(@PathVariable("userName") String userName) {
        return userService.findByUsername(userName).getUserName();

    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    String register(User u) {
        userService.save(u);
        return "register success:"+u.getUserName();
    }

    @RequestMapping(value = "/id/{id}",method = RequestMethod.GET)
    @ResponseBody
    String findById(@PathVariable("id") long id){
        return JSONUtils.toJSONString(userService.getUserById(id));
    }

    @RequestMapping(value="page/user",method = RequestMethod.GET)
    @ResponseBody
    public Page<User> getEntryByPageable(
            @PageableDefault(value = 15, sort = { "id" },
            direction = Sort.Direction.DESC) Pageable pageable) {
        return userService.getEntryByPageable(pageable);
    }
}
