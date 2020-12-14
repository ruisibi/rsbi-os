/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.frame;

import com.ruisitech.ext.service.DataControlImpl;
import com.ruisitech.ext.service.DataControlInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName 数据权限控制类
 * @Author huangqin
 * @Date 2020/12/14 3:29 下午
 */
@Configuration
public class DataControlConfig {

    @Bean
    public DataControlInterface dataControl(){
        DataControlInterface dc = new DataControlImpl();
        return dc;
    }
}
