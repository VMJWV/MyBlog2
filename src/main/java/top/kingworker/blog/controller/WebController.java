package top.kingworker.blog.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import top.kingworker.blog.entity.VisitHistory;
import top.kingworker.blog.service.VisitRecordService;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 用来负责转发简单的页面请求
 * 和一些都需要用到的东西
 */
@Controller
public class WebController {

    @Value("${image.path:none}")
    String baseImgPath;

    @Autowired
    VisitRecordService visitRecordService;

    @RequestMapping(value = {"/admin","/admin/index"})
    public ModelAndView adminIndex(@RequestParam(required = false,defaultValue = "0")Integer pageNum){
        ModelAndView modelAndView = new ModelAndView("admin/admin-index");
        PageHelper.startPage(pageNum,8);
        List<VisitHistory> allVisitHistory = visitRecordService.getAllVisitHistory();
        PageInfo<VisitHistory> pageInfo = new PageInfo<>(allVisitHistory);
        modelAndView.addObject("visitHistorys",allVisitHistory);
        modelAndView.addObject("pageInfo",pageInfo);
        return modelAndView;
    }

    @RequestMapping("/admin/deleteHistory")
    public String deleteHistory(@RequestParam(required = false,defaultValue = "1")Integer pageNum,
                                Integer historyId){
        visitRecordService.deleteVisitHistory(historyId);
        return "redirect:/admin/index?pageNum=" + pageNum;
    }

    /**
     * 文件上传的Mapping
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping("/picture/add")
    public Map<String, Object> pictureUpload(@RequestParam("editormd-image-file") MultipartFile file) {
        //改用map 为了支持editormd的文件上传
        Map<String, Object> map = new HashMap<>();
        if (file.isEmpty()) {
            map.put("success", 0);
            map.put("message", "file empty!");
            return map;
        }
        try {
            //这么写是可以的
            //String filePath = this.getClass().getClassLoader().getResource("").getPath();
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (!"jpg".equalsIgnoreCase(suffixName) && !"png".equalsIgnoreCase(suffixName)) {
                map.put("failure", 0);
                map.put("message", "file error!");
                return map;
            }
            String filePath;
            String webFilePath;
            if("none".equals(baseImgPath)){
                filePath =  ResourceUtils.getURL("classpath:").getPath() + "/static/";
                webFilePath = "/images/" + UUID.randomUUID().toString().substring(0, 3)
                        + new Date().getTime() + "." + suffixName;
            }
            else{
                filePath = baseImgPath;
                webFilePath = "/myimages/" + UUID.randomUUID().toString().substring(0, 3)
                        + new Date().getTime() + "." + suffixName;
            }
            file.transferTo(new File(filePath, webFilePath));
            map.put("success", 1);
            map.put("message", "upload success!");
            map.put("url", webFilePath);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            map.put("success", 0);
            map.put("message", "failure");
            return map;
        }
    }
}
