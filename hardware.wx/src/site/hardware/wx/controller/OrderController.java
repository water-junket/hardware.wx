package site.hardware.wx.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import site.hardware.wx.bean.Manager;
import site.hardware.wx.bean.Order;
import site.hardware.wx.bean.Receiver;
import site.hardware.wx.service.OrderService;
import site.hardware.wx.service.ReceiverService;
import site.hardware.wx.service.UserService;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private ReceiverService receiverService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@RequestMapping(value="/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Order o, Receiver r, @RequestParam("rid") int rid, @RequestParam("openid") String openid){
		HashMap<String, String> hm = new HashMap<String, String>();
		if(userService.permission(r.getUid(), openid)){
			if(rid == 0) receiverService.add(r);
			String result = orderService.add(o, r);
			if(!result.equals("fail") && o.getPayMethod() == 0){
				
			}
			hm.put("result", result);
		}else hm.put("result", "fail");
		return hm;
	}

	@RequestMapping(value="/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listOrder(@ModelAttribute("m") Manager m, @RequestParam("uid") int uid, @RequestParam("openid") String openid, @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "step", required = false, defaultValue = "20") int step){
		HashMap<String, Object> hm = new HashMap<String, Object>();
		if(userService.permission(uid, openid)){
			hm.put("list", orderService.list(uid, page, step));
			hm.put("pages", orderService.countPages(uid, step));
			hm.put("result", true);
		}else hm.put("result", false);
		return hm;
	}
}
