package com.xxl.job.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.xxl.job.admin.core.model.RoleInfo;
import com.xxl.job.admin.core.model.XxlJobGroup;

public interface RoleInfoDao {

    public RoleInfo getRoleInfoByPrimkey(@Param("roleId") int roleId );

    public RoleInfo getRoleInfoByUK(@Param("roleName") String roleName );
    
    public List<RoleInfo> getRoleInfoByUserId(@Param("userId") int userId );

    public int insertRoleInfo(RoleInfo roleInfo);

    public int updateRoleInfo(RoleInfo roleInfo);

	public List<RoleInfo> findAllRoleInfo();

}