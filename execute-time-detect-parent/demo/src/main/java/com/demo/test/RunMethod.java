package com.demo.test;

import com.github.zjimmy.annotation.TimeDetect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhongxiaojun
 * @date 2022/6/22 17:05
 * @desc
 */
@Component
public class RunMethod implements TestMethod{

	@Autowired
	RunMethod2 runMethod2;

	@TimeDetect(threshold = 300)
	public void test1() throws InterruptedException {
		Thread.sleep(500);
	}


	public void test2() throws InterruptedException {
		Thread.sleep(2000);
		this.test1();
	}

}
