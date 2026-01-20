package com.example.Webapp.Utils;
import io.jsonwebtoken.Claims;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateTokenUtils {

   public static void UpdateToken(HttpServletRequest req, HttpServletResponse resp,String refreshToken) throws IOException {


        //验证RefreshToken的有效性
        if (refreshToken == null ) {
            System.out.println("refreshToken is null");
            return;
        }
        //生成新的AccessToken和RefreshToken
        Claims claims = JwtUtils.GetClaims(refreshToken);
        String account = claims.getSubject();
        int userId = claims.get("userId", Integer.class);

        String newAccessToken = JwtUtils.GenerateAccessToken(account);
        String newRefreshToken = JwtUtils.GenerateRefreshToken(account, userId);

        // 将新的Token设置到HttpOnly Cookie中，name与之前的一样，可以覆盖掉之前的Token,不需要手动清除
        Cookie accessTokenCookie = new Cookie("accessToken", newAccessToken);
        accessTokenCookie.setPath("/");//设为每一个请求都会带有accessToken
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(req.isSecure());
        accessTokenCookie.setMaxAge(360); // AccessToken的生命周期，设为6分钟

        Cookie refreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(req.isSecure());
        refreshTokenCookie.setMaxAge(60 * 60); // RefreshToken的生命周期，设为1小时

        resp.addCookie(accessTokenCookie);
        resp.addCookie(refreshTokenCookie);



    }

}