layui.define(['index', 'carousel', 'echarts', 'table'], function(exports){
    var $ = layui.$
        ,admin = layui.admin
        ,table = layui.table;

    var currurl = window.location.href;
    var lo = currurl.lastIndexOf("?examId=");
    var eId;
    if (lo > 0)
        eId = currurl.substring(lo + 8, currurl.length);

    var obj;
    admin.req({
        url: '/1/examScore/' + eId,
        type: 'get',
        async: false,  //同步
        dataType: "json",
        done: function (res) {
            obj = res;
        }
    });

    function funSData(){
        return obj.data.numList;
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
            name: "分数段",
            data: ["0-9","10-19","20-29","30-39","40-49","50-59","60-69","70-79","80-89","90-100"],
            axisLabel : {
                interval : 0,
                rotate: 45
            }
        },
        yAxis: {
            name: "人数",
            type : 'value',
        },
        series: [{
            name: '人数',
            type: 'bar',
            data: funSData()
        }]
    };
    if (option && typeof option === 'object') {
        myChart.setOption(option);
    }




    table.render({
        elem: '#score-table'
        ,url: '/1/examScore/' + eId
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
                {field:'userCode', width:150, title: '学号', align: 'center', sort: true}
                ,{field:'remark', width:150, title: '姓名', align: 'center'}
                ,{field:'score', width:150, title: '成绩', align: 'center', sort: true}
                ,{width:80, title:'操作', align: 'center', fixed: 'right', toolbar: '#score-table-barDemo'}
            ]
        ]
        ,skin: 'row'  // 行/列边框风格
        ,even: true   // 开启隔行背景

    });


    exports('bar', {});
});