package com.ruisitech.bi.mapper.model;

import com.ruisitech.bi.entity.model.Dimension;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DimensionMapper {
	
	void insertDim(Dimension dim);
	
	void updatedim(Dimension dim);
	
	void deleteDim(Dimension dim);
	
	void insertGroup(Dimension dim);
	
	Integer getMaxDimId();
	
	void deleteGroupById(@Param("groupId") String groupId);
		
	void deleteGroupByCubeId(@Param("cubeId") Integer cubeId);
	
	List<String> listGroup(@Param("cubeId") Integer cubeId);
	
	Dimension getDimInfo(@Param("dimId") Integer dimId, @Param("cubeId") Integer cubeId);
	
	void updateColType(Map<String, Object> dim);
}
