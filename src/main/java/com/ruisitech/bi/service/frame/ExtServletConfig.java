/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.frame;

import com.rsbi.ext.engine.control.ExtControlServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置Ext使用的Servlet
 * @ClassName ExtServletConfig
 * @Description ExtServletConfig
 * @Author huangqin
 * @Date 2021/2/7 5:49 下午
 */
@Configuration
public class ExtServletConfig implements WebMvcConfigurer {

    @Bean
    public ServletRegistrationBean getServletRegistrationBean(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new ExtControlServlet(),"/control/extControl");
        return registrationBean;
    }
}
