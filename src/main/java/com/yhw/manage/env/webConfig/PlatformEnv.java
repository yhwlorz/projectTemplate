package com.yhw.manage.env.webConfig;


import com.yhw.common.dataSource.DynamicDataSourceHolder;

/**
 *
 *
 *  总述：
 *  尽量把变量控制在
 *  ①environment ： 环境变量
 *  ②page : xpath和对元素的操作(点、写)
 *  ③enum : 固定的值
 *
 * @author 杨怀伟
 * @date 2019 -03-20 21:20:19
 */
public class PlatformEnv {

    public static  boolean openChrome = false; //只有操作到location中的方法，才由PlatformAspect将其置为true
    /*---------------------------------------------------------------------------------*/
    //云平台地址
    public static final String env = ".pre";
    public static final String userName = "18660166578";
    public static final String password = "C123456";
    public static final String pkGroup = "5b076cd928ff70000165a081";
    //数据库
    public static String takeoutDb = DynamicDataSourceHolder.DATASOURCE_TAKEOUT_PRE;
    public static String accountDb = DynamicDataSourceHolder.DATASOURCE_ACCOUNT_PRE;
    public static String localDb = DynamicDataSourceHolder.DATASOURCE_TAKEOUT_LOCAL;
    //缓存
    public static String redisEnv = "pre";
    //开放平台信息
    public static String app_id = "5c348192f7f9725b460c631e";
    public static Integer client_id = 110006;
    public static String client_secret = "4bb623727d5531005b1f2b233b6fe7c0";
    public static String grant_type = "client_credentials";

    /*---------------------------------------------------------------------------------*/

    /*public static final String env = ".tst";
    public static final String userName = "18766162056";
    public static final String password = "Y123456";
    public static final String pkGroup = "5bdbb67b834e900001bbf86c";
   //数据库
   public static String takeoutDb = DynamicDataSourceHolder.DATASOURCE_TAKEOUT_TST;
   public static String accountDb = DynamicDataSourceHolder.DATASOURCE_ACCOUNT_TST;
   public static String localDb = DynamicDataSourceHolder.DATASOURCE_TAKEOUT_LOCAL;
   //缓存 --》需手动修改
   public static String redisEnv = "tst";
    //开放平台信息
    public static String app_id = "5d1d9eeff4bba423007023c1";
    public static Integer client_id = 110011;
    public static String client_secret = "25bd1370cb322669fcc5c54daabf9d37";
    public static String grant_type = "client_credentials";
*/
    /*---------------------------------------------------------------------------------*/

    /*public static final String env = ".tst";
    public static final String userName = "15201006604";
    public static final String password = "Admin123456";
    public static final String pkGroup = "5d4146b628c5710001d18585";
    //数据库
    public static String takeoutDb = DynamicDataSourceHolder.DATASOURCE_TAKEOUT_TST;
    public static String accountDb = DynamicDataSourceHolder.DATASOURCE_ACCOUNT_TST;
    public static String localDb = DynamicDataSourceHolder.DATASOURCE_TAKEOUT_LOCAL;
    //缓存 --》需手动修改
    public static String redisEnv = "tst";
    //开放平台信息
    public static String app_id = "5d415dd7f4bba423ff9823f5";
    public static Integer client_id = 110017;
    public static String client_secret = "8721f623f8e5aa2830b2142ce8dcd9dc";
    public static String grant_type = "client_credentials";*/

    }
