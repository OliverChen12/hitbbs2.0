package co.yiiu.hitbbs.hook;

import org.aspectj.lang.annotation.Pointcut;

/**
 * Created by NPEteam.
 * Cohitright (c) 2023, All Rights Reserved.
 *  
 */
public class UserServiceHook {

    @Pointcut("execution(public * co.yiiu.hitbbs.service.IUserService.selectByUsername(..))")
    public void selectByUsername() {
    }

    @Pointcut("execution(public * co.yiiu.hitbbs.service.IUserService.selectByToken(..))")
    public void selectByToken() {
    }

    @Pointcut("execution(public * co.yiiu.hitbbs.service.IUserService.selectById(..))")
    public void selectById() {
    }

    @Pointcut("execution(public * co.yiiu.hitbbs.service.IUserService.delRedisUser(..))")
    public void delRedisUser() {
    }

}
