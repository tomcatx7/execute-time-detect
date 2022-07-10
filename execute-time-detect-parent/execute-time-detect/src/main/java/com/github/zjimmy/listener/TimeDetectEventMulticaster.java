package com.github.zjimmy.listener;

import org.springframework.lang.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * @author zhongxiaojun
 * @date 2022/6/23 11:28
 * @desc 事件分发器
 */
public class TimeDetectEventMulticaster {

	/**
	 * 任务执行器
	 */
	@Nullable
	private static Executor taskExecutor;

	private static Map<Class, List<TimeDetectListener>> listenerCache = new ConcurrentHashMap<>();

	/**
	 * 初始化默认的listener
	 */
	static {
		initListener();
	}

	private static void initListener() {
		addListener(TimeDetectEvent.class, new TimeDetectTimeoutListener());
	}

	public static void setTaskExecutor(Executor executor) {
		taskExecutor = executor;
	}

	public static void addListener(Class<?> eventType, TimeDetectListener listener) {
		listenerCache.computeIfAbsent(eventType,(key)-> new ArrayList<>()).add(listener);
	}

	public static void multicastEvent(TimeDetectEvent timeDetectEvent) {
		List<TimeDetectListener> timeDetectListeners = listenerCache.get(timeDetectEvent.getClass());
		for (TimeDetectListener timeDetectListener : timeDetectListeners) {
			if (taskExecutor != null) {
				taskExecutor.execute(() -> timeDetectListener.onListen(timeDetectEvent));
			} else {
				timeDetectListener.onListen(timeDetectEvent);
			}
		}
	}

}
