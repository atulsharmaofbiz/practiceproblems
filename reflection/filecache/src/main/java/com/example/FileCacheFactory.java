package com.example;

public class FileCacheFactory implements CacheFactory {

    private String cacheFile;

    @Override
    public CacheService getCacheService() {
        String cacheFileLocation=System.getProperty("cacheFile");
        return new FileCache(cacheFileLocation);
    }
}
