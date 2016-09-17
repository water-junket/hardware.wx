package site.hardware.wx.service;

import java.util.Hashtable;
import java.util.List;
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
			User user = userDao.select(u.getTel());
			if(user != null && u.getToken().equals(user.getToken())){
				tokens.put(user.getId(), user.getToken());
				return true;
			}
			return false;
		}
	}

	public User login(User u){
		User user = userDao.select(u.md5().getTel());
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

	public boolean edit(User u){
		return userDao.pw(u.md5()) == 1;
	}

	public boolean reset(int id, String tel){
		User u=new User();
		u.setId(id);
		u.setPw("123456");
		u.setTel(tel);
		return edit(u);
	}

	public List<User> list(int page, int step){
		return userDao.select(page * step - step + 1, page * step);
	}

	public int countPages(int step){
		int count = userDao.count();
		return count / step + (count % step > 0 ? 1 : 0);
	}

	/**
	 * @param id
	 * @param point
	 * @return
	 * @see site.hardware.wx.dao.UserDao#point(int, int)
	 */
	public boolean point(int id, int point) {
		return userDao.point(id, point) == 1;
	}

	/**
	 * @param id
	 * @param consume
	 * @return
	 * @see site.hardware.wx.dao.UserDao#consume(int, int)
	 */
	public boolean consume(int id, int consume) {
		return userDao.consume(id, consume) == 1;
	}
}
