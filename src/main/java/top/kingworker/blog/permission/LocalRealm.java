package top.kingworker.blog.permission;

import cn.hutool.core.lang.Validator;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import top.kingworker.blog.entity.LocalAuth;
import top.kingworker.blog.service.ProfileService;

/**
 * 用户本地 登录  用户通过这个可以进行邮箱和用户名登录
 */
public class LocalRealm extends AuthorizingRealm {
    @Autowired
    public ProfileService profileService;

    public LocalRealm(){
        super();
        setName("LocalRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Integer userId = (Integer) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(profileService.getPermissionsByUserId(userId));
        return simpleAuthorizationInfo;
    }

    //这个方法用来认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        /**
         * 在这里需要查出用户的信息  然后 把密码交给Shiro去和前端传来的值进行对比
         * 对比成功 Shiro这里就会登录 就会走授权方法
         */
        LocalAuth localAuth = null;
        //这个可以获取我们传过来的username
        String loginName = (String) authenticationToken.getPrincipal();
        if(Validator.isEmail(loginName)){
            localAuth = profileService.findLocalAuthByEmail(loginName);
        }
        else{
            localAuth = profileService.findLocalAuthByUsername(loginName);
        }
        if(localAuth == null){
            return null;
        }
//        第一个参数 可以放入一个对象 今后我们可以通过subject取出来
        //第二个参数是密码  第三个参数是盐值 第三个是realmName
        //保存UserId就好了  我们需要什么内容用这个Id进行查询就好了
        return new SimpleAuthenticationInfo(localAuth.getUserId(),localAuth.getPassword(),
                ByteSource.Util.bytes(localAuth.getSalt()),getName());
    }
}
