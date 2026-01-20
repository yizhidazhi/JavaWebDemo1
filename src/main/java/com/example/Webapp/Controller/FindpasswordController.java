package com.example.Webapp.Controller;
import com.alibaba.fastjson.JSONObject;
import com.example.Webapp.DAO.UserMapper;
import org.apache.ibatis.session.SqlSession;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/Findpassword")
public class FindpasswordController extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        Map<String, Object> res = new HashMap<>();
        SqlSession sqlSession = null;
        try {
            sqlSession = com.example.Webapp.Utils.MybatisUtils.getSqlSession();

            String account = req.getParameter("account");
            String password = req.getParameter("password");
            String verifyCode = req.getParameter("verifyCode");

            String sendVerifyCode = VerifyCodeController.getVerifyCode();


            System.out.println(sendVerifyCode);
            System.out.println(verifyCode);

            //如果发送的验证码与用户输入的一致，则将数据库数据更新
            if (sendVerifyCode.equals(verifyCode)) {
                UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
                userMapper.updateUser(account, password);
                sqlSession.commit();
                res.put("success", true);
            } else {
                System.out.println("I 'm  here!");
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        this.doPost(req, resp);
    }
}
