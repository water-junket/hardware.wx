package site.hardware.wx.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import site.hardware.wx.bean.Receiver;

@Repository
public class ReceiverDao {

	/**
	 * 自动装配的数据库链接模板
	 */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public int insert(Receiver r){
		String sql = "insert into tbl_receiver(uid,name,tel,address) values(?,?,?,?)";
		Object[] param = new Object[] {r.getUid(), r.getName(), r.getTel(), r.getAddress()};
		try{
			return jdbcTemplate.update(sql, param);
		}catch(DataAccessException e){
			return 0;
		}
	}

	public List<Receiver> select(int uid){
		String sql = "select id,uid,name,tel,address from tbl_receiver where uid=?";
		Object[] param = new Object[] {uid};
		return jdbcTemplate.query(sql, param, new BeanPropertyRowMapper<Receiver>(Receiver.class));
	}

	public int delete(int id){
		String sql = "delete from tbl_receiver where id=?";
		Object[] param = new Object[] {id};
		return jdbcTemplate.update(sql, param);
	}
}
