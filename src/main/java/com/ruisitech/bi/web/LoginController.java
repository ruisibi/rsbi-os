package com.ruisitech.bi.web;

import com.ruisitech.bi.service.frame.UserService;
import com.ruisitech.bi.util.BaseController;
import com.ruisitech.bi.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/")
public class LoginController extends BaseController {

	@Autowired
	private UserService userService;

	@RequestMapping(value="/doLogin.action", method = RequestMethod.POST)
	public @ResponseBody
    Object doLogin(String userName, String password) {
		String msg = userService.shiroLogin(userName, password);
		if("SUC".equals(msg)){
			//更新登陆次数及时间
			Integer userId = RSBIUtils.getLoginUserInfo().getUserId();
			userService.updateLogDateAndCnt(userId);
			return super.buildSucces();
		}else{
			return super.buildError(msg);
		}
	}
}
