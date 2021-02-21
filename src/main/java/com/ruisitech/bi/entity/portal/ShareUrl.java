/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有 
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.entity.portal;

import com.ruisitech.bi.entity.common.BaseEntity;
import com.ruisitech.bi.util.RSBIUtils;

import java.util.Date;

public class ShareUrl extends BaseEntity {

    private String token;//

    private Integer islogin;// 1true, 0 false

    private Integer yxq;//小时为单位， -1表示不限制
    
    private Date crtdate;
    
    private String reportId;
    
    private Integer crtUser; //创建人

    private Integer rType; //类型，1 报表， 2 仪表盘

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Integer getIslogin() {
        return islogin;
    }

    public void setIslogin(Integer islogin) {
        this.islogin = islogin;
    }

    public Integer getYxq() {
        return yxq;
    }

    public void setYxq(Integer yxq) {
        this.yxq = yxq;
    }

	public Date getCrtdate() {
		return crtdate;
	}

	public void setCrtdate(Date crtdate) {
		this.crtdate = crtdate;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public Integer getCrtUser() {
		return crtUser;
	}

	public void setCrtUser(Integer crtUser) {
		this.crtUser = crtUser;
	}
	public Integer getrType() {
		return rType;
	}
	public void setrType(Integer rType) {
		this.rType = rType;
	}

	@Override
	public void validate() {
		this.token = RSBIUtils.htmlEscape(this.token);
		this.reportId = RSBIUtils.htmlEscape(this.reportId);
	}
}