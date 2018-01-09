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
import com.xxl.job.admin.core.enums.ExecutorFailStrategyEnum;
import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.xxl.job.admin.core.util.CookieUtil;
import com.xxl.job.admin.dao.XxlJobGroupDao;
import com.xxl.job.admin.service.UserRoleService;
import com.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;

/**
 * index controller
 * @author xuxueli 2015-12-19 16:13:16
 */
@Controller
@RequestMapping("/jobinfo")
public class JobInfoController {


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
		boolean isEditable = isEditable(request);

		// 枚举-字典
		model.addAttribute("ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values());	// 路由策略-列表
		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());								// Glue类型-字典
		model.addAttribute("ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values());	// 阻塞处理策略-字典
		model.addAttribute("ExecutorFailStrategyEnum", ExecutorFailStrategyEnum.values());		// 失败处理策略-字典

		// 任务组
		List<XxlJobGroup> jobGroupList =  xxlJobGroupDao.findAll();
		model.addAttribute("JobGroupList", jobGroupList);
		model.addAttribute("jobGroup", jobGroup);
		
		model.addAttribute("editable", isEditable);

		return "jobinfo/jobinfo.index";
	}
	
	private boolean isEditable(HttpServletRequest request) {
		String userName = CookieUtil.getValue(request, PermissionInterceptor.LOGIN_USER_NAME);
		return userRoleService.isUserAsAdmin(userName);
	}

	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,  
			@RequestParam(required = false, defaultValue = "10") int length,
			int jobGroup, String jobDesc, String executorHandler, String filterTime) {
		
		return xxlJobService.pageList(start, length, jobGroup, jobDesc, executorHandler, filterTime);
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(HttpServletRequest request, XxlJobInfo jobInfo) {
		if(isEditable(request)) {
			return xxlJobService.add(jobInfo);
		}else {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "没有操作权限");
		}
	}
	
	@RequestMapping("/reschedule")
	@ResponseBody
	public ReturnT<String> reschedule(HttpServletRequest request, XxlJobInfo jobInfo) {
		if(isEditable(request)) {
			return xxlJobService.reschedule(jobInfo);
		}else {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "没有操作权限");
		}
	}
	
	@RequestMapping("/remove")
	@ResponseBody
	public ReturnT<String> remove(HttpServletRequest request, int id) {
		if(isEditable(request)) {
			return xxlJobService.remove(id);
		}else {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "没有操作权限");
		}
	}
	
	@RequestMapping("/pause")
	@ResponseBody
	public ReturnT<String> pause(HttpServletRequest request, int id) {
		if(isEditable(request)) {
			return xxlJobService.pause(id);
		}else {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "没有操作权限");
		}
	}
	
	@RequestMapping("/resume")
	@ResponseBody
	public ReturnT<String> resume(HttpServletRequest request, int id) {
		if(isEditable(request)) {	
			return xxlJobService.resume(id);
		}else {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "没有操作权限");
		}
	}
	
	@RequestMapping("/trigger")
	@ResponseBody
	public ReturnT<String> triggerJob(HttpServletRequest request, int id) {
		if(isEditable(request)) {
			return xxlJobService.triggerJob(id);
		}else {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "没有操作权限");
		}
	}
	
}
