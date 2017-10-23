package com.xxl.job.dao.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.xxl.job.admin.core.model.RoleInfo;
import com.xxl.job.admin.dao.RoleInfoDao;

@RunWith(SpringJUnit4ClassRunner.class)                                                             
@ContextConfiguration(locations = "classpath*:test-applicationContext.xml")                            
@TransactionConfiguration(defaultRollback = true)                                                   
public class RoleInfoMapperTest {

	@Autowired                                                                                         
	private RoleInfoDao roleInfoMapper;           
                                                                                                    
	@Test                                                                                              
	@Transactional                                                                                              
	public void baseTest() {                                                                    
		RoleInfo roleInfo = new RoleInfo();                                      
		                                                                                                 
		roleInfo.setRoleId(1);                                                           
		roleInfo.setRoleName("a");                                                          
		roleInfo.setRoleDesc("a");                                                          
		roleInfo.setStatus("a");                                                          
		roleInfoMapper.insertRoleInfo(roleInfo);	                               
		                                                                                                 
		roleInfo = roleInfoMapper.getRoleInfoByUK("a");                 
		System.out.println(roleInfo);                                                
		                                                                                                 
		roleInfo.setRoleDesc("b");                                                          
		roleInfoMapper.updateRoleInfo(roleInfo);                                 
		roleInfo = roleInfoMapper.getRoleInfoByUK("a");                  
		System.out.println(roleInfo);                                                
		                                                                                                 
		List<RoleInfo> roles = roleInfoMapper.getRoleInfoByUserId(7);  
		System.out.println(roles.get(0));
		                                                                                                 
	}                                                                                                  
	                                                                                                   
	                                                                                                   
}                                                                                                   
