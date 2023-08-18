package co.yiiu.hitbbs.hook;

import org.aspectj.lang.annotation.Pointcut;

public class FileUtilHook {

    @Pointcut("execution(public * co.yiiu.hitbbs.util.FileUtil.upload(..))")
    public void upload() {
    }

}
