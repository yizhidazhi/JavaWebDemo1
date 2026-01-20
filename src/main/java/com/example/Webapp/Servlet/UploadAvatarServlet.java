package com.example.Webapp.Servlet;

import com.alibaba.fastjson.JSONObject;
import com.example.Webapp.DAO.UserMapper;
import com.example.Webapp.Model.User;
import com.example.Webapp.Utils.JwtUtils;
import com.example.Webapp.Utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet("/Avatar")
@MultipartConfig
public class UploadAvatarServlet extends HttpServlet {
    //服务器图片的储存路径
    private static final String  AVATAR_SAVE_PATH = "D:\\Javacode\\JavaWebDemo1\\src\\main\\webapp\\Picture";

    //前端访问路径
    private static final String AVATAR_FRONT_PATH = "/Picture/";

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


            if (token == null||JwtUtils.GetClaims(token)==null) {
               jsonObject.put("success", false);
               jsonObject.put("message","请先登录");
               resp.getWriter().write(jsonObject.toJSONString());
               return;
            }
            //获取账号作为上传头像的名称的一部分
            String account = JwtUtils.GetClaims(token).get("account").toString();
            Part filePart = req.getPart("avatar");
            if (filePart == null||filePart.getSize()==0) {

                jsonObject.put("success", false);
                jsonObject.put("message","请先选择图片文件");
                resp.getWriter().write(jsonObject.toJSONString());
                return;
            }
            //生成唯一文件名
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String extension = fileName.substring(fileName.lastIndexOf("."));
            String uniqueFileName = account +"_"+ System.currentTimeMillis() + extension;

            //保存文件到服务器
            File savDir = new File(AVATAR_SAVE_PATH);
            if (!savDir.exists()) {
                savDir.mkdir();
            }
            filePart.write(savDir.getAbsolutePath() + File.separator + uniqueFileName);

            //更新数据库中的头像URL
            String Avatarurl = AVATAR_FRONT_PATH + uniqueFileName;
            SqlSession sqlSession = MybatisUtils.getSqlSession();
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            //更新数据库中的头像的URL
            userMapper.setAvatar(Avatarurl,account);
            sqlSession.commit();
            sqlSession.close();


            //把Session中的数据也先修改了，用于实时更新
            HttpSession session = req.getSession();
            User user = (User) session.getAttribute("user");
            user.setAvatar(Avatarurl);
            session.setAttribute("user", user);

            //返回成功的信息
            jsonObject.put("success", true);
            jsonObject.put("message","头像上传成功");
            jsonObject.put("avatarUrl",Avatarurl);
            resp.getWriter().write(jsonObject.toJSONString());


        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("success", false);
            jsonObject.put("message", "上传失败：" + e.getMessage());
            resp.getWriter().write(jsonObject.toJSONString());
        }

    }


}
