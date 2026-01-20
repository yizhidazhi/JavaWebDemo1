package com.example.Webapp.Servlet;

import com.alibaba.fastjson.JSONObject;
import com.example.Webapp.DAO.UserMapper;
import com.example.Webapp.Model.User;
import com.example.Webapp.Utils.JwtUtils;
import com.example.Webapp.Utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/resource")
public class UploadResourceServelet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        JSONObject jsonObject = new JSONObject();
        try {
            String token = null;
            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("accessToken")) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }

            if (token == null || JwtUtils.GetClaims(token) == null) {
                jsonObject.put("success", false);
                jsonObject.put("message", "请先登录");
                resp.getWriter().write(jsonObject.toJSONString());
                return;
            }

            String account = JwtUtils.GetClaims(token).get("account").toString();

            String name = req.getParameter("name");
            String gender = req.getParameter("gender");
            String description = req.getParameter("description");


            //把Session中的数据也先修改了，用于实时更新
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");
            user.setName(name);
            user.setDescription(description);
            user.setGender(gender);

            System.out.println("My page‘s  data has  been  transmission :"+user);
            session.setAttribute("user", user);


            SqlSession sqlSession = MybatisUtils.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            //更新数据库中的名字、个性标签等数据
            userMapper.setresource(name,gender,description,account);
            sqlSession.commit();
            sqlSession.close();

            //返回成功的信息
            jsonObject.put("success", true);
            jsonObject.put("message","个人资料上传成功");
            resp.getWriter().write(jsonObject.toJSONString());

        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("message", "上传失败：" + e.getMessage());
            resp.getWriter().write(jsonObject.toJSONString());
        }

    }


}
