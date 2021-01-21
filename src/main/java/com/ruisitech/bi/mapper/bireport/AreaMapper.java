/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有 
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.mapper.bireport;

import com.ruisitech.bi.entity.bireport.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AreaMapper {
	
	List<Area> listCityByProvCode(@Param("code") String code);
	
	List<Area> listTownByCityCode(@Param("code") String code);
	
	/**
	 * 查询省得编码，名称
	 * @param name
	 * @return
	 */
	Area getProvByName( @Param("name") String name);
	
	Area getCityByName( @Param("name") String name);
	
	/**
	 * 查询所有省市
	 * @param dbName
	 * @return
	 */
	List<Map<String, Object>> listProvAndCitys(@Param("dbName") String dbName);
	
	Area getProvByCityCode(@Param("code") String code);

}
