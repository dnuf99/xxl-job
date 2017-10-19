package com.xxl.job.admin.core.model;

import java.util.Date;

public class UserInfo {

    /**自增长主键**/ 
    private int userId;

    /**用户名**/ 
    private String username;

    /**密码**/ 
    private String password;

    /**真实用户名**/ 
    private String fullname;

    /**手机号**/ 
    private String mobile;

    /**邮箱**/ 
    private String email;

    /**状态 Y:有效 C:无效**/ 
    private String status;

    /**最近一次登录IP**/ 
    private String lastLoginIp;

    /**最近一次登录时间**/ 
    private Date lastLoginTime;

    public int getUserId(){
        return userId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getFullname(){
        return fullname;
    }

    public void setFullname(String fullname){
        this.fullname = fullname;
    }

    public String getMobile(){
        return mobile;
    }

    public void setMobile(String mobile){
        this.mobile = mobile;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getLastLoginIp(){
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp){
        this.lastLoginIp = lastLoginIp;
    }

    public Date getLastLoginTime(){
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime){
        this.lastLoginTime = lastLoginTime;
    }

    @Override                                                     
    public String toString() {                                    
        return "UserInfo{" +                         
                "userId='" + userId + "'," +                
                "username='" + username + "'," +                
                "password='" + password + "'," +                
                "fullname='" + fullname + "'," +                
                "mobile='" + mobile + "'," +                
                "email='" + email + "'," +                
                "status='" + status + "'," +                
                "lastLoginIp='" + lastLoginIp + "'," +                
                "lastLoginTime='" + lastLoginTime + "'," +                
                "} " + super.toString();                        
    }                                                             
}