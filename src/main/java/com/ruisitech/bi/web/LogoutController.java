package com.ruisitech.bi.web;

import com.ruisitech.bi.util.BaseController;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/frame")
public class LogoutController extends BaseController {

	@RequestMapping(value="/Logout.action")
	public @ResponseBody Object logout(){
		Subject subject = SecurityUtils.getSubject();
		if (subject != null) {  
	        try{  
	            subject.logout();  
	        }catch(Exception ex){  
	        	ex.printStackTrace();
	        }  
	    }  
		return super.buildSucces();
	}
}
