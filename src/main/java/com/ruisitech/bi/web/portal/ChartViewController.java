package com.ruisitech.bi.web.portal;

import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.view.context.ExtContext;
import com.rsbi.ext.engine.view.context.MVContext;
import com.ruisitech.bi.entity.portal.PortalChartQuery;
import com.ruisitech.bi.service.portal.PortalChartService;
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
@RequestMapping(value = "/portal")
public class ChartViewController extends BaseController {

	private static Logger logger = Logger.getLogger(ChartViewController.class);

	@Autowired
	private PortalChartService chartService;

	@RequestMapping(value="/ChartView.action", method = RequestMethod.POST)
	public @ResponseBody
    Object tableView(@RequestBody PortalChartQuery chartJson, HttpServletRequest req, HttpServletResponse res) throws Exception {
		try {
			ExtContext.getInstance().removeMV(PortalChartService.deftMvId);
			MVContext mv = chartService.json2MV(chartJson);

			CompPreviewService ser = new CompPreviewService(req, res, req.getServletContext());
			ser.setParams(chartService.getMvParams());
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

	@RequestMapping(value="/chartColors.action")
	public @ResponseBody
    Object chartColors() {
		return super.buildSucces(chartService.queryChartColors());
	}
}
