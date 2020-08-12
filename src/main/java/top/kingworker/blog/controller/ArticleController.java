package top.kingworker.blog.controller;

import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import top.kingworker.blog.entity.Article;
import top.kingworker.blog.entity.Profile;
import top.kingworker.blog.service.ArticleService;
import top.kingworker.blog.service.CommentService;
import top.kingworker.blog.service.ProfileService;
import top.kingworker.blog.service.VisitRecordService;
import top.kingworker.blog.vo.*;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @Autowired
    CommentService commentService;

    @Autowired
    ProfileService profileService;

    @Autowired
    VisitRecordService visitRecordService;

    @RequestMapping({"/", "/index"})
    public ModelAndView index(@RequestParam(required = false, defaultValue = "1") int pageNum) {
        ModelAndView modelAndView = new ModelAndView("index");
        PageInfo<PageArticle> pageInfo = articleService.selectPageArticle(pageNum, 6);
        List<PageArticle> list = pageInfo.getList();
        modelAndView.addObject("pageInfo", pageInfo);
        modelAndView.addObject("articles", list);
        PageInfo<HotLabel> hotLabels = articleService.getHotLabels(1);
        modelAndView.addObject("hotLabels", hotLabels.getList());
        modelAndView.addObject("hotArticles", articleService.getHotArticles());
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if(principal != null) {
            modelAndView.addObject("profile", profileService.getProfileById((Integer) principal));
        }
        modelAndView.addObject("todayVisitCount",visitRecordService.toDayVisitCount());
        modelAndView.addObject("sumVisitCount",visitRecordService.sumVisitCount());
        return modelAndView;
    }

    @RequestMapping("/article/new")
    public String addPage() {
        return "add-article";
    }

    @PostMapping("/article/add")
    public String addArticle(@Validated Article article,
                             @RequestParam String labelStr) {
        articleService.addArticle(article, labelStr);
        return "redirect:/index";
    }

    @RequestMapping("/article/edit")
    public ModelAndView editPage(int articleId) {
        Article article = articleService.queryUserArticleById(articleId);
        ModelAndView modelAndView = new ModelAndView("edit-article");
        if (article == null) {
            modelAndView.setViewName("error/error");
            return modelAndView;
        }
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @RequestMapping("/article/update")
    public String articleUpdate(@Validated Article article, String labelStr) {
        articleService.updateArticleUser(article, labelStr);
        return "redirect:/article/query";
    }

    @RequestMapping("/article/{id}")
    public ModelAndView showArticle(@PathVariable(name = "id") int articleId,
                                    @RequestParam(required = false, defaultValue = "1") int pageNum) {
        ArticleDetail article = articleService.queryArticleByPk(articleId);
        ModelAndView modelAndView = new ModelAndView("article");
        PageInfo<ArticleComments> pageInfo = commentService.getArticleComments(pageNum, articleId);
        Profile userProfile = profileService.getProfileById((Integer) SecurityUtils.getSubject().getPrincipal());
        articleService.addViewCount(articleId);
        modelAndView.addObject("viewCount",articleService.getViewCountByArticleId(articleId));
        modelAndView.addObject("article", article);
        modelAndView.addObject("pageInfo", pageInfo);
        modelAndView.addObject("comments", pageInfo.getList());
        modelAndView.addObject("profile", userProfile);
        return modelAndView;
    }

    @RequestMapping("/article/query")
    public ModelAndView articles(@RequestParam(defaultValue = "1", required = false) int pageNum,
                                 @RequestParam(defaultValue = "6", required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("articles");
        PageInfo<PageArticle> pageInfo = articleService.selectPageArticleUser(pageNum, pageSize,
                (Integer) SecurityUtils.getSubject().getPrincipal());
        modelAndView.addObject("pageInfo", pageInfo);
        modelAndView.addObject("articles", pageInfo.getList());
        return modelAndView;
    }

    @RequestMapping("/article/delete")
    public String deleteArticle(Integer articleId) {
        articleService.deleteArticle(articleId);
        return "redirect:/article/query";
    }

    @RequestMapping("/admin/article/query")
    public ModelAndView queryAllArticles(@RequestParam(defaultValue = "1", required = false) int pageNum,
                                         @RequestParam(defaultValue = "6", required = false) int pageSize) {
        ModelAndView modelAndView = new ModelAndView("admin/admin-articles");
        PageInfo<PageArticle> pageInfo = articleService.selectPageArticle(pageNum, pageSize);
        modelAndView.addObject("pageInfo", pageInfo);
        modelAndView.addObject("articles", pageInfo.getList());
        return modelAndView;
    }

    @RequestMapping("/admin/article/setTop")
    public String setTop(boolean isTop, Integer articleId) {
        articleService.setArticleTop(isTop, articleId);
        return "redirect:/admin/article/query";
    }

    @ResponseBody
    @RequestMapping("/article/search")
    public PageInfo<SearchArticle> searchArticle(String condition,
                                                 @RequestParam(defaultValue = "0", required = false) Integer pageNum) {
        return articleService.searchByCondition(pageNum, condition);
    }

    @RequestMapping("/archives")
    public ModelAndView archives(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(required = false, defaultValue = "-1") Integer year) {
        ModelAndView modelAndView = new ModelAndView("archives");
        if(year == -1){
            year = Calendar.getInstance().get(Calendar.YEAR);
        }
        List<Integer> years = articleService.getArticleCreateYears();
        if(years == null || years.size() == 0){
            return modelAndView;
        }
        if(!years.contains(year)){
            throw new RuntimeException("非法传参");
        }
        PageInfo<SearchArticle> pageInfo = articleService.getArticlesByYear(pageNum, year);
        modelAndView.addObject("years",years);
        modelAndView.addObject("currentYear",year);
        modelAndView.addObject("articles",pageInfo.getList());
        modelAndView.addObject("pageInfo",pageInfo);
        return modelAndView;
    }

}
