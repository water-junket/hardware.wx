package site.hardware.wx.controller;

import java.util.HashMap;
import java.util.List;
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

import site.hardware.wx.bean.Discount;
import site.hardware.wx.bean.Goods;
import site.hardware.wx.bean.Manager;
import site.hardware.wx.bean.Odata;
import site.hardware.wx.bean.Order;
import site.hardware.wx.bean.OrderSearch;
import site.hardware.wx.bean.SubGoods;
import site.hardware.wx.service.DiscountService;
import site.hardware.wx.service.GoodsService;
import site.hardware.wx.service.ManagerService;
import site.hardware.wx.service.OdataService;
import site.hardware.wx.service.OrderService;
import site.hardware.wx.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private ManagerService managerService;

	@Autowired
	private OdataService odataService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private UserService userService;

	@Autowired
	private DiscountService discountService;

	@InitBinder("m")
	public void initBinder1(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("m.");
	}

	@InitBinder("o")
	public void initBinder2(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("o.");
	}

	@InitBinder("g")
	public void initBinder3(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("g.");
	}

	@InitBinder("r")
	public void initBinder4(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("r.");
	}

	@InitBinder("s")
	public void initBinder5(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("s.");
	}

	@InitBinder("d")
	public void initBinder6(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("d.");
	}

	@RequestMapping(value="/login", method = RequestMethod.POST)
	@ResponseBody
	public String login(@ModelAttribute("m") Manager m){
		return managerService.login(m.md5());
	}

	@RequestMapping(value="/category", method = RequestMethod.POST)
	@ResponseBody
	public List<Odata> category(@ModelAttribute("m") Manager m, @RequestParam(value = "parent", required = false, defaultValue = "-1") int parent){
		if (managerService.permission(m, 0)) return odataService.category(parent);
		else return null;
	}

	@RequestMapping(value="/addCategory", method = RequestMethod.POST)
	@ResponseBody
	public boolean addCategory(@ModelAttribute("m") Manager m, @RequestParam(value = "parent", required = false, defaultValue = "-1") int parent, @RequestParam("title") String title){
		if (managerService.permission(m, 0)) return odataService.addCategory(title, parent);
		else return false;
	}

	@RequestMapping(value="/saveCategory", method = RequestMethod.POST)
	@ResponseBody
	public boolean saveCategory(@ModelAttribute("m") Manager m, @ModelAttribute("o") Odata o){
		if (managerService.permission(m, 0)) return odataService.saveCategory(o);
		else return false;
	}

	@RequestMapping(value="/statusCategory", method = RequestMethod.POST)
	@ResponseBody
	public boolean statusCategory(@ModelAttribute("m") Manager m, @ModelAttribute("o") Odata o){
		if (managerService.permission(m, 0)) return odataService.statusCategory(o);
		else return false;
	}

	@RequestMapping(value="/statusSub", method = RequestMethod.POST)
	@ResponseBody
	public boolean statusSub(@ModelAttribute("m") Manager m, @ModelAttribute("s") SubGoods s){
		if (managerService.permission(m, 0)) return goodsService.statusSub(s, m.getId());
		else return false;
	}

	@RequestMapping(value="/addGoods", method = RequestMethod.POST)
	@ResponseBody
	public boolean addGoods(@ModelAttribute("m") Manager m, @ModelAttribute("g") Goods g){
		if (managerService.permission(m, 0)) return goodsService.add(g, m.getId());
		else return false;
	}

	@RequestMapping(value="/addSub", method = RequestMethod.POST)
	@ResponseBody
	public boolean addSub(@ModelAttribute("m") Manager m, @ModelAttribute("s") SubGoods s){
		if (managerService.permission(m, 0)) return goodsService.addSub(s);
		else return false;
	}

	@RequestMapping(value="/editGoods", method = RequestMethod.POST)
	@ResponseBody
	public boolean editGoods(@ModelAttribute("m") Manager m, @ModelAttribute("g") Goods g){
		if (managerService.permission(m, 0)) return goodsService.edit(g, m.getId());
		else return false;
	}

	@RequestMapping(value="/editSub", method = RequestMethod.POST)
	@ResponseBody
	public boolean editSub(@ModelAttribute("m") Manager m, @ModelAttribute("s") SubGoods s){
		if (managerService.permission(m, 0)) return goodsService.editSub(s, m.getId());
		else return false;
	}

	@RequestMapping(value="/listGoods", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listGoods(@ModelAttribute("m") Manager m, @RequestParam("category") int category, @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "step", required = false, defaultValue = "20") int step){
		if (!managerService.permission(m, 0)) return null;
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("list", goodsService.list(category, page, step));
		hm.put("pages", goodsService.countPages(category, step));
		return hm;
	}

	@RequestMapping(value="/listSub", method = RequestMethod.POST)
	@ResponseBody
	public List<SubGoods> listSub(@ModelAttribute("m") Manager m, @RequestParam("gid") int gid){
		if (!managerService.permission(m, 0)) return null;
		return goodsService.listSub(gid);
	}

	@RequestMapping(value="/listOrder", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listOrder(OrderSearch os, @ModelAttribute("m") Manager m, @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "step", required = false, defaultValue = "20") int step){
		if (!managerService.permission(m, 0)) return null;
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("list", orderService.list(os, page, step));
		hm.put("pages", orderService.countPages(os, step));
		return hm;
	}

	@RequestMapping(value="/statusOrder", method = RequestMethod.POST)
	@ResponseBody
	public boolean statusOrder(@ModelAttribute("m") Manager m, @ModelAttribute("r") Order r){
		if (managerService.permission(m, 0)){
			boolean flag=orderService.status(r, m.getId());
			if (flag){
				if (r.getStatus() == 20){
					goodsService.sales(r.getDetail(), 1);
					userService.consume(r.getUid(), r.getPrice());
				}else if(r.getStatus() == -2){
					goodsService.sales(r.getDetail(), -1);
					userService.consume(r.getUid(), -r.getPrice());
				}
			}
			return flag;
		}
		else return false;
	}

	@RequestMapping(value="/noteOrder", method = RequestMethod.POST)
	@ResponseBody
	public boolean noteOrder(@ModelAttribute("m") Manager m, @ModelAttribute("r") Order r){
		if (managerService.permission(m, 0)) return orderService.annotation(r, m.getId());
		else return false;
	}

	@RequestMapping(value="/listUser", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> listUser(@ModelAttribute("m") Manager m, @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "step", required = false, defaultValue = "20") int step){
		if (!managerService.permission(m, 0)) return null;
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("list", userService.list(page, step));
		hm.put("pages", userService.countPages(step));
		return hm;
	}

	@RequestMapping(value="/minusPoint", method = RequestMethod.POST)
	@ResponseBody
	public boolean minusPoint(@ModelAttribute("m") Manager m, @RequestParam("uid") int uid, @RequestParam("point") int point){
		if (managerService.permission(m, 0)) return userService.point(uid, point);
		else return false;
	}

	@RequestMapping(value="/listDiscount", method = RequestMethod.POST)
	@ResponseBody
	public List<Discount> listDiscount(@ModelAttribute("m") Manager m){
		if (!managerService.permission(m, 0)) return null;
		return discountService.select();
	}

	@RequestMapping(value="/addDiscount", method = RequestMethod.POST)
	@ResponseBody
	public boolean addDiscount(@ModelAttribute("m") Manager m, @ModelAttribute("d") Discount d){
		if (managerService.permission(m, 0)) return discountService.insert(d);
		else return false;
	}

	@RequestMapping(value="/editDiscount", method = RequestMethod.POST)
	@ResponseBody
	public boolean editDiscount(@ModelAttribute("m") Manager m, @ModelAttribute("d") Discount d){
		if (managerService.permission(m, 0)) return discountService.edit(d);
		else return false;
	}

	@RequestMapping(value="/removeDiscount", method = RequestMethod.POST)
	@ResponseBody
	public boolean removeDiscount(@ModelAttribute("m") Manager m, @RequestParam("did") int did){
		if (managerService.permission(m, 0)) return discountService.remove(did);
		else return false;
	}

	@RequestMapping(value="/resetPw", method = RequestMethod.POST)
	@ResponseBody
	public boolean resetPw(@ModelAttribute("m") Manager m, @RequestParam("uid") int uid, @RequestParam("tel") String tel){
		if (managerService.permission(m, 0)) return userService.reset(uid, tel);
		else return false;
	}
}
