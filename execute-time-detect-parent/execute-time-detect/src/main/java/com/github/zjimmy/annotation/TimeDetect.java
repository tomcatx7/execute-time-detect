package com.github.zjimmy.annotation;

import java.lang.annotation.*;

/**
 * @author zhongxiaojun
 * @date 2022/6/22 14:59
 * @desc
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TimeDetect {
	/**
	 * 1 毫秒
	 * 2 秒
	 * 3 分
	 * 4 小时
	 */
	int timeUnit() default 1;

	/**
	 * 监控阈值，执行超过这个时间就会触发监听器
	 */
	long threshold() default 1000;
}
