package co.yiiu.hitbbs.controller.api;

import co.yiiu.hitbbs.exception.ApiAssert;
import co.yiiu.hitbbs.model.Code;
import co.yiiu.hitbbs.model.User;
import co.yiiu.hitbbs.service.ICodeService;
import co.yiiu.hitbbs.service.ISystemConfigService;
import co.yiiu.hitbbs.service.IUserService;
import co.yiiu.hitbbs.util.Result;
import co.yiiu.hitbbs.util.StringUtil;
import co.yiiu.hitbbs.util.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
@RestController
@RequestMapping("/api/settings")
public class SettingsApiController extends BaseApiController {

    @Resource
    private IUserService userService;
    @Resource
    private ICodeService codeService;
    @Resource
    private ISystemConfigService systemConfigService;

    // 更新用户个人信息
    @PutMapping
    public Result update(@RequestBody Map<String, String> body, HttpSession session) {
        User user = getApiUser();
        String telegramName = body.get("telegramName");
        String website = body.get("website");
        String bio = body.get("bio");
        Boolean emailNotification = Boolean.parseBoolean(body.get("emailNotification"));
        // 查询当前用户的最新信息
        User user1 = userService.selectById(user.getId());
        user1.setTelegramName(telegramName);
        user1.setWebsite(website);
        user1.setBio(bio);
        user1.setEmailNotification(emailNotification);
        userService.update(user1);

        User user2 = getUser();
        if (user2 != null) {
            user2.setBio(bio);
            session.setAttribute("_user", user2);
        }
        return success();
    }

    // 发送激活邮件
    @GetMapping("/sendActiveEmail")
    public Result sendActiveEmail() {
        User user = getApiUser();
        ApiAssert.notTrue(StringUtils.isEmpty(user.getEmail()), "你的帐号还没有绑定邮箱，请先绑定邮箱");
        ApiAssert.notTrue(user.getActive(), "你的帐号当前已经是激活状态，不需要再发激活邮件了");

        String title = "感谢注册%s，点击下面链接激活帐号";
        String content = "如果不是你注册了%s，请忽略此邮件&nbsp;&nbsp;<a href='%s/active?email=%s&code=${code}'>点击激活</a>";

        if (codeService.sendEmail(
                user.getId(),
                user.getEmail(),
                String.format(title, systemConfigService.selectAllConfig().get("base_url").toString()),
                String.format(content, systemConfigService.selectAllConfig().get("name").toString(),
                        systemConfigService.selectAllConfig().get("base_url").toString(),
                        user.getEmail()))) {
            return success();
        } else {
            return error("邮件发送失败，也可能是站长没有配置邮箱");
        }
    }

    // 发送邮箱验证码
    @GetMapping("/sendEmailCode")
    public Result sendEmailCode(String email) {
        User user = getApiUser();
        ApiAssert.notEmpty(email, "请输入邮箱 ");
        ApiAssert.isTrue(StringUtil.check(email, StringUtil.EMAILREGEX), "邮箱格式不正确");
        User emailUser = userService.selectByEmail(email);
        ApiAssert.isNull(emailUser, "这个邮箱已经被注册过了，请更换一个邮箱");
        if (codeService.sendEmail(user.getId(), email, "修改邮箱验证码", "你的验证码是：<code>${code}</code><br>请在30分钟内使用")) {
            return success();
        } else {
            return error("邮件发送失败，也可能是站长没有配置邮箱");
        }
    }

    // 更新用户邮箱
    @PutMapping("/updateEmail")
    public Result updateEmail(@RequestBody Map<String, String> body, HttpSession session) {
        User user = getApiUser();
        String email = body.get("email");
        String code = body.get("code");
        ApiAssert.notEmpty(email, "请输入邮箱 ");
        ApiAssert.isTrue(StringUtil.check(email, StringUtil.EMAILREGEX), "邮箱格式不正确");
        Code code1 = codeService.validateCode(user.getId(), email, null, code);
        if (code1 == null) return error("验证码错误");
        // 将code的状态置为已用
        code1.setUsed(true);
        codeService.update(code1);
        // 查询当前用户的最新信息
        User user1 = userService.selectById(user.getId());
        user1.setEmail(email);
        // 如果用户帐号还没有激活，当修改邮箱的时候自动激活帐号
        if (!user1.getActive()) user1.setActive(true);
        userService.update(user1);
        // 更新session中的用户信息
        User _user = getUser();
        _user.setEmail(email);
        session.setAttribute("_user", _user);
        return success();
    }

    // 修改密码
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Map<String, String> body) {
        User user = getApiUser();
        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");
        ApiAssert.notEmpty(oldPassword, "请输入旧密码");
        ApiAssert.notEmpty(newPassword, "请输入新密码");
        ApiAssert.notTrue(oldPassword.equals(newPassword), "新密码怎么还是旧的？");
        ApiAssert.isTrue(new BCryptPasswordEncoder().matches(oldPassword, user.getPassword()), "旧密码不正确");
        // 查询当前用户的最新信息
        User user1 = userService.selectById(user.getId());
        user1.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userService.update(user1);
        return success();
    }

    // 刷新token
    @GetMapping("/refreshToken")
    public Result refreshToken(HttpSession session) {
        User user = getApiUser();
        String token = StringUtil.uuid();
        user.setToken(token);
        userService.update(user);
        // 更新session中的用户信息
        User _user = getUser();
        _user.setToken(token);
        session.setAttribute("_user", _user);
        return success(token);
    }

}
