package top.kingworker.blog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.kingworker.blog.entity.Comment;
import top.kingworker.blog.vo.ArticleComments;

@Repository
public interface CommentMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    Comment selectByPrimaryKey(Integer id);

    List<Comment> selectAll();

    int updateByPrimaryKey(Comment record);

    List<ArticleComments> getArticleComments(Integer articleId);

    List<Comment> selectArticleComments(Integer articleId);

    Integer isUserArticle(@Param("userId") Integer userId,@Param("articleId") Integer articleId);
}