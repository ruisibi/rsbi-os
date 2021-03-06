package com.ruisitech.bi.mapper.model;

import com.ruisitech.bi.entity.model.CubeColMeta;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CubeColMetaMapper {
	
	void insertMeta(CubeColMeta meta);
	
	Integer getMaxRid();

	void deleteKpiMeta(@Param("cubeId") Integer cubeId);
	
	void deleteDimMeta(@Param("cubeId") Integer cubeId);
	
	void deleteByCubeId(@Param("cubeId") Integer cubeId);
}
