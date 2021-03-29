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
        ,url: '/1/proShow'
        ,method: 'get'
        ,width: 888

        ,request: {
            pageName: 'pageNum'
            ,limitName: 'pageSize'
        }
        ,parseData: function(res){
            return {
                "code": res.code,
                "msg": res.msg,
                "count": res.data.total,
                "data": res.data.list
            };
        }
        ,cols: [
            [
                {field:'problemId', width:100, title: 'ID', align: 'center', sort: true , fixed: 'left',}  // type有多种
                ,{field:'problemText', width:400, title: '问题描述', align: 'center'}
                ,{field:'problemAnswer', width:200, title: '问题答案', align: 'center'}
                ,{field:'problemType', width:100, title: '类型', align: 'center', sort: true, templet: '#statusTpl'}
                ,{title:'操作',width:80,  align: 'center', fixed: 'right', toolbar: '#pro-table-barDemo'}
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
        if(obj.event === 'detail'){
            layer.msg('detail');
        } else {}
    });

    //对外暴露的接口
    exports('problem1',{});
});