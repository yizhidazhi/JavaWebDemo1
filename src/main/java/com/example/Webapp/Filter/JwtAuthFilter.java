package com.example.Webapp.Filter;
import com.example.Webapp.Model.Response;
import com.example.Webapp.Utils.JwtUtils;
import com.example.Webapp.Utils.ResponseUtils;
import com.example.Webapp.Utils.UpdateTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.lang.Strings;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class JwtAuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();
        // 对登录、注册、找回密码、刷新Token和静态资源直接放行

        if(requestURI.equals("/JavaWeb/Login") ||
                requestURI.equals("/JavaWeb/SuccessServlet") ||
                requestURI.equals("/JavaWeb/Register") ||
                requestURI.equals("/JavaWeb/Verify") ||
                requestURI.equals("/JavaWeb/Findpassword") ||
                requestURI.equals("/JavaWeb/Search") ||
                requestURI.equals("/JavaWeb/blog") ||
                requestURI.equals("/JavaWeb/auth/refresh") ||
                requestURI.equals("/JavaWeb/")    ||
                requestURI.startsWith("/JavaWeb/css/") ||
                requestURI.startsWith("/JavaWeb/js/") ||
                requestURI.startsWith("/JavaWeb/Picture/") ||
                requestURI.endsWith(".css") ||
                requestURI.endsWith(".js") ||
                requestURI.endsWith(".html") ||
                requestURI.endsWith(".ico")) {

            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        Cookie accessTokenCookie = null;
        Cookie refreshTokenCookie = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessTokenCookie = cookie;
                } else if ("refreshToken".equals(cookie.getName())) {
                    refreshTokenCookie = cookie;
                }
            }
        }

        String accessToken = (accessTokenCookie != null) ? accessTokenCookie.getValue() : null;
        String refreshToken = (refreshTokenCookie != null) ? refreshTokenCookie.getValue() : null;

        System.out.println("I have got the accessToken:"+accessToken+"\n");
        System.out.println("I have got the refreshToken:"+refreshToken+"\n");

        if (!Strings.hasText(refreshToken)) {
            Response rm = new Response(401, "false", "请重新登录"); // 状态码应为401
            ResponseUtils.write(rm, response);
            return;
        }
        try {
            //accessToken和refreshToken都有效（ accessToken有效，refreshToken就肯定有效）直接放行
            JwtUtils.GetClaims(accessToken);
            System.out.println("accessToken and  refreshToken  both are  ok! "+"\n");
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ExpiredJwtException e) {
            //accessToken过期了，检查一下refreshToken过期了没
            try {
                //refreshToken没过期，对两个Token都进行更新
                JwtUtils.GetClaims(refreshToken);
                System.out.println("refreshToken are  ok but  accessToken not!need  to be refreshed! "+"\n");
                //刷新两个Token后放行
                UpdateTokenUtils.UpdateToken(request, response, refreshToken);
                doFilter(servletRequest, servletResponse, filterChain);

            } catch (ExpiredJwtException e1) {
                Response rm = new Response(401,"false","两个令牌都过期了，请重新登录");
                ResponseUtils.write(rm,response);

            }catch (Exception e1) {
                //刷新失败
                Response rm = new Response(401,"false","令牌刷新失败");
                ResponseUtils.write(rm,response);
            }
        }catch (Exception e) {
            //用accessToken令牌访问失败
            Response rm = new Response(401,"false","accessToken令牌访问失败");
            ResponseUtils.write(rm,response);
        }
    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
