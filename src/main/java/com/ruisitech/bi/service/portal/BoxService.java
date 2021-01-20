package com.ruisitech.bi.service.portal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rsbi.ext.engine.ExtConstants;
import com.rsbi.ext.engine.init.TemplateManager;
import com.rsbi.ext.engine.util.IdCreater;
import com.rsbi.ext.engine.view.context.Element;
import com.rsbi.ext.engine.view.context.MVContext;
import com.rsbi.ext.engine.view.context.MVContextImpl;
import com.rsbi.ext.engine.view.context.form.InputField;
import com.rsbi.ext.engine.view.context.html.*;
import com.rsbi.ext.engine.view.emitter.chart.ChartUtils;
import com.ruisitech.bi.entity.bireport.KpiDto;
import com.ruisitech.bi.entity.portal.BoxQuery;
import com.ruisitech.bi.service.bireport.BaseCompService;
import com.ruisitech.bi.service.bireport.ModelCacheService;
import com.ruisitech.bi.util.RSBIUtils;
import com.ruisitech.ext.service.DataControlInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Scope("prototype")
public class BoxService extends BaseCompService {
	
	public final static String deftMvId = "mv.portal.box";
	
	private Map<String, InputField> mvParams = new HashMap<String, InputField>(); //mv的参数

	@Autowired
	private DataControlInterface dataControl; //数据权限控制
	
	@Autowired
	private ModelCacheService cacheService;
	
	public @PostConstruct void init() {
		
	}  
	
	public @PreDestroy void destory() {
		mvParams.clear();
	}
	
	public MVContext json2MV(BoxQuery box) throws Exception{
		//创建MV
		MVContext mv = new MVContextImpl();
		mv.setChildren(new ArrayList<Element>());
		String formId = ExtConstants.formIdPrefix + IdCreater.create();
		mv.setFormId(formId);
		mv.setMvid(deftMvId);
		
		//处理参数,把参数设为hidden
		super.parserHiddenParam(box.getPortalParams(), mv, mvParams);	
		
		this.json2Box(box, mv);
		
		super.createDsource(this.cacheService.getDsource(box.getDsid()), mv);
		
		return mv;
	}
	
	/**
	 * 通过数据生成 box 块
	 * @param mv
	 * @throws IOException 
	 */
	public void json2Box(BoxQuery box, Element mv) throws IOException{
		if(box.getKpiJson()== null){
			return;
		}

		//创建box 的 data 标签
		String sql = createSql(box);
		DataContext data = new DataContextImpl();
		data.setKey("k" + box.getKpiJson().getKpi_id());
		data.setRefDsource(box.getDsid());
		String name = TemplateManager.getInstance().createTemplate(sql);
		data.setTemplateName(name);
		mv.getChildren().add(data);
		data.setParent(mv);

		KpiDto kpi = box.getKpiJson();
		TextContext text = new TextContextImpl();
		Integer id = box.getKpiJson().getKpi_id();
		String alias = box.getKpiJson().getAlias();
		String p1 = id+"."+alias;
		String alias2 = box.getKpiJson().getAlias()+"_sq";
		String p2 = id + "." + alias2;
		String fmt =box.getKpiJson().getFmt();
		Integer rate = box.getKpiJson().getRate();
		String s = "";
		if(rate != null){
			s += ChartUtils.writerUnit(new Integer(rate.toString()));
		}
		String unit = box.getKpiJson().getUnit();
		if(unit == null){
			unit = "";
		}
		unit = s + unit;
		String str = " {\"trueValue\":"+"$!k"+p1+", ";
		str += "value:\"$extUtils.numberFmt($!k"+p1+", '"+(fmt==null?"":fmt)+"')"+unit+"\", ";
		str += "alias:\""+alias+"\", ";
		if(kpi.getTfontsize() != null){
			str += "fontsize:"+kpi.getTfontsize()+",";
		}
		if(kpi.getTfontcolor() != null && kpi.getTfontcolor().length() > 0){
			str += "fontcolor:'"+kpi.getTfontcolor()+"',";
		}
		str += "desc:\""+kpi.getKpi_name()+"\"}";
		String word = TemplateManager.getInstance().createTemplate(str);
		text.setTemplateName(word);
		mv.getChildren().add(text);
		text.setParent(mv);
	}
	
	public String createSql(BoxQuery box){
		JSONObject dset = this.cacheService.getDset(box.getDsetId());
		Map<String, String> tableAlias = createTableAlias(dset);
		KpiDto kpi = box.getKpiJson();
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		Integer rate = kpi.getRate();
		if(kpi.getCalc() != null && kpi.getCalc() == 1){  //表达式，直接取表达式
			sql.append(kpi.getCol_name());
		}else{  //获取字段别名
			String name = super.convertKpiName(kpi, tableAlias);
			sql.append( name);
		}
		if(rate != null){
			sql.append("/" + rate);
		}
		sql.append(" as ");
		sql.append(kpi.getAlias());
		
		JSONArray joinTabs = (JSONArray)dset.get("joininfo");
		String master = dset.getString("master");
		sql.append(" from " + master + " a0");
		
		for(int i=0; joinTabs!=null&&i<joinTabs.size(); i++){  //通过主表关联
			JSONObject tab = joinTabs.getJSONObject(i);
			String ref = tab.getString("ref");
			String refKey = tab.getString("refKey");
			String jtype = (String)tab.get("jtype");
			if("left".equals(jtype) || "right".equals(jtype)){
				sql.append(" " + jtype);
			}
			sql.append(" join " + ref+ " " + tableAlias.get(ref));
			sql.append(" on a0."+tab.getString("col")+"="+tableAlias.get(ref)+"."+refKey);
			sql.append(" ");
		}
		sql.append(" where 1=1 ");
		
		if(dataControl != null){
			String ret = dataControl.process(RSBIUtils.getLoginUserInfo(), (String)dset.get("master"));
			if(ret != null){
				sql.append(ret + " ");
			}
		}
		sql.append(" " + dealCubeParams(box.getParams(), tableAlias));
		return sql.toString().replaceAll("@", "'");
	}

	public Map<String, InputField> getMvParams() {
		return mvParams;
	}
}
