package com.ruisitech.bi.mapper.model;

import com.ruisitech.bi.entity.model.Measure;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MeasureMapper {

	void insertKpi(Measure kpi);
	
	void updateKpi(Measure kpi);
	
	void deleteKpi(Measure kpi);
	
	int getMaxKpiId();
}
