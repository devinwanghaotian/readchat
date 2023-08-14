package top.devinwang.readChat.commonutils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.devinwang.readChat.utils.JWTUtils;

/**
 * @author devinWang
 * @Date 2023/5/26 20:50
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class JWTUtilsTest {
    @Test
    public void testValue() {
        long expire = JWTUtils.getExpire();
        String signKey = JWTUtils.getSignKey();
        System.out.println("expire = " + expire);
        System.out.println("signKey = " + signKey);
    }
}
