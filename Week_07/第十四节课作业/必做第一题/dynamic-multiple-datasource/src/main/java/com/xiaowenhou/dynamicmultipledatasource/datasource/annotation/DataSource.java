package com.xiaowenhou.dynamicmultipledatasource.datasource.annotation;

import com.xiaowenhou.dynamicmultipledatasource.datasource.DataSourceTypeEnum;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DataSource {
    DataSourceTypeEnum value() default DataSourceTypeEnum.MASTER;
}
