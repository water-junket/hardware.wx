package site.hardware.wx.bean;

import java.util.Date;

public class Goods {
	private int id;
	private String name;
	private Date lastTime;
	private int lastBy;
	private int category1;
	private int category2;
	private int price;
	private int dummyPrice;
	private boolean status;//0下架/1上架
	private String param;
	private String note;
	private int sales;
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
	 * @return the lastTime
	 */
	public Date getLastTime() {
		return lastTime;
	}
	/**
	 * @param lastTime the lastTime to set
	 */
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	/**
	 * @return the lastBy
	 */
	public int getLastBy() {
		return lastBy;
	}
	/**
	 * @param lastBy the lastBy to set
	 */
	public void setLastBy(int lastBy) {
		this.lastBy = lastBy;
	}
	/**
	 * @return the category1
	 */
	public int getCategory1() {
		return category1;
	}
	/**
	 * @param category1 the category1 to set
	 */
	public void setCategory1(int category1) {
		this.category1 = category1;
	}
	/**
	 * @return the category2
	 */
	public int getCategory2() {
		return category2;
	}
	/**
	 * @param category2 the category2 to set
	 */
	public void setCategory2(int category2) {
		this.category2 = category2;
	}
	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}
	/**
	 * @return the dummyPrice
	 */
	public int getDummyPrice() {
		return dummyPrice;
	}
	/**
	 * @param dummyPrice the dummyPrice to set
	 */
	public void setDummyPrice(int dummyPrice) {
		this.dummyPrice = dummyPrice;
	}
	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		this.status = status;
	}
	/**
	 * @return the param
	 */
	public String getParam() {
		return param;
	}
	/**
	 * @param param the param to set
	 */
	public void setParam(String param) {
		this.param = param;
	}
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * @return the sales
	 */
	public int getSales() {
		return sales;
	}
	/**
	 * @param sales the sales to set
	 */
	public void setSales(int sales) {
		this.sales = sales;
	}
}
