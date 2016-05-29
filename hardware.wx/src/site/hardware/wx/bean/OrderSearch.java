package site.hardware.wx.bean;

public class OrderSearch {
	private int type;
	private String oid;
	private int payMethod;
	private String begin;
	private String end;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public int getPayMethod() {
		return payMethod;
	}
	public void setPayMethod(int payMethod) {
		this.payMethod = payMethod;
	}
	public String getBegin() {
		return begin;
	}
	public void setBegin(String begin) {
		this.begin = begin;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		if(type==0) sb.append(" and payMethod+status<5");
		else if(type==1) sb.append(" and payMethod+status=5");
		else if(type==2) sb.append(" and status=10");
		else if(type==3) sb.append(" and status=20");
		else if(type==4) sb.append(" and status=-1");
		if(oid!=null && !oid.equals("")) sb.append(" and id=\'").append(oid).append("\'");
		if(payMethod!=-1) sb.append(" and payMethod=").append(payMethod);
		if(begin!=null && !begin.equals("")) sb.append(" and orderTime>cast(\'").append(payMethod).append("\' as datetime)");
		if(end!=null && !end.equals("")) sb.append(" and orderTime<cast(\'").append(payMethod).append("\' as datetime)");
		if(sb.length()>0) sb.replace(1, 4, "where");
		return sb.toString();
	}
}
