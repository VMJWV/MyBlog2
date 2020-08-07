package top.kingworker.blog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.kingworker.blog.entity.Label;
import top.kingworker.blog.entity.LocalAuth;
import top.kingworker.blog.entity.Message;
import top.kingworker.blog.service.ArticleService;
import top.kingworker.blog.service.LabelService;
import top.kingworker.blog.vo.HotLabel;
import top.kingworker.blog.vo.PageArticle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LabelController {
    @Autowired
    LabelService labelService;

    @Autowired
    ArticleService articleService;

    @RequestMapping("/label/query")
    public ModelAndView labels(@RequestParam(defaultValue = "1", required = false) int pageNum,
                               @RequestParam(defaultValue = "6", required = false) int pageSize) {
        ModelAndView view = new ModelAndView("labels");
        PageInfo<Label> pageInfo = labelService.getAllLabelsByUserId(pageNum, pageSize,
                (Integer) SecurityUtils.getSubject().getPrincipal());
        view.addObject("page", pageInfo);
//        这个是多余的 但是不添加这个 thymeleaf爆一大堆红
        view.addObject("labels", pageInfo.getList());
        return view;
    }

    @ResponseBody
    @RequestMapping("/label/labels")
    public PageInfo<Label> labelsAjax(@RequestParam(defaultValue = "1", required = false) int pageNum,
                                      @RequestParam(defaultValue = "6", required = false) int pageSize) {
        PageInfo<Label> pageInfo = labelService.getAllLabelsByUserId(pageNum, pageSize,
                (Integer) SecurityUtils.getSubject().getPrincipal());
        return pageInfo;
    }

    @ResponseBody
    @PostMapping("/label/add")
    public Message uploadLabel(@Validated Label label,@RequestParam int type) {
        Subject subject = SecurityUtils.getSubject();
        Integer userId = (Integer) subject.getPrincipal();
        label.setUserId(userId);
        Message message = new Message();
        Map<String, String> map = new HashMap<>();
        if (labelService.addLabel(label)) {
            message.setCode(200);
            if(type == 2){
                message.setMessage(label.getId()+"");
            }
            else{
                message.setMessage("/label/query?pageNum=99");
            }
        } else {
            message.setCode(200);
            message.setMessage("upload failed!");
        }
        return message;
    }

    @RequestMapping("/label/delete")
    public String deleteLabel(@RequestParam(required = false, defaultValue = "1") int pageNum,
                              @RequestParam(required = false, defaultValue = "6") int pageSize,
                              @RequestParam int labelId) {
        Subject subject = SecurityUtils.getSubject();
        Integer userId = (Integer) subject.getPrincipal();
        labelService.deleteLabel(labelId, userId);
        return String.format("redirect:/label/query?pageNum=%d&pageSize=%d", pageNum, pageSize);
    }

    @ResponseBody
    @RequestMapping("/label/update")
    public Message updateLabel(@RequestParam(required = false, defaultValue = "1") int pageNum,
                               @RequestParam(required = false, defaultValue = "6") int pageSize,
                               @Validated Label label) {
        //同样的这里的labelId 到时候也从Session取的
        Subject subject = SecurityUtils.getSubject();
        Integer userId = (Integer) subject.getPrincipal();
        label.setUserId(userId);
        if (labelService.updateLabel(label)) {
            return new Message(200, String.format("/label/query?pageNum=%d&pageSize=%d", pageNum, pageSize));
        } else {
            return new Message(400, "update failed!");
        }
    }

    @RequestMapping("/admin/label/query")
    public ModelAndView labelsAdmin(@RequestParam(defaultValue = "1", required = false) int pageNum,
                               @RequestParam(defaultValue = "6", required = false) int pageSize) {
        ModelAndView view = new ModelAndView("admin/admin-labels");
        PageInfo<Label> pageInfo = labelService.getAllLabelsAdmin(pageNum,pageSize);
        view.addObject("page", pageInfo);
//        这个是多余的 但是不添加这个 thymeleaf爆一大堆红
        view.addObject("labels", pageInfo.getList());
        return view;
    }

    @RequestMapping("/admin/label/delete")
    public String deleteLabelAdmin(@RequestParam(required = false, defaultValue = "1") int pageNum,
                              @RequestParam(required = false, defaultValue = "6") int pageSize,
                              @RequestParam int labelId) {
        Subject subject = SecurityUtils.getSubject();
        Integer userId = (Integer) subject.getPrincipal();
        labelService.deleteLabelAdmin(labelId);
        return String.format("redirect:/admin/label/query?pageNum=%d&pageSize=%d", pageNum, pageSize);
    }

    @RequestMapping("/hotlabels")
    public ModelAndView hotLabels(@RequestParam(defaultValue = "1",required = false)Integer labelPageNum,
                                  @RequestParam(defaultValue = "1",required = false)Integer articlePageNum,
                                  @RequestParam(required = false,defaultValue = "-1")Integer labelId){
        ModelAndView modelAndView = new ModelAndView("hot-labels");
        PageInfo<HotLabel> hotLabels = articleService.getHotLabels(labelPageNum);
        modelAndView.addObject("labels",hotLabels.getList());
        modelAndView.addObject("pageInfo",hotLabels);
        if(hotLabels.getTotal() == 0){
            return modelAndView;
        }
        PageInfo<PageArticle> articles = null;
        if(labelId == -1){
            labelId = hotLabels.getList().get(0).getId();
            articles = articleService.getArticlesByLabelId(articlePageNum,labelId);
        }
        else{
            articles = articleService.getArticlesByLabelId(articlePageNum, labelId);
        }
        modelAndView.addObject("articles",articles.getList());
        modelAndView.addObject("pageInfo1",articles);
        modelAndView.addObject("currentLabel",labelService.selectLabelByLabelId(labelId));
        return modelAndView;
    }
}
