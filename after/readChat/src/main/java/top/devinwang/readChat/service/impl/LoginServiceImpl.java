package top.devinwang.readChat.service.impl;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.devinwang.readChat.commonutils.ReadChatException;
import top.devinwang.readChat.commonutils.RedisConstant;
import top.devinwang.readChat.commonutils.ResultCode;
import top.devinwang.readChat.config.RedisConfig;
import top.devinwang.readChat.entity.AuthUser;
import top.devinwang.readChat.model.WXMessage;
import top.devinwang.readChat.utils.HttpRequest;
import top.devinwang.readChat.utils.JWTUtils;
import top.devinwang.readChat.commonutils.R;
import top.devinwang.readChat.mapper.AuthUserMapper;
import top.devinwang.readChat.service.LoginService;
import top.devinwang.readChat.utils.WxDecode;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author devinWang
 * @Date 2023/5/27 11:35
 */
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthUserMapper authUserMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WXMessage wxMessage;
    /**
     * 判断是否需要进行登录
     *
     * @param request HTTP请求
     * @return true 表示不需要登录，false 表示需要进行登录
     */
    @Override
    public boolean isAuth(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (!StringUtils.hasLength(token)) {
            log.info("请求头为空，未进行登录");
            return false;
        }
        try {
            Claims claims = JWTUtils.parseJWT(token);
            // 判断数据库中是否有该openid
            String openid = claims.get("openid", String.class);
            int count = authUserMapper.countOfOpenId(openid);
            if (count == 0) {
                log.info("进行token验证得出的openid无效：token = [{}], openid = [{}]", token, openid);
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * 登录操作
     *
     * @param code 微信登录需要的字符串
     */
    @Override
    public R login(String code) {
        if (code == null || code.length() == 0) {
            return R.error().message("code不可以为空...");
        }
        //授权（必填）
        String grant_type = "authorization_code";

        // 向微信小程序发送请求
        // 请求参数
        String params = "appid=" + wxMessage.getAppid() + "&secret=" + wxMessage.getSecret() + "&js_code=" + code + "&grant_type=" + grant_type;
        // 发送请求
        String sr = HttpRequest.sendGet("https://api.weixin.qq.com/sns/jscode2session", params);
        // 解析相应内容（转换成json对象）
        JSONObject json = JSONObject.parseObject(sr);
        // 获取会话密钥（session_key）
        String sessionKey = json.get("session_key").toString();
        // 用户的唯一标识（openid）
        String openid = (String) json.get("openid");
        Map<String, Object> map = new HashMap<>();
        map.put("openid", openid);
        map.put("sessionKey", sessionKey);
        String token = JWTUtils.generateJwt(map);
        return R.ok().data("token", token);
    }

    /**
     * 获取用户数据
     */
    @Override
    public R getMessage(HttpServletRequest request, String encryptedData, String iv) {
        if (!StringUtils.hasLength(encryptedData) || !StringUtils.hasLength(iv)) {
            return R.error().message("传输的值为空...");
        }

        // jwt解码获取sessionKey 和 openid
        String token = request.getHeader("token");
        Claims claims = null;
        try {
            claims = JWTUtils.parseJWT(token);
        } catch (Exception e) {
            throw new ReadChatException(ResultCode.getError(), "登录失败...");
        }
        String sessionKey = claims.get("sessionKey", String.class);
        String openid = claims.get("openid", String.class);

        // 查询redis看看是否有用户数据，有就直接返回
        AuthUser authUser = (AuthUser) redisTemplate.opsForValue().get(RedisConstant.getUserLogin() + openid);
        if (Objects.isNull(authUser)) {
            String result = WxDecode.decryptS5(encryptedData, "UTF-8", sessionKey, iv);
            authUser = JSONObject.parseObject(result, AuthUser.class);
            assert authUser != null;
            authUser.setOpenid(openid);
            authUserMapper.insertOrUpdate(authUser);
        }
        authUser.setOpenid(openid);

        // 将用户数据传入redis中
        redisTemplate.opsForValue().set(RedisConstant.getUserLogin() + openid, authUser, 3, TimeUnit.DAYS);
        return R.ok().data("userInfo", authUser);
    }

    @Override
    public void logout(HttpServletRequest request) {
        String token = request.getHeader("token");
        try {
            Claims claims = JWTUtils.parseJWT(token);
            String openid = claims.get("openid", String.class);
            redisTemplate.delete(RedisConstant.getUserLogin() + openid);
        } catch (Exception e) {
            log.info(String.valueOf(e));
            log.info("解析token出现问题...");
        }
    }
}
