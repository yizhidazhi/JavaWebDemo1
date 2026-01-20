package com.example.Webapp.Controller;
import com.alibaba.fastjson.JSONObject;
import com.example.Webapp.Model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/mainScreen")
public class MainScreenController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");
        HttpSession session = req.getSession();
         User user = (User) session.getAttribute("user");
        System.out.println(user);

        JSONObject responseJson = new JSONObject();
        responseJson.put("isLogin", true);
        responseJson.put("avatar", user.getAvatar());

        resp.getWriter().write(String.valueOf(responseJson));

    }
}
