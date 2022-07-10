package com.github.zjimmy.listener;

/**
 * @author zhongxiaojun
 * @date 2022/6/23 10:59
 * @desc 当方法执行超过设置的阈值，就会触发监听
 */
public class TimeDetectTimeoutListener implements TimeDetectListener {

	@Override
	public void onListen(TimeDetectEvent event) {
		System.out.println("打印超时方法" + event.getClazz().getName() + "." + event.getMethod().getName() + ": " + event.getMsg());
	}
}
