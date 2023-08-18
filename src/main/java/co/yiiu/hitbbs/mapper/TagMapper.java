package co.yiiu.hitbbs.mapper;

import co.yiiu.hitbbs.model.Tag;
import co.yiiu.hitbbs.util.MyPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
public interface TagMapper extends BaseMapper<Tag> {

    MyPage<Map<String, Object>> selectTopicByTagId(MyPage<Map<String, Object>> iPage, @Param("tagId") Integer tagId);

    int countToday();
}
