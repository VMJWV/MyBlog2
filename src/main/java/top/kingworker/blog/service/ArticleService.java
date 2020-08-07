package top.kingworker.blog.service;

import com.github.pagehelper.PageInfo;
import top.kingworker.blog.entity.Article;
import top.kingworker.blog.vo.*;

import java.util.List;

public interface ArticleService {
    void addArticle(Article article,String labelStr);
    int deleteArticle(Integer articleId);
    Article queryUserArticleById(Integer articleId);
    boolean updateArticleUser(Article article, String labelStr);
    void addArticleLabels(Integer articleId, String labelStr);
    ArticleDetail queryArticleByPk(int articleId);
    PageInfo<PageArticle> selectPageArticle(int pageNum, int pageSize);
    PageInfo<PageArticle> selectPageArticleUser(int pageNum, int pageSize,int userId);
    ArticleTitle getArticleTitle(Integer articleId);
    void setArticleTop(boolean isTop,Integer articleId);
    PageInfo<HotLabel> getHotLabels(int pageNum);
    List<HotArticle> getHotArticles();
    PageInfo<SearchArticle> searchByCondition(Integer pageNum,String condition);
    PageInfo<PageArticle> getArticlesByLabelId(Integer pageNum,Integer labelId);
    List<Integer> getArticleCreateYears();
    PageInfo<SearchArticle> getArticlesByYear(Integer pageNum,Integer year);
    void addViewCount(Integer articleId);
    Integer getViewCountByArticleId(Integer articleId);
    List<Integer> getViewCounts(List<Integer> articleIds);
}
