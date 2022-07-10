package com.github.zjimmy.processer;

import com.github.zjimmy.config.TimeDetectPropConfiguration;
import com.github.zjimmy.intecepter.TimeDetectInterceper;
import com.github.zjimmy.util.ClassUtils;
import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;

import javax.websocket.server.ServerEndpoint;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * @author zhongxiaojun
 * @date 2022/6/22 15:01
 * @desc 处理@TimeDetect注解 生成代理对象
 */
public class TimeDetectBeanPostProcessor extends AbstractAutoProxyCreator implements ApplicationContextAware {

	TimeDetectPropConfiguration timeDetectPropConfiguration;

	/**
	 * 采用动态代理 可能会导致注解丢失的问题，这里只处理了 @ServerEndpoint ，后续如果还有 其他注解问题可以采用扩展
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		handleServerEndPoint(bean);
		return bean;
	}

	private void handleServerEndPoint(Object bean) {
		//获取serverEndpoint
		ServerEndpoint serverEndpoint = AnnotationUtils.findAnnotation(bean.getClass(), ServerEndpoint.class);
		if (!Objects.isNull(serverEndpoint)) {
			//设置@ServerEndpoint注解支持继承，相当于注解@Inherited，应对动态代理导致类上的@ServerEndpoint注解丢失
			InvocationHandler h = Proxy.getInvocationHandler(serverEndpoint);
			try {
				Field typeField = h.getClass().getDeclaredField("type");
				typeField.setAccessible(true);
				Field annotationTypeField = Class.class.getDeclaredField("annotationType");
				annotationTypeField.setAccessible(true);
				Object o = annotationTypeField.get(typeField.get(h));
				Field inheritedField = o.getClass().getDeclaredField("inherited");
				ClassUtils.updateFinalModifiers(inheritedField);
				inheritedField.set(o, true);

			} catch (NoSuchFieldException | IllegalAccessException e) {
				throw new RuntimeException("修改@ServerEndPoint注解失败");
			}
		}
	}

	/**
	 * 自定义ProxyFactory，这里设置暴露代理对象为 targetClass ，默认采用cglib实现
	 * @param proxyFactory
	 */
	@Override
	protected void customizeProxyFactory(ProxyFactory proxyFactory) {
		proxyFactory.setProxyTargetClass(true);
	}

	@Override
	protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
		try {
			if (!ClassUtils.hasTimeDetectAnnotation(bean) && !enbleGlobalConfig(this.timeDetectPropConfiguration, ClassUtils.findTargetClass(bean))) {
				return bean;
			}
			//如果是非代理对象，则去执行默认的代理方法
			if (!AopUtils.isAopProxy(bean)) {
				return super.wrapIfNecessary(bean, beanName, cacheKey);
			}
			//如果已经是代理对象了，则这里再次进行增强
			AdvisedSupport advisedSupport = ClassUtils.getAdvisedSupport(bean);
			if (null != advisedSupport) {
				Advisor[] advisors = this.buildAdvisors(beanName, getAdvicesAndAdvisorsForBean(null, null, null));
				for (Advisor advisor : advisors) {
					advisedSupport.addAdvisor(0,advisor);
				}
			}
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 这里添加自定义的拦截面，进行增强
	 *
	 * @param beanClass
	 * @param beanName
	 * @param customTargetSource
	 * @return
	 * @throws BeansException
	 */
	@Override
	protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {
		return new Object[]{new TimeDetectInterceper()};
	}

	/**
	 * 是否开启了默认配置
	 */
	private boolean enbleGlobalConfig(TimeDetectPropConfiguration configuration, Class beanClazz) {
		if (configuration.getBasePackages() != null && configuration.getBasePackages().length > 0) {
			for (String basePackage : configuration.getBasePackages()) {
				if (org.springframework.util.ClassUtils.getPackageName(beanClazz).contains(basePackage)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.timeDetectPropConfiguration = applicationContext.getBean(TimeDetectPropConfiguration.class);
		Assert.notNull(timeDetectPropConfiguration, "can not find bean timeDetectPropConfiguration");
	}
}
