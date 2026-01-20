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
import java.io.IOException;
import java.io.PrintWriter;

@SuppressWarnings("/all")
@WebServlet("/write")
public class WriteBlogController extends HttpServlet {

    private static final long serialVersionUID = -6475470664131054966L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应格式为 JSON
        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        System.out.println("I'm  coming  in Write!");

        PrintWriter out = resp.getWriter();
        JSONObject result = new JSONObject();

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        String account  = user.getAccount();
        // 获取通过 FormData 发送的参数
        String title = req.getParameter("title");
        String content = req.getParameter("content");


        //将文章数据导入到数据库中
        int articleId = InsertArticle(account, title, content);
        int commentsId =articleId;
        //生成标签的描述
         if(content.codePointCount(0, content.length()) > 100) {
             int end = content.offsetByCodePoints(0, 100);
             content = content.substring(0,end)+".......";
         }
        int labelId = InsertLabel(title, content);
        InsertArticle_Label(articleId, labelId);
        InsertArticle_Comments(articleId,commentsId);


        // 构造响应数据
        result.put("code", 200);
        result.put("message", "文章发布成功");
        out.print(result.toJSONString());
        out.flush();
        out.close();
    }


    public int  InsertArticle(String  account, String title, String content) {
        Article article = new Article();
        try {
            SqlSession sqlSession = com.example.Webapp.Utils.MybatisUtils.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            article.setAccount(account);
            article.setArticle_content(content);
            article.setArticle_title(title);
            userMapper.insertArticle(article);

            sqlSession.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return article.getArticle_id();
    }
    public int  InsertLabel(String title, String content) {
        Label label = new Label();
        try {
            SqlSession sqlSession = com.example.Webapp.Utils.MybatisUtils.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            label.setLabel_name(title);
            label.setLabel_description(content);
            userMapper.insertLabel(label);
            sqlSession.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return label.getLabel_id();
    }

    public int  InsertArticle_Label(int articleId, int labelId) {
        int count = 0;
        try {
            SqlSession sqlSession = com.example.Webapp.Utils.MybatisUtils.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            count = userMapper.insertArticle_Label(articleId,labelId);
            sqlSession.commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return count;
    }

    public int  InsertArticle_Comments(int articleId, int commentsId) {
        int count = 0;
        try {
            SqlSession sqlSession = com.example.Webapp.Utils.MybatisUtils.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            count = userMapper.insertArticle_Comments(articleId,commentsId);
            sqlSession.commit();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return count;
    }

}


