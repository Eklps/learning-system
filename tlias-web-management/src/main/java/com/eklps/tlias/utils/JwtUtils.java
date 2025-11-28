package com.eklps.tlias.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Map;

public class JwtUtils {

    // 签名密钥 (随便写，但越复杂越安全，不要泄露)
    private static final String signKey = "Eclipse";

    // 过期时间 (这里设为 12 小时，单位毫秒)
    private static final Long expire = 43200000L;

    /**
     * 生成 JWT 令牌
     * @param claims 我们要存入令牌的数据 (比如用户的 ID, 名字)
     * @return String 令牌字符串
     */
    public static String generateJwt(Map<String, Object> claims){
        return Jwts.builder()
                .addClaims(claims) // 自定义载荷 (存数据)
                .signWith(SignatureAlgorithm.HS256, signKey) // 签名算法 + 密钥
                .setExpiration(new Date(System.currentTimeMillis() + expire)) // 有效期
                .compact();
    }

    /**
     * 解析 JWT 令牌
     * @param jwt 令牌字符串
     * @return Claims 解析后的数据
     */
    public static Claims parseJWT(String jwt){
        return Jwts.parser()
                .setSigningKey(signKey) // 使用相同的密钥解析
                .parseClaimsJws(jwt)
                .getBody();
    }
}