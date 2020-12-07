package com.ruisitech.bi.mapper.app;

import com.ruisitech.bi.entity.app.Collect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CollectMapper {
	
	List<Collect> listCollect(@Param("userId") Integer userId);
	
	Integer collectExist(Collect collect);
	
	void addCollect(Collect collect);
	
	void delCollect(Collect collect);
}
