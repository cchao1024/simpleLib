package com.cchao.simplelib.core;

import com.cchao.simplelib.ui.interfaces.BaseStateView;
import com.cchao.simplelib.ui.interfaces.BaseView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author cchao
 * @date 2017-7-28 09:13:30
 */
public class RxHelper {

    /**
     * 统一线程处理, 主线程消费
     */
    public static <T> ObservableTransformer<T, T> toMain() {
        // compose简化线程
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 返回通用的 异常处理 consumer
     *
     * @return Consumer<?       super       Throwable>
     */
    public static Consumer<? super Throwable> getErrorConsumer() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Logs.logException(throwable);
            }
        };
    }

    /**
     * 返回 发生异常切换Error界面 consumer
     *
     * @param stateView 状态view接口
     * @return Consumer<?       super       Throwable>
     */
    public static Consumer<? super Throwable> getSwitchErrorConsumer(BaseStateView stateView) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (stateView != null) {
                    stateView.switchView(BaseStateView.NET_ERROR);
                }
                Logs.logException(throwable);
            }
        };
    }

    /**
     * 返回 发生异常隐藏进度加载 consumer
     *
     * @param stateView 状态切换接口
     * @return Consumer<?       super       Throwable>
     */
    public static Consumer<? super Throwable> getHideProgressConsumer(BaseStateView stateView) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (stateView != null) {
                    stateView.hideProgress();
                }
                Logs.logException(throwable);
            }
        };
    }

    /**
     * 返回 发生异常显示异常文案 consumer
     *
     * @param baseView 基础界面接口
     * @return Consumer<?       super       Throwable>
     */
    public static Consumer<? super Throwable> getErrorTextConsumer(BaseView baseView) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Logs.logException(throwable);
                if (baseView != null) {
                    baseView.showError();
                    baseView.hideProgress();
                }
            }
        };
    }

    /**
     * 延时执行
     *
     * @param delayMSeconds 毫秒数
     * @return Disposable
     */
    public static Disposable timerConsumer(long delayMSeconds, Consumer<Long> consumer) {
        return Observable.timer(delayMSeconds, TimeUnit.MILLISECONDS)
            .compose(toMain())
            .subscribe(consumer, RxHelper.getErrorConsumer());
    }

    /**
     * 空订阅，仅捕获异常
     *
     * @param <T> obj
     */
    public static <T> Observer<T> getNothingObserver() {

        return new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable e) {
                Logs.logException(e);
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
