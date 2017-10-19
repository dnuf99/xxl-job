$(function(){
	// 复选框
    $('input').iCheck({
      checkboxClass: 'icheckbox_square-blue',
      radioClass: 'iradio_square-blue',
      increaseArea: '20%' // optional
    });
    
	// 登录.规则校验
	var loginFormValid = $("#changePwdForm").validate({
		errorElement : 'span',  
        errorClass : 'help-block',
        focusInvalid : true,  
        rules : {  
            oldPassword : {  
            	required : true ,
                minlength: 5,
                maxlength: 18
            },
             newPassword : {  
            	required : true ,
                minlength: 5,
                maxlength: 18
            } ,
             checkPassword : {  
            	required : true ,
                minlength: 5,
                maxlength: 18,
                equalTo: "#newPassword"
            } 
        }, 
        messages : {  
            oldPassword : {
            	required :"请输入原登录密码."  ,
                minlength:"原登录密码不应低于5位",
                maxlength:"原登录密码不应超过18位"
            },
            newPassword : {
            	required :"请输入新登录密码."  ,
                minlength:"新登录密码不应低于5位",
                maxlength:"新登录密码不应超过18位"
            },
            checkPassword : {
            	required :"请再次输入登录密码."  ,
                minlength:"登录密码不应低于5位",
                maxlength:"登录密码不应超过18位",
                equalTo: "*请再次输入相同的新密码"
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
			$.post(base_url + "/updatePwd", $("#changePwdForm").serialize(), function(data, status) {
				if (data.code == "200") {
                    layer.open({
                        title: '系统提示',
                        content: '修改密码成功',
                        icon: '1',
                        end: function(layero, index){
                            window.location.href = base_url;
                        }
                    });
				} else {
                    layer.open({
                        title: '系统提示',
                        content: (data.msg || "修改密码失败"),
                        icon: '2'
                    });
				}
			});
		}
	});
});