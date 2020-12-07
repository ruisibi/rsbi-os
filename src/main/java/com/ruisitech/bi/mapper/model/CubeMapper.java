package com.ruisitech.bi.mapper.model;

import com.ruisitech.bi.entity.model.Cube;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CubeMapper {

	List<Cube> listCube(@Param("keyword") String keyword);
	
	Integer getMaxCubeId();
	
	void insertCube(Cube cube);
	
	void updateCube(Cube cube);
	
	void deleteCube(@Param("cubeId") Integer cubeId);
	
	Cube getCubeById(@Param("cubeId") Integer cubeId);
	
	List<Map<String, Object>> getCubeDims(@Param("cubeId") Integer cubeId);
	
	List<Map<String, Object>> getCubeKpis(@Param("cubeId") Integer cubeId);
	
	List<Map<String, Object>> listCubeMeta(@Param("cubeId") Integer cubeId);
	
	List<Map<String, Object>> listDs(@Param("selectDsIds") String selectDsIds);
}
