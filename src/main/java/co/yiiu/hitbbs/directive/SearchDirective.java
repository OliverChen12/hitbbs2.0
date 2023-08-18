package co.yiiu.hitbbs.directive;

import co.yiiu.hitbbs.service.ISystemConfigService;
import co.yiiu.hitbbs.service.ITopicService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
@Component
public class SearchDirective implements TemplateDirectiveModel {

    @Resource
    private ISystemConfigService systemConfigService;
    @Resource
    private ITopicService topicService;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody
            templateDirectiveBody) throws TemplateException, IOException {
        Page<Map<String, Object>> page = new Page<>();
        String keyword = String.valueOf(map.get("keyword"));
        Integer pageNo = Integer.parseInt(map.get("pageNo").toString());
        if (!StringUtils.isEmpty(keyword)) {
            Integer pageSize = Integer.parseInt(systemConfigService.selectAllConfig().get("page_size").toString());
//      page = elasticSearchService.searchDocument(pageNo, pageSize, keyword, "title", "content");
            page = topicService.search(pageNo, pageSize, keyword);
        }

        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28);
        environment.setVariable("page", builder.build().wrap(page));
        templateDirectiveBody.render(environment.getOut());
    }
}
