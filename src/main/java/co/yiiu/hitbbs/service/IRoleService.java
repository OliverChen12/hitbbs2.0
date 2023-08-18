package co.yiiu.hitbbs.service;

import co.yiiu.hitbbs.model.Role;

import java.util.List;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *
 */
public interface IRoleService {
    Role selectById(Integer roleId);

    List<Role> selectAll();

    void insert(String name, Integer[] permissionIds);

    void update(Integer id, String name, Integer[] permissionIds);

    void delete(Integer id);
}
