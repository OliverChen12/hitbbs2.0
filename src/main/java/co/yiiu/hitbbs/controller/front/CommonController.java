package co.yiiu.hitbbs.controller.front;

import co.yiiu.hitbbs.controller.api.BaseApiController;
import co.yiiu.hitbbs.util.FileUtil;
import co.yiiu.hitbbs.util.captcha.Captcha;
import co.yiiu.hitbbs.util.captcha.GifCaptcha;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *
 */
@Controller
@RequestMapping("/common")
public class CommonController extends BaseApiController {

    @Resource
    private FileUtil fileUtil;

    // gif 验证码
    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response, HttpSession session) throws IOException {
        Captcha captcha = new GifCaptcha();
        captcha.out(response.getOutputStream());
        String text = captcha.text();
        session.setAttribute("_captcha", text);
    }

    @GetMapping("/show_img")
    public String showOssImg(String name) {
        return "redirect:" + fileUtil.generatorOssUrl(name);
    }

}
