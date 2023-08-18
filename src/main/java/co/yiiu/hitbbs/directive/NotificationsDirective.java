package co.yiiu.hitbbs.directive;

import co.yiiu.hitbbs.service.INotificationService;
import freemarker.core.Environment;
import freemarker.template.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
@Component
public class NotificationsDirective implements TemplateDirectiveModel {

    @Resource
    private INotificationService notificationService;

    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody
            templateDirectiveBody) throws TemplateException, IOException {
        Integer userId = Integer.parseInt(map.get("userId").toString());
        Boolean read = Integer.parseInt(map.get("read").toString()) == 1;
        // 如果想查询所有的消息，limit 传一个负数就可以了 比如 -1
        Integer limit = Integer.parseInt(map.get("limit").toString());
        List<Map<String, Object>> notifications = notificationService.selectByUserId(userId, read, limit);
        DefaultObjectWrapperBuilder builder = new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_28);
        environment.setVariable("notifications", builder.build().wrap(notifications));
        templateDirectiveBody.render(environment.getOut());
    }
}
