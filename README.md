# execute-time-detect
基于SpringBoot扩展开发，监控Bean方法执行时间

+ 扩展BeanPostProcessor，对目标Bean进行代理

+ 添加注解@TimeDetect，自定义监控设置

## 项目结构 ##
+ demo -- 是一个基础的SpringBoot应用，作为客户端演示
+ execute-time-detect -- 基于SpringBoot扩展的方法时间监控

## 注解和配置 ##
+ @TimeDetect 注解
1. 加在Class类上，表示整个该类的实例方法都开启方法时间监控。
2. 也可直接作用在方法上。
3. timeUnit 表示监控时间单位，threshold 表示监控时间阈值，超过阈值就会触发事件

+ 全局配置
在application.yaml 文件开启全局配置
 ``` 
 time-detect:
        enable: true //是否开启全局扫描
        basePackages: com.demo // 扫描的包路径，多个路径用;隔开
        threshold: 2000 //时间阈值
 ```

## 自定义扩展监听器 ##

当监听方法超过阈值时会触发监听器，可以自定义扩展。
实现TimeDetectListener接口，并添加以下代码即可。
 ``` 
TimeDetectEventMulticaster.addListener(TimeDetectEvent.class, new TimeDetectTimeoutListener() //(自定义接口实现) )
 ```
