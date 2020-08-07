package top.kingworker.blog.controller;

import cn.hutool.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import top.kingworker.blog.entity.GithubToken;
import top.kingworker.blog.entity.LocalAuth;
import top.kingworker.blog.entity.Profile;
import top.kingworker.blog.entity.QqToken;
import top.kingworker.blog.permission.UserToken;
import top.kingworker.blog.service.ProfileService;
import top.kingworker.blog.utils.AuthUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Controller
public class UserController {

    @Autowired
    ProfileService profileService;

    @Autowired
    AuthUtils authUtils;

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping("/login")
    public String loginPage(){
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if(principal != null){ //这说明已经登录过了 跳转到首页
            return "redirect:/index";
        }
        return "login";
    }

    @RequestMapping("/register")
    public String registerPage(){
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if(principal != null){ //这说明已经登录过了 跳转到首页
            return "redirect:/index";
        }
        return "register";
    }

    @RequestMapping("/user/login")
    public ModelAndView login(final String loginName, final String password,
                              @RequestParam(defaultValue = "false",required = false)
                              final boolean remember){
        ModelAndView modelAndView = new ModelAndView("login");
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if(principal != null){ //这说明已经登录过了 跳转到首页
            modelAndView.setViewName("redirect:/index");
            return modelAndView;
        }
        UserToken userToken = new UserToken(loginName, password, UserToken.LoginType.NORMAL);
        try {
            userToken.setRememberMe(remember);
            subject.login(userToken);
            modelAndView.setViewName("redirect:/index");
            return modelAndView;
        } catch (UnknownAccountException uae) {
            modelAndView.addObject("msg","账户不存在");
        } catch (IncorrectCredentialsException ice) {
            modelAndView.addObject("msg","密码错误");
        } catch (AuthenticationException ae) {
            modelAndView.addObject("msg","账户异常");
        }
        return modelAndView;
    }

    @RequestMapping("/user/register")
    public ModelAndView register(@Validated LocalAuth localAuth) {
        ModelAndView modelAndView = new ModelAndView("login");
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        if(principal != null){ //这说明已经登录过了 跳转到首页
            modelAndView.setViewName("redirect:/index");
            return modelAndView;
        }
        String password = localAuth.getPassword();
        if(profileService.registerUser(localAuth)){
            UserToken token = new UserToken(localAuth.getUsername(),password, UserToken.LoginType.NORMAL);
            subject.login(token);
            modelAndView.setViewName("redirect:/index");
        }
        else{
            modelAndView.addObject("msg","注册失败 请联系管理员");
        }
        return modelAndView;
    }

    @RequestMapping("/github/callback")
    public String githubCallback(String code) throws IOException {
        String token = authUtils.getGithubAccessToken(code);
        JSONObject userInfo = authUtils.getGithubUserInfo(token);
        Subject subject = SecurityUtils.getSubject();
        String id = userInfo.get("id") + "";
        Session session = subject.getSession();
        GithubToken githubToken = new GithubToken();
        githubToken.setOpenId(id);
        githubToken.setRepositoryUrl((String) userInfo.get("html_url"));
        System.out.println(subject);
        session.setAttribute("githubToken",githubToken);
        UserToken userToken = new UserToken( id,"", UserToken.LoginType.FREE);
        try {
            userToken.setRememberMe(true);
            subject.login(userToken);
        }catch (Exception e){
            return "redirect:/bindPage";
        }
        return "redirect:/index";
    }

    @RequestMapping("/qq/callback")
    public String qqCallback(String code) throws IOException {
        String token = authUtils.getQQAccessToken(code);
        String qqOpenId = authUtils.getQqOpenId(token);
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        System.out.println(subject);
        QqToken qqToken = new QqToken();
        qqToken.setOpenId(qqOpenId);
        session.setAttribute("qqToken",qqToken);
        UserToken userToken = new UserToken( qqOpenId,"", UserToken.LoginType.FREE);
        try {
            userToken.setRememberMe(true);
            subject.login(userToken);
        }catch (Exception e){
            return "redirect:/bindPage";
        }
        return "redirect:/index";
    }

