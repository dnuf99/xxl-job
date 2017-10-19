package com.xxl.job.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.xxl.job.admin.core.model.UserInfo;
import com.xxl.job.admin.dao.UserInfoDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:test-applicationContext.xml")                     
@TransactionConfiguration(defaultRollback = true)                                                   
public class UserInfoMapperTest {

	@Autowired                                                                                         
	private UserInfoDao userInfoMapper;           
                                                                                                    
	@Test                                                                                              
	@Transactional                                                                                              
	public void baseTest() {                                                                    
		UserInfo userInfo = new UserInfo();                                      
		                                                                                                                                                         
		userInfo.setUsername("a");                                                          
		userInfo.setPassword("a");                                                          
		userInfo.setFullname("a");                                                          
		userInfo.setMobile("a");                                                          
		userInfo.setEmail("a");                                                          
		userInfo.setStatus("Y");                                                          
		userInfo.setLastLoginIp("a");                                                          
		userInfoMapper.insertUserInfo(userInfo);	                               
		                                                                                                 
		userInfo = userInfoMapper.getUserInfoByName("a");  		System.out.println(userInfo);                                                
		                                                                                                 
		userInfo.setPassword("b");                                                          
		userInfoMapper.updateUserInfo(userInfo);                                 
		userInfo = userInfoMapper.getUserInfoByName("a");                  
		System.out.println(userInfo);                                                
		                                                                                                 
		                                                                                                 
		                                                                                                 
	}                                                                                                  
	                                                                                                   
	                                                                                                   
}                                                                                                   
