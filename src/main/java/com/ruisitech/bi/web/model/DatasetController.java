package com.ruisitech.bi.web.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruisitech.bi.entity.model.Dataset;
import com.ruisitech.bi.service.model.DataSourceService;
import com.ruisitech.bi.service.model.DatasetService;
import com.ruisitech.bi.util.BaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/model")
public class DatasetController extends BaseController {
	
	@Autowired
	private DatasetService service;
	
	@Autowired
	private DataSourceService dsservice;

	private static Logger logger = Logger.getLogger(DatasetController.class);
	
	@RequestMapping(value="/listDataset.action")
	public @ResponseBody
    Object list(){
		return super.buildSucces(service.listDataset());
	}
	
	@RequestMapping(value="/listTables.action")
	public @ResponseBody
    Object listTables(String dsid, String tname) throws Exception{
		return super.buildSucces(dsservice.listTables(dsid, tname));
	}
	
	@RequestMapping(value="/listTableColumns.action")
	public @ResponseBody
    Object listTableColumns(String dsid, String tname) throws Exception {
		return super.buildSucces(service.listTableColumns(dsid, tname));
	}
	
	@RequestMapping(value="/queryDatasetMeta.action", method = RequestMethod.POST)
	public @ResponseBody
    Object queryDatasetMeta(String cfg, String dsid) throws Exception {
		JSONObject dset = (JSONObject)JSON.parse(cfg);
		return super.buildSucces(service.queryMetaAndIncome(dset, dsid));
	}

	@RequestMapping(value="/queryDataset.action", method = RequestMethod.GET)
	public @ResponseBody
	Object queryDataset(String dsetId, String dsid) {
		try {
			return super.buildSucces(service.queryDsetDatas(dsid, dsetId));
		}catch (Exception ex){
			logger.error("查询出错", ex);
			return super.buildError(ex.getMessage());
		}
	}
	
	@RequestMapping(value="/updateDset.action", method = RequestMethod.POST)
	public @ResponseBody
    Object updateDset(Dataset dset)  {
		service.updateDset(dset);
		return this.buildSucces();
	}
	
	@RequestMapping(value="/saveDset.action", method = RequestMethod.POST)
	public @ResponseBody
    Object saveDset(Dataset dset)  {
		service.insertDset(dset);
		return buildSucces();
	}
	
	@RequestMapping(value="/deleteDset.action")
	public @ResponseBody
    Object deleteDset(String dsetId)  {
		service.deleteDset(dsetId);
		return buildSucces();
	}
	
	@RequestMapping(value="/getDatasetCfg.action")
	public @ResponseBody
    Object getDatasetCfg(String dsetId)  {
		String ret = service.getDatasetCfg(dsetId);
		return super.buildSucces(JSONObject.parseObject(ret));
	}
	
	@RequestMapping(value="/reloadDset.action")
	public @ResponseBody
    Object reloadDset(String dsetId, String dsid)  {
		try{
			service.reloadDset(dsetId, dsid);
			return super.buildSucces();
		}catch(Exception ex){
			ex.printStackTrace();
			return super.buildError(ex.getMessage());
		}
	}
}
