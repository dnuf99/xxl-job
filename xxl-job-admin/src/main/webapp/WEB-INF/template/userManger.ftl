<!DOCTYPE html>
<html>
<head>
  	<title>任务调度中心</title>
  	<#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if> ">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "help" />
	
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>用户中心<small>任务调度中心</small></h1>
			<!--
			<ol class="breadcrumb">
				<li><a><i class="fa fa-dashboard"></i>调度中心</a></li>
				<li class="active">用户管理</li>
			</ol>
			-->
		</section>

		<!-- Main content -->
		<section class="content">
		  <div class="login-box">
			<form id="userAddForm" method="post" class="forget-pwd">
				<div class="login-box-body">
					<p class="login-box-msg">新增用户</p>
					
					<div class="form-group has-feedback">
		            	<label for="userName" class="col-sm-8 control-label">用户名称<font color="red">*</font></label>
                        <input type="text" class="form-control" name="userName" placeholder="请输入用户名称">
					</div>
		          	<div class="form-group has-feedback">
					  	<label for="roleName" class="col-sm-8 control-label">用户角色<font color="red">*</font></label>
                            <select class="form-control" name="roleName" >
							<#list roleList as role>
                                <option value="${role.roleName}" >${role.roleName}</option>
							</#list>
                            </select>
		          	</div>
		          	
		          	<div class="form-group has-feedback">
		            	<label for="fullname" class="col-sm-8 control-label">用户全名</label>
                        <input type="text" class="form-control" name="fullname" placeholder="请输入用户真实姓名">
					</div>
					<div class="form-group has-feedback">
		            	<label for="mobileno" class="col-sm-8 control-label">用户手机号码</label>
                        <input type="text" class="form-control" name="mobileno" placeholder="请输入用户手机号码">
					</div>
					

		            <div class="form-group has-feedback">
						<button type="submit" class="btn btn-primary btn-block btn-flat">确定</button>
					</div>

				</div>      
			    </form><!--forget-pwd/-->
			</div>
		</section>
		<!-- /.content -->
	</div>
	<!-- /.content-wrapper -->
	
</div>
<@netCommon.commonScript />
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>
<script src="${request.contextPath}/static/js/userManage.1.js"></script>


</body>
</html>
