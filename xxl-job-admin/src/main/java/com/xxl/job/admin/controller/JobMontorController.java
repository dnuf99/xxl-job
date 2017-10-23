package com.xxl.job.admin.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.job.admin.controller.interceptor.PermissionInterceptor;
import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.util.CookieUtil;
import com.xxl.job.admin.dao.XxlJobGroupDao;
import com.xxl.job.admin.service.UserRoleService;
import com.xxl.job.admin.service.XxlJobService;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/jobmontor")
public class JobMontorController {

	@Resource
	private XxlJobGroupDao xxlJobGroupDao;
	@Resource
	private XxlJobService xxlJobService;
	
	@Resource
	private UserRoleService userRoleService;
	
	@RequestMapping
	public String index(HttpServletRequest request, Model model, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {

		String userName = CookieUtil.getValue(request, PermissionInterceptor.LOGIN_USER_NAME);
		model.addAttribute("userName", userName);
		boolean isEditable = userRoleService.isUserAsAdmin(userName);
		model.addAttribute("editable", isEditable);
		// 任务组
		List<XxlJobGroup> jobGroupList =  xxlJobGroupDao.findAll();
		model.addAttribute("JobGroupList", jobGroupList);
		model.addAttribute("jobGroup", jobGroup);

		return "jobmontor/jobmontor.index";
	}
	
	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,  
			@RequestParam(required = false, defaultValue = "10") int length,
			@RequestParam(required = false, defaultValue = "-1")int jobGroup, String executorHandler, String filterTime) {
		
		return xxlJobService.pageMontorList(start, length, jobGroup, executorHandler, filterTime);
	}
	
	
	
}
