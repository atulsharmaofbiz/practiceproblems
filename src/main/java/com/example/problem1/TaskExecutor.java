package com.example.problem1;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class TaskExecutor {

    private Logger logger = Logger.getLogger(TaskExecutor.class);

    private final int poolSize=100;

    private ExecutorService executorService;

    public TaskExecutor(){
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    public void execute(Runnable start,final List<Runnable> paralledTasks,Runnable end){
        try {
            CompletableFuture futureStart = CompletableFuture.runAsync(start);
            futureStart.join();
            logger.debug("Finished Task1");
            List<CompletableFuture<Void>> futures = paralledTasks
                    .stream()
                    .map(s -> CompletableFuture.runAsync(s))
                    .collect(Collectors.toList());
            CompletableFuture<Void> completableFutures =
                    CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
            completableFutures.join();
            logger.debug("Finished parallelTasks");

            CompletableFuture futureEnd = CompletableFuture.runAsync(end);
            futureEnd.join();
            logger.debug("Finished End Task");
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public void execute2(Runnable start,final List<Runnable> paralledTasks,Runnable end){
        try {
            Thread tStart = new Thread(()->{
                start.run();
            });
            tStart.start();
            tStart.join();
            logger.debug("Finished Task1");
            CountDownLatch latch = new CountDownLatch(paralledTasks.size());
            paralledTasks.forEach(pTask->{
                executorService.submit(()->{
                    try{
                        pTask.run();
                    }finally {
                        latch.countDown();
                    }
                });
            });
            latch.await();
            logger.debug("Finished parallelTasks");

            Thread tEnd = new Thread(()->{
                end.run();
            });
            tEnd.start();
            tEnd.join();
            logger.debug("Finished End Task");
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
