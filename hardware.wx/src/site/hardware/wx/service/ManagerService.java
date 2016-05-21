package site.hardware.wx.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.hardware.wx.bean.Manager;
import site.hardware.wx.dao.ManagerDao;

@Service
public class ManagerService {
	@Autowired
	private ManagerDao managerDao;

	public String login(Manager m){
		String token=UUID.randomUUID().toString();
		m.md5().setToken(token);
		if (managerDao.token(m) == 1) return token;
		else return "fail";
	}
}
