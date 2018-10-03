package com.example.problem2;

import java.util.concurrent.Semaphore;

public class SemaphoreAction {

    private boolean isAcquired;

    private Semaphore semaphore;

    public SemaphoreAction(Semaphore semaphore){
        this.semaphore = semaphore;
    }

    public boolean acquire(){
        try {
            semaphore.acquire();
            isAcquired = true;
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public void release() {
        if (isAcquired) {
            semaphore.release();
            isAcquired = false;
        }
    }
}
