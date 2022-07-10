package com.github.zjimmy.listener;

import java.lang.reflect.Method;

/**
 * @author zhongxiaojun
 * @date 2022/6/23 10:55
 * @desc
 */
public class TimeDetectEvent {

	private Class<?> clazz;

	private Method method;

	private String msg;

	public TimeDetectEvent() {
	}

	public TimeDetectEvent(Class<?> clazz, Method method, String msg) {
		this.clazz = clazz;
		this.method = method;
		this.msg = msg;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
