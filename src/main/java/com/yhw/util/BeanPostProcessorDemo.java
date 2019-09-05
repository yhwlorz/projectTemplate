package com.yhw.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class BeanPostProcessorDemo implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println ("正要初始化的bean是："+beanName+"=============="+bean+"---------"+bean.toString ()+"======="+bean.hashCode ());
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println ("已经完成了初始化的bean是："+beanName);

        return bean;
    }
}
