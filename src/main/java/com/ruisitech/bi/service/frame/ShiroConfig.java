/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.frame;

import com.ruisitech.bi.service.portal.ShareUrlService;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;


import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName ShiroConfig
 * @Description ShiroConfig
 * @Author gdp
 * @Date 2020/9/18 10:29 上午
 */
@Configuration
public class ShiroConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private ShareUrlService shareUrlService;

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //拦截器
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("authc", new SessionAuthcFilter(userService));
        filters.put("shareAuthc", new ShareAuthcFilter(shareUrlService));
        shiroFilterFactoryBean.setFilters(filters);
        //注销
        shiroFilterFactoryBean.setLoginUrl("/frame/Logout.action");
        //过滤的URL
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/doLogin.action", "anon");
        filterChainDefinitionMap.put("/chartjson/**", "anon");  //json文件不用登录
        filterChainDefinitionMap.put("/control/extControl", "anon");
        filterChainDefinitionMap.put("/portal/share/**", "shareAuthc");  //报表分享后的URL不用登录
        filterChainDefinitionMap.put("/control/extShare", "shareAuthc");
        filterChainDefinitionMap.put("/static/**", "anon");  //资源文件(vue)
        filterChainDefinitionMap.put("/index.html", "anon");  //资源文件(vue)
        filterChainDefinitionMap.put("/rs_favicon.ico", "anon");  //资源文件(vue)
        filterChainDefinitionMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(ShiroDbRealm customRealm) {
        DefaultWebSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
        defaultSecurityManager.setRealm(customRealm);
        return defaultSecurityManager;
    }

    @Bean
    public ShiroDbRealm customRealm() {
        ShiroDbRealm customRealm = new ShiroDbRealm();
        return customRealm;
    }

    /*
    @Bean
    public Filter customFilter(){
        SessionAuthcFilter filter = new SessionAuthcFilter();
        return filter;
    }

     */

}
