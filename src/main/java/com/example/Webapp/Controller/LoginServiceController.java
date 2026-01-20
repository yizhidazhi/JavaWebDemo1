package com.example.Webapp.Controller;
import com.example.Webapp.DAO.UserMapper;
import com.example.Webapp.Model.User;
import com.example.Webapp.Utils.JwtUtils;
import org.apache.ibatis.session.SqlSession;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;


@SuppressWarnings("all")
@WebServlet("/Login")
public class LoginServiceController extends HttpServlet {

    SqlSession sqlSession = com.example.Webapp.Utils.MybatisUtils.getSqlSession();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        String account = null;
        String password = null;
        int id = 0;

        // 检查Content-Type来判断数据格式
        String contentType = req.getContentType();

        if (contentType != null && contentType.contains("application/json")) {
            // 解析JSON格式数据
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            JSONObject json = JSONObject.parseObject(sb.toString());
            account = json.getString("account");
            password = json.getString("password");
        } else {
            // 解析表单格式数据
            account = req.getParameter("account");
            password = req.getParameter("password");
            System.out.println("account" + account);
            System.out.println("password" + password);
        }

        User user = login(account, password);
        System.out.println(user);

        JSONObject responseJson = new JSONObject();

        if (user != null) {
            id = user.getUserid();
            String accessToken = JwtUtils.GenerateAccessToken(account);
            String refreshToken = JwtUtils.GenerateRefreshToken(account, id);

            //将 AccessToken Cookie 存储到 HttpOnly Cookie
            Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
            accessTokenCookie.setPath("/"); // 设置 Cookie 的作用域为整个应用
            accessTokenCookie.setHttpOnly(true); // 阻止 JavaScript 访问 Cookie
            // 检查请求是否是安全的，如果是，则设置 Secure 标志
            accessTokenCookie.setSecure(req.isSecure());
            accessTokenCookie.setMaxAge(360); // AccessToken 的生命周期 ，设为6分钟


            // RefreshToken Cookie
            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setPath("/");
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(req.isSecure());
            refreshTokenCookie.setMaxAge(60 * 60); // RefreshToken 的生命周期，一个小时

            resp.addCookie(accessTokenCookie);
            resp.addCookie(refreshTokenCookie);

            String avatar = user.getAvatar();
            String name = user.getName();

            //存入用户的数据到cookie中，用于其他网页的交互
            HttpSession session = req.getSession();
            session.setAttribute("user", user);


            responseJson.put("success", true);
            responseJson.put("message", "登录成功");
            responseJson.put("avatar", avatar);
            responseJson.put("redirect", "/JavaWeb/index.html");
            resp.getWriter().write(responseJson.toJSONString());
        } else {
            responseJson.put("success", false);
            responseJson.put("message", "用户名或密码错误");
            resp.getWriter().write(responseJson.toJSONString());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 重定向到POST方法
        this.doPost(req, resp);
    }

    public User login(String account,String password) {
        User user = null;
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            user = userMapper.selectUser(account,password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return user;
    }
}