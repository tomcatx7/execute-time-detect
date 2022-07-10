package com.github.zjimmy.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhongxiaojun
 * @date 2022/6/24 9:54
 * @desc
 */
public class TimeDetectGlobalConfig {

	private static Map<String, Object> configMap = new ConcurrentHashMap<>();

	public static void put(String key, Object value) {
		configMap.put(key, value);
	}

	/**
	 * 获取开启扫描的包路径
	 */
	public static String[] getBasePackages() {
		return (String[]) configMap.getOrDefault("basePackages", new String[]{});
	}

	public static long getThreshold() {
		return (Long) configMap.getOrDefault("threshold", new Long(3000));
	}


}
