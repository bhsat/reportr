package com.yahoo.reportr.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.util.LruCache;

/**
 * Created by bhavanis on 1/25/16.
 */
public class MemCache {

    private static MemCache instance;
    private LruCache<Object, Object> lru;
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = maxMemory / 8;

    private MemCache() {
        lru = new LruCache<Object, Object>(cacheSize);
    }

    public static MemCache getInstance() {
        if (instance == null) {
            instance = new MemCache();
        }
        return instance;
    }

    public LruCache<Object, Object> getLru() {
        return lru;
    }
}
