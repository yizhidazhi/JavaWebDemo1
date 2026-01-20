package com.example.Webapp.DAO;
import com.example.Webapp.Model.Article;
import com.example.Webapp.Model.Comment;
import com.example.Webapp.Model.Label;
import com.example.Webapp.Model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@SuppressWarnings("All")
public interface UserMapper {
    //查找账号
    @Select("SELECT * FROM user WHERE account = #{account} AND password = #{password}")
    User selectUser(@Param("account") String account, @Param("password") String password);

    @Select("SELECT * FROM user WHERE userid = #{userid}")
    User selectUser_Id(@Param("userid") int userid);

    //添加账号
    @Insert("INSERT INTO user (account, password, name, gender, avatar, description) VALUES (#{account}, #{password}, #{name}, null, null, null)")
    @Options(useGeneratedKeys = true, keyProperty = "userid", keyColumn = "userid")
    int insertUser(User user);

    //修改账号密码
    @Update("UPDATE user SET password = #{password} WHERE account = #{account}")
    int updateUser(@Param("account") String account, @Param("password") String password);


    @Delete("DELETE FROM user WHERE account = #{account}")
    int deleteUser(@Param("account") String account);

    //设置、更新头像
    @Update("UPDATE user SET avatar = #{avatar} WHERE account = #{account}")
    int setAvatar(@Param("avatar")String avatar,@Param("account") String account);

    //设置、更新个人数据
    @Update("UPDATE user SET  name = #{name} , gender = #{gender}, description = #{description} WHERE account = #{account}")
    int setresource(@Param("name")String name, @Param("gender")String gender, @Param("description")String description, @Param("account") String account);

    // 根据ID查询文章
    @Select("SELECT " +
            "article_id, " +
            "account, " +
            "article_title, " +
            "CAST(article_content AS CHAR) AS article_content, " + // BLOB 转 String
            "article_view, " +
            "article_comments_count, " +
            "article_like, " +
            "article_date " +
            "FROM article WHERE article_id = #{id}")
    @Results({
            @Result(column = "article_id", property = "article_id"),
            @Result(column = "account", property = "account"),
            @Result(column = "article_title", property = "article_title"),
            @Result(column = "article_content", property = "article_content"), // 映射转换后的字符串
            @Result(column = "article_view", property = "article_view"),
            @Result(column = "article_comments_count", property = "article_comments_count"),
            @Result(column = "article_like", property = "article_like"), // 对应修正后的 setter 方法
            @Result(column = "article_date", property = "article_date")
    })
    Article selectArticleById(@Param("id") int id);

    // 根据用户账号查询文章
    @Select("SELECT " +
            "article_id, " +
            "account, " +
            "article_title, " +
            "CAST(article_content AS CHAR) AS article_content, " + // BLOB 转 String
            "article_view, " +
            "article_comments_count, " +
            "article_like, " +
            "article_date " +
            "FROM article WHERE account = #{account} ORDER BY article_date DESC")
    @Results({
            @Result(column = "article_id", property = "article_id"),
            @Result(column = "account", property = "account"),
            @Result(column = "article_title", property = "article_title"),
            @Result(column = "article_content", property = "article_content"), // 映射转换后的字符串
            @Result(column = "article_view", property = "article_view"),
            @Result(column = "article_comments_count", property = "article_comments_count"),
            @Result(column = "article_like", property = "article_like"), // 对应修正后的 setter 方法
            @Result(column = "article_date", property = "article_date")
    })
    List<Article> selectArticlesByAccount(@Param("account") String account);

    // 查询所有文章（分页）
    @Select("SELECT * FROM article ORDER BY article_date DESC LIMIT #{offset}, #{pageSize}")
    List<Article> selectAllArticles(@Param("offset") int offset, @Param("pageSize") int pageSize);

