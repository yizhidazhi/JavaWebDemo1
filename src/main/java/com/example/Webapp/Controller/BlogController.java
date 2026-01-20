package com.example.Webapp.Controller;
import com.alibaba.fastjson.JSONObject;
import com.example.Webapp.DAO.UserMapper;
import com.example.Webapp.Model.Article;
import com.example.Webapp.Model.Comment;
import org.apache.ibatis.session.SqlSession;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
@WebServlet("/blog")
public class BlogController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        System.out.println("I'm come in  Blog");

       int articleId = Integer.parseInt(req.getParameter("id"));

       System.out.println("articleId:"+articleId);

       Article article = selectarticle(articleId);

        System.out.println("article:"+article);

       int commentsId = selectcomentsId(articleId);

       System.out.println("comments:"+commentsId);

        JSONObject responseJson = new JSONObject();

       if(String.valueOf(commentsId) != null) {
           List<Comment> comments = selectComments(commentsId);
           System.out.println("comments:"+comments);
           responseJson.put("comments", comments);
       }else{
           responseJson.put("comments", false);
       }
        responseJson.put("suceess", true);
        responseJson.put("article", article);

        resp.getWriter().write(responseJson.toJSONString());
    }


    public Article selectarticle(int articleId){
        Article article = null;
        try (SqlSession sqlSession = com.example.Webapp.Utils.MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            article = userMapper.selectArticleById(articleId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return article;
    }

    public int selectcomentsId(int articleId){
        int commentsId = 0;
        try (SqlSession sqlSession = com.example.Webapp.Utils.MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            commentsId = userMapper.selectcommentId(articleId);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return commentsId;
    }

    public List<Comment> selectComments(int comments_section_id){
        List<Comment> comments = new ArrayList<>();
        try (SqlSession sqlSession = com.example.Webapp.Utils.MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            comments = userMapper.selectComments(comments_section_id);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  comments;
    }
}
