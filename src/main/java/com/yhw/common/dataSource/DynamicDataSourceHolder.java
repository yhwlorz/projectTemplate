package com.yhw.common.dataSource;

/**
 * The type Dynamic data source holder.
 *
 * @author yhw 自定义数据源操作类,用以指定当前线程上的数据源
 * @date 2018 -12-20 17:14:31
 */
public class DynamicDataSourceHolder {

    public static final String DATASOURCE_TAKEOUT_LOCAL = "takeout_local";
    public static final String DATASOURCE_TAKEOUT_PRE = "takeout_pre";
    public static final String DATASOURCE_ACCOUNT_PRE = "account_pre";
    public static final String DATASOURCE_TAKEOUT_TTT = "takeout_ttt";
    public static final String DATASOURCE_ACCOUNT_TTT = "account_ttt";
    public static final String DATASOURCE_TAKEOUT_TST = "takeout_tst";
    public static final String DATASOURCE_ACCOUNT_TST = "account_tst";

    /**
     * 用ThreadLocal来设置当前线程使用哪个dataSource
     */
    private static ThreadLocal<String> contextHolder = new ThreadLocal<String>();

    public static void setDdType(String dbType) {
        contextHolder.set(dbType);
    }

    /**
     * 获取指定的数据源，此方法被AutoSwitchDataSource自动调用
     *
     * @return “tackout”或者“account”，是targetDataSources这一map属性的key
     */
    public static String getDbType() {
        return contextHolder.get();
    }

    /**
     * 删除与当前线程绑定的数据源路由的key
     */
    public static void removeDBType() {
        contextHolder.remove();
    }
}