    // 插入新文章
    @Insert("INSERT INTO article (account, article_title, article_content, article_view, article_comments_count, article_like, article_date) " +
            "VALUES (#{account}, #{article_title}, #{article_content}, 0, 0, 0, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "article_id", keyColumn = "article_id")
    int insertArticle(Article param); // 传入对象，而非 @Param


    // 更新文章浏览量
    @Update("UPDATE article SET article_view = article_view + 1 WHERE article_id = #{id}")
    int updateArticleView(@Param("id") int id);

    // 更新文章评论数
    @Update("UPDATE article SET article_comments_count = article_comments_count + #{count} WHERE article_id = #{id}")
    int updateArticleCommentsCount(@Param("id") Long id, @Param("count") int count);

    // 更新文章点赞数
    @Update("UPDATE article SET article_like = article_like + 1 WHERE article_id = #{id}")
    int updateArticleLike(@Param("id") int id);

    // 更新文章内容
    @Update("UPDATE article SET article_title = #{title}, article_content = #{content} WHERE article_id = #{id}")
    int updateArticleContent(@Param("id") Long id,
                             @Param("title") String title,
                             @Param("content") String content);
    // 删除文章
    @Delete("DELETE FROM article WHERE article_id = #{id}")
    int deleteArticle(@Param("id") int id);

    //根据article_id查找label_id
    @Select("SELECT label_id FROM article_label WHERE article_id = #{articleId}")
    int selectlabelid(@Param("articleId") int articleId);

    //根据article_id查找comments_section_id
    @Select("SELECT comments_section_id FROM article_comments WHERE article_id = #{articleId}")
    int selectcommentId(@Param("articleId") int articleId);


    //根据labelid查找label表的全部信息
    @Select("SELECT " +
            "label_id, " +
            "label_name, " +
            "CAST(label_description AS CHAR) AS label_description " + // BLOB 转 String
            "FROM label WHERE label_id = #{labelid}")
    @Results({
            @Result(column = "label_id", property = "label_id"),
            @Result(column = "label_name", property = "label_name"),
            @Result(column = "label_description", property = "label_description"),
    })
    Label selectlabel(@Param("labelid") int labelid);

    //插入新label
    @Insert("INSERT INTO label ( label_name, label_description)" +
            "VALUES (#{label_name}, #{label_description})")
    @Options(useGeneratedKeys = true, keyProperty = "label_id", keyColumn = "label_id")
    int insertLabel(Label param); // 传入对象，而非 @Param


    //插入新的article和label的关系表数据
    @Insert("INSERT INTO article_label (article_id,label_id) VALUES (#{articleId}, #{labelId})")
    int insertArticle_Label(@Param("articleId") int articleId, @Param("labelId") int labelId);


    //根据搜索框输入查找是否包含分类表中的关键词
    @Select("SELECT sort_id FROM sort WHERE sort_description LIKE CONCAT('%',#{content},'%')")
    Integer selectSortId(@Param("content") String content);

    //查找该类别下的所有文章的id，作为一个集合返回
    @Select("SELECT article_id FROM article_sort WHERE sort_id = #{sortId} ")
    List<Integer> selectArticleIds(@Param("sortId") int sortId);

    // 根据ID查询评论区信息
    @Select("SELECT " +
            "id, " +
            "comments_section_id, " +
            "user_id, " +
            "to_user_id, " +
            "root_id, " +
            "CAST(content AS CHAR) AS content, " + // BLOB 转 String
            "create_time " +
            "FROM comments WHERE comments_section_id = #{comments_section_id} ORDER BY id DESC")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "comments_section_id", property = "comments_section_id"),
            @Result(column = "user_id", property = "user_id"),
            @Result(column = "to_user_id", property = "to_user_id"),
            @Result(column = "root_id", property = "root_id"),
            @Result(column = "content", property = "content"),
            @Result(column = "create_time", property = "create_time")
    })
    List<Comment> selectComments(@Param("comments_section_id") int comments_section_id);


    //插入新的article和评论区的关系表数据
    @Insert("INSERT INTO article_comments (article_id,comments_section_id) VALUES (#{articleId}, #{comentsId})")
    int insertArticle_Comments(@Param("articleId") int articleId, @Param("commentsId") int commentsId);

    //插入新的评论记录
    @Insert("INSERT INTO article (comments_section_id, user_id, to_user_id,root_id, content, article_date) " +
            "VALUES (#{comments_section_id}, #{user_id}, #{to_user_id}, #{root_id}, #{content}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertComments(Comment comment);
}

