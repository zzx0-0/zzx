layui.define(['table', 'form'], function(exports) {
    var $ = layui.$
        ,layer = layui.layer
        ,view = layui.view
        ,table = layui.table
        ,form = layui.form
        ,admin =layui.admin;

    form.on('select(pro-type)', function(data){
        //执行重载
        table.reload('pro-table', {
            where: {
                type: data.value
            }
        });
    });

    table.render({
        elem: '#pro-table'

        ,url: '/2/proShow'
        ,method: 'get'
        ,width: 930

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
                {field:'problemId', width:100, title: 'ID', align: 'center', sort: true , fixed: 'left',}  // type有多种
                ,{field:'problemText', width:400, title: '问题描述', align: 'center'}
                ,{field:'problemAnswer', width:200, title: '问题答案', align: 'center'}
                ,{field:'problemType', width:100, title: '类型', align: 'center', sort: true, templet: '#statusTpl'}
                ,{title:'操作', width:130, align: 'center', fixed: 'right', toolbar: '#pro-table-barDemo',}
            ]
        ]
        ,page: true
        ,limit: 20
        ,limits: [10,20,30,50,100]
        ,skin: 'row'  // 行/列边框风格
        ,even: true   // 开启隔行背景

        // ,done(res, curr, count){ }   //回调函数
    });

    table.on('tool(pro-table)', function(obj){
        var data = obj.data;
        if(obj.event === 'edit'){
            
        } else if (obj.event === 'del'){
            layer.confirm('真的删除该试题么', function(index){
                admin.req({
                    url: '/2/pro/' + data.problemId,
                    type: 'delete',

                    // headers: {
                    //     Authorization: setter.request.tokenPrefix + layui.data(setter.tableName).token
                    // },
                    dataType: 'json',
                    done: function (result) {    //
                        if (result.code === 0) {
                            layer.alert(result.msg, {icon: 1});
                        } else {
                            layer.msg(result.msg, {icon: 5});
                        }
                        layui.table.reload('pro-table'); //重载表格
                        layer.close(index); //执行关闭
                    }
                });
                layer.close(index);
            });
        }
    });



    var active = {
        reload: function(){
            var demoReload = $('#test-table-demoReload');

            //执行重载
            table.reload('pro-table', {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                ,where: {
                    key: {
                        problemId: demoReload.val()
                    }
                }
            });
        }
    };

    $('.test-table-reload-btn .layui-btn').on('click', function(){
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });


    //对外暴露的接口
    exports('problem2',{});
});