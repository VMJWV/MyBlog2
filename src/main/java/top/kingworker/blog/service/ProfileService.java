package top.kingworker.blog.service;

import com.github.pagehelper.PageInfo;
import top.kingworker.blog.entity.*;

import java.util.Set;

/**
 * LocalAuth 和 其他access_token 表 都是通过认证来获取Userid
 * 然后拿UserId 去profile表中取出用户信息
 */
public interface ProfileService {
    /**
     * 获取用户信息
     * @param id
     * @return
     */
    Profile getProfileById(Integer id);
    //下面两个方法 通过username和email 取用户进行登录认证
    LocalAuth findLocalAuthByUsername(String username);
    LocalAuth findLocalAuthByEmail(String email);
    //注册用户
    boolean registerUser(LocalAuth localAuth);
    //采用默认规则插入一个个人信息
    Profile insertProfile();
    //给定用户id和权限id 插入到user_permission表
    void insertPermissions(int userId, Set<Integer> permissions);
    //获取用户权限
    Set<String> getPermissionsByUserId(int userId);
    //获取当前登录用户信息
    Profile getUserProfile();
    PageInfo<Profile> getAllProfiles(Integer pageNum);
    int updateProfile(Profile profile);
    int deleteProfile(Integer profile);
    void deletePermissions(int userId);
    int insertPermissions(int userId,String permsStr);
    void updateProfileUser(Profile profile);
    Integer getUserIdByEmailAndUsername(LocalAuth localAuth);
    void bindAccountGithub(GithubToken githubToken);
    void bindAccountQQ(QqToken qqToken);
    Profile register();
    void unbindGithub(Integer userId);
    void unbindQQ(Integer userId);
    LocalAuth getLocalAuthUser();
    void updateLocalAuth(LocalAuth localAuth,boolean isUser);
    void insertLocalAuth(LocalAuth localAuth,boolean isUser);
    LocalAuth getLocalAuthByUserId(Integer userId);
}
