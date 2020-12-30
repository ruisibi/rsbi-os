/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.frame;

import com.rsbi.ext.engine.dao.DaoHelper;
import com.rsbi.ext.engine.dao.DaoHelperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 配置 daoHelper
 * @Author huangqin
 * @Date 2020/10/19 5:17 下午
 */
@Configuration
public class DaoHelperConfig {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public DaoHelper daoHelper(){
        DaoHelperImpl dao = new DaoHelperImpl();
        dao.setJdbcTemplate(jdbcTemplate);
        return dao;
    }
}
