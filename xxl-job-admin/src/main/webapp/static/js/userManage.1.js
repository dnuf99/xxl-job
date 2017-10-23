$(function(){
	// 复选框
    $('input').iCheck({
      checkboxClass: 'icheckbox_square-blue',
      radioClass: 'iradio_square-blue',
      increaseArea: '20%' // optional
    });
    
	// 登录.规则校验
	var loginFormValid = $("#userAddForm").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {  
            userName : {  
            	required : true ,
                minlength: 5,
                maxlength: 45
            },
             roleName : {  
            	required : true 
            }
        }, 
        messages : {  
            userName : {
            	required :"请输入用户名称."  ,
                minlength:"用户名称不应低于5位",
                maxlength:"用户名称不应超过18位"
            },
            roleName : {
            	required :"请选择用户角色."  

            }
        }, 
		highlight : function(element) {  
            $(element).closest('.form-group').addClass('has-error');  
        },
        success : function(label) {  
            label.closest('.form-group').removeClass('has-error');  
            label.remove();  
        },
        errorPlacement : function(error, element) {  
            element.parent('div').append(error);  
        },
        submitHandler : function(form) {
			$.post(base_url + "/userAdd", $("#userAddForm").serialize(), function(data, status) {
				if (data.code == "200") {
                    layer.open({
                        title: '系统提示',
                        content: '新增用户成功',
                        icon: '1'
                    });
				} else {
                    layer.open({
                        title: '系统提示',
                        content: (data.msg || "新增用户失败"),
                        icon: '2'
                    });
				}
			});
		}
	});
});