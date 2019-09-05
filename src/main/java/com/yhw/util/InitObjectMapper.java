package com.yhw.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * The type Init object mapper.
 *
 * 单例 objectMapper
 * @author 杨怀伟
 * @date 2019 -02-13 16:49:33
 */
public class InitObjectMapper {

   public static ObjectMapper objectMapper = null;

   static {
           initOm ();
   }

    private static void initOm() {

          objectMapper = new ObjectMapper ();
        //当反序列化json时，未知属性会引起的反序列化被打断，这里我们禁用未知属性打断反序列化功能，
        //因为，例如json里有10个属性，而我们的bean中只定义了2个属性，其它8个属性将被忽略
        //mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 忽略json字符串中不识别的属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 忽略无法转换的对象 “No serializer found for class com.xxx.xxx”
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        //让Json可以缩进，可读性更好，一般用在测试和开发阶段。
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        //忽略大小写 2019年7月24日23:14:30 json字符串中是大写字母，用大写的属性去接受，收不到。
/*
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
*/
        //让map的key按自然顺序排列
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        //①如果属性没有值，那么Json是会处理的，int类型为0，String类型为null，数组为[]，设置这个特性可以忽略空值属性
        //②如果属性没有值，json直接不显示这个节点；如果布尔值为false或其他的null也不显示这个节点
        //NON_NULL、NON_EMPTY、NON_ABSENT执行① ； NON_DEFAULT执行②
       // objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);


    }

}
