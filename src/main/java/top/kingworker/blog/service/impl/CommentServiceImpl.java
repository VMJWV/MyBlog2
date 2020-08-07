package top.kingworker.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.kingworker.blog.aspect.ClearAllCache;
import top.kingworker.blog.entity.Comment;
import top.kingworker.blog.entity.Profile;
import top.kingworker.blog.mapper.CommentMapper;
import top.kingworker.blog.mapper.ProfileMapper;
import top.kingworker.blog.service.CommentService;
import top.kingworker.blog.vo.ArticleComments;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    ProfileMapper profileMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "articleComments",unless="#result == null")
    public PageInfo<ArticleComments> getArticleComments(Integer pageNum, Integer articleId) {
        PageHelper.startPage(pageNum,6);
        List<ArticleComments> articleComments = commentMapper.getArticleComments(articleId);
        PageInfo<ArticleComments> pageInfo = new PageInfo<>(articleComments);
        return  pageInfo;
    }

    @Override
    @ClearAllCache
    public boolean addComment(Comment comment) {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        comment.setUserId((Integer) principal);
        comment.setLeftTime(new Date());
        if(StringUtils.isEmpty(comment.getReplyName())){
            comment.setReplyName(null);
        }
        //防止用户通过客户端来提交不正确的表单
        if(principal != null){
            Profile profile = profileMapper.selectByPrimaryKey((Integer) principal);
            comment.setNickname(profile.getNickname());
        }
        return commentMapper.insert(comment) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "userArticleComment",unless="#result == null")
    public PageInfo<Comment> getUserArticleComment(Integer pageNum, Integer articleId) {
        Integer principal = (Integer)SecurityUtils.getSubject().getPrincipal();
        Integer userArticle = commentMapper.isUserArticle(principal,articleId);
        if(userArticle == 0){
            throw new RuntimeException("非法操作");
        }
        PageHelper.startPage(pageNum,6);
        List<Comment> comments = commentMapper.selectArticleComments(articleId);
        PageInfo<Comment> pageInfo = new PageInfo<>(comments);
        return pageInfo;
    }

    @Override
    @ClearAllCache
    public boolean deleteCommentUser(int commentId, int articleId) {
        Integer principal = (Integer)SecurityUtils.getSubject().getPrincipal();
        Integer userArticle = commentMapper.isUserArticle(principal,articleId);
        if(userArticle == 0){
            throw new RuntimeException("非法操作");
        }
        return commentMapper.deleteByPrimaryKey(commentId) > 0;
    }

    @Override
    @Cacheable(value = "articleCommentAdmin",unless="#result == null")
    public PageInfo<Comment> getArticleCommentAdmin(Integer pageNum, Integer articleId) {
        PageHelper.startPage(pageNum,6);
        List<Comment> comments = commentMapper.selectArticleComments(articleId);
        PageInfo<Comment> pageInfo = new PageInfo<>(comments);
        return pageInfo;
    }

    @Override
    @ClearAllCache
    public boolean deleteCommentAdmin(int commentId) {
        return commentMapper.deleteByPrimaryKey(commentId) > 0;
    }
}
