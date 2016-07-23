package site.hardware.wx.bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
	private int id;
	private int point;
	private String name;
	private String pw;
	private String token;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the point
	 */
	public int getPoint() {
		return point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(int point) {
		this.point = point;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the pw
	 */
	public String getPw() {
		return pw;
	}
	/**
	 * @param pw the pw to set
	 */
	public void setPw(String pw) {
		this.pw = pw;
	}
	public User md5(){
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] b = md.digest((name+"siren"+pw).getBytes());
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < b.length; i++) {
				if ((b[i] & 0xff) < 0x10) {
					sb.append("0");
				}
				sb.append(Long.toString(b[i] & 0xff, 16));
			}
			pw = sb.toString();
		}catch(NoSuchAlgorithmException e){
			System.out.println("md5加密算法当前不可用");
		}
		return this;
	}
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
}
