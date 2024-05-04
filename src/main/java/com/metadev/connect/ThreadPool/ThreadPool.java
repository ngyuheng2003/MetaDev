package com.metadev.connect.ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ThreadPool {

    private BlockingQueue taskQueue = null;
    private List<PoolThreadRunnable> runnables = new ArrayList<>();
    private boolean isStopped = false;
    private boolean updateFlag;

    public ThreadPool(int numberOfThreads, int maxNumberOfTasks){
        taskQueue = new ArrayBlockingQueue(maxNumberOfTasks);

        for(int i = 0; i < numberOfThreads; i++){
            PoolThreadRunnable poolThreadRunnable = new PoolThreadRunnable(taskQueue);

            runnables.add(poolThreadRunnable);
        }

        for(PoolThreadRunnable runnable : runnables){
            new Thread(runnable).start();
        }
    }

    public synchronized void execute(Runnable task) throws Exception{
        if(this.isStopped)
            throw new IllegalStateException("ThreadPool is stopped");

        this.taskQueue.offer(task);
    }

    public synchronized void stop(){
        this.isStopped = true;
        for(PoolThreadRunnable runnable : runnables){
            runnable.doStop();
        }
    }

    public synchronized void waitTaskFinished(){
        while(this.taskQueue.size() > 0){
            try{
                Thread.sleep(1);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public synchronized int getQueueSize(){
        return taskQueue.size();
    }

    public synchronized void setUpdateFlag(boolean flag){
        updateFlag = flag;
    }

    public synchronized boolean getUpdateFlag(){
         return updateFlag;
    }
}
