package com.xxl.job.admin.dao;

import org.apache.ibatis.annotations.Param;

import com.xxl.job.admin.core.model.UserRoleMapping;

public interface UserRoleMappingDao {

    public UserRoleMapping getUserRoleMappingByPrimkey(@Param("userRoleId") int userRoleId );

    public UserRoleMapping getUserRoleMappingByUK(@Param("userId") int userId, @Param("roleId") int roleId);

    public int insertUserRoleMapping(UserRoleMapping userRoleMapping);

    public int updateUserRoleMapping(UserRoleMapping userRoleMapping);

}