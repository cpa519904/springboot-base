package com.company.common.tools;


public class ThreadLocalUtil {
    private static final ThreadLocal<Object> repository = new ThreadLocal<>();

    public static void set(Object value) {
        repository.set(value);
    }

    public static Object get() {
        return repository.get();
    }

    public static void clean() {
        repository.remove();
    }
}
