package com.ruisitech.bi.web.bireport;

import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.view.context.ExtContext;
import com.rsbi.ext.engine.view.context.MVContext;
import com.ruisitech.bi.entity.bireport.Area;
import com.ruisitech.bi.entity.bireport.ChartQueryDto;
import com.ruisitech.bi.service.bireport.AreaService;
import com.ruisitech.bi.service.bireport.ChartService;
import com.ruisitech.bi.util.BaseController;
import com.ruisitech.bi.util.CompPreviewService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Scope("prototype")
@RequestMapping(value = "/bireport")
public class ChartController extends BaseController  {
	
	@Autowired
	private ChartService chartService;

	@Autowired
	private AreaService areaService;

	private static Logger logger = Logger.getLogger(TableController.class);

	@RequestMapping(value="/ChartView.action", method = RequestMethod.POST)
	public @ResponseBody
    Object chartView(@RequestBody ChartQueryDto chartJson, HttpServletRequest req, HttpServletResponse res) throws Exception {
		ExtContext.getInstance().removeMV(ChartService.deftMvId);
		MVContext mv = chartService.json2MV(chartJson, false);
		try {
			CompPreviewService ser = new CompPreviewService(req, res, req.getServletContext());
			//ser.setParams(tableService.getMvParams());
			ser.initPreview();
			String ret = ser.buildMV(mv, req.getServletContext());
			JSONObject obj = JSONObject.parseObject(ret);
			if(obj.get("result") != null && obj.getInteger("result") == 500){
				return super.buildError(obj.getString("msg"));
			}
			return super.buildSucces(obj);
		}catch (Exception ex){
			logger.error("图形展现出错", ex);
			return super.buildError(ex.getMessage());
		}
	}

	@RequestMapping(value="/listAreas.action")
	public @ResponseBody Object listAreas(){
		return super.buildSucces(areaService.listProvAndCitys());
	}


	@RequestMapping(value="/getProvByName.action")
	public @ResponseBody Object getProvByName(String name){
		return super.buildSucces(areaService.getProvByName(name));
	}

	@RequestMapping(value="/getCityByName.action")
	public @ResponseBody Object getCityByName(String name){
		return super.buildSucces(areaService.getCityByName(name));
	}

	@RequestMapping(value="/getProvByCityCode.action")
	public @ResponseBody Object getProvByCityCode(String code){
		Area area = areaService.getProvByCityCode(code);
		return super.buildSucces(area);
	}
}
