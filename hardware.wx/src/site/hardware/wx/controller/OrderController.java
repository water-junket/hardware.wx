package site.hardware.wx.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import site.hardware.wx.bean.Order;
import site.hardware.wx.bean.Receiver;
import site.hardware.wx.bean.User;
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

	@InitBinder("u")
	public void initBinder1(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("u.");
	}

	@InitBinder("r")
	public void initBinder2(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("r.");
	}

	@RequestMapping(value="/add", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> add(Order o, @ModelAttribute("u") User u, @ModelAttribute("r") Receiver r){
		HashMap<String, String> hm = new HashMap<String, String>();
		if(userService.permission(u)){
			if(r.getId() == 0) receiverService.add(r, u);
			String result = orderService.add(o, u, r);
			if(!result.equals("fail") && o.getPayMethod() == 0){
				
			}
			hm.put("result", result);
		}else hm.put("result", "fail");
		return hm;
	}

	@RequestMapping(value="/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listOrder(@ModelAttribute("u") User u, @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "step", required = false, defaultValue = "20") int step){
		HashMap<String, Object> hm = new HashMap<String, Object>();
		if(userService.permission(u)){
			hm.put("list", orderService.list(u.getId(), page, step));
			hm.put("pages", orderService.countPages(u.getId(), step));
			hm.put("result", true);
		}else hm.put("result", false);
		return hm;
	}
}
