package com.zhumj.preventmulticlick.aop;

import android.util.Log;
import android.view.View;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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
public class AopClickAspect {

    //用来缓存最近点击过的
    private final LinkedHashMap<String, Long> linkedHashMap = new LinkedHashMap<String, Long>() {
        @Override
        protected boolean removeEldestEntry(Entry<String, Long> eldest) {
            return size() > 10;
        }
    };

    /**
     * 拦截之前进行的操作
     */
    @Before("dealWithNormal()||dealWithLambda()||onClickInXmlPointcuts()")
    public void beforePoint(JoinPoint joinPoint) {
        //拦截时的日志打印
    }

    @Pointcut("execution(void com..lambda*(android.view.View))")
    public void dealWithLambda() {
    }

    @Pointcut("execution(void android.view.View.OnClickListener.onClick(..))")
    public void dealWithNormal() {
    }

    @Pointcut("execution(* android.support.v7.app.AppCompatViewInflater.DeclaredOnClickListener.onClick(..))")
    public void onClickInXmlPointcuts() {
    }

    /**
     * 定义一个切面方法，包裹切点方法
     */
    @Around("dealWithLambda() || dealWithNormal() || onClickInXmlPointcuts()")
    public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        try {

            if (!AopClickUtil.isEnable) {
                //如果关闭，就不拦截，直接执行
                joinPoint.proceed();
                return;
            }

            Log.d("1111111111111111", "AopClickAspect");

            Signature signature = joinPoint.getSignature();
            if (signature instanceof MethodSignature) {
                MethodSignature methodSignature = (MethodSignature) signature;
                Method method = methodSignature.getMethod();

                // 如果有 Except 注解，就不需要处理
                boolean isExcept = method != null && method.isAnnotationPresent(AopClickExcept.class);
                if (isExcept) {
                    joinPoint.proceed();
                } else {
                    String key = String.valueOf(method.hashCode());
                    View view = getViewFromArgs(joinPoint.getArgs());
                    if (view != null) {
                        key += String.valueOf(view.hashCode());
                    }
                    long currentTime = System.currentTimeMillis();
                    //判断两次点击之间的时间
                    if (currentTime - getTime(key) > AopClickUtil.intervalTime) {
                        //超过限定时间，直接执行点击事件
                        linkedHashMap.put(key, currentTime);
                        joinPoint.proceed();
                    }
                }
            } else {
                joinPoint.proceed();
            }

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
