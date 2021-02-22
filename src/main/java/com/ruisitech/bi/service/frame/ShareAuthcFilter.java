package com.ruisitech.bi.service.frame;

import com.alibaba.fastjson.JSONObject;
import com.ruisitech.bi.entity.common.RequestStatus;
import com.ruisitech.bi.entity.common.Result;
import com.ruisitech.bi.entity.frame.User;
import com.ruisitech.bi.entity.portal.ShareUrl;
import com.ruisitech.bi.service.portal.ShareUrlService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Date;

/**
 * 用户登录验证的拦截器
 */
public class ShareAuthcFilter extends AdviceFilter {

	public ShareAuthcFilter(ShareUrlService shareUrlService){
		this.urlService = shareUrlService;
	}

	private ShareUrlService urlService;

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response)
			throws Exception {
		Subject us = SecurityUtils.getSubject(); 
		Session session = us.getSession();

		String token = request.getParameter("token");
		ShareUrl shareUrl = urlService.getByToken(token);
		if(shareUrl == null){
			response.setContentType("application/json; charset=utf-8");
			response.setCharacterEncoding("utf-8");
			Result r = new Result(RequestStatus.FAIL_FIELD.getStatus(), "报表不存在", null);
			response.getWriter().print(JSONObject.toJSONString(r));
			return false;
		}
		//判断是否过期
		Date crtdate = shareUrl.getCrtdate();
		Date now = new Date();
		long between = (now.getTime() - crtdate.getTime()) / (1000 * 60 * 60);  //相差小时
		if(shareUrl.getYxq() != - 1 && between  > shareUrl.getYxq()){
			response.setContentType("application/json; charset=utf-8");
			response.setCharacterEncoding("utf-8");
			Result r = new Result(RequestStatus.FAIL_FIELD.getStatus(), "报表已过期", null);
			response.getWriter().print(JSONObject.toJSONString(r));
			return false;
		}
		return true;
	}

}
