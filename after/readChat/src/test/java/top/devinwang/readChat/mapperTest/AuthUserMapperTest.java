package top.devinwang.readChat.mapperTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.devinwang.readChat.entity.AuthUser;
import top.devinwang.readChat.mapper.AuthUserMapper;

import java.util.List;

/**
 * @author devinWang
 * @Date 2023/5/4 22:51
 */
// 是一个测试运行器，JUnit所有的测试方法都有测试运行器负责执行
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AuthUserMapperTest {
    @Autowired
    private AuthUserMapper userMapper;

    @Test
    public void testSelectAll() {
        List<AuthUser> authUsers = userMapper.selectAll();
        System.out.println(authUsers);
    }

    @Test
    public void testSelectByOpenId() {
        AuthUser authUser = userMapper.selectByOpenId("10");
        System.out.println(authUser);
    }

    @Test
    public void testCountOfOpenId() {
//        int count = userMapper.countOfOpenId("0");
        int count = userMapper.countOfOpenId("1");
        System.out.println("count = " + count);
    }
}
