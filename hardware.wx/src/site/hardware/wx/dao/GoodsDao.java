package site.hardware.wx.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import site.hardware.wx.bean.Goods;

@Repository
public class GoodsDao {
	private static final String FIELDS = "id,name,category1,category2,price,dummyPrice,sales,status,info,act";

	/**
	 * 自动装配的数据库链接模板
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int insert(Goods g){
		String sql = "insert into tbl_goods(name,lastBy,category1,category2,info,act) values(?,?,?,?,?,?)";
		Object[] param = new Object[] {g.getName(), g.getLastBy(), g.getCategory1(), g.getCategory2(), g.getInfo(), g.isAct()?1:0};
		return jdbcTemplate.update(sql, param);
	}

	public Goods select(int id){
		StringBuilder sql = new StringBuilder("select ").append(FIELDS).append(" from tbl_goods where id=?");
		Object[] param = new Object[] {id};
		try{
			return jdbcTemplate.queryForObject(sql.toString(), param, new BeanPropertyRowMapper<Goods>(Goods.class));
		}catch(IncorrectResultSizeDataAccessException e){
			return null;
		}
	}

	public List<Goods> select(int c, int begin, int end){
		StringBuilder sql = new StringBuilder("select ").append(FIELDS).append(" from (select ").append(FIELDS).append(",row_number() over(order by id desc) as rn from tbl_goods where category2=?) as t where rn between ? and ?");
		Object[] param = new Object[] {c, begin, end};
		try{
			return jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<Goods>(Goods.class));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public List<Goods> select(int c, int begin, int end, int status, String ob){
		StringBuilder sql =  new StringBuilder("select ").append(FIELDS).append(",img from (select ").append(FIELDS).append(",img,row_number() over(order by ").append(ob).append(") as rn from v_goods where (category1=? or category2=?) and status=?) as t where rn between ? and ?");
		Object[] param = new Object[] {c, c, status, begin, end};
		try{
			return jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<Goods>(Goods.class));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public List<Goods> select(int c, int begin, int end, int status, String name, String ob){
		StringBuilder sql =  new StringBuilder("select ").append(FIELDS).append(",img from (select ").append(FIELDS).append(",img,row_number() over(order by ").append(ob).append(") as rn from v_goods where (category1=? or category2=?) and status=? and name like ?) as t where rn between ? and ?");
		Object[] param = new Object[] {c, c, status, "%"+name+"%", begin, end};
		try{
			return jdbcTemplate.query(sql.toString(), param, new BeanPropertyRowMapper<Goods>(Goods.class));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public int count(int c){
		String sql = "select count(1) from tbl_goods where category1=? or category2=?";
		Object[] param = new Object[] {c, c};
		return jdbcTemplate.queryForObject(sql, param, Integer.class);
	}

	public int count(int c, int status){
		String sql = "select count(1) from tbl_goods where (category1=? or category2=?) and status=?";
		Object[] param = new Object[] {c, c, status};
		return jdbcTemplate.queryForObject(sql, param, Integer.class);
	}

	public int count(int c, int status, String name){
		String sql = "select count(1) from tbl_goods where (category1=? or category2=?) and status=? and name like ?";
		Object[] param = new Object[] {c, c, status, "%"+name+"%"};
		return jdbcTemplate.queryForObject(sql, param, Integer.class);
	}

	public int update(Goods g){
		String sql = "update tbl_goods set name=?,lastBy=?,lastTime=getdate(),category1=?,category2=?,info=?,act=? where id=?";
		Object[] param = new Object[] {g.getName(), g.getLastBy(), g.getCategory1(), g.getCategory2(), g.getInfo(), g.isAct(), g.getId()};
		return jdbcTemplate.update(sql, param);
	}

	public int status(int id, int mid){
		String sql = "update tbl_goods set status=case (select count(1) from tbl_sub_goods where gid=? and status=1) when 0 then 0 else 1 end where id=?";
		Object[] param = new Object[] {id, id};
		return jdbcTemplate.update(sql, param);
	}

	public int sales(int id, int sales){
		String sql = "update tbl_goods set sales=sales+? where id=?";
		Object[] param = new Object[] {sales, id};
		return jdbcTemplate.update(sql, param);
	}

	public int price(int id){
		String sql = "update tbl_goods set price=isnull((select min(price) from tbl_sub_goods where gid=? and status=1),999999),dummyPrice=isnull((select max(price) from tbl_sub_goods where gid=? and status=1),0) where id=?";
		Object[] param = new Object[] {id, id, id};
		return jdbcTemplate.update(sql, param);
	}
}
