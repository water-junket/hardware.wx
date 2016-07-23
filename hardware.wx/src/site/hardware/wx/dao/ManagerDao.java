package site.hardware.wx.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import site.hardware.wx.bean.Manager;

@Repository
public class ManagerDao {

	/**
	 * 自动装配的数据库链接模板
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Manager login(Manager m){
		String sql = "select id,name,permission from tbl_manager where pw=? and name=?";
		Object[] param = new Object[] {m.getPw(), m.getName()};
		try{
			return jdbcTemplate.queryForObject(sql, param, new BeanPropertyRowMapper<Manager>(Manager.class));
		}catch(IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	public int token(Manager m){
		String sql = "update tbl_manager set token=? where id=?";
		Object[] param = new Object[] {m.getToken(), m.getId()};
		return jdbcTemplate.update(sql, param);
	}

	public int permission(Manager m){
		String sql = "select permission from tbl_manager where id=? and token=?";
		Object[] param = new Object[] {m.getId(), m.getToken()};
		try{
			return jdbcTemplate.queryForObject(sql, param, Integer.class);
		}catch(IncorrectResultSizeDataAccessException e){
			return -1;
		}
	}
}
