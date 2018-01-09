package com.xxl.job.admin.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.quartz.Trigger.TriggerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.core.schedule.XxlJobDynamicScheduler;
import com.xxl.job.admin.dao.XxlJobInfoDao;
import com.xxl.job.admin.dao.XxlJobLogDao;
import com.xxl.job.admin.dao.XxlJobRegistryDao;
import com.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * @author xuxueli 2017-07-27 21:54:20
 */
@Service
public class AdminBizImpl implements AdminBiz {
    private static Logger logger = LoggerFactory.getLogger(AdminBizImpl.class);

    @Resource
    public XxlJobLogDao xxlJobLogDao;
    @Resource
    private XxlJobInfoDao xxlJobInfoDao;
    @Resource
    private XxlJobRegistryDao xxlJobRegistryDao;
    @Resource
    private XxlJobService xxlJobService;


    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        for (HandleCallbackParam handleCallbackParam: callbackParamList) {
            ReturnT<String> callbackResult = callback(handleCallbackParam);
            logger.info(">>>>>>>>> JobApiController.callback {}, handleCallbackParam={}, callbackResult={}",
                    (callbackResult.getCode()==IJobHandler.SUCCESS.getCode()?"success":"fail"), handleCallbackParam, callbackResult);
        }

        return ReturnT.SUCCESS;
    }

    private ReturnT<String> callback(HandleCallbackParam handleCallbackParam) {
        // valid log item
        XxlJobLog log = xxlJobLogDao.load(handleCallbackParam.getLogId());
        if (log == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "log item not found.");
        }

        // trigger success, to trigger child job, and avoid repeat trigger child job
        String callbackMsg = null;
        if (IJobHandler.SUCCESS.getCode() ==handleCallbackParam.getExecuteResult().getCode()) {
            XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(log.getJobId());
            if (xxlJobInfo!=null && StringUtils.isNotBlank(xxlJobInfo.getChildJobKey())) {
                callbackMsg = "<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发子任务<<<<<<<<<<< </span><br>";
                String[] childJobKeys = xxlJobInfo.getChildJobKey().split(",");
                for (int i = 0; i < childJobKeys.length; i++) {
                	//判断后续任务是否处于暂停状态，若为暂停状态则不触发
                    String[] jobKeyArr = childJobKeys[i].split("_");
                    if (jobKeyArr!=null && jobKeyArr.length==2) {
                    	if(isChildJobNomal(jobKeyArr[0], jobKeyArr[1])) {
	                    	ReturnT<String> triggerChildResult = xxlJobService.triggerJob(Integer.valueOf(jobKeyArr[1]));
	                        // add msg
                            callbackMsg += MessageFormat.format("<br> {0}/{1} 触发后续任务{2}, 后续任务Key: {3}, 后续任务触发备注: {4}",
	                                (i+1), childJobKeys.length, (triggerChildResult.getCode()==ReturnT.SUCCESS_CODE?"成功":"失败"), childJobKeys[i], triggerChildResult.getMsg());
                    	}else {
                            callbackMsg += MessageFormat.format("<br> {0}/{1} 触发后续任务失败, 后续任务为暂停发起状态, 后续任务Key: {2}",
                                    (i+1), childJobKeys.length, childJobKeys[i]);
                    	}
                    } else {
                        callbackMsg += MessageFormat.format("<br> {0}/{1} 触发后续任务失败, 后续任务Key格式错误, 后续任务Key: {2}",
                                (i+1), childJobKeys.length, childJobKeys[i]);
                    }
                	
                }

            } else if (IJobHandler.FAIL_RETRY.getCode() == handleCallbackParam.getExecuteResult().getCode()){
                ReturnT<String> retryTriggerResult = xxlJobService.triggerJob(log.getJobId());
                callbackMsg = "<br><br><span style=\"color:#F39C12;\" > >>>>>>>>>>>执行失败重试<<<<<<<<<<< </span><br>";

                callbackMsg += MessageFormat.format("触发{0}, 触发备注: {1}",
                        (retryTriggerResult.getCode()==ReturnT.SUCCESS_CODE?"成功":"失败"), retryTriggerResult.getMsg());
            }
        }

        // handle msg
        StringBuffer handleMsg = new StringBuffer();
        if (log.getHandleMsg()!=null) {
            handleMsg.append(log.getHandleMsg()).append("<br>");
        }
        if (handleCallbackParam.getExecuteResult().getMsg() != null) {
            handleMsg.append(handleCallbackParam.getExecuteResult().getMsg());
        }
        if (callbackMsg !=null) {
            handleMsg.append("<br>后续任务触发备注：").append(callbackMsg);
        }

        // success, save log
        log.setHandleTime(new Date());
        log.setHandleCode(handleCallbackParam.getExecuteResult().getCode());
        log.setHandleMsg(handleMsg.toString());
        xxlJobLogDao.updateHandleInfo(log);

        return ReturnT.SUCCESS;
    }

    private boolean isChildJobNomal(String group, String name) {
    	TriggerState status = XxlJobDynamicScheduler.queryJobStatus(group, name);
    	if(status != null && TriggerState.NORMAL == status) {
    		return true;
    	}
		return false;
	}

	@Override
    public ReturnT<String> registry(RegistryParam registryParam) {
        int ret = xxlJobRegistryDao.registryUpdate(registryParam.getRegistGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
        if (ret < 1) {
            xxlJobRegistryDao.registrySave(registryParam.getRegistGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
        }
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> registryRemove(RegistryParam registryParam) {
        xxlJobRegistryDao.registryDelete(registryParam.getRegistGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> triggerJob(int jobId) {
        return xxlJobService.triggerJob(jobId);
    }

}
