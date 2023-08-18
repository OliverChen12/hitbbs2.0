package co.yiiu.hitbbs.controller.front;

import co.yiiu.hitbbs.model.User;
import co.yiiu.hitbbs.service.ISystemConfigService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *
 */
public class BaseController {

    @Resource
    private ISystemConfigService systemConfigService;

    protected String redirect(String path) {
        return "redirect:" + path;
    }

    protected User getUser() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        HttpSession session = request.getSession();
        return (User) session.getAttribute("_user");
    }

    // 只针对前台页面的模板路径渲染，后台不变
    protected String render(String path) {
        return String.format("theme/%s/%s", systemConfigService.selectAllConfig().get("theme").toString(), path);
    }

}
