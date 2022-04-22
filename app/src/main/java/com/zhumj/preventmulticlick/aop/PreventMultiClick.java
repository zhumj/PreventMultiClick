package com.zhumj.preventmulticlick.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ProjectName : PreventMultiClick
 * @Author : zhumj
 * @Time : 2022/4/22 9:42
 * @Description : 防止短时间内重复点击 注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PreventMultiClick {
    /* 点击间隔时间 */
    long value() default 500;
}
