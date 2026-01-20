package com.example.Webapp.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.Webapp.DAO.UserMapper;
import com.example.Webapp.Model.Article;
import com.example.Webapp.Model.Label;
import com.example.Webapp.Model.User;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
@WebServlet("/MyPage")
public class MyPageController extends HttpServlet {

    private static final long serialVersionUID = -6491209416242259377L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        String account = null;
        String ContentType = req.getContentType();

        // 使用try-with-resources确保SqlSession正确关闭
        try (SqlSession sqlSession = com.example.Webapp.Utils.MybatisUtils.getSqlSession()) {
            if (ContentType != null && ContentType.contains("application/json")) {
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
                System.out.println("数据为空嘛？"+account);

                List<Article> articles = article(sqlSession, account);
                List<Label> labels = new ArrayList<>();

                //根据article_id查找label表信息
                for (Article article : articles) {
                    int articleId = article.getArticle_id();
                    int labelid = labelId(sqlSession, articleId);
                    System.out.println("Label标签:"+labelid);

                    Label label = selectlabel(sqlSession, labelid);
                    System.out.println(label);
                    labels.add(label);
                }

                // 将 articles 和 labels 封装到一个 JSON 对象中
                JSONObject responseJson = new JSONObject();
                responseJson.put("success", true);
                responseJson.put("articles", articles);
                responseJson.put("labels", labels);
                // 将 JSON 对象写入响应
                resp.getWriter().write(responseJson.toJSONString());
            } else {
                JSONObject responseJson = new JSONObject();
                responseJson.put("suceess", false);
                responseJson.put("message", "获取信息失败");
                resp.getWriter().write(responseJson.toJSONString());
            }
        } catch (Exception e) {
            throw new ServletException("数据库操作失败", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        try (SqlSession sqlSession = com.example.Webapp.Utils.MybatisUtils.getSqlSession()) {
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");
            System.out.println(user);

            JSONObject responseJson = new JSONObject();
            responseJson.put("isLogin", true);
            responseJson.put("avatar", user.getAvatar());
            responseJson.put("name", user.getName());
            responseJson.put("gender", user.getGender());
            responseJson.put("description", user.getDescription());
            responseJson.put("account", user.getAccount());

            resp.getWriter().write(String.valueOf(responseJson));
        } catch (Exception e) {
            throw new ServletException("数据库操作失败", e);
        }
    }

    // 修改方法签名，接收SqlSession参数
    public List<Article> article(SqlSession sqlSession, String account) {
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            return userMapper.selectArticlesByAccount(account);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int labelId(SqlSession sqlSession, int articleId){
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            return userMapper.selectlabelid(articleId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Label selectlabel(SqlSession sqlSession, int labelid){
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            return userMapper.selectlabel(labelid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}