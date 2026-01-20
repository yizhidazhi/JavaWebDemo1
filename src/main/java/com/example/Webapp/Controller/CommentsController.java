package com.example.Webapp.Controller;
import com.alibaba.fastjson.JSONObject;
import com.example.Webapp.DAO.UserMapper;
import com.example.Webapp.Model.User;
import org.apache.ibatis.session.SqlSession;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/comments")
public class CommentsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        System.out.println("I'm come in  Comments!");

        int userId = Integer.parseInt(req.getParameter("userid"));
        int to_userid = Integer.parseInt(req.getParameter("to_userid"));

        JSONObject responseJson = new JSONObject();

        User user = selectUser(userId);
        System.out.println("user:"+user);

        if(to_userid!=0||user!=null){
            User to_user = selectUser(to_userid);
            System.out.println("to_user:"+to_user);
            responseJson.put("to_user", to_user);
        }else{
            responseJson.put("to_user", false);
        }
        responseJson.put("user", user);
        resp.getWriter().write(responseJson.toJSONString());

    }

    public User selectUser(int userid){
        User user = null;
        try (SqlSession sqlSession = com.example.Webapp.Utils.MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            user = userMapper.selectUser_Id(userid);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return user;
    }


}
