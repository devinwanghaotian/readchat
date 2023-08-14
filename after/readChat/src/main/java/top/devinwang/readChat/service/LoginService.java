package top.devinwang.readChat.service;

import top.devinwang.readChat.commonutils.R;

import javax.servlet.http.HttpServletRequest;

/**
 * @author devinWang
 * @Date 2023/5/27 11:35
 */
public interface LoginService {
    /**
     * 判断是否需要进行登录
     * @param request HTTP请求
     * @return true 表示不需要登录，false 表示需要进行登录
     */
    boolean isAuth(HttpServletRequest request);

    /**
     * 登录操作
     * @param code 微信登录需要的字符串
     */
    R login(String code);

    /**
     * 得到用户数据
     */
    R getMessage(HttpServletRequest request, String encryptedData, String iv);

    /**
     * 退出登录
     */
    void logout(HttpServletRequest request);
}
