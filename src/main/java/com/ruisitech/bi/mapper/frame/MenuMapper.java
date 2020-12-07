/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有 
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.mapper.frame;

import com.ruisitech.bi.entity.frame.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MenuMapper {
	
	List<Menu> listUserMenus(@Param("userId") Integer userId);

	/**
	 * 查询角色名称
	 * @param roleIds
	 * @return
	 */
	List<Menu> listRolesMenus(@Param("roleIds") List<Integer> roleIds);

	/**
	 * 根据角色名称获取角色ID列表
	 * @param names
	 * @return
	 */
	List<Integer> listRoleIdsByNames(@Param("names") List<String> names);

	List<Map<String, Object>> listMenuByPid(@Param("pid") Integer pid);

	Menu getById(@Param("menuId") Integer menuId);
}
