package com.demo;

import com.demo.test.RunMethod;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author zhongxiaojun
 * @date 2022/6/22 16:44
 * @desc
 */
@SpringBootApplication
public class Application {

	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);
		RunMethod runMethod = (RunMethod)applicationContext.getBean("runMethod");
		runMethod.test1();
		runMethod.test2();
	}
}
