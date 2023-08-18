package co.yiiu.hitbbs.service;

import co.yiiu.hitbbs.model.RolePermission;

import java.util.List;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
public interface IRolePermissionService {
    // 根据角色id查询所有的角色权限关联记录
    List<RolePermission> selectByRoleId(Integer roleId);

    // 根据角色id删除关联关系
    void deleteByRoleId(Integer roleId);

    // 根据权限id删除关联关系
    void deleteByPermissionId(Integer permissionId);

    void insert(RolePermission rolePermission);
}
