package com.chenli.health.dao;

import com.chenli.health.pojo.Role;

import java.util.Set;

/**
 * 角色接口
 */
public interface RoleDao {

    Set<Role> find_RoleByUserId(Integer userId);
}
