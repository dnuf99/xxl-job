$(function() {
	// init date tables
	var jobTable = $("#job_list").dataTable({
		"deferRender": true,
		"processing" : true, 
	    "serverSide": true,
		"ajax": {
			url: base_url + "/jobmontor/pageList",
			type:"post",
	        data : function ( d ) {
	        	var obj = {};
	        	obj.start = d.start;
	        	obj.length = d.length;
                return obj;
            }
	    },
	    "searching": false,
	    "ordering": false,
	    //"scrollX": true,	// X轴滚动条，取消自适应
	    "columns": [
	                
					{
						"data": 'childJobKey',
						"width":'10%',
						"visible" : true,
						"render": function ( data, type, row ) {
							var jobKey = row.jobGroup + "_" + row.id;
							return jobKey;
						}
					},
	                { "data": 'jobDesc', "visible" : true,"width":'10%'},
	                { "data": 'dailyFailCnt', "visible" : true,"width":'10%'},
	                { "data": 'dailySuccessCnt', "visible" : true,"width":'10%'},
	                { "data": 'lastCallTime', "visible" : true,"width":'10%'},
	                { "data": 'dailyAvgCallTime', "visible" : true,"width":'10%'},
	                { "data": 'dailyMaxCallTime', "visible" : true,"width":'10%'},
	                { "data": 'monthFailCnt', "visible" : true,"width":'10%'},
	                { "data": 'monthSuccessCnt', "visible" : true,"width":'10%'},
	                { "data": 'monthAvgCallTime', "visible" : true,"width":'10%'},
	                { "data": 'monthMaxCallTime', "visible" : true,"width":'10%'}
	                    
					
	                
	            ],
		"language" : {
			"sProcessing" : "处理中...",
			"sLengthMenu" : "每页 _MENU_ 条记录",
			"sZeroRecords" : "没有匹配结果",
			"sInfo" : "第 _PAGE_ 页 ( 总共 _PAGES_ 页，_TOTAL_ 条记录 )",
			"sInfoEmpty" : "无记录",
			"sInfoFiltered" : "(由 _MAX_ 项结果过滤)",
			"sInfoPostFix" : "",
			"sSearch" : "搜索:",
			"sUrl" : "",
			"sEmptyTable" : "表中数据为空",
			"sLoadingRecords" : "载入中...",
			"sInfoThousands" : ",",
			"oPaginate" : {
				"sFirst" : "首页",
				"sPrevious" : "上页",
				"sNext" : "下页",
				"sLast" : "末页"
			},
			"oAria" : {
				"sSortAscending" : ": 以升序排列此列",
				"sSortDescending" : ": 以降序排列此列"
			}
		}
	});

    // table data
    var tableData = {};



	
	
	

});
