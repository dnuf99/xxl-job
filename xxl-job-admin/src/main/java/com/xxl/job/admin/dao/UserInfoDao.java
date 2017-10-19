package com.xxl.job.admin.dao;

import org.apache.ibatis.annotations.Param;

import com.xxl.job.admin.core.model.UserInfo;

public interface UserInfoDao {

    public UserInfo getUserInfoByPrimkey(@Param("userId") int userId );

    public UserInfo getUserInfoByName(@Param("username") String username );

    public int insertUserInfo(UserInfo userInfo);

    public int updateUserInfo(UserInfo userInfo);

}