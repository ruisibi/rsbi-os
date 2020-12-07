package com.ruisitech.bi.mapper.portal;

import com.ruisitech.bi.entity.portal.MobReportType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MobReportTypeMapper {

	List<MobReportType> listcataTree();
	
	void insertType(MobReportType type);
	
	void updateType(MobReportType type);
	
	void deleleType(@Param("id") Integer id);
	
	MobReportType getType(@Param("id") Integer id);
	
	Integer cntReport(@Param("id") Integer id);
	
	Integer maxTypeId();
}
