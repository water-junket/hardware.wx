package site.hardware.wx.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import site.hardware.wx.bean.Img;

@Repository
public class ImgDao {

	/**
	 * 自动装配的数据库链接模板
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int insert(Img i){
		String sql = "insert into tbl_img(gid,oname,ctype,type) values(?,?,?,?)";
		Object[] param = new Object[] {i.getGid(), i.getOname(), i.getCtype(), i.getType()};
		try{
			if (jdbcTemplate.update(sql, param) == 0) return 0;
			sql = "select ident_current('tbl_img')";
			return jdbcTemplate.queryForObject(sql, Integer.class);
		}catch(DataAccessException e){
			return 0;
		}
	}

	public int delete(int id){
		String sql = "delete from tbl_img where id=?";
		Object[] param = new Object[] {id};
		return jdbcTemplate.update(sql, param);
	}

	public Img select(int id){
		String sql = "select id,gid,oname,ctype,type from tbl_img where id=?";
		Object[] param = new Object[] {id};
		return jdbcTemplate.queryForObject(sql, param, new BeanPropertyRowMapper<Img>(Img.class));
	}

	public List<Img> selectList(int gid){
		String sql = "select id,gid,oname,ctype,type from tbl_img where gid=? order by type desc";
		Object[] param = new Object[] {gid};
		return jdbcTemplate.query(sql, param, new BeanPropertyRowMapper<Img>(Img.class));
	}
}
