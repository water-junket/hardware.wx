package site.hardware.wx.controller;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import site.hardware.wx.WxUtil;
import site.hardware.wx.bean.User;
import site.hardware.wx.bean.Wx;
import site.hardware.wx.service.UserService;
import site.hardware.wx.service.WxService;

@Controller
@RequestMapping("/wx")
public class WxController {
	@Autowired
	private UserService userService;

	@RequestMapping(value="/init", method = RequestMethod.GET)
	@ResponseBody
	public String doGet(Wx token, @RequestParam("echostr") String echostr){
		if (WxUtil.checkSignature(token)) {
			return echostr;
		}
		return "error";
	}

	@RequestMapping(value="/login", method = RequestMethod.POST)
	@ResponseBody
	public User login(@RequestParam("code") String code){
		StringBuilder sb = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/access_token?appid=").append(WxService.APPID).append("&secret=").append(WxService.APPSECRET).append("&code=").append(code).append("&grant_type=authorization_code");
		JSONObject jsonObject = WxService.request(sb.toString(), "GET", null);
		try {
			String openid = jsonObject.getString("openid");
			User u = userService.login(openid);
			if(u == null){
				String access_token = jsonObject.getString("access_token");
				sb = new StringBuilder("https://api.weixin.qq.com/sns/userinfo?access_token=").append(access_token).append("&openid=").append(openid).append("&lang=zh_CN");
				jsonObject = WxService.request(sb.toString(), "GET", null);
				String nickname = jsonObject.getString("nickname");
				u = userService.reg(openid, nickname);
			}
			return u;
		} catch (JSONException e) {
			try {
				System.out.println("+++++++++++++jsonerror++++++++++");
				System.out.println(jsonObject.getString("errcode")+":"+jsonObject.getString("errmsg"));
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
}
