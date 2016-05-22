package site.hardware.wx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import site.hardware.wx.bean.Manager;
import site.hardware.wx.bean.Odata;
import site.hardware.wx.service.ManagerService;
import site.hardware.wx.service.OdataService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private ManagerService managerService;

	@Autowired
	private OdataService odataService;

	@InitBinder("m")  
	public void initBinder1(WebDataBinder binder) {  
		binder.setFieldDefaultPrefix("m.");  
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

}
