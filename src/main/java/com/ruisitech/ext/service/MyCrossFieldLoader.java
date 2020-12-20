package com.ruisitech.ext.service;

import com.ruisi.ext.engine.cross.CrossFieldLoader;
import com.ruisi.ext.engine.view.context.cross.CrossField;
import com.ruisi.ext.engine.wrapper.ExtRequest;

import java.util.List;
import java.util.Map;

public class MyCrossFieldLoader implements CrossFieldLoader {
	
	private ExtRequest request;
	
	public void setRequest(ExtRequest request) {
		this.request = request;
	}

	@Override
	public List loadField(String type, String values, Map keys) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public CrossField loadFieldByKpiCode(String kpiCode) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String loadFieldName(String type, String value) {
		return "合计";
	}

	@Override
	public List<String> loadUserCustomize(String userId, String mvId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
