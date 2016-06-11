package site.hardware.wx.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.hardware.wx.bean.Order;
import site.hardware.wx.bean.OrderSearch;
import site.hardware.wx.bean.Receiver;
import site.hardware.wx.dao.OrderDao;

@Service
public class OrderService {
	@Autowired
	private OrderDao orderDao;

	private String lastDay;
	private int lastNum = 0;

	public String add(Order o, Receiver r){
		o.setReceive(r);
		o.setId(getId());
		return orderDao.insert(o) == 1 ? o.getId() : "fail";
	}

	private String getId(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String today = format.format(new Date());
		if(lastDay == null){
			String temp = orderDao.newest();
			if(temp == null || !temp.substring(0, 8).equals(today)){
				lastDay = today;
				lastNum = 0;
			}else{
				lastDay = temp.substring(0, 8);
				lastNum = Integer.parseInt(temp.substring(8)) + 1;
			}
		}else if(!lastDay.equals(today)){
			lastDay = today;
			lastNum = 0;
		}
		return lastDay + String.format("%04d", lastNum++);
	}

	public List<Order> list(OrderSearch os, int page, int step){
		return orderDao.select(os.toString(), page * step - step + 1, page * step);
	}

	public List<Order> list(int uid, int page, int step){
		return orderDao.select(uid, page * step - step + 1, page * step);
	}

	public int countPages(OrderSearch os, int step){
		int count = orderDao.count(os.toString());
		return count / step + (count % step > 0 ? 1 : 0);
	}

	public int countPages(int uid, int step){
		int count = orderDao.count(uid);
		return count / step + (count % step > 0 ? 1 : 0);
	}

	public boolean status(Order o, int mid) {
		return orderDao.status(o.getStatus(), o.getId(), mid) == 1;
	}

	public boolean annotation(Order o, int mid) {
		return orderDao.annotation(o.getAnnotation(), o.getId(), mid) == 1;
	}
}
