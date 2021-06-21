package com.xiaowenhou.reids.operate.aspect.enums;

/**
 * 删除redis缓存的枚举策略类
 */
public enum ClearCacheStrategy {

    /**
     * 在目标方法执行前删除
     */
    BEFORE,

    /**
     * 在目标方法执行后删除
     */
    AFTER,

    /**
     * 延迟双删， 在目标方法执行前后都删除
     */
    AROUND;

}
