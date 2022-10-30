package org.rainy.commerce.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangyu
 */
@Slf4j
public class JsonMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String object2String(T src) {
        Preconditions.checkNotNull(src, "object can not be null");
        try {
            return src instanceof String ? (String) src : objectMapper.writeValueAsString(src);
        } catch (JsonProcessingException e) {
            log.error("parse object to string error", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T string2Object(String src, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(src)) {
            throw new NullPointerException("jsonStr can not be empty or null");
        }
        try {
            return objectMapper.readValue(src, typeReference);
        } catch (JsonProcessingException e) {
            log.error("parse string to object error", e);
            throw new RuntimeException(e);
        }
    }

}
