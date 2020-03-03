package com.chenli.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenli.health.dao.UserDao;
import com.chenli.health.pojo.User;
import com.chenli.health.serviceinterface.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImp implements UserService {

    @Autowired
    private UserDao userDao;
    @Override
    public User find_UserByUsername(String username) {
        User userByUserName = userDao.find_UserByUserName(username);
        return userByUserName;
    }
}
