package co.yiiu.hitbbs.controller.api;

import co.yiiu.hitbbs.exception.ApiAssert;
import co.yiiu.hitbbs.model.Collect;
import co.yiiu.hitbbs.model.Tag;
import co.yiiu.hitbbs.model.Topic;
import co.yiiu.hitbbs.model.User;
import co.yiiu.hitbbs.model.vo.CommentsByTopic;
import co.yiiu.hitbbs.service.*;
import co.yiiu.hitbbs.util.IpUtil;
import co.yiiu.hitbbs.util.Result;
import co.yiiu.hitbbs.util.SensitiveWordUtil;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *
 */
@RestController
@RequestMapping("/api/topic")
public class TopicApiController extends BaseApiController {

    @Resource
    private ITopicService topicService;
    @Resource
    private ITagService tagService;
    @Resource
    private ICommentService commentService;
    @Resource
    private IUserService userService;
    @Resource
    private ICollectService collectService;

    // 话题详情
    @GetMapping("/{id}")
    public Result detail(@PathVariable Integer id, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 查询话题详情
        Topic topic = topicService.selectById(id);
        // 查询话题关联的标签
        List<Tag> tags = tagService.selectByTopicId(id);
        // 查询话题的评论
        List<CommentsByTopic> comments = commentService.selectByTopicId(id);
        // 查询话题的作者信息
        User topicUser = userService.selectById(topic.getUserId());
        // 查询话题有多少收藏
        List<Collect> collects = collectService.selectByTopicId(id);
        // 如果自己登录了，查询自己是否收藏过这个话题
        User user = getApiUser(false);
        if (user != null) {
            Collect collect = collectService.selectByTopicIdAndUserId(id, user.getId());
            map.put("collect", collect);
        }
        // 话题浏览量+1
        String ip = IpUtil.getIpAddr(request);
        ip = ip.replace(":", "_").replace(".", "_");
        topic = topicService.updateViewCount(topic, ip);
        topic.setContent(SensitiveWordUtil.replaceSensitiveWord(topic.getContent(), "*", SensitiveWordUtil.MinMatchType));

        map.put("topic", topic);
        map.put("tags", tags);
        map.put("comments", comments);
        map.put("topicUser", topicUser);
        map.put("collects", collects);
        return success(map);
    }

    // 保存话题
    @PostMapping
    public Result create(@RequestBody Map<String, String> body) {
        User user = getApiUser();
        ApiAssert.isTrue(user.getActive(), "你的帐号还没有激活，请去个人设置页面激活帐号");
        String title = body.get("title");
        String content = body.get("content");
        String tag = body.get("tag");
        //    String tags = body.get("tags");
        ApiAssert.notEmpty(title, "请输入标题");
        ApiAssert.isNull(topicService.selectByTitle(title), "话题标题重复");
        //    String[] strings = StringUtils.commaDelimitedListToStringArray(tags);
        //    Set<String> set = StringUtil.removeEmpty(strings);
        //    ApiAssert.notTrue(set.isEmpty() || set.size() > 5, "请输入标签且标签最多5个");
        // 保存话题
        // 再次将tag转成逗号隔开的字符串
        //    tags = StringUtils.collectionToCommaDelimitedString(set);
        Topic topic = topicService.insert(title, content, tag, user);
        topic.setContent(SensitiveWordUtil.replaceSensitiveWord(topic.getContent(), "*", SensitiveWordUtil.MinMatchType));
        return success(topic);
    }

    // 更新话题
    @PutMapping(value = "/{id}")
    public Result edit(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        User user = getApiUser();
        String title = body.get("title");
        String content = body.get("content");
        ApiAssert.notEmpty(title, "请输入标题");
        // 更新话题
        Topic topic = topicService.selectById(id);
        ApiAssert.isTrue(topic.getUserId().equals(user.getId()), "谁给你的权限修改别人的话题的？");
        topic.setTitle(Jsoup.clean(title, Whitelist.none().addTags("video")));
        topic.setContent(content);
        topic.setModifyTime(new Date());
        topicService.update(topic, null);
        topic.setContent(SensitiveWordUtil.replaceSensitiveWord(topic.getContent(), "*", SensitiveWordUtil.MinMatchType));
        return success(topic);
    }

    // 删除话题
    @DeleteMapping("{id}")
    public Result delete(@PathVariable Integer id) {
        User user = getApiUser();
        Topic topic = topicService.selectById(id);
        ApiAssert.isTrue(topic.getUserId().equals(user.getId()), "谁给你的权限删除别人的话题的？");
        topicService.delete(topic);
        return success();
    }

    @GetMapping("/{id}/vote")
    public Result vote(@PathVariable Integer id) {
        User user = getApiUser();
        Topic topic = topicService.selectById(id);
        ApiAssert.notNull(topic, "这个话题可能已经被删除了");
        ApiAssert.notTrue(topic.getUserId().equals(user.getId()), "给自己话题点赞，脸皮真厚！！");
        int voteCount = topicService.vote(topic, getApiUser());
        return success(voteCount);
    }
}
