package com.xiaowenhou.dynamicmultipledatasource.datasource.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 将配置文件中的配置封装到一个map中，key为数据源的名字， value为对应的配置
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "dynamic")
public class DynamicDataSourceProperties {
    private Map<String, DataSourceProperties> datasource = new LinkedHashMap<>();
}
