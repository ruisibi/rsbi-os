package com.ruisitech.bi.web.bireport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.view.context.ExtContext;
import com.rsbi.ext.engine.view.context.MVContext;
import com.rsbi.ext.engine.view.emitter.ContextEmitter;
import com.rsbi.ext.engine.view.emitter.excel.ExcelEmitter;
import com.rsbi.ext.engine.view.emitter.pdf.PdfEmitter;
import com.rsbi.ext.engine.view.emitter.text.TextEmitter;
import com.rsbi.ext.engine.view.emitter.word.WordEmitter;
import com.ruisitech.bi.entity.bireport.OlapInfo;
import com.ruisitech.bi.service.bireport.OlapService;
import com.ruisitech.bi.service.bireport.ReportService;
import com.ruisitech.bi.service.model.CubeService;
import com.ruisitech.bi.util.BaseController;
import com.ruisitech.bi.util.CompPreviewService;
import com.ruisitech.bi.util.RSBIUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/bireport")
@Scope("prototype")
public class ReportDesignController extends BaseController {
	
	@Autowired
	private CubeService cubeService;
	
	@Autowired
	private OlapService service;
	
	@Autowired
	private ReportService reportService;
	

	@RequestMapping(value="/ReportExport.action", method = RequestMethod.POST)
	public @ResponseBody
    Object export(String type, String json, String picinfo, HttpServletRequest req, HttpServletResponse res) throws Exception{
		
		ExtContext.getInstance().removeMV(ReportService.deftMvId);
		JSONObject obj = (JSONObject)JSON.parse(json);
		MVContext mv = reportService.json2MV(obj, 0);
		req.setAttribute("picinfo", picinfo);
		CompPreviewService ser = new CompPreviewService(req, res, req.getServletContext());
		ser.setParams(null);
		ser.initPreview();
		
		String fileName = "file.";
		if("html".equals(type)){
			fileName += "html";
		}else
		if("excel".equals(type)){
			fileName += "xls";
		}else
		if("csv".equals(type)){
			fileName += "csv";
		}else
		if("pdf".equals(type)){
			fileName += "pdf";
		}else 
		if("word".equals(type)){
			fileName += "docx";
		}
		
		res.setContentType("application/x-msdownload");
		String contentDisposition = "attachment; filename=\""+fileName+"\"";
		res.setHeader("Content-Disposition", contentDisposition);
		
		if("html".equals(type)){
			String ret = ser.buildMV(mv, req.getServletContext());
			String html = RSBIUtils.htmlPage(ret, RSBIUtils.getConstant("resPath"), "olap");
			InputStream is = IOUtils.toInputStream(html, "utf-8");
			IOUtils.copy(is, res.getOutputStream());
			is.close();
		}else
		if("excel".equals(type)){
			ContextEmitter emitter = new ExcelEmitter();
			ser.buildMV(mv, emitter, req.getServletContext());
		}else
		if("csv".equals(type)){
			ContextEmitter emitter = new TextEmitter();
			String ret = ser.buildMV(mv, emitter, req.getServletContext());
			InputStream is = IOUtils.toInputStream(ret, "gb2312");
			IOUtils.copy(is, res.getOutputStream());
			is.close();
		}else 
		if("pdf".equals(type)){
			ContextEmitter emitter = new PdfEmitter();
			ser.buildMV(mv, emitter, req.getServletContext());
		}else
		if("word".equals(type)){
			ContextEmitter emitter = new WordEmitter();
			ser.buildMV(mv, emitter, req.getServletContext());
		}
		
		return null;
	}
	
	@RequestMapping(value="/kpidesc.action")
	public @ResponseBody Object kpidesc(Integer cubeId){
		List<Map<String, Object>>  ret = service.listKpiDesc(cubeId);
		return super.buildSucces(ret);
	}
	
	@RequestMapping(value="/print.action", method = RequestMethod.POST)
	public @ResponseBody Object print(String pageInfo, HttpServletRequest req, HttpServletResponse res) throws Exception{
		ExtContext.getInstance().removeMV(ReportService.deftMvId);
		JSONObject obj = (JSONObject)JSON.parse(pageInfo);
		MVContext mv = reportService.json2MV(obj, 1);
		CompPreviewService ser = new CompPreviewService(req, res, req.getServletContext());
		ser.setParams(reportService.getParams());
		ser.initPreview();

		String ret = ser.buildMV(mv, req.getServletContext());
		JSONObject json = JSONObject.parseObject(ret);
		return super.buildSucces(json);
	}
}
