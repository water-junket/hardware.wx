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

import site.hardware.wx.bean.Goods;
import site.hardware.wx.bean.Odata;
import site.hardware.wx.bean.SubGoods;
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
		return odataService.categoryAvailable(parent);
	}

	@RequestMapping(value="/detail", method = RequestMethod.POST)
	@ResponseBody
	public Goods detail(@RequestParam("id") int id){
		return goodsService.select(id);
	}

	@RequestMapping(value="/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(@RequestParam(value = "ob", defaultValue = "name asc") String ob, @RequestParam("category") int category, @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "step", required = false, defaultValue = "8") int step){
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("list", goodsService.listAvailable(category, page, step, ob));
		hm.put("pages", goodsService.countAvailablePages(category, step));
		return hm;
	}

	@RequestMapping(value="/search", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> search(@RequestParam(value = "ob", defaultValue = "name asc") String ob, @RequestParam("category") int category, @RequestParam("name") String name, @RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "step", required = false, defaultValue = "8") int step){
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("list", goodsService.search(category, page, step, name, ob));
		hm.put("pages", goodsService.countPages(category, step, name));
		return hm;
	}

	@RequestMapping(value="/listSub", method = RequestMethod.POST)
	@ResponseBody
	public List<SubGoods> listSub(@RequestParam("parent") int parent){
		return goodsService.listSubAvailable(parent);
	}
}
