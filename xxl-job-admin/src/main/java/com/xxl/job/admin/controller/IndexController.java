package com.xxl.job.admin.controller;

import com.xxl.job.admin.controller.annotation.PermessionLimit;
import com.xxl.job.admin.controller.interceptor.PermissionInterceptor;
import com.xxl.job.admin.core.model.RoleInfo;
import com.xxl.job.admin.core.model.UserInfo;
import com.xxl.job.admin.core.util.CookieUtil;
import com.xxl.job.admin.dao.RoleInfoDao;
import com.xxl.job.admin.dao.UserInfoDao;
import com.xxl.job.admin.service.UserRoleService;
import com.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
public class IndexController {

	private static final String DEFAULT_PASSWORD = "abc=123";

	@Resource
	private XxlJobService xxlJobService;
	
	@Resource
	private UserInfoDao userInfoDao;
	
	@Resource
	private RoleInfoDao roleInfoDao;
		
	@Resource
	private UserRoleService userRoleService;
	
	

	@RequestMapping("/")
	public String index(HttpServletRequest request, Model model) {
		String userName = CookieUtil.getValue(request, PermissionInterceptor.LOGIN_USER_NAME);
		model.addAttribute("userName", userName);
		boolean isEditable = userRoleService.isUserAsAdmin(userName);
		model.addAttribute("editable", isEditable);
		
		Map<String, Object> dashboardMap = xxlJobService.dashboardInfo();
		model.addAllAttributes(dashboardMap);
		
		return "index";
	}

    @RequestMapping("/triggerChartDate")
	@ResponseBody
	public ReturnT<Map<String, Object>> triggerChartDate(Date startDate, Date endDate) {
        ReturnT<Map<String, Object>> triggerChartDate = xxlJobService.triggerChartDate(startDate, endDate);
        return triggerChartDate;
    }
	
	@RequestMapping("/toLogin")
	@PermessionLimit(limit=false)
	public String toLogin(Model model, HttpServletRequest request) {
		if (isLogin(request)) {
			return "redirect:/";
		}
		return "login";
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(limit=false)
	public ReturnT<String> loginDo(HttpServletRequest request, HttpServletResponse response, String userName, String password, String ifRemember){
		if (!isLogin(request)) {
			if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)) {
				UserInfo userInfo = userInfoDao.getUserInfoByName(userName);
				if(userInfo != null) {
					String key = password + "_" + userName;
					String token = DigestUtils.md5Hex(key);
					if(StringUtils.isNotEmpty(token) && token.equals(userInfo.getPassword())) {
						boolean ifRem = false;
						if (StringUtils.isNotBlank(ifRemember) && "on".equals(ifRemember)) {
							ifRem = true;
						}
						PermissionInterceptor.login(response, ifRem, userName, token);
						return ReturnT.SUCCESS;
					}
				}
				
			} 						
		}else {
			return ReturnT.SUCCESS;
		}
		
