package com.xxl.job.admin.core.model;

/**
 * xxl-job montor
 * @author wjy
 */
public class XxlJobMontorInfo {
	
	private int id;				// 主键ID	    (JobKey.name)
	
	private int jobGroup;		// 执行器主键ID	(JobKey.group)
	private String jobDesc;
	

	private int dailyFailCnt;	    // 日失败次数
	private int dailySuccessCnt;	// 日成功次数
	private int dailyAvgCallTime;	// 日平均调用时间（MS）
	private int dailyMaxCallTime;	// 日最长调用时间（MS）
	
	private int monthFailCnt;	    // 日失败次数
	private int monthSuccessCnt;	// 日成功次数
	private int monthAvgCallTime;	// 日平均调用时间（MS）
	private int monthMaxCallTime;	// 日最长调用时间（MS）
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(int jobGroup) {
		this.jobGroup = jobGroup;
	}
	public String getJobDesc() {
		return jobDesc;
	}
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
	public int getDailyFailCnt() {
		return dailyFailCnt;
	}
	public void setDailyFailCnt(int dailyFailCnt) {
		this.dailyFailCnt = dailyFailCnt;
	}
	public int getDailySuccessCnt() {
		return dailySuccessCnt;
	}
	public void setDailySuccessCnt(int dailySuccessCnt) {
		this.dailySuccessCnt = dailySuccessCnt;
	}
	public int getDailyAvgCallTime() {
		return dailyAvgCallTime;
	}
	public void setDailyAvgCallTime(int dailyAvgCallTime) {
		this.dailyAvgCallTime = dailyAvgCallTime;
	}
	public int getDailyMaxCallTime() {
		return dailyMaxCallTime;
	}
	public void setDailyMaxCallTime(int dailyMaxCallTime) {
		this.dailyMaxCallTime = dailyMaxCallTime;
	}
	public int getMonthFailCnt() {
		return monthFailCnt;
	}
	public void setMonthFailCnt(int monthFailCnt) {
		this.monthFailCnt = monthFailCnt;
	}
	public int getMonthSuccessCnt() {
		return monthSuccessCnt;
	}
	public void setMonthSuccessCnt(int monthSuccessCnt) {
		this.monthSuccessCnt = monthSuccessCnt;
	}
	public int getMonthAvgCallTime() {
		return monthAvgCallTime;
	}
	public void setMonthAvgCallTime(int monthAvgCallTime) {
		this.monthAvgCallTime = monthAvgCallTime;
	}
	public int getMonthMaxCallTime() {
		return monthMaxCallTime;
	}
	public void setMonthMaxCallTime(int monthMaxCallTime) {
		this.monthMaxCallTime = monthMaxCallTime;
	}
	@Override
	public String toString() {
		return "XxlJobMontorInfo [id=" + id + ", jobGroup=" + jobGroup + ", jobDesc=" + jobDesc + ", dailyFailCnt="
				+ dailyFailCnt + ", dailySuccessCnt=" + dailySuccessCnt + ", dailyAvgCallTime=" + dailyAvgCallTime
				+ ", dailyMaxCallTime=" + dailyMaxCallTime + ", monthFailCnt=" + monthFailCnt + ", monthSuccessCnt="
				+ monthSuccessCnt + ", monthAvgCallTime=" + monthAvgCallTime + ", monthMaxCallTime=" + monthMaxCallTime
				+ "]";
	}
	


	

}
