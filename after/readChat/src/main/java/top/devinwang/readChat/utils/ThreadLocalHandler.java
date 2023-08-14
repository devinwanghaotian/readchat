package top.devinwang.readChat.utils;

import org.springframework.stereotype.Component;
import top.devinwang.readChat.entity.AuthUser;

/**
 * 持有用户的信息
 * @author devinWang
 * @Date 2023/6/6 9:31
 */
@Component
public class ThreadLocalHandler {
    /**
     * ThreadLocal 本质是以线程为 key 存储元素
     */
    private ThreadLocal<AuthUser> users = new ThreadLocal<>();

    public void setUser(AuthUser user) {
        users.set(user);
    }

    public AuthUser getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
