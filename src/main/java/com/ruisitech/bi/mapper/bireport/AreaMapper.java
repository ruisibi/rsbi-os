package com.ruisitech.bi.mapper.bireport;

import com.ruisitech.bi.entity.bireport.Area;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AreaMapper {
	
	List<Area> listCityByProvCode(@Param("code") String code);

}
