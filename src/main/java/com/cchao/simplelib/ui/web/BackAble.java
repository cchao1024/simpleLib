package com.cchao.simplelib.ui.web;

/**
 * @author cchao
 * @version 2019-05-10.
 */
public interface BackAble {
    default boolean onBackPressed() {
        return false;
    }
}