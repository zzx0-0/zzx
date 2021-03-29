layui.define(['table', 'form'], function(exports) {
    var $ = layui.$
        ,layer = layui.layer
        ,table = layui.table
        ,form = layui.form
        ,admin =layui.admin;

    table.render({
        elem: '#exam-table'
        // ,height: 315 //容器高度

        ,url: '/1/examShow'
        ,method: 'get'
        ,width: 1000

        // ,cellMinWidth: 80 //全局定义常规单元格的最小宽度

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
                ,{field:'examName', width:200, title: '考试名称', align: 'center'}
                ,{field:'examLength', width:120, title: '时长（h）', align: 'center'}
                ,{field:'examBeginTime', width:170, title: '开始时间', align: 'center', sort: true}
                ,{field:'examEndTime', width:170, title: '结束时间', align: 'center', sort: true}
                ,{title: '状态',width:100, align: 'center', templet: '#statusTpl'}  //  style:'background-color: #eee;'
                ,{title:'操作', width:130, align: 'center', fixed: 'right', toolbar: '#exam-table-barDemo'}
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
        if(obj.event === 'edit'){

        } else if(obj.event === 'del'){
            layer.confirm('真的删除该次考试么', function(index){
                admin.req({      //
                    url: '/1/exam/' + data.examId,
                    type: 'delete',

                    // headers: {
                    //     Authorization: setter.request.tokenPrefix + layui.data(setter.tableName).token
                    // },
                    dataType: 'json',
                    done: function (result) {
                        if (result.code === 0) {
                            layer.alert(result.msg, {icon: 1});
                        } else {
                            layer.msg(result.msg, {icon: 5});
                        }
                        layui.table.reload('exam-table'); //重载表格
                        layer.close(index); //执行关闭
                    }
                });
                layer.close(index); //执行关闭
            });
        }
    });

    //对外暴露的接口
    exports('exam1',{});
});