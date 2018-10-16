package com.example;

public class MemCacheFactory implements CacheFactory {
    @Override
    public CacheService getCacheService() {
        return new MemCache();
    }
}
