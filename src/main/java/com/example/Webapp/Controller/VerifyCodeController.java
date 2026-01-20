package com.example.Webapp.Controller;
import com.alibaba.fastjson.JSONObject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebServlet("/Verify")
public class VerifyCodeController extends HttpServlet {

    private static String VerifyCode;
    public static  String  getVerifyCode() {
        return VerifyCode;
    }

    public static void setVerifyCode(String verifyCode) {
        VerifyCode = verifyCode;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        System.out.println("I'm coming !!!!!");

        req.setCharacterEncoding("utf-8");
        resp.setContentType("application/json;charset=utf-8");


          String  email = req.getParameter("email");
             VerifyCode = produce();

            System.out.println("verifyCOde:"+VerifyCode);
            try {
                SendVerificationCode(email,VerifyCode);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();//定义一个计时工具
            Runnable task = ()->setVerifyCode("");//设置任务
            scheduler.schedule(task,60, TimeUnit.SECONDS);//设置时间
            scheduler.shutdown();//关闭

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }


    public static void SendVerificationCode(String receivemail, String emailcode) throws Exception {
        // 使用固定的发件人邮箱和授权码
        String senderEmail = "2437511734@qq.com"; // 发件人的QQ邮箱
        String authorizationCode = "lacwmayxutrjeajf"; //授权码QQ邮箱授权码

        // QQ邮箱SMTP服务器配置
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, authorizationCode);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail)); // 设置发件人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receivemail)); // 设置收件人
            message.setSubject("【QQ验证码】");
            message.setText("收到验证码：" + emailcode);

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常信息
            throw new RuntimeException(e);
        }
    }

    //随机生成验证码
    public static String produce(){
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for(int i = 0;i<6;i++)
        {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
