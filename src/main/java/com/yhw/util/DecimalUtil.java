package com.yhw.util;

import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DecimalUtil {

    //默认的保留位数
    final static int POINT = 2;

    //获取较小的额数
    public static double lesserDouble(double d1,double d2) {
       int result =  compare (d1, d2);

        if (result >= 0) {
            return d2;
        } else {
            return d1;
        }
    }
    public static int lesserInt(int d1,int d2) {
       int result =  compare (d1, d2);

        if (result >= 0) {
            return d2;
        } else {
            return d1;
        }
    }
    public static double moreerDouble(double d1,double d2) {
       int result =  compare (d1, d2);

        if (result >= 0) {
            return d1;
        } else {
            return d2;
        }
    }

    //随机数 四舍五入
    public static float nextFloat() {
        float f = RandomUtils.nextFloat ();
        String s = format4down5up (f);
        return DecimalUtil.string2float (s);
    }
    public static float nextFloat(float min,float max) {
        float f = RandomUtils.nextFloat (min,max);
        String s = format4down5up (f);
        return DecimalUtil.string2float (s);
    }
    public static double nextDouble() {
        double d = RandomUtils.nextDouble ();
        String s = format4down5up (d);
        return DecimalUtil.string2double (s);
    }
    public static double nextDouble(double min,double max) {
        double d = RandomUtils.nextDouble (min,max);
        return format4down5up2Double (d);
    }


    /**
     * 格式化金额。doubleToString 保留 0-2位
     *
     * @param v
     *
     * @return
     */
    public static String moneyFormat(Double v) {
        DecimalFormat formater = new DecimalFormat ();
        //设置数的小数部分所允许的最大位数，避免小数位被舍掉
        formater.setMaximumFractionDigits (2);
        //设置数的小数部分所允许的最小位数，避免小数位有多余的0
        formater.setMinimumFractionDigits (2);
        //去掉科学计数法显示，避免显示为111,111,111,111
        formater.setGroupingUsed (false);

        //formater.setGroupingSize (3); //设置分组大小。分组大小是数的整数部分中分组分隔符之间的数字位数。例如在数 "123,456.78" 中，分组大小是 3。传入的值被转换为 一个字节，这可能导致信息丢失。
        //formater.setRoundingMode (RoundingMode.FLOOR);  //https://blog.csdn.net/alanzyy/article/details/8465098
        return formater.format (v.doubleValue ());
    }
    public static String moneyFormat(Float v) {
        DecimalFormat formater = new DecimalFormat ();
        //设置数的小数部分所允许的最大位数，避免小数位被舍掉
        formater.setMaximumFractionDigits (2);
        //设置数的小数部分所允许的最小位数，避免小数位有多余的0
        formater.setMinimumFractionDigits (2);
        //去掉科学计数法显示，避免显示为111,111,111,111
        formater.setGroupingUsed (false);

        //formater.setGroupingSize (3); //设置分组大小。分组大小是数的整数部分中分组分隔符之间的数字位数。例如在数 "123,456.78" 中，分组大小是 3。传入的值被转换为 一个字节，这可能导致信息丢失。
        //formater.setRoundingMode (RoundingMode.FLOOR);  //https://blog.csdn.net/alanzyy/article/details/8465098
        return formater.format (v.floatValue ());
    }
    public static String moneyFormat(String v) {
        DecimalFormat formater = new DecimalFormat ();
        //设置数的小数部分所允许的最大位数，避免小数位被舍掉
        formater.setMaximumFractionDigits (2);
        //设置数的小数部分所允许的最小位数，避免小数位有多余的0
        formater.setMinimumFractionDigits (2);
        //去掉科学计数法显示，避免显示为111,111,111,111
        formater.setGroupingUsed (false);

        //formater.setGroupingSize (3); //设置分组大小。分组大小是数的整数部分中分组分隔符之间的数字位数。例如在数 "123,456.78" 中，分组大小是 3。传入的值被转换为 一个字节，这可能导致信息丢失。
        //formater.setRoundingMode (RoundingMode.FLOOR);  //https://blog.csdn.net/alanzyy/article/details/8465098
        return formater.format(v);
    }


    /**
     * 带小数的显示小数。不带小数的显示整数
     *
     * @param d
     *
     * @return
     */
    public static String doubleTrans(Double d) {
        if (Math.round (d) - d == 0) {
            return String.valueOf ((long) d.doubleValue ());
        }
        return String.valueOf (d);
    }

    /**
     * BigDecimal 相加
     *
     * @param v1
     * @param v2
     *
     * @return double
     */
    public static Double add(double v1, double v2) {
        BigDecimal n1 = new BigDecimal (Double.toString (v1));
        BigDecimal n2 = new BigDecimal (Double.toString (v2));

        return n1.add (n2).doubleValue ();
    }

    public static Double add(double v1, float v2) {
        BigDecimal n1 = new BigDecimal (Double.toString (v1));
        BigDecimal n2 = new BigDecimal (Float.toString (v2));
        return n1.add (n2).doubleValue ();
    }

    public static Double add(float v1, double v2) {
        BigDecimal n1 = new BigDecimal (Float.toString (v1));
        BigDecimal n2 = new BigDecimal (Double.toString (v2));
        return n1.add (n2).doubleValue ();
    }

    public static Float add(float v1, float v2) {
        BigDecimal n1 = new BigDecimal (Float.toString (v1));
        BigDecimal n2 = new BigDecimal (Float.toString (v2));
        return n1.add (n2).floatValue ();
    }


    /**
     * BigDecimal 相减
     *
     * @param v1
     * @param v2
     *
     * @return double
     */
    public static Double subtract(double v1, double v2) {
        BigDecimal n1 = new BigDecimal (Double.toString (v1));
        BigDecimal n2 = new BigDecimal (Double.toString (v2));
        return n1.subtract (n2).doubleValue ();
    }

    public static Double subtract(double v1, float v2) {
        BigDecimal n1 = new BigDecimal (Double.toString (v1));
        BigDecimal n2 = new BigDecimal (Float.toString (v2));
        return n1.subtract (n2).doubleValue ();
    }

    public static Double subtract(float v1, double v2) {
        BigDecimal n1 = new BigDecimal (Float.toString (v1));
        BigDecimal n2 = new BigDecimal (Double.toString (v2));
        return n1.subtract (n2).doubleValue ();
    }

    public static Float subtract(float v1, float v2) {
        BigDecimal n1 = new BigDecimal (Float.toString (v1));
        BigDecimal n2 = new BigDecimal (Float.toString (v2));
        return n1.subtract (n2).floatValue ();
    }

    /**
     * BigDecimal 相乘
     *
     * @param v1
     * @param v2
     *
     * @return double
     */
    public static Double multiply(double v1, double v2) {
        BigDecimal n1 = new BigDecimal (Double.toString (v1));
        BigDecimal n2 = new BigDecimal (Double.toString (v2));
        return n1.multiply (n2).doubleValue ();
    }

    public static Double multiply(double v1, double v2, double v3) {
        BigDecimal n1 = new BigDecimal (Double.toString (v1));
        BigDecimal n2 = new BigDecimal (Double.toString (v2));
        BigDecimal n3 = new BigDecimal (Double.toString (v3));

        BigDecimal temp = n1.multiply (n2);
        return temp.multiply (n3).doubleValue ();
    }

    public static Double multiply(double v1, float v2) {
        BigDecimal n1 = new BigDecimal (Double.toString (v1));
        BigDecimal n2 = new BigDecimal (Float.toString (v2));
        return n1.multiply (n2).doubleValue ();
    }

    public static Double multiply(float v1, double v2) {
        BigDecimal n1 = new BigDecimal (Float.toString (v1));
        BigDecimal n2 = new BigDecimal (Double.toString (v2));
        return n1.multiply (n2).doubleValue ();
    }

    public static Float multiply(float v1, float v2) {
        BigDecimal n1 = new BigDecimal (Float.toString (v1));
        BigDecimal n2 = new BigDecimal (Float.toString (v2));
        return n1.multiply (n2).floatValue ();
    }


    /**
     * BigDecimal 相除
     *
     * @param v1
     * @param v2
     *
     * @return double
     */
    public static Double divide(double v1, double v2) {
        BigDecimal n1 = new BigDecimal (Double.toString (v1));
        BigDecimal n2 = new BigDecimal (Double.toString (v2));
        return n1.divide (n2, 10, BigDecimal.ROUND_HALF_UP).doubleValue ();
    }

    public static Double divide(double v1, float v2) {
        BigDecimal n1 = new BigDecimal (Double.toString (v1));
        BigDecimal n2 = new BigDecimal (Float.toString (v2));
        return n1.divide (n2, 10, BigDecimal.ROUND_HALF_UP).doubleValue ();
    }

    public static Double divide(float v1, double v2) {
        BigDecimal n1 = new BigDecimal (Float.toString (v1));
        BigDecimal n2 = new BigDecimal (Double.toString (v2));
        return n1.divide (n2, 10, BigDecimal.ROUND_HALF_UP).doubleValue ();
    }

    public static Float divide(float v1, float v2) {
        BigDecimal n1 = new BigDecimal (Float.toString (v1));
        BigDecimal n2 = new BigDecimal (Float.toString (v2));
        return n1.divide (n2, 10, BigDecimal.ROUND_HALF_UP).floatValue ();
    }

    /**
     * 比较大小 小于0：v1 < v2； 大于0：v1 > v2； 等于0：v1 = v2
     *
     * @param v1
     * @param v2
     *
     * @return
     */
  /*  public static int compare(double v1, double v2) {
        BigDecimal n1 = new BigDecimal (Double.toString (v1));
        BigDecimal n2 = new BigDecimal (Double.toString (v2));
        return n1.compareTo (n2);
    }*/

    public static Integer compare(Object v1, Object v2) {
        if (v1 instanceof String ) {
            v1 = string2double (((String) v1));
        }
        if (v2 instanceof String ) {
            v2 = string2double (((String) v2));
        }

        if (v1 instanceof Double && v2 instanceof Double) {
            BigDecimal n1 = new BigDecimal (Double.toString ((Double) v1));
            BigDecimal n2 = new BigDecimal (Double.toString ((Double)v2));
            return n1.compareTo (n2);
        }else
        if (v1 instanceof Double && v2 instanceof Float) {
            BigDecimal n1 = new BigDecimal (Double.toString ((Double) v1));
            BigDecimal n2 = new BigDecimal (Float.toString ((Float)v2));
            return n1.compareTo (n2);
        }else
        if (v1 instanceof Float && v2 instanceof Double) {
            BigDecimal n1 = new BigDecimal (Float.toString ((Float) v1));
            BigDecimal n2 = new BigDecimal (Double.toString ((Double)v2));
            return n1.compareTo (n2);
        }else if (v1 instanceof Float && v2 instanceof Float) {
            BigDecimal n1 = new BigDecimal (Float.toString ((Float) v1));
            BigDecimal n2 = new BigDecimal (Float.toString ((Float) v2));
            return n1.compareTo (n2);
        } else {
          return  compare (v1.toString (), v2.toString ());
        }
    }


    /**
     * 按四舍五入保留指定小数位数，位数不够用0补充
     *
     * @param d     格式化前的小数
     * @param point 保留小数位数
     *
     * @return 格式化后的小数
     */
    public static String format4down5up(double d, int point) {
        String pattern = "0.";
        for (int i = 0; i < point; i++) {
            pattern += "0";
        }
        DecimalFormat df = new DecimalFormat (pattern);
        return df.format (d);
    }
    public static double format4down5up2Double(double d, int point) {
        String pattern = "0.";
        for (int i = 0; i < point; i++) {
            pattern += "0";
        }
        DecimalFormat df = new DecimalFormat (pattern);
        String s = df.format (d);

        return string2double (s);
    }

    public static String format4down5up(float f, int point) {
        String pattern = "0.";
        for (int i = 0; i < point; i++) {
            pattern += "0";
        }
        DecimalFormat df = new DecimalFormat (pattern);
        return df.format (f);
    }

    public static String format4down5up(double d) {

        return format4down5up (d, POINT);
    }
    public static double format4down5up2Double(double d) {

        return format4down5up2Double (d, POINT);
    }

    public static String format4down5up(float f) {

        return format4down5up (f, POINT);
    }

    /**
     * 按四舍五入保留指定小数位数，小数点后仅保留有效位数
     *
     * @param d     格式化前的小数
     * @param point 保留小数位数
     *
     * @return 格式化后的小数
     */
    public static String format4down5upNoZero(double d, int point) {
        String pattern = "#.";
        for (int i = 0; i < point; i++) {
            pattern += "#";
        }
        DecimalFormat df = new DecimalFormat (pattern);
        return df.format (d);
    }

    /**
     * 四舍五入。但如果5在最后一位，不会进位。如1.55和1.555保留2位 都=1.55
     *
     * @param v
     * @param point 小数位数
     *
     * @return double
     */
    public static Double formatDecimal45(double v, int point) {
        String vs = Double.toString (v);
        //1.255
        try {
            String vs2 = vs.split ("\\.")[1];
            if (vs2.length () > point) {
                int last = vs2.lastIndexOf ("5");
                if (last == vs2.length () - 1) {
                    StringBuffer stringBuffer = new StringBuffer (vs2);
                    stringBuffer.delete (stringBuffer.length () - 1, stringBuffer.length () - 1);
                    stringBuffer.append ("6");
                    vs2 = stringBuffer.toString ();

                    vs = vs.split ("\\.")[0] + "." + vs2;

                    v = Double.valueOf (vs);

                }
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }

        BigDecimal b = new BigDecimal (v);
        return b.setScale (point, BigDecimal.ROUND_HALF_UP).doubleValue ();
    }



    /**
     * 进一位 （保留位不为0就+1） 如 1.2 1.21 保留2位，=为1.2和1.22
     */
    public static Double formatUp1(double v, int point) {
        BigDecimal b = new BigDecimal (v);
        return b.setScale (point, BigDecimal.ROUND_CEILING).doubleValue ();
    }

    //float 转 double
    public static Double float2double(float f) {
        BigDecimal fbig = new BigDecimal (Float.toString (f));
        return fbig.doubleValue ();
    }
    public static Long float2long(float f) {
        BigDecimal fbig = new BigDecimal (Float.toString (f));
        return fbig.longValue ();
    }

    //double 转 float
    public static Float double2float(Double d) {
        BigDecimal dbig = new BigDecimal (Double.toString (d));
        return dbig.floatValue ();
    }

    /*注意：double类型能保证小数点后16的转化一致；float保证小数点后5位；且末尾的0都会被舍去。如 1.20d --> “1.2”【推翻】
    2019年7月31日14:22:20 哎 999999999999999.99d --> 1000000000000000 这可咋弄
    99999999.99999999 前后八位是对的..同样是16位，99.99999999999999是对的，这个9999999.999999999尾部就成了8.无语了。先用着吧，总比valueOf和 +“”好，差不多的吧。都不咋地
    */
    public static String object2String(Object v) {
        if (v instanceof String) {
            return (String) v;
        }

        DecimalFormat formater = new DecimalFormat ("###################.####################################################");
        //设置数的小数部分所允许的最大位数，避免小数位被舍掉
        //formater.setMaximumFractionDigits (20);
        //设置数的小数部分所允许的最小位数，避免小数位有多余的0
        //formater.setMinimumFractionDigits (0);
        //去掉科学计数法显示，避免显示为111,111,111,111
        //formater.setGroupingUsed (false);

        //formater.setGroupingSize (3); //设置分组大小。分组大小是数的整数部分中分组分隔符之间的数字位数。例如在数 "123,456.78" 中，分组大小是 3。传入的值被转换为 一个字节，这可能导致信息丢失。
        //formater.setRoundingMode (RoundingMode.FLOOR);  //https://blog.csdn.net/alanzyy/article/details/8465098
        if (v instanceof Double) {
            return formater.format ((Double)v);
        }else
        if (v instanceof Float) {
            return formater.format (float2double ((Float)v));
        }else {
            return String.valueOf (v);
        }
    }


    //废弃。转string后会使 x.xx的double变得特别长 -->使用上边的 moneyFormat，保留两位小数
    private static String double2String(Double d) {
        BigDecimal bigDecimal = new BigDecimal (d);
        return bigDecimal.toString ();
    }
    private static String float2String(Float d) {
        BigDecimal bigDecimal = new BigDecimal (d);
        return bigDecimal.toString ();
    }

    /**
     * 只保留指定位数，其他舍弃
     *
     * @param d     格式化前的小数
     * @param point 保留小数位数
     *
     * @return 格式化后的小数
     */
    public static Double formatTrunc(double d, int point) {
        String ds = Double.toString (d);
        StringBuffer stringBuffer = new StringBuffer (ds);
        //如果是小数
        if (stringBuffer.indexOf (".") != -1) {
            String s1 = stringBuffer.toString ().split ("\\.")[0];
            String s2 = stringBuffer.toString ().split ("\\.")[1];
            //1.333 3位数大于 point的2,则截断
            if (s2.length () > point) {
                stringBuffer = new StringBuffer (s1 + "." + s2.substring (0, point));
            } else { //1.3 转成1.300
                for (int i = 0; i < point - s2.length (); i++) {
                    stringBuffer.append ("0");
                }
            }

        } else {
            stringBuffer.append (".");
            for (int i = 0; i < point; i++) {
                stringBuffer.append ("0");
            }

        }
        return new BigDecimal (stringBuffer.toString ()).doubleValue ();
    }

    public static Float formatTrunc(float f, int point) {
        f = double2float (formatTrunc (float2double (f), point));
        return f;
    }

    public static Double formatTrunc(double d) {
        return formatTrunc (d, POINT);
    }

    public static long double2long(double d) {
        BigDecimal bigDecimal = new BigDecimal (Double.toString (d));
        return bigDecimal.longValue ();
    }

    //String 转double 1会变成1.0 ；1.00会变成1.0
    public static Double string2double(String s) {
        BigDecimal decimal = new BigDecimal (s);
        return decimal.doubleValue ();
    }

    public static Float string2float(String s) {
        BigDecimal decimal = new BigDecimal (s);
        return decimal.floatValue ();
    }
}
