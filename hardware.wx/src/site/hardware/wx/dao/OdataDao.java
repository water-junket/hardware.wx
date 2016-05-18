package site.hardware.wx.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import site.hardware.wx.bean.Goods;
import site.hardware.wx.bean.Odata;

@Repository
public class OdataDao {

	/**
	 * 自动装配的数据库链接模板
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int insert(Odata o){
		String sql = "insert into tbl_odata(name,parent,category,value) values(?,?,?,?)";
		Object[] params = new Object[] {o.getName(), o.getParent(), o.getCategory(), o.getValue()};
		try{
			return jdbcTemplate.update(sql, params);
		}catch(DataAccessException e){
			return 0;
		}
	}

	public List<Odata> select(int id){
		String sql = "select id,name,parent,category,value from tbl_odata where parent=?";
		Object[] param = new Object[] {id};
		return jdbcTemplate.query(sql, param, new BeanPropertyRowMapper<Odata>(Odata.class));
	}

	public List<Odata> select(String category){
		String sql = "select id,name,parent,category,value from tbl_odata where category=? and parent=-1";
		Object[] param = new Object[] {category};
		return jdbcTemplate.query(sql, param, new BeanPropertyRowMapper<Odata>(Odata.class));
	}

	public int update(Odata o){
		String sql = "update tbl_odata set name=? where id=?";
		Object[] params = new Object[] {o.getName(), o.getId()};
		return jdbcTemplate.update(sql, params);
	}
}
