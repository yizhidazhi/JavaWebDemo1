package com.example.Webapp.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.Webapp.DAO.UserMapper;
import com.example.Webapp.Model.Article;
import com.example.Webapp.Model.Label;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
@WebServlet("/Search")
public class SearchController extends HttpServlet {

    SqlSession sqlSession = com.example.Webapp.Utils.MybatisUtils.getSqlSession();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");

        System.out.println("I 'm  coming!");

        String content = null;
        String ContentType = req.getContentType();

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
            content = json.getString("content");

            System.out.println("data :"+content);

            //根据搜索框输入的值判断类别
            int sortId = SelectsortId(content);

            //根据类别Id获取到这一类的文章的Id
            List<Integer>articleIds = SelectArticleId(sortId);
            List<Article>articles = new ArrayList<>();
            List<Label> labels = new ArrayList<>();

            for (Integer articleId : articleIds) {
                int labelid = labelId(articleId);
                System.out.println("Label标签:"+labelid);

                Label label = selectlabel(labelid);
                Article article = selectarticle(articleId);

                System.out.println(label);
                labels.add(label);
                articles.add(article);
            }

            // 将 articles 和 labels 封装到一个 JSON 对象中
            JSONObject responseJson = new JSONObject();
            responseJson.put("success", true);
            responseJson.put("articles", articles);
            responseJson.put("labels", labels);
            // 将 JSON 对象写入响应
            resp.getWriter().write(responseJson.toJSONString());
        }else{
            JSONObject responseJson = new JSONObject();
            responseJson.put("suceess", false);
            responseJson.put("message", "获取信息失败");
            resp.getWriter().write(responseJson.toJSONString());
        }


    }

    public int SelectsortId(String content){
        int sortId = 0;
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            sortId = userMapper.selectSortId(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sortId;
    }

    public List<Integer> SelectArticleId(int sortId){
        List<Integer> articleIds = new ArrayList<>();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            articleIds = userMapper.selectArticleIds(sortId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return articleIds;
    }

    public int labelId(int articleId){
        int labelId = 0;
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            labelId = userMapper.selectlabelid(articleId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return labelId;
    }
    public Label selectlabel(int labelid){
        Label label = null;
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            label = userMapper.selectlabel(labelid);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return label;
    }

    public Article selectarticle(int Id){
       Article article = null;
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            article = userMapper.selectArticleById(Id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return article;
    }


}
