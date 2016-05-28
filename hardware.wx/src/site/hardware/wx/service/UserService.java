package site.hardware.wx.service;

import java.util.Hashtable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.hardware.wx.bean.User;
import site.hardware.wx.dao.UserDao;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;

	private Hashtable<Integer, String> tokens = new Hashtable<Integer, String>();

	public boolean permission(int uid, String openid){
		if(tokens.containsKey(uid)){
			return tokens.get(uid).equals(openid);
		}else{
			User u = login(openid);
			return u != null && uid == u.getId();
		}
	}

	public User login(String openid){
		User u = userDao.select(openid);
		if(u != null) tokens.put(u.getId(), u.getOpenid());
		return u;
	}

	public User reg(String openid, String name){
		User u = new User();
		u.setName(name);
		u.setOpenid(openid);
		int id = userDao.insert(u);
		if(id != 0){
			u.setId(id);
			u.setPoint(0);
			tokens.put(id, openid);
			return u;
		}
		return null;
	}
}
