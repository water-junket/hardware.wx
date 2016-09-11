package site.hardware.wx.bean;

import java.util.Date;

public class Img {
	private int id;
	private int gid;
	private String oname;
	private String ctype;
	private int type;//0轮播图，1列表标题图，2介绍图
	private Date lastDate;
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
	 * @return the gid
	 */
	public int getGid() {
		return gid;
	}
	/**
	 * @param gid the gid to set
	 */
	public void setGid(int gid) {
		this.gid = gid;
	}
	/**
	 * @return the oname
	 */
	public String getOname() {
		return oname;
	}
	/**
	 * @param oname the oname to set
	 */
	public void setOname(String oname) {
		this.oname = oname;
	}
	/**
	 * @return the ctype
	 */
	public String getCtype() {
		return ctype;
	}
	/**
	 * @param ctype the ctype to set
	 */
	public void setCtype(String ctype) {
		this.ctype = ctype;
	}
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the lastDate
	 */
	public Date getLastDate() {
		return lastDate;
	}
	/**
	 * @param lastDate the lastDate to set
	 */
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public String getActualName(){
		String[] l = getOname().split("\\.");
		return id + "." + l[l.length-1];
	}
}
