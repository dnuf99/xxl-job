package com.xxl.job.admin.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xxl.job.admin.core.enums.ExecutorFailStrategyEnum;
import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobMontorInfo;
import com.xxl.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.xxl.job.admin.core.schedule.XxlJobDynamicScheduler;
import com.xxl.job.admin.dao.XxlJobGroupDao;
import com.xxl.job.admin.dao.XxlJobInfoDao;
import com.xxl.job.admin.dao.XxlJobLogDao;
import com.xxl.job.admin.dao.XxlJobLogGlueDao;
import com.xxl.job.admin.dao.XxlJobMontorDao;
import com.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;

/**
 * core job action for xxl-job
 * @author xuxueli 2016-5-28 15:30:33
 */
@Service
public class XxlJobServiceImpl implements XxlJobService {
	private static Logger logger = LoggerFactory.getLogger(XxlJobServiceImpl.class);

	
	@Resource
	private XxlJobGroupDao xxlJobGroupDao;
	@Resource
	private XxlJobInfoDao xxlJobInfoDao;
	@Resource
	public XxlJobLogDao xxlJobLogDao;
	@Resource
	private XxlJobLogGlueDao xxlJobLogGlueDao;
	
	@Resource
	private XxlJobMontorDao xxlJobMontorDao;
	
	@Override
	public Map<String, Object> pageList(int start, int length, int jobGroup, String executorHandler, String filterTime) {

		// page list
		List<XxlJobInfo> list = xxlJobInfoDao.pageList(start, length, jobGroup, executorHandler);
		int list_count = xxlJobInfoDao.pageListCount(start, length, jobGroup, executorHandler);
		
		// fill job info
		if (list!=null && list.size()>0) {
			for (XxlJobInfo jobInfo : list) {
				XxlJobDynamicScheduler.fillJobInfo(jobInfo);
			}
		}
		
		// package result
		Map<String, Object> maps = new HashMap<String, Object>();
	    maps.put("recordsTotal", list_count);		// 总记录数
	    maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
	    maps.put("data", list);  					// 分页列表
		return maps;
	}
	
	@Override
	public Map<String, Object> pageMontorList(int start, int length, int jobGroup, String executorHandler, String filterTime) {

		// page list
		List<XxlJobInfo> list = xxlJobInfoDao.pageList(start, length, jobGroup, executorHandler);
		int list_count = xxlJobInfoDao.pageListCount(start, length, jobGroup, executorHandler);
		
		// fill jobMontor
		List<XxlJobMontorInfo> montorList = new ArrayList<XxlJobMontorInfo>();
		if (list!=null && list.size()>0) {
			for (XxlJobInfo jobInfo : list) {				 
				XxlJobMontorInfo montor = xxlJobMontorDao.getMontorData(jobInfo.getJobGroup(), jobInfo.getId());
				if(montor != null) {
					montor.setJobDesc(jobInfo.getJobDesc());
					montorList.add(montor);
				}
			}
		}
		
		// package result
		Map<String, Object> maps = new HashMap<String, Object>();
	    maps.put("recordsTotal", list_count);		// 总记录数
	    maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
	    maps.put("data", montorList);  					// 分页列表
		return maps;
	}

