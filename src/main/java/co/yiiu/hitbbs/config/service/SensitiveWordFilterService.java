package co.yiiu.hitbbs.config.service;

import co.yiiu.hitbbs.model.SensitiveWord;
import co.yiiu.hitbbs.service.ISensitiveWordService;
import co.yiiu.hitbbs.util.SensitiveWordUtil;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *
 */
@Component
@DependsOn("mybatisPlusConfig")
public class SensitiveWordFilterService {

    @Resource
    private ISensitiveWordService sensitiveWordService;

    // 初始化过滤器
    @PostConstruct
    public void init() {
        List<SensitiveWord> sensitiveWords = sensitiveWordService.selectAll();
        Set<String> sensitiveWordSet = new HashSet<>();
        for (SensitiveWord sensitiveWord : sensitiveWords) {
            sensitiveWordSet.add(sensitiveWord.getWord());
        }
        SensitiveWordUtil.init(sensitiveWordSet);
    }
}
