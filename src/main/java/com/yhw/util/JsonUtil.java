package com.yhw.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtil {

   static ObjectMapper mapper = InitObjectMapper.objectMapper;

    public static <T>String object2String(T t) {
        if (t instanceof String) {

            return StringUtil.removeNl ((String) t);
        }

        try {
           return StringUtil.removeNl (mapper.writeValueAsString (t));
        } catch (JsonProcessingException e) {
            e.printStackTrace ();
            return null;
        }
    }
    public static <F,T> Map<F,T> object2Map(Object object) {
        //① 2String
        String str = object2String (object);

        //② 2Map
        try {
            JavaType javaType = mapper.getTypeFactory ().constructParametricType (Map.class,Object.class , Object.class);
            Map<F,T> map = mapper.readValue (str, javaType);
            return map;
        } catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
    }

    public static <T> List<T> object2List(Object object,Class<T> tClass) {
        //① 2String
        String str = object2String (object);

        //② 2List
        List<T> tList = null;
        try {
            JavaType javaType = mapper.getTypeFactory ().constructParametricType (List.class, tClass);
            tList = mapper.readValue (str, javaType);
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return tList;
    }

    public  static  <T> T object2Bean(Object object,T bean){

        //2string
       String str = object2String (object);

       //2bean
        try {
            bean = (T)mapper.readValue (str, bean.getClass ());
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return bean;

    }


    public  static  <T> T object2Bean(Object object,Class<T> tClass){
        //2string
       String str = object2String (object);
        T bean = null;
        //2bean
        try {
            bean = mapper.readValue (str, tClass);
        } catch (Exception e) {
            e.printStackTrace ();
        }

        return bean;

    }
}
