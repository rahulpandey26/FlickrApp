package com.robosoft.flickrapp.helper;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by rahul on 27/5/16.
 */
public class ImagesCache {

    private LruCache<String, Bitmap> mImagesMemoryCache;
    private static ImagesCache sImagesCache;

    private ImagesCache() {
        initializeCache();
    }

    public static ImagesCache getInstance() {
        if (sImagesCache == null) {
            sImagesCache = new ImagesCache();
        }
        return sImagesCache;
    }

    public void initializeCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mImagesMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap value) {
                // The sImagesCache size will be measured in kilobytes rather than number of items.

                int bitmapByteCount = value.getRowBytes() * value.getHeight();

                return bitmapByteCount / 1024;
            }
        };
    }

    public void addImageToMemoryCache(String key, Bitmap value) {
        if (mImagesMemoryCache != null && mImagesMemoryCache.get(key) == null) {
            mImagesMemoryCache.put(key, value);
        }
    }

    public Bitmap getImageFromMemoryCache(String key) {
        if (key != null) {
            return mImagesMemoryCache.get(key);
        } else {
            return null;
        }
    }

    public void removeImageFromMemoryCache(String key) {
        mImagesMemoryCache.remove(key);
    }

    public void clearCache() {
        if (mImagesMemoryCache != null) {
            mImagesMemoryCache.evictAll();
        }
    }
}
