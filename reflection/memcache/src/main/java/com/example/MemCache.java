package com.example;

import java.util.HashMap;
import java.util.Map;

public class MemCache implements CacheService{
    private Map<String,String> cache;
    @Override
    public void put(String key, String value) {
        if(cache==null){
            synchronized (this){
                if(cache==null)
                    cache=new HashMap<>();
            }
        }
        cache.put(key,value);
    }
}
