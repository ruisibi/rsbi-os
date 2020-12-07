/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有 
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.web.frame;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruisitech.bi.entity.common.PageParam;
import com.ruisitech.bi.entity.frame.Role;
import com.ruisitech.bi.service.frame.RoleService;
import com.ruisitech.bi.util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 * @author gdp
 *
 */
@Controller
@RequestMapping(value = "/frame")
public class RoleController extends BaseController {
	
	@Autowired
	private RoleService service;
	
	@RequestMapping(value="/role/list.action")
	public @ResponseBody
    Object list(PageParam page) {
		if(page != null && page.getPage() != null && page.getRows() != null){
			PageHelper.startPage(page.getPage(), page.getRows());
		}
		List<Role> ls = service.list(page.getSearch());
		PageInfo<Role> pageInfo=new PageInfo<Role>(ls);
		return super.buildSucces(pageInfo);
	}
	
	@RequestMapping(value="/role/userRolelist.action")
	public @ResponseBody
    Object userRolelist(Integer userId) {
		return super.buildSucces(service.listUserRole(userId));
	}
	
	@RequestMapping(value="/role/userRoleSave.action")
	public @ResponseBody
    Object userRoleSave(@RequestBody Map<String, Object> pms) {
		List<Integer> roles = (List<Integer>) pms.get("roleId");
		Integer userId = (Integer) pms.get("userId");
		service.addUserRole(roles, userId);
		return super.buildSucces();
	}
	
	@RequestMapping(value="/role/save.action", method = RequestMethod.POST)
	public @ResponseBody
    Object save(Role role) {
		service.saveRole(role);
		return super.buildSucces();
	}
	
	@RequestMapping(value="/role/update.action", method = RequestMethod.POST)
	public @ResponseBody
    Object update(Role role) {
		service.updateRole(role);
		return super.buildSucces();
	}
	
	@RequestMapping(value="/role/delete.action")
	public @ResponseBody
    Object delete(Integer roleId) {
		service.deleteRole(roleId);
		return super.buildSucces();
	}
	
	@RequestMapping(value="/role/get.action")
	public @ResponseBody
    Object getRole(Integer roleId) {
		return super.buildSucces(service.getRole(roleId));
	}

	@RequestMapping(value="/role/roleMenu.action")
	public @ResponseBody
	Object roleMenu(Integer roleId) {
		Map<String, Object> menus = service.listRoleMenus(roleId);
		return super.buildSucces(menus);
	}

	@RequestMapping(value="/role/menuSave.action", method = RequestMethod.POST)
	public @ResponseBody Object roleMenuSave(String menuIds, Integer roleId) {
		service.roleMenu(menuIds, roleId);
		return super.buildSucces();
	}
}
