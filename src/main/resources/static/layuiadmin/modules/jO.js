layui.define(['index', 'form', 'laydate'], function(exports){
    var $ = layui.$
        , admin = layui.admin
        , element = layui.element
        , layer = layui.layer
        , form = layui.form;

    form.render(null, 'component-form-jO');

    var currurl = window.location.href;
    var lo = currurl.lastIndexOf("?jId=");
    var jId = currurl.substring(lo + 5, currurl.length);
    //给表单赋值
    $.ajax({
        url: '/1/getJO/' + jId,
        type: 'get',
        dataType: "json",
        success: function (res) {
            form.val("component-form-jO", { //formTest 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
                "judgeId": res.data.judgeId
                , "problemText": res.data.problemText
                , "problemAnswer": res.data.problemAnswer
                //   , "problemType": res.data.problemType
                , "sub": res.data.sub
                , "score": res.data.score
                , "give": res.data.give
                , "judgeBy": res.data.judgeBy
            });
        }
    });

    /* 监听提交 */
    form.on('submit(component-form-demo1)', function(){
        //var data = obj.data;
        admin.req({
            url: "/1/updS",
            type: "put",
            contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
            data: {
                "jId": $("#jId").val(),
                "score": $("#score").val()
            },
            dataType: "json",
            done: function (result) {
                if (result.code === 0) {
                    layer.alert(result.msg, {icon: 1});
                }
            }
        });
        return false;
    });


    exports('jO', {});
});