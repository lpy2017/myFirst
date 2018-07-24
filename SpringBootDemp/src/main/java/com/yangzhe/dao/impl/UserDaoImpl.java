package com.yangzhe.dao.impl;

import com.yangzhe.dao.IUserDao;
import com.yangzhe.entity.User;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/6.
 */
@Service("userDao")
public class UserDaoImpl implements IUserDao{

    private SqlSessionTemplate sqlSessionTemplate;

    @Resource
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
    @Override
    public Map<String,Object> selectByPrimaryKey(long userId) {
        List<Map<String,Object>> list=this.sqlSessionTemplate.selectList("IUserDao.findUserById",userId);
        return list.get(0);
    }

    @Override
    public void saveUser(User user) {
        this.sqlSessionTemplate.insert("IUserDao.saveUser",user);
    }
}
