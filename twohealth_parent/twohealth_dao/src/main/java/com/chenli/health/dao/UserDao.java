package com.chenli.health.dao;

import com.chenli.health.pojo.User;

public interface UserDao {
    User find_UserByUserName(String username);
}
