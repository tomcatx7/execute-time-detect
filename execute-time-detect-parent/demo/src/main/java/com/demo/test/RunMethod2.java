package com.demo.test;

import org.springframework.stereotype.Component;

/**
 * @author zhongxiaojun
 * @date 2022/6/23 10:11
 * @desc
 */

@Component
public class RunMethod2 implements TestMethod {

	public void test1() throws InterruptedException {
		Thread.sleep(1000);
	}

	public void test2() throws InterruptedException {
		Thread.sleep(2000);
	}
}
