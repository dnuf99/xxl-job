package com.xxl.job.admin.dao;

import org.apache.ibatis.annotations.Param;

import com.xxl.job.admin.core.model.XxlJobMontorInfo;

/**
 * job log
 * @author xuxueli 2016-1-12 18:03:06
 */
public interface XxlJobMontorDao {

	public XxlJobMontorInfo getMontorData(@Param("jobGroup") int jobGroup, @Param("jobId") int jobId);


}
