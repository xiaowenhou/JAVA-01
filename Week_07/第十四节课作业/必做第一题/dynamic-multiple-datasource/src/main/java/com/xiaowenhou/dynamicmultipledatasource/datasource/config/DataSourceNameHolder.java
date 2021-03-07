package com.xiaowenhou.dynamicmultipledatasource.datasource.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将数据源类型和数据源名称封装在一个map中， 用于后续负载均衡
 */
public class DataSourceNameHolder {

    private final Map<String, List<String>> sourceNameHolder = new HashMap<>();


    public void setDataSourceNames(String sourceType, List<String> sourceNames) {
        this.sourceNameHolder.put(sourceType, sourceNames);
    }

    public List<String> getDataSourceNames(String sourceType) {
        return this.sourceNameHolder.get(sourceType);
    }

}
