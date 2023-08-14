package top.devinwang.readChat.timer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.devinwang.readChat.mapper.MySQLConnectionMapper;

/**
 * 每10分钟访问一下数据库，防止断开连接
 *
 * @author wanght50855
 * @date 2023/8/6 10:53
 */
@Slf4j
@Component
public class MySQLConnectionTask {

    @Autowired
    private MySQLConnectionMapper connectionTask;

    @Scheduled(cron="0 */10 * * * ?")
    public void task() {
        int result = connectionTask.look();
        log.info("result = [{}]", result);
    }
}
