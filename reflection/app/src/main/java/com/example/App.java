package com.example;

public class App
{
    public static void main( String[] args )
    {
        try {
        String cacheFactoryName=System.getProperty("cacheFactory");
        Class cacheFactoryClass = null;
        cacheFactoryClass = Class.forName(cacheFactoryName);
        CacheFactory cacheFactory= ((CacheFactory) cacheFactoryClass.newInstance());
        CacheService cacheService=cacheFactory.getCacheService();
        cacheService.put("1","A");
        cacheService.put("2","B");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
