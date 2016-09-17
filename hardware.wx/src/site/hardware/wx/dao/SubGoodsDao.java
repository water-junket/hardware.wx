package site.hardware.wx.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import site.hardware.wx.bean.SubGoods;

@Repository
public class SubGoodsDao {

	/**
	 * 自动装配的数据库链接模板
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int insert(SubGoods s){
		String sql = "insert into tbl_sub_goods(gid,name,price) values(?,?,?)";
		Object[] param = new Object[] {s.getGid(), s.getName(), s.getPrice()};
		return jdbcTemplate.update(sql, param);
	}

	public int update(SubGoods s){
		String sql = "update tbl_sub_goods set name=?,price=? where id=?";
		Object[] param = new Object[] {s.getName(), s.getPrice(), s.getId()};
		return jdbcTemplate.update(sql, param);
	}

	public List<SubGoods> select(int gid){
		String sql = "select id,name,price,img,status from tbl_sub_goods where gid=?";
		Object[] param = new Object[] {gid};
		return jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<SubGoods>(SubGoods.class));
	}

	public List<SubGoods> select(int gid, boolean status){
		String sql = "select id,name,price,img,status from tbl_sub_goods where gid=? and status=?";
		Object[] param = new Object[] {gid, status?1:0};
		return jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<SubGoods>(SubGoods.class));
	}

	public int status(int id, int mid){
		String sql = "update tbl_sub_goods set status=1-status where id=?";
		Object[] param = new Object[] {id};
		return jdbcTemplate.update(sql, param);
	}

	public int sales(int id, int sales){
		String sql = "update tbl_goods set sales=sales+? where id=?";
		Object[] param = new Object[] {sales, id};
		return jdbcTemplate.update(sql, param);
	}

	public int img(int id, String img, int mid){
		String sql = "update tbl_sub_goods set img=? where id=?";
		Object[] param = new Object[] {img, id};
		return jdbcTemplate.update(sql, param);
	}

	public int delImg(int id, int mid){
		String sql = "update tbl_sub_goods set img=? where id=?";
		Object[] param = new Object[] {null, id};
		return jdbcTemplate.update(sql, param);
	}

	public String ctype(int id){
		String sql = "select img from tbl_sub_goods where id=?";
		Object[] param = new Object[] {id};
		return jdbcTemplate.queryForObject(sql, param, String.class);
	}
}
