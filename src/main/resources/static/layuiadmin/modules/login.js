layui.define(['form','jquery','sliderVerify'], function(exports) {

    var $ = layui.$,
        admin = layui.admin,
        layer = layui.layer,
        form = layui.form,
        sliderVerify = layui.sliderVerify;

    $("#forget").click(function (){
        layer.msg('请联系管理员！', {
            offset: '15px',
            icon: 2
        });
    });

    form.render();

    var slider = sliderVerify.render({
        elem: '#slider'
    })

    form.verify({
        code: [/(.+){3,10}$/, '请输入有效账号'],
        pass: [/(.+){3,12}$/, '请输入有效密码'],
        content: function(value){
            layedit.sync(editIndex);
        }
    });

    form.on('submit(LAY-user-login-submit)', function(data){
        if(slider.isOk()){//用于表单验证是否已经滑动成功
            console.log("滑块检验通过");
        }else{
            layer.msg("请先通过滑块验证");
            return false;
        }

        $.ajax({
            url: '/login'
            ,type: 'post'
            ,data: JSON.stringify(data.field)
            ,contentType: "application/json"
            ,dataType: "json"
            ,success: function(result){
                if (result.code === 0) {
                    layer.msg('登入成功', {
                        offset: '15px'
                        , icon: 1
                        , time: 1000
                    }, function () {
                        location.href = '/';
                    });
                } else {
                    layer.msg(result.msg, {
                        offset: '15px'
                        , icon: 2
                        , time: 1000
                    }, function () {
                        location.href = '/';
                    });
                }
            }
        });
    });




    //对外暴露的接口
    exports('login',{});
});