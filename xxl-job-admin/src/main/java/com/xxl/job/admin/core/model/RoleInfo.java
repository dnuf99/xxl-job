package com.xxl.job.admin.core.model;

public class RoleInfo {
	public static final String EDITABLE_ROLE = "admin";

    /**自增长主键**/ 
    private int roleId;

    /**角色名称**/ 
    private String roleName;

    /**角色描述**/ 
    private String roleDesc;

    /**状态 Y:有效 C:无效**/ 
    private String status;

    public int getRoleId(){
        return roleId;
    }

    public void setRoleId(int roleId){
        this.roleId = roleId;
    }

    public String getRoleName(){
        return roleName;
    }

    public void setRoleName(String roleName){
        this.roleName = roleName;
    }

    public String getRoleDesc(){
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc){
        this.roleDesc = roleDesc;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    @Override                                                     
    public String toString() {                                    
        return "RoleInfo{" +                         
                "roleId='" + roleId + "'," +                
                "roleName='" + roleName + "'," +                
                "roleDesc='" + roleDesc + "'," +                
                "status='" + status + "'," +                
                "} " + super.toString();                        
    }                                                             
}