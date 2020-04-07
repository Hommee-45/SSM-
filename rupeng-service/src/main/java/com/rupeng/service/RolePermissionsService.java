package com.rupeng.service;

import org.springframework.stereotype.Service;

import com.rupeng.pojo.Permission;
import com.rupeng.pojo.Role;
import com.rupeng.pojo.RolePermission;

@Service
public class RolePermissionsService extends ManyToManyBaseService<Role, RolePermission, Permission> {

}
