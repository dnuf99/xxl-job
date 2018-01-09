package com.xxl.job.admin.service;


import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.core.biz.model.ReturnT;

import java.util.Date;
import java.util.Map;

/**
 * core job action for xxl-job
 * 
 * @author xuxueli 2016-5-28 15:30:33
 */
public interface XxlJobService {
	
	public final static String NO_SCHEEULER_CRON = "0 15 10 15 * ? 2099";
	
	public Map<String, Object> pageList(int start, int length, int jobGroup, String jobDesc, String executorHandler, String filterTime);
	
	public ReturnT<String> add(XxlJobInfo jobInfo);
	
	public ReturnT<String> reschedule(XxlJobInfo jobInfo);
	
	public ReturnT<String> remove(int id);
	
	public ReturnT<String> pause(int id);
	
	public ReturnT<String> resume(int id);
	
	public ReturnT<String> triggerJob(int id);

	public Map<String,Object> dashboardInfo();

	public ReturnT<Map<String,Object>> triggerChartDate(Date startDate, Date endDate);
	
	public Map<String, Object> pageMontorList(int start, int length, int jobGroup, String executorHandler, String filterTime);


}
