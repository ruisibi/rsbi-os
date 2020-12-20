package com.ruisitech.bi.web.bireport;

import com.ruisitech.bi.entity.bireport.ParamDto;
import com.ruisitech.bi.entity.model.Dimension;
import com.ruisitech.bi.service.bireport.OlapService;
import com.ruisitech.bi.service.model.DimensionService;
import com.ruisitech.bi.util.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/bireport")
public class DimController extends BaseController {
	
	@Autowired
	private OlapService service;
	
	@Autowired
	private DimensionService dimService;

	@RequestMapping(value="/queryDims.action")
	public @ResponseBody
    Object queryDims(Integer cubeId){
		return super.buildSucces(service.listDims(cubeId));
	}
	
	@RequestMapping(value="/paramFilter.action")
	public @ResponseBody
	Object paramFilter(ParamDto param, String keyword) throws Exception{
		Dimension d = dimService.getDimInfo(param.getId(), param.getCubeId());
		List<Map<String, Object>> ls = service.paramFilter(d, keyword, param.getDsid());
		Map<String, Object> ret = new HashMap<>();
		ret.put("datas", ls);
		if(ls.size() > 0) {
			if (d.getType().equals("month") || d.getType().equals("day")) {
				//排序
				ls = ls.stream().sorted((m1, m2)->{
					String v1 = (String)m1.get("id");
					String v2 = (String)m2.get("id");
					return v1.compareTo(v2);
				}).collect(Collectors.toList());
				Map<String, Object> first = ls.get(0);
				Map<String, Object> end = ls.get(ls.size() - 1);

				ret.put("st", first.get("id"));
				ret.put("end", end.get("id"));
				ret.remove("datas");
			}
		}
		return super.buildSucces(ret);
	}
}
