package site.hardware.wx.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import site.hardware.wx.bean.Goods;

@Repository
public class GoodsDao {

	/**
	 * 自动装配的数据库链接模板
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int insert(Goods g){
		String sql = "insert into tbl_goods(name,lastBy,category1,category2,price,dummyPrice,param,note) values(?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] {g.getName(), g.getLastBy(), g.getCategory1(), g.getCategory2(), g.getPrice(), g.getDummyPrice(), g.getParam(), g.getNote()};
		return jdbcTemplate.update(sql, params);
	}

	public Goods select(int id){
		String sql = "select id,name,category1,category2,price,dummyPrice,param,note,sales from tbl_goods where id=?";
		Object[] param = new Object[] {id};
		try{
			return jdbcTemplate.queryForObject(sql, param, new BeanPropertyRowMapper<Goods>(Goods.class));
		}catch(IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	public int update(Goods g){
		String sql = "update tbl_goods set name=?,lastBy=?,lastTime=getdate(),category1=?,category2=?,price=?,dummyPrice=?,param=?,note=? where id=?";
		Object[] params = new Object[] {g.getName(), g.getLastBy(), g.getCategory1(), g.getCategory2(), g.getPrice(), g.getDummyPrice(), g.getParam(), g.getNote(), g.getId()};
		return jdbcTemplate.update(sql, params);
	}

	public int status(int id){
		String sql = "update tbl_goods set status=1-status where id=?";
		Object[] params = new Object[] {id};
		return jdbcTemplate.update(sql, params);
	}

	public int sales(int id, int sales){
		String sql = "update tbl_goods set sales=sales+? where id=?";
		Object[] params = new Object[] {sales, id};
		return jdbcTemplate.update(sql, params);
	}
}
