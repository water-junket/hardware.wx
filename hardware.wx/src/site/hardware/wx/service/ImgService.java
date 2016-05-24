package site.hardware.wx.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import site.hardware.wx.bean.Img;
import site.hardware.wx.dao.ImgDao;

@Service
public class ImgService {
	@Autowired
	private ImgDao imgDao;

	/**
	 * 文件路径，用配置文件注入
	 */
	private static String path;

	/**
	 * @return the path
	 */
	public static String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public static void setPath(String path) {
		ImgService.path = path;
	}

	public boolean upload(MultipartFile file, int gid, boolean isTitle){
		Img i = new Img();
		i.setGid(gid);
		i.setCtype(file.getContentType());
		i.setOname(file.getOriginalFilename());
		i.setTitle(isTitle);
		i.setId(imgDao.insert(i));
		if (i.getId()==0) return false;
		String[] l = i.getOname().split(File.separator);
		File temp = new File(path, i.getId() + File.separator + l[l.length-1]);
		try {
			file.transferTo(temp);
			return true;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			imgDao.delete(i.getId());
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			imgDao.delete(i.getId());
			return false;
		}
	}

	public Img download(int id){
		return imgDao.select(id);
	}

	public List<Img> list(int gid){
		return imgDao.selectList(gid);
	}
}
