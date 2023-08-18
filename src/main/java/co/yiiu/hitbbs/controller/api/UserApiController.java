package co.yiiu.hitbbs.controller.api;

import co.yiiu.hitbbs.model.OAuthUser;
import co.yiiu.hitbbs.model.User;
import co.yiiu.hitbbs.service.*;
import co.yiiu.hitbbs.util.MyPage;
import co.yiiu.hitbbs.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
@RestController
@RequestMapping("/api/user")
public class UserApiController extends BaseApiController {

    @Resource
    private IUserService userService;
    @Resource
    private ITopicService topicService;
    @Resource
    private ICommentService commentService;
    @Resource
    private ICollectService collectService;
    @Resource
    private IOAuthUserService oAuthUserService;

    // 用户的个人信息
    @GetMapping("/{username}")
    public Result profile(@PathVariable String username) {
        // 查询用户个人信息
        User user = userService.selectByUsername(username);
        // 查询oauth登录的用户信息
        List<OAuthUser> oAuthUsers = oAuthUserService.selectByUserId(user.getId());
        // 查询用户的话题
        MyPage<Map<String, Object>> topics = topicService.selectByUserId(user.getId(), 1, 10);
        // 查询用户参与的评论
        MyPage<Map<String, Object>> comments = commentService.selectByUserId(user.getId(), 1, 10);
        // 查询用户收藏的话题数
        Integer collectCount = collectService.countByUserId(user.getId());

        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("oAuthUsers", oAuthUsers);
        map.put("topics", topics);
        map.put("comments", comments);
        map.put("collectCount", collectCount);
        return success(map);
    }

    // 用户发布的话题
    @GetMapping("/{username}/topics")
    public Result topics(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo) {
        // 查询用户个人信息
        User user = userService.selectByUsername(username);
        // 查询用户的话题
        MyPage<Map<String, Object>> topics = topicService.selectByUserId(user.getId(), pageNo, null);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("topics", topics);
        return success(map);
    }

    // 用户评论列表
    @GetMapping("/{username}/comments")
    public Result comments(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo) {
        // 查询用户个人信息
        User user = userService.selectByUsername(username);
        // 查询用户参与的评论
        MyPage<Map<String, Object>> comments = commentService.selectByUserId(user.getId(), pageNo, null);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("comments", comments);
        return success(map);
    }

    // 用户收藏的话题
    @GetMapping("/{username}/collects")
    public Result collects(@PathVariable String username, @RequestParam(defaultValue = "1") Integer pageNo) {
        // 查询用户个人信息
        User user = userService.selectByUsername(username);
        // 查询用户参与的评论
        MyPage<Map<String, Object>> collects = collectService.selectByUserId(user.getId(), pageNo, null);
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("collects", collects);
        return success(map);
    }
}
