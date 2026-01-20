package com.example.Webapp.Utils;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("all")
public class MybatisUtils {

    private static SqlSessionFactory sqlSessionFactory;
    // 静态代码块：在类加载时初始化，只执行一次
    static {
        try {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("初始化MyBatis失败: " + e.getMessage());
        }
    }
    // 获取SqlSession的方法
    public static SqlSession getSqlSession() {
        // openSession() 默认不会自动提交事务，需要手动 sqlSession.commit()
        // 可以重载方法，传入 true 来获取自动提交的 session: openSession(true)
        return sqlSessionFactory.openSession();
    }

}
