package com.example.problem2;

import org.apache.log4j.Logger;

import java.util.Random;

public class Application {

    private static Logger logger = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        int poolSize=100;
        TaskExecutor taskExecutor = new TaskExecutor(poolSize);
        while (true) {
            int taskId = Math.abs(new Random().nextInt())%5;
            Task task = getTasks(taskId,logger);
            taskExecutor.execute(task);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Task getTasks(int taskId,Logger logger) {
        return new Task(taskId, new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
