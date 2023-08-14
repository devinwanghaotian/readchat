package top.devinwang.readChat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.devinwang.readChat.entity.AuthUser;
import top.devinwang.readChat.mapper.AuthUserMapper;
import top.devinwang.readChat.service.AuthUserService;

/**
 * @author devinWang
 * @Date 2023/6/11 12:20
 */
@Service
public class AuthUserServiceImpl implements AuthUserService {

    @Autowired
    private AuthUserMapper authUserMapper;

    /**
     * 通过 openid 获取用户数据
     * @param openid 查询用户的openid
     * @return 用户的数据 null 表示没有查询到该数据
     */
    @Override
    public AuthUser getMessageById(String openid) {
        AuthUser authUser = authUserMapper.selectByOpenId(openid);
        return authUser;
    }
}
