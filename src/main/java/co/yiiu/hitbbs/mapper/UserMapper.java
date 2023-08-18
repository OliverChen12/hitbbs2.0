package co.yiiu.hitbbs.mapper;

import co.yiiu.hitbbs.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    int countToday();
}
