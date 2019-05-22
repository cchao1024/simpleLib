# Desc
simpleLib 是笔者开发中积累下来，觉得切实好用的一些方法和约束整合。旨在**帮助基于该类库的开发者能够高效的完成项目开发**。
项目持续更新中，觉得不错的小伙伴可以 star 和 pull requests

项目深度依赖以下开源类库

* [data-binding](https://developer.android.com/topic/libraries/data-binding)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [glide](https://github.com/bumptech/glide)
* [okHttp](https://github.com/square/okhttp)
* [Gson](https://github.com/google/gson)


# LibCore
simpleLib 的核心，进行初始化和依赖对象的赋值, 由 InfoSupport 和 LibConfig 提供配置项
* InfoSupport 返回基本且必须的参数
* LibConfig 配置关于样式上的自定义

## InfoSupport
传入 HttpClient ，debug状态，appName，日志收集回调等。部分方法提供了默认实现，
比如 getOkHttpClient(), 如果不传入 应用层的 OkHttp 则 SimpleLib 会提供 **默认的 OkHttpClient 对象**
当 Logs 发生日志统计时，会调用 LibCore.ILogEvents 执行日志收集（比如如下的 Bugly 收集）

## LibConfig
自定义的配置项，比如加载对话框，标题栏，页面加载图，加载失败图等。非必选的，不配置的话 会返回默认的实现。

像这样
```java

// 在 Application类对 SimpleLib 进行初始化，传入所需的 必要参数
private void initSimpleLib() {
    LibCore.init(this, new LibCore.InfoSupport() {

        @Override
        public OkHttpClient getOkHttpClient() {
            return OkHttpManager.getOriginClient();
        }

        @Override
        public boolean isDebug() {
            return BuildConfig.Debug;
        }

        @Override
        public String getAppName() {
            return getApplicationContext().getString(R.string.app_name);
        }

        @Override
        public LibCore.ILogEvents getLogEvents() {
            return new LibCore.ILogEvents() {
                @Override
                public void logEvent(String tag, String event) {
                    BuglyLog.i(tag, event);
                }

                @Override
                public void logException(Throwable e) {
                    CrashReport.postCatchedException(e);
                }
            };
        }
    });

    LibCore.setLibConfig(new LibCore.LibConfig() {
        @Override
        public BaseView getBaseViewDelegate(Context context) {
            return new MyBaseViewDelegate(context);
        }

        @Override
        public BaseStateView getStateViewDelegate(Context context, View childContent, Runnable retryCallBack) {
            return new MyStateViewDelegate(context, childContent, retryCallBack);
        }

        @Override
        public DefaultTitleBarDelegate getTitleBarDelegate(Context context, ViewGroup parent) {
            return new MyTitleBarDelegate(context, parent);
        }
    });
}
```

# ui
ui提供了 基础的 Activity 和 Fragment ,各级 Base 类通过实现接口方便上层业务交互时调用
## 接口 

- BaseView 提供简单的界面交互操作
- BaseStateView 提供界面状态切换（加载、异常、空数据等）
- TitleBar 提供基本的标题栏操作

```java
public interface BaseView {

    void showError();

    void showText(String string);

    default void showText(@StringRes int string) {
        showText(UiHelper.getString(string));
    }

    void showProgress(String string);

    void showProgress();

    void hideProgress();
}

public interface BaseStateView extends BaseView {

    String LOADING = "Loading";
    String NET_ERROR = "NetError";
    String CONTENT = "Content";
    String EMPTY = "Empty";

    @StringDef({LOADING, NET_ERROR, CONTENT, EMPTY})
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewType {
    }

    /** 切换不同的View状态 */
    void switchView(@ViewType String viewType);
}
```

## 实现
基类是有 RxJava 的 CompositeDisposable 成员变量，在执行**网路交互、事件订阅**时需要调用 **addSubscribe** 将订阅收集起来，会在 **OnDestroy** 时取消订阅的回调。

- BaseActivity/BaseFragment 实现接口 BaseView 的基类。特别的，
- BaseStatefulActivity/BaseStatefulFragment 实现接口 BaseStateView 的基类
- BaseTitleBarActivity 提供简单 TitleBar 操作的基类，该类通过委托类提供了 自定义的线性布局和Toolbar（暂未实现）两种实现
- SimpleLazyFragment 懒加载的 Fragment，用于ViewPager 等，

# Core
该目录提供核心的基础类，
- Logs 日志类，包括日志输出与日志收集
- RxBus 基于 RxJava 的事件总线
- PrefHelper 对 SharedPreferences 基本操作
- RxHelper 对 RxJava 一些方法调用封装
- Router 对 Activity 跳转和传参的封装
- UiHelper 界面相关操作的整合
- GsonUtil 对 Json 的操作

## Logs
日志使用的默认的Log实现，其中 logEvent 和 logException 是作为日志事件收集（InfoSupport 需实现异常收集平台的收集代码）使用的，便于异常发生时能够回溯用户的行为。
NOTE: 网络请求默认输出并上报异常到日志收集，具体请查阅http目录下的 RequestLogInterceptor 和 RespExceptionLogInterceptor

## RxBus 
RxBus 基于 Rxjava 的事件订阅，可以在任一线程 发送事件，事件可以为通用事件 CommonEvent，加上数字 Code 作为标识，也可以通过特定对象传递大的事件数据。
简单的事件传递 像这样
```java
// 发送事件
RxBus.get().postEvent(Constant.Code.LOG_IN);

// 订阅事件
addSubscribe(RxBus.get().toObservable(event -> {
    switch (event.getCode()) {
        case Constant.Code.LOG_IN:
            showText("Login");
            break;
        case Constant.Code.LOG_OUT:
            showText("LogOut");
            break;
        case Constant.Code.SIGN_UP:
            showText("SignUp");
            break;
    }
}));
```

## RxHelper
RxHelper 提供了线程切换和结合基础界面接口 BaseView/BaseStateView 的交互和状态切换

- toMain 线程从 io 到 main 的切换
- getSwitchErrorConsumer 传入 BaseStateView 切换视图为网络异常状态
- getHideProgressConsumer 隐藏 加载框
- getErrorTextConsumer 弹出网络异常文案

执行 Error consumer 均会触发 Logs 的异常日志收集上报

我们来个简单的网络交互例子：

```java
// 显示 加载框，开始发起网络请求
showProgress();
addSubscribe(RetrofitHelper.getApis().login(email, password)
    .compose(RxUtil.rxSchedulerHelper())
    .subscribe((respBean -> {
        // 不论返回啥，均取消加载框
        hideProgress();
        // 如果失败，弹出异常文案
        if (respBean.isCodeFail()) {
            showText(respBean.getMsg());
            return;
        }
        // 发送事件，登录成功
        RxBus.get().postEvent(Constant.Code.LOG_IN);
    }, RxUtil.getHideProgressError(this));

```
getSwitchErrorConsumer

## UiHelper
提供笔者认为能简化代码，提高效率的工具方法，

- dp2px/sp2px , getScreenWidth/Height dp转化，屏幕宽高
- setVisibleElseGone(View,boolean) 传入 true 显示 visible，否则 gone
- runOnUiThread 使用主线程运行传入的方法
- getDrawable/Color/String 获取资源

Note： 部分需要 Context 的方法会使用 LibCore 传入的上下文，无需调用者再传入

# 其他目录
- util 该模块为基础工具类的整合，包含均为核心常用方法
- view 一些笔者觉得能用且好用的 View，特别的 *state* 目录下是 BaseStateView 状态切换使用到的默认界面
- Const 类库依赖的常量

# Todo
- TitleBarDelegate 提供 Toolbar的添加右侧按钮 的实现
- 提供 MVVM 的支持(mvvm分支在尝试)，做到既可以简单实用，又可以在复杂业务中使用 MVVM 
