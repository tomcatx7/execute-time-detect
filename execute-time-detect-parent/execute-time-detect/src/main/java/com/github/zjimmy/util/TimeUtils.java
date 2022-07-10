package com.github.zjimmy.util;

/**
 * @author zhongxiaojun
 * @date 2022/6/23 10:27
 * @desc
 */
public class TimeUtils {

	/**
	 * @param source source单位是ms
	 * @param unit   target 时间单位 ，参考TimeDetect TimeUnit取值
	 * @param target
	 * @return
	 */
	public static int compare(long source, int unit, long target) {
		switch (unit) {
			case 1:
				break;
			case 2:
				target = target * 1000;//s
				break;
			case 3:
				target = target * 1000 * 60;//min
				break;
			case 4:
				target = target * 1000 * 60 * 60;//hour
				break;
		}
		return Long.compare(source, target);
	}

	/**
	 * 根据输入的毫秒时间 输出对应的时间字符串
	 *
	 * @param totalMilliSeconds
	 * @return
	 */
	public static String getTimeStr(long totalMilliSeconds) {
		//获得系统的时间，单位为毫秒,转换为妙
		long totalSeconds = totalMilliSeconds / 1000;
		long currentMilliSeconds = totalMilliSeconds % 1000;
		//求出现在的秒
		long currentSecond = totalSeconds % 60;
		//求出现在的分
		long totalMinutes = totalSeconds / 60;
		long currentMinute = totalMinutes % 60;
		//求出现在的小时
		long totalHour = totalMinutes / 60;
		long currentHour = totalHour % 24;

		StringBuffer stringBuffer = new StringBuffer();
		if (currentHour > 0) {
			stringBuffer.append(currentHour).append(" Hour ");
		}
		if (currentMinute > 0) {
			stringBuffer.append(currentMinute).append(" min ");
		}
		if (currentSecond > 0) {
			stringBuffer.append(currentSecond).append(" s ");
		}
		if (currentMilliSeconds > 0) {
			stringBuffer.append(currentMilliSeconds).append(" ms");
		}
		return stringBuffer.toString();
	}

}
