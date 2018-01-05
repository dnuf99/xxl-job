package com.xxl.job.core.biz;

import com.xxl.job.core.biz.model.LogResult;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;


public interface ShutdownBiz {

    /**
     * 取消心跳注册，同时等待执行线程执行完成后返回
     * @return
     */
    public ReturnT<String> shutdownAndWaitingExcutor(String token);


}
