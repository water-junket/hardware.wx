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

	public List<Goods> select(int c2, int page, int step){
		return goodsDao.select(c2, page * step - step + 1, page * step);
	}

	public boolean insert(Goods g, int mid){
		g.setLastBy(mid);
		return goodsDao.insert(g) == 1;
	}

	public int countPages(int c2, int step){
		int count = goodsDao.count(c2);
		return count / step + (count % step > 0 ? 1 : 0);
	}
}
