package com.zhumj.preventmulticlick.aop;

import android.util.Log;
import android.view.View;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;

/**
 * @ProjectName : PreventMultiClick
 * @Author : zhumj
 * @Time : 2022/4/22 9:45
 * @Description : 防止短时间内重复点击 注解 切面
 */
@Aspect
public class PreventMultiClickAspect {

    //用来缓存最近点击过的
    private final LinkedHashMap<String, Long> linkedHashMap = new LinkedHashMap<String, Long>() {
        @Override
        protected boolean removeEldestEntry(Entry<String, Long> eldest) {
            return size() > 10;
        }
    };

    /**
     * 定义切点，标记切点为所有被 @PreventMultiClick 注解的方法
     * 注意：这里 com.zhumj.preventmulticlick.aop.PreventMultiClick 需要替换成你自己项目中 PreventMultiClick 这个类的全路径
     * pointcut指明了什么情况下执行切面方法，还有比如某些方法，某个路径的指定
     */
    @Pointcut("execution(@com.zhumj.preventmulticlick.aop.PreventMultiClick * *(..))")
    public void method() {}

    /**
     * 定义一个切面方法，包裹切点方法
     */
    @Around("method()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Log.d("1111111111111111", "PreventMultiClickAspect");
            Signature signature = joinPoint.getSignature();
            if (signature instanceof MethodSignature) {
                MethodSignature methodSignature = (MethodSignature) signature;
                Method method = methodSignature.getMethod();

                // 检查是否存在 PreventMultiClick 注解
                boolean hasAnnotation = method != null && method.isAnnotationPresent(PreventMultiClick.class);
                if (hasAnnotation) {
                    // 计算点击间隔，没有注解默认500，有注解按注解参数来，注解参数为空默认500；
                    PreventMultiClick preventMultiClick = method.getAnnotation(PreventMultiClick.class);
                    if (preventMultiClick != null) {
                        String key = String.valueOf(method.hashCode());
                        View view = getViewFromArgs(joinPoint.getArgs());
                        if (view != null) {
                            key += String.valueOf(view.hashCode());
                        }
                        long currentTime = System.currentTimeMillis();
                        //判断两次点击之间的时间
                        if (currentTime - getTime(key) > preventMultiClick.value()) {
                            //超过限定时间，直接执行点击事件
                            linkedHashMap.put(key, currentTime);
                            joinPoint.proceed();
                        }
                        // 检测间隔时间是否达到预设时间并且线程空闲
                    } else {
                        joinPoint.proceed();
                    }
                } else {
                    joinPoint.proceed();
                }
            } else {
                joinPoint.proceed();
            }

//            if (!AopClickUtil.isFilter) {
//                //如果关闭，就不拦截，直接执行
//                joinPoint.proceed();
//                return;
//            }
//
//            Signature signature = joinPoint.getSignature();
//            if (signature instanceof MethodSignature) {
//                MethodSignature methodSignature = (MethodSignature) signature;
//                Method method = methodSignature.getMethod();
//                // 如果有 AopClickExcept 注解，就不需要处理
//                boolean isExcept = method != null && method.isAnnotationPresent(AopClickExcept.class);
//                if (isExcept) {
//                    joinPoint.proceed();
//                    return;
//                }
//            }

        } catch (Throwable e) {
            e.printStackTrace();
            joinPoint.proceed();
        }
    }

    private View getViewFromArgs(Object[] args) {
        if (args != null && args.length > 0) {
            Object arg = args[0];
            if (arg instanceof View) {
                return (View) arg;
            }
        }
        return null;
    }

    private long getTime(String key) {
        if (linkedHashMap.containsKey(key)) {
            Long time = linkedHashMap.get(key);
            return time == null ? 0 : time;
        } else {
            return 0;
        }
    }

}
