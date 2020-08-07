package top.kingworker.blog.controller;

import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.kingworker.blog.entity.LocalAuth;
import top.kingworker.blog.entity.Message;
import top.kingworker.blog.entity.Profile;
import top.kingworker.blog.service.ProfileService;
import java.util.Set;

@Controller
public class ProfileController {

    @Autowired
    ProfileService profileService;


    @RequestMapping("/admin/profile/query")
    public ModelAndView queryProfiles(@RequestParam(defaultValue = "1", required = false) Integer pageNum) {
        ModelAndView modelAndView = new ModelAndView("admin/admin-profiles");
        PageInfo<Profile> allProfiles = profileService.getAllProfiles(pageNum);
        modelAndView.addObject("profiles", allProfiles.getList());
        modelAndView.addObject("pageInfo", allProfiles);
        modelAndView.addObject("currentId", SecurityUtils.getSubject().getPrincipal());
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping("/admin/profile/permissions")
    public Set<String> getPermissions(Integer userId) {
        return profileService.getPermissionsByUserId(userId);
    }

    @ResponseBody
    @RequestMapping("/admin/permission/update")
    public Message updatePermission(String permsStr, Integer userId) {
        if (profileService.insertPermissions(userId, permsStr) > 0) {
            return new Message(200, "update succeed ");
        }
        return new Message(400, "update failed");
    }

    @RequestMapping("/admin/profile/edit")
    public ModelAndView editProfile(Integer userId) {
        ModelAndView modelAndView = new ModelAndView("admin/admin-profile");
        Profile profile = profileService.getProfileById(userId);
        modelAndView.addObject("profile", profile);
        return modelAndView;
    }

    @RequestMapping("/admin/profile/update")
    public String updateProfile(Profile profile) {
        profileService.updateProfile(profile);
        return "redirect:/admin/profile/query";
    }

    @RequestMapping("/admin/profile/delete")
    public String deleteProfile(Integer userId){
        profileService.deleteProfile(userId);
        return "redirect:/admin/profile/query";
    }

    @RequestMapping("/profile/{id}")
    public ModelAndView profile(@PathVariable("id") Integer userId,
                                @RequestParam(required = false,defaultValue = "0")boolean flag) {
        ModelAndView modelAndView = new ModelAndView("infocenter");
        Profile profile = profileService.getProfileById(userId);
        if (profile == null) {
            throw new RuntimeException("Not Found");
        }
        if(flag){
            modelAndView.addObject("visitWriterPage",flag);
        }
        modelAndView.addObject("profile", profile);
        modelAndView.addObject("currentId", SecurityUtils.getSubject().getPrincipal());
        return modelAndView;
    }

    @RequestMapping("/profile/query")
    public ModelAndView profileQuery() {
        ModelAndView modelAndView = new ModelAndView("infocenter");
        Profile profile = profileService.getProfileById((Integer) SecurityUtils.getSubject().getPrincipal());
        modelAndView.addObject("profile", profile);
        modelAndView.addObject("currentId", SecurityUtils.getSubject().getPrincipal());
        return modelAndView;
    }

    @RequestMapping("/profile/edit")
    public ModelAndView profileEdit() {
        ModelAndView modelAndView = new ModelAndView("profile");
        Profile profile = profileService.getProfileById((Integer) SecurityUtils.getSubject().getPrincipal());
        modelAndView.addObject("profile", profile);
        return modelAndView;
    }

    @RequestMapping("/profile/update")
    public String profileUpdate(Profile profile) {
        profileService.updateProfileUser(profile);
        return "redirect:/profile/query";
    }

    @RequestMapping("/localauth/query")
    public ModelAndView queryLocalAuth(){
        ModelAndView modelAndView = new ModelAndView("edit-local-auth");
        modelAndView.addObject("type","update");
        modelAndView.addObject("localauth",profileService.getLocalAuthUser());
        return modelAndView;
    }

    @RequestMapping("/localauth/update")
    public String updateLocalAuth(@Validated LocalAuth localAuth){
        profileService.updateLocalAuth(localAuth,true);
        return "redirect:/profile/query";
    }

    @RequestMapping("/localauth/new")
    public ModelAndView localauthNew(){
        ModelAndView modelAndView = new ModelAndView("edit-local-auth");
        modelAndView.addObject("type","insert");
        return modelAndView;
    }

    @RequestMapping("/localauth/add")
    public String localauthAdd(@Validated LocalAuth localAuth){
        profileService.insertLocalAuth(localAuth,true);
        return "redirect:/profile/query";
    }

    @RequestMapping("/admin/localauth/new")
    public ModelAndView adminLocalauthNew(Integer userId){
        ModelAndView modelAndView = new ModelAndView("admin/admin-edit-local-auth");
        modelAndView.addObject("type","insert");
        modelAndView.addObject("userId",userId);
        return modelAndView;
    }

    @RequestMapping("/admin/localauth/add")
    public String adminLocalauthAdd(@Validated LocalAuth localAuth){
        profileService.insertLocalAuth(localAuth,false);
        return "redirect:/admin/profile/query";
    }

    @RequestMapping("/admin/localauth/update")
    public String adminUpdateLocalAuth(@Validated LocalAuth localAuth){
        profileService.updateLocalAuth(localAuth,false);
        return "redirect:/admin/profile/query";
    }

    @RequestMapping("/admin/localauth/query")
    public ModelAndView adminQueryLocalAuth(Integer userId){
        ModelAndView modelAndView = new ModelAndView("admin/admin-edit-local-auth");
        modelAndView.addObject("type","update");
        modelAndView.addObject("localauth",profileService.getLocalAuthByUserId(userId));
        modelAndView.addObject("userId",userId);
        return modelAndView;
    }

}
