package com.pongift20.marketplace.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON 변환 유틸
 */
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonUtils() {
    }

    public static <T> T fromJson(String json, Class<T> valueType) throws JsonProcessingException {
        return objectMapper.readValue(json, valueType);
    }

    public static String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
