package com.sunlands.common.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * Created on 2017年11月15日 下午7:27:50
 * 
 * Description: [json转换工具]
 * 
 * Company: [尚德机构]
 * 
 * @author [liujiangbo]
 *
 */
public class JsonUtils {
	private static final Logger log = LoggerFactory.getLogger(JsonUtils.class);
	private static ObjectMapper mapper = new ObjectMapper();

	private JsonUtils() {
	}

	public static String toJson(Object obj) {
		try {
			mapper.setSerializationInclusion(Include.NON_NULL);
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public static String toJsonWithDatePattern(Object obj, String pattern) {
		try {
			mapper.setDateFormat(new SimpleDateFormat(pattern));
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	public static <T> T toBean(String json, Class<T> valueType) {
		T bean = null;
		try {
			bean = mapper.readValue(json, valueType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bean;
	}

	public static <T> T toCollectionBean(String json, Class<?> collectionClass, Class<?>... elementClasses) {
		T bean = null;
		try {
			JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
			bean = mapper.readValue(json, javaType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bean;
	}

}
