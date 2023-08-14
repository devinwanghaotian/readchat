package top.devinwang.readChat.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * @author devinWang
 * @Date 2023/5/28 11:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class redisConfigTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void add() {
        HashMap<String, String> map = new HashMap<>();
        map.put("redis", "hello");
        redisTemplate.opsForValue().set("test", map);
    }

    @Test
    public void get() {
        HashMap<String, String> test = (HashMap<String, String>) redisTemplate.opsForValue().get("test");
        System.out.println(test);
    }
}
