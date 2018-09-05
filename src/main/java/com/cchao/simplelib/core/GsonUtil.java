package com.cchao.simplelib.core;

import com.cchao.simplelib.util.ExceptionCollect;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * description json包装
 * author  cchao
 * date  2016/12/2
 **/
public class GsonUtil {
    private static Gson gson = new Gson();

    /**
     * 转成json
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * 转成bean
     */
    public static <T> T fromJson(String json, Class<T> cls) {
        try {
            T t = gson.fromJson(json, cls);
            return t;
        } catch (Exception e) {
            ExceptionCollect.logException(e);
            return null;
        }
    }

    /**
     * 转成bean*
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        try {
            T t = gson.fromJson(json, typeOfT);
            return t;
        } catch (Exception e) {
//            ParseUtils.logJsonException(json, e.getMessage());
            return null;
        }
    }

    /**
     * 转成list
     * 泛型在编译期类型被擦除导致报错
     *
     * @param gsonString
     * @return
     */
    public static <T> List<T> gsonToList(String gsonString, Type type) {
        List<T> list = null;
        list = gson.fromJson(gsonString, type);
        return list;
    }

    /**
     * 转成list
     * 解决泛型问题
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }


    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> gsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        list = gson.fromJson(gsonString,
            new TypeToken<List<Map<String, T>>>() {
            }.getType());
        return list;
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> gsonToMaps(String gsonString) {
        Map<String, T> map = null;
        map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
        }.getType());
        return map;
    }

    public static Gson getGson() {
        return gson;
    }

    public static JsonObject parseObj(String json) {
        return new JsonParser().parse(json).getAsJsonObject();
    }

    public static JsonArray parseArray(String json) {
        return new JsonParser().parse(json).getAsJsonArray();
    }
}
