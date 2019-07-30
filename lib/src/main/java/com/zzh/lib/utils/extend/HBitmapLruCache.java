package com.zzh.lib.utils.extend;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Bitmap的lru算法缓存管理
 */
public class HBitmapLruCache extends LruCache<String, Bitmap> {
    private static HBitmapLruCache sInstance;

    private HBitmapLruCache() {
        super((int) ((double) Runtime.getRuntime().maxMemory() / 16));
    }

    public static HBitmapLruCache getInstance() {
        if (sInstance == null) {
            synchronized (HBitmapLruCache.class) {
                if (sInstance == null) {
                    sInstance = new HBitmapLruCache();
                }
            }
        }
        return sInstance;
    }

    @Override
    protected final int sizeOf(String key, Bitmap value) {
        return value.getByteCount();
    }
}
