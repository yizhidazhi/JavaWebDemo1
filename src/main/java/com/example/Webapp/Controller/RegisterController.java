package com.example.Webapp.Controller;
import com.alibaba.fastjson.JSONObject;
import com.example.Webapp.DAO.UserMapper;
import com.example.Webapp.Model.User;
import com.example.Webapp.Utils.SnowFlakeAccounterGenerator;
import org.apache.ibatis.session.SqlSession;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
@WebServlet("/Register")
public class RegisterController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 重定向到POST方法
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        SqlSession sqlSession = null;
        Map<String, Object> res = new HashMap<>();
        try {
         sqlSession = com.example.Webapp.Utils.MybatisUtils.getSqlSession();

            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String verifyCode = req.getParameter("verifyCode");


            //利用雪花算法生成账号
            String account = SnowFlakeAccounterGenerator.generateAccount();
            //获取发送的验证码
            String sendVerifyCode = VerifyCodeController.getVerifyCode();


            //如果发送的验证码与用户输入的一致，则将数据插入数据库
            if (sendVerifyCode.equals(verifyCode)) {
                UserMapper userMapper = sqlSession.getMapper(UserMapper.class);


                User user = new User();
                user.setAccount(account);
                user.setPassword(password);
                user.setName(name);

                userMapper.insertUser(user);
                sqlSession.commit();

                res.put("success", true);
            } else {
                res.put("success", false);
            }
            resp.getWriter().write(JSONObject.toJSONString(res));
        }catch (Exception e) {
            // 打印异常日志，方便排查
            e.printStackTrace();
            // 发生异常时回滚事务
            if (sqlSession != null) {
                sqlSession.rollback();
            }
            // 返回错误信息给前端
            res.put("success", false);
            res.put("error", "服务器内部错误");
            resp.getWriter().write(JSONObject.toJSONString(res));
        }
    }

}
