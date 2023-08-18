package co.yiiu.hitbbs.service;

import co.yiiu.hitbbs.model.SystemConfig;

import java.util.List;
import java.util.Map;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
public interface ISystemConfigService {
    Map<String, String> selectAllConfig();

    // 根据键取值
    SystemConfig selectByKey(String key);

    Map<String, Object> selectAll();

    // 在更新系统设置后，清一下selectAllConfig()的缓存
    void update(List<Map<String, String>> list);

    // 根据key更新数据
    void updateByKey(String key, SystemConfig systemConfig);
}