    @RequestMapping("/bindPage")
    public String bindAccount(){
        if(SecurityUtils.getSubject().getPrincipal() != null){
            return "redirect:/index";
        }
        return "quick-register";
    }

    @RequestMapping("/bind")
    public ModelAndView bind(LocalAuth localAuth) {
        ModelAndView modelAndView = new ModelAndView("redirect:/index");
        if(SecurityUtils.getSubject().getPrincipal() != null){
            return modelAndView;
        }
        Integer userId = profileService.getUserIdByEmailAndUsername(localAuth);
        if(userId == null){
            modelAndView.setViewName("quick-register");
            modelAndView.addObject("msg","输入错误 没找到对应账号 请重试");
            return modelAndView;
        }
        Session session = SecurityUtils.getSubject().getSession();
        GithubToken githubToken = (GithubToken) session.getAttribute("githubToken");
        session.removeAttribute("githubToken");
        QqToken qqToken = (QqToken) session.getAttribute("qqToken");
        session.removeAttribute("qqToken");
        try {
            if (githubToken != null) {
                githubToken.setUserId(userId);
                profileService.bindAccountGithub(githubToken);
                UserToken token = new UserToken(githubToken.getOpenId(), "", UserToken.LoginType.FREE);
                SecurityUtils.getSubject().login(token);
            } else if (qqToken != null) {
                qqToken.setUserId(userId);
                profileService.bindAccountQQ(qqToken);
                UserToken token = new UserToken(qqToken.getOpenId(), "", UserToken.LoginType.FREE);
                SecurityUtils.getSubject().login(token);
            }
        }catch (Exception e){
            e.printStackTrace();
            modelAndView.setViewName("login");
            modelAndView.addObject("msg","发生未知错误 请尝试重新登录");
        }
        return modelAndView;
    }

    @RequestMapping("/quick/register")
    public String quickRegister(Model model){
        Session session = SecurityUtils.getSubject().getSession();
        GithubToken githubToken = (GithubToken) session.getAttribute("githubToken");
        session.removeAttribute("githubToken");
        QqToken qqToken = (QqToken) session.getAttribute("qqToken");
        session.removeAttribute("qqToken");
        try {
            Profile profile = profileService.register();
            if (githubToken != null) {
                githubToken.setUserId(profile.getId());
                profileService.bindAccountGithub(githubToken);
                UserToken userToken = new UserToken(githubToken.getOpenId(), "", UserToken.LoginType.FREE);
                SecurityUtils.getSubject().login(userToken);
            }
            else if(qqToken != null){
                qqToken.setUserId(profile.getId());
                profileService.bindAccountQQ(qqToken);
                UserToken userToken = new UserToken(qqToken.getOpenId(), "", UserToken.LoginType.FREE);
                SecurityUtils.getSubject().login(userToken);
            }
            return "redirect:/index";
        }catch (Exception e){
        }
        model.addAttribute("msg","发生未知错误");
        return "redirect:/login";
    }

    @RequestMapping("/github/unbind")
    public String unbindGithub(){
        Integer userId = (Integer) SecurityUtils.getSubject().getPrincipal();
        profileService.unbindGithub(userId);
        return "redirect:/profile/"+userId;
    }

    @RequestMapping("/qq/unbind")
    public String unbindQQ(){
        Integer userId = (Integer) SecurityUtils.getSubject().getPrincipal();
        profileService.unbindQQ(userId);
        return "redirect:/profile/"+userId;
    }

    @RequestMapping("/qqLogin")
    public String qqLogin() throws UnsupportedEncodingException {
        return "redirect:"+authUtils.getQQAuthUrl();
    }

    @RequestMapping("/githubLogin")
    public String githubLogin(){
        return "redirect:"+authUtils.getGithubAuthUrl();
    }
}
