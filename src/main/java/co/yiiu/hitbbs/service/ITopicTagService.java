package co.yiiu.hitbbs.service;

import co.yiiu.hitbbs.model.Tag;
import co.yiiu.hitbbs.model.TopicTag;

import java.util.List;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
public interface ITopicTagService {
    List<TopicTag> selectByTopicId(Integer topicId);

    List<TopicTag> selectByTagId(Integer tagId);

    void insertTopicTag(Integer topicId, List<Tag> tagList);

    // 删除话题所有关联的标签记录
    void deleteByTopicId(Integer id);
}
