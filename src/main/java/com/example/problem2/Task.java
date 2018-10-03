package com.example.problem2;

public class Task implements Runnable {


    private int taskId;

    private Runnable runnable;

    public Task(int taskId,Runnable runnable){
        this.taskId = taskId;
        this.runnable = runnable;
    }

    public int getTaskId() {
        return taskId;
    }

    @Override
    public void run() {
        runnable.run();
    }
}
