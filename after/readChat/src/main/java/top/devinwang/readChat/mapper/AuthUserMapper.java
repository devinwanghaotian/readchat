package top.devinwang.readChat.mapper;

import org.apache.ibatis.annotations.Param;
import top.devinwang.readChat.entity.AuthUser;

import java.util.List;

public interface AuthUserMapper {
    /**
     * 查询所有用户
     */
    List<AuthUser> selectAll();

    /**
     * 根据 openid 查询单个用户
     */
    AuthUser selectByOpenId(@Param("openid") String openid);

    /**
     * 查询符合 openid 的数量，0表示没有该用户，1表示有该用户
     */
    int countOfOpenId(@Param("openid") String openid);

    /**
     * 如果有该数据就插入，没这数据就更新
     */
    void insertOrUpdate(AuthUser authUser);
}