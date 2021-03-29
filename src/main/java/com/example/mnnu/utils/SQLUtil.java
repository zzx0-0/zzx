package com.example.mnnu.utils;

import com.example.mnnu.config.YmlConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

@Slf4j
public class SQLUtil {

//    @Autowired
//    private static YmlConfig ymlConfig;
//
//    //mysqldump -hlocalhost -P3306 -uroot -p123456 db > E:/back.sql
//    public static void backup(String host, String port, String username, String password, String databasename) throws Exception {
//        String path = ymlConfig.getUpPath() ;
//        File file = new File(path);
//        if (!file.exists()) {
//            log.info("{}已存在", file);
//            return ;
//        }
//        String sqlName = TimeUtil.getTime();
//        File datafile = new File(file + File.separator + sqlName + ".sql");
//        if (datafile.exists()) {
//            log.info(sqlName + "文件名已存在，请更换");
//            return ;
//        }
//        //拼接cmd命令
//        Process exec = Runtime.getRuntime().exec(ymlConfig.getCmd() + host + " -P" + port + " -u " + username + " -p" + password + " " + databasename + " > " + datafile);
//        if (exec.waitFor() == 0) {
//            log.info("数据库备份成功,备份路径为：" + datafile);
//        }
//    }

}
