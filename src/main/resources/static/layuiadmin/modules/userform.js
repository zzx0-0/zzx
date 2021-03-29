
layui.define(['index', 'form', 'laydate'], function(exports){
    var $ = layui.$
        ,admin = layui.admin
        ,element = layui.element
        ,layer = layui.layer
        ,laydate = layui.laydate
        ,form = layui.form;

    form.render(null, 'component-form-user');


    var currurl = window.location.href;
    var lo = currurl.lastIndexOf("?user=");
    if (lo > 0) {
        var uCode = currurl.substring(lo+6, currurl.length);
        //给表单赋值
        $.ajax({
            url: '/i/users/' + uCode,
            type: 'get',
            dataType: "json",
            success: function (res) {
                form.val("component-form-user", { //formTest 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
                    "userId": res.data.userId
                    ,"userCode": res.data.userCode
                    ,"userName": res.data.userName
                    ,"userPassword": res.data.userPassword
                    ,"userGender": res.data.userGender
                    ,"userRole": res.data.userRole
                    ,"userClassCode": res.data.userClassCode
                    //,"userDeleted": res.data.userDeleted
                });
                if (res.data.userRole !== 0)
                    $("#classCodeItem").hide();
                if (res.data.userDeleted === 0)
                    $("#deleteItem").hide();
            }
        });
        $("#passItem").hide();

    } else {
        $("#iditem").hide();
        $("#classCodeItem").hide();
        $("#deleteItem").hide();

        form.verify({
            code: [/(.+){3,10}$/, '账号必须3到10位'],
            nam: [/(.+){2,4}$/, '姓名必须2到4位'],
            pass: [/(.+){3,12}$/, '密码必须3到12位'],
            content: function(value){
                layedit.sync(editIndex);
            }
        });
    }


    /* 监听提交 */
    form.on('submit(component-form-demo1)', function(data){
        admin.req({
            url: "/2/register",
            type: "POST",
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(data.field),
            dataType: "json",
            done: function (result) {
                if (result.code === 0) {
                    layer.alert(result.msg, {icon: 1});
                }
            }
        });
        return false;
    });



    //对外暴露的接口
    exports('userform', {});
});