package com.xxl.job.dao.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.xxl.job.admin.core.model.UserRoleMapping;
import com.xxl.job.admin.dao.UserRoleMappingDao;

@RunWith(SpringJUnit4ClassRunner.class)                                                             
@ContextConfiguration(locations = "classpath*:test-applicationContext.xml")                            
@TransactionConfiguration(defaultRollback = true)                                                   
public class UserRoleMappingMapperTest {

	@Autowired                                                                                         
	private UserRoleMappingDao userRoleMappingMapper;           
                                                                                                    
	@Test                                                                                              
	@Transactional                                                                                              
	public void baseTest() {                                                                    
		UserRoleMapping userRoleMapping = new UserRoleMapping();                                      
		                                                                                                 
		userRoleMapping.setUserRoleId(1);                                                           
		userRoleMapping.setUserId(1);                                                           
		userRoleMapping.setRoleId(1);                                                           
		userRoleMapping.setStatus("a");                                                          
		userRoleMappingMapper.insertUserRoleMapping(userRoleMapping);	                               
		                                                                                                 
		userRoleMapping = userRoleMappingMapper.getUserRoleMappingByUK(1, 1);                 
		System.out.println(userRoleMapping);                                                
		                                                                                                 
		userRoleMapping.setStatus("b");                                                          
		userRoleMappingMapper.updateUserRoleMapping(userRoleMapping);                                 
		userRoleMapping = userRoleMappingMapper.getUserRoleMappingByUK(1, 1);                  
		System.out.println(userRoleMapping);                                                
		                                                                                                 
		                                                                                                 
		                                                                                                 
	}                                                                                                  
	                                                                                                   
	                                                                                                   
}                                                                                                   
