package site.hardware.wx.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import site.hardware.wx.bean.Odata;
import site.hardware.wx.service.GoodsService;
import site.hardware.wx.service.OdataService;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	@Autowired
	private OdataService odataService;

	@Autowired
	private GoodsService goodsService;

	@RequestMapping(value="/category", method = RequestMethod.POST)
	@ResponseBody
	public List<Odata> category(@RequestParam(value = "parent", required = false, defaultValue = "-1") int parent){
		return odataService.category(parent);
	}

	@RequestMapping(value="/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> goods(@RequestParam("category") int category, @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "step", required = false, defaultValue = "8") int step){
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("list", goodsService.selectAvailable(category, page, step));
		hm.put("pages", goodsService.countPages(category, step));
		return hm;
	}

}
