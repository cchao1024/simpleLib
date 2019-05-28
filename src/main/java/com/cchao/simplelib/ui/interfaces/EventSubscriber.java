package com.cchao.simplelib.ui.interfaces;

import com.cchao.simplelib.model.event.CommonEvent;

/**
 * 事件订阅者
 * @author cchao
 * @version 2019-05-28.
 */
public interface EventSubscriber {

    /** 事件发生，
     * @param event commonEvent
     */
    void onEvent(CommonEvent event);
}
