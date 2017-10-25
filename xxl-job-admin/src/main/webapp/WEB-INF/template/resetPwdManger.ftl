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
				<li class="active">密码重置</li>
			</ol>
			-->
		</section>

		<!-- Main content -->
		<section class="content">
		  <div class="login-box">
			<form id="resetPwdForm" method="post" class="forget-pwd">
				<div class="login-box-body">
					<p class="login-box-msg">密码重置</p>
					
					<div class="form-group has-feedback">
		            	<label for="userName" class="col-sm-8 control-label">用户名称<font color="red">*</font></label>
                        <input type="text" class="form-control" name="userName" placeholder="请输入用户名称">
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
<script src="${request.contextPath}/static/js/resetPwdManage.1.js"></script>


</body>
</html>
