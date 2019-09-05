package com.yhw.manage.aspect;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

//@Component
@Aspect
public class ExceptionAspect {

    @Pointcut(value = "execution(* com.yhw..*.*(..))) !execution(* com.yhw.manage.aspect.*.*(..))")
    public void pointCut() {

    }


    @AfterThrowing(pointcut = "pointCut()",throwing = "e")
    public void afterException(JoinPoint joinPoint, Throwable e) {
      /*  DynamicDataSourceHolder.setDdType(DynamicDataSourceHolder.DATASOURCE_TAKEOUT_LOCAL);
        BasesetCaseBeanInput basesetCaseBean = new BasesetCaseBeanInput();
        basesetCaseBean.setCaseid(901);
        BasesetCaseInput basesetCaseInput = new BasesetCaseInput();
        basesetCaseInput.insertInputResult(basesetCaseBean,"抛异常了");*/

        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

       /* Method m = c.getDeclaredMethod ("preIntoTabBar",String.class);

        m.setAccessible(true);//解除私有限定
        m.invoke (o,keyName);*/
        //开始打log
       // System.out.println("异常:" + e.getMessage());
        System.out.println("异常所在方法：" + className +":"+ methodName);



    }
}
