package com.xxl.job.admin.service;


import com.xxl.job.admin.core.model.UserInfo;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.core.biz.model.ReturnT;

import java.util.Map;

/**
 * core job action for xxl-job
 * 
 * @author xuxueli 2016-5-28 15:30:33
 */
public interface UserRoleService {
	
	public static final String STAUTS_VAILD = "Y";
	
	public boolean isUserAsAdmin(String userName);
	
	public ReturnT<String> AddUserInfo(UserInfo userInfo, String roleName);

}
