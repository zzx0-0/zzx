layui.define(['index', 'form', 'laydate', 'util'], function(exports){
    var $ = layui.$
        ,admin = layui.admin
        ,element = layui.element
        ,layer = layui.layer
        ,laydate = layui.laydate
        ,form = layui.form
        ,util = layui.util;

    form.render(null, 'component-form-exam');


    var currurl = window.location.href;
    if (currurl.lastIndexOf("/1") < 0) {
        $("#sub").hide();
    }
    var lo = currurl.lastIndexOf("?examId=");
    if (lo > 0) {
        var eId = currurl.substring(lo+8, currurl.length);
        //给表单赋值
        $.ajax({
            url: '/1/examDetail',
            type: 'get',
            contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
            data: {
                "examId": eId
            },
            dataType: "json",
            success: function (res) {
                form.val("component-form-exam", { //formTest 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
                    "examId": res.data.examId
                    ,"examName": res.data.examName
                    ,"examPassword": res.data.examPassword
                    ,"examProblemId": res.data.examProblemId
                    ,"examPro": res.data.examPro
                    ,"examBeginTime": res.data.examBeginTime
                    ,"examEndTime": res.data.examEndTime
                    ,"examLength": res.data.examLength
                });
            }
        });
    } else {
        $("#iditem").hide();
    }


    //开始日期
    var insStart = laydate.render({
        elem: '#test-laydate-start'
        ,type: 'datetime'
        ,theme: 'molv'
        ,min: 0
        ,bian : true// 新增的
        ,done: function(value, date){
            //更新结束日期的最小日期
            insEnd.config.min = lay.extend({}, date, {
                month: date.month - 1
            });

            //自动弹出结束日期的选择器
            insEnd.config.elem[0].focus();
        }
    });

    //结束日期
    var insEnd = laydate.render({
        elem: '#test-laydate-end'
        ,type: 'datetime'
        ,theme: 'molv'
        ,min: 0
        ,done: function(value, date){
            //更新开始日期的最大日期
            insStart.config.max = lay.extend({}, date, {
                month: date.month - 1
            });
        }
    });

    /* 监听提交 */
    form.on('submit(component-form-demo1)', function(data){
        admin.req({
            url: "/1/exam",
            type: "POST",
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(data.field),
            dataType: "json",
            done: function (result) {
                if (result.code === 0) {
                    layer.alert(result.msg, {icon: 1});
                } else {
                    alert("错误信息：" + result.msg);
                }
            }
        });
        return false;
    });

    $("#texta").text("           \n" +
        "                    试卷由5大题型组成，分别是\n" +
        "                    1 - 选择题,\n" +
        "                    2 - 填空题,\n" +
        "                    3 - 判断题,\n" +
        "                    4 - 综合题,\n" +
        "                    5 - 书写\n" +
        "                    \n" +
        "                    [ [1, 2], [3, 4], [5, 6], [7, 8], [9, 10] ] 的意思是\n" +
        "                    第一大题有1小题，每题2分；   第二大题有3小题， 每题4分   。。。。\n" +
        "                    可通过这种方式, 让系统自动生成试卷试题\n" +
        "                    但需保证 ： 总分为 100\n" +
        "                    \n");



    exports('ex', {});
});