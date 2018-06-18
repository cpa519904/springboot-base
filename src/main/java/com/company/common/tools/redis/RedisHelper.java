package com.company.common.tools.redis;

import com.company.common.exception.SystemException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisHelper {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public String readValue(String key) {
        String content;
        try {
            content = stringRedisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            logger.error("getFromRedis error", e);
            throw new SystemException("getFromRedis Exception");
        }
        return content;
    }

    public Object readValue(String key, Class<?> clazz) {
        String content = readValue(key);

        if (StringUtils.isEmpty(content)) {
            return null;
        }

        Object value;
        try {
            value = objectMapper.readValue(content, clazz);
        } catch (IOException e) {
            logger.error("read Value is error", e);
            throw new SystemException("redis readValue error");
        }

        return value;
    }

    public Object readValue(String key, TypeReference<?> valueTypeRef) {
        String content = readValue(key);

        if (StringUtils.isEmpty(content)) {
            return null;
        }

        Object value;
        try {
            value = objectMapper.readValue(content, valueTypeRef);
        } catch (IOException e) {
            logger.error("read TypeReferenceValue is error", e);
            throw new SystemException("redis read TypeReferenceValue error");
        }

        return value;
    }

    private void writeString(String key, String content) {
        try {
            stringRedisTemplate.opsForValue().set(key, content);
        } catch (Exception e) {
            logger.error("setToRedis error", e);
            throw new SystemException("setToRedis Exception");
        }
    }

    public void writeString(String key, String content, long timeout) {
        try {
            stringRedisTemplate.opsForValue().set(key, content, timeout, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("setToRedis error", e);
            throw new SystemException("setToRedis Exception");
        }
    }

    public void writeObject(String key, Object value) {
        String content = writeValueAsString(value);
        writeString(key, content);
    }

    public void writeObject(String key, Object value, long timeout) {
        String content = writeValueAsString(value);
        writeString(key, content, timeout);
    }

    public void putValue(String key, Object value) {
        Long timeOut = this.getTimeOut(key);
        if (timeOut != null && timeOut > 0L) {
            this.writeObject(key, value, timeOut);
        } else {
            this.writeObject(key, value);
        }

    }

    public Long getTimeOut(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public void delete(String key) {
        try {
            stringRedisTemplate.delete(key);
        } catch (Exception e) {
            logger.error("delete error", e);
            throw new SystemException("redis delete key error");
        }
    }

    public boolean setIfAbsent(String key, String value) {
        boolean result;
        try {
            result = stringRedisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            logger.error("setIfAbsent error", e);
            throw new SystemException("redis setIfAbsent error");
        }
        return result;
    }

    public void writeIntoSortedSet(String key, String member, double score) {
        try {
            stringRedisTemplate.opsForZSet().add(key, member, score);
        } catch (Exception e) {
            logger.error("writeIntoSortedSet error", e);
            throw new SystemException("redis writeIntoSortedSet error");
        }
    }

    public void deleteFromSortedSet(String key, Object... member) {
        try {
            stringRedisTemplate.opsForZSet().remove(key, member);
        } catch (Exception e) {
            logger.error("deleteFromSortedSet error", e);
            throw new SystemException("redis deleteFromSortedSet error");
        }
    }

    public Set<String> readFromSortedSetRangeByScore(String key, double min, double max) {
        Set<String> result;
        try {
            result = stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
        } catch (Exception e) {
            logger.error("readFromSortedSetRangeByScore error", e);
            throw new SystemException("redis readFromSortedSetRangeByScore error");
        }
        return result;
    }

    private String writeValueAsString(Object value) {
        String content;
        try {
            content = objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            logger.error("把Object转换为String错误", e);
            throw new SystemException("redis writeValueAsString JsonProcessingException");
        }
        return content;
    }
}
