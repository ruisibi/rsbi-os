package com.ruisitech.bi.web.portal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.view.context.ExtContext;
import com.rsbi.ext.engine.view.context.MVContext;
import com.ruisitech.bi.entity.portal.PortalTableQuery;
import com.ruisitech.bi.service.portal.PortalTableService;
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
public class TableViewController extends BaseController {

	private static Logger logger = Logger.getLogger(TableViewController.class);

	@Autowired
	private PortalTableService serivce;

	@RequestMapping(value="/TableView.action", method = RequestMethod.POST)
	public @ResponseBody
    Object tableView(@RequestBody PortalTableQuery table, HttpServletRequest req, HttpServletResponse res) throws Exception {
		ExtContext.getInstance().removeMV(PortalTableService.deftMvId);
		try {
			MVContext mv = serivce.json2MV(table);

			CompPreviewService ser = new CompPreviewService(req, res, req.getServletContext());
			ser.setParams(serivce.getMvParams());
			ser.initPreview();
			String ret = ser.buildMV(mv , req.getServletContext());
			Object obj = JSON.parse(ret);
			if(obj instanceof JSONObject){
				JSONObject json = (JSONObject)obj;
				if (json.get("result") != null && json.getInteger("result") == 500) {
					return super.buildError(json.getString("msg"));
				}
				return super.buildSucces(json);
			}else {
				return super.buildSucces(obj);
			}
		}catch (Exception ex){
			logger.error("表格展现出错", ex);
			return super.buildError(ex.getMessage());
		}
	}
}
