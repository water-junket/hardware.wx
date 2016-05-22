package site.hardware.wx.service;

import java.util.Hashtable;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.hardware.wx.bean.Manager;
import site.hardware.wx.dao.ManagerDao;

@Service
public class ManagerService {
	@Autowired
	private ManagerDao managerDao;

	private Hashtable<Integer, Manager> manager = new Hashtable<Integer, Manager>();

	public String login(Manager m){
		m = managerDao.login(m);
		if (m == null){
			return "fail";
		}else{
			String token=UUID.randomUUID().toString();
			m.setToken(token);
			managerDao.token(m);
			manager.put(m.getId(), m);
			return token+"||"+m.getId();
		}
	}

	public boolean permission(Manager m, int r){
		int p = 0;
		if (manager.containsKey(m.getId())) {//如已缓存
			Manager t = manager.get(m.getId());
			if (t.getToken().equals(m.getToken())) p = t.getPermission();//如token正确
			else return false;//如token错误
		}else{//未未缓存
			p = managerDao.permission(m);
			if (p == -1) return false;//如校验失败
			m.setPermission(p);
			manager.put(m.getId(), m);
		}
		r = 1 << r;
		return (r & p) == r;
	}
}
