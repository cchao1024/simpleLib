package com.cchao.simplelib.core;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;

/**
 * @author cchao
 * @version 2018.1.26.
 */
@GlideModule
public class GlideAppModule extends AppGlideModule {
    private static final long MEMORY_CACHE_SIZE_BYTES = 10 << 10 << 10;
    private static final long DISK_CACHE_SIZE_BYTES = 50 << 10 << 10;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setMemoryCache(new LruResourceCache(MEMORY_CACHE_SIZE_BYTES));
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, DISK_CACHE_SIZE_BYTES));
    }
}