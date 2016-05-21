package site.hardware.wx.dao;

import org.springframework.beans.factory.annotation.Autowired;
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

	public int token(Manager m){
		String sql = "update tbl_manager set token=? where name=? and pw=?";
		Object[] param = new Object[] {m.getToken(), m.getName(), m.getPw()};
		return jdbcTemplate.update(sql, param);
	}
}
