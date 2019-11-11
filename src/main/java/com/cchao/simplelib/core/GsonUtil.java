package com.cchao.simplelib.core;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * description json 工具类包装
 * author  cchao
 * date  2016/12/2
 * 新版 {@link JsonHelper}
 **/
@Deprecated
public class GsonUtil {
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
     * 转成bean
     */
    public static <T> T fromJson(String json, Class<T> cls) {
        try {
            T t = gson.fromJson(json, cls);
            return t;
        } catch (Exception e) {
            Logs.logException(e);
            return null;
        }
    }

    /**
     * 转成bean
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
        List<T> list = new ArrayList<>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }


    /**
     * 转成包含map的list
     *
     * @param json
     * @return
     */
    public static <T> List<Map<String, T>> toListMaps(String json) {
        List<Map<String, T>> list = null;
        list = gson.fromJson(json, new TypeToken<List<Map<String, T>>>() {
        }.getType());
        return list;
    }

    /**
     * 转成map的
     *
     * @param json
     * @return
     */
    public static <T> Map<String, T> gsonToMaps(String json) {
        Map<String, T> map = null;
        map = gson.fromJson(json, new TypeToken<Map<String, T>>() {
        }.getType());
        return map;
    }

    public static JsonObject parseObj(String json) {
        return new JsonParser().parse(json).getAsJsonObject();
    }

    public static JsonArray parseArray(String json) {
        return new JsonParser().parse(json).getAsJsonArray();
    }

    /**
     * json数组转化成List类型
     *
     * @param json json
     * @param cls  class
     * @param <T>  类型对象
     */
    public static <T> List<T> toList(String json, Class<T> cls) {
        return gson.fromJson(json, new ListOfSomething<>(cls));
    }

    /**
     * List参数类型
     *
     * @param <T>
     */
    static class ListOfSomething<T> implements ParameterizedType {

        private Class<?> mWrapped;

        public ListOfSomething(Class<T> wrapped) {
            this.mWrapped = wrapped;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{mWrapped};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
