package co.yiiu.hitbbs.hook;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *
 */
public class TopicServiceHook {

    @Pointcut("execution(public * co.yiiu.hitbbs.service.ITopicService.search(..))")
    public void search() {
    }

    @Pointcut("execution(public * co.yiiu.hitbbs.service.ITopicService.selectById(..))")
    public void selectById() {
    }

    @Pointcut("execution(public * co.yiiu.hitbbs.service.ITopicService.update(..))")
    public void update() {
    }

    @Pointcut("execution(public * co.yiiu.hitbbs.service.ITopicService.vote(..))")
    public void vote() {
    }

    @Pointcut("execution(public * co.yiiu.hitbbs.service.ITopicService.updateViewCount(..))")
    public void updateViewCount() {
    }

}
