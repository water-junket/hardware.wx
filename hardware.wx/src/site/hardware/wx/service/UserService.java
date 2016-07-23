package site.hardware.wx.service;

import java.util.Hashtable;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.hardware.wx.bean.User;
import site.hardware.wx.dao.UserDao;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;

	private Hashtable<Integer, String> tokens = new Hashtable<Integer, String>();

	public boolean permission(User u){
		if(tokens.containsKey(u.getId())){
			return tokens.get(u.getId()).equals(u.getToken());
		}else{
			User user = userDao.select(u.getName());
			if(user != null && u.getToken().equals(user.getToken())){
				tokens.put(user.getId(), user.getToken());
				return true;
			}
			return false;
		}
	}

	public User login(User u){
		User user = userDao.select(u.md5().getName());
		if(user != null && u.getPw().equals(user.getPw())){
			String token=UUID.randomUUID().toString();
			user.setToken(token);
			userDao.token(user);
			tokens.put(user.getId(), token);
			return user;
		}
		return u;
	}

	public User reg(User u){
		String token=UUID.randomUUID().toString();
		u.setToken(token);
		int id = userDao.insert(u.md5());
		if(id != 0){
			u.setId(id);
			u.setPoint(0);
			tokens.put(id, token);
		}
		return u;
	}
}
