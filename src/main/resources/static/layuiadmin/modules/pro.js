layui.define(['index', 'form', 'laydate'], function(exports){
    var $ = layui.$
        ,admin = layui.admin
        ,element = layui.element
        ,layer = layui.layer
        ,laydate = layui.laydate
        ,form = layui.form;

    form.render(null, 'component-form-pro');

    var currurl = window.location.href;
    if (currurl.lastIndexOf("/2") < 0) {
        $("#sub").hide();
    }
    var lo = currurl.lastIndexOf("?proId=");
    if (lo > 0) {
        var pId = currurl.substring(lo+7, currurl.length);
        //给表单赋值
        $.ajax({
            url: '/i/pro',
            type: 'get',
            contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
            data: {
                "proId": pId
            },
            success: function (res) {
                form.val("component-form-pro", { //formTest 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
                    "problemId": res.data.problemId
                    ,"problemText": res.data.problemText
                    ,"problemAnswer": res.data.problemAnswer
                    ,"problemType": res.data.problemType
                });
            }
        });
    } else {
        $("#iditem").hide();
    }



    /* 监听提交 */
    form.on('submit(component-form-demo1)', function(data){
        admin.req({
            url: "/2/pro",
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

    exports('pro', {});
});