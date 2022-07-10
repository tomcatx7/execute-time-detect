package com.github.zjimmy.util;

import com.github.zjimmy.annotation.TimeDetect;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.target.EmptyTargetSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author zhongxiaojun
 * @date 2022/6/22 15:52
 * @desc
 */
public class ClassUtils {

	/**
	 * 检查bean 方法上是否包含TimeDetect注解
	 *
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static boolean hasTimeDetectAnnotation(Object bean) throws Exception {

		Class<?> targetClass = findTargetClass(bean);
		Assert.notNull(targetClass, "can not find target class " + bean.getClass().getName());
		//先从类上找注解
		if (AnnotationUtils.findAnnotation(targetClass, TimeDetect.class) != null) {
			return true;
		}
		//找方法上的注解
		Method[] methods = targetClass.getMethods();
		return Arrays.stream(methods).anyMatch(m -> Objects.nonNull(m.getAnnotation(TimeDetect.class)));
	}

	//获取代理对象中的advise
	public static AdvisedSupport getAdvisedSupport(Object proxy) throws Exception {
		Field h;
		if (AopUtils.isJdkDynamicProxy(proxy)) {
			h = proxy.getClass().getSuperclass().getDeclaredField("h");
		} else {
			h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
		}
		h.setAccessible(true);
		Object dynamicAdvisedInterceptor = h.get(proxy);
		try {
			Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
			advised.setAccessible(true);
			return (AdvisedSupport) advised.get(dynamicAdvisedInterceptor);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 找到代理对象的TargetClass
	 *
	 * @param proxy
	 * @return
	 * @throws Exception
	 */
	public static Class<?> findTargetClass(Object proxy) throws Exception {
		AdvisedSupport advised;
		if (AopUtils.isAopProxy(proxy) && (advised = getAdvisedSupport(proxy)) != null) {
			if (AopUtils.isJdkDynamicProxy(proxy)) {
				TargetSource targetSource = advised.getTargetSource();
				return targetSource instanceof EmptyTargetSource ? getFirstInterfaceByAdvised(advised) : targetSource.getTargetClass();
			} else {
				Object target = advised.getTargetSource().getTarget();
				return findTargetClass(target);
			}
		} else {
			return proxy == null ? null : proxy.getClass();
		}
	}

	private static Class<?> getFirstInterfaceByAdvised(AdvisedSupport advised) {
		Class<?>[] interfaces = advised.getProxiedInterfaces();
		if (interfaces.length > 0) {
			return interfaces[0];
		} else {
			throw new IllegalStateException("Find the jdk dynamic proxy class that does not implement the interface");
		}
	}

	public static void updateFinalModifiers(Field field) throws NoSuchFieldException, IllegalAccessException {
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & Modifier.FINAL);
	}

}
