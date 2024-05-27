package com.ruisitech.bi.service.frame;

import com.alibaba.fastjson.JSONObject;
import com.ruisitech.bi.entity.common.RequestStatus;
import com.ruisitech.bi.entity.common.Result;
import com.ruisitech.bi.entity.frame.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 用户登录验证的拦截器
 */
public class SessionAuthcFilter extends AdviceFilter {

	private UserService userService;

	public SessionAuthcFilter(UserService userService){
		this.userService = userService;
	}

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response)
			throws Exception {
		Subject us = SecurityUtils.getSubject();
		Session session = us.getSession();

		if(!us.isAuthenticated() && us.isRemembered() && session.getAttribute(ShiroDbRealm.SESSION_USER_KEY) == null){
			//说明是记住我过来的,恢复SESSION里的值
			Object staffId = us.getPrincipal();
			if(staffId != null){
				User u = userService.getUserByStaffId(staffId.toString());
				session.setAttribute(ShiroDbRealm.SESSION_USER_KEY, u);
			}else{
				response.setContentType("application/json; charset=utf-8");
				response.setCharacterEncoding("utf-8");
				Result r = new Result(RequestStatus.NOLOGIN.getStatus(), "message.base.noLoginInfo", null);
				response.getWriter().print(JSONObject.toJSONString(r));
				return false;
			}
		}
		if(us.isAuthenticated() || us.isRemembered()){  //不管是认证登陆 还是 记住我登陆， 都放行
			return true;
		}else{
			response.setContentType("application/json; charset=utf-8");
			response.setCharacterEncoding("utf-8");
			Result r = new Result(RequestStatus.NOLOGIN.getStatus(), "message.base.noLoginInfo", null);
			response.getWriter().print(JSONObject.toJSONString(r));
			return false;
		}
	}

}
