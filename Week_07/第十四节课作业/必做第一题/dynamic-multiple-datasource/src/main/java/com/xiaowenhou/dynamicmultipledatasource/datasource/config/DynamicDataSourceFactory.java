package com.xiaowenhou.dynamicmultipledatasource.datasource.config;

import com.xiaowenhou.dynamicmultipledatasource.datasource.properties.DataSourceProperties;
import com.zaxxer.hikari.util.DriverDataSource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 数据源静态工厂方法
 * 根据属性创建数据源
 */
public class DynamicDataSourceFactory {

    public static DataSource buildDataSource(DataSourceProperties properties) {

        return new DriverDataSource(
                properties.getUrl(), properties.getDriverClassName(), new Properties(), properties.getUsername(), properties.getPassword());
    }
}
