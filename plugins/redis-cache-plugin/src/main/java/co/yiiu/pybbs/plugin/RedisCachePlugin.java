package co.yiiu.hitbbs.plugin;

import co.yiiu.hitbbs.model.Comment;
import co.yiiu.hitbbs.model.Topic;
import co.yiiu.hitbbs.model.User;
import co.yiiu.hitbbs.model.vo.CommentsByTopic;
import co.yiiu.hitbbs.service.ISystemConfigService;
import co.yiiu.hitbbs.service.ITopicService;
import co.yiiu.hitbbs.util.JsonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
@Component
@Aspect
public class RedisCachePlugin {

    @Resource
    private RedisService redisService;
    @Resource
    private ITopicService topicService;
    @Resource
    private ISystemConfigService systemConfigService;

    // ---------- topic cache start ----------

    @Around("co.yiiu.hitbbs.hook.TopicServiceHook.selectById()")
    public Object topicSelectById(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String topicJson = redisService.getString(String.format(RedisKeys.REDIS_TOPIC_KEY, proceedingJoinPoint.getArgs()[0]));
        if (topicJson == null) {
            Object topic = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            // 缓存在redis里
            redisService.setString(String.format(RedisKeys.REDIS_TOPIC_KEY, proceedingJoinPoint.getArgs()[0]), JsonUtil.objectToJson(topic));
            return topic;
        } else {
            return JsonUtil.jsonToObject(topicJson, Topic.class);
        }
    }

    @Around("co.yiiu.hitbbs.hook.TopicServiceHook.updateViewCount()")
    public Object topicUpdateViewCount(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Topic topic = (Topic) proceedingJoinPoint.getArgs()[0];
        String ip = (String) proceedingJoinPoint.getArgs()[1];
        String s = redisService.getString(String.format(RedisKeys.REDIS_TOPIC_VIEW_IP_ID_KEY, ip, topic.getId()));
        if (redisService.isRedisConfig()) {
            if (s == null) {
                topic.setView(topic.getView() + 1);
                topicService.update(topic, null);
                redisService.setString(String.format(RedisKeys.REDIS_TOPIC_VIEW_IP_ID_KEY, ip, topic.getId()), String.valueOf
                        (topic.getId()), Integer.parseInt(systemConfigService.selectAllConfig().get("topic_view_increase_interval").toString()));
            }
            return topic;
        } else {
            return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
        }
    }

    @Around("co.yiiu.hitbbs.hook.TopicServiceHook.vote()")
    public Object voteTopic(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 点赞后删除redis内缓存的topic数据
        Topic topic = (Topic) proceedingJoinPoint.getArgs()[0];
        redisService.delString(String.format(RedisKeys.REDIS_TOPIC_KEY, topic.getId()));
        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }

    @After("co.yiiu.hitbbs.hook.TopicServiceHook.update()")
    public void topicUpdate(JoinPoint joinPoint) {
        Topic topic = (Topic) joinPoint.getArgs()[0];
        // 缓存到redis里
        redisService.setString(String.format(RedisKeys.REDIS_TOPIC_KEY, topic.getId()), JsonUtil.objectToJson(topic));
    }

    // ---------- topic cache end ----------

    // ---------- comment cache start ----------

    @Around("co.yiiu.hitbbs.hook.CommentServiceHook.selectByTopicId()")
    public Object commentSelectByTopicId(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Integer topicId = (Integer) proceedingJoinPoint.getArgs()[0];
        String commentsJson = redisService.getString(String.format(RedisKeys.REDIS_COMMENTS_KEY, topicId));
        if (commentsJson != null) {
            // 带泛型转换, 这里如果不带泛型转换，会报错
            return JsonUtil.jsonToListObject(commentsJson, CommentsByTopic.class);
        } else {
            Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            redisService.setString(String.format(RedisKeys.REDIS_COMMENTS_KEY, topicId), JsonUtil.objectToJson(returnValue));
            return returnValue;
        }
    }

    @After("co.yiiu.hitbbs.hook.CommentServiceHook.insert()")
    public void commentInsert(JoinPoint joinPoint) {
        Comment comment = (Comment) joinPoint.getArgs()[0];
        redisService.delString(String.format(RedisKeys.REDIS_COMMENTS_KEY, comment.getTopicId()));
    }

    @After("co.yiiu.hitbbs.hook.CommentServiceHook.update()")
    public void commentUpdate(JoinPoint joinPoint) {
        Comment comment = (Comment) joinPoint.getArgs()[0];
        redisService.delString(String.format(RedisKeys.REDIS_COMMENTS_KEY, comment.getTopicId()));
    }

