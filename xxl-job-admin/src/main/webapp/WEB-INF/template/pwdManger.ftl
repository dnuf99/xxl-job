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
				<li class="active">密码管理</li>
			</ol>
			-->
		</section>

		<!-- Main content -->
		<section class="content">
		  <div class="login-box">
			<form id="changePwdForm" method="post" class="forget-pwd">
				<div class="login-box-body">
					<p class="login-box-msg">密码修改</p>
					<div class="form-group has-feedback">
		            	<input type="password" name="oldPassword" class="form-control" placeholder="请输入原登录" >
		            	<span class="glyphicon glyphicon-lock form-control-feedback"></span>
					</div>
		          	<div class="form-group has-feedback">
		            	<input type="password" name="newPassword" id="newPassword" class="form-control" placeholder="请输入新登录密码">
		            	<span class="glyphicon glyphicon-lock form-control-feedback"></span>
		          	</div>
		          	<div class="form-group has-feedback">
		            	<input type="password" name="checkPassword" class="form-control" placeholder="请再次输入新登录密码">
		            	<span class="glyphicon glyphicon-lock form-control-feedback"></span>
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
<script src="${request.contextPath}/static/js/pwdChange.1.js"></script>


</body>
</html>
