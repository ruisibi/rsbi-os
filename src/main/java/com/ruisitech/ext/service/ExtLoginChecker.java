package com.ruisitech.ext.service;

import com.ruisi.ext.engine.control.sys.LoginSecurityAdapter;
import com.ruisi.ext.engine.dao.DaoHelper;
import com.ruisi.ext.engine.wrapper.ExtRequest;
import com.ruisi.ext.engine.wrapper.ExtResponse;

import javax.servlet.ServletContext;

public class ExtLoginChecker implements LoginSecurityAdapter {

	public boolean loginChk(ExtRequest req, ExtResponse arg1, ServletContext ctx, DaoHelper arg2) {
		/**
		User user = (User)req.getSession().getAttribute(VdopConstant.USER_KEY_IN_SESSION);
		if(user == null){
			return false;
		}else{
			return true;
		}
		**/
		return true;
	}

}
