package co.yiiu.hitbbs.directive;

import co.yiiu.hitbbs.model.Tag;
import co.yiiu.hitbbs.service.ITagService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
@Component
public class TagsDirective implements TemplateDirectiveModel {

    @Resource
    private ITagService tagService;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody
            templateDirectiveBody) throws TemplateException, IOException {
        Integer pageNo = Integer.parseInt(map.get("pageNo").toString());
        Integer pageSize = Integer.parseInt(map.get("pageSize").toString());
        IPage<Tag> page = tagService.selectAll(pageNo, pageSize, null);

        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28);
        environment.setVariable("page", builder.build().wrap(page));
        templateDirectiveBody.render(environment.getOut());
    }
}
