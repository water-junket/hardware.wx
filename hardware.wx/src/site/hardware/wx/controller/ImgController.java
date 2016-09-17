package site.hardware.wx.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import site.hardware.wx.bean.Img;
import site.hardware.wx.bean.Manager;
import site.hardware.wx.service.GoodsService;
import site.hardware.wx.service.ImgService;
import site.hardware.wx.service.ManagerService;

@Controller
@RequestMapping("/img")
public class ImgController {
	@InitBinder("m")  
	public void initBinder1(WebDataBinder binder) {  
		binder.setFieldDefaultPrefix("m.");  
	}

	@Autowired
	private ManagerService managerService;

	@Autowired
	private ImgService imgService;

	@Autowired
	private GoodsService goodsService;

	@RequestMapping(value="/download", method = RequestMethod.GET)
	public void download(@RequestParam("id") int id, @RequestParam(value="type", required=false, defaultValue="") String type, HttpServletResponse response){
		String file = ImgService.getPath() + File.separator + "empty.jpg";
//		response.setContentType("image/png");
//		response.setHeader("Content-Disposition", "attachment;filename=404.png");
		if(type.equals("")){
			Img i = imgService.download(id);
			response.setContentType(i.getCtype());
			response.setHeader("Content-Disposition", "attachment;filename=" + i.getOname());
			file = ImgService.getPath() + File.separator + i.getActualName();
		}else{
			file = ImgService.getPath() + File.separator + type + id;
			response.setContentType(goodsService.subCtype(id));
			response.setHeader("Content-Disposition", "attachment;filename=" + type + id + ".jpg");
		}
		FileInputStream fis = null;
		OutputStream os = null;
		try{
			fis = new FileInputStream(file);
			os = response.getOutputStream();
			byte[] byteTemp = new byte[1024];
			while(fis.read(byteTemp) != -1){
				int size =byteTemp.length;
				os.write(byteTemp,0,size);
			}
			os.flush();
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}finally{
			try{
				if (os != null) {
					os.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@RequestMapping(value="/upload", method = RequestMethod.POST)
	@ResponseBody
	public boolean upload(@ModelAttribute("m") Manager m, @RequestParam(value = "imgFile", required = false) MultipartFile imgFile, @RequestParam("gid") int gid, @RequestParam("type") int type){
		if (managerService.permission(m, 0) && !imgFile.isEmpty()) return imgService.upload(imgFile, gid, type);
		else return false;
	}

	@RequestMapping(value="/upload1", method = RequestMethod.POST)
	@ResponseBody
	public boolean upload1(@ModelAttribute("m") Manager m, @RequestParam(value = "imgFile", required = false) MultipartFile imgFile, @RequestParam("sid") int sid){
		if (managerService.permission(m, 0) && !imgFile.isEmpty()) return imgService.upload1(imgFile, sid, m.getId());
		else return false;
	}

	@RequestMapping(value="/remove", method = RequestMethod.POST)
	@ResponseBody
	public boolean remove(@ModelAttribute("m") Manager m, @RequestParam("iid") int iid, @RequestParam("aname") String aname){
		if (managerService.permission(m, 0)) return imgService.remove(iid, aname);
		else return false;
	}

	@RequestMapping(value="/remove1", method = RequestMethod.POST)
	@ResponseBody
	public boolean remove1(@ModelAttribute("m") Manager m, @RequestParam("sid") int sid){
		if (managerService.permission(m, 0)) return imgService.remove1(sid, m.getId());
		else return false;
	}

	@RequestMapping(value="/list", method = RequestMethod.POST)
	@ResponseBody
	public List<Img> list(@RequestParam("gid") int gid){
		return imgService.list(gid);
	}

}
