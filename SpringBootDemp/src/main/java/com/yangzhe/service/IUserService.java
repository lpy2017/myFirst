package com.yangzhe.service;

import com.yangzhe.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Created by Administrator on 2016/6/6.
 */
public interface IUserService {
    User findByUsername(String userName);

    void save(User u);

    Map<String,Object> getUserById(long id);

    Page<User> getEntryByPageable(Pageable pageable);

}
