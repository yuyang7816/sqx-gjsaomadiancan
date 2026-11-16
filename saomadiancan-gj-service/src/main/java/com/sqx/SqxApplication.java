package com.sqx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class SqxApplication {

	public static void main(String[] args) {
		SpringApplication.run(SqxApplication.class, args);
		System.out.println("(♥◠‿◠)ﾉﾞ  外卖点餐项目启动成功   ლ(´ڡ`ლ)ﾞ  \n"+
							"       _    \n" +
							"      | |   \n" +
							"  ___ | | __\n" +
							" / _ \\| |/ /\n" +
							"| (_) |   < \n" +
							" \\___/|_|\\_\\");

	}

}