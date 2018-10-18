package com.example.problem3;

import com.example.problem3.client.FileServerClient;
import com.example.problem3.client.FileServerProxy;
import com.example.problem3.server.FileServer;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.*;

public class Application {

    public static final int POOL_SIZE=10;

    public static final String fileUrl="/Users/atul/testtenders/test-file-server/spring.dmg";
    public static final String tgtDir="/Users/atul/testtenders/test-file-server/out";

    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        FileServer fileServer = new FileServer();
        fileServer.start();
        FileServerProxy proxy = new FileServerProxy(fileServer);
        FileServerClient client = new FileServerClient(proxy);
        startFileConsumers(client);

    }

    private static void startFileConsumers(FileServerClient client) {
        CountDownLatch latch = new CountDownLatch(10);
        for(int i=0;i<5;i++) {
            String srcFile = fileUrl.replace("spring","spring"+i);
            String fName=FileUtil.getFileName(srcFile);
            executor.submit(() -> {
                String tgtFilePath = tgtDir + "/" + fName;
                try {
                    client.downloadFile(srcFile, tgtFilePath);
                } catch (Exception e) {
                    e.printStackTrace();
                    latch.countDown();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
