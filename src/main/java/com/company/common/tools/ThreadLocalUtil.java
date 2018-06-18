package com.company.common.tools;

import java.util.Map;

public class ThreadLocalUtil {
    private static final ThreadLocal<Map<String, Object>> repository = new ThreadLocal<>();

    public static void set(String key, Object value) {
        Map<String, Object> map = repository.get();
        map.put(key, value);
        repository.set(map);
    }

    public static Object get(String key) {
        Map<String, Object> map = repository.get();
        return map.get(key);
    }

    public static void clean() {
        repository.remove();
    }
}
