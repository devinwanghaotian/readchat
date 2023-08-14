package top.devinwang.readChat.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * jwt工具类
 */

@Slf4j
@Component
public class JWTUtils {
    /**
     * 过期时间
     * 3 天 = 3 * 24 * 60 * 60 * 1000L
     */
    private static long expire;
    /**
     * 密钥
     */
    private static String signKey;

    public static long getExpire() {
        return expire;
    }

    public static String getSignKey() {
        return signKey;
    }

    @Value("${jwt.expire}")
    public void setExpire(long expire) {
        JWTUtils.expire = expire;
    }

    @Value("${jwt.signKey}")
    public void setSignKey(String signKey) {
        JWTUtils.signKey = signKey;
    }

    /**
     * 生成 JWT 令牌
     * @param claims JWT第二部分负载 payload 中存储的内容
     * @return  JWT 字符串
     */
    public static String generateJwt(Map<String, Object> claims) {
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
        return jwt;
    }

    public static Claims parseJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }
}

