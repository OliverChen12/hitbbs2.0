package co.yiiu.hitbbs.service.impl;

import co.yiiu.hitbbs.mapper.TopicTagMapper;
import co.yiiu.hitbbs.model.Tag;
import co.yiiu.hitbbs.model.TopicTag;
import co.yiiu.hitbbs.service.ITopicTagService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *
 */
@Service
@Transactional
public class TopicTagService implements ITopicTagService {

    @Resource
    private TopicTagMapper topicTagMapper;

    @Override
    public List<TopicTag> selectByTopicId(Integer topicId) {
        QueryWrapper<TopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TopicTag::getTopicId, topicId);
        return topicTagMapper.selectList(wrapper);
    }

    @Override
    public List<TopicTag> selectByTagId(Integer tagId) {
        QueryWrapper<TopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TopicTag::getTagId, tagId);
        return topicTagMapper.selectList(wrapper);
    }

    @Override
    public void insertTopicTag(Integer topicId, List<Tag> tagList) {
        // 先删除topicId对应的所有记录
        this.deleteByTopicId(topicId);
        // 循环保存对应关联
        tagList.forEach(tag -> {
            TopicTag topicTag = new TopicTag();
            topicTag.setTopicId(topicId);
            topicTag.setTagId(tag.getId());
            topicTagMapper.insert(topicTag);
        });
    }

    // 删除话题所有关联的标签记录
    @Override
    public void deleteByTopicId(Integer id) {
        QueryWrapper<TopicTag> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(TopicTag::getTopicId, id);
        topicTagMapper.delete(wrapper);
    }
}
