/*
 * Copyright 2018 本系统版权归成都睿思商智科技有限公司所有 
 * 用户不能删除系统源码上的版权信息, 使用许可证地址:
 * https://www.ruisitech.com/licenses/index.html
 */
package com.ruisitech.bi.service.frame;

import com.rsbi.ext.engine.dao.DaoHelper;
import com.ruisitech.bi.entity.frame.Role;
import com.ruisitech.bi.mapper.frame.RoleMapper;
import com.ruisitech.bi.util.RSBIUtils;
import com.ruisitech.bi.util.TreeInterface;
import com.ruisitech.bi.util.TreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限管理 - 角色管理模块
 * @author gdp
 *
 */
@Service
public class RoleService {
	
	@Autowired
	private RoleMapper mapper;
	
	@Autowired
	private DaoHelper daoHelper;

	private String dbName = RSBIUtils.getConstant("dbName");

	public Map<String, Object> listRoleMenus(Integer roleId){
		List<Map<String, Object>> menus = mapper.listRoleMenus(roleId);
		TreeService tree = new TreeService();
		List<Map<String, Object>> ret = tree.createTreeData(menus, new TreeInterface(){
			@Override
			public void processMap(Map<String, Object> m) {
				Object id2 = m.get("id2");
				if(id2 != null){
					Map<String, Object> state = new HashMap<String, Object>();
					state.put("selected", true);
					m.put("state", state);
				}
				
			}

			@Override
			public void processEnd(Map<String, Object> m, boolean hasChild) {
				if(hasChild){
					m.put("icon", "fa fa-folder-o");
					m.remove("state");
				}else {
					m.put("icon", "fa fa-file-o");
				}
			}
			
		});
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id", "root");
		m.put("text", "系统菜单树");
		m.put("icon", "fa fa-home");
		m.put("children", ret);
		return m;
	}
	
	public List<Map<String, Object>> roledata(Integer roleId){
		List<Map<String, Object>> datas = mapper.roledata(roleId);
		TreeService tree = new TreeService();
		List<Map<String, Object>> ret = tree.createTreeData(datas, new TreeInterface(){
			@Override
			public void processMap(Map<String, Object> m) {
				Object seldata = m.get("seldata");
				if(seldata != null){
					Map<String, Object> state = new HashMap<String, Object>();
					state.put("selected", true);
					m.put("state", state);
				}
			}

			@Override
			public void processEnd(Map<String, Object> m, boolean hasChild) {
				Map<String, Object> attr = new HashMap<String, Object>();
				String tp = (String)m.get("tp");
				attr.put("tp", tp);
				m.put("li_attr", attr);
			}
			
		});
		return ret;
	}
	
	public List<Role> list(String keyword){
		return mapper.list(keyword);
	}
	
	/**
	 * 查询所有角色及用户所有的角色
	 * @param userId
	 * @return
	 */
	public List<Role> listUserRole(Integer userId){
		return mapper.listUserRole(userId);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void addUserRole(List<Integer> roleIds, Integer userId) {
		//删除角色
		daoHelper.execute("delete from role_user_rela where user_id = " + userId);
		for(int i=0; roleIds != null && i<roleIds.size(); i++)
		{
			Integer roleId = roleIds.get(i);
			daoHelper.execute("insert into role_user_rela(user_id,role_id) values("+userId+","+roleId+")");
		}
	}

	public void saveRole(Role role) {
		String dt = dbName.equals("mysql")?"now()":"strftime('%s','now') * 1000";
		String sql = "insert into sc_role(role_name,role_desc,create_date,create_user, ord) values(?,?,"+dt+",?,?)";
		daoHelper.execute(sql, new PreparedStatementCallback<Object>(){
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setString(1, role.getRoleName());
				ps.setString(2, role.getRoleDesc());
				ps.setString(3, RSBIUtils.getLoginUserInfo().getLoginName());
				ps.setInt(4, role.getOrd());

				ps.executeUpdate();
				return null;
			}
		});
	}
	
	public void updateRole(Role role) {
		String sql = "update sc_role set role_name = ?,role_desc = ?, ord=? where role_id = ?";
		daoHelper.execute(sql, new PreparedStatementCallback<Object>(){
			public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
				ps.setInt(4, role.getRoleId());
				ps.setString(1, role.getRoleName());
				ps.setString(2, role.getRoleDesc());
				ps.setInt(3, role.getOrd());
				ps.executeUpdate();
				return null;
			}
		});
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void deleteRole(Integer id) {
		String sql = "delete from sc_role where role_id = " + id ;
		daoHelper.execute(sql);
		//删除角色菜单关系
		daoHelper.execute("delete from role_menu_rela where role_id = " + id);
		//删除角色用户关系
		daoHelper.execute("delete from role_user_rela where role_id = " + id);
	}
	
	public Role getRole(Integer id) {
		return mapper.getById(id);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void roleMenu(String menuIds, Integer roleId) {
		//删除以前数据
		String delSql = "delete from role_menu_rela where role_id = " + roleId;
		daoHelper.execute(delSql);
		
		String[] ids = menuIds.split(",");//处理获取的菜单ID格式
		String sql = "insert into role_menu_rela(role_id, menu_id) values(?,?)";
		for(final String tmp : ids){//这个循环用于循环插入授权数据
			if(tmp.length() > 0){
				daoHelper.execute(sql, new PreparedStatementCallback<Object>(){
					public Object doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
						ps.setInt(1, roleId);
						ps.setInt(2, new Integer(tmp));
						ps.executeUpdate();
						return null;
					}
				});
			}
		}
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void roleDataSave(Integer roleId, String dataIds) {
		//删除以前数据
		String delsql = "delete from role_data_rela where role_id = " + roleId;
		this.daoHelper.execute(delsql);
		if(dataIds == null || dataIds.length() == 0) {
			return;
		}
		String[] datas = dataIds.split(",");
		for(final String data : datas){
			String sql = "insert into role_data_rela(role_id, data_id) values(?,?)";
			this.daoHelper.execute(sql, new PreparedStatementCallback<Object>(){
				@Override
				public Object doInPreparedStatement(PreparedStatement ps)
						throws SQLException, DataAccessException {
					ps.setInt(1, roleId);
					ps.setInt(2, Integer.parseInt(data));
					ps.executeUpdate();
					return null;
				}				
			});
		}
	}
}
