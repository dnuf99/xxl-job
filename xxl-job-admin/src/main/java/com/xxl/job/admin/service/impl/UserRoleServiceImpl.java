package com.xxl.job.admin.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xxl.job.admin.core.model.RoleInfo;
import com.xxl.job.admin.core.model.UserInfo;
import com.xxl.job.admin.core.model.UserRoleMapping;
import com.xxl.job.admin.dao.RoleInfoDao;
import com.xxl.job.admin.dao.UserInfoDao;
import com.xxl.job.admin.dao.UserRoleMappingDao;
import com.xxl.job.admin.service.UserRoleService;
import com.xxl.job.core.biz.model.ReturnT;


@Service
public class UserRoleServiceImpl implements UserRoleService {
	
	@Resource
	private UserInfoDao userInfoDao;
	
	@Resource
	private RoleInfoDao roleInfoDao;
	
	@Resource
	private UserRoleMappingDao userRoleMappingDao;

	@Override
	public boolean isUserAsAdmin(String userName) {
		UserInfo userInfo = userInfoDao.getUserInfoByName(userName);
		List<RoleInfo> roles = roleInfoDao.getRoleInfoByUserId(userInfo.getUserId());
		for(RoleInfo role : roles) {
			if(RoleInfo.EDITABLE_ROLE.equals(role.getRoleName())) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	@Transactional
	public ReturnT<String> AddUserInfo(UserInfo userInfo, String roleName) {
		userInfoDao.insertUserInfo(userInfo);
		
		userInfo = userInfoDao.getUserInfoByName(userInfo.getUsername());
		RoleInfo roleInfo = roleInfoDao.getRoleInfoByUK(roleName);
		
		if(userInfo != null && roleInfo !=null) {
			UserRoleMapping userRoleMapping = new UserRoleMapping();
			userRoleMapping.setUserId(userInfo.getUserId());
			userRoleMapping.setRoleId(roleInfo.getRoleId());
			userRoleMapping.setStatus(STAUTS_VAILD);
			userRoleMapping.setInsertimestamp(new Date());
			userRoleMappingDao.insertUserRoleMapping(userRoleMapping);
			return ReturnT.SUCCESS;
		}else {
			String errorMsg = "新增用户信息或者角色信息错误";
			return  new ReturnT<String>(500, errorMsg);
		}
	}
	
}
