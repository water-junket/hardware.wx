package site.hardware.wx.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.hardware.wx.bean.Goods;
import site.hardware.wx.bean.SubGoods;
import site.hardware.wx.dao.GoodsDao;
import site.hardware.wx.dao.SubGoodsDao;

@Service
public class GoodsService {
	@Autowired
	private GoodsDao goodsDao;

	@Autowired
	private SubGoodsDao subGoodsDao;

	public List<Goods> list(int c, int page, int step){
		return goodsDao.select(c, page * step - step + 1, page * step);
	}

	public List<SubGoods> listSub(int gid){
		return subGoodsDao.select(gid);
	}

	public List<Goods> listAvailable(int c, int page, int step, String ob){
		return goodsDao.select(c, page * step - step + 1, page * step, 1, ob);
	}

	public List<SubGoods> listSubAvailable(int gid){
		return subGoodsDao.select(gid, true);
	}

	public List<Goods> search(int c, int page, int step, String name, String ob){
		return goodsDao.select(c, page * step - step + 1, page * step, 1, name, ob);
	}

	public Goods select(int id) {
		return goodsDao.select(id);
	}

	public boolean add(Goods g, int mid){
		g.setLastBy(mid);
		return goodsDao.insert(g) == 1;
	}

	public boolean addSub(SubGoods s){
		return subGoodsDao.insert(s) == 1;
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

	public boolean statusSub(SubGoods s, int mid){
		boolean flag = subGoodsDao.status(s.getId(), mid) == 1;
		if (flag){
			goodsDao.status(s.getGid(), mid);
			goodsDao.price(s.getGid());
		}
		return flag;
	}

	public boolean edit(Goods g, int mid){
		g.setLastBy(mid);
		return goodsDao.update(g) == 1;
	}

	public boolean editSub(SubGoods s, int mid){
		boolean flag = subGoodsDao.update(s) == 1;
		if (flag) goodsDao.price(s.getGid());
		return flag;
	}

	/**
	 * @param id
	 * @return
	 * @see site.hardware.wx.dao.SubGoodsDao#ctype(int)
	 */
	public String subCtype(int id) {
		return subGoodsDao.ctype(id);
	}

	public void sales(String detail, int ratio) {
		String[] list = detail.split("\\|\\|");
		for(String goods : list){
			String[] p = goods.split(",,");
			goodsDao.sales(Integer.parseInt(p[0]), ratio * Integer.parseInt(p[5]));
		}
	}
}
