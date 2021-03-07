package com.xiaowenhou.dynamicmultipledatasource.datasource.aspect;

import cn.hutool.core.util.RandomUtil;
import com.xiaowenhou.dynamicmultipledatasource.datasource.annotation.DataSource;
import com.xiaowenhou.dynamicmultipledatasource.datasource.config.DataSourceNameHolder;
import com.xiaowenhou.dynamicmultipledatasource.datasource.config.DynamicContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 通过切面在方法执行前将对应的数据源注入进去
 * @author xiaowenhou
 */
@Slf4j
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceAspect {

    @Resource
    private DataSourceNameHolder holder;

    @Pointcut("@annotation(com.xiaowenhou.dynamicmultipledatasource.datasource.annotation.DataSource) " +
        "|| @within(com.xiaowenhou.dynamicmultipledatasource.datasource.annotation.DataSource)")
    public void dataSourcePointCut(){}

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class targetClass = point.getTarget().getClass();
        Method method = signature.getMethod();

        //获取方法和类上的注解
        DataSource targetDataSource = (DataSource) targetClass.getAnnotation(DataSource.class);
        DataSource methodDataSource = method.getAnnotation(DataSource.class);
        if (targetDataSource != null || methodDataSource != null) {
            String datasourceType;
            datasourceType = methodDataSource != null ? methodDataSource.value().getType() : targetDataSource.value().getType();

            //TODO  用面向对象的方式重构
            //根据类型获取对应的数据源名称， 然后使用随机的负载均衡策略
            List<String> sourceNameList = holder.getDataSourceNames(datasourceType);
            String sourceName = sourceNameList.get(RandomUtil.randomInt(0, sourceNameList.size()));

            //根据注解的值确定使用哪个数据源
            DynamicContextHolder.set(sourceName);
            log.info("set datasource is {}", sourceName);
        }

        try {
            return point.proceed();
        } finally {
            DynamicContextHolder.clear();
            log.info("clean datasouce");
        }
    }
}
