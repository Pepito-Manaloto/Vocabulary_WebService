package com.aaron.vocabularyapi.util;

import static java.util.Objects.*;
import static org.apache.commons.lang3.StringUtils.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonUtils
{
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectReader READER = MAPPER.readerFor(new TypeReference<Map<String, Object>>()
    {
    });

    static
    {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtils()
    {
        throw new AssertionError("Utility class cannot be initialized");
    }

    public static String toJson(Object object) throws JsonProcessingException
    {
        String jsonString = null;
        if(nonNull(object))
        {
            log.info("toJson. Object {} converting to Json string", object.getClass().getName());
            jsonString = MAPPER.writeValueAsString(object);
        }

        return jsonString;
    }

    public static byte[] toBytes(Object object)
    {
        byte[] jsonBytes = null;
        if(nonNull(object))
        {
            log.info("toBytes. Object {} converting to Json string", object.getClass().getName());
            try
            {
                jsonBytes = MAPPER.writeValueAsBytes(object);
            }
            catch(JsonProcessingException e)
            {
                log.error("error. Failed creating error response.", e);

                String defaultMsg = "Failed converting '" + object + "' to bytes[]: " + e.getMessage();
                jsonBytes = defaultMsg.getBytes();
            }
        }

        return jsonBytes;
    }

    public static void toFile(Object object, String filename) throws IOException
    {
        if(nonNull(object))
        {
            log.info("toFile. Object {} converting to file", object.getClass().getName());
            MAPPER.writeValue(new File(filename), object);
        }
    }

    public static void toOutputStream(Object object, OutputStream outputStream) throws IOException
    {
        if(nonNull(object))
        {
            log.info("toOutputStream. Object {} converting to OutputStream", object.getClass().getName());
            MAPPER.writeValue(outputStream, object);
        }
    }

    public static Map<String, Object> toMap(String jsonString) throws JsonProcessingException
    {
        Map<String, Object> map = new LinkedHashMap<>();

        if(isNotBlank(jsonString))
        {
            log.info("toMap. jsonString converting to LinkedHashMap<String, Object>");
            map = READER.readValue(jsonString);
        }

        return map;
    }

    public static <T> T toJavaBean(String jsonString, Class<T> clazz) throws JsonMappingException, JsonProcessingException
    {
        T javaBean = null;

        if(isNotBlank(jsonString))
        {
            log.info("toJavaBean. jsonString converting to {}", clazz.getName());
            javaBean = MAPPER.readValue(jsonString, clazz);
        }
        else
        {
            log.info("toJavaBean. jsonString to convert is blank. class={}", clazz.getName());
        }

        return javaBean;
    }

    public static <T> T toJavaBeanIgnoreCase(String jsonString, Class<T> clazz) throws JsonMappingException, JsonProcessingException
    {
        T javaBean = null;

        if(isNotBlank(jsonString))
        {
            MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            log.info("toJavaBeanIgnoreCase. jsonString converting to {}", clazz.getName());
            javaBean = MAPPER.readValue(jsonString, clazz);
            MAPPER.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, false);
        }
        else
        {
            log.info("toJavaBeanIgnoreCase. jsonString to convert is blank. class={}", clazz.getName());
        }

        return javaBean;
    }
}
