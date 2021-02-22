package com.ruisitech.bi.web.portal;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.ExtConstants;
import com.rsbi.ext.engine.view.context.ExtContext;
import com.rsbi.ext.engine.view.context.MVContext;
import com.rsbi.ext.engine.view.emitter.ContextEmitter;
import com.rsbi.ext.engine.view.emitter.excel.ExcelEmitter;
import com.rsbi.ext.engine.view.emitter.pdf.PdfEmitter;
import com.rsbi.ext.engine.view.emitter.text.TextEmitter;
import com.rsbi.ext.engine.view.emitter.word.WordEmitter;
import com.ruisitech.bi.entity.portal.ShareUrl;
import com.ruisitech.bi.service.portal.PortalPageService;
import com.ruisitech.bi.service.portal.PortalService;
import com.ruisitech.bi.service.portal.ShareUrlService;
import com.ruisitech.bi.util.BaseController;
import com.ruisitech.bi.util.CompPreviewService;
import com.ruisitech.bi.util.RSBIUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Controller
@Scope("prototype")
@RequestMapping(value = "/portal")
public class PortalViewController extends BaseController {

	private static Logger logger = Logger.getLogger(PortalViewController.class);

	@Autowired
	private PortalService portalService;
	
	@Autowired
	private PortalPageService pageService;

	@Autowired
	private ShareUrlService urlService;

	@RequestMapping(value="/view.action")
	public @ResponseBody
    Object view(String pageId, HttpServletRequest req, HttpServletResponse res) {
		String cfg = portalService.getPortalCfg(pageId);
		if(cfg == null){
			return super.buildError("找不到报表文件。");
		}
		try {
			JSONObject json = (JSONObject)JSON.parse(cfg);
			String id = json.getString("id");
			ExtContext.getInstance().removeMV("mv_" + id);
			MVContext mv = pageService.json2MV(json, false, false);
			CompPreviewService ser = new CompPreviewService(req, res, req.getServletContext());
			ser.setParams(pageService.getMvParams());
			ser.initPreview();
			String ret = ser.buildMV(mv, req.getServletContext());
			JSONObject rjson = JSONObject.parseObject(ret);
			if (rjson.get("result") != null && rjson.getInteger("result") == 500) {
				return super.buildError(rjson.getString("msg"));
			}
			return super.buildSucces(rjson);

		}catch (Exception ex){
			logger.error("报表展现错误", ex);
			return super.buildError(ex.getMessage());
		}
	}

	@RequestMapping(value="/share/view.action")
	public @ResponseBody
	Object shareView(String token, HttpServletRequest req, HttpServletResponse res) {
		ShareUrl url = urlService.getByToken(token);
		return this.view(url.getReportId(), req, res);
	}
	
	@RequestMapping(value="/export.action")
	public void export(String type, String pageId, String json, String picinfo, HttpServletRequest req, HttpServletResponse res) throws Exception {
		if(json == null || json.length() == 0){
			json = portalService.getPortalCfg(pageId);
		}
		req.setAttribute("picinfo", picinfo);
		JSONObject obj = (JSONObject)JSON.parse(json);
		String mvId = "mv_" + obj.getString("id");
		MVContext mv = ExtContext.getInstance().getMVContext(mvId);
		
		CompPreviewService ser = new CompPreviewService(req, res, req.getServletContext());
		ser.setParams(mv.getMvParams());
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
		req.setAttribute(ExtConstants.isExporting, "true");  //导出状态
		
		if("html".equals(type)){
			String ret = ser.buildMV(mv, req.getServletContext());
			String html = RSBIUtils.htmlPage(ret, RSBIUtils.getConstant("resPath"), "report");
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
	}
	
	@RequestMapping(value="/print.action")
	public String print(String pageId, String pageInfo, HttpServletRequest req, HttpServletResponse res) throws Exception {
		if(pageInfo == null || pageInfo.length() == 0){
			pageInfo = portalService.getPortalCfg(pageId);
		}
		if(pageInfo == null){
			return null;
		}
		JSONObject obj = (JSONObject)JSON.parse(pageInfo);

		String mvId = "mv_" + obj.getString("id");
		MVContext mv = ExtContext.getInstance().getMVContext(mvId);
		
		CompPreviewService ser =  new CompPreviewService(req, res, req.getServletContext());
		ser.setParams(mv.getMvParams());
		ser.initPreview();
		String ret = ser.buildMV(mv, req.getServletContext());
		req.setAttribute("str", ret);
		return "portal/PortalIndex-print";
	}
	
	@RequestMapping(value="/getReportJson.action")
	public @ResponseBody
    Object getReportJson(String reportId) {
		return portalService.getPortalCfg(reportId);
	}
}
