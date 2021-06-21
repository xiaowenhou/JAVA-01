package com.xiaowenhou.reids.operate.key;


import com.xiaowenhou.reids.operate.annotation.KeyPrefix;
import com.xiaowenhou.reids.operate.annotation.RedisKey;
import com.xiaowenhou.reids.operate.utils.ProceedingJoinPointUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.*;

//数据库名或者业务名 + 表名 + 参数名：参数值
public class RedisKeyGenerator {

    private final static String COLON = ":";

    private final static String NULL_STR = "null";

    public static String generate(ProceedingJoinPoint point) {
        String keyPrefix = buildRedisKeyPrefix(point);

        List<RedisKeyParam> keyParamList = buildRedisKeyParam(point);

        StringBuilder redisKey = new StringBuilder();
        redisKey.append(keyPrefix);
        keyParamList.stream().sorted(Comparator.comparing(RedisKeyParam::getOrder)).forEach(k -> {
            redisKey.append(COLON);
            redisKey.append(k.getParamName());
            redisKey.append(COLON);
            redisKey.append(k.getParamValue());
        });

        return redisKey.toString();
    }

    private static String buildRedisKeyPrefix(ProceedingJoinPoint point) {

        //先获取方法上的注解， 没有再获取类上的注解
        KeyPrefix keyPrefix = ProceedingJoinPointUtils.getMethodAnnotation(point, KeyPrefix.class);
        if (Objects.isNull(keyPrefix)) {
            keyPrefix = ProceedingJoinPointUtils.getTypeAnnotation(point, KeyPrefix.class);
        }

        if (Objects.isNull(keyPrefix)) {
            return NULL_STR + COLON + NULL_STR;
        }

        return (StringUtils.isEmpty(keyPrefix.bizName()) ? NULL_STR : keyPrefix.bizName()) +
                COLON +
                (StringUtils.isEmpty(keyPrefix.tableName()) ? NULL_STR : keyPrefix.tableName());
    }


    private static List<RedisKeyParam> buildRedisKeyParam(ProceedingJoinPoint point) {
        List<RedisKeyParam> keyParamList = new ArrayList<>();
        Parameter[] parameters = ProceedingJoinPointUtils.getMethodParameters(point);
        Object[] args = point.getArgs();
        for (int i = 0; i < parameters.length; i++) {
            //遍历方法参数， Parameter是参数对应的参数对象， arg是真是的参数
            Parameter parameter = parameters[i];
            Object arg = args[i];
            RedisKey annotation = parameter.getAnnotation(RedisKey.class);
            if (annotation != null) {
                String paramName = StringUtils.isEmpty(annotation.alias()) ? parameter.getName() : annotation.alias();
                String paramValue = StringUtils.isEmpty(arg.toString()) ? NULL_STR : arg.toString();
                keyParamList.add(new RedisKeyParam(annotation.order(), paramName, paramValue));
            }

            findParameterRecursion(arg, arg.getClass(), keyParamList, new HashSet<>());
        }

        return keyParamList;
    }


    private static void findParameterRecursion(Object arg, Class<?> clazz, List<RedisKeyParam> keyParamList, Set<String> cached) {
        Field[] fields = clazz.getDeclaredFields();
//        log.info("clazz type name is : {}", clazz);
        //TODO 把8个基本类型的包装类和String也包括进来
        if ("java.lang.Object".equals(clazz.getTypeName())) {
            return;
        }
        //已经判断过的不再重复判断
        if (cached.contains(clazz.getTypeName())) {
            return;
        }

        for (Field field : fields) {
            field.setAccessible(true);
            RedisKey annotation = field.getAnnotation(RedisKey.class);
            if (annotation == null) {
                continue;
            }
            //如果字段被@RedisKey注解修饰， 则执行该字段的get方法
            try {
                Object fieldValue = field.get(arg);

                String paramName = StringUtils.isEmpty(annotation.alias()) ? field.getName() : annotation.alias();
                String paramValue = Objects.isNull(fieldValue) ? NULL_STR : fieldValue.toString();
                RedisKeyParam param = new RedisKeyParam(annotation.order(), paramName, paramValue);
                keyParamList.add(param);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        cached.add(clazz.getTypeName());
        findParameterRecursion(arg, clazz.getSuperclass(), keyParamList, cached);
    }

}
