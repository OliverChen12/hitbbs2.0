package co.yiiu.hitbbs.hook;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *
 */
public class IndexedServiceHook {

    @Pointcut("execution(public * co.yiiu.hitbbs.service.IIndexedService.indexAllTopic(..))")
    public void indexAllTopic() {
    }

    @Pointcut("execution(public * co.yiiu.hitbbs.service.IIndexedService.indexTopic(..))")
    public void indexTopic() {
    }

    @Pointcut("execution(public * co.yiiu.hitbbs.service.IIndexedService.deleteTopicIndex(..))")
    public void deleteTopicIndex() {
    }

    @Pointcut("execution(public * co.yiiu.hitbbs.service.IIndexedService.batchDeleteIndex(..))")
    public void batchDeleteIndex() {
    }

}
