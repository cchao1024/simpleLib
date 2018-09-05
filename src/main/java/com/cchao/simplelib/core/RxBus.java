package com.cchao.simplelib.core;

import com.cchao.simplelib.model.event.CommonEvent;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Description: rxBus
 * Created by cchao on 2017/5/17.
 */

public class RxBus {

    // 主题
    private final Subject<Object> mBus;

    private RxBus() {
        mBus = PublishSubject.create();
    }

    public static RxBus getDefault() {
        return RxBusHolder.INSTANCE;
    }

    private static class RxBusHolder {
        private static final RxBus INSTANCE = new RxBus();
    }

    // 提供了一个新的事件
    public void post(Object o) {
        mBus.onNext(o);
    }

    /**
     * 默认在主线程
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType).observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 传递一个通用实体，主要用于在一个回调里面写 switch
     */
    public Disposable toObservable(CommonCodeCallBack callBack) {
        return toObservable(CommonEvent.class)
            .subscribe(new Consumer<CommonEvent>() {
                @Override
                public void accept(CommonEvent commonEvent) throws Exception {
                    if (callBack != null) {
                        callBack.onConsumer(commonEvent);
                    }
                }
            }, RxHelper.getErrorConsumer());
    }

    /**
     * 订阅特定code事件
     *
     * @param code
     * @param callBack 回调
     * @return Disposable
     */
    public Disposable toObserveCode(int code,CommonCodeCallBack callBack) {
        return toObservable(CommonEvent.class)
            .subscribe(new Consumer<CommonEvent>() {
                @Override
                public void accept(CommonEvent commonEvent) throws Exception {
                    if (commonEvent.getCode() == code && callBack != null) {
                        callBack.onConsumer(commonEvent);
                    }
                }
            }, RxHelper.getErrorConsumer());
    }

    /**
     * 订阅特定code事件 , 只订阅一次，收到事件就关闭订阅
     *
     * @param callBack 回调
     * @return Disposable
     */
    public Disposable toObserveCodeOnce(int code, CommonCodeCallBack callBack) {
        Disposable[] finalDisposable = {null};
        finalDisposable[0] = toObserveCode(code, new CommonCodeCallBack() {
            @Override
            public void onConsumer(CommonEvent commonEvent) {
                callBack.onConsumer(commonEvent);
                finalDisposable[0].dispose();
            }
        });
        return finalDisposable[0];
    }

    /**
     * 发射 不同的code 事件
     */
    public void postEvent(int code) {
        post(CommonEvent.newEvent(code));
    }

    public void postEvent(int code, String content) {
        post(CommonEvent.newEvent(code, content));
    }

    public void postEvent(int code, Object bean) {
        post(CommonEvent.newEvent(code, bean));
    }

    /**
     * 通用的回调,如果code匹配就调用
     */
    public interface CommonCodeCallBack {
        void onConsumer(CommonEvent commonEvent);
    }
}
