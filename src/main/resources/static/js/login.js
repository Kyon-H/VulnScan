$.ajaxSetup({
    async : false
});
function getCaptcha(){
}

function checkUsername(username){
    if(username==null){
        return false;
    }
    if(username.length<=3||username.length>=11){
        return false;
    }
    return true;
}


function checkPassword(password){
    if(password==null){
        return false;
    }
    if(password.length<=3||password.length>=21){
        return false;
    }
    return true;
}

function checkEmail(email){
    var re = new RegExp("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$");
    if(re.test(email)&&email!=""){
        return true;
    }
}

function RegisterSubmit(){
    var form={
        username: $("#inputUsername").val(),
        email: $("#inputEmail").val(),
        password: $("#inputPassword").val(),
        repassword:$("#inputRepassword").val(),
        }
    if(checkUsername(form.username)==false){
        layer.msg("用户名长度必须在4-10之间", {icon: 2});
        return false;
    }
    if(checkPassword(form.password)==false){
        layer.msg("密码长度必须在4-20之间", {icon: 2});
        return false;
    }
    if(form.repassword!=form.password){
        layer.msg("两次密码不一致", {icon: 2});
        return false;
    }

    // 提交
    $.post("/Register",
        form,
        function(data){
            if(data.code !=200)
            {
                layer.alert(data.msg,{icon: 2,title:false});
            }
},'json');
return false;
}

function LoginSubmit(){
    var form={
        username: $("#inputUsername").val(),
        password: $("#inputPassword").val()
    }
    if(checkUsername(form.username)==false){
        layer.msg("用户名长度必须在4-10之间", {icon: 2});
        return false;
    }
    if(checkPassword(form.password)==false){
        layer.msg("密码长度必须在4-20之间", {icon: 2});
        return false;
    }
    return true;

    // 提交
    $.post("/Login",
        form,
        function(data){
            if(data.code!=200)
            {
                layer.alert(data.msg,{icon: 2,title:false});
            }
        }
    );
}
