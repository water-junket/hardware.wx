package site.hardware.wx.bean;

public class Wx {
	public static final String TOKEN = "wx32167hl";
	private String signature;
	private String timestamp;
	private String nonce;
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String toString(){
		StringBuilder sb = new StringBuilder("--------------token-------------------\r\n");
		sb.append("signature:").append(signature);
		sb.append("timestamp:").append(timestamp);
		sb.append("nonce:").append(nonce);
		return sb.toString();
	}
}
