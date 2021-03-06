package com.cchao.simplelib.core;

import com.cchao.simplelib.ui.interfaces.BaseStateView;
import com.cchao.simplelib.ui.interfaces.BaseView;
import com.cchao.simplelib.util.CallBacks;
import com.cchao.simplelib.view.state.StateSwitchable;
import com.kennyc.view.MultiStateView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
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
     * @return Consumer  T super Throwable
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
     * MultiStateView 异常处理 consumer
     *
     * @return Consumer  T super Throwable
     */
    public static Consumer<? super Throwable> getSwitchErrorConsumer(MultiStateView multiStateView) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (multiStateView != null) {
                    multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                }
                Logs.logException(throwable);
            }
        };
    }

    public static Consumer<? super Throwable> getSwitchableErrorConsumer(StateSwitchable switchable) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (switchable != null) {
                    switchable.setViewState(MultiStateView.VIEW_STATE_ERROR);
                }
                Logs.logException(throwable);
            }
        };
    }

    /**
     * 返回 发生异常切换Error界面 consumer
     *
     * @param stateView 状态view接口
     * @return Consumer  T super Throwable
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
     * @return Consumer  T super Throwable
     */
    public static Consumer<? super Throwable> getHideProgressConsumer(BaseView stateView) {
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
     * @return Consumer  T super Throwable
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
     * 倒计时
     *
     * @param count    次数
     * @param callback 定时回调
     * @param complete 完成回调
     */
    public static Disposable countDownConsumer(int count, CallBacks.Int callback, Runnable complete) {
        //
        return Flowable.intervalRange(0, count, 0, 1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(aLong -> {
                //
                callback.onCallBack(Integer.parseInt(String.valueOf(aLong)));
            })
            .doOnComplete(complete::run)
            .subscribe();
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
