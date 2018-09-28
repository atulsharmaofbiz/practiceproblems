package com.example;

import org.apache.log4j.Logger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TaskExecutor {

    private Logger logger = Logger.getLogger(TaskExecutor.class);

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
}
