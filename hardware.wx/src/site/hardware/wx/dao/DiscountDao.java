package site.hardware.wx.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import site.hardware.wx.bean.Discount;

@Repository
public class DiscountDao {

	/**
	 * 自动装配的数据库链接模板
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int insert(Discount d){
		String sql = "insert into tbl_discount(line,reduce) values(?,?)";
		Object[] param = new Object[] {d.getLine(), d.getReduce()};
		return jdbcTemplate.update(sql, param);
	}

	public List<Discount> select(){
		String sql = "select id,line,reduce from tbl_discount order by line asc";
		return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<Discount>(Discount.class));
	}

	public int delete(int id){
		String sql = "delete from tbl_discount where id=?";
		Object[] param = new Object[] {id};
		return jdbcTemplate.update(sql, param);
	}

	public int update(Discount d){
		String sql = "update tbl_discount set line=?,reduce=? where id=?";
		Object[] param = new Object[] {d.getLine(), d.getReduce(), d.getId()};
		return jdbcTemplate.update(sql, param);
	}
}
