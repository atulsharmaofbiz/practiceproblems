package com.example;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import org.apache.log4j.Logger;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class Application
{
    public static void main( String[] args )
    {
        TaskExecutor taskExecutor = new TaskExecutor();
        Logger logger = Logger.getLogger(Application.class);

        Runnable start = getRunnable("start",logger);
        List<Runnable> parallelTasks = new ArrayList<>();
        for(int i=0;i<10;i++){
            parallelTasks.add(getRunnable("task"+i,logger));
        }
        Runnable end = getRunnable("end",logger);
        try {
            taskExecutor.execute(start, parallelTasks, end);
        }catch (RuntimeException ex){
            logger.error(ex);
        }
    }

    public static Runnable getRunnable(String name,Logger logger){
        return new Runnable() {
            @Override
            public void run() {
                if(name.contains("1"))
                    throw new RuntimeException("Exception in parallel task");
                logger.info(name);
            }
        };
    }
}
