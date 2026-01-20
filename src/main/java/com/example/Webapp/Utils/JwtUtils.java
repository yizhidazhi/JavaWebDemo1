package com.example.Webapp.Utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import java.util.Date;
import java.util.UUID;


@SuppressWarnings("all")
public class JwtUtils {

    //JWT的密钥
    private static String singKey = "yizhidazhi";//密钥
    //AccessToken 的存活时间，6分钟
    private static final Long ACCESSTOKENTIME = 360*1000L;//以毫秒为计算单位
    //RefreshToken的存活时间，1个小时
    private static final Long REFRESHTOKENTIME = 3600* 1000L;


    //生成RefreshToken的JWT令牌
    public static String GenerateRefreshToken(String account, int id) {
        String  token = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                .setId(UUID.randomUUID().toString())//唯一标识
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+REFRESHTOKENTIME))
                .claim("id",id)
                .claim("account",account)//自定义数据
                .signWith(SignatureAlgorithm.HS256, singKey)
                .compact();
        System.out.println("token:"+token);
        return  token;
    }

    //生成AccessToken的令牌
    public static String GenerateAccessToken(String account) {
        String  token = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                .setId(UUID.randomUUID().toString())//唯一标识
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ACCESSTOKENTIME))
                .claim("account",account)
                .signWith(SignatureAlgorithm.HS256, singKey)
                .compact();
        return  token;
    }

    //解析JWT令牌
    public static Claims GetClaims(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(singKey)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

}
