package com.xxl.job.core.biz.impl;

import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.ShutdownBiz;
import com.xxl.job.core.biz.model.LogResult;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.enums.RegistryConfig;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.glue.GlueFactory;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.impl.GlueJobHandler;
import com.xxl.job.core.handler.impl.ScriptJobHandler;
import com.xxl.job.core.log.XxlJobFileAppender;
import com.xxl.job.core.thread.ExecutorRegistryThread;
import com.xxl.job.core.thread.JobThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuxueli on 17/3/1.
 */
public class ShutdownBizImpl implements ShutdownBiz {
    private static Logger logger = LoggerFactory.getLogger(ShutdownBizImpl.class);



    @Override
    public ReturnT<String> shutdownAndWaitingExcutor(String token) {
        //取消心跳注册
        // destroy Registry-Server
        ExecutorRegistryThread registryThread = ExecutorRegistryThread.getInstance();
        if(registryThread != null && registryThread.isAlive()){
            registryThread.toStop();
        }

        //等待执行线程完成
        while (true) {
            boolean hasFinished = true;
            ConcurrentHashMap<Integer, JobThread> allJobThread = XxlJobExecutor.getAllJobThread();
            for (Map.Entry<Integer, JobThread> integerJobThreadEntry : allJobThread.entrySet()) {
                JobThread jobThread = integerJobThreadEntry.getValue();
                if (jobThread != null && jobThread.isAlive()) {
                    if(jobThread.isRunningOrHasQueue()){
                        hasFinished = false;
                        break;
                    }
                }
            }
            if(hasFinished){
                break;
            }
            else {
                try {
                    TimeUnit.SECONDS.sleep(RegistryConfig.BEAT_TIMEOUT);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }

        return ReturnT.SUCCESS;
    }

}
