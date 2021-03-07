package com.xiaowenhou.dynamicmultipledatasource.datasource.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * 封装配置的类
 */
@Getter
@Setter
public class DataSourceProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private String flag;
}
