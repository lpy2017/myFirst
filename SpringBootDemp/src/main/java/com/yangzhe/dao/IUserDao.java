package com.yangzhe.dao;

import com.yangzhe.entity.User;

import java.util.Map;

/**
 * Created by Administrator on 2016/6/6.
 */
public interface IUserDao {
    Map<String,Object> selectByPrimaryKey(long userId);
    void saveUser(User user);
}
