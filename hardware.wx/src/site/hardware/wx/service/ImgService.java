package site.hardware.wx.service;

import org.springframework.stereotype.Service;

@Service
public class ImgService {
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
}
