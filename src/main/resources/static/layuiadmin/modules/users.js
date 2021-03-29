layui.define(['index', 'table'], function(exports){
    var $ = layui.jquery,
        admin = layui.admin,
        form = layui.form,
        table = layui.table;

    form.on('submit(LAY-user-front-search)', function(data){
        var field = data.field;
        //执行重载
        table.reload('user-table', {
            where: field
        });
    });

    table.render({
        elem: '#user-table'

        ,url: '/2/users'
        ,method: 'get'

        // ,height: 315 //容器高度
        ,width: 790
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
                {field:'userId', width:100, title: 'ID', align: 'center', sort: true , fixed: 'left',}  // type有多种
                ,{field:'userCode', width:150, title: '账号', align: 'center', sort: true}
                ,{field:'userName', width:150, title: '姓名', align: 'center'}
                ,{field:'userRole', width:100, title: '角色', align: 'center', sort: true, templet: '#roleTpl'}
                ,{field:'userDeleted', width:100, title: '状态', align: 'center', sort: true, templet: '#statusTpl'}
                ,{title:'操作', width:180, align: 'center', fixed: 'right', toolbar: '#user-table-barDemo', }
            ]
        ]
        ,page: true
        ,limit: 20
        ,limits: [10,20,30,50,100]
        ,skin: 'row'  // 行/列边框风格
        ,even: true   // 开启隔行背景

        // ,done(res, curr, count){ }   //回调函数
    });

    table.on('tool(user-table)', function(obj){
        var data = obj.data;
        if(obj.event === 'detail'){

        } else if(obj.event === 'edit'){
            layer.prompt({title: '敏感操作，请输入指令', formType: 1},function(value1, index){
                layer.prompt({title: '请输入重置密码', formType: 0},function(value2, index){
                    admin.req({
                        url: '/2/password',
                        type: 'put',
                        contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
                        data: {
                            "userCode": data.userCode,
                            "password": value2,
                            "value1": value1   // 新增
                        },
                        dataType: 'json',
                        done: function (result) {
                            if (result.code === 0) {
                                layer.alert(result.msg, {icon: 1});
                            } else {
                                alert("错误信息：" + result.msg);
                            }
                        }
                    });
                    layer.close(index);
                });
                layer.close(index);
            }, function (){
                layer.msg('吓死宝宝了', {
                    time: 1500,
                });
            });
        } else if(obj.event === 'del'){
            layer.prompt({title: '敏感操作，请输入指令', formType: 1},function(value1, index){
                $.ajax({
                    url: '/2/users/' + data.userCode,
                    type: 'delete',
                    contentType: 'application/x-www-form-urlencoded;charset=UTF-8',
                    data: {
                        "value1": value1   // 新增
                    },
                    dataType: 'json',
                    success: function (result) {
                        if (result.code === 0) {
                            layer.alert(result.msg, {icon: 1, skin: 'layer-ext-moon'});
                        } else {
                            layer.msg(result.msg, {icon: 5});
                        }
                    },
                    error: function (result) {
                        layer.msg('不开心。。' + result.responseText, {icon: 5});
                    }
                });
                layer.close(index);
            });
        }
    });


    exports('users', {});
});