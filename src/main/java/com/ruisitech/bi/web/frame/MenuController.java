/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有 
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.web.frame;

import com.ruisitech.bi.entity.common.Result;
import com.ruisitech.bi.entity.frame.Menu;
import com.ruisitech.bi.service.frame.MenuService;
import com.ruisitech.bi.util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping(value = "/frame")
public class MenuController extends BaseController {

	@Autowired
	private MenuService service;
	
	@RequestMapping(value="/menu/loadData.action")
	public @ResponseBody
    Object loadData(Integer id) {
		return super.buildSucces(service.listMenuByPid(id));
	}
	
	@RequestMapping(value="/menu/save.action", method = RequestMethod.POST)
	public @ResponseBody
    Object save(Menu menu) {
		service.saveMenu(menu);
		return super.buildSucces(menu.getMenuId());
	}
	
	@RequestMapping(value="/menu/update.action", method = RequestMethod.POST)
	public @ResponseBody
    Object update(Menu menu) {
		service.updateMenu(menu);
		return super.buildSucces();
	}
	
	@RequestMapping(value="/menu/get.action")
	public @ResponseBody
    Object getMenu(Integer menuId) {
		return super.buildSucces(service.getById(menuId));
	}
	
	@RequestMapping(value="/menu/delete.action")
	public @ResponseBody
    Object delete(Integer menuId) {
		Result ret = service.deleteMenu(menuId);
		return ret;
	}
}
