package top.devinwang.readChat.timer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 隔2秒校验异常lettuce连接是否正常，
 * 解决长期空闲lettuce连接关闭但是netty不能及时监控到的问题。
 */
@Component
@Slf4j
public class LettuceConnectionValidTask {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Scheduled(cron="0/2 * * * * ?")
    public void task() {
        if(redisConnectionFactory instanceof LettuceConnectionFactory){
            LettuceConnectionFactory c=(LettuceConnectionFactory)redisConnectionFactory;
            c.validateConnection();
        }
    }
}
