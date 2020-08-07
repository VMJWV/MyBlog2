package top.kingworker.blog.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.kingworker.blog.entity.Article;
import top.kingworker.blog.vo.*;

@Repository
public interface ArticleMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    Article selectByPrimaryKey(Integer id);

    List<Article> selectAll();

    int updateByPrimaryKey(Article record);

    Article queryUserArticleById(@Param("id") Integer id, @Param("userId") Integer userId);

    ArticleDetail queryArticleById(Integer id);

    int updateByPrimaryKeyUser(@Param("article") Article article, @Param("userId") Integer userId);

    List<PageArticle> selectPageArticleByUserId(int userId);

    List<PageArticle> selectPageArticle();

    int deleteArticleUser(@Param("articleId") Integer articleId, @Param("userId") Integer userId);

    ArticleTitle getArticleTitle(Integer articleId);

    int setTop(@Param("isTop") boolean isTop,@Param("articleId") int articleId);

    List<HotArticle> getHotArticles();

    List<SearchArticle> getSearchResult(String condition);

    List<PageArticle> selectPageArticleByLabelId(Integer labelId);

    List<Integer> getArticleCreateYears();

    List<SearchArticle> getArticlesByYear(Integer year);

    void addViewCount(Integer articleId);

    Integer getViewCountByArticleId(Integer articleId);

    List<Integer> getViewCounts(@Param("articleIds") List<Integer> articleIds);
}