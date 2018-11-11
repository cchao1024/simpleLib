package com.cchao.simplelib.core;

import com.cchao.simplelib.ui.interfaces.BaseStateView;
import com.cchao.simplelib.ui.interfaces.BaseView;
import com.cchao.simplelib.util.ExceptionCollect;

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
 * cchao 2017-7-28 09:13:30
 */
public class RxHelper {

    /**
     * 统一线程处理,
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerTran() {
        // compose简化线程
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static Consumer<? super Throwable> getErrorConsumer() {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                ExceptionCollect.logException(throwable);
            }
        };
    }

    public static Consumer<? super Throwable> getSwitchErrorConsumer(BaseStateView baseView) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (baseView != null) {
                    baseView.switchView(BaseStateView.NET_ERROR);
                }
                ExceptionCollect.logException(throwable);
            }
        };
    }
    public static Consumer<? super Throwable> getHideProgressConsumer(BaseStateView baseView) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (baseView != null) {
                    baseView.hideProgress();
                }
                ExceptionCollect.logException(throwable);
            }
        };
    }

    public static Consumer<? super Throwable> getErrorTextConsumer(BaseView baseView) {
        return new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                ExceptionCollect.logException(throwable);
                if (baseView != null) {
                    baseView.showError();
                    baseView.hideProgress();
                }
            }
        };
    }

    public static Disposable timerConsumer(long delay, Consumer<Long> consumer) {
        return Observable.timer(delay, TimeUnit.MILLISECONDS)
            .compose(rxSchedulerTran())
            .subscribe(consumer,RxHelper.getErrorConsumer());
    }

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
                ExceptionCollect.logException(e);
            }

            @Override
            public void onComplete() {

            }
        };
    }
}
