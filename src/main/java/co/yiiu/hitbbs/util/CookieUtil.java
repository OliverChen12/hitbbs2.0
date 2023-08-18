package co.yiiu.hitbbs.util;

import co.yiiu.hitbbs.service.ISystemConfigService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *
 */
@Component
public class CookieUtil {

    @Resource
    private ISystemConfigService systemConfigService;

    public void setCookie(String key, String value) {
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getResponse();
        Cookie cookie = new Cookie(key, value);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(Integer.parseInt(systemConfigService.selectAllConfig().get("cookie_max_age").toString()));
        cookie.setDomain(systemConfigService.selectAllConfig().get("cookie_domain").toString());
        assert response != null;
        response.addCookie(cookie);
    }


    public String getCookie(String name) {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (c.getName().equalsIgnoreCase(name)) {
                    return c.getValue();
                }
            }
        }
        return null;
    }

    // 清除cookie
    public void clearCookie(String name) {
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getResponse();
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(-1);
        cookie.setDomain((String) systemConfigService.selectAllConfig().get("cookie_domain"));
        cookie.setPath("/");
        assert response != null;
        response.addCookie(cookie);
    }
}
