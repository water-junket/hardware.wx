package site.hardware.wx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import site.hardware.wx.bean.Receiver;
import site.hardware.wx.bean.User;
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
	public List<Receiver> list(User u) {
		if(userService.permission(u)) return receiverService.list(u.getId());
		else return null;
	}

	@RequestMapping(value="/remove", method = RequestMethod.POST)
	@ResponseBody
	public boolean remove(User u, @RequestParam("rid") int rid) {
		if(userService.permission(u)) return receiverService.remove(rid);
		else return false;
	}
}
