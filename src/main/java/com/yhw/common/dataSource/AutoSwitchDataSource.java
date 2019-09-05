package com.yhw.common.dataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 *
 * @author yhw
 * @date 2018 -12-20 17:34:59
 */
public class AutoSwitchDataSource extends AbstractRoutingDataSource {


    //重写determineCurrentLookupKey()方法，自动切换数据源
    @Override
    public Object determineCurrentLookupKey() {

        String dbType = DynamicDataSourceHolder.getDbType ();

        return dbType;
    }

}
