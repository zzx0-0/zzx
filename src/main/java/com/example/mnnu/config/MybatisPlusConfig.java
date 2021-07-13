package com.example.mnnu.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.DynamicTableNameInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.example.mnnu.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @Author zzx
 */
@Component
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

//        // 分页插件
//        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
//        paginationInnerInterceptor.setMaxLimit(100000L);
//        interceptor.addInnerInterceptor(paginationInnerInterceptor);
//
//        DynamicTableNameInnerInterceptor myDynamicTableNameInnerInterceptor = new DynamicTableNameInnerInterceptor();
//        // map的key就是原始表名,value就是表名替换规则
//        HashMap<String, MyTableNameHandler> map = new HashMap<String, TableNameHandler>(2) {{
//            //整个函数返回的结果就是替换后的新表名，这个生成的表名的规则可以自己随便指定
//            put("user", (sql, tableName, user) -> tableName + "_"+((User)user).getUserGender());
//
//            //上面的是lambda表达式，等价于下面的代码
//            put("user", new MyTableNameHandler(){
//                @Override
//                public String dynamicTableName(String sql, String tableName){
//                    if (user.() == null){
//                        throw new RuntimeException("User必须指定‘DepartCode’字段");
//                    }
//                    // T_USER表生成规则是[T_USER_用户所属部门编码]
//                    return tableName + user.getDepartCode().toUpperCase();
//                }
//            });
//        }};// 上面的代码实现了只对T_USER表生成新表
//        myDynamicTableNameInnerInterceptor.setTableNameHandlerMap(map);
//        interceptor.addInnerInterceptor(myDynamicTableNameInnerInterceptor);

        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor()); // 乐观锁插件
        return interceptor;
    }
}
