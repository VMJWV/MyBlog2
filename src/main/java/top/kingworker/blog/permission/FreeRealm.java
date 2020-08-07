package top.kingworker.blog.permission;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import top.kingworker.blog.entity.GithubToken;
import top.kingworker.blog.entity.Profile;
import top.kingworker.blog.entity.QqToken;
import top.kingworker.blog.mapper.GithubTokenMapper;
import top.kingworker.blog.mapper.QqTokenMapper;
import top.kingworker.blog.service.ProfileService;

/**
 * 专门用来第三方登录
 * 这里的登录不需要使用password
 */
public class FreeRealm extends AuthorizingRealm {

    @Autowired
    ProfileService profileService;

    @Autowired
    GithubTokenMapper githubTokenMapper;

    @Autowired
    QqTokenMapper qqTokenMapper;


    public FreeRealm(){
        super();
        setName("FreeRealm");
    }
    //授权方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //这个授权方法可以不写 shiro会走其他realm的授权方法
        return null;
    }
    //认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        String token = (String)authenticationToken.getPrincipal();
        GithubToken githubToken = githubTokenMapper.selectByToken(token);
        QqToken qqToken = qqTokenMapper.selectByOpenId(token);
        //这个用户都还没注册过 跳转到绑定和注册页
        if(githubToken == null && qqToken == null){
            return null;
        }
        else if(githubToken != null) {
            return new SimpleAuthenticationInfo(githubToken.getUserId(), "",
                    getName());
        }
        else{
            return new SimpleAuthenticationInfo(qqToken.getUserId(), "",
                    getName());
        }
    }
}
