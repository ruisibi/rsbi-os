/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有 
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.frame;

import com.ruisi.ext.engine.dao.DaoHelper;
import com.ruisitech.bi.entity.common.BaseEntity;
import com.ruisitech.bi.entity.common.Result;
import com.ruisitech.bi.entity.frame.Menu;
import com.ruisitech.bi.mapper.frame.MenuMapper;
import com.ruisitech.bi.util.RSBIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuService {
	
	@Autowired
	private MenuMapper mapper;
	
	@Autowired
	private DaoHelper daoHelper;

	private String dbName = RSBIUtils.getConstant("dbName");

	/**
	 * 查询用户PC端菜单
	 * @param userId
	 * @return
	 */
	public List<Menu> listUserMenus(Integer userId){
		List<Menu> menuList = mapper.listUserMenus(userId);
		List<Menu> roots = this.findMenuChildren(0, menuList);
		//过滤移动端菜单
		for(int i=0; i<roots.size(); i++){
			Menu root = roots.get(i);
			Integer id = root.getMenuId();
			List<Menu> subList = findMenuChildren(id, menuList);
			root.setChildren(subList);  //菜单只支持3级
			//查询第三级
			for(int j=0; j<subList.size(); j++){
				Menu sub = subList.get(j);
				Integer subId = sub.getMenuId();
				sub.setChildren(findMenuChildren(subId, menuList));
			}
		}
		return roots;
	}

	private List<Menu> findMenuChildren(Integer pid, List<Menu> menuList){
		List<Menu> ret = new ArrayList<Menu>();
		for(int i=0; i<menuList.size(); i++){
			Menu m = (Menu)menuList.get(i);
			Integer value = m.getMenuPid();
			if(value != null && value.equals(pid) ){
				ret.add(m);
			}
		}
		return ret;
	}
	
	public Menu getById(Integer menuId) {
		return mapper.getById(menuId);
	}
	
	public List<Map<String, Object>> listMenuByPid(Integer pid){
		List<Map<String, Object>> ls = mapper.listMenuByPid(pid);
		for(Map<String, Object> dt : ls) {
			if("true".equals(dt.get("children"))) {
				dt.put("children", true);
				dt.put("icon", "fa fa-folder-o");
			}else {
				dt.put("children", false);
			}
		}
		return ls;
	}
	
	public void saveMenu(Menu m) {
		String idSql = "select max(menu_id) from sc_menu";
		Integer maxId = (Integer)daoHelper.queryForObject(idSql, Integer.class);
		final int mid = maxId.intValue() + 1;
		final Integer pid = m.getMenuPid();
		String sql="insert into sc_menu(menu_id,menu_pid,menu_name,menu_desc,menu_date,menu_order,menu_url, avatar) values(?,?,?,?,";
		sql +=  dbName.equals("mysql")?"now()":"strftime('%s','now') * 1000";
		sql += ",?,?,?)";
		daoHelper.execute(sql, new PreparedStatementCallback<Object>(){
			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setInt(1, mid);
				ps.setString(3, m.getMenuName());
				ps.setString(4, m.getMenuDesc());
				ps.setInt(2, pid);
				ps.setInt(5, m.getMenuOrder());
				ps.setString(6, m.getMenuUrl());
				ps.setString(7, m.getAvatar());
				ps.executeUpdate();
				return null;
			}
		});
		m.setMenuId(mid);
	}

	public void updateMenu(Menu menu) {
		String dt = dbName.equals("mysql")?"now()":"strftime('%s','now') * 1000";
		String sql="update sc_menu set menu_name=?,menu_desc=?,menu_date="+dt+",menu_order=?,menu_url=?,avatar=? where menu_id=?";
		daoHelper.execute(sql, new PreparedStatementCallback<Object>(){
			@Override
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setInt(3, menu.getMenuOrder());
				ps.setString(2, menu.getMenuDesc());
				ps.setString(1, menu.getMenuName());
				ps.setString(4, menu.getMenuUrl());
				ps.setString(5, menu.getAvatar());
				ps.setInt(6, menu.getMenuId());	
				ps.executeUpdate();
				return null;
			}
		});
	}
	
	@Transactional(rollbackFor = Exception.class)
	public Result deleteMenu(Integer id) {
		Result r = new Result();
		String chkSql = "select count(*) from sc_menu where menu_pid = "+id;
		BigDecimal ct = (BigDecimal)daoHelper.queryForObject(chkSql, BigDecimal.class);
		if(ct.intValue() > 0){
			r.setResult(0);
			r.setMsg("该菜单下可能含有子菜单，不能删除。");
			return r;
		}
		
		String sql = "delete from sc_menu where menu_id = "+id;
		daoHelper.execute(sql);
		
		//删除菜单角色关系
		daoHelper.execute("delete from role_menu_rela where menu_id = " + id);
		//删除菜单用户关系
		daoHelper.execute("delete from user_menu_rela where menu_id = " + id);
		r.setResult(1);
		return r;
	}
}
