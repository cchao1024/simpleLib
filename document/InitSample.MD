# 类库初始化 范例
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
        public LibCore.RouterConfig getRouterConfig() {
            return new LibCore.RouterConfig() {
                @Override
                public boolean checkLogin(Context context, boolean isNeedLogin) {
                    boolean hasLogin = AppUtils.getIsLogin(context);
                    if (isNeedLogin && !hasLogin) {
                        Router.turnTo(context, LoginActivity.class)
                            .startForResult(Constants.LOGIN);
                    }
                    return hasLogin;
                }

                @Override
                public boolean checkStatus(Context context, String statusName, boolean isNeedObtainStatus) {
                    switch (statusName) {
                        case "bind_email":
                            // 先检测登录
                            boolean hasLogin = checkLogin(context, true);
                            if (!hasLogin) {
                                return false;
                            }
                            boolean hasBind = UserManager.getUserBean().hasBindEmail();
                            // 未绑EMAIL
                            if (!hasBind && isNeedObtainStatus) {
                                // TODO 跳转邮箱绑定页
                                showToast(Constants.PLEASE_BIND_EMAIL_FIRST);
                            }
                            return hasBind;
                    }
                    return false;
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

```