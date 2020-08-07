package top.kingworker.blog.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.kingworker.blog.entity.Comment;
import top.kingworker.blog.entity.Message;
import top.kingworker.blog.mapper.CommentMapper;
import top.kingworker.blog.service.ArticleService;
import top.kingworker.blog.service.CommentService;
import top.kingworker.blog.vo.ArticleComments;
import top.kingworker.blog.vo.ArticleTitle;

import java.util.List;

@Controller
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    ArticleService articleService;


    @RequestMapping("/comment/add")
    public String addComment(@Validated Comment comment,
                             @RequestParam(required = false,defaultValue = "1")Integer pageNum){
        commentService.addComment(comment);
        return "redirect:/article/"+comment.getArticleId()+"?pageNum="+pageNum;
    }

    @RequestMapping("/comment/query")
    public ModelAndView queryComments(int articleId,
                                      @RequestParam(defaultValue = "1",required = false)Integer pageNum){
        ModelAndView modelAndView = new ModelAndView("comments");
        PageInfo<Comment> userArticleComment = commentService.getUserArticleComment(pageNum, articleId);
        ArticleTitle articleTitle = articleService.getArticleTitle(articleId);
        modelAndView.addObject("pageInfo",userArticleComment);
        modelAndView.addObject("comments",userArticleComment.getList());
        modelAndView.addObject("article",articleTitle);
        return modelAndView;
    }

    @RequestMapping("/comment/delete")
    public String deleteComment(int commentId,int articleId,
                                @RequestParam(defaultValue = "1",required = false)int pageNum){
        commentService.deleteCommentUser(commentId,articleId);

        return String.format("redirect:/comment/query?pageNum=%d&articleId=%d",pageNum,articleId);
    }

    @RequestMapping("/admin/comment/query")
    public ModelAndView amdinQueryComments(int articleId,
                                      @RequestParam(defaultValue = "1",required = false)Integer pageNum){
        ModelAndView modelAndView = new ModelAndView("admin/admin-comments");
        PageInfo<Comment> articleCommentAdmin = commentService.getArticleCommentAdmin(pageNum, articleId);
        ArticleTitle articleTitle = articleService.getArticleTitle(articleId);
        modelAndView.addObject("pageInfo",articleCommentAdmin);
        modelAndView.addObject("comments",articleCommentAdmin.getList());
        modelAndView.addObject("article",articleTitle);
        return modelAndView;
    }

    @RequestMapping("/admin/comment/delete")
    public String deleteCommentAdmin(int commentId,int articleId,
                                @RequestParam(defaultValue = "1",required = false)int pageNum){
        commentService.deleteCommentAdmin(commentId);
        return String.format("redirect:/admin/comment/query?pageNum=%d&articleId=%d",pageNum,articleId);
    }

}
