/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有 
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.bireport;

import com.ruisitech.bi.entity.bireport.Area;
import com.ruisitech.bi.mapper.bireport.AreaMapper;
import com.ruisitech.bi.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AreaService {

	@Autowired
	private AreaMapper mapper;
	
	private String dbName = RSBIUtils.getConstant("dbName");
	
	public Area getProvByCityCode(String name){
		return mapper.getProvByCityCode(name);
	}
	
	public Area getProvByName(String name){
		return mapper.getProvByName(name);
	}
	
	public Area getCityByName(String name) {
		return mapper.getCityByName( name);
	}
	
	public List<Area> listCityByProvCode(String code){
		return mapper.listCityByProvCode(code);
	}
	
	public List<Area> listTownByCityCode(String code){
		return mapper.listTownByCityCode(code);
	}
	
	public List<Map<String, Object>> listProvAndCitys(){
		return mapper.listProvAndCitys(dbName);
	}
}
