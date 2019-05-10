package com.cchao.simplelib.core;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.cchao.simplelib.Const;
import com.cchao.simplelib.http.SslCertHelper;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author cchao
 * @version 2018.1.26.
 */
@GlideModule
public class GlideAppModule extends AppGlideModule {
    private static final long MEMORY_CACHE_SIZE_BYTES = 30 << 10 << 10;
    private static final long DISK_CACHE_SIZE_BYTES = 100 << 10 << 10;

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setMemoryCache(new LruResourceCache(MEMORY_CACHE_SIZE_BYTES));
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, DISK_CACHE_SIZE_BYTES));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .connectTimeout(Const.Config.IMAGE_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Const.Config.IMAGE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Const.Config.IMAGE_TIMEOUT, TimeUnit.SECONDS);

        SslCertHelper.enableTrustAllCert(builder);

        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(builder.build()));
    }
}