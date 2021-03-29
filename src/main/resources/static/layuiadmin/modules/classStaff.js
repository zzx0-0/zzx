layui.define(['index', 'table', 'laydate'], function(exports){
    var $ = layui.jquery,
        admin = layui.admin,
        form = layui.form,
        table = layui.table;

    form.on('submit(studentCode-Name)', function(data){
        var field = data.field;
        //执行重载
        table.reload('user-table', {
            where: field
        });
    });

    var currurl = window.location.href;
    var lo = currurl.lastIndexOf("?cCode=");
    var cCode = currurl.substring(lo + 7, currurl.length);

    table.render({
        elem: '#user-table'
        ,url: '/1/classes/' + cCode
        ,method: 'get'
        ,width: 485
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
                {field:'userId', width:100, title: 'ID', align: 'center', sort: true , fixed: 'left',}  // type有多种
                ,{field:'userCode', width:150, title: '账号', align: 'center', sort: true}
                ,{field:'userName', width:150, title: '姓名', align: 'center'}
                ,{width:80, title:'操作', align: 'center', fixed: 'right', toolbar: '#user-table-barDemo'}
            ]
        ]
        ,page: false
        // ,limit: 20
        // ,limits: [10,20,30,50,100]
        ,skin: 'row'
        ,even: true
    });


    exports('classStaff', {});
});