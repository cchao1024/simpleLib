package com.cchao.simplelib.core;

import com.cchao.simplelib.util.StringHelper;

import java.util.List;
import java.util.Map;

/**
 * @author cchao
 * @version 2018/6/12.
 */
public class CollectionHelper {

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmptyArr(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

    public static boolean isEmpty(Map list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmpty(Map list) {
        return !isEmpty(list);
    }

    public static boolean mapContainEmpty(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (StringHelper.isEmpty(entry.getValue())) {
                return true;
            }
        }
        return false;
    }

    public static <T> T getFirst(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    public static <T> T getLast(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(list.size() - 1) : null;
    }
}
