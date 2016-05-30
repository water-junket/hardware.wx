package site.hardware.wx.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.hardware.wx.bean.Goods;
import site.hardware.wx.dao.GoodsDao;

@Service
public class GoodsService {
	@Autowired
	private GoodsDao goodsDao;

	public List<Goods> list(int c, int page, int step){
		return goodsDao.select(c, page * step - step + 1, page * step);
	}

	public List<Goods> listAvailable(int c, int page, int step){
		return goodsDao.select(c, page * step - step + 1, page * step, 1);
	}

	public List<Goods> search(int c, int page, int step, String name){
		return goodsDao.select(c, page * step - step + 1, page * step, 1, name);
	}

	public Goods select(int id) {
		return goodsDao.select(id);
	}

	public boolean add(Goods g, int mid){
		g.setLastBy(mid);
		return goodsDao.insert(g) == 1;
	}

	public int countPages(int c, int step){
		int count = goodsDao.count(c);
		return count / step + (count % step > 0 ? 1 : 0);
	}

	public int countAvailablePages(int c, int step){
		int count = goodsDao.count(c, 1);
		return count / step + (count % step > 0 ? 1 : 0);
	}

	public int countPages(int c, int step, String name){
		int count = goodsDao.count(c, 1, name);
		return count / step + (count % step > 0 ? 1 : 0);
	}

	public boolean status(Goods g, int mid){
		return goodsDao.status(g.getId(), mid) == 1;
	}

	public boolean edit(Goods g, int mid){
		g.setLastBy(mid);
		return goodsDao.update(g) == 1;
	}
}
