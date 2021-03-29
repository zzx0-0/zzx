
layui.define(['index', 'form', 'laydate', 'upload'], function(exports){
    var $ = layui.$
        ,admin = layui.admin
        ,element = layui.element
        ,layer = layui.layer
        ,laydate = layui.laydate
        ,form = layui.form
        ,upload = layui.upload;

    form.render(null, 'component-form-group');

    laydate.render({
        elem: '#LAY-component-form-group-date'
    });

    //给表单赋值
    $.ajax({
        url: '/me',
        type: 'get',
        success: function (res) {
            form.val("component-form-group", { //formTest 即 class="layui-form" 所在元素属性 lay-filter="" 对应的值
                "userCode": res.data.userCode
                ,"userName": res.data.userName
                ,"userPetName": res.data.userPetName
                ,"userGender": res.data.userGender
                ,"userPhone": res.data.userPhone
                ,"userMail": res.data.userMail
                ,"userBirthday": res.data.userBirthday
                ,"userMotto": res.data.userMotto
                ,"userAvatar": res.data.userAvatar
            });
        }
    });
    form.render(null, 'component-form-group');

    /* 自定义验证规则 */
    form.verify({
        title: function(value){
            if(value.length < 5){
                return '账号至少得5个字符啊';
            }
        }
        ,content: function(value){
            layedit.sync(editIndex);
        }
    });


    //上传头像
    var avatarSrc = $('#LAY_avatarSrc');
    upload.render({
        elem: '#LAY_avatarUpload',
        url: '/i/file/upload',
        done: function(res){
            if(res.code === 0){
                avatarSrc.val(res.data);
                layer.msg("图片上传成功", {
                    offset: '15px'
                    ,icon: 1
                    ,time: 1000
                });
            } else {
                layer.msg(res.msg, {icon: 5});
            }
        }
    });

    //查看头像
    admin.events.avartatPreview = function(othis){
        var src = avatarSrc.val();
        layer.photos({
            photos: {
                "title": "查看头像" //相册标题
                ,"data": [{
                    "src": src //原图地址
                }]
            }
            ,shade: 0.01
            ,closeBtn: 1
            ,anim: 5
        });
    };


    /* 监听提交 */
    form.on('submit(component-form-demo1)', function(data){
        admin.req({
            url: "/i/users",
            type: "put",
            contentType: "application/json;charset=UTF-8",
            data: JSON.stringify(data.field),
            dataType: "json",
            done: function (result) {
                if (result.code === 0) {
                    layer.alert(result.msg, {icon: 1});
                }
            }
        });
        return false;
    });


    //对外暴露的接口
    exports('set', {});
});