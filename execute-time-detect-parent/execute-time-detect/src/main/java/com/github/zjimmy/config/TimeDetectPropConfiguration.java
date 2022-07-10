package com.github.zjimmy.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author zhongxiaojun
 * @date 2022/6/23 16:51
 * @desc
 */
@ConfigurationProperties(prefix = "time-detect")
@Configuration
public class TimeDetectPropConfiguration implements InitializingBean {

	@Value("${time-detect.enable:false}")
	private boolean enable;

	/**
	 * 配置自动开启方法超时监控的包路径,多路径用分号隔开
	 */
	@Value("${time-detect.basePackages}")
	private String basePackages;

	/**
	 * 方法超时阈值 默认时间单位为ms
	 */
	@Value("${time-detect.threshold:3000}")
	private long threshold;

	public String[] getBasePackages() {
		return StringUtils.hasLength(basePackages) ? basePackages.split(";") : new String[]{};
	}

	public void setBasePackages(String basePackages) {
		this.basePackages = basePackages;
	}

	public long getThreshold() {
		return threshold;
	}

	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		TimeDetectGlobalConfig.put("basePackages",getBasePackages());
		TimeDetectGlobalConfig.put("threshold",getThreshold());
	}
}
