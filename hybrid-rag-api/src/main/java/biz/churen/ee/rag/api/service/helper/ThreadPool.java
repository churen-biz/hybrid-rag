package biz.churen.ee.rag.api.service.helper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import cn.hutool.core.thread.ThreadUtil;


public class ThreadPool {

    public static final ExecutorService EXECUTOR_SERVICE_4 = ThreadUtil.newExecutor(
            4, 4, 102400
    );

    public static final ScheduledExecutorService SCHEDULER_EXECUTOR = Executors.newScheduledThreadPool(8);
}
