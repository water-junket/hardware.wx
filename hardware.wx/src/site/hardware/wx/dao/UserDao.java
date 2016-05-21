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
		String sql = "insert into tbl_user(name,openid) values(?,?)";
		Object[] param = new Object[] {u.getName(), u.getOpenid()};
		try{
			jdbcTemplate.update(sql, param);
			sql = "select ident_current('tbl_user')";
			return jdbcTemplate.queryForObject(sql, Integer.class);
		}catch(DataAccessException e){
			return 0;
		}
	}

	public User select(String openid){
		String sql = "select id,name,openid from tbl_user where openid=?";
		Object[] param = new Object[] {openid};
		try{
			return jdbcTemplate.queryForObject(sql, param, new BeanPropertyRowMapper<User>(User.class));
		}catch(IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

}
