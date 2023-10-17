/*
 * Copyright 2011-2018 Amazon Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *    http://aws.amazon.com/apache2.0
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.projecthope.util;

import com.example.projecthope.util.module.CustomJavaTimeModule;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.type.CollectionType;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

/**
 * @author WangGang
 */
public class Jackson {
	/**
	 * or different mapper for other format
	 */
	public static final ObjectMapper OBJECT_MAPPER = objectMapper();

	public static final ObjectMapper TYPE_OBJECT_MAPPER = OBJECT_MAPPER.copy().activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

	public static final ObjectWriter PRETTY_OBJECT_WRITER = OBJECT_MAPPER.writerWithDefaultPrettyPrinter();

	private static ObjectMapper objectMapper() {
		return JsonMapper.builder()
				//反序列化时，属性不存在不报错
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
				//序列化时，没有getter/setter方法时不报错,输出{}
				.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
				//关闭时间输出为字符串
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
				.defaultDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
				.defaultTimeZone(TimeZone.getDefault())
				.addModule(new CustomJavaTimeModule())
				.findAndAddModules()
				.build();
	}


	public static String toJsonString(Object value) {
		try {
			return OBJECT_MAPPER.writeValueAsString(value);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static byte[] toJsonByte(Object value) {
		try {
			return OBJECT_MAPPER.writeValueAsBytes(value);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static String toJsonPrettyString(Object value) {
		try {
			return PRETTY_OBJECT_WRITER.writeValueAsString(value);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static String toJsonStringWithTypeInfo(Object value) {
		if (value == null) {
			return null;
		}
		try {
			return TYPE_OBJECT_MAPPER.writeValueAsString(value);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Returns the deserialized object from the given json string and target
	 * class; or null if the given json string is null.
	 */
	public static <T> T fromJsonString(String json, Class<T> clazz) {
		if (json == null) {
			return null;
		}
		try {
			return OBJECT_MAPPER.readValue(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse Json String.", e);
		}
	}

	public static <T> List<T> fromArrayJsonString(String json, Class<T> clazz) {
		if (json == null) {
			return null;
		}
		try {
			CollectionType collectionType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
			return OBJECT_MAPPER.readValue(json, collectionType);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse array Json String.", e);
		}
	}

	public static <T> List<T> fromArrayJsonString(String json, TypeReference<T> typeReference) {
		if (json == null) {
			return null;
		}
		try {
			JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructType(typeReference);
			CollectionType collectionType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, javaType);
			return OBJECT_MAPPER.readValue(json, collectionType);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse array Json String.", e);
		}
	}

	public static <T> T fromJsonString(String json, TypeReference<T> typeReference) {
		if (json == null) {
			return null;
		}
		try {
			return OBJECT_MAPPER.readValue(json, typeReference);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse Json String.", e);
		}
	}

	public static JsonNode fromJsonString(String json) {
		if (json == null) {
			return null;
		}
		try {
			return OBJECT_MAPPER.readTree(json);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse Json String.", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromJsonString(String json, Type type) {
		if (json == null) {
			return null;
		}
		if (type == String.class) {
			return (T) json;
		}
		try {
			return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructType(type));
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse Json String.", e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T fromTypeInfoJsonString(String json) {
		if (json == null) {
			return null;
		}
		try {
			return (T) TYPE_OBJECT_MAPPER.readValue(json, Object.class);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse Json String.", e);
		}
	}

    @SuppressWarnings("unchecked")
    public static <T> T fromTypeInfoJsonString(String json, Class<? super T> tClass) {
        if (json == null) {
            return null;
        }
        try {
            return (T) TYPE_OBJECT_MAPPER.readValue(json, tClass);
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse Json String.", e);
        }
    }

	public static <T> T fromJsonByte(byte[] bytes , Class<T> clazz) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		try {
			return OBJECT_MAPPER.readValue(bytes, clazz);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse Json String.", e);
		}
	}

	public static <T> T fromJsonByte(byte[] bytes , TypeReference<T> reference) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		try {
			return OBJECT_MAPPER.readValue(bytes, reference);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse Json String.", e);
		}
	}

	public static <T> T loadFrom(File file, Class<T> clazz) throws IOException {
		try {
			return OBJECT_MAPPER.readValue(file, clazz);
		} catch (IOException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public static  <T> T treeToValue(TreeNode treeNode, Class<T> tClass) {
		try {
			return OBJECT_MAPPER.treeToValue(treeNode, tClass);
		} catch (Exception e) {
			throw new RuntimeException("Unable to parse Json String.", e);
		}
	}
}
