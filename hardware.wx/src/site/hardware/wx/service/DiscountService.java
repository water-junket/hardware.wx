package site.hardware.wx.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.hardware.wx.bean.Discount;
import site.hardware.wx.dao.DiscountDao;

@Service
public class DiscountService {
	@Autowired
	private DiscountDao discountDao;

	/**
	 * @param d
	 * @return
	 * @see site.hardware.wx.dao.DiscountDao#insert(site.hardware.wx.bean.Discount)
	 */
	public boolean insert(Discount d) {
		return discountDao.insert(d) == 1;
	}

	/**
	 * @return
	 * @see site.hardware.wx.dao.DiscountDao#select()
	 */
	public List<Discount> select() {
		return discountDao.select();
	}

	/**
	 * @param id
	 * @return
	 * @see site.hardware.wx.dao.DiscountDao#delete(int)
	 */
	public boolean remove(int id) {
		return discountDao.delete(id) == 1;
	}

	/**
	 * @param d
	 * @return
	 * @see site.hardware.wx.dao.DiscountDao#update(site.hardware.wx.bean.Discount)
	 */
	public boolean edit(Discount d) {
		return discountDao.update(d) == 1;
	}
}
