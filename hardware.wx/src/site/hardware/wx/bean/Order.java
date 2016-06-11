package site.hardware.wx.bean;

import java.util.Date;

public class Order {
	private String id;//yyyymmdd+4位数字
	private int uid;
	private String receive;//收货信息
	private String detail;//商品详情
	private String note;//用户备注
	private int price;//总价
	private int payMethod;//5货到付款/0微信支付
	private Date orderTime;//下单时间
	private Date handleTime;//处理时间
	private Date endTime;//收货时间
	private int status;//0已下单/5微信已支付/10已处理/20已收货/-1已取消
	private String annotation;//客服备注
	private boolean edit;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}
	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}
	/**
	 * @return the receive
	 */
	public String getReceive() {
		return receive;
	}
	/**
	 * @param receive the receive to set
	 */
	public void setReceive(String receive) {
		this.receive = receive;
	}
	/**
	 * @param receive the receive to set
	 */
	public void setReceive(Receiver receive) {
		setReceive(receive.toString());
	}
	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}
	/**
	 * @param detail the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
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
	 * @return the payMethod
	 */
	public int getPayMethod() {
		return payMethod;
	}
	/**
	 * @param payMethod the payMethod to set
	 */
	public void setPayMethod(int payMethod) {
		this.payMethod = payMethod;
	}
	/**
	 * @return the orderTime
	 */
	public Date getOrderTime() {
		return orderTime;
	}
	/**
	 * @param orderTime the orderTime to set
	 */
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	/**
	 * @return the handleTime
	 */
	public Date getHandleTime() {
		return handleTime;
	}
	/**
	 * @param handleTime the handleTime to set
	 */
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	/**
	 * @return the annotation
	 */
	public String getAnnotation() {
		return annotation;
	}
	/**
	 * @param annotation the annotation to set
	 */
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	/**
	 * @return the edit
	 */
	public boolean isEdit() {
		return edit;
	}
	/**
	 * @param edit the edit to set
	 */
	public void setEdit(boolean edit) {
		this.edit = edit;
	}
}
