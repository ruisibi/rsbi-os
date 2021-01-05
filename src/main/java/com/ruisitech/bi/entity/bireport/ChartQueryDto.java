package com.ruisitech.bi.entity.bireport;

import com.ruisitech.bi.entity.common.BaseEntity;

import java.util.List;

public class ChartQueryDto extends BaseEntity {

	private String dsid;
	private String dsetId;
	private String id;

	private List<KpiDto> kpiJson;
	
	private ChartJSONDto chartJson;
	
	private List<ParamDto> params;

	public List<KpiDto> getKpiJson() {
		return kpiJson;
	}
	public void setKpiJson(List<KpiDto> kpiJson) {
		this.kpiJson = kpiJson;
	}
	public ChartJSONDto getChartJson() {
		return chartJson;
	}
	public void setChartJson(ChartJSONDto chartJson) {
		this.chartJson = chartJson;
	}
	public List<ParamDto> getParams() {
		return params;
	}
	public void setParams(List<ParamDto> params) {
		this.params = params;
	}
	
	public String getDsid() {
		return dsid;
	}
	public void setDsid(String dsid) {
		this.dsid = dsid;
	}
	public String getDsetId() {
		return dsetId;
	}
	public void setDsetId(String dsetId) {
		this.dsetId = dsetId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public void validate() {
		 
	}
}
