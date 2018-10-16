package com.example;

import java.io.*;

public class FileCache implements CacheService{

    private final String cacheFile;

    public FileCache(String cacheFile){
        this.cacheFile = cacheFile;
    }

    @Override
    public void put(String key, String value) {
        File file = new File(cacheFile);
        if(!file.exists()){
            synchronized (this){
                if(!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(cacheFile, true)));
            out.println("key="+key+",value="+value);
            out.close();
        } catch (IOException e) {
        }

    }
}
