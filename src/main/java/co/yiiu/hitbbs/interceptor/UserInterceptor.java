package co.yiiu.hitbbs.interceptor;

import co.yiiu.hitbbs.model.User;
import co.yiiu.hitbbs.service.ISystemConfigService;
import co.yiiu.hitbbs.service.impl.UserService;
import co.yiiu.hitbbs.util.CookieUtil;
import co.yiiu.hitbbs.util.HttpUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
@Component
public class UserInterceptor implements HandlerInterceptor {

    @Resource
    private CookieUtil cookieUtil;
    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("_user");
        if (user == null) {
            String token = cookieUtil.getCookie(systemConfigService.selectAllConfig().get("cookie_name").toString());
            if (!StringUtils.isEmpty(token)) {
                user = userService.selectByToken(token);
                session.setAttribute("_user", user);
            }
        }
        if (user == null) {
            HttpUtil.responseWrite(request, response);
            return false;
        } else {
            return true;
        }
    }
}
