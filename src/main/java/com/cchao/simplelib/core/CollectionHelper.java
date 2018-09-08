package com.cchao.simplelib.core;

import java.util.List;
import java.util.Map;

/**
 * @author cchao
 * @version 2018/6/12.
 */
public class CollectionHelper {

    public static boolean isEmptyList(List list) {
        return list == null || list.isEmpty();
    }

    public static boolean isEmptyArr(Object[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isNotEmptyList(List list) {
        return !isEmptyList(list);
    }

    public static boolean isEmptyMap(Map list) {
        return list == null || list.isEmpty();
    }

    public static boolean isNotEmptyMap(Map list) {
        return !isEmptyMap(list);
    }
}
