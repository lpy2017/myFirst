package com.yangzhe.service.impl;

import com.yangzhe.dao.IUserDao;
import com.yangzhe.entity.User;
import com.yangzhe.repository.UserRepository;
import com.yangzhe.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/6.
 */
@Service("userService")
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserDao userDao;

    @Autowired
    UserRepository userRepository;


    @Override
    public User findByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public void save(User u) {
        userRepository.save(u);
    }

    @Override
    public Map<String,Object> getUserById(long id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public Page<User> getEntryByPageable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}