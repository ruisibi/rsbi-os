package com.ruisitech.bi.web.bireport;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.view.context.ExtContext;
import com.rsbi.ext.engine.view.context.MVContext;
import com.ruisitech.bi.entity.bireport.TableQueryDto;
import com.ruisitech.bi.service.bireport.TableService;
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
public class TableController extends BaseController {
	
	@Autowired
	private TableService tableService;

	private static Logger logger = Logger.getLogger(TableController.class);

	@RequestMapping(value="/TableView.action", method = RequestMethod.POST)
	public @ResponseBody
    Object tableView(@RequestBody TableQueryDto tableJson, HttpServletRequest req, HttpServletResponse res) throws Exception {
		tableJson.setCompId("t1");
		ExtContext.getInstance().removeMV(TableService.deftMvId);
		MVContext mv = tableService.json2MV(tableJson);
		try {
			CompPreviewService ser = new CompPreviewService(req, res, req.getServletContext());
			ser.setParams(tableService.getMvParams());
			ser.initPreview();
			String ret = ser.buildMV(mv, req.getServletContext());
			JSONObject json = JSONObject.parseObject(ret);
			if(json.get("result") != null && json.getInteger("result") == 500){
				return super.buildError(json.getString("msg"));
			}
			json = json.getJSONObject(tableJson.getCompId());
			return super.buildSucces(json);
		}catch (Exception ex){
			logger.error("表格展现出错", ex);
			return super.buildError(ex.getMessage());
		}
	}
}
