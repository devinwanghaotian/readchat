package top.devinwang.readChat.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.devinwang.readChat.commonutils.RedisConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wanght50855
 * @Date 2023/7/25 16:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ArticleServiceImplTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test1() {
//        redisTemplate.opsForHash().put("testHash", "1", "测试1");
//        redisTemplate.opsForHash().put("testHash", "2", "测试2");
//        redisTemplate.opsForHash().put("testHash", "3", "测试3");
//        redisTemplate.opsForHash().put("testHash", "4", "测试4");
//        redisTemplate.opsForHash().put("testHash", "5", "测试5");
//        redisTemplate.opsForHash().put("testHash", "6", "测试6");
//        redisTemplate.opsForHash().put("testHash", "7", "测试7");
//        redisTemplate.opsForHash().put("testHash", "8", "测试8");
//        redisTemplate.opsForHash().put("testHash", "9", "测试9");
//        redisTemplate.opsForHash().put("testHash", "10", 110);
//        redisTemplate.opsForHash().increment("testHash", "100", 1);
        Map<Long, Long> pageViewsMap = redisTemplate.opsForHash().entries(RedisConstant.getArticlePageViews());
        System.out.println(pageViewsMap);
    }

    @Test
    public void test2() {
//        ArrayList<String> list = new ArrayList<>();
//        list.add("2");
//        list.add("1");
//        list.add("3");
//        list.add("111");
//        List testHash = redisTemplate.opsForHash().multiGet("testHash", list);
//        Object o = testHash.get(0);
//        System.out.println((int) o);
    }
}
