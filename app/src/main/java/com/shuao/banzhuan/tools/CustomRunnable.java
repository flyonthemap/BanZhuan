package com.shuao.banzhuan.tools;

import android.os.Message;

/**
 * Created by flyonthemap on 2017/3/8.
 */
public abstract class CustomRunnable<Params, Result> implements ThreadPoolService.CusRunnable {

    private Params[] params;
    private Result result;
    private Thread cThread;// 当前运行后台任务的线程
    private boolean interruptTag = false;//不能设置为static类型

    public CustomRunnable(Params... params) {
        this.params = params;
    }

    /**
     * 在线程线程任务前执行的方法 即在ThreadPoolExecutor.execute(Runnalbe r)执行前执行的方法
     */
    public void beforTask() {
    };

    /**
     * 放在线程之中执行的任务方法
     *
     * @param params
     *            给任务方法传递的参数
     * @return 任务执行完后的返回值
     */
    public abstract Result executeTask(Params... param);

    /**
     * 在线程线程任务后执行的方法 即在ThreadPoolExecutor.execute(Runnalbe r)执行完后执行的方法
     *
     * @param result
     *            线程任务完成后返回的数据
     */
    public void afterTask(Result result) {

    };

    @Override
    public void run() {
        cThread = Thread.currentThread();
        result = executeTask(params);
        if (!interruptTag) {

            Message message = mhandler.obtainMessage(RESULT, CustomRunnable.this);
            message.sendToTarget();
        }
    }

    public Result getResult() {
        return result;
    }

    public void cancleTask() {
        if (cThread != null) {
            interruptTag = true;
            // 冲断线程
            cThread.interrupt();
        }
    }
}

