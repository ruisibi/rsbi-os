/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有 
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.portal;

import com.ruisitech.bi.entity.portal.ShareUrl;
import com.ruisitech.bi.mapper.portal.ShareUrlMapper;
import com.ruisitech.bi.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 复制url service
 * 复制的 url 可以发给别人
 * @author hq
 *
 */
@Service
public class ShareUrlService {
	
	@Autowired
	private ShareUrlMapper mapper;

	private String sysUser = RSBIUtils.getConstant("sysUser");
	
	public void saveShareUrl(ShareUrl vo){
		//生成token
		String token = UUID.randomUUID().toString().replaceAll("-", "");
		Integer userId = RSBIUtils.getLoginUserInfo().getUserId();
		vo.setCrtUser(userId);
		vo.setToken(token);
		mapper.insert(vo);
	}
	
	public ShareUrl getByToken(String token){
		return mapper.selectByPrimaryKey(token);
	}
}
