package co.yiiu.hitbbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "co.yiiu.hitbbs",
        exclude = {DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy
public class HitbbsApplication {
    public static void main(String[] args) {
        SpringApplication.run(HitbbsApplication.class, args);
    }
}
