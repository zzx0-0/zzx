layui.define(['index', 'carousel', 'echarts', 'table'], function(exports){
    var $ = layui.$
        ,admin = layui.admin
        ,table = layui.table
        ,carousel = layui.carousel
        ,element = layui.element
        ,device = layui.device();

    var currurl = window.location.href;
    var lo = currurl.lastIndexOf("?userCode=");
    var userCode;
    if (lo > 0)  userCode = currurl.substring(lo + 10, currurl.length);

    var obj;
    admin.req({
        url: '/i/score/' + userCode,
        type: 'get',
        async: false,  //同步
        dataType: "json",
        done: function (res) {
            obj = res;
        }
    });


    function funXData(){
        return obj.data.eList;
    }
    function funSData(){
        return obj.data.scList;
    }
    function funTitle(){
        return obj.data.message;
    }


    var myChart = echarts.init(document.getElementById("container"));
    var app = {};
    var option;

    option = {
        title : {
            text: funTitle(),
            x: "center"
        },
        tooltip: {
            trigger: 'axis'
        },
        calculable : true,
        xAxis: {
            name: "历次考试\n(examId)",
            data: funXData(),
            axisLabel : {
                interval : 0,
                rotate: 45
            }
        },
        yAxis: {
            name: "分数",
            type : 'value',
        },
        series: [{
            name: '成绩',
            type: 'line',
            data: funSData()
        }]
    };
    if (option && typeof option === 'object') {
        myChart.setOption(option);
    }



    table.render({
        elem: '#score-table'
        ,url: '/i/score/' + userCode
        ,method: 'get'
        ,width: 535
        ,parseData: function(res){
            return {
                "code": res.code,
                "msg": res.msg,
                "count": res.data.count,
                "data": res.data.scoreList
            };
        }
        ,cols: [
            [
                {field:'examId', width:100, title: 'examId', align: 'center', sort: true}
                ,{field:'remark', width:200, title: '考试名称', align: 'center'}
                ,{field:'score', width:150, title: '成绩', align: 'center', sort: true}
                ,{width:80, title:'操作', align: 'center', fixed: 'right', toolbar: '#score-table-barDemo', }
            ]
        ]
        ,skin: 'row'  // 行/列边框风格
        ,even: true   // 开启隔行背景

    });


    exports('line', {});
});