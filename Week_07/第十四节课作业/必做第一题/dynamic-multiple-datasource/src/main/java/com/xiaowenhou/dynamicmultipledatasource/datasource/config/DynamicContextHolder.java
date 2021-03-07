package com.xiaowenhou.dynamicmultipledatasource.datasource.config;

import java.util.ArrayDeque;
import java.util.Deque;

public class DynamicContextHolder {

    private static final ThreadLocal<Deque<String>> CONTEXT_HOLDER = ThreadLocal.withInitial(ArrayDeque::new);

    public static String get() {
        return CONTEXT_HOLDER.get().peekFirst();
    }

    public static void set(String dataSourceName) {
        CONTEXT_HOLDER.get().addLast(dataSourceName);
    }

    public static void clear() {
        Deque<String> deque = CONTEXT_HOLDER.get();
        deque.removeFirst();
        if (deque.isEmpty()) {
            CONTEXT_HOLDER.remove();
        }
    }
}
