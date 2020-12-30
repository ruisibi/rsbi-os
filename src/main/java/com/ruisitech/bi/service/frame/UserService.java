package com.ruisitech.bi.service.frame;

import com.rsbi.ext.engine.dao.DaoHelper;
import com.ruisitech.bi.entity.frame.User;
import com.ruisitech.bi.mapper.frame.UserMapper;
import com.ruisitech.bi.util.RSBIUtils;
import com.ruisitech.bi.util.TreeInterface;
import com.ruisitech.bi.util.TreeService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
	
	@Autowired
	private UserMapper mapper;

	@Autowired
	private DaoHelper daoHelper;

	public User getUserByStaffId(String staffId){
		return mapper.getUserByStaffId(staffId);
	}

	public List<User> listUsers(String keyword){
		return mapper.listUsers(keyword);
	}


	public User getUserByUserId(Integer userId){
		return mapper.getUserById(userId);
	}
	
	public void updateLogDateAndCnt(Integer userId){
		mapper.updateLogDateAndCnt(userId, RSBIUtils.getConstant("dbName"));
	}
	
	public void modPsd(User u){
		mapper.modPsd(u);
	}
	
	public String checkPsd(Integer userId){
		return mapper.checkPsd(userId);
	}
	
	public Map<String, Object> appUserinfo(Integer userId){
		return mapper.appUserinfo(userId);
	}
	
	public String shiroLogin(String userName, String password){
		UsernamePasswordToken token = new UsernamePasswordToken(userName, password, null);   
	    token.setRememberMe(true);
	    // shiro登陆验证  
	    try {  
	        SecurityUtils.getSubject().login(token);  
	    } catch (UnknownAccountException ex) {  
	        return "账号不存在或者密码错误！";  
	    } catch (IncorrectCredentialsException ex) {  
	        return "账号不存在或者密码错误！";  
	    } catch (AuthenticationException ex) {
	    	String ret = null;
	    	Throwable t = ex;
	    	while(true){
	    		t = t.getCause();
	    		if(t == null || t.getCause() == null){
	    			ret = t.getMessage();
	    			break;
	    		}
	    	}
	    	return "系统错误：" + ret;
	    } catch (Exception ex) {  
	        ex.printStackTrace();  
	        return "内部错误，请重试！";  
	    }
	    return "SUC";
	}

	public String saveUser(User u) {
		int cnt = mapper.userExist(u.getStaffId());
		if(cnt > 0){
			return "账号已经存在。";
		}
		u.setUserId(mapper.maxUserId());
		u.setPassword(RSBIUtils.getMD5(u.getPassword().getBytes()));
		mapper.insertuser(u);
		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteUser(Integer userId) {
		//删除用户
		daoHelper.execute("delete from sc_login_user where user_id = " + userId);
		//删除用户菜单关系
		daoHelper.execute("delete from user_menu_rela where user_id = " + userId);
		//删除用户角色关系
		daoHelper.execute("delete from role_user_rela where user_id = " + userId);
	}

	public User getUserById(Integer userId) {
		return mapper.getUserById(userId);
	}

	public void updateUser(User u) {
		mapper.updateuser(u);
		if(u.getPassword() != null && u.getPassword().length() > 0) {
			//修改密码
			u.setPassword(RSBIUtils.getMD5(u.getPassword().getBytes()));
			mapper.modPsd(u);
		}
	}

	public Map<String, Object> listUserMenus(Integer userId){
		List<Map<String, Object>> ls = mapper.listUserMenus(userId);
		TreeService ser = new TreeService();
		List<Map<String, Object>> ret = ser.createTreeData(ls, new TreeInterface(){

			@Override
			public void processMap(Map<String, Object> m) {
				Map<String, Object> state = new HashMap<String, Object>();
				String chk2 = m.get("id2") == null ? "" : m.get("id2").toString();
				if(chk2 == null || chk2.length() == 0){
					//id3为用户所拥有的菜单，需要判断是否checked
					String chk3 = m.get("id3") == null ? "" : m.get("id3").toString();
					if(chk3 == null || chk3.length() == 0){
						state.put("selected", false);
					}else{
						state.put("selected", true);
					}
					state.put("disabled", false);
				}else{
					state.put("disabled", true);
					state.put("selected", true);
				}
				m.put("state", state);
				//设置属性
				Map<String, Object> attributes = new HashMap<String, Object>();
				m.put("li_attr", attributes);
				attributes.put("id2", m.get("id2"));
				attributes.put("id3", m.get("id3"));
				attributes.put("disabled", m.get("disabled"));
			}

			@Override
			public void processEnd(Map<String, Object> m, boolean hasChild) {
				String chk3 = m.get("id3") == null ? "" : m.get("id3").toString();
				if(hasChild && chk3 != null && chk3.length() > 0){
					m.remove("checked");
				}
				if(hasChild) {
					m.put("icon", "fa fa-folder-o");
				}else {
					m.put("icon", "fa fa-file-o");
				}
			}

		});
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id", "root");
		m.put("text", "系统菜单树");
		m.put("icon", " fa fa-home");
		Map<String, Object> state = new HashMap<String, Object>();
		state.put("disabled", true);
		m.put("state", state);
		m.put("children", ret);

		return m;
	}

	/**
	 * 给用户授权菜单
	 * @param userId
	 * @param menuIds
	 */
	@Transactional(rollbackFor = Exception.class)
	public void saveUserMenu(Integer userId, String menuIds) {
		//删除以前数据
		String delSql = "delete from user_menu_rela where user_id = " + userId ;
		daoHelper.execute(delSql);

		String[] ids = menuIds.split(",");
		String sql = "insert into user_menu_rela(user_id, menu_id) values(?,?)";
		for(final String tmp : ids){
			if(tmp.length() > 0){
				daoHelper.execute(sql, ps -> {
					ps.setInt(1, userId);
					ps.setInt(2, new Integer(tmp));
					ps.executeUpdate();
					return null;
				});
			}
		}
	}
}
