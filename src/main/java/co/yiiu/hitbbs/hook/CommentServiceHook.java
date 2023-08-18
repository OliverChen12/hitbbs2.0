package co.yiiu.hitbbs.hook;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
public class CommentServiceHook {

    @Pointcut("execution(public * co.yiiu.hitbbs.service.ICommentService.selectByTopicId(..))")
    public void selectByTopicId() {
    }

    @Pointcut("execution(public * co.yiiu.hitbbs.service.ICommentService.insert(..))")
    public void insert() {
    }

    @Pointcut("execution(public * co.yiiu.hitbbs.service.ICommentService.update(..))")
    public void update() {
    }

    @Pointcut("execution(public * co.yiiu.hitbbs.service.ICommentService.vote(..))")
    public void vote() {
    }

    @Pointcut("execution(public * co.yiiu.hitbbs.service.ICommentService.delete(..))")
    public void delete() {
    }

}
