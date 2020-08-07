package top.kingworker.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.kingworker.blog.aspect.ClearAllCache;
import top.kingworker.blog.entity.Article;
import top.kingworker.blog.mapper.ArticleLabelsMapper;
import top.kingworker.blog.mapper.ArticleMapper;
import top.kingworker.blog.service.ArticleService;
import top.kingworker.blog.utils.MdtoHTML;
import top.kingworker.blog.vo.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(rollbackFor = Exception.class)
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    ArticleLabelsMapper articleLabelsMapper;


    @Override
    @ClearAllCache
    public void addArticle(Article article,String labelStr) {
        Integer userId = (Integer) SecurityUtils.getSubject().getPrincipal();
        article.setUserId(userId);
        article.setCreateTime(new Date());
        article.setLastEditTime(new Date());
        article.setViewCount(1);
        article.setTop(false);
        articleMapper.insert(article);
        addArticleLabels(article.getId(),labelStr);
    }

    @Override
    @ClearAllCache
    public int deleteArticle(Integer articleId) {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        return articleMapper.deleteArticleUser(articleId, (Integer) principal);
    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "userArticleById",unless="#result == null")
    public Article queryUserArticleById(Integer articleId) {
        Integer userId = (Integer) SecurityUtils.getSubject().getPrincipal();
        return articleMapper.queryUserArticleById(articleId,userId);
    }

    @Override
    @ClearAllCache
    public boolean updateArticleUser(Article article, String labelStr) {
        Integer userId = (Integer) SecurityUtils.getSubject().getPrincipal();
        article.setLastEditTime(new Date());
        List<Integer> collect = Arrays.stream(labelStr.split(",")).
                map(val -> Integer.parseInt(val)).collect(Collectors.toList());
        articleLabelsMapper.deleteLabels(article.getId());
        articleLabelsMapper.insertBatch(article.getId(),collect);
        return articleMapper.updateByPrimaryKeyUser(article,userId) > 0;
    }

    @Override
    @ClearAllCache
    public void addArticleLabels(Integer articleId, String labelStr) {
        if (StringUtils.isEmpty(labelStr)){
            return;
        }
        List<Integer> list = Arrays.stream(labelStr.split(",")).
                map(val -> Integer.parseInt(val)).collect(Collectors.toList());
        articleLabelsMapper.insertBatch(articleId,list);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "articleDetail",unless="#result == null")
    public ArticleDetail queryArticleByPk(int articleId) {
        ArticleDetail article = articleMapper.queryArticleById(articleId);
        article.setContent(MdtoHTML.markdownToHtmlExtensions(article.getContent()));
        return article;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "pageArticles",unless="#result == null")
    public PageInfo<PageArticle> selectPageArticle(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<PageArticle> pageArticle = articleMapper.selectPageArticle();
        PageInfo<PageArticle> pageInfo = new PageInfo<>(pageArticle);
        return pageInfo;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "pageArticlesUser",unless="#result == null")
    public PageInfo<PageArticle> selectPageArticleUser(int pageNum, int pageSize,int userId) {
        PageHelper.startPage(pageNum,pageSize);
        List<PageArticle> pageArticle = articleMapper.selectPageArticleByUserId(userId);
        PageInfo<PageArticle> pageInfo = new PageInfo<>(pageArticle);
        return pageInfo;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "articleTitle",unless="#result == null")
    public ArticleTitle getArticleTitle(Integer articleId) {
        return articleMapper.getArticleTitle(articleId);
    }

    @Override
    @ClearAllCache
    public void setArticleTop(boolean isTop, Integer articleId) {
        articleMapper.setTop(isTop,articleId);
    }

    @Override
    @Cacheable(value = "hotLbales",unless="#result == null")
    public PageInfo<HotLabel> getHotLabels(int pageNum) {
        PageHelper.startPage(pageNum,6);
        List<HotLabel> hotLabels = articleLabelsMapper.getHotLabels();
        PageInfo<HotLabel> pageInfo = new PageInfo<>(hotLabels);
        return pageInfo;
    }

    @Override
    public List<HotArticle> getHotArticles() {
        return articleMapper.getHotArticles();
    }

    @Override
    @Cacheable(value = "searchResult",unless="#result == null")
    public PageInfo<SearchArticle> searchByCondition(Integer pageNum,String condition) {
        PageHelper.startPage(pageNum,5);
        List<SearchArticle> result = articleMapper.getSearchResult(condition);
        PageInfo<SearchArticle> pageInfo = new PageInfo<>(result);
        return pageInfo;
    }

    @Override
    @Cacheable(value = "articlesByLabelId",unless="#result == null")
    public PageInfo<PageArticle> getArticlesByLabelId(Integer pageNum, Integer labelId) {
        PageHelper.startPage(pageNum,6);
        List<PageArticle> pageArticles = articleMapper.selectPageArticleByLabelId(labelId);
        PageInfo<PageArticle> pageInfo = new PageInfo<>(pageArticles);
        return pageInfo;
    }

    @Override
    @Cacheable(value = "articleYears",unless="#result == null")
    public List<Integer> getArticleCreateYears() {
        return articleMapper.getArticleCreateYears();
    }

    @Override
    @Cacheable(value = "searchPage",unless="#result == null")
    public PageInfo<SearchArticle> getArticlesByYear(Integer pageNum, Integer year) {
        PageHelper.startPage(pageNum,6);
        List<SearchArticle> articles = articleMapper.getArticlesByYear(year);
        PageInfo<SearchArticle> pageInfo = new PageInfo<>(articles);
        return pageInfo;
    }

    @Override
    public void addViewCount(Integer articleId) {
        articleMapper.addViewCount(articleId);
    }

    @Override
    public Integer getViewCountByArticleId(Integer articleId) {
        return articleMapper.getViewCountByArticleId(articleId);
    }

    @Override
    public List<Integer> getViewCounts(List<Integer> articleIds) {
        return articleMapper.getViewCounts(articleIds);
    }

}
