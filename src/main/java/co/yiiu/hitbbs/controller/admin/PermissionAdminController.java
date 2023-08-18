package co.yiiu.hitbbs.controller.admin;

import co.yiiu.hitbbs.model.Permission;
import co.yiiu.hitbbs.service.IPermissionService;
import co.yiiu.hitbbs.util.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *
 */
@Controller
@RequestMapping("/admin/permission")
public class PermissionAdminController extends BaseAdminController {

    @Resource
    private IPermissionService permissionService;

    @RequiresPermissions("permission:list")
    @GetMapping("/list")
    public String list(Integer pid, Model model) {
        List<Permission> permissions = permissionService.selectByPid(0);
        model.addAttribute("permissions", permissions);
        List<Permission> childPermissions;
        if (pid == null || pid == 0) {
            childPermissions = permissionService.selectByPid(permissions.get(0).getId());
        } else {
            childPermissions = permissionService.selectByPid(pid);
        }
        model.addAttribute("childPermissions", childPermissions);
        model.addAttribute("pid", pid);
        return "admin/permission/list";
    }

    @RequiresPermissions("permission:add")
    @PostMapping("/add")
    @ResponseBody
    public Result add(Permission permission) {
        permission = permissionService.insert(permission);
        return success(permission.getPid() == 0 ? permission.getId() : permission.getPid());
    }

    @RequiresPermissions("permission:edit")
    @PostMapping("/edit")
    @ResponseBody
    public Result edit(Permission permission) {
        permission = permissionService.update(permission);
        return success(permission.getPid() == 0 ? permission.getId() : permission.getPid());
    }

    @RequiresPermissions("permission:delete")
    @GetMapping("/delete")
    @ResponseBody
    public Result delete(Integer id) {
        permissionService.delete(id);
        return success();
    }
}
