package top.kingworker.blog.service;

import com.github.pagehelper.PageInfo;
import top.kingworker.blog.entity.Comment;
import top.kingworker.blog.vo.ArticleComments;

import java.util.List;

public interface  CommentService {
    PageInfo<ArticleComments> getArticleComments(Integer pageNum, Integer articleId);
    boolean addComment(Comment comment);
    PageInfo<Comment> getUserArticleComment(Integer pageNum,Integer articleId);
    boolean deleteCommentUser(int commentId,int articleId);
    PageInfo<Comment> getArticleCommentAdmin(Integer pageNum,Integer articleId);
    boolean deleteCommentAdmin(int commentId);
}
