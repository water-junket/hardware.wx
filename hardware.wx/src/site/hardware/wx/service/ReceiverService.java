package site.hardware.wx.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.hardware.wx.bean.Receiver;
import site.hardware.wx.dao.ReceiverDao;

@Service
public class ReceiverService {
	@Autowired
	private ReceiverDao receiverDao;

	public List<Receiver> list(int uid) {
		return receiverDao.select(uid);
	}

	public boolean add(Receiver r) {
		return receiverDao.insert(r) == 1;
	}

	public boolean remove(int id) {
		return receiverDao.delete(id) == 1;
	}
}