	@Override
	public ReturnT<String> add(XxlJobInfo jobInfo) {
		//新增逻辑，若触发条件为空，则为手工触发任务， 则将cron表达式设置为2099年
		if (StringUtils.isBlank(jobInfo.getJobCron())) {
			jobInfo.setJobCron(NO_SCHEEULER_CRON);
		}
		// valid
		XxlJobGroup group = xxlJobGroupDao.load(jobInfo.getJobGroup());
		if (group == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请选择“执行器”");
		}
		if (!CronExpression.isValidExpression(jobInfo.getJobCron())) {
			
			if (StringUtils.isNotBlank(jobInfo.getJobCron())) {
				String[] childJobKeys = jobInfo.getJobCron().split(",");
				for (String childJobKeyItem: childJobKeys) {
					String[] childJobKeyArr = childJobKeyItem.split("_");
					if (childJobKeyArr.length!=2) {
						return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入格式正确的“Cron”或者前置任务ID");					
					}
					XxlJobInfo childJobInfo = xxlJobInfoDao.loadById(Integer.valueOf(childJobKeyArr[1]));
					if (childJobInfo==null) {
						return new ReturnT<String>(ReturnT.FAIL_CODE, MessageFormat.format("前置任务Key({0})无效", childJobKeyItem));
					}
				}
			}else {
				return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入格式正确的“Cron”或者前置任务ID");					
			}
			
		}
		if (StringUtils.isBlank(jobInfo.getJobDesc())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“任务描述”");
		}
		if (StringUtils.isBlank(jobInfo.getAuthor())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“负责人”");
		}
		if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "路由策略非法");
		}
		if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "阻塞处理策略非法");
		}
		if (ExecutorFailStrategyEnum.match(jobInfo.getExecutorFailStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "失败处理策略非法");
		}
		if (GlueTypeEnum.match(jobInfo.getGlueType()) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "运行模式非法非法");
		}
		if (GlueTypeEnum.BEAN==GlueTypeEnum.match(jobInfo.getGlueType()) && StringUtils.isBlank(jobInfo.getExecutorHandler())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“JobHandler”");
		}

		// fix "\r" in shell
		if (GlueTypeEnum.GLUE_SHELL==GlueTypeEnum.match(jobInfo.getGlueType()) && jobInfo.getGlueSource()!=null) {
			jobInfo.setGlueSource(jobInfo.getGlueSource().replaceAll("\r", ""));
		}

		// childJobKey valid
		if (StringUtils.isNotBlank(jobInfo.getChildJobKey())) {
			String[] childJobKeys = jobInfo.getChildJobKey().split(",");
			for (String childJobKeyItem: childJobKeys) {
				String[] childJobKeyArr = childJobKeyItem.split("_");
				if (childJobKeyArr.length!=2) {
					return new ReturnT<String>(ReturnT.FAIL_CODE, MessageFormat.format("后续任务Key({0})格式错误", childJobKeyItem));
				}
				XxlJobInfo childJobInfo = xxlJobInfoDao.loadById(Integer.valueOf(childJobKeyArr[1]));
				if (childJobInfo==null) {
					return new ReturnT<String>(ReturnT.FAIL_CODE, MessageFormat.format("后续任务Key({0})无效", childJobKeyItem));
				}
			}
		}

		// add in db
		persist(jobInfo);
		
		if (jobInfo.getId() < 1) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "新增任务失败");
		}

		// 若为前置任务驱动， 则不需要定时调度，为了支持单独发起，需要设置一个不会定时发起的cron表达式
		//add in quartz
		String qz_group = String.valueOf(jobInfo.getJobGroup());
        String qz_name = String.valueOf(jobInfo.getId());
		        
	    return addDynamicScheduler(qz_group, qz_name, jobInfo.getJobCron());
	}

	private void persist(XxlJobInfo jobInfo) {
		xxlJobInfoDao.save(jobInfo);
		dealForDepenceJob(jobInfo, false);	
	}

	private ReturnT<String> addDynamicScheduler(String qz_group, String qz_name, String cron) {
		try {
			if(isVaildJobCode(cron)) {	
				XxlJobDynamicScheduler.addJob(qz_name, qz_group, NO_SCHEEULER_CRON);
				//XxlJobDynamicScheduler.pauseJob(qz_name, qz_group);				
			}
			else {
				XxlJobDynamicScheduler.addJob(qz_name, qz_group, cron);
			}
		    
		    return ReturnT.SUCCESS;
		} catch (SchedulerException e) {
		    logger.error(e.getMessage(), e);
		    try {
		        xxlJobInfoDao.delete(Integer.valueOf(qz_name));
		        XxlJobDynamicScheduler.removeJob(qz_name, qz_group);
		    } catch (SchedulerException e1) {
		        logger.error(e.getMessage(), e1);
		    }
		    return new ReturnT<String>(ReturnT.FAIL_CODE, "新增任务失败:" + e.getMessage());
		}
	}

	private boolean isVaildJobCode(String jobCron) {
		Pattern pattern =Pattern.compile("^\\d+_\\d+$");
		Matcher matcher = pattern.matcher(jobCron);
		return matcher.matches();
	}
	
	 

	@Override
	public ReturnT<String> reschedule(XxlJobInfo jobInfo) {
		//新增逻辑，若触发条件为空，则为手工触发任务， 则将cron表达式设置为2099年
		if (StringUtils.isBlank(jobInfo.getJobCron())) {
			jobInfo.setJobCron(NO_SCHEEULER_CRON);
		}
		// valid
		if (!CronExpression.isValidExpression(jobInfo.getJobCron())) {
			if (StringUtils.isNotBlank(jobInfo.getJobCron())) {
				String[] childJobKeys = jobInfo.getJobCron().split(",");
				for (String childJobKeyItem: childJobKeys) {
					String[] childJobKeyArr = childJobKeyItem.split("_");
					if (childJobKeyArr.length!=2) {
						return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入格式正确的“Cron”或者前置任务ID");					
					}
					XxlJobInfo childJobInfo = xxlJobInfoDao.loadById(Integer.valueOf(childJobKeyArr[1]));
					if (childJobInfo==null) {
						return new ReturnT<String>(ReturnT.FAIL_CODE, MessageFormat.format("前置任务Key({0})无效", childJobKeyItem));
					}
				}
			}else {
				return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入格式正确的“Cron”或者前置任务ID");					
			}
		}
		if (StringUtils.isBlank(jobInfo.getJobDesc())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“任务描述”");
		}
		if (StringUtils.isBlank(jobInfo.getAuthor())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“负责人”");
		}
		if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "路由策略非法");
		}
		if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "阻塞处理策略非法");
		}
		if (ExecutorFailStrategyEnum.match(jobInfo.getExecutorFailStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "失败处理策略非法");
		}

		// childJobKey valid
		if (StringUtils.isNotBlank(jobInfo.getChildJobKey())) {
			String[] childJobKeys = jobInfo.getChildJobKey().split(",");
			for (String childJobKeyItem: childJobKeys) {
				String[] childJobKeyArr = childJobKeyItem.split("_");
				if (childJobKeyArr.length!=2) {
					return new ReturnT<String>(ReturnT.FAIL_CODE, MessageFormat.format("后续任务Key({0})格式错误", childJobKeyItem));
				}
                XxlJobInfo childJobInfo = xxlJobInfoDao.loadById(Integer.valueOf(childJobKeyArr[1]));
				if (childJobInfo==null) {
					return new ReturnT<String>(ReturnT.FAIL_CODE, MessageFormat.format("后续任务Key({0})无效", childJobKeyItem));
				}
			}
		}

		// stage job info
		XxlJobInfo exists_jobInfo = xxlJobInfoDao.loadById(jobInfo.getId());
		if (exists_jobInfo == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "参数异常");
		}
		//String old_cron = exists_jobInfo.getJobCron();
		boolean isNeedUpdateDepence = true; 
		if(StringUtils.isEmpty(exists_jobInfo.getJobCron()) && StringUtils.isEmpty(jobInfo.getJobCron())) {
			isNeedUpdateDepence = false;
		}
		else if(exists_jobInfo.getJobCron().trim().equals(jobInfo.getJobCron().trim())) {
			isNeedUpdateDepence = false;
		}
		
		exists_jobInfo.setJobCron(jobInfo.getJobCron());
		exists_jobInfo.setJobDesc(jobInfo.getJobDesc());
		exists_jobInfo.setAuthor(jobInfo.getAuthor());
		exists_jobInfo.setAlarmEmail(jobInfo.getAlarmEmail());
		exists_jobInfo.setAlarmMobileno(jobInfo.getAlarmMobileno());
		exists_jobInfo.setExecutorRouteStrategy(jobInfo.getExecutorRouteStrategy());
		exists_jobInfo.setExecutorHandler(jobInfo.getExecutorHandler());
		exists_jobInfo.setExecutorParam(jobInfo.getExecutorParam());
		exists_jobInfo.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
		exists_jobInfo.setExecutorFailStrategy(jobInfo.getExecutorFailStrategy());
		exists_jobInfo.setChildJobKey(jobInfo.getChildJobKey());
		persistUpdate(exists_jobInfo, isNeedUpdateDepence);
		

		// fresh quartz
		String qz_group = String.valueOf(exists_jobInfo.getJobGroup());
		String qz_name = String.valueOf(exists_jobInfo.getId());
		return reScheduler(qz_group, qz_name, exists_jobInfo.getJobCron());
	}
	
	private void persistUpdate(XxlJobInfo jobInfo, boolean isNeedUpdateDepence) {
		xxlJobInfoDao.update(jobInfo);
		dealForDepenceJob(jobInfo, isNeedUpdateDepence);			
	}
	
	private void dealForDepenceJob(XxlJobInfo jobInfo, boolean isNeedUpdateDepence) {
		if(isNeedUpdateDepence) {
			clearOldDepence(jobInfo);
		}
		addNewDepence(jobInfo);
		
	}

	private void addNewDepence(XxlJobInfo jobInfo) {
		if(isVaildJobCode(jobInfo.getJobCron())) {
			String depenceKey = jobInfo.getJobGroup() + "_" + jobInfo.getId();
			String[] childJobKeyArr = jobInfo.getJobCron().split("_");
			XxlJobInfo dependenceJob = xxlJobInfoDao.loadById(Integer.valueOf(childJobKeyArr[1]));
			String childJobs = dependenceJob.getChildJobKey();
			if(childJobs != null && StringUtils.isNotEmpty(childJobs.trim())) {
				childJobs += "," + depenceKey;
			}
			else {
				childJobs = depenceKey;
			}
			dependenceJob.setChildJobKey(childJobs);
			xxlJobInfoDao.update(dependenceJob);
		}
		
	}

	private void clearOldDepence(XxlJobInfo jobInfo) {
		String depenceKey = jobInfo.getJobGroup() + "_" + jobInfo.getId();
		String likeKey = "%"+depenceKey+"%";
		List<XxlJobInfo> depenceJobs = xxlJobInfoDao.findDepenceJob(likeKey);
		for(XxlJobInfo depenceJob : depenceJobs) {
			Set<String> jobIds = new HashSet<String>();
			
			String[] childJobKeys = depenceJob.getChildJobKey().split(",");		
			for(String jobId : childJobKeys) {
				jobIds.add(jobId.trim());
			}
						
			jobIds.remove(depenceKey);
			
			StringBuilder sb = new StringBuilder();
			for(String newJobId : jobIds) {
				sb.append(newJobId).append(",");
			}
			int index = sb.lastIndexOf(",");
			if(index > 0) {
				depenceJob.setChildJobKey(sb.substring(0, index));				
			}else {
				depenceJob.setChildJobKey("");
			}
			
			xxlJobInfoDao.update(depenceJob);
		}
	}

	private ReturnT<String> reScheduler(String qz_group, String qz_name, String cron) {
		try {
			boolean ret = false;
			if(isVaildJobCode(cron)) {	
				ret = XxlJobDynamicScheduler.rescheduleJob(qz_group, qz_name, NO_SCHEEULER_CRON);
				//XxlJobDynamicScheduler.pauseJob(qz_name, qz_group);				
			}
			else {
				ret = XxlJobDynamicScheduler.rescheduleJob(qz_group, qz_name, cron);
				if(NO_SCHEEULER_CRON.equals(cron)) {
					XxlJobDynamicScheduler.resumeJob(qz_name, qz_group);	
				}
			}
		    
		    return ret?ReturnT.SUCCESS:ReturnT.FAIL;
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
		return ReturnT.FAIL;
	}

	@Override
	public ReturnT<String> remove(int id) {
		XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);
        String group = String.valueOf(xxlJobInfo.getJobGroup());
        String name = String.valueOf(xxlJobInfo.getId());

		try {
			XxlJobDynamicScheduler.removeJob(name, group);
			clearOldDepence(xxlJobInfo);
			xxlJobInfoDao.delete(id);
			xxlJobLogDao.delete(id);
			xxlJobLogGlueDao.deleteByJobId(id);
			return ReturnT.SUCCESS;
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
		}
		return ReturnT.FAIL;
	}

	@Override
	public ReturnT<String> pause(int id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);
        String group = String.valueOf(xxlJobInfo.getJobGroup());
        String name = String.valueOf(xxlJobInfo.getId());

		try {
            boolean ret = XxlJobDynamicScheduler.pauseJob(name, group);	// jobStatus do not store
            return ret?ReturnT.SUCCESS:ReturnT.FAIL;
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
			return ReturnT.FAIL;
		}
	}

	@Override
	public ReturnT<String> resume(int id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);
        String group = String.valueOf(xxlJobInfo.getJobGroup());
        String name = String.valueOf(xxlJobInfo.getId());

		try {
			boolean ret = XxlJobDynamicScheduler.resumeJob(name, group);
			return ret?ReturnT.SUCCESS:ReturnT.FAIL;
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
			return ReturnT.FAIL;
		}
	}

	@Override
	public ReturnT<String> triggerJob(int id) {
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(id);
        if (xxlJobInfo == null) {
        	return new ReturnT<String>(ReturnT.FAIL_CODE, "任务ID非法");
		}

        String group = String.valueOf(xxlJobInfo.getJobGroup());
        String name = String.valueOf(xxlJobInfo.getId());

		try {
			XxlJobDynamicScheduler.triggerJob(name, group);
			return ReturnT.SUCCESS;
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
			return new ReturnT<String>(ReturnT.FAIL_CODE, e.getMessage());
		}
	}

	@Override
	public Map<String, Object> dashboardInfo() {

		int jobInfoCount = xxlJobInfoDao.findAllCount();
		int jobLogCount = xxlJobLogDao.triggerCountByHandleCode(-1);
		int jobLogSuccessCount = xxlJobLogDao.triggerCountByHandleCode(ReturnT.SUCCESS_CODE);
		int jobRunningCount = xxlJobLogDao.findRunningJobCount();

		// executor count
		Set<String> executerAddressSet = new HashSet<String>();
		List<XxlJobGroup> groupList = xxlJobGroupDao.findAll();

		if (CollectionUtils.isNotEmpty(groupList)) {
			for (XxlJobGroup group: groupList) {
				if (CollectionUtils.isNotEmpty(group.getRegistryList())) {
					executerAddressSet.addAll(group.getRegistryList());
				}
			}
		}

		int executorCount = executerAddressSet.size();

		Map<String, Object> dashboardMap = new HashMap<String, Object>();
		dashboardMap.put("jobRunningCount", jobRunningCount);
		dashboardMap.put("jobInfoCount", jobInfoCount);
		dashboardMap.put("jobLogCount", jobLogCount);
		dashboardMap.put("jobLogSuccessCount", jobLogSuccessCount);
		dashboardMap.put("executorCount", executorCount);
		return dashboardMap;
	}

	@Override
	public ReturnT<Map<String, Object>> triggerChartDate() {
		Date from = DateUtils.addDays(new Date(), -30);
		Date to = new Date();

		List<String> triggerDayList = new ArrayList<String>();
		List<Integer> triggerDayCountSucList = new ArrayList<Integer>();
		List<Integer> triggerDayCountFailList = new ArrayList<Integer>();
		int triggerCountSucTotal = 0;
		int triggerCountFailTotal = 0;

		List<Map<String, Object>> triggerCountMapAll = xxlJobLogDao.triggerCountByDay(from, to, -1);
		List<Map<String, Object>> triggerCountMapSuc = xxlJobLogDao.triggerCountByDay(from, to, ReturnT.SUCCESS_CODE);
		if (CollectionUtils.isNotEmpty(triggerCountMapAll)) {
			for (Map<String, Object> item: triggerCountMapAll) {
				String day = String.valueOf(item.get("triggerDay"));
				int dayAllCount = Integer.valueOf(String.valueOf(item.get("triggerCount")));
				int daySucCount = 0;
				int dayFailCount = dayAllCount - daySucCount;

				if (CollectionUtils.isNotEmpty(triggerCountMapSuc)) {
					for (Map<String, Object> sucItem: triggerCountMapSuc) {
						String daySuc = String.valueOf(sucItem.get("triggerDay"));
						if (day.equals(daySuc)) {
							daySucCount = Integer.valueOf(String.valueOf(sucItem.get("triggerCount")));
							dayFailCount = dayAllCount - daySucCount;
						}
					}
				}

				triggerDayList.add(day);
				triggerDayCountSucList.add(daySucCount);
				triggerDayCountFailList.add(dayFailCount);
				triggerCountSucTotal += daySucCount;
				triggerCountFailTotal += dayFailCount;
			}
		} else {
            for (int i = 4; i > -1; i--) {
                triggerDayList.add(FastDateFormat.getInstance("yyyy-MM-dd").format(DateUtils.addDays(new Date(), -i)));
                triggerDayCountSucList.add(0);
                triggerDayCountFailList.add(0);
            }
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("triggerDayList", triggerDayList);
		result.put("triggerDayCountSucList", triggerDayCountSucList);
		result.put("triggerDayCountFailList", triggerDayCountFailList);
		result.put("triggerCountSucTotal", triggerCountSucTotal);
		result.put("triggerCountFailTotal", triggerCountFailTotal);
		return new ReturnT<Map<String, Object>>(result);
	}

}
