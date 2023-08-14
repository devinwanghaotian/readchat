package top.devinwang.readChat.run;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import top.devinwang.readChat.mapper.MySQLConnectionMapper;

/**
 * 在项目启动时访问数据库，保持与数据库的连接
 *
 * @author wanght50855
 * @date 2023/8/7 9:15
 */
@Slf4j
@Component
public class AfterRunner implements ApplicationRunner {
    @Autowired
    private MySQLConnectionMapper connectionTask;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        connectionTask.look();
        log.info("项目启动了....");
    }
}
