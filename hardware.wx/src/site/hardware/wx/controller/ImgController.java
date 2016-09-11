package site.hardware.wx.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@RequestMapping(value="/download", method = RequestMethod.GET)
	public void download(@RequestParam("id") int id, HttpServletResponse response){
		Img i = imgService.download(id);
		response.setContentType(i.getCtype());
		response.setHeader("Content-Disposition", "attachment;filename=" + i.getOname());
		String file = ImgService.getPath() + File.separator + i.getActualName();
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

	@RequestMapping(value="/remove", method = RequestMethod.POST)
	@ResponseBody
	public boolean remove(@ModelAttribute("m") Manager m, @RequestParam("iid") int iid, @RequestParam("aname") String aname){
		if (managerService.permission(m, 0)) return imgService.remove(iid, aname);
		else return false;
	}

	@RequestMapping(value="/list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> list(@RequestParam("gid") int gid){
		List<Img> l = imgService.list(gid);
		HashMap<String,Object> hm = new HashMap<String, Object>();
		if(l.size()>0 && l.get(0).getType()==1){
			hm.put("title", l.get(0));
			hm.put("normal", l.subList(1, l.size()));
		}else hm.put("normal", l);
		return hm;
	}

}
