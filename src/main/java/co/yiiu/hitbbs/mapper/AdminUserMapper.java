package co.yiiu.hitbbs.mapper;

import co.yiiu.hitbbs.model.AdminUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
public interface AdminUserMapper extends BaseMapper<AdminUser> {

    List<Map<String, Object>> selectAll();
}
