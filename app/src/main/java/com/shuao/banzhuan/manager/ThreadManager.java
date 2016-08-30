package com.shuao.banzhuan.manager;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by flyonthemap on 16/8/4.
 * 使用线程池来管理线程，达到节约资源的目的
 */
public class ThreadManager  {
    private volatile static ThreadManager threadManager;
    private ThreadPoolProxy longPool;
    private ThreadPoolProxy shortPool;
    private ThreadManager(){}
    public static ThreadManager getThreadManager(){
        if(threadManager == null){
            synchronized (ThreadManager.class){
                if(threadManager == null){
                    threadManager = new ThreadManager();
                }
            }
        }
        return threadManager;
    }
    //  线程开的数目 CPU的核数*2 +1,联网
    public synchronized ThreadPoolProxy createLongPool(){
        if(longPool == null){
            longPool = new ThreadPoolProxy(4,9,5000L);
        }
        return longPool;
    }
    //  操作本地文件
    public synchronized ThreadPoolProxy createShortPool(){
        if(shortPool == null){
            shortPool = new ThreadPoolProxy(3,3 ,5000L);
        }
        return shortPool;
    }
    public class ThreadPoolProxy{

        //      核心线程池的大小
        private int corePoolSize;
        //      线程池中的最大的线程数量
        private int maximumPoolSize;
        private long aliveTime;
        private ThreadPoolExecutor threadPoolExecutor;
        public ThreadPoolProxy(int corePoolSize,int maximumPoolSize,long aliveTime) {
            this.maximumPoolSize = maximumPoolSize;
            this.corePoolSize = corePoolSize;
            this.aliveTime = aliveTime;
        }


        //      执行任务
        public void execute(Runnable runnable){
            if(threadPoolExecutor == null){
                threadPoolExecutor =
                        new ThreadPoolExecutor(corePoolSize,maximumPoolSize, aliveTime,
                                TimeUnit.MILLISECONDS,new LinkedBlockingDeque<Runnable>(10));

            }
            threadPoolExecutor.execute(runnable);
        }
        //取消任务
        public void cancel(Runnable runnable){
            if(threadPoolExecutor!=null && !threadPoolExecutor.isShutdown( ) && !threadPoolExecutor.isTerminated()){
                //移除异步任务
                threadPoolExecutor.remove(runnable);
            }
        }
    }
}
