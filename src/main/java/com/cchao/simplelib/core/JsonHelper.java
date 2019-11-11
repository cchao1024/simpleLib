package com.cchao.simplelib.core;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * description json 工具类包装
 *
 * @author cchao
 * @version 2019-11-11
 * @date 2016/12/2
 **/
public class JsonHelper {
    private static Gson gson = new Gson();

    public static Gson getGson() {
        return gson;
    }

    /**
     * 转成json
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return gson.toJson(JsonNull.INSTANCE);
        }
        return gson.toJson(obj);
    }

    /**
     * 转换生成对象bean
     */
    public static <T> T toObject(String json, Type typeOfT) {
        try {
            T t = gson.fromJson(json, typeOfT);
            return t;
        } catch (Exception e) {
            Logs.logException(e);
            return null;
        }
    }

    /**
     * 转换成 List<T> 对象
     *
     * @param json json
     * @param cls  实体类型
     */
    public static <T> List<T> toList(String json, Class<T> cls) {
        return toObject(json, getListType(cls));
    }

    /**
     * 转换成 Map<String,T> 对象
     *
     * @param json json
     * @param cls  实体类型
     */
    public static <T> Map<String, T> toMap(String json, Class<T> cls) {
        return toObject(json, getMapType(String.class, cls));
    }

    /**
     * 获取List Type
     *
     * @param type .class 对象
     */
    public static Type getListType(final Type type) {
        return TypeToken.getParameterized(List.class, type).getType();
    }

    /**
     * 获取 Set Type
     *
     * @param type .class 对象
     */
    public static Type getSetType(final Type type) {
        return TypeToken.getParameterized(Set.class, type).getType();
    }

    /**
     * 返回映射类型  k-v
     *
     * @param keyType   key 的类型
     * @param valueType value 的类型
     */
    public static Type getMapType(final Type keyType, final Type valueType) {
        return TypeToken.getParameterized(Map.class, keyType, valueType).getType();
    }

    /**
     * 返回数组类型
     */
    public static Type getArrayType(final Type type) {
        return TypeToken.getArray(type).getType();
    }

    public static JsonObject parseObj(String json) {
        return new JsonParser().parse(json).getAsJsonObject();
    }

    public static JsonArray parseArray(String json) {
        return new JsonParser().parse(json).getAsJsonArray();
    }
}
