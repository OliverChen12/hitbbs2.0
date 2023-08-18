package co.yiiu.hitbbs.service.impl;

import co.yiiu.hitbbs.mapper.NotificationMapper;
import co.yiiu.hitbbs.model.Notification;
import co.yiiu.hitbbs.service.INotificationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *
 */
@Service
@Transactional
public class NotificationService implements INotificationService {

    @Resource
    private NotificationMapper notificationMapper;

    // 查询消息
    @Override
    public List<Map<String, Object>> selectByUserId(Integer userId, Boolean read, Integer limit) {
        List<Map<String, Object>> notifications = notificationMapper.selectByUserId(userId, read, limit);
        return notifications;
    }

    @Override
    public void markRead(Integer userId) {
        notificationMapper.updateNotificationStatus(userId);
    }

    // 查询未读消息数量
    @Override
    public long countNotRead(Integer userId) {
        return notificationMapper.countNotRead(userId);
    }

    @Override
    public void deleteByTopicId(Integer topicId) {
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Notification::getTopicId, topicId);
        notificationMapper.delete(wrapper);
    }

    @Override
    public void deleteByUserId(Integer userId) {
        QueryWrapper<Notification> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(Notification::getTargetUserId, userId).or().eq(Notification::getUserId, userId);
        notificationMapper.delete(wrapper);
    }

    @Override
    public void insert(Integer userId, Integer targetUserId, Integer topicId, String action, String content) {
        Notification notification = new Notification();
        notification.setAction(action);
        notification.setContent(content);
        notification.setUserId(userId);
        notification.setTargetUserId(targetUserId);
        notification.setTopicId(topicId);
        notification.setInTime(new Date());
        notification.setRead(false);
        notificationMapper.insert(notification);
    }
}
