package site.hardware.wx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import site.hardware.wx.bean.Receiver;
import site.hardware.wx.service.ReceiverService;
import site.hardware.wx.service.UserService;

@Controller
@RequestMapping("/rec")
public class ReceiverController {
	@Autowired
	private ReceiverService receiverService;

	@Autowired
	private UserService userService;

	@RequestMapping(value="/list", method = RequestMethod.POST)
	@ResponseBody
	public List<Receiver> list(@RequestParam("uid") int uid, @RequestParam("openid") String openid) {
		if(userService.permission(uid, openid)) return receiverService.list(uid);
		else return null;
	}

	@RequestMapping(value="/add", method = RequestMethod.POST)
	@ResponseBody
	public int add(Receiver r, @RequestParam("openid") String openid) {
		if(userService.permission(r.getUid(), openid)) return receiverService.add(r);
		else return 0;
	}

	@RequestMapping(value="/remove", method = RequestMethod.POST)
	@ResponseBody
	public boolean remove(int id, @RequestParam("uid") int uid, @RequestParam("openid") String openid) {
		if(userService.permission(uid, openid)) return receiverService.remove(id);
		else return false;
	}
}
