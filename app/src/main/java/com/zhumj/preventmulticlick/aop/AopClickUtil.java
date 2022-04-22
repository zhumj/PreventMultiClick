package com.zhumj.preventmulticlick.aop;

/**
 * @ProjectName : PreventMultiClick
 * @Author : zhumj
 * @Time : 2022/4/22 10:28
 * @Description : 文件描述
 */
public class AopClickUtil {
    /**
     * 控制是否启用 AOP 防重复点击功能
     */
    static boolean isEnable = true;
    /**
     * 控制点击间隔时间
     */
    static long intervalTime = 500;

    /**
     * 开始拦截
     */
    public static void enable() {
        isEnable = true;
    }

    /**
     * 停止拦截
     */
    public static void disable() {
        isEnable = false;
    }

    public static void setEnable(boolean enable) {
        if (enable) {
            AopClickUtil.enable();
        } else {
            AopClickUtil.disable();
        }
    }


    /**
     * 设置两次点击事件之间的间隔
     */
    public static void setIntervalTime(long intervalTime) {
        AopClickUtil.intervalTime = intervalTime;
    }
}
