package com.xiaowenhou.dynamicmultipledatasource.datasource.config;

import com.xiaowenhou.dynamicmultipledatasource.datasource.DataSourceTypeEnum;
import com.xiaowenhou.dynamicmultipledatasource.datasource.properties.DataSourceProperties;
import com.xiaowenhou.dynamicmultipledatasource.datasource.properties.DynamicDataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceConfig {


    @Resource
    private DynamicDataSourceProperties properties;


    @Bean
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = getDynamicDataSource();
        dynamicDataSource.setTargetDataSources(targetDataSources);
        //默认为主库
        dynamicDataSource.setDefaultTargetDataSource(targetDataSources.get("master"));
        return dynamicDataSource;
    }


    @Bean
    public DataSourceNameHolder getDataSourceNameHolder() {
        Map<String, DataSourceProperties> dataSourcePropertiesMap = properties.getDatasource();
        //将数据源名称按照类型封装在一起
        DataSourceNameHolder holder = new DataSourceNameHolder();
        for (DataSourceTypeEnum value : DataSourceTypeEnum.values()) {
            List<String> sourceNameList = new ArrayList<>();
            dataSourcePropertiesMap.forEach((k,v) -> {
                if (value.getType().equals(v.getFlag())) {
                    sourceNameList.add(k);
                }
            });
            holder.setDataSourceNames(value.getType(), sourceNameList);
        }
        return holder;
    }

    /**
     * 将数据源名称和数据源存储在一个map中
     * @return
     */
    private Map<Object, Object> getDynamicDataSource() {
        Map<String, DataSourceProperties> dataSourcePropertiesMap = properties.getDatasource();
        Map<Object, Object> targetDataSources = new HashMap<>(dataSourcePropertiesMap.size());
        dataSourcePropertiesMap.forEach((k, v) -> {
            DataSource dataSource = DynamicDataSourceFactory.buildDataSource(v);
            targetDataSources.put(k, dataSource);
        });
        return targetDataSources;
    }

}
