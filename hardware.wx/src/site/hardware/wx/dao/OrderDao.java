package site.hardware.wx.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import site.hardware.wx.bean.Order;

@Repository
public class OrderDao {
	private static final String FIELDS = "id,uid,receive,detail,note,price,payMethod,orderTime,handleTime,endTime,status,annotation";

	/**
	 * 自动装配的数据库链接模板
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int insert(Order o){
		String sql = "insert into tbl_order(id,uid,receive,detail,note,price,payMethod) values(?,?,?,?,?,?,?)";
		Object[] param = new Object[] {o.getId(), o.getUid(), o.getReceive(), o.getDetail(), o.getNote(), o.getPrice(), o.getPayMethod()};
		try{
			return jdbcTemplate.update(sql, param);
		}catch(DataAccessException e){
			return 0;
		}
	}

	public String newest(){
		String sql = "select top 1 id from tbl_order order by orderTime desc";
		try{
			return jdbcTemplate.queryForObject(sql, String.class);
		}catch(IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	public Order select(String id){
		StringBuilder sql = new StringBuilder("select ").append(FIELDS).append(" from tbl_order where id=?");
		Object[] param = new Object[] {id};
		try{
			return jdbcTemplate.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<Order>(Order.class));
		}catch(IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	public List<Order> select(String os, int begin, int end){
		StringBuilder sql = new StringBuilder("select ").append(FIELDS).append(" from (select ").append(FIELDS).append(",row_number() over(order by id desc) as rn from tbl_order").append(os).append(") as t where rn between ? and ?");
		Object[] param = new Object[] {begin, end};
		return jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<Order>(Order.class));
	}

	public int count(String os){
		StringBuilder sql = new StringBuilder("select count(1) from tbl_order").append(os);
		return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
	}

	public List<Order> select(int uid, int begin, int end){
		StringBuilder sql = new StringBuilder("select ").append(FIELDS).append(" from (select ").append(FIELDS).append(",row_number() over(order by id desc) as rn from tbl_order where uid=?) as t where rn between ? and ?");
		Object[] param = new Object[] {uid, begin, end};
		return jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<Order>(Order.class));
	}

	public int count(int uid){
		String sql = "select count(1) from tbl_order where uid=?";
		Object[] param = new Object[] {uid};
		return jdbcTemplate.queryForObject(sql, param, Integer.class);
	}

	public int status(int status, String id, int mid){
		StringBuilder sql = new StringBuilder("update tbl_order set status=?");
		if(status==10) sql.append(",handleTime=getdate()");
		else if(status==20) sql.append(",endTime=getdate()");
		Object[] param = new Object[] {status, id};
		return jdbcTemplate.update(sql.append(" where id=?").toString(), param);
	}

	public int annotation(String annotation, String id, int mid){
		String sql = "update tbl_order set annotation=? where id=?";
		Object[] param = new Object[] {annotation, id};
		return jdbcTemplate.update(sql, param);
	}
}
