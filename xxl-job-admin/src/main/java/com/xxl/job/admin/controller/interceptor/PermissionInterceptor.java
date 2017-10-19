package com.xxl.job.admin.controller.interceptor;

import java.math.BigInteger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.xxl.job.admin.controller.annotation.PermessionLimit;
import com.xxl.job.admin.core.model.UserInfo;
import com.xxl.job.admin.core.util.CookieUtil;
import com.xxl.job.admin.core.util.PropertiesUtil;
import com.xxl.job.admin.dao.UserInfoDao;

/**
 * 权限拦截, 简易版
 * @author xuxueli 2015-12-12 18:09:04
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {
	
	@Resource
	private UserInfoDao userInfoDao;

	
	public static final String LOGIN_USER_NAME = "USER_NAME_KEY";
	public static final String LOGIN_IDENTITY_KEY = "LOGIN_IDENTITY";
	public static final String LOGIN_IDENTITY_TOKEN;
    static {
        String username = PropertiesUtil.getString("xxl.job.login.username");
        String password = PropertiesUtil.getString("xxl.job.login.password");
        String temp = username + "_" + password;
        LOGIN_IDENTITY_TOKEN = new BigInteger(1, temp.getBytes()).toString(16);
    }
	
	public static boolean login(HttpServletResponse response, boolean ifRemember){
		CookieUtil.set(response, LOGIN_IDENTITY_KEY, LOGIN_IDENTITY_TOKEN, ifRemember);
		return true;
	}
	
	public static boolean login(HttpServletResponse response, boolean ifRemember, String userName, String token){
		CookieUtil.set(response, LOGIN_USER_NAME, userName, ifRemember);
		CookieUtil.set(response, LOGIN_IDENTITY_KEY, token, ifRemember);
		return true;
	}
	public static void logout(HttpServletRequest request, HttpServletResponse response){
		CookieUtil.remove(request, response, LOGIN_USER_NAME);
		CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
	}
	
	public boolean ifLogin(HttpServletRequest request){
		String userName = CookieUtil.getValue(request, LOGIN_USER_NAME);
		String indentityInfo = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(indentityInfo)) {
			return false;
		}
		UserInfo userInfo = userInfoDao.getUserInfoByName(userName);
		if(userInfo != null && indentityInfo.equals(userInfo.getPassword())) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}
		
		if (!ifLogin(request)) {
			HandlerMethod method = (HandlerMethod)handler;
			PermessionLimit permission = method.getMethodAnnotation(PermessionLimit.class);
			if (permission == null || permission.limit()) {
				response.sendRedirect(request.getContextPath() + "/toLogin");
				//request.getRequestDispatcher("/toLogin").forward(request, response);
				return false;
			}
		}
		
		return super.preHandle(request, response, handler);
	}
	
}
