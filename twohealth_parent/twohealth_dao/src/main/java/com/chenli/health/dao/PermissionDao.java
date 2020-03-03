package com.chenli.health.dao;

import com.chenli.health.pojo.Permission;

import java.util.Set;

public interface PermissionDao {

    Set<Permission> find_permissionByRoleId(Integer roleId);
}
