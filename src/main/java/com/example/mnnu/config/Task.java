package com.example.mnnu.config;

import com.example.mnnu.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
@EnableScheduling
public class Task {

//    @Scheduled(fixedRate = 3000)     //每3秒执行一次
//    public void task1(){
//    }

    //      http://cron.qqe2.com
    @Scheduled(cron = "0 0 3 * * ? ")    //每天凌晨3点执行
    public void dump() throws Exception {
        log.info("备份数据库");
        backup("47.107.92.114", "3306", "root", "qwert111@ZZX", "mnnu");
    }


    @Autowired
    private YmlConfig ymlConfig;

    private void backup(String host, String port, String username, String password, String databasename) throws Exception {
        String path = ymlConfig.getUpPath() ;
        File file = new File(path);
        if (!file.exists()) {
            log.info("{}已存在", file);
            return ;
        }
        String sqlName = TimeUtil.getTime();
        File datafile = new File(file + File.separator + sqlName + ".sql");
        if (datafile.exists()) {
            log.info(sqlName + "文件名已存在，请更换");
            return ;
        }
        //拼接cmd命令
        Process exec = Runtime.getRuntime().exec(ymlConfig.getCmd() + host + " -P" + port + " -u " + username + " -p" + password + " " + databasename + " > " + datafile);
        if (exec.waitFor() == 0) {
            log.info("数据库备份成功,备份路径为：" + datafile);
        }
    }
}
