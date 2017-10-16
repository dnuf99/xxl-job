<!DOCTYPE html>
<html>
<head>
  	<title>任务调度中心</title>
  	<#import "/common/common.macro.ftl" as netCommon>
	<@netCommon.commonStyle />
	<!-- DataTables -->
  	<link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.css">

</head>
<body class="hold-transition skin-blue sidebar-mini <#if cookieMap?exists && "off" == cookieMap["xxljob_adminlte_settings"].value >sidebar-collapse</#if>">
<div class="wrapper">
	<!-- header -->
	<@netCommon.commonHeader />
	<!-- left -->
	<@netCommon.commonLeft "jobmontor" />
	
	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- Content Header (Page header) -->
		<section class="content-header">
			<h1>任务监控<small>任务调度中心</small></h1>
			<!--
			<ol class="breadcrumb">
				<li><a><i class="fa fa-dashboard"></i>调度监控</a></li>
				<li class="active">调度中心</li>
			</ol>
			-->
		</section>
		
		<!-- Main content -->
	    <section class="content">
	    
	    	
	    	
			<div class="row">
				<div class="col-xs-12">
					<div class="box">
			            <div class="box-header hide">
			            	<h3 class="box-title">调度监控数据</h3>
			            </div>
			            <div class="box-body" >
			              	<table id="job_list" class="table table-bordered table-striped">
				                <thead>
					            	<tr>
                                        <th name="childJobKey" >JobKey</th>
					                  	<th name="jobDesc" >描述</th>
                                        <th name="dailyFailCnt" >日失败调用数</th>
					                  	<th name="dailySuccessCnt" >日成功调用数</th>
                                        <th name="dailyAvgCallTime" >日平均调用时间（MS）</th>
					                  	<th name="dailyMaxCallTime" >日最长调用时间（MS）</th>
					                  	<th name="monthFailCnt" >月失败调用数</th>
					                  	<th name="monthSuccessCnt" >月成功调用数</th>
					                  	<th name="monthAvgCallTime" >月平均调用时间（MS）</th>
					                  	<th name="monthMaxCallTime" >月最长调用时间（MS）</th>
					                </tr>
				                </thead>
				                <tbody></tbody>
				                <tfoot></tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
	    </section>
	</div>
	
	<!-- footer -->
	<@netCommon.commonFooter />
</div>



<@netCommon.commonScript />
<!-- DataTables -->
<script src="${request.contextPath}/static/adminlte/plugins/datatables/jquery.dataTables.min.js"></script>
<script src="${request.contextPath}/static/adminlte/plugins/datatables/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/static/plugins/jquery/jquery.validate.min.js"></script>
<!-- moment -->
<script src="${request.contextPath}/static/adminlte/plugins/daterangepicker/moment.min.js"></script>
<script src="${request.contextPath}/static/js/jobmontor.index.1.js"></script>
</body>
</html>
