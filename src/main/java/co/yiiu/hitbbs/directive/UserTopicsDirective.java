package co.yiiu.hitbbs.directive;

import co.yiiu.hitbbs.model.User;
import co.yiiu.hitbbs.service.ITopicService;
import co.yiiu.hitbbs.service.IUserService;
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
public class UserTopicsDirective implements TemplateDirectiveModel {

    @Resource
    private ITopicService topicService;
    @Resource
    private IUserService userService;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody
            templateDirectiveBody) throws TemplateException, IOException {
        String username = String.valueOf(map.get("username"));
        Integer pageNo = Integer.parseInt(map.get("pageNo").toString());
        Integer pageSize = map.get("pageSize") == null ? null : Integer.parseInt(map.get("pageSize").toString());
        User user = userService.selectByUsername(username);
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28);
        environment.setVariable("topics", builder.build().wrap(topicService.selectByUserId(user.getId(), pageNo,
                pageSize)));
        templateDirectiveBody.render(environment.getOut());
    }
}
