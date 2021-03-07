package com.xiaowenhou.dynamicmultipledatasource.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum DataSourceTypeEnum {
    /**
     * 表示主库
     */
    MASTER("master"),

    /**
     * 表示从库
     */
    SLAVE("slave");

    private String type;
}
