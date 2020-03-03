package com.chenli.health.serviceinterface;

import com.chenli.health.pojo.User;

public interface UserService {
    User find_UserByUsername(String username);
}
