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
			User u = userDao.select(openid);
			tokens.put(u.getId(), u.getOpenid());
			return uid == u.getId();
		}
	}
}
