package com.xxl.job.admin.core.model;

import java.util.Date;

public class UserRoleMapping {

    /**自增长主键**/ 
    private int userRoleId;

    /**用户ID**/ 
    private int userId;

    /**角色ID**/ 
    private int roleId;

    /**状态 Y:有效 C:无效**/ 
    private String status;

    /**插入时间**/ 
    private Date insertimestamp;

    /**更新时间**/ 
    private Date updatetimestamp;

    public int getUserRoleId(){
        return userRoleId;
    }

    public void setUserRoleId(int userRoleId){
        this.userRoleId = userRoleId;
    }

    public int getUserId(){
        return userId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public int getRoleId(){
        return roleId;
    }

    public void setRoleId(int roleId){
        this.roleId = roleId;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public Date getInsertimestamp(){
        return insertimestamp;
    }

    public void setInsertimestamp(Date insertimestamp){
        this.insertimestamp = insertimestamp;
    }

    public Date getUpdatetimestamp(){
        return updatetimestamp;
    }

    public void setUpdatetimestamp(Date updatetimestamp){
        this.updatetimestamp = updatetimestamp;
    }

    @Override                                                     
    public String toString() {                                    
        return "UserRoleMapping{" +                         
                "userRoleId='" + userRoleId + "'," +                
                "userId='" + userId + "'," +                
                "roleId='" + roleId + "'," +                
                "status='" + status + "'," +                
                "insertimestamp='" + insertimestamp + "'," +                
                "updatetimestamp='" + updatetimestamp + "'," +                
                "} " + super.toString();                        
    }                                                             
}