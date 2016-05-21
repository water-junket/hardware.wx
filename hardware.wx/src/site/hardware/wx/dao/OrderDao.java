package site.hardware.wx.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import site.hardware.wx.bean.Order;

@Repository
public class OrderDao {

	/**
	 * 自动装配的数据库链接模板
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int insert(Order o){
		String sql = "insert into tbl_order(id,uid,receive,detail,note,price,payMethod) values(?,?,?,?,?,?,?)";
		Object[] param = new Object[] {o.getId(), o.getUid(), o.getReceive(), o.getDetail(), o.getNote(), o.getPrice(), o.getPayMethod()};
		return jdbcTemplate.update(sql, param);
	}

	public String newest(){
		String sql = "select top 1 id from tbl_order order by orderTime desc";
		return jdbcTemplate.queryForObject(sql, String.class);
	}

	public Order select(String id){
		String sql = "select id,uid,receive,detail,note,price,payMethod,orderTime,handleTime,endTime,status,annotation from tbl_order where id=?";
		Object[] param = new Object[] {id};
		try{
			return jdbcTemplate.queryForObject(sql, param, new BeanPropertyRowMapper<Order>(Order.class));
		}catch(IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	public int status(int status, int id){
		StringBuilder sql = new StringBuilder("update tbl_order set status=?");
		if(status==10) sql.append(",handleTime=getdate()");
		else if(status==20) sql.append(",endTime=getdate()");
		Object[] param = new Object[] {status, id};
		return jdbcTemplate.update(sql.append(" where id=?").toString(), param);
	}

	public int annotation(String annotation, int id){
		String sql = "update tbl_order set annotation=? where id=?";
		Object[] param = new Object[] {annotation, id};
		return jdbcTemplate.update(sql, param);
	}
}
