package com.yhw.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil extends StringUtils {

    //有一个相等，就是true
    public static boolean isEqualOne(String str,String[] compareStrs) {
        boolean isEqualOne = false;
        for (int i=0;i<compareStrs.length;i++) {
            if (equals (str, compareStrs[i])) {//有一个相等就行
                isEqualOne = true;
                break;
            }
        }
        return isEqualOne;
    }

    /**
     * 删除 \n\r换行
     *
     * @param str the str
     *
     * @return the string
     */
    public static String removeNl(String str) {

        String patternString = "\r\n\\s*";
        Pattern pattern = Pattern.compile (patternString);
        Matcher matcher = pattern.matcher (str);

        while (matcher.find ()) {
            //matcher替换，是在orderString基础上进行所有符合条件数据的替换
            str = matcher.replaceAll ("");
        }
        return str;
    }

    public static boolean containOne(String orginStr, String... constant) {
        for (int i=0;i<constant.length;i++) {
            if (orginStr.contains (constant[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 使首字母小写
     *
     * @return the string
     */
    public static String toLowerFirstChar(String str) {

        char[] chars = new char[1];
        chars[0] = str.charAt(0);
        String temp = new String(chars);
        if (chars[0] >= 'A' && chars[0] <= 'Z') {//当为字母时，则转换为小写
         str = str.replaceFirst(temp, temp.toLowerCase());
        }
        return str;
    }

    /**
     * 如 mtOrderBean 返回mt
     * 如果是 MtOrderBean 则返回“Mt”
     * @return the string
     */
    public static String firstCamel(String str) {
        int index =-1;
        for (int i = 0; i < str.length (); i++) {
            if (Character.isUpperCase (str.charAt (i))) {
                if (i == 0) {
                    continue;
                } else {
                    index = i;
                    break;
                }
            }
        }
        if (index != -1) {
            return str.substring (0, index);
        }
        return null;
    }
}
