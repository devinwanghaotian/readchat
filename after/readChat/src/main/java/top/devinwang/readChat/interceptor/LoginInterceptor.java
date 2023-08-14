package top.devinwang.readChat.interceptor;

import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import top.devinwang.readChat.commonutils.R;
import top.devinwang.readChat.commonutils.RedisConstant;
import top.devinwang.readChat.commonutils.ResultCode;
import top.devinwang.readChat.entity.AuthUser;
import top.devinwang.readChat.utils.JWTUtils;
import top.devinwang.readChat.utils.ThreadLocalHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 登录拦截器
 * @author devinWang
 * @Date 2023/5/24 12:04
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ThreadLocalHandler threadLocalHandler;

    /**
     * 将本次请求的用户信息删除
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        threadLocalHandler.clear();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("该请求已被登录拦截器拦截");
        String token = request.getHeader("token");
        // token 为空的情况
        if (!StringUtils.hasLength(token)) {
            handleFalseResponse(response);
            log.info("请求未包含 token 或 token 为空");
            return false;
        }
        // 解析 token
        try {
            Claims claims = JWTUtils.parseJWT(token);
            String openid = claims.get("openid", String.class);
            AuthUser user = (AuthUser) redisTemplate.opsForValue().get(RedisConstant.getUserLogin() + openid);
            if (user == null) {
                handleFalseResponse(response);
                log.info("通过redis没有查询到数据，表明该该用户登录已经过期。");
                return false;
            }
            // 延长过期时间
            redisTemplate.expire(RedisConstant.getUserLogin() + openid, 3, TimeUnit.DAYS);
            // 将用户信息存储在ThreadLocal中
            threadLocalHandler.setUser(user);
        } catch (Exception e) {
            log.info(String.valueOf(e));
            handleFalseResponse(response);
            log.info("解析token失败");
            return false;
        }
        return true;
    }

    private static void handleFalseResponse(HttpServletResponse response) throws Exception {
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        R r = R.error().code(ResultCode.getNotLoginSkip());
        response.getWriter().write(JSONUtil.toJsonStr(r));
        response.getWriter().flush();
    }
}
