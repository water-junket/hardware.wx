package site.hardware.wx.dao;

import java.util.List;

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
		String sql = "insert into tbl_user(name,pw,token,tel) values(?,?,?,?)";
		Object[] param = new Object[] {u.getName(), u.getPw(), u.getToken(), u.getTel()};
		try{
			jdbcTemplate.update(sql, param);
			sql = "select ident_current('tbl_user')";
			return jdbcTemplate.queryForObject(sql, Integer.class);
		}catch(DataAccessException e){
			return 0;
		}
	}

	public User select(String tel){
		String sql = "select id,name,pw,point,token,tel from tbl_user where tel=?";
		Object[] param = new Object[] {tel};
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

	public List<User> select(int begin, int end){
		String sql = "select id,name,pw,point,token,tel,consume from (select id,name,pw,point,token,tel,consume,row_number() over(order by id asc) as rn from tbl_user) as t where rn between ? and ?";
		Object[] param = new Object[] {begin, end};
		try{
			return jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<User>(User.class));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public int count(){
		String sql = "select count(1) from tbl_user";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	public int pw(User u){
		String sql = "update tbl_user set pw=? where id=?";
		Object[] param = new Object[] {u.getPw(), u.getId()};
		return jdbcTemplate.update(sql, param);
	}

	public int point(int id, int point){
		String sql = "update tbl_user set point=point-? where id=?";
		Object[] param = new Object[] {point, id};
		return jdbcTemplate.update(sql, param);
	}

	public int consume(int id, int consume){
		String sql = "update tbl_user set consume=consume+?,point=point+? where id=?";
		Object[] param = new Object[] {consume, consume, id};
		return jdbcTemplate.update(sql, param);
	}
}
