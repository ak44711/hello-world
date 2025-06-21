package org.example.demo.common.util;


import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    //密钥字符串
    public static final String KEYSTRING = "6kjq3xGPFKBlYWslPQlVaeAbEnozo2WbNe1uR1dXAQA=";
    private static final int JWT_TTL = 60 * 60 * 1000; // ms
    private static final String JWT_ID = "JWT";


    private JwtUtil() {
    }

    //签名安全密钥
    public static SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEYSTRING));
    }


    public static Long extractUserId(String token) {
        return JSONObject.parseObject(extractClaim(token, Claims::getSubject)).getLong("id");
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {

        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    }

    private static Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public static String generateToken(String subject) {
        return Jwts.builder()
                .id(JWT_ID)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + JWT_TTL)) // 1小时后过期
                .signWith(getKey()).compact();
    }

    public static Boolean validateToken(String token, String id) {
        final Long userId = extractUserId(token);
        return id.equals(String.valueOf(userId)) && !isTokenExpired(token);

    }

}
