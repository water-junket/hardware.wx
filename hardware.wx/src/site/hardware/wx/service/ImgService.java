package site.hardware.wx.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import site.hardware.wx.bean.Img;
import site.hardware.wx.dao.ImgDao;
import site.hardware.wx.dao.SubGoodsDao;

@Service
public class ImgService {
	@Autowired
	private ImgDao imgDao;

	@Autowired
	private SubGoodsDao subGoodsDao;

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

	public boolean upload(MultipartFile file, int gid, int type){
		Img i = new Img();
		i.setGid(gid);
		i.setCtype(file.getContentType());
		i.setOname(file.getOriginalFilename());
		i.setType(type);
		i.setId(imgDao.insert(i));
		if (i.getId()==0) return false;
		String[] l = i.getOname().split("\\.");
		File temp = new File(path, i.getId() + "." + l[l.length-1]);
		try {
			file.transferTo(temp);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			imgDao.delete(i.getId());
			return false;
		}
	}

	public boolean upload1(MultipartFile file, int id, int mid){
		if (subGoodsDao.img(id, file.getContentType(), mid) == 0) return false;
		File temp = new File(path, "sub" + id);
		try {
			file.transferTo(temp);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			subGoodsDao.delImg(id, mid);
			return false;
		}
	}

	public boolean remove(int id, String aname){
		File temp = new File(path, aname);
		if (!temp.exists()) return false;
		if (imgDao.delete(id) == 0 ) return false;
		return temp.delete();
	}

	public boolean remove1(int id, int mid){
		File temp = new File(path, "sub" + id);
		if (!temp.exists()) return false;
		if (subGoodsDao.delImg(id,mid) == 0 ) return false;
		return temp.delete();
	}

	public Img download(int id){
		return imgDao.select(id);
	}

	public List<Img> list(int gid){
		return imgDao.selectList(gid);
	}
}
