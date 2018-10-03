package com.example.problem2;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class TaskExecutor {

    private int poolSize;

    private Logger logger = Logger.getLogger(TaskExecutor.class);

    private ExecutorService executorService;

    private Map<Integer, Semaphore> semaphoreMap;

    public TaskExecutor(int poolSize) {
        this.poolSize=poolSize;
        executorService = Executors.newFixedThreadPool(poolSize);
        semaphoreMap = new HashMap<>();
        for(int i=0;i<poolSize;i++){
            semaphoreMap.put(i,new Semaphore(1));
        }
    }

    public void execute(Task task){
        executorService.submit(()->{
            Semaphore semaphore = semaphoreMap.get(task.getTaskId());
            SemaphoreAction semaphoreAction = new SemaphoreAction(semaphore);
            logger.info(String.format("Submitted Task:%s",task.getTaskId()));
            try {
                semaphoreAction.acquire();
                logger.info(String.format("Started Task:%s", task.getTaskId()));
                task.run();
            }finally {
                logger.info(String.format("Finished Task:%s",task.getTaskId()));
                semaphoreAction.release();
            }
        });
    }
}

