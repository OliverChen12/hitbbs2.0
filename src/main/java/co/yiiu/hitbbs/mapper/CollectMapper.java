package co.yiiu.hitbbs.mapper;

import co.yiiu.hitbbs.model.Collect;
import co.yiiu.hitbbs.util.MyPage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
public interface CollectMapper extends BaseMapper<Collect> {

    MyPage<Map<String, Object>> selectByUserId(MyPage<Map<String, Object>> iPage, @Param("userId") Integer userId);
}
