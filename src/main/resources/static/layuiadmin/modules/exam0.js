layui.define(['table', 'form'], function(exports) {
    var $ = layui.$
        ,layer = layui.layer
        ,table = layui.table
        ,form = layui.form
        ,admin =layui.admin;

    table.render({
        elem: '#exam-table'
        ,url: '/0/examShow'
        ,method: 'get'
        ,width: 950
        ,request: {
            pageName: 'pageNum'
            ,limitName: 'pageSize'
        }
        ,parseData: function(res){ //res 即为原始返回的数据
            return {
                "code": res.code, //解析接口状态
                "msg": res.msg, //解析提示文本
                "count": res.data.total, //解析数据长度
                "data": res.data.list //解析数据列表
            };
        }
        ,cols: [
            [
                {field:'examId', width:100, title: 'ID', align: 'center', sort: true , fixed: 'left',}  // type有多种
                ,{field:'examName', width:200, title: '考试名称', align: 'center', templet: '#usernameTpl'}
                ,{field:'examLength', width:120, title: '时长（h）', align: 'center'}
                ,{field:'examBeginTime', width:170, title: '开始时间', align: 'center', sort: true}
                ,{field:'examEndTime', width:170, title: '结束时间', align: 'center', sort: true}
                ,{title:'状态', width:100, align: 'center', templet: '#statusTpl'}  //  style:'background-color: #eee;'
                ,{title:'操作', width:80, align: 'center',fixed: 'right', toolbar: '#exam-table-barDemo'}
            ]
        ]
        ,page: true
        ,limit: 20
        ,limits: [10,20,30,50,100]
        ,skin: 'row'  // 行/列边框风格
        ,even: true   // 开启隔行背景
        ,text:{none: ''}

        ,done(res, curr, count){
        }   //回调函数
    });

    table.on('tool(exam-table)', function(obj){
        var data = obj.data;
        if(obj.event === 'detail'){
            admin.req({
                url: '/0/score',
                type: 'get',
                contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
                data: {
                    "examId": data.examId
                },
                dataType: 'json',
                done: function (result) {
                    if (result.code === 0) {
                        layer.alert(result.msg+"，该次成绩为："+result.data, {icon: 1});
                    }
                }
            })
        }
    });

    //对外暴露的接口
    exports('exam0',{});
});