    @After("co.yiiu.hitbbs.hook.CommentServiceHook.delete()")
    public void commentDelete(JoinPoint joinPoint) {
        Comment comment = (Comment) joinPoint.getArgs()[0];
        redisService.delString(String.format(RedisKeys.REDIS_COMMENTS_KEY, comment.getTopicId()));
    }

    @Around("co.yiiu.hitbbs.hook.CommentServiceHook.vote()")
    public Object voteComment(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Comment comment = (Comment) proceedingJoinPoint.getArgs()[0];
        redisService.delString(String.format(RedisKeys.REDIS_COMMENTS_KEY, comment.getTopicId()));
        return proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
    }

    // ---------- comment cache end ----------

    // ---------- user cache start ----------

    @Around("co.yiiu.hitbbs.hook.UserServiceHook.selectByUsername()")
    public Object userSelectByUsername(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String username = (String) proceedingJoinPoint.getArgs()[0];
        String userJson = redisService.getString(String.format(RedisKeys.REDIS_USER_USERNAME_KEY, username));
        if (userJson != null) {
            // 带泛型转换, 这里如果不带泛型转换，会报错
            return JsonUtil.jsonToObject(userJson, User.class);
        } else {
            Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            if (returnValue != null) {
                redisService.setString(String.format(RedisKeys.REDIS_USER_USERNAME_KEY, username), JsonUtil.objectToJson(returnValue));
            }
            return returnValue;
        }
    }

    @Around("co.yiiu.hitbbs.hook.UserServiceHook.selectByToken()")
    public Object userSelectByToken(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String token = (String) proceedingJoinPoint.getArgs()[0];
        String userJson = redisService.getString(String.format(RedisKeys.REDIS_USER_TOKEN_KEY, token));
        if (userJson != null) {
            // 带泛型转换, 这里如果不带泛型转换，会报错
            return JsonUtil.jsonToObject(userJson, User.class);
        } else {
            Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            if (returnValue != null) {
                redisService.setString(String.format(RedisKeys.REDIS_USER_TOKEN_KEY, token), JsonUtil.objectToJson(returnValue));
            }
            return returnValue;
        }
    }

    @Around("co.yiiu.hitbbs.hook.UserServiceHook.selectById()")
    public Object userSelectById(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Integer id = (Integer) proceedingJoinPoint.getArgs()[0];
        String userJson = redisService.getString(String.format(RedisKeys.REDIS_USER_ID_KEY, id));
        if (userJson != null) {
            // 带泛型转换, 这里如果不带泛型转换，会报错
            return JsonUtil.jsonToObject(userJson, User.class);
        } else {
            Object returnValue = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            if (returnValue != null) {
                redisService.setString(String.format(RedisKeys.REDIS_USER_ID_KEY, id), JsonUtil.objectToJson(returnValue));
            }
            return returnValue;
        }
    }

    @After("co.yiiu.hitbbs.hook.UserServiceHook.delRedisUser()")
    public void userDelRedisUser(JoinPoint joinPoint) {
        User user = (User) joinPoint.getArgs()[0];
        redisService.delString(String.format(RedisKeys.REDIS_USER_ID_KEY, user.getId()));
        redisService.delString(String.format(RedisKeys.REDIS_USER_USERNAME_KEY, user.getUsername()));
        redisService.delString(String.format(RedisKeys.REDIS_USER_TOKEN_KEY, user.getToken()));
    }

    // ---------- user cache end ----------

    static class RedisKeys {
        public static final String REDIS_TOPIC_KEY = "hitbbs_topic_%s"; // 后面还要拼上话题的id
        public static final String REDIS_TOPIC_VIEW_IP_ID_KEY = "hitbbs_topic_view_ip_%s_topic_%s"; // 需要格式化字符串填充上ip地址跟话题id
        public static final String REDIS_COMMENTS_KEY = "hitbbs_comments_%s"; // 后面还要拼上话题的id

        public static final String REDIS_USER_ID_KEY = "hitbbs_user_id_%s"; // 后面还要拼上用户的id
        public static final String REDIS_USER_USERNAME_KEY = "hitbbs_user_username_%s"; // 后面还要拼上用户名
        public static final String REDIS_USER_TOKEN_KEY = "hitbbs_user_token_%s"; // 后面还要拼上用户的token
    }
}
