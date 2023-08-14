package top.devinwang.readChat.service;

import top.devinwang.readChat.entity.AuthUser;

/**
 * @author devinWang
 * @Date 2023/6/11 12:20
 */
public interface AuthUserService {
    /**
     * 通过 openid 获取用户数据
     * @param openid 查询用户的openid
     * @return 用户的数据 null 表示没有查询到该数据
     */
    AuthUser getMessageById(String openid);
}