		return new ReturnT<String>(500, "账号或密码错误");
		
	}
	
	@RequestMapping(value="logout", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(limit=false)
	public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response){
		if (isLogin(request)) {
			PermissionInterceptor.logout(request, response);
		}
		return ReturnT.SUCCESS;
	}
	
	private boolean isLogin(HttpServletRequest request) {
		String userName = CookieUtil.getValue(request, PermissionInterceptor.LOGIN_USER_NAME);
		String indentityInfo = CookieUtil.getValue(request, PermissionInterceptor.LOGIN_IDENTITY_KEY);
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(indentityInfo)) {
			return false;
		}
		UserInfo userInfo = userInfoDao.getUserInfoByName(userName);
		if(userInfo != null && indentityInfo.equals(userInfo.getPassword())) {
			return true;
		}
		return false;
	}

	@RequestMapping("/pwdManger")
	public String pwdManger(HttpServletRequest request, Model model) {

		String userName = CookieUtil.getValue(request, PermissionInterceptor.LOGIN_USER_NAME);
		model.addAttribute("userName", userName);
		boolean isEditable = userRoleService.isUserAsAdmin(userName);
		model.addAttribute("editable", isEditable);
		
		return "pwdManger";
	}
	

	@RequestMapping(value="updatePwd", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(limit=false)
	public ReturnT<String> updatePwd(HttpServletRequest request, HttpServletResponse response, String oldPassword, String newPassword) {
		
		String errorMsg = "修改密码失败！";
		if (isLogin(request)) {
			if (StringUtils.isNotBlank(oldPassword) && StringUtils.isNotBlank(newPassword)) {
				String userName = CookieUtil.getValue(request, PermissionInterceptor.LOGIN_USER_NAME);
				UserInfo userInfo = userInfoDao.getUserInfoByName(userName);
				if(userInfo != null) {
					String key = oldPassword + "_" + userName;
					String token = DigestUtils.md5Hex(key);
					if(StringUtils.isNotEmpty(token) && token.equals(userInfo.getPassword())) {
						if(StringUtils.isNotEmpty(newPassword)) {
							String newKey = newPassword + "_" + userName;
							String newToken = DigestUtils.md5Hex(newKey);
							userInfo.setPassword(newToken);
							userInfoDao.updateUserInfo(userInfo);
							PermissionInterceptor.logout(request, response);
							return ReturnT.SUCCESS;
						}
						else {
							errorMsg = "新密码设置失败，新密码为空";
						}
					}
					else {
						errorMsg = "新密码设置失败，旧密码错误";
					}
				}
				
			} 						
		}else {
			errorMsg = "账号未登录，无法修改密码";
		}
		
		return new ReturnT<String>(500, errorMsg);

	}
	
	
	@RequestMapping("/userManger")
	public String userManger(HttpServletRequest request, Model model) {
		String userName = CookieUtil.getValue(request, PermissionInterceptor.LOGIN_USER_NAME);
		model.addAttribute("userName", userName);
		boolean isEditable = userRoleService.isUserAsAdmin(userName);
		// 执行器列表
		List<RoleInfo> roleList =  roleInfoDao.findAllRoleInfo();
		model.addAttribute("roleList", roleList);
		model.addAttribute("editable", isEditable);
		return "userManger";
	}
	
	private boolean isEditable(HttpServletRequest request) {
		String userName = CookieUtil.getValue(request, PermissionInterceptor.LOGIN_USER_NAME);
		return userRoleService.isUserAsAdmin(userName);
	}
	
	@RequestMapping(value="userAdd", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(limit=false)
	public ReturnT<String> userAdd(HttpServletRequest request, HttpServletResponse response, String userName, String roleName, String fullname, String mobileno) {
		if(!isEditable(request)) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "没有操作权限");
		}
		
		String errorMsg = "新增用户失败！";
		if (isLogin(request)) {
			if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(roleName)) {
				UserInfo userInfo = new UserInfo();	
				userInfo.setUsername(userName);
				String key = DEFAULT_PASSWORD + "_" + userName;
				String token = DigestUtils.md5Hex(key);
				userInfo.setPassword(token);
				userInfo.setFullname(fullname);
				userInfo.setMobile(mobileno);
				userInfo.setStatus(UserRoleService.STAUTS_VAILD);
				
				return userRoleService.AddUserInfo(userInfo, roleName);
				
			} 						
		}else {
			errorMsg = "账号未登录，无法新增用户";
		}
		
		return new ReturnT<String>(500, errorMsg);

	}
	
	
	
	@RequestMapping("/resetPwdManger")
	public String resetPwdManger(HttpServletRequest request, Model model) {
		String userName = CookieUtil.getValue(request, PermissionInterceptor.LOGIN_USER_NAME);
		model.addAttribute("userName", userName);
		boolean isEditable = userRoleService.isUserAsAdmin(userName);
		model.addAttribute("editable", isEditable);
		
		return "resetPwdManger";
	}
	
	
	@RequestMapping(value="resetPwdAction", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(limit=false)
	public ReturnT<String> resetPwdAction(HttpServletRequest request, HttpServletResponse response, String userName) {
		if(!isEditable(request)) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "没有操作权限");
		}
		
		String errorMsg = "重置密码失败！";
		if (isLogin(request)) {
			if (StringUtils.isNotBlank(userName)) {
				UserInfo userInfo = userInfoDao.getUserInfoByName(userName);
				if(userInfo != null) {
					userInfo.setUsername(userName);
					String key = DEFAULT_PASSWORD + "_" + userName;
					String token = DigestUtils.md5Hex(key);
					userInfo.setPassword(token);
					userInfoDao.updateUserInfo(userInfo);	
					return ReturnT.SUCCESS;
				}
				else {
					errorMsg = "输入的客户名未不存在";
				}
			} 						
		}else {
			errorMsg = "账号未登录，无法重置密码";
		}
		
		return new ReturnT<String>(500, errorMsg);

	}
	
}
