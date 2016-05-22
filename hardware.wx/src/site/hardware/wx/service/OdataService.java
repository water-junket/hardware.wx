package site.hardware.wx.service;

import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.hardware.wx.bean.Odata;
import site.hardware.wx.dao.OdataDao;

@Service
public class OdataService {
	@Autowired
	private OdataDao odataDao;

	private Hashtable<Integer, List<Odata>> category = new Hashtable<Integer, List<Odata>>();

	public List<Odata> category(int p){
		List<Odata> l = category.get(p);
		if (l == null) {
			if (p == -1) l = odataDao.select("category");
			else l = odataDao.select(p);
			category.put(p, l);
		}
		return l;
	}

	public boolean addCategory(String t, int p){
		Odata n = new Odata();
		n.setCategory("category");
		n.setName(t);
		n.setParent(p);
		boolean flag = odataDao.insert(n) == 1;
		if (flag) category.remove(p);
		return flag;
	}

	public boolean saveCategory(Odata o){
		boolean flag = odataDao.update(o) == 1;
		if (flag) category.remove(o.getParent());
		return flag;
	}

	public boolean statusCategory(Odata o){
		boolean flag = odataDao.status(o.getId()) == 1;
		if (flag) category.remove(o.getParent());
		return flag;
	}
}
