package com.yhw.manage.aspect;


import com.yhw.manage.env.webConfig.PlatformEnv;
import com.yhw.util.StringUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * pageobject层切面
 * 只能切到注入到spring的bean，无法切到new出来的方法
 *
 * @author 杨怀伟
 */
//TODO 进入每个页面时，判断展示正常，没有“网络或服务器异常”等类似告警
//声明这是一个组件
@Component
//声明这是一个切面Bean
@Aspect
public class PlatformAspect {
    Logger logger = Logger.getLogger (PlatformAspect.class);
//TODO 预置商户门店的餐道模式，清redis

    int loginTimes =0;//登录次数
    String remarkLocationPageClassName = "";//如果相邻两次切的location是同一个页面，那说明没必要重复执行before方法
    String remarkReportPageName = "";//如果相邻两次切的location是同一个页面，那说明没必要重复执行before方法

    //配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
    @Pointcut(value = "execution(* com.yhw.pageObject.location..*(..))) ")//获取页面xpath只能经过location中的方法，所以只切location中的方法就够了
    //@Pointcut(value = "within(com.yhw..*) && !execution(* com.yhw.pageObject.location.platform.*.beforeMethod(..))")
    public void pointCut() {}

    //再来一个切点，切orderManageOperation。在所有方法执行之前，先执行openConditionSetting
    @Pointcut(value = "execution(public * com.yhw.pageObject.operation..*(..))")
    public void orderManageCut() {}

/*
    @Around(value = "pointCut()")
    public void action(ProceedingJoinPoint joinPoint) { }
    */


    @Before(value = "pointCut()")
    public void before1_loginTakeout2(JoinPoint joinPoint) {
        if (loginTimes == 0) {//登录外卖，只需要执行一次即可

            logger.info ("触发登录外卖2的方法是"+((MethodSignature)joinPoint.getSignature()).getMethod().getName());
            PlatformEnv.openChrome = true;

         /*   LoginPage.loginPlatform ();
            LoginPage.loginTakeout2 ();*/

            loginTimes =1;
        }
    }

    //TODO 表中记录页面关联、关键节点，自动判断并跳入。（2019年5月20日23:33:42本想在此根据LeftMenuEnum来跳入页面的，但考虑到还有下钻页面，无法标记，暂放）
    @Before(value = "pointCut()")
    public void before2_execBeforeMethod(JoinPoint joinPoint) {
        Object o = joinPoint.getTarget ();
        Class c = joinPoint.getSignature ().getDeclaringType ();

        //2019年7月30日00:16:04 同样的orderReport类，其实是好几个页面。
        boolean diffPage = false;
       /* if (ReportEnv.reportPage!=null && !StringUtil.equals (ReportEnv.reportPage.getMenuName (), remarkReportPageName)) {
            diffPage = true;
            remarkReportPageName = ReportEnv.reportPage.getMenuName ();
        }*/

        String sourceClassName = joinPoint.getSignature ().getDeclaringTypeName ();

        if ( !sourceClassName.equals (remarkLocationPageClassName) || diffPage) {//如果所切方法所在类的全限定名与之前记录的不符，需要重新执行这个类的beforeMethod方法
            logger.info ("触发进入操作页的方法："+((MethodSignature)joinPoint.getSignature()).getMethod().getName());

            try {

            Method m = c.getDeclaredMethod ("beforeMethod");

            m.setAccessible(true);//解除私有限定
            m.invoke (o);
            remarkLocationPageClassName = sourceClassName;

        } catch (Exception e) {

            System.out.println(joinPoint.getTarget().getClass().getName()+"__" +joinPoint.getSignature().getName()+":没有beforeMethod方法");

        }
    }
    }

    @Before(value = "orderManageCut()")
    public void before_orderManage(JoinPoint joinPoint) {
        //由于必然是先走operation再走location方法。所以当再次进入订单管理页面是，location依然记录的是其他页面的classname。operation就能判断到 !equal，就会点击“展开”
        String sourceClassName = joinPoint.getSignature ().getDeclaringTypeName (); //全限定名

      //外卖单管理页，才有‘更多’；重复进入此页面无需再次点击‘更多’；考虑在report的外卖单管理操作后，点了商户外卖管理(此时reprotEnv是始终记录着外卖单管理)，又再次进入外卖单管理。故还需判断类名
     /* if (ReportEnv.reportPage!=null && StringUtil.equals (ReportEnv.reportPage.getMenuName (),LeftMenuPageContent.order.getMenuName ()) && !StringUtil.equals (ReportEnv.reportPage.getMenuName (),remarkReportPageName) && !StringUtil.contains (remarkLocationPageClassName,"orderReport")) {//每次进入此页面执行一次即可
        //执行某个方法
        Object obj = joinPoint.getTarget ();
        Class cutClass = obj.getClass ();
        try {
            Method m = cutClass.getDeclaredMethod ("openConditionSetting");
            m.setAccessible (true);
            m.invoke (obj);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        }*/
    }

  /*  //配置后置通知,使用在方法aspect()上注册的切入点
    @After(value = "pointCut()")
    public void after() {
        System.out.println("after");
    }*/
}
