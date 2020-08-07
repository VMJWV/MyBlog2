package top.kingworker.blog.permission;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.*;

@Configuration
public class ShrioConfig {

    /**
     * 配置Shiro的工厂Bean 需要SecurityManager 和 传递Filtermap来定义
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shrioFilter(@Qualifier("createSecutiryManager")
                                                      SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, String> filterMap = new LinkedHashMap<>();
        //这是设置过滤规则
        /**
         * 有这几种过滤规则
         * anon 直接访问
         * authc 认证后可以访问
         * user 登录或者记住我都可以访问
         * perms 拥有对某个资源的权限才能访问
         * roles  拥有某个角色才可以访问
         */
        //label的增删改查 需要用户登录
        //article增删改查 需要用户登录
        //用户自己的增删改查需要用户登录
        // user[xxx,xxx]  表示登录的账号有xxx权限即可访问
        // perms[xxx,xxx] 表示有xx权限即可访问，但是不支持RememberMe
        // roles[xxx,xxx] 表示有xxx角色可以访问 注意的是如果这里的roles中有多个角色
        //那么要保证用户有全部的角色才能访问 想要单角色即可访问 需要自定义Filter
        filterMap.put("/comment/add","anon");
        //自定义一个Filter 让权限匹配到达我们想要的效果
        filterMap.put("/*/add", "MyPerms[add|manage]");
        filterMap.put("/*/delete", "MyPerms[delete|manage]");
        filterMap.put("/*/update", "MyPerms[update|manage]");
        filterMap.put("/*/query", "MyPerms[query|manage]");
        filterMap.put("/label/labels", "MyPerms[query|manage]");
        filterMap.put("/article/new", "MyPerms[add|manage]");
        filterMap.put("/*/edit", "MyPerms[update|manage]");
        filterMap.put("/admin/**", "MyPerms[manage]");
        filterMap.put("/*/unbind","user");
        //logout不用写 shiro已经实现了 我们只需要写key 指定登出的url地址即可
        filterMap.put("/logout","logout");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        /**
         * 获取到shiro的默认filter 然后在添加工厂中去
         */
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("MyPerms",new MyPermissionAuthFilter());
        shiroFilterFactoryBean.setFilters(filters);
        shiroFilterFactoryBean.setLoginUrl("/login");
        return shiroFilterFactoryBean;
    }

    /**
     * 配置安全管理器 安全管理器这里是配置realm 和realm的选用策略的
     *
     * @param localRealm
     * @param freeRealm
     * @param modularRealmAuthenticator
     * @return
     */
    @Bean
    public SecurityManager createSecutiryManager(@Qualifier("getLocalRealm") LocalRealm localRealm,
                                                 @Qualifier("getFreeRealm") FreeRealm freeRealm,
                                                 @Qualifier("getMouModularRealmAuthenticator") ModularRealmAuthenticator
                                                         modularRealmAuthenticator) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //把两个Realm添加进去
        List<Realm> realms = new ArrayList<>();
        securityManager.setAuthenticator(modularRealmAuthenticator);
        realms.add(localRealm);
        realms.add(freeRealm);
        securityManager.setRealms(realms);
        return securityManager;
    }

    /**
     * 自定义realm
     *
     * @param hashedCredentialsMatcher
     * @return
     */
    @Bean
    public LocalRealm getLocalRealm(@Qualifier("hashedCredentialsMatcher")
                                            HashedCredentialsMatcher hashedCredentialsMatcher) {
        LocalRealm localRealm = new LocalRealm();
        //设置匹配器
        localRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return localRealm;
    }

    /**
     * 自定义realm
     *
     * @return
     */
    @Bean
    public FreeRealm getFreeRealm() {
        FreeRealm freeRealm = new FreeRealm();
        return freeRealm;
    }

    /**
     * 自定义realm选择器
     *
     * @return
     */
    @Bean
    public ModularRealmAuthenticator getMouModularRealmAuthenticator() {
        ModularRealmAuthenticator modularRealmAuthenticator = new MyModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }


    /**
     * 配置加密方式 shiro会用这个进行匹配 加密方式是配置给realm的 注意
     *
     * @return
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("SHA-256");//散列算法:使用 sha-256
        hashedCredentialsMatcher.setHashIterations(500);//散列的次数
        //是否进行了Hex编码 没有进行 设置为false 默认为true
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(false);
        return hashedCredentialsMatcher;
    }

    /**
     * 配置shiro标签
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
