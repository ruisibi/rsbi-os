/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.frame;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 首页导航
 * @Author huangqin
 * @Date 2022/9/3 4:51 下午
 */
@Configuration
public class WebConfigue implements WebMvcConfigurer {

    @Bean //将组件注册在容器
    public WebMvcConfigurer webMvcConfigurer(){

        return new WebMvcConfigurer() {

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("index.html");
            }
        };
    }

}
