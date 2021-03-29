layui.define(['index', 'form', 'laydate'], function(exports){
    var $ = layui.jquery
        ,admin = layui.admin
        ,form = layui.form
        ,table = layui.table;

    form.on('submit(judge-one-proId)', function(data){
        var field = data.field;
        //执行重载
        table.reload('judge-one-table', {
            where: field
        });
    });

    table.render({
        elem: '#judge-one-table'
        ,url: '/1/judgeO'
        ,method: 'get'
        ,width: 940

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
                {field:'judgeId', width:100, title: 'ID', align: 'center', sort: true , fixed: 'left',}  // type有多种
                ,{field:'problemId', width:100, title: '题目', align: 'center'}
                ,{field:'sub', width:200, title: '作答', align: 'center'}
                ,{field:'score', width:100, title: '分值', align: 'center'}
                ,{field:'give', width:100, title: '给分', align: 'center'}
                ,{field:'judged', width:100, title: '状态', align: 'center', templet: '#statusTpl'}
                ,{field:'judgeBy', width:150, title: '判题老师', align: 'center'}
                ,{width:80, title:'操作', align: 'center', fixed: 'right', toolbar: '#judge-one-table-barDemo'}
            ]
        ]
        ,page: true
        ,limit: 20
        ,limits: [10,20,30,50,100]
        ,skin: 'row'
        ,even: true
    });


    exports('judgeOne', {});
});