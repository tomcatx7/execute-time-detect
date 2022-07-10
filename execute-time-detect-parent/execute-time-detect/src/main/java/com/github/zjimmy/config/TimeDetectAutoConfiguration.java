package com.github.zjimmy.config;

import com.github.zjimmy.processer.TimeDetectBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author zhongxiaojun
 * @date 2022/6/22 17:20
 * @desc
 */
@Configuration
@Import({TimeDetectPropConfiguration.class})
public class TimeDetectAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public TimeDetectBeanPostProcessor TimeDetectBeanPostProcessor() {
		return new TimeDetectBeanPostProcessor();
	}

}



