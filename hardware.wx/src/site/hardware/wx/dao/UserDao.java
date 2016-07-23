package site.hardware.wx.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import site.hardware.wx.bean.User;

@Repository
public class UserDao {

	/**
	 * 自动装配的数据库链接模板
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int insert(User u){
		String sql = "insert into tbl_user(name,pw,token) values(?,?,?)";
		Object[] param = new Object[] {u.getName(), u.getPw(), u.getToken()};
		try{
			jdbcTemplate.update(sql, param);
			sql = "select ident_current('tbl_user')";
			return jdbcTemplate.queryForObject(sql, Integer.class);
		}catch(DataAccessException e){
			return 0;
		}
	}

	public User select(String name){
		String sql = "select id,name,pw,point,token from tbl_user where name=?";
		Object[] param = new Object[] {name};
		try{
			return jdbcTemplate.queryForObject(sql, param, new BeanPropertyRowMapper<User>(User.class));
		}catch(IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	public int token(User u){
		String sql = "update tbl_user set token=? where id=?";
		Object[] param = new Object[] {u.getToken(), u.getId()};
		return jdbcTemplate.update(sql, param);
	}

	public int permission(User u){
		String sql = "select count(id) from tbl_user where token=? and id=?";
		Object[] param = new Object[] {u.getToken(), u.getId()};
		try{
			return jdbcTemplate.queryForObject(sql, param, Integer.class);
		}catch(IncorrectResultSizeDataAccessException e){
			return -1;
		}
	}
}
