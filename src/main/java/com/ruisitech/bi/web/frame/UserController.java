package com.ruisitech.bi.web.frame;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruisitech.bi.entity.common.PageParam;
import com.ruisitech.bi.entity.frame.User;
import com.ruisitech.bi.service.frame.UserService;
import com.ruisitech.bi.util.BaseController;
import com.ruisitech.bi.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(value = "/frame")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@RequestMapping(value="/user.action")
	public @ResponseBody
    Object getUserInfo(){
		Integer userId = RSBIUtils.getLoginUserInfo().getUserId();
		User u = userService.getUserByUserId(userId);
		return super.buildSucces(u);
	}

	@RequestMapping(value="/user/list.action")
	public @ResponseBody Object list(PageParam page){
		if(page != null && page.getPage() != null && page.getRows() != null){
			PageHelper.startPage(page.getPage(), page.getRows());
		}
		List<User> ls = userService.listUsers(page.getSearch());
		PageInfo<User> pageInfo=new PageInfo<User>(ls);
		return super.buildSucces(pageInfo);
	}

	@RequestMapping(value="/user/save.action")
	public @ResponseBody Object save(User user){
		String ret = userService.saveUser(user);
		if(ret == null){
			return super.buildSucces();
		}else{
			return super.buildError(ret);
		}
	}

	@GetMapping(value="/user/delete.action")
	public @ResponseBody Object userDelete(Integer userId) {
		userService.deleteUser(userId);
		return super.buildSucces();
	}

	@RequestMapping(value="/chgPsd.action", method = RequestMethod.POST)
	public @ResponseBody
    Object chgPsd(String password1, String password2, String password3){
		Integer userId = RSBIUtils.getLoginUserInfo().getUserId();
		String userPassword = userService.checkPsd(userId);
		if(!userPassword.equals(RSBIUtils.getEncodedStr(password1)))
		{
			return this.buildError("message.main.psd.error");
		}
		else
		{
			User u = new User();
			u.setUserId(userId);
			u.setPassword(RSBIUtils.getEncodedStr(password2));
			userService.modPsd(u);
			return this.buildSucces();
		}
	}

	@RequestMapping(value="/user/update.action", method = RequestMethod.POST)
	public @ResponseBody Object userUpdate(User u) {
		userService.updateUser(u);
		return super.buildSucces();
	}

	@RequestMapping(value="/user/get.action")
	public @ResponseBody Object getUser(Integer userId) {
		User u = userService.getUserById(userId);
		return super.buildSucces(u);
	}

	@RequestMapping(value="/user/userMenu.action")
	public @ResponseBody Object  userMenu(Integer userId) {
		Map<String, Object> dts = userService.listUserMenus(userId);
		return super.buildSucces(dts);
	}

	@RequestMapping(value="/user/userMenu/save.action", method = RequestMethod.POST)
	public @ResponseBody Object saveUserMenu(Integer userId, String menuIds) {
		userService.saveUserMenu(userId, menuIds);
		return super.buildSucces();
	}
}
