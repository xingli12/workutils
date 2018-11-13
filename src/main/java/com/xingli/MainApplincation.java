package com.xingli;


import com.xingli.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/12/18.
 */
@SpringBootApplication
@RestController
//@EnableEurekaClient
//@ComponentScan(basePackages = { "com.iflytek.fsp.flylog.sdk.java","com.iflytek"})
//@EnableFeignClients
//@EnableHystrix
public class MainApplincation {
    public static void main(String[] args) {
        ApplicationContext app = SpringApplication.run(MainApplincation.class, args);
        SpringContextUtil.setApplicationContext(app);
    }

}
