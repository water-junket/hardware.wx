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

import site.hardware.wx.bean.Goods;
import site.hardware.wx.bean.Manager;
import site.hardware.wx.bean.Odata;
import site.hardware.wx.service.GoodsService;
import site.hardware.wx.service.ManagerService;
import site.hardware.wx.service.OdataService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private ManagerService managerService;

	@Autowired
	private OdataService odataService;

	@Autowired
	private GoodsService goodsService;

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

	@RequestMapping(value="/goods", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> goods(@ModelAttribute("m") Manager m, @RequestParam("category") int category, @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "step", required = false, defaultValue = "15") int step){
		if (managerService.permission(m, 0)){
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("list", goodsService.select(category, page, step));
			hm.put("pages", goodsService.countPages(category, step));
			return hm;
		}
		else return null;
	}

	@RequestMapping(value="/addGoods", method = RequestMethod.POST)
	@ResponseBody
	public boolean addGoods(@ModelAttribute("m") Manager m, @ModelAttribute("g") Goods g){
		if (managerService.permission(m, 0)) return goodsService.insert(g, m.getId());
		else return false;
	}
}
