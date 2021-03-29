layui.define(['index', 'form', 'laydate'], function(exports){
    var $ = layui.jquery
        ,admin = layui.admin
        ,form = layui.form
        ,table = layui.table;

    form.on('submit(classId-Name)', function(data){
        var field = data.field;
        //执行重载
        table.reload('class-table', {
            where: field
        });
    });

    table.render({
        elem: '#class-table'
        ,url: '/1/classes'
        ,method: 'get'
        ,width: 740
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
                {field:'classId', width:100, title: 'ID', align: 'center', sort: true , fixed: 'left',}  // type有多种
                ,{field:'classCode', width:150, title: '班级代码', align: 'center'}
                ,{field:'className', width:200, title: '班级名称', align: 'center'}
                ,{field:'classStuCount', width:150, title: '班级人数', align: 'center'}
                ,{width:130, title:'操作', align: 'center', fixed: 'right', toolbar: '#class-table-barDemo'}
            ]
        ]
        ,page: true
        ,limit: 20
        ,limits: [10,20,30,50,100]
        ,skin: 'row'
        ,even: true
    });

    //监听工具条
    table.on('tool(class-table)', function(obj){
        var data = obj.data;
        if(obj.event === 'detail'){
            layer.msg('det');
        } else if(obj.event === 'edit'){
            layer.prompt(
                {
                    title: '设置班级名',
                    formType: 0,
                    value: data.className
                }, function(value, index, elem){
                    console.log("value+"+value+", elem="+elem);
                    $.ajax({
                        url: '/1/className',
                        type: 'put',
                        contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
                        data: {
                            "classCode": data.classCode,
                            "className": value
                        },
                        dataType: 'json',
                        success: function (result) {
                            if (result.code === 0) {
                                layer.alert('success', {icon: 1});
                            } else {
                                layer.msg('不开心。。' + result.msg, {icon: 5});
                            }
                            layui.table.reload('class-table'); //重载表格
                            layer.close(index); //执行关闭
                        },
                        error: function (result) {
                            layer.msg('不开心。。' + result.responseText, {icon: 5});
                        }
                    });
                    layer.close(index);
                }
            );
        }

    });


    exports('clas', {});
});