package top.kingworker.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.kingworker.blog.aspect.ClearAllCache;
import top.kingworker.blog.entity.*;
import top.kingworker.blog.mapper.*;
import top.kingworker.blog.service.ProfileService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    ProfileMapper profileMapper;

    @Autowired
    LocalAuthMapper localAuthMapper;

    @Autowired
    PermissionMapper permissionMapper;

    @Autowired
    GithubTokenMapper githubTokenMapper;

    @Autowired
    UserPermissionMapper userPermissionMapper;

    @Autowired
    QqTokenMapper qqTokenMapper;

    @Override
    @Cacheable(value = "profileById",unless = "#result == null")
    public Profile getProfileById(Integer id) {
        return profileMapper.selectByPrimaryKey(id);
    }

    @Override
    public LocalAuth findLocalAuthByUsername(String username) {
        return localAuthMapper.findByUsername(username);
    }

    @Override
    public LocalAuth findLocalAuthByEmail(String email) {
        return localAuthMapper.findByEmail(email);
    }

    @Override
    @ClearAllCache
    public boolean registerUser(LocalAuth localAuth) {
        Profile profile = insertProfile();
        Set<Integer> set = new HashSet<>();
        //默认具备增删改查权限
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        insertPermissions(profile.getId(),set);
        String salt = UUID.randomUUID().toString();
        //这是加密后的密码
        String password = new Sha256Hash(localAuth.getPassword(),salt,500).toBase64();
        localAuth.setPassword(password);
        localAuth.setSalt(salt);
        localAuth.setUserId(profile.getId());
        return localAuthMapper.insert(localAuth)>0;
    }

    @Override
    @ClearAllCache
    public Profile insertProfile() {
        Profile profile = new Profile();
        profile.setAvatar("/images/default-avatar.svg");
        profile.setBackground("/images/default-bg.jpg");
        profile.setCreateTime(new Date());
        profile.setNickname("用户"+ UUID.randomUUID().toString().substring(0,3));
        profileMapper.insert(profile);
        return profile;
    }

    @Override
    @ClearAllCache
    public void insertPermissions(int userId, Set<Integer> permissions) {
        UserPermission userPermission = new UserPermission();
        userPermission.setUserId(userId);
        for(Integer val : permissions){
            userPermission.setPermissionId(val);
            userPermissionMapper.insert(userPermission);
        }
    }

    @Override
    @Cacheable(value = "userPermissions",unless="#result == null")
    public Set<String> getPermissionsByUserId(int userId) {
        return userPermissionMapper.getPermissionsByUserId(userId);
    }

    @Override
    public Profile getUserProfile() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if(principal == null) {
            return null;
        }
        Profile profile = profileMapper.selectByPrimaryKey((Integer) principal);
        return profile;
    }

    @Override
    @Cacheable(value = "allProfiles",unless="#result == null")
    public PageInfo<Profile> getAllProfiles(Integer pageNum) {
        PageHelper.startPage(pageNum,6);
        List<Profile> profiles = profileMapper.selectAll();
        PageInfo<Profile> pageInfo = new PageInfo<>(profiles);
        return pageInfo;
    }

    @Override
    @ClearAllCache
    public int updateProfile(Profile profile) {
        return profileMapper.updateByPrimaryKey(profile);
    }

    @Override
    @ClearAllCache
    public int deleteProfile(Integer profile) {
        return profileMapper.deleteByPrimaryKey(profile);
    }

    @Override
    @ClearAllCache
    public void deletePermissions(int userId) {
        userPermissionMapper.deletePermissions(userId);
    }

    @Override
    @ClearAllCache
    public int insertPermissions(int userId, String permsStr) {
        deletePermissions(userId);
        List<Integer> perms = Arrays.stream(permsStr.split(",")).map( val ->
            Integer.parseInt(val)
        ).collect(Collectors.toList());
        return userPermissionMapper.insertBatch(userId,perms);
    }

    @Override
    @ClearAllCache
    public void updateProfileUser(Profile profile) {
        profile.setId((Integer) SecurityUtils.getSubject().getPrincipal());
        profileMapper.updateByPrimaryKey(profile);
    }

    @Override
    public Integer getUserIdByEmailAndUsername(LocalAuth localAuth) {
        Integer userIdByAccount = localAuthMapper.getUserIdByAccount(localAuth.getEmail(), localAuth.getUsername());
        return userIdByAccount;
    }

    @Override
    @ClearAllCache
    public void bindAccountGithub(GithubToken githubToken) {
        githubTokenMapper.insert(githubToken);
    }

    @Override
    @ClearAllCache
    public void bindAccountQQ(QqToken qqToken) {
        qqTokenMapper.insert(qqToken);
    }

    @Override
    @ClearAllCache
    public Profile register() {
        Profile profile = insertProfile();
        Set<Integer> set = new HashSet<>();
        //默认具备增删改查权限
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        insertPermissions(profile.getId(),set);
        return profile;
    }

    @Override
    @ClearAllCache
    public void unbindGithub(Integer userId) {
        githubTokenMapper.deleteByUserId(userId);
    }

    @Override
    @ClearAllCache
    public void unbindQQ(Integer userId) {
        qqTokenMapper.deleteByUserId(userId);
    }

    @Override
    public LocalAuth getLocalAuthUser() {
        return localAuthMapper.findByUserId((Integer) SecurityUtils.getSubject().getPrincipal());
    }

    @Override
    @ClearAllCache
    public void updateLocalAuth(LocalAuth localAuth,boolean isUser) {
        String salt = UUID.randomUUID().toString();
        //这是加密后的密码
        String password = new Sha256Hash(localAuth.getPassword(),salt,500).toBase64();
        localAuth.setPassword(password);
        localAuth.setSalt(salt);
        if(isUser){
            localAuth.setUserId((Integer) SecurityUtils.getSubject().getPrincipal());
            localAuthMapper.updateByUserId(localAuth);
        }
        else{
            localAuthMapper.updateByUserId(localAuth);
        }
    }

    @Override
    @ClearAllCache
    public void insertLocalAuth(LocalAuth localAuth, boolean isUser) {
        String salt = UUID.randomUUID().toString();
        //这是加密后的密码
        String password = new Sha256Hash(localAuth.getPassword(),salt,500).toBase64();
        localAuth.setPassword(password);
        localAuth.setSalt(salt);
        if(isUser){
            localAuth.setUserId((Integer) SecurityUtils.getSubject().getPrincipal());
            localAuthMapper.insert(localAuth);
        }
        else{
            localAuthMapper.insert(localAuth);
        }
    }

    @Override
    @Cacheable(value = "localAuth",unless="#result == null")
    public LocalAuth getLocalAuthByUserId(Integer userId) {
        return localAuthMapper.findByUserId(userId);
    }
}
