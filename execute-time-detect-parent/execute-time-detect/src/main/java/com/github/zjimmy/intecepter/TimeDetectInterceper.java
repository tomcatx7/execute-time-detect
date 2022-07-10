package com.github.zjimmy.intecepter;

import com.github.zjimmy.config.TimeDetectGlobalConfig;
import com.github.zjimmy.listener.TimeDetectEvent;
import com.github.zjimmy.util.TimeUtils;
import com.github.zjimmy.annotation.TimeDetect;
import com.github.zjimmy.listener.TimeDetectEventMulticaster;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author zhongxiaojun
 * @date 2022/6/22 16:07
 * @desc 自定义拦截面
 */
public class TimeDetectInterceper implements MethodInterceptor {


	private final Logger logger = LoggerFactory.getLogger(TimeDetectInterceper.class);

	private Class getInterceperAnnotation() {
		return TimeDetect.class;
	}

	/**
	 * 代理对象进行方法增强
	 *
	 * @param invocation
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Class<?> target = invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null;
		Method method = ClassUtils.getMostSpecificMethod(invocation.getMethod(), target);
		//如果注解是加载class上，则表示开启了所有方法的拦截
		Annotation annotation = AnnotationUtils.findAnnotation(target, getInterceperAnnotation());
		if (annotation != null || (annotation = method.getAnnotation(getInterceperAnnotation())) != null) {
			int timeUnit = (Integer) AnnotationUtils.getAnnotationAttributes(annotation).get("timeUnit");
			long threshold = (Long) AnnotationUtils.getAnnotationAttributes(annotation).get("threshold");
			return enhance(invocation, timeUnit, threshold);
		} else {
			//如果没有注解信息，就走全局配置
			return enhance(invocation, 1, TimeDetectGlobalConfig.getThreshold());
		}
	}

	private Object enhance(MethodInvocation invocation, int timeUnit, long threshold) throws Throwable {
		long startTime = System.currentTimeMillis();
		try {
			return invocation.proceed();
		} catch (Throwable e) {
			throw e;
		} finally {
			long endTime = System.currentTimeMillis();
			long executeTime = endTime - startTime;
			String timeFormat = TimeUtils.getTimeStr(executeTime);
			logger.debug("execute " + invocation.getThis().getClass() + "." + invocation.getMethod().getName() + " time : " + timeFormat);
			//执行事件超过设定的阈值，触发事件分发
			if (TimeUtils.compare(executeTime, timeUnit, threshold) > 0) {
				logger.debug("execute method time too long , start multicastEvent");
				TimeDetectEventMulticaster.multicastEvent(new TimeDetectEvent(invocation.getThis().getClass(), invocation.getMethod(), timeFormat));
			}
		}
	}
}
