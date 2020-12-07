package com.ruisitech.bi.service.model;

import com.ruisitech.bi.entity.model.Dimension;
import com.ruisitech.bi.mapper.model.DimensionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DimensionService {

	@Autowired
	private DimensionMapper mapper;
	
	public Dimension getDimInfo(Integer dimId, Integer cubeId){
		return mapper.getDimInfo(dimId, cubeId);
	}
}